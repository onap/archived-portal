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
    const HTTP_PROTOCOL_RGX = /https?:\/\//;
    class  ApplicationsHomeCtrl {
        constructor(applicationsService, $log, $window, userProfileService, $scope,$cookies, utilsService) {
			//activate spinner
            this.isLoading = true;
            $scope.getUserAppsIsDone = false;
            this.userProfileService = userProfileService;
            //get all user's applications on init
            $scope.buildNumber = "OpenECOMP Portal Version: 1.0.0"

            userProfileService.getUserProfile()
                .then(profile=> {
                    $log.info('ApplicationsHomeCtrl::getUserProfile', profile);
                    $scope.userId = profile.orgUserId;
                    //$scope.appsViewData = ['notempty']; // initialize with length != 0
                    $scope.appsViewData = [];
                    $scope.appsView = [];

                    //redirect to login.htm, if the EPService cookie is missing and logged in user is not a guest.
                    if (!$cookies.get('EPService') && !profile.guestSession) {
                        this.isLoading = false;
                        var myHostName;
                        myHostName = location.host;
                        $log.info("EPService cookie is missing, so attempting to redirecting to login page.");
                        if (utilsService.isRunningInLocalDevEnv()) {
                            $log.info("Portal is running in local development and redirecting to 'http://localhost:8080/ecompportal/login.htm'.");
                            $window.location.href = "http://localhost:8080/ecompportal/login.htm";
                        } else {
                            $log.info("Redirecting to 'login.htm'.");
                            $window.location.href = "login.htm";
                        }
                    }

                    applicationsService.getUserApps()
                    .then(res => {
                        $log.info('ApplicationsHomeCtrl::getUserApps: ', res);
                        this.apps = res;
                        let rowNo = 0;
                        for (let i = 0; i < this.apps.length; i++) {
                            $scope.appsView[i] = {
                                sizeX: 2,
                                sizeY: 2,
                                headerText: '',
                                subHeaderText: '',
                                imageLink: '',
                                order: '',
                                url: ''
                            };
                            $scope.appsView[i].headerText = this.apps[i].name;
                            $scope.appsView[i].subHeaderText = this.apps[i].notes;
                            $scope.appsView[i].imageLink = this.apps[i].thumbnail || this.apps[i].imageUrl;
                            $scope.appsView[i].order = this.apps[i].order;
                            $scope.appsView[i].url = this.apps[i].url;
                            $scope.appsView[i].restrictedApp = this.apps[i].restrictedApp;
                        }
                        $log.info('ApplicationsHomeCtrl::getUserApps:  apps count : ' + $scope.appsView.length);

                        if ($cookies.getObject($scope.userId+'_apps') == undefined || $cookies.getObject($scope.userId+'_apps') == null || $cookies.getObject($scope.userId+'_apps').length == 0) {
                            if (($scope.appsView != undefined) && ($scope.appsView != null) && ($scope.appsView.length > 0)){
                                $scope.appsViewData = $scope.appsView;
                                $cookies.putObject($scope.userId + '_apps', $scope.appsView);
                            }
                        }
                        else{
                            this.listChanged = false;
                            this.appsListFromCookie = $cookies.getObject($scope.userId+'_apps');
                            this.finalAppsList = [];
                            //
                            // If an app is still valid for this user from backend(appsView) and
                            // it was in the cookie already, put it in the list in the same order
                            // it was in within the cookie.
                            //
                            let finalCount = 0;
                            for (let i = 0; i < this.appsListFromCookie.length; i++) {
                                this.foundAppInListFromBackend = false;
                                for (let j = 0; j < $scope.appsView.length; j++) {
                                    if (this.appsListFromCookie[i].url == $scope.appsView[j].url) {
                                        this.finalAppsList[finalCount] = {
                                            sizeX: 2,
                                            sizeY: 2,
                                            headerText: '',
                                            subHeaderText: '',
                                            imageLink: '',
                                            order: '',
                                            url: ''
                                        };
                                        this.finalAppsList[finalCount].headerText = this.appsListFromCookie[i].headerText;
                                        this.finalAppsList[finalCount].subHeaderText = this.appsListFromCookie[i].subHeaderText;
                                        this.finalAppsList[finalCount].imageLink = this.appsListFromCookie[i].imageLink;
                                        this.finalAppsList[finalCount].order = this.appsListFromCookie[i].order;
                                        this.finalAppsList[finalCount].url = this.appsListFromCookie[i].url;
                                        this.finalAppsList[finalCount].restrictedApp = this.appsListFromCookie[i].restrictedApp;
                                        finalCount++;
                                        this.foundAppInListFromBackend = true;
                                        break;
                                    }
                                }
                                if (this.foundAppInListFromBackend == false) {
                                    this.listChanged = true;
                                }
                            }

                            //
                            // Fill in the rest of the list with the Apps retrieved from the backend that we did not already add.  There could have been
                            // new Apps configured for the user that are not in the cookie.
                            //
                            for (let i = 0; i < $scope.appsView.length; i++) {
                                this.found = false;
                                for (let j = 0; j < this.finalAppsList.length; j++) {
                                    if ($scope.appsView[i].url == this.finalAppsList[j].url) {
                                        // already present
                                        this.found = true;
                                        break;
                                    }
                                }
                                if (this.found == false) {
                                    this.finalAppsList[finalCount] = {
                                        sizeX: 2,
                                        sizeY: 2,
                                        headerText: '',
                                        subHeaderText: '',
                                        imageLink: '',
                                        order: '',
                                        url: ''
                                    };
                                    this.finalAppsList[finalCount].headerText = $scope.appsView[i].headerText;
                                    this.finalAppsList[finalCount].subHeaderText = $scope.appsView[i].subHeaderText;
                                    this.finalAppsList[finalCount].imageLink = $scope.appsView[i].imageLink;
                                    this.finalAppsList[finalCount].order = $scope.appsView[i].order;
                                    this.finalAppsList[finalCount].url = $scope.appsView[i].url;
                                    this.finalAppsList[finalCount].restrictedApp = $scope.appsView[i].restrictedApp;
                                    finalCount++;
                                    this.listChanged = true;
                                }
                            }

                            if ((this.finalAppsList != undefined) && (this.finalAppsList != null) && (this.finalAppsList.length > 0)) {
                                if (this.listChanged) {
                                    $scope.appsViewData = this.finalAppsList;
                                    $cookies.putObject($scope.userId + '_apps', this.finalAppsList);
                                } else {
                                    $scope.appsViewData = $cookies.getObject($scope.userId+'_apps');
                                }
                            }
                            this.isLoading = false;
                            $scope.getUserAppsIsDone = true;
                        }
                    }).catch(err => {
                        $log.error('oh no! couldnt get applications list...', err);
                        this.isLoading = false;
                        $scope.getUserAppsIsDone = true;
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
                draggable : {
                    stop: function () {
                        $cookies.putObject($scope.userId + '_apps', $scope.appsViewData);
                    }
                }
            };

            //navigate to application url in new tab
            this.goToPortal = (item) => {
            	$log.info("goToPortal called with item: " + item);
                let url = item.url;
                let restrictedApp = item.restrictedApp;
                if (!url) {
                    $log.info('No url found for this application, doing nothing..');
                    return;
                }
                if (!HTTP_PROTOCOL_RGX.test(url)) {
                    url = 'http://' + url;
                }
                if (restrictedApp) {
                    $window.open(url, '_blank');
                } else {
                    var tabContent = { id: new Date(), title: item.headerText, url:item.url.split('#')[0] + '?' + (new Date).getTime() + "#" + item.url.split('#')[1]};
                	$cookies.putObject('addTab', tabContent );
                }

            };
            // try {
            //     userProfileService.getUserProfile()
            //         .then(profile=> {
                        // if (profile.roles.indexOf('superAdmin') > -1) {
                        //     $scope.buildNumber = "ECOMP Portal Version: 1610.0.2058"
                        // }
            //         }).catch(err=> {
            //             $log.error('Applications Home:: getUserProfile() failed: ' + err);
            //     });
            // } catch (err) {
            //     $log.error('Applications Home:: getUserProfile() failed: ' + err);
            // }
            if(getParameterByName('noUserError')!=null){
            	 if(getParameterByName('noUserError')=="Show"){
                 	  $("#errorInfo").show();
                 }
            }
        }
    }
    ApplicationsHomeCtrl.$inject = ['applicationsService', '$log', '$window', 'userProfileService', '$scope','$cookies', 'utilsService'];
    angular.module('ecompApp').controller('ApplicationsHomeCtrl', ApplicationsHomeCtrl);
})();

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return '';
    if (!results[2]) return '';
    return (results[2].replace(/\+/g, " "));
}
