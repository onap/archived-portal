/*-
 * ================================================================================
 * eCOMP Portal
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ================================================================================
 */

'use strict';

(function () {
  class MenusService {
    constructor($q, $log, $http, conf, uuid) {
      this.$q = $q;
      this.$log = $log;
      this.$http = $http;
      this.conf = conf;
      this.uuid = uuid;
    }

    GetFunctionalMenuForUser() {
      let deferred = this.$q.defer();
      // this.$log.info('MenusService::GetFunctionalMenuForUser via REST API');
      this.$http({
        method: "GET",
        url: this.conf.api.functionalMenuForAuthUser,
        cache: false,
        headers: {
          'X-ECOMP-RequestID':this.uuid.generate()
        }
      }).then( res => {
          if (Object.keys(res.data).length == 0) {
              deferred.reject("MenusService::GetFunctionalMenuForUser Failed");
          } else {
              this.$log.info('MenusService::GetFunctionalMenuForUser success:');
              deferred.resolve(res.data);
          }
      })
      .catch( status => {
        this.$log.info('MenusService::rejection:' + status);
        deferred.reject(status);
      });
             

      return deferred.promise;
    }
      
    getFavoriteItems() {
      let deferred = this.$q.defer();
      // this.$log.info('MenusService::getFavoriteItems via REST API');
        this.$http({
            method: "GET",
            url: this.conf.api.getFavoriteItems +"?date="+new Date().getTime(),
            cache: false,
            headers: {
                'X-ECOMP-RequestID':this.uuid.generate()
            }
        }).then( res => {
            if (Object.keys(res.data).length == 0) {
                deferred.reject("MenusService::getFavoriteItems Failed");
            } else {
                this.$log.info('MenusService::getFavoriteItems success:');
                deferred.resolve(res.data);
            }
        })
        .catch( status => {
            this.$log.error('MenusService::getFavoriteItems rejection:' + status);
            deferred.reject(status);
        });
               

        return deferred.promise;
    }
    
    setFavoriteItem(menuId) {
      let deferred = this.$q.defer();
      // this.$log.info('menus-service.service::setFavoriteItem  via REST API' + menuId);
      let url = this.conf.api.setFavoriteItem;
      this.$http({
          method: "POST",
          url: url,
          cache: false,
          headers: {
              'X-ECOMP-RequestID':this.uuid.generate(),
              'Content-Type': 'application/json'
          },
          data: menuId
      }).then(res => {
          if (Object.keys(res.data).length == 0) {
              deferred.reject("MenusService::setFavoriteItem Failed");
          } else {
              this.$log.info('MenusService::setFavoriteItem success:');
              deferred.resolve(res.data);
          }
      })
      .catch(errRes => {
          this.$log.error('MenusService::setFavoriteItem rejection:' + JSON.stringify(errRes));
          deferred.reject(errRes);
      });
              
      return deferred.promise;
    }
    
      removeFavoriteItem(menuId) {
          let deferred = this.$q.defer();
          // this.$log.info('menus-service.service::removeFavoriteItem  via REST API');
          let url = this.conf.api.removeFavoriteItem.replace(':menuId', menuId);
          this.$http({
              method: "DELETE",
              url: url,
              cache: false,
              headers: {
                  'X-ECOMP-RequestID':this.uuid.generate()
              }
          }).then(res => {
              if (Object.keys(res.data).length == 0) {
                  deferred.reject("MenusService::removeFavoriteItem Failed");
              } else {
                  // this.$log.info('MenusService::removeFavoriteItem success:');
                  deferred.resolve(res.data);
              }
          })
          .catch(errRes => {
              this.$log.error('MenusService::removeFavoriteItem rejection:' + status);
              deferred.reject(errRes);
          });
          return deferred.promise;
      }
    
  }
  MenusService.$inject = ['$q', '$log', '$http', 'conf', 'uuid4'];
  angular.module('ecompApp').service('menusService', MenusService)
})();
