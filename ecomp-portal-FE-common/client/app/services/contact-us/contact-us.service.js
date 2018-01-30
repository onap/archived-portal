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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
/**
 * Created by robertlo on 10/10/2016.
 */
'use strict';

(function () {
    class ContactUsService {
        constructor($q, $log, $http, conf, uuid) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.uuid = uuid;
        }
        getListOfApp() {
            // this.$log.info('ContactUsService::getListOfavailableApps: get all app list');
            let deferred = this.$q.defer();
            // this.$log.info('ContactUsService::getListOfavailableApps: ', this.conf.api.listOfApp);
            this.$http({
                method: "GET",
                url: this.conf.api.availableApps,           
                cache: false
            }).then( res => {
                // If response comes back as a redirected HTML page which IS NOT a success
            	// this.$log.info('ContactUsService::getListOfavailableApps availableApps response: ', res);
                if (Object.keys(res).length == 0) {
                    deferred.reject("ContactUsService::getListOfavailableApps: Failed"); 
                } else {
                    // this.$log.debug('ContactUsService::getListOfavailableApps: Succeeded results: ', res);
                    deferred.resolve(res);
                }
            }).catch( status => {
            	this.$log.error('ContactUsService::getListOfavailableApps: query error: ',status);
                deferred.reject(status);
            });
            return deferred.promise;           
        }
        
        getContactUs() {
            let deferred = this.$q.defer();
            // this.$log.info('ContactUsService::get all Contact Us list');
            this.$http({
                    url: this.conf.api.getContactUS,
                    method: 'GET',
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                }).then(res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                    if (Object.keys(res.data).length == 0) {
                        deferred.reject("ContactUsService::getContactUs Failed");
                    } else {
                        deferred.resolve(res.data);
                    }
                })
                .catch(status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }
        
        getAppsAndContacts() {
            let deferred = this.$q.defer();
            // this.$log.info('ContactUsService::getAppsAndContacts');
            this.$http({
                    url: this.conf.api.getAppsAndContacts,
                    method: 'GET',
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                }).then(res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                    if (Object.keys(res.data).length == 0) {
                        deferred.reject("ContactUsService::getAppsAndContacts Failed");
                    } else {
                        deferred.resolve(res.data);
                    }
                })
                .catch(status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }

        getContactUSPortalDetails(){
        	let deferred = this.$q.defer();
            // this.$log.info('ContactUsService::get all Contact Us Portal Details');
            this.$http({
                    url: this.conf.api.getContactUSPortalDetails,
                    method: 'GET',
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                }).then(res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                    if (Object.keys(res.data).length == 0) {
                        deferred.reject("ContactUsService::getContactUSPortalDetails Failed");
                    } else {
                        deferred.resolve(res.data);
                    }
                })
                .catch(status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }

        getAppCategoryFunctions(){
        	let deferred = this.$q.defer();
            // this.$log.info('ContactUsService::get all App Category Functions');
            this.$http({
                    url: this.conf.api.getAppCategoryFunctions,
                    method: 'GET',
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                }).then(res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                    if (Object.keys(res.data).length == 0) {
                        deferred.reject("ContactUsService::getAppCategoryFunctions Failed");
                    } else {
                        deferred.resolve(res.data);
                    }
                })
                .catch(status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }
        
        addContactUs(newContactUs) {
        	// this.$log.info('ContactUsService::add a new Contact Us');
        	// this.$log.info(newContactUs)
        	let deferred = this.$q.defer();
            // this.$log.info('ContactUsService:: add Contact Us' + JSON.stringify(newContactUs));
            
            var contactUsObj={
            		appId:newContactUs.app.value,
            		appName:newContactUs.app.title,
            		description:newContactUs.desc,
            		contactName:newContactUs.name,
            		contactEmail:newContactUs.email,
            		url:newContactUs.url,            		
            };
            this.$http({
                url: this.conf.api.saveContactUS,
                method: 'POST',
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                },
                data: contactUsObj
            }).then(res => {
                // this.$log.info('ContactUsService:: add Contact Us res' ,res);
                // If response comes back as a redirected HTML page which IS NOT a success
                if (res==null || Object.keys(res.data).length == 0 || res.data.message == 'failure') {
                    deferred.reject("Add Contact Us failed");
                    this.$log.error('ContactUsService:: add Contact Us failed');
                } else {
                    deferred.resolve(res.data);
                }
            }).catch(errRes => {
                   deferred.reject(errRes);
             });
            return deferred.promise;
        }
        
        modifyContactUs(contactUsObj) {
        	// this.$log.info('ContactUsService::edit Contact Us',contactUsObj);        	
        	let deferred = this.$q.defer();
            this.$http({
                url: this.conf.api.saveContactUS,
                method: 'POST',
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                },
                data: contactUsObj
            }).then(res => {
                // this.$log.info('ContactUsService:: edit Contact Us res' ,res);
                // If response comes back as a redirected HTML page which IS NOT a success
                if (res==null || Object.keys(res.data).length == 0 || res.data.message == 'failure') {
                    deferred.reject("Edit Contact Us failed");
                    this.$log.error('ContactUsService:: edit Contact Us failed');
                } else {
                    deferred.resolve(res.data);
                }
            }).catch(errRes => {
                   deferred.reject(errRes);
             });
            return deferred.promise;
        }

        removeContactUs(id) {
            let deferred = this.$q.defer();
            let url = this.conf.api.deleteContactUS + '/' + id;
            // this.$log.info('ContactUsService:: remove Contact Us');
            this.$http({
                url: url,
                method: 'POST',
                cache: false,
                data: '',
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                // If response comes back as a redirected HTML page which IS NOT a success
            	// this.$log.info("ContactUsService::removeContactUs res",res);
            	deferred.resolve(res.data);
                if (Object.keys(res.data).length == 0) {
                    deferred.reject("ContactUsService::removeContactUs Failed");
                } else {
                    deferred.resolve(res.data);
                }
            }).catch(errRes => {
                deferred.reject(errRes);
            });

            return deferred.promise;
        }
    }
    ContactUsService.$inject = ['$q', '$log', '$http', 'conf', 'uuid4'];
    angular.module('ecompApp').service('contactUsService', ContactUsService)
})();
