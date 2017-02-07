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
    class CatalogService {
    	
        constructor($q, $log, $http, conf, uuid) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.uuid = uuid;
        }

        getAppCatalog() {
            let deferred = this.$q.defer();
            this.$http(
                {
                	method: "GET",
                	url: this.conf.api.appCatalog,
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                    // Detect non-JSON
                    if (res == null || res.data == null) {
                        deferred.reject("CatalogService::getAppCatalog Failed");
                    } else {
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
                    // Detect non-JSON
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

    }
    
    CatalogService.$inject = ['$q', '$log', '$http', 'conf','uuid4'];
    angular.module('ecompApp').service('catalogService', CatalogService)
})();
