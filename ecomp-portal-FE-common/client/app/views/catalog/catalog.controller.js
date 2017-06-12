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

function _classCallCheck(instance, Constructor) {
	if (!(instance instanceof Constructor)) {
		throw new TypeError('Cannot call a class as a function');
	}
}

(function() {
	var HTTP_PROTOCOL_RGX = /https?:\/\//;

	var CatalogCtrl = function CatalogCtrl(conf, catalogService, confirmBoxService, ExternalRequestAccessService,
			$log, $window, userProfileService, applicationsService, $scope, $state, 
			$timeout, $interval, $modal, ngDialog) {

		this.conf = conf;	
		var _this = this;
		var externalRequest = true;
		_classCallCheck(this, CatalogCtrl);

		// activate spinner
		this.isLoading = true;
		$scope.getAppCatalogIsDone = false;
		$scope.radioValue = 'All';
		$scope.$watch('radioValue', function(newValue, oldValue) {
			var appCatalog = $scope.appCatalog;
			$scope.appCatalog = [];
			$scope.appCatalog = appCatalog;

			
		});
		
		this.catalogService = catalogService;
		this.userProfileService = userProfileService;
		this.applicationsService = applicationsService;
		var resultAccessValue = null;
		$scope.demoNum = 1;
		$scope.appRoles = [];
		
        let init = () => {
            getExternalAccess();
        };
        
        var getExternalAccess = () => {
    		ExternalRequestAccessService.getExternalRequestAccessServiceInfo().then(
    				function(property) {
    		resultAccessValue = property.accessValue;
    					// $log.info(res);
    				}).catch(err => {
                        $log.error('CatalogCtrl: failed getExternalRequestAccessServiceInfo: ' + JSON.stringify(err));
                    });    
        };
        

	
		$scope.applyPresentationDetailsToAppsCatalog = function(res, value) {
			
			_this.apps = res;
			var rowNo = 0;

			// defend against error string result -
			// a huge list that should never happen.
			var maxItems = 333;
			if (_this.apps.length < maxItems)
				maxItems = _this.apps.length;
			for (var i = 0; i < maxItems; i++) {												
				let imgLnk = '';
				if (_this.apps[i].imageUrl)
					imgLnk = conf.api.appThumbnail.replace(':appId', _this.apps[i].id);
				//$log.debug('CatalogCtlr::applyPresn: imgLink = ' + imgLnk);
				$scope.appCatalog[i] = {
					sizeX : 2,
					sizeY : 2,
					id : _this.apps[i].id,
					headerText : _this.apps[i].name,
					mlAppName: _this.apps[i].mlAppName,
					imageLink : imgLnk,
					restricted : _this.apps[i].restricted,  
					select : _this.apps[i].select,
					access : _this.apps[i].access,
					pending: _this.apps[i].pending,
					mlproperty: value
				};
			}
			//$log.debug('CatalogCtrl: getAppCatalog count : '
			//				+ $scope.appCatalog.length);
			_this.isLoading = false;
			$scope.getAppCatalogIsDone = true;
		}
		
        let getAppsCatalog = () => {
            catalogService.getAppCatalog()
                .then(appsList => {
                	$scope.applyPresentationDetailsToAppsCatalog(appsList);
                }).catch(err => {
                    confirmBoxService.showInformation('There was a problem retrieving the Applications. ' +
                        'Please try again later. Error Status: '+ err.status).then(isConfirmed => {});
                    $log.error('CatalogCtrl:openAddRoleModal: Error: ', err);
                });
        };
        
        this.openAddRoleModal = (item) => { 
        	let data = null;

            if((!item.restricted) && (item.mlproperty)){
                data = {
                    dialogState: 2,
                    selectedUser:{
                        attuid: $scope.attuid,
                        firstName: $scope.firstName,
                        lastName: $scope.lastName,
                        headerText: item.headerText,
                        haloAppName : item.mlAppName,
                    	item: item,
                    	extReqValue : externalRequest
                        
                    }
                }
            ngDialog.open({
                templateUrl: 'app/views/catalog/add-catalog-dialogs/new-catalog.modal.html',
                controller: 'NewCatalogModalCtrl',
                controllerAs: 'userInfo',
                data: data
            }).closePromise.then(needUpdate => {
                if(needUpdate.value === true){
                	getAppsCatalog();
                }
            	
            });
          }
        };

		// Run this function when user clicks on checkbox.
		this.storeSelection = function(item) {
			// $log.debug('CatalogCtrl:storeSelection: item.id is ' + item.id + ', select is ' + item.select);
			var pendingFlag = false;
			
			if(item.access) 
				pendingFlag = false;
			else
				pendingFlag =  item.pending;
							
			var appData = { 
					appId   : item.id,
					select  : item.select,
					pending : pendingFlag	// TODO
			};
			
			catalogService.updateManualAppSort(appData).then(
					function(result) {
						// $log.debug('CatalogCtrl:storeSelection result is ', result);
					})['catch'](function(err) {
						$log.error('CatalogCtrl:storeSelection: exception: ', err);
					});
			catalogService.updateAppCatalog(appData).then(
				function(result) {
					// $log.debug('CatalogCtrl:storeSelection result is ', result);
				})['catch'](function(err) {
					$log.error('CatalogCtrl:storeSelection: exception: ', err);
				});
		};
		
		userProfileService
				.getUserProfile()
				.then(
						function(profile) {
							$scope.attuid = profile.orgUserId;
							$scope.firstName = profile.firstName;
							$scope.lastName = profile.lastName;
							$scope.appCatalog = [];
							
							catalogService.getAppCatalog().then(
											function(res) {	
									$scope.applyPresentationDetailsToAppsCatalog(res, resultAccessValue);
											})['catch'](function(err) {
									$log.error('CatalogCtrl: failed getAppCatalog: ', JSON.stringify(err));
									_this.isLoading = false;
									$scope.getAppCatalogIsDone = true;
						  });
						});

							// applicationsService.getUserApps()


		this.gridsterOpts = {
			columns : 12,
			colWidth : 95,
			rowHeight : 95,
			margins : [ 20, 20 ],
			outerMargin : true,
			pushing : true,
			floating : true,
			swapping : true,
			resizable: {
				enabled: false,
			},
		};

		if (getParameterByName('noUserError') != null) {
			if (getParameterByName('noUserError') == "Show") {
				$("#errorInfo").show();
			}

		}
		
		init();
	};

	CatalogCtrl.$inject = [ 'conf', 'catalogService', 'confirmBoxService', 'ExternalRequestAccessService', '$log',
			'$window', 'userProfileService', 'applicationsService', '$scope', 
			'$state', '$timeout', '$interval', '$modal', 'ngDialog' ];
	angular.module('ecompApp').controller('CatalogCtrl', CatalogCtrl);
})();

function getParameterByName(name, url) {
	if (!url)
		url = window.location.href;
	name = name.replace(/[\[\]]/g, "\\$&");
	var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"), results = regex
			.exec(url);
	if (!results)
		return '';
	if (!results[2])
		return '';
	return results[2].replace(/\+/g, " ");
}
