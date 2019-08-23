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
/**
 * Created by doritrieur on 12/8/15.
 */
'use strict';

(function () {
    class UsersService {
        constructor($q, $log, $http, conf, uuid, utilsService) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.uuid = uuid;
            this.utilsService = utilsService;
        }


        searchUsers(queryString) {
            let canceller = this.$q.defer();
            let isActive = false;

            let cancel = () => {
                if(isActive){
                    this.$log.debug('UsersService::searchUsers: canceling the request');
                    canceller.resolve();
                }
            };

            let promise = () => {
                let deferred = this.$q.defer();
                if(!queryString){
                    return deferred.reject(new Error('query string is mandatory'));
                }
                isActive = true;
                this.$http({
                    method: 'GET',
                    url: this.conf.api.queryUsers,
                    params: {search: queryString},
                    cache: false,
                    timeout: canceller.promise,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                }).then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                    if (this.utilsService.isValidJSON(res)== false) {
                        deferred.reject('UsersService::queryUsers Failed');
                    } else {
                        //this.$log.info('UsersService::queryUsers Succeeded');
                        isActive = false;
                        deferred.resolve(res.data);
                    }
                }).catch( status => {
                    isActive = false;
                    deferred.reject('UsersService::searchUsers:: API Failed with status: ' + status);
                });
                return deferred.promise;
            };

            return {
                cancel: cancel,
                promise: promise
            };

        }

        getAccountUsers(appId) {
            let deferred = this.$q.defer();
            let log = this.$log;
            // this.$log.debug('UsersService::getAccountUsers for appId: ' + appId);

            let url = this.conf.api.accountUsers.replace(':appId', appId);
            this.$http({
                method: 'GET',
                url: url,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                if (this.utilsService.isValidJSON(res)== false) {
                        deferred.reject('UsersService::getAccountUsers for appId Failed');
                    } else {
                        // log.info('UsersService::getAccountUsers(appId) Succeeded');
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                    log.error('getAccountUsers(appId) $http error = ', status);
                    deferred.reject(status);
                });

            return deferred.promise;
        }

        getUserAppRoles(appid, orgUserId, extRequestValue,isSystemUser){
        	let canceller = this.$q.defer();
            let isActive = false;

            let cancel = () => {
                if(isActive){
                    this.$log.debug('UsersService::getUserAppRoles: canceling the request');
                    canceller.resolve();
                }
            };

            let promise = () => {
                let deferred = this.$q.defer();
                isActive = false;
            this.$http({
                method: 'GET',
                url: this.conf.api.userAppRoles,
                params: {user: orgUserId, app: appid, externalRequest: extRequestValue,isSystemUser: isSystemUser},
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then( res => {
                    if (!this.utilsService.isValidJSON(res.data)) {
                        deferred.reject('UsersService::getUserAppRoles: Failed');
                    } else {
                    	isActive = false;
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                	isActive = false;
                    deferred.reject(status);
                });

            return deferred.promise;
            };
            
            return {
                cancel: cancel,
                promise: promise
            };
        }

        updateUserAppRoles(newUserAppRoles) {
//            let deferred = this.$q.defer();
            let canceller = this.$q.defer();
            let isActive = false;

            let cancel = () => {
                if(isActive){
                    this.$log.debug('UsersService::updateUserAppRoles: canceling the request');
                    canceller.resolve();
                }
            };

            // this.$log.info('UsersService::updateUserAppRoles');
            let promise = () => {
                let deferred = this.$q.defer();
            this.$http({
                method: 'PUT',
                url: this.conf.api.userAppRoles,
                data: newUserAppRoles,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then( res => {
                // this.$log.debug('getUserAppRoles response: ', JSON.stringify(res))
                // If response comes back as a redirected HTML page which IS NOT a success
                if (this.utilsService.isValidJSON(res)== false ||  res.data.httpStatusCode == '500' || res.data.status == 'ERROR') {
                    deferred.reject(' Error:'  + res.data.message);
                } else {
                    // this.$log.info('UsersService::updateUserAppRoles: Succeeded');
                    deferred.resolve(res.data);
                }
            })
            .catch( status => {
                deferred.reject(status);
            });

            return deferred.promise;
            };
            
            return {
                cancel: cancel,
                promise: promise
            };

        }
        
        getLoggedInUser () {
        	let deferred = this.$q.defer();
        	this.$http({
        		method: 'GET',
        		url: this.conf.api.loggedinUser,
        		cache: false,
        		headers: {
        			'X-ECOMP-RequestID':this.uuid.generate(),
        			'Content-Type': 'application/json'
                },
                data: ''
        	}).then( res => {
        		if (res.data==null || !this.utilsService.isValidJSON(res.data)) { 			
        			deferred.reject('MenusService::getLoggedInUser rest call failed');
        		} else {
        			if(res.data.status!='OK' && res.data.message!=null)
            			deferred.reject('MenusService::getLoggedInUser rest call failed ' + res.data.message);
        			else
        				deferred.resolve(res.data);
        		}
        	})
        	.catch( status => {
        		this.$log.error('MenusService::getLoggedInUser rejection:' + status);
        		deferred.reject(status);
        	});

        	return deferred.promise;
        }
        
        modifyLoggedInUser (profileDetail) {
        	let deferred = this.$q.defer();
        	this.$http({
        		method: 'PUT',
        		url: this.conf.api.modifyLoggedinUser,
        		cache: false,
        		headers: {
        			'X-ECOMP-RequestID':this.uuid.generate(),
        			'Content-Type': 'application/json'
                },
                data: profileDetail
        	}).then( res => {
        		if (res.data==null || !this.utilsService.isValidJSON(res.data)) { 			
        			deferred.reject('MenusService::getLoggedInUser rest call failed');
        		} else {
        			if(res.data.status!='OK' && res.data.message!=null)
            			deferred.reject('MenusService::getLoggedInUser rest call failed ' + res.data.message);
        			else
        				deferred.resolve(res.data);
        		}
        	})
        	.catch( status => {
        		console.log(status);
        		this.$log.error('MenusService::getLoggedInUser rejection:' + status);
        		deferred.reject(status);
        	});

        	return deferred.promise;
        }

    }
    UsersService.$inject = ['$q', '$log', '$http', 'conf','uuid4', 'utilsService'];
    angular.module('ecompApp').service('usersService', UsersService)
})();