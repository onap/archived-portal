/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
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
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatCheckboxModule } from '@angular/material/checkbox';

import { AdminsComponent } from './admins/admins.component';
import { ApplicationCatalogComponent } from './application-catalog/application-catalog.component';
import { ApplicationOnboardingComponent } from './application-onboarding/application-onboarding.component';
import { ApplicationDetailsDialogComponent } from './application-onboarding/application-details-dialog/application-details-dialog.component';
import { AccountOnboardingComponent } from './account-onboarding/account-onboarding.component';
import { ApplicationCatalogService } from '../shared/services/application-catalog/application-catalog.service';
import { ContactUsComponent } from './contact-us/contact-us.component';
import { ContactUsManageComponent } from './contact-us/contact-us-manage/contact-us-manage.component';
import { ConfirmationModalComponent } from '../modals/confirmation-modal/confirmation-modal.component';
import { CatalogModalComponent } from './catalog-modal/catalog-modal.component';
import { DashboardComponent } from '../pages/dashboard/dashboard.component';
import { FunctionalMenuComponent } from './functional-menu/functional-menu.component';
import { GridsterModule } from 'angular-gridster2';
import { InformationModalComponent } from '../modals/information-modal/information-modal.component';
import { NgMaterialModule } from '../ng-material-module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NewPortalAdminComponent } from './portal-admins/new-portal-admin/new-portal-admin.component';
import { NotificationHistoryComponent } from './notification-history/notification-history.component';
import { PagesComponent } from './pages.component';
import { PagesRoutingModule } from './pages-routing.module';
import { PortalAdminsComponent } from './portal-admins/portal-admins.component';
import { RoleComponent } from './role/role.component';
import { SearchUsersComponent } from '../layout/components/search-users/search-users.component';
import { SchedulerComponent } from './scheduler/scheduler.component';
import { UserNotificationAdminComponent } from './user-notification-admin/user-notification-admin.component';
import { WidgetsComponent } from './widgets/widgets.component';
import { WidgetCatalogComponent } from './widget-catalog/widget-catalog.component';
import { WebAnalyticsComponent } from './web-analytics/web-analytics.component';
import { NewAdminComponent } from './admins/new-admin/new-admin.component';
import { ExternalRequestAccessService } from '../shared/services/external-request-access-service/external-request-access.service';
import { UsersService } from '../shared/services/users/users.service';
import { DynamicWidgetComponent } from './dynamic-widget/dynamic-widget.component';
import { MicroserviceOnboardingComponent } from './microservice-onboarding/microservice-onboarding.component';
import { WidgetOnboardingComponent } from './widget-onboarding/widget-onboarding.component';
import { WebAnalyticsDetailsDialogComponent } from './web-analytics/web-analytics-details-dialog/web-analytics-details-dialog.component';
import { BulkUploadRoleComponent } from './role/bulk-upload-role/bulk-upload-role.component';
import { AddRoleComponent } from './role/add-role/add-role.component';
import { RoleFunctionsComponent } from './role/role-functions/role-functions.component';
import { RoleFunctionModalComponent } from './role/role-functions/role-function-modal/role-function-modal.component';
import { NewNotificationModalComponent } from './user-notification-admin/new-notification-modal/new-notification-modal.component';
import { AccountAddDetailsComponent } from './account-onboarding/account-add-details/account-add-details.component';
import { MicroserviceAddDetailsComponent } from './microservice-onboarding/microservice-add-details/microservice-add-details.component';
import { DashboardApplicationCatalogComponent } from './dashboard-application-catalog/dashboard-application-catalog.component';
import { DashboardWidgetCatalogComponent } from './dashboard-widget-catalog/dashboard-widget-catalog.component';
import { WidgetDetailsDialogComponent } from './widget-onboarding/widget-details-dialog/widget-details-dialog.component';
import { FunctionalMenuDialogComponent } from './functional-menu/functional-menu-dialog/functional-menu-dialog.component';
import { GetAccessComponent } from './get-access/get-access.component';
import { PluginModule } from '../shared/plugin/plugin.module';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { HeaderInterceptor } from '../shared/interceptors/header-interceptor';
import { ApplicationPipesModule } from '../shared/pipes/application-pipes.module';
import { UsersComponent } from './users/users.component';
import { NewUserModalComponent } from './users/new-user-modal/new-user-modal.component';
import { BulkUserComponent } from './users/bulk-user/bulk-user.component';
import { UserDetailsFormComponent } from './users/user-details-form/user-details-form.component';


@NgModule({
  declarations: [
    PagesComponent,
    DashboardComponent,
    PortalAdminsComponent,
    AccountOnboardingComponent,
    FunctionalMenuComponent,
    MicroserviceOnboardingComponent,
    NotificationHistoryComponent,
    RoleComponent,
    SchedulerComponent,
    UserNotificationAdminComponent,
    UsersComponent,
    WidgetCatalogComponent,
    WidgetOnboardingComponent,
    WidgetsComponent,
    WebAnalyticsComponent,
    NewAdminComponent,
    NewUserModalComponent,
    BulkUserComponent,
    SearchUsersComponent,
    InformationModalComponent,
    ConfirmationModalComponent,
    AdminsComponent,
    NewPortalAdminComponent,
    SchedulerComponent,
    UserNotificationAdminComponent,
    WidgetCatalogComponent,
    ApplicationOnboardingComponent,
    AccountOnboardingComponent,
    ApplicationDetailsDialogComponent,
    ContactUsComponent,
    ContactUsManageComponent,
    WebAnalyticsDetailsDialogComponent,
    ApplicationCatalogComponent,
    WidgetCatalogComponent,
    CatalogModalComponent,
    DynamicWidgetComponent,
    BulkUploadRoleComponent,
    AddRoleComponent,
    RoleFunctionsComponent,
    RoleFunctionModalComponent,
    NewNotificationModalComponent,
    AccountAddDetailsComponent,
    MicroserviceAddDetailsComponent,
    WidgetDetailsDialogComponent,
    DashboardApplicationCatalogComponent,
    DashboardWidgetCatalogComponent,
    FunctionalMenuDialogComponent,
    GetAccessComponent,
    UserDetailsFormComponent
  ],
  imports: [
    CommonModule,
    NgMaterialModule,
    ReactiveFormsModule,
    FormsModule,
    PagesRoutingModule,
    ApplicationPipesModule,
    NgbModule,
    GridsterModule,
    MatIconModule,
    MatCheckboxModule,
    FormsModule,
    PluginModule
  ],
  entryComponents: [
    SchedulerComponent,
    InformationModalComponent,
    SearchUsersComponent,
    ConfirmationModalComponent,
    NewPortalAdminComponent,
    NewAdminComponent,
    BulkUserComponent,
    NewUserModalComponent,
    ApplicationDetailsDialogComponent,
    ContactUsManageComponent,
    CatalogModalComponent,
    WebAnalyticsDetailsDialogComponent,
    AddRoleComponent,
    BulkUploadRoleComponent,
    RoleFunctionModalComponent,
    NewNotificationModalComponent,
    AccountAddDetailsComponent,
    MicroserviceAddDetailsComponent,
    WidgetDetailsDialogComponent,
    FunctionalMenuDialogComponent
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HeaderInterceptor,
      multi: true,
    }],


})
export class PagesModule {

  constructor(public ngbModalService: NgbModal) {
    this.addSchdulerEventListners();
  }


  addSchdulerEventListners() {
    let eventMethod = window.addEventListener ? "addEventListener" : "attachEvent";
    let eventer = window[eventMethod];
    let messageEvent = eventMethod == "attachEvent" ? "onmessage" : "message";

    eventer(messageEvent, function (e) {
      if (e.data != null && e.data['widgetName'] == 'Portal-Common-Scheduler') {
        this.openAddSchedulerModal(e.data);
      }
    }.bind(this), false);

  }

  openAddSchedulerModal(rowData: any) {
    const modalRef = this.ngbModalService.open(SchedulerComponent, { size: 'lg' });
    modalRef.componentInstance.title = 'Scheduler Change';
    console.log("selectedData in parent", rowData);
    if (rowData != 'undefined' && rowData) {
      modalRef.componentInstance.payload = rowData;
    } else {
      modalRef.componentInstance.payload = {};
    }

  }
}
