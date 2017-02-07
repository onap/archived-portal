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
    class GetAccessService {
        constructor($q, $log, $http, conf,uuid) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.uuid = uuid;
        }
        getListOfApp(searchStr) {
            //this.$log.info('GetAccessService::getListOfApp: get all app list');
            let deferred = this.$q.defer();
            //this.$log.info('GetAccessService::getListOfApp: searchStr', searchStr);
            //this.$log.info('GetAccessService::getListOfApp: ', this.conf.api.listOfApp);
            this.$http({
                method: "GET",
                url: this.conf.api.listOfApp,
                params: {search:searchStr},
                cache: false
            }).then( res => {
            	// this.$log.info('GetAccessService::getListOfApp response: ', res);
                if (Object.keys(res).length == 0) {
                    deferred.reject("GetAccessService::getListOfApp: Failed");
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
    GetAccessService.$inject = ['$q', '$log', '$http', 'conf','uuid4'];
    angular.module('ecompApp').service('getAccessService', GetAccessService)
})();
