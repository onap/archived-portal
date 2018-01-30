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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
/**
 * Created by nnaffar on 1/28/16.
 */
(function () {
    class LeftMenu {
        constructor($rootScope, userbarUpdateService,notificationService,auditLogService) {
            this.templateUrl = 'app/directives/left-menu/left-menu.tpl.html';
            this.restrict = 'AE';
            this.$rootScope = $rootScope;
            this.userbarUpdateService = userbarUpdateService;
            this.notificationService = notificationService;
            this.auditLogService= auditLogService;
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
            
            scope.refreshNotification = () => {
            	this.notificationService.setRefreshCount(this.notificationService.maxCount);
            };

            scope.toggleSidebar = () => {
                scope.isOpen = !scope.isOpen;
                if(scope.isOpen==true)
                	setContentPos(1);
                else
                	setContentPos(0);
            };
            scope.auditLog =(name) => {         
        		this.auditLogService.storeAudit(1,'leftMenu',name);
        	};


            scope.isBrowserInternetExplorer = false;
            scope.browserName = bowser.name;

            if (bowser.msie || bowser.msedge) {
                scope.isBrowserInternetExplorer = true;
            } else {
                scope.isBrowserInternetExplorer = false;
            }


            this.$rootScope.$on('$stateChangeStart', () => {
                scope.isOpen = true;
            });
        }
    }
    angular.module('ecompApp').directive('leftMenu', ($rootScope,userbarUpdateService,notificationService,auditLogService) => new LeftMenu($rootScope,userbarUpdateService,notificationService,auditLogService));
})();

function setContentPos(open) {
	// console.log("*******************************************");
	if(open==1){
		$("#page-content" ).css( "padding-left", "210px" );
	}else{
		$("#page-content" ).css( "padding-left", "50px" );
	}

}
