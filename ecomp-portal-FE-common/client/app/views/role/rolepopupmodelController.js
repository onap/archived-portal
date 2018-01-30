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
app.controller('rolepopupController',  function ($scope, role, roleId, confirmBoxService, $http, $state, availableRoles, conf, availableRoleFunctions,ngDialog){
	
	  		$scope.role = role;
	  		console.log($scope.role);
	  		if($scope.role.childRoles==null){
	  			$scope.role.childRoles=[];
	  		}
	  		
	  		$scope.ociavailableRoles=availableRoles;
	  		console.log($scope.ociavailableRoles);
	  		
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
	  			
	  		$scope.ociavailableRoleFunctions = availableRoleFunctions; 
	  		console.log($scope.ociavailableRoleFunctions);
	  		$scope.availableRoleFunctions = []; 
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
	  		//$scope.resetMenu();
		
		$scope.toggleRoleFunction = function(selected,availableRoleFunction) {
			//alert('toggleRole: '+selected);
			 
			if(!selected) {
				//remove role function
				if(role.id==null){
					var index = $scope.role.roleFunctions.indexOf(availableRoleFunction);
				 	if(index>=0)
						$scope.role.roleFunctions.splice(index, 1);
					return;
				}
				var uuu = conf.api.toggleRoleRoleFunction + "?role_id=" + roleId;
				confirmBoxService.confirm("You are about to remove the role function "+availableRoleFunction.name+" from the role for "+$scope.role.name+". Do you want to continue?").then(
		    			function(confirmed){
		    					if(confirmed) {	
									availableRoleFunction.id = roleId;
									var index = $scope.role.roleFunctions.indexOf(availableRoleFunction);
								 	if(index>=0)
										$scope.role.roleFunctions.splice(index, 1);
									return;										
									
								}
		    			}).catch(function(err) {
							  availableRoleFunction.selected=!availableRoleFunction.selected;
					    		$log.error('roleListController::confirmBoxService.confirm error:', err);

				    	});
	
			} else {
				//add role function
				if(role.id==null){
					$scope.role.roleFunctions.push(availableRoleFunction);
					return;
				}
				var uuu = conf.api.addRoleRoleFunction + "?role_id=" + roleId;
				
				confirmBoxService.confirm("You are about to add the role function "+availableRoleFunction.name+" to the role for "+$scope.role.name+". Do you want to continue?").then(
		    			function(confirmed){
		    				if(confirmed) {
		    					availableRoleFunction.id = roleId;
		    					$scope.role.roleFunctions.push(availableRoleFunction);
		    					return;
							}
			}).catch(function(err) {
				  availableRoleFunction.selected=!availableRoleFunction.selected;
		    		$log.error('roleListController::confirmBoxService.confirm error:', err);
	    	});
			
			  
	}
		};
	
	$scope.toggleChildRole = function(selected,availableRole) {
		//alert('toggleRole: '+selected);

		if(!selected) {
			//remove role
			if(role.id==null){
				var index = $scope.role.childRoles.indexOf(availableRole);
			 	if(index>=0)
					$scope.role.childRoles.splice(index, 1);
				return;
			}
			var uuu = conf.api.toggleRoleChildRole +"?role_id=" + roleId;
			
			confirmBoxService.confirm("You are about to remove the child role "+availableRole.name+" from the role for "+$scope.role.name+". Do you want to continue?").then(
	    			function(confirmed){
	    				if(confirmed) {
	    			
						var postData={childRole:availableRole};
						 $http.post(uuu,postData).then(function(response) {
							  $scope.role=response.data.role;
							  },
							  function(data) {
								  confirmBoxService.showInformation("Error while saving.");
							  });
						  }
	    			}).catch(function(err) {
	    				availableRole.selected=!availableRole.selected;
			    		$log.error('roleListController::confirmBoxService.confirm error:', err);
		    	});
				
	    	
		} else {
			//add role
			if(role.id==null){
				$scope.role.childRoles.push(availableRole);
				return;
			}
			var uuu = conf.api.addRoleChildRole +"?role_id=" + roleId;
			
			confirmBoxService.confirm("You are about to add the child role "+availableRole.name+" to the role for "+$scope.role.name+". Do you want to continue?").then(
	    			function(confirmed){
	    				if(confirmed) {
						  var postData={childRole:availableRole};
						  $http.post(uuu,postData).then(function(response) {
							  $scope.role=response.data.role;
							  },
							  function(data) {
								  confirmBoxService.showInformation("Error while saving.");
							  });
						  }
				
	    	}).catch(function(err) {
				availableRole.selected=!availableRole.selected;
	    		$log.error('roleListController::confirmBoxService.confirm error:', err);
    	});
		
		  
	}
	};
	
	$scope.close = function() {
		console.log('role', $scope.role);
		//$modalInstance.close({role:$scope.role});
		this.closeThisDialog(true);
	};
	
});
