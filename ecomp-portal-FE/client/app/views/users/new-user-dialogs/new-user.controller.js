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
    class NewUserModalCtrl {
        constructor($scope, $log, usersService, applicationsService, confirmBoxService) {
            let init = () => {
                $log.info('NewUserModalCtrl::init');
                this.isSaving = false;
                this.anyChanges = false;
                this.isGettingAdminApps = false;
                if($scope.ngDialogData && $scope.ngDialogData.selectedUser && $scope.ngDialogData.dialogState){
                    this.selectedUser = $scope.ngDialogData.selectedUser;
                    this.dialogState = $scope.ngDialogData.dialogState;
                    this.isShowBack = false;
                    if(this.dialogState === 3){
                        this.getUserAppsRoles();
                    }
                }else{
                    this.isShowBack = true;
                    this.selectedUser = null;
                    this.dialogState = 1;
                }
            };

            this.appChanged = (index) => {
                let myApp = this.adminApps[index];
                $log.debug('NewUserModalCtrl::appChanged: index: ', index, '; app id: ', myApp.id, 'app name: ',myApp.name);
                myApp.isChanged = true;
                this.anyChanges = true;
            }

            this.deleteApp = (app) => {
                let appMessage = this.selectedUser.firstName + ' ' + this.selectedUser.lastName;
                confirmBoxService.deleteItem(appMessage).then(isConfirmed => {
                    if(isConfirmed){
                        app.isChanged = true;
                        this.anyChanges = true;
                        app.isDeleted = true;
                        app.appRoles.forEach(function(role){
                            role.isApplied = false;
                        });
                    }
                }).catch(err => {
                    $log.error('NewUserModalCtrl::deleteApp error: ',err);
                });
            };

            this.getUserAppsRoles = () => {
                if (!this.selectedUser || !this.selectedUser.orgUserId) {
                    $log.error('NewUserModalCtrl::getUserAppsRoles error: No user is selected');
                    this.dialogState = 1;
                    return;
                }
                $log.debug('NewUserModalCtrl::getUserAppsRoles: about to call getAdminAppsSimpler');
                this.isGettingAdminApps = true;
                applicationsService.getAdminAppsSimpler().then((apps) => {
                    $log.debug('NewUserModalCtrl::getUserAppsRoles: beginning of then for getAdminAppsSimpler');
                    this.isGettingAdminApps = false;
                    if (!apps || !apps.length) {
                        $log.error('NewUserModalCtrl::getUserAppsRoles error: no apps found');
                        return null;
                    }
                    $log.debug('NewUserModalCtrl::getUserAppsRoles: then for getAdminAppsSimpler: step 2');
                    $log.debug('NewUserModalCtrl::getUserAppsRoles: admin apps: ', apps);
                    this.adminApps = apps;
                    this.dialogState = 3;
                    this.userAppRoles = {};
                    this.numberAppsProcessed = 0;
                    this.isLoading = true;
                    apps.forEach(app => {
                        $log.debug('NewUserModalCtrl::getUserAppsRoles: app: id: ', app.id, 'name: ',app.name);
                        app.isChanged = false;
                        app.isLoading = true;
                        app.isError = false;
                        app.isDeleted = false;
                        app.printNoChanges = false;
                        app.isUpdating = false;
                        app.isErrorUpdating = false;
                        app.isDoneUpdating = false;
                        app.errorMessage = "";
                        usersService.getUserAppRoles(app.id, this.selectedUser.orgUserId).then((userAppRolesResult) => {
                            $log.debug('NewUserModalCtrl::getUserAppsRoles: got a result for app: ',app.id,': ',app.name,': ',userAppRolesResult);
                            app.appRoles = userAppRolesResult;
                            app.isLoading = false;

                        }).catch(err => {
                            $log.error(err);
                            app.isError = true;
                            app.isLoading = false;
                            app.errorMessage = err.headers('FEErrorString');
                            $log.debug('NewUserModalCtrl::getUserAppsRoles: in new-user.controller: response header: '+err.headers('FEErrorString'));
                        }).finally(()=>{
                            this.numberAppsProcessed++;
                            if (this.numberAppsProcessed == this.adminApps.length) {
                                this.isLoading = false;
                            }
                        });
                    })
                    return;
                }).catch(err => {
                    $log.error(err);
                })

            }

            this.getAdminApps = () => {
                if (!this.selectedUser || !this.selectedUserorgUserId) {
                    $log.error('NewUserModalCtrl::getAdminApps: No user is selected');
                    this.dialogState = 1;
                    return;
                }
                applicationsService.getAdminApps().promise().then(apps => {
                    if (!apps || !apps.length) {
                        $log.error('NewUserModalCtrl::getAdminApps: no apps found');
                        return null;
                    }
                    $log.debug('NewUserModalCtrl::getAdminApps: admin apps: ', apps);
                    this.adminApps = apps;
                    this.dialogState = 3;
                    return;
                }).catch(err => {
                    $log.error('NewUserModalCtrl::getAdminApps: ', err);
                })

            }

            this.updateUserAppsRoles = () => {
                $log.debug('NewUserModalCtrl::updateUserAppsRoles: entering updateUserAppsRoles');
                if(!this.selectedUser || !this.selectedUser.orgUserId || !this.adminApps){
                    $log.debug('NewUserModalCtrl::updateUserAppsRoles: returning early');
                    return;
                }
                this.isSaving = true;
                $log.debug('NewUserModalCtrl::updateUserAppsRoles: going to update user: ' + this.selectedUser.orgUserId);
                this.numberAppsProcessed = 0;
                this.numberAppsSucceeded = 0;
                this.adminApps.forEach(app => {
                    if (app.isChanged) {
                        $log.debug('NewUserModalCtrl::updateUserAppsRoles: app roles have changed; going to update: id: ', app.id, '; name: ', app.name);
                        app.isUpdating = true;
                        usersService.updateUserAppRoles({orgUserId: this.selectedUser.orgUserId, appId: app.id, appRoles: app.appRoles})
                        .then(res => {
                            $log.debug('NewUserModalCtrl::updateUserAppsRoles: User app roles updated successfully on app: ',app.id);
                            app.isUpdating = false;
                            app.isDoneUpdating = true;
                            this.numberAppsSucceeded++;
                        }).catch(err => {
                            $log.error(err);
                            app.isErrorUpdating = true;
                        }).finally(()=>{
                            this.numberAppsProcessed++;
                            if (this.numberAppsProcessed == this.adminApps.length) {
                                this.isSaving = false;
                            }
                            if (this.numberAppsSucceeded == this.adminApps.length) {
                                $scope.closeThisDialog(true);
                            }
                        })
                    } else {
                        $log.debug('NewUserModalCtrl::updateUserAppsRoles: app roles have NOT changed; NOT going to update: id: ', app.id, '; name: ', app.name);
                        app.noChanges = true;
                        app.isError = false;
                        this.numberAppsProcessed++;
                        this.numberAppsSucceeded++;
                        if (this.numberAppsProcessed == this.adminApps.length) {
                            this.isSaving = false;
                        }
                        if (this.numberAppsSucceeded == this.adminApps.length) {
                            $scope.closeThisDialog(true);
                        }
                    }
                });
            };

            this.navigateBack = () => {
                if (this.dialogState === 1) {
                }
                if (this.dialogState === 3) {
                    this.dialogState = 1;
                }
            };

            init();

            $scope.$on('$stateChangeStart', e => {
                e.preventDefault();
            });
        }
    }
    NewUserModalCtrl.$inject = ['$scope', '$log', 'usersService', 'applicationsService', 'confirmBoxService'];
    angular.module('ecompApp').controller('NewUserModalCtrl', NewUserModalCtrl);
})();
