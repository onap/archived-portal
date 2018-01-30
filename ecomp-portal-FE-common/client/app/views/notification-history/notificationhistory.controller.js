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
       		 notificationService.getMessageRecipients(selectedAdminNotification.notificationId).then(res =>{
                    $scope.messageRecipients = res;
				 var messageObject=JSON.parse(selectedAdminNotification.msgDescription);
				  var modalInstance = $modal.open({
	                    templateUrl: 'app/views/user-notifications-admin/user.notifications.json.details.modal.page.html',
	                    controller: 'userNotificationCtrl',
	                    sizeClass: 'modal-large', 
	                    resolve: {
	    					items: function () {
	    						var items = {
	  				    			   title:    '',
	 	                       		    selectedAdminNotification:selectedAdminNotification,messageObject:messageObject,messageRecipients:$scope.messageRecipients
	  		                       		
	  		                           	};
	  				          return items;
	    			        	}
	    		        }
	                })
		     
    	 
      	 }).catch(err => {
               $log.error('userNotificationsCtrl:getMessageRecipients:: error ', err);
               $scope.isLoadingTable = false;
           });
			 };
         }
    }
    notificationHistoryCtrl.$inject = ['$scope', '$log', 'notificationService', 'confirmBoxService', '$modal', 'ngDialog', '$state'];
    angular.module('ecompApp').controller('notificationHistoryCtrl', notificationHistoryCtrl);
})();
