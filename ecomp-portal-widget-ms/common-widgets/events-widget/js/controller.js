function EventsCtrl($rootScope, applicationsService , $log,
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
		/*Setting News data*/
		$scope.eventData = [];
		$scope.updateEvents = function() {

			$scope.eventData.length=0;
			//dashboardService.getCommonWidgetData('EVENTS').then(function(res) {
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
			//})['catch'](function(err) {
			//	$log.error('dashboard controller: failed to get Events list', err);
			//	_this.isLoading = false;
			//});
		}
		$scope.updateEvents();


	}
