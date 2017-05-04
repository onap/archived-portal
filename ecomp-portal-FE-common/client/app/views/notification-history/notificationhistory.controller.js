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

    class notificationHistoryCtrl {
        constructor( $scope, $log,notificationService, confirmBoxService, $modal, ngDialog, $state) {
        	
        	var priorityItems={"1":"Normal","2":"Important"};
        	$scope.priorityItems=priorityItems;
            $scope.externalNotification="External System";
        	$scope.isLoadingTable = false;
            $scope.searchString = '';
            $scope.notificationHistory = [];
            let getNotificationHistory = () => {
            	$scope.isLoadingTable = true;
                notificationService.getNotificationHistory().then(res => {
                    $scope.notificationHistory = res.data;
                    $scope.isLoadingTable = false;
                }).catch(err => {
                    $log.error('notificationHistoryCtlr:notifSvc.getNotifHist failed: ', err);
                    $scope.isLoadingTable = false;
                });
            }

            getNotificationHistory();           
        	
            $scope.showDetailedJsonMessage=function (selectedAdminNotification) {
				 var messageObject=JSON.parse(selectedAdminNotification.msgDescription);
				 var html="";
				 html+='<p>'+'Message Source'+' : '+selectedAdminNotification.msgSource+'</p>';
				 html+='<p>'+'Message Title'+' : '+selectedAdminNotification.msgHeader+'</p>';
				  for(var field in  messageObject){
					 if(field=='eventDate'||field=='lastModifiedDate'){
						 html+='<p>'+field+' : '+new Date(+messageObject[field])+'</p>';
						  
					 }else{
					 html+='<p>'+field+' : '+messageObject[field]+'</p>';
					 
					 }
				 }
			
		     var modalInstance = ngDialog.open({
				    templateUrl: 'app/views/user-notifications-admin/user.notifications.Json.details.modal.page.html',
				    controller: 'userNotificationCtrl',
				    resolve: {
				    	message: function () {
				    		var message = {
				    			   title:    '',
		                       		text:    html
		                       		
		                           	};
				          return message;
				        },
				     
				      }
				  }); 
			
				 
			 };
         }
    }
    notificationHistoryCtrl.$inject = ['$scope', '$log', 'notificationService', 'confirmBoxService', '$modal', 'ngDialog', '$state'];
    angular.module('ecompApp').controller('notificationHistoryCtrl', notificationHistoryCtrl);
})();
