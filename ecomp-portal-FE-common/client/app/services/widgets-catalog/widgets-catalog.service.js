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
	class WidgetsCatalogService {
        constructor($q, $log, $http, conf,uuid,base64Service, beReaderService, utilsService) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.uuid = uuid;
            this.base64Service = base64Service;
            this.beReaderService = beReaderService;
            this.utilsService = utilsService;
        }
        
        getUserWidgets(loginName) {
        	 let deferred = this.$q.defer();
  	         this.$http({
  	        	 method: "GET",
	             url: this.conf.api.widgetCommon + '/widgetCatalog' + '/' + loginName,
	             cache: false,
	             headers: {
	            	 'X-Widgets-Type': 'all',
	                 'X-ECOMP-RequestID':this.uuid.generate()
	             }
             }).then(res => {
        	 	if (res == null || res.data == null) {
                     deferred.reject("WidgetsCatalogService::getUserWidgets Failed");
                 } else {
                     deferred.resolve(res.data);
                 }
             })
             .catch(status => {
            	 deferred.reject(status);
	         });
             return deferred.promise;
        }

        getManagedWidgets() {          	
            let deferred = this.$q.defer();
            let url = this.conf.api.widgetCommon + '/widgetCatalog';
            this.$http({
            	method: "GET",
            	url: url,
            	cache: false,
            	headers: {
            		'X-Widgets-Type': 'all',
            		'X-ECOMP-RequestID':this.uuid.generate()
            	}
            }).then(res => {
        	 	if (res == null || res.data == null) {
                     deferred.reject("WidgetsCatalogService::getManagedWidgets Failed");
                 } else {
                     deferred.resolve(res.data);
                 }
             })
             .catch(status => {
                 deferred.reject(status);
             });  
            return deferred.promise;
        }
        
        createWidget(newWidget, file) {
        	console.log(newWidget);
        	let deferred = this.$q.defer();
        	var formData = new FormData();
        	formData.append('file', file);
        	this.$http({
                method: "POST",
                url: this.conf.api.widgetCommon + '/widgetCatalog',
                transformRequest: angular.identity,  
                data: formData,
                headers:{  
                	'Content-Type': undefined,
                	'X-Widgets-Type': 'all',
	                'X-ECOMP-RequestID':this.uuid.generate()
                },
                params:{
                	'newWidget': newWidget
                }
            }).then(res => {
            	if (res == null || res.data == null) {
                    deferred.reject("WidgetsCatalogService::getManagedWidgets Failed");
                } else {
                    deferred.resolve(res.data);
                }
            })
            .catch(errRes => {
                deferred.reject(errRes);
            });
            return deferred.promise;
        }
        
        updateWidgetWithFile(file, widgetId, newWidget){
        	console.log(widgetId);
        	let deferred = this.$q.defer();
            var formData = new FormData();
        	formData.append('file', file);
    		let url = this.conf.api.widgetCommon + '/widgetCatalog/' + widgetId;
        	this.$http({
                method: 'POST',
                url: url, 
                transformRequest: angular.identity,  
                data: formData,
                headers: {        
                	'Content-Type': undefined,
                	'X-Widgets-Type': 'all',
	                'X-ECOMP-RequestID':this.uuid.generate()
                },
        	 	params:{
        	 		'newWidget': newWidget
        	 	}
            })
            .then(res => {
               if (res == null || res.data == null)
            	   deferred.reject("WidgetsCatalogService::saveWidgetFile Failed");
               else 
            	   deferred.resolve(res.data);
            })
            .catch(status => {
                 deferred.reject(status);
             });  
        	return deferred.promise;
        }
        
        updateWidget(widgetId, widgetData) {
            let deferred = this.$q.defer();
            let url = this.conf.api.widgetCommon  + '/widgetCatalog' + '/' + widgetId;
            this.$http({
            	method: 'PUT',
            	url: url,
            	cache: false,
            	data: widgetData,
            	headers: {
            		'X-Widgets-Type': 'all',
	                'X-ECOMP-RequestID':this.uuid.generate()
            	}
            })
            .then(res => {
            	deferred.resolve(res.data);
            })
            .catch(errRes => {
            	deferred.reject(errRes);
            });
           
            return deferred.promise;
        }
        
        
        deleteWidgetFile(widgetName) {
        	let deferred = this.$q.defer();
            this.$log.info('WidgetsCatalogService::deleteWidgetCatalog');
            let url = this.conf.api.widgetCommon + '/doUpload' + '/' + widgetName;
            this.$http({
                method: "DELETE",
                url: url,
                cache: false,
                headers:{  
                	'X-Widgets-Type': 'all',
	                'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                    deferred.resolve(res.data); 
                })
                .catch(status => {
                	deferred.reject(status);
                });
           
            return deferred.promise;
        }
        
        deleteWidget(widgetId) {
        	let deferred = this.$q.defer();
            this.$log.info('WidgetsCatalogService::deleteWidgetCatalog');
            let url = this.conf.api.widgetCommon + '/widgetCatalog'  + '/' + widgetId;
            this.$http({
                method: "DELETE",
                url: url,
                cache: false,
                headers:{  
                	'X-Widgets-Type': 'all',
	                'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                    deferred.resolve(res.data); 
                })
                .catch(status => {
                	deferred.reject(status);
                });
           
            return deferred.promise;
        }
        
        downloadWidgetFile(widgetId) {
        	let deferred = this.$q.defer();
            this.$log.info('WidgetsCatalogService::downloadWidgetFile');
            let url = this.conf.api.widgetCommon + '/download/' + widgetId;
            console.log(url);
            this.$http({
                method: "GET",
                url: url,
                cache: false,
                responseType: "arraybuffer",
                headers:{  
                	'X-Widgets-Type': 'all',
	                'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                    deferred.resolve(res.data); 
                })
                .catch(status => {
                	deferred.reject(status);
                });
           
            return deferred.promise;
        }
        
        updateWidgetCatalog(appData){
        	let deferred = this.$q.defer();
            // Validate the request, maybe this is overkill
            if (appData == null || appData.widgetId == null || appData.select == null) {
            	var msg = 'WidgetCatalogService::updateAppCatalog: field appId and/or select not found';
                this.$log.error(msg);
                return deferred.reject(msg);
            }
            this.$http({
                method: "PUT",
                url: this.conf.api.widgetCatalogSelection,
                data: appData,
                headers: {
                	 'X-Widgets-Type': 'all',
	                 'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then( res => {
                    // Detect non-JSON
                    if (res == null || res.data == null) {
                        deferred.reject("WidgetCatalogService::updateAppCatalog Failed");
                    } else {
                        deferred.resolve(res.data);
                    }
                })
                .catch( status => {
                	this.$log.error('WidgetCatalogService:updateAppCatalog failed: ' + status);
                    deferred.reject(status);
                });
            return deferred.promise;
        }
        
        getServiceJSON(serviceId){
        	let deferred = this.$q.defer();
            this.$log.info('WidgetsCatalogService::getServiceJSON');
            let url = this.conf.api.microserviceProxy + "/" + serviceId;
            console.log(url);
            this.$http({
                method: "GET",
                url: url,
                cache: false,
                headers:{  
                	'X-Widgets-Type': 'all',
	                'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
	            	if (res == null || res == null) {
	        	 		this.$log.error('WidgetCatalogService::getServiceJSON Failed: Result or result.data is null');
	                    deferred.reject("WidgetCatalogService::getServiceJSON Failed: Result or result.data is null");
	                } else{
	                	deferred.resolve(res.data);
	                }
	                
                })
                .catch(status => {
                	deferred.reject(status);
                });
           
            return deferred.promise;
        }
        
        getWidgetCatalogParameters(widgetId){
        	let deferred = this.$q.defer();
            this.$log.info('WidgetsCatalogService::getWidgetCatalogParameters');
            let url = this.conf.api.widgetCommon + "/parameters/" + widgetId;
            console.log(url);
            this.$http({
                method: "GET",
                url: url,
                cache: false,
                headers:{  
                	'X-Widgets-Type': 'all',
	                'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
	            	if (res == null || res.data == null) {
	        	 		this.$log.error('WidgetCatalogService::getWidgetCatalogParameters Failed: Result or result.data is null');
	                    deferred.reject("WidgetCatalogService::getWidgetCatalogParameters Failed: Result or result.data is null");
	                } else {
	                    deferred.resolve(res.data);
	                }
                })
                .catch(status => {
                	deferred.reject(status);
                });
           
            return deferred.promise;
        }
        
        
        saveWidgetParameter(widgetParamObject){
        	let deferred = this.$q.defer();
            this.$log.info('WidgetsCatalogService::saveWidgetParameter');
            let url = this.conf.api.widgetCommon + "/parameters";
            this.$http({
                method: "POST",
                url: url,
                cache: false,
                data: widgetParamObject,
                headers:{  
                	'X-Widgets-Type': 'all',
	                'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
	            	if (res == null || res.data == null) {
	        	 		this.$log.error('WidgetCatalogService::getWidgetCatalogParameters Failed: Result or result.data is null');
	                    deferred.reject("WidgetCatalogService::getWidgetCatalogParameters Failed: Result or result.data is null");
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
    
    WidgetsCatalogService.$inject = ['$q', '$log', '$http', 'conf','uuid4','base64Service', 'beReaderService', 'utilsService'];
    angular.module('ecompApp').service('widgetsCatalogService', WidgetsCatalogService)
})();
