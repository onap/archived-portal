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
    class MenuDetailsModalCtrl {
        constructor($scope, $log, functionalMenuService, errorMessageByCode, ECOMP_URL_REGEX,$rootScope,confirmBoxService) {


            let newMenuModel = {
                name: null,
                menuId: null,
                parentMenuId: null,
                url: null
            };

            let getAvailableRoles = (appid) => {
                this.isSaving = true;
                if (appid != null) {
                    $log.debug("MenuDetailsModalCtrl::getAvailableRoles: About to call getManagedRolesMenu");
                    functionalMenuService.getManagedRolesMenu(appid).then(rolesObj => {
                        $log.debug("MenuDetailsModalCtrl::getAvailableRoles: Roles returned = " + JSON.stringify(rolesObj))
                        this.availableRoles = rolesObj;
                        this.preSelectedRoles = {roles:[]};

                        if(($scope.ngDialogData.source==='edit') && this.isMidLevelMenuItem()){
                            // in Edit flow , for Midlevel menu item no need to preSelect.
                            this.preSelectedRoles = {roles:[]};
                        }else if(!angular.isUndefined(this.menuItem.menuDetails) &&
                            $scope.ngDialogData.source==='edit' && this.isLeafMenuItem() &&
                            this.menuItem.menuDetails.appid!=appid) {
                            // in Edit flow , for LeafMenuItem, if appid changed then no need to preSelect.
                            this.preSelectedRoles = {roles:[]};
                        }else{
                            if((!angular.isUndefined(this.menuItem.menuDetails)) &&
                                (!angular.isUndefined(this.menuItem.menuDetails.roles))){
                                    $log.debug('menuDetails.roles: ' + this.menuItem.menuDetails.roles);
                                    for(var i=0; i<this.menuItem.menuDetails.roles.length; i++){
                                        var role = {"roleId":this.menuItem.menuDetails.roles[i]};
                                        $log.debug('MenuDetailsModalCtrl::getAvailableRoles: adding role to preselected: ' + i + ': ' + JSON.stringify(role));
                                        this.preSelectedRoles.roles.push(role);
                                    }
                            }
                        }
                        $rootScope.$broadcast('availableRolesReady');
                        this.isSaving = false;
                    }).catch(err => {
                        $log.error("MenuDetailsModalCtrl::getAvailableRoles: error: " + err);
                    });
                } else {
                    $log.debug("MenuDetailsModalCtrl::getAvailableRoles: appid was null");
                }
            };

            let getAvailableApps = () => {
                functionalMenuService.getAvailableApplications().then(apps => {
                    $log.debug("MenuDetailsModalCtrl::getAvailableApps: Apps returned = " + JSON.stringify(apps))
                    this.availableApps = apps;
                    // Keep track of whether or not the selected app is disabled
                    if (angular.isDefined(this.selectedApp) && angular.isDefined(this.selectedApp.index)) {
                        for (var i = 0; i < apps.length; i++) {
                            if (apps[i].index === this.selectedApp.index) {
                                $log.debug("MenuDetailsModalCtrl::getAvailableApps: found app with index: " + this.selectedApp.index);
                                $log.debug("MenuDetailsModalCtrl::getAvailableApps: setting isDisabled to: " + !apps[i].enabled);
                                this.selectedApp.isDisabled = !apps[i].enabled;
                                break;
                            }
                        }
                        $log.debug("didn't find index: " + this.selectedApp.index);
                    }
                })['catch'](function (err) {
                    confirmBoxService.showInformation('There was a problem retrieving the Applications. ' +
                        'Please try again later. Error Status: '+ err.status).then(isConfirmed => {});
                    $log.error("MenuDetailsModalCtrl::getAvailableApps: getAvailableApps error: " + err);
                }).finally(()=>{
                    this.isSaving = false;
                });
            };

            let init = () => {
                $log.info('MenuDetailsModalCtrl::init');
                this.saveOrContinueBtnText = "Save";
                this.isSaving = false;
                this.displayRoles = $scope.ngDialogData.source==='view' ? true : false;
                this.formEditable = $scope.ngDialogData.source==='view' ? false : true;
                this.selectedRole = [];
                this.availableRoles = [];
                this.menuItem = _.clone($scope.ngDialogData.menuItem);
                $log.info('MenuDetailsModalCtrl::getAvailableApps: Within init, about to check menuDetails for defined');
                if(!angular.isUndefined(this.menuItem.menuDetails) &&
                    ($scope.ngDialogData.source==='view' ||
                    ($scope.ngDialogData.source==='edit') && this.isLeafMenuItem() )){

                    $log.debug("MenuDetailsModalCtrl::init: menuItem: ");
                    $log.debug('MenuDetailsModalCtrl::init: ',this.menuItem);
                    this.menuItem.menu.url = this.menuItem.menuDetails.url;
                    this.selectedApp={};
                    this.selectedApp.index = this.menuItem.menuDetails.appid;
                    getAvailableRoles(this.selectedApp.index);

                }

                if($scope.ngDialogData.source==='view' || $scope.ngDialogData.source==='edit'){
                    this.menutitle = this.menuItem.menu.name;
                    this.menuLocation = this.isParentMenuItem() ? this.menuItem.menu.name : this.menuItem.menu.parent.name;
                }else{
                    this.menutitle = '';
                    this.menuLocation = this.menuItem.menu.name;
                }
                // Temporarily passing 0 as dummy for getAvailableRoles incase of this.selectedApp is not there i.e., in Add flow
                //  getAvailableRoles(angular.isUndefined(this.selectedApp) ? 0: this.selectedApp.index );
                getAvailableApps();
                $log.debug("MenuDetailsModalCtrl::init: Menu details: " +  JSON.stringify(this.menuItem.menuDetails));
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
                $log.debug('MenuDetailsModalCtrl::handleConflictErrors: err.data = ' + JSON.stringify(err.data));
                _.forEach(err.data, item => {
                    _.forEach(item.fields, field => {
                        //set conflict message
                        this.conflictMessages[field.name] = errorMessageByCode[item.errorCode];
                        //set field as invalid
                        $log.debug('MenuDetailsModalCtrl::handleConflictErrors: fieldName = ' + field.name);
                        $scope.functionalMenuForm[field.name].$setValidity('conflict', false);
                        //set watch once to clear error after user correction
                        watchOnce[field.name]();
                    });
                });
                this.scrollApi.scrollTop();
            };

            let resetConflict = fieldName => {
                delete this.conflictMessages[fieldName];
                $log.debug('MenuDetailsModalCtrl::resetConflict: $setValidity(true) = ' + fieldName);
                if($scope.functionalMenuForm[fieldName]){
                    $scope.functionalMenuForm[fieldName].$setValidity('conflict', true);
                }
            };

            let watchOnce = {
                text: () => {
                    let unregisterName = $scope.$watch('functionalMenuDetails.menutitle', (newVal, oldVal) => {
                        // $log.debug('title:: newVal, oldVal = ' + newVal.toLowerCase() + " | " + oldVal.toLowerCase());
                        if(newVal.toLowerCase() !== oldVal.toLowerCase()){
                            resetConflict('text');
                            unregisterName();
                        }
                    });
                },
                url: () => {
                    let unregisterUrl = $scope.$watch('functionalMenuDetails.menuItem.menu.url', (newVal, oldVal) => {
                        if(newVal.toLowerCase() !== oldVal.toLowerCase()){
                            resetConflict('url');
                            unregisterUrl();
                        }
                    });
                }
            };

            //***************************

            this.isLeafMenuItem = () => {
                return this.menuItem.menu.children.length>0 ? false : true;
            };

            this.isMidLevelMenuItem = () => {
                return this.menuItem.menu.parentMenuId!=null && this.menuItem.menu.children.length>0 ? true : false;
            };

            this.isParentMenuItem = () => {
                return this.menuItem.menu.parentMenuId!=null ? false : true;
            };

            this.updateSelectedApp = (appItem) => {
                if (!appItem) {
                    return;
                }
                $log.debug('MenuDetailsModalCtrl::updateSelectedApp: drop down app item = ' + JSON.stringify(appItem.index));
                $log.debug("MenuDetailsModalCtrl::updateSelectedApp: appItem in updateSelectedApp: ");
                $log.debug('MenuDetailsModalCtrl::updateSelectedApp: ',appItem);
                this.selectedApp.isDisabled = ! appItem.enabled;
                $log.debug("MenuDetailsModalCtrl::updateSelectedApp: isDisabled: "+this.selectedApp.isDisabled);
                getAvailableRoles(appItem.index);
            };

            this.continue = () => {
                this.displayRoles = true;
                this.formEditable = false;
            };

            this.saveChanges = () => {

                //todo : form validation was commented as dialog message is kept for error validations
                /*if($scope.functionalMenuForm.$invalid){
                 return;
                 }*/

                if(!!this.menuItem.menu.url && (angular.isUndefined(this.selectedApp) || !this.selectedApp.index>0)) {
                    confirmBoxService.showInformation('Please select the appropriate app, or remove the url').then(isConfirmed => {});
                    return;
                }else if(!this.menuItem.menu.url && !angular.isUndefined(this.selectedApp) && this.selectedApp.index>0){
                    confirmBoxService.showInformation('Please enter url, or select "No Application"').then(isConfirmed => {});
                    return;
                }else if(!this.menutitle){
                    confirmBoxService.showInformation('Please enter the Menu title').then(isConfirmed => {});
                    return;
                }

                this.isSaving = true;
                var activeMenuItem = {};

                if ($scope.ngDialogData.source === 'edit') {     // Edit Menu Item
                    $log.debug('MenuDetailsModalCtrl::saveChanges: Will be saving an edit menu item');
                    activeMenuItem = {
                        menuId:this.menuItem.menu.menuId,
                        column:this.menuItem.menu.column,
                        text:this.menutitle,
                        parentMenuId:this.menuItem.menu.parentMenuId,
                        url:this.menuItem.menu.url,
                        appid: angular.isUndefined(this.selectedApp) ? null:this.selectedApp.index,
                        roles:this.selectedRole
                    };
                    // If we have removed the url and appid, we must remove the roles
                    if (!activeMenuItem.appid && !activeMenuItem.url) {
                        activeMenuItem.roles = null;
                    }
                    functionalMenuService.saveEditedMenuItem(activeMenuItem)
                        .then(() => {
                            $log.debug('MenuDetailsModalCtrl::saveChanges:  Menu Item saved');
                            $scope.closeThisDialog(true);
                        }).catch(err => {
                        if(err.status === 409){//Conflict
                            handleConflictErrors(err);
                        } else {
                            confirmBoxService.showInformation('There was a problem saving your change. ' +
                                'Please try again later. Error Status: '+ err.status).then(isConfirmed => {});
                        }
                        $log.error('MenuDetailsModalCtrl::saveChanges: error - ',err);
                    }).finally(()=>{
                        this.isSaving = false;
                    });

                    $log.debug("MenuDetailsModalCtrl::saveChanges: Edit Menu output will be: " + JSON.stringify(activeMenuItem));
                } else {   // New Menu Item
                    $log.debug('MenuDetailsModalCtrl::saveChanges: Will be saving a New menu item');
                    var newMenuItem = {
                        menuId:null, // this is a new menu item
                        column:this.menuItem.menu.column,
                        text:this.menutitle,
                        // We are creating this new menu item under the menu item that was clicked on.
                        parentMenuId:this.menuItem.menu.menuId,
                        url:this.menuItem.menu.url,
                        appid: angular.isUndefined(this.selectedApp) ? null:this.selectedApp.index,
                        roles:this.selectedRole
                    };

                    $log.debug("MenuDetailsModalCtrl::saveChanges:  New Menu output will be: " + JSON.stringify(newMenuItem));
                    functionalMenuService.saveMenuItem(newMenuItem)
                        .then(() => {
                            $log.debug('MenuDetailsModalCtrl::saveChanges:  Menu Item saved');
                            $scope.closeThisDialog(true);
                        }).catch(err => {
                        if(err.status === 409){//Conflict
                            handleConflictErrors(err);
                        } else {
                            confirmBoxService.showInformation('There was a problem saving your menu. ' +
                                'Please try again later. Error Status: '+ err.status).then(isConfirmed => {});
                        }
                        $log.error('MenuDetailsModalCtrl::saveChanges error: ', err);
                    }).finally(()=>{
                        this.isSaving = false;
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
    MenuDetailsModalCtrl.$inject = ['$scope', '$log', 'functionalMenuService', 'errorMessageByCode', 'ECOMP_URL_REGEX','$rootScope','confirmBoxService'];
    angular.module('ecompApp').controller('MenuDetailsModalCtrl', MenuDetailsModalCtrl);

    angular.module('ecompApp').directive('dropdownMultiselect', ['functionalMenuService',function(){
        return {
            restrict: 'E',
            scope: {
                model: '=',
                options: '=',
                populated_roles: '=preSelected',
                dropdownTitle: '@',
                source: '='
            },
            template: "<div class='btn-group' data-ng-class='{open: open}'>" +
            "<button class='btn btn-medium'>{{dropdownTitle}}</button>" +
            "<button class='btn dropdown-toggle' data-ng-click='open=!open;openDropDown()'><span class='caret'></span></button>" +
            "<ul class='dropdown-menu dropdown-menu-medium' aria-labelledby='dropdownMenu'>" +
            "<li data-ng-repeat='option in options'> <input ng-disabled='setDisable(source)'  type='checkbox' data-ng-change='setSelectedItem(option.roleId)' ng-model='selectedItems[option.roleId]'>{{option.rolename}}</li>" +
            "</ul>" +
            "</div>",
            controller: function ($scope) {
                $scope.selectedItems = {};
                $scope.checkAll = false;
                $scope.$on('availableRolesReady', function() {
                    init();
                });

                function init() {
                    console.log('dropdownMultiselect init');
                    $scope.dropdownTitle = $scope.source ==='view' ? 'View Roles' : 'Select Roles';
                    console.log('$scope.populated_roles = ' + $scope.populated_roles);
                }

                $scope.$watch('populated_roles', function(){
                    if ($scope.populated_roles && $scope.populated_roles.length>0) {
                        for (var i = 0; i < $scope.populated_roles.length; i++) {
                            $scope.model.push($scope.populated_roles[i].roleId);
                            $scope.selectedItems[$scope.populated_roles[i].roleId] = true;
                        }
                        if ($scope.populated_roles.length === $scope.options.length) {
                            $scope.checkAll = true;
                        }
                    }else{
                        deselectAll();
                    }
                });

                $scope.openDropDown = function () {

                };

                $scope.checkAllClicked = function () {
                    if ($scope.checkAll) {
                        selectAll();
                    } else {
                        deselectAll();
                    }
                };

                function selectAll() {
                    $scope.model = [];
                    $scope.selectedItems = {};
                    angular.forEach($scope.options, function (option) {
                        $scope.model.push(option.roleId);
                    });
                    angular.forEach($scope.model, function (id) {
                        $scope.selectedItems[id] = true;
                    });
                    console.log($scope.model);
                };

                function deselectAll() {
                    $scope.model = [];
                    $scope.selectedItems = {};
                    console.log($scope.model);
                };

                $scope.setSelectedItem = function (id) {
                    var filteredArray = [];
                    if ($scope.selectedItems[id] === true) {
                        $scope.model.push(id);
                    } else {
                        filteredArray = $scope.model.filter(function (value) {
                            return value != id;
                        });
                        $scope.model = filteredArray;
                        $scope.checkAll = false;
                    }
                    console.log(filteredArray);
                    return false;
                };

                $scope.setDisable = function(source){
                    return source ==='view' ? true : false;
                }
            }
        }
    }]);

})();
