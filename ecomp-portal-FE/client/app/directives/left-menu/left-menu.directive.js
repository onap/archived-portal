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

(function () {
    class LeftMenu {
    	constructor($rootScope, $log, userbarUpdateService) {
            this.templateUrl = 'app/directives/left-menu/left-menu.tpl.html';
            this.restrict = 'AE';
            this.$rootScope = $rootScope;
            this.$log = $log;
            this.userbarUpdateService = userbarUpdateService;
            this.link = this._link.bind(this);
            this.scope = {
                sidebarModel: '='
            }
        }

        _link(scope) {
            let init = () => {
                scope.isOpen = true;
            };

            init();
            
            scope.refreshOnlineUsers = () => {
                this.userbarUpdateService.setRefreshCount(this.userbarUpdateService.maxCount);
            };

            scope.toggleSidebar = () => {
                scope.isOpen = !scope.isOpen;
                if(scope.isOpen)
                	setContentPos(1);
                else
                	setContentPos(0);
                
            };

            scope.isBrowserInternetExplorer = false;
            scope.browserName = bowser.name;

            if (bowser.msie || bowser.msedge) {
                scope.isBrowserInternetExplorer = true;
            } else {
                scope.isBrowserInternetExplorer = false;
            }

            let log = this.$log;

            this.userbarUpdateService.getWidthThresholdLeftMenu().then(function (res) {
            if (res == null || res.response == null) {
                log.error('userbarUpdateService: failed to get window width threshold for collapsing left menu; please make sure "window_width_threshold_left_menu" is specified in system.properties file.');
            } else { 
                var leftMenuCollapseWidthThreshold = parseInt(res.response.windowWidth);
                if ($(window).width()<leftMenuCollapseWidthThreshold) {
                    scope.toggleSidebar();
                }
            }
            })['catch'](function (err) {
            log.error('LeftMenu Controller:: getWidthThresholdLeftMenu() failed: ' + err);
            });



            this.$rootScope.$on('$stateChangeStart', () => {
                scope.isOpen = true;
            });
        }
    }
    angular.module('ecompApp').directive('leftMenu', ($rootScope,$log,userbarUpdateService) => new LeftMenu($rootScope,$log,userbarUpdateService));
})();

function setContentPos(open) {
	if(open==1){
		$("#contentId" ).css( "padding-left", "210px" );
	}else{
		$("#contentId" ).css( "padding-left", "50px" );
	}

}
