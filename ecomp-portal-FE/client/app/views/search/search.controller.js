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

    class SearchCtrl {
        constructor($log, $scope, $cookies, $timeout, userProfileService, sessionService, dashboardService) {
            $scope.firstName="";
            $scope.lastName="";
            
            function  showHideSearchSnippet() {
            	
            	setTimeout(function() {
        			jQuery("#mainSearchSnippet").click();
        			},500);
        		
        		setTimeout(function() {
        			jQuery("#mainSearchText").focus();
        			},1000);
            }
            
            jQuery("#mainSearchDiv").keyup(function(event){
                if(event.keyCode == 13){
                    // there is a watch on this variable which will trigger the database pull
                	dashboardService.searchString = jQuery("#mainSearchText").val();
                	
                	// opens the popup
                    var popupDomObj = jQuery("[content='searchSnippet.html']");
                	if(popupDomObj.length == 0) {
                		showHideSearchSnippet();
                	} else {
                		jQuery("#mainSearchSnippet").click();
                		showHideSearchSnippet();
                	}
                	                	
                	
           		 
                }
            });
            
            
            
        }
        
        
    }
    
    
    class SearchSnippetCtrl {
        constructor($log, $scope, $cookies, $timeout, userProfileService, sessionService, dashboardService,applicationsService, $window, $state) {
            $scope.firstName="";
            $scope.lastName="";
            $scope.goToUrl = goToUrl;
            $scope.dService = dashboardService;

            
            
            function goToUrl (item, type) {
                $log.info("goToUrl called")
                $log.info(item + "/" + type);
                
                
                if(type == 'intra') {
                	
                	var intraSearcLink = "http://to_do_link.com";
                	var intraSpecSearcLink = intraSearcLink + encodeURIComponent(dashboardService.searchString);
                	$window.open(intraSpecSearcLink, '_blank');
                	
                } else if (type == 'extra') {
                	var extraSearcLink = "https://to_do_link.com";
                	var extraSpecSearcLink = extraSearcLink + encodeURIComponent(dashboardService.searchString);
                	$window.open(extraSpecSearcLink, '_blank');
                }

                let url = item.target;
                let restrictedApp = item.uuid;
                let getAccessState =  "root.getAccess"
                if (!url) {
                	
                	applicationsService.goGetAccessAppName = item.name;
                	if($state.current.name == getAccessState)
                		$state.reload();
                	else
                		$state.go(getAccessState);
                    //$log.info('No url found for this application, doing nothing..');
                    return;
                }
                
                if (restrictedApp != "true") {
                    $window.open(url, '_blank');
                } else {
                	if(item.url=="root.access"){
                		$state.go(url);
                		var tabContent = { id: new Date(), title: 'Home', url: url };
                    	$cookies.putObject('addTab', tabContent );
                	} else {
                    	var tabContent = { id: new Date(), title: item.name, url: url };
                    	$cookies.putObject('addTab', tabContent );
                    }
                }

            }
            
            function getItems(searchString) {
            	
            	var items;
            	var itemMap = dashboardService.getSearchAllByStringResults(searchString)            	
            	 .then(res => {
            		 $scope.items = res;
            		 
            		 
            	 }).catch(err => {
            		 $scope.items = [];
                     $log.error('Couldnt get search results...', err)
            	 });

            }
            
            $scope.$watch('dService.searchString', function(searchString) {
                if(searchString != undefined )
                	getItems(searchString);

            });
            
             
            
        }
        
        
        
    }
    
    

    SearchCtrl.$inject = ['$log', '$scope', '$cookies', '$timeout','userProfileService', 'sessionService', 'dashboardService'];
    SearchSnippetCtrl.$inject = ['$log', '$scope', '$cookies', '$timeout','userProfileService', 'sessionService', 'dashboardService','applicationsService', '$window','$state'];
    angular.module('ecompApp').controller('searchCtrl', SearchCtrl); 
    angular.module('ecompApp').controller('searchSnippetCtrl', SearchSnippetCtrl);
    angular.module('ecompApp').directive('searchBox', function() {
        return {
            restrict: "E",
            templateUrl: 'app/views/search/search.tpl.html',
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
    angular.module( 'ecompApp' ).config( [
        '$compileProvider',
        function( $compileProvider )
        {   
            $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|chrome-extension|qto):/);
        }
    ]);

})();


