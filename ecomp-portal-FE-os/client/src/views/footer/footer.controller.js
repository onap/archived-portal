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
    class FooterCtrl {
        constructor($scope, $rootScope, manifestService, $log, menusService) {
            this.manifestService = manifestService;
            this.$log = $log;
            this.$scope = $scope;
            this.$rootScope = $rootScope;
            $scope.buildinfo = null;

            $rootScope.showFooter = true;
            $scope.date = new Date();
            $scope.ecompTitle='';
           
        	menusService.getEcompPortalTitle()
        	.then(title=> {
        		$scope.ecompTitle = title.response;
        	}).catch(err=> {
        		$log.error('FooterCtrl.getEcompPortalTitle:: Error retrieving ECMOP portal title: ' + err);
        	});
     
            manifestService.getManifest().then( jsonObj => {
                // $log.debug('FooterCtrl.getManifest: ', JSON.stringify(jsonObj));
                $scope.buildInfo = jsonObj;

                }).catch(err=> {
                    $log.error('FooterCtrl::updateTableData error :',err);
                });
            };

        }

    FooterCtrl.$inject = ['$scope', '$rootScope', 'manifestService', '$log', 'menusService'];
    angular.module('ecompApp').controller('FooterCtrl', FooterCtrl);
})();
