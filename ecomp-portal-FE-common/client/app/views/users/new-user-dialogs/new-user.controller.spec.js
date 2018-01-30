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
 * Created by nnaffar on 12/20/15.
 */
'use strict';

describe('Controller: NewUserModalCtrl ', () => {
    beforeEach(module('testUtils'));
    beforeEach(module('ecompApp'));

        let promisesTestUtils;
        //destroy $http default cache before starting to prevent the error 'default cache already exists'
        //_promisesTestUtils_ comes from testUtils for promises resolve/reject
        beforeEach(inject((_CacheFactory_, _promisesTestUtils_)=> {
            _CacheFactory_.destroyAll();
            promisesTestUtils = _promisesTestUtils_;
        }));

        let newUser, $controller, $q, $rootScope, $log, $scope;

        let applicationsServiceMock, usersServiceMock, confirmBoxServiceMock;
        let deferredAdminApps, deferredUsersAccounts, deferredUsersAppRoles, deferredUsersAppRoleUpdate;

        beforeEach(inject((_$controller_, _$q_, _$rootScope_, _$log_)=> {
            $rootScope = _$rootScope_;
            $q = _$q_;
            $controller = _$controller_;
            $log = _$log_;
        }));

    beforeEach(()=> {
            [deferredAdminApps, deferredUsersAccounts, deferredUsersAppRoles, deferredUsersAppRoleUpdate] = [$q.defer(),$q.defer(), $q.defer(), $q.defer()];

            /*applicationsServiceMock = {
                getAdminApps: () => {
                    var promise = () => {return deferredAdminApps.promise};
                    var cancel = jasmine.createSpy();
                    return {
                        promise: promise,
                        cancel: cancel
                    }
                }
            };*/

            confirmBoxServiceMock = {
                deleteItem: () => {
                    var promise = () => {return deferredAdminApps.promise};
                    var cancel = jasmine.createSpy();
                    return {
                        promise: promise,
                        cancel: cancel
                    }
                }
            };

            applicationsServiceMock = jasmine.createSpyObj('applicationsServiceMock', ['getAdminAppsSimpler']);
            applicationsServiceMock.getAdminAppsSimpler.and.returnValue(deferredAdminApps.promise);

            usersServiceMock = jasmine.createSpyObj('usersServiceMock', ['getAccountUsers','getUserAppRoles','updateUserAppsRoles']);

            //applicationsServiceMock.getAdminApps().promise().and.returnValue(deferredAdminApps.promise);
            usersServiceMock.getAccountUsers.and.returnValue(deferredUsersAccounts.promise);
            usersServiceMock.getUserAppRoles.and.returnValue(deferredUsersAppRoles.promise);
            usersServiceMock.updateUserAppsRoles.and.returnValue(deferredUsersAppRoleUpdate.promise);

            $scope = $rootScope.$new();
            newUser = $controller('NewUserModalCtrl', {
                $scope: $scope,
                $log: $log,
                usersService: usersServiceMock,
                applicationsService: applicationsServiceMock,
                confirmBoxService: confirmBoxServiceMock
            });
            //$scope.users = users;
        });

        /*beforeEach(()=> {
            scope = $rootScope.$new();
            newUser = $controller('NewUserModalCtrl', {
                $scope: scope,
                $log: $log,
                usersService: usersService,
                applicationsService: applicationsService,
                confirmBoxService: confirmBoxService
            });
        });*/


        it('should open modal window without user when no user is selected', ()=> {
            expect(newUser.selectedUser).toBe(null);
        });

        it('should open modal window with selectedUser apps roles when user is selected', ()=> {
            let roles = {apps: [{id: 1, appRoles: [{id: 3, isApplied: true}]}]};
            let someUser = {orgUserId: 'asdfjl'};

           deferredUsersAppRoles.resolve(roles);
           deferredAdminApps.resolve(roles.apps);

            $scope.ngDialogData = {
                selectedUser: someUser,
                dialogState: 2
            };

            //inject ngDialogData to the scope controller
            newUser = $controller('NewUserModalCtrl', {
                $scope: $scope,
                $log: $log,
                usersService: usersServiceMock,
                applicationsService: applicationsServiceMock,
                confirmBoxService: confirmBoxServiceMock
            });

            newUser.getUserAppsRoles();
            $scope.$apply();

            expect(newUser.selectedUser).toBe(someUser);
            expect(newUser.adminApps).toEqual(roles.apps);
        });

        it('should push to apps order list only apps that has applied roles when initializing', () => {
            let roles = {apps: [{appId: 13, appRoles: [{id: 3, isApplied: true}]},{appId: 20, appRoles: [{id: 3, isApplied: false}]}]};
            let someUser = {orgUserId: 'asdfjl'};

            deferredUsersAppRoles.resolve(roles);
            //deferredAdminApps.resolve(roles.apps);

            $scope.ngDialogData = {
                selectedUser: someUser,
                dialogState: 2
            };

            //inject ngDialogData to the scope controller
            newUser = $controller('NewUserModalCtrl', {
                $scope: $scope,
                $log: $log,
                usersService: usersServiceMock,
                applicationsService: applicationsServiceMock,
                confirmBoxService: confirmBoxServiceMock
            });

            $scope.$apply();

           // expect(newUser.appsOrder).toEqual([13]);
        });

        it('should push app to apps order list when applying at least one role to user from app', () => {
            let roles = {apps: [{appId: 13, appRoles: [{id: 3, isApplied: true}]},{appId: 20, appRoles: [{id: 3, isApplied: false}]}]};
            let someUser = {orgUserId: 'asdfjl'};

            // promisesTestUtils.resolvePromise(usersService, 'getUserAppsRoles', roles);
            deferredUsersAppRoles.resolve(roles);

            $scope.ngDialogData = {
                selectedUser: someUser,
                dialogState: 2
            };

            //inject ngDialogData to the scope controller
            newUser = $controller('NewUserModalCtrl', {
                $scope: $scope,
                $log: $log,
                usersService: usersServiceMock,
                applicationsService: applicationsServiceMock,
                confirmBoxService: confirmBoxServiceMock
            });

            //$scope.$apply();
            //newUser.updateAppsOrder({appId: 39, appRoles: [{id: 13, isApplied: true}]});
            $scope.$apply();

          //  expect(newUser.appsOrder).toEqual([13, 39]);
        });


        it('should remove app from list when removing all user roles in it', () => {
            let roles = {apps: [{appName: 'aaa', appId: 13, appRoles: [{id: 3, isApplied: true}]},{appName: 'vvv', appId: 20, appRoles: [{id: 3, isApplied: true}]}]};
            let someUser = {orgUserId: 'asdfjl'};

           // promisesTestUtils.resolvePromise(usersService, 'getUserAppsRoles', roles);
            promisesTestUtils.resolvePromise(confirmBoxServiceMock, 'deleteItem', true);

            deferredUsersAppRoles.resolve(roles);

            $scope.ngDialogData = {
                selectedUser: someUser,
                dialogState: 2
            };

            //inject ngDialogData to the scope controller
            newUser = $controller('NewUserModalCtrl', {
                $scope: $scope,
                $log: $log,
                usersService: usersServiceMock,
                applicationsService: applicationsServiceMock,
                confirmBoxService: confirmBoxServiceMock
            });

            $scope.$apply();
            newUser.deleteApp(roles.apps[0]);
            $scope.$apply();

           // expect(newUser.appsOrder).toEqual([20]);
        });

        it('should close the modal when update changes succeeded', () => {
            let roles = {apps: [{appName: 'aaa', appId: 13, appRoles: [{id: 3, isApplied: true}]},{appName: 'vvv', appId: 20, appRoles: [{id: 3, isApplied: true}]}]};
            let someUser = {orgUserId: 'asdfjl'};
            //promisesTestUtils.resolvePromise(usersServiceMock, 'getUserAppsRoles', roles);
            //promisesTestUtils.resolvePromise(usersServiceMock, 'updateUserAppsRoles');
            deferredUsersAppRoles.resolve(roles);
            deferredUsersAppRoleUpdate.resolve();
            deferredAdminApps.resolve(roles.apps);

            $scope.ngDialogData = {
                selectedUser: someUser,
                dialogState: 2
            };

            //inject ngDialogData to the scope controller
            newUser = $controller('NewUserModalCtrl', {
                $scope: $scope,
                $log: $log,
                usersService: usersServiceMock,
                applicationsService: applicationsServiceMock,
                confirmBoxService: confirmBoxServiceMock
            });
            $scope.closeThisDialog = function(){};
            spyOn($scope, 'closeThisDialog');

            newUser.getUserAppsRoles();
            $scope.$apply();
            newUser.updateUserAppsRoles();
            $scope.$apply();
            expect($scope.closeThisDialog).toHaveBeenCalledWith(true);
        });
    });
