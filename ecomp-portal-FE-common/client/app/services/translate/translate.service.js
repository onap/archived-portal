/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2019 CMCC, Inc. and others. All rights reserved.
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
'use strict';

(function () {
  class TranslateService {
    constructor($q, $log, $http, conf, uuid, utilsService) {
      this.$q = $q;
      this.$log = $log;
      this.$http = $http;
      this.conf = conf;
      this.uuid = uuid;
      this.utilsService = utilsService;
    }
    getCurrentLang(loginId) {
      let deferred = this.$q.defer();
      this.$log.info('TranslateService::getCurrentLang');
      let url = this.conf.api.getCurrentLang.replace(':loginId', loginId);
      this.$log.info('TranslateMenuService::getCurrentLang url: '+url);
      this.$http({
        method: 'GET',
        url: url,
        cache: false,
        headers: {
          'X-ECOMP-RequestID':this.uuid.generate()
        }
      }).then(res => {
        // If response comes back as a redirected HTML page which IS NOT a success
        if (this.utilsService.isValidJSON(res)== false) {
          deferred.reject('functionalMenu.service::getManagedRolesMenu Failed');
        } else {
          this.$log.info('functionalMenu.service::getManagedRolesMenu succeeded: ');
          deferred.resolve(res.data);
        }
      }).catch(status => {
        deferred.reject(status);
      });
      return deferred.promise;
    }
    getLangList() {
      let deferred = this.$q.defer();
      this.$http({
        method: 'GET',
        url: this.conf.api.getLanguages,
        // url: 'http://172.20.230.41:9000/ecompportal/auxapi/language',
        cache: false,
        headers: {
          'X-ECOMP-RequestID':this.uuid.generate()
        }
      }).then(res => {
        // If response comes back as a redirected HTML page which IS NOT a success
        if (this.utilsService.isValidJSON(res)== false) {
          deferred.reject('TranslateService::getLangList Failed');
        } else {
          this.$log.info('TranslateService::getLangList succeeded: ');
          deferred.resolve(res.data);
        }
      }).catch(status => {
        deferred.reject(status);
      });
      return deferred.promise;
    }
    saveSelectedLang(loginId, data) {
      let deferred = this.$q.defer();
      this.$log.info('saveSelectedLang::saveSelectedLang: ', data);
      let url = this.conf.api.updateLang.replace(':loginId', loginId);
      this.$http({
        method: 'POST',
        url: url,
        cache: false,
        data: data,
        headers: {
          'X-ECOMP-RequestID':this.uuid.generate()
        }
      }).then(res => {
        // If response comes back as a redirected HTML page which IS NOT a success
        if (this.utilsService.isValidJSON(res)== false) {
          deferred.reject('saveSelectedLang::saveSelectedLang Failed');
        } else {
          this.$log.info('saveSelectedLang::saveSelectedLang succeeded: ');
          deferred.resolve(res.data);
        }
      }).catch(errRes => {
        deferred.reject(errRes);
      });
      return deferred.promise;
    }
  }
  TranslateService.$inject = ['$q', '$log', '$http', 'conf','uuid4', 'utilsService'];
  angular.module('ecompApp').service('translateService', TranslateService)
})();
