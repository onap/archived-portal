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
 * Created by nnaffar on 1/18/16.
 */
'use strict';

(function () {
    class ConfirmBoxService {
        constructor($q, $log, ngDialog,$modal) {
            this.$q = $q;
            this.$log = $log;
            this.ngDialog = ngDialog;
            this.$modal = $modal;
        }
        reloadPageConfirm(msg){
        	let deferred = this.$q.defer();
          	var modalInstance = this.$modal.open({
                  templateUrl: 'app/views/confirmation-box/reload-page-confirm.html',
                  controller: 'ConfirmationBoxCtrl',
                  sizeClass:'modal-small',
                  resolve: {
                      message: function message() {
                          var message = {
                              title:msg.title,
                              content: msg.content
                          };
                          return message;
                     }
                  }
              });
          	modalInstance.result.then(function () {
          		deferred.resolve();
          	});  
          	return deferred.promise;
        }
        showInformation(msg) {
            let deferred = this.$q.defer();
        	var modalInstance = this.$modal.open({
                templateUrl: 'app/views/confirmation-box/information-box.tpl.html',
                controller: 'ConfirmationBoxCtrl',
                sizeClass:'modal-small',
                resolve: {
                    message: function message() {
                        var message = {
                            title:'',
                            content: msg
                        };
                        return message;
                    }
                }
            });
        	modalInstance.result.then(function () {
        		deferred.resolve();
        	});  
        	return deferred.promise;
        };
        
        editItem(msg) {
        	let deferred = this.$q.defer();
       	 	var modalInstance = this.$modal.open({
                templateUrl: 'app/views/confirmation-box/confirmation-box.tpl.html',
                controller: 'ConfirmationBoxCtrl',
                sizeClass:'modal-small',
                resolve: {
                    message: function message() {
                        var message = {
                            title:'',
                            content: msg
                        };
                        return message;
                    }
                }
            });
            modalInstance.result.then(function (confirm) {
        		if(confirm)
        			deferred.resolve(confirm);
        		else
        			deferred.reject(confirm);
        	});      	
        	return deferred.promise;
        };
       
        
        showDynamicInformation(msg, templatePath, controller) {
        	let deferred = this.$q.defer();
        	var modalInstance = this.$modal.open({
                templateUrl: templatePath,
                controller: controller,
                sizeClass:'modal-small',
                resolve: {
                    message: function message() {
                        var message = {
                            title:'',
                            content: msg
                        };
                        return message;
                    }
                }
            });
        	modalInstance.result.then(function () {
        		deferred.resolve();
        	});  
        	return deferred.promise;
        };
        
        confirm(msg) {
        	let deferred = this.$q.defer();
       	 	var modalInstance = this.$modal.open({
                templateUrl: 'app/views/confirmation-box/confirmation-box.tpl.html',
                controller: 'ConfirmationBoxCtrl',
                sizeClass:'modal-small',
                resolve: {
                    message: function message() {
                        var message = {
                            title:'',
                            content: msg
                        };
                        return message;
                    }
                }
            });
            modalInstance.result.then(function (confirm) {
        		if(confirm)
        			deferred.resolve(confirm);
        		else
        			deferred.reject(confirm);
        	});      	
        	return deferred.promise;
        };

        deleteItem(item) {
        	let deferred = this.$q.defer();
        	 var modalInstance = this.$modal.open({
                templateUrl: 'app/views/confirmation-box/confirmation-box.tpl.html',
                controller: 'ConfirmationBoxCtrl',
                sizeClass:'modal-small',
                resolve: {
                    message: function message() {
                        var message = {
                        	item:'',                     
                            title:'Confirmation',
                            content: 'Are you sure you want to delete ' +item+ ' ?'
                        };
                        return message;
                    }
                }
            });    	 
        	modalInstance.result.then(function (confirm) {
        		if(confirm)
        			deferred.resolve(confirm);
        		else
        			deferred.reject(confirm);
        	});      	
        	return deferred.promise;
        };

        moveMenuItem(msg) {
            let deferred = this.$q.defer();
	       	var modalInstance = this.$modal.open({
	               templateUrl: 'app/views/confirmation-box/dragdrop-confirmation-box.tpl.html',
	               controller: 'ConfirmationBoxCtrl',
	               sizeClass:'modal-small',
	               resolve: {
	                   message: function message() {
	                       var message = {
	                       	item:'',                     
	                           title:'Functional Menu - Move',
	                           content: msg
	                       };
	                       return message;
	                   }
	               }
	           });    	 
	       	modalInstance.result.then(function (confirm) {
	       		if(confirm)
	       			deferred.resolve(confirm);
	       		else
	       			deferred.reject(confirm);
	       	});      	
	       	return deferred.promise;
        };

        makeAdminChanges(msg) {
        	let deferred = this.$q.defer();
	       	var modalInstance = this.$modal.open({
	               templateUrl: 'app/views/confirmation-box/admin-confirmation-box.tpl.html',
	               controller: 'ConfirmationBoxCtrl',
	               sizeClass:'modal-small',
	               resolve: {
	                   message: function message() {
	                       var message = {
	                       	item:'',                     
	                           title:'Admin Update',
	                           content: msg
	                       };
	                       return message;
	                   }
	               }
	           });    	 
	       	modalInstance.result.then(function (confirm) {
	       		if(confirm)
	       			deferred.resolve(confirm);
	       		else
	       			deferred.reject(confirm);
	       	});      	
	       	return deferred.promise;
        };
        
        
        makeUserAppRoleCatalogChanges(msg) {
        	let deferred = this.$q.defer();
	       	var modalInstance = this.$modal.open({
	               templateUrl: 'app/views/confirmation-box/admin-confirmation-box.tpl.html',
	               controller: 'ConfirmationBoxCtrl',
	               sizeClass:'modal-small',
	               resolve: {
	                   message: function message() {
	                       var message = {
	                       	item:'',                     
	                           title:'UserRoles Update',
	                           content: msg
	                       };
	                       return message;
	                   }
	               }
	           });    	 
	       	modalInstance.result.then(function (confirm) {
	       		if(confirm)
	       			deferred.resolve(confirm);
	       		else
	       			deferred.reject(confirm);
	       	});      	
	       	return deferred.promise;
        };
     
        
        webAnalyticsChanges(msg) {
        	let deferred = this.$q.defer();
	       	var modalInstance = this.$modal.open({
	               templateUrl: 'app/views/confirmation-box/admin-confirmation-box.tpl.html',
	               controller: 'ConfirmationBoxCtrl',
	               sizeClass:'modal-small',
	               resolve: {
	                   message: function message() {
	                       var message = {
	                       	item:'',                     
	                           title:'Add WebAnalytics Source',
	                           content: msg
	                       };
	                       return message;
	                   }
	               }
	           });    	 
	       	modalInstance.result.then(function (confirm) {
	       		if(confirm)
	       			deferred.resolve(confirm);
	       		else
	       			deferred.reject(confirm);
	       	});      	
	       	return deferred.promise;
        };

        
        updateWebAnalyticsReport(msg) {
            let deferred = this.$q.defer();
	       	var modalInstance = this.$modal.open({
	               templateUrl: 'app/views/confirmation-box/admin-confirmation-box.tpl.html',
	               controller: 'ConfirmationBoxCtrl',
	               sizeClass:'modal-small',
	               resolve: {
	                   message: function message() {
	                       var message = {
	                       	item:'',                     
	                           title:'Update WebAnalytics Source',
	                           content: msg
	                       };
	                       return message;
	                   }
	               }
	           });    	 
	       	modalInstance.result.then(function (confirm) {
	       		if(confirm)
	       			deferred.resolve(confirm);
	       		else
	       			deferred.reject(confirm);
	       	});      	
	       	return deferred.promise;
        };

    }
    ConfirmBoxService.$inject = ['$q', '$log', 'ngDialog','$modal'];
    angular.module('ecompApp').service('confirmBoxService', ConfirmBoxService)
})();
