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
	class HeaderCtrl {
        constructor($log, $window, userProfileService, menusService, $scope, ECOMP_URL_REGEX, $cookies, $state) {
            this.firstName = '';
            this.lastName = '';
            this.$log = $log;
            this.menusService = menusService;
            this.$scope = $scope;
            this.favoritesMenuItems = '';
            $scope.favoriteItemsCount = 0;
            $scope.favoritesMenuItems = '';
            $scope.showFavorites = false;
            $scope.emptyFavorites = false;
            $scope.favoritesWindow = false;

            $scope.showNotification = true;

            $scope.hideMenus = false;

            $scope.menuItems = [];
            $scope.activeClickSubMenu = {
                x: ''
            }; 
            $scope.activeClickMenu = {
                x: ''
            };
            $scope.megaMenuDataObject =[];
            
            this.isLoading = true;
            this.ECOMP_URL_REGEX = ECOMP_URL_REGEX;

            var menuStructureConvert = function(menuItems) {
                $scope.megaMenuDataObjectTemp = [
                    {
                        text: "Manage/Functions",
                        children: [],
                    },
                    {
                        text: "Help",
                        children: [{
                        	restrictedApp: true,
                            text:"Contact Us",
                            url:""
                        },
                            {
                                text:"Get Access",
                                url:"root.access"
                            }]
                    }

                ];
                return $scope.megaMenuDataObjectTemp;
            };
            
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
            
                var self = this;
                $scope.$on('handleUpdateUserInfo', function () {
                    userProfileService.resetFunctionalMenuStaticInfo()
                    .then(reset_res=> {
                        userProfileService.getFunctionalMenuStaticInfo()
                        .then(res=> {
                            $log.info('HeaderCtrl::getFunctionalMenuStaticInfo: getting Functional Menu Static Info init');
                            if(res==null || res.firstName==null || res.firstName=='' || res.lastName==null || res.lastName=='' ){
                                $log.info('HeaderCtrl::getFunctionalMenuStaticInfo: failed getting userinfo from shared context.. ');
                                $log.info('HeaderCtrl::getFunctionalMenuStaticInfo: getting userinfo from session ');
                                userProfileService.getUserProfile()
                                .then(profile=> {
                                    $log.info('HeaderCtrl::getFunctionalMenuStaticInfo: getting userinfo from session success');
                                    self.firstName = profile.firstName;
                                    self.lastName = profile.lastName;                     
                                    $log.info('HeaderCtrl::getFunctionalMenuStaticInfo: user has the following roles: ' + profile.roles);
                                }).catch(err=> {
                                    $log.error('Header Controller:: getUserProfile() failed: ' + err);
                                });
                            }else{
                                $log.info('HeaderCtrl::getFunctionalMenuStaticInfo: getting Functional Menu Static Info successfully',res);
                                self.firstName = res.firstName;
                                self.lastName = res.lastName;
                                $scope.contactUsURL = res.contact_us_link;
                            }

                            menusService.GetFunctionalMenuForUser()
                            .then(jsonHeaderMenu=> {
                                $scope.menuItems = unflatten( jsonHeaderMenu );
                                $scope.megaMenuDataObject = $scope.menuItems;
                            }).catch(err=> {
                                $scope.megaMenuDataObject = menuStructureConvert('');
                                $log.error('HeaderCtrl::GetFunctionalMenuForUser: HeaderCtrl json returned: ' + err);
                            });      

                            userProfileService.refreshUserBusinessCard();
                            
                        }).catch(err=> {
                            $log.error('HeaderCtrl::getFunctionalMenuStaticInfo failed: ' + err);
                        });
                    }).catch(err=> {
                        $log.error('HeaderCtrl::resetFunctionalMenuStaticInfo failed: ' + err);
                    })


                });

            userProfileService.getFunctionalMenuStaticInfo()
            .then(res=> {
            	$log.info('HeaderCtrl::getFunctionalMenuStaticInfo: getting Functional Menu Static Info init');
            	if(res==null || res.firstName==null || res.firstName=='' || res.lastName==null || res.lastName=='' ){
            		$log.info('HeaderCtrl::getFunctionalMenuStaticInfo: failed getting userinfo from shared context.. ');
            		$log.info('HeaderCtrl::getFunctionalMenuStaticInfo: getting userinfo from session ');
            		userProfileService.getUserProfile()
                    .then(profile=> {
                    	$log.info('HeaderCtrl::getFunctionalMenuStaticInfo: getting userinfo from session success');
                        this.firstName = profile.firstName;
                        this.lastName = profile.lastName;                     
                        $log.info('HeaderCtrl::getFunctionalMenuStaticInfo: user has the following roles: ' + profile.roles);
                    }).catch(err=> {
                        $log.error('Header Controller:: getUserProfile() failed: ' + err);
                    });
            	}else{
            		$log.info('HeaderCtrl::getFunctionalMenuStaticInfo: getting Functional Menu Static Info successfully',res);
            		this.firstName = res.firstName;
            		this.lastName = res.lastName;           	   	  	  
              	  	$scope.contactUsURL = res.contact_us_link;
            	}

            	menusService.GetFunctionalMenuForUser()
            	.then(jsonHeaderMenu=> {
            		$scope.menuItems = unflatten( jsonHeaderMenu );
            		$scope.megaMenuDataObject = $scope.menuItems;
            	}).catch(err=> {
            		$scope.megaMenuDataObject = menuStructureConvert('');
            		$log.error('HeaderCtrl::GetFunctionalMenuForUser: HeaderCtrl json returned: ' + err);
            	});      
            	
            }).catch(err=> {
            	$log.error('HeaderCtrl::getFunctionalMenuStaticInfo failed: ' + err);
            });
            

            $scope.loadFavorites = function () {
                $scope.hideMenus = false;
                $log.debug('HeaderCtrl::loadFavorites: loadFavorites has happened.');
                if ($scope.favoritesMenuItems == '') {
                    generateFavoriteItems();
                    $log.debug('HeaderCtrl::loadFavorites: loadFavorites is calling generateFavoriteItems()');
                } else {
                    $log.debug('HeaderCtrl::loadFavorites: loadFavorites is NOT calling generateFavoriteItems()');
                }
            }

            $scope.goToUrl = (item) =>  {
                $log.info('HeaderCtrl::goToUrl has started');
                let url = item.url;
                let restrictedApp = item.restrictedApp;
                if (!url) {
                    $log.info('HeaderCtrl::goToUrl: No url found for this application, doing nothing..');
                    return;
                }
                if (restrictedApp) {
                    $window.open(url, '_blank');
                } else {
                	if(item.url=="getAccess" || item.url=="contactUs"){
                	    // if (url = window.location.href)
                		$state.go("root."+url);
                		var tabContent = { id: new Date(), title: 'Home', url: item.url };
                    	$cookies.putObject('addTab', tabContent );
                	} else {
                    	var tabContent = { id: new Date(), title: item.text, url: item.url };
                    	$cookies.putObject('addTab', tabContent );
                    }
                    $log.debug('HeaderCtrl::goToUrl: url = ', url);
                }
                $scope.hideMenus = true;
            }
            
            
            
            $scope.submenuLevelAction = function(index, column) {
                if ($scope.favoritesMenuItems == '') {
                    generateFavoriteItems();
                    $log.debug('HeaderCtrl::submenuLevelAction: submenuLevelAction is calling generateFavoriteItems()');
                }
                if (index=='Favorites' && $scope.favoriteItemsCount != 0) {
                    $log.debug('HeaderCtrl::submenuLevelAction: Showing Favorites window');
                    $scope.favoritesWindow = true;
                    $scope.showFavorites = true;
                    $scope.emptyFavorites = false;
                }
                if (index=='Favorites' && $scope.favoriteItemsCount == 0) {
                    $log.debug('HeaderCtrl::submenuLevelAction: Hiding Favorites window in favor of No Favorites Window');
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
            
            let generateFavoriteItems  = () => {
                menusService.getFavoriteItems()
                    .then(favorites=> {
                        $scope.favoritesMenuItems = favorites;
                        $scope.favoriteItemsCount = Object.keys(favorites).length;
                        $log.info('HeaderCtrl.getFavoriteItems:: number of favorite menus: ' + $scope.favoriteItemsCount);
                    }).catch(err=> {
                        $log.error('HeaderCtrl.getFavoriteItems:: Error retrieving Favorites menus: ' + err);
                });
            }

           $scope.setAsFavoriteItem =  function(event, menuId){
               var jsonMenuID = angular.toJson({'menuId': + menuId });
               $log.debug('HeaderCtrl::setFavoriteItems: ' + jsonMenuID  + " - " +  event.target.id);

               menusService.setFavoriteItem(jsonMenuID)
               .then(() => {
                   angular.element('#' + event.target.id).css('color', '#fbb313');
                   generateFavoriteItems();
               }).catch(err=> {
                   $log.error('HeaderCtrl::setFavoriteItems:: API setFavoriteItem error: ' + err);
               });
           };

            $scope.removeAsFavoriteItem =  function(event, menuId){
                $log.debug('-----------------------------removeAsFavoriteItem: ' + menuId + " - " +  event.target.id);
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
                    $log.info('HeaderCtrl::goToPortal: No url found for this application, doing nothing..');
                    return;
                }
                if (!ECOMP_URL_REGEX.test(url)) {
                    url = 'http://' + url;
                }

                if(headerText.startsWith("vUSP")) {
                	window.open(url, '_blank');
                }
                else {
                	var tabContent = { id: new Date(), title: headerText, url: url };
                	$cookies.putObject('addTab', tabContent );
                }
            };

        }
    }
    class LoginSnippetCtrl {
        constructor($log, $scope, $cookies, $timeout, userProfileService, sessionService,ngDialog) {
            $scope.firstName="";
            $scope.lastName="";
            
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
             	// wait for individular applications to logoutout before the portal logout
             	$timeout(function() {
             		window.location = "logout.htm";
             	}, 2000);
            }
            
            $scope.openEditUserModal = function(){
            	
            	var data = {
            			loginId : $scope.loginSnippetUserId,
            	        updateRemoteApp : false,
            	        appId : $scope.selectedApp!=null?$scope.selectedApp.id:''
            	}
            	var modalInstance = ngDialog.open({
                    templateUrl: 'app/views/header/user-edit/edit-user.tpl.html',
                    controller: 'editUserController',
                    data: data,
                    resolve: {
                        message: function message() {
                            var message = {
                                type: 'Contact',
                            };
                            return message;
                        }
                    }
                }).closePromise.then(needUpdate => {
               	    //updateContactUsTable();
	            });       
            
            }
                        
            try {
            	userProfileService.getFunctionalMenuStaticInfo()
                .then(res=> {
              	  $log.info('HeaderCtrl::LoginSnippetCtrl: Login information: ' + JSON.stringify(res));
              	  $scope.firstName = res.firstName;
              	  $scope.lastName = res.lastName;
              	  $scope.loginSnippetEmail = res.email;
              	  $scope.loginSnippetUserId = res.userId;
              	  $scope.lastLogin = res.last_login;
                }).catch(err=> {
              	  $log.error('HeaderCtrl::LoginSnippetCtrl: User Profile error: ' + err);
                });
            } catch (err) {
                $log.error('HeaderCtrl::LoginSnippetCtrl:getFunctionalMenuStaticInfo failed: ' + err);
            }

            $scope.$on('refreshUserBusinessCard', function () {
                try {
                    userProfileService.getFunctionalMenuStaticInfo()
                    .then(res=> {
                    $log.info('HeaderCtrl::LoginSnippetCtrl: Login information: ' + JSON.stringify(res));
                    $scope.firstName = res.firstName;
                    $scope.lastName = res.lastName;
                    $scope.loginSnippetEmail = res.email;
                    $scope.loginSnippetUserId = res.userId;
                    $scope.lastLogin = res.last_login;
                    }).catch(err=> {
                    $log.error('HeaderCtrl::LoginSnippetCtrl: User Profile error: ' + err);
                    });
                } catch (err) {
                    $log.error('HeaderCtrl::LoginSnippetCtrl:getFunctionalMenuStaticInfo failed: ' + err);
                }
            });


        }        
    }
    class NotificationCtrl{
    	constructor($log, $scope, $cookies, $timeout, sessionService) {
    		 $scope.notifications=[];
             for(var i=0;i<6;i++){
            	 var data ={
            			 content:'Notification '+i,
            			 time:'10:0'+i+'AM'
            	 };
            	 $scope.notifications.push(data);
             }      
             
             $scope.deleteNotification = function(index){
             	
            	 $scope.notifications.splice(index,1);
             }
    	}
    }
    NotificationCtrl.$inject = ['$log', '$scope', '$cookies', '$timeout','userProfileService', 'sessionService'];
    LoginSnippetCtrl.$inject = ['$log', '$scope', '$cookies', '$timeout','userProfileService', 'sessionService','ngDialog'];
    HeaderCtrl.$inject = ['$log', '$window', 'userProfileService', 'menusService', '$scope', 'ECOMP_URL_REGEX','$cookies','$state'];
    angular.module('ecompApp').controller('HeaderCtrl', HeaderCtrl);
    angular.module('ecompApp').controller('loginSnippetCtrl', LoginSnippetCtrl);
    angular.module('ecompApp').controller('notificationCtrl', NotificationCtrl);

})();
