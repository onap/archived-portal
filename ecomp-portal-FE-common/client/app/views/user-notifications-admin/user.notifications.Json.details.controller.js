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
'use strict';

(function () {

    class userNotificationCtrl {
        constructor($scope,  ngDialog,items) {
    		$scope.messageData=items.text;
    		$scope.messageObject=items.messageObject;
    		$scope.selectedAdminNotification=items.selectedAdminNotification;
    		$scope.messageRecipients=items.messageRecipients;
    		$scope.notifiHyperlink=function(ticket){
     	    	window.open(ticket);
    		}
         }
    }
    userNotificationCtrl.$inject = ['$scope','ngDialog','items'];
    angular.module('ecompApp').controller('userNotificationCtrl', userNotificationCtrl);
})();



 
