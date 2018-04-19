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
    class userbarUpdateService {
        constructor($q, $log, $http, conf, uuid) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.uuid = uuid;
            this.refreshCount = 0;
            this.maxCount = 0;
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

        getWidthThresholdLeftMenu() {
            let deferred = this.$q.defer();
            this.$log.info('userbarUpdateService::getWidthThresholdLeftMenu');
            this.$http({
                    url: this.conf.api.getWidthThresholdLeftMenu,
                    method: 'GET',
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                }).then(res => {
                    if (Object.keys(res.data).length == 0) {
                        deferred.reject("userbarUpdateService::getWidthThresholdLeftMenu Failed");
                    } else {
                        deferred.resolve(res.data);
                    }
                })
                .catch(status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }
        
        getWidthThresholdRightMenu() {
            let deferred = this.$q.defer();
            this.$log.info('userbarUpdateService::getWidthThresholdRightMenu');
            this.$http({
                    url: this.conf.api.getWidthThresholdRightMenu,
                    method: 'GET',
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                }).then(res => {
                    if (Object.keys(res.data).length == 0) {
                        deferred.reject("userbarUpdateService::getWidthThresholdRightMenu Failed");
                    } else {
                        deferred.resolve(res.data);
                    }
                })
                .catch(status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }


    }
    userbarUpdateService.$inject = ['$q', '$log', '$http', 'conf', 'uuid4'];
    angular.module('ecompApp').service('userbarUpdateService', userbarUpdateService)
})();
