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
    class AppDetailsModalCtrl {
        constructor($scope, $log, applicationsService, errorMessageByCode,
                    ECOMP_URL_REGEX,userProfileService, $cookies, confirmBoxService,items) {
//            let emptyImg = 'data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==';
            // empty image should really be empty, or it causes problems for the back end
            let emptyImg = null;
            this.emptyImgForPreview = 'data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==';
            $scope.ngDialogData=items;
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
                'restrictedApp': false,
                'nameSpace': null,
                'isCentralAuth': false                
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
                        this.app.imageLink = null;
                    }
                }).catch(err => {
                    $log.error('AppDetailsModalCtrl:removeImage error:: ',err);
                });
            };

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
                        $scope.appForm[field.name].$setValidity('conflict', false);
                        //set watch once to clear error after user correction
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
                        // $log.info(profile);
                        $scope.orgUserId = profile.orgUserId;
                        $log.debug('AppDetailsModalCtrl:emptyCookies for the following orgUserId: ' + profile.orgUserId);
                        if ($cookies.getObject($scope.orgUserId + '_apps') != undefined && $cookies.getObject($scope.orgUserId + '_apps') != null) {
                            $cookies.remove($scope.orgUserId + '_apps');
                            $log.debug('AppDetailsModalCtrl:emptyCookies removed: ' + $scope.orgUserId + '_apps');
                        }
                        if ($cookies.getObject($scope.orgUserId + '_widget') != undefined && $cookies.getObject($scope.orgUserId + '_widget') != null) {
                            $cookies.remove($scope.orgUserId + '_widget');
                            $log.debug('AppDetailsModalCtrl:emptyCookies removed: ' + $scope.orgUserId + '_widget');
                        }
                    }).catch(err => {
                        $log.error('AppDetailsModalCtrl:emptyCookies error:: '+ JSON.stringify(err));
                    });
            };


            let watchOnce = {
                name: () => {
                    let unregisterName = $scope.$watch('appDetails.app.name', (newVal, oldVal) => {
                        // $log.debug('newVal, oldVal = ' + newVal.toLowerCase() + " | " + oldVal.toLowerCase())
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
            //***************************

            this.saveChanges = () => {
                //if valid..
            	 if(((angular.isUndefined(this.app.name) || !this.app.name)&&(angular.isUndefined(this.app.url) || !this.app.url)
            			 &&(angular.isUndefined(this.app.username) || !this.app.username)&&(angular.isUndefined(this.app.appPassword) || !this.app.appPassword))) {
            		 confirmBoxService.showInformation('Please fill in all required fields').then(isConfirmed => {});
                     return;
                 }else if(!((angular.isUndefined(this.app.name) || !!this.app.name)&&(angular.isUndefined(this.app.url) || !!this.app.url))){
                     confirmBoxService.showInformation('Please fill in all required fields').then(isConfirmed => {});
                     return;
                 }
                this.isSaving = true;
                // For a restricted app, null out all irrelevant fields
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
                    if (this.app.nameSpace=="") {this.app.nameSpace = null;}
                    applicationsService.updateOnboardingApp(this.app)
                        .then(() => {
                            $log.debug('AppDetailsModalCtrl:updateOnboardingApp:: App update succeeded!');
                          //  $scope.closeThisDialog(true);
                            $scope.$dismiss('cancel');
                            emptyCookies();
                        }).catch(err => {
                            switch (err.status) {
                                case '409':         // Conflict
                                    handleConflictErrors(err);
                                    break;
                                case '500':         // Internal Server Error
                                    confirmBoxService.showInformation('There was a problem updating the application changes. ' +
                                        'Please try again later. Error: ' + err.status).then(isConfirmed => {});
                                    break;
                                case '403':         // Forbidden... possible webjunction error to try again
                                    confirmBoxService.showInformation('There was a problem updating the application changes. ' +
                                        'Please try again. If the problem persists, then try again later. Error: ' + err.status).then(isConfirmed => {});
                                    break;
                                default:
                                    confirmBoxService.showInformation('There was a problem updating the application changes. ' +
                                        'Please try again. If the problem persists, then try again later. Error: ' + err.status).then(isConfirmed => {});
                            }
                            $log.error('applicationsService:updateOnboardingApp error:: '+ JSON.stringify(err));
                        }).finally(()=>{
                            this.isSaving = false;
                            // for bug in IE 11
                            var objOffsetVersion = objAgent.indexOf("MSIE");
                            if (objOffsetVersion != -1) {
                                $log.debug('AppDetailsModalCtrl:updateOnboardingApp:: Browser is IE, forcing Refresh');
                                $window.location.reload();            // for bug in IE 11
                            }
                            // for bug in IE 11
                        });
                }else{
                    applicationsService.addOnboardingApp(this.app)
                        .then(() => {
                            $log.debug('App creation succeeded!');
                            //$scope.closeThisDialog(true);
                            $scope.$dismiss('cancel');
                            emptyCookies();
                        }).catch(err => {
                            switch (err.status) {
                                case '409':         // Conflict
                                    handleConflictErrors(err);
                                    break;
                                case '500':         // Internal Server Error
                                    confirmBoxService.showInformation('There was a problem adding the application information. ' +
                                        'Please try again later. Error: ' + err.status).then(isConfirmed => {});
                                    break;
                                default:
                                    confirmBoxService.showInformation('There was a problem updating the application changes. ' +
                                        'Please try again. If the problem persists, then try again later. Error: ' +
                                        err.status).then(isConfirmed => {});
                            }
                            $log.error('applicationsService:addOnboardingApp error:: '+ JSON.stringify(err));
                        }).finally(()=>{
                            this.isSaving = false;
                            // for bug in IE 11
                            var objOffsetVersion = objAgent.indexOf("MSIE");
                            if (objOffsetVersion != -1) {
                                $log.debug('applicationsService:addOnboardingApp:: Browser is IE, forcing Refresh');
                                $window.location.reload();            // for bug in IE 11
                            }
                            // for bug in IE 11
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
                    this.app.imageLink = null;
                    this.app.thumbnail = newVal.resized.dataURL;
                }
            });

            $scope.$on('$stateChangeStart', e => {
                //Disable navigation when modal is opened
                e.preventDefault();
            });
        }
    }
    AppDetailsModalCtrl.$inject = ['$scope', '$log', 'applicationsService', 'errorMessageByCode',
        'ECOMP_URL_REGEX','userProfileService','$cookies', 'confirmBoxService','items'];
    angular.module('ecompApp').controller('AppDetailsModalCtrl', AppDetailsModalCtrl);
})();