'use strict';
(function () {
    class ErrorCtrl {
        constructor($log, $scope,$state) {
        	$scope.errorState = $state.current.name;
        	if($scope.errorState=='root.error404'){
        		$scope.errorTitle="Page Not Found";
        		$scope.errorMsg="The page you are looking for cannot be found";
        	}else if($scope.errorState=='noUserError'){
        		$scope.errorTitle="Authorization denied";
        		$scope.errorMsg= "Please Contact Your Administrator for the page access";
        	}else {
        		$scope.errorTitle="Something went wrong";
        		$scope.errorMsg= "Please go back to the previous page";
        	}	     	
        }
    }
    ErrorCtrl.$inject = ['$log','$scope','$state'];
    angular.module('ecompApp').controller('ErrorCtrl', ErrorCtrl);
})();