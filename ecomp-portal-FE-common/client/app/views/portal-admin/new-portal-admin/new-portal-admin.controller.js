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
/**
 * Created by nnaffar on 12/8/15.
 */
'use strict';
(function () {
    class NewPortalAdminModalCtrl {
        constructor($log, portalAdminsService, $scope, confirmBoxService) {

            let init = () => {
                this.isSaving = false;
                /* istanbul ignore if */
                if($scope.ngDialogData && $scope.ngDialogData.selectedUser && $scope.ngDialogData.dialogState){
                    this.selectedUser = $scope.ngDialogData.selectedUser;
                    this.dialogState = $scope.ngDialogData.dialogState;
                }else{
                    this.selectedUser = null;
                    this.dialogState = 1;
                }
                //this.searchUsersInProgress = false;
                //this.showNewAdminAppDropdown = false;
                $log.info('NewPortalAdminModalCtrl:: initiated');
            };

            this.addNewPortalAdmin = () => {
                confirmBoxService.makeAdminChanges('Are you sure you want to add "' + this.selectedUser.firstName + ' ' + this.selectedUser.lastName + '" as a Portal Admin?')
                    .then(isConfirmed => {
                        if(isConfirmed) {
                            if (!this.selectedUser || !this.selectedUser.orgUserId) {
                                $log.error('NewPortalAdminModalCtrl::makeAdminChanges: No portal admin or ID... cannot add');
                                return;
                            }
                            portalAdminsService.addPortalAdmin(this.selectedUser.orgUserId)
                                .then(() => {
                                    $log.debug("NewPortalAdminModalCtrl::addNewPortalAdmin: portal admin added successfully");
                                    $scope.closeThisDialog(true);
                                }).catch(err => {
                                    if(err.status === 409) {    //Conflict
                                        confirmBoxService.showInformation('This user already exists as a portal admin!').then(function (isConfirmed) {
                                            $scope.closeThisDialog(true);
                                        });
                                    } else {
                                        confirmBoxService.showInformation('There was a unknown problem adding the portal admin. ' + 'Please try again later. Error Status: '+ err.status).then(function (isConfirmed)  {
                                            $scope.closeThisDialog(true);
                                        });
                                    }
                            });
                        }
                    }).catch(err => {
                        confirmBoxService.showInformation('There was a unknown problem adding the portal admin. ' + 'Please try again later. Error Status: '+ err.status).then(function (isConfirmed)  {
                            $scope.closeThisDialog(true);
                        });
                        $log.error('portalAdminsService.addPortalAdmin error status: '+ err.status);
                });
            };

            /**
             * this function set the selected user
             * @param user: selected user object
             */
            this.setSelectedUser = (user) => {
                $log.debug('NewPortalAdminModalCtrl::setSelectedUser: selected user: ', user);
                this.selectedUser = user;
            };

            init();

            $scope.$on('$stateChangeStart', e => {
                //Disable navigation when modal is opened
                //**Nabil - note: this will cause the history back state to be replaced with current state
                e.preventDefault();
            });
        }
    }
    NewPortalAdminModalCtrl.$inject = ['$log', 'portalAdminsService', '$scope', 'confirmBoxService'];
    angular.module('ecompApp').controller('NewPortalAdminModalCtrl', NewPortalAdminModalCtrl);
})();
