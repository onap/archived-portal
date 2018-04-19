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
 * Created by robertlo on 09/26/2016.
 */
'use strict';

(function () {
    class DashboardService {
        constructor($q, $log, $http, conf, uuid) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.dashboardService = null;
            this.uuid = uuid;
        }

        getCommonWidgetData(widgetType) {
            // this.$log.info('ecomp::dashboard-service::getting news data');
            let deferred = this.$q.defer();
            let url = this.conf.api.commonWidget + '?resourceType=' + widgetType;
         	 
            this.$http({
                    method: "GET",
                    url: url,
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                	 // this.$log.info('ecomp::dashboard-service::getting news data',res);
                    // If response comes back as a redirected HTML page which IS NOT a success
                    if (Object.keys(res.data).length == 0 || Object.keys(res.data.response) ==null || Object.keys(res.data.response.items) ==null) {
                        deferred.reject("ecomp::dashboard-service::getNewsData Failed");
                    } else {
                        this.userProfile = res.data;
                        // this.$log.info('ecomp::dashboard-service::getNewsData Succeeded');
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }
        
        saveCommonWidgetData(newData){
            let deferred = this.$q.defer();
            //this.$log.info('ecomp::dashboard-service::saveCommonWidgetData');
            //this.$log.debug('ecomp::dashboard-service::saveCommonWidgetData with:', newData);

            this.$http({
                method: "POST",
                url: this.conf.api.commonWidget,
                data: newData,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
            		// this.$log.info(res.data);
                    if (Object.keys(res.data).length == 0) {
                        deferred.reject("ecomp::dashboard-service::saveCommonWidgetData Failed");
                    } else {
                        // this.$log.info('ecomp::dashboard-service::saveCommonWidgetData Succeeded');
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }
        
        
        
        removeCommonWidgetData(widgetToRemove){
            let deferred = this.$q.defer();
            // this.$log.info('ecomp::dashboard-service::removeCommonWidgetData');
            // this.$log.debug('ecomp::dashboard-service::removeCommonWidgetData with:', widgetToRemove);
            this.$http({
                method: "POST",
                url: this.conf.api.deleteCommonWidget,
                data: widgetToRemove,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
            		// this.$log.info(res.data);
                    if (Object.keys(res.data).length == 0) {
                        deferred.reject("ecomp::dashboard-service::saveCommonWidgetData Failed");
                    } else {
                        // this.$log.info('ecomp::dashboard-service::saveCommonWidgetData Succeeded');
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }

        getSearchAllByStringResults(searchStr) {
        	// this.$log.info('ecomp::getSearchAllByStringResults::getting search by string results');
            let deferred = this.$q.defer();
            let url = this.conf.api.getSearchAllByStringResults;
            
            this.$http({
                    method: "GET",
                    url : url,
                    params : {
                    	'searchString' : searchStr
                    },
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                }).then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                    if (Object.keys(res.data).length == 0) {
                        deferred.reject("ecomp::dashboard-service::getSearchAllByStringResults Failed");
                    } else {
                        //this.searchResults = res.data;
                        // this.$log.info('ecomp::dashboard-service::getSearchAllByStringResults Succeeded');
                        deferred.resolve(res.data.response);
                    }
                }).catch( status => {
                    this.$log.error('ecomp::getSearchAllByStringResults error');
                    deferred.reject(status);
                });
            return deferred.promise;
        	
        }
        
        getOnlineUserUpdateRate() {
            let deferred = this.$q.defer();
            let url = this.conf.api.onlineUserUpdateRate;
            this.$http({
                    method: "GET",
                    url: url,
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                }).then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                	if (Object.keys(res.data).length == 0) {
                        deferred.reject("ecomp::dashboard-service::getOnlineUserUpdateRate Failed");
                    } else {
                    	// this.$log.info('ecomp::dashboard-service::getOnlineUserUpdateRate Succeeded',res);
                        deferred.resolve(res.data);
                    }
                }).catch( status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }
    
    }
    
    DashboardService.$inject = ['$q', '$log', '$http', 'conf', 'uuid4'];
    angular.module('ecompApp').service('dashboardService', DashboardService)
})();
