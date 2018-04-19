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
 * 
 */
'use strict';
(function () {

    class WidgetCatalogCtrl {
        constructor(widgetsService, $log, $cookies, $scope, conf, beReaderService, widgetsCatalogService, userProfileService,dashboardService,$compile, ngDialog) {
            
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
    		
        	$scope.WidgetCatView = [];  
        	$scope.applyPresentationDetailsToWidgetsCatalog = function(widgetsReturned){
        		var rowNo = 0;
        		for (var i = 0; i < widgetsReturned.length; i++) {
        		$scope.WidgetCatView[i] = {
        		sizeX : 2,
        		sizeY : 2,
        		headerText:'',
        		widgetIdentifier : '',
        		url : '',
        		widgetid: '',
        		attrb:'',
        		select: false,
        		};
        		$scope.WidgetCatView[i].widgetid = widgetsReturned[i].id;
        		$scope.WidgetCatView[i].headerText = widgetsReturned[i].headerName;

        		if(widgetsReturned[i].headerName === 'widget-news'){
        		$scope.WidgetCatView[i].widgetIdentifier = 'NEWS';
        		}
        		else
        		if(widgetsReturned[i].headerName === 'widget-resources'){
        		$scope.WidgetCatView[i].widgetIdentifier = 'IMPORTANTRESOURCES';
        		}
        		else
        		if(widgetsReturned[i].headerName === 'widget-events'){
        		$scope.WidgetCatView[i].widgetIdentifier = 'EVENTS';
        		}

        		$scope.WidgetCatView[i].url = widgetsReturned[i].url;
        		$scope.WidgetCatView[i].attrb = widgetsReturned[i].attrs;
        		$scope.WidgetCatView[i].select = widgetsReturned[i].select;        		
        		}

        		$scope.widgetViewData = $scope.WidgetCatView;
        		
        		}
        	
        	/** Widget code starts */
            let getUserWidgets = (loginName) => {
            	 
            	 this.isCommError = false;
            	 var conf = this.conf;
        		 widgetsCatalogService.getUserWidgets(loginName).then(res => {
        			 
        			 if(!(res instanceof Array)){
        				this.isCommError = true;
        				return;
        			 }
            		 for(var i = 0; i < res.length; i++){	
            			 var widget_id = res[i][0];
            			 var widget_name = res[i][1];
            			 let url = this.conf.api.widgetCommon + "/" + widget_id + "/framework.js";
            			 $scope.widgetsList.push({
			    			   id: widget_id,
			                   name: widget_name,
			                   headerName: widget_name,
			                   url: url,
			                   attrs: [{attr: 'data-' + widget_id, value: ''}],
			                   status: res[i][4],
			                   select: (res[i][4] == 'S' || res[i][4] === null) ? true : false
			    		 }); 
            			 var script = document
         				 .createElement('script');
         				 script.src = url;
         				 script.async = true;
         				 var entry = document
         				 	.getElementsByTagName('script')[0];
         				 entry.parentNode
         				 	.insertBefore(script, entry);
            		 }
            		 $scope.applyPresentationDetailsToWidgetsCatalog($scope.widgetsList);
                 }).catch(err => {
                     $log.error('WidgetCatalogCtrl::getUserWidgets caught error', err);
                 }).finally(()=> {
                	 
                 });
            };
            
            let init = () => {
            	userProfileService.getUserProfile()
                .then(profile=> {
                    // $log.info('WidgetCatalogCtrl::getUserProfile: ',
					// profile);
                    $scope.orgUserId = profile.orgUserId;
                    $scope.widgetsViewData = [];
                    $scope.widgetsView = [];    
                	getUserWidgets($scope.orgUserId);  
                });
                this.conf = conf;
                $scope.widgetsList = [];
            };
            
            /** Widget code ends */
          
            $scope.activateThis = function(ele){
   			 	$compile(ele.contents())($scope);
   			 	$scope.$apply();
            };
            $scope.setCommonWidget = function() {
            	/* News Events Resources */
                var widgetLength = ($scope.widgetsViewData==null || $scope.widgetsViewData.length==0) ? 0:$scope.widgetsViewData.length;
                $scope.widgetsViewData[widgetLength] = {
                        sizeX: 2,
                        sizeY: 2,
                        headerText: 'News',
                        width: '',
                        height: '',
                        url: '',
                        selected:true
                    };
                $scope.widgetsViewData[widgetLength+1] = {
                        sizeX: 2,
                        sizeY: 2,
                        headerText: 'Calendar Events',
                        width: '',
                        height: '',
                        url: '',
                        selected:true
                    };
                $scope.widgetsViewData[widgetLength+2] = {
                        sizeX: 2,
                        sizeY: 2,
                        headerText: 'Resources',
                        width: '',
                        height: '',
                        url: '',
                        selected:true
                    };
                
                /* Setting News data */
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
        			});
        		}
        		$scope.updateNews();
        		/* Setting Events data */

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
        			});
        		}
        		$scope.updateEvents();
        		/* Setting Important Resources data */

        		$scope.importResData = [];
        		$scope.updateImportRes = function() {
        			$scope.importResData.length=0;
        			dashboardService.getCommonWidgetData('IMPORTANTRESOURCES').then(
        					function(res) {
        						// $log.info(res);
        						var importResDataJSONArray = res.response.items;
        						for (var i = 0; i < importResDataJSONArray.length; i++) {
        							$scope.importResData.push(importResDataJSONArray[i]);
        						}
        					})['catch'](function(err) {
        				$log.error('dashboard controller: failed to get resources list...', err);
        			});
        		}
        		$scope.updateImportRes();
                
                /** ******End hardcoded news events and resources*************** */
        		
            }
            
        	$scope.newsGridsterItem = {
        			headerText : 'Test',
        			subHeaderText : ''
        	};
        	
        	$scope.newsGridsterItem = {
    			headerText : 'News',
    			subHeaderText : ''
    		};
            
            $scope.eventsGridsterItem = {
            	headerText : 'Events',
        		subHeaderText : ''
        	};

        	$scope.impoResGridsterItem = {
        		headerText : 'Resources',
        		subHeaderText : ''
        	};
        	
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
                	handle: '.icon-content-gridguide'
                }
            };
            
            // Run this function when user clicks on checkbox.
    		$scope.storeSelection = function(widget) {
    			
    			// not needed as only 'SHOW' and 'HIDE' status_cd is expected from the micro service now
    			/*var pendingFlag = false;    			
    			if(widget.access) 
    				pendingFlag = false;
    			else
    				pendingFlag =  widget.pending;	*/	
    			
    			var appData = { 
    					widgetId: widget.widgetid,
    					select  : widget.select,
    				//	pending : pendingFlag	
    			};

    			widgetsCatalogService.updateWidgetCatalog(appData).then(
    				function(result) {
    					// $log.debug('CatalogCtrl:storeSelection result is ', result);
    				})['catch'](function(err) {
    					$log.error('CatalogCtrl:storeSelection: exception: ', err);
    				});
    		};

            init();
        }
    }
    
    
    
    WidgetCatalogCtrl.$inject = ['widgetsService', '$log', '$cookies', '$scope', 'conf', 'beReaderService', 'widgetsCatalogService', 'userProfileService','dashboardService','$compile','ngDialog'];
    angular.module('ecompApp').controller('WidgetCatalogCtrl', WidgetCatalogCtrl);

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
            /*
			 * timeoutId = $interval(function () { updateIframe(); // update DOM },
			 * refreshInterval);
			 */
        }

        return {
            link: link
        };
    } ]);

})();


app.directive('dynAttr', function() {
    return {
        scope: { list: '=dynAttr' },
        link: function(scope, elem, attrs){
            for(var attr in scope.list){
                elem.attr(scope.list[attr].attr, scope.list[attr].value);   
            }
        }
    };
});	 
