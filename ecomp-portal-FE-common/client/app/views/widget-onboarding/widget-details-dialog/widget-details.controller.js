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
    class WidgetOnboardingDetailsModalCtrl {
        constructor($scope, $log, $interval, applicationsService, adminsService, microserviceService, widgetsCatalogService, errorMessageByCode, ECOMP_URL_REGEX, $window,userProfileService, confirmBoxService, $cookies,items) {
     	    
    	    this.appUpdate = function(){
    	    	this.hasSelectedApp = false;
    	    	this.appCounter = 0;
    	    	for(var i = 0; i < this.availableApps.length; i++){
    	    		if(this.availableApps[i].isSelected){
    	    			this.appCounter++;
    	    			if(!this.hasSelectedApp)
    	    				this.hasSelectedApp = true;
    	    		}
    	    		if(this.availableApps[i].isSelected
    	    		&& this.availableApps[i].roles.length == 0){
    	    			var index = i;
    	    			this.availableRoles = [];    
    	    			adminsService.getRolesByApp(this.availableApps[i].id).then(roles => {
 				    	   for(var i = 0; i < roles.length; i++){
 				    		   this.availableRoles.push({
 				    			   id: roles[i].id,
 				                   name: roles[i].name,
 				                   isSelected: false,
 				    		   }); 
 				    	   }
 				    	   this.availableApps[index].roles = this.availableRoles;
    	    			});
    	    		}
			    }
    	    	this.allRoleSelected = true;
    	    	this.checkRoleSelected();
    	    }
    	    
    	    this.roleUpdate = function(app){
	    		this.allRoleSelected = true;
    	    	for(var i = 0; i < app.roles.length; i++){
    	    		if(app.roles[i].isSelected){
    	    			app.roleSelected = true;
    	    			this.checkRoleSelected();
    	    			return;
    	    		}
    	    	}
	    		app.roleSelected = false;
	    		this.checkRoleSelected();
    	    }
    	    
    	    this.checkRoleSelected = function(){
    	    	for(var i = 0; i < this.availableApps.length; i++){
            		if(this.availableApps[i].isSelected
            		&& !this.availableApps[i].roleSelected){
            			this.allRoleSelected = false;
            			return;
            		}
            	}
    	    }
    	    	   
    	    this.getAppName = function(appId){
    	    	 for(var i = 0; i < this.availableApps.length; i++){
    	    		 if(this.availableApps[i].id == appId){
    	    			 return this.availableApps[i].name;
    	    		 }
    	    	 }
    	    }
    	    
            let newWidgetModel = {
                name: null,
                roleId: null,
                roleName: null,
                appId: null,
                appName: null,
                url: null,
                showAppOptions: false,
                showRoleOptions: false,
                hasSelectedApp: false
            };
          
            let getAvailableApps = () => {           	
            	
            	if(this.isEditMode == false){	
	            	applicationsService.getAppsForSuperAdminAndAccountAdmin().then(apps => {
	            		this.availableApps=[];
	            		for(var i=0;i<apps.length;i++) {
	                        if (!apps[i].restrictedApp) {
                            this.availableApps.push({
                                id: apps[i].id,
                                name: apps[i].name,
                                roles: [],
                                roleSelected: false,
                                isSelected: false,
                            });
	                        }
	                    }
	                }).catch(err => {
	                    $log.error(err);
	                });
            	}
            	else if(this.isEditMode == true){
            		if(this.widget.allowAllUser == "Y")
            			this.widget.allUser = true;
            		applicationsService.getAppsForSuperAdminAndAccountAdmin().then(apps => {
	            		this.availableApps=[];
	            		let selectedApps = {};
	            		var availableApps = this.availableApps;  
	            		this.allRoleSelected = true;
	            		for(var i=0; i < this.widget.widgetRoles.length; i++){
	            			if(selectedApps[this.widget.widgetRoles[i].app.appId] != undefined)
	            				selectedApps[this.widget.widgetRoles[i].app.appId] += this.widget.widgetRoles[i].roleId + ";" + this.widget.widgetRoles[i].roleName + ";"; 
	            			else{
	            				selectedApps[this.widget.widgetRoles[i].app.appId] = this.widget.widgetRoles[i].roleId + ";" + this.widget.widgetRoles[i].roleName + ";";        		
	            				this.appCounter++;
	            			}
	            		}         		
	            		apps.forEach(function(app, index){
		    				availableApps.push({
		                       id: app.id,
		                       name: app.name,
		                       roles: [],
		                       roleSelected: false,
		                       isSelected: false,
		    				});
		    				if(selectedApps[app.id] != undefined){
			    				adminsService.getRolesByApp(app.id).then(roles => {
		        				var role = selectedApps[app.id].split(';');
			            		var selectedRoles = [];
			            		var n = 0;
			            		while((n+1) < role.length){
			                		selectedRoles.push({
						    			   id: role[n++],
						                   name: role[n++],
						                   isSelected: true,
						    		});
			            		}					
			    				for(var m = 0; m < roles.length; m++){
						    		var hasSelected = true;
			    					for(var n = 0; n < selectedRoles.length; n++){
			    						if(selectedRoles[n].id == roles[m].id){
			    							hasSelected = false;
						    				break;
						    			}
						    		}
						    		if(hasSelected){
						    		   selectedRoles.push({
						    			   id: roles[m].id,
						                   name: roles[m].name,
						                   isSelected: false,
						    		   }); 
						    		}	   
			    				}  
			    				availableApps[index].roleSelected = true;
			    				availableApps[index].isSelected = true;
			    				availableApps[index].roles = selectedRoles;
		    					});
		    				}
	            		})
	                })
            	}
            };
            
            let getAvailableServices = () =>{
            	microserviceService.getServiceList().then(services => {
              		this.availableServices = [];
              		for(var i = 0; i < services.length; i++){
              			this.availableServices.push({
              				id: services[i].id,
              				name: services[i].name,
              				option: services[i].name + ": " + services[i].url
              			});
              			
              			if(this.widget.serviceId != null && this.widget.serviceId == services[i].id){
              				this.widget.serviceURL = this.availableServices[i];
              			}
              		}
                }).catch(err => {
                   $log.error(err);
                });
            }
            

            let init = () => {
            	$log.info('WidgetOnboardingDetailsModalCtrl::init');
                this.widgetsList = [];
                this.duplicatedName = true;
                this.allRoleSelected = false;
                this.appCounter = 0;
            	this.isSaving = false;
            	this.allUser = false;
            	this.emptyWidgetName = false;
            	
                if (items && items.widget) {
                    this.isEditMode = true;
            		this.allRoleSelected = true;
                    this.widget = _.clone(items.widget);
                } else {
                    this.isEditMode = false;
                    this.widget = _.clone(newWidgetModel);
                }
                
                widgetsCatalogService.getManagedWidgets().then(res => {
            		for(var i = 0; i < res.length; i++){
            			this.widgetsList.push(res[i].name);
            		}
                 }).catch(err => {
                     $log.error('WidgetOnboardingDetailsModalCtrl::init error: ' + err);
                 }).finally(()=> {
                     this.isLoadingTable = false;
                 });
                 getAvailableApps();
                 getAvailableServices();
            };
            this.ECOMP_URL_REGEX = ECOMP_URL_REGEX;
            this.conflictMessages = {};
            this.scrollApi = {};

            let resetConflict = fieldName => {
                delete this.conflictMessages[fieldName];
                if($scope.widgetForm[fieldName]){
                    $scope.widgetForm[fieldName].$setValidity('conflict', true);
                }
            };
            
            this.updateSelectedRole = () => {
                if (!this.selectedRole) {
                    return;
                }
                this.widget.RoleId = this.selectedRole.id;
                this.widget.RoleName = this.selectedRole.name;
            };
            
            let emptyCookies = () => {
                userProfileService.getUserProfile()
                .then(profile=> {
                    $log.info('AppDetailsModalCtrl::emptyCookies profile: ', profile);
                    $scope.orgUserId = profile.orgUserId;
                    $log.info('user has the following orgUserId: ' + profile.orgUserId);
                    if ($cookies.getObject($scope.orgUserId + '_widget') != undefined && $cookies.getObject($scope.orgUserId + '_widget') != null) {
                        $cookies.remove($scope.orgUserId + '_widget');
                    }
                });
            };
              
            this.updateWidgetName = () => {
            	for(var i = 0; i < this.widgetsList.length; i++){
            		if(this.widget.name.toUpperCase() == this.widgetsList[i].toUpperCase()){
            			this.duplicatedName = false;
            			return;
            		}
            	}
            	this.duplicatedName = true;
            };
            
            this.saveChanges = () => {     
            	
            	if(!this.isEditMode)
            		this.updateWidgetName();
            	
            	if(this.duplicatedName == false 
            	   || this.widget.name == ''
            	   || this.widget.name == undefined){
                    	this.emptyWidgetName = true;
                    	return;
                }
            	
            	if((this.widget.file == undefined && !this.isEditMode) ||
            	(!this.widget.allUser && this.appCounter == 0) ||
            	this.widget.name == null ||
            	(!this.widget.allUser && !this.allRoleSelected) ||
            	this.widget.saving == true)
            		return;	
            	
            	
        		this.widget.saving = true;
            	var selectedRoles = [];
            	if(!this.widget.allUser){
	            	for(var i = 0; i < this.availableApps.length; i++){
	            		if(this.availableApps[i].isSelected){
		        			for(var n = 0; n < this.availableApps[i].roles.length; n++) {
			    				if(this.availableApps[i].roles[n].isSelected){
			    				var role = {
								app: {
									appId: this.availableApps[i].id
								},
								roleId: this.availableApps[i].roles[n].id,
								roleName: this.availableApps[i].roles[n].name,
								};
			    				selectedRoles.push(role);
			    				}
			            	}
	            		}
	            	}
            	}
            	
            	var allowAllUser = 0;
            	if(this.widget.allUser)
            		allowAllUser = 1;  	
            	
            	var serviceId = null;
            	if(this.widget.serviceURL != null &&
            	this.widget.serviceURL != undefined){
            		serviceId = parseInt(this.widget.serviceURL);
            	}
            	
            	
            	var file_loc = this.widget.name + ".zip";
            	var newWidget = {
            			name: this.widget.name,
            			desc: this.widget.desc,
            			widgetRoles: selectedRoles,
            			fileLocation: file_loc,
            			allowAllUser: allowAllUser,
            			serviceId: serviceId
            	};
            	
            	if(this.isEditMode){
            		
            		if(this.widget.file != undefined){
            			widgetsCatalogService.updateWidgetWithFile(this.widget.file, this.widget.id, newWidget).then(res => {
            				if(!res.valid){
            					if(!res.error){
                					confirmBoxService.showInformation("Could not save. Please retry.");
                					this.widget.saving = false;
                					return;
                				}
            					confirmBoxService.showInformation(res.error);
            					this.widget.saving = false;
            					return;
            				}
            				$scope.$dismiss('cancel');
    	            		this.widget.saving = false;
            			});	
            		}
            		else{
            			widgetsCatalogService.updateWidget(this.widget.id, newWidget)
	            		.then(() => {
            				$scope.$dismiss('cancel');

	                    });
            		}
            	}
            	else{
            		widgetsCatalogService.createWidget(newWidget, this.widget.file).then(res => {
	            		if(!res.valid){
	        				if(!res.error)
	        					confirmBoxService.showInformation("Could not save. Please retry.");
	        				else
	        					confirmBoxService.showInformation(res.error);
	        				this.widget.saving = false;
	        				return;
	        			}
        				$scope.$dismiss('cancel');

	            		this.widget.saving = false;
            		});
            	}
            };
            init();
            $scope.$on('$stateChangeStart', e => {
                e.preventDefault();
            });
        }
    }
    WidgetOnboardingDetailsModalCtrl.$inject = ['$scope', '$log', '$interval', 'applicationsService', 'adminsService', 'microserviceService', 'widgetsCatalogService', 'errorMessageByCode', 'ECOMP_URL_REGEX', '$window','userProfileService', 'confirmBoxService', '$cookies','items'];
    angular.module('ecompApp').controller('WidgetOnboardingDetailsModalCtrl', WidgetOnboardingDetailsModalCtrl);
})(); 
