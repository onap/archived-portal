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
	
    class UserbarCtrl {
        constructor(userProfileService,userbarUpdateService, $log, $rootScope , $interval,$scope,$timeout,dashboardService) {
            this.$log = $log;
            this.userProfileService = userProfileService;
            this.$rootScope = $rootScope;
            $rootScope.isAdminPortalAdmin = false;
            $scope.updateRate = 10000; //default online user bar refreshing rate
            var intervalPromise = null;
            $scope.myservice = userbarUpdateService;
            $scope.userList=this.userLists;
            var websocket = '';
            var currentUser = '';
            var remoteUser = '';
            var f = '';
           
        	
            function socketSetup(initialPageVisit,_currentUser, _remoteUser, _f) {
            	


            	if( true) {
            		  
            		var href = window.location.href;
            	    var hostPatt = new RegExp(window.location.host +"/[^/]*");
            	    var res = hostPatt.exec(href);
            	    var protocol = window.location.protocol.replace("http","ws");
            	      var signalingServerPath = protocol + "//" + res + "/opencontact";
            		  var wsUri = signalingServerPath;
            		  console.log("Connecting to " + wsUri);
            		  websocket = new WebSocket(wsUri);
            		  //localStorage.notifySocket = JSON.stringify(websocket);
            		  //window.top.notifySocket = websocket;
            		  
            		  currentUser = _currentUser;
            		  remoteUser = _remoteUser;
            		  f = socketSend;
            		 
            		  
            	}
            	
            	//var websocket = JSON.parse(localStorage.notifySocket || "{}") ;
            	if( websocket != null) {
            		websocket.onopen = function(evt) { onOpen(evt); };
            		websocket.onmessage = function(evt) { onMessage(evt); };
            		websocket.onerror = function(evt) { onError(evt); };
            	
            	}
            	
            	//if(f != undefined)
            	 // f();
            	
            	//window.top.notifySocket.send("");
            }
            
            function socketSend(currentUser, remoteUser) {
            	


           	 websocket.send(JSON.stringify({
                    from: currentUser,
                    to: remoteUser
                }));
           	
           	//window.top.notifySocket.send("");
           }




           function onOpen(evt) {
               console.log("onOpen");
               //writeToScreen("CONNECTED");
               
               if(f != undefined)
           		f(currentUser, remoteUser);
               
           }

           function onMessage(evt) {
               if (typeof evt.data == "string") {
                   writeToScreen("RECEIVED (text): " + evt.data);
                   var chatObject = JSON.parse(evt.data);
                   if(confirm("User " + chatObject.from + " is requesting a chat session with you. Please click ok to accept")== true) {
                	   
                	   var url = userProfileService.getSortedUserIdCombination(chatObject.from, chatObject.to);
                	   var win = window.open(url, '_blank');
	              	   win.focus();
                   } else {
                	   //
                   }
                
                   
               } else {
                   writeToScreen("RECEIVED (binary): " + evt.data);
               }
           }

           function onError(evt) {
               writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
           }

           function writeToScreen(message) {
        	   console.log(message);
           }
            
            function callAtInterval() {
            	userProfileService.getActiveUser()
                .then(res=> {
            		$log.info('sasasasasa getting userinfo from shared context.. ');
                	if(res==null){
                		$log.info('failed getting userinfo from shared context.. ');
                		$log.info('getting userinfo from session ');
                	}else{
                		$log.info('getting ActiveUser successfully',res);   
                		$scope.userList = [];
                		for(var i=0;i<res.length;i++){
                			var data= {
                					userId:res[i][0],
                					linkQ:userProfileService.getSortedUserIdCombination(userProfileService.userProfile.userId , res[i][0])
                			}
                			$scope.userList.push(data);
                		}      		
                		$log.info(' ActiveUser =',$scope.userList);   		
                	}   	
                }).catch(err=> {
                	$log.error('Header Controller:: getActiveUser() failed: ' + err);
                }).finally(() => {
                   	var footerOff = $('#online-userbar').offset().top;
            		var headOff = $('#footer').offset().top;
            		var userbarHeight= parseInt($(".online-user-container").css('height'),10);
            		var defaultOffSet = 45;
            		$(".online-user-container").css({
        				"height" : headOff - footerOff-defaultOffSet
        			});
                });      
            }
            
            
            var intervalPromise;
            dashboardService.getOnlineUserUpdateRate().then(res=> {
            	// $log.info('getting Online User Update Rate init***********************', res);
            	if (res == null || res.response == null) {
            		$log.error('UserbarCtlr: failed to get online user update rate!');
            	} else {
           			// $log.debug('UserbarCtlr: got online User Update Rate ' + res.response);            		
            		var rate = parseInt(res.response.onlineUserUpdateRate);
					// var updateRate = parseInt(res.response.onlineUserUpdateRate);
					var duration = parseInt(res.response.onlineUserUpdateDuration);
					userbarUpdateService.setMaxRefreshCount(parseInt(duration/rate)+1);
					userbarUpdateService.setRefreshCount(userbarUpdateService.maxCount);
					// $scope.refreshCount = userbarUpdateService.getRefreshCount();

           			if (rate != NaN && duration != NaN) {
           				// $log.debug('UserbarCtlr: scheduling function at interval ' + millis);
						$scope.updateRate=rate;
						$scope.start($scope.updateRate);
           			}                 			
            	}
            }).catch(err=> {
            	$log.error('Header Controller:: getOnlineUserUpdateRate() failed: ' + err);
            });
            
            $scope.start = function(rate) {
				// stops any running interval to avoid two intervals running at the same time
				$scope.stop(); 	
				// store the interval promise
				intervalPromise = $interval(updateActiveUsers, rate);
			};

			$scope.stop = function() {
				$interval.cancel(intervalPromise);
			};
		
			$scope.$watch('myservice.getRefreshCount()', function (newVal,oldVal) {
				//$log.info("refresh "+$scope.myservice.refreshCount + " more time(s)");
				if (newVal<=0) {
					// $log.info("UserbarCtrl: stopped refreshing online users");
					$scope.stop();
				} else if (newVal== $scope.myservice.maxCount){
					// $log.info("UserbarCtrl: start refreshing online users at every "+($scope.updateRate/1000)+" sec");
					// initial update of activate users
					userProfileService.getUserProfile().then(res=> { 
						if (res == null || res.orgUserId == null) {
							$log.error('UserbarCtlr: failed to get profile of the user!');
						}
						else {
							updateActiveUsers();
							$scope.start($scope.updateRate);
							socketSetup(null, res.orgUserId, null, "socketSend");
						}
					});
					
					
				}
			});
		function updateActiveUsers() {
            userProfileService.getActiveUser()
            .then(res=> {
            	$log.info('getting Active User init***********************');
            	// decrement the counter every time updateActiveUser is called;
				userbarUpdateService.decrementRefreshCount();
            
            	if(res==null ||res.status!='OK'){
            		$log.error('failed updateActiveUsers res is null.. ');
            		
            	}else{
            		$log.info('getting ActiveUser successfully',res);   
            		$scope.userList = [];
            		var maxItems = 10;
            		var users = res.response;
            		if (users.length < maxItems)
            			maxItems = users.length;
            		for(var i=0;i<maxItems;i++){
            			var data= {
            					userId:users[i][0],
            					linkQ:userProfileService.getSortedUserIdCombination(userProfileService.userProfile.orgUserId , users[i][0])
            			}
            			$scope.userList.push(data);
            		}
            		
            		$log.info(' ActiveUser =',$scope.userList);   
            	}   	
            }).catch(err=> {
            	$log.error('Header Controller:: getActiveUser() failed: ' + err);
            }).finally(() => {
               	var footerOff = $('#online-userbar').offset().top;
        		var headOff = $('#footer').offset().top;
        		var userbarHeight= parseInt($(".online-user-container").css('height'),10);
        		var defaultOffSet = 45;
        		$(".online-user-container").css({
    				"height" : headOff - footerOff-defaultOffSet
    			});
            });
		}
        }
    }
    UserbarCtrl.$inject = ['userProfileService', 'userbarUpdateService', '$log', '$rootScope','$interval','$scope','$timeout','dashboardService'];
    angular.module('ecompApp').controller('UserbarCtrl', UserbarCtrl);
})();
