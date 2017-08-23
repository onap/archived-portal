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
    class UserProfileService {
    	
        constructor($q, $log, $http, conf, uuid, utilsService) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.uuid = uuid;
            this.utilsService = utilsService;
            this.userProfile = null;
            this.debug = false;
        }

        getUserProfile() {
            if (this.debug)
            	this.$log.debug('UserProfileService::getUserProfile: get logged user profile');
            let deferred = this.$q.defer();
            let url = this.conf.api.userProfile;
            this.$http({
                    method: "GET",
                    url: url,
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                	if (this.debug)
                		this.$log.debug('UserProfileService::getUserProfile: result is ' + JSON.stringify(res));
                    // Res is always JSON, but the data object might be an HTML error page.
                    if (! this.utilsService.isValidJSON(res.data)) {
                    	var msg = 'UserProfileService::getUserProfile: result data is not JSON';
                    	if (this.debug)
                    		this.$log.debug(msg);
                    	deferred.reject(msg);
                    } else {
                    	if (this.debug)
                    		this.$log.debug('UserProfileService::getUserProfile: success');
                    	this.userProfile = res.data;
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                    this.$log.error('UserProfileService::getUserProfile caught exception: ' + JSON.stringify(status));
                    deferred.reject(status);
                });
            return deferred.promise;
        }
        
        getSortedUserIdCombination(user1, user2) {
        	
        	var combination = "";
        	if(user1<user2) {
        		combination =  user1+"-"+user2;
        	} else if (user1>user2){
        		combination =  user2+"-"+user1;
        	} else {
        		
        		return "self";
        	}
        	
        	var collaborateUrl =  'opencollaboration?chat_id=' + combination ;
        	
        	return collaborateUrl;
        	
        	
        	
        }
        
        getFunctionalMenuStaticInfo() {
        	if (this.debug)
        		this.$log.debug('UserProfileService::getFunctionalMenuStaticInfo: start');
            let deferred = this.$q.defer();
            let url = this.conf.api.functionalMenuStaticInfo;
            this.$http({
                    method: "GET",
                    url : url,
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                }).then( res => {
                	if (this.debug)
                		this.$log.debug('UserProfileService::getFunctionalMenuStaticInfo: result is ' + JSON.stringify(res));
                    // Res is always JSON, but the data object might be an HTML error page.
                    if (! this.utilsService.isValidJSON(res.data)) {
                    	var msg = 'UserProfileService::getFunctionalMenuStaticInfo: result data is not JSON';
                    	if (this.debug)
                    		this.$log.debug(msg);
                    	deferred.reject(msg);
                    } else {
                        if (this.debug)
                        	this.$log.debug('UserProfileService::getFunctionalMenuStaticInfo Succeeded');
                        deferred.resolve(res.data);
                    }
                }).catch( status => {
                    this.$log.error('UserProfileService::getFunctionalMenuStaticInfo caught exception: ' + JSON.stringify(status));
                    deferred.reject(status);
                });
            return deferred.promise;
        }
        
        getActiveUser() {	
            if (this.debug)
            	this.$log.debug('UserProfileService::getActiveUser: start');
            let deferred = this.$q.defer();
            let url = this.conf.api.getActiveUser;           
            this.$http({
                    method: 'GET',
                    url : url,
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':  this.uuid.generate()
                    }
                }).then( res => {
                	if (this.debug) {
                		this.$log.debug('UserProfileService::getActiveUser: result is ' + JSON.stringify(res));
                		this.$log.debug('UserProfileService::getActiveUser: isValidJSON is ' + this.utilsService.isValidJSON(res.data));
                	}
                    // Res is always JSON, but the data object might be an HTML error page.
                	// res.data should be a list of Org IDs; an empty list is NOT an error.
                    if (! this.utilsService.isValidJSON(res.data)) {
                    	var msg = 'UserProfileService::getActiveUser: result data is not JSON';
                    	if (this.debug)
                    		this.$log.debug(msg);
                    	deferred.reject(msg);
                    }
                    else {
                    	if (this.debug)
                    		this.$log.debug('UserProfileService::getActiveUser: success');
                        deferred.resolve(res.data);
                    }
                }).catch( status => {
                    this.$log.error('UserProfileService::getActiveUser caught exception: ' + JSON.stringify(status));
                    deferred.reject(status);
                });
            return deferred.promise;
        }
        
        
        getUserAppRoles(userId) {
            if (this.debug)
            	this.$log.debug('UserProfileService::getUserAppRoles: get logged user profile');
            let deferred = this.$q.defer();
            this.$http({
                    method: "GET",
                    url: this.conf.api.userApplicationRoles,
                    params: {userId:userId},
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                	if (this.debug)
                		this.$log.debug('UserProfileService::getUserAppRoles: result is ' + JSON.stringify(res));
                    // Res is always JSON, but the data object might be an HTML error page.
                    if (! this.utilsService.isValidJSON(res.data)) {
                    	var msg = 'UserProfileService::getUserAppRoles: result data is not JSON';
                    	if (this.debug)
                    		this.$log.debug(msg);
                    	deferred.reject(msg);
                    } else {
                    	if (this.debug)
                    		this.$log.debug('UserProfileService::getUserAppRoles: success');
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                    this.$log.error('UserProfileService::getUserAppRoles caught exception: ' + JSON.stringify(status));
                    deferred.reject(status);
                });

            return deferred.promise;
        }
        
  
        
    }
    UserProfileService.$inject = ['$q', '$log', '$http', 'conf', 'uuid4', 'utilsService'];
    angular.module('ecompApp').service('userProfileService', UserProfileService)
})();
