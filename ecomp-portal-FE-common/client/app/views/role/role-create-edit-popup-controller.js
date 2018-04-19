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
app.controller('roleCreateEditController',function($scope, conf, $http, $modalInstance, confirmBoxService, message, RoleService) {
	$scope.isGlobalRole = false; 
	$scope.availableRoles = message.availableRoles;
	$scope.roleFunctions = message.availableRoleFunctions;
	$scope.appId = message.appId;
	if($scope.appId == 1){
		$scope.isGlobalRole = true;
	}
	$scope.role = message.role;
	$scope.isGlobalRoleChecked={
			isChecked : false
	}
	if(message.role.name)
		$scope.isGlobalRoleChecked.isChecked=(message.role.name.indexOf('global_')==-1)?false:true;		
	$scope.availableRoleFunctions=[];
	$scope.finalSelectedRoleFunctions=[];

	if($scope.roleFunctions)
		for(var i=0; i< $scope.roleFunctions.length; i++){
			var availableRoleFunction = $scope.roleFunctions[i];
			availableRoleFunction.selected = false;
			for(var j=0; j< $scope.role.roleFunctions.length; j++){
				if($scope.roleFunctions[i].code === $scope.role.roleFunctions[j].code
						&& $scope.roleFunctions[i].type === $scope.role.roleFunctions[j].type
						&& $scope.roleFunctions[i].action === $scope.role.roleFunctions[j].action) {
					availableRoleFunction.selected = true;
					console.log(availableRoleFunction.selected);
				}
			};
			$scope.availableRoleFunctions.push(availableRoleFunction);	    
		};
	
		$scope.toggleRoleFunction = function(selected,selectedRoleFunction){
			if($scope.roleFunctions){
				for(var i=0; i< $scope.roleFunctions.length; i++){
					var availableRoleFunction = $scope.roleFunctions[i];					
						if(availableRoleFunction.selected){
							$scope.finalSelectedRoleFunctions.push(availableRoleFunction);
						}
				}
			}
			if(!selected) {
				for(var i=0; i<$scope.finalSelectedRoleFunctions.length; i++){
					var availableRoleFunction = $scope.finalSelectedRoleFunctions[i];
					if(availableRoleFunction.code == selectedRoleFunction.code
							&& availableRoleFunction.type == selectedRoleFunction.type
							&& availableRoleFunction.action == selectedRoleFunction.action){
						$scope.finalSelectedRoleFunctions.splice(i, 1);
					}
				}					
			}
		}
		
		
		$scope.saveRole = function() {			
			var exists = false,x;	
			for(x in $scope.availableRoles){
				if($scope.availableRoles[x].name==$scope.role.name){
					exists = true;
				}
			}
			if (exists) {
				confirmBoxService.showInformation( "Role already exists.");
			}			
			else {	
				console.log($scope.role);
				var uuu = conf.api.saveRole.replace(':appId', $scope.appId);
				if($scope.isGlobalRoleChecked.isChecked ){
					$scope.role.name = ($scope.role.name.indexOf('global_')==-1)?('global_'+$scope.role.name):($scope.role.name);
					saveOrUpdateRole(uuu);
				}else{
					var roleName = $scope.role.name.toLowerCase();
					if(roleName.includes('global_')){
						confirmBoxService.showInformation('Global prefix:"global_" can only be used when the global flag is checked for the role name:' +$scope.role.name+ '. Please try again!' ).then(confirmed => {
						});
					} else{
						saveOrUpdateRole(uuu);
					}
				}
			}
		};
		
        let saveOrUpdateRole = (uuu) => {
			//overriding the final list of rolefunctions to role
			if($scope.finalSelectedRoleFunctions.length > 0)
				$scope.role.roleFunctions = $scope.finalSelectedRoleFunctions;
			var postData = {
					role: $scope.role,
					childRoles: $scope.role.childRoles,
					roleFunctions :  $scope.role.roleFunctions
			};
			
			$http.post(uuu, postData).then(function(res) {
				if (res && res.data && res.data.role){
					confirmBoxService.showInformation("Update Successful.").then(confirmed => {
						$modalInstance.close("confirmed");
					});
				}
				
				else{
					confirmBoxService.showInformation('Failed to create role: ' + res.data.error)
				}
			},
			function(res){
				console.log('post failed', res.data);
				if (res.data.includes('BAD_REQUEST')){
					confirmBoxService.showInformation("Error while saving." + "BAD_REQUEST");
					}
				else{
					confirmBoxService.showInformation("Error while saving." + res.data.error);
					}					
			}
			);
		}
		
})