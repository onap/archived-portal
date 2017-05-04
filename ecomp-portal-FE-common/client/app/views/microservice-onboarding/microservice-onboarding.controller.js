'use strict';
(function () {
    class MicroserviceOnboardingCtrl {
        constructor($log, applicationsService, microserviceService, ngDialog, confirmBoxService,
                    userProfileService, $cookies, $scope) {
        	
        	
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
				
                ngDialog.open({
                    templateUrl: 'app/views/microservice-onboarding/microservice-add-details/microservice-add-details.html',
                    controller: 'MicroserviceAddDetailsCtrl',
                    controllerAs: 'microserviceAddDetails',
                    data: data
                }).closePromise.then(needUpdate => {
                	if(needUpdate.value === true){
                		getOnboardingServices();
                    }
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
        'userProfileService','$cookies', '$scope'];
    angular.module('ecompApp').controller('MicroserviceOnboardingCtrl', MicroserviceOnboardingCtrl);
})();