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
/**
 * Created by nnaffar on 12/20/15.
 */
'use strict';
(function () {
    class NewCatalogModalCtrl {
        constructor($scope, $log, usersService, catalogService, applicationsService, confirmBoxService) {
            let init = () => {
                //$log.info('NewUserModalCtrl::init');
                this.isSaving = false;
                this.adminApps =[];
                this.appRoles = [];
                $scope.userAppRoles = [];
                $scope.titleText = "Request for Access in MyLogins:";
                $scope.title ="Request is pending in MyLogins for the following Roles";
                this.isGettingAdminApps = false;
                if($scope.ngDialogData && $scope.ngDialogData.selectedUser && $scope.ngDialogData.dialogState){
                    this.selectedUser = $scope.ngDialogData.selectedUser;
                    this.dialogState = $scope.ngDialogData.dialogState;
                    this.isShowBack = false;
                    if(this.dialogState === 2){
                        this.getUserAppsRoles();
                    }
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

                //$log.debug('NewUserModalCtrl::getUserAppsRoles: about to call getAdminAppsSimpler');
                this.isGettingAdminApps = true;
                catalogService.getAppCatalog().then((apps) => {
                    //$log.debug('NewUserModalCtrl::getUserAppsRoles: beginning of then for getAdminAppsSimpler');
                	
                	this.isGettingAdminApps = false;
                    if (!apps || !apps.length) {
                        $log.error('NewUserModalCtrl::getUserApps error: no admin apps found');
                        return null;
                    }
                    //$log.debug('NewUserModalCtrl::getUserAppsRoles: then for getAdminAppsSimpler: step 2');
                    //$log.debug('NewUserModalCtrl::getUserAppsRoles: admin apps: ', apps);
                    
                    this.dialogState = 2; 
                    this.isLoading = true;
                    this.adminApps = apps;
                   
        			catalogService.getuserAppRolesCatalog(this.selectedUser.headerText).then(
        					function(result) {
        						// $log.debug('CatalogCtrl:storeSelection result is ', result);
        						
        						$scope.userAppRoles = result;
        						$scope.displyUserAppCatalogRoles = true;
        						if(result.length === 1 && result[0].requestedRoleId === null)
        							{
        							$scope.title= "Removal Request is pending in MyLogins";
        							}
        						
        						   if($scope.userAppRoles.length==0)
        							{
        							$scope.displyUserAppCatalogRoles = false;
        							}
        					});
                    apps.forEach(app => {
                        if(app.name === this.selectedUser.headerText){
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
                        usersService.getUserAppRoles(app.id, this.selectedUser.attuid).promise().then((userAppRolesResult) => {
                            //$log.debug('NewUserModalCtrl::getUserAppsRoles: got a result for app: ',app.id,': ',app.name,': ',userAppRolesResult);
                           	app.appRoles = userAppRolesResult;
                           	app.isLoading = false;
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
                        }
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
                if(!this.selectedUser || !this.selectedUser.attuid || !this.adminApps){
                    $log.error('NewUserModalCtrl::updateUserAppsRoles: mmissing arguments');
                    return;
                }
                this.isSaving = true;
                confirmBoxService.makeUserAppRoleCatalogChanges('Are you sure you want to make these  changes?')
                                            .then(confirmed => {
                                              	if(confirmed === true){
                //$log.debug('NewUserModalCtrl::updateUserAppsRoles: going to update user: ' + this.selectedUser.attuid);
                this.numberAppsProcessed = 0;
                this.numberAppsSucceeded = 0;
                this.adminApps.forEach(app => {
                    if (app.isChanged) {
                        //$log.debug('NewUserModalCtrl::updateUserAppsRoles: app roles have changed; going to update: id: ', app.id, '; name: ', app.name);
                        app.isUpdating = true;
                        var UserAppRolesRequest = {
                        	attuid: this.selectedUser.attuid,
                        	appId: app.id, 
                        	appRoles: app.appRoles,
                        	appName: app.name
                        };                 
                        this.isSaving = true;
                        $log.debug('going to update user: ' + this.selectedUser.attuid + ' with app roles: ' + JSON.stringify(this.adminAppsRoles));
                                applicationsService.saveUserAppsRoles(UserAppRolesRequest).then(res => {
                                	 app.isUpdating = false;
                                     $scope.closeThisDialog(true);
                                }).catch(err => {
                                    $log.error('NewAdminModalCtrl.updateAdminAppsRoles:: Failed - ' + err);
                                }).finally(()=> {
                                    this.isSaving = false;
                                })
                    	}
                });
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
    NewCatalogModalCtrl.$inject = ['$scope', '$log', 'usersService', 'catalogService', 'applicationsService', 'confirmBoxService'];
    angular.module('ecompApp').controller('NewCatalogModalCtrl', NewCatalogModalCtrl);
})();
