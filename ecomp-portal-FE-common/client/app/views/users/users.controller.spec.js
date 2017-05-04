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
// /**
//  * Created by nnaffar on 12/17/15.
//  */
// 'use strict';
//
// describe('Controller: UsersCtrl ', () => {
//     beforeEach(module('ecompApp'));
//
//     //destroy $http default cache before starting to prevent the error 'default cache already exists'
//     beforeEach(inject((_CacheFactory_)=> {
//         _CacheFactory_.destroyAll();
//     }));
//
//     let users, $controller, $q, $rootScope, $log, $scope;
//
//     beforeEach(inject((_$controller_, _$q_, _$rootScope_, _$log_)=> {
//         [$controller, $q, $rootScope, $log] = [_$controller_, _$q_, _$rootScope_, _$log_];
//     }));
//
//     let applicationsServiceMock, usersServiceMock;
//     let deferredAdminApps, deferredUsersAccounts;
//     beforeEach(()=> {
//         [deferredAdminApps, deferredUsersAccounts] = [$q.defer(), $q.defer()];
//
//         applicationsServiceMock = {
//             getAdminApps: () => {
//                 var promise = () => {return deferredAdminApps.promise};
//                 var cancel = jasmine.createSpy();
//                 return {
//                     promise: promise,
//                     cancel: cancel
//                 }
//             }
//         };
//
//         usersServiceMock = jasmine.createSpyObj('usersServiceMock', ['getAccountUsers']);
//
//         //applicationsServiceMock.getAdminApps().promise().and.returnValue(deferredAdminApps.promise);
//         usersServiceMock.getAccountUsers.and.returnValue(deferredUsersAccounts.promise);
//
//         $scope = $rootScope.$new();
//         users = $controller('UsersCtrl', {
//             $log: $log,
//             applicationsService: applicationsServiceMock,
//             usersService: usersServiceMock,
//             $scope: $scope
//         });
//         $scope.users = users;
//     });
//
//     //MOCKS
//     let appsListMock = [
//         {value: 'SSP', title: 'SSP', id: 3},
//         {value: 'ASDC', title: 'ASDC', id: 23},
//         {value: 'Formation', title: 'Formation', id: 223}
//     ];
//
//     let usersListMock = [
//         {
//             "orgUserId": "nn605g",
//             "firstName": "Nabil",
//             "lastName": "Naffar",
//             "roles": [
//                 {
//                     "roleId": 1,
//                     "roleName": "Standard user"
//                 },
//                 {
//                     "roleId": 9,
//                     "roleName": "Super standard user"
//                 },
//                 {
//                     "roleId": 2,
//                     "roleName": "Super duper standard user"
//                 }
//             ]
//         }];
//     let secondUsersListMock = [
//         {
//             "orgUserId": "sadf7",
//             "firstName": "John",
//             "lastName": "Hall",
//             "roles": [
//                 {
//                     "roleId": 1,
//                     "roleName": "Standard user"
//                 },
//                 {
//                     "roleId": 2,
//                     "roleName": "Super duper standard user"
//                 }
//             ]
//         }];
//
//     it('should get all user\'s administrated applications when initializing the view', ()=> {
//         deferredAdminApps.resolve(appsListMock);
//         deferredUsersAccounts.resolve(usersListMock);
//         $scope.$apply();
//         expect(users.adminApps).toEqual(appsListMock);
//         expect(users.selectedApp).toEqual(appsListMock[0]);
//     });
//
//     it('should get first application users by default when initializing the view', () => {
//         $scope.$apply();
//         deferredAdminApps.resolve(appsListMock);
//         deferredUsersAccounts.resolve(usersListMock);
//         $scope.$apply();
//         expect(users.accountUsers).toEqual(usersListMock);
//     });
//
//     it('should get application users when changing application', () => {
//         $scope.$apply();
//         deferredAdminApps.resolve(appsListMock);
//         $scope.$apply();
//
//         users.selectedApp = appsListMock[1];
//         deferredUsersAccounts.resolve(secondUsersListMock);
//         $scope.$apply('users');//change app
//
//         expect(users.accountUsers).toEqual(secondUsersListMock);
//     });
// });
