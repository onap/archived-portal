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
    class SidebarCtrl {
        constructor(userProfileService, $log, $rootScope) {
            this.$log = $log;
            this.userProfileService = userProfileService;
            this.$rootScope = $rootScope;
            $rootScope.isAdminPortalAdmin = false;

            //if (bowser.msie || bowser.msedge)
            //    $log.debug('SidebarCtrl::init: Browser is: Internet Explorer or Edge');
            // else
            //    $log.debug('SidebarCtrl::init: Browser is: ' + bowser.name + ': ' + bowser.version);

            userProfileService.getUserProfile()
                .then(profile=> {
                    // $log.debug('SidebarCtrl::getUserProfile: profile.roles.indexOf(superAdmin) = ' + profile.roles.indexOf('superAdmin'));
                    // $log.debug('SidebarCtrl::getUserProfile: profile.roles.indexOf(admin) = ' + profile.roles.indexOf('admin'));
                    if (profile.roles.indexOf('superAdmin') > -1) {
                        $rootScope.isAdminPortalAdmin = true;
                        // this.$log.debug('SidebarCtrl::getUserProfile: user has the superAdmin role');
                        this.sidebarModel = {
                            label: 'ECOMP portal',
                            navItems: [
                                {
                                    name: 'Home',
                                    state: 'root.applicationsHome',
                                    imageSrc : 'ion-home'
                                }, {
                                    name: 'Application Catalog',
                                    state: 'root.appCatalog',
                                    imageSrc : 'ion-grid'
                                }, {
                                    name: 'Widgets',
                                    state: 'root.widgetsHome',
                                    imageSrc : 'ion-android-apps'
                                }, {
                                    name: 'Admins',
                                    state: 'root.admins',
                                    imageSrc : 'ion-android-star'
                                }, {
                                    name: 'Users',
                                    state: 'root.users',
                                    imageSrc : 'ion-person'
                                },{
                                    name: 'Portal Admins',
                                    state: 'root.portalAdmins',
                                    imageSrc : 'ion-gear-a'
                                }, {
                                    name: 'Application Onboarding',
                                    state: 'root.applications',
                                    imageSrc : 'ion-ios-upload-outline'
                                }, {
                                    name: 'Widget Onboarding',
                                    state: 'root.widgets',
                                    imageSrc : 'ion-ios-upload-outline'
                                },{
                                    name: 'Edit Functional Menu',
                                    state: 'root.functionalMenu',
                                    imageSrc : 'ion-compose'
                                }
                            ]
                        };
                    }
                    else if (profile.roles.indexOf('admin')  > -1) {
                        // this.$log.debug('SidebarCtrl::getUserProfile: user has the admin role');
                        this.sidebarModel = {
                            label: 'ECOMP portal',
                            navItems: [
                                       {
                                           name: 'Home',
                                           state: 'root.applicationsHome',
                                           imageSrc : 'ion-home'
                                       }, {
                                           name: 'Application Catalog',
                                           state: 'root.appCatalog',
                                           imageSrc : 'ion-grid'
                                       }, {
                                       	name: 'Widgets',
                                       	state: 'root.widgetsHome',
                                       	imageSrc : 'ion-grid'
                                       }, {
                                       	name: 'Users',
                                       	state: 'root.users',
                                       	imageSrc : 'ion-person'
                                       }, {
                                       	name: 'Widget Onboarding',
                                       	state: 'root.widgets',
                                       	imageSrc : 'ion-grid'
                                       }
                                   ]
                        };
                    }
                    else {
                        $rootScope.isAdminPortalAdmin = false;
                        // this.$log.debug('SidebarCtrl::getUserProfile: user is not superAdmin nor admin');
                        this.sidebarModel = {
                            label: 'ECOMP portal',
                            navItems: [
                                       {
                                           name: 'Home',
                                           state: 'root.applicationsHome',
                                           imageSrc : 'ion-home'
                                       }, {
                                           name: 'Application Catalog',
                                           state: 'root.appCatalog',
                                           imageSrc : 'ion-grid'
                                       }, {
                                       	name: 'Widgets',
                                       	state: 'root.widgetsHome',
                                       	imageSrc : 'ion-grid'
                                       }
                                   ]
                        };
                    }
                }).catch(err=> {
                	  $log.error('SidebarCtrl: failed to get user profile: ' + err);
            });

        }
    }
    SidebarCtrl.$inject = ['userProfileService', '$log', '$rootScope'];
    angular.module('ecompApp').controller('SidebarCtrl', SidebarCtrl);
})();
