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
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { Observable } from 'rxjs';
import * as uuid from 'uuid';

@Injectable({
  providedIn: 'root'
})
export class ApplicationsService {

  api = environment.api;
  resp: string;
  headerParams = { 'X-Widgets-Type': 'all' };

  constructor(private http: HttpClient) { }

  getOnboardingApps(): Observable<any> {
    let getOnboardingAppsURL = this.api.onboardingApps;
    return this.http.get(getOnboardingAppsURL);
  };

  getSingleAppInfo(appName): Observable<any> {
    let getSingleAppInfoURL = this.api.singleAppInfo;
    return this.http.get(getSingleAppInfoURL);
  };

  getSingleAppInfoById(appId) {
    let httpParams = new HttpParams()
      .set('appParam', appId);
    return this.http.get(this.api.singleAppInfoById, { params: httpParams, responseType: 'json' })
  }

  getPersUserApps(): Observable<any> {
    let getPersUserAppsURL = this.api.persUserApps;
    return this.http.get(getPersUserAppsURL);
  };

  getAppsOrderBySortPref(userAppSortTypePref): Observable<any> {
    let getAppsOrderBySortPrefURL = this.api.userAppsOrderBySortPref;
    return this.http.get(getAppsOrderBySortPrefURL);
  }

  checkIfUserIsSuperAdmin(): Observable<any> {
    let checkIfUserIsSuperAdminURL = this.api.checkIfUserIsSuperAdmin;
    return this.http.get(checkIfUserIsSuperAdminURL);
  }

  saveAppsSortTypeManual(appsSortManual: any): Observable<any> {
    let saveAppsSortTypeManualURL = this.api.saveUserAppsSortingManual;
    return this.http.put(saveAppsSortTypeManualURL, appsSortManual);
  }

  saveAppsSortTypePreference(appsSortPreference: any): Observable<any> {
    let saveAppsSortTypePreferenceURL = this.api.saveUserAppsSortingPreference;
    return this.http.put(saveAppsSortTypePreferenceURL, appsSortPreference);
  }

  getUserAppsSortTypePreference(): Observable<any> {
    let getUserAppsSortTypePreferenceURL = this.api.userAppsSortTypePreference;
    return this.http.get(getUserAppsSortTypePreferenceURL);
  }

  saveWidgetsSortManual(widgetsSortManual: any): Observable<any> {
    let saveWidgetsSortManualURL = this.api.saveUserWidgetsSortManual;
    return this.http.put(saveWidgetsSortManualURL, widgetsSortManual);
  }

  delWidgetsSortPref(widgetsData: any): Observable<any> {
    let delWidgetsSortPrefURL = this.api.updateWidgetsSortPref;
    return this.http.put(delWidgetsSortPrefURL, widgetsData);
  }

  getAvailableApps(): Observable<any> {
    let getAvailableAppsURL = this.api.availableApps;
    return this.http.get(getAvailableAppsURL);
  }

  getAdminApps(): Observable<any> {
    let getAdminAppsURL = this.api.adminApps;
    return this.http.get(getAdminAppsURL);
  }

  getLeftMenuItems(): Observable<any> {
    let getLeftMenuItemsURL = this.api.leftmenuItems;
    return this.http.get(getLeftMenuItemsURL);
  }

  getAppsForSuperAdminAndAccountAdmin(): Observable<any> {
    let getAppsForSuperAdminAndAccountAdminURL = this.api.appsForSuperAdminAndAccountAdmin;
    return this.http.get(getAppsForSuperAdminAndAccountAdminURL);
  }

  getAdminAppsSimpler(): Observable<any> {
    let getAdminAppsSimplerURL = this.api.adminApps;
    return this.http.get(getAdminAppsSimplerURL);
  }

  addOnboardingApp(newApp: any): Observable<any> {
    let addOnboardingAppURL = this.api.onboardingApps;
    return this.http.post(addOnboardingAppURL, newApp);
  }

  updateOnboardingApp(appData: any): Observable<any> {
    let updateOnboardingAppURL = this.api.onboardingApps;
    return this.http.put(updateOnboardingAppURL, appData);
  }

  saveUserAppsRoles(UserAppRolesRequest: any): Observable<any> {
    let saveUserAppsRolesURL = this.api.saveUserAppRoles;
    return this.http.put(saveUserAppsRolesURL, UserAppRolesRequest);
  }

  deleteOnboardingApp(appId: any): Observable<any> {
    let deleteOnboardingAppURL = this.api.onboardingApps + '/' + appId;
    return this.http.delete(deleteOnboardingAppURL);
  }

  syncRolesEcompFromExtAuthSystem(appId: any): Observable<any> {
    let syncRolesEcompFromExtAuthSystemURL = this.api.syncRolesFromExternalAuthSystem;
    return this.http.post(syncRolesEcompFromExtAuthSystemURL, appId);
  }

  syncFunctionsFromExternalAuthSystem(appId: any): Observable<any> {
    let syncFunctionsFromExternalAuthSystemURL = this.api.syncFunctionsFromExternalAuthSystem;
    return this.http.post(syncFunctionsFromExternalAuthSystemURL, appId);
  }

  ping(appId): Observable<any> {
    let pingURL = this.api.ping;
    return this.http.get(pingURL);
  }

}
