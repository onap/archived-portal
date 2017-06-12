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
    class EditProfileModalCtrl {
        constructor($log,ngDialog,usersService,confirmBoxService,adminsService) {
        	this.firstName ='';
        	this.middleName ='';
        	this.lastName ='';
        	this.email ='';
        	this.loginId ='';
        	this.loginPwd ='';
        	this.confirmLoginPwd=''
        	this.isLoading = false;
        	let getUser  = () => {
        		this.isLoading = true;
        		usersService.getLoggedInUser()
            	.then(user=> {
            		var data = user.response;
            		console.log(data);
            		this.firstName =data.firstName;
            		this.middleName =data.middleName;
                	this.lastName =data.lastName;
                	this.email =data.email;
                	this.loginId =data.loginId;
                	this.loginPwd =data.loginPassword;
                	this.isLoading = false;
            	}).catch(err=> {
            		$log.error('EditProfileModalCtrl.getUser:: Error retrieving ECMOP portal user: ' + err);
            	});
            }
        	getUser();
        	
        	this.save = ()=>{
        		var profileDetail ={
        				firstName :	this.firstName,
                		middleName :this.middleName,
                    	lastName :this.lastName,
                    	email :this.email,
                    	loginId :this.loginId,
                    	loginPassword :this.loginPwd
            		}
        		if (this.firstName =='' || this.lastName == '' || this.email == '' || this.loginId =='' || this.loginPwd ==''|| this.confirmLoginPwd ==''){
        			var warningMsg = "Please enter a value for all fields marked with *.";
        			confirmBoxService.showInformation(warningMsg).then(isConfirmed => {return;});
        			return;
        		} else if (this.loginPwd != this.confirmLoginPwd) {
        			var warningMsg = "Passwords do not match, please try again.";
        			confirmBoxService.showInformation(warningMsg).then(isConfirmed => {return;});
        			return;
        		} else {
        			// check password length complexity.
        			var warningMsg = adminsService.isComplexPassword(this.loginPwd);
        			if (warningMsg != null) {
        				confirmBoxService.showInformation(warningMsg).then(isConfirmed => {return;});
        				return;
        			}
        		}
        		usersService.modifyLoggedInUser(profileDetail).then(res=> {
        			confirmBoxService.showInformation("Profile detail updated").then(isConfirmed => {return;});
        		}).catch(err=> {
        			$log.error('EditProfileModalCtrl.getUser:: Error retrieving ECMOP portal user: ' + err);
        			confirmBoxService.showInformation("Error while updating profile detail: "+ err).then(isConfirmed => {return;});
        		});
        	}
        }
    }
    EditProfileModalCtrl.$inject = ['$log', 'ngDialog','usersService','confirmBoxService','adminsService'];
    angular.module('ecompApp').controller('EditProfileModalCtrl', EditProfileModalCtrl);
})();
