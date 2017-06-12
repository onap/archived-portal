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
(function () {
    class WidgetsCtrl {
        constructor($log, applicationsService, widgetsService, ngDialog, confirmBoxService,
                    userProfileService, $cookies, $scope, $rootScope) {
            //$log.info('WidgetsCtrl::init: Starting Up');
            $scope.infoMessage = true;
            $rootScope.noWidgets = false;

            let populateAvailableApps = widgets => {
                let allPortalsFilterObject = {index: 0, title: 'All applications', value: ''};
                this.availableApps = [allPortalsFilterObject];
                this.filterByApp = this.availableApps[0];
                applicationsService.getAppsForSuperAdminAndAccountAdmin().then(myApps => {
                    var reSortedApp = myApps.sort(getSortOrder("name"));
                    var realAppIndex = 1;
                    for (let i = 1; i <= reSortedApp.length; i++) {
                        if (!reSortedApp[i-1].restrictedApp) {
                            $log.debug('WidgetsCtrl::populateAvailableApps: pushing {index: ', realAppIndex, 'title: ', reSortedApp[i - 1].name,
                                'value: ', reSortedApp[i - 1].name, '}');
                            this.availableApps.push({
                                index: realAppIndex,
                                title: reSortedApp[i - 1].name,
                                value: reSortedApp[i - 1].name
                            })
                            realAppIndex = realAppIndex + 1;
                        }
                    }
                }).catch(err => {
                    $log.error(err);
                });
            };

            let getOnboardingWidgets = () => {
                this.isLoadingTable = true;
                widgetsService.getManagedWidgets().then(res => {
                    $log.debug('WidgetsCtrl.getOnboardingWidgets:: ' + JSON.stringify(res));
                    // if (JSON.stringify(res) === '[]') {
                    //     confirmBoxService.showInformation('There are currently no Widgets. ').then(isConfirmed => {});
                    // }
                    var reSortedWidget = res.sort(getSortOrder("name"));
                    this.widgetsList = reSortedWidget;
                    populateAvailableApps(reSortedWidget);
                    // $log.info('WidgetsHomeCtrl::getUserWidgets count : ' + $scope.widgetsList.length);
                    if (Object.keys(res).length === 0 ) {
                        $rootScope.noWidgets = true;
                        $scope.isLoadingTable = false;
                        $log.info('WidgetsHomeCtrl::getUserWidgets: There are no available Widgets');
                    }
                }).catch(err => {
                    confirmBoxService.showInformation('There was a problem retrieving the Widgets. ' +
                        'Please try again later.').then(isConfirmed => {});
                    $log.error('WidgetsCtrl::getOnboardingWidgets error: ' + err);
                }).finally(()=> {
                    this.isLoadingTable = false;
                });
            };

            // Refactor this into a directive
            let getSortOrder = (prop) => {
                return function(a, b) {
                    // $log.debug('a = ' + JSON.stringify(a) + "| b = " + JSON.stringify(b));
                    if (a[prop].toLowerCase() > b[prop].toLowerCase()) {
                        return 1;
                    } else if (a[prop].toLowerCase() < b[prop].toLowerCase()) {
                        return -1;
                    }
                    return 0;
                }
            }

            $scope.hideMe = function () {
                $scope.infoMessage = false;
            }

            let init = () => {
                this.isLoadingTable = false;
                getOnboardingWidgets();

                /*Table general configuration params*/
                this.searchString = '';
                /*Table data*/
                this.widgetsTableHeaders = [
                    {name: 'Widget Name', value: 'name', isSortable: false},
                    {name: 'Application', value: 'appName', isSortable: true},
                    {name: 'Width', value: 'width', isSortable: false},
                    {name: 'Height', value: 'height', isSortable: false}
                ];
                this.widgetsList = [];
            };

            this.filterByDropdownValue = item => {
                if(this.filterByApp.value === ''){
                    return true;
                }
                return item.appName === this.filterByApp.value;
            };

            this.openWidgetDetailsModal = (selectedWidget) => {
                let data = null;
                if(selectedWidget){
                    if(!selectedWidget.id){
                        $log.error('Widget id not found');
                        return;
                    }
                    data = {
                        widget: selectedWidget
                    }
                }
                ngDialog.open({
                    templateUrl: 'app/views/widgets/widget-details-dialog/widget-details.modal.html',
                    controller: 'WidgetDetailsModalCtrl',
                    controllerAs: 'widgetDetails',
                    data: data
                }).closePromise.then(needUpdate => {
                    if(needUpdate.value === true){
                        $log.debug('WidgetsCtrl::openWidgetDetailsModal: updating table data...');
                        getOnboardingWidgets();
                    }
                });
            };

            this.deleteWidget = widget => {
                confirmBoxService.deleteItem(widget.name).then(isConfirmed => {
                    if(isConfirmed){
                        if(!widget || !widget.id){
                            $log.error('WidgetsCtrl::deleteWidget: No widget or ID... cannot delete');
                            return;
                        }
                        widgetsService.deleteWidget(widget.id).then(() => {
                            this.widgetsList.splice(this.widgetsList.indexOf(widget), 1);
                        }).catch(err => {
                            $log.error('WidgetsCtrl::deleteWidget error:',err);
                            confirmBoxService.showInformation('There was a problem deleting the Widget. ' +
                                'Please try again later.').then(isConfirmed => {});
                        });
                    }
                }).catch(err => {
                    $log.error('WidgetsCtrl::deleteWidget error:',err);
                });
            };

            init();
        }
    }
    WidgetsCtrl.$inject = ['$log', 'applicationsService', 'widgetsService', 'ngDialog', 'confirmBoxService',
        'userProfileService','$cookies', '$scope', '$rootScope'];
    angular.module('ecompApp').controller('WidgetsCtrl', WidgetsCtrl);
})();
