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
 * Created by wl849v on 12/14/2016.
 */
'use strict';
(function () {
    class NotificationService {
    	constructor($q, $log, $http, conf, uuid,utilsService) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.uuid = uuid;
            this.notificationCount = {count:0};
            this.refreshCount = 0;
            this.maxCount = 0;
            this.utilsService = utilsService;            
        }   	
        getNotificationCount() {
        	 return this.notificationCount;
        }
        setNotificationCount(count) {
        	this.notificationCount.count = count;
        }
        getRefreshCount() {
            return this.refreshCount;
        }
        setRefreshCount(count){
            this.refreshCount = count;
        }
        setMaxRefreshCount(count){
            this.maxCount = count;
        }
        decrementRefreshCount(){
            this.refreshCount = this.refreshCount - 1;
        }
        
        getNotificationRate() {
            let deferred = this.$q.defer();
            let url = this.conf.api.notificationUpdateRate;
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
                        deferred.reject("ecomp::NotificationService::getNotificationRate Failed");
                    } else {
                        deferred.resolve(res.data);
                    }
                }).catch( status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }
        
        getNotification(){
    		let deferred = this.$q.defer();
            this.$http({
                    method: "GET",
                    cache: false,
                    url: this.conf.api.getNotifications,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success           	
                    if (this.utilsService.isValidJSON(res)=== false) {
                    	this.$log.error('NotificationService::getNotification Failed');
                        deferred.reject("NotificationService::getNotification Failed");
                    } else {
                        deferred.resolve(res);
                    }
                })
                .catch( status => {
                	this.$log.error('NotificationService::getNotification Failed', status);
                    deferred.reject(status);
                });
           
            return deferred.promise;
    	}
        getNotificationHistory(){
    		let deferred = this.$q.defer();
            this.$http({
                    method: "GET",
                    cache: false,
                    url: this.conf.api.getNotificationHistory,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success           	
                    if (this.utilsService.isValidJSON(res)=== false) {
                    	this.$log.error('NotificationService::getNotification Failed');
                        deferred.reject("NotificationService::getNotification Failed");
                    } else {
                        deferred.resolve(res);
                    }
                })
                .catch( status => {
                	this.$log.error('NotificationService::getNotification Failed', status);
                    deferred.reject(status);
                });
           
            return deferred.promise;
    	}
        
        getAdminNotification(){
    		let deferred = this.$q.defer();
            this.$http({
                    method: "GET",
                    cache: false,
                    url: this.conf.api.getAdminNotifications,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success           	
                    if (this.utilsService.isValidJSON(res)=== false) {
                    	this.$log.error('NotificationService::getAdminNotification Failed');
                        deferred.reject("NotificationService::getAdminNotification Failed");
                    } else {
                        deferred.resolve(res);
                    }
                })
                .catch( status => {
                	this.$log.error('NotificationService::getAdminNotification Failed', status);
                    deferred.reject(status);
                });
           
            return deferred.promise;
    	}

        
        getMessageRecipients(notificationId){
    		let deferred = this.$q.defer();
            this.$http({
                    method: "GET",
                    cache: false,
                   url: this.conf.api.getMessageRecipients+"?notificationId="+notificationId,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success           	
                    if (this.utilsService.isValidJSON(res.data)=== false) {
                    	this.$log.error('NotificationService::getMessageRecipients Failed');
                        deferred.reject("NotificationService::getMessageRecipients Failed");
                    } else {
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                	this.$log.error('NotificationService::getMappedRecipients Failed', status);
                    deferred.reject(status);
                });
           
            return deferred.promise;
    	}
        
        getAppRoleIds(){
    		let deferred = this.$q.defer();
            this.$http({
                    method: "GET",
                    cache: false,
                    url: this.conf.api.getAllAppRoleIds,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success           	
                    if (this.utilsService.isValidJSON(res)=== false) {
                    	this.$log.error('NotificationService::getAppRoleIds Failed');
                        deferred.reject("NotificationService::getAppRoleIds Failed");
                    } else {
                        deferred.resolve(res);
                    }
                })
                .catch( status => {
                	this.$log.error('NotificationService::getAppRoleIds Failed', status);
                    deferred.reject(status);
                });           
            return deferred.promise;
    	}

        getNotificationRoles(notificationId){
    		let deferred = this.$q.defer();
            this.$http({
                    method: "GET",
                    cache: false,
                    url: this.conf.api.getNotificationRoles + '/'+notificationId+'/roles',
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success           	
                    if (this.utilsService.isValidJSON(res)=== false) {
                    	this.$log.error('NotificationService::getAdminNotification Failed');
                        deferred.reject("NotificationService::getAdminNotification Failed");
                    } else {
                        deferred.resolve(res);
                    }
                })
                .catch( status => {
                	this.$log.error('NotificationService::getAdminNotification Failed', status);
                    deferred.reject(status);
                });           
            return deferred.promise;
    	}

        addAdminNotification(newAdminNotif){
            let deferred = this.$q.defer();
            // this.$log.debug('applications-service::addOnboardingApp with:', newApp);

            this.$http({
                method: "POST",
                url: this.conf.api.saveNotification,
                data: newAdminNotif,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                	// But don't declare an empty list to be an error.
                    if (res == null || res.data == null) {
                        deferred.reject("NotificationService::addAdminNotification Failed");
                    } else {
                        // this.$log.info('NotificationService::addAdminNotification Succeeded');
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }

        updateAdminNotification(adminNotif){
            let deferred = this.$q.defer();
            this.$http({
                method: "POST",
                url: this.conf.api.saveNotification,
                data: adminNotif,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                	// But don't declare an empty list to be an error.
                    if (res == null || res.data == null) {
                        deferred.reject("NotificationService::updateAdminNotification Failed");
                    } else {
                        // this.$log.info('NotificationService::updateAdminNotification Succeeded');
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }

        setNotificationRead(notificationId){
        	let deferred = this.$q.defer();
            this.$http({
                    method: "GET",
                    cache: false,
                    url: this.conf.api.notificationRead+"?notificationId="+notificationId,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                    if (this.utilsService.isValidJSON(res)=== false) {
                    	this.$log.error('NotificationService::setNotificationRead Failed');
                        deferred.reject("NotificationService::setNotificationRead Failed");
                    } else {
                        deferred.resolve(res);
                    }
                })
                .catch( status => {
                	this.$log.error('NotificationService::setNotificationRead Failed', status);
                    deferred.reject(status);
                });
           
            return deferred.promise;	
        }
    }
    NotificationService.$inject = ['$q', '$log', '$http', 'conf', 'uuid4','utilsService'];
    angular.module('ecompApp').service('notificationService', NotificationService)
})();
