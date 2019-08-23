/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Unless otherwise specified, all documentation contained herein is licensed
 * under the Creative Commons License, Attribution 4.0 Intl. (the "License");
 * you may not use this documentation except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             https://creativecommons.org/licenses/by/4.0/
 *
 * Unless required by applicable law or agreed to in writing, documentation
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ============LICENSE_END============================================
 *
 * 
 */
/**
 * Created by nnaffar on 12/20/15.
 */
'use strict';
(function () {
    class NewUserModalCtrl {
        constructor($scope, $log, usersService, applicationsService, confirmBoxService, items) {
            var extRequestValue = false;
            var isSystemUser = false;

            $scope.ngRepeatDemo = [
		        {id: 'userButton', value: 'true', labelvalue: 'User'},
		        {id: 'systemUserButton', value: 'false', labelvalue: 'System'}
		    ]
            
            $scope.selectedvalueradioButtonGroup = {
			        type: 'true'
			    }
            
            
            let init = () => {
                //$log.info('NewUserModalCtrl::init');
                this.isSaving = false;
                this.anyChanges = false;
                this.adminApps = [];
                this.isGettingAdminApps = false;
                if(items && items.selectedUser && items.dialogState){
                    this.selectedUser = items.selectedUser;
                    this.dialogState = items.dialogState;
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
                //$log.debug('NewUserModalCtrl::appChanged: index: ', index, '; app id: ', myApp.id, 'app name: ',myApp.name);
                myApp.isChanged = true;
                this.anyChanges = true;
            }

            this.deleteApp = (app) => {
                let appMessage = this.selectedUser.firstName + ' ' + this.selectedUser.lastName;
                confirmBoxService.deleteItem(appMessage).then(isConfirmed => {
                    if(isConfirmed){
                        this.anyChanges = true;
                        app.isChanged = true;
                        app.isDeleted = true; // use this to hide the app in the display
                        app.appRoles.forEach(function(role){
                            role.isApplied = false;
                        });
                    }
                }).catch(err => {
                    $log.error('NewUserModalCtrl::deleteApp error: ',err);
                    confirmBoxService.showInformation('There was a problem deleting the the applications. ' +
                        'Please try again later. Error: ' + err.status).then(isConfirmed => {});
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
                    //$log.debug('NewUserModalCtrl::getUserAppsRoles: beginning of then for getAdminAppsSimpler');
                    this.isGettingAdminApps = false;
                    if (!apps || !apps.length) {
                        $log.error('NewUserModalCtrl::getUserAppsRoles error: no admin apps found');
                        return null;
                    }
                    //$log.debug('NewUserModalCtrl::getUserAppsRoles: then for getAdminAppsSimpler: step 2');
                    //$log.debug('NewUserModalCtrl::getUserAppsRoles: admin apps: ', apps);
                    this.adminApps = apps;
                    this.dialogState = 3;
                    this.userAppRoles = {};
                    this.numberAppsProcessed = 0;
                    this.isLoading = true;
                    apps.forEach(app => {
                        //$log.debug('NewUserModalCtrl::getUserAppsRoles: app: id: ', app.id, 'name: ',app.name);
                        // Keep track of which app has changed, so we know which apps to update using a BE API
                        app.isChanged = false;
                        // Each of these specifies a state, which corresponds to a different message and style that gets displayed
                        app.isLoading = true;
                        app.isError = false;
                        app.isDeleted = false;
                        app.printNoChanges = false;
                        app.isUpdating = false;
                        app.isErrorUpdating = false;
                        app.isDoneUpdating = false;
                        app.errorMessage = "";
                        if($scope.selectedvalueradioButtonGroup.type == 'false')
                        	{
                        	   isSystemUser = true;
                        	}
                        usersService.getUserAppRoles(app.id, this.selectedUser.orgUserId, extRequestValue,isSystemUser).promise().then((userAppRolesResult) => {
                            //$log.debug('NewUserModalCtrl::getUserAppsRoles: got a result for app: ',app.id,': ',app.name,': ',userAppRolesResult);
                            app.appRoles = userAppRolesResult;
                            app.isLoading = false;
                            for(var i=0;i<app.appRoles.length;i++){
                                
                            	if(app.appRoles[i].roleName.indexOf('global_')!=-1){
                            		app.appRoles[i].roleName='*'+app.appRoles[i].roleName;
            						
            					}
                                }

                        }).catch(err => {
                            $log.error(err);
                            app.isError = true;
                            app.isLoading = false;
                            app.errorMessage = err.headers('FEErrorString');
                            //$log.debug('NewUserModalCtrl::getUserAppsRoles: in new-user.controller: response header: '+err.headers('FEErrorString'));
                        }).finally(()=>{
                            this.numberAppsProcessed++;
                            if (this.numberAppsProcessed === this.adminApps.length) {
                                this.isLoading = false;
                            }
                        });
                    })
                    return;
                }).catch(err => {
                    $log.error(err);
                })

            }

            /**
             * Update the selected user apps with the new roles.
             * If no roles remain, set the user to inactive.
             */
            this.updateUserAppsRoles = () => {
                // $log.debug('NewUserModalCtrl::updateUserAppsRoles: entering updateUserAppsRoles');
                if(!this.selectedUser || !this.selectedUser.orgUserId || !this.adminApps){
                    $log.error('NewUserModalCtrl::updateUserAppsRoles: mmissing arguments');
                    return;
                }
                this.isSaving = true;
                //$log.debug('NewUserModalCtrl::updateUserAppsRoles: going to update user: ' + this.selectedUser.orgUserId);
                this.numberAppsProcessed = 0;
                this.numberAppsSucceeded = 0;
                this.adminApps.forEach(app => {
                    if (app.isChanged) {
                        //$log.debug('NewUserModalCtrl::updateUserAppsRoles: app roles have changed; going to update: id: ', app.id, '; name: ', app.name);
                        app.isUpdating = true;
                        for(var i=0;i<app.appRoles.length;i++){
                          if(app.appRoles[i].roleName.indexOf('*global_')!=-1){
                        		app.appRoles[i].roleName=app.appRoles[i].roleName.replace('*','');
        						
        					}
                            }
                        if($scope.selectedvalueradioButtonGroup.type == 'false')
                    	{
                    	   isSystemUser = true;
                    	}else{
                    		isSystemUser = false;
                    	}
                        var newUserAppRoles = {
                        	orgUserId: this.selectedUser.orgUserId,
                        	appId: app.id, 
                        	appRoles: app.appRoles,
                        	appName: app.name,
                        	isSystemUser : isSystemUser
                        }; 
                        usersService.updateUserAppRoles(newUserAppRoles).promise()
                        .then(res => {
                            //$log.debug('NewUserModalCtrl::updateUserAppsRoles: User app roles updated successfully on app: ',app.id);
                            app.isUpdating = false;
                            app.isDoneUpdating = true;
                            this.numberAppsSucceeded++;
                        }).catch(err => {
                        	$log.error(err);                       
                            var errorMessage = 'Failed to update the user application roles: ' + err;
                            if(err.status == 504){ 
                            	this.numberAppsSucceeded++;
                            	errorMessage = 'Request is being processed, please check back later!';
                            } else{
                            	app.isErrorUpdating = true;
                            }
                            confirmBoxService.showInformation(
                            		errorMessage)
                            		.then(isConfirmed => {});                      	
                        }).finally(()=>{
                            this.numberAppsProcessed++;
                            if (this.numberAppsProcessed === this.adminApps.length) {
                                this.isSaving = false; // hide the spinner
                            }
                            if (this.numberAppsSucceeded === this.adminApps.length) {
                            	$scope.$dismiss('cancel');//close and resolve dialog promise with true (to update the table)
                            }
                        })
                    } else {
                        //$log.debug('NewUserModalCtrl::updateUserAppsRoles: app roles have NOT changed; NOT going to update: id: ', app.id, '; name: ', app.name);
                        app.noChanges = true;
                        app.isError = false; //remove the error message; just show the No Changes messages
                        this.numberAppsProcessed++;
                        this.numberAppsSucceeded++;
                        if (this.numberAppsProcessed === this.adminApps.length) {
                            this.isSaving = false; // hide the spinner
                        }
                        if (this.numberAppsSucceeded === this.adminApps.length) {
                        	$scope.$dismiss('cancel');//close and resolve dialog promise with true (to update the table)
                        }
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
                if (this.dialogState === 3) {
                    this.dialogState = 1;
                }
            };

            init();

            $scope.$on('$stateChangeStart', e => {
                //Disable navigation when modal is opened
                //**Nabil - note: this will cause the history back state to be replaced with current state
                e.preventDefault();
            });
        }
    }
    NewUserModalCtrl.$inject = ['$scope', '$log', 'usersService', 'applicationsService', 'confirmBoxService', 'items'];
    angular.module('ecompApp').controller('NewUserModalCtrl', NewUserModalCtrl);
})();
