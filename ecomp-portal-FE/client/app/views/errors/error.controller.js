/*-
 * ================================================================================
 * eCOMP Portal
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
'use strict';
(function () {
    class ErrorCtrl {
        constructor($scope, $window) {

        	$scope.errorMsg = '';
        	
        	if(window.location.href.indexOf('noUserError') !== -1){
        		$scope.errorMsg = 'You do not have a user account for this application. Please contact your system administrator';
        	} else if(window.location.href.indexOf('unKnownError') !== -1){
        		$scope.errorMsg = 'An unknown error has occurred. Please contact your system administrator';
        	}
        }
    }
    ErrorCtrl.$inject = ['$scope','$window'];
    angular.module('ecompApp').controller('ErrorCtrl', ErrorCtrl);
})();
