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
    class WidgetOnboardingCtrl {
        constructor($log, applicationsService, widgetsCatalogService, ngDialog, confirmBoxService,
                    userProfileService, $cookies, $scope) {
            $scope.infoMessage = true;

            let populateAvailableApps = widgets => {
            	let allPortalsFilterObject = {index: 0, title: 'All applications', value: ''};
                this.availableApps = [allPortalsFilterObject];
                this.filterByApp = this.availableApps[0];
                applicationsService.getAppsForSuperAdminAndAccountAdmin().then(myApps => {
                	var reSortedApp = myApps.sort(getSortOrder("name"));
                    var realAppIndex = 1;
                    for (let i = 1; i <= reSortedApp.length; i++) {
                        if (!reSortedApp[i-1].restrictedApp) {
                            this.availableApps.push({
                                index: realAppIndex,
                                title: reSortedApp[i - 1].name,
                                value: reSortedApp[i - 1].name
                            })
                            realAppIndex = realAppIndex + 1;
                        }
                    }
                }).catch(err => {
                    $log.error('WidgetOnboardingCtrl:getAppsForSuperAdmin failed', err);
                });
            };
                 
            let getOnboardingWidgets = () => {
                this.isLoadingTable = true;
                this.isCommError = false;
                widgetsCatalogService.getManagedWidgets().then(res => {
                	if(!(res instanceof Array)){
        				this.isCommError = true;
        				return;
        			 }
                	
                    var reSortedWidget = res.sort(getSortOrder("name"));
                    $scope.widgetsList = reSortedWidget;
                    for(var i = 0; i < $scope.widgetsList.length; i++){
                    	let set = new Set();
                    	var info = "";
                    	var appContent = [];
                    	var appName = [];	
                    	for(var n = 0; n < $scope.widgetsList[i].widgetRoles.length; n++){
                    		set.add($scope.widgetsList[i].widgetRoles[n].app.appName);
                    	}
                    	if($scope.widgetsList[i].allowAllUser == "Y"){
                    		info = "All Applications";
                    		appContent.push("All Applications");
                    		appName.push("All Applications");
                    	}
                    	
                    	set.forEach(function (item) {
                    		info = item.toString() + " - ";
                    		for(var n = 0; n < $scope.widgetsList[i].widgetRoles.length; n++){
                    			if(item.toString() == $scope.widgetsList[i].widgetRoles[n].app.appName){
                        			info += $scope.widgetsList[i].widgetRoles[n].roleName + "; ";
                        		}
                    		}
                    		appContent.push(info);
                    		appName.push(item.toString());
                    	});
                    	$scope.widgetsList[i].appContent = appContent;
                    	$scope.widgetsList[i].appName = appName;
                	}
                    populateAvailableApps(reSortedWidget);
                }).catch(err => {
                	// Land here when the micro service is down
                    $log.error('WidgetOnboardingCtrl::getOnboardingWidgets caught error', err);
                }).finally(()=> {
                    this.isLoadingTable = false;
                });
             
            };
            
            
            // Refactor this into a directive
            let getSortOrder = (prop) => {
                return function(a, b) {
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
                this.searchString = '';
                this.widgetsTableHeaders = [
                    {name: 'Widget Name', value: 'name', isSortable: false}
                ];
                $scope.widgetsList = [];
            };
           
            this.filterByDropdownValue = item => {            	
                if(this.filterByApp.value === '')
                    return true;
                
             	for(var i = 0; i < item.appName.length; i++){
                	if(item.appName[i] == this.filterByApp.value
                	|| item.appName[i] == 'All Applications'){
                		return true;
                	}
            	}
                return false;
            };

            this.openWidgetCatalogDetailsModal = (selectedWidget) => {
            	let data = null;
                if(selectedWidget){
                    if(!selectedWidget.id){
                        $log.error('WidgetOnboardingCtrl:openWidgetCatalogDetailModal: widget id not found');
                        return;
                    }
                    data = {
                        widget: selectedWidget
                    }
                }
                ngDialog.open({
                    templateUrl: 'app/views/widget-onboarding/widget-details-dialog/widget-details.modal.html',
                    controller: 'WidgetOnboardingDetailsModalCtrl',
                    controllerAs: 'widgetOnboardingDetails',
                    data: data
                }).closePromise.then(needUpdate => {
                	if(needUpdate.value === true){
                        getOnboardingWidgets();
                    }
                });
            };

            this.deleteWidget = widget => { 
            	
               confirmBoxService.deleteItem(widget.name).then(isConfirmed => {
                	if(isConfirmed){
                        if(!widget || !widget.id){
                            $log.error('WidgetOnboardingCtrl::deleteWidget: No widget or ID... cannot delete');
                            return;
                        }
                        widgetsCatalogService.deleteWidget(widget.id).then(() => {
                        	$scope.widgetsList.splice($scope.widgetsList.indexOf(widget), 1);
                        }).catch(err => {
                            $log.error('WidgetOnboardingCtrl::deleteWidget error:',err);
                        });
                    }
                }).catch(err => {
                    $log.error('WidgetOnboardingCtrl::deleteWidget error:',err);
                });
                
            };
            
            
            this.downloadWidget = widget => {
	        	widgetsCatalogService.downloadWidgetFile(widget.id).then(res => {
	        		var data = res;
	        		var filename = widget.name + ".zip";
	        		
	        		if (data == undefined || data == null){
	        			confirmBoxService.showInformation("Could not download. Please retry.");
	        			return;         	
	        		}
	        		var a = document.createElement('a');
	        		var blob = new Blob([data], {type: 'application/octet-stream'});
	        		var url = window.URL.createObjectURL(blob);
	        		a.href = url;
	        		a.download = filename;
	        		document.body.appendChild(a);
	        		a.click();
	        		
	        		setTimeout(function(){
	        	        document.body.removeChild(a);
	        	        window.URL.revokeObjectURL(url);  
	        	    }, 100);  
	        	});
            };
            
            init();
        }
    }
    WidgetOnboardingCtrl.$inject = ['$log', 'applicationsService', 'widgetsCatalogService', 'ngDialog', 'confirmBoxService',
        'userProfileService','$cookies', '$scope'];
    angular.module('ecompApp').controller('WidgetOnboardingCtrl', WidgetOnboardingCtrl);
})();
