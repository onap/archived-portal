/*-
 * ================================================================================
 * eCOMP Portal
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
        constructor($log, applicationsService, usersService, $scope, ngDialog,$timeout) {
            this.$log = $log;
            $scope.adminAppsIsNull = false;
            $scope.appsIsDown = false;

            $log.info('UsersCtrl:: initializing...');
            let activeRequests = [];
            let clearReq = (req) => {
                activeRequests.splice(activeRequests.indexOf(req), 1);
            };

            let getAdminApps = () => {
                $log.debug('UsersCtrl::getAdminApps: - Starting getAdminApps');
                try {
                    this.isLoadingTable = true;
                    var adminAppsReq = applicationsService.getAdminApps();

                    activeRequests.push(adminAppsReq);
                    adminAppsReq.promise().then(apps => {
                        $log.debug('UsersCtrl::getAdminApps: Apps for this user are: ' + JSON.stringify(apps));
                        $log.debug('UsersCtrl::getAdminApps: Apps length: ' + apps.length);
                        var res1 = apps.sort(getSortOrder("name"));
                        if (!res1 || !res1.length) {
                            $log.error('UsersCtrl::getAdminApps:  - no apps found');
                            return null;
                        }
                        for (let i = 0; i < apps.length; i++) {
                            res1[i].index = i;
                            res1[i].value = apps[i].name;
                            res1[i].title = apps[i].name;
                        }

                        this.adminApps = apps;
                        this.selectedApp = apps[0];
                        clearReq(adminAppsReq);
                        $scope.adminAppsIsNull = false;
                        }).catch(e => {
                            $scope.adminAppsIsNull = true;
                            $log.error('UsersCtrl::getAdminApps:  - getAdminApps() failed = '+ e.message);
                            clearReq(adminAppsReq);
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
                $log.debug('UsersCtrl::updateUsersList: Starting updateUsersList');
                this.searchString = '';
                this.isAppSelectDisabled = true;
                this.isLoadingTable = true;
                usersService.getAccountUsers(this.selectedApp.id)
                    .then(accountUsers => {
                        $log.debug('UsersCtrl::updateUsersList length: '+ Object.keys(accountUsers).length);
                        this.isAppSelectDisabled = false;
                        this.accountUsers = accountUsers;
                    }).catch(err => {
                        this.isAppSelectDisabled = false;
                        $log.error('UsersCtrl::updateUsersList: ' + err);
                        $scope.appsIsDown = true;
                    }).finally(() => {
                        this.isLoadingTable = false;
                });
            };


            let init = () => {
                this.isLoadingTable = false;
                this.selectedApp = null;
                this.isAppSelectDisabled = false;
                getAdminApps();

                this.searchString = '';
                this.usersTableHeaders = ['First Name', 'Last Name', 'User ID', 'Roles'];
                this.accountUsers = [];
            };

            this.openAddNewUserModal = (user) => {
                let data = null;
                if (user) {
                    data = {
                        dialogState: 3,
                        selectedUser: {
                            orgUserId: user.orgUserId,
                            firstName: user.firstName,
                            lastName: user.lastName
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
            
            this.openEditUserModal = (loginId) => {
            	var data = {
            			loginId : loginId,
            	        updateRemoteApp : true,
            	        appId : this.selectedApp!=null?this.selectedApp.id:''
            	}
            	var modalInstance = ngDialog.open({
                    templateUrl: 'app/views/header/user-edit/edit-user.tpl.html',
                    controller: 'editUserController',
                    data: data,
                    resolve: {
                        message: function message() {
                            var message = {
                                type: 'Contact',
                            };
                            return message;
                        }
                    }
                }).closePromise.then(needUpdate => {
                	//update selected app's database for this user.
                	console.log("'''''''''''''''''' now updating user list after update remote server");
                	$timeout(this.updateUsersList, 1500);
	            });       
            }
            
            
            $scope.$watch('users.selectedApp', (newVal, oldVal) => {
                if (!newVal || _.isEqual(newVal, oldVal)) {
                    return;
                }
                $log.debug('UsersCtrl::openAddNewUserModal:$watch selectedApp -> Fire with: ', newVal);
                this.accountUsers = [];
                this.updateUsersList();
            });

            $scope.$on('$destroy', () => {
                activeRequests.forEach(req => {
                    req.cancel();
                });
            });

            init();
        }
    }
    UsersCtrl.$inject = ['$log', 'applicationsService', 'usersService', '$scope', 'ngDialog','$timeout'];
    angular.module('ecompApp').controller('UsersCtrl', UsersCtrl);
})();
