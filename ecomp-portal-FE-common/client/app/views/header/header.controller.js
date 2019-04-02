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
 * 
 */
'use strict';
(function () {
	class HeaderCtrl {
    constructor($log, $window, $translate, translateService,userProfileService, menusService, $scope, ECOMP_URL_REGEX, $cookies, $state,auditLogService,notificationService,ngDialog,$modal) {
      this.firstName = '';
      this.lastName = '';
      this.$log = $log;
      this.menusService = menusService;
      this.$scope = $scope;
      this.favoritesMenuItems = '';
      $scope.cur_lang = '';
      $scope.langList = []
      $scope.favoriteItemsCount = 0;
      $scope.favoritesMenuItems = '';
      $scope.showFavorites = false;
      $scope.emptyFavorites = false;
      $scope.favoritesWindow = false;
      $scope.notificationCount=0;
      $scope.showNotification = true;
      // get all languages
      var loginId = sessionStorage.getItem('userId')
      translateService.getCurrentLang(loginId).then(res => {
        $scope.cur_lang = res.languageAlias
        $translate.use($scope.cur_lang);
      })
      translateService.getLangList().then(res => {
        $scope.langList = res.languageList
      })
      // switch language
      $scope.switching = function(lang) {
        $translate.use(lang);
        var langs = $scope.langList
        var langId = ''
        var selectedLang = langs.find(function(item) {
          return item.languageAlias === lang;
        });
        langId = selectedLang.languageId
        var loginId = sessionStorage.getItem('userId')
        translateService.saveSelectedLang(loginId, {'languageId': langId}).then(res => {
          window.location.reload();
        })
      }
      $scope.cur_lang = $translate.use();

            $scope.hideMenus = false;

            $scope.menuItems = [];
            $scope.activeClickSubMenu = {
                x: ''
            }; 
            $scope.activeClickMenu = {
                x: ''
            };
            $scope.megaMenuDataObject =[];
            $scope.notificationCount= notificationService.notificationCount;
            this.isLoading = true;
            this.ECOMP_URL_REGEX = ECOMP_URL_REGEX;
            
            var unflatten = function( array, parent, tree ){
            
                tree = typeof tree !== 'undefined' ? tree : [];
                parent = typeof parent !== 'undefined' ? parent : { menuId: null };
                var children = _.filter( array, function(child){ return child.parentMenuId == parent.menuId; });
                
                if( !_.isEmpty( children )  ){
                  if( parent.menuId === null ){
                    tree = children;
                  }else{
                    parent['children'] = children
                  }
                  _.each( children, function( child ){ unflatten( array, child ) } );
                }
            
                return tree;
            }
            
            userProfileService.getFunctionalMenuStaticInfo()
            .then(res=> {
            	if(res==null || res.firstName==null || res.firstName=='' || res.lastName==null || res.lastName=='' ){
            		$log.info('HeaderCtrl: failed to get all required data, trying user profile');
            		userProfileService.getUserProfile()
                    .then(profile=> {
                        this.firstName = profile.firstName;
                        this.lastName = profile.lastName;                     
                    }).catch(err=> {
                        $log.error('Header Controller:: getUserProfile() failed: ' + err);
                    });
            	} else {
            		this.firstName = res.firstName;
            		this.lastName = res.lastName;           	   	  	  
            	}

            	menusService.GetFunctionalMenuForUser()
            	.then(jsonHeaderMenu=> {
            		$scope.menuItems = unflatten( jsonHeaderMenu );
            		$scope.megaMenuDataObject = $scope.menuItems;
            	}).catch(err=> {
            		$log.error('HeaderCtrl::GetFunctionalMenuForUser: HeaderCtrl json returned: ' + err);
            	});      
            	
            }).catch(err=> {
            	$log.error('HeaderCtrl::getFunctionalMenuStaticInfo failed: ' + err);
            });
            
          //store audit log
            $scope.auditLog = function(app,type) {
            	var comment = 'type: '+type+ ',title: '+app.text+",url: "+app.url;
        		auditLogService.storeAudit(app.appid,'functional',comment);
        	};

            $scope.loadFavorites = function () {
                $scope.hideMenus = false;
                if ($scope.favoritesMenuItems == '') {
                    generateFavoriteItems();
                }
            }

            $scope.goToUrl = (item) =>  {
                let url = item.url;
                let restrictedApp = item.restrictedApp;
                if (!url) {
                    $log.warn('HeaderCtrl::goToUrl: No url found for this application, doing nothing..');
                    return;
                }
                if (restrictedApp) {
                    $window.open(url, '_blank');
                } else {
                	if(item.url=="getAccess" || item.url=="contactUs"){
                		$state.go("root."+url);
                	} else {
                    	var tabContent = { id: new Date(), title: item.text, url: item.url,appId:item.appid };
                    	$cookies.putObject('addTab', tabContent );
                    }
                }
                $scope.hideMenus = true;
            }
            
            
            
            $scope.submenuLevelAction = function(index, column) {           
                if (index=='Favorites' && $scope.favoriteItemsCount != 0) {
                    $scope.favoritesWindow = true;
                    $scope.showFavorites = true;
                    $scope.emptyFavorites = false;
                }
                if (index=='Favorites' && $scope.favoriteItemsCount == 0) {
                    $scope.favoritesWindow = true;
                    $scope.showFavorites = false;
                    $scope.emptyFavorites = true;
                }
                if (index!='Favorites' ) {
                    $scope.favoritesWindow = false;
                    $scope.showFavorites = false;
                    $scope.emptyFavorites = false;
                }
            };
            
            $scope.hideFavoritesWindow = function() {
                $scope.showFavorites = false;
                $scope.emptyFavorites = false;
            }
            
            $scope.isUrlFavorite = function (menuId) {
                var jsonMenu =  JSON.stringify($scope.favoritesMenuItems);
                var isMenuFavorite =  jsonMenu.indexOf('menuId\":' + menuId);
                if (isMenuFavorite==-1) {
                    return false;
                } else {
                    return true;
                }
            }
            
            /*Getting Ecomp portal Title*/

            let getEcompPortalTitle  = () => {
            	menusService.getEcompPortalTitle()
            	.then(title=> {
            		$scope.ecompTitle = title.response;
            	}).catch(err=> {
            		$log.error('HeaderCtrl.getEcompPortalTitle:: Error retrieving ECMOP portal title: ' + err);
            	});
            }
            getEcompPortalTitle();
            
            let generateFavoriteItems  = () => {
                menusService.getFavoriteItems()
                    .then(favorites=> {
                        $scope.favoritesMenuItems = favorites;
                        $scope.favoriteItemsCount = Object.keys(favorites).length;
                    }).catch(err=> {
                        $log.error('HeaderCtrl.getFavoriteItems:: Error retrieving Favorites menus: ' + err);
                });
            }

           $scope.setAsFavoriteItem =  function(event, menuId){
               var jsonMenuID = angular.toJson({'menuId': + menuId });
               menusService.setFavoriteItem(jsonMenuID)
               .then(() => {
                   angular.element('#' + event.target.id).css('color', '#fbb313');
                   generateFavoriteItems();
               }).catch(err=> {
                   $log.error('HeaderCtrl::setFavoriteItems:: API setFavoriteItem error: ' + err);
               });
           };

            $scope.removeAsFavoriteItem =  function(event, menuId){
                menusService.removeFavoriteItem(menuId)
                .then(() => {
                    angular.element('#' + event.target.id).css('color', '#666666');
                    generateFavoriteItems();
                }).catch(err=> {
                    $log.error('HeaderCtrl::removeAsFavoriteItem: API removeFavoriteItem error: ' + err);
                });
            };

            $scope.goToPortal = (headerText, url) => {
                if (!url) {
                    $log.warn('HeaderCtrl::goToPortal: No url found for this application, doing nothing..');
                    return;
                }
                if (!ECOMP_URL_REGEX.test(url)) {
                    url = 'http://' + url;
                }

                if(headerText.startsWith("vUSP")) {
                	window.open(url, '_blank');//, '_self'
                }
                else {
                	var tabContent = { id: new Date(), title: headerText, url: url };
                	$cookies.putObject('addTab', tabContent );
                }
            };
            
            $scope.editProfile = () => {
                let data = null;
               
                ngDialog.open({
                    templateUrl: 'app/views/header/profile-edit-dialogs/profile-edit.modal.html',
                    controller: 'EditProfileModalCtrl',
                    controllerAs: 'profileDetail',
                    data: ''
                }).closePromise.then(needUpdate => {
                    if(needUpdate.value === true){
                        updateTableData();
                    }
                });
            };
        }
    }
    class LoginSnippetCtrl {
        constructor($log, $scope, $cookies, $timeout, userProfileService, sessionService) {
            $scope.firstName="";
            $scope.lastName="";
            $scope.displayUserAppRoles=false; 
            $scope.allAppsLogout = function(){
            	
            	var cookieTabs = $cookies.getObject('visInVisCookieTabs');
             	if(cookieTabs!=null){
             		for(var t in cookieTabs){
             		
             			var url = cookieTabs[t].content;
             			if(url != "") {
             				sessionService.logout(url);
             	      	}
             		}
             	}
             	// wait for individual applications to log out before the portal logout
             	$timeout(function() {
             		window.location = "logout.htm";
             	}, 2000);
            }
            
            
            try {
            	userProfileService.getFunctionalMenuStaticInfo()
                .then(res=> {
              	  $scope.firstName = res.firstName;
              	  $scope.lastName = res.lastName;
              	  $scope.loginSnippetEmail = res.email;
              	  $scope.loginSnippetUserid = res.userId;
              	  $scope.lastLogin = Date.parse(res.last_login);
                }).catch(err=> {
              	  $log.error('HeaderCtrl::LoginSnippetCtrl: failed in getFunctionalMenuStaticInfo: ' + err);
                });
            } catch (err) {
                $log.error('HeaderCtrl::LoginSnippetCtrl caught exception: ' + err);
            }
            
            $scope.getUserApplicationRoles= function(){
          	  $scope.userapproles = [];
          	  if($scope.displayUserAppRoles)
	         		$scope.displayUserAppRoles = false;
	         		 else
	         			$scope.displayUserAppRoles = true;
          	  
        	        userProfileService.getUserAppRoles($scope.loginSnippetUserid)
        	          .then(res=>{
        			
       		 for(var i=0;i<res.length;i++){              			
	            	var userapprole ={
	            		App:res[i].appName,
	            		Roles:res[i].roleNames,	
	            	};	            	
	            	$scope.userapproles.push(userapprole); 
       		 }
       		 
        	});
        	
          }
        }        
    }
    class NotificationCtrl{
    	constructor($log, $scope, $cookies, $timeout, sessionService,notificationService,$interval,ngDialog,$modal) {
    		 $scope.notifications=[];   
    		 var intervalPromise = null;
             $scope.notificationCount= notificationService.notificationCount;
             
             $scope.getNotification = function(){            	 
            	 notificationService.getNotification()
                 .then(res=> {
                	notificationService.decrementRefreshCount();
                	var count = notificationService.getRefreshCount();
                 	if (res==null || res.data==null || res.data.message!='success') {
                 		$log.error('NotificationCtrl::updateNotifications: failed to get notifications');
                 		if (intervalPromise != null)
                 			$interval.cancel(intervalPromise);
                 	} else if(count<=0){
                 		if (intervalPromise != null)
                 			$interval.cancel(intervalPromise);
                 	} else {
                 		$scope.notifications = [];
                 		notificationService.setNotificationCount(res.data.response.length);
                 		for(var i=0;i<res.data.response.length;i++){
                 			var data = res.data.response[i];                			
			            	var notification ={
			            		id:data.notificationId,
			            		msgHeader:data.msgHeader,
			            		message:data.msgDescription,
			            		msgSource:data.msgSource,
			            		time:data.createdDate,
			            		priority:data.priority,
			            		notificationHyperlink:data.notificationHyperlink
			            	};
			            	$scope.notifications.push(notification);       
			             }  
                 	}   	
                 }).catch(err=> {
                 	$log.error('NotificationCtrl::getNotification: caught exception: ' + err);
                 	if (intervalPromise != null)
                 		$interval.cancel(intervalPromise);
                 });      
             }
             $scope.getNotification();
             function updateNotifications() {
            	 $scope.getNotification();
             }
             $scope.start = function(rate) {
 				// stops any running interval to avoid two intervals running at the same time
 				$scope.stop(); 	
 				// store the interval promise
 				intervalPromise = $interval(updateNotifications, rate);
 			 };

 			 $scope.stop = function() {
 				$interval.cancel(intervalPromise);
 			 };
 			 
 			 $scope.showDetailedJsonMessage=function (selectedAdminNotification) {
        		 notificationService.getMessageRecipients(selectedAdminNotification.id).then(res =>{
                     $scope.messageRecipients = res;
				 var messageObject=JSON.parse(selectedAdminNotification.message);
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
 			 
 			notificationService.getNotificationRate().then(res=> {
            	if (res == null || res.response == null) {
            		$log.error('NotificationCtrl: failed to notification update rate or duration, check system.properties file.');
            	} else {
            		var rate = parseInt(res.response.updateRate);
					var duration = parseInt(res.response.updateDuration);
					notificationService.setMaxRefreshCount(parseInt(duration/rate)+1);
					notificationService.setRefreshCount(notificationService.maxCount);
           			if (rate != NaN && duration != NaN) {
						$scope.updateRate=rate;
						$scope.start($scope.updateRate);
           			}            			
            	}
            }).catch(err=> {
            	$log.error('NotificationCtrl: getNotificationRate() failed: ' + err);
            });
             
             $scope.deleteNotification = function(index){
            	 if ($scope.notifications[index].id == null || $scope.notifications[index].id == '') {
             		$log.error('NotificationCtrl: failed to delete Notification.');
             		return;
            	 }
            	 notificationService.setNotificationRead($scope.notifications[index].id);
            	 $scope.notifications.splice(index,1);
            	 notificationService.setNotificationCount($scope.notifications.length);     	 
             }
    	}
    }
    
    NotificationCtrl.$inject = ['$log', '$scope', '$cookies', '$timeout', 'sessionService','notificationService','$interval','ngDialog','$modal'];
    LoginSnippetCtrl.$inject = ['$log', '$scope', '$cookies', '$timeout','userProfileService', 'sessionService'];
    HeaderCtrl.$inject = ['$log', '$window', '$translate', 'translateService', 'userProfileService', 'menusService', '$scope', 'ECOMP_URL_REGEX','$cookies','$state','auditLogService','notificationService','ngDialog','$modal'];
    angular.module('ecompApp').controller('HeaderCtrl', HeaderCtrl);
    angular.module('ecompApp').controller('loginSnippetCtrl', LoginSnippetCtrl);
    angular.module('ecompApp').controller('notificationCtrl', NotificationCtrl);
})();
