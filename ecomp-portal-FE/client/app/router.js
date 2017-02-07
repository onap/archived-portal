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
                    controllerAs: 'footer'                }
            }
//        }).state('root.applicationsHome', {
//            url: '/applicationsHome',
//            views: {
//            	'content@': {
//                    templateUrl: 'app/views/home/applications-home/applications-home.tpl.html',
//                    controller: 'ApplicationsHomeCtrl',
//                    controllerAs: 'applicationsHome'
//                }
//            }
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
        }).state('root.widgetsHome', {
            url: '/widgetsHome',
            views: {
                'content@': {
                    templateUrl: 'app/views/home/widgets-home/widgets-home.tpl.html',
                    controller: 'WidgetsHomeCtrl',
                    controllerAs: 'widgetsHome'
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
        }).state('root.widgets', {
            url: '/widgets',
            views: {
                'content@': {
                    templateUrl: 'app/views/widgets/widgets.tpl.html',
                    controller: 'WidgetsCtrl',
                    controllerAs: 'widgets'
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
        }).state('root.error404', {
            url: '/error404',
            views: {
                'content@': {
                    templateUrl: 'app/views/errors/error.404.tpl.html',
                    controller: 'Error404Ctrl',
                    controllerAs: 'error404'
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
        }).state('root.kpidash', {
            url: '/kpidash',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/DCAE/DCAE_KPI.html',
                    controller: 'DCAE_Ctrl_KPI'
                }
            }
        }).state('root.kpidash_DCAE', {
            //url: '/kpidash_DCAE',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/DCAE/DCAE_KPI.html',
                    controller: 'DCAE_Ctrl_KPI'
                }
            }
        }).state('root.kpidash_DCAE_KPI', {
            //url: '/kpidash_DCAE_KPI',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/DCAE/DCAE_KPI.html',
                    controller: 'DCAE_Ctrl_KPI'
                }
            }
        }).state('root.kpidash_DCAE_UserDefinedKPI', {
            //url: '/kpidash_DCAE_UserDefinedKPI',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/DCAE/DCAE_UserDefinedKPI.html',
                    controller: 'DCAE_Ctrl_UserDefinedKPI'
                }
            }
        }).state('root.kpidash_DCAE_Metrics', {
            //url: '/kpidash_DCAE_Metrics',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/DCAE/DCAE_Metrics.html',
                    controller: 'DCAE_Ctrl_Metrics'
                }
            }
        }).state('root.kpidash_ECOMP', {
            //url: '/kpidash_ECOMP',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/ECOMP.html',
                    controller: 'ECOMP_Ctrl'
                }
            }
        }).state('root.kpidash_AAI', {
            //url: '/kpidash_AAI',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/AAI/AAI_KPI.html',
                    controller: 'AAI_Ctrl_KPI'
                }
            }
        }).state('root.kpidash_AAI_KPI', {
            //url: '/kpidash_AAI_KPI',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/AAI/AAI_KPI.html',
                    controller: 'AAI_Ctrl_KPI'
                }
            }
        }).state('root.kpidash_AAI_UserDefinedKPI', {
            //url: '/kpidash_AAI_UserDefinedKPI',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/AAI/AAI_UserDefinedKPI.html',
                    controller: 'AAI_Ctrl_UserDefinedKPI'
                }
            }
        }).state('root.kpidash_AAI_Metrics', {
            //url: '/kpidash_AAI_Metrics',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/AAI/AAI_Metrics.html',
                    controller: 'AAI_Ctrl_Metrics'
                }
            }
        }).state('root.kpidash_APPC', {
            //url: '/kpidash_APPC',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/APPC/APPC_KPI.html',
                    controller: 'APPC_Ctrl_KPI'
                }
            }
        }).state('root.kpidash_APPC_KPI', {
            //url: '/kpidash_APPC_KPI',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/APPC/APPC_KPI.html',
                    controller: 'APPC_Ctrl_KPI'
                }
            }
        }).state('root.kpidash_APPC_UserDefinedKPI', {
            //url: '/kpidash_APPC_UserDefinedKPI',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/APPC/APPC_UserDefinedKPI.html',
                    controller: 'APPC_Ctrl_UserDefinedKPI'
                }
            }
        }).state('root.kpidash_APPC_Metrics', {
            //url: '/kpidash_APPC_Metrics',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/APPC/APPC_Metrics.html',
                    controller: 'APPC_Ctrl_Metrics'
                }
            }
        }).state('root.kpidash_ASDC', {
            //url: '/kpidash_ASDC',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/ASDC/ASDC_KPI.html',
                    controller: 'ASDC_Ctrl_KPI'
                }
            }
        }).state('root.kpidash_ASDC_KPI', {
            //url: '/kpidash_ASDC_KPI',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/ASDC/ASDC_KPI.html',
                    controller: 'ASDC_Ctrl_KPI'
                }
            }
        }).state('root.kpidash_ASDC_UserDefinedKPI', {
            //url: '/kpidash_ASDC_UserDefinedKPI',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/ASDC/ASDC_UserDefinedKPI.html',
                    controller: 'ASDC_Ctrl_UserDefinedKPI'
                }
            }
        }).state('root.kpidash_ASDC_Metrics', {
            //url: '/kpidash_ASDC_Metrics',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/ASDC/ASDC_Metrics.html',
                    controller: 'ASDC_Ctrl_Metrics'
                }
            }
        }).state('root.kpidash_Closedloop', {
            //url: '/kpidash_Closedloop',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/Closedloop/Closedloop_KPI.html',
                    controller: 'Closedloop_Ctrl_KPI'
                }
            }
        }).state('root.kpidash_Closedloop_KPI', {
            //url: '/kpidash_Closedloop_KPI',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/Closedloop/Closedloop_KPI.html',
                    controller: 'Closedloop_Ctrl_KPI'
                }
            }
        }).state('root.kpidash_Closedloop_UserDefinedKPI', {
            //url: '/kpidash_Closedloop_UserDefinedKPI',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/Closedloop/Closedloop_UserDefinedKPI.html',
                    controller: 'Closedloop_Ctrl_UserDefinedKPI'
                }
            }
        }).state('root.kpidash_Closedloop_Metrics', {
            //url: '/kpidash_Closedloop_Metrics',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/Closedloop/Closedloop_Metrics.html',
                    controller: 'Closedloop_Ctrl_Metrics'
                }
            }
        }).state('root.kpidash_DMaaP', {
            //url: '/kpidash_DMaaP',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/DMaaP/DMaaP_KPI.html',
                    controller: 'DMaaP_Ctrl_KPI'
                }
            }
        }).state('root.kpidash_DMaaP_KPI', {
            //url: '/kpidash_DMaaP_KPI',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/DMaaP/DMaaP_KPI.html',
                    controller: 'DMaaP_Ctrl_KPI'
                }
            }
        }).state('root.kpidash_DMaaP_UserDefinedKPI', {
            //url: '/kpidash_DMaaP_UserDefinedKPI',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/DMaaP/DMaaP_UserDefinedKPI.html',
                    controller: 'DMaaP_Ctrl_UserDefinedKPI'
                }
            }
        }).state('root.kpidash_DMaaP_Metrics', {
            //url: '/kpidash_DMaaP_Metrics',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/DMaaP/DMaaP_Metrics.html',
                    controller: 'DMaaP_Ctrl_Metrics'
                }
            }
        }).state('root.kpidash_ECOMP_Portal', {
            //url: '/kpidash_ECOMP_Portal',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/ECOMP_Portal/ECOMP_Portal_KPI.html',
                    controller: 'ECOMP_Portal_Ctrl_KPI'
                }
            }
        }).state('root.kpidash_ECOMP_Portal_KPI', {
            //url: '/kpidash_ECOMP_Portal_KPI',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/ECOMP_Portal/ECOMP_Portal_KPI.html',
                    controller: 'ECOMP_Portal_Ctrl_KPI'
                }
            }
        }).state('root.kpidash_ECOMP_Portal_UserDefinedKPI', {
            //url: '/kpidash_ECOMP_Portal_UserDefinedKPI',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/ECOMP_Portal/ECOMP_Portal_UserDefinedKPI.html',
                    controller: 'ECOMP_Portal_Ctrl_UserDefinedKPI'
                }
            }
        }).state('root.kpidash_ECOMP_Portal_Metrics', {
            //url: '/kpidash_ECOMP_Portal_Metrics',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/ECOMP_Portal/ECOMP_Portal_Metrics.html',
                    controller: 'ECOMP_Portal_Ctrl_Metrics'
                }
            }
        }).state('root.kpidash_InfrastructurePortal', {
            //url: '/kpidash_InfrastructurePortal',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/InfrastructurePortal/InfrastructurePortal_KPI.html',
                    controller: 'InfrastructurePortal_Ctrl_KPI'
                }
            }
        }).state('root.kpidash_InfrastructurePortal_KPI', {
            //url: '/kpidash_InfrastructurePortal_KPI',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/InfrastructurePortal/InfrastructurePortal_KPI.html',
                    controller: 'InfrastructurePortal_Ctrl_KPI'
                }
            }
        }).state('root.kpidash_InfrastructurePortal_UserDefinedKPI', {
            //url: '/kpidash_InfrastructurePortal_UserDefinedKPI',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/InfrastructurePortal/InfrastructurePortal_UserDefinedKPI.html',
                    controller: 'InfrastructurePortal_Ctrl_UserDefinedKPI'
                }
            }
        }).state('root.kpidash_InfrastructurePortal_Metrics', {
            //url: '/kpidash_InfrastructurePortal_Metrics',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/InfrastructurePortal/InfrastructurePortal_Metrics.html',
                    controller: 'InfrastructurePortal_Ctrl_Metrics'
                }
            }
        }).state('root.kpidash_MSO', {
            //url: '/kpidash_MSO',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/MSO/MSO_KPI.html',
                    controller: 'MSO_Ctrl_KPI'
                }
            }
        }).state('root.kpidash_MSO_KPI', {
            //url: '/kpidash_MSO_KPI',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/MSO/MSO_KPI.html',
                    controller: 'MSO_Ctrl_KPI'
                }
            }
        }).state('root.kpidash_MSO_UserDefinedKPI', {
            //url: '/kpidash_MSO_UserDefinedKPI',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/MSO/MSO_UserDefinedKPI.html',
                    controller: 'MSO_Ctrl_UserDefinedKPI'
                }
            }
        }).state('root.kpidash_MSO_Metrics', {
            //url: '/kpidash_MSO_Metrics',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/MSO/MSO_Metrics.html',
                    controller: 'MSO_Ctrl_Metrics'
                }
            }
        }).state('root.kpidash_Policy', {
            //url: '/kpidash_Policy',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/Policy/Policy_KPI.html',
                    controller: 'Policy_Ctrl_KPI'
                }
            }
        }).state('root.kpidash_Policy_KPI', {
            //url: '/kpidash_Policy_KPI',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/Policy/Policy_KPI.html',
                    controller: 'Policy_Ctrl_KPI'
                }
            }
        }).state('root.kpidash_Policy_UserDefinedKPI', {
            //url: '/kpidash_Policy_UserDefinedKPI',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/Policy/Policy_UserDefinedKPI.html',
                    controller: 'Policy_Ctrl_UserDefinedKPI'
                }
            }
        }).state('root.kpidash_Policy_Metrics', {
            //url: '/kpidash_Policy_Metrics',
            views: {
                'content@': {
                    templateUrl: 'kpi-dashboard/views/Policy/Policy_Metrics.html',
                    controller: 'Policy_Ctrl_Metrics'
                }
            }
        });
    });
