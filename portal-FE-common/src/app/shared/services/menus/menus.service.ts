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
export class MenusService {

  apiUrl = environment.api;
  constructor(private http: HttpClient) { }

  getFunctionalMenuForUser() {
    return this.http.get(this.apiUrl.functionalMenuForAuthUser);
  }

  getPortalTitle() {
    return this.http.get(this.apiUrl.portalTitle);
  }

  getFavoriteItems() {
    return this.http.get(this.apiUrl.getFavoriteItems + '?date=' + new Date().getTime());
  }

  setFavoriteItem(menuId) {
    return this.http.post(this.apiUrl.setFavoriteItem, menuId);

  }

  removeFavoriteItem(menuId) {
    return this.http.delete(this.apiUrl.removeFavoriteItem.replace(':menuId', menuId));
  }

  getAllLanguages() {
    return this.http.get(this.apiUrl.getLanguages);
  }

  setLanguage(langId, loginId) {
    const body = { languageId: langId}
    return this.http.post(this.apiUrl.updateLang.replace(':loginId',loginId ), body);
  }

  getCurrentLang(loginId) {
    return this.http.get(this.apiUrl.getCurrentLang.replace(':loginId',loginId ));
  }

  logout(appStr) {
    // this.$log.info('SessionService::logout from App');
    // this.$log.info('SessionService appStr: ', appStr);

    var eaccessPattern = '\https?\:\/\/[^/]+/[^/]+/[^/]+';
    var standardPattern = '\https?\:\/\/[^/]+/[^/]+';

    if (appStr.includes('e-access')) {
      standardPattern = eaccessPattern;
    }

    var contextUrl = appStr.match(new RegExp(standardPattern));
    var logoutUrl = contextUrl + '/logout.htm';
    // this.$sce.trustAsResourceUrl(logoutUrl);
    console.log('logoutUrl ' + logoutUrl);
    jQuery('#reg-logout-div').append("<iframe style='display:none' src='" + logoutUrl + "' />");

  }

}
