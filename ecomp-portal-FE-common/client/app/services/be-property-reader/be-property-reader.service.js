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
/**
 * Created by nnaffar on 11/22/2015.
 */
'use strict';

(function () {
    class BeReaderService {
        constructor($q, $log, $http, conf, uuid, utilsService) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.uuid = uuid;
            this.utilsService = utilsService;
        }

        getProperty(property) {
            let deferred = this.$q.defer();
            //this.$log.info('BeReaderService::get all applications admins list');
            
            let url = this.conf.api.beProperty + "?key=" + property;
            this.$http({
                    method: "GET",
                    cache: false,
                    url: url,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                    //if (this.utilsService.isValidJSON(res)=== false) {
                    //	this.$log.error('BeReaderService::getAccountAdmins Failed');
                    //    deferred.reject("BeReaderService::getAccountAdmins Failed");
                    //} else {
                        // this.$log.info('BeReaderService::getAccountAdmins Succeeded');
                    deferred.resolve(res.data);
                    //}
                })
                .catch( status => {
                	this.$log.error('BeReaderService::getAccountAdmins Failed', status);
                    deferred.reject(status);
                });
           
            return deferred.promise;

        }
    }
    BeReaderService.$inject = ['$q', '$log', '$http', 'conf','uuid4', 'utilsService'];
    angular.module('ecompApp').service('beReaderService', BeReaderService)
})();
