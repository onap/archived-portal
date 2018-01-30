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
'use strict';
(function () {
    class AdminsCtrl {
        constructor($log, adminsService, applicationsService, ngDialog, $modal) {

            let allPortalsFilterObject = {index: 0, title: 'All applications', value: ''};

            let updateTableData = () => {
                this.isLoadingTable = true;
                adminsService.getAccountAdmins().then(res=> {
                    if (!res || !res.length) {
                        $log.error('AdminsCtrl::updateTableData: no admins err handling');
                        this.adminsTableData = [];
                        return;
                    }
                    this.adminsTableData = res;
                }).catch(err=> {
                    $log.error('AdminsCtrl::updateTableData error: ', err);
                }).finally(() => {
                    this.isLoadingTable = false;
                });
            };

            let init = () => {
                //$log.info('AdminsCtrl:: ::initializing...');
                this.isLoadingTable = false;
                this.availableApps = [allPortalsFilterObject];
                this.filterByApp = this.availableApps[0];

                /*Table general configuration params*/
                this.searchString= '';
                /*Table data*/
                this.adminsTableHeaders = ['First Name', 'Last Name', 'User ID', 'Applications'];
                this.adminsTableData = [];
                updateTableData();
            };

            applicationsService.getAvailableApps().then(res=> {
                //if(!res || Object.prototype.toString.call(res) !== '[object Array]'){
                //    this.availableApps = [allPortalsFilterObject];
                //    return;
                //}
                //this part overrides index param to fix ABS select bug
                // (index has to be the same as location in array)
                // todo:change BE object to contain only id and name
                this.availableApps = [allPortalsFilterObject];
                var res1 = res.sort(getSortOrder("title"));
                var realAppIndex = 1;
                for(let i=1; i<=res1.length; i++){
                    if (!res1[i-1].restrictedApp) {
                        //$log.debug('AdminsCtrl:getAvailableApps:: pushing: {index: ', realAppIndex, 'title: ', res1[i - 1].title,
                         //   '| value: ', res1[i -1].value, '}');
                        this.availableApps.push({
                            index: realAppIndex,
                            title: res1[i - 1].title,
                            value: res1[i - 1].value
                        });
                        realAppIndex = realAppIndex + 1;
                    } else {
                        // $log.debug('AdminsCtrl:getAvailableApps:: Restricted/URL only App will not be used = ' + res1[i - 1].title);
                    }
                }
            }).catch(err=> {
                $log.error('AdminsCtrl::ctor', err);
                this.availableApps = [allPortalsFilterObject];
            });

            // Refactor this into a directive
            let getSortOrder = (prop) => {
                return function(a, b) {
                    // $log.debug('a = ' + JSON.stringify(a) + "| b = " + JSON.stringify(b));
                    if (a[prop].toLowerCase() > b[prop].toLowerCase()) {
                        return 1;
                    } else if (a[prop].toLowerCase() < b[prop].toLowerCase()) {
                        return -1;
                    }
                    return 0;
                }
            }

            init();

            //Filter function
            this.portalsRowFilter = (input) => {
                if (this.filterByApp.value === '') {
                    return true;
                }
                return _.find(input.apps, {appName: this.filterByApp.value}) !== undefined;
            };

            this.openAddNewAdminModal = (user) => {
                let data = null;
                if(user){
                    data = {
                        dialogState: 2,
                        selectedUser:{
                            orgUserId: user.orgUserId,
                            firstName: user.firstName,
                            lastName: user.lastName
                        }
                    }
                }
                var modalInstance = $modal.open({
                    templateUrl: 'app/views/admins/add-admin-dialogs/new-admin.modal.html',
                    controller: 'NewAdminModalCtrl as newAdmin',
                    sizeClass: 'modal-medium', 
                    resolve: {
    					items: function () {
    						return data;
    			        }
    		        }
                });
                
                modalInstance.result.finally(function () {
                	updateTableData();  
     	        });
            };
        }
    }
    AdminsCtrl.$inject = ['$log', 'adminsService', 'applicationsService', 'ngDialog', '$modal'];
    angular.module('ecompApp').controller('AdminsCtrl', AdminsCtrl);
})();
