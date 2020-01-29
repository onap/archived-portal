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
import { environment } from '../../../../environments/environment'
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, from } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UsersService {
  api = environment.api;

  constructor(private http: HttpClient) { }
  
  searchUsers(queryString: string) {
    let httpParams = new HttpParams()
      .set('search', queryString);
    return this.http.get(this.api.queryUsers, { params: httpParams, responseType: 'json' });
  }

  getAccountUsers(appId: any) {
    return this.http.get(this.api.accountUsers.replace(':appId', appId));
  }

  getUserAppRoles(appid: any, orgUserId: string, extRequestValue: any, isSystemUser: any) {
    return this.http.get(this.api.userAppRoles, { params: { 'user': orgUserId, 'app': appid, 'externalRequest': extRequestValue, 'isSystemUser': isSystemUser } });
  }

  updateUserAppRoles(newUserAppRoles) {
    return this.http.put(this.api.userAppRoles, newUserAppRoles);
  }

  //Not used in dev.json
  getLoggedInUser() { }

  //Not used in dev.jon 
  modifyLoggedInUser() { }

  getUserProfile(): Observable<any> {
    return this.http.get(environment.api.userProfile);
  }
  
  addNewUser(newUserData){
    console.log("User service : ", newUserData);
    return this.http.post(this.api.saveNewUser, newUserData).subscribe((response)=>{
    });
  }

}
