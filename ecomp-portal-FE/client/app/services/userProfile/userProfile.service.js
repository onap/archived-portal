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
        constructor($q, $log, $http, conf, uuid,$rootScope) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.userProfile = null;
            this.uuid = uuid;
            this.$rootScope = $rootScope;
        }


        broadCastUpdatedUserInfo() {
            this.$rootScope.$broadcast('handleUpdateUserInfo');
        }

        refreshUserBusinessCard(){
            this.$rootScope.$broadcast('refreshUserBusinessCard');
        }

        getUserProfile() {
            this.$log.info('UserProfileService::getUserProfile: get logged user profile');
            let deferred = this.$q.defer();
            let url = this.conf.api.userProfile;
            // if(this.userProfile){
            //     return deferred.resolve(this.userProfile);
            // }
            this.$http({
                    method: "GET",
                    url: url,
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                	// this.$log.debug('Profile-service::getUserProfile: res: ', res);
                    if (Object.keys(res.data).length == 0) {
                        deferred.reject('UserProfileService::getUserProfile unexpected result: ', res);
                    } else {
                        this.userProfile = res.data;
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
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
        
        getCurrentUserProfile(loginId) {
            this.$log.info('UserProfileService::getCurrentUserProfile: get logged user profile');
            let deferred = this.$q.defer();
            let url = this.conf.api.currentUserProfile + '/' + loginId;
            // if(this.userProfile){
            //     return deferred.resolve(this.userProfile);
            // }
            this.$http({
                    method: "GET",
                    url: url,
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                	// this.$log.debug('Profile-service::getUserProfile: res: ', res);
                    if (Object.keys(res.data).length == 0) {
                        deferred.reject('UserProfileService::CurrentgetUserProfile unexpected result: ', res);
                    } else {
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }
        
        updateRemoteUserProfile(loginId,appId) {
            this.$log.info('UserProfileService::updateRemoteUserProfile: update remote user profile');
            let deferred = this.$q.defer();
            let url = this.conf.api.updateRemoteUserProfile + "?loginId=" + loginId + '&appId=' + appId;
            // if(this.userProfile){
            //     return deferred.resolve(this.userProfile);
            // }
            this.$http({
                    method: "GET",
                    url: url,
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                	// this.$log.debug('Profile-service::getUserProfile: res: ', res);
                    if (Object.keys(res.data).length == 0) {
                        deferred.reject('UserProfileService::update remote user profile unexpected result: ', res);
                    } else {
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }
        
        getFunctionalMenuStaticInfo() {
            this.$log.info('UserProfileService::getFunctionalMenuStaticInfo: get logged user profile');
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
                    if (Object.keys(res.data).length == 0) {
                        deferred.reject("UserProfileService::getFunctionalMenuStaticInfo Failed");
                    } else {
                        this.userProfile = res.data;
                        this.$log.info('UserProfileService::getFunctionalMenuStaticInfo Succeeded');
                        deferred.resolve(res.data);
                    }
                }).catch( status => {
                    this.$log.error('UserProfileService::getFunctionalMenuStaticInfo error');
                    deferred.reject(status);
                });
            return deferred.promise;
        }

        resetFunctionalMenuStaticInfo() {
            this.$log.info('UserProfileService::getFunctionalMenuStaticInfo: get logged user profile');
            let deferred = this.$q.defer();
            let url = this.conf.api.resetFunctionalMenuStaticInfo;

            this.$http({
                    method: "GET",
                    url : url,
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                }).then( res => {
                    if (res.data.message != "success") {
                        deferred.reject("UserProfileService::resetFunctionalMenuStaticInfo Failed");
                    } else {
                        this.userProfile = res.data;
                        this.$log.info('UserProfileService::resetFunctionalMenuStaticInfo Succeeded');
                        deferred.resolve(res.data);
                    }
                }).catch( status => {
                    this.$log.error('UserProfileService::resetFunctionalMenuStaticInfo error');
                    deferred.reject(status);
                });
            return deferred.promise;
        }


        getActiveUser() {	
            this.$log.info('ecomp::getActiveUser::get active users');
            let deferred = this.$q.defer();
            let url = this.conf.api.getActiveUser;
            
            this.$http({
                    method: "GET",
                    url : url,
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                }).then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                    if (Object.keys(res.data).length == 0) {
                        deferred.reject("ecomp::userProfile-service::getActiveUser Failed");
                    } else {
                        this.$log.info('ecomp::userProfile-service::getActiveUser Succeeded');
                        deferred.resolve(res.data);
                    }
                }).catch( status => {
                    this.$log.error('ecomp::getActiveUser error');
                    deferred.reject(status);
                });
            return deferred.promise;
        }
        
    }
    UserProfileService.$inject = ['$q', '$log', '$http', 'conf', 'uuid4', '$rootScope'];
    angular.module('ecompApp').service('userProfileService', UserProfileService)
})();
