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
/**
 * Created by nnaffar on 12/20/15.
 */
'use strict';
(function () {
    class WidgetDetailsModalCtrl {
        constructor($scope, $log, applicationsService, widgetsService, errorMessageByCode,
                    ECOMP_URL_REGEX, $window, userProfileService, $cookies, $rootScope) {

            let newWidgetModel = {
                name: null,
                appId: null,
                appName: null,
                width: 360,
                height: 300,
                url: null
            };

            let getAvailableApps = () => {
                applicationsService.getAppsForSuperAdminAndAccountAdmin().then(apps => {
                    this.availableApps=[];
                    for(var i=0;i<apps.length;i++) {
                        if (!apps[i].restrictedApp) {
                            $log.debug('WidgetDetailsModalCtrl::getAvailableApps: pushing {id: ', apps[i].id, 'name: ', apps[i].name,
                                            'restrictedApp: ', apps[i].restrictedApp, '}');
                            this.availableApps.push({
                                id: apps[i].id,
                                name: apps[i].name,
                                restrictedApp: apps[i].restrictedApp
                            });
                        }
                    }
                    
                    if (this.isEditMode) {
                        this.selectedApp = _.find(apps, {id: this.widget.appId});
                        if(!this.selectedApp){
                            //workaround to display validation errors for apps dropdown in case selectedApp isn't valid
                            $scope.widgetForm.app.$dirty = true;
                        }
                    } else {
                        this.selectedApp = null;
                    }
                    //init appId & appName with selectedApp
                    this.updateSelectedApp();
                }).catch(err => {
                    confirmBoxService.showInformation('There was a problem retrieving the Widgets. ' +
                        'Please try again later.').then(isConfirmed => {});
                    $log.error('WidgetDetailsModalCtrl::getAvailableApps error: '+ err);
                });
            };
            /**/

            let init = () => {
                this.isSaving = false;
                if ($scope.ngDialogData && $scope.ngDialogData.widget) {
                    $log.debug('WidgetDetailsModalCtrl::getAvailableApps: Edit widget mode for', $scope.ngDialogData.widget);
                    this.isEditMode = true;
                    this.widget = _.clone($scope.ngDialogData.widget);
                } else {
                    $log.debug('WidgetDetailsModalCtrl::init: New app mode');
                    this.isEditMode = false;
                    this.widget = _.clone(newWidgetModel);
                }
                getAvailableApps();
            };

            this.ECOMP_URL_REGEX = ECOMP_URL_REGEX;

            //This part handles conflict errors (409)
            this.conflictMessages = {};
            this.scrollApi = {};
            let handleConflictErrors = err => {
                if(!err.data){
                    return;
                }
                if(!err.data.length){ //support objects
                    err.data = [err.data]
                }
                _.forEach(err.data, item => {
                    _.forEach(item.fields, field => {
                        //set conflict message
                        this.conflictMessages[field.name] = errorMessageByCode[item.errorCode];
                        //set field as invalid
                        $scope.widgetForm[field.name].$setValidity('conflict', false);
                        //set watch once to clear error after user correction
                        watchOnce[field.name]();
                    });
                });
                this.scrollApi.scrollTop();
            };

            let resetConflict = fieldName => {
                delete this.conflictMessages[fieldName];
                if($scope.widgetForm[fieldName]){
                    $scope.widgetForm[fieldName].$setValidity('conflict', true);
                }
            };

            let watchOnce = {
                name: () => {
                    let unregisterName = $scope.$watchGroup(['widgetDetails.selectedApp','widgetDetails.widget.name'], (newVal, oldVal) => {
                        if(newVal.toLowerCase() !== oldVal.toLowerCase()){
                            resetConflict('name');
                            unregisterName();
                        }
                    });
                },
                url: () => {
                    let unregisterUrl = $scope.$watch('widgetDetails.widget.url', (newVal, oldVal) => {
                        if(newVal.toLowerCase() !== oldVal.toLowerCase()) {
                            resetConflict('url');
                            unregisterUrl();
                        }
                    });
                }
            };
            //***************************

            this.updateSelectedApp = () => {
                if (!this.selectedApp) {
                    return;
                }
                this.widget.appId = this.selectedApp.id;
                this.widget.appName = this.selectedApp.name;
            };

            let emptyCookies = () => {
                userProfileService.getUserProfile()
                    .then(profile=> {
                        $scope.orgUserId = profile.orgUserId;
                        if ($cookies.getObject($scope.orgUserId + '_widget') != undefined && $cookies.getObject($scope.orgUserId + '_widget') != null) {
                            $cookies.remove($scope.orgUserId + '_widget');
                        }
                    }).catch(err => {
                        $log.error('WidgetDetailsModalCtrl::emptyCookies: There was a problem emptying the cookies! No user error presented though.');
                    });
            };

            this.saveChanges = () => {
                if($scope.widgetForm.$invalid){
                    return;
                }
                this.isSaving = true;
                if(this.isEditMode){
                    widgetsService.updateWidget(this.widget.id, this.widget)
                        .then(() => {
                            $log.debug('WidgetDetailsModalCtrl::saveChanges: Widget update succeeded!');
                            $scope.closeThisDialog(true);
                            emptyCookies();
                        }).catch(err => {
                            if(err.status === 409){//Conflict
                                handleConflictErrors(err);
                            } else {
                                confirmBoxService.showInformation('There was a problem saving the Widget. ' +
                                    'Please try again later. Error Status: ' + err.status).then(isConfirmed => {});
                            }
                            $log.error('WidgetDetailsModalCtrl::saveChanges error: ', err);
                        }).finally(()=>{
                            this.isSaving = false;
                            // for bug in IE 11
                            var objOffsetVersion = objAgent.indexOf("MSIE");
                            if (objOffsetVersion != -1) {
                                $log.debug('WidgetDetailsModalCtrl::saveChanges: Browser is IE, forcing Refresh');
                                $window.location.reload();            // for bug in IE 11
                            }
                            // for bug in IE 11
                        });
                } else {
                    widgetsService.createWidget(this.widget)
                        .then(() => {
                            $log.debug('WidgetDetailsModalCtrl::createWidget: Widget creation succeeded!');
                            $scope.closeThisDialog(true);
                            emptyCookies();
                            $rootScope.noWidgets = false;
                        }).catch(err => {
                        if(err.status === 409){//Conflict
                            handleConflictErrors(err);
                        } else {
                            confirmBoxService.showInformation('There was a problem creating the Widget. ' +
                                'Please try again later. Error Status: ' + err.status).then(isConfirmed => {});
                        }
                        $log.error('WidgetDetailsModalCtrl::createWidget error: ',err);
                    }).finally(()=>{
                        this.isSaving = false;
                        // for bug in IE 11
                        var objOffsetVersion = objAgent.indexOf("MSIE");
                        if (objOffsetVersion != -1) {
                            $log.debug('WidgetDetailsModalCtrl::createWidget: Browser is IE, forcing Refresh');
                            $window.location.reload();            // for bug in IE 11
                        }
                        // for bug in IE 11
                    });
                }
            };

            init();

            $scope.$on('$stateChangeStart', e => {
                //Disable navigation when modal is opened
                e.preventDefault();
            });
        }
    }
    WidgetDetailsModalCtrl.$inject = ['$scope', '$log', 'applicationsService', 'widgetsService', 'errorMessageByCode',
        'ECOMP_URL_REGEX', '$window','userProfileService','$cookies', '$rootScope'];
    angular.module('ecompApp').controller('WidgetDetailsModalCtrl', WidgetDetailsModalCtrl);
})();
