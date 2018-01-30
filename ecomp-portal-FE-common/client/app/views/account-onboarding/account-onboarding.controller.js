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
                		if(needUpdate == 'confirmed'){
               			 getOnboardingAccounts();
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