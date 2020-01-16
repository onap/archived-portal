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
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { Observable } from 'rxjs';
import * as uuid from 'uuid';

@Injectable({
  providedIn: 'root'
})
export class WidgetOnboardingService {

  api = environment.api;
  resp:string;

  headerParams = {'X-Widgets-Type': 'all', 'X-ECOMP-RequestID': uuid.v4() };

  constructor(private http: HttpClient) { }

  getManagedWidgets(): Observable<any>{ 
    let getManagedWidgetsURL  = this.api.widgetCommon + '/widgetCatalog';
    return this.http.get(getManagedWidgetsURL , { withCredentials: true } );
  }

  deleteWidget(widgetId: any): Observable<any> {
    let deleteWidgetURL = this.api.widgetCommon + '/widgetCatalog'  + '/' + widgetId;
    return this.http.delete(deleteWidgetURL , { withCredentials: true } );
  }

  downloadWidgetFile(widgetId: any): Observable<any> {
    let downloadWidgetURL = this.api.widgetCommon + '/download/' + widgetId;
    let httpParam = new  HttpParams()
    .append('requestType', 'downloadWidgetFile');
    return this.http.get(downloadWidgetURL,{params: httpParam, responseType: "arraybuffer"});
  }

  getUploadFlag(): Observable<any> {
    let getUploadFlagURL = this.api.widgetCommon + '/uploadFlag';
    return this.http.get(getUploadFlagURL , { withCredentials: true } );
  }

  updateWidgetWithFile(formData: any, widgetId: any, newWidget: any): Observable<any>{
    let updateWidgetWithFileURL = this.api.widgetCommon + '/widgetCatalog/' + widgetId;
    let httpParam = new  HttpParams()
    .append('newWidget', JSON.stringify(newWidget))
    .append('requestType', 'fileUpload');
    return this.http.post(updateWidgetWithFileURL, formData, {params: httpParam, withCredentials: true})
  }

  updateWidget(widgetId: any, widgetData: any): Observable<any> {
    let updateWidgetURL = this.api.widgetCommon  + '/widgetCatalog' + '/' + widgetId;
    return this.http.put(updateWidgetURL, widgetData, { withCredentials: true });
  }

  createWidget(newWidget: any, formData: any): Observable<any> {
    let httpParam = new  HttpParams()
    .append('newWidget', JSON.stringify(newWidget))
    .append('requestType', 'fileUpload');
    let  createWidgetURL = this.api.widgetCommon + '/widgetCatalog';
    return this.http.post(createWidgetURL, formData, {params: httpParam, withCredentials: true})
  }

  populateAvailableApps(): Observable<any> {
    let  populateAvailableAppsURL = this.api.appsForSuperAdminAndAccountAdmin;
    return this.http.get(populateAvailableAppsURL, { withCredentials: true })
  }
}
