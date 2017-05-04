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
    class SearchUsersCtrl {

        constructor($log, usersService,adminsService, $scope,confirmBoxService) {

            $scope.UserSearchsIsNull=false;
            $scope.userExist = false;
            this.scrollApi = {};//scrollTop directive
            $scope.txtResults = 'result';
             
            this.showAddUser = false;
            this.showSearch = true;
            this.newUser ={
        			firstName:'',
        			lastName:'',
        			emailAddress:'',

        			middleName:'',
        			loginId:'',
        			loginPwd:'',
                    loginPwdCheck:''
        	};

            let activeRequests = [];
            let clearReq = (req) => {
                activeRequests.splice(activeRequests.indexOf(req), 1);
            };
           
            this.showAddUserSection = () => {
            	 this.showAddUser = true;
	           	 this.showSearch = false;
            }
            
           this.addNewUserFun = () => {
        	   if (this.newUser.loginId =='' || this.newUser.loginPwd == '' || this.newUser.firstName == '' || this.newUser.lastName =='' || this.newUser.emailAddress ==''||this.newUser.loginPwd ==''){
        		   var warningMsg = "Please enter a value for all fields marked with *.";
        		   confirmBoxService.showInformation(warningMsg).then(isConfirmed => {return;});
        		   return;
        	   } else if (this.newUser.loginPwd != this.newUser.loginPwdCheck) {
        		   var warningMsg = "Passwords do not match, please try again.";
        		   confirmBoxService.showInformation(warningMsg).then(isConfirmed => {return;});
        		   return;
        	   }
        	   else {
        		   // check password length complexity.
        		   var warningMsg = adminsService.isComplexPassword(this.newUser.loginPwd);
        		   if (warningMsg != null) {
        			   confirmBoxService.showInformation(warningMsg).then(isConfirmed => {return;});
        			   return;
        		   }
        	   } // password

        	   adminsService.addNewUser(this.newUser,'Yes').then(res=> {
	       			
	   				if(res.message == 'Record already exist'){
	   					
	   					this.showAddUser = true;
	   	           	    this.showSearch = false;
	   					$scope.userExist = true;
	   					
	   				}else{
	   					
	   					$scope.userExist = false;
	   					this.selectedUser = this.newUser;
		   				this.selectedUser.orgUserId = this.newUser.loginId;
		   				this.searchUsersResults = [];
		   				this.searchUsersResults.push(this.newUser);
		   				this.showAddUser = false;
		   				this.showSearch = true;
		   				this.newUser ={
		               			firstName:'',
		               			lastName:'',
		               			emailAdress:'',
		               			middleName:'',
		               			loginId:'',
		               			loginPwd:'',
	                            loginPwdCheck:''
		               	};
                        this.searchUserString ='';
		   				$scope.UserSearchsIsNull = false;	   					
	   				}
	   				
	
	               }).catch(err=> {
	                   $log.error('adminsService: addNewUser error:: ', err);
	                  // $scope.errMsg=err;
	                   confirmBoxService.showInformation('Add New User failed: ' + err);
	                  
	               }).finally(() => {
	                   //this.isLoadingTable = false;
	            	   
	               });
           }
           
            this.searchUsers = () => {
                this.isLoading = true;
                if(this.searchUsersInProgress){
                    return;
                }
                this.selectedUser = null;
                this.searchUsersInProgress = true;
                this.searchUsersResults = null;

                let searchUsersReq = usersService.searchUsers(this.searchUserString);
                activeRequests.push(searchUsersReq);
                searchUsersReq.promise().then(usersList => {
                    $log.debug('searchUsers found the following users: ', JSON.stringify(usersList));
                    this.searchUsersResults = usersList;
                    $log.debug('searchUsersResults length: ', usersList.length);
                    if (usersList.length != 1) {
                        $scope.txtResults = 'results'
                    } else {
                        $scope.txtResults = 'result'
                    }
                    $scope.UserSearchsIsNull=false;
                }).catch(err => {
                    $log.error('SearchUsersCtrl.searchUsers: ' + err);
                    $scope.UserSearchsIsNull=true;
                }).finally(() => {
                    this.scrollApi.scrollTop();
                    this.searchUsersInProgress = false;
                    clearReq(searchUsersReq);
                    this.isLoading = false;
                });
            };

            let init = () => {
                this.isLoading = false;
                this.searchUsersInProgress = false;
            };

            this.setSelectedUser = user => {
                this.selectedUser = user;
            };

            init();

            $scope.$on('$destroy', () => {
                activeRequests.forEach(req => {
                    req.cancel();
                });
            });
        }
    }
    SearchUsersCtrl.$inject = ['$log', 'usersService','adminsService', '$scope','confirmBoxService'];
    angular.module('ecompApp').controller('SearchUsersCtrl', SearchUsersCtrl);
})();
