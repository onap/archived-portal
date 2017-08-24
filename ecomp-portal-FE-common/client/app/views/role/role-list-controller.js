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
app.controller('roleListController', function ($scope,RoleService,confirmBoxService,conf,$state,$http,$log){
	$scope.showSpinner = true;
	
	RoleService.getRoles().then(function(data){
		$scope.showSpinner = true;
		var j = data;
  		$scope.data = JSON.parse(j.data);
  		$scope.availableRoles =JSON.parse($scope.data.availableRoles);
  		$scope.showSpinner = false;
	
	},function(error){
		$log.debug('failed');
	});
	
	
	$scope.goToUrl = function(roleIdVal) {
			$state.go("root.role", {"roleId":roleIdVal});
		}
		$scope.toggleRole = function(selected,availableRole) {
				var toggleType = null;
				if(selected) {
					toggleType = "activate";
				} else {
					toggleType = "inactivate";
				}
				if((availableRole.id == "1") || (availableRole.id =="999"))
				{
				 confirmBoxService.showInformation(availableRole.name+" role cannot be disabled");
				 availableRole.active=!availableRole.active
	             return;
				}
				confirmBoxService.confirm("You are about to "+toggleType+" the role "+availableRole.name+". Do you want to continue?").then(
		    		function(confirmed){
		    				
		    			if(confirmed) {
			            	var uuu = conf.api.toggleRole;
								
								var postData={role:availableRole};
								$http.post(uuu, postData).then(function(response) {
									var data = response.data;
									if (typeof data === 'object') {
										$log.debug('data:'+data);
							  			$scope.availableRoles=data.availableRoles; 
							  			$log.debug('role::availableRoles:'+$scope.availableRoles);
									} else {
										//
									}

								}, function(response) {
									debug.log('response:'+response);
									availableRole.active=!availableRole.active;
									confirmBoxService.showInformation("Error while saving.");
								});
								
		    				 }

		    	}) .catch(function(err) {
		    		$log.error('roleListController::confirmBoxService.confirm error:', err);
		    		availableRole.active=!availableRole.active;					
		    	});
				
				  
		};

		$scope.removeRole = function(role) {
			
			confirmBoxService.confirm("You are about to delete the role "+role.name+". Do you want to continue?").then(
	    			function(confirmed){
							var uuu = conf.api.removeRole;
							  var postData={role:role};

							  
							  
							  $http.post(uuu, postData).then(function(response) {
									var data = response.data;
									if (typeof data === 'object') {
							  			$scope.availableRoles=data.availableRoles; 
									} else {
										//
									}

								}, function(response) {
									$log.debug('response:'+response.data);
									confirmBoxService.showInformation("Error while deleting: "+ data.responseText);
								});
				
	    	});
	    	
			
		};
		

});
