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
    class FooterCtrl {
        constructor($scope, $rootScope, manifestService, $log) {
            this.manifestService = manifestService;
            this.$log = $log;
            this.$scope = $scope;
            this.$rootScope = $rootScope;

            $scope.buildinfo = null;

            $rootScope.showFooter = true;
            $scope.date = new Date();

                   
            manifestService.getManifest().then( jsonObj => {
                // $log.debug('FooterCtrl.getManifest: ', JSON.stringify(jsonObj));
                $scope.buildInfo = jsonObj;

                }).catch(err=> {
                    $log.error('FooterCtrl::updateTableData error :',err);
                });
            };

        }

    FooterCtrl.$inject = ['$scope', '$rootScope', 'manifestService', '$log'];
    angular.module('ecompApp').controller('FooterCtrl', FooterCtrl);
})();
