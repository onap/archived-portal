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
/**
 * Created by wl849v on 12/14/2016.
 */
'use strict';
(function () {
    class RecommendationService {
    	constructor($q, $log, $http, conf, uuid,utilsService) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.uuid = uuid;
            this.recommendationCount = {count:0};
            this.refreshCount = 0;
            this.maxCount = 0;
            this.utilsService = utilsService;            
        }   	
        getRecommendationCount() {
         	 return this.recommendationCount;
         }
        setRecommendationCount(count) {
        	this.recommendationCount.count = count;
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
        
        
        getRecommendations(){
    		let deferred = this.$q.defer();
            this.$http({
                    method: "GET",
                    cache: false,
                    url: this.conf.api.getRecommendations,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                })
                .then( res => {
                    // If response comes back as a redirected HTML page which IS NOT a success           	
                    if (this.utilsService.isValidJSON(res.data)=== false) {
                    	this.$log.error('NotificationService::getRecommendations Failed');
                        deferred.reject("NotificationService::getRecommendations Failed");
                    } else {
                        deferred.resolve(res);
                    }
                })
                .catch( status => {
                	this.$log.error('NotificationService::getRecommendations Failed', status);
                    deferred.reject(status);
                });
           
            return deferred.promise;
    	}
        
       
    }
    RecommendationService.$inject = ['$q', '$log', '$http', 'conf', 'uuid4','utilsService'];
    angular.module('ecompApp').service('recommendationService', RecommendationService)
})();
