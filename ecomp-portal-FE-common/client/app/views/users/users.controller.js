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
    class UsersCtrl {
        constructor($log, applicationsService, usersService, confirmBoxService, $scope, ngDialog) {
            this.$log = $log;
            $scope.adminAppsIsNull = false;
            $scope.appsIsDown = false;
            $scope.noUsersInApp = false;
            $scope.multiAppAdmin = false;

            $log.info('UsersCtrl:: initializing...');
            /**
             * Handle all active HTTP requests
             * activeRequests @type {Array[requests with cancel option]}
             */
            let activeRequests = [];
            let clearReq = (req) => {
                activeRequests.splice(activeRequests.indexOf(req), 1);
            };

            let init = () => {
                this.isLoadingTable = false;
                this.selectedApp = null;
                this.isAppSelectDisabled = false;
                this.selectApp = 'Select application';
                this.adminApps = [{index: 0, id: 0, value: this.selectApp, title: this.selectApp}];
                getAdminApps();

                /*Table general configuration params*/
                this.searchString = '';
                /*Table data*/
                this.usersTableHeaders = ['First Name', 'Last Name', 'User ID', 'Roles'];
                this.accountUsers = [];
            };

            let getAdminApps = () => {
                $log.debug('UsersCtrl::getAdminApps: - Starting getAdminApps');
                try {
                    this.isLoadingTable = true;
                    let adminAppsReq = applicationsService.getAdminApps();
                    adminAppsReq.promise().then(apps => {
                        if (!apps || !apps.length) {
                            $log.error('UsersCtrl::getAdminApps:  - no apps found');
                            return null;
                        }
                        $log.debug('UsersCtrl::getAdminApps: Apps for this user are: ' + JSON.stringify(apps));
                        if (apps.length >= 2) {
                            $log.info('UsersCtrl::getAdminApps:  - more than one app for this admin:', apps.length, ' apps');
                            $scope.multiAppAdmin = true;
                        } else {
                            this.adminApps = [] ;
                        }
                        let sortedApps = apps.sort(getSortOrder("name"));
                        let realAppIndex = 1;
                        for(let i=1; i<=sortedApps.length; i++){
                            this.adminApps.push({
                                index: realAppIndex,
                                id: sortedApps[i - 1].id,
                                value: sortedApps[i - 1].name,
                                title: sortedApps[i - 1].name
                            });
                            realAppIndex = realAppIndex + 1;
                        }

                        $log.debug('UsersCtrl::getAdminApps: Apps for this user are: ' + JSON.stringify(this.adminApps));

                        this.selectedApp = this.adminApps[0];
                        clearReq(adminAppsReq);
                        $scope.adminAppsIsNull = false;
                        }).catch(e => {
                            $scope.adminAppsIsNull = true;
                            $log.error('UsersCtrl::getAdminApps:  - getAdminApps() failed = '+ e.message);
                            clearReq(adminAppsReq);
                            confirmBoxService.showInformation('There was a problem retrieving the applications. ' +
                                'Please try again later.').then(isConfirmed => {});

                    }).finally(() => {
                            this.isLoadingTable = false;
                        });
                    } catch (e) {
                        $scope.adminAppsIsNull = true;
                        $log.error('UsersCtrl::getAdminApps:  - getAdminApps() failed!');
                        this.isLoadingTable = false;
                    }
            };

            let getSortOrder = (prop) => {
                return function(a, b) {
                    if (a[prop] > b[prop]) {
                        return 1;
                    } else if (a[prop] < b[prop]) {
                        return -1;
                    }
                    return 0;
                }
            }

            this.updateUsersList = () => {
                $scope.appsIsDown = false;
                $scope.noUsersInApp = false;
                // $log.debug('UsersCtrl::updateUsersList: Starting updateUsersList');
                //reset search string
                this.searchString = '';
                //should i disable this too in case of moving between tabs?
                this.isAppSelectDisabled = true;
                //activate spinner
                this.isLoadingTable = true;
                
                if(this.adminApps!=null && this.selectedApp!=null){
                	 var tempSelected = null;
                	 for(let i=0; i<=this.adminApps.length; i++){
                     	if(typeof this.adminApps[i] != 'undefined' && this.selectedApp.value==this.adminApps[i].value){
                     		tempSelected=_.clone(this.adminApps[i]);
                     	}
                     }
                     if(tempSelected!=null){
                     	this.selectedApp= tempSelected;
                     }
                }
               
                if (this.selectedApp.title != this.selectApp) { // 'Select Application'
                    usersService.getAccountUsers(this.selectedApp.id)
                        .then(accountUsers => {
                            $log.debug('UsersCtrl::updateUsersList accountUsers: '+ accountUsers);
                            if (angular.isObject(accountUsers)===false) {
                                $log.error('UsersCtrl::updateUsersList accountUsers: App is down!');
                                $scope.appsIsDown = true;
                            }
                            $log.debug('UsersCtrl::updateUsersList length: '+ Object.keys(accountUsers).length);
                            this.isAppSelectDisabled = false;
                            this.accountUsers = accountUsers;
                            if (angular.isObject(accountUsers) && Object.keys(accountUsers).length === 0) {
                                $log.debug('UsersCtrl::updateUsersList accountUsers: App has no users.');
                                $scope.noUsersInApp = true;
                            }
                        }).catch(err => {
                            this.isAppSelectDisabled = false;
                            $log.error('UsersCtrl::updateUsersList error: ' + err);
                            confirmBoxService.showInformation('There was a problem updating the users List. ' +
                                'Please try again later.').then(isConfirmed => {});
                            $scope.appsIsDown = true;
                        }).finally(() => {
                            this.isLoadingTable = false;
                            $scope.noAppSelected = false;
                    });
                } else {
                    // this.selectedApp = this.adminApps[0];
                    this.isAppSelectDisabled = false;
                    this.isLoadingTable = false;
                    $scope.noUsersInApp = false;
                    $scope.noAppSelected = true;
                }
            };


            this.openAddNewUserModal = (user) => {
                let data = null;
                if (user) {
                    data = {
                        dialogState: 3,
                        selectedUser: {
                            orgUserId: user.orgUserId,
                            firstName: user.firstName,
                            lastName: user.lastName,

                        }
                    }
                }
                ngDialog.open({
                    templateUrl: 'app/views/users/new-user-dialogs/new-user.modal.html',
                    controller: 'NewUserModalCtrl',
                    controllerAs: 'newUser',
                    data: data
                }).closePromise.then(needUpdate => {
                    if (needUpdate.value === true) {
                        $log.debug('UsersCtrl::openAddNewUserModal updating table data...');
                        this.updateUsersList();
                    }
                });
            };
            
            this.openBulkUserUploadModal = (adminApps) => {
                let data = null;
                if (adminApps) {
                    data = {
                        dialogState: 3,
                        selectedApplication: {
                            appid: adminApps[0].appid,
                            appName: adminApps[0].appName
                        }
                    }
                }
                ngDialog.open({
                    templateUrl: 'app/views/users/new-user-dialogs/bulk-user.modal.html',
                    controller: 'BulkUserModalCtrl',
                    controllerAs: 'bulkUser',
                    data: data
                }).closePromise.then(needUpdate => {
                	this.updateUsersList();
                });
            };


            $scope.$watch('users.selectedApp.value', (newVal, oldVal) => {
                if (!newVal || _.isEqual(newVal, oldVal)) {
                    return;
                }
                $log.debug('UsersCtrl::openAddNewUserModal:$watch selectedApp -> Fire with: ', newVal);
                this.accountUsers = []; //reset table and show swirl here
                this.updateUsersList();
            });

            $scope.$on('$destroy', () => {
                //cancel all active requests when closing the modal
                activeRequests.forEach(req => {
                    req.cancel();
                });
            });

            init();
        }
    }
    UsersCtrl.$inject = ['$log', 'applicationsService', 'usersService', 'confirmBoxService', '$scope', 'ngDialog'];
    angular.module('ecompApp').controller('UsersCtrl', UsersCtrl);
})();
