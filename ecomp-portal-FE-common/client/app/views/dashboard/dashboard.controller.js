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

function _classCallCheck(instance, Constructor) {
    if (!(instance instanceof Constructor)) {
        throw new TypeError('Cannot call a class as a function');
    }
}

(function() {
    var HTTP_PROTOCOL_RGX = /https?:\/\//;

    var DashboardCtrl = function DashboardCtrl(conf, applicationsService, $log,
        $window, userProfileService, $scope, $cookies, $timeout, $interval,
        $modal, $state, beReaderService, dashboardService, confirmBoxService,
        auditLogService, ngDialog, $compile, widgetsCatalogService) {

        this.conf = conf;
        var _this = this;

        _classCallCheck(this, DashboardCtrl);

        // activate spinner
        this.isLoading = true;
        this.isCommError = false;
        $scope.getUserAppsIsDone = false;
        this.userProfileService = userProfileService;
        $scope.demoNum = 1;
        $scope.event_content_show = false;
        $scope.widgetData = [];
        $scope.activateThis = function(ele) {
            $compile(ele.contents())($scope);
            $scope.$apply();
        };

        $scope.editWidgetModalPopup = function(availableData, resourceType) {
            $scope.editData = JSON.stringify(availableData);
            $scope.availableDataTemp = $scope.availableData;
            var modalInstance = $modal.open({
                templateUrl: 'app/views/dashboard/dashboard-widget-manage.html',
                controller: 'CommonWidgetController',
                windowClass: 'modal-docked',
                resolve: {
                    message: function message() {
                        var message = {
                            type: resourceType,
                            availableData: $scope.editData
                        };
                        return message;
                    }
                }
            })
            
            modalInstance.result.finally(function (needUpdate){
            	   if (resourceType == 'NEWS') {
                       $scope.updateNews();
                   } else if (resourceType == 'EVENTS') {
                       $scope.updateEvents();
                   } else if (resourceType == 'IMPORTANTRESOURCES') {
                       $scope.updateImportRes();
                   }
 	        });
        };

        $scope.editWidgetParameters = function(widgetId) {
            let data = {
                widgetId: widgetId
            }
            var modalInstance = $modal.open({
                templateUrl: 'app/views/dashboard/dashboard-widget-parameter-manage.html',
                controller: 'WidgetParameterController',
                windowClass: 'modal-docked',

                resolve: {
					items: function () {
    	        	  return data;
			        	}
		        }
            })
            
            modalInstance.result.then(function (needUpdate) {
              });
        };

    

        $scope.sort_options = [{
                index: 0,
                value: 'N',
                title: 'Name'
            },
            {
                index: 1,
                value: 'L',
                title: 'Last used'
            },
            {
                index: 2,
                value: 'F',
                title: 'Most used'
            },
            {
                index: 3,
                value: 'M',
                title: 'Manual'
            }
        ];

        $scope.selectedSortTypeChanged = function(userAppSortTypePref) {
            $scope.appsViewData = [];
            $scope.appsView = [];

            if(userAppSortTypePref == ""){
            	$scope.selectedSortType = $scope.sort_options[0];
            } else {
            	angular.forEach($scope.sort_options, function(sort_type, key){
            		if(sort_type.value == userAppSortTypePref)
            			$scope.selectedSortType = sort_type;
            	});
            }

            applicationsService
                .getAppsOrderBySortPref(userAppSortTypePref)
                .then(function(res) {
                    _this.apps = res;
                    $scope.applyPresentationDetailsToApps(_this.apps);
                })
            applicationsService
                .saveAppsSortTypePreference($scope.selectedSortType)
                .then(function(res) {
                    // Nothing to do
                })

        }
        $scope.$watch('selectedSortType.value', (newVal, oldVal) => {
            for (var i = 0; i < $scope.sort_options.length; i++) {
                if ($scope.sort_options[i].value == newVal) {
                    $scope.selectedSortType = angular.copy($scope.sort_options[i]);;
                }
            }
        });

        $scope.restoreSortSelected = function() {
            confirmBoxService.confirm("Restore the default size and position of all widgets?").then(
                function(confirmed) {
                    var checkConfirm = confirmed;
                    if (checkConfirm === true) {
                        applicationsService
                            .delWidgetsSortPref($scope.widgetsViewData).then(function() {
                                $state.reload();
                            });
                    }
                });
            /*
             * if(confirm('Restore the default size and position of all widgets?') ==
             * true) { applicationsService
             * .delWidgetsSortPref($scope.widgetsViewData).then(function(){
             * $state.reload(); }) }
             */

        }

        $scope.applyPresentationDetailsToApps = function(appsReturned) {
            var rowNo = 0;
            for (var i = 0; i < _this.apps.length; i++) {
                $scope.appsView[i] = {
                    sizeX: 1,
                    sizeY: 1,
                    headerText: '',
                    subHeaderText: '',
                    imageLink: '',
                    order: '',
                    url: '',
                    appid: '',
                };
                $scope.appsView[i].headerText = appsReturned[i].name;
                $scope.appsView[i].subHeaderText = appsReturned[i].notes;
                let imgLnk = '';
                if (appsReturned[i].imageUrl)
                    imgLnk = conf.api.appThumbnail.replace(':appId', appsReturned[i].id);
                // $log.debug('DashboardCtlr::applyPresn: imgLink = ' + imgLnk);
                $scope.appsView[i].imageLink = imgLnk;
                $scope.appsView[i].order = appsReturned[i].order;
                $scope.appsView[i].url = appsReturned[i].url;
                $scope.appsView[i].restrictedApp = appsReturned[i].restrictedApp;
                $scope.appsView[i].appid = appsReturned[i].id;
            }
            $scope.appsView[_this.apps.length] = {
                addRemoveApps: true,
                sizeX: 1,
                sizeY: 1,
                headerText: 'Add/Remove Applications',
                subHeaderText: '',
                imageLink: 'assets/images/cloud.png',
                order: '',
                restrictedApp: false,
                url: '',
            };
            if ($scope.appsView.length > 6) {
                $(".dashboard-boarder").css({
                    "height": "400px"
                });
            } else {
                $(".dashboard-boarder").css({
                    "height": "210px"
                });
            }

            if ($scope.appsView != undefined &&
                $scope.appsView != null &&
                $scope.appsView.length > 0) {
                $scope.appsViewData = $scope.appsView;
            }
        }

        $scope.widgetsView = [];

        $scope.applyPresentationDetailsToWidgets = function(widgetsReturned) {
            var rowNo = 0;
            for (var i = 0; i < widgetsReturned.length; i++) {
                $scope.widgetsView[i] = {
                    sizeX: '',
                    sizeY: '',
                    headerText: '',
                    widgetText: '',
                    widgetIdentifier: '',
                    url: '',
                    widgetid: '',
                    attrb: '',
                    row: '',
                    col: '',
                };
                $scope.widgetsView[i].widgetid = widgetsReturned[i].id;
                $scope.widgetsView[i].headerText = widgetsReturned[i].headerName;
                $scope.widgetsView[i].widgetText = widgetsReturned[i].name;

                if (widgetsReturned[i].headerName.toLowerCase() === 'news') {
                    $scope.widgetsView[i].widgetIdentifier = 'NEWS';
                } else
                if (widgetsReturned[i].headerName.toLowerCase() === 'resources') {
                    $scope.widgetsView[i].widgetIdentifier = 'IMPORTANTRESOURCES';
                } else
                if (widgetsReturned[i].headerName.toLowerCase() === 'events') {
                    $scope.widgetsView[i].widgetIdentifier = 'EVENTS';
                }

                $scope.widgetsView[i].url = widgetsReturned[i].url;
                $scope.widgetsView[i].attrb = widgetsReturned[i].attrs;
                if (widgetsReturned[i].width === null) {
                    $scope.widgetsView[i].sizeX = 2;
                } else {
                    $scope.widgetsView[i].sizeX = widgetsReturned[i].width;
                }
                if (widgetsReturned[i].height === null) {
                    $scope.widgetsView[i].sizeY = 2;
                } else {
                    $scope.widgetsView[i].sizeY = widgetsReturned[i].height;
                }
                $scope.widgetsView[i].row = widgetsReturned[i].x;
                $scope.widgetsView[i].col = widgetsReturned[i].y;
            }
            if ($scope.widgetsView != undefined &&
                $scope.widgetsView != null &&
                $scope.widgetsView.length > 0) {
                $scope.widgetsViewData = $scope.widgetsView;
            }
        }

        applicationsService
            .getUserAppsSortTypePreference().then(function(res) {
                var resJson = {};
                resJson.value = res;
                if (resJson.value === "N" || resJson.value === "") {
                    resJson.index = 0;
                    resJson.title = 'Name';
                } else if (resJson.value === "L") {
                    resJson.index = 1;
                    resJson.title = 'Last used';
                } else if (resJson.value === "F") {
                    resJson.index = 2;
                    resJson.title = 'Most used';
                } else {
                    resJson.index = 3;
                    resJson.title = 'Manual';
                }
                $scope.selectedSortType = resJson;
                $scope.selectedSortTypeChanged(res);


            });

        $scope.widgetsList = [];

        let getUserWidgets = (loginName) => {
            var conf = this.conf;
            widgetsCatalogService.getUserWidgets(loginName).then(res => {
                if (!(res instanceof Array)) {
                    this.isCommError = true;
                    return;
                }
                for (var i = 0; i < res.length; i++) {
                    var widget_id = res[i][0];
                    var widget_name = res[i][1];
                    let url = this.conf.api.widgetCommon + "/" + widget_id + "/framework.js";
                    var header_name = widget_name;
                    if (res[i][7] == 1) {
                        header_name = (widget_name.length > 9) ? widget_name.substring(0, 8) + '...' : widget_name;
                    }
                    if (res[i][4] === "S" || res[i][4] === null) {
                        $scope.widgetsList.push({
                            id: widget_id,
                            headerName: header_name,
                            name: widget_name,
                            url: url,
                            attrs: [{
                                attr: 'data-' + res[i][0],
                                value: ''
                            }],
                            x: res[i][3],
                            y: res[i][5],
                            height: res[i][6],
                            width: res[i][7]
                        });
                    }
                    var script = document
                        .createElement('script');
                    script.src = url;
                    script.async = false;
                    var entry = document
                        .getElementsByTagName('script')[0];
                    entry.parentNode
                        .insertBefore(script, entry);
                }
                $scope.applyPresentationDetailsToWidgets($scope.widgetsList);
            }).catch(err => {
                $log.error('WidgetsHomeCtrl::getUserWidgets error: ' + err);
            }).finally(() => {

            });
        };

        userProfileService.getUserProfile().then(
            function(profile) {
                $scope.orgUserId = profile.orgUserId;
                getUserWidgets($scope.orgUserId);
            });

        /* Widget Gridster Section */
        $scope.newsGridsterItem = {
            sizeX: 1,
            sizeY: 1,
            headerText: 'News',
            subHeaderText: '',
            imageLink: '',
            order: '',
            url: ''
        };

        $scope.eventsGridsterItem = {
            sizeX: 1,
            sizeY: 1,
            headerText: 'Events',
            subHeaderText: '',
            imageLink: '',
            order: '',
            url: ''
        };

        $scope.impoResGridsterItem = {
            sizeX: 1,
            sizeY: 1,
            headerText: 'Resources',
            subHeaderText: '',
            imageLink: '',
            order: '',
            url: ''
        };

        this.gridsterAppOpts = {
            columns: 6,
            colWidth: 190,
            rowHeight: 190,
            margins: [20, 20],
            outerMargin: true,
            pushing: true,
            floating: true,
            swapping: true,
            resizable: {
                enabled: false,
            },
            draggable: {
                handle: '.icon-content-gridguide',
                stop: function stop() {
                    $scope.defaultSortBy = function() {
                        var resJson = {};
                        resJson.value = 'M';
                        resJson.index = 3;
                        resJson.title = 'Manual';
                        $scope.selectedSortType = resJson;
                        applicationsService.saveAppsSortTypeManual($scope.appsViewData)
                        applicationsService.saveAppsSortTypePreference($scope.selectedSortType)
                    }
                    $scope.defaultSortBy();
                }
            }
        };

        this.gridsterWidgetOpts = {
            columns: 6,
            colWidth: 190,
            rowHeight: 190,
            margins: [20, 20],
            outerMargin: true,
            pushing: true,
            floating: true,
            swapping: true,
            resizable: {
                enabled: true,
                stop: function stop(event, uiWidget, $element) {
                    if ($element.sizeX == 1)
                        $element.headerText = ($element.widgetText.length > 9) ? $element.widgetText.substring(0, 8) + '...' : $element.widgetText;
                    if ($element.sizeX >= 2)
                        $element.headerText = $element.widgetText;

                    applicationsService
                        .saveWidgetsSortManual($scope.widgetsViewData)

                }
            },
            draggable: {
                handle: '.icon-content-gridguide',
                stop: function stop() {
                    applicationsService
                        .saveWidgetsSortManual($scope.widgetsViewData)

                }
            }
        };

        this.goToCatalog = function(item) {
            $state.go('root.appCatalog');
        }

        this.goToWidgetCatLog = function(item) {
            $state.go('root.widgetCatalog');
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
                // cache control so browsers load app page every
                // time
                var ccParam = 'cc=' + new Date().getTime();
                var urlParts = item.url.split('#');
                var appUrl = null;
                if (urlParts.length < 2) {
                    // no #
                    let urlLastChar = item.url.charAt(item.url.length - 1);
                    if (item.url.includes("?"))
                        appUrl = (urlLastChar === '&' ? item.url + ccParam : item.url + '&' + ccParam);
                    else
                        appUrl = item.url + '?' + ccParam;
                } else {
                    // has #
                    let urlLastChar = urlParts[0].charAt(urlParts[0].length - 1);
                    if (item.url.includes("?"))
                        appUrl = (urlLastChar === '&' ? urlParts[0] + ccParam + '#' + urlParts[1] : urlParts[0] + '&' + ccParam + '#' + urlParts[1]);
                    else
                        appUrl = urlParts[0] + '?' + ccParam + "#" + urlParts[1];
                }
                // $log.debug('DashboardCtrlr::goToPortal: opening
                // tab with URL
                // ' + appUrl);
                var tabContent = {
                    id: new Date(),
                    title: item.headerText,
                    url: appUrl,
                    appId: item.appid
                };
                $cookies.putObject('addTab', tabContent);
            }


        
        };

        this.auditLog = function(app) {
            console.log(app);
            auditLogService.storeAudit(app.appid, 'app', app.url);
        };

        if (getParameterByName('noUserError') != null) {
            if (getParameterByName('noUserError') == "Show") {
                $("#errorInfo").show();
            }
        }
    };

    DashboardCtrl.$inject = ['conf', 'applicationsService', '$log', '$window',
        'userProfileService', '$scope', '$cookies', '$timeout', '$interval',
        '$modal', '$state', 'beReaderService', 'dashboardService', 'confirmBoxService',
        'auditLogService', 'ngDialog', '$compile', 'widgetsCatalogService'
    ];
    angular.module('ecompApp').controller('DashboardCtrl', DashboardCtrl);
})();

function getParameterByName(name, url) {
    if (!url)
        url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex
        .exec(url);
    if (!results)
        return '';
    if (!results[2])
        return '';
    return results[2].replace(/\+/g, " ");
}