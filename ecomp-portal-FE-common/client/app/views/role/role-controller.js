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

app.controller('roleController', function ($scope, $http, confirmBoxService, ngDialog, RoleService, conf, $stateParams){
	//$scope.role=${role};
		
	$( "#dialogRoleFunction" ).hide();
	$( "#dialogChildRole" ).hide();
	
	//$scope.ociavailableRoleFunctions=${availableRoleFunctions};
	$scope.fetchRoles = function() {
	
	RoleService.getRole($stateParams.roleId).then(function(data){
		
		var j = data;
  		$scope.data = JSON.parse(j.data);
  		
  		$scope.role =JSON.parse($scope.data.role);
  		
  		$scope.ociavailableRoleFunctions =JSON.parse($scope.data.availableRoleFunctions);
  		$scope.availableRoleFunctions=[];
  		
  		if($scope.ociavailableRoleFunctions)
  			$.each($scope.ociavailableRoleFunctions, function(i, a){ 
  				var availableRoleFunction = a;
  				availableRoleFunction.selected = false;
  			    $.each($scope.role.roleFunctions, function(j, b){ 
  			    	if(a.code === b.code) {
  			    		availableRoleFunction.selected = true;
  			    	}
  			    });
  			    $scope.availableRoleFunctions.push(availableRoleFunction);	    
  		});	
  		

  		$scope.ociavailableRoles=JSON.parse($scope.data.availableRoles);
  		$scope.availableRoles=[];
  		
  		if($scope.ociavailableRoles)
  			$.each($scope.ociavailableRoles, function(i, a){ 
  				var availableRole = a;
  				availableRole.selected = false;
  				if($scope.role.childRoles){
  			    $.each($scope.role.childRoles, function(j, b){ 
  			    	if(a.id === b.id) {
  			    		availableRole.selected = true;
  			    	}
  			    });
  				};
  			    $scope.availableRoles.push(availableRole);	    
  		});
  			
	
	},function(error){
		console.log("RoleService.getRole failed", error);
		//reloadPageOnce();
	});
	}
	
	$scope.fetchRoles();

	$scope.saveRole = function() {
				var exists = false,x;	
				for(x in $scope.availableRoles){
					if($scope.availableRoles[x].name==$scope.role.name){
						exists = true;
						//$modalInstance.close({availableRoleFunctions:message.availableRoleFunctions});
					}
				}
				if (exists) {
					confirmBoxService.showInformation( "Role already exists.");
				}
				else {
					var uuu = conf.api.saveRole + "?role_id="+$stateParams.roleId;
					var postData = {
							role: $scope.role, 
							childRoles: $scope.role.childRoles,
							roleFunctions : $scope.role.roleFunctions
					};
					$http.post(uuu, JSON.stringify(postData)).then(function(res) {
						// console.log('roleController::saveRole: ' + JSON.stringify(res));
						if (res && res.data && res.data.role)
							confirmBoxService.showInformation("Update Successful.");
						else
							confirmBoxService.showInformation('Failed to create role: ' + res.data.error)
					},
					function(res){
						console.log('post failed', res.data);
						confirmBoxService.showInformation("Error while saving.");
					}
					);
				}
			};
		
	$scope.addNewRoleFunctionModalPopup = function() {
			var modalInstance = ngDialog.open({
			    templateUrl: 'app/views/role/role_functions_popup.html',
			    controller: 'rolepopupController',
			    
			    resolve: {
			    	roleId: function () {
				          return $stateParams.roleId;
				        },
			    	role: function () {
			          return $scope.role;
			        },
			        availableRoles: function () {
				          return $scope.ociavailableRoles;
				    },
				    availableRoleFunctions: function () {
				          return $scope.ociavailableRoleFunctions;
				    },
			      }
			  });
			 modalInstance.closePromise.then(response => {
					if($stateParams.roleId === '0'){
						return $scope.role;
					}else{
						$scope.fetchRoles();
					}
	           // $scope.role=response.role;
	        });
	};
		
	 $scope.addNewChildRoleModalPopup = function() {
			var modalInstance = ngDialog.open({
			    templateUrl: 'app/views/role/role_childrole_popup.html',
			    controller: 'rolepopupController',
			  
			    resolve: {
			    	roleId: function () {
				          return $stateParams.roleId;
				        },
			    	role: function () {
			          return $scope.role;
			        },
			        availableRoles: function () {
				          return $scope.ociavailableRoles;
				    },
				    availableRoleFunctions: function () {
				          return $scope.ociavailableRoleFunctions;
				    },
			      }
			  }).closePromise.then(function(response){
					if($stateParams.roleId === '0'){
						 return $scope.role;
					}else{
			            $scope.fetchRoles();
					}
	            //$scope.role=response.role;
	        });
		};
		
		$scope.removeRoleFunction = function(roleFunction) {
			confirmBoxService.confirm("You are about to remove the role function "+roleFunction.name+" from the role for "+$scope.role.name+". Do you want to continue?").then(
	    			function(confirmed){
						var uuu = conf.api.toggleRoleRoleFunction + "?role_id=" + $stateParams.roleId;
						  var postData={roleFunction:roleFunction};
						  	if(confirmed) {	
								$http.post(uuu, postData).then(
										function(response) {
											$scope.role= response.data.role;
											$.each($scope.availableRoleFunctions, function(k, c){ 
							  			    	if(c.code === roleFunction.code) {
							  			    		c.selected = false;
							  			    	}
							  			    });
										}, 
										function(response) {
											confirmBoxService.showInformation("Error while saving.");
										}
								);									
								}
				
	    	});
		
		};
		
		$scope.removeChildRole = function(childRole) {
			confirmBoxService.confirm("You are about to remove the child role "+childRole.name+" from the role for "+$scope.role.name+". Do you want to continue?").then(
	    			function(confirmed){
					var uuu = conf.api.toggleRoleChildRole + "?role_id=" + $stateParams.roleId;
					  var postData={childRole:childRole};
					  if(confirmed) {
							  $http.post(uuu,postData).then( function(response) {
								  $scope.role=response.data.role;
								  $.each($scope.availableRoles, function(k, c){ 
					  			    	if(c.id === childRole.id) {
					  			    		c.selected = false;
					  			    	}
					  			    });
								  },
								  
								  function(data) {
									  confirmBoxService.showInformation("Error while saving.");
								  });
						}				
	    	});
			
		};
		
});
