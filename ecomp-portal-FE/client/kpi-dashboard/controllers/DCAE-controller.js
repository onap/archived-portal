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
//app.controller('DCAE_Ctrl_KPI',
(function() {
	var DCAE_Ctrl_KPI = function($scope, $http, $log, $uibModal, $modal, KpiDashboardService) {
					$scope.Math = window.Math;
					$scope.activeToplevelTabId = 'DCAE';
					$scope.activeTabId = 'KPI';
					var TabIdforState = 'DCAE';
					$scope.toplevelgTabs1 = KpiDashboardService.getToplevelgTabs1();
					$scope.toplevelgTabs2 = KpiDashboardService.getToplevelgTabs2();
					$scope.toplevelgTabs3 = KpiDashboardService.getToplevelgTabs3();
					$scope.gTabs = KpiDashboardService.getGenericTabs(TabIdforState);

					$scope.progressReadinessMap = {
						'C' : 'Complete',
						'Y' : 'Yellow',
						'R' : 'Red',
						'G' : 'Green',
						'n' : 'N/A'
					}

					$scope.progressReadinessColorMap = {
						'C' : 'black',
						'Y' : '#FFCC00',
						'R' : 'red',
						'G' : '#00FF00',
						'n' : 'black'
					}

					$scope.selectedIST = null;
					$scope.progressReadinessList = [];

					$scope.progressReadinessListIndexMap = {};
					var counter = 0;
					for ( var key in $scope.progressReadinessMap) {
						if ($scope.progressReadinessMap.hasOwnProperty(key)) {
							$scope.progressReadinessList.push({
								'index' : counter,
								'value' : key,
								'title' : $scope.progressReadinessMap[key]
							})
							$scope.progressReadinessListIndexMap[key] = counter
						}
						counter = counter + 1;
					}
					$scope.selectedProgressReadiness1 = $scope.progressReadinessList[0];

					// restAPI calls to retrieve all user stories
					KpiDashboardService.getKpiUserStoriesStats().then(
							function(response) {
								$scope.userStories = response.data;
								$scope.releases = [];

								if ($scope.userStories) {
									$scope.userStories.forEach(function(item,
											index) {
										$scope.releases.push({
											'index' : index,
											'value' : item.releaseKey,
											'title' : item.releaseKey
										});
									});
									$scope.selectedRelease = $scope.releases[0];
								}
							});

							KpiDashboardService.getKpiLocStats()
							.then(
									function(response) {
										$scope.locStats = response.data;
										var arr = $scope.locStats;
										$scope.totalLOC = 0
										$.each(arr, function() {
											$scope.totalLOC += this;
										});
										var sorted = arr.slice().sort(
												function(a, b) {
													return b - a
												})
										$scope.LOCranks = arr.slice().map(
												function(v) {
													return sorted.indexOf(v)
												});
										$scope.sortedLOC = []
										KpiDashboardService.getKpiLocStatsCat()
												.then(
														function(response) {
															$scope.locStatsCat = response.data;
															$scope.locStatsCatRanked = [];
															var OtherPutLast = false
															for (i = 0; i < $scope.LOCranks.length; i++) {
																$scope.locStatsCatRanked[$scope.LOCranks[i]] = $scope.locStatsCat[i];
																$scope.sortedLOC[$scope.LOCranks[i]] = $scope.locStats[i];
															}

															$scope.sortedLocArray = [];
															var OthersLoC = null;

															for (i = 0; i < $scope.sortedLOC.length; i++) {
																if ($scope.locStatsCatRanked[i]
																		.toLowerCase() !== "others") {
																	$scope.sortedLocArray
																			.push({
																				'category' : $scope.locStatsCatRanked[i],
																				'LOC' : $scope.sortedLOC[i]
																			});
																} else {
																	OthersLoC = $scope.sortedLOC[i]
																}
															}
															$scope.sortedLocArray
																	.push({
																		'category' : 'Others',
																		'LOC' : OthersLoC
																	})
														});
									});

					KpiDashboardService.getKpiServiceSupported()
					.then(
							function(response) {
								$scope.kpiServiceSupported = response.data;
							});

					KpiDashboardService.getKpiPublishedDelivered()
							.then(function(response) {
								$scope.kpiTrafficStatsArray = response.data;
								$scope.kpiTrafficStats = [];
								$scope.kpiTrafficStats.push({
									'title' : 'Published',
									'count' : $scope.kpiTrafficStatsArray[0]
								});
								$scope.kpiTrafficStats.push({
									'title' : 'Delivered',
									'count' : $scope.kpiTrafficStatsArray[1]
								});
							});

					KpiDashboardService.getKpiFeedStats()
					.then(
							function(response) {
								$scope.kpiFeedStatsArray = response.data;
								$scope.kpiFeedStats = [];
								$scope.kpiFeedStats.push({
									'title' : 'Total Feeds',
									'count' : $scope.kpiFeedStatsArray[0]
								});
								$scope.kpiFeedStats.push({
									'title' : 'Active Feeds',
									'count' : $scope.kpiFeedStatsArray[1]
								});
								$scope.kpiFeedStats.push({
									'title' : 'Active Subs',
									'count' : $scope.kpiFeedStatsArray[2]
								});
							});

					KpiDashboardService.getKpiUserApiStats()
							.then(
									function(response) {
										$scope.userApis = response.data;
										$scope.userApiTypes = [];
										if ($scope.userApis) {
											$scope.userApis.forEach(function(
													item, index) {
												$scope.userApiTypes.push({
													'index' : index,
													'value' : item.apiType,
													'title' : item.apiType
												});
											});
											$scope.selectedUserApiType = $scope.userApiTypes[0]
										}
									});

					$scope.linesOfCode = {
						"TotalLOC" : "2.068M",
						"OpenSource" : "63%",
						"Organic" : "36%",
						"Generated" : "<1%"
					};

					$scope.updateUserStory = function(releases, userStories,
							progressReadinessList,
							progressReadinessListIndexMap) {
						var modalInstance = $uibModal
								.open({
									animation : $scope.animationsEnabled,
									templateUrl : 'kpi-dashboard/app/fusion/scripts/view-models/kpidash-page/userstory-edit.html',
									size : 'lg',
									controller : [
											'$scope',
											'$uibModalInstance',
											'$http',
											function($scope, $uibModalInstance,
													$http) {

												$scope.AngChangeCalled = function() {
													console
															.log('change called');
													console
															.log($scope.selectedRelease.releaseKey);
													$scope.selectedIST = $scope.progressReadinessList[progressReadinessListIndexMap[$scope.userStories[$scope.selectedRelease.index].istprogressReadiness]];
													$scope.selectedETE = $scope.progressReadinessList[progressReadinessListIndexMap[$scope.userStories[$scope.selectedRelease.index].e2EProgressReadiness]];
													console
															.log("$scope.selectedIST");
													console
															.log($scope.selectedIST);
													console
															.log("$scope.selectedETE");
													console
															.log($scope.selectedETE);
												};
												$scope.releases = releases;
												$scope.userStories = userStories;
												$scope.progressReadinessList = progressReadinessList;
												$scope.progressReadinessListIndexMap = progressReadinessListIndexMap;
												$scope.ok = function() {
													$scope.userStories[$scope.selectedRelease.index].istprogressReadiness = $scope.selectedIST.value;
													$scope.userStories[$scope.selectedRelease.index].e2EProgressReadiness = $scope.selectedETE.value;
													$http
															.post(
																	'portalApi/update_user_stories_stats',
																	$scope.userStories[$scope.selectedRelease.index])
															.then(
																	function(
																			response) {
																		$scope.userStories = response.data;
																		$scope.releases = [];
																		if ($scope.userStories) {
																			$scope.userStories
																					.forEach(function(
																							item,
																							index) {
																						$scope.releases
																								.push({
																									'index' : index,
																									'value' : item.releaseKey,
																									'title' : item.releaseKey
																								});
																					});
																		}
																		$uibModalInstance
																				.close();
																	});
												};

												$scope.cancel = function() {
													$uibModalInstance.dismiss();
												};
											} ],
									// End of inner controller
									resolve : {}
								});
					};

					$scope.updateUserApi = function(userApiTypes, userApis) {
						// workflowToEdit.active='true';
						var modalInstance = $uibModal
								.open({
									animation : $scope.animationsEnabled,
									templateUrl : 'kpi-dashboard/app/fusion/scripts/view-models/kpidash-page/userapi-edit.html',
									size : 'lg',
									controller : [
											'$scope',
											'$uibModalInstance',
											'$http',
											function($scope, $uibModalInstance,
													$http) {
												$scope.userApiTypes = userApiTypes;
												$scope.userApis = userApis;
												$scope.ok = function() {
													/*
													 * var temp =
													 * {"id":null,"created":null,"modified":null,"createdId":null,"modifiedId":null,"rowNum":null,"auditUserId":null,"auditTrail":null,"apiType":"Physical
													 * APIs","totalApi":2000,"comment":null};
													 */
													var temp = $scope.userApis[$scope.selectedUserApiType.apiType.index]
													$http
															.post(
																	'portalApi/update_user_api_stats',
																	temp)
															.then(
																	function(
																			response) {
																		$uibModalInstance
																				.close();
																		/*
																		 * $http.post('update_user_stories_stats',$scope.userStories[$scope.selectedRelease.releaseKey.index])
																		 * .then(function(response){
																		 * $scope.userStories =
																		 * response.data;
																		 * $scope.releases
																		 * =[];
																		 * if($scope.userStories){
																		 * $scope.userStories.forEach(function(item,
																		 * index){
																		 * $scope.releases.push({'index':
																		 * index,
																		 * 'value':
																		 * item.releaseKey,
																		 * 'title':item.releaseKey});
																		 * });
																		 * $uibModalInstance.close();
																		 *  }
																		 */
																	});
												};

												$scope.cancel = function() {
													$uibModalInstance.dismiss();
												};
											} ],
									// End of inner controller
									resolve : {}
								});
					};
	};

	DCAE_Ctrl_KPI.$inject = ['$scope','$http', '$log', '$uibModal', '$modal', 'KpiDashboardService'];
	angular.module('ecompApp').controller('DCAE_Ctrl_KPI',DCAE_Ctrl_KPI);

}());
				// });

app.controller('DCAE_Ctrl_UserDefinedKPI', function($scope, $http, $log, KpiDashboardService) {
	$scope.activeToplevelTabId = 'DCAE';
	$scope.activeTabId = 'User Defined KPI';
	var TabIdforState = 'DCAE';
	$scope.toplevelgTabs1 = KpiDashboardService.getToplevelgTabs1();
	$scope.toplevelgTabs2 = KpiDashboardService.getToplevelgTabs2();
	$scope.toplevelgTabs3 = KpiDashboardService.getToplevelgTabs3();
	$scope.gTabs = KpiDashboardService.getGenericTabs(TabIdforState);

});

app.controller('DCAE_Ctrl_Metrics', function($scope, $http, $log, $uibModal,
		$modal, KpiDashboardService) {
	$scope.activeToplevelTabId = 'DCAE';
	$scope.activeTabId = 'Metrics';
	var TabIdforState = 'DCAE';
	$scope.toplevelgTabs1 = KpiDashboardService.getToplevelgTabs1();
	$scope.toplevelgTabs2 = KpiDashboardService.getToplevelgTabs2();
	$scope.toplevelgTabs3 = KpiDashboardService.getToplevelgTabs3();
	$scope.gTabs = KpiDashboardService.getGenericTabs(TabIdforState);

	KpiDashboardService.getKpiGeoMapUrl()
	.then(function(response) {
		$scope.geoMapUrl = response.data;
	});

	KpiDashboardService.getKpiRCloudAUrl()
	.then(function(response) {
		$scope.rcloudAUrl = response.data;
	});

});
