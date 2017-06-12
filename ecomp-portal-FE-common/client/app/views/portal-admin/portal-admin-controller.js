/*-
 * ================================================================================
 * ECOMP Portal
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
    class PortalAdminsCtrl {
        constructor($log, portalAdminsService, ngDialog, confirmBoxService) {

            let updateTableData = () => {
                this.isLoadingTable = true;
                portalAdminsService.getPortalAdmins().then(result=> {
                    $log.debug('PortalAdminsCtrl::updateTableData: result: ' + JSON.stringify(result));
                    if (!result || !result.length) {
                        $log.info('PortalAdminsCtrl::updateTableData: no Portal Admins err handling');
                        this.portalAdminsTableData = [];
                        return;
                    }
                    this.portalAdminsTableData = result;
                }).catch(err=> {
                    $log.error('PortalAdminsCtrl::updateTableData error :',err);
                    confirmBoxService.showInformation('There was a problem retrieving the portal admins. ' +
                        'Please try again later. Error: ' + err.status).then(isConfirmed => {});

                }).finally(() => {
                    this.isLoadingTable = false;
                });
            };

            let init = () => {
                $log.info('portalAdminsService.getPortalAdmins::initializing...');
                this.isLoadingTable = false;

                /*Table general configuration params*/
                this.searchString= '';
                /*Table data*/
                this.portalAdminsTableHeaders = ['First Name', 'Last Name', 'User ID', 'Delete'];
                this.portalAdminsTableData = [];
                updateTableData();
            };

            init();

            this.removePortalAdmin = pAdmin => {
                $log.debug('PortalAdminsCtrl::removePortalAdmin: pAdmin = ' + JSON.stringify(pAdmin));
                confirmBoxService.deleteItem(pAdmin.firstName + ' ' + pAdmin.lastName )
                    .then(isConfirmed => {
                    if(isConfirmed){
                        if(!pAdmin || !pAdmin.userId){
                            $log.error('PortalAdminsCtrl::removePortalAdmin No portal admin or ID... cannot delete');
                            return;
                        }
                        portalAdminsService.removePortalAdmin(pAdmin.userId,pAdmin.loginId).then(() => {
                            $log.info("PortalAdminsCtrl::removePortalAdmin removed admin");
                            init();
                        }).catch(err => {
                            $log.error('PortalAdminsCtrl::removePortalAdmin.deleteItem error: '+ err);
                            confirmBoxService.showInformation('There was a problem deleting this portal admins. ' +
                                'Please try again later. Error: ' + err.status).then(isConfirmed => {});
                        });
                    }
                }).catch(err => {
                    $log.error('PortalAdminsCtrl::removePortalAdmin.deleteItem error: '+ err);
                });
            };
            
            this.openAddNewPortalAdminModal = (user) => {
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
                ngDialog.open({
                    templateUrl: 'app/views/portal-admin/new-portal-admin/new-portal-admin.modal.html',
                    controller: 'NewPortalAdminModalCtrl',
                    controllerAs: 'newPortalAdmin',
                    data: data
                }).closePromise.then(needUpdate => {
                    if(needUpdate.value === true){
                        $log.debug('PortalAdminsCtrl::openAddNewPortalAdminModal: updating Portal Admin table data...');
                        updateTableData();
                    }
                });
            };
        }
    }
    PortalAdminsCtrl.$inject = ['$log', 'portalAdminsService', 'ngDialog', 'confirmBoxService'];
    angular.module('ecompApp').controller('PortalAdminsCtrl', PortalAdminsCtrl);
})();
