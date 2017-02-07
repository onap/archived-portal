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

	var DashboardCtrl = function DashboardCtrl(applicationsService, $log,
			$window, userProfileService, $scope, $cookies, $timeout, $interval,
			$modal, $state, dashboardService,ngDialog) {

		var _this = this;

		_classCallCheck(this, DashboardCtrl);

		// activate spinner
		this.isLoading = true;
		$scope.getUserAppsIsDone = false;
		this.userProfileService = userProfileService;
		$scope.demoNum = 1;
		$scope.event_content_show = false;
		$scope.widgetData = [];

		$scope.editWidgetModalPopup = function(availableData, resourceType) {
			$scope.editData = JSON.stringify(availableData);
			$scope.availableDataTemp = $scope.availableData;
			ngDialog.open({
					templateUrl : 'app/views/dashboard/dashboard-widget-manage.html',
					controller : 'CommonWidgetController',
					resolve : {
						message : function message() {
							var message = {
								type : resourceType,
								availableData : $scope.editData
							};
							return message;
						}
					}
				}).closePromise.then(needUpdate => {	
					if(resourceType=='NEWS'){
						$scope.updateNews();
					}else if(resourceType=='EVENTS'){
						$scope.updateEvents();
					}else if(resourceType=='IMPORTANTRESOURCES'){
						$scope.updateImportRes();
					}
	          });		
		};

		userProfileService.getUserProfile()
			.then(
				function(profile) {
					// $log.info(profile);
					$scope.attuid = profile.attuid;
					// $log.info('user has the following attuid: ' +
					// profile.attuid);
					$scope.appsView = [];
					
					applicationsService.getPersUserApps()
						.then(
							function(res) {
							// $log.info(res);
							_this.apps = res;
							
							for (var i = 0; i < _this.apps.length; i++) {
								$scope.appsView[i] = {
									sizeX 			: 2,
									sizeY 			: 2,
									headerText 		: _this.apps[i].name,
									subHeaderText 	: _this.apps[i].notes,
									imageLink 		: _this.apps[i].thumbnail || _this.apps[i].imageUrl,
									order 			: _this.apps[i].order,
									restrictedApp	: _this.apps[i].restrictedApp,
									url 			: _this.apps[i].url
								};
							}
							
							// Append the show add/remove applications tile
							$scope.appsView[_this.apps.length] = {
									addRemoveApps : true,
									sizeX : 2,
									sizeY : 2,
									headerText : 'Add/Remove Applications',
									subHeaderText : '',
									imageLink : 'assets/images/cloud.png',
									order : '',
									restrictedApp : false,
									url : ''
							};
		
							// $log.info('getUserApps apps count : ' +
							// $scope.appsView.length);
							// Show 2 rows in the gridster if needed
							if ($scope.appsView.length > 6) {													
								$(".dashboard-boarder").css({
				    				"height" : "400px"
									});
								}else{
									$(".dashboard-boarder").css({
					    				"height" : "210px"
					    			});
								}
								_this.isLoading = false;
								$scope.getUserAppsIsDone = true;																									
							})['catch']
					(function(err) {
						$log.error('DashboardCtlr: failed to get applications list', err);
						_this.isLoading = false;
						$scope.getUserAppsIsDone = true;
					});
		});


		/* Widget Gridster Section */
		$scope.newsGridsterItem = {
			sizeX : 4,
			sizeY : 4,
			headerText : 'News',
			subHeaderText : '',
			imageLink : '',
			order : '',
			url : ''
		};

		$scope.eventsGridsterItem = {
			sizeX : 4,
			sizeY : 4,
			headerText : 'Calendar Events',
			subHeaderText : '',
			imageLink : '',
			order : '',
			url : ''
		};

		$scope.impoResGridsterItem = {
			sizeX : 4,
			sizeY : 4,
			headerText : 'Resources',
			subHeaderText : '',
			imageLink : '',
			order : '',
			url : ''
		};
		this.gridsterOpts = {
			columns : 12,
			colWidth : 95,
			rowHeight : 95,
			margins : [ 20, 20 ],
			outerMargin : true,
			pushing : true,
			floating : true,
			swapping : true,
			draggable : {
				handle: 'img'
			}
		};
		
		this.emptyGridsterOpts = {
				columns : 24,
				colWidth : 190,
				rowHeight : 190,
				margins : [ 20, 20 ],
				outerMargin : true,
				pushing : true,
				floating : true,
				swapping : true,
				draggable : {
					handle: 'img'
				}
			};

		this.goToCatalog = function(item) {
			$state.go('root.appCatalog');
		} 
		
		// navigate to application url in new tab
		this.goToPortal = function(item) {
			if (!item.url) {
				$log.error('No URL found for this application, doing nothing!');
				return;
			}
			if (item.restrictedApp) {
				// Link-based apps open in their own browser tab
				$window.open(item.url, '_blank');
			} else {
				// cache control so browsers load app page every time
				var ccParam = '?cc=' + new Date().getTime();
				var appUrl = null;
				var urlParts = item.url.split('#');
				if (urlParts.length < 2)
					appUrl = item.url + ccParam;
				else
					appUrl = urlParts[0] + ccParam + "#" + urlParts[1];
				// $log.debug('DashboardCtrlr::goToPortal: opening tab with URL
				// ' + appUrl);
				var tabContent = {
					id : new Date(),
					title : item.headerText,
					url : appUrl
				};
				$cookies.putObject('addTab', tabContent);
			}
		};
		
		/* News data */
		$scope.newsData = [];
		$scope.updateNews = function() {
			$scope.newsData.length=0;
			dashboardService.getCommonWidgetData('NEWS').then(function(res) {
				// $log.info(res.message);
				var newsDataJsonArray = res.response.items;
				for (var i = 0; i < newsDataJsonArray.length; i++) {
					$scope.newsData.push(newsDataJsonArray[i]);
				}
			})['catch'](function(err) {
				$log.error('dashboard controller: failed to get news list', err);
				_this.isLoading = false;
			});
		}
		$scope.updateNews();
		
		/* Events data */
		$scope.eventData = [];
		$scope.updateEvents = function() {
			$scope.eventData.length=0;
			dashboardService.getCommonWidgetData('EVENTS').then(function(res) {
				var eventDataJsonArray = res.response.items;	
				for (var i = 0; i < eventDataJsonArray.length; i++) {
					if(eventDataJsonArray[i].eventDate !=null) {
						// yyyy-mm-dd
						eventDataJsonArray[i].year = eventDataJsonArray[i].eventDate.substring(2,4);
						eventDataJsonArray[i].mon  = eventDataJsonArray[i].eventDate.substring(5,7);
						eventDataJsonArray[i].day  = eventDataJsonArray[i].eventDate.substring(8,10);
					}
					$scope.eventData.push(eventDataJsonArray[i]);
				}
			})['catch'](function(err) {
				$log.error('dashboard controller: failed to get Events list', err);
				_this.isLoading = false;
			});
		}
		$scope.updateEvents();
		
		/* Important Resources data */
		$scope.importResData = [];
		$scope.updateImportRes = function() {
			$scope.importResData.length=0;
			dashboardService.getCommonWidgetData('IMPORTANTRESOURCES').then(
					function(res) {
						// $log.error('couldnt get important list...',
						// res.response.dataBlob);
						// $log.info(res);
						var importResDataJSONArray = res.response.items;
						for (var i = 0; i < importResDataJSONArray.length; i++) {
							$scope.importResData.push(importResDataJSONArray[i]);
						}
					})['catch'](function(err) {
				$log.error('dashboard controller: failed to get resources list...', err);
				_this.isLoading = false;
			});
		}
		$scope.updateImportRes();

		if (getParameterByName('noUserError') != null) {
			if (getParameterByName('noUserError') == "Show") {
				$("#errorInfo").show();
			}
		}
		
	};

	DashboardCtrl.$inject = [ 'applicationsService', '$log', '$window',
			'userProfileService', '$scope', '$cookies', '$timeout',
			'$interval', '$modal',  '$state', 'dashboardService','ngDialog'];
	angular.module('ecompApp').controller('DashboardCtrl', DashboardCtrl);
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
