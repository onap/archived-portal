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
    class FunctionalMenuService {
        constructor($q, $log, $http, conf,uuid) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.uuid = uuid;
        }


        getManagedRolesMenu( appId )
        {
            let deferred = this.$q.defer();
            this.$log.info('FunctionalMenuService::getManagedRolesMenu');
            let url = this.conf.api.appRoles.replace(':appId', appId);
            this.$log.info("FunctionalMenuService::getManagedRolesMenu url: "+url);

            this.$http({
                method: "GET",
                url: url,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                if (Object.keys(res.data).length == 0) {
                    deferred.reject("functionalMenu.service::getManagedRolesMenu Failed");
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
                method: "GET",
//                url: this.conf.api.availableApps,
                url: this.conf.api.allAvailableApps,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                if (Object.keys(res.data).length == 0) {
                    deferred.reject("FunctionalMenuService::getManagedRolesMenu:getAvailableApplications Failed");
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
            this.$log.info("FunctionalMenuService::getMenuDetails url: "+url);

           this.$http({
                method: "GET",
                url: url,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                if (Object.keys(res.data).length == 0) {
                    deferred.reject("FunctionalMenuService::getMenuDetails:getMenuDetails Failed");
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
                method: "GET",
                url: this.conf.api.functionalMenuForEditing,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                if (Object.keys(res.data).length == 0) {
                    deferred.reject("FunctionalMenuService::getManagedFunctionalMenu Failed");
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

        regenerateFunctionalMenuAncestors() {
            let deferred = this.$q.defer();
            this.$log.info('FunctionalMenuService::regenerateFunctionalMenuAncestors');

            this.$http({
                method: "GET",
                url: this.conf.api.regenerateFunctionalMenuAncestors,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                if (Object.keys(res.data).length == 0) {
                    deferred.reject("FunctionalMenuService::regenerateFunctionalMenuAncestors Failed");
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
                method: "PUT",
                url: url,
                cache: false,
                data: menuData,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                if (Object.keys(res.data).length == 0) {
                    deferred.reject("FunctionalMenuService::saveEditedMenuItem Failed");
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
                method: "POST",
                url: url,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                },
                data: menuData
            }).then(res => {
                if (Object.keys(res.data).length == 0) {
                    deferred.reject("FunctionalMenuService::saveMenuItem:  Failed");
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
                method: "DELETE",
                url: url,
                cache: false,
                data:'',
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                if (Object.keys(res.data).length == 0) {
                    deferred.reject("FunctionalMenuService::deleteMenuItem Failed");
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

}
    FunctionalMenuService.$inject = ['$q', '$log', '$http', 'conf','uuid4'];
    angular.module('ecompApp').service('functionalMenuService', FunctionalMenuService)
})();
