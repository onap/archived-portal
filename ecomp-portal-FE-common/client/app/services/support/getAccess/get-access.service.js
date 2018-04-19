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
    class GetAccessService {
        constructor($q, $log, $http, conf, uuid, utilsService) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.uuid = uuid;
            this.utilsService = utilsService;
        }
        getListOfApp(searchStr) {
            //this.$log.info('GetAccessService::getListOfApp: get all app list');
            let deferred = this.$q.defer();
            //this.$log.info('GetAccessService::getListOfApp: searchStr', searchStr);
            //this.$log.info('GetAccessService::getListOfApp: ', this.conf.api.listOfApp);
            this.$http({
                method: 'GET',
                url: this.conf.api.listOfApp,
                params: {search:searchStr},
                cache: false
            }).then( res => {
                // If response comes back as a redirected HTML page which IS NOT a success
//            	this.$log.info('GetAccessService::getListOfApp response: ', res);
                if (this.utilsService.isValidJSON(res)== false) {
                    deferred.reject('GetAccessService::getListOfApp: Failed');
                } else {
                    // this.$log.debug('GetAccessService::getListOfApp: query results: ', res);
                    // this.$log.info('GetAccessService::getListOfApp Succeeded');
                    deferred.resolve(res);
                }
            }).catch( status => {
            	this.$log.error('GetAccessService::getListOfApp: query error: ',status);
                deferred.reject(status);
            });
            return deferred.promise;           
        }

    }
    GetAccessService.$inject = ['$q', '$log', '$http', 'conf','uuid4', 'utilsService'];
    angular.module('ecompApp').service('getAccessService', GetAccessService)
})();
