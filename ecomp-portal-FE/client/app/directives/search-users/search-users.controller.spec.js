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

//'use strict';
//
//describe('Controller: NewAdminCtrl ', () => {
//    beforeEach(module('ecompApp'));
//
//    //destroy $http default cache before starting to prevent the error 'default cache already exists'
//    beforeEach(inject((_CacheFactory_)=> {
//        _CacheFactory_.destroyAll();
//    }));
//
//
//    let newCtrl, $controller, $q, $rootScope, $log;
//
//    beforeEach(inject((_$controller_, _$q_, _$rootScope_, _$log_)=> {
//        [$controller, $q, $rootScope, $log] = [_$controller_, _$q_, _$rootScope_, _$log_];
//    }));
//
//    let deferredUsersList, deferredAdminAppsRoles, deferredUpdateRolesRes;
//    let usersServiceMock, adminsServiceMock;
//    beforeEach(()=> {
//        [deferredUsersList, deferredAdminAppsRoles, deferredUpdateRolesRes] = [$q.defer(), $q.defer(), $q.defer()];
//
//        //usersServiceMock = jasmine.createSpyObj('usersServiceMock', ['searchUsers']);
//        usersServiceMock = {
//            searchUsers: () => {
//                var promise = () => {return deferredUsersList.promise};
//                var cancel = jasmine.createSpy();
//                return {
//                    promise: promise,
//                    cancel: cancel
//                }
//            }
//        };
//
//        adminsServiceMock = jasmine.createSpyObj('adminsServiceMock', ['getAdminAppsRoles', 'updateAdminAppsRoles']);
//
//        //usersServiceMock.searchUsers.and.returnValue(deferredUsersList.promise);
//        adminsServiceMock.getAdminAppsRoles.and.returnValue(deferredAdminAppsRoles.promise);
//        adminsServiceMock.updateAdminAppsRoles.and.returnValue(deferredUpdateRolesRes.promise);
//
//        newCtrl = $controller('NewAdminModalCtrl', {
//            $log: $log,
//            usersService: usersServiceMock,
//            adminsService: adminsServiceMock,
//            $scope: $rootScope
//        });
//    });
//
//    it('should init default values when loading the controller', ()=> {
//        //expect(newCtrl.searchUsersInProgress).toBe(false);
//        expect(newCtrl.dialogState).toBe(1);
//        expect(newCtrl.selectedUser).toBe(null);
//    });
//
//    it('should populate retrieved users when search users service returns a list ', ()=> {
//        //spyOn(usersServiceMock, 'searchUsers');
//        let usersListRes = [{user: 1}, {user: 2}];
//        newCtrl.searchUserString = 'some user name';
//        deferredUsersList.resolve(usersListRes);
//        newCtrl.searchUsers();
//        $rootScope.$apply();
//
//        //expect(usersServiceMock.searchUsers).toHaveBeenCalledWith(newCtrl.searchUserString);
//        expect(newCtrl.searchUsersResults).toEqual(usersListRes);
//        expect(newCtrl.searchUsersInProgress).toBe(false);
//    });
//
//    it('should log the error when search users fails', ()=> {
//        spyOn($log, 'error');
//        deferredUsersList.reject('oh snap!');
//        newCtrl.searchUsers();
//        $rootScope.$apply();
//        expect($log.error).toHaveBeenCalled();
//    });
//
//    it('should populate admin apps roles and move to the next screen when adminsService.getAdminAppsRoles succeeded', ()=> {
//        let userApps = {appsRoles: [{id: 1, isAdmin: false}, {id: 2, isAdmin: true}]};
//        deferredAdminAppsRoles.resolve(userApps);
//
//        newCtrl.searchUsersInProgress = false;
//        newCtrl.selectedUser = {userId: 'userId'};
//
//        newCtrl.getAdminAppsRoles();
//        $rootScope.$apply();
//
//        expect(adminsServiceMock.getAdminAppsRoles).toHaveBeenCalledWith(newCtrl.selectedUser.userId);
//        expect(newCtrl.adminAppsRoles).toEqual(userApps.appsRoles);
//        expect(newCtrl.dialogState).toBe(2);
//    });
//
//    it('should  log the error when adminsService.getAdminAppsRoles fails', ()=> {
//        spyOn($log, 'error');
//        deferredAdminAppsRoles.reject('some error');
//
//        newCtrl.searchUsersInProgress = false;
//        newCtrl.selectedUser = {userId: 'userId'};
//
//        newCtrl.getAdminAppsRoles();
//        $rootScope.$apply();
//
//        expect($log.error).toHaveBeenCalled();
//    });
//    it('should  log the error when trying to getAdminAppsRoles without selecting user ', ()=> {
//        spyOn($log, 'error');
//
//        newCtrl.searchUsersInProgress = false;
//        newCtrl.selectedUser = null;
//
//        newCtrl.getAdminAppsRoles();
//        $rootScope.$apply();
//
//        expect($log.error).toHaveBeenCalled();
//    });
//    //it('should setSelectedUser when choosing user', ()=> {
//    //
//    //});
//    //it('should set isAdmin as false when removing app from the administrated apps list', ()=> {
//    //});
//    it('should set isAdmin as true when adding app via the dropdown menu', ()=> {
//        newCtrl.adminAppsRoles = [{id: 1, isAdmin: false},{id: 2, isAdmin: true}];
//        //simulate UI change
//        $rootScope.$apply('newAdmin.selectedNewApp = null');
//        $rootScope.$apply('newAdmin.selectedNewApp = {id: 1, isAdmin: true}');
//
//        expect(newCtrl.adminAppsRoles[0].isAdmin).toBe(true);
//        expect(newCtrl.selectedNewApp).toBe(null);
//    });
//
//    it('should close the modal when updating apps roles succeeded', ()=> {
//        $rootScope.closeThisDialog = () => {};
//        spyOn($rootScope,'closeThisDialog');
//
//        newCtrl.selectedUser = {userId: 'userId'};
//        newCtrl.adminAppsRoles = [{id: 1}];
//
//        deferredUpdateRolesRes.resolve();
//        newCtrl.updateAdminAppsRoles();
//        $rootScope.$apply();
//
//        expect(adminsServiceMock.updateAdminAppsRoles).toHaveBeenCalledWith({userId: newCtrl.selectedUser.userId, appsRoles: newCtrl.adminAppsRoles});
//        expect($rootScope.closeThisDialog).toHaveBeenCalled();
//    });
//    it('should log the error when updating apps roles fails', ()=> {
//        newCtrl.selectedUser = {userId: 'userId'};
//        newCtrl.adminAppsRoles = [{id: 1}];
//
//        spyOn($log,'error');
//        deferredUpdateRolesRes.reject();
//        newCtrl.updateAdminAppsRoles();
//        $rootScope.$apply();
//        expect($log.error).toHaveBeenCalled();
//    });
//    //it('should display the add admin dropdown when clicking the add button', ()=> {
//    //});
//
//});
