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
    class ContactUsManageController {
        constructor($scope,$log, message, $q, $http, conf,contactUsService,confirmBoxService) {
        	$scope.contactUsList=[];
        	$scope.contactUsAllAppList=[];
        	$scope.errMsg='';
        	$scope.newContactUs ={
        			app:'',
        			name:'',
        			email:'',
        			url:'',
        			desc:''       			
        	};
        	/*Get the existing contact us first, then based on the existing, filter from all apps*/
        	$scope.getContactUsList = function(){
        		contactUsService.getContactUs().then(res=> {
            		// $log.info('getting getContactUs',res.response);
            		if(res!=null && res.response!=null){
            			for(var i=0; i<res.response.length;i++){
            				if(res.response[i].appId!=1)
            					$scope.contactUsList.push(res.response[i]);
            			}
            		}
            		/*get all the apps*/
            		contactUsService.getListOfApp().then(res=> {
                    	var tableData=[];
                    	$scope.contactUsAllAppListTemp=[];
                    	// $log.info('contactUsService::getListOfApp: getting res');
                    	var result = (typeof(res.data) != "undefined" && res.data!=null)?res.data:null;
                    	// $log.info('contactUsService::getListOfApp: result',result);  	
                    	var res1 = result;
                        var realAppIndex = 0;
                        $scope.contactUsAllAppList.length=0;
                        for (var i = 1; i <= res1.length; i++) {
                            if (!res1[i - 1].restrictedApp) {
                            	var okToAdd = true;
                            	for(var j =0; j<$scope.contactUsList.length;j++){
                            		if(res1[i - 1].title==$scope.contactUsList[j].appName)
                            			okToAdd=false;
                            	}
                            	// not allowed to add(duplicate) another entry if the app is already available in the table
                            	if(okToAdd){
                            		if(res1[i - 1].title){
                            			$scope.contactUsAllAppList.push({
                                            index: realAppIndex,
                                            title: res1[i - 1].title,
                                            value: res1[i - 1].index
                                        });
                            		}       
                            		realAppIndex = realAppIndex + 1;
                            	}         
                            } else {
                                // $log.debug('contactUsService:getAvailableApps:: Restricted/URL only App will not be used = ' + res1[i - 1].title);
                            }
                        }       
                    }).catch(err=> {
                        $log.error('contactUsService:error:: ', err);
                        
                       
                    }).finally(() => {
                        //this.isLoadingTable = false;
                    });
            	});
        	}
        	
        	$scope.getContactUsList();
        	
        	$scope.closeDialog = function(){
        		$scope.closeThisDialog( $scope.widgetData);
        	}
        	
        	/*Add new Contact Us*/
        	$scope.newContactUsFun = function(){ 
        		if($scope.newContactUs.app.title==null || $scope.newContactUs.app.title=='' ){
        			confirmBoxService.showInformation('Please select an App to add').then(isConfirmed => {
        				return;
        			});	
        		}
        					
    			if($scope.newContactUs.url !=null && $scope.newContactUs.url != '' && !validateUrl($scope.newContactUs.url)){
    				var warningMsg = "Please enter a valid URL";
    				confirmBoxService.showInformation(warningMsg).then(isConfirmed => {return;});
    				return;
    			}
    			
        		contactUsService.addContactUs($scope.newContactUs).then(res=> {
        			// $log.info('contactUsService: add ContactUs successfully');
        			$scope.contactUsList.length=0;
        			// $log.info('contactUsService: refreshing the Contact US table');
        			$scope.getContactUsList();
        			$scope.errMsg='';
        			/*	confirmBoxService.showInformation('You have added a new Contact Us item').then(isConfirmed => {	});*/
        			var defaultSel={
    						index: 0,
                            title: '',
                            value: ''
    				}
    				$scope.newContactUs ={
    	        			app:defaultSel,       			
    	        			name:'',
    	        			email:'',
    	        			url:'',
    	        			desc:''       			
    	        	};

                }).catch(err=> {
                    $log.error('contactUsService: addContactUs error:: ', err);
                   // $scope.errMsg=err;
                    confirmBoxService.showInformation('Add Contact Us list failed: ' + err);
                   
                }).finally(() => {
                    //this.isLoadingTable = false;
                });
        	}
        	/* Edit Contact Us*/
        	$scope.editContactUsFun = function(contactObj){
    			// $log.info('contactUsService: edit ContactUs save successfully', contactObj);    			
    			var contactUsObj={
                		appId:contactObj.appId,
                		appName:contactObj.appName,
                		description:contactObj.description,
                		contactName:contactObj.contactName,
                		contactEmail:contactObj.contactEmail,
                		url:contactObj.url,       		
                };
    			
    			contactUsService.modifyContactUs(contactUsObj).then(res=> {
        			// $log.info('contactUsService: edit ContactUs successfully');
    				//	confirmBoxService.showInformation('You have saved the changes').then(isConfirmed => {});
        			$scope.errMsg='';
        			
                }).catch(err=> {
                    $log.error('contactUsService: editContactUs error:: ', err);
                    confirmBoxService.showInformation('Edit Contact Us list failed: ' + err);
                   // $scope.errMsg=err;
                }).finally(() => {
                    //this.isLoadingTable = false;
                });
        		
        	}
        	
        	$scope.$watch('newContactUs.app.value', (newVal, oldVal) => {
        		for(var i=0;i<$scope.contactUsAllAppList.length;i++){
        		if($scope.contactUsAllAppList[i].value==newVal){
        		$scope.newContactUs.app=angular.copy($scope.contactUsAllAppList[i]);;
        		}
        		}
        		});
        	/*del Contact Us*/
        	$scope.delContactUsFun = function(appObj){
        		var confirmMsg = 'Are you sure you want to delete '+appObj.appName +' from the list?' + ' Press OK to delete.';
        		confirmBoxService.confirm(confirmMsg).then(function (confirmed) {
                    if (confirmed == true) {                     	
                    	contactUsService.removeContactUs(appObj.appId).then(res=> {   
                    		// $log.info('delContactUsFun: delete ContactUs successfully',res);
                			$scope.errMsg='';
                			$scope.contactUsList.length=0;
                			$scope.getContactUsList();
                			confirmBoxService.showInformation('Item has been deleted').then(isConfirmed => {});
                        }).catch(err=> {
                            $log.error('contactUsService: addContactUs error:: ', err);
                            confirmBoxService.showInformation('Deletion failed: ' + err);
                           // $scope.errMsg=err;
                        }).finally(() => {
                            //this.isLoadingTable = false;
                        });
                    }
                });
        		
        	}	
        	
        }        
    }
    ContactUsManageController.$inject = ['$scope','$log',  'message', '$q', '$http', 'conf','contactUsService','confirmBoxService'];
    angular.module('ecompApp').controller('ContactUsManageController', ContactUsManageController);

 
})();
function validateUrl(value){
    return /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i.test(value);
  }
