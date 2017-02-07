'use strict';
(function () {

    class WidgetsHomeCtrl {
        constructor(widgetsService, $log, $cookies, $scope, userProfileService,dashboardService) {
            this.userProfileService = userProfileService;
            this.isLoading = true;

            userProfileService.getUserProfile()
                .then(profile=> {
                    $log.info('WidgetsHomeCtrl::getUserProfile: ', profile);
                    $scope.attuid = profile.attuid;
                    $log.info('WidgetsHomeCtrl::getUserProfile: user has the following attuid: ' + profile.attuid);
                    $scope.widgetsViewData = [];
                    $scope.widgetsView = [];

                    widgetsService.getUserWidgets()
                        .then(widgets => {
                            $log.info('WidgetsHomeCtrl::getUserWidgets', widgets);
                            this.widgets = widgets;
                            this.enlargeURL = "";
                            this.isEnlarged = false;
                            let rowNo = 0;
                            for (let i = 0; i < this.widgets.length; i++) {
                                $scope.widgetsView[i] = {
                                    sizeX: 2,
                                    sizeY: 2,
                                    headerText: '',
                                    width: '',
                                    height: '',
                                    url: '',
                                    selected:false
                                };
                                $scope.widgetsView[i].headerText = this.widgets[i].name;
                                $scope.widgetsView[i].url = this.widgets[i].url;
                                //$scope.widgetsView[i].width = this.widgets[i].width;
                                //$scope.widgetsView[i].height = this.widgets[i].height;
                                //$scope.widgetsView[i].sizeX = this.widgets[i].width/180;
                                //$scope.widgetsView[i].sizeY = this.widgets[i].height/150;
                            }
                            /*News Events Resources*/
                            $scope.widgetsView[this.widgets.length] = {
                                    sizeX: 2,
                                    sizeY: 2,
                                    headerText: 'News',
                                    width: '',
                                    height: '',
                                    url: '',
                                    selected:true
                                };
                            $scope.widgetsView[this.widgets.length+1] = {
                                    sizeX: 2,
                                    sizeY: 2,
                                    headerText: 'Events',
                                    width: '',
                                    height: '',
                                    url: '',
                                    selected:true
                                };
                            $scope.widgetsView[this.widgets.length+2] = {
                                    sizeX: 2,
                                    sizeY: 2,
                                    headerText: 'Resources',
                                    width: '',
                                    height: '',
                                    url: '',
                                    selected:true
                                };
                            
                            /*Setting News data*/
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
                    		/*Setting Events data*/

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
                    		/*Setting Important Resources data*/

                    		$scope.importResData = [];
                    		$scope.updateImportRes = function() {
                    			$scope.importResData.length=0;
                    			dashboardService.getCommonWidgetData('IMPORTANTRESOURCES').then(
                    					function(res) {
                    						//	 $log.error('couldnt get important list...', res.response.dataBlob);
                    						$log.info(res);
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
                           
                            /********End hardcoded news events and resources****************/
                            
                            
                            $log.info('WidgetsHomeCtrl::getUserWidgets count : ' + $scope.widgetsView.length);

                            if ($cookies.getObject($scope.attuid + '_widget') == undefined || $cookies.getObject($scope.attuid + '_widget') == null || $cookies.getObject($scope.attuid + '_widget').length == 0) {
                                if (($scope.widgetsView != undefined) && ($scope.widgetsView != null) && ($scope.widgetsView.length > 0)) {
                                    $scope.widgetsViewData = $scope.widgetsView;
                                    //$cookies.putObject($scope.attuid + '_widget', $scope.widgetsView);
                                }
                            }
                            else {
                                this.listChanged = false;
                                this.listFromCookie = $cookies.getObject($scope.attuid + '_widget');
                                this.finalList = [];
                                //
                                // If a widget is still valid for this user from backend and
                                // it was in the cookie already, put it in the list in the same order
                                // it was in within the cookie.
                                //
                                let finalCount = 0;
                                for (let i = 0; i < this.listFromCookie.length; i++) {
                                    this.foundInListFromBackend = false;
                                    for (let j = 0; j < $scope.widgetsView.length; j++) {
                                        if ((this.listFromCookie[i].url == $scope.widgetsView[j].url) &&
                                            (this.listFromCookie[i].headerText == $scope.widgetsView[j].headerText)) {
                                            this.finalList[finalCount] = {
                                                sizeX: 2,
                                                sizeY: 2,
                                                headerText: '',
                                                width: '',
                                                height: '',
                                                url: '',
                                                selected:false
                                            };
                                            this.finalList[finalCount].headerText = this.listFromCookie[i].headerText;
                                            //this.finalList[finalCount].width = this.listFromCookie[i].width;
                                            //this.finalList[finalCount].height = this.listFromCookie[i].height;
                                            this.finalList[finalCount].url = this.listFromCookie[i].url;
                                            //this.finalList[finalCount].sizeX = this.listFromCookie[i].width/180;
                                            //this.finalList[finalCount].sizeY = this.listFromCookie[i].height/150;
                                            finalCount++;
                                            this.foundInListFromBackend = true;
                                            break;
                                        }
                                    }
                                    if (this.foundInListFromBackend == false) {
                                        this.listChanged = true;
                                    }
                                }

                                //
                                // Fill in the rest of the list with the widgets retrieved from the backend that we did
                                // not already add.  There could have been
                                // new widgets configured for the user that are not in the cookie.
                                //
                                for (let i = 0; i < $scope.widgetsView.length; i++) {
                                    this.found = false;
                                    for (let j = 0; j < this.finalList.length; j++) {
                                        if (($scope.widgetsView[i].url == this.finalList[j].url) &&
                                            ($scope.widgetsView[i].headerText == this.finalList[j].headerText)){
                                            // already present
                                            this.found = true;
                                            break;
                                        }
                                    }
                                    if (this.found == false) {
                                        this.finalList[finalCount] = {
                                            sizeX: 2,
                                            sizeY: 2,
                                            headerText: '',
                                            width: '',
                                            height: '',
                                            url: '',
                                            selected:false
                                        };
                                        this.finalList[finalCount].headerText = $scope.widgetsView[i].headerText;
                                        //this.finalList[finalCount].width = $scope.widgetsView[i].width;
                                        //this.finalList[finalCount].height = $scope.widgetsView[i].height;
                                        this.finalList[finalCount].url = $scope.widgetsView[i].url;
                                        //this.finalList[finalCount].sizeX = $scope.widgetsView[i].width/180;
                                        //this.finalList[finalCount].sizeY = $scope.widgetsView[i].height/150;
                                        finalCount++;
                                        this.listChanged = true;
                                    }
                                }

                                if ((this.finalList != undefined) && (this.finalList != null) && (this.finalList.length > 0)) {
                                    if (this.listChanged) {
                                        $scope.widgetsViewData = this.finalList;
                                        $cookies.putObject($scope.attuid + '_widget', this.finalList);
                                    } else {
                                        $scope.widgetsViewData = $cookies.getObject($scope.attuid + '_widget');
                                    }
                                }
                                this.isLoading = false;
                            }
                        }).catch(err => {
                        $log.error('WidgetsHomeCtrl::getUserWidgets: oh no! couldnt get widgets list...', err);
                        this.isLoading = false;
                        });
                    });


            this.gridsterOpts = {
                columns: 6,
                colWidth: 190,
                rowHeight: 190,
                margins: [20, 20],
                outerMargin: true,
                pushing: true,
                floating: true,
                swapping: true,
                draggable: {
                	handle: 'img',
                    stop: function () {                   	
                        $cookies.putObject($scope.attuid + '_widget', $scope.widgetsViewData);
                    }
                }
            };
        }
    }

    WidgetsHomeCtrl.$inject = ['widgetsService', '$log', '$cookies', '$scope', 'userProfileService','dashboardService'];
    angular.module('ecompApp').controller('WidgetsHomeCtrl', WidgetsHomeCtrl);

    angular.module('ecompApp').constant('refreshInterval', '30000');

    angular.module('ecompApp').directive('refreshIframe', ['$interval', 'refreshInterval', function ($interval, refreshInterval) {

        function link(scope, element, attrs) {
            var timeoutId;

            function updateIframe() {
                if(attrs.isEnlarged == "false")
                {
                    element.attr('src', element.attr('src'));
                }
            }

            element.on('$destroy', function () {
                $interval.cancel(timeoutId);
            });

            // start the UI update process; save the timeoutId for cancelling
            /*timeoutId = $interval(function () {
                updateIframe(); // update DOM
            }, refreshInterval);*/
        }

        return {
            link: link
        };
    } ]);

})();
