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
(function () {
   	/*
   	 * Custom version of b2b-left-navigation directive:
   	 * 1. Make parent menu a link if no child menus.
   	 * 2. Add unique IDs to all items.
   	 * 3. Hide icon if no child menus.
   	 * 4. Add arrow toggle button.
   	 * 5. Adjust the page on collapse/expand.
   	 */
    class B2BLeftMenu {
        constructor($rootScope) {
            this.templateUrl = 'app/directives/b2b-leftnav-ext/b2b-leftnav-ext.tpl.html';
            this.restrict = 'EA';
            this.$rootScope = $rootScope;
            this.link = this._link.bind(this);
            this.scope = {
            	menuData: '='
            }
        }
        _link(scope) {
        	scope.idx = -1;
            scope.itemIdx = -1;
            scope.navIdx = -1;
            scope.toggleNav = function (val,link) {
                if (val === scope.idx) {
                    scope.idx = -1;
                    return;
                }
                scope.idx = val;
            };
            /*New function for ECOMP sdk*/
            scope.toggleDrawer = function(showmenu){
            	scope.idx=-1; /*hide the sunmenus*/
            	if(showmenu){
        			document.getElementById('page-content').style.paddingLeft = "50px";
        		}
        		else
        			document.getElementById('page-content').style.paddingLeft = "230px";           	
            };
            scope.liveLink = function (evt, val1, val2) {
                scope.itemIdx = val1;
                scope.navIdx = val2;
                evt.stopPropagation();
            };
        }
    }
    angular.module('ecompApp').directive('leftMenuEcomp', ($rootScope) => new B2BLeftMenu($rootScope));
})();

