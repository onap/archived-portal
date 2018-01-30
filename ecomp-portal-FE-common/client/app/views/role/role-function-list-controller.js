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
"use strict";

app.controller('roleFunctionListController', function ($scope, RoleService, applicationsService, $http, $state, conf, confirmBoxService, ngDialog, $modal) {
	$("#dialog").hide();
	$scope.centralizedApps = RoleService.getManageRoleDetails().apps;
	$scope.apps = {
		selectedCentralizedApp: RoleService.getManageRoleDetails().id
	};

	$scope.getRoleFunctions = function (id) {
		$scope.isLoadingRoleFunctions = true;
		RoleService.getRoleFunctionList(id).then(function (data) {
			var j = data;
			$scope.data = JSON.parse(j.data);
			$scope.availableRoleFunctions = $scope.data.availableRoleFunctions;
		}, function (error) {
			confirmBoxService.showInformation("Failed to get role functions. Please try again!");
			console.log("failed");
		}).finally(function(){
			$scope.isLoadingRoleFunctions = false;
		});
	};

	function init() {
		if($scope.apps.selectedCentralizedApp){
			$scope.getRoleFunctions($scope.apps.selectedCentralizedApp);
		}
		if($scope.apps.selectedCentralizedApp == undefined){
			$scope.getCentralizedApps(sessionStorage.userId);
		}
	}

	//getCentalizedApps
	$scope.getCentralizedApps = function(userId) {
		RoleService.getCentralizedApps(userId).then(res=> {
           if (res.length>0) {
        	   $scope.centralizedApps = res;
        	   for(var i = 0; i<res.length; i++){
        		      if(res[i].appId == 1){
        		      $scope.apps.selectedCentralizedApp = res[i].appId;
        		    	  return;
        		      }
        		       $scope.apps.selectedCentralizedApp = res[0].appId;
        	   }
            }
        }).catch(err=> {
            $log.error('RoleListCtrl::centralizedApps retrieval error: ', err);
        }).finally(() => {
           // this.isLoadingTable = false;
        });

	}
	
	init();

	$scope.editRoleFunction = null;
	var dialog = null;

	$scope.editRoleFunctionModalPopup = function (availableRoleFunction) {
		$scope.editRoleFunction = availableRoleFunction;
		$scope.availableRoleFunctionsTemp = $scope.availableRoleFunctions;

		var modalInstance = $modal.open({
			templateUrl: 'app/views/role/popup_modal_rolefunction.html',
			controller: 'rolefunctionpopupController',
			sizeClass: 'modal-small',
			resolve: {
				message: function message() {
					var message = {
						availableRoleFunction: $scope.editRoleFunction,
						availableRoleFunctions: $scope.availableRoleFunctionsTemp,
						appId: $scope.apps.selectedCentralizedApp
					};
					return message;
				},
				isEditing: function isEditing() {
					return true;
				}
			}
		});

		modalInstance.result.then(function (response) {
			if (response) {
				$scope.getRoleFunctions($scope.apps.selectedCentralizedApp);
			}
			else
				$scope.availableRoleFunctions = $scope.availableRoleFunctionsTemp;
		});
	};

	$scope.addNewRoleFunctionModalPopup = function (availableRoleFunction) {

		$scope.editRoleFunction = null;
		$scope.availableRoleFunctionsTemp = $scope.availableRoleFunctions;
		var modalInstance = $modal.open({
			templateUrl: 'app/views/role/popup_modal_rolefunction.html',
			controller: 'rolefunctionpopupController',
			sizeClass: 'modal-small',
			resolve: {
				message: function message() {
					var message = {
						availableRoleFunction: $scope.editRoleFunction,
						availableRoleFunctions: $scope.availableRoleFunctionsTemp,
						appId: $scope.apps.selectedCentralizedApp
					};
					return message;
				},
				isEditing: function isEditing() {
					return false;
				}
			}
		});

		modalInstance.result.then(function (response) {
			if (response) {
				$scope.getRoleFunctions($scope.apps.selectedCentralizedApp);
			}
			if (response.availableRoleFunctions != undefined) 
				$scope.availableRoleFunctions = response.availableRoleFunctions;else $scope.availableRoleFunctions = $scope.availableRoleFunctionsTemp;
		});
	};

	$scope.addNewRoleFunctionPopup = function () {
		$scope.editRoleFunction = null;
		$("#dialog").dialog({
			modal: true
		});
	};

	 $scope.syncRoleFunctionsFromExternalAuthSystem = function(appId){
        	applicationsService.syncFunctionsFromExternalAuthSystem(appId).then(function(res){
        		if(res.status == 200){
        	     confirmBoxService.showInformation('Sync operation completed successfully!').then(isConfirmed => {
        	    	 $scope.getRoleFunctions(appId);
        	     });          			
        		} else{
	        		 confirmBoxService.showInformation('Sync operation failed for '+app).then(isConfirmed => {});          			
        		}
        	});
        };
        
	$scope.saveRoleFunction = function (availableRoleFunction) {
		var uuu = conf.api.saveRoleFuncion;
		var postData = { availableRoleFunction: availableRoleFunction };
		$http.post(uuu, postData).then(function (response) {
			var data = response.data;
			$scope.availableRoleFunctions = data.availableRoleFunctions;
			$scope.editRoleFunction = null;
		}, function () {

			confirmBoxService.showInformation("Error while saving");
		});
	};

	$scope.removeRole = function (availableRoleFunction) {
		confirmBoxService.confirm("You are about to delete the role function " + availableRoleFunction.name + ". Do you want to continue?").then(function (confirmed) {
			if (confirmed) {
				$scope.availableRoleFunctionsTemp = $scope.availableRoleFunctions;
				var uuu = conf.api.removeRoleFunction.replace(':appId', $scope.apps.selectedCentralizedApp);
				var postData = availableRoleFunction;
				$http.post(uuu, postData).then(function (response) {
					if(response.data.status == 'OK'){
						confirmBoxService.showInformation(response.data.message);
						$scope.availableRoleFunctionsTemp.splice($scope.availableRoleFunctionsTemp.indexOf(availableRoleFunction), 1);
						$scope.availableRoleFunctions = $scope.availableRoleFunctionsTemp;
					} else{
						confirmBoxService.showInformation("Error while deleting: " + response.data.message);
					}
				}, function () {
					$scope.availableRoleFunctions = $scope.availableRoleFunctionsTemp;
				});
			}
		});
	};
});
