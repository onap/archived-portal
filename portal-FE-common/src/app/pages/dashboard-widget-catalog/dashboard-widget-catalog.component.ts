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
import { WidgetCatalogService } from '../../shared/services/widget-catalog/widget-catalog.service';
import { IWidgetCatalog } from '../../shared/model/widget-catalog.model';
import { Observable } from 'rxjs';
import { inflateRaw } from 'zlib';
import { UsersService } from 'src/app/shared/services/users/users.service';

@Component({
  selector: 'app-dashboard-widget-catalog',
  templateUrl: './dashboard-widget-catalog.component.html',
  styleUrls: ['./dashboard-widget-catalog.component.scss']
})
export class DashboardWidgetCatalogComponent implements OnInit {

 
  widgetCatalogData: IWidgetCatalog[];
  isCommonError: boolean = false;
  isApiRunning: boolean = true;
  userName: string;


  get options(): GridsterConfig {
    return this.widgetCatalogService.options;
  } get layout(): GridsterItem[] {
    return this.widgetCatalogService.layout;
  } constructor(private widgetCatalogService: WidgetCatalogService, private userService: UsersService) {

  }

  ngOnInit() {
    const widgetCatalogObservable = this.widgetCatalogService.getWidgetCatalog();
    this.widgetCatalogService.layout = [];
    this.getUserWidgets(this.userName);
  }


  getUserWidgets(loginName: string) {
    const widgetCatalogUserObservable = this.userService.getUserProfile();
    widgetCatalogUserObservable.subscribe((userProfile: any) => {
      //console.log('UserProfile is ' + userProfile);
      if (userProfile) {
        const widgetCatalogObservable = this.widgetCatalogService.getUserWidgets(userProfile.orgUserId);
        widgetCatalogObservable.subscribe(data => {
          //console.log("What is coming from backend" + JSON.parse(data));
          this.widgetCatalogData = data;
          console.log(this.widgetCatalogData);
		  if(this.widgetCatalogData != null){
			  for (let entry of this.widgetCatalogData) {
            if (entry[1] == 'Events' || entry[1] == 'News' || entry[1] == 'Resources') {
              if(entry[4] === 'S' || entry[4] === null)
              {
              var appCatalog = {
                id: entry[0],
                name: entry[1],
                headerName: entry[2],
                //select: (entry[4] === 'S' || entry[4] === null) ? true : false
              };
              this.widgetCatalogService.addItem(appCatalog);
            }
            }
          }
		  }
        });
      }
    });

  }

  getUserProfile(): Observable<any> {
    const widgetCatalogObservable = this.userService.getUserProfile();
    return widgetCatalogObservable;
  }

  storeSelection(widgetCatalogData: any) {
    console.log("Store selection called " + widgetCatalogData.select);
    if (widgetCatalogData && widgetCatalogData.select) {
      var appData = {
        widgetId: widgetCatalogData.id,
        select: widgetCatalogData.select,
        pending:false
      };
      this.widgetCatalogService.updateWidgetCatalog(appData).subscribe(data => {
        //console.log("Update App sort data" + data);
      }, error => {
        console.log('updateWidgetCatalog error' + error);
      });
    }

  }
}
