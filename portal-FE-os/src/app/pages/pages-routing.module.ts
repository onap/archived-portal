/*
 * ============LICENSE_START==========================================
 * ONAP Portal SDK
 * ===================================================================
 * Copyright � 2019 AT&T Intellectual Property. All rights reserved.
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
 * 
 */
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AdminsComponent } from './admins/admins.component';
import { DashboardComponent } from '../pages/dashboard/dashboard.component';
import { PortalAdminsComponent } from './portal-admins/portal-admins.component';
import { RoleComponent } from './role/role.component';
import { UsersComponent } from './users/users.component';
import { FunctionalMenuComponent } from './functional-menu/functional-menu.component';
import { UserNotificationAdminComponent } from './user-notification-admin/user-notification-admin.component';
import { WebAnalyticsComponent } from './web-analytics/web-analytics.component';
import { ApplicationCatalogComponent } from './application-catalog/application-catalog.component';
import { WidgetCatalogComponent } from './widget-catalog/widget-catalog.component';
import { MicroserviceOnboardingComponent } from './microservice-onboarding/microservice-onboarding.component';
import { ApplicationOnboardingComponent } from './application-onboarding/application-onboarding.component';
import { WidgetOnboardingComponent } from './widget-onboarding/widget-onboarding.component';
import { AccountOnboardingComponent } from './account-onboarding/account-onboarding.component';
import { ContactUsComponent } from './contact-us/contact-us.component';
import { RoleFunctionsComponent } from './role/role-functions/role-functions.component';
import { NotificationHistoryComponent } from './notification-history/notification-history.component';
import { GetAccessComponent } from './get-access/get-access.component';

const routes: Routes = [
    { path: '', component: DashboardComponent },
    { path: 'applicationsHome', component: DashboardComponent },
    { path: 'admins', component: AdminsComponent },
    { path: 'portalAdmins', component: PortalAdminsComponent },
    { path: 'appCatalog', component: ApplicationCatalogComponent },
    { path: 'widgetCatalog', component: WidgetCatalogComponent },
    { path: 'roles', component: RoleComponent },
    { path: 'roleFunctions', component: RoleFunctionsComponent },
    { path: 'users', component: UsersComponent },
    { path: 'applications', component: ApplicationOnboardingComponent },
    { path: 'widgetOnboarding', component: WidgetOnboardingComponent },
    { path: 'functionalMenu', component: FunctionalMenuComponent },
    { path: 'userNotifications', component: UserNotificationAdminComponent },
    { path: 'microserviceOnboarding', component: MicroserviceOnboardingComponent },
    { path: 'accountOnboarding', component: AccountOnboardingComponent },
    { path: 'webAnlayticsSource', component: WebAnalyticsComponent },
    { path: 'contactUs', component: ContactUsComponent },
    { path: 'getAccess', component: GetAccessComponent },
    { path: 'recentNotifications', component: NotificationHistoryComponent },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class PagesRoutingModule { }



