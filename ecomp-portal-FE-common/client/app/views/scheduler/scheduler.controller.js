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
	class SchedulerCtrl {
		constructor($scope,$state,message,$modalInstance,widgetsCatalogService,$log,schedulerService,$filter,confirmBoxService,userProfileService,conf,$interval,$compile,$rootScope) {
			$scope.parentData = message;
			/****define fields****/
			$scope.widgetName= '';
			$scope.widgetData = [];
			

			/**** Functions for widgets *****/
			$scope.getWidgetData =function(widgetId){
				$rootScope.showSpinner = true;
				var script = document.createElement('script');
				script.src =  conf.api.widgetCommon + "/" + widgetId + "/framework.js";
				script.async = true;
				var entry = document.getElementsByTagName('script')[0];
				entry.parentNode.insertBefore(script, entry);			
			}
			$scope.getUserWidgets = function(loginName){ 
				this.isCommError = false;
				widgetsCatalogService.getUserWidgets(loginName).then(res => {
					if(!(res instanceof Array)){
						$rootScope.showSpinner = false;
						this.isCommError = true;
						return;
					}
					for(var i = 0; i < res.length; i++){	
						var widget_id = res[i][0];
						var widget_name = res[i][1];
						if(widget_name== $scope.widgetName){
							var data={
								attr:'data-'+widget_id,
								value:''
							};
							$scope.widgetData.push(data);
							$scope.getWidgetData(widget_id);
						}
					}
				}).catch(err => {
					$rootScope.showSpinner = false;
					$log.error('WidgetCatalogCtrl::getUserWidgets caught error', err);
				})
			};
			

			$rootScope.closeModal = function(){
				$modalInstance.dismiss('cancel');
			}
			$scope.getUserIdAndWidget = function(){
				$rootScope.showSpinner = true;
				userProfileService.getUserProfile()
				.then(profile=> {
					$scope.orgUserId = profile.orgUserId;
					$scope.getUserWidgets($scope.orgUserId);  
				})
			}
			
			$scope.activateThis = function(ele){
   			 	$compile(ele.contents())($scope);
   			 	$scope.$apply();
            };
            
            /*Calling Scheduler UI controller in microservice*/
            $scope.submit = function(){
            	$scope.$broadcast('submit','');
            }
            
            $scope.reject = function(){
            	$scope.$broadcast('reject','');
            }
            
            $scope.schedule = function(){
            	$scope.$broadcast('schedule','');
            }
            
            
            /** init **/
            var init = function () {				
				$rootScope.showSpinner = false;
				$scope.widgetName = message.id;
				$scope.getUserIdAndWidget(); //get logged in user id, then widget data			

			};
			init();
		}
	}
	SchedulerCtrl.$inject = ['$scope','$state','message','$modalInstance','widgetsCatalogService','$log', 'schedulerService','$filter','confirmBoxService','userProfileService','conf','$interval','$compile','$rootScope'];
	angular.module('ecompApp').controller('SchedulerCtrl', SchedulerCtrl);
})();
