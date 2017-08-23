'use strict';
(function () {
    class AccountOnboardingCtrl {
        constructor($log, ngDialog, confirmBoxService, basicAuthAccountService, $cookies, $scope,$modal) {
        	
        	
            let init = () => {
            	 $scope.accountList = [];
            	 getOnboardingAccounts();
            	 
            	 this.accoutTableHeaders = [
                 	{name: 'Account Name', value: 'applicationName', isSortable: true},
                    {name: 'Username', value: 'username', isSortable: false}
                 ];
            };
            
            let getOnboardingAccounts = () => {
            	basicAuthAccountService.getAccountList().then(res => {            		
                    $scope.accountList = res;
                }).catch(err => {
                    $log.error('AccountOnboardingCtrl::getOnboardingAccounts caught error', err);
                });
            };
            
            this.openAddNewAccountModal = (selectedAccount) => {
            	let data = null; 
				if(selectedAccount){
					data = { 
						account:selectedAccount,
						list: $scope.accountList
					}
				}else{
					data = {
						list: $scope.accountList	
					}
				}
          		var modalInstance = $modal.open({
                    templateUrl: 'app/views/account-onboarding/account-add-details/account-add-details.html',
                    controller: 'AccountAddDetailsCtrl as accountAddDetails',
                    sizeClass: 'modal-medium', 
                    resolve: {
    					items: function () {
        	        	  return data;
    			        	}
    		        }
                })
                
                modalInstance.result.then(function (needUpdate) {
                	if(needUpdate.value === true){
                		if(needUpdate.value === true){
               			 getOnboardingAccounts();
                       }
                  }
     	        });
            };
            
            
            this.deleteAccount = account => { 
            	console.log(account);
     		    confirmBoxService.deleteItem(account.applicationName).then(isConfirmed => {   
                	if(isConfirmed){
                		basicAuthAccountService.deleteAccount(account.id).then(() => {
                        	$scope.accountList.splice($scope.accountList.indexOf(account), 1);
                        }).catch(err => {
                            $log.error('AccountOnboardingCtrl::deleteAccount error:',err);
                        });
                    }
                }).catch(err => {
                    $log.error('AccountOnboardingCtrl::deleteAccount error:',err);
                });
             };
            
            init();
        }
    }
    AccountOnboardingCtrl.$inject = ['$log', 'ngDialog', 'confirmBoxService', 'basicAuthAccountService', '$cookies', '$scope','$modal'];
    angular.module('ecompApp').controller('AccountOnboardingCtrl', AccountOnboardingCtrl);
})();