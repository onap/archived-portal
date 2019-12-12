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
import * as uuid from 'uuid';

@Injectable({
  providedIn: 'root'
})
export class ContactUsService {

  api = environment.api;
  resp:string;
  headerParams = {'X-Widgets-Type': 'all', 'X-ECOMP-RequestID': uuid.v4() };

  constructor(private http: HttpClient) { }

  getListOfApp(): Observable<any>{ 
    let getListOfAppURL  = this.api.availableApps;
    return this.http.get(getListOfAppURL , { withCredentials: true } );
  }

  getContactUs(): Observable<any>{ 
    let getContactUsURL  = this.api.getContactUS;
    return this.http.get(getContactUsURL , { withCredentials: true } );
  }

  getAppsAndContacts(): Observable<any>{ 
    let getAppsAndContactsURL  = this.api.getAppsAndContacts;
    return this.http.get(getAppsAndContactsURL , { withCredentials: true } );
  }

  getContactUSPortalDetails(): Observable<any>{ 
    let getContactUSPortalDetailsURL  = this.api.getContactUSPortalDetails;
    return this.http.get(getContactUSPortalDetailsURL , { withCredentials: true } );
  }

  getAppCategoryFunctions(): Observable<any>{ 
    let getAppCategoryFunctionsURL  = this.api.getAppCategoryFunctions;
    return this.http.get(getAppCategoryFunctionsURL , { withCredentials: true } );
  }

  addContactUs(newContactUs: any): Observable<any>{
    let addContactUsURL  = this.api.saveContactUS;
    return this.http.post(addContactUsURL, newContactUs, { withCredentials: true });
  }

  modifyContactUs(contactUsObj: any): Observable<any>{
    let modifyContactUsURL  = this.api.saveContactUS;
    return this.http.post(modifyContactUsURL, contactUsObj, { withCredentials: true });
  }

  removeContactUs(id: any): Observable<any>{
    let removeContactUsURL  = this.api.deleteContactUS + '/' + id;
    return this.http.post(removeContactUsURL, { withCredentials: true });
  }

}
