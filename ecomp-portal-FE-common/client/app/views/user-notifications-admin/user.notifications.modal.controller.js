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

(function() {

    class userNotificationsModalCtrl {
        constructor($scope, $log, functionalMenuService, confirmBoxService, notificationService, $modal, ngDialog, $state, $filter, items) {
            this.debug = false;
            $scope.newNotifModel = {
                'isOnlineUsersOnly': null,
                'isForAllRolesOptions': null,
                'selectedPriority': null,
                'isActive': null,
                'startTime': '',
                'endTime': '',
                'msgHeader': null,
                'msgDescription': null,
                'roleIds': null,
                'anyTreeItemSelected': false,
                'roleObj': {
                    notificationRoleIds: null
                }
            };

            $scope.notificationId = null;
            $scope.selectedCat = null;
            $scope.selectedEcompFunc = null;
            this.YN_index_mapping = {
                "Y": 0,
                "N": 1
            }

            $scope.onlineAllUsersOptions = [{
                    "index": 0,
                    "value": "Y",
                    "title": "Online Users Only"
                },
                {
                    "index": 1,
                    "value": "N",
                    "title": "Online & Offline Users"
                }

            ];

            $scope.isForAllRolesOptions = [{
                    "index": 0,
                    "value": "Y",
                    "title": "Yes"
                },
                {
                    "index": 1,
                    "value": "N",
                    "title": "No"
                }
            ];

            $scope.priorityOptions = [{
                    "index": 0,
                    "value": 1,
                    "title": "Normal"
                },
                {
                    "index": 1,
                    "value": 2,
                    "title": "Important"
                }
            ];

            $scope.isActiveOptions = [{
                    "index": 0,
                    "value": "Y",
                    "title": "Yes"
                },
                {
                    "index": 1,
                    "value": "N",
                    "title": "No"
                }
            ];
            $scope.newNotifModel.isActive = $scope.isActiveOptions[0];
            $scope.newNotifModel.selectPriority = $scope.priorityOptions[0];
            $scope.newNotifModel.isOnlineUsersOnly = $scope.onlineAllUsersOptions[1];
            $scope.newNotifModel.isForAllRoles = $scope.isForAllRolesOptions[0].value;
            $scope.newNotifModel.isFunctionalMenu = "Y";

            $scope.newNotifModel.selectedPriority = $scope.priorityOptions[0].value;
            $scope.newNotifModel.msgHeader = '';
            $scope.newNotifModel.msgDescription = '';
            $scope.newNotifModel.treeTitle = "Functional Menu";
            $scope.newNotifModel.notifObj = {
                isCategoriesFunctionalMenu: true
            };

            let init = () => {
                if (this.debug)
                    $log.debug('userNotificationsModalCtrl::init');
                this.isSaving = false;
                var today = new Date();
                $scope.minDate = today.toISOString().substring(0, 10);
                var threeMonthsFromNow = new Date();
                threeMonthsFromNow.setMonth(threeMonthsFromNow.getMonth() + 3);
                $scope.maxDate = threeMonthsFromNow.toISOString().substring(0, 10);
                if (items && items.notif) {
                    if (this.debug)
                        $log.debug('userNotificationsModalCtrl:init:: Edit notification mode for', items.notif);
                    $scope.isEditMode = true;
                    $scope.editModeObj = {
                        isEditMode: true
                    };
                    this.notif = _.clone(items.notif);
                    $scope.modalPgTitle = 'View Notification'
                    $scope.newNotifModel.isOnlineUsersOnly = $scope.onlineAllUsersOptions[this.YN_index_mapping[this.notif.isForOnlineUsers]];
                    $scope.newNotifModel.isForAllRoles = $scope.isForAllRolesOptions[this.YN_index_mapping[this.notif.isForAllRoles]].value;
                    $scope.newNotifModel.isActive = $scope.isActiveOptions[this.YN_index_mapping[this.notif.activeYn]];
                    $scope.newNotifModel.selectedPriority = $scope.priorityOptions[this.notif.priority - 1].value;
                    $scope.newNotifModel.startTime = new Date(this.notif.startTime);
                    $scope.newNotifModel.endTime = new Date(this.notif.endTime);
                    $scope.newNotifModel.msgHeader = this.notif.msgHeader;
                    $scope.newNotifModel.msgDescription = this.notif.msgDescription;
                    $scope.notificationId = this.notif.notificationId;
                    $scope.newNotifModel.notificationRoleIds = this.notif.roleIds;
                    $scope.roleObj = {
                        notificationRoleIds: this.notif.roleIds
                    };
                } else {
                    if (this.debug)
                        $log.debug('AppDetailsModalCtrl:init:: New app mode');
                    $scope.isEditMode = false;
                    $scope.editModeObj = {
                        isEditMode: false
                    };
                    $scope.modalPgTitle = 'Add a New Notification'
                    this.notif = _.clone($scope.newNotifModel);
                    $scope.roleObj = {
                        notificationRoleIds: null
                    };
                }
            };
            this.conflictMessages = {};
            this.scrollApi = {};
            let handleConflictErrors = err => {
                if (!err.data) {
                    return;
                }
                if (!err.data.length) { // support objects
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
                if ($scope.appForm[fieldName]) {
                    $scope.appForm[fieldName].$setValidity('conflict', true);
                }
            };
            $scope.addUserNotificationValidation = function() {
                // // pre-processing
                if (!($scope.isEditMode)) {
                    var validation = false;
                    if ($scope.isStartDateValidFromToday($scope.newNotifModel.startTime)&&$scope.isStartDateValidFromToday($scope.newNotifModel.endTime)&&$scope.isDateValid($scope.newNotifModel.startTime) && $scope.isDateValid($scope.newNotifModel.endTime) && $scope.newNotifModel.msgHeader != '' && $scope.newNotifModel.msgDescription != '' && ($scope.newNotifModel.startTime < $scope.newNotifModel.endTime)) {
                        validation = true;
                        if ($scope.newNotifModel.isForAllRoles == 'N') {
                            validation = $scope.checkTreeSelect();
                        }
                    } else {
                        validation = false;
                    }
                    return !validation;
                }
            }

            /* format the value for viewing a notification */
            $scope.formatStartDate = function() {
                if ($scope.newNotifModel.startTime) {
                    $scope.newNotifModel.startTime = $filter('date')($scope.startTime, 'medium');
                }
            }

            /* format the value for viewing a notification */
            $scope.formatEndDate = function() {
                if ($scope.newNotifModel.endTime) {
                    $scope.newNotifModel.endTime = $filter('date')($scope.endTime, 'medium');
                }
            }
            
            /*To validate the manual entry of date in MM/DD/YYYY Format*/

            $scope.isDateValid = function(time) {
                if (time == undefined) {
                    return false;
                }
                if (typeof time == 'object') {
                    return true;
                }
                var startDateformat = time.split('/');
                if (startDateformat.length != 3) return false;
                var day = startDateformat[1];
                var month = startDateformat[0];
                month = parseInt(month) - 1;
                var year = startDateformat[2];
                if (year.length != 4) return false;
                var composedDate = new Date(year, month, day);
                 return composedDate.getDate() == day &&
                         composedDate.getMonth() == month &&
                         composedDate.getFullYear() == year;
            };
            
         /*The manual and drop down calendar should be consistent.
         Start date must be greater than or equal to current date.The end dates are not allowed after the 3 months from current dates*/
            
            $scope.isStartDateValidFromToday = function (time) {
            	if(time == undefined){
                    return false;
                }
            	if(typeof time == 'object'){
            		return true;
            	}
                var startDateformat	=time.split('/');
                if (startDateformat.length != 3) return true;
                var day = startDateformat[1];
                var month = startDateformat[0];
                 month= parseInt(month)-1;
                var year = startDateformat[2];
                if(year.length!=4) return true;
                var composedDate = new Date(year, month, day);
              /* As end dates are not allowed after the 3 months from current dates*/
                var x = 3; //or whatever offset
                var CurrentDate = new Date();
                /*If composed date is less than the current date,error message should display*/
                if(composedDate<CurrentDate)
                	return false;
                CurrentDate.setMonth(CurrentDate.getMonth() + x);
                if(composedDate>CurrentDate)
                	return false;
                 return true;
            };


            $scope.addUserNotification = function() {
                $scope.notificationRoleIds = [];
                // pre-processing

                for (var fi = 0; fi < $scope.treedata.length; fi++) {
                    var fLevel = $scope.treedata[fi];
                    if (fLevel) {
                        var fLevelChild = fLevel.child;
                        if (fLevelChild) {
                            for (var si = 0; si < fLevelChild.length; si++) {
                                var sLevel = fLevelChild[si];
                                if (sLevel) {
                                    var sLevelChild = sLevel.child;
                                    if (sLevelChild) {
                                        for (var ti = 0; ti < sLevelChild.length; ti++) {
                                            var tLevel = sLevelChild[ti];
                                            if (tLevel.isSelected && tLevel.roleId) {
                                                $scope.newNotifModel.anyTreeItemSelected = true;
                                                for (var i in tLevel.roleId)
                                                    $scope.notificationRoleIds.push(tLevel.roleId[i]);
                                            }
                                        }
                                    }
                                }
                                if (sLevel.isSelected && sLevel.roleId) {
                                    for (var i in sLevel.roleId)
                                        $scope.notificationRoleIds.push(sLevel.roleId[i]);
                                }
                            }
                        }
                    }
                }

                $scope.notificationRoleIds.sort();
                if (($scope.newNotifModel.isOnlineUsersOnly) && ($scope.newNotifModel.isForAllRoles) && ($scope.newNotifModel.selectedPriority) && ($scope.newNotifModel.isActive) &&
                    ($scope.newNotifModel.startTime) && ($scope.newNotifModel.endTime) && ($scope.newNotifModel.msgHeader != '') && ($scope.newNotifModel.msgDescription != '')) {
                    this.newUserNotification = {
                        'notificationId': $scope.notificationId,
                        'isForOnlineUsers': $scope.newNotifModel.isOnlineUsersOnly.value,
                        'isForAllRoles': $scope.newNotifModel.isForAllRoles,
                        'priority': $scope.newNotifModel.selectedPriority,
                        'activeYn': $scope.newNotifModel.isActive.value,
                        'startTime': $scope.newNotifModel.startTime,
                        'endTime': $scope.newNotifModel.endTime,
                        'msgHeader': $scope.newNotifModel.msgHeader,
                        'msgDescription': $scope.newNotifModel.msgDescription,
                        'roleIds': $scope.notificationRoleIds,
                        'createdDate': new Date()
                    };

                    // POST ajax call here;
                    if ($scope.isEditMode) {
                        notificationService.updateAdminNotification(this.newUserNotification)
                            .then(() => {
                                if (this.debug)
                                    $log.debug('NotificationService:updateAdminNotification:: Admin notification update succeeded!');
                                $scope.closeThisDialog(true);
                            }).catch(err => {
                                $log.error('notificationService.updateAdminNotfication failed: ' + JSON.stringify(err));
                                switch (err.status) {
                                    case '409': // Conflict
                                        // handleConflictErrors(err);
                                        break;
                                    case '500': // Internal Server Error
                                        confirmBoxService.showInformation('There was a problem updating the notification. ' +
                                            'Please try again later. Error: ' + err.status).then(isConfirmed => {});
                                        break;
                                    case '403': // Forbidden... possible
                                        // webjunction error to
                                        // try again
                                        confirmBoxService.showInformation('There was a problem updating the notification. ' +
                                            'Please try again. If the problem persists, then try again later. Error: ' + err.status).then(isConfirmed => {});
                                        break;
                                    default:
                                        confirmBoxService.showInformation('There was a problem updating the notification. ' +
                                            'Please try again. If the problem persists, then try again later. Error: ' + err.status).then(isConfirmed => {});
                                }
                            }).finally(() => {
                                // for bug in IE 11
                                var objOffsetVersion = objAgent.indexOf("MSIE");
                                if (objOffsetVersion != -1) {
                                    if (this.debug)
                                        $log.debug('AppDetailsModalCtrl:updateOnboardingApp:: Browser is IE, forcing Refresh');
                                    $window.location.reload();
                                }
                                // for bug in IE 11
                            });

                    } else {
                        notificationService.addAdminNotification(this.newUserNotification)
                            .then((res) => {
                                if (this.debug)
                                    $log.debug('notificationService:addAdminNotification:: Admin notification creation succeeded!,', res);
                                if (res.status == 'ERROR') {
                                    confirmBoxService.showInformation('There was a problem adding the notification. ' +
                                        ' Error: ' + res.response).then(isConfirmed => {});


                                } else {
                                    // $scope.closeThisDialog(true);
                                    $scope.$dismiss('cancel');
                                }

                                // emptyCookies();
                            }).catch(err => {
                                switch (err.status) {
                                    case '409': // Conflict
                                        // handleConflictErrors(err);
                                        break;
                                    case '500': // Internal Server Error
                                        confirmBoxService.showInformation('There was a problem adding the notification. ' +
                                            'Please try again later. Error: ' + err.status).then(isConfirmed => {});
                                        break;
                                    default:
                                        confirmBoxService.showInformation('There was a problem adding the notification. ' +
                                            'Please try again. If the problem persists, then try again later. Error: ' +
                                            err.status).then(isConfirmed => {});
                                }
                                $log.error('notificationService:addAdminNotification error:: ' + JSON.stringify(err));
                            })
                    }


                } else {
                    $log.warn('please fill in all required fields');
                    confirmBoxService.showInformation('Please fill in all required fields').then(isConfirmed => {});
                }
            }
            $scope.functionalMenuRes = {};
            $scope.checkTreeSelect = function() {
                if ($scope.treedata) {
                    for (var fi = 0; fi < $scope.treedata.length; fi++) {
                        var fLevel = $scope.treedata[fi];
                        if (fLevel.isSelected) {
                            return true;
                        }
                        var sLevel = fLevel.child;
                        if (sLevel) {
                            for (var si = 0; si < sLevel.length; si++) {
                                if (sLevel[si].isSelected) {
                                    return true;
                                }
                                var tLevel = sLevel[si].child;
                                if (tLevel) {
                                    for (var ti = 0; ti < tLevel.length; ti++) {
                                        if (tLevel[ti].isSelected) {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return false;
            }
            // Populate the category list for category dropdown list
            let getFunctionalMenu = () => {
                this.isLoadingTable = true;
                if (this.debug)
                    $log.debug('getFunctionalMenu:init');

                functionalMenuService.getFunctionalMenuRole().then(role_res => {
                    var menu_role_dict = {};
                    if (this.debug)
                        $log.debug('functionalMenuService:getFunctionalMenuRole:: getting result', role_res);

                    for (var i in role_res) {
                        // if first time appear in menu_role_dict
                        if (!(role_res[i].menuId in menu_role_dict)) {
                            menu_role_dict[role_res[i].menuId] = [role_res[i].roleId];
                        } else {
                            menu_role_dict[role_res[i].menuId].push(role_res[i].roleId);
                        }
                    }

                    functionalMenuService.getManagedFunctionalMenuForNotificationTree().then(res => {
                        if (this.debug)
                            $log.debug('functionalMenuService:getManagedFunctionalMenuForNotificationTree:: getting result', res);
                        var exclude_list = ['Favorites'];
                        let actualData = [];
                        $scope.functionalMenuRes = res;

                        //Adding children and label attribute to all objects in res
                        for (let i = 0; i < res.length; i++) {
                            res[i].child = [];
                            res[i].name = res[i].text;
                            res[i].id = res[i].text;
                            res[i].displayCheckbox = true;
                            $scope.checkBoxObj = {
                                isAnyRoleSelected: false
                            };
                            res[i].roleId = menu_role_dict[res[i].menuId];
                            res[i].onSelect = function() {
                                $scope.$apply(function() {
                                    $scope.newNotifModel.anyTreeItemSelected = $scope.checkTreeSelect();
                                })
                            };

                            if (res[i].roleId && res[i].roleId.length == _.intersection(res[i].roleId, $scope.roleObj.notificationRoleIds).length) {
                                res[i].isSelected = true;
                                res[i].selected = true;
                                res[i].indeterminate = false;
                            } else {
                                /*default*/
                                res[i].isSelected = false;
                                res[i].selected = false;
                                res[i].indeterminate = false;
                            }
                        }

                        // Adding actual child items to children array in res
                        // objects
                        $scope.parentChildDict = {};
                        $scope.parentChildRoleIdDict = {};
                        for (let i = 0; i < res.length; i++) {
                            let parentId = res[i].menuId;
                            $scope.parentChildDict[parentId] = [];
                            $scope.parentChildRoleIdDict[parentId] = [];
                            for (let j = 0; j < res.length; j++) {
                                let childId = res[j].parentMenuId;
                                if (parentId === childId) {
                                    res[i].child.push(res[j]);
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
                        for (var key in $scope.parentChildDict) {
                            var child = $scope.parentChildDict[key];
                            var isGrandParent = false;
                            if (child.length > 0) {
                                for (var i in child) {
                                    if ($scope.parentChildDict[child[i]].length > 0) {
                                        isGrandParent = true;
                                        break;
                                    }
                                }
                            }
                            if (isGrandParent) {
                                for (var i in child) {
                                    // if the child has children
                                    if ($scope.parentChildDict[child[i]].length > 0) {
                                        for (var j in $scope.parentChildRoleIdDict[child[i]]) {
                                            if ($scope.parentChildRoleIdDict[key].indexOf($scope.parentChildRoleIdDict[child[i]][j]) === -1) {
                                                $scope.parentChildRoleIdDict[key].push($scope.parentChildRoleIdDict[child[i]][j]);
                                            }
                                        }
                                    }
                                }
                            }

                        };

                        // Sort the top-level menu items in order based on the column
                        res.sort(function(a, b) {
                            return a.column - b.column;
                        });

                        // Sort all the child in order based on the column
                        for (let i = 0; i < res.length; i++) {
                            res[i].child.sort(function(a, b) {
                                return a.column - b.column;
                            });
                        }

                        //Forming actual parent items
                        for (let i = 0; i < res.length; i++) {
                            let parentId = res[i].parentMenuId;
                            if (parentId === null) {
                                actualData.push(res[i]);
                            }
                        }
                        var treedata = actualData[0].child;
                        $scope.treedata = [];

                        /*Remove favorite from the list */
                        for (var i in treedata) {
                            if (!(treedata[i].name.indexOf(exclude_list) > -1)) {
                                $scope.treedata.push(treedata[i])
                            }
                        }
                        //setting b2b tree parameter
                        $scope.settingTreeParam();

                    }).catch(err => {
                        $log.error('FunctionalMenuCtrl:getFunctionalMenu:: error ', err);
                    }).finally(() => {
                        this.isLoadingTable = false;
                    })

                }).catch(err => {
                    $log.error('FunctionalMenuCtrl:getFunctionalMenu:: error ', err);
                });
            }


            let getAppRoleIds = () => {
                $scope.notifObj = {
                    isCategoriesFunctionalMenu: false
                };
                notificationService.getAppRoleIds().then(res => {
                    if (this.debug)
                        $log.debug('notificationService:getAppRoleIds:: getting result', res);
                    res = res.data;
                    let actualData = [];
                    var app_id_name_list = {};
                    $scope.checkBoxObj = {
                        isAnyRoleSelected: false
                    };
                    for (let i = 0; i < res.length; i++) {
                        if (!(res[i].appId in app_id_name_list)) {
                            app_id_name_list[res[i].appId] = res[i].appName;
                        }
                        res[i].child = [];
                        res[i].name = res[i].roleName;
                        res[i].displayCheckbox = true;
                        res[i].id = res[i].roleId;
                        res[i].menuId = res[i].roleId;
                        res[i].parentMenuId = res[i].appId;
                        res[i].can_check = true;
                        res[i].roleId = [res[i].roleId];
                        res[i].onSelect = function() {
                            $scope.$apply(function() {
                                $scope.newNotifModel.anyTreeItemSelected = $scope.checkTreeSelect();
                            })
                        };
                        /*assigning selected value*/
                        if (res[i].roleId && res[i].roleId.length == _.intersection(res[i].roleId, $scope.roleObj.notificationRoleIds).length) {
                            res[i].isSelected = true;
                            res[i].selected = true;
                            res[i].indeterminate = false;
                        } else {
                            /*default*/
                            res[i].isSelected = false;
                            res[i].selected = false;
                            res[i].indeterminate = false;
                        }
                    }

                    for (var app_id in app_id_name_list) {
                        var new_res = {};
                        new_res.child = [];
                        new_res.name = app_id_name_list[app_id];
                        new_res.id = app_id;
                        new_res.displayCheckbox = true;
                        new_res.menuId = app_id;
                        new_res.parentMenuId = null;
                        new_res.appId = null;
                        new_res.can_check = true;
                        new_res.roleId = null;
                        new_res.onSelect = function() {
                            $scope.$apply(function() {
                                $scope.newNotifModel.anyTreeItemSelected = $scope.checkTreeSelect();
                            })
                        };
                        res.push(new_res);
                    }
                    $scope.parentChildRoleIdDict = {};
                    //Adding actual child items to child array in res objects
                    for (let i = 0; i < res.length; i++) {
                        let parentId = res[i].menuId;
                        $scope.parentChildRoleIdDict[parentId] = [];
                        for (let j = 0; j < res.length; j++) {
                            let childId = res[j].parentMenuId;
                            if (parentId == childId) {
                                res[i].child.push(res[j]);
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
                    //setting correct parameters for b2b tree
                    $scope.settingTreeParam();
                }).catch(err => {
                    $log.error('FunctionalMenuCtrl:getFunctionalMenu:: error ', err);
                }).finally(() => {
                    this.isLoadingTable = false;
                })
            }
            $scope.getFunctionalMenu = function() {
                $scope.treeTitle = "Functional Menu";
                getFunctionalMenu();
            }
            $scope.getAppRoleIds = function() {
                $scope.treeTitle = "Applications/Roles";
                getAppRoleIds();
            }



            $scope.settingTreeParam = function() {
                /**************first level****************/
                for (var fi = 0; fi < $scope.treedata.length; fi++) {
                    var fLevel = $scope.treedata[fi];
                    var sLevel = $scope.treedata[fi].child;
                    var sLevelSelectedCount = 0;
                    var sLevelChildNumber = 0
                    if (fLevel.child.length == 0 && fLevel.roleId == null) {
                        delete fLevel.child;
                    } else if (sLevel) {
                        /**************Second level****************/
                        var sLevelDelArray = [];
                        for (var si = 0; si < sLevel.length; si++) {
                            var deletThisSLev = false;
                            if (sLevel[si].child.length == 0 && sLevel[si].roleId == null) {
                                sLevel[si].displayCheckbox = false;
                                sLevelDelArray.push(sLevel[si].name);
                                sLevel[si].name = '';
                                sLevel[si].active = false;
                                delete sLevel[si].child;
                            } else if (sLevel[si].child.length == 0) {
                                delete sLevel[si].child;
                            } else {
                                /**************Third level****************/
                                var tLevel = sLevel[si].child;
                                var tLevelSelectedCount = 0;
                                var tLevelChildNumber = 0;
                                if (tLevel) {
                                    var tLevelDelArray = [];
                                    var tLevelLen = tLevel.length;
                                    var tLevelRoleIdUndefined = 0;
                                    for (var ti = 0; ti < tLevel.length; ti++) {
                                        delete tLevel[ti].child;
                                        if (tLevel[ti].roleId == null) {
                                            tLevel[ti].displayCheckbox = false;
                                            tLevelDelArray.push(tLevel[ti].name);
                                            tLevel[ti].name = '';
                                            tLevel[ti].active = false;
                                            tLevelRoleIdUndefined++
                                        } else {
                                            if (tLevel[ti].isSelected)
                                                tLevelSelectedCount++;

                                            if (tLevel[ti].displayCheckbox)
                                                tLevelChildNumber++;
                                        }
                                    }
                                    if (tLevelRoleIdUndefined == tLevelLen)
                                        deletThisSLev = true;
                                    if (tLevelSelectedCount == tLevelChildNumber) {
                                        sLevel[si].isSelected = true;
                                        sLevel[si].indeterminate = false;
                                        sLevel[si].active = true;
                                    } else if (tLevelSelectedCount > 0) {
                                        sLevel[si].indeterminate = true;
                                        sLevel[si].active = true;
                                    }

                                    /*Cleanup unused third level items*/
                                    for (var i = 0; i < tLevelDelArray.length; i++) {
                                        var name = tLevelDelArray[i];
                                        for (var ti = 0; ti < tLevel.length; ti++) {
                                            if (name == tLevel[ti].text) {
                                                tLevel.splice(ti, 1);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            if (deletThisSLev) { //remove the whole second level item if all it's child has no roleId
                                sLevel[si].displayCheckbox = false;
                                sLevelDelArray.push(sLevel[si].name);
                                sLevel[si].name = '';
                                sLevel[si].active = false;
                            } else {
                                if (sLevel[si].isSelected)
                                    sLevelSelectedCount++;
                                if (sLevel[si].displayCheckbox)
                                    sLevelChildNumber++;
                            }
                        }
                        if (sLevelSelectedCount == sLevelChildNumber && sLevelChildNumber != 0) {
                            fLevel.isSelected = true;
                            fLevel.indeterminate = false;
                            fLevel.active = true;
                        } else if (sLevelSelectedCount > 0) {
                            fLevel.indeterminate = true;
                            fLevel.active = true;
                        } else {
                            //fLevel.active=false;
                            fLevel.indeterminate = false;
                        }
                        /*Cleanup unused second level items*/
                        for (var i = 0; i < sLevelDelArray.length; i++) {
                            var name = sLevelDelArray[i];
                            for (var si = 0; si < sLevel.length; si++) {
                                if (name == sLevel[si].text) {
                                    sLevel.splice(si, 1);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            init();
            getFunctionalMenu();

        }

    }

    userNotificationsModalCtrl.$inject = ['$scope', '$log', 'functionalMenuService', 'confirmBoxService', 'notificationService', '$modal', 'ngDialog', '$state', '$filter', 'items'];
    angular.module('ecompApp').controller('userNotificationsModalCtrl', userNotificationsModalCtrl);

})();