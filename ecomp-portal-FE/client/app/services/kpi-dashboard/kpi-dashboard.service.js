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
    class KpiDashboardService {
        constructor($q, $log, $http, conf, uuid) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.uuid = uuid;
            this.applicationsHomeUrl = 'kpidash';             
        }

        kpiApiCall(ApiName) {
            let deferred = this.$q.defer();
            this.$log.info('KpiDashboardService::kpiApiCall: '+ApiName);
            this.$http({
                    method: 'GET',
                    url: this.conf.api[ApiName],
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                }).then(res => {
                    this.$log.debug("KpiDashboardService::kpiApiCall: response successfully retrieved");
                    if (Object.keys(res.data).length == 0) {
                        deferred.reject("KpiDashboardService::kpiApiCall: "+ ApiName+ " Failed");
                    } else {                        
                        deferred.resolve(res);
                    }
                })
                .catch(status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }

        getKpiUserStoriesStats(){
            return this.kpiApiCall("getKpiDashUserStoriesStats");
        }
        getKpiUserApiStats(){
            return this.kpiApiCall("getKpiDashUserApiStats");
        }
        getKpiLocStats(){
            return this.kpiApiCall("getKpiDashLocStats");
        }
        getKpiLocStatsCat(){
            return this.kpiApiCall("getKpiDashLocStatsCat");
        }
        getKpiServiceSupported(){
            return this.kpiApiCall("getKpiDashServiceSupported");
        }
        getKpiPublishedDelivered(){
            return this.kpiApiCall("getKpiDashPublishedDelivered");
        }
        getKpiFeedStats(){
            return this.kpiApiCall("getKpiDashFeedStats");
        }
        getKpiUserApis(){
            return this.kpiApiCall("getKpiDashUserApis");
        }
        getKpiGeoMapUrl(){
            return this.kpiApiCall("getKpiDashGeoMapUrl");
        }
        getKpiRCloudAUrl(){
            return this.kpiApiCall("getKpiDashRCloudAUrl");            
        }
        getKpiGeoMapApiUrl(){
            return this.kpiApiCall("getKpiDashGeoMapApiUrl");
        }

        getToplevelgTabs1() {
            var toplevelgTabs1=[{
                title : 'eCOMP',
                id : 'ECOMP',
		        url : this.applicationsHomeUrl,
                state : 'root.kpidash_ECOMP'
            }]
            return toplevelgTabs1;
        }

        getToplevelgTabs2() {
            var toplevelgTabs2 = [ {
                title : 'A&AI',
                id : 'A&AI',
                url : this.applicationsHomeUrl,
                state : 'root.kpidash_AAI'
            }, {
                title : 'APP-C',
                id : 'APP-C',
                url : this.applicationsHomeUrl,
                state : 'root.kpidash_APPC'
            }, {
                title : 'ASDC',
                id : 'ASDC',
                url : this.applicationsHomeUrl,
                state : 'root.kpidash_ASDC'
            }, {
                title : 'DCAE',
                id : 'DCAE',
                url : this.applicationsHomeUrl,
                state : 'root.kpidash_DCAE'
            }, {
                title : 'OpenECOMP Portal',
                id : 'OpenECOMP Portal',
                url : this.applicationsHomeUrl,
                state : 'root.kpidash_ECOMP_Portal'
            }, {
                title : 'i-Portal',
                id : 'i-Portal',
                url : this.applicationsHomeUrl,
                state : 'root.kpidash_InfrastructurePortal'
            }, {
                title : 'MSO',
                id : 'MSO',
                url : this.applicationsHomeUrl,
                state : 'root.kpidash_MSO'
            }, {
                title : 'Policy',
                id : 'Policy',
                url : this.applicationsHomeUrl,
                state : 'root.kpidash_Policy'
            }];
            return toplevelgTabs2;
        }

        getToplevelgTabs3() {
            var toplevelgTabs3 = [ {
						title : 'Closed Loop',
						id : 'Closedloop',
						url : this.applicationsHomeUrl,
						state : 'root.kpidash_Closedloop'
					}, {
						title : 'eDMaaP',
						id : 'DMaaP',
						url : this.applicationsHomeUrl,
						state : 'root.kpidash_DMaaP'
					} ];
            return toplevelgTabs3;
        }

        getGenericTabs(activeTab) {
            var gTabs = [ {
                        title : 'KPI',
                        id : 'KPI',
                        url : this.applicationsHomeUrl,
                        state : 'root.kpidash_'+activeTab+'_KPI'
                    }, {
                        title : 'User Defined KPI',
                        id : 'User Defined KPI',
                        url : this.applicationsHomeUrl,
                        state : 'root.kpidash_'+activeTab+'_UserDefinedKPI'
                    }, {
                        title : 'Metrics',
                        id : 'Metrics',
                        url : this.applicationsHomeUrl,
                        state : 'root.kpidash_'+activeTab+'_Metrics'
                    } ];
            return gTabs;        
        }
    }

    KpiDashboardService.$inject = ['$q', '$log', '$http', 'conf', 'uuid4'];
    angular.module('ecompApp').service('KpiDashboardService', KpiDashboardService)
})();
