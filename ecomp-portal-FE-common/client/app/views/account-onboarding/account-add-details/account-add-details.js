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
'use strict';
(function () {
    class AccountAddDetailsCtrl {
        constructor($scope, $log, $interval, basicAuthAccountService, $modalInstance, errorMessageByCode, ECOMP_URL_REGEX, $window, confirmBoxService, $cookies,items) {
         
           this.addEndpoint = () => {
        	   confirmBoxService.showInformation('Please add the roles to this Username/MechId through AAF Screen ').then(isConfirmed => {});
               return;
           	 /* this.account.endpointList.push({
           		  valid: true
           	  }); */
           }
            let init = () => {
            	this.account = [];
                this.account.endpointList = [];
                this.passwordMatched = true;
                this.dupliateName = false;
                this.emptyAccountName = false;
                this.emptyAccountUsername = false;
                this.accountList = items.list;
                
                if (items&& items.account) {
                    this.isEditMode = true;
                    this.account = _.clone(items.account);
                    this.account.repassword = this.account.password;
                    this.account.endpointList = this.account.endpoints;
                    if(this.account.isActive == 'Y')
                    	this.account.active = true;
                    else
                    	this.account.active = false;
                } else {
                    this.isEditMode = false;
                    this.account.active = true;
                } 
                console.log(this.account);
            };
            
            let resetConflict = fieldName => {
                delete this.conflictMessages[fieldName];
                if($scope.widgetForm[fieldName]){
                    $scope.widgetForm[fieldName].$setValidity('conflict', true);
                }
            };

            
          /*  this.closeThisDialog = () => {
            	$scope.closeThisDialog(true);
            }*/
            
            this.removeEndpointItem = (endpoint) => {
        		for(var i = 0; i < this.account.endpointList.length; i++){
        			if(this.account.endpointList[i].name == endpoint.name){
        				this.account.endpointList.splice(i, 1);
        				return; 
        			}
        		}
            }
            
            this.updateUsername = () => {
            	this.emptyAccountUsername = false;
            }
            
            this.updateAccountName = () => {
            	this.dupliateName = false;
            	for(var i = 0; i < this.accountList.length; i++){
            		if(this.accountList[i].applicationName == this.account.applicationName){
            			this.dupliateName = true;
            			return;
            		}
            	}
            }
            
            this.updateAccountEndpoint = (endpoint) => {
            	endpoint.valid = true;
            }
           
            this.saveChanges = () => {

            	var isValid = true;
            	var r = /\/[^ "]+$/;
            	
            	for(var i = 0; i < this.account.endpointList.length; i++){
               		if(this.account.endpointList[i].name == undefined
            		|| this.account.endpointList[i].name == null
            		|| this.account.endpointList[i].name == ""){
            			this.account.endpointList.splice(i, 1);
            			i--;
            		}else{
            			if(!this.account.endpointList[i].name.startsWith("/")){
            				this.account.endpointList[i].name = "/" + this.account.endpointList[i].name;
            			}
            			if(!r.test(this.account.endpointList[i].name)){
            				this.account.endpointList[i].valid = false;
            				isValid = false;
            			}
            			
            		}
            	}
            	
            	if(this.account.applicationName == ''
                || this.account.applicationName == undefined){
            		this.emptyAccountName = true;
                	isValid = false;
                }
            	
            	if(this.account.username == ''
                || this.account.username == undefined){
            		this.emptyAccountUsername = true;
                    isValid = false;
                }
            	
            	if(this.dupliateName == true){
                   isValid = false;
               	}
            	
            	if(this.account.password != this.account.repassword){
            		this.passwordMatched =  false;
            		isValid = false;
            	}
            	
            	if(!isValid)
            		return;
            	
               	
            	
            	var active = 'N';
            	if(this.account.active == true)
            		active = 'Y';
            	
            	var newAccount = {
            			applicationName: this.account.applicationName,
            			username: this.account.username,
            			password: this.account.password,
            			endpoints: this.account.endpointList,
            			isActive: active
            	};
            	
            	
            	if(this.isEditMode){
            		var message = "Are you sure you want to change '" + this.account.applicationName + "'?"
            		confirmBoxService.editItem(message).then(isConfirmed => {
	            		if(isConfirmed){
	            			basicAuthAccountService.updateAccount(this.account.id, newAccount).then(() => {
	            				$modalInstance.close("confirmed");
	                		});
	           			}
            		});
            	}else{
            		basicAuthAccountService.createAccount(newAccount).then(() => {
            			 $modalInstance.close("confirmed");
            		});
            	}
            }
            
            init();
            $scope.$on('$stateChangeStart', e => {
                e.preventDefault();
            });
        }
    }
    AccountAddDetailsCtrl.$inject = ['$scope', '$log', '$interval', 'basicAuthAccountService', '$modalInstance', 'errorMessageByCode', 'ECOMP_URL_REGEX', '$window', 'confirmBoxService', '$cookies','items'];
    angular.module('ecompApp').controller('AccountAddDetailsCtrl', AccountAddDetailsCtrl);
})(); 