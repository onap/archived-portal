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
export class NotificationService {

  api = environment.api;

  constructor(private http: HttpClient) { }

  getNotificationRate(): Observable<any>{ 
    let getNotificationRateURL  = this.api.notificationUpdateRate;
    return this.http.get(getNotificationRateURL);
  }

  getNotification(): Observable<any>{
    let getNotificationURL  = this.api.getNotifications;
    return this.http.get(getNotificationURL);
  }

  getNotificationHistory(): Observable<any>{
    let getNotificationHistoryURL = this.api.getNotificationHistory;
    return this.http.get(getNotificationHistoryURL);
  }

  getAdminNotification(): Observable<any>{
    let getAdminNotificationURL = this.api.getAdminNotifications;
    return this.http.get(getAdminNotificationURL);
  }

  getMessageRecipients(notificationId: any): Observable<any>{
    let getMessageRecipientsURL = this.api.getMessageRecipients+"?notificationId="+notificationId;
    return this.http.get(getMessageRecipientsURL);
  }

  getAppRoleIds(): Observable<any>{
    let getAppRoleIdsURL = this.api.getAllAppRoleIds;
    return this.http.get(getAppRoleIdsURL);
  }

  getNotificationRoles(notificationId: any): Observable<any>{
    let getNotificationRolesURL = this.api.getNotificationRoles + '/'+notificationId+'/roles';
    return this.http.get(getNotificationRolesURL);
  }

  addAdminNotification(newAdminNotification: any): Observable<any>{
    let addAdminNotificationURL = this.api.saveNotification;
    return this.http.post(addAdminNotificationURL, newAdminNotification);
  }

  updateAdminNotification(adminNotification: any): Observable<any>{
    let updateAdminNotificationURL = this.api.saveNotification;
    return this.http.post(updateAdminNotificationURL, adminNotification);
  }

  setNotificationRead(notificationId: any): Observable<any>{
    let  setNotificationReadURL = this.api.notificationRead+"?notificationId="+notificationId;
    return this.http.get(setNotificationReadURL);
  }

}
