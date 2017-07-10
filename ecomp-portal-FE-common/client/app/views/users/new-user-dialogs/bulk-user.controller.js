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
 * bulk user upload controller
 */
'use strict';
(function () {
    class BulkUserModalCtrl {
    	constructor($scope, $log, $filter, $q, usersService, applicationsService, confirmBoxService, functionalMenuService, ngDialog) {
    		
    		// Set to true for copious console output
    		var debug = false;
    		// Roles fetched from app service
    		var appRolesResult = [];
    		// Users fetched from user service
    		var	userCheckResult = [];
    		// Requests for user-role assignment built by validator
    		var appUserRolesRequest = [];
    		
    		let init = () => {
    			if (debug)
    				$log.debug('BulkUserModalCtrl::init');
    			// Angular insists on this.
    			$scope.fileModel = {};
    			// Model for drop-down
    			$scope.adminApps = [];
    			// Enable modal controls
    			this.step1 = true;
    			this.fileSelected = false;

    			// Flag that indicates background work is proceeding
    			$scope.isProcessing = true;

    			// Load user's admin applications
    			applicationsService.getAdminApps().promise().then(apps => {
    				if (debug)
    					$log.debug('BulkUserModalCtrl::init: getAdminApps returned' + JSON.stringify(apps));
                    if (!apps || typeof(apps) != 'object') {
                        $log.error('BulkUserModalCtrl::init: getAdminApps returned unexpected data');
                    }
                    else {
                    	if (debug)
                    		$log.debug('BulkUserModalCtrl::init:  admin apps length is ', apps.length);
                    	
                    	// Sort app names and populate the drop-down model
                        let sortedApps = apps.sort(getSortOrder('name', true));
                        for (let i = 0; i < sortedApps.length; ++i) {
                            $scope.adminApps.push({
                                index: i,
                                id: sortedApps[i].id,
                                value: sortedApps[i].name,
                                title: sortedApps[i].name
                            });
                        }
                        // Pick the first one in the list
                        $scope.selectedApplication = $scope.adminApps[0];
                    }
    				$scope.isProcessing = false;
                }).catch(err => {
                    $log.error('BulkUserModalCtrl::init: getAdminApps threw', err);
                	$scope.isProcessing = false;
                });
    			
    		}; // init
    		
    		// Answers a function that compares properties with the specified name.
    		let getSortOrder = (prop, foldCase) => {
                return function(a, b) {
                	let aProp = foldCase ? a[prop].toLowerCase() : a[prop];
                	let bProp = foldCase ? b[prop].toLowerCase() : b[prop];
                    if (aProp > bProp)
                        return 1;
                    else if (aProp < bProp) 
                        return -1;
                    else
                    	return 0;
                }
            }
    		
    		//This is a fix for dropdown selection, due to b2b dropdown only update value field
    		$scope.$watch('selectedApplication.value', (newVal, oldVal) => {
    			for(var i=0;i<$scope.adminApps.length;i++){ 			
    				if($scope.adminApps[i].value==newVal){
    					$scope.selectedApplication=angular.copy($scope.adminApps[i]);;
    				}
    			}
    		});

    		// Invoked when user picks an app on the drop-down.
    		$scope.appSelected = () => {
    			if (debug)
    				$log.debug('BulkUserModalCtrl::appSelected: selectedApplication.id is ' + $scope.selectedApplication.id);
    			this.appSelected = true;
    		}
    		
    		// Caches the file name supplied by the event handler.
    		$scope.fileChangeHandler = (event, files) => {
    			this.fileSelected = true;
    			this.fileToRead = files[0];
    			if (debug)
    				$log.debug("BulkUserModalCtrl::fileChangeHandler: file is ", this.fileToRead);
    		}; // file change handler
    		
    		/**
    		 * Reads the contents of the file, calls portal endpoints
    		 * to validate roles, userIds and existing role assignments;
    		 * ultimately builds array of requests to be sent.
    		 * Creates scope variable with input file contents for
    		 * communication with functions.
    		 * 
    		 * This function performs a synchronous step-by-step process
    		 * using asynchronous promises. The code could all be inline
    		 * here but the nesting becomes unwieldy.
    		 */
    		$scope.readValidateFile = () => {
    			$scope.isProcessing = true;
    			$scope.progressMsg = 'Reading upload file..';
    			var reader = new FileReader();
    			reader.onload = function(event) {
    				$scope.uploadFile = $filter('csvToObj')(reader.result);
    				if (debug)
    					$log.debug('BulkUserModalCtrl::readValidateFile onload: data length is ' + $scope.uploadFile.length);
    				// sort input by orgUserId
    				$scope.uploadFile.sort(getSortOrder('orgUserId', true));
    				
    				let appid = $scope.selectedApplication.id;
    				$scope.progressMsg = 'Fetching application roles..';
                    functionalMenuService.getManagedRolesMenu(appid).then(function (rolesObj) {
                    	if (debug)
                    		$log.debug("BulkUserModalCtrl::readValidateFile: managedRolesMenu returned " + JSON.stringify(rolesObj));
           				appRolesResult = rolesObj;
           				$scope.progressMsg = 'Validating application roles..';
                    	$scope.verifyRoles();
    				
                    	let userPromises = $scope.buildUserChecks();
                    	if (debug)
                    		$log.debug('BulkUserModalCtrl::readValidateFile: userPromises length is ' + userPromises.length);
                    	$scope.progressMsg = 'Validating Org Users..';
                    	$q.all(userPromises).then(function() {
                    		if (debug)
                    			$log.debug('BulkUserModalCtrl::readValidateFile: userCheckResult length is ' + userCheckResult.length);
                    		$scope.evalUserCheckResults();
    					
                    		let appPromises = $scope.buildAppRoleChecks();
                    		if (debug)
                    			$log.debug('BulkUserModalCtrl::readValidateFile: appPromises length is ' + appPromises.length);
                    		$scope.progressMsg = 'Querying application for user roles..';
                    		$q.all(appPromises).then( function() {
                    			if (debug)
                    				$log.debug('BulkUserModalCtrl::readValidateFile: appUserRolesRequest length is ' + appUserRolesRequest.length);
                    			$scope.evalAppRoleCheckResults();
                    			
	    						// Re sort by line for the confirmation dialog
	    						$scope.uploadFile.sort(getSortOrder('line', false));
	    						// We're done, confirm box may show the table
	    						if (debug)
	    							$log.debug('BulkUserModalCtrl::readValidateFile inner-then ends');
	    						$scope.progressMsg = 'Done.';
	    						$scope.isProcessing = false;
                    		},
                    		function(error) {
                    			$log.error('BulkUserModalCtrl::readValidateFile: failed retrieving user-app roles');
	    						$scope.isProcessing = false;
                    		}
                    		); // then of app promises
                    	},
                    	function(error) {
                    		$log.error('BulkUserModalCtrl::readValidateFile: failed retrieving user info');
                    		$scope.isProcessing = false;
                    	}
                    	); // then of user promises
                    },
                    function(error) {
                    	$log.error('BulkUserModalCtrl::readValidateFile: failed retrieving app role info');
                    	$scope.isProcessing = false;
                    }
                    ); // then of role promise
           
    			} // onload
    			
    			// Invoke the reader on the selected file
    			reader.readAsText(this.fileToRead);
    		}; 
    		
    		/**
    		 * Evaluates the result set returned by the app role service.
    		 * Sets an uploadFile array element status if a role is not defined.
    		 * Reads and writes scope variable uploadFile.
    		 * Reads closure variable appRolesResult.
    		 */
    		$scope.verifyRoles = () => {
    			if (debug)
    				$log.debug('BulkUserModalCtrl::verifyRoles: appRoles is ' + JSON.stringify(appRolesResult));
    			// check roles in upload file against defined app roles
    			$scope.uploadFile.forEach( function (uploadRow) {
    				// skip rows that already have a defined status: headers etc.
    				if (uploadRow.status) {
    					if (debug)
    						$log.debug('BulkUserModalCtrl::verifyRoles: skip row ' + uploadRow.line);
    					return;
    				}
    				uploadRow.role = uploadRow.role.trim();
    				var foundRole=false;
    				for (var i=0; i < appRolesResult.length; i++) {
    					if (uploadRow.role.toUpperCase() === appRolesResult[i].rolename.trim().toUpperCase()) {
    						if (debug)
    							$log.debug('BulkUserModalCtrl::verifyRoles: match on role ' + uploadRow.role);
    						foundRole=true;
    						break;
    					}
    				};
    				if (!foundRole) {
    					if (debug)
    						$log.debug('BulkUserModalCtrl::verifyRoles: NO match on role ' + uploadRow.role);
    					uploadRow.status = 'Invalid role';
    				};
    			}); // foreach
    		}; // verifyRoles
    		
    		/**
    		 * Builds and returns an array of promises to invoke the 
    		 * searchUsers service for each unique Org User UID in the input.
    		 * Reads and writes scope variable uploadFile, which must be sorted by Org User UID.
    		 * The promise function writes to closure variable userCheckResult
    		 */
    		$scope.buildUserChecks = () => {
    			if (debug)
    				$log.debug('BulkUserModalCtrl::buildUserChecks: uploadFile length is ' + $scope.uploadFile.length);
    			userCheckResult = [];
    			let promises = [];
    			let prevRow = null;
    			$scope.uploadFile.forEach(function (uploadRow) {
    				if (uploadRow.status) {
    					if (debug)
    						$log.debug('BulkUserModalCtrl::buildUserChecks: skip row ' + uploadRow.line);
    					return;
    				};
    				// detect repeated UIDs
    				if (prevRow == null || prevRow.orgUserId.toLowerCase() !== uploadRow.orgUserId.toLowerCase()) {
    					if (debug)
    						$log.debug('BulkUserModalCtrl::buildUserChecks: create request for orgUserId ' + uploadRow.orgUserId);
    					let userPromise = usersService.searchUsers(uploadRow.orgUserId).promise().then( (usersList) => {
    						if (typeof usersList[0] !== "undefined") {
    							userCheckResult.push({ 
    								orgUserId:    usersList[0].orgUserId,
    								firstName: usersList[0].firstName,
    								lastName:  usersList[0].lastName,
    								jobTitle:  usersList[0].jobTitle
    							});
    						}
    						else {
    							// User not found.
    							if (debug)
    								$log.debug('BulkUserModalCtrl::buildUserChecks: searchUsers returned null');
    						}
    					}, function(error){
    						$log.error('BulkUserModalCtrl::buildUserChecks: searchUsers failed ' + JSON.stringify(error));
    					}); 
    					promises.push(userPromise);
    				}
    				else {
    					if (debug)
    						$log.debug('BulkUserModalCtrl::buildUserChecks: skip repeated orgUserId ' + uploadRow.orgUserId);    					
    				}
    				prevRow = uploadRow;
    			}); // foreach
    			return promises;
    		}; // buildUserChecks
    		
    		/**
    		 * Evaluates the result set returned by the user service to set
    		 * the uploadFile array element status if the user was not found.
    		 * Reads and writes scope variable uploadFile.
    		 * Reads closure variable userCheckResult.
    		 */
    		$scope.evalUserCheckResults = () => {
    			if (debug)
    				$log.debug('BulkUserModalCtrl::evalUserCheckResult: uploadFile length is ' + $scope.uploadFile.length);
    			$scope.uploadFile.forEach(function (uploadRow) {
    				if (uploadRow.status) {
    					if (debug)
    						$log.debug('BulkUserModalCtrl::evalUserCheckResults: skip row ' + uploadRow.line);
    					return;
    				};
    				let foundorgUserId = false;
    				userCheckResult.forEach(function(userItem) {
    					if (uploadRow.orgUserId.toLowerCase() === userItem.orgUserId.toLowerCase()) {
    						if (debug)
    							$log.debug('BulkUserModalCtrl::evalUserCheckResults: found orgUserId ' + uploadRow.orgUserId);
    						foundorgUserId=true;
    					};
    				});
    				if (!foundorgUserId) {
    					if (debug)
    						$log.debug('BulkUserModalCtrl::evalUserCheckResults: NO match on orgUserId ' + uploadRow.orgUserId);
    					uploadRow.status = 'Invalid orgUserId';
    				}
    			}); // foreach
    		}; // evalUserCheckResults

            /**
    		 * Builds and returns an array of promises to invoke the getUserAppRoles
    		 * service for each unique Org User in the input file.
    		 * Each promise creates an update to be sent to the remote application
    		 * with all role names.
    		 * Reads scope variable uploadFile, which must be sorted by Org User.
    		 * The promise function writes to closure variable appUserRolesRequest
    		 */
    		$scope.buildAppRoleChecks = () => {
    			if (debug)
    				$log.debug('BulkUserModalCtrl::buildAppRoleChecks: uploadFile length is ' + $scope.uploadFile.length); 
    			appUserRolesRequest = [];
    			let appId = $scope.selectedApplication.id;
    			let promises = [];
    			let prevRow = null;
    			$scope.uploadFile.forEach( function (uploadRow) {
    				if (uploadRow.status) {
    					if (debug)
    						$log.debug('BulkUserModalCtrl::buildAppRoleChecks: skip row ' + uploadRow.line);
    					return;
    				}
    				// Because the input is sorted, generate only one request for each Org User
    				if (prevRow == null || prevRow.orgUserId.toLowerCase() !== uploadRow.orgUserId.toLowerCase()) {
            			 if (debug)
            				 $log.debug('BulkUserModalCtrl::buildAppRoleChecks: create request for orgUserId ' + uploadRow.orgUserId);
            			 let appPromise = usersService.getUserAppRoles(appId, uploadRow.orgUserId,true).promise().then( (userAppRolesResult) => {
            				 // Reply for unknown user has all defined roles with isApplied=false on each.  
            				 if (typeof userAppRolesResult[0] !== "undefined") {
            					 if (debug)
            						 $log.debug('BulkUserModalCtrl::buildAppRoleChecks: adding result ' 
            								 + JSON.stringify(userAppRolesResult));
            					 appUserRolesRequest.push({
            						 orgUserId: uploadRow.orgUserId,
            						 userAppRoles: userAppRolesResult                    					 
            					 });
            				 } else {
            					 $log.error('BulkUserModalCtrl::buildAppRoleChecks: getUserAppRoles returned ' + JSON.stringify(userAppRolesResult));
            				 };
            			 }, function(error){
            				 $log.error('BulkUserModalCtrl::buildAppRoleChecks: getUserAppRoles failed ', error);
            			 });
            			 promises.push(appPromise);
    				} else {
            			 if (debug)
            				 $log.debug('BulkUserModalCtrl::buildAppRoleChecks: duplicate orgUserId, skip: '+ uploadRow.orgUserId);
            		 }
            		 prevRow = uploadRow;
            	 }); // foreach
    			return promises;
    		}; // buildAppRoleChecks
    		
    		/**
    		 * Evaluates the result set returned by the app service and adjusts 
    		 * the list of updates to be sent to the remote application by setting
    		 * isApplied=true for each role name found in the upload file.
    		 * Reads and writes scope variable uploadFile.
    		 * Reads closure variable appUserRolesRequest.
    		 */
    		$scope.evalAppRoleCheckResults = () => {
    			if (debug)
    				$log.debug('BulkUserModalCtrl::evalAppRoleCheckResults: uploadFile length is ' + $scope.uploadFile.length);
    			$scope.uploadFile.forEach(function (uploadRow) {
    				if (uploadRow.status) {
    					if (debug)
    						$log.debug('BulkUserModalCtrl::evalAppRoleCheckResults: skip row ' + uploadRow.line);
    					return;
    				}
    				// Search for the match in the app-user-roles array
    				appUserRolesRequest.forEach( function (appUserRoleObj) {
    					if (uploadRow.orgUserId.toLowerCase() === appUserRoleObj.orgUserId.toLowerCase()) {
    						if (debug)
    							$log.debug('BulkUserModalCtrl::evalAppRoleCheckResults: match on orgUserId ' + uploadRow.orgUserId);
    						let roles = appUserRoleObj.userAppRoles;
    						roles.forEach(function (appRoleItem) {
    							//if (debug)
    							//	$log.debug('BulkUserModalCtrl::evalAppRoleCheckResults: checking uploadRow.role='
    							//			+ uploadRow.role + ', appRoleItem.roleName= ' + appRoleItem.roleName);
    							if (uploadRow.role === appRoleItem.roleName) {
    								if (appRoleItem.isApplied) {
    									if (debug)
    										$log.debug('BulkUserModalCtrl::evalAppRoleCheckResults: existing role ' 
    											+ appRoleItem.roleName);
    									uploadRow.status = 'Role exists';
    								}
    								else {
    									if (debug)
    										$log.debug('BulkUserModalCtrl::evalAppRoleCheckResults: new role ' 
    											+ appRoleItem.roleName);
    									// After much back-and-forth I decided a clear indicator
    									// is better than blank in the table status column.
    									uploadRow.status = 'OK';
    									appRoleItem.isApplied = true;
    								}
    								// This count is not especially interesting.
    								// numberUserRolesSucceeded++;
    							}
    						}); // for each role
    					}
    				}); // for each result		
    			}); // for each row
    		}; // evalAppRoleCheckResults
             
    		/**
    		 * Sends requests to Portal requesting user role assignment.
    		 * That endpoint handles creation of the user at the remote app if necessary.
    		 * Reads closure variable appUserRolesRequest.
    		 * Invoked by the Next button on the confirmation dialog.
    		 */
    		$scope.updateDB = () => {
    			$scope.isProcessing = true;
    			$scope.progressMsg = 'Sending requests to application..';
    			if (debug)
    				$log.debug('BulkUserModalCtrl::updateDB: request length is ' + appUserRolesRequest.length);
    			var numberUsersSucceeded = 0;
    			let promises = [];
    			appUserRolesRequest.forEach(function(appUserRoleObj) {
    				if (debug) 
    					$log.debug('BulkUserModalCtrl::updateDB: appUserRoleObj is ' + JSON.stringify(appUserRoleObj));
                     let updateRequest = {
                    		 orgUserId: appUserRoleObj.orgUserId, 
                    		 appId: $scope.selectedApplication.id, 
                    		 appRoles: appUserRoleObj.userAppRoles
                     };
                     if (debug)
                    	 $log.debug('BulkUserModalCtrl::updateDB: updateRequest is ' + JSON.stringify(updateRequest));
                     let updatePromise = usersService.updateUserAppRoles(updateRequest).promise().then(res => {
                    	 if (debug)
                    		 $log.debug('BulkUserModalCtrl::updateDB: updated successfully: ' + JSON.stringify(res));
                    	 numberUsersSucceeded++;
                     }).catch(err => {
                    	 // What to do if one of many fails??
                    	 $log.error('BulkUserModalCtrl::updateDB failed: ', err);
                    	 confirmBoxService.showInformation(
                    			 'Failed to update the user application roles. ' +
                    			 'Error: ' + err.status).then(isConfirmed => { });
                     }).finally( () => {
                    	 // $log.debug('BulkUserModalCtrl::updateDB: finally()');
                     });
                     promises.push(updatePromise);
            	 }); // for each
    			
            	 // Run all the promises
            	 $q.all(promises).then(function(){
            		 $scope.isProcessing = false;
            		 confirmBoxService.showInformation('Processed ' + numberUsersSucceeded + ' users.').then(isConfirmed => {
            			 // Close the upload-confirm dialog
            			 ngDialog.close();
            		 });
            	 });
             }; // updateDb
             
    		// Sets the variable that hides/reveals the user controls
    		$scope.step2 = () => {
    			this.fileSelected = false;
    			$scope.selectedFile = null;
    			$scope.fileModel = null;
    			this.step1 = false;  			
    		}
    		
             // Navigate between dialog screens using step number: 1,2,...
             $scope.navigateBack = () => {
            	 this.step1 = true;
                 this.fileSelected = false;
             };
             
             // Opens a dialog to show the data to be uploaded.
             // Invoked by the upload button on the bulk user dialog.
             $scope.confirmUpload = () => {
            	// Start the process
            	$scope.readValidateFile();
            	// Dialog shows progress
            	ngDialog.open({
            		templateUrl: 'app/views/users/new-user-dialogs/bulk-user.confirm.html',
            		scope: $scope
            	});
             };

             // Invoked by the Cancel button on the confirmation dialog.
             $scope.cancelUpload = () => {
            	 ngDialog.close();
             };
             
             init();
    	} // constructor
    } // class
    BulkUserModalCtrl.$inject = ['$scope', '$log', '$filter', '$q', 'usersService', 'applicationsService', 'confirmBoxService', 'functionalMenuService', 'ngDialog'];    
    angular.module('ecompApp').controller('BulkUserModalCtrl', BulkUserModalCtrl);

    angular.module('ecompApp').directive('fileChange', ['$parse', function($parse){
    	return {
    		require: 'ngModel',
    	    restrict: 'A',
    	    link : function($scope, element, attrs, ngModel) {
    	    	var attrHandler = $parse(attrs['fileChange']);
    	    	var handler=function(e) {
    	    		$scope.$apply(function() {
    	    			attrHandler($scope, { $event:e, files:e.target.files } );
    	    			$scope.selectedFile = e.target.files[0].name;
    	    		});
    	    	};
    	    	element[0].addEventListener('change',handler,false);
    	   }
    	}
    }]);

    angular.module('ecompApp').filter('csvToObj',function() {
    	return function(input) {
    	    var result = [];
    	    var len, i, line, o;
    		var lines = input.split('\n');
    	    // Need 1-based index below
    	    for (len = lines.length, i = 1; i <= len; ++i) {
    	    	// Use 0-based index for array
    	    	line = lines[i - 1].trim();
   	    		if (line.length == 0) {
   	    			// console.log("Skipping blank line");
   	    			result.push({
   	    				line: i,
   	    				orgUserId: '',
   	    				role: '',
   	    				status: 'Blank line'
   	    			});
   	    			continue;
   	    		}
   	    		o = line.split(',');
   	    		if (o.length !== 2) {
   	    			// other lengths not valid for upload
   	    			result.push({
   	    				line: i,
   	    				orgUserId: line,   
   	    				role: '',
   	    				status: 'Failed to find 2 comma-separated values'
   	    			});
   	    		}
   	    		else {
   	    			// console.log("Valid line: ", val);
   	    			let entry = {
   	    					line: i,
   	    					orgUserId: o[0],
   	    					role: o[1]
   	    					// leave status undefined, this could be valid.
   	    			};
   	    			if (o[0].toLowerCase() === 'orgUserId') {
   	    				// not valid for upload, so set status
   	    				entry.status = 'Header';
   	    			}
   	    			else if (o[0].trim() == '' || o[1].trim() == '') {
   	    				// defend against line with only a single comma etc.
   	    				entry.status = 'Failed to find 2 non-empty values';   	    				
   	    			}
   	    			result.push(entry);
   	    		} // len 2
    	    } // for
    	    return result;
    	};
    });
    
  
        
})();
