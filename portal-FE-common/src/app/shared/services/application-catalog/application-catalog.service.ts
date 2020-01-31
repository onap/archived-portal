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
import { Injectable } from '@angular/core';
import { GridsterConfig, GridsterItem, DisplayGrid, GridType } from 'angular-gridster2';
import { Observable } from 'rxjs';
import { IApplicationCatalog } from '../../model/application-catalog.model';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ApplicationCatalogService {
  public options: GridsterConfig = {
    minCols: 6,
    maxCols: 6,
    minRows: 7,
    //maxRows: 4,
    maxItemCols: 50,
    minItemCols: 1,
    maxItemRows: 50,
    minItemRows: 1,
    maxItemArea: 2500,
    minItemArea: 1,
    defaultItemCols: 1,
    defaultItemRows: 1,
    setGridSize: false,
    fixedColWidth: 250,
    fixedRowHeight: 250,
    gridType: GridType.ScrollVertical,
    swap: true,
    dynamicColumns: true,
    displayGrid: DisplayGrid.None,
    itemChangeCallback: this.itemChange,


    draggable: {
      enabled: true
    },
    pushItems: true,
    resizable: {
      enabled: true
    }
  };
  public layout: GridsterItem[] = [];
  constructor(private api: HttpClient) { }

  addItem(appData: any): void {
    this.layout.push(appData);
  }
  getAppCatalog(): Observable<any> {
    return this.api.get(environment.api.appCatalog);
  }
  updateAppCatalog(appData: any): Observable<any> {
    return this.api.put(environment.api.appCatalog, appData);
  }
  updateManualAppSort(appData: any): Observable<any> {
    return this.api.put(environment.api.UpdateUserAppsSortManual, appData);
  }
  getuserAppRolesCatalog(appName: string): Observable<any> {
    return this.api.get(environment.api.appCatalogRoles);
  }
  getAppsFullList(): Observable<any> {
    return this.api.get(environment.api.appsFullList);
  }
  getUserAppsSortTypePreference(): Observable<any> {
    const headers = new HttpHeaders().set('Content-Type','text/plain;charset=utf-8');
    return this.api.get(environment.api.userAppsSortTypePreference,{headers,responseType:'text'});
  }
  getAppsOrderBySortPref(appSortPrefData: any): Observable<any> {
    let httpParam = new  HttpParams().set('mparams', appSortPrefData);
    return this.api.get(environment.api.userAppsOrderBySortPref, {params: httpParam});
  }
  saveAppsSortTypePreference(userPrefData: any): Observable<any> {
    return this.api.put(environment.api.saveUserAppsSortingPreference, userPrefData);
  }
  itemChange(item, itemComponent) {
    //console.info('itemChanged', item, itemComponent);
    if(this.layout)
    {
    //console.log("check the layout value "+this.layout.values);
    }
  }
  checkIfUserIsSuperAdmin(): Observable<any> {
    let checkIfUserIsSuperAdminURL = environment.api.checkIfUserIsSuperAdmin;
    return this.api.get(checkIfUserIsSuperAdminURL);
  }
  clearCatalog(): void {
    this.layout =[];
  }

}
