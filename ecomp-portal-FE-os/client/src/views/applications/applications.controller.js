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
    class ApplicationsCtrl {

    	constructor($log, $cookies, conf, ngDialog,
        			applicationsService, confirmBoxService, userProfileService, utilsService,$modal) {
            this.emptyImgForPreview = 'data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==';
            let getOnboardingApps = () => {
                this.isLoadingTable = true;
                applicationsService.getOnboardingApps()
                    .then(appsList => {
                    	// Use proper back-end URL to load image
                    	for (var i = 0; i < appsList.length; i++) {
                    		appsList[i].imageLink = '';
                    		if (appsList[i].imageUrl){
                    			appsList[i].imageLink = conf.api.appThumbnail.replace(':appId', appsList[i].id);
                        		appsList[i].imageLink = appsList[i].imageLink+'?' + new Date().getTime();
                    		}
                    	}
                    	this.appsList = appsList;
                    }).catch(err => {
                        confirmBoxService.showInformation('There was a problem retrieving the Applications. ' +
                            'Please try again later. Error Status: '+ err.status).then(isConfirmed => {});
                        $log.error('ApplicationsCtrl:openAddNewAppModal: Error: ', err);
                    }).finally(()=> {
                        this.isLoadingTable = false;
                    });
            };

            let init = () => {
                this.isLoadingTable = false;
                getOnboardingApps();
                this.searchString = '';
                this.appsTableHeaders = [
                    {name: 'Application Name', value: 'name', isSortable: true},
                    {name: 'Active', value: 'isEnabled', isSortable: true},
                    {name: 'Integration Type', value: 'restrictedApp', isSortable: true},
                    {name: 'Guest Access', value: 'isOpen', isSortable: true},
                    {name: 'URL', value: 'url', isSortable: true},
                    {name: 'REST URL', value: 'restUrl', isSortable: true},                
                    {name: 'Communication Topic', value: 'uebTopicName', isSortable: true},
                    {name: 'Communication Key', value: 'uebKey', isSortable: true},
                    {name: 'Communication Secret', value: 'uebSecret', isSortable: true},
                    {name: 'Application Namespace', value: 'nameSpace', isSortable: true},
                    {name: 'Central Access Type', value: 'isCentralAuth', isSortable: true}
                ];
                this.appsList = [];
            };

            init();

            this.openAddNewAppModal = (selectedApp) => {
                let data = null;
                if (selectedApp) {
                    if (!selectedApp.id) {
                        $log.error('App id not found');
                        return;
                    }
                    data = {
                        app: selectedApp
                    }
                }
                var modalInstance = $modal.open({
                    templateUrl: 'app/views/applications/application-details-dialog/application-details.modal.html',
                    controller: 'AppDetailsModalCtrl as appDetails',
                    sizeClass: 'modal-large', 
                    resolve: {
    					items: function () {
        	        	  return data;
    			        	}
    		        }
                })
                
                modalInstance.result.finally(function (){
                		getOnboardingApps();
     	        });

            };

            this.deleteApp = application => {
                $log.debug('ApplicationsCtrl:deleteApp:: ', application.name);

                confirmBoxService.deleteItem(application.name).then(isConfirmed => {
                    if(isConfirmed){
                        if(!application || !application.id){
                            $log.error('ApplicationsCtrl:deleteApp:: No application or ID... cannot delete');
                            return;
                        }
                        applicationsService.deleteOnboardingApp(application.id).then(() => {
                            this.appsList.splice(this.appsList.indexOf(application), 1);
                        }).catch(err => {
                            confirmBoxService.showInformation('There was a problem deleting the Application. ' +
                                'Please try again later. Error Status: '+ err.status).then(isConfirmed => {});
                            $log.error('ApplicationsCtrl:deleteApp: Error: ', err);
                        });
                    }
                }).catch(err => {
                    confirmBoxService.showInformation('There was a problem deleting the Application. ' +
                        'Please try again later. Error Status: '+ err.status).then(isConfirmed => {});
                    $log.error('ApplicationsCtrl:deleteApp error:: ', err);
                });

            };

        }
    }
    ApplicationsCtrl.$inject = ['$log', '$cookies', 'conf', 'ngDialog',
    				'applicationsService', 'confirmBoxService', 'userProfileService', 'utilsService','$modal'];
    angular.module('ecompApp').controller('ApplicationsCtrl', ApplicationsCtrl);
})();
