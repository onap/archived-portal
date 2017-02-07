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
/**
 * Created by nnaffar on 1/28/16.
 */
(function () {
    class RightMenu {
        constructor($rootScope,$window,$log,userbarUpdateService) {
            this.templateUrl = 'app/directives/right-menu/right-menu.tpl.html';
            this.restrict = 'AE';
            this.$rootScope = $rootScope;
            this.userbarUpdateService = userbarUpdateService;
            this.$window = $window;
            this.$log = $log;
            this.link = this._link.bind(this);
            this.scope = {              
                userList :'='
            }
        }
        
        

        _link(scope) {
            let init = () => {
                scope.isOpen = true;
                scope.rightSideToggleBtn = 'Collapse';  
                
                scope.openInNewTab = (url) => {
                	if(url == "self") {
                		alert("Cannot chat with self!");
                	} else {
	              	 	var win = window.open(url, '_blank');
	              	 	setCookie(url.split("chat_id=")[1], 'source', 1);
	              	 	//window.localStorage.setItem(url.split("chat_id=")[1],'source'); 
	                   	win.focus();
                	}
                };
                
            };
            
            function setCookie(cname,cvalue,exdays) {
        	    var d = new Date();
        	    d.setTime(d.getTime() + (exdays*24*60*60*1000));
        	    var expires = "expires=" + d.toGMTString();
        	    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
        	}

        	function getCookie(cname) {
        	    var name = cname + "=";
        	    var decodedCookie = decodeURIComponent(document.cookie);
        	    var ca = decodedCookie.split(';');
        	    for(var i = 0; i < ca.length; i++) {
        	        var c = ca[i];
        	        while (c.charAt(0) == ' ') {
        	            c = c.substring(1);
        	        }
        	        if (c.indexOf(name) == 0) {
        	            return c.substring(name.length, c.length);
        	        }
        	    }
        	    return "";
        	}
            
           

            init();
            
            /***Getting the list of the users***/
            scope.toggleSidebar = () => {
                scope.isOpen = !scope.isOpen;
                if(scope.isOpen){
                	scope.rightSideToggleBtn = 'Collapse';
                }else{
                	scope.rightSideToggleBtn = 'Expand';
                }
            };

            scope.isBrowserInternetExplorer = false;
            scope.browserName = bowser.name;

            if (bowser.msie || bowser.msedge) {
                scope.isBrowserInternetExplorer = true;
            } else {
                scope.isBrowserInternetExplorer = false;
            }
            
           

            
            scope.calculateUserBarHeight = () => {
           	 var footerOff = $('#online-userbar').offset().top;
        		var headOff = $('#footer').offset().top;
        		var userbarHeight=  parseInt($(".online-user-container").css('height'),10);
        		var defaultOffSet = 45;
        		// console.log(headOff - footerOff-defaultOffSet);
        		$(".online-user-container").css({
    				"height" : headOff - footerOff-defaultOffSet
    			});
            };       

            let log = this.$log;

            this.userbarUpdateService.getWidthThresholdRightMenu().then(function (res) {
            if (res.status=="ERROR") {
                log.error('userbarUpdateService: failed to get window width threshold for collapsing right menu; please make sure "window_width_threshold_right_menu" is specified in system.properties file.');
            } else { 
                var rightMenuCollapseWidthThreshold = parseInt(res.response.windowWidth);
                if ($(window).width()<rightMenuCollapseWidthThreshold) {
                    scope.toggleSidebar();
                }
            }
            })['catch'](function (err) {
            log.error('LeftMenu Controller:: getWidthThresholdLeftMenu() failed: ' + err);
            });

            angular.element(this.$window).bind('resize', function(){
            	 scope.calculateUserBarHeight();
            });
        }
        
    }
    angular.module('ecompApp').directive('rightMenu', ($rootScope,$window,$log,userbarUpdateService) => new RightMenu($rootScope,$window,$log,userbarUpdateService));
})();
