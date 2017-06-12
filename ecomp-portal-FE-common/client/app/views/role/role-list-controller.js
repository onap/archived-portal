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
app.controller('roleListController', function ($scope,RoleService,confirmBoxService,conf,$state,$http){
	$scope.showSpinner = true;
	
	RoleService.getRoles().then(function(data){
		$scope.showSpinner = true;
		var j = data;
  		$scope.data = JSON.parse(j.data);
  		$scope.availableRoles =JSON.parse($scope.data.availableRoles);
  		$scope.showSpinner = false;
  		//$scope.resetMenu();
	
	},function(error){
		console.log("failed");
		//reloadPageOnce();
	});
	
	
		$scope.goToUrl = function(roleIdVal) {
			$state.go("root.role", {"roleId":roleIdVal});
		}	
	//console.log($scope.availableRoles);
		$scope.toggleRole = function(selected,availableRole) {
				//alert('toggleRole: '+selected);
				var toggleType = null;
				if(selected) {
					toggleType = "activate";
				} else {
					toggleType = "inactivate";
				}
				
				confirmBoxService.confirm("You are about to "+toggleType+" the role "+availableRole.name+". Do you want to continue?").then(
		    			function(confirmed){
		    				
		    				 if(confirmed) {
			                    var uuu = conf.api.toggleRole;
								
								var postData={role:availableRole};
								$http.post(uuu, postData).then(function(response) {
									var data = response.data;
									if (typeof data === 'object') {
										console.log(data);
							  			$scope.availableRoles=data.availableRoles; 
							  			console.log($scope.availableRoles);
									} else {
										//
									}

								}, function(response) {
									console.log(response.data);
									availableRole.active=!availableRole.active;
									confirmBoxService.showInformation("Error while saving.");
								});
								
								/*
							  	  $.ajax({
							  		 type : 'POST',
							  		 url : uuu,
							  		 dataType: 'json',
							  		 contentType: 'application/json',
							  		 data: JSON.stringify(postData),
							  		 success : function(data){
							  			console.log(data);
							  			$scope.$apply(function(){$scope.availableRoles=data.availableRoles;}); 
							  			console.log($scope.availableRoles);
									 },
									 error : function(data){
										 console.log(data);
										 availableRole.active=!availableRole.active;
										 confirmBoxService.showInformation("Error while saving.");
									 }
							  	  });
							  	  */
		    				 }
		    				 else {
		    					 availableRole.active=!availableRole.active;
		    				 }
					
		    	});
		    	//,
		    	//function(){
		    //		availableRole.active=!availableRole.active;
		    	//})
				
				  
		};

		$scope.removeRole = function(role) {
			
			confirmBoxService.confirm("You are about to delete the role "+role.name+". Do you want to continue?").then(
	    			function(confirmed){
							var uuu = conf.api.removeRole;
							  var postData={role:role};
						  /*	  $.ajax({
						  		 type : 'POST',
						  		 url : uuu,
						  		 dataType: 'json',
						  		 contentType: 'application/json',
						  		 data: JSON.stringify(postData),
						  		 success : function(data){
						  			$scope.$apply(function(){$scope.availableRoles=data.availableRoles;});  
								 },
								 error : function(data){
									 console.log(data);
									 confirmBoxService.showInformation("Error while deleting: "+ data.responseText);
								 }
						  	  }); */
							  
							  
							  $http.post(uuu, postData).then(function(response) {
									var data = response.data;
									if (typeof data === 'object') {
							  			$scope.availableRoles=data.availableRoles; 
									} else {
										//
									}

								}, function(response) {
									console.log(response.data);
									confirmBoxService.showInformation("Error while deleting: "+ data.responseText);
								});
				
	    	});
	    	
			
		};
		

});
