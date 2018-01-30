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
    class FunctionalMenuService {
        constructor($q, $log, $http, conf, uuid, utilsService) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.uuid = uuid;
            this.utilsService = utilsService;
        }


        getManagedRolesMenu( appId )
        {
            let deferred = this.$q.defer();
            this.$log.info('FunctionalMenuService::getManagedRolesMenu');
            let url = this.conf.api.appRoles.replace(':appId', appId);
            this.$log.info('FunctionalMenuService::getManagedRolesMenu url: '+url);

            this.$http({
                method: 'GET',
                url: url,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                    if (this.utilsService.isValidJSON(res)== false) {
                        deferred.reject('functionalMenu.service::getManagedRolesMenu Failed');
                    } else {
                        this.$log.info('functionalMenu.service::getManagedRolesMenu succeeded: ');
                        deferred.resolve(res.data);
                    }
                })
                .catch(status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }

        getAvailableApplications()
        {
            let deferred = this.$q.defer();
            this.$log.info('FunctionalMenuService::getManagedRolesMenu:getAvailableApplications');

            this.$http({
                method: 'GET',
//                url: this.conf.api.availableApps,
                url: this.conf.api.allAvailableApps,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                    if (this.utilsService.isValidJSON(res)== false) {
                        deferred.reject('FunctionalMenuService::getManagedRolesMenu:getAvailableApplications Failed');
                    } else {
                        this.$log.info('FunctionalMenuService::getManagedRolesMenu:getAvailableApplications succeeded: ');
                        deferred.resolve(res.data);
                    }
                })
                .catch(status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }
        getMenuDetails( menuId )
        {
            let deferred = this.$q.defer();
            this.$log.info('FunctionalMenuService::getMenuDetails:getMenuDetails');
            let url = this.conf.api.functionalMenuItemDetails.replace(':menuId',menuId);
            this.$log.info('FunctionalMenuService::getMenuDetails url: '+url);


            this.$http({
                method: 'GET',
                url: url,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                if (this.utilsService.isValidJSON(res)== false) {
                        deferred.reject('FunctionalMenuService::getMenuDetails:getMenuDetails Failed');
                    } else {
                        this.$log.info('FunctionalMenuService::getMenuDetails:getMenuDetails succeeded: ');
                        deferred.resolve(res.data);
                    }
                })
                .catch(status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }


        getManagedFunctionalMenu() {
            let deferred = this.$q.defer();
            this.$log.info('FunctionalMenuService::getMenuDetails:getManagedFunctionalMenu');

            this.$http({
                method: 'GET',
//                url: this.conf.api.functionalMenuForAuthUser,
                url: this.conf.api.functionalMenuForEditing,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                    if (this.utilsService.isValidJSON(res)== false) {
                        deferred.reject('FunctionalMenuService::getManagedFunctionalMenu Failed');
                    } else {
                        this.$log.info('FunctionalMenuService::getManagedFunctionalMenu succeeded: ');
                        deferred.resolve(res.data);
                    }
                })
                .catch(status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }
        
        getManagedFunctionalMenuForNotificationTree() {
            let deferred = this.$q.defer();
            this.$log.info('FunctionalMenuService::getMenuDetails:getManagedFunctionalMenuForNotificationTree');
            this.$http({
                method: 'GET',
                url: this.conf.api.functionalMenuForNotificationTree,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                    if (this.utilsService.isValidJSON(res)== false) {
                        deferred.reject('FunctionalMenuService::getManagedFunctionalMenuForNotificationTree Failed');
                        
                    } else {
                        this.$log.info('FunctionalMenuService::getManagedFunctionalMenuForNotificationTree succeeded: ');
                        deferred.resolve(res.data);
                    }
                })
                .catch(status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }


        regenerateFunctionalMenuAncestors() {
            let deferred = this.$q.defer();
            this.$log.info('FunctionalMenuService::regenerateFunctionalMenuAncestors');

            this.$http({
                method: 'GET',
                url: this.conf.api.regenerateFunctionalMenuAncestors,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                    if (this.utilsService.isValidJSON(res)== false) {
                        deferred.reject('FunctionalMenuService::regenerateFunctionalMenuAncestors Failed');
                    } else {
                        this.$log.info('FunctionalMenuService::regenerateFunctionalMenuAncestors succeeded: ');
                        deferred.resolve(res.data);
                    }
                })
                .catch(status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }

        saveEditedMenuItem(menuData) {
            let deferred = this.$q.defer();
            this.$log.info('FunctionalMenuService::saveEditedMenuItem: ' + menuData);
        
            let url = this.conf.api.functionalMenuItem;
            this.$http({
                method: 'PUT',
                url: url,
                cache: false,
                data: menuData,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                // If response comes back as a redirected HTML page which IS NOT a success
                if (this.utilsService.isValidJSON(res)== false) {
                    deferred.reject('FunctionalMenuService::saveEditedMenuItem Failed');
                } else {
                    this.$log.info('FunctionalMenuService::saveEditedMenuItem succeeded: ');
                    deferred.resolve(res.data);
                }
                })
                .catch(errRes => {
                    deferred.reject(errRes);
                });
            return deferred.promise;
        }
        
        saveMenuItem(menuData) {
            let deferred = this.$q.defer();
            this.$log.info('FunctionalMenuService::saveMenuItem: ' + JSON.stringify(menuData));

            let url = this.conf.api.functionalMenuItem;
            this.$http({
                method: 'POST',
                url: url,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                },
                data: menuData
            }).then(res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                    if (this.utilsService.isValidJSON(res)== false) {
                        deferred.reject('FunctionalMenuService::saveMenuItem:  Failed');
                    } else {
                        this.$log.info('FunctionalMenuService::saveMenuItem:  succeeded: ');
                        deferred.resolve(res.data);
                    }
                })
                .catch(errRes => {
                    deferred.reject(errRes);
                });
            return deferred.promise;
        }


        deleteMenuItem(menuId) {
            let deferred = this.$q.defer();
            let url = this.conf.api.functionalMenuItem + '/' + menuId;

            this.$log.info('FunctionalMenuService::deleteMenuItem: ' +menuId);

            this.$http({
                method: 'DELETE',
                url: url,
                cache: false,
                data:'',
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                    if (this.utilsService.isValidJSON(res)== false) {
                        deferred.reject('FunctionalMenuService::deleteMenuItem Failed');
                    } else {
                        this.$log.info('FunctionalMenuService::deleteMenuItem succeeded: ');
                        deferred.resolve(res.data);
                    }
                })
                .catch(errRes => {
                    deferred.reject(errRes);
                });
            return deferred.promise;
        }


        getFunctionalMenuRole()
        {
            let deferred = this.$q.defer();
            this.$log.info('FunctionalMenuService::getFunctionalMenuRole');

            this.$http({
                method: 'GET',
                url: this.conf.api.getFunctionalMenuRole,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                    if (this.utilsService.isValidJSON(res)== false) {
                        deferred.reject('FunctionalMenuService::getFunctionalMenuRole Failed');
                    } else {
                        this.$log.info('FunctionalMenuService::getFunctionalMenuRole succeeded: ');
                        deferred.resolve(res.data);
                    }
                })
                .catch(status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }

}
    FunctionalMenuService.$inject = ['$q', '$log', '$http', 'conf','uuid4', 'utilsService'];
    angular.module('ecompApp').service('functionalMenuService', FunctionalMenuService)
})();
