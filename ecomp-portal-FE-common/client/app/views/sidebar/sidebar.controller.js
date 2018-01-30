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
'use strict';
(function () {
    class SidebarCtrl {
        constructor(applicationsService,userProfileService, $log, $rootScope) {
            this.$log = $log;
            this.userProfileService = userProfileService;
            this.$rootScope = $rootScope;
            $rootScope.isAdminPortalAdmin = false;


            //if (bowser.msie || bowser.msedge)
            //    $log.debug('SidebarCtrl::init: Browser is: Internet Explorer or Edge');
            // else
            //    $log.debug('SidebarCtrl::init: Browser is: ' + bowser.name + ': ' + bowser.version);

            //note: this model should be retrieved from BE via sidebar specific service
            	userProfileService.getUserProfile()
                .then(profile=> {
                	
                	if (profile.roles.indexOf('System Administrator') > -1) {
                        $rootScope.isAdminPortalAdmin = true;
                	} else {
                		 this.$log.debug('SidebarCtrl::getUserProfile: user is not superAdmin nor admin');
                	}
                });
                // $log.debug('SidebarCtrl::getUserProfile: profile.roles.indexOf(superAdmin) = ' + profile.roles.indexOf('superAdmin'));
                // $log.debug('SidebarCtrl::getUserProfile: profile.roles.indexOf(admin) = ' + profile.roles.indexOf('admin'));
            	applicationsService
    			.getLeftMenuItems()
    			.then(res=>
    				 {
    						//console.log("Menu items is "+JSON.stringify(res));
    						this.sidebarModel = res;	 
    				 }).catch(err => {
            		//confirmBoxService.showInformation('There was a problem creating the menu. ' +
                    // 'Please try again later. Error Status: '+ err.status).then(isConfirmed => {});
            		$log.error('SidebarCtrl::getUserProfile: User Profile error occurred: ' + err);
    				});
    
        }
    }
    SidebarCtrl.$inject = ['applicationsService','userProfileService', '$log', '$rootScope'];
    angular.module('ecompApp').controller('SidebarCtrl', SidebarCtrl);
})();
