function NewsCtrl($rootScope, applicationsService , $log,
			$window, userProfileService, $scope, $cookies, $timeout, $interval,
			$uibModal, dashboardService, ngDialog) {
		
		var _this = this;	

		//activate spinner
		this.isLoading = true;
		$scope.getUserAppsIsDone = false;
		this.userProfileService = userProfileService;
		$scope.demoNum = 1;
		$scope.event_content_show = false;
		$scope.widgetData = [];

		$scope.editWidgetModalPopup = function(availableData, resourceType) {

			$scope.editData = JSON.stringify(availableData);
			$scope.availableDataTemp = $scope.availableData;
			
		};
		
		/*Setting News data*/
		$scope.newsData = [];
		$scope.updateNews = function() {
			$scope.newsData.length=0;
			//dashboardService.getCommonWidgetData('NEWS').then(function(res) {
				// $log.info(res.message);
				var newsDataJsonArray = res.response.items;
				for (var i = 0; i < newsDataJsonArray.length; i++) {
					$scope.newsData.push(newsDataJsonArray[i]);
				}
			//})['catch'](function(err) {
			//	$log.error('dashboard controller: failed to get news list', err);
			//	_this.isLoading = false;
			//});
		}
		$scope.updateNews();

	}

