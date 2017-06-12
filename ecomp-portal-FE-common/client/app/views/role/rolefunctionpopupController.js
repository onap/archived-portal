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
app.controller('rolefunctionpopupController', function ($scope, confirmBoxService, message, $http,RoleService, conf, isEditing){
				if(message.availableRoleFunction==null) {
					$scope.label='Add Role Function';
					var tempText = "";
				}
				else{
					$scope.label='Edit Role Function'
					$scope.disableCd=true;
					var tempText = new String(message.availableRoleFunction.name);
					$scope.editRoleFunction = angular.copy(message.availableRoleFunction);
				}
				
				$scope.tempText = tempText;
				$scope.isEditing = isEditing;
				
				$scope.saveRoleFunction = function(availableRoleFunction) {
					  var uuu = conf.api.saveRoleFunction;
					  var postData={availableRoleFunction: availableRoleFunction};

					  if(availableRoleFunction==null){
						  confirmBoxService.showInformation("Please enter valid role function details.");
					  }
					  var exists = false,x;
					  for(x in message.availableRoleFunctions){
						  console.log(message.availableRoleFunctions[x].name);
							if(message.availableRoleFunctions[x].name==availableRoleFunction.name){
								confirmBoxService.showInformation("Role Function already exists.");
								exists = true;
								availableRoleFunction.name = $scope.tempText;
								break;
							} 
							if(!isEditing){
								if (message.availableRoleFunctions[x].code == availableRoleFunction.code) {
									confirmBoxService.showInformation("Code already exists. Please create a role function with a different code to proceed.");
									exists = true;
									availableRoleFunction.name = $scope.tempText;
									break;
								}
							}
					  }
					  
					  if(!exists && availableRoleFunction.name.trim() != '' && availableRoleFunction.code.trim() != ''){
			              $http.post(uuu, JSON.stringify(postData)).then(function(res){
			            	  console.log("data");
//			            	  console.log(res.data);
//			            	  $scope.availableRoleFunctionsTemp = res.data.availableRoleFunctions;
			            	  RoleService.getRoleFunctionList().then(function(data){
			            			
			            			var j = data;
			            	  		$scope.data = JSON.parse(j.data);
			            	  		$scope.availableRoleFunctions =JSON.parse($scope.data.availableRoleFunctions);
			            	  		
			            	  		//$scope.resetMenu();
			            	  		$scope.closeThisDialog({result: true, availableRoleFunctions: $scope.availableRoleFunctions });
			            		},function(error){
			            			console.log("failed");
			            			//reloadPageOnce();
			            			$scope.closeThisDialog(true);		            			
			            		});
			            	  
			            	  
			              });						  
						  
						  
						  
						  
						}
				};
					  
				  	  
					
				$scope.close = function() { 
					this.closeThisDialog(true);
				};
}
);
