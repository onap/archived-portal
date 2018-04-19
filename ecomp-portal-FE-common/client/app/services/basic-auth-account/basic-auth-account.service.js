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
 * 
 */
'use strict';

(function () {
    class BasicAuthAccountService {
        constructor($q, $log, $http, conf,uuid, utilsService) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.uuid = uuid;
            this.utilsService = utilsService;   
        }
        
        createAccount(newAccount) {
        	let deferred = this.$q.defer();
        	this.$http({
                method: "POST",
                url: this.conf.api.basicAuthAccount,
                data: newAccount,
                cache: false,
                headers:{
            		'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
            	if (res == null || res.data == null) {
        	 		this.$log.error('BasicAuthAccountService::createAccount Failed: Result or result.data is null');
                     deferred.reject("BasicAuthAccountService::createAccount Failed: Result or result.data is null");
                } else if (!this.utilsService.isValidJSON(res.data)) {
                 	this.$log.error('BasicAuthAccountService::createAccount Failed: Invalid JSON format');
                    deferred.reject("BasicAuthAccountService::createAccount Failed: Invalid JSON format");
                } else {
                    deferred.resolve(res.data);
                }
            })
            .catch(errRes => {
                deferred.reject(errRes);
            });
            return deferred.promise;
        }
        
        updateAccount(accountId, newAccount) {
        	let deferred = this.$q.defer();
        	this.$http({
                method: "PUT",
                url: this.conf.api.basicAuthAccount + "/" + accountId,
                data: newAccount,
                cache: false,
                headers:{
            		'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
            	if (res == null || res.data == null) {
        	 		this.$log.error('BasicAuthAccountService::updateAccount Failed: Result or result.data is null');
                     deferred.reject("BasicAuthAccountService::updateAccount Failed: Result or result.data is null");
                } else if (!this.utilsService.isValidJSON(res.data)) {
                 	this.$log.error('BasicAuthAccountService::updateAccount Failed: Invalid JSON format');
                    deferred.reject("BasicAuthAccountService::updateAccount Failed: Invalid JSON format");
                } else {
                    deferred.resolve(res.data);
                }
            })
            .catch(errRes => {
                deferred.reject(errRes);
            });
            return deferred.promise;
        }
        
        getAccountList() {
        	let deferred = this.$q.defer();
        	this.$http({
                method: "GET",
                url: this.conf.api.basicAuthAccount,
                cache: false,
                headers:{
            		'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
            	if (res == null || res.data == null) {
        	 		this.$log.error('BasicAuthAccountService::getAccountList Failed: Result or result.data is null');
                     deferred.reject("BasicAuthAccountService::getAccountList Failed: Result or result.data is null");
                } else if (!this.utilsService.isValidJSON(res.data)) {
                 	this.$log.error('BasicAuthAccountService::getAccountList Failed: Invalid JSON format');
                    deferred.reject("BasicAuthAccountService::getAccountList Failed: Invalid JSON format");
                } else {
                    deferred.resolve(res.data.response);
                }
            })
            .catch(errRes => {
                deferred.reject(errRes);
            });
            return deferred.promise;
        }
        
        deleteAccount(accountId) {
        	let deferred = this.$q.defer();
        	this.$http({
                method: "DELETE",
                url: this.conf.api.basicAuthAccount + "/" + accountId,
                cache: false,
                headers:{
            		'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
            	if (res == null || res.data == null) {
        	 		this.$log.error('BasicAuthAccountService::deleteAccount Failed: Result or result.data is null');
                     deferred.reject("BasicAuthAccountService::deleteAccount Failed: Result or result.data is null");
                } else if (!this.utilsService.isValidJSON(res.data)) {
                 	this.$log.error('BasicAuthAccountService::deleteAccount Failed: Invalid JSON format');
                    deferred.reject("BasicAuthAccountService::deleteAccount Failed: Invalid JSON format");
                } else {
                    deferred.resolve(res.data);
                }
            })
            .catch(errRes => {
                deferred.reject(errRes);
            });
            return deferred.promise;
        }
       
    }
    
    BasicAuthAccountService.$inject = ['$q', '$log', '$http', 'conf','uuid4', 'utilsService'];
    angular.module('ecompApp').service('basicAuthAccountService', BasicAuthAccountService)
})();
