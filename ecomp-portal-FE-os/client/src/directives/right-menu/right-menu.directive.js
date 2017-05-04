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
        constructor($rootScope,$window,$timeout) {
            this.templateUrl = 'app/directives/right-menu/right-menu.tpl.html';
            this.restrict = 'AE';
            this.$rootScope = $rootScope;
            this.$window = $window;
            this.$timeout=$timeout;
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

            init();
            
           
            scope.toggleSidebar = () => {
                scope.isOpen = !scope.isOpen;
                if(scope.isOpen){
                	scope.rightSideToggleBtn = 'Collapse';
                }else{
                	scope.rightSideToggleBtn = 'Expand';
                }
            };

        	scope.scrollbarWidth  =function(){
        	    var $outer = $('<div>').css({visibility: 'hidden', width: 100, overflow: 'scroll'}).appendTo('body'),
        	    				widthWithScroll = $('<div>').css({width: '100%'}).appendTo($outer).outerWidth();
        	    $outer.remove();
        	    return 100 - widthWithScroll;
        	};
 
            scope.calculateUserbarOffset = function(){           	
            	var userbarWid = $("#online-userbar").width()==0?75:$("#online-userbar").width();
            	var scrollbarWid = scope.scrollbarWidth();
	    		var userbarOffset = scrollbarWid+userbarWid-1;
	    		/*Setting the offset for userbar*/
	    		$("#online-userbar").css("left",-userbarOffset);    	
	    		/*Setting the offset for user bar toggle button*/
	    		$(".ecomp-right-sidebar-toggle-btn").css("right",scrollbarWid);
            }
            this.$timeout(scope.calculateUserbarOffset, 0);
            
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
            angular.element(this.$window).bind('resize', function(){
            	 scope.calculateUserBarHeight();
            });
        }
        
    }
    angular.module('ecompApp').directive('rightMenu', ($rootScope,$window,$timeout) => new RightMenu($rootScope,$window,$timeout));
})();