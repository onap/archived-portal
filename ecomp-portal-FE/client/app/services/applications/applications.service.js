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
    class ApplicationsService {
        constructor($q, $log, $http, conf, uuid) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.uuid = uuid;
        }
        
        getPersUserApps() {
            let deferred = this.$q.defer();
            // this.$log.info('ApplicationsService::getPersUserApps');
            this.$http.get(this.conf.api.persUserApps,
                {
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                	// But don't declare an empty list to be an error.
                    if (res == null || res.data == null) {
                        deferred.reject("ApplicationsService::getPersUserApps Failed");
                    } else {
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }

        getUserApps(){
            let deferred = this.$q.defer();
            this.$log.info('ApplicationsService::getUserApps');
            this.$http.get(this.conf.api.userApps,
                {
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                    if (Object.keys(res.data).length == 0) {
                        deferred.reject("ApplicationsService::getUserApps Failed");
                    } else {
                        this.$log.info('ApplicationsService::getUserApps Succeeded');
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                    deferred.reject(status);
                });

            return deferred.promise;
        }

        getAvailableApps() {
            let deferred = this.$q.defer();
            this.$log.info('ApplicationsService::getAvailableApps');
            this.$http(
                {
                    method: "GET",
                    url: this.conf.api.availableApps,
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                    if (Object.keys(res.data).length == 0) {
                        deferred.reject("ApplicationsService::getAvailableApps Failed");
                    } else {
                        this.$log.info('ApplicationsService::getAvailableApps Succeeded');
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                    deferred.reject(status);
                });

            return deferred.promise;
        }

        getAdminApps(){
            let canceller = this.$q.defer();
            let isActive = false;

            let cancel = () => {
                if(isActive){
                    this.$log.debug('ApplicationsService::getAdminApps: canceling the request');
                    canceller.resolve();
                }
            };

            let promise = () => {
                isActive = true;
                let deferred = this.$q.defer();
                this.$log.info('ApplicationsService::getAdminApps: starting');
                this.$http({method: "GET",
                        url: this.conf.api.adminApps,
                        cache: false,
                        timeout: canceller.promise,
                        headers: {
                            'X-ECOMP-RequestID':this.uuid.generate()
                        }
                    }).then(res => {
                        isActive = false;
                        if (Object.keys(res.data).length == 0) {
                            deferred.reject("ApplicationsService::adminApps Failed");
                        } else {
                            this.$log.info('ApplicationsService::adminApps Succeeded');
                            deferred.resolve(res.data);
                        }
                    })
                    .catch(status => {
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

        getAppsForSuperAdminAndAccountAdmin(){
            let deferred = this.$q.defer();
            this.$log.info('ApplicationsService::getAppsForSuperAdminAndAccountAdmin');
            this.$http({method: "GET",
                url: this.conf.api.appsForSuperAdminAndAccountAdmin,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                if (Object.keys(res.data).length == 0) {
                    deferred.reject("ApplicationsService::getAppsForSuperAdminAndAccountAdmin Failed");
                } else {
                    this.$log.info('ApplicationsService::getAppsForSuperAdminAndAccountAdmin Succeeded');
                    deferred.resolve(res.data);
                }
            })
            .catch(status => {
                deferred.reject(status);
            });
            return deferred.promise;
        }

        getAdminAppsSimpler(){
                let deferred = this.$q.defer();
                this.$log.info('ApplicationsService::getAdminApps');
                this.$http({method: "GET",
                    url: this.conf.api.adminApps,
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                }).then(res => {
                    if (Object.keys(res.data).length == 0) {
                        deferred.reject("ApplicationsService::getAdminApps Failed");
                    } else {
                        this.$log.info('ApplicationsService::getAdminApps Succeeded');
                        deferred.resolve(res.data);
                    }
                })
                .catch(status => {
                    deferred.reject(status);
                });
                return deferred.promise;
        }

        getOnboardingApps(){
            let deferred = this.$q.defer();
            this.$log.info('ApplicationsService::getOnboardingApps');

            this.$http.get(this.conf.api.onboardingApps,
                {
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                    if (Object.keys(res.data).length == 0) {
                        deferred.reject("ApplicationsService::getOnboardingApps Failed");
                    } else {
                        this.$log.info('ApplicationsService::getOnboardingApps Succeeded');
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                    deferred.reject(status);
                });

            return deferred.promise;
        }

        addOnboardingApp(newApp){
            let deferred = this.$q.defer();
            this.$log.info('applications-service::addOnboardingApp');
            this.$log.debug('applications-service::addOnboardingApp with:', newApp);

            this.$http({
                method: "POST",
                url: this.conf.api.onboardingApps,
                data: newApp,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then( res => {
                if (Object.keys(res.data).length == 0) {
                    deferred.reject("ApplicationsService::addOnboardingApp Failed");
                } else {
                    this.$log.info('ApplicationsService::addOnboardingApp Succeeded');
                    deferred.resolve(res.data);
                }
            })
            .catch( status => {
                deferred.reject(status);
            });
            return deferred.promise;
        }

        updateOnboardingApp(appData){
            let deferred = this.$q.defer();
            this.$log.info('ApplicationsService::addOnboardingApp');
            if(!appData.id){
                this.$log.error('ApplicationsService::addOnboardingApp: App id not found!');
                return deferred.reject('App id not found');
            }

            this.$http({
                method: "PUT",
                url: this.conf.api.onboardingApps,
                data: appData,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then( res => {
                if (Object.keys(res.data).length == 0) {
                    deferred.reject("ApplicationsService::updateOnboardingApp Failed");
                } else {
                    this.$log.info('ApplicationsService::updateOnboardingApp Succeeded');
                    deferred.resolve(res.data);
                }
            })
            .catch( status => {
                deferred.reject(status);
            });
            return deferred.promise;
        }

        deleteOnboardingApp(appId) {
            let deferred = this.$q.defer();
            let url = this.conf.api.onboardingApps + '/' + appId;

            this.$log.info('applications.service::deleteOnboardingApp' +appId);

            this.$http({
                method: "DELETE",
                url: url,
                cache: false,
                data:'',
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                if (Object.keys(res.data).length == 0) {
                    deferred.reject("applications.service::deleteOnboardingApp Failed");
                } else {
                    this.$log.info('applications.service::deleteOnboardingApp succeeded: ');
                    deferred.resolve(res.data);
                }
            })
            .catch(errRes => {
                deferred.reject(errRes);
            });
            return deferred.promise;
        }

        getTopMenuData(selectedApp) {
            let deferred = this.$q.defer();
            this.$log.info('ApplicationsService:getTopMenuData');
            this.$log.debug('ApplicationsService:getTopMenuData with:', selectedApp);

        }

        ping(){
            let deferred = this.$q.defer();
            this.$log.info('ApplicationsService::ping: ');

            this.$http.get(this.conf.api.ping,
                {
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .success( res => {
                    if (Object.keys(res).length == 0) {
                        deferred.reject("ApplicationsService::ping: Failed");
                    } else {
                        this.$log.info('ApplicationsService::ping: Succeeded');
                        deferred.resolve(res);
                    }
                })
                .error( status => {
                    deferred.reject(status);
                });

            return deferred.promise;
        }
    }
    ApplicationsService.$inject = ['$q', '$log', '$http', 'conf','uuid4'];
    angular.module('ecompApp').service('applicationsService', ApplicationsService)
})();
