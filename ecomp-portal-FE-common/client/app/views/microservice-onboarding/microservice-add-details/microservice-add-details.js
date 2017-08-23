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
    class MicroserviceAddDetailsCtrl {
        constructor($scope, $log, $interval, widgetsCatalogService, applicationsService, adminsService, microserviceService, errorMessageByCode, ECOMP_URL_REGEX, $window,userProfileService, confirmBoxService, $cookies,items) {
     	    
        	$scope.ngDialogData=items;
            let getAvailableApps = () => {       
            	applicationsService.getAppsForSuperAdminAndAccountAdmin().then(apps => {
            		this.availableApps=[];
            		apps.unshift({
                        id: 1,
                        name: "ECOMP Portal"
                    });
            		for(var i = 0; i < apps.length; i++) {
                        this.availableApps.push({
                            id: apps[i].id,
                            name: apps[i].name
                        });
                        if(this.isEditMode == true && this.service.appId == apps[i].id){
                        	this.service.application = this.availableApps[i];
                        }
                    }
            	}).catch(err => {
                    $log.error(err);
                });
            };
            
            let getAvailableWidgets = () => {       
            	microserviceService.getWidgetListByService(this.service.id).then(widgets =>{
            		this.availableWidgets = [];
            		for(var i = 0; i < widgets.length; i++){
            			this.availableWidgets.push({
            				name: widgets[i]
            			})
            		}
            	}).catch(err => {
                    $log.error(err);
                });
            };
            
            
            let getAvailableSecurityTypes = () => {   
            	this.availableSecurityTypes = [];
            	this.availableSecurityTypes.push({
            		id: 0,
            		name: 'No Authentication'
            	});
            	this.availableSecurityTypes.push({
            		id: 1,
            		name: 'Basic Authentication'
            	});
            	this.availableSecurityTypes.push({
            		id: 2,
            		name: 'Cookie based Authentication'
            	});
            }

            let init = () => {
            	$log.info('MicroserviceAddDetailsCtrl::init');
                this.service = [];
                this.availableApps=[];
                this.service.parameterList = [];
                this.service.active = true;
                this.emptyServiceName = false;
                this.emptyServiceDesc = false;
                this.emptyServiceURL = false;
                this.emptyServiceApp = false;
                this.dupliateName = false;
                this.serviceList = $scope.ngDialogData.list;
                
                if ($scope.ngDialogData && $scope.ngDialogData.service) {
                	
                    this.isEditMode = true;
                    this.service = _.clone($scope.ngDialogData.service);
                    
                    this.originalName  = this.service.name;
                    if(this.service.active == 'Y')
                    	this.service.active = true;
                    else
                    	this.service.active = false;
                } else {
                    this.isEditMode = false;
                } 
                getAvailableApps();
                getAvailableSecurityTypes();
                //getAvailableWidgets();
                
                /**
				 * 0: Basic Authentication
				 * 
				 * TODO: change the structure
				 */
                if(this.service.securityType == "No Authentication"){
                	this.service.security = this.availableSecurityTypes[0];
                }else if(this.service.securityType == "Basic Authentication"){
                	this.service.security = this.availableSecurityTypes[1];
                }else if(this.service.securityType == "Cookie based Authentication"){
                	this.service.security = this.availableSecurityTypes[2];
                }
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

            this.addParameter = () => {
            	this.service.parameterList.push({}); 
            }
            
            this.closeThisDialog = () => {
            	$scope.closeThisDialog(true);
            }
            
            this.removeParamItem = (parameter) => {
            	microserviceService.getUserParameterById(parameter.id).then((res) => {
            		if(res.length > 0){
            			var message = res.length + " users have their own widget parameters. Are you sure you want to delete?";
            			confirmBoxService.editItem(message).then(isConfirmed => {
            				if(isConfirmed){
            					microserviceService.deleteUserParameterById(parameter.id).then((res) => {
	            					for(var i = 0; i < this.service.parameterList.length; i++){
	            	            		if(this.service.parameterList[i].para_key == parameter.para_key
	            	            		&& this.service.parameterList[i].para_value == parameter.para_value){
	            	            			this.service.parameterList.splice(i, 1);
	            	            			return;
	            	            		}
	            	            	}
            					});
            				}
            			});
            		}
            		else{
            			for(var i = 0; i < this.service.parameterList.length; i++){
    	            		if(this.service.parameterList[i].para_key == parameter.para_key
    	            		&& this.service.parameterList[i].para_value == parameter.para_value){
    	            			this.service.parameterList.splice(i, 1);
    	            			return;
    	            		}
    	            	}
            		}
            	});
            	
            	
            }
            
            this.updateServiceName = () => {
            	this.dupliateName = false;
            	for(var i = 0; i < this.serviceList.length; i++){
            		if(this.serviceList[i].name == this.service.name){
            			if(this.isEditMode && this.service.name == this.originalName)
            				continue;
            			this.dupliateName = true;
            			return;
            		}
            	}
            }
            
            this.updateDesc = () => {
            	this.emptyServiceDesc = false;
            }
            
            this.updateURL = () => {
            	this.emptyServiceURL  = false;
            }
            
            this.updateApp = () => {
            	this.emptyServiceApp = false;
            }
            
          //This is a fix for dropdown selection, due to b2b dropdown only update value field
    		$scope.$watch('microserviceAddDetails.service.application.name', (newVal, oldVal) => {
    			for(var i=0;i<$scope.microserviceAddDetails.availableApps.length;i++){ 			
    				if($scope.microserviceAddDetails.availableApps[i].name==newVal){
    					$scope.microserviceAddDetails.service.application=angular.copy($scope.microserviceAddDetails.availableApps[i]);
    				}
    			}
    		});
    		$scope.$watch('microserviceAddDetails.service.security.name', (newVal, oldVal) => {
    			for(var i=0;i<$scope.microserviceAddDetails.availableSecurityTypes.length;i++){ 			
    				if($scope.microserviceAddDetails.availableSecurityTypes[i].name==newVal){
    					$scope.microserviceAddDetails.service.security=angular.copy($scope.microserviceAddDetails.availableSecurityTypes[i]);
    				}
    			}
    		});
            
            let emptyCookies = () => {
                userProfileService.getUserProfile()
                .then(profile=> {
                    $log.info('AppDetailsModalCtrl::emptyCookies profile: ', profile);
                    $scope.attuid = profile.attuid;
                    $log.info('user has the following attuid: ' + profile.attuid);
                    if ($cookies.getObject($scope.attuid + '_widget') != undefined && $cookies.getObject($scope.attuid + '_widget') != null) {
                        $cookies.remove($scope.attuid + '_widget');
                    }
                });
            };
            
            this.testServiceURL = () =>{
            	widgetsCatalogService.getServiceJSON(this.service.id).then(res => {
            		document.getElementById("microservice-details-input-json").innerHTML = (JSON.stringify(res));
				});
            }
            
            this.saveChanges = () => {     
            	/* TODO: add form validation */
            	
            	var isValid = true;
            	this.updateServiceName();
            	
            	if(this.service.name == ''
                || this.service.name == undefined){
            		this.emptyServiceName = true;
            		isValid = false;
            	}
            	
            	if(this.dupliateName == true){
            		isValid = false;
            	}
            		
            	
            	if(this.service.desc == ''
            	|| this.service.desc == undefined){
            		this.emptyServiceDesc = true;
            		isValid = false;
            	}
            	
            	if(this.service.url == ''
               	|| this.service.url == undefined){
              		this.emptyServiceURL = true;
               		isValid = false;
               	}
            		
            	if(this.service.application == undefined
             	|| this.service.application == null){
            		this.emptyServiceApp = true;
            		isValid = false;
            	}
            	
            	if(!isValid)
            		return;
            	
            	/*
				 * Check the parameter list, delete those parameters that don't
				 * have key
				 */
            	for(var i = 0; i < this.service.parameterList.length; i++){
            		if(this.service.parameterList[i].para_key == undefined
            		|| this.service.parameterList[i].para_key == null
            		|| this.service.parameterList[i].para_key == ""){
            			this.service.parameterList.splice(i, 1);
            			i--;
            		}
            	}
      
            	var securityType;
            	var username;
            	var password;
            	if(this.service.security == undefined ||
            	this.service.security == null)
            		securityType = "No Authentication";
            	else{
            		securityType = this.service.security.name;
            		username = this.service.username;
            		password = this.service.password;
            	}
            	
            	var active = 'N';
            	if(this.service.active == true)
            		active = 'Y';
            	
            	var newService = {
            			name: this.service.name,
            			desc: this.service.desc,
            			appId: this.service.application.id,
            			url: this.service.url,
            			securityType: securityType,
            			username: username,
            			password: password,
            			active: active,
            			parameterList: this.service.parameterList
            	};
            	
            	if(this.isEditMode){
            		// console.log(this.service.parameterList);
            		var message = "Are you sure you want to change '" + this.service.name + "'?"
            		confirmBoxService.editItem(message).then(isConfirmed => {
	            		if(isConfirmed){
		            		microserviceService.updateService(this.service.id, newService).then(() => {
		            			// TODO: result validation check
		            			//this.closeThisDialog(); 
		            			$scope.$dismiss('cancel');
		            		});
            			}
            		});
            	}else{
            		microserviceService.createService(newService).then(() => {
            			// TODO: result validation check
            			//$scope.closeThisDialog(true);
            			$scope.$dismiss('cancel');
            		});
            	}
             	
            };
            init();
            $scope.$on('$stateChangeStart', e => {
                e.preventDefault();
            });
        }
    }
    MicroserviceAddDetailsCtrl.$inject = ['$scope', '$log', '$interval', 'widgetsCatalogService', 'applicationsService', 'adminsService', 'microserviceService', 'errorMessageByCode', 'ECOMP_URL_REGEX', '$window','userProfileService', 'confirmBoxService', '$cookies','items'];
    angular.module('ecompApp').controller('MicroserviceAddDetailsCtrl', MicroserviceAddDetailsCtrl);
})(); 
