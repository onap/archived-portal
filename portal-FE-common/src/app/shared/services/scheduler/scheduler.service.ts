/*
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2019 AT&T Intellectual Property. All rights reserved.
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
export class SchedulerService {

  api = environment.api;

  constructor(private http: HttpClient) { }

  /** get scheduler uuID **/
  getStatusSchedulerId(schedulerInfo:any): Observable<any> {
    let getStatusSchedulerByIdURL = this.api.getSchedulerId+'?r='+ Math.random();
    return this.http.post(getStatusSchedulerByIdURL,schedulerInfo);
  }

  /** get time slots for Range scheduler **/
  getTimeslotsForScheduler(schedulerID:any): Observable<any> {
    let getTimeslotsForSchedulerURL = this.api.getTimeslotsForScheduler + '/' + schedulerID + '?r=' + Math.random();
    return this.http.get(getTimeslotsForSchedulerURL);
  }

  /** postSubmitForApprovedTimeslots **/
  postSubmitForApprovedTimeslots(approvedTimeSlotsObj:any): Observable<any> {
    let getStatusSchedulerByIdURL = this.api.postSubmitForApprovedTimeslots+'?r='+ Math.random();
    return this.http.post(getStatusSchedulerByIdURL,approvedTimeSlotsObj);
  }

  /** Get policy information from BE **/
  getPolicyInfo(): Observable<any> {
    let getPolicyInfoURL = this.api.getPolicy+'?r='+ Math.random();
    return this.http.get<Map<String, any>>(getPolicyInfoURL);
  }

  /** get Scheduler UI constants from BE **/
  getSchedulerConstants(): Observable<any> {
    let getSchedulerConstantsURL = this.api.getSchedulerConstants;
    return this.http.get(getSchedulerConstantsURL);
  }

  /** getWidgetData **/
  getWidgetData(widgetId: any,widgetData: any, widgetParam: any){
    let widgetInfo={
      id: widgetId,
      data: widgetData,
      param: widgetParam
    }
    return widgetInfo;
  }

  showWidget(widgetId: any,widgetData: any, widgetParam: any) {
    let widgetInfo={
      id: widgetId,
      data: widgetData,
      param: widgetParam
    }
    //Need to open  popup here
  }
}
