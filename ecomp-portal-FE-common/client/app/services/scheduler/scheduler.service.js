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
	class SchedulerService {
		constructor($q, $log, $http, conf, uuid, utilsService,$modal) {
			this.$q = $q;
			this.$log = $log;
			this.$http = $http;
			this.conf = conf;
			this.uuid = uuid;
			this.utilsService = utilsService;
			this.$modal = $modal;
			this.widgetInfo={
					id:'',
					data:'',
					param:''
			}
		}

		/** get scheduler uuID **/
		getStatusSchedulerId(schedulerInfo) {
			let deferred = this.$q.defer();
			var url = this.conf.api.getSchedulerId
			this.$http({
				url: url+'?r='+ Math.random(),
				method: 'POST',
				cache: false,
				timeout: 60000,
				data:schedulerInfo,
				headers: {
					'X-ECOMP-RequestID':this.uuid.generate()
				}
			}).then(res => {
				if (res == null || res.data == null || this.utilsService.isValidJSON(res.data)== false) {
					deferred.reject('SchedulerService::getStatusSchedulerId Failed');
				} else {
					deferred.resolve(res.data);
				}
			}).catch(status => {
				deferred.reject(status);
			});
			return deferred.promise;
		}

		/** get time slots for Range scheduler **/
		getTimeslotsForScheduler(schedulerID) {
			let deferred = this.$q.defer();
			var url = this.conf.api.getTimeslotsForScheduler + '/' + schedulerID + '?r=' + Math.random();
			this.$http({
				url: url,
				method: 'GET',
				timeout: 60000,
				cache: false,
				headers: {
					'X-ECOMP-RequestID':this.uuid.generate()
				},
			}).then(res => {
				this.$log.debug('SchedulerService:: this.conf.api.portalAdmin: ' + JSON.stringify(res));
				if (res == null || res.data == null || this.utilsService.isValidJSON(res.data)== false) {
					deferred.reject('SchedulerService::getTimeslotsForScheduler Failed');
				} else {
					deferred.resolve(res.data);
				}
			}).catch(errRes => {
				deferred.reject(errRes);
			});
			return deferred.promise;
		}

		
		postSubmitForApprovedTimeslots(approvedTimeSlotsObj) {
			let deferred = this.$q.defer();
			let url = this.conf.api.postSubmitForApprovedTimeslots + '?r='+ Math.random();
			this.$http({
				url: url,
				method: 'POST',
				timeout: 60000,
				cache: false,
				data: approvedTimeSlotsObj, 
				headers: {
					'X-ECOMP-RequestID':this.uuid.generate()
				}
			}).then(res => {
				if (res == null || res.data == null || this.utilsService.isValidJSON(res.data)== false) {
					deferred.reject('SchedulerService::postSubmitForapprovedTimeslots');
				} else {
					deferred.resolve(res.data);
				}
			}).catch(errRes => {
				deferred.reject(errRes);
			});

			return deferred.promise;
		}

		/** Get policy information from BE **/
		getPolicyInfo() {
			let deferred = this.$q.defer();
			let url =  this.conf.api.getPolicy + '?r=' + Math.random();
			this.$http({
				url: url,
				method: 'GET',
				timeout: 60000,
				cache: false,
				headers: {
					'X-ECOMP-RequestID':this.uuid.generate()
				}
			}).then(res => {
				if (res == null || res.data == null || this.utilsService.isValidJSON(res.data)== false) {
					deferred.reject('SchedulerService::getPolicyInfo');
				} else {
					deferred.resolve(res.data);
				}
			}).catch(errRes => {
				deferred.reject(errRes);
			});

			return deferred.promise;
		}

		/** get Scheduler UI constants from BE **/
		getSchedulerConstants() {
			let deferred = this.$q.defer();
			let url =  this.conf.api.getSchedulerConstants;       
			this.$http({
				url: url,
				method: 'GET',
				timeout: 60000,
				cache: false,
				headers: {
					'X-ECOMP-RequestID':this.uuid.generate()
				}
			}).then(res => {
				if (res == null || res.data == null || this.utilsService.isValidJSON(res.data)== false) {
					deferred.reject('SchedulerService::getSchedulerConstant');
				} else {
					deferred.resolve(res.data);
				}
			}).catch(errRes => {
				deferred.reject(errRes);
			});
			return deferred.promise;
		}

		
		/**Opens up the popup for Scheduler UI**/
		showWidget(widgetId,widgetData,widgetParam) {
			let deferred = this.$q.defer();
			this.widgetInfo.id = widgetId;
			this.widgetInfo.data = widgetData;
			this.widgetInfo.param = widgetParam;   	
			var modalInstance = this.$modal.open({
				templateUrl: 'app/views/scheduler/scheduler.tpl.html',
				controller: 'SchedulerCtrl',
				sizeClass:'modal-large',
				windowClass:"modal-docked",
				resolve: {
					message: function message() {
						var message = {
								id: widgetId,
								data: widgetData,
								param: widgetParam
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

		getWidgetData(){
			return this.widgetInfo;
		}
	}
	SchedulerService.$inject = ['$q', '$log', '$http', 'conf', 'uuid4', 'utilsService', '$modal'];
	angular.module('ecompApp').service('schedulerService', SchedulerService)
})();
