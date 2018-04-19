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
 * Created by mlittle on 9/9/2016.
 */
'use strict';

(function () {
    class ManifestService {
        constructor($q, $log, $http, conf, uuid, utilsService) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.uuid = uuid;
            this.utilsService = utilsService;
        }

        getManifest() {
            let deferred = this.$q.defer();
            this.$http({
                method: "GET",
                url: this.conf.api.getManifest,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then( res => {
                if (this.utilsService.isValidJSON(res)== false) {
                    this.$log.error('ManifestService.getManifest failed: ');
                    deferred.reject('ManifestService.getManifest: response.data null or not object');
                } else {
                    // this.$log.info('ManifestService.getManifest Succeeded');
                    // this.$log.debug('ManifestService.getManifest: ', JSON.stringify(res))
                    deferred.resolve(res.data);
                }
            }).catch( status => {
                this.$log.error('ManifestService.getManifest failed: ' + status.data);
                deferred.reject(status);
            });
            return deferred.promise;
        }

    }
    ManifestService.$inject = ['$q', '$log', '$http', 'conf', 'uuid4', 'utilsService'];
    angular.module('ecompApp').service('manifestService', ManifestService)
})();
