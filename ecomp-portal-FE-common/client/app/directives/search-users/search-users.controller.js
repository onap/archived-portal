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
    class SearchUsersCtrl {
        constructor($log, usersService, $scope) {
            $scope.UserSearchsIsNull=false;
            this.scrollApi = {};//scrollTop directive
            $scope.txtResults = 'result';
            /**
             * Handle all active HTTP requests
             * activeRequests @type {Array[requests with cancel option]}
             */
            let activeRequests = [];
            let clearReq = (req) => {
                activeRequests.splice(activeRequests.indexOf(req), 1);
            };

            /**
             * this function retrieves users info 
             */
            this.searchUsers = () => {
                this.isLoading = true;
                if(this.searchUsersInProgress){
                    return;
                }
                this.selectedUser = null;
                this.searchUsersInProgress = true;
                this.searchUsersResults = null;

                let searchUsersReq = usersService.searchUsers(this.searchUserString);
                activeRequests.push(searchUsersReq);
                searchUsersReq.promise().then(usersList => {
                    $log.debug('searchUsers found the following users: ', JSON.stringify(usersList));
                    this.searchUsersResults = usersList;
                    $log.debug('searchUsersResults length: ', usersList.length);
                    if (usersList.length != 1) {
                        $scope.txtResults = 'results'
                    } else {
                        $scope.txtResults = 'result'
                    }
                    $scope.UserSearchsIsNull=false;
                }).catch(err => {
                    $log.error('SearchUsersCtrl.searchUsers: ' + err);
                    $scope.UserSearchsIsNull=true;
                }).finally(() => {
                    this.scrollApi.scrollTop();
                    this.searchUsersInProgress = false;
                    clearReq(searchUsersReq);
                    this.isLoading = false;
                });
            };

            let init = () => {
                this.isLoading = false;
                this.searchUsersInProgress = false;
            };

            this.setSelectedUser = user => {
                this.selectedUser = user;
            };

            init();

            $scope.$on('$destroy', () => {
                //cancel all active requests when closing the modal
                activeRequests.forEach(req => {
                    req.cancel();
                });
            });
        }
    }
    SearchUsersCtrl.$inject = ['$log', 'usersService', '$scope'];
    angular.module('ecompApp').controller('SearchUsersCtrl', SearchUsersCtrl);
})();
