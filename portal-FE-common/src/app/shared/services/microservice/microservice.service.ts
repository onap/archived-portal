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
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MicroserviceService {

  api = environment.api;

  constructor(private http: HttpClient) { }

  createService(newMicroservice: any): Observable<any> {
    return this.http.post(this.api.widgetCommon, newMicroservice, { withCredentials: true } );
  }

  updateService(serviceId: string, newMicroservice: any): Observable<any> {
    return this.http.put(this.api.widgetCommon + "/" + serviceId, newMicroservice, { withCredentials: true } );
  }

  deleteService(serviceId: any): Observable<any> {
    let deleteServiceURL = this.api.widgetCommon + "/" + serviceId;
    return this.http.delete(deleteServiceURL, { withCredentials: true });
  }

  getServiceList(): Observable<any> {
    return this.http.get(this.api.widgetCommon, { withCredentials: true });
  }

  getWidgetListByService(serviceId: string): Observable<any> {
    return this.http.get(this.api.widgetCommon + '/' + serviceId, { withCredentials: true });
  }

  getUserParameterById(paramId: string): Observable<any> {
    return this.http.get(this.api.widgetCommon + '/services/' +  paramId, { withCredentials: true });
  }

  deleteUserParameterById(paramId: string): Observable<any> {
    return this.http.delete(this.api.widgetCommon + '/services/' +  paramId, { withCredentials: true });
  }

  getServiceJSON(serviceId: string): Observable<any> {
    return this.http.get(this.api.microserviceProxy + "/" + serviceId, { withCredentials: true });
  }
 
}
