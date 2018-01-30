/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
/**
 * Created by robertlo on 11/18/2016.
 */
'use strict';

(function () {
    class AuditLogService {
        constructor($q, $log, $http, conf, uuid) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
          
            this.uuid = uuid;
        }
        storeAudit(affectedAppId) {
            // this.$log.error('ecomp::storeAudit storeAudit',affectedAppId);
        	let deferred = this.$q.defer();
        	this.$http({
                    method: "GET",
                    url: this.conf.api.storeAuditLog+'?affectedAppId=' + affectedAppId +"&type=''&comment=''",
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
            })
        	return deferred.promise;
        }
        
        storeAudit(affectedAppId,type) {
            // this.$log.error('ecomp::storeAudit storeAudit',affectedAppId + " " +type);
        	let deferred = this.$q.defer();
        	this.$http({
                    method: "GET",
                    url: this.conf.api.storeAuditLog+'?affectedAppId=' + affectedAppId + '&type='+type+"&comment=''",
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
            })
        	return deferred.promise;
        }
        storeAudit(affectedAppId,type,comment) {
        	comment = filterDummyValue(comment);
        	let deferred = this.$q.defer();
        	var url =this.conf.api.storeAuditLog+'?affectedAppId=' + affectedAppId;
        	if(type!=''){
        		url= url+'&type='+type;
        	}
        	if(comment!=''){
        		url= url+'&comment='+comment;
        	}
        	this.$http({
                    method: "GET",
                    url: url,
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
            })
        	return deferred.promise;
        }
    }  
    AuditLogService.$inject = ['$q', '$log', '$http', 'conf', 'uuid4'];
    angular.module('ecompApp').service('auditLogService', AuditLogService)
})();

function filterDummyValue(comment){
	var n = comment.indexOf("?dummyVar");
	if(n!=-1)
		comment = comment.substring(0, n);
	return comment;
}
