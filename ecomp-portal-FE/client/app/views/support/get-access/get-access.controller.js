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
    class GetAccessCtrl {
        constructor($log,  $stateParams, getAccessService,applicationsService, ngDialog) {
        	// $log.debug('GetAccessCtrl: appService param is: ' + applicationsService.goGetAccessAppName);

        	this.updateAppsList = () => {
                getAccessService.getListOfApp().then(res=> {
                	var tableData=[];
                	// $log.info('GetAccessCtrl::updateAppsList: getting res');
                	var result = (typeof(res.data) != "undefined" && res.data!=null)?res.data:null;
                	// $log.info('GetAccessCtrl::updateAppsList: result',result);
                	// $log.info('GetAccessCtrl::updateAppsList: done');
                	var source = result;
                	// $log.info('GetAccessCtrl::updateAppsList source: ', source);
                	for(var i=0;i<source.length; i++){
                		var dataArr = source[i];
                    	var dataTemp={
                    		ecomp_function: dataArr.ecompFunction,
                    		app_name:dataArr.appName,
                    		role_name:dataArr.roleName
                    	}
                    	tableData.push(dataTemp);
                	}              	
                	this.appTable=tableData;
                	
                	if( $stateParams.appName != null)
                		this.searchString = $stateParams.appName;
                	else
                		this.searchString = applicationsService.goGetAccessAppName;
                	// the parameter has been used; clear the value.
                	applicationsService.goGetAccessAppName = '';
                }).catch(err=> {
                    $log.error('GetAccessCtrl:error:: ', err);
                }).finally(() => {
                    this.isLoadingTable = false;
                });
            };
        	let init = () => {
                // $log.info('GetAccessCtrl:: initializing...');
                this.searchString = '';
                this.getAccessTableHeaders = ['ECOMP Function', 'Application Name', 'Role Name'];
                this.appTable=[];
                this.updateAppsList();
            };
            init();
        }
    }
    GetAccessCtrl.$inject = ['$log', '$stateParams', 'getAccessService', 'applicationsService', 'ngDialog'];
    angular.module('ecompApp').controller('GetAccessCtrl', GetAccessCtrl);
})();
