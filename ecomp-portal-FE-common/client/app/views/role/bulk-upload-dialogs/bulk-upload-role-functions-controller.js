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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
/**
 * bulk upload role-functions controller
 */
'use strict';
(function () {
    class BulkRoleAndFunctionsModalCtrl {
    	constructor($scope, $log, $filter, $q, $modalInstance, $modal, ngDialog, message, confirmBoxService, usersService, applicationsService, functionalMenuService, RoleService) {
    		// Set to true for copious console output
    		var debug = false;
    		// Roles fetched from Role service
    		var appRoleFuncsResult = [];
    		// Functions fetched from Role service
    		var appFunctionsResult = [];
    		// Global  roles fetched from Role service
    		var appGlobalRolesResult = [];
    		
    		var appId = message.appid;
    		
   		 $scope.ngRepeatBulkUploadOptions = [
		        {id: '1', title: 'Functions', value: 'functions'},
		        {id: '2', title: 'Roles', value: 'roles'},
		        {id: '3', title: 'Role Functions', value: 'roleFunctions'},
		        {id: '4', title: 'Global Role Functions', value: 'globalRoleFunctions'}
		    ];
		 
		 $scope.selectedUploadType =   $scope.ngRepeatBulkUploadOptions[0];
		 $scope.UploadTypeInstruction = "Function Type, Function Instance, Function Action, Function Name";
		 $scope.changeUploadTypeInstruction = function(typeInstrc){
			 switch(typeInstrc) {
			    case 'functions':
			    	$scope.UploadTypeInstruction = "Function Type, Function Instance, Function Action, Function Name";
			        break;
			    case 'roles':
			    	$scope.UploadTypeInstruction = "Role Name, Priority (Optional)";
			        break;
			    case 'roleFunctions':
			    	$scope.UploadTypeInstruction = "Role Name, Function Type, Function Instance, Function Action, Function Name";
			        break;
			    default:
			    	$scope.UploadTypeInstruction = "Global Role Name, Function Type, Function Instance, Function Action, Function Name";
			}
		 };
    		
    		let init = () => {
    			if (debug)
    				$log.debug('BulkRoleAndFunctionsModalCtrl::init');
    			// Angular insists on this.
    			$scope.fileModel = {};
    			// Enable modal controls
    			this.step1 = true;
    			
    			this.fileSelected = false; 			
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
    		
    		// Caches the file name supplied by the event handler.
    		$scope.fileChangeHandler = (event, files) => {
    			this.fileSelected = true;
    			this.fileToRead = files[0];
    			if (debug)
    				$log.debug("BulkRoleAndFunctionsModalCtrl::fileChangeHandler: file is ", this.fileToRead);
    		}; // file change handler
    		
    		/**
		 * Reads the contents of the file, calls portal endpoints to
		 * validate roles, userIds and existing role assignments;
		 * ultimately builds array of requests to be sent. Creates scope
		 * variable with input file contents for communication with
		 * functions.
		 * 
		 * This function performs a synchronous step-by-step process
		 * using asynchronous promises. The code could all be inline
		 * here but the nesting becomes unwieldy.
		 */
    		$scope.readValidateFile = (typeUpload) => {
    			$scope.isProcessing = true;
    			$scope.progressMsg = 'Reading upload file...';
    			var reader = new FileReader();
    			reader.onload = function(event) {
    				if(typeUpload === 'roles'){
    					$scope.uploadFile = $filter('csvToRoleObj')(reader.result);
    	    				if (debug){
        					$log.debug('BulkRoleAndFunctionsModalCtrl::readValidateFile onload: data length is ' + $scope.uploadFile.length);
    	    				}
        				$scope.progressMsg = 'Fetching & validating application roles...';
								// fetch app roles
		    					RoleService.getRoles(appId).then(function (appRoles){
		    						if (debug){
			                    		$log.debug("BulkRoleAndFunctionsModalCtrl::readValidateFile: getRoles returned " + JSON.stringify(appFunctions.data));
		        	    				}
									let availableRolesList = JSON.parse(appRoles.data);
									appRoleFuncsResult = availableRolesList.availableRoles;
		    						$scope.evalAppRolesCheckResults();
		    						// Re sort by line for the confirmation dialog
			    	    			$scope.uploadFile.sort(getSortOrder('line', false));
			    	    			// We're done, confirm box may show the  table
			    	    			if (debug)
			    	    					$log.debug('BulkRoleAndFunctionsModalCtrl::readValidateFile inner-then ends');
			    	    			$scope.progressMsg = 'Done.';
			    	    			$scope.isProcessing = false;
		    					}, function(error) {
		                        	$log.error('BulkUserModalCtrl::readValidateFile: failed retrieving app roles info');
		                        	$scope.isProcessing = false;
		                        });
    				} else if (typeUpload === 'roleFunctions'){
    					$scope.uploadFile = $filter('csvToRoleFuncObj')(reader.result);
    	    				if (debug){
        					$log.debug('BulkRoleAndFunctionsModalCtrl::readValidateFile onload: data length is ' + $scope.uploadFile.length);
    	    				}
        				$scope.progressMsg = 'Fetching & validating application role functions...';
    					//fetch app functions
        				RoleService.getRoleFunctionList(appId).then(function (appFunctions){
    						if (debug)
	                    		$log.debug("BulkRoleAndFunctionsModalCtrl::readValidateFile: getRoleFunctionList returned " + JSON.stringify(appFunctions.data));
								let availableRoleFunctionsList = JSON.parse(appFunctions.data);
								appFunctionsResult = availableRoleFunctionsList.availableRoleFunctions;
								// fetch app roles
		    					RoleService.getRoles(appId).then(function (appRoles){
		    						if (debug){
			                    		$log.debug("BulkRoleAndFunctionsModalCtrl::readValidateFile: getRoles returned " + JSON.stringify(appFunctions.data));
		        	    				}
									let availableRolesList = JSON.parse(appRoles.data);
									appRoleFuncsResult = availableRolesList.availableRoles;
		    						$scope.evalAppRoleFuncsCheckResults();
		    						// Re sort by line for the confirmation dialog
			    	    			$scope.uploadFile.sort(getSortOrder('line', false));
			    	    			// We're done, confirm box may show the  table
			    	    			if (debug)
			    	    					$log.debug('BulkRoleAndFunctionsModalCtrl::readValidateFile inner-then ends');
			    	    			$scope.progressMsg = 'Done.';
			    	    			$scope.isProcessing = false;
		    					}, function(error) {
		                        	$log.error('BulkUserModalCtrl::readValidateFile: failed retrieving app roles info');
		                        	$scope.isProcessing = false;
		                        });
    					},
                        function(error) {
                        	$log.error('BulkUserModalCtrl::readValidateFile: failed retrieving app functions info');
                        	$scope.isProcessing = false;
                        }
                        );
    				} else if(typeUpload === 'functions'){
    					$scope.uploadFile = $filter('csvToFuncObj')(reader.result);
    	    				if (debug){
        					$log.debug('BulkRoleAndFunctionsModalCtrl::readValidateFile onload: data length is ' + $scope.uploadFile.length);
    	    				}
    	    				$scope.progressMsg = 'Fetching & validating the application functions...';
    	    				// fetch app functions
    	    				RoleService.getRoleFunctionList(appId).then(function (appFunctions){
    						if (debug)
	                    		$log.debug("BulkRoleAndFunctionsModalCtrl::readValidateFile: getRoleFunctionList returned " + JSON.stringify(appFunctions.data));
								let availableRoleFunctionsList = JSON.parse(appFunctions.data);
								appFunctionsResult = availableRoleFunctionsList.availableRoleFunctions;
    	           				$scope.verifyFunctions();
    	           				$scope.evalAppFunctionsCheckResults();
    	    	    			// Re sort by line for the confirmation dialog
    	    	    			$scope.uploadFile.sort(getSortOrder('line', false));
    	    	    			// We're done, confirm box may show the  table
    	    	    			if (debug)
    	    	    					$log.debug('BulkRoleAndFunctionsModalCtrl::readValidateFile inner-then ends');
    	    	    			$scope.progressMsg = 'Done.';
    	    	    			$scope.isProcessing = false;
    					},
                        function(error) {
                        	$log.error('BulkUserModalCtrl::readValidateFile: failed retrieving app functions info');
                        	$scope.isProcessing = false;
                        }
                        );
    				} else if(typeUpload === 'globalRoleFunctions'){
    					$scope.uploadFile = $filter('csvToRoleFuncObj')(reader.result);
    	    				if (debug){
        					$log.debug('BulkRoleAndFunctionsModalCtrl::readValidateFile onload: data length is ' + $scope.uploadFile.length);
    					}
    	    				$scope.progressMsg = 'Fetching application global role functions...';
    	    				//fetch app functions
            				RoleService.getRoleFunctionList(appId).then(function (appFunctions){
        						if (debug)
    	                    		$log.debug("BulkRoleAndFunctionsModalCtrl::readValidateFile: getRoleFunctionList returned " + JSON.stringify(appFunctions.data));
    								let availableRoleFunctionsList = JSON.parse(appFunctions.data);
    								appFunctionsResult = availableRoleFunctionsList.availableRoleFunctions;
    								// fetch app roles
    		    					RoleService.getRoles(appId).then(function (appRoles){
    		    						if (debug){
    			                    		$log.debug("BulkRoleAndFunctionsModalCtrl::readValidateFile: getRoles returned " + JSON.stringify(appFunctions.data));
    		        	    				}
    									let availableRolesList = JSON.parse(appRoles.data);
    									appRoleFuncsResult = availableRolesList.availableRoles;
    									appRoleFuncsResult.forEach(function(appRole) {
    										if(appRole.name.toLowerCase().startsWith("global_")){
    											appGlobalRolesResult.push(appRole);
    										}
										});
    		    						$scope.evalAppRoleFuncsCheckResults(typeUpload);
    		    						// Re sort by line for the confirmation dialog
    			    	    			$scope.uploadFile.sort(getSortOrder('line', false));
    			    	    			// We're done, confirm box may show the  table
    			    	    			if (debug)
    			    	    					$log.debug('BulkRoleAndFunctionsModalCtrl::readValidateFile inner-then ends');
    			    	    			$scope.progressMsg = 'Done.';
    			    	    			$scope.isProcessing = false;
    		    					}, function(error) {
    		                        	$log.error('BulkUserModalCtrl::readValidateFile: failed retrieving app roles info');
    		                        	$scope.isProcessing = false;
    		                        });
        					},
                            function(error) {
                            	$log.error('BulkUserModalCtrl::readValidateFile: failed retrieving app functions info');
                            	$scope.isProcessing = false;
                            }
                            );
    				}

    			} // onload
    			
    			// Invoke the reader on the selected file
    			reader.readAsText(this.fileToRead);
    		}; 
    		
    		/**
		 * Evaluates the result set returned by the role service.
		 * Sets an uploadFile array element status if a functions is not
		 * defined. Reads and writes scope variable uploadFile. Reads
		 * closure variable appFunctionsResult.
		 */
    		$scope.verifyFunctions = () => {
    			if (debug)
    				$log.debug('BulkRoleAndFunctionsModalCtrl::verifyFunctions: appFunctions is ' + JSON.stringify(appFunctionsResult));
    			// check functions in upload file against defined app functions
    			$scope.uploadFile.forEach( function (uploadRow) {
    				// skip rows that already have a defined status: headers etc.
    				if (uploadRow.status) {
    					if (debug)
    						$log.debug('BulkRoleAndFunctionsModalCtrl::verifyFunctions: skip row ' + uploadRow.line);
    					return;
    				}
    				for (var i=0; i < appFunctionsResult.length; i++) {
    					if (uploadRow.type.toUpperCase() === appFunctionsResult[i].type.toUpperCase()
    						&& uploadRow.instance.toUpperCase() === appFunctionsResult[i].code.toUpperCase()
    						&& uploadRow.action.toUpperCase() === appFunctionsResult[i].action.toUpperCase()) {
    						if (debug)
    							$log.debug('BulkRoleAndFunctionsModalCtrl::verifyFunctions: match on function ' + uploadRow.type,
    								uploadRow.instance, uploadRow.type,  uploadRow.type);
    						break;
    					}
    				}
    			}); // foreach
    		}; // verifyFunctions
    		
    	/**
		 * Evaluates the result set of existing functions returned by 
		 * the Roleservice and list of functions found in the upload file. 
		 * Reads and writes scope variable uploadFile. 
		 * Reads closure variable appFunctionsResult.
		 */
    		$scope.evalAppFunctionsCheckResults = () => {
    			if (debug)
    				$log.debug('BulkRoleAndFunctionsModalCtrl::evalAppFunctionsCheckResults: uploadFile length is ' + $scope.uploadFile.length);
    			$scope.uploadFile.forEach(function (uploadRow) {
    				if (uploadRow.status) {
    					if (debug)
    						$log.debug('BulkRoleAndFunctionsModalCtrl::evalAppFunctionsCheckResults: skip row ' + uploadRow.line);
    					return;
    				}
    				// Search for the match in the app-functions
				// array
    				let isFunctionExist = false;
    				appFunctionsResult.forEach( function (exixtingFuncObj) {
    							if (uploadRow.type.toUpperCase() === exixtingFuncObj.type.toUpperCase()
    								&& uploadRow.instance.toUpperCase() === exixtingFuncObj.code.toUpperCase()
    								&& uploadRow.action.toUpperCase() === exixtingFuncObj.action.toUpperCase()) {
								uploadRow.status = 'Function exits!';
								uploadRow.isCreate = false;
								isFunctionExist = true;
    							}
    				}); // for each result
    				if(!isFunctionExist) {
    					if(/[^a-zA-Z0-9\-\.\_]/.test(uploadRow.type) 
    							|| (uploadRow.action !== '*' 
    							&& /[^a-zA-Z0-9\-\.\_]/.test(uploadRow.action))
    							|| /[^a-zA-Z0-9\-\:\_\./*]/.test(uploadRow.instance)
    							|| /[^a-zA-Z0-9\-\_ \.]/.test(uploadRow.name)){
    						uploadRow.status = 'Invalid function';
    						uploadRow.isCreate = false;
    					} else {
    						if (debug){
    							$log.debug('BulkRoleAndFunctionsModalCtrl::evalAppFunctionsCheckResults: new function ' 
									+ uploadRow);
    						}
							// After much back-and-forth I decided a clear  indicator is better than blank in the table  status column.
    						uploadRow.status = 'Create';
    						uploadRow.isCreate = true;
    					}
					}
    			}); // for each row
    		}; // evalAppFunctionsCheckResults
    		
    		/**
    		 * Evaluates the result set of existing roles returned by 
    		 * the Roleservice and list of roles found in the upload file. 
    		 * Reads and writes scope variable uploadFile. 
    		 * Reads closure variable appRolesResult.
    		 */
        		$scope.evalAppRolesCheckResults = () => {
        			if (debug)
        				$log.debug('BulkRoleAndFunctionsModalCtrl::evalAppRolesCheckResults: uploadFile length is ' + $scope.uploadFile.length);
        			$scope.uploadFile.forEach(function (uploadRow) {
        				if (uploadRow.status) {
        					if (debug)
        						$log.debug('BulkRoleAndFunctionsModalCtrl::evalAppRolesCheckResults: skip row ' + uploadRow.line);
        					return;
        				}
        				// Search for the match in the app-roles
    				// array
        				let isRoleExist = false;
        				appRoleFuncsResult.forEach( function (existingRoleObj) {
        							if (uploadRow.role.toUpperCase() === existingRoleObj.name.toUpperCase()) {
    								uploadRow.status = 'Role exits!';
    								uploadRow.isCreate = false;
    								isRoleExist = true;
        							}
        				}); // for each result
        				if(!isRoleExist) {
        					if(/[^a-zA-Z0-9\-\_ \.\/]/.test(uploadRow.role) ||
        							uploadRow.role.toLowerCase().startsWith("global_")){
        						uploadRow.status = 'Invalid role!';
        						uploadRow.isCreate = false;
        					} else {
        						if (debug){
        							$log.debug('BulkRoleAndFunctionsModalCtrl::evalAppRolesCheckResults: new function ' 
    									+ uploadRow);
        						}
    							// After much back-and-forth I decided a clear  indicator is better than blank in the table  status column.
        						uploadRow.status = 'Create';
        						uploadRow.isCreate = true;
        					}
    					}
        			}); // for each row
        		}; // evalAppRolesCheckResults
        		
        		/**
        		 * Evaluates the result set of existing roles returned by 
        		 * the Roleservice and list of roles found in the upload file. 
        		 * Reads and writes scope variable uploadFile. 
        		 * Reads closure variable appRolesResult.
        		 */
            		$scope.evalAppRoleFuncsCheckResults = (typeUpload) => {
            			if (debug)
            				$log.debug('BulkRoleAndFunctionsModalCtrl::evalAppRoleFuncsCheckResults: uploadFile length is ' + $scope.uploadFile.length);
            			$scope.uploadFile.forEach(function (uploadRow) {
            				if (uploadRow.status) {
            					if (debug)
            						$log.debug('BulkRoleAndFunctionsModalCtrl::evalAppRoleFuncsCheckResults: skip row ' + uploadRow.line);
            					return;
            				}
            				// Search for the match in the app-functions array
            				let isValidFunc = false;
            				appFunctionsResult.forEach(function (existingFuncObj){
            					if(uploadRow.type.toUpperCase() === existingFuncObj.type.toUpperCase()
        								&& uploadRow.instance.toUpperCase() === existingFuncObj.code.toUpperCase()
        								&& uploadRow.action.toUpperCase() === existingFuncObj.action.toUpperCase()
        								&& uploadRow.name.toUpperCase() === existingFuncObj.name.toUpperCase()){
            						isValidFunc = true;
    							}
            				});
            				
            				let isValidRole = false;
            				let isRoleFuncExist = false;
            				if(typeUpload === 'globalRoleFunctions'){
    	        				// Search for the match in the app-role array
            					appGlobalRolesResult.forEach( function (existingRoleObj) {
    	        							if (uploadRow.role.toUpperCase() === existingRoleObj.name.toUpperCase()) {
    	        								isValidRole = true;
    	        								if(isValidFunc){
    	        									existingRoleObj.roleFunctions.forEach(function (existingRoleFuncObj){
    	        										if(uploadRow.type.toUpperCase() === existingRoleFuncObj.type.toUpperCase()
    	            		    								&& uploadRow.instance.toUpperCase() === existingRoleFuncObj.code.toUpperCase()
    	            		    								&& uploadRow.action.toUpperCase() === existingRoleFuncObj.action.toUpperCase()){
    	        											isRoleFuncExist = true;
    	            									}
    	        									});
    	        								}
    	        							}
    	        				}); // for each result
            				} else {
                				// Search for the match in the app-role array
                				appRoleFuncsResult.forEach( function (existingRoleObj) {
                							if (uploadRow.role.toUpperCase() === existingRoleObj.name.toUpperCase()) {
                								isValidRole = true;
                								if(isValidFunc){
                									existingRoleObj.roleFunctions.forEach(function (existingRoleFuncObj){
                										if(uploadRow.type.toUpperCase() === existingRoleFuncObj.type.toUpperCase()
                    		    								&& uploadRow.instance.toUpperCase() === existingRoleFuncObj.code.toUpperCase()
                    		    								&& uploadRow.action.toUpperCase() === existingRoleFuncObj.action.toUpperCase()){
                											isRoleFuncExist = true;
                    									}
                									});
                								}	
                							}
                				}); // for each result
            				}
            				
            			uploadRow.isCreate = false;
            			if(typeUpload === 'globalRoleFunctions' && (!isValidRole || !isValidFunc)){
            				uploadRow.status = 'Invalid global role function!';
            			} else if(typeUpload !== 'globalRoleFunctions' && (!isValidRole || !isValidFunc)){
            				uploadRow.status = 'Invalid role function!';
            			} else if(typeUpload === 'globalRoleFunctions' && !isRoleFuncExist) {
            				uploadRow.status = 'Add global role function!';
    						uploadRow.isCreate = true;
            			} else if(typeUpload !== 'globalRoleFunctions' && !isRoleFuncExist){
            				uploadRow.status = 'Add role function!';
            				uploadRow.isCreate = true;
            			} else if(typeUpload === 'globalRoleFunctions'){
            				uploadRow.status = 'Global role function exists!';
            			} else {
            				uploadRow.status = 'Role function exists!';
            			}
            				
            			}); // for each row
            		}; // evalAppRolesCheckResults
            		
    		
    	/**
    	 * Sends requests to Portal BE requesting application functions assignment.
		 * That endpoint handles creation of the application functions in the 
		 * external auth system if necessary. Reads closure variable appFunctionsResult.
		 * Invoked by the Next button on the confirmation dialog.
		 */
    		$scope.updateFunctionsInDB = () => {
    			$scope.isProcessing = true;
    			$scope.progressMsg = 'Sending requests to application..';
    			if (debug)
    				$log.debug('BulkRoleAndFunctionsModalCtrl::updateFunctionsInDB: request length is ' + appUserRolesRequest.length);
    			var numberFunctionsSucceeded = 0;
    			let promises = [];
    			$scope.uploadFile.forEach(function(appFuncPostData) {
    				if (debug) 
    					$log.debug('BulkRoleAndFunctionsModalCtrl::updateFunctionsInDB: appFuncPostData is ' + JSON.stringify(appFuncPostData));
    				let updateFunctionsFinalPostData = {
                             		 type: appFuncPostData.type, 
                               		 code: appFuncPostData.instance, 
                               		 action: appFuncPostData.action,
                               		 name: appFuncPostData.name
                        };
                     if (debug)
                    	 $log.debug('BulkRoleAndFunctionsModalCtrl::updateFunctionsInDB: updateFunctionsFinalPostData is ' + JSON.stringify(updateFunctionsFinalPostData));
                     let updatePromise = {};
                     if(appFuncPostData.isCreate){
                     updatePromise = functionalMenuService.saveBulkFunction(appId, updateFunctionsFinalPostData).promise().then(res => {
                    	 if (debug)
                    		 $log.debug('BulkRoleAndFunctionsModalCtrl::updateFunctionsInDB: updated successfully: ' + JSON.stringify(res));
                    	 numberFunctionsSucceeded++;
                     }).catch(err => {
                    	 // What to do if one of many fails??
                    	 $log.error('BulkRoleAndFunctionsModalCtrl::updateFunctionsInDB failed: ', err);
                    	 confirmBoxService.showInformation(
                    			 'Failed to update the application functions. ' +
                    			 'Error: ' + err.status).then(isConfirmed => { });
                     }).finally( () => {
                     });
                 	}
                    promises.push(updatePromise);
            	 }); // for each
    			
            	 // Run all the promises
            	 $q.all(promises).then(function(){
            		 $scope.isProcessing = false;
            		 confirmBoxService.showInformation('Processed ' + numberFunctionsSucceeded + ' records.').then(isConfirmed => {
            			 // Close the upload-confirm dialog
            			 ngDialog.close();
            		 });
            	 });
             }; // updateFunctionsInDB
             
             /**
         	 * Sends requests to Portal BE requesting application functions assignment.
     		 * That endpoint handles creation of the application role in the 
     		 * external auth system if necessary. Reads closure variable appRoleFuncResult.
     		 * Invoked by the Next button on the confirmation dialog.
     		 */
         		$scope.updateRolesInDB = () => {
         			$scope.isProcessing = true;
         			$scope.progressMsg = 'Sending requests to application..';
         			if (debug)
         				$log.debug('BulkRoleAndFunctionsModalCtrl::updateRolesInDB: request length is ' + appUserRolesRequest.length);
         			var numberRolesSucceeded = 0;
         			let promises = [];
         			$scope.uploadFile.forEach(function(appRolePostData) {
    					let priority = parseInt(appRolePostData.priority);
         				if (debug) 
         					$log.debug('BulkRoleAndFunctionsModalCtrl::updateRolesInDB: appRolePostData is ' + JSON.stringify(appFuncPostData));
         				let uplaodRolePostData = "";
         				if(isNaN(priority)){
                           		uplaodRolePostData = {
                                		 name: appRolePostData.role,
                                  		 active: true,
                					}
         				} else {
         					uplaodRolePostData = {
                            		 name: appRolePostData.role, 
                              		 priority: appRolePostData.priority, 
                              		 active: true,
         					}
         				}
	         				var postData = {
	         						role: uplaodRolePostData,
	         						roleFunctions: [],
	         						childRoles: []
	         					}
                          if (debug)
                         	 $log.debug('BulkRoleAndFunctionsModalCtrl::updateRolesInDB: uplaodRoleFinalPostData is ' + JSON.stringify(uplaodRoleFinalPostData));
                          let updatePromise = {};
                          if(appRolePostData.isCreate){
                          updatePromise = functionalMenuService.saveBulkRole(appId, JSON.stringify(postData)).promise().then(res => {
                         	 if (debug)
                         		 $log.debug('BulkRoleAndFunctionsModalCtrl::updateRolesInDB: updated successfully: ' + JSON.stringify(res));
                         	numberRolesSucceeded++;
                          }).catch(err => {
                         	 // What to do if one of many fails??
                         	 $log.error('BulkRoleAndFunctionsModalCtrl::updateRolesInDB failed: ', err);
                         	 confirmBoxService.showInformation(
                         			 'Failed to update the application role. ' +
                         			 'Error: ' + err.status).then(isConfirmed => { });
                          }).finally( () => {
                          });
                      	}
                         promises.push(updatePromise);
                 	 }); // for each
         			
                 	 // Run all the promises
                 	 $q.all(promises).then(function(){
                 		 $scope.isProcessing = false;
                 		 confirmBoxService.showInformation('Processed ' + numberRolesSucceeded + ' records. Please sync roles').then(isConfirmed => {
                 			 // Close the upload-confirm dialog
                 			 ngDialog.close();
                 		 });
                 	 });
                  }; // updateRolesInDB
                  
         	/**
     		 * Sends requests to Portal BE requesting role function assignment.
     		 * That endpoint handles adding role function in the external auth system
     		 * if necessary.Invoked by the Next button on the confirmation dialog.
     		 */
         		$scope.updateRoleFunctionsInDB = () => {
         			$scope.isProcessing = true;
        			$scope.progressMsg = 'Sending requests to application..';
        			if (debug)
        				$log.debug('BulkRoleAndFunctionsModalCtrl::updateRoleFunctionsInDB: request length is ' + appUserRolesRequest.length);
        			var numberRoleFunctionSucceeded = 0;
        			let promises = [];
        			$scope.uploadFile.forEach(function(appRoleFuncPostData) {
        				if (debug) 
        					$log.debug('BulkRoleAndFunctionsModalCtrl::updateRoleFunctionsInDB: appRoleFuncPostData is ' + JSON.stringify(appFuncPostData));
        				let updateRoleFunctionFinalPostData = {
                                 		 roleName: appRoleFuncPostData.role,
                                   		 type: appRoleFuncPostData.type, 
                                   		 instance: appRoleFuncPostData.instance, 
                                   		 action: appRoleFuncPostData.action,
                                   		 name: appRoleFuncPostData.name,
                                   		 isGlobalRolePartnerFunc: false
                            };
                         if (debug)
                        	 $log.debug('BulkRoleAndFunctionsModalCtrl::updateRoleFunctionsInDB: updateRoleFunctionFinalPostData is ' + JSON.stringify(updateFunctionsFinalPostData));
                         let updatePromise = {};
                         if(appRoleFuncPostData.isCreate){
                         updatePromise = functionalMenuService.updateBulkRoleFunction(appId, updateRoleFunctionFinalPostData).promise().then(res => {
                        	 if (debug)
                        		 $log.debug('BulkRoleAndFunctionsModalCtrl::updateRoleFunctionsInDB: updated successfully: ' + JSON.stringify(res));
                        	 numberRoleFunctionSucceeded++;
                         }).catch(err => {
                        	 // What to do if one of many fails??
                        	 $log.error('BulkRoleAndFunctionsModalCtrl::updateRoleFunctionsInDB failed: ', err);
                        	 confirmBoxService.showInformation(
                        			 'Failed to update the application role function. ' +
                        			 'Error: ' + err.status).then(isConfirmed => { });
                         }).finally( () => {
                         });
                     	}
                        promises.push(updatePromise);
                	 }); // for each
        			
                	 // Run all the promises
                	 $q.all(promises).then(function(){
                		 $scope.isProcessing = false;
                		 confirmBoxService.showInformation('Processed ' + numberRoleFunctionSucceeded + ' records. Please sync roles to reflect in portal').then(isConfirmed => {
                			 // Close the upload-confirm dialog
                			 ngDialog.close();
                		 });
                	 });
                  }; // updateRoleFunctionsInDB
                  
              	/**
          		 * Sends requests to Portal requesting global role functions assignment.
          		 * That endpoint handles updating global role functions in the external auth system
          		 * if necessary. Invoked by the Next button on the confirmation dialog.
          		 */
              		$scope.updateGlobalRoleFunctionsInDB = () => {
              			$scope.isProcessing = true;
              			$scope.progressMsg = 'Sending requests to application..';
              			if (debug)
              				$log.debug('BulkRoleAndFunctionsModalCtrl::updateGlobalRoleFunctionsInDB: request length is ' + appUserRolesRequest.length);
              			var numberGlobalRoleFunctionSucceeded = 0;
              			let promises = [];
              			$scope.uploadFile.forEach(function(appRoleFuncPostData) {
            				if (debug) 
            					$log.debug('BulkRoleAndFunctionsModalCtrl::updateGlobalRoleFunctionsInDB: appRoleFuncPostData is ' + JSON.stringify(appFuncPostData));
            				let updateGlobalRoleFunctionFinalPostData = {
                                     		 roleName: appRoleFuncPostData.role,
                                       		 type: appRoleFuncPostData.type, 
                                       		 instance: appRoleFuncPostData.instance, 
                                       		 action: appRoleFuncPostData.action,
                                       		 name: appRoleFuncPostData.name,
                                       		 isGlobalRolePartnerFunc: true
                                };
                             if (debug)
                            	 $log.debug('BulkRoleAndFunctionsModalCtrl::updateGlobalRoleFunctionsInDB: updateRoleFunctionFinalPostData is ' + JSON.stringify(updateFunctionsFinalPostData));
                             let updatePromise = {};
                             if(appRoleFuncPostData.isCreate){
                             updatePromise = functionalMenuService.updateBulkRoleFunction(appId, updateGlobalRoleFunctionFinalPostData).promise().then(res => {
                            	 if (debug)
                            		 $log.debug('BulkRoleAndFunctionsModalCtrl::updateGlobalRoleFunctionsInDB: updated successfully: ' + JSON.stringify(res));
                            	 numberGlobalRoleFunctionSucceeded++;
                             }).catch(err => {
                            	 // What to do if one of many fails??
                            	 $log.error('BulkRoleAndFunctionsModalCtrl::updateGlobalRoleFunctionsInDB failed: ', err);
                            	 confirmBoxService.showInformation(
                            			 'Failed to update the global role partner function. ' +
                            			 'Error: ' + err.status).then(isConfirmed => { });
                             }).finally( () => {
                             });
                         	}
                            promises.push(updatePromise);
                    	 }); // for each
              			
                      	 // Run all the promises
                      	 $q.all(promises).then(function(){
                      		 $scope.isProcessing = false;
                      		 confirmBoxService.showInformation('Processed ' + numberGlobalRoleFunctionSucceeded + ' records. Please sync roles to reflect in portal').then(isConfirmed => {
                      			 // Close the upload-confirm dialog
                      			 ngDialog.close();
                      		 });
                      	 });
                       }; // updateGlobalRoleFunctionsInDB
                       
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
             $scope.confirmUpload = (typeUpload) => {
            	// Start the process
            	$scope.readValidateFile(typeUpload);
            	// Dialog shows progress
            	if(typeUpload === 'functions'){
                   	$modal.open({
                    	templateUrl: 'app/views/role/bulk-upload-dialogs/bulk-upload-functions-confirm.html',
                        controller: '',
                        sizeClass: 'modal-medium', 
                        resolve:'',
                        scope: $scope
                    })
            	} else if(typeUpload === 'roleFunctions'){
                	$modal.open({
                		templateUrl: 'app/views/role/bulk-upload-dialogs/bulk-upload-role-functions-confirm.html',
                        controller: '',
                        sizeClass: 'modal-medium', 
                        resolve:'',
                        scope: $scope
                    })
            		
            	} else if(typeUpload === 'roles'){
                   	$modal.open({
                    	templateUrl: 'app/views/role/bulk-upload-dialogs/bulk-upload-roles-confirm.html',
                        controller: '',
                        sizeClass: 'modal-medium', 
                        resolve:'',
                        scope: $scope
                    })
            	} else if(typeUpload === 'globalRoleFunctions'){
                   	$modal.open({
                    	templateUrl: 'app/views/role/bulk-upload-dialogs/bulk-upload-global-role-functions-confirm.html',
                        controller: '',
                        sizeClass: 'modal-medium', 
                        resolve:'',
                        scope: $scope
                    })
            	}
             };

             // Invoked by the Cancel button on the confirmation dialog.
             $scope.cancelUpload = () => {
            	 ngDialog.close();
             };
             
             init();
    	} // constructor
    } // class
    BulkRoleAndFunctionsModalCtrl.$inject = ['$scope', '$log', '$filter', '$q',  '$modalInstance', '$modal', 'ngDialog', 'message', 'confirmBoxService', 'usersService', 'applicationsService', 'functionalMenuService', 'RoleService'];    
    angular.module('ecompApp').controller('BulkRoleAndFunctionsModalCtrl', BulkRoleAndFunctionsModalCtrl);

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
    
    angular.module('ecompApp').filter('csvToFuncObj',function() {
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
   	    				type: '',
   	    				instance: '',
   	    				action: '',
   	    				name: '',
   	    				status: 'Blank line'
   	    			});
   	    			continue;
   	    		}
   	    		o = line.split(',');
   	    		if (o.length !== 4) {
   	    			// other lengths not valid for upload
   	    			result.push({
   	    				line: i,
   	    				type: o[0],
   	    				instance: o[1],
   	    				action: o[2],
   	    				name: '',
   	    				status: 'Failed to find 4 comma-separated values'
   	    			});
   	    		}
   	    		else {
   	    			// console.log("Valid line: ", val);
   	    			let entry = {
   	    					line: i,
   	    					type: o[0],
   	   	    				instance: o[1],
   	   	    				action: o[2],
   	   	    				name: o[3]
   	    					// leave status undefined, this
						// could be valid.
   	    			};
   	    			if (o[0].toLowerCase() === 'type') {
   	    				// not valid for upload, so set status
   	    				entry.status = 'Header';
   	    			}
   	    			else if (o[0].toLowerCase() === 'instance') {
   	    				// not valid for upload, so set status
   	    				entry.status = 'Header';
   	    			}
   	    			else if (o[0].toLowerCase() === 'action') {
   	    				// not valid for upload, so set status
   	    				entry.status = 'Header';
   	    			}
   	    			else if (o[0].toLowerCase() === 'name') {
   	    				// not valid for upload, so set status
   	    				entry.status = 'Header';
   	    			}
   	    			else if (o[0].trim() == '' || o[1].trim() == '' ||  o[2].trim() == '' ||  o[3].trim() == '') {
   	    				// defend against line with only a
					// single comma etc.
   	    				entry.status = 'Failed to find non-empty values';   	    				
   	    			}
   	    			result.push(entry);
   	    		} // len 2
    	    } // for
    	    return result;
    	};
    });
    
    angular.module('ecompApp').filter('csvToRoleFuncObj',function() {
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
   	    				role:'',
   	    				type: '',
   	    				instance: '',
   	    				action: '',
   	    				name: '',
   	    				status: 'Blank line'
   	    			});
   	    			continue;
   	    		}
   	    		o = line.split(',');
   	    		if (o.length !== 5) {
   	    			// other lengths not valid for upload
   	    			result.push({
   	    				line: i,
   	    				role: o[0],
   	    				type: o[1],
   	    				instance: o[2],
   	    				action: o[3],
   	    				name: '',
   	    				status: 'Failed to find 4 comma-separated values'
   	    			});
   	    		}
   	    		else {
   	    			// console.log("Valid line: ", val);
   	    			let entry = {
   	    					line: i,
   	    					role: o[0],
   	    					type: o[1],
   	   	    				instance: o[2],
   	   	    				action: o[3],
   	   	    				name: o[4]
   	    					// leave status undefined, this
						// could be valid.
   	    			};
   	    			if (o[0].toLowerCase() === 'role') {
   	    				// not valid for upload, so set status
   	    				entry.status = 'Header';
   	    			} else if (o[0].toLowerCase() === 'type') {
   	    				// not valid for upload, so set status
   	    				entry.status = 'Header';
   	    			}
   	    			else if (o[0].toLowerCase() === 'instance') {
   	    				// not valid for upload, so set status
   	    				entry.status = 'Header';
   	    			}
   	    			else if (o[0].toLowerCase() === 'action') {
   	    				// not valid for upload, so set status
   	    				entry.status = 'Header';
   	    			}
   	    			else if (o[0].toLowerCase() === 'name') {
   	    				// not valid for upload, so set status
   	    				entry.status = 'Header';
   	    			}
   	    			else if (o[0].trim() == '' || o[1].trim() == '' ||  o[2].trim() == '' ||  o[3].trim() == '' || o[4].trim() == '') {
   	    				// defend against line with only a
					// single comma etc.
   	    				entry.status = 'Failed to find non-empty values';   	    				
   	    			}
   	    			result.push(entry);
   	    		} // len 2
    	    } // for
    	    return result;
    	};
    });
    
    angular.module('ecompApp').filter('csvToRoleObj',function() {
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
   	    				role:'',
   	    				priority: '',
   	    				status: 'Blank line'
   	    			});
   	    			continue;
   	    		}
   	    		o = line.split(',');
   	    		if (o.length === 0 && line.length !== 0) {
   	    			// other lengths not valid for upload
   	    			result.push({
   	    				line: i,
   	    				role: o[0],
   	    				priority:null
   	    			});
   	    		}
   	    		else {
   	    			// console.log("Valid line: ", val);
   	    			let entry = {
   	    					line: i,
   	    					role: o[0],
   	   	    				priority: o[1]
   	    					// leave status undefined, this
						// could be valid.
   	    			};
   	    			if (o[0].toLowerCase() === 'role') {
   	    				// not valid for upload, so set status
   	    				entry.status = 'Header';
   	    			}
   	    			if (o[0].toLowerCase() === 'priority') {
   	    				// not valid for upload, so set status
   	    				entry.status = 'Header';
   	    			}
   	    			else if (o[0].trim() == '') {
   	    				// defend against line with only a
					// single comma etc.
   	    				entry.status = 'Failed to find non-empty values';   	    				
   	    			}
   	    			result.push(entry);
   	    		} // len 2
    	    } // for
    	    return result;
    	};
    });
    
})();
