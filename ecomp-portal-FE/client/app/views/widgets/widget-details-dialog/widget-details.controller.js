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
    class WidgetDetailsModalCtrl {
        constructor($scope, $log, applicationsService, widgetsService, errorMessageByCode, ECOMP_URL_REGEX, $window,userProfileService,$cookies) {

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
                            $scope.widgetForm.app.$dirty = true;
                        }
                    } else {
                        this.selectedApp = null;
                    }
                    this.updateSelectedApp();
                }).catch(err => {
                    $log.error(err);
                });
            };
            /**/

            let init = () => {
                $log.info('AppDetailsModalCtrl::init');
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

            this.conflictMessages = {};
            this.scrollApi = {};
            let handleConflictErrors = err => {
                if(!err.data){
                    return;
                }
                if(!err.data.length){
                    err.data = [err.data]
                }
                _.forEach(err.data, item => {
                    _.forEach(item.fields, field => {
                        this.conflictMessages[field.name] = errorMessageByCode[item.errorCode];
                        $scope.widgetForm[field.name].$setValidity('conflict', false);
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
                        $log.info('AppDetailsModalCtrl::emptyCookies profile: ', profile);
                        $scope.userId = profile.orgUserId;
                        $log.info('user has the following userId: ' + profile.userId);
                        if ($cookies.getObject($scope.userId + '_widget') != undefined && $cookies.getObject($scope.userId + '_widget') != null) {
                            $cookies.remove($scope.userId + '_widget');
                        }
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
                        if(err.status === 409){
                            handleConflictErrors(err);
                        }
                        $log.error(err);
                    }).finally(()=>{
                        this.isSaving = false;
                        var objOffsetVersion = objAgent.indexOf("MSIE");
                        if (objOffsetVersion != -1) {
                            $log.debug('WidgetDetailsModalCtrl::saveChanges: Browser is IE, forcing Refresh');
                            $window.location.reload();
                        }
                    });
                }else{
                    widgetsService.createWidget(this.widget)
                        .then(() => {
                            $log.debug('WidgetDetailsModalCtrl::createWidget: Widget creation succeeded!');
                            $scope.closeThisDialog(true);
                            emptyCookies();
                        }).catch(err => {
                        if(err.status === 409){
                            handleConflictErrors('WidgetDetailsModalCtrl::createWidget error: ',err);
                        }
                        $log.error('WidgetDetailsModalCtrl::createWidget error: ',err);
                    }).finally(()=>{
                        this.isSaving = false;
                        var objOffsetVersion = objAgent.indexOf("MSIE");
                        if (objOffsetVersion != -1) {
                            $log.debug('WidgetDetailsModalCtrl::createWidget: Browser is IE, forcing Refresh');
                            $window.location.reload();
                        }
                    });
                }
            };

            init();

            $scope.$on('$stateChangeStart', e => {
                e.preventDefault();
            });
        }
    }
    WidgetDetailsModalCtrl.$inject = ['$scope', '$log', 'applicationsService', 'widgetsService', 'errorMessageByCode', 'ECOMP_URL_REGEX', '$window','userProfileService','$cookies'];
    angular.module('ecompApp').controller('WidgetDetailsModalCtrl', WidgetDetailsModalCtrl);
})();
