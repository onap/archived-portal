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
import { AddTabFunctionService } from 'src/app/shared/services/tab/add-tab-function.service';
import { AuditLogService } from 'src/app/shared/services/auditLog/audit-log.service';

@Component({
  selector: 'app-dashboard-application-catalog',
  templateUrl: './dashboard-application-catalog.component.html',
  styleUrls: ['./dashboard-application-catalog.component.scss']
})
export class DashboardApplicationCatalogComponent implements OnInit {

  widgetCatalogData: IWidgetCatalog[];
  appCatalogData: IApplicationCatalog[];
  resultAccessValue: string;
  orgUserId: string;
  firstName: string;
  lastName: string;
  selectedSortType: any;
  sortOptions: Array<any>;

  get options(): GridsterConfig {
    return this.applicationCatalogService.options;
  } get layout(): GridsterItem[] {
    return this.applicationCatalogService.layout;
  } constructor(private applicationCatalogService: ApplicationCatalogService, private externalRequestAccessService: ExternalRequestAccessService, private userService: UsersService,private addTabFuntionService: AddTabFunctionService, private auditLogService: AuditLogService) {
    this.sortOptions = [{
      index: 0,
      value: 'N',
      title: 'Name'
    },
    {
      index: 1,
      value: 'L',
      title: 'Last used'
    },
    {
      index: 2,
      value: 'F',
      title: 'Most used'
    },
    {
      index: 3,
      value: 'M',
      title: 'Manual'
    }
    ];
    this.selectedSortType = {};
   }

  ngOnInit() {
    this.applicationCatalogService.clearCatalog();
    //this.selectedSortType = this.sortOptions[0];
    this.getUserAppsSortTypePreference();
    //
    // //this.getUserAppsSortTypePreference();
    //this.getAppCatalogService('N');
  }

  getUserAppsSortTypePreference() {
    this.applicationCatalogService.getUserAppsSortTypePreference().subscribe(data => {
      //console.log("getUserAppsSortTypePreference data"+data);
      if (data) {
        var resJson: any = {};
        resJson.value = data;
        if (resJson.value === "N" || resJson.value === "") {
          resJson.index = 0;

        } else if (resJson.value === "L") {
          resJson.index = 1;

        } else if (resJson.value === "F") {
          resJson.index = 2;

        } else {
          resJson.index = 3;

        } 
	  this.selectedSortType = this.sortOptions[resJson.index];
        //console.log(this.selectedSortType);
      this.getAppCatalogService(data);		
      }
	  else {
		  this.sortTypeChanged('N');
	  }
	  
		  
    }, error => {
      console.log('getUserAppsSortTypePreference Error Object' + error.message);
    });

  }

  sortTypeChanged(userAppSortTypePref: string) {
    //console.log("check whether get into the method");
    if (!userAppSortTypePref) {
      this.selectedSortType = this.sortOptions[0];
    }
    else {
      this.sortOptions.forEach(obj => {
        if (obj.value == userAppSortTypePref) {
          this.selectedSortType = obj;
        }
      })

    }

    this.getAppCatalogService(userAppSortTypePref);
    this.saveAppsSortTypePreference(this.selectedSortType);

  }

  getAppCatalogService(userAppSortTypePref: string) {
    //console.log("getAppCatalogServices called");
	if(!userAppSortTypePref)
    {
      userAppSortTypePref = "N";
	  this.selectedSortType = this.sortOptions[0];
      //console.log("userAppSortTypePref"+userAppSortTypePref);
    }
    this.applicationCatalogService.getAppsOrderBySortPref(userAppSortTypePref).subscribe(data => {
      //console.log("Response data" + data);
      this.appCatalogData = data;
      if (data) {
        this.applicationCatalogService.layout = [];
        for (let entry of data) {
          //console.log("Check the URL" + environment.api.appThumbnail);
		  if(entry.applicationType != '3'){
          var appCatalog = {
            x: -1,
            y: -1,
            id: entry.id,
            name: entry.name,
            subHeaderText: entry.notes,
            imageLink: environment.api.appThumbnail.replace(':appId', <string><any>entry.id),
            applicationType: entry.applicationType,
            select: entry.select,
            access: entry.access,
            pending: entry.pending,
            order: entry.order,
            url: entry.url,
            appid: entry.id
          };
          this.applicationCatalogService.addItem(appCatalog);
        }
		}
      }
    }, error => {
      console.log('getAppCatalogServices Error Object' + error);
    });
  };

  saveAppsSortTypePreference(selectedSortType: any) {
    this.applicationCatalogService.saveAppsSortTypePreference(selectedSortType).subscribe();

  }

  openAddRoleModal(item: any) {
    //console.log("OpenModal check" + item.id+" "+item.url);
    if (item.applicationType =='2') {
      // Link-based apps open in their own browser tab
      window.open(item.url, '_blank');
    } else{
      var tabContent = {
        id: new Date(),
        title: item.name,
        url: item.url,
        appId: item.appid
    };
      this.addTabFuntionService.filter(tabContent);
    }
  }

  auditLog(app:any) {
    this.auditLogService.storeAudit(app.appid, 'app', app.url).subscribe(data => {
      console.log('App action Saved');
    }, error => {
      console.log('auditLog Save Error' + error);
    });
  }
}