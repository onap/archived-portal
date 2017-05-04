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
/**
 * Created by nnaffar on 11/22/2015.
 */
'use strict';

(function () {
    class AdminsService {
        constructor($q, $log, $http, conf, uuid, utilsService) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.uuid = uuid;
            this.utilsService = utilsService;
        }

        getAccountAdmins() {
            let deferred = this.$q.defer();
            //this.$log.info('AdminsService::get all applications admins list');
            this.$http({
                    method: "GET",
                    cache: false,
                    url: this.conf.api.accountAdmins,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                    if (this.utilsService.isValidJSON(res)=== false) {
                    	this.$log.error('AdminsService::getAccountAdmins Failed');
                        deferred.reject("AdminsService::getAccountAdmins Failed");
                    } else {
                        // this.$log.info('AdminsService::getAccountAdmins Succeeded');
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                	this.$log.error('AdminsService::getAccountAdmins Failed', status);
                    deferred.reject(status);
                });
           
            return deferred.promise;
        }

        getAdminAppsRoles(orgUserId) {
            let deferred = this.$q.defer();
            //this.$log.info('AdminsService::getAdminAppsRoles.adminAppsRoles');

            this.$http({
                method: "GET",
                url: this.conf.api.adminAppsRoles,
                params: {user: orgUserId},
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                    if (this.utilsService.isValidJSON(res)=== false) {
                        this.$log.error('AdminsService::getAdminAppsRoles.adminAppsRoles Failed');
                        deferred.reject("AdminsService::getAdminAppsRoles.adminAppsRoles Failed");
                    } else {
                    	// this.$log.info('AdminsService::getAdminAppsRoles.adminAppsRoles Succeeded');
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                	this.$log.error('AdminsService::getAdminAppsRoles.adminAppsRoles Failed', status);
                    deferred.reject(status);
                });

            return deferred.promise;
        }
        /*Author: Rui*/
        getRolesByApp(appId) {
            let deferred = this.$q.defer();
            this.$log.info('AdminsService::getRolesByApp');
            let url = this.conf.api.adminAppsRoles + '/' + appId;
            this.$http({
                method: "GET",
                url: url,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                    if (Object.keys(res.data).length == 0) {
                        deferred.reject("AdminsService::getAdminAppsRoles.getRolesByApp Failed");
                    } else {
                        this.$log.info('AdminsService::getAdminAppsRoles.getRolesByApp Succeeded');
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
            // this.$log.info('AdminsService::updateAdminAppsRoles');
            this.$http({
                method: "PUT",
                url: this.conf.api.adminAppsRoles,
                data: newAdminAppRoles,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then( res => {
                if (this.utilsService.isValidJSON(res)=== false) {
                    this.$log.error('AdminsService::updateAdminAppsRoles Failed');
                    deferred.reject("AdminsService::updateAdminAppsRoles Failed");
                } else {
                    //this.$log.info('AdminsService::updateAdminAppsRoles success:');
                    deferred.resolve(res.data);
                }

                })
                .catch( status => {
                    this.$log.error('AdminsService::updateAdminAppsRoles: rejection:' + status);
                    deferred.reject(status);
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
        
    }
    AdminsService.$inject = ['$q', '$log', '$http', 'conf','uuid4', 'utilsService'];
    angular.module('ecompApp').service('adminsService', AdminsService)
})();
