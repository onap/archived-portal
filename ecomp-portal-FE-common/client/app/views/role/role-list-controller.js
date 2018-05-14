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
app.controller('roleListController', function ($scope,RoleService, applicationsService, confirmBoxService,conf,$state,$http,$log, $modal){
	$scope.showSpinner = true;	
	$scope.syncRolesApplied = false; 
	 $scope.app = {
		        appName: ''
		    }
	$scope.goToUrl = function(roleIdVal) {
			$state.go("root.role", {"roleId":roleIdVal});
		}
	 
	$scope.toggleRole = function(appId, selected, availableRole) {		
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
		else if((appId != '1') && (availableRole.name.indexOf('global_')!=-1))
			{
			confirmBoxService.showInformation("Global role cannot be disabled");
			availableRole.active=!availableRole.active
			return;
			}
		confirmBoxService.confirm("You are about to "+toggleType+" the role "+availableRole.name+". Do you want to continue?").then(
    		function(confirmed){
    			if(confirmed) {
	            	var uuu = conf.api.toggleRole + '/' + appId + '/' + availableRole.id;
						var postData={
								appId: appId,
								role:availableRole
								};
						$http.post(uuu, postData).then(function(response) {
							var data = response.data;
							if (typeof data === 'object' & data.restcallStatus=='Success') {
					  			$scope.availableRoles = data.availableRoles; 
					  			$log.debug('role::availableRoles:'+$scope.availableRoles);
							} else {
								confirmBoxService.showInformation("Error while saving." + data.restCallStatus);
								availableRole.active=!availableRole.active;
							}

						}, function(response) {
							debug.log('response:'+response);
							availableRole.active=!availableRole.active;
							confirmBoxService.showInformation("Error while saving." + data.restCallStatus);
						});
						
    				 }

    	}) .catch(function(err) {
    		$log.error('roleListController::confirmBoxService.confirm error:', err);
    		availableRole.active=!availableRole.active;					
    	});
	};

		$scope.removeRole = function(appId, availableRole) {
		if ((appId != '1') && (availableRole.name.indexOf('global_')!=-1)){
				confirmBoxService.showInformation("Global role cannot be deleted");
			}
			else{
				confirmBoxService.confirm("You are about to delete the role "+availableRole.name+". Do you want to continue?").then(
	    			function(confirmed){
							var uuu = conf.api.removeRole + '/' + appId + '/' + availableRole.id;
							  var postData={ 
									  appId: appId,
									  availableRoleId: availableRole.id
									  };
							  $http.post(uuu, postData).then(function(response) {
									var data = response.data;
									if (typeof data === 'object' & data.restCallStatus == 'Success') {
							  			$scope.availableRoles = data.availableRoles; 
									} else {
										confirmBoxService.showInformation('Failed to remove role '+ data.error )
									}

								}, function(response) {
									confirmBoxService.showInformation("Error while deleting: "+ data.error);
								});
				
	    		}); 
	    	}  
		};
		
		 $scope.openBulkUploadRolesAndFunctionsModal = function(appId) {
	            var modalInstance = $modal.open({
	            	templateUrl: 'app/views/role/bulk-upload-dialogs/bulk-upload-role-functions-modal.html',
	                controller: 'BulkRoleAndFunctionsModalCtrl as bulkRoleAndFunctions',
	                sizeClass: 'modal-medium',
	                resolve: {
	                	message: function () {
		 		    		var message = {
		 		    				appid: appId
		                     };
		 		          return message;
		 		        }
			        }
	            });         
	    		modalInstance.result.then(function (confirmed) {
					if(confirmed == 'confirmed'){
					   // update role list table
					}
				});
	        };
				
		// getCentalizedApps
		$scope.getCentralizedApps = function(userId) {
			RoleService.getCentralizedApps(userId).then(res=> {
               if (res.length>0) {
            	   $scope.centralizedApps = res;
            	   for(var i = 0; i<res.length; i++){
            		      if(res[i].appId == 1){
            		    	  $scope.getRolesForSelectedCentralizedApp(res[i].appId);
            		    	  $scope.apps.selectedCentralizedApp = res[i].appId;
            		    	  return;
            		      }  
            		      $scope.getRolesForSelectedCentralizedApp(res[0].appId);
            		      $scope.apps.selectedCentralizedApp = res[0].appId;
            	   }
                }
            }).catch(err=> {
                $log.error('RoleListCtrl::centralizedApps retrieval error: ', err);
            }).finally(() => {
               // this.isLoadingTable = false;
            });
	
		}
		
		   $scope.syncRolesFromExternalAuthSystem = function(appId){
           	applicationsService.syncRolesEcompFromExtAuthSystem(appId).then(function(res){
           		if(res.status == 200){
           	     confirmBoxService.showInformation('Sync operation completed successfully!').then(isConfirmed => {
           	    	$scope.getRolesForSelectedCentralizedApp(appId);
           	     });          			
           		} else{
  	        		 confirmBoxService.showInformation('Sync operation failed for '+app).then(isConfirmed => {});          			
           		}
           	});
           };
           
				
		$scope.getRolesForSelectedCentralizedApp = function(val) {
			$scope.showSpinner = true;
			  applicationsService.getSingleAppInfoById(val).then(function(res) {
        		  if(res.centralAuth == true){
        			  $scope.syncRolesApplied = true;
        		  }
	            });
			RoleService.getRoles(val).then(function(data){
				var j = data;
		  		$scope.data = JSON.parse(j.data);
		  		$scope.availableRoles =$scope.data.availableRoles;
		  		$scope.showSpinner = false;
			
			},function(error){
				$scope.showSpinner = false;
				confirmBoxService.showInformation('Failed to get roles. Please try again!').then(isConfirmed => {});
				$scope.availableRoles = [];
				$log.debug('failed');
			});
		}
		
		$scope.fnManageRoleFunctions = function(){
			RoleService.setManageRoleDetails($scope.centralizedApps, $scope.apps.selectedCentralizedApp);
		};
		
		function init(){	
			$scope.apps = {
					selectedCentralizedApp:''
			};
			$scope.getCentralizedApps(sessionStorage.userId);
		}
		
		init();
		
		// edit Role
		$scope.editRoleModalPopup = function(appId, availableRole) {
			$scope.editRole = availableRole;
			if(appId != undefined && availableRole.id != undefined){
				RoleService.getRole(appId, availableRole.id).then(function(data){	
					var response = JSON.parse(data.data);					
					var role = JSON.parse(response.role);
					var availableRoles = JSON.parse(response.availableRoles);
					var availableRoleFunctions = JSON.parse(response.availableRoleFunctions);					
					$scope.availableRoleFunctions = response.roleFunctions;
					var modalInstance = $modal.open({
			             templateUrl: 'app/views/role/popup_createedit_role.html',
			             controller: 'roleCreateEditController',
			             sizeClass: 'modal-large', 
			             windowClass: "modal-docked",
			             resolve: {
			 		    	message: function () {
			 		    		var message = {
			 		    				availableRoles: availableRoles,
			 		    				availableRoleFunctions: availableRoleFunctions,
			 		    				appId: $scope.apps.selectedCentralizedApp,
			 		    				role: role
			 		    		};
			 		          return message;
			 		        }
			 		      }
			 		  });
					modalInstance.result.then(function (confirmed) {
						if(confirmed == 'confirmed'){
							$scope.getRolesForSelectedCentralizedApp(appId);
						}
					});
				},function(error){
					$log.debug('Failed to editRole');
				});
			}
			 
		};
		
		// add Role
		$scope.addRoleModalPopup = function(appId) {
			if(appId){
				var roleId = 0;
				RoleService.getRole(appId, roleId).then(function(data){	
					var response = JSON.parse(data.data);					
					var role = JSON.parse(response.role);
					var availableRoles = JSON.parse(response.availableRoles);
					var availableRoleFunctions = JSON.parse(response.availableRoleFunctions);					
					$scope.availableRoleFunctions = response.roleFunctions;
					var modalInstance = $modal.open({
			             templateUrl: 'app/views/role/popup_createedit_role.html',
			             controller: 'roleCreateEditController',
			             sizeClass: 'modal-large', 
			             windowClass: "modal-docked",
			             resolve: {
			 		    	message: function () {
			 		    		var message = {
			 		    				role: role,
			 		    				appId: $scope.apps.selectedCentralizedApp
			                     };			 		    		
			 		          return message;
			 		        }
			 		      }
			 		  });	
					modalInstance.result.then(function (confirmed) {
						if(confirmed == 'confirmed'){
							$scope.getRolesForSelectedCentralizedApp(appId);
						}
					});
				},function(error){
					$log.debug('Failed to AddRole');
				});
			}			 
		};

});
