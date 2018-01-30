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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
'use strict';
(function () {
    class MenuDetailsModalCtrl {
        constructor($scope, $log, functionalMenuService, errorMessageByCode, $modalInstance, ECOMP_URL_REGEX,$rootScope,confirmBoxService,items) {
        	$scope.ngDialogData=items;
        	$scope.isAllApplications = false;
            let newMenuModel = {
                name: null,
                menuId: null,
                parentMenuId: null,
                url: null
            };

            let getAvailableRoles = (appid) => {
                if (appid != null) {
                    $log.debug("MenuDetailsModalCtrl::getAvailableRoles: About to call getManagedRolesMenu");
                    functionalMenuService.getManagedRolesMenu(appid).then(rolesObj => {
                        $log.debug("MenuDetailsModalCtrl::getAvailableRoles: Roles returned = " + JSON.stringify(rolesObj))
                        this.availableRoles = rolesObj;
                       
                   
                        this.preSelectedRoles = {roles:[]};

                        if(($scope.ngDialogData.source==='edit') && this.isMidLevelMenuItem()){
                            // in Edit flow , for Midlevel menu item no need to preSelect.
                            this.preSelectedRoles = {roles:[]};
                        }else if(!angular.isUndefined(this.menuItem.menuDetails) &&
                            $scope.ngDialogData.source==='edit' && this.isLeafMenuItem() &&
                            this.menuItem.menuDetails.appid!=appid) {
                            // in Edit flow , for LeafMenuItem, if appid changed then no need to preSelect.
                            this.preSelectedRoles = {roles:[]};
                        }else{
                            if((!angular.isUndefined(this.menuItem.menuDetails)) &&
                                (!angular.isUndefined(this.menuItem.menuDetails.roles))){
                                    $log.debug('menuDetails.roles: ' + this.menuItem.menuDetails.roles);
                                    for(var i=0; i<this.menuItem.menuDetails.roles.length; i++){
                                        var role = {"roleId":this.menuItem.menuDetails.roles[i]};
                                        $log.debug('MenuDetailsModalCtrl::getAvailableRoles: adding role to preselected: ' + i + ': ' + JSON.stringify(role));
                                        this.preSelectedRoles.roles.push(role);
                                    }
                            }
                        }
                        
                        $rootScope.$broadcast('availableRolesReady');
                        for(var i=0; i<rolesObj.length;i++){
                        	this.availableRoles[i].isApplied = false;
                        	for(var j=0;j<this.preSelectedRoles.roles.length;j++){
                        		if(this.preSelectedRoles.roles[j].roleId==this.availableRoles[i].roleId){
                        			this.availableRoles[i].isApplied=true;
                        			break;
                        		}
                        	}
                        	

                        	}
                    }).catch(err => {
                        $log.error("MenuDetailsModalCtrl::getAvailableRoles: error: " + err);
                    });
                } else {
                    $log.debug("MenuDetailsModalCtrl::getAvailableRoles: appid was null");
                }
            };

            let getAvailableApps = () => {
            	$scope.isAllApplications = true;
                functionalMenuService.getAvailableApplications().then(apps => {
                    $log.debug("MenuDetailsModalCtrl::getAvailableApps: Apps returned = " + JSON.stringify(apps))
                    this.availableApps = apps;
                    // Keep track of whether or not the selected app is disabled
                    if (angular.isDefined(this.selectedApp) && angular.isDefined(this.selectedApp.index)) {
                        for (var i = 0; i < apps.length; i++) {
                            if (apps[i].index === this.selectedApp.index) {
                                $log.debug("MenuDetailsModalCtrl::getAvailableApps: found app with index: " + this.selectedApp.index);
                                $log.debug("MenuDetailsModalCtrl::getAvailableApps: setting isDisabled to: " + !apps[i].enabled);
                                this.selectedApp.isDisabled = !apps[i].enabled;
                                break;
                            }
                        }
                        $log.debug("didn't find index: " + this.selectedApp.index);
                    }
                })['catch'](function (err) {
                    confirmBoxService.showInformation('There was a problem retrieving the Applications. ' +
                        'Please try again later. Error Status: '+ err.status).then(isConfirmed => {});
                    $log.error("MenuDetailsModalCtrl::getAvailableApps: getAvailableApps error: " + err);
                    $scope.isAllApplications = false;
                }).finally(()=>{
                    this.isSaving = false;
                    $scope.isAllApplications = false;
                });
            };

            let init = () => {
            	$scope.isAllApplications = false;
                $log.info('MenuDetailsModalCtrl::init');
                this.saveOrContinueBtnText = "Save";
                this.isSaving = false;
                this.displayRoles = $scope.ngDialogData.source==='view' ? true : false;
                this.formEditable = $scope.ngDialogData.source==='view' ? false : true;
                this.selectedRole = [];
                this.availableRoles = [];
                this.selectedApp={};
                this.menuItem = _.clone($scope.ngDialogData.menuItem);
                $log.info('MenuDetailsModalCtrl::getAvailableApps: Within init, about to check menuDetails for defined');
                if(!angular.isUndefined(this.menuItem.menuDetails) &&
                    ($scope.ngDialogData.source==='view' ||
                    ($scope.ngDialogData.source==='edit') && this.isLeafMenuItem() )){

                    $log.debug("MenuDetailsModalCtrl::init: menuItem: ");
                    $log.debug('MenuDetailsModalCtrl::init: ',this.menuItem);
                    this.menuItem.menu.url = this.menuItem.menuDetails.url;
                    this.selectedAppIndex=this.menuItem.menuDetails.appid;
                    this.selectedApp.index = this.menuItem.menuDetails.appid;
                    getAvailableRoles(this.selectedApp.index);

                }

                if($scope.ngDialogData.source==='view' || $scope.ngDialogData.source==='edit'){
                    this.menutitle = this.menuItem.menu.name;
                    this.menuLocation = this.isParentMenuItem() ? this.menuItem.menu.name : this.menuItem.menu.parent.name;
                }else{
                    this.menutitle = '';
                    this.menuLocation = this.menuItem.menu.name;
                }
                // Temporarily passing 0 as dummy for getAvailableRoles incase of this.selectedApp is not there i.e., in Add flow
                //  getAvailableRoles(angular.isUndefined(this.selectedApp) ? 0: this.selectedApp.index );
                getAvailableApps();
                $log.debug("MenuDetailsModalCtrl::init: Menu details: " +  JSON.stringify(this.menuItem.menuDetails));
            };


            this.ECOMP_URL_REGEX = ECOMP_URL_REGEX;

            //This part handles conflict errors (409)
            this.conflictMessages = {};
            this.scrollApi = {};
            let handleConflictErrors = err => {
                if(!err.data){
                    return;
                }
                if(!err.data.length){ //support objects
                    err.data = [err.data]
                }
                $log.debug('MenuDetailsModalCtrl::handleConflictErrors: err.data = ' + JSON.stringify(err.data));
                _.forEach(err.data, item => {
                    _.forEach(item.fields, field => {
                        //set conflict message
                        this.conflictMessages[field.name] = errorMessageByCode[item.errorCode];
                        //set field as invalid
                        $log.debug('MenuDetailsModalCtrl::handleConflictErrors: fieldName = ' + field.name);
                        $scope.functionalMenuForm[field.name].$setValidity('conflict', false);
                        //set watch once to clear error after user correction
                        watchOnce[field.name]();
                    });
                });
                this.scrollApi.scrollTop();
            };

            let resetConflict = fieldName => {
                delete this.conflictMessages[fieldName];
                $log.debug('MenuDetailsModalCtrl::resetConflict: $setValidity(true) = ' + fieldName);
                if($scope.functionalMenuForm[fieldName]){
                    $scope.functionalMenuForm[fieldName].$setValidity('conflict', true);
                }
            };

            let watchOnce = {
                text: () => {
                    let unregisterName = $scope.$watch('functionalMenuDetails.menutitle', (newVal, oldVal) => {
                        // $log.debug('title:: newVal, oldVal = ' + newVal.toLowerCase() + " | " + oldVal.toLowerCase());
                        if(newVal.toLowerCase() !== oldVal.toLowerCase()){
                            resetConflict('text');
                            unregisterName();
                        }
                    });
                },
                url: () => {
                    let unregisterUrl = $scope.$watch('functionalMenuDetails.menuItem.menu.url', (newVal, oldVal) => {
                        if(newVal.toLowerCase() !== oldVal.toLowerCase()){
                            resetConflict('url');
                            unregisterUrl();
                        }
                    });
                }
            };

            //***************************

            this.isLeafMenuItem = () => {
                return this.menuItem.menu.children.length>0 ? false : true;
            };

            this.isMidLevelMenuItem = () => {
                return this.menuItem.menu.parentMenuId!=null && this.menuItem.menu.children.length>0 ? true : false;
            };

            this.isParentMenuItem = () => {
                return this.menuItem.menu.parentMenuId!=null ? false : true;
            };
            
            this.isRoleSelected=()=>{
                var selectedRoleIds=[];
            	 for(var i=0;i<this.availableRoles.length;i++){
                 	if(this.availableRoles[i].isApplied){
                 	selectedRoleIds.push(this.availableRoles[i].roleId);
                 	return true;
                 	}
                 }
            	 return false;
            	
            };

            this.updateSelectedApp = (appItem) => {
            	/*var appItemobj= JSON.parse(appItem);
            	this.selectedApp=JSON.parse(this.selectedApp);*/
                if (!appItem) {
                    return;
                }
                var appobj={};
                for(var i=0;i<this.availableApps.length;i++ ){
                	if(this.availableApps[i].index==appItem){
                		appobj=this.availableApps[i];
                		break;
                	}
                }
                $log.debug('MenuDetailsModalCtrl::updateSelectedApp: drop down app item = ' + JSON.stringify(appItem.index));
                $log.debug("MenuDetailsModalCtrl::updateSelectedApp: appItem in updateSelectedApp: ");
                $log.debug('MenuDetailsModalCtrl::updateSelectedApp: ',appItem);
                if (appItem !== "Select Application"){
                		this.selectedApp.isDisabled = ! appobj.enabled;
                }
                this.selectedApp.index=appobj.index;
                 $log.debug("MenuDetailsModalCtrl::updateSelectedApp: isDisabled: "+this.selectedApp.isDisabled);
                getAvailableRoles(appobj.index);
            };

            this.continue = () => {
                this.displayRoles = true;
                this.formEditable = false;
            };

            this.saveChanges = () => {

                //todo : form validation was commented as dialog message is kept for error validations
                /*if($scope.functionalMenuForm.$invalid){
                 return;
                 }*/
                if(!!this.menuItem.menu.url && (angular.isUndefined(this.selectedApp) || !this.selectedApp.index>0)) {
                    confirmBoxService.showInformation('Please select the appropriate app, or remove the url').then(isConfirmed => {});
                    return;
                }else if(!this.menuItem.menu.url && !angular.isUndefined(this.selectedApp) && this.selectedApp.index>0){
                    confirmBoxService.showInformation('Please enter url, or select "No Application"').then(isConfirmed => {});
                    return;
                }else if(!this.menutitle){
                    confirmBoxService.showInformation('Please enter the Menu title').then(isConfirmed => {});
                    return;
                }

                this.isSaving = true;
                var activeMenuItem = {};

                if ($scope.ngDialogData.source === 'edit') {     // Edit Menu Item
                    $log.debug('MenuDetailsModalCtrl::saveChanges: Will be saving an edit menu item');
                    var selectedRoleIds=[];
                    for(var i=0;i<this.availableRoles.length;i++){
                    	if(this.availableRoles[i].isApplied){
                    	selectedRoleIds.push(this.availableRoles[i].roleId);
                    	}
                    }
                    activeMenuItem = {
                        menuId:this.menuItem.menu.menuId,
                        column:this.menuItem.menu.column,
                        text:this.menutitle,
                        parentMenuId:this.menuItem.menu.parentMenuId,
                        url:this.menuItem.menu.url,
                        
                        appid: angular.isUndefined(this.selectedApp) ? null:this.selectedApp.index,
                        
                        roles:selectedRoleIds
                    };
                    
                   // alert(activeMenuItem);
                    // If we have removed the url and appid, we must remove the roles
                    if (!activeMenuItem.appid && !activeMenuItem.url) {
                        activeMenuItem.roles = null;
                    }
                    functionalMenuService.saveEditedMenuItem(activeMenuItem)
                        .then(() => {
                            $log.debug('MenuDetailsModalCtrl::saveChanges:  Menu Item saved');
                            $scope.$close(true);
                        }).catch(err => {
                        if(err.status === 409){//Conflict
                            handleConflictErrors(err);
                        } else {
                            confirmBoxService.showInformation('There was a problem saving your change. ' +
                                'Please try again later. Error Status: '+ err.status).then(isConfirmed => {});
                        }
                        $log.error('MenuDetailsModalCtrl::saveChanges: error - ',err);
                    }).finally(()=>{
                        this.isSaving = false;
                    });

                    $log.debug("MenuDetailsModalCtrl::saveChanges: Edit Menu output will be: " + JSON.stringify(activeMenuItem));
                } else {   // New Menu Item
                    $log.debug('MenuDetailsModalCtrl::saveChanges: Will be saving a New menu item');
                    var selectedRoleIds=[];
                    for(var i=0;i<this.availableRoles.length;i++){
                    	if(this.availableRoles[i].isApplied){
                    	selectedRoleIds.push(this.availableRoles[i].roleId);
                    	}
                    }
                    var newMenuItem = {
                        menuId:null, // this is a new menu item
                        column:this.menuItem.menu.column,
                        text:this.menutitle,
                        // We are creating this new menu item under the menu item that was clicked on.
                        parentMenuId:this.menuItem.menu.menuId,
                        url:this.menuItem.menu.url,
                        appid: angular.isUndefined(this.selectedApp) ? null:this.selectedApp.index,
                        roles:selectedRoleIds
                    };

                    $log.debug("MenuDetailsModalCtrl::saveChanges:  New Menu output will be: " + JSON.stringify(newMenuItem));
                    functionalMenuService.saveMenuItem(newMenuItem)
                        .then(() => {
                            $log.debug('MenuDetailsModalCtrl::saveChanges:  Menu Item saved');
                           // $scope.closeThisDialog(true);
                            $modalInstance.close("confirmed");
                        }).catch(err => {
                        if(err.status === 409){//Conflict
                            handleConflictErrors(err);
                        } else {
                            confirmBoxService.showInformation('There was a problem saving your menu. ' +
                                'Please try again later. Error Status: '+ err.status).then(isConfirmed => {});
                        }
                        $log.error('MenuDetailsModalCtrl::saveChanges error: ', err);
                    }).finally(()=>{
                        this.isSaving = false;
                    });

                }
            };

            init();

            $scope.$on('$stateChangeStart', e => {
                //Disable navigation when modal is opened
                e.preventDefault();
            });
        }
    }
    MenuDetailsModalCtrl.$inject = ['$scope', '$log', 'functionalMenuService', 'errorMessageByCode', '$modalInstance', 'ECOMP_URL_REGEX','$rootScope','confirmBoxService','items'];
    angular.module('ecompApp').controller('MenuDetailsModalCtrl', MenuDetailsModalCtrl);

  })();