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

    class userNotificationsModalCtrl {
        constructor($scope, $log, functionalMenuService, confirmBoxService, notificationService, $modal, ngDialog, $state, $filter) {

            let newNotifModel = {
                'isOnlineUsersOnly': null,
                'isForAllRolesOptions': null,
                'selectedPriority': null,
                'isActive': null,
                'startTime': null,
                'endTime': null,
                'msgHeader': null,
                'msgDescription': null,
                'roleIds': null,
                'roleObj': {notificationRoleIds:null}
            };
            
            $scope.notificationId = null;
            $scope.selectedCat = null;
            $scope.selectedEcompFunc = null;
            this.YN_index_mapping = {
                "Y": 0,
                "N": 1
            }
            
            $scope.onlineAllUsersOptions = [
                { "index": 0, "value": "Y", "title": "Online Users Only" },
                { "index": 1, "value": "N", "title": "Online & Offline Users" }
                
            ];

            $scope.isForAllRolesOptions = [
                { "index": 0, "value": "Y", "title": "Yes" },
                { "index": 1, "value": "N", "title": "No" }
            ];

            $scope.priorityOptions  = [
                { "index": 0, "value": 1, "title": "Normal" },
                { "index": 1, "value": 2, "title": "Important" }
            ];

            $scope.isActiveOptions = [
                { "index": 0, "value": "Y", "title": "Yes" },
                { "index": 1, "value": "N", "title": "No" }
            ];
            $scope.isActive = $scope.isActiveOptions[0];
            $scope.selectPriority = $scope.priorityOptions[0];
            $scope.isOnlineUsersOnly = $scope.onlineAllUsersOptions[1];
          $scope.isForAllRoles=$scope.isForAllRolesOptions[0].value;
          $scope.isFunctionalMenu ="Y";

          $scope.selectedPriority=$scope.priorityOptions[0].value;

            // $scope.notificationRoleIds = [];
            $scope.msgHeader = '';
            $scope.msgDescription = '';
            $scope.treeTitle="Functional Menu";
            $scope.notifObj= {isCategoriesFunctionalMenu:true};

            let init = () => {
                // $log.info('userNotificationsModalCtrl::init');
                this.isSaving = false;
                var today = new Date();
                $scope.minDate = today.toISOString().substring(0, 10);
                var threeMonthsFromNow = new Date();
                threeMonthsFromNow.setMonth(threeMonthsFromNow.getMonth() + 3);
                $scope.maxDate = threeMonthsFromNow.toISOString().substring(0, 10);
                if ($scope.ngDialogData && $scope.ngDialogData.notif) {
                    // $log.debug('userNotificationsModalCtrl:init:: Edit
					// notification mode for', $scope.ngDialogData.notif);
                    $scope.isEditMode = true;
                    $scope.editModeObj = {isEditMode: true};
                    this.notif = _.clone($scope.ngDialogData.notif);
                    $scope.modalPgTitle = 'View Notification'
                    $scope.isOnlineUsersOnly = $scope.onlineAllUsersOptions[this.YN_index_mapping[this.notif.isForOnlineUsers]];
                    $scope.isForAllRoles = $scope.isForAllRolesOptions[this.YN_index_mapping[this.notif.isForAllRoles]].value;
                    $scope.isActive = $scope.isActiveOptions[this.YN_index_mapping[this.notif.activeYn]];
                    $scope.selectedPriority = $scope.priorityOptions[this.notif.priority - 1].value;
                    $scope.startTime = new Date(this.notif.startTime);
                    $scope.endTime = new Date(this.notif.endTime);
                    $scope.msgHeader = this.notif.msgHeader;
                    $scope.msgDescription = this.notif.msgDescription;
                    $scope.notificationId = this.notif.notificationId;
                    $scope.notificationRoleIds = this.notif.roleIds;
                    $scope.roleObj = {notificationRoleIds:this.notif.roleIds};
                } else {
                	// $log.debug('AppDetailsModalCtrl:init:: New app mode');
                    $scope.isEditMode = false;
                    $scope.editModeObj = {isEditMode: false};
                    $scope.modalPgTitle = 'Add a New Notification'
                    this.notif = _.clone(newNotifModel);
                    $scope.roleObj = {notificationRoleIds:null};
                }
            };
            this.conflictMessages = {};
            this.scrollApi = {};
            let handleConflictErrors = err => {
                if(!err.data){
                    return;
                }
                if(!err.data.length){ // support objects
                    err.data = [err.data]
                }
                _.forEach(err.data, item => {
                    _.forEach(item.fields, field => {
                        // set conflict message
                        this.conflictMessages[field.name] = errorMessageByCode[item.errorCode];
                        // set field as invalid
                        $scope.appForm[field.name].$setValidity('conflict', false);
                        // set watch once to clear error after user correction
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
            $scope.addUserNotificationValidation = function () {
                // // pre-processing
                if (!($scope.isEditMode)) {                    
                    var validation=false;

               if($scope.startTime && $scope.endTime && $scope.msgHeader != '' && $scope.msgDescription != '' && ($scope.startTime<$scope.endTime)){
            	   validation=true;
            	   if( $scope.isForAllRoles=='N'){
                       validation =  $scope.checkBoxObj.isAnyRoleSelected;
                   }
               }
               else{
            	   validation=false;
            	   }
                    
                    	
                   return !validation; 
                }
            }
            
            /* format the value for viewing a notification */
            $scope.formatStartDate = function () {
            	if ($scope.startTime) {
            		$scope.startTime = $filter('date')($scope.startTime, 'medium'); 
           	}            	
            }
            
            /* format the value for viewing a notification */
            $scope.formatEndDate = function () {
            	if($scope.endTime){
            		$scope.endTime = $filter('date')($scope.endTime, 'medium');
            	}
            }
            
            $scope.addUserNotification = function () {
                $scope.notificationRoleIds = [];
                // pre-processing
                for (var key in $scope.checkboxIdDict) {
                    if ($scope.checkboxIdDict[key].is_box_checked && ($scope.checkboxIdDict[key].role_id != null)) {
                        var role_ids = $scope.checkboxIdDict[key].role_id;
                        for (var i in role_ids) {
                            if (!($scope.notificationRoleIds.indexOf(role_ids[i]) > -1)) {
                                $scope.notificationRoleIds.push(role_ids[i]);
                            }
                        }
                    }
                }

                $scope.notificationRoleIds.sort();
                if (($scope.isOnlineUsersOnly) && ($scope.isForAllRoles) && ($scope.selectedPriority) && ($scope.isActive)
                    && ($scope.startTime) && ($scope.endTime) && ($scope.msgHeader != '') && ($scope.msgDescription != '')) {
                    this.newUserNotification =
                        {
                            'notificationId':$scope.notificationId,
                            'isForOnlineUsers': $scope.isOnlineUsersOnly.value,
                            'isForAllRoles': $scope.isForAllRoles,
                            'priority': $scope.selectedPriority,
                            'activeYn': $scope.isActive.value,
                            'startTime': $scope.startTime,
                            'endTime': $scope.endTime,
                            'msgHeader': $scope.msgHeader,
                            'msgDescription': $scope.msgDescription,
                            'roleIds': $scope.notificationRoleIds,
                            'createdDate': new Date()
                        };

                    // POST ajax call here;
                    if ($scope.isEditMode) {
                        notificationService.updateAdminNotification(this.newUserNotification)
                            .then(() => {
                                //$log.debug('NotificationService:updateAdminNotification:: Admin notification update succeeded!');
                                $scope.closeThisDialog(true);
                                // emptyCookies();
                            }).catch(err => {
                                $log.error('notificationService.updateAdminNotfication failed: ' + JSON.stringify(err));
                                switch (err.status) {
                                    case '409':         // Conflict
                                        // handleConflictErrors(err);
                                        break;
                                    case '500':         // Internal Server Error
                                        confirmBoxService.showInformation('There was a problem updating the notification. ' +
                                            'Please try again later. Error: ' + err.status).then(isConfirmed => { });
                                        break;
                                    case '403':         // Forbidden... possible
														// webjunction error to
														// try again
                                        confirmBoxService.showInformation('There was a problem updating the notification. ' +
                                            'Please try again. If the problem persists, then try again later. Error: ' + err.status).then(isConfirmed => { });
                                        break;
                                    default:
                                        confirmBoxService.showInformation('There was a problem updating the notification. ' +
                                            'Please try again. If the problem persists, then try again later. Error: ' + err.status).then(isConfirmed => { });
                                }
                            }).finally(() => {
                                // for bug in IE 11
                                var objOffsetVersion = objAgent.indexOf("MSIE");
                                if (objOffsetVersion != -1) {
                                    $log.debug('AppDetailsModalCtrl:updateOnboardingApp:: Browser is IE, forcing Refresh');
                                    $window.location.reload();   
                                }
                                // for bug in IE 11
                            });

                    } else {
                        notificationService.addAdminNotification(this.newUserNotification)
                            .then((res) => {
                                $log.debug('notificationService:addAdminNotification:: Admin notification creation succeeded!,',res);
                                if(res.status=='ERROR'){
                                	 confirmBoxService.showInformation('There was a problem adding the notification. ' +
                                             ' Error: ' + res.response).then(isConfirmed => { });

                                	
                                }
                                else{
                                	 $scope.closeThisDialog(true);
                                }
                               
                                // emptyCookies();
                            }).catch(err => {
                                switch (err.status) {
                                    case '409':         // Conflict
                                        // handleConflictErrors(err);
                                        break;
                                    case '500':         // Internal Server Error
                                        confirmBoxService.showInformation('There was a problem adding the notification. ' +
                                            'Please try again later. Error: ' + err.status).then(isConfirmed => { });
                                        break;
                                    default:
                                        confirmBoxService.showInformation('There was a problem adding the notification. ' +
                                            'Please try again. If the problem persists, then try again later. Error: ' +
                                            err.status).then(isConfirmed => { });
                                }
                                $log.error('notificationService:addAdminNotification error:: ' + JSON.stringify(err));
                            })
                    }

                   
                } else {
                    $log.warn('please fill in all required fields');
                	confirmBoxService.showInformation('Please fill in all required fields').then(isConfirmed => { });
                }
            }
            // Populate the category list for category dropdown list
            let getFunctionalMenu = () => {
                this.isLoadingTable = true;
                $scope.notifObj= {isCategoriesFunctionalMenu:true};
                functionalMenuService.getFunctionalMenuRole().then(role_res => {
                    var menu_role_dict = {};
                    for (var i in role_res) {
                        // if first time appear in menu_role_dict
                        if (!(role_res[i].menuId in menu_role_dict)) {
                            menu_role_dict[role_res[i].menuId] = [role_res[i].roleId];
                        } else {
                            menu_role_dict[role_res[i].menuId].push(role_res[i].roleId);
                        }
                    }
                    functionalMenuService.getManagedFunctionalMenuForNotificationTree().then(res => {
                        let actualData = [];
                        var exclude_list = ['Favorites']
                        // Adding children and label attribute to all objects in
                        $scope.checkboxIdDict = {};
                        $scope.checkBoxObj = {isAnyRoleSelected:false};
                        for (let i = 0; i < res.length; i++) {
                            res[i].children = [];
                            res[i].label = res[i].text;
                            res[i].id = res[i].text;
                            // res[i].is_box_checked = false;
                            res[i].can_check = true;
                            res[i].roleId = menu_role_dict[res[i].menuId];
                            $scope.checkboxIdDict[res[i].id] = { 'is_box_checked': false, 'role_id': res[i].roleId };
                        }

                        // Adding actual child items to children array in res
						// objects
                        $scope.parentChildDict ={};
                        $scope.parentChildRoleIdDict ={};
                        for (let i = 0; i < res.length; i++) {
                            let parentId = res[i].menuId;
                            $scope.parentChildDict[parentId] = [];
                            $scope.parentChildRoleIdDict[parentId]=[];
                            for (let j = 0; j < res.length; j++) {
                                let childId = res[j].parentMenuId;
                                if (parentId === childId) {
                                    res[i].children.push(res[j]);
                                    $scope.parentChildDict[parentId].push(res[j].menuId);
                                    //if res[j].roleId is defined
                                    if (res[j].roleId) {
                                    	for (let k in res[j].roleId) {
                                            $scope.parentChildRoleIdDict[parentId].push(res[j].roleId[k]);
                                    	}
                                    	
                                    }
                                }
                            }
                        }
                        
                        //check if grand children exist
                            for (var key in $scope.parentChildDict){
                            	var children = $scope.parentChildDict[key];
                            	var isGrandParent = false;
                            	if (children.length>0) {
                            		for (var i in children) {
                            			if ($scope.parentChildDict[children[i]].length>0){
                            				isGrandParent = true;
                            				break;
                            			}
                            		}
                            	}
                            	if (isGrandParent) {
                                	for (var i in children) {
                                		// if the child has children
                                		if ($scope.parentChildDict[children[i]].length>0) {
                                			for (var j in $scope.parentChildRoleIdDict[children[i]]) {
                                				
                                				if ($scope.parentChildRoleIdDict[key].indexOf($scope.parentChildRoleIdDict[children[i]][j]) === -1) {
                                					$scope.parentChildRoleIdDict[key].push($scope.parentChildRoleIdDict[children[i]][j]);
                                				}
                                			}
                                		} else {
                                			 
                                		}
                                	}
                            	}

                            };                            
                                                        

                                var ListMenuIdToRemove = [];
                        //$scope.parentObj = {ListMenuIdToRemove : []};
                        //get the list of menuId that needs to be removed 
                        for (let i = 0; i < res.length; i++) {
                            if ((res[i].children.length==0)&&(!res[i].roleId)) {
                                var menuIdToRemove = res[i].menuId;
                                if (ListMenuIdToRemove.indexOf(menuIdToRemove) === -1){
                                    ListMenuIdToRemove.push(menuIdToRemove);
                                }
                            }
                        }

                        // a scope variable that marks whether each functional menu item should be displayed.
                        $scope.toShowItemDict = {};                        
                        for (let i = 0; i < res.length; i++) {
                            if (res[i].roleId==null) {
                                if (res[i].children.length==0) {
                                    $scope.toShowItemDict[res[i].menuId]=false;                                
                                } else if(res[i].children.length>0){
                                    if($scope.parentChildDict[res[i].menuId].length === _.intersection($scope.parentChildDict[res[i].menuId], ListMenuIdToRemove).length){
                                        $scope.toShowItemDict[res[i].menuId]=false;                                
                                    } else {
                                        $scope.toShowItemDict[res[i].menuId]=true;                                
                                    }
                                }
                            } else {
                                $scope.toShowItemDict[res[i].menuId]=true;
                            }
                        }
 
                        // Sort the top-level menu items in order based on the
						// column
                        res.sort(function (a, b) {
                            return a.column - b.column;
                        });

                        // Sort all the children in order based on the column
                        for (let i = 0; i < res.length; i++) {
                            res[i].children.sort(function (a, b) {
                                return a.column - b.column;
                            });
                        }

                        // Forming actual parent items
                        for (let i = 0; i < res.length; i++) {
                            let parentId = res[i].parentMenuId;
                            if (parentId === null) {
                                actualData.push(res[i]);
                            }
                        }

                        // $scope.treedata = actualData;
                        var treedata = actualData[0].children;
                        $scope.treedata = [];
                        for (var i in treedata) {
                            if (!(treedata[i].label.indexOf(exclude_list) > -1)) {
                                $scope.treedata.push(treedata[i])
                            }
                        }

                    }).catch(err => {
                        $log.error('FunctionalMenuCtrl:getFunctionalMenu:: error ', err);
                    }).finally(() => {
                        this.isLoadingTable = false;
                    })

                }).catch(err => {
                    $log.error('FunctionalMenuCtrl:getFunctionalMenu:: error ', err);
                })
                    ;
            }
     
            
            let getAppRoleIds = () => {
                $scope.notifObj= {isCategoriesFunctionalMenu:false};
                notificationService.getAppRoleIds().then(res => {
                	
                    res = res.data;
                    let actualData = [];
                    // var exclude_list = ['Favorites']
                    var app_id_name_list = {};
                        $scope.checkboxIdDict = {};
                        $scope.checkBoxObj = {isAnyRoleSelected:false};

                    for (let i = 0; i < res.length; i++) {
                        if (!(res[i].appId in app_id_name_list)) {
                            app_id_name_list[res[i].appId] = res[i].appName;
                        }

                        res[i].children = [];
                        res[i].label = res[i].roleName;
                        res[i].id = res[i].roleId;
                        res[i].menuId = res[i].roleId;
                        res[i].parentMenuId = res[i].appId;
                        res[i].can_check = true;
                        res[i].roleId = [res[i].roleId];
                        $scope.checkboxIdDict[res[i].id] = { 'is_box_checked': false, 'role_id': res[i].roleId};   
                    }
                    
                    for (var app_id in app_id_name_list) {
                        var new_res = {};
                        new_res.children = [];
                        new_res.label = app_id_name_list[app_id];
                        new_res.id = app_id;
                        new_res.menuId = app_id;
                        new_res.parentMenuId = null;
                        new_res.appId = null;
                        new_res.can_check = true;
                        new_res.roleId = null;
                        $scope.checkboxIdDict[new_res.id]= { 'is_box_checked': false, 'role_id': new_res.roleId };
                        res.push(new_res);
                    }
                    $scope.parentChildRoleIdDict ={};
                    //Adding actual child items to children array in res objects
                    for (let i = 0; i < res.length; i++) {
                        let parentId = res[i].menuId;
                        $scope.parentChildRoleIdDict[parentId]=[];
                        for (let j = 0; j < res.length; j++) {
                            let childId = res[j].parentMenuId;
                            if (parentId == childId) {
                                res[i].children.push(res[j]);
                                if (res[j].roleId) {
                                	for (let k in res[j].roleId) {
                                        $scope.parentChildRoleIdDict[parentId].push(res[j].roleId[k]);
                                	}
                                	
                                }
                            }
                        }
                    }
                    //Forming actual parent items
                    for (let i = 0; i < res.length; i++) {
                        let parentId = res[i].parentMenuId;
                        if (parentId === null) {
                            actualData.push(res[i]);
                        }
                    }

                    $scope.treedata = actualData;
                }).catch(err => {
                    $log.error('FunctionalMenuCtrl:getFunctionalMenu:: error ', err);
                }).finally(() => {
                    this.isLoadingTable = false;
                })
            }
            $scope.getFunctionalMenu= function() {
            $scope.treeTitle="Functional Menu";
            getFunctionalMenu();
            }
            $scope.getAppRoleIds = function() {
            $scope.treeTitle="Applications/Roles";
                getAppRoleIds();
            }
             
            init();
            getFunctionalMenu();

        }
        
    }
    
    userNotificationsModalCtrl.$inject = ['$scope', '$log', 'functionalMenuService', 'confirmBoxService', 'notificationService', '$modal', 'ngDialog', '$state', '$filter'];
    angular.module('ecompApp').controller('userNotificationsModalCtrl', userNotificationsModalCtrl);
    
    angular.module('ecompApp').directive('attDatepickerCustom', ['$log', function($log) {
        return {
            restrict: 'A',
          require: 'ngModel',
            scope: {},
            
            controller: ['$scope', '$element', '$attrs', '$compile', 'datepickerConfig', 'datepickerService', function($scope, $element, $attrs, $compile, datepickerConfig, datepickerService) {
               var dateFormatString = angular.isDefined($attrs.dateFormat) ? $scope.$parent.$eval($attrs.dateFormat) : datepickerConfig.dateFormat;
               var selectedDateMessage = '<div class="sr-focus hidden-spoken" tabindex="-1">the date you selected is {{$parent.current | date : \'' + dateFormatString + '\'}}</div>';
               $element.removeAttr('att-datepicker-custom');
                $element.removeAttr('ng-model');
                $element.attr('ng-value', '$parent.current |  date:"EEEE, MMMM d, y"');
                $element.attr('aria-describedby', 'datepicker');
               
                $element.attr('maxlength', 10);

                var wrapperElement = angular.element('<div></div>');
                wrapperElement.attr('datepicker-popup', '');
                wrapperElement.attr('current', 'current');

                datepickerService.setAttributes($attrs, wrapperElement);
                datepickerService.bindScope($attrs, $scope);

                wrapperElement.html('');
                wrapperElement.append($element.prop('outerHTML'));
                if (navigator.userAgent.match(/MSIE 8/) === null) {
                    wrapperElement.append(selectedDateMessage);
                }
                var elm = wrapperElement.prop('outerHTML');
                elm = $compile(elm)($scope);
                $element.replaceWith(elm);
            }],
            link: function(scope, elem, attr, ctrl) {
                if (!ctrl) {
                    // do nothing if no ng-model
                    $log.error("ng-model is required.");
                    return;
                }

                scope.$watch('current', function(value) {
                    ctrl.$setViewValue(value);
                });
                ctrl.$render = function() {
                    scope.current = ctrl.$viewValue;
                };
              
            }
        };
    }]);

    angular.module('ecompApp').directive('jqTreeUserNotif', ['functionalMenuService', '$log', 'confirmBoxService', '$compile', function (functionalMenuService, $log, confirmBoxService, $compile) {
        return {
            scope: true,
            templateUrl: 'jq-tree-tmpl-user-notif.html',
            link: function (scope, el, attrs) {

                var $jqTree = el.find('#jqTreeUserNotif').tree({
                    data: scope.treedata,
                    autoOpen: scope.editModeObj.isEditMode,
                    dragAndDrop: false,
                    onCreateLi: function (node, $li) {
                        node.is_checked = false;
                        if (node.roleId&&scope.roleObj.notificationRoleIds) {
                            node.is_checked = (node.roleId.length === _.intersection(node.roleId, scope.roleObj.notificationRoleIds).length);
                        }                        
                        if (typeof node.id =="string"){
                            $li.attr('id', node.id.replace(/\s+/g, '_'));
                        }
                        var isChecked = '';
                        if (node.is_checked) {
                            isChecked = 'checked="checked"';
                        }
                        if (node.can_check) {
                            var toShow = true;
                            if (scope.notifObj.isCategoriesFunctionalMenu) {
                                toShow = scope.toShowItemDict[node.menuId];
                            }
                            var isDisabled = "";
                            if (scope.editModeObj.isEditMode) {
                                isDisabled = " disabled"
                                	
                                	//if node is a parent/grandparent node
                                    if (node.children.length>0){
                                             	//whether to show node first
                                    	if (_.intersection(scope.parentChildRoleIdDict[node.menuId], scope.roleObj.notificationRoleIds).length) {
                                    		toShow=true;
                                    		if (scope.parentChildRoleIdDict[node.menuId].length==_.intersection(scope.parentChildRoleIdDict[node.menuId], scope.roleObj.notificationRoleIds).length) {
                                                isChecked = 'checked="checked"';                                			
                                    		}
                                    	} else {
                                    		toShow=false;
                                    	}
                                    } 
                                    //if node is a child node
                                    else {
                                    	if (node.is_checked) {
                                        	toShow=true;                                	
                                    	} else {
                                    		toShow=false;
                                    	}
                                    }

                              }

                            

                            var template = '<input ng-click="thisCheckboxClicked($event)" type="checkbox" class="edit js-node-check" data-node-menu-id="' + node.menuId + '"  data-node-id="' + node.id + '" ' + isChecked + ' ng-show="' + toShow + '"' + isDisabled+ '/>'

                            var templateEl = angular.element(template);
                            var $jqCheckbox = $compile(templateEl)(scope);
							if (toShow){
									$li.find('.jqtree-element').prepend($jqCheckbox);
								} else {
									$li.find('.jqtree-element').remove();
									}
                        }
                    }
                });

                scope.thisCheckboxClicked = function (e) {
                
                	var nodeId = e.target.attributes[4].value;
                	
             
               
                	var sBrowser, sUsrAg = window.navigator.userAgent;
                	//if (sUsrAg.indexOf("Firefox") > -1) {
                	
                	if (sUsrAg.indexOf("Trident") > -1) {
                		nodeId = e.target.attributes[5].value;
                	}
                    
//                	if (sUsrAg.indexOf("MSIE") > 1) {
//                		alert("hELLO tHIS IS IE10");
//                		nodeId = e.target.attributes[3].value;
//                		alert('nodeId 26 of IE 45 : '+nodeId);
//                	}
//                	
                	var version = navigator.userAgent.match(/Firefox\/(.*)$/);
                	
                	if(version && version.length > 1){
                	if(parseInt(version[1]) >= 50){
                		nodeId = e.target.attributes[3].value;
                	} else if(parseInt(version[1]) >= 45){
                		
                		nodeId = e.target.attributes[2].value;
                	}
                }
                	var thisNode = el.find('#jqTreeUserNotif').tree('getNodeById', nodeId);
                    var isChecked = e.target.checked;
                    scope.checkboxIdDict[nodeId]['is_box_checked'] = isChecked;

                    thisNode = angular.element(thisNode);
                    if (thisNode[0].hasOwnProperty('children') && thisNode[0].children.length > 0) {
                        var jsNodeCheckList = angular.element(e.target).parent().next().find('.js-node-check')
                        // check/uncheck children items
                        jsNodeCheckList.prop('checked', isChecked);

                        for (var i in jsNodeCheckList) {
                            var singlediv = jsNodeCheckList[i];
                            if (typeof singlediv == 'object' & (!singlediv.length)) {
                            	
                            	var tempNodeId = angular.element(singlediv)[0].attributes[4].value;
                            
                                
                                
                                if (sUsrAg.indexOf("Trident") > -1) {
                                	
                                	var tempNodeId = angular.element(singlediv)[0].attributes[5].value;
                                	
                               
                                }
                                
//                                if (sUsrAg.indexOf("MSIE") > 0) {
//                                    var tempNodeId = angular.element(singlediv)[0].attributes[3].value;
//                                    alert('tempNodeId 2 FF 45 : '+tempNodeId);
//                                    }
                                if(version && version.length > 1){
                                	if(parseInt(version[1]) >= 50){
                                		tempNodeId = angular.element(singlediv)[0].attributes[3].value;
                                	} 
                                	else if(parseInt(version[1]) >= 45){
                                		tempNodeId = angular.element(singlediv)[0].attributes[2].value;
                                	}
                                }
                                scope.checkboxIdDict[tempNodeId]['is_box_checked'] = isChecked;
                            }
                        }
                    }

                    scope.checkBoxObj.isAnyRoleSelected = false;
                    for (var key in scope.checkboxIdDict) {
                        if (scope.checkboxIdDict[key]['is_box_checked']&&scope.checkboxIdDict[key]['role_id']) {
                            scope.checkBoxObj.isAnyRoleSelected = true;
                            break;
                        }
                    }
                }



                scope.$watch('treedata', function (oldValue, newValue) {
                    if (oldValue !== newValue) {
                        $jqTree.tree('loadData', scope.treedata);
                        $jqTree.tree('reload', function () {
                        });
                    }
                });
            }
        };
    }]);
})();
