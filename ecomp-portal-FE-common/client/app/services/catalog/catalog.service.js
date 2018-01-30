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
'use strict';

(function () {
    class CatalogService {
    	
        constructor($q, $log, $http, conf, uuid, utilsService) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.uuid = uuid;
            this.debug = false;
            this.utilsService = utilsService;
        }

        getAppCatalog() {
            let deferred = this.$q.defer();
            this.$http({
                	method: "GET",
                	url: this.conf.api.appCatalog,
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                	if (this.debug)
                		this.$log.debug('CatalogService::getAppCatalog: result is ' + JSON.stringify(res));
                    // Res is always JSON, but the data object might be an HTML error page.
                    if (! this.utilsService.isValidJSON(res.data)) {
                    	var msg = 'CatalogService::getAppCatalog: result data is not JSON';
                    	if (this.debug)
                    		this.$log.debug(msg);
                    	deferred.reject(msg);
                    } else {
                    	if (this.debug)
                    		this.$log.debug('CatalogService::getAppCatalog: success');
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                	this.$log.error('CatalogService:getAppCatalog failed: ' + status);
                    deferred.reject(status);
                });
            return deferred.promise;
        }
        
        // Expects an object with fields matching model class AppCatalogSelection:
        // appId (number), select (boolean), pending (boolean).
        updateAppCatalog(appData) {
            let deferred = this.$q.defer();
            // Validate the request, maybe this is overkill
            if (appData == null || appData.appId == null || appData.select == null) {
            	var msg = 'CatalogService::updateAppCatalog: field appId and/or select not found';
                this.$log.error(msg);
                return deferred.reject(msg);
            }
            this.$http({
                method: "PUT",
                url: this.conf.api.appCatalog,
                data: appData,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then( res => {
                    // Detect missing result
                    if (res == null || res.data == null) {
                        deferred.reject("CatalogService::updateAppCatalog Failed");
                    } else {
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                	this.$log.error('CatalogService:updateAppCatalog failed: ' + status);
                    deferred.reject(status);
                });
            return deferred.promise;
        }
        
        // Expects an object with fields and used to update records for ep_pers_user_app_man_sort table:
        // appId (number), select (boolean).
        updateManualAppSort(appData) {
            let deferred = this.$q.defer();
            
            // Validate the request, maybe this is overkill
            if (appData == null || appData.appId == null || appData.select == null) {
            	var msg = 'CatalogService::updateManualAppSort: field appId and/or select not found';
                this.$log.error(msg);
                return deferred.reject(msg);
            }
            this.$http({
                method: "PUT",
                url: this.conf.api.UpdateUserAppsSortManual,
                data: appData,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then( res => {
                    // Detect missing result
                    if (res == null || res.data == null) {
                        deferred.reject("CatalogService::updateManualAppSort Failed");
                    } else {
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                	this.$log.error('CatalogService:updateManualAppSort failed: ' + status);
                    deferred.reject(status);
                });
            
            return deferred.promise;
        }
           
        getuserAppRolesCatalog(item) {
            let deferred = this.$q.defer();
            this.$http({
                	method: "GET",
                	url: this.conf.api.appCatalogRoles,
                	params:{appName:item},
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                	if (this.debug)
                		this.$log.debug('CatalogService::getAppCatalog: result is ' + JSON.stringify(res));
                    // Res is always JSON, but the data object might be an HTML error page.
                    if (! this.utilsService.isValidJSON(res.data)) {
                    	var msg = 'CatalogService::getAppCatalog: result data is not JSON';
                    	if (this.debug)
                    		this.$log.debug(msg);
                    	deferred.reject(msg);
                    } else {
                    	if (this.debug)
                    		this.$log.debug('CatalogService::getAppCatalog: success');
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                	this.$log.error('CatalogService:getAppCatalog failed: ' + status);
                    deferred.reject(status);
                });
            return deferred.promise;
        }
        
        getAppsFullList() {
            let deferred = this.$q.defer();
            this.$http({
                	method: "GET",
                	url: this.conf.api.appsFullList,
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                	if (this.debug)
                		this.$log.debug('CatalogService::getAppsFullList: result is ' + JSON.stringify(res));
                    // Res is always JSON, but the data object might be an HTML error page.
                    if (! this.utilsService.isValidJSON(res.data)) {
                    	var msg = 'CatalogService::getAppsFullList: result data is not JSON';
                    	if (this.debug)
                    		this.$log.error(msg);
                    	deferred.reject(msg);
                    } else {
                    	if (this.debug)
                    		this.$log.debug('CatalogService::getAppsFullList: success');
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                	this.$log.error('CatalogService:getAppsFullList failed: ' + status);
                    deferred.reject(status);
                });
            return deferred.promise;
        }

    }
    
    CatalogService.$inject = ['$q', '$log', '$http', 'conf','uuid4', 'utilsService'];
    angular.module('ecompApp').service('catalogService', CatalogService)
})();
