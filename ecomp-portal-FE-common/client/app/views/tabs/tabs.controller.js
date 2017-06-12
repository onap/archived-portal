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
    const HTTP_PROTOCOL_RGX = /https?:\/\//;
    class  TabsCtrl {
        constructor(applicationsService, $log, $window, conf, userProfileService, userbarUpdateService, $scope,$cookies,$rootScope,confirmBoxService,auditLogService) {        	
        	// Tab counter
            var counter = 1;
            var tabLimit = 6;
            this.conf = conf;
            var cookieDomain = this.conf.cookieDomain;
            // Array to store the tabs
            $scope.tabs = [];
            $scope.notificationShow=true;
            $rootScope.showFooter = "";
            $cookies.putObject('show_app_header', false,{domain: cookieDomain, path : '/'});

            let  getCookieDomain = function() {
         	   return window.location.hostname;
         	}
          let getContextPath=function() {
         	   return  window.location.pathname.substring( 0, window.location.pathname.lastIndexOf( '/' ) + 1 );
         	}

            let noRefresh = function () {
                    window.onbeforeunload = function(e) {

                    	var isQtoHref = false;
                    	try{
                    		isQtoHref = e.srcElement.activeElement.href.includes("to");
                    	} catch(err) {

                    	}

                        if ($scope.tabs.length > 1 && isQtoHref == false) {
                            return "Changes you made may not be saved. Are you sure you want to refresh?";
                        } else {
                            return null;
                        }
                    }
            }
            // Add tab to the end of the array
            var addTab = function (title, content) {
            	if($scope.tabs.length===tabLimit){
            		//alert
            		confirmBoxService.showInformation('You have reached your maximum limit of tabs allowed.').then(isConfirmed => {});
            	} else {
            		// console.log(window.performance.memory);
            		// var usedperc = (window.performance.memory.usedJSHeapSize/window.performance.memory.jsHeapSizeLimit)*100;
            		// console.log('Current memory usage: '+usedperc+'%');
            		if(title!=='Home' && content.indexOf('https') == -1){
            			console.log('App URL: '+content+'. The application URL you are trying to open is not HTTPS. We recommend to use secured HTTPS URL while on-boarding the application.');
            			//confirmBoxService.showInformation('The application URL you are trying to open is not HTTPS. We recommend to use secured HTTPS URL while on-boarding the application.').then(isConfirmed => {});
            		}
            		
                    $scope.tabs.push({ title: title, content: content });
                    counter++;
                    //$scope.tabs[$scope.tabs.length - 1].disabled = false;
                    $scope.selectedIndex = $scope.tabs.length - 1;
                    if ($scope.tabs.length > 1) {
                        noRefresh();
                    }
                    $cookies.putObject('cookieTabs', $scope.tabs,{domain: getCookieDomain(), path : getContextPath()});
                    $cookies.putObject('visInVisCookieTabs', $scope.tabs,{domain: getCookieDomain(), path : getContextPath()});
                   
            	}
            };
            
            //with APP ID
            var addTab = function (title, content, appId) {
            	if($scope.tabs.length===tabLimit){
            		//alert
            		confirmBoxService.showInformation('You have reached your maximum limit of tabs allowed.').then(isConfirmed => {});
            	} else {     		
            		if(title!=='Home' && content.indexOf('https') === -1){
            			console.log('App URL: '+content+'. The application URL you are trying to open is not HTTPS. We recommend to use secured HTTPS URL while on-boarding the application.');
            		}
                    $scope.tabs.push({ title: title, content: content, appId:appId });
                    counter++;
                    $scope.selectedIndex = $scope.tabs.length - 1;
                    if ($scope.tabs.length > 1) {
                        noRefresh();
                    }
                    $cookies.putObject('cookieTabs', $scope.tabs,{domain: getCookieDomain(), path : getContextPath()});
                    $cookies.putObject('visInVisCookieTabs', $scope.tabs,{domain: getCookieDomain(), path : getContextPath()});
            	}
            };
            
            // adjust title - trim the title and append ...
            var adjustTitle = function (title) {
            	var index = 15;
            	var nonEmptyCharPattern = /(\s|\w)/;
            	var adjustedTitle = title.substring(0,index);
            	var ext = title.charAt(index).replace(nonEmptyCharPattern,'...');
            	return adjustedTitle.concat(ext);
            	
            	
            };
            
            //store audit log
            $scope.auditLog = function(app) {
                $log.debug('auditLog::auditLog: auditLog.ping() = ' + app);
                var comment = '';
                if(app.content==null || app.content=='')
                	comment= app.title;
                else
                	comment = app.content;
                auditLogService.storeAudit(app.appId, 'tab', comment);
        	};
            
            // Remove tab by index
            var removeTab = function (event, index) {
              event.preventDefault();
              event.stopPropagation();
              $scope.tabs.splice(index, 1);
              $cookies.putObject('cookieTabs', $scope.tabs,{domain: getCookieDomain(), path : getContextPath()});
            };
            
          //adjust height of the tab due to the search popup being hidden 
        	$scope.adjustTabStyle = function(title){
        		if(title=='Home'){
        			$(".w-ecomp-tabs").css('height',"50px");
        		}else{
        			$(".w-ecomp-tabs").css('height',"100%");
        		}
        	}
        	
           // select tab 
            var selectTab = function (title) {
            	$scope.adjustTabStyle(title);
                if(title==='Home') {
                    $rootScope.ContentModel.IsVisible=true;
                    $rootScope.showFooter = true;
                    $rootScope.tabBottom = 75;
                    userbarUpdateService.setRefreshCount(userbarUpdateService.maxCount);
                }
                else {
                    $rootScope.ContentModel.IsVisible=false;
                    $rootScope.showFooter = false;
                    $rootScope.tabBottom = 0;
                }
            };

            // Initialize the scope functions
            $scope.addTab    = addTab;
            $scope.removeTab = removeTab;
            $scope.selectTab = selectTab;
            $scope.adjustTitle = adjustTitle;
            

            $rootScope.ContentModel = {
            	    IsVisible : false,
            	    ViewUrl : null,
            	};
            
            
        	var sessionActive = applicationsService.ping()
        	.then(sessionActive => {
            // $log.debug('TabsCtrl::addTab: applicationsService.ping() = ' + JSON.stringify(sessionActive));
        	// For demonstration add 5 tabs
               
            var cookieTabs = $cookies.getObject('cookieTabs');
        	if(cookieTabs!=null){
        		for(var t in cookieTabs){
        			// console.log('TabsCtrl::addTab: cookieTabs title: '+cookieTabs[t].title);
        			if(cookieTabs[t].title!=null && cookieTabs[t].title==='Home'){
        				cookieTabs[t].content = "";
        				$rootScope.ContentModel.IsVisible=true;
            			addTab( cookieTabs[t].title, cookieTabs[t].content,1) ;
        			}else{
            			addTab( cookieTabs[t].title, cookieTabs[t].content,cookieTabs[t].appId) ;
        			}
        				
        		}
        	} else {
            for (var i = 0; i < 1; i++) {
            	var content="";
            	var title="";
            	var appId=""
            	if(i===0){
            		title="Home";
            		$rootScope.ContentModel.IsVisible=true;
                    addTab(title, content,1);
            	}else{
                    addTab(title, content,appId);
            	} 
            }
        	}
            

            
            //$scope.tabs[$scope.tabs.length - 1].active = false;
            //$scope.tabs[0].disabled = false;
            $scope.selectedIndex = 0;
        	});
        	
            $scope.$watchCollection(function() { return $cookies.getObject('addTab'); }, function(newValue) {
            	// $log.log('Cookie string: ' + $cookies.get('test'))
            	var tabContent = $cookies.getObject('addTab');
            	if(tabContent!=null && tabContent.url!=null){
            		var tabExists = false;
            		for(var x in $scope.tabs){
            			// console.log($scope.tabs[x].content);
            			if($scope.tabs[x].title==tabContent.title){
            				tabExists = true;
            				//$scope.tabs[x].disabled = false;
            				$scope.selectedIndex = x;
                            // added dummy variable to get iframe reloded if tab is already opened.
                            if(tabContent.url.indexOf('?')===-1){
                                $scope.tabs[x].content=tabContent.url+'?dummyVar='+(new Date()).getTime();
                            }
                            else{
                                $scope.tabs[x].content=tabContent.url+'&dummyVar='+(new Date()).getTime();
                            }
            			}
            		}
            		if(!tabExists){
            	         addTab( tabContent.title, tabContent.url,tabContent.appId) ;
            		}
            		$cookies.remove('addTab');
            	}
            });
        }
    }
    TabsCtrl.$inject = ['applicationsService', '$log', '$window', 'conf', 'userProfileService', 'userbarUpdateService', '$scope','$cookies','$rootScope','confirmBoxService','auditLogService'];
    angular.module('ecompApp').controller('TabsCtrl', TabsCtrl);
    
    angular.module('ecompApp').directive('mainArea', function() {
        return {
            restrict: "E",
            templateUrl: 'app/views/tabs/tabframe.html',
            link: function(scope, element) {
            	           	
            	//var iframeId = "#tabframe-" + scope.$parent.tab.title.split(' ').join('-');
            	// jQuery(iframeId).load(function() {
            	//        alert("hello");
            	//    }); //.attr("src",'{{tab.content | trusted}}' ); //src='{{tab.content | trusted}}'
            	// jQuery(iframeId).attr('src', '{{tab.content | trusted}}');
            	 
            	//element.childNodes[0].on('load', function() {
            	//	alert('hello');
            	//});
            }
        }
    });
    
   
    
    angular.module('ecompApp').directive('tabHighlight', [function () {
        return {
          restrict: 'A',
          link: function (scope, element) {
            // Here is the major jQuery usage where we add the event
            // listeners mousemove and mouseout on the tabs to initalize
            // the moving highlight for the inactive tabs
            var x, y, initial_background = '#c3d5e6';

            element
              .removeAttr('style')
              .mousemove(function (e) {
                // Add highlight effect on inactive tabs
                if(!element.hasClass('md-active'))
                {
                  x = e.pageX - this.offsetLeft;
                  y = e.pageY - this.offsetTop;

                  // Set the background when mouse moves over inactive tabs
                  element
                    .css({ background: '-moz-radial-gradient(circle at ' + x + 'px ' + y + 'px, rgba(255,255,255,0.4) 0px, rgba(255,255,255,0.0) 45px), ' + initial_background })
                    .css({ background: '-webkit-radial-gradient(circle at ' + x + 'px ' + y + 'px, rgba(255,255,255,0.4) 0px, rgba(255,255,255,0.0) 45px), ' + initial_background })
                    .css({ background: 'radial-gradient(circle at ' + x + 'px ' + y + 'px, rgba(255,255,255,0.4) 0px, rgba(255,255,255,0.0) 45px), ' + initial_background });
                }
              })
              .mouseout(function () {
                // Return the inital background color of the tab
                element.removeAttr('style');
              });
          }
        };
      }]);


    
})();

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return '';
    if (!results[2]) return '';
    return (results[2].replace(/\+/g, " "));
}

function isCascadeFrame(ref) {
	  // alert(ref.id);
	   if (self != top) {
		   var e = document.body;
		   e.parentNode.removeChild(e);
		   window.location = "unKnownError";
		   }
}
