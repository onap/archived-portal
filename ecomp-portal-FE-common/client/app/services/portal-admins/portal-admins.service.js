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
    class PortalAdminsService {
        constructor($q, $log, $http, conf, uuid, utilsService) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.uuid = uuid;
            this.utilsService = utilsService;
        }

        getPortalAdmins() {
            let deferred = this.$q.defer();
            this.$log.info('PortalAdminsService::get all portal admins list');
            this.$http({
                    url: this.conf.api.portalAdmins,
                    method: 'GET',
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                }).then(res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                if (this.utilsService.isValidJSON(res)== false) {
                        deferred.reject('PortalAdminsService::getPortalAdmins Failed');
                    } else {
                        deferred.resolve(res.data);
                    }
                })
                .catch(status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }

        addPortalAdmin(userData) {
            let deferred = this.$q.defer();
            this.$log.info('PortalAdminsService::addPortalAdmin: ' + JSON.stringify(userData));
            this.$http({
                url: this.conf.api.portalAdmin,
                method: 'POST',
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                },
                data: userData
            }).then(res => {
                // If response comes back as a redirected HTML page which IS NOT a success
                this.$log.debug('PortalAdminsService:: this.conf.api.portalAdmin: ' + JSON.stringify(res));
                if (this.utilsService.isValidJSON(res)== false) {
                    deferred.reject('PortalAdminsService::addPortalAdmin Failed');
                } else {
                    deferred.resolve(res.data);
                }
            })
                .catch(errRes => {
                    this.$log.debug('PortalAdminsService:: this.conf.api.portalAdmin: ' + JSON.stringify(errRes));
                    deferred.reject(errRes);
                });
            return deferred.promise;
        }

        removePortalAdmin(userId,orUserId) {
            let deferred = this.$q.defer();
            let userInfo = userId+"-"+orUserId;
            let url = this.conf.api.portalAdmin + '/' + userInfo;
            this.$log.info('PortalAdminsService:: remove Portal Admin');
            this.$http({
                url: url,
                method: 'DELETE',
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                // If response comes back as a redirected HTML page which IS NOT a success
                if (this.utilsService.isValidJSON(res)== false) {
                    deferred.reject('PortalAdminsService::removePortalAdmin Failed');
                } else {
                    deferred.resolve(res.data);
                }
            }).catch(errRes => {
                deferred.reject(errRes);
            });

            return deferred.promise;
        }
    }
    PortalAdminsService.$inject = ['$q', '$log', '$http', 'conf', 'uuid4', 'utilsService'];
    angular.module('ecompApp').service('portalAdminsService', PortalAdminsService)
})();
