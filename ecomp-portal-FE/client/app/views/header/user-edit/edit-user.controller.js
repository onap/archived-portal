/*-
 * ================================================================================
 * eCOMP Portal
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
    class EditUserController {
        constructor($scope,$log, message, $q, $http, conf,contactUsService,confirmBoxService,userProfileService,adminsService) {
        	
        	$scope.newUser ={
        			firstName:'',
        			lastName:'',
        			emailAddress:'',
        			middleName:'',
        			loginId:'',
        			loginPwd:'',
        	};
        	
        	$scope.userId = $scope.ngDialogData.loginId;
        	$scope.updateRemoteApp = $scope.ngDialogData.updateRemoteApp;
        	try {
            	userProfileService.getCurrentUserProfile($scope.userId)
                .then(res=> {
              	   $scope.newUser.firstName = res.firstName;
              	   $scope.newUser.lastName = res.lastName;
              	   $scope.newUser.emailAddress = res.email;
              	   $scope.newUser.middleName = res.middleInitial;
              	   $scope.newUser.loginId = res.loginId;
              	   $scope.newUser.loginPwd = res.loginPwd;
              	   $scope.newUser.loginPwdCheck = res.loginPwd;
              	   
                }).catch(err=> {
              	  $log.error('HeaderCtrl::LoginSnippetCtrl:get Current User Profile error: ' + err);
                });
            } catch (err) {
                $log.error('HeaderCtrl::LoginSnippetCtrl:getFunctionalMenuStaticInfo failed: ' + err);
            }       	
        	
            $scope.changePwdText = function(){
            	$scope.newUser.loginPwdCheck = '';
            }
        	
        	$scope.closeDialog = function(){
        		$scope.closeThisDialog( $scope.widgetData);
        	}
        	
        	$scope.updateUserFun = function(){
        	
        	   if ($scope.newUser.firstName==''||$scope.newUser.lastName==''||$scope.newUser.loginPwd=='') {
					var warningMsg = "Please enter missing values";
	   				confirmBoxService.showInformation(warningMsg).then(isConfirmed => {return;});
					return;
			   } else if ($scope.newUser.loginPwd != $scope.newUser.loginPwdCheck) {
        		   var warningMsg = "Passwords do not match, please try again.";
        		   confirmBoxService.showInformation(warningMsg).then(isConfirmed => {return;});
        		   return;
        	   } else {
        		   
        		   // check password length complexity.
        		   var warningMsg = adminsService.isComplexPassword($scope.newUser.loginPwd);
        		   if (warningMsg != null) {
        			   confirmBoxService.showInformation(warningMsg).then(isConfirmed => {return;});
        			   return;
        		   }
        		
        	        adminsService.addNewUser($scope.newUser,'No').then(res=> {
        		       
	        		       $scope.closeThisDialog();
			                
		            	   confirmBoxService.showInformation('Update User Info successfully');
						   userProfileService.broadCastUpdatedUserInfo();

		            	   
		            	   if($scope.updateRemoteApp){
			             		  //need update remote app's database
			             		  var remoteAppId = $scope.ngDialogData.appId;
			             		  if(remoteAppId!=null){
			             			          			  
			             			   userProfileService.updateRemoteUserProfile($scope.userId,remoteAppId).then(res=> {
			                		          		                
			        	            	 
			        		               }).catch(err=> {
			        		                   $log.error('userProfileService: update user profile in remote app  error:: ', err);
			        		                   confirmBoxService.showInformation('Update User in remote app failed: ' + err);
			        		                  
			        		               }).finally(() => {
			        		            	   
			        		               });
			             		  
			             		  
			             		  }
			             	   
			               }
	            	 
		               }).catch(err=> {
		                   $log.error('adminsService: addNewUser error:: ', err);
		                  // $scope.errMsg=err;
		                   confirmBoxService.showInformation('Add New User failed: ' + err);
		                  
		               }).finally(() => {
		                   //this.isLoadingTable = false;
		            	   
		               });
        		
        	}
         }
        	
        	
        }        
    }
    EditUserController.$inject = ['$scope','$log',  'message', '$q', '$http', 'conf','contactUsService','confirmBoxService','userProfileService','adminsService'];
    angular.module('ecompApp').controller('editUserController', EditUserController);

 
})();
function validateUrl(value){
    return /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i.test(value);
  }