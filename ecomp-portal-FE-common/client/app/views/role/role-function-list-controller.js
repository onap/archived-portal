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
app.controller('roleFunctionListController', function ($scope,RoleService,$http,$state,conf,confirmBoxService, ngDialog,$modal){
	$( "#dialog" ).hide();
	$scope.isLoadingRoleFunctions = true;
	RoleService.getRoleFunctionList().then(function(data){
		
		var j = data;
  		$scope.data = JSON.parse(j.data);
  		$scope.availableRoleFunctions =JSON.parse($scope.data.availableRoleFunctions);
  		//$scope.resetMenu();
	
	},function(error){
		console.log("failed");
		//reloadPageOnce();
	}) .finally(function(){
		$scope.isLoadingRoleFunctions = false;
	});
	
	$scope.editRoleFunction = null;
	var dialog = null;
	$scope.editRoleFunctionPopup = function(availableRoleFunction) {
		$scope.editRoleFunction = availableRoleFunction;
		$( "#dialog" ).dialog({
		      modal: true
	    });
	};
	
	$scope.editRoleFunctionModalPopup = function(availableRoleFunction) {
		$scope.editRoleFunction = availableRoleFunction;
		$scope.availableRoleFunctionsTemp=$scope.availableRoleFunctions;
		//$scope.availableRoleFunctions={};
		
		 var modalInstance = $modal.open({
             templateUrl: 'app/views/role/popup_modal_rolefunction.html',
             controller: 'rolefunctionpopupController',
             sizeClass: 'modal-small', 
             resolve: {
 		    	message: function () {
 		    		var message = {
 		    				availableRoleFunction:  $scope.editRoleFunction,
 		    				availableRoleFunctions: $scope.availableRoleFunctionsTemp
                            	};
 		          return message;
 		        },
 		        isEditing: function () {
 		        	return true;
 		        }
 		      }
 		  });
         
         modalInstance.result.then(function (response) {
        	 if(response.value!=null){
             	if(response.value.result){
             		$scope.availableRoleFunctions=response.value.availableRoleFunctions;	
             	}
             }            	
             else
             	$scope.availableRoleFunctions=$scope.availableRoleFunctionsTemp;
             
         });
	};
	
	$scope.addNewRoleFunctionModalPopup = function(availableRoleFunction) {
		
		$scope.editRoleFunction = null;
		$scope.availableRoleFunctionsTemp=$scope.availableRoleFunctions;
		//$scope.availableRoleFunctions={};
		 var modalInstance = $modal.open({
             templateUrl: 'app/views/role/popup_modal_rolefunction.html',
             controller: 'rolefunctionpopupController',
             sizeClass: 'modal-small', 
             resolve: {
 		    	message: function () {
 		    		var message = {
 		    				availableRoleFunction: $scope.editRoleFunction,
 		    				availableRoleFunctions: $scope.availableRoleFunctionsTemp
                            	};
 		          return message;
 		        },
 		        isEditing: function () {
 		        	return false;
 		        }
 		      }
 		  });
         
         modalInstance.result.then(function (response) {
         	if(response.value!=null){
         	if(response.value.result){
        		$scope.availableRoleFunctions=response.value.availableRoleFunctions;	
        	}
        }
        if(response.availableRoleFunctions != undefined)
        	$scope.availableRoleFunctions=response.availableRoleFunctions;
        else
        	$scope.availableRoleFunctions=$scope.availableRoleFunctionsTemp;
        	
    });
	};
	
	$scope.addNewRoleFunctionPopup = function() {
		$scope.editRoleFunction = null;
		$( "#dialog" ).dialog({
		      modal: true
	    });
	};
	
	$scope.saveRoleFunction = function(availableRoleFunction) {
		  var uuu = conf.api.saveRoleFuncion;
		  var postData={availableRoleFunction: availableRoleFunction};
		  $http.post(uuu,postData).then(function(response) {
			  var data = response.data;
	  		$scope.availableRoleFunctions=data.availableRoleFunctions; 
	  		$scope.editRoleFunction = null;
	  	  },	  	  
	  	function() {
	  		
	  		confirmBoxService.showInformation("Error while saving");
	  	  }
	  	  );
		};
	
		
		$scope.removeRole = function(availableRoleFunction) {
			confirmBoxService.confirm("You are about to delete the role function "+availableRoleFunction.name+". Do you want to continue?").then(
			function(confirmed){
			if(confirmed){
			$scope.availableRoleFunctionsTemp=$scope.availableRoleFunctions;
			//$scope.availableRoleFunctions={};
			var uuu = conf.api.removeRoleFunction;
			var postData=availableRoleFunction;
			$http.post(uuu,postData).then(function(response) {
			var data = response.data;
			if(data == undefined)
			confirmBoxService.showInformation("Error while deleting: "+ data);
			else
			$scope.availableRoleFunctionsTemp.splice($scope.availableRoleFunctionsTemp.indexOf(availableRoleFunction), 1);
			$scope.availableRoleFunctions=$scope.availableRoleFunctionsTemp;
			},
			function() {
			$scope.availableRoleFunctions=$scope.availableRoleFunctionsTemp;
			confirmBoxService.showInformation("Error while deleting: "+ data.responseText);
			}
			);
			}
			});

			};

});
