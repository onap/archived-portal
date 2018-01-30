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
    class MicroserviceOnboardingCtrl {
        constructor($log, applicationsService, microserviceService, ngDialog, confirmBoxService,
                    userProfileService, $cookies, $scope,$modal) {
        	
        	
            let getOnboardingServices = () => {
            	microserviceService.getServiceList().then(res => {
                     $scope.serviceList = res;
                }).catch(err => {
                    $log.error('MicroserviceOnboardingCtrl::getOnboardingServices caught error', err);
                });
            };
            
            
            
            let init = () => {
                $scope.serviceList = [];
                getOnboardingServices();
                this.serviceTableHeaders = [
                    {name: 'Microservice Name', value: 'name', isSortable: false},
                    {name: 'Service Endpoint URL', value: 'url', isSortable: false},
                    {name: 'Security Type', value: 'securityType', isSortable: false}
                ];
            };
            
            this.openAddNewMicroserviceModal = (selectedService) => {
            	let data = null; 
				if(selectedService){
					if(!selectedService.id){
						$log.error('MicroserviceOnboardingCtrl:openAddNewMicroserviceModal:service id not found');
						return; 
					} 
					data = { 
						service:selectedService,
						list: $scope.serviceList
					}
				}else{
					data = {
						list: $scope.serviceList	
					}
				}
				
				var modalInstance = $modal.open({
                    templateUrl: 'app/views/microservice-onboarding/microservice-add-details/microservice-add-details.html',
                    controller: 'MicroserviceAddDetailsCtrl as microserviceAddDetails',
                    sizeClass: 'modal-large', 
                    resolve: {
    					items: function () {
        	        	  return data;
    			        	}
    		        }
                })
                
                modalInstance.result.finally(function (){
                		getOnboardingServices();
     	        });

            };
                        
            this.deleteService = service => { 
    		   confirmBoxService.deleteItem(service.name).then(isConfirmed => {   
               	if(isConfirmed){
               			if(!service || !service.id){
                           $log.error('MicroserviceOnboardingCtrl::deleteService: No service or ID... cannot delete');
                           return;
                       }
                       microserviceService.deleteService(service.id).then((res) => {                    	   
                    	   if(res.status == "WARN"){
                    		   confirmBoxService.showInformation("Failed: widgets " +  res.response + " are assoicated with this microservice!");
                    	   }else{
                    		   $scope.serviceList.splice($scope.serviceList.indexOf(service), 1);
                    	   }
                       }).catch(err => {
                           $log.error('MicroserviceOnboardingCtrl::deleteService error:',err);
                       });
                   }
               }).catch(err => {
                   $log.error('MicroserviceOnboardingCtrl::deleteService error:',err);
               });

                
            };
            
            init();
        }
    }
    MicroserviceOnboardingCtrl.$inject = ['$log', 'applicationsService', 'microserviceService', 'ngDialog', 'confirmBoxService',
        'userProfileService','$cookies', '$scope','$modal'];
    angular.module('ecompApp').controller('MicroserviceOnboardingCtrl', MicroserviceOnboardingCtrl);
})();