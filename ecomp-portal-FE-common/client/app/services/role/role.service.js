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
app.factory('RoleService', function ($http, $q, conf,uuid4) {
	var manageRoleDetails = {};
	return {
		getRoles(appId) {
			return $http.get(conf.api.getRoles.replace(':appId', appId),{
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':uuid4.generate()
                }
            })
			.then(function(response) {
				if (typeof response.data === 'object') {
					return response.data;
				} else {
					return $q.reject(response.data);
				}

			}, function(response) {
				// something went wrong
				return $q.reject(response.data);
			});
		},
		
		saveRoleFunction(appId) {
			return $http.post(conf.api.saveRoleFuncion.replace(':appId', appId))
			.then(function(response) {
				if (typeof response.data === 'object') {
					return response.data;
				} else {
					return $q.reject(response.data);
				}

			}, function(response) {
				// something went wrong
				return $q.reject(response.data);
			});
		},
		
		getRoleFunctionList(appId) {
			return $http.get(conf.api.getRoleFunctions.replace(':appId', appId),{
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':uuid4.generate()
                }
            })
			.then(function(response) {
				if (typeof response.data === 'object') {
					return response.data;
				} else {
					return $q.reject(response.data);
				}

			}, function(response) {
				// something went wrong
				return $q.reject(response.data);
			});
		},
		
		getFnMenuItems: function(){
			
			return $http.get('admin_fn_menu')
			.then(function(response) {
				if (typeof response.data === 'object') {
					
					return response.data;
				} else {
					return $q.reject(response.data);
				}

			}, function(response) {
				// something went wrong
				return $q.reject(response.data);
			});			
		},

		getCacheRegions: function() {
			return $http.get('get_regions')
			.then(function(response) {
				if (typeof response.data === 'object') {
					return response.data;
				} else {
					return $q.reject(response.data);
				}
	
			}, function(response) {
				// something went wrong
				return $q.reject(response.data);
			});
		},
		
		getUsageList: function() {
			return $http.get('get_usage_list')
			.then(function(response) {
				if (typeof response.data === 'object') {
					return response.data;
				} else {
					return $q.reject(response.data);
				}
	
			}, function(response) {
				// something went wrong
				return $q.reject(response.data);
			});
		},
		
		getBroadcastList: function() {
			return $http.get('get_broadcast_list')
			.then(function(response) {
				if (typeof response.data === 'object') {
					return response.data;
				} else {
					return $q.reject(response.data);
				}
	
			}, function(response) {
				// something went wrong
				return $q.reject(response.data);
			});
		},
		
		getBroadcast: function(messageLocationId, messageLocation, messageId) {
			return $http.get('get_broadcast?message_location_id='+messageLocationId + '&message_location=' + messageLocation + ((messageId != null) ? '&message_id=' + messageId : ''))
			.then(function(response) {
				if (typeof response.data === 'object') {
					return response.data;
				} else {
					return $q.reject(response.data);
				}
	
			}, function(response) {
				// something went wrong
				return $q.reject(response.data);
			});
		},
		
		getCollaborateList: function() {
			return $http.get('get_collaborate_list')
			.then(function(response) {
				if (typeof response.data === 'object') {
					return response.data;
				} else {
					return $q.reject(response.data);
				}
	
			}, function(response) {
				// something went wrong
				return $q.reject(response.data);
			});
		},
		
		getRole: function(appId, roleId) {
			
			return $http.get(conf.api.getRole + '/' + appId + '/' + roleId,{    
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':uuid4.generate()
                }
            })
			.then(function(response) {
				if (typeof response.data === 'object') {
					return response.data;
				} else {
					return $q.reject(response.data);
				}
	
			}, function(response) {
				// something went wrong
				return $q.reject(response.data);
			});
		},
		
		saveRole(appId) {
			return $http.post(conf.api.saveRole.replace(':appId', appId))
			.then(function(response) {
				if (typeof response.data === 'object') {
					return response.data;
				} else {
					return $q.reject(response.data);
				}

			}, function(response) {
				// something went wrong
				return $q.reject(response.data);
			});
		},
		
		getCentralizedApps: function(userId) {
			
			return $http.get(conf.api.centralizedApps + '?userId=' + userId,{
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':uuid4.generate()
                }
            })
			.then(function(response) {
				if (typeof response.data === 'object') {
					return response.data;
				} else {
					return $q.reject(response.data);
				}
	
			}, function(response) {
				// something went wrong
				return $q.reject(response.data);
			});
		},
		
		setManageRoleDetails:function(apps, id){
			manageRoleDetails = {
					apps: apps,
					id: id
			}
		},
		
		getManageRoleDetails: function(){
			return manageRoleDetails;
		}
	};
});
