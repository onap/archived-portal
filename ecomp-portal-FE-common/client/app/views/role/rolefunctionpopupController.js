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
app.controller('rolefunctionpopupController',function($scope, confirmBoxService, message, $http, $modalInstance, RoleService, conf, isEditing) {
	if (message.availableRoleFunction == null) {
		$scope.label = 'Add Role Function';
		var tempText = "";
		$scope.selectedAppId = message.appId;
		$scope.defaultAction = "*";
		$scope.disableTypeAction = true;	
		 $scope.ngRepeatDemo = [
		        {id: 'menuradiobutton1', value: 'menu', labelvalue: 'menu'},
		        {id: 'urlradiobutton2', value: 'url', labelvalue: 'url'},
		        {id: 'otherradiobutton3', value: 'other', labelvalue: 'other'}
		    ]
		 $scope.selectedvalueradioButtonGroup = {
			        type: 'menu'
			    }
		 $scope.editRoleFunction = {
			        action: '*'
			    }
	} else {
		 $scope.ngRepeatDemo = [
		        {id: 'menuradiobutton1', value: 'menu', labelvalue: 'menu'},
		        {id: 'urlradiobutton2', value: 'url', labelvalue: 'url'},
		        {id: 'otherradiobutton3', value: 'other', labelvalue: 'other'}
		    ]
		$scope.label = 'Edit Role Function'
		$scope.disableCd = true;
		$scope.disableTypeAction = false;
		var tempText = new String(message.availableRoleFunction.name);
		$scope.editRoleFunction = angular.copy(message.availableRoleFunction);
		if($scope.editRoleFunction.type.includes('menu')||$scope.editRoleFunction.type.includes('url')){
			 $scope.selectedvalueradioButtonGroup = {
				        type: $scope.editRoleFunction.type
				    }
		} else{
			 $scope.selectedvalueradioButtonGroup = {
				        type: 'other'
				    }
		}
		$scope.selectedAppId = message.appId;
	}
	$scope.tempText = tempText;
	$scope.isEditing = isEditing;

	$scope.saveRoleFunction = function(availableRoleFunction, type) {
		if(type !== 'other'){
			if (!availableRoleFunction.hasOwnProperty('type')) {
				availableRoleFunction['type'] = type ; 
			} else{
				availableRoleFunction.type = type ; 
			}
		}
		if(/[^a-zA-Z0-9\-\.\_]/.test(availableRoleFunction.type)){
			confirmBoxService.showInformation('Type can only contain alphanumeric characters, dots(.) and underscores(_)').then(isConfirmed => {});
			return;
		}		
		if(availableRoleFunction.action !== '*' && /[^a-zA-Z0-9\-\.\_]/.test(availableRoleFunction.action)){
			confirmBoxService.showInformation('Action can only contain alphanumeric characters, hyphens(-), dots(.) and underscores(_) and single asterisk character(*)').then(isConfirmed => {});
			return;
		}
		if(/[^a-zA-Z0-9\-\:\_\./*]/.test(availableRoleFunction.code)){
			confirmBoxService.showInformation('Instance can only contain alphanumeric characters, hyphens(-), dots(.), colons(:), forwardSlash(/) , asterisk(*) and underscores(_)').then(isConfirmed => {});
			return;
		}
		if(/[^a-zA-Z0-9\-\_ \.]/.test(availableRoleFunction.name)){
			confirmBoxService.showInformation('Name can only contain alphanumeric characters, spaces, hyphens(-), dots(.) and underscores(_)').then(isConfirmed => {});
			return;
		}
		confirmBoxService.confirm(
				"You are about to Create the role function "+ availableRoleFunction.name+ ". Do you want to continue?")
				.then(function(confirmed) {
					if (confirmed) {
							var uuu = conf.api.saveRoleFunction.replace(':appId', $scope.selectedAppId);
							var postData = availableRoleFunction;
						if (availableRoleFunction == null) {
							confirmBoxService.showInformation("Please enter valid role function details.");
						}
						var exists = false, x;
						for (x in message.availableRoleFunctions) {
							console.log(message.availableRoleFunctions[x].name);
							if (message.availableRoleFunctions[x].type == availableRoleFunction.type
									&& message.availableRoleFunctions[x].code == availableRoleFunction.code
									&& message.availableRoleFunctions[x].action == availableRoleFunction.action
									&& message.availableRoleFunctions[x].name == availableRoleFunction.name) {
								confirmBoxService.showInformation("Role Function already exists.");
								exists = true;
								availableRoleFunction.name = $scope.tempText;
								break;
							}
							if (!isEditing) {
								if (message.availableRoleFunctions[x].code == availableRoleFunction.code) {
									confirmBoxService.showInformation("Code already exists. Please create a role function with a different code to proceed.");
									exists = true;
									availableRoleFunction.name = $scope.tempText;
									break;
								}
							}
						}

						if (!exists&& availableRoleFunction.name.trim() != ''&& availableRoleFunction.code.trim() != '') {
							$http.post(uuu,JSON.stringify(postData)).then(function(res) {
								if(res.data.status == 'OK'){
									confirmBoxService.showInformation(res.data.message).then(isConfirmed => {});
								} else{
									confirmBoxService.showInformation('Error:' + res.data.message).then(isConfirmed => {});
								}
								$scope.availableRoleFunctionsTemp = res.data.availableRoleFunctions;
								$modalInstance.close(true);
							});

						}
					};

					$scope.close = function() {
						this.closeThisDialog(true);
					};
				}

				);
	}
});