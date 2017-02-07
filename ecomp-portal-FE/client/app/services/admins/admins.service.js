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
    class AdminsService {
        constructor($q, $log, $http, conf,uuid) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.uuid = uuid;
        }

        getAccountAdmins() {
            let deferred = this.$q.defer();
            this.$log.info('AdminsService::get all applications admins list');
            this.$http({
                    method: "GET",
                    cache: false,
                    url: this.conf.api.accountAdmins,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                    if (Object.keys(res.data).length == 0) {
                        deferred.reject("AdminsService::getAccountAdmins Failed");
                    } else {
                        this.$log.info('AdminsService::getAccountAdmins Succeeded');
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                    deferred.reject(status);
                });
           
            return deferred.promise;
        }

        getAdminAppsRoles(orgUserId) {
            let deferred = this.$q.defer();
            this.$log.info('AdminsService::getAdminAppsRoles.adminAppsRoles');

            this.$http({
                method: "GET",
                url: this.conf.api.adminAppsRoles,
                params: {orgUserId: orgUserId},
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then( res => {
                    if (Object.keys(res.data).length == 0) {
                        deferred.reject("AdminsService::getAdminAppsRoles.adminAppsRoles Failed");
                    } else {
                        this.$log.info('AdminsService::getAdminAppsRoles.adminAppsRoles Succeeded');
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                    deferred.reject(status);
                });

            return deferred.promise;
        }

        updateAdminAppsRoles(newAdminAppRoles) {
            let deferred = this.$q.defer();
            this.$log.info('AdminsService::updateAdminAppsRoles');
            this.$http({
                method: "PUT",
                url: this.conf.api.adminAppsRoles,
                data: newAdminAppRoles,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then( res => {
                    deferred.resolve(res.data);
                })
                .catch( status => {
                    deferred.reject(status);
                });

            return deferred.promise;
        }
        
        addNewUser(newUser,checkDuplicate) {
        	// this.$log.info(newContactUs)
        	let deferred = this.$q.defer();
            // this.$log.info('ContactUsService:: add Contact Us' + JSON.stringify(newContactUs));
            
            var newUserObj={
            		firstName:newUser.firstName,
            		middleInitial:newUser.middleName,
            		lastName:newUser.lastName,
            		email:newUser.emailAddress,
            		loginId:newUser.loginId,
            		loginPwd:newUser.loginPwd,            		
            };
            this.$http({
                url: this.conf.api.saveNewUser + "?isCheck=" + checkDuplicate,
                method: 'POST',
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                },
                data: newUserObj
            }).then(res => {
                // this.$log.info('ContactUsService:: add Contact Us res' ,res);
                // If response comes back as a redirected HTML page which IS NOT a success
                if (res==null || Object.keys(res.data).length == 0 || res.data.message == 'failure') {
                    deferred.reject("Add new User failed");
                    this.$log.error('adminService:: add New User failed');
                } else {
                    deferred.resolve(res.data);
                }
            }).catch(errRes => {
                   deferred.reject(errRes);
             });
            return deferred.promise;
        }
                
        /**
         * Tests the specified password against complexity requirements.
         * Returns an explanation message if the test fails; null if it passes.
         */
        isComplexPassword(str) {
        	let minLength = 8;
        	let message = 'Password is too simple.  Minimum length is '+ minLength + ', '
        				  + 'and it must use letters, digits and special characters.';
        	if (str == null)
        		return message;

        	let hasLetter = false;
        	let hasDigit = false;
        	let hasSpecial = false;
        	var code, i, len;
        	for (i = 0, len = str.length; i < len; i++) {
        		code = str.charCodeAt(i);
        		if (code > 47 && code < 58) // numeric (0-9)
        			hasDigit = true;
        		else if ((code > 64 && code < 91) || (code > 96 && code < 123)) // A-Z, a-z
        			hasLetter = true;
        		else
        			hasSpecial = true;
        	} // for

        	if (str.length < minLength || !hasLetter || !hasDigit || !hasSpecial)
        		return message;
        	
        	// All is well.
        	return null;
        }

    }
    AdminsService.$inject = ['$q', '$log', '$http', 'conf','uuid4'];
    angular.module('ecompApp').service('adminsService', AdminsService)
})();
