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

@Injectable({
  providedIn: 'root'
})
export class WebAnalyticsService {

  api = environment.api;

  constructor(private http: HttpClient) { }

  getWebAnalyticsAppReports(): Observable<any> {
    let getWebAnalyticsAppReportsURL  = this.api.getUserAppsWebAnalytics;
    return this.http.get(getWebAnalyticsAppReportsURL, { withCredentials: true });
  }

  getWebAnalyticsReportOfApplication(appName: any) {
    let httpParam = new  HttpParams()
    .append('appName', appName);
    let getWebAnalyticsReportOfApplicationURL  = this.api.getWebAnalyticsOfApp;
    return this.http.get(getWebAnalyticsReportOfApplicationURL, {params: httpParam, withCredentials: true});
  }

  getUserJourneyList(appName: any){
    let httpParam = new  HttpParams()
    .append('appName', appName);
    let getUserJourneyListURL = this.api.getUserJourneyAnalyticsReport
    return this.http.get(getUserJourneyListURL, {params: httpParam, withCredentials: true});
  }

  save(reportSource: any): Observable<any> {
    let  saveURL = this.api.addWebAnalyticsReport;
    return this.http.post(saveURL, reportSource, {withCredentials: true})
  }

  updateWebAnalyticsReport(reportSource: any): Observable<any> {
    let  updateWebAnalyticsReportURL = this.api.modifyWebAnalyticsReport;
    return this.http.put(updateWebAnalyticsReportURL, reportSource, {withCredentials: true})
  }

  getAllWebAnalyticsReport(){
    let  updateWebAnalyticsReportURL = this.api.getAllWebAnalytics;
    return this.http.get(updateWebAnalyticsReportURL, { withCredentials: true });
  }

  deleteWebAnalyticsReport(webAnalyticsReport){
    let deleteWebAnalyticsReportURL = this.api.deleteWebAnalyticsReport +'?' + 'resourceId='+ webAnalyticsReport.resourceId;
    return this.http.delete(deleteWebAnalyticsReportURL, { withCredentials: true });
  }

  getAllApplications(){
    let  getAllApplicationsURL = this.api.appsFullList;
    return this.http.get(getAllApplicationsURL, { withCredentials: true });
  }
  
}
