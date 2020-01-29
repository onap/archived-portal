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
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  api = environment.api;

  constructor(private http: HttpClient) { }

  getRoles(appId: string) {
    return this.http.get(this.api.getRoles.replace(':appId', appId));
  }

  saveRoleFunction(appId: string) {
    return this.http.post(this.api.saveRoleFunction.replace(':appId', appId), {});
  }

  getRoleFunctionList(appId) {
    return this.http.get(this.api.getRoleFunctions.replace(':appId', appId));
  }

  getFnMenuItems() {
    return this.http.get('admin_fn_menu');
  }

  getCacheRegions() {
    return this.http.get('get_regions');
  }

  getUsageList() {
    return this.http.get('get_usage_list');
  }

  getBroadcastList() {
    return this.http.get('get_broadcast_list');
  }

  getBroadcast(messageLocationId, messageLocation, messageId) {
    return this.http.get('get_broadcast?message_location_id=' + messageLocationId + '&message_location=' + messageLocation + ((messageId != null) ? '&message_id=' + messageId : ''));
  }

  getCollaborateList() {
    return this.http.get('get_collaborate_list');
  }

  getRole(appId, roleId) {
    return this.http.get(this.api.getRole + '/' + appId + '/' + roleId);
  }

  saveRole(appId) {
    return this.http.post(this.api.saveRole.replace(':appId', appId), {});
  }

  getCentralizedApps(userId) {
    return this.http.get(this.api.centralizedApps + '?userId=' + userId);
  }

}
