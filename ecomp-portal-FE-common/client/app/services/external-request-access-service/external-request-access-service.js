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
'use strict';

(function () {
    class ExternalRequestAccessService {
        constructor($q, $log, $http, conf, uuid, utilsService) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.uuid = uuid;
            this.utilsService = utilsService;
        }

        getExternalRequestAccessServiceInfo() {
            let deferred = this.$q.defer();
            var _this = this;
            let url = this.conf.api.externalRequestAccessSystem;
            this.$http({
                    method: "GET",
                    cache: false,
                    url: url,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                	if (res == null || res.data == null || _this.utilsService.isValidJSON(res.data) == false) {
                        deferred.reject("ExternalRequestAccessService::getExternalRequestAccessServiceInfo Failed");
                    }else{
                    deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                	this.$log.error('ExternalRequestAccessService::getExternalRequestAccessServiceInfo Failed', status);
                    deferred.reject(status);
                });
           
            return deferred.promise;

        }
    }
    ExternalRequestAccessService.$inject = ['$q', '$log', '$http', 'conf','uuid4', 'utilsService'];
    angular.module('ecompApp').service('ExternalRequestAccessService', ExternalRequestAccessService)
})();
