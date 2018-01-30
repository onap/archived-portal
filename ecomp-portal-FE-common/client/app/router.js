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

angular.module('ecompApp')
    .config($stateProvider => {
        $stateProvider
           .state('root', {
            abstract: true,
            views: {
                'header@': {
                    templateUrl: 'app/views/header/header.tpl.html',
                    controller: 'HeaderCtrl',
                    controllerAs: 'header'
                },
                'tabbar@': {
                    templateUrl: 'app/views/tabs/tabs.tpl.html',
                    controller: 'TabsCtrl',
                    controllerAs: 'tabsHome'
                },
                'sidebar@':{
                    templateUrl: 'app/views/sidebar/sidebar.tpl.html',
                    controller: 'SidebarCtrl',
                    controllerAs: 'sidebar'
                },
                'userbar@':{
                    templateUrl: 'app/views/userbar/userbar.tpl.html',
                    controller: 'UserbarCtrl',
                    controllerAs: 'userbar'
                },
                'footer@': {
                    templateUrl: 'app/views/footer/footer.tpl.html',
                    controller: 'FooterCtrl',
                    controllerAs: 'footer'
                }
            }
        }).state('root.applicationsHome', {
            url: '/applicationsHome',
            views: {
            	'content@': {
                    templateUrl: 'app/views/dashboard/dashboard.tpl.html',
                    controller: 'DashboardCtrl',
                    controllerAs: 'dashboard'
                }
            }
        }).state('root.appCatalog', {
            url: '/appCatalog',
            views: {
            	'content@': {
                    templateUrl: 'app/views/catalog/catalog.tpl.html',
                    controller: 'CatalogCtrl',
                    controllerAs: 'catalog'
                }
            }
        }).state('root.widgetCatalog', {
            url: '/widgetCatalog',
            views: {
                'content@': {
                    templateUrl: 'app/views/widget-catalog/widget-catalog.tpl.html',
                    controller: 'WidgetCatalogCtrl',
                    controllerAs: 'widgetCatalog'
                }
            }
        }).state('root.admins', {
            url: '/admins',
            views: {
                'content@': {
                    templateUrl: 'app/views/admins/admins.tpl.html',
                    controller: 'AdminsCtrl',
                    controllerAs: 'admins'
                }
            }
        }).state('root.roles', {
            url: '/roles',
            views: {
                'content@': { 
                    templateUrl: 'app/views/role/role_list.html',
                    controller: 'roleListController',
                    controllerAs: 'roles'
                }
            }
        }).state('root.role', {
            url: '/role',
            params: {
            	roleId: 0,
            },
            views: {
                'content@': { 
                    templateUrl: 'app/views/role/role.html',
                    controller: 'roleController',
                    controllerAs: 'role'
                }
            }
        }).state('root.roleFunctions', {
            url: '/roleFunctions',
            views: {
                'content@': { 
                    templateUrl: 'app/views/role/role_function_list.html',
                    controller: 'roleFunctionListController',
                    controllerAs: 'roleFunctions'
                }
            }
        }).state('root.users', {
            url: '/users',
            views: {
                'content@': {
                    templateUrl: 'app/views/users/users.tpl.html',
                    controller: 'UsersCtrl',
                    controllerAs: 'users'
                }
            }
        }).state('root.applications', {
            url: '/applications',
            views: {
                'content@': {
                    templateUrl: 'app/views/applications/applications.tpl.html',
                    controller: 'ApplicationsCtrl',
                    controllerAs: 'apps'
                }
            }
        }).state('root.microserviceOnboarding', {
            url: '/microserviceOnboarding',
            views: {
                'content@': {
		            templateUrl: 'app/views/microservice-onboarding/microservice-onboarding.tpl.html',
		            controller: 'MicroserviceOnboardingCtrl',
		            controllerAs: 'microserviceOnboarding'
                }
            }
        }).state('root.widgetOnboarding', {
            url: '/widgetOnboarding',
            views: {
                'content@': {
                	templateUrl: 'app/views/widget-onboarding/widget-onboarding.tpl.html',
                	controller: 'WidgetOnboardingCtrl',
                	controllerAs: 'widgetOnboarding'
                }
            }
        }).state('root.accountOnboarding', {
            url: '/accountOnboarding',
            views: {
                'content@': {
		            templateUrl: 'app/views/account-onboarding/account-onboarding.tpl.html',
		            controller: 'AccountOnboardingCtrl',
		            controllerAs: 'accountOnboarding'
                }
            }
        }).state('root.functionalMenu', {
            url: '/functionalMenu',
            views: {
                'content@': {
                    templateUrl: 'app/views/functionalMenu/functionalMenu.tpl.html',
                    controller: 'FunctionalMenuCtrl',
                    controllerAs: 'functionalMenu'
                }
            }
        }).state('root.getAccess', {
            url: '/getAccess',
            params: {
                appName: null,
              },
            views: {
                'content@': {
                    templateUrl: 'app/views/support/get-access/get-access.tpl.html',
                    controller: 'GetAccessCtrl',
                    controllerAs: 'access'
                }
            }
        }).state('root.contactUs', {
            url: '/contactUs',
            views: {
                'content@': {
                    templateUrl: 'app/views/support/contact-us/contact-us.tpl.html',
                    controller: 'ContactUsCtrl',
                    controllerAs: 'contact'
                }
            }
        }).state('root.userNotifications', {
            url: '/userNotifications',
            views: {
                'content@': {
                    templateUrl: 'app/views/user-notifications-admin/user.notifications.tpl.html',
                    controller: 'userNotificationsCtrl',
                    controllerAs: 'userNotifications'
                }
            }
        }).state('root.notificationHistory', {
            url: '/notificationHistory',
            views: {
                'content@': {
                    templateUrl: 'app/views/notification-history/notificationhistory.tpl.html',
                    controller: 'notificationHistoryCtrl',
                    controllerAs: 'notificationHistory'
                }
            }
        }).state('root.portalAdmins', {
            url: '/portalAdmins',
            views: {
                'content@': {
                    templateUrl: 'app/views/portal-admin/portal-admin.tpl.html',
                    controller: 'PortalAdminsCtrl',
                    controllerAs: 'portalAdmin'
                }
            }
        }).state('root.webAnalytics', {
            url: '/webAnalytics',
            views: {
                'content@': {
                    templateUrl: 'app/views/webAnalytics/webAnalytics.tpl.html',
                    controller: 'WebAnalyticsCtrl',
                    controllerAs: 'webAnalytics'
                }
            }
        }).state('root.webAnlayticsSource', {
            url: '/addWebAnalyticsSource',
            views: {
                'content@': {
                    templateUrl: 'app/views/webAnalytics/webAnalyticsList.tpl.html',
                    controller: 'WebAnalyticsCtrl',
                    controllerAs: 'webAnalytics'
                }
            }
        }).state('root.error404', {
            url: '/error404',
            views: {
                'content@': {
                    templateUrl: 'app/views/errors/error.tpl.html',
                    controller: 'ErrorCtrl',
                    controllerAs: 'error'
                }
            }
        }).state('noUserError', {
            url: '/noUserError',
            views: {
                'error@': {
                    templateUrl: 'app/views/errors/error.tpl.html',
                    controller: 'ErrorCtrl',
                    controllerAs: 'error'
                }
            }
        }).state('unKnownError', {
            url: '/unKnownError',
            views: {
                'error@': {
                    templateUrl: 'app/views/errors/error.tpl.html',
                    controller: 'ErrorCtrl',
                    controllerAs: 'error'
                }
            }
        });
    });
