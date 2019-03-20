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
    class ApplicationsService {
        constructor($q, $log, $http, conf, uuid, utilsService) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.uuid = uuid;
            this.utilsService = utilsService
        }
        
        getSingleAppInfo(appName) {
            let deferred = this.$q.defer();
            var _this12 = this;
            this.$http.get(this.conf.api.singleAppInfo,
                {
                    cache: false,
                    params:{'appParam':appName},
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                	// But don't declare an empty list to be an error.
                    if (res == null || res.data == null || _this12.utilsService.isValidJSON(res.data) == false) {
                        deferred.reject("ApplicationsService::getSingleAppInfo Failed");
                    } else {
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }
        
        getSingleAppInfoById(appId) {
            let deferred = this.$q.defer();
            var _this14 = this;
            this.$http.get(this.conf.api.singleAppInfoById,
                {
                    cache: false,
                    params:{'appParam':appId},
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                	// But don't declare an empty list to be an error.
                    if (res == null || res.data == null || _this14.utilsService.isValidJSON(res.data) == false) {
                        deferred.reject("ApplicationsService::getSingleAppInfoById Failed");
                    } else {
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }
        
        getPersUserApps() {
            let deferred = this.$q.defer();
            var _this0 = this;
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
                    if (res == null || res.data == null || _this0.utilsService.isValidJSON(res.data) == false) {
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
        
        getAppsOrderBySortPref(userAppSortTypePref) {
            let deferred = this.$q.defer();
            var _this1 = this;
            // this.$log.info('ApplicationsService::getAppsOrderBySortPref');
            this.$http.get(this.conf.api.userAppsOrderBySortPref,
                {
                    cache: false,
                    params:{'mparams':userAppSortTypePref},
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                	// But don't declare an empty list to be an error.
                    if (res == null || res.data == null || _this1.utilsService.isValidJSON(res.data)== false) {
                        deferred.reject("ApplicationsService::getAppsOrderBySortPref Failed");
                    } else {
                        // this.$log.info('ApplicationsService::getAppsOrderBySortPref Succeeded');
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                    deferred.reject(status);
                });

            return deferred.promise;
        }

        saveAppsSortTypeManual(appsSortManual){
            let deferred = this.$q.defer();
            if (appsSortManual== undefined
					|| appsSortManual == null
					|| appsSortManual.length == 0) {
            	this.$log.error('ApplicationsService::saveAppsSortTypeManual: Apps Sort Manual received empty string!'); 
            	return deferred.reject('Apps Sort Manual received empty string ');
            }
            
            this.$http({
                method: 'PUT',
                url: this.conf.api.saveUserAppsSortingManual,
                data: appsSortManual,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then( res => {
                    // If response comes back as a redirected HTML page which IS
					// NOT a success
                	// But don't declare an empty list to be an error.
                    if (res == null || res.data == null) {
                        deferred.reject("ApplicationsService::saveAppsSortTypeManual Failed");
                    } else {
                        // this.$log.info('ApplicationsService::saveAppsSortTypeManual
						// Succeeded');
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }
            
        saveAppsSortTypePreference(appsSortPreference){
            let deferred = this.$q.defer();
            var _this2 = this;
            if (appsSortPreference== undefined
					|| appsSortPreference == null
					|| appsSortPreference.length == 0){
            	this.$log.error('ApplicationsService::saveAppsSortTypePreference: Apps Sort type Preference received empty string!'); 
           		 return deferred.reject('Apps Sort type Preference received empty string ');
            }
            this.$http({
                method: 'PUT',
                url: this.conf.api.saveUserAppsSortingPreference,
                data: appsSortPreference,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                	// But don't declare an empty list to be an error.
                    if (res == null || res.data == null || _this2.utilsService.isValidJSON(res) == false) {
                        deferred.reject("ApplicationsService::saveAppsSortTypePreference Failed");
                    } else {
                        // this.$log.info('ApplicationsService::saveAppsSortTypePreference Succeeded');
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }
        
        getUserAppsSortTypePreference() {
        	let deferred = this.$q.defer();
        	var _this3 = this;
        	this.$http({
                        method: "GET",
                        url: this.conf.api.userAppsSortTypePreference,
                        cache: false,
                        headers: {
                            'X-ECOMP-RequestID':this.uuid.generate()
                        }
                    })
                    .then( res => {
                        if (res == null || res.data == null || _this3.utilsService.isValidJSON(res) == false) {  
                        	deferred.reject("ApplicationsService::getSortTypePreference Failed");
                        } else {
                            // this.$log.info('ApplicationsService::getUserAppsSortTypePreference Succeeded');
                            deferred.resolve(res.data);
                        }
                    })
                    .catch( status => {
                        deferred.reject(status);
                    });
        		return deferred.promise;
        }
        
        saveWidgetsSortManual(widgetsSortManual){
            let deferred = this.$q.defer();
            var _this4 = this;
            if (widgetsSortManual== undefined
					|| widgetsSortManual == null
					|| widgetsSortManual.length == 0){
            	this.$log.error('ApplicationsService::saveWidgetsSortManual: Widegts Sort type Preference received empty string!'); 
           		 return deferred.reject('Widgets Sort Manual received empty string ');
            }
            this.$http({
                method: 'PUT',
                url: this.conf.api.saveUserWidgetsSortManual,
                data: widgetsSortManual,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then( res => {
                    if (res == null || res.data == null || _this4.utilsService.isValidJSON(res) == false) {
                        deferred.reject("ApplicationsService::saveWidgetsSortManual Failed");
                    } else {
                        // this.$log.info('ApplicationsService::saveWidgetsSortManual
						// Succeeded');
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }
        
        delWidgetsSortPref(widgetsData){
            let deferred = this.$q.defer();
            var _this5 = this;
             
            if (widgetsData== undefined
					|| widgetsData == null
					|| widgetsData.length == 0){
            	this.$log.error('ApplicationsService::delWidgetsSortPref: While deleting, widgets  received empty string!'); 
            	return deferred.reject('Widgets received empty string ');
            }
            
            this.$http({
                method: 'PUT',
                url: this.conf.api.updateWidgetsSortPref,
                data: widgetsData,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then( res => {
                    if (res == null || res.data == null || _this5.utilsService.isValidJSON(res) == false) {
                        deferred.reject("ApplicationsService::delWidgetsSortPref Failed");
                    } else {
                        // this.$log.info('ApplicationsService::delWidgetsSortPref
						// Succeeded');
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
            var _this6 = this;
            // this.$log.info('ApplicationsService::getAvailableApps');
            
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
                    if (res == null || res.data == null || _this6.utilsService.isValidJSON(res) == false) {
                        deferred.reject("ApplicationsService::getAvailableApps Failed");
                    } else {
                        // this.$log.info('ApplicationsService::getAvailableApps Succeeded');
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                    deferred.reject(status);
                });

            return deferred.promise;
        }

        getAdminApps(){
            var _this7 = this;
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
                // this.$log.info('ApplicationsService::getAdminApps: starting');
                this.$http({method: "GET",
                        url: this.conf.api.adminApps,
                        cache: false,
                        timeout: canceller.promise,
                        headers: {
                            'X-ECOMP-RequestID':this.uuid.generate()
                        }
                    }).then(res => {
                        isActive = false;
                        if (res == null || res.data == null || _this7.utilsService.isValidJSON(res) == false) {
                            deferred.reject("ApplicationsService::adminApps Failed");
                        } else {
                            // this.$log.info('ApplicationsService::adminApps Succeeded');
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

        getLeftMenuItems(){
            let deferred = this.$q.defer();
            var _this8 = this;
            // this.$log.info('ApplicationsService::getAppsForSuperAdminAndAccountAdmin');
            this.$http({method: "GET",
                url: this.conf.api.leftmenuItems,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                	// But don't declare an empty list to be an error.
                this.$log.debug('getLeftMenuItems::getAdminApps: canceling the request',res);

                    if (res == null || res.data == null || _this8.utilsService.isValidJSON(res) == false) {
                        deferred.reject("ApplicationsService::getLeftMenuItems Failed");
                    } else {
                        // this.$log.info('ApplicationsService::getAppsForSuperAdminAndAccountAdmin Succeeded');
                        deferred.resolve(res.data);
                    }
                })
                .catch(status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }
        
        getAppsForSuperAdminAndAccountAdmin(){
            let deferred = this.$q.defer();
            var _this9 = this;
            // this.$log.info('ApplicationsService::getAppsForSuperAdminAndAccountAdmin');
            this.$http({method: "GET",
                url: this.conf.api.appsForSuperAdminAndAccountAdmin,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                	// But don't declare an empty list to be an error.
                    if (res == null || res.data == null || _this9.utilsService.isValidJSON(res) == false) {
                        deferred.reject("ApplicationsService::getAppsForSuperAdminAndAccountAdmin Failed");
                    } else {
                        // this.$log.info('ApplicationsService::getAppsForSuperAdminAndAccountAdmin Succeeded');
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
        	var _this10 = this;
            // this.$log.info('ApplicationsService::getAdminAppsSimpler');
        	this.$http({method: "GET",
        		url: this.conf.api.adminApps,
        		cache: false,
        		headers: {
        			'X-ECOMP-RequestID':this.uuid.generate()
        		}
        	}).then(res => {
        		// If response comes back as a redirected HTML page which IS NOT a success
        		// But don't declare an empty list to be an error.
        		if (res == null || res.data == null || _this10.utilsService.isValidJSON(res.data) == false) {
        			deferred.reject("ApplicationsService::getAdminAppsSimpler Failed");
        		} else {
        			// this.$log.info('ApplicationsService::getAdminAppsSimpler Succeeded');
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
            var _this11 = this;
            // this.$log.debug('applications-service::getOnboardingApps');
            this.$http.get(this.conf.api.onboardingApps,
                {
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                	// But don't declare an empty list to be an error.
                    if (res == null || res.data == null || _this11.utilsService.isValidJSON(res.data) == false) {
                        deferred.reject("ApplicationsService::getOnboardingApps Failed");
                    } else {
                        // this.$log.info('ApplicationsService::getOnboardingApps Succeeded');
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
            // this.$log.debug('applications-service::addOnboardingApp with:', newApp);
            this.$http({
                method: "POST",
                url: this.conf.api.onboardingApps,
                data: newApp,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                	// But don't declare an empty list to be an error.
                    if (res == null || res.data == null) {
                        deferred.reject("ApplicationsService::addOnboardingApp Failed");
                    } else {
                        // this.$log.info('ApplicationsService::addOnboardingApp Succeeded');
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
            // this.$log.info('ApplicationsService::addOnboardingApp');
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
                    // If response comes back as a redirected HTML page which IS NOT a success
                	// But don't declare an empty list to be an error.
                    if (res == null || res.data == null) {
                        deferred.reject("ApplicationsService::updateOnboardingApp Failed");
                    } else {
                        // this.$log.info('ApplicationsService::updateOnboardingApp Succeeded');
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }
        
        saveUserAppsRoles(UserAppRolesRequest) {
          	 let deferred = this.$q.defer();
          	 
   	        if (UserAppRolesRequest== undefined
   				|| UserAppRolesRequest == null
   				|| UserAppRolesRequest.length == 0){
   	    	this.$log.error('ApplicationsService::saveAppsSortTypeManual: Apps Sort Manual received empty string!'); 
   	   		 return deferred.reject('Apps Sort Manual received empty string ');
   	        	}
   			// var manualAppsJson = angular.toJson(appsSortManual);
      
   		    // console.log(manual_jsonData);
   		    this.$http({
   		        method: 'PUT',
   		        url: this.conf.api.saveUserAppRoles,
   		        data: UserAppRolesRequest,
   		        cache: false,
   		        headers: {
   		            'X-ECOMP-RequestID':this.uuid.generate()
   		        }
   		    }).then( res => {
   		            // If response comes back as a redirected HTML page which IS
   					// NOT a success
   		        	// But don't declare an empty list to be an error.
   		            if (res == null || res.data == null) {
   		                deferred.reject("ApplicationsService::saveAppsSortTypeManual Failed");
   		            } else {
   		                // this.$log.info('ApplicationsService::saveAppsSortTypeManual
   						// Succeeded');
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
            // this.$log.info('applications.service::deleteOnboardingApp' +appId);

            this.$http({
                method: "DELETE",
                url: url,
                cache: false,
                data:'',
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                	// But don't declare an empty list to be an error.
                    if (res == null || res.data == null) {
                        deferred.reject("applications.service::deleteOnboardingApp Failed");
                    } else {
                        // this.$log.info('applications.service::deleteOnboardingApp succeeded: ');
                        deferred.resolve(res.data);
                    }
                })
                .catch(errRes => {
                    deferred.reject(errRes);
                });
            return deferred.promise;
        }
        
        syncRolesEcompFromExtAuthSystem(appId){
        	 let deferred = this.$q.defer();
        	 let url = this.conf.api.syncRolesFromExternalAuthSystem;
             var _this13 = this; 
                this.$http({
                method: "POST",
                url: url,
                cache: false,
                data:appId,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
              }).then( res => {
                     // If response comes back as a redirected HTML page which IS NOT a success
                 	// But don't declare an empty list to be an error.
                     if (res == null || res.data == null || _this13.utilsService.isValidJSON(res.data) == false ||  res.data.status == 'ERROR') {
                         deferred.reject("ApplicationsService::syncRolesEcompFromExtAuthSystem Failed"  + res.data.message);
                     } else {
                         deferred.resolve(res);
                     }
                 })
                 .catch( status => {
                     deferred.reject(status);
                 });
             return deferred.promise;
        }
        
    	syncFunctionsFromExternalAuthSystem(appId){
          	 let deferred = this.$q.defer();
          	 let url = this.conf.api.syncFunctionsFromExternalAuthSystem;
               var _this13 = this; 
                  this.$http({
                  method: "POST",
                  url: url,
                  cache: false,
                  data:appId,
                  headers: {
                      'X-ECOMP-RequestID':this.uuid.generate()
                  }
                }).then( res => {
                       // If response comes back as a redirected HTML page which IS NOT a success
                   	// But don't declare an empty list to be an error.
                       if (res == null || res.data == null || _this13.utilsService.isValidJSON(res.data) == false) {
                           deferred.reject("ApplicationsService::syncFunctionsFromExternalAuthSystem Failed");
                       } else {
                           deferred.resolve(res);
                       }
                   })
                   .catch( status => {
                       deferred.reject(status);
                   });
               return deferred.promise;
          }

        getTopMenuData(selectedApp) {
            let deferred = this.$q.defer();
            // this.$log.info('ApplicationsService:getTopMenuData');
            this.$log.debug('ApplicationsService:getTopMenuData with:', selectedApp);
        }

        ping(){
            let deferred = this.$q.defer();
            // this.$log.info('ApplicationsService::ping: ');
            this.$http.get(this.conf.api.ping,
                {
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .success( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                    if (Object.keys(res).length == 0) {
                        deferred.reject("ApplicationsService::ping: Failed");
                    } else {
                        // this.$log.info('ApplicationsService::ping: Succeeded');
                        deferred.resolve(res);
                    }
                })
                .error( status => {
                    deferred.reject(status);
                });

            return deferred.promise;
        }
    }
    ApplicationsService.$inject = ['$q', '$log', '$http', 'conf','uuid4','utilsService'];
    angular.module('ecompApp').service('applicationsService', ApplicationsService)
})();
