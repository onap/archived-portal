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

function _classCallCheck(instance, Constructor) {
	if (!(instance instanceof Constructor)) {
		throw new TypeError('Cannot call a class as a function');
	}
}

(function() {
	var HTTP_PROTOCOL_RGX = /https?:\/\//;

	var CatalogCtrl = function CatalogCtrl(catalogService, confirmBoxService, 
			$log, $window, userProfileService, $scope, $timeout, $interval,
			$modal, ngDialog) {

		var _this = this;

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
		$scope.demoNum = 1;

		this.getAccess = function(item) {
			if(!item.access)
				confirmBoxService.showDynamicInformation(item,
					'app/views/catalog/information-box.tpl.html','CatalogConfirmationBoxCtrl'
				).then(isConfirmed => {});
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
							$scope.attuid = profile.attuid;
							$scope.appCatalog = [];

							// applicationsService.getUserApps()
							catalogService
									.getAppCatalog()
									.then(
											function(res) {
												// $log.info(res);
												_this.apps = res;
												var rowNo = 0;
												for (var i = 0; i < _this.apps.length; i++) {
													$scope.appCatalog[i] = {
														sizeX : 2,
														sizeY : 2,
														id : _this.apps[i].id,
														headerText : _this.apps[i].name,
														imageLink : _this.apps[i].thumbnail
																|| _this.apps[i].imageUrl,
														url : _this.apps[i].url,
														restricted : _this.apps[i].restricted,  
														select : (_this.apps[i].select || (_this.apps[i].access && _this.apps[i].pending)), 
														access : _this.apps[i].access,
														pending: _this.apps[i].pending
													};
												}
												//$log.debug('CatalogCtrl: getAppCatalog count : '
												//				+ $scope.appCatalog.length);
												_this.isLoading = false;
												$scope.getAppCatalogIsDone = true;
											})['catch'](function(err) {
								$log.error('CatalogCtrl: failed to get app list: ', err);
								_this.isLoading = false;
								$scope.getAppCatalogIsDone = true;
							});
						});

		this.gridsterOpts = {
			columns : 12,
			colWidth : 95,
			rowHeight : 95,
			margins : [ 20, 20 ],
			outerMargin : true,
			pushing : true,
			floating : true,
			swapping : true,
		};

		if (getParameterByName('noUserError') != null) {
			if (getParameterByName('noUserError') == "Show") {
				$("#errorInfo").show();
			}

		}
	};

	CatalogCtrl.$inject = [ 'catalogService', 'confirmBoxService', '$log',
			'$window', 'userProfileService', '$scope', '$timeout', '$interval',
			'$modal', 'ngDialog' ];
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