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
        constructor($scope, $log, notificationService, filterFilter,confirmBoxService, $modal, ngDialog, $state) {

        	var priorityItems={"1":"Normal","2":"Important"};
        	$scope.priorityItems=priorityItems;
        	$scope.searchString='';
            $scope.externalNotification="External System";
        	$scope.itemExpired={"background-color":"silver "};
        	$scope.showInput = true;
        	$scope.totalPages1 = 0;
        	$scope.viewPerPage1 = 15;
        	$scope.currentPage1 = 1;
            $scope.startIndex=0;
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
    			$scope.tableAdminNotifItems = $scope.tableData; 
            	}).catch(err => {
                $log.error('userNotificationsCtrl:getAdminNotifications:: error ', err);
                $scope.isLoadingTable = false;
            });
             }
        	 
        	
      	   getAdminNotifications();           
      	   
        	
        	  $scope.$watch('searchString', function (searchKey) {
                  var search = searchKey;               
                  this.totalPage1 = filterFilter($scope.tableData, search);
                  var resultLen = this.totalPage1.length;
                  $scope.totalPage1 = Math.ceil(resultLen/$scope.viewPerPage1);
                  $scope.currentPage1 = 1;
              });
          
        	  $scope.updateTable = (num) => {
                  this.startIndex=this.viewPerPage1*(num-1);
                  this.currentPage1 = num;
              };
           	
           this.removeUserNotification = (selectedAdminNotification) => {
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
           

          
           this.showDetailedJsonMessage = (selectedAdminNotification) => {
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
    
			 
            this.editUserNotificationModal = (selectedAdminNotification) => {

                // retrieve roleIds here
                selectedAdminNotification.roleIds = null;
                notificationService.getNotificationRoles(selectedAdminNotification.notificationId)
                    .then(res => {
                        selectedAdminNotification.roleIds = res.data;

                        this.openUserNotificationModal(selectedAdminNotification);
                    }).catch(err => {
                        $log.error('UserNotifCtlr:getNotificationRoles:: error ', err);

                    });
            }

            this.openUserNotificationModal = (selectedAdminNotification) => {
                let data = null;
                if (selectedAdminNotification) {
                    data = {
                        notif: selectedAdminNotification
                    }
                }
             
                
                var modalInstance = $modal.open({
                    templateUrl: 'app/views/user-notifications-admin/user.notifications.modal.page.html',
                    controller: 'userNotificationsModalCtrl as userNotifModal',
                    sizeClass: 'modal-large', 
                    resolve: {
    					items: function () {
        	        	  return data;
    			        	}
    		        }
                })
                
                modalInstance.result.finally(function () {
                		getAdminNotifications();
     	        });
            }
            $scope.customPageHandler = function(num) {
        		$scope.currentPage1=num;
        		this.startIndex=$scope.viewPerPage1*(num-1);
        	};
        	  
        }
    }
    userNotificationsCtrl.$inject = ['$scope', '$log', 'notificationService','filterFilter', 'confirmBoxService', '$modal', 'ngDialog', '$state'];
    angular.module('ecompApp').controller('userNotificationsCtrl', userNotificationsCtrl);
})();
