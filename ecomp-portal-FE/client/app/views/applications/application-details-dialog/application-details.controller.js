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
    class AppDetailsModalCtrl {
        constructor($scope, $log, applicationsService, errorMessageByCode,
                    ECOMP_URL_REGEX,userProfileService, $cookies, confirmBoxService) {
            let emptyImg = null;
            this.emptyImgForPreview = 'data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==';

            let newAppModel = {
                'id': null,
                'name': null,
                'imageUrl': null,
                'description': null,
                'notes': null,
                'url': null,
                'alternateUrl': null,
                'restUrl': null,
                'isOpen': false,
                'username': null,
                'appPassword': null,
                'thumbnail': emptyImg,
                'isEnabled': true,
                'restrictedApp': false
            };

            let init = () => {
                $log.info('AppDetailsModalCtrl::init');
                this.isSaving = false;
                if($scope.ngDialogData && $scope.ngDialogData.app){
                    $log.debug('AppDetailsModalCtrl:init:: Edit app mode for', $scope.ngDialogData.app);
                    this.isEditMode = true;
                    this.app = _.clone($scope.ngDialogData.app);
                }else{
                    $log.debug('AppDetailsModalCtrl:init:: New app mode');
                    this.isEditMode = false;
                    this.app = _.clone(newAppModel);
                }
                this.originalImage = null
            };

            this.ECOMP_URL_REGEX = ECOMP_URL_REGEX;

            this.imageApi = {};
            this.removeImage = () => {
                $log.debug('AppDetailsModalCtrl:removeImage:: entering removeImage');

                confirmBoxService.confirm("Are you sure you want to remove the image?").then(isConfirmed => {
                    if(isConfirmed){
                        this.imageApi.clearFile();
                        this.app.thumbnail = emptyImg;
                        this.originalImage = null;
                        this.app.imageUrl = null;
                    }
                }).catch(err => {
                    $log.error('AppDetailsModalCtrl:removeImage error:: ',err);
                });
            };

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
                        $scope.appForm[field.name].$setValidity('conflict', false);
                        watchOnce[field.name]();
                    });
                });
                this.scrollApi.scrollTop();
            };

            let resetConflict = fieldName => {
                delete this.conflictMessages[fieldName];
                if($scope.appForm[fieldName]){
                    $scope.appForm[fieldName].$setValidity('conflict', true);
                }
            };


            let emptyCookies = () => {
                $log.debug('AppDetailsModalCtrl:emptyCookies:: entering emptyCookies');
                userProfileService.getUserProfile()
                    .then(profile=> {
                        $scope.userId = profile.orgUserId;
                        $log.debug('AppDetailsModalCtrl:emptyCookies for the following userId: ' + profile.orgUserId);
                        if ($cookies.getObject($scope.userId + '_apps') != undefined && $cookies.getObject($scope.userId + '_apps') != null) {
                            $cookies.remove($scope.userId + '_apps');
                            $log.debug('AppDetailsModalCtrl:emptyCookies removed: ' + $scope.userId + '_apps');
                        }
                        if ($cookies.getObject($scope.userId + '_widget') != undefined && $cookies.getObject($scope.userId + '_widget') != null) {
                            $cookies.remove($scope.userId + '_widget');
                            $log.debug('AppDetailsModalCtrl:emptyCookies removed: ' + $scope.userId + '_widget');
                        }
                    }).catch(err => {
                        $log.error('AppDetailsModalCtrl:emptyCookies error:: '+ JSON.stringify(err));
                    });
            };


            let watchOnce = {
                name: () => {
                    let unregisterName = $scope.$watch('appDetails.app.name', (newVal, oldVal) => {
                        if(newVal.toLowerCase() !== oldVal.toLowerCase()){
                            resetConflict('name');
                            unregisterName();
                        }
                    });
                },
                url: () => {
                    let unregisterUrl = $scope.$watch('appDetails.app.url', (newVal, oldVal) => {
                        if(newVal.toLowerCase() !== oldVal.toLowerCase()) {
                            resetConflict('url');
                            unregisterUrl();
                        }
                    });
                }
            };

            this.saveChanges = () => {
                if($scope.appForm.$invalid){
                    return;
                }
                this.isSaving = true;

                if (this.app.restrictedApp) {
                    this.app.restUrl = null;
                    this.app.isOpen = true;
                    this.app.username = null;
                    this.app.appPassword = null;
                    this.app.uebTopicName = null;
                    this.app.uebKey = null;
                    this.app.uebSecret = null;
                }
                if(this.isEditMode){
                    applicationsService.updateOnboardingApp(this.app)
                        .then(() => {
                            $log.debug('AppDetailsModalCtrl:updateOnboardingApp:: App update succeeded!');
                            $scope.closeThisDialog(true);
                            emptyCookies();
                        }).catch(err => {
                            if(err.status === 409){
                                handleConflictErrors(err);
                            }
                            if(err.status === 500){
                                confirmBoxService.showInformation('There was a problem updating the application changes. ' +
                                    'Please try again later.').then(isConfirmed => {});
                            }
                            if(err.status === 403){
                                confirmBoxService.showInformation('There was a problem updating the application changes. ' +
                                    'Please try again. If the problem persists, then try again later.').then(isConfirmed => {});
                            }
                            $log.error('applicationsService:updateOnboardingApp error status:: '+ err.status);
                            $log.error('applicationsService:updateOnboardingApp error:: '+ JSON.stringify(err));
                        }).finally(()=>{
                            this.isSaving = false;
                            var objOffsetVersion = objAgent.indexOf("MSIE");
                            if (objOffsetVersion != -1) {
                                $log.debug('AppDetailsModalCtrl:updateOnboardingApp:: Browser is IE, forcing Refresh');
                                $window.location.reload();
                            }
                        });
                }else{
                    applicationsService.addOnboardingApp(this.app)
                        .then(() => {
                            $log.debug('App creation succeeded!');
                            $scope.closeThisDialog(true);
                            emptyCookies();
                        }).catch(err => {
                            if(err.status === 409){
                                handleConflictErrors(err);
                            }
                            if(err.status === 500){
                                confirmBoxService.showInformation('There was a problem adding the application information. ' +
                                    'Please try again later.').then(isConfirmed => {});
                            }
                            $log.error('applicationsService:addOnboardingApp error status:: '+ err.status);
                            $log.error('applicationsService:addOnboardingApp error:: '+ JSON.stringify(err));
                        }).finally(()=>{
                            this.isSaving = false;
                            var objOffsetVersion = objAgent.indexOf("MSIE");
                            if (objOffsetVersion != -1) {
                                $log.debug('applicationsService:addOnboardingApp:: Browser is IE, forcing Refresh');
                                $window.location.reload();
                            }
                        });
                }
            };


            init();

            $scope.$watch('appDetails.originalImage', (newVal, oldVal) => {
              if((!newVal || !newVal.resized) && !this.app.imageUrl){
                  if (!newVal) {
                      $log.debug('applicationsService:$scope.$watch:: originalImage: newVal is null');
                  } else {
                      $log.debug('applicationsService:$scope.$watch:: originalImage: newVal is not resized and no imageUrl');
                  }
                  this.app.imageUrl = null;
                  this.app.thumbnail = emptyImg;
                  return;
              }

                if(!(_.isEqual(newVal, oldVal))){
                    $log.debug('applicationsService:$scope.$watch:: thumbnail updated!');
                    this.app.imageUrl = null;
                    this.app.thumbnail = newVal.resized.dataURL;
                }
            });

            $scope.$on('$stateChangeStart', e => {
                e.preventDefault();
            });
        }
    }
    AppDetailsModalCtrl.$inject = ['$scope', '$log', 'applicationsService', 'errorMessageByCode',
        'ECOMP_URL_REGEX','userProfileService','$cookies', 'confirmBoxService'];
    angular.module('ecompApp').controller('AppDetailsModalCtrl', AppDetailsModalCtrl);
})();
