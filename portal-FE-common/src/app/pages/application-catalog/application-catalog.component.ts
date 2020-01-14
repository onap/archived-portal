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
import { Component, OnInit } from '@angular/core';
import { GridsterConfig, GridsterItem } from 'angular-gridster2';
import { ApplicationCatalogService } from '../../shared/services/application-catalog/application-catalog.service';
import { IApplicationCatalog } from '../../shared/model/application-catalog.model';
import { IWidgetCatalog } from '../../shared/model/widget-catalog.model';
import { environment } from 'src/environments/environment';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CatalogModalComponent } from '../catalog-modal/catalog-modal.component';
import { ExternalRequestAccessService } from 'src/app/shared/services/external-request-access-service/external-request-access.service';
import { UsersService } from 'src/app/shared/services/users/users.service';

@Component({
  selector: 'app-application-catalog',
  templateUrl: './application-catalog.component.html',
  styleUrls: ['./application-catalog.component.scss']
})
export class ApplicationCatalogComponent implements OnInit {
  widgetCatalogData: IWidgetCatalog[];
  appCatalogData: IApplicationCatalog[];
  resultAccessValue: string;
  orgUserId: string;
  firstName: string;
  lastName: string;
  radioValue: any;
  isUserSuperAdmin: boolean;

  get options(): GridsterConfig {
    return this.applicationCatalogService.options;
  } get layout(): GridsterItem[] {
    return this.applicationCatalogService.layout;
  } constructor(private applicationCatalogService: ApplicationCatalogService, private externalRequestAccessService: ExternalRequestAccessService, private userService: UsersService, private modal: NgbModal) { }

  ngOnInit() {
    this.applicationCatalogService.clearCatalog();
    this.radioValue = 'All';
    this.callAppCatalogExecutor();
  }

  callAppCatalogExecutor() {
    //Check whether Admin is Super Admin
    this.checkAdminIsSuperAdmin();
    //To fetch ML value
    this.getExternalAccess();

    //Call user profile service
    this.getUserProfile();

    //Call Application Catalog services
    this.getAppCatalogService();
  }

  checkAdminIsSuperAdmin() {
    this.applicationCatalogService.checkIfUserIsSuperAdmin().subscribe(data => {
      this.isUserSuperAdmin = data;
    }, error => {
      console.log('checkAdminIsSuperAdmin Error Object' + error);
    });
  }

  getAppCatalogService() {
    //console.log("getAppCatalogServices called");
    this.applicationCatalogService.getAppCatalog().subscribe(data => {
      //console.log("Response data" + data);
      this.appCatalogData = data;
      for (let entry of this.appCatalogData) {
        //console.log("Check the URL" + environment.api.appThumbnail);
        var appCatalog = {
          x: -1,
          y: -1,
          id: entry.id,
          name: entry.name,
          mlAppName: entry.mlAppName,
          imageLink: environment.api.appThumbnail.replace(':appId', <string><any>entry.id),
          restricted: entry.restricted,
          select: entry.select,
          access: entry.access,
          pending: entry.pending,
          mlproperty: this.resultAccessValue
        };
        this.applicationCatalogService.addItem(appCatalog);
      }
    }, error => {
      console.log('getAppCatalogServices Error Object' + error);
    });
  };

  storeSelection(appCatalogData: any) {
    //console.log("Store selection called " + appCatalogData.name);
    var pendingFlag: boolean = false;
    if (appCatalogData.access)
      pendingFlag = false;
    else
      pendingFlag = appCatalogData.pending;

    var appData = {
      appId: appCatalogData.id,
      select: appCatalogData.select,
      pending: pendingFlag
    };
    this.applicationCatalogService.updateManualAppSort(appData).subscribe(data => {
      //console.log("Update App sort data" + data);
    }, error => {
      console.log('Update App sort error' + error);
    });

    this.applicationCatalogService.updateAppCatalog(appData).subscribe(data => {
      //console.log("Update App Catalog data" + data);
    }, error => {
      console.log('Update App Catalog error' + error);
    });
  };
  openAddRoleModal(item: any) {
    //console.log("OpenModal check" + item.id);
    if ((!item.restricted) && (item.mlproperty)) {
      this.modal.open(CatalogModalComponent);
    }
  }

  getExternalAccess() {
    //console.log("getExternalAccess service called");
    this.externalRequestAccessService.getExternalRequestAccessServiceInfo().subscribe(data => {
      //console.log("Response data" + data);
      if (data)
        this.resultAccessValue = data.accessValue;
    }, error => {
      console.log('getExternalAccess Error object' + error);
    });
  }

  getUserProfile() {
    const userProfileObservable = this.userService.getUserProfile();
    userProfileObservable.subscribe((userProfile: any) => {
      //console.log('UserProfile is ' + userProfile);
      if (userProfile) {
        this.orgUserId = userProfile.orgUserId;
        this.firstName = userProfile.firstName;
        this.lastName = userProfile.lastName;
      }
    });

  }
}
