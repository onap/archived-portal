/*-
 * ================================================================================
 * ECOMP Portal
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ================================================================================
 */

'use strict';
(function () {
    class NewAdminModalCtrl {
        constructor($log, adminsService, $scope, confirmBoxService, utilsService, $location) {

            let init = () => {
                this.isSaving = false;
                this.originalApps = [];
                /* istanbul ignore if */
                if ($scope.ngDialogData && $scope.ngDialogData.selectedUser && $scope.ngDialogData.dialogState) {
                    this.selectedUser = $scope.ngDialogData.selectedUser;
                    this.dialogState = $scope.ngDialogData.dialogState;
                    this.isShowBack = false;
                    if (this.dialogState === 2) {
                        this.getAdminAppsRoles();
                    }
                } else {
                    this.isShowBack = true;
                    this.selectedUser = null;
                    this.dialogState = 1;
                }

                //this.searchUsersInProgress = false;
                //this.showNewAdminAppDropdown = false;
                $log.info('NewAdminModalCtrl::initiated');
                this.appsOrder = [];
            };

            let orderList = (apps) => {
                this.appsOrder = [];
                for (var i = 0; i < apps.length; i++) {
                    if (apps[i].isAdmin) {
                        this.appsOrder.push(apps[i].id);
                    }
                }
            };

            this.orderFilter = app => {
                if (!app || !app.id || !this.appsOrder.length) {
                    return;
                }
                return this.appsOrder.indexOf(app.id);
            };

            /**
             * this function get the selected admin apps roles
             */
            this.getAdminAppsRoles = () => {
                if (!this.selectedUser || !this.selectedUser.orgUserId) {
                    $log.error('No user is selected / searchUsers is InProgress');
                    this.dialogState = 1;
                    return;
                }
                adminsService.getAdminAppsRoles(this.selectedUser.orgUserId).then(roles => {
                    $log.debug('apps roles res: ', JSON.stringify(roles));
                    if (!roles.appsRoles) {
                        return;
                    }

                    this.adminAppsRoles = [];
                    for (var i = 0; i < roles.appsRoles.length; i++) {
                        if (!roles.appsRoles[i].restrictedApp) {
                            $log.debug('pushing: {id: ', roles.appsRoles[i].id,
                                'name: ', roles.appsRoles[i].appName,
                                'restrictedApp: ', roles.appsRoles[i].restrictedApp,
                                'isAdmin: ', roles.appsRoles[i].isAdmin, '}');
                            this.adminAppsRoles.push({
                                id: roles.appsRoles[i].id,
                                appName: roles.appsRoles[i].appName,
                                isAdmin: roles.appsRoles[i].isAdmin,
                                restrictedApp: roles.appsRoles[i].restrictedApp
                            });
                        }
                    }
                    this.dialogState = 2;
                    this.adminAppsRoles = this.adminAppsRoles.sort(getSortOrder("appName"));

                    orderList(roles.appsRoles);
                    if (this.appsOrder != null) {
                        for (var j = 0; j < this.appsOrder.length; j++) {
                            this.originalApps.push(this.appsOrder[j]);
                        }
                    }
                }).catch(err => {
                    $log.error(err);
                });
            };

            // Refactor this into a directive
            let getSortOrder = (prop) => {
                return function (a, b) {
                    if (a[prop].toLowerCase() > b[prop].toLowerCase()) {
                        return 1;
                    } else if (a[prop].toLowerCase() < b[prop].toLowerCase()) {
                        return -1;
                    }
                    return 0;
                }
            }

            /**
             * this function set the selected user
             * @param user: selected user object
             */
            this.setSelectedUser = (user) => {
                $log.debug('selected user: ', user);
                this.selectedUser = user;
            };

            /**
             * Mark the user as not admin of the selected app
             * @param app: selected app object
             */
            this.unadminApp = (app) => {
                confirmBoxService.deleteItem(app.appName).then(confirmed => {
                    if (confirmed === true) {
                        app.isAdmin = false;
                        this.appsOrder.splice(this.appsOrder.indexOf(app.id), 1);
                    }
                }).catch(err => {
                    $log(err);
                });
            };

            /**
             * update the selected admin app with the new roles
             */
            this.updateAdminAppsRoles = () => {
                if (!this.selectedUser || !this.selectedUser.orgUserId || !this.adminAppsRoles) {
                    return;
                }
                this.isSaving = true;
                $log.debug('going to update user: ' + this.selectedUser.orgUserId + ' with app roles: ' + JSON.stringify(this.adminAppsRoles));
                confirmBoxService.makeAdminChanges('Are you sure you want to make these admin changes?')
                    .then(confirmed => {
                    	if(confirmed === true){
                        adminsService.updateAdminAppsRoles({
                                orgUserId: this.selectedUser.orgUserId,
                                appsRoles: this.adminAppsRoles
                            })
                            .then(res => {
                                $log.debug('Admin apps roles updated successfully!', res);
                                //close and resolve dialog promise with true (to update the table)
                                this.remindToAddUserIfNecessary();
                                $scope.closeThisDialog(true);
                            }).catch(err => {
                            $log.error('NewAdminModalCtrl.updateAdminAppsRoles:: Failed - ' + err);
                        }).finally(()=> {
                            this.isSaving = false;
                        })
                    	}else{
                        	this.isSaving = false;
                        }
                    });
            };

            /**
             * Navigate between dialog screens using step number: 1,2,...
             */
            this.navigateBack = () => {
                if (this.dialogState === 1) {
                    //back from 1st screen?
                }
                if (this.dialogState === 2) {
                    this.dialogState = 1;
                }
            };

            init();

            /**
             * each time new app is selected in the drop down,
             * add it to the user administrated apps list
             */
            $scope.$watch('newAdmin.selectedNewApp.value', (newVal) => {
                var newVal= JSON.parse(newVal);
                if (!newVal || newVal.isAdmin === undefined) {
                    return;
                }
                //newVal.isAdmin = true; - track by ruined this, here is the workaround:
                let app = _.find(this.adminAppsRoles, {id: newVal.id});
                if (app) {
                    app.isAdmin = true;
                    this.appsOrder.push(app.id);
                }
                this.selectedNewApp = null;
                //this.showNewAdminAppDropdown = false;
            });

            $scope.$on('$stateChangeStart', e => {
                //Disable navigation when modal is opened
                //**Nabil - note: this will cause the history back state to be replaced with current state
                e.preventDefault();
            });

            /**
             * If an Admin was added for an application remind the portal admin to add the admin as a user
             */
            this.remindToAddUserIfNecessary = () => {

                var adminAddedToNewApp = false;
                if ((this.originalApps != null) && (this.originalApps.length > 0)) {
                    for (var i = 0; i < this.appsOrder.length; i++) {
                        var foundApp = false;
                        for (var j = 0; j < this.originalApps.length; j++) {
                            if (this.originalApps[j] === this.appsOrder[i]) {
                                foundApp = true;
                            }
                        }
                        if (foundApp === false) {
                            adminAddedToNewApp = true;
                            break;
                        }
                    }
                } else {
                    adminAddedToNewApp = true;
                }

                if (adminAddedToNewApp === true) {
                    confirmBoxService.confirm('Add this person as an application user? This allows them to access the application from ECOMP Portal. Press OK to go to the Add Users page.')
                        .then(confirmed => {
                            if (confirmed === true) {
                                $location.path('/users');
                            }
                        });
                }
            }

        }
    }
    NewAdminModalCtrl.$inject = ['$log', 'adminsService', '$scope', 'confirmBoxService', 'utilsService', '$location'];
    angular.module('ecompApp').controller('NewAdminModalCtrl', NewAdminModalCtrl);
})();
