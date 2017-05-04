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
    class WidgetParameterController {
        constructor($scope, widgetsCatalogService, userProfileService) {
        	let widgetId = $scope.ngDialogData.widgetId;
        	$scope.modflag = false;
        	$scope.isLoadingTable = false;
        	$scope.messageInfo = false;
        	
        	widgetsCatalogService.getWidgetCatalogParameters(widgetId).then(res => {
        		if(res.status == 'OK'){
        			$scope.isLoadingTable = true;
        			$scope.widgetParam = res.response;
        		}else if(res.status == 'WARN'){
        			$scope.messageInfo = true;
        		}
             }).catch(err => {
                 $log.error('WidgetParameterController::init error: ' + err);
             })
            	
        	 $scope.setEdit = function(index) {
 	        	if($scope.modflag === false){
 	        		$scope.widgetParam[index].showEdit = true;
 		        	$scope.modflag = true;
 	        	}
 	         }	        
        	
        	 $scope.resetDefault = function(index) {
        		var widgetParamObject = {};
             	widgetParamObject.user_value = $scope.widgetParam[parseInt(index)].default_value;
             	widgetParamObject.paramId = $scope.widgetParam[parseInt(index)].param_id;
             	widgetParamObject.widgetId = widgetId;
  	        	widgetsCatalogService.saveWidgetParameter(widgetParamObject)
         		.then(function(res){
         			if(res.status == 'OK'){
         				$scope.widgetParam[index].user_value = $scope.widgetParam[index].default_value;
         			}
         		});
  	         }	     
        	
        	 $scope.modify = function(index) {
         		
         		var widgetParamObject = {};
         		widgetParamObject.user_value = $scope.widgetParam[parseInt(index)].user_value;
         		widgetParamObject.paramId = $scope.widgetParam[parseInt(index)].param_id;
         		widgetParamObject.widgetId = widgetId;
         		
         		widgetsCatalogService.saveWidgetParameter(widgetParamObject)
         		.then(function(res){
         			if(res.status == 'OK'){
         				$scope.modflag = false;
         				$scope.widgetParam[index].showEdit = false;
         			}
         		});
         		
         	};	
        }           
    }
    WidgetParameterController.$inject = ['$scope', 'widgetsCatalogService', 'userProfileService'];
    angular.module('ecompApp').controller('WidgetParameterController', WidgetParameterController); 
})();
