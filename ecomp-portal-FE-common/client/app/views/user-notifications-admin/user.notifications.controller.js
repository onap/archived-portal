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

    class userNotificationsCtrl {
        constructor($scope, $log, notificationService, confirmBoxService, $modal, ngDialog, $state) {

        	var priorityItems={"1":"Normal","2":"Important"};
        	$scope.priorityItems=priorityItems;
        	$scope.searchString='';
            $scope.externalNotification="External System";
        	$scope.itemExpired={"background-color":"silver "};
        	$scope.showInput = true;
        	$scope.totalPages1 = 0;
        	$scope.viewPerPage1 = 15;
        	$scope.currentPage1 = 1;
        	$scope.showLoader = false;
        	$scope.firstPlay = true;
        	// Start with empty list to silence error in console
        	$scope.tableData = [];
        	$scope.tableAdminNotifItems = [];
        	 let getAdminNotifications = () => {
               $scope.isLoadingTable = true;
        	notificationService.getAdminNotification().then(res => {
                $scope.adminNotifications = res.data;
                $scope.isLoadingTable = false;
                $scope.tableData = res.data;
    			var totalItems = $scope.tableData.length;
    			$scope.totalPages1  = Math.ceil(totalItems / $scope.viewPerPage1);
    			$scope.showLoader = false;
    			$scope.currentPage1=1;
    			var endIndex = 1 * $scope.viewPerPage1;
    			var startIndex = endIndex - $scope.viewPerPage1;
    			$scope.tableAdminNotifItems = $scope.tableData.slice(startIndex, endIndex); 
            	}).catch(err => {
                $log.error('userNotificationsCtrl:getAdminNotifications:: error ', err);
                $scope.isLoadingTable = false;
            });
             }
        	 
      	   getAdminNotifications();           

        	$scope.customPageHandler = function(num) {
        		$scope.currentPage1=num;
        		var endIndex = num * $scope.viewPerPage1;
        		var startIndex = endIndex - $scope.viewPerPage1;
        		$scope.tableAdminNotifItems = $scope.tableData.slice(startIndex, endIndex);
        	};
        	  

          

           	
           $scope.removeUserNotification = function (selectedAdminNotification) {
                selectedAdminNotification.activeYn = 'N';
                confirmBoxService.deleteItem(selectedAdminNotification.msgHeader)
                    .then(isConfirmed => {
                        if (isConfirmed) {
                            notificationService.updateAdminNotification(selectedAdminNotification)
                                .then(() => {
                                    getAdminNotifications();
                                }).catch(err => {
                                    switch (err.status) {
                                        case '409':         // Conflict
                                            // handleConflictErrors(err);
                                            break;
                                        case '500':         // Internal Server
															// Error
                                            confirmBoxService.showInformation('There was a problem updating the notification. ' +
                                                'Please try again later. Error: ' + err.status).then(isConfirmed => { });
                                            break;
                                        case '403':         // Forbidden...
															// possible
															// webjunction error
															// to try again
                                            confirmBoxService.showInformation('There was a problem updating the notification. ' +
                                                'Please try again. If the problem persists, then try again later. Error: ' + err.status).then(isConfirmed => { });
                                            break;
                                        default:
                                            confirmBoxService.showInformation('There was a problem updating the notification. ' +
                                                'Please try again. If the problem persists, then try again later. Error: ' + err.status).then(isConfirmed => { });
                                    }
                                    $log.error('UserNotifCtlr::updateAdminNOtif failed: ' + JSON.stringify(err));
                                }).finally(() => {
                                    var objOffsetVersion = objAgent.indexOf("MSIE");
                                    if (objOffsetVersion != -1) {
                                        $window.location.reload();       
                                    }
                                });
                        }
                    }).catch(err => {
                        $log.error('UserNotifCtlr::deleteItem error: ' + err);
                    });
            }
           

          
        	 $scope.showDetailedJsonMessage=function (selectedAdminNotification) {
        		 notificationService.getMessageRecipients(selectedAdminNotification.notificationId).then(res =>{
                     $scope.messageRecipients = res;
				 var messageObject=JSON.parse(selectedAdminNotification.msgDescription);
				 var html="";
				 html+='<p>'+'Message Source'+' : '+selectedAdminNotification.msgSource+'</p>';
				 html+='<p>'+'Message Title'+' : '+selectedAdminNotification.msgHeader+'</p>';
				 html+='<p>'+'Message Recipient'+' : '+$scope.messageRecipients+'</p>';

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
 		     
        	 }).catch(err => {
                 $log.error('userNotificationsCtrl:getMessageRecipients:: error ', err);
                 $scope.isLoadingTable = false;
             });

 			 };
    
			 
            $scope.editUserNotificationModal = function (selectedAdminNotification) {

                // retrieve roleIds here
                selectedAdminNotification.roleIds = null;
                notificationService.getNotificationRoles(selectedAdminNotification.notificationId)
                    .then(res => {
                        selectedAdminNotification.roleIds = res.data;

                        $scope.openUserNotificationModal(selectedAdminNotification);
                    }).catch(err => {
                        $log.error('UserNotifCtlr:getNotificationRoles:: error ', err);

                    });
            }

            $scope.openUserNotificationModal = function (selectedAdminNotification) {
                let data = null;
                if (selectedAdminNotification) {
                    data = {
                        notif: selectedAdminNotification
                    }
                }
                ngDialog.open({
                    templateUrl: 'app/views/user-notifications-admin/user.notifications.modal.page.html',
                    controller: 'userNotificationsModalCtrl',
                    controllerAs: 'userNotifModal',
                    data: data
                }).closePromise.then(function (needUpdate) {
                    getAdminNotifications();
                });
            }

        }
    }
    userNotificationsCtrl.$inject = ['$scope', '$log', 'notificationService', 'confirmBoxService', '$modal', 'ngDialog', '$state'];
    angular.module('ecompApp').controller('userNotificationsCtrl', userNotificationsCtrl);
})();
