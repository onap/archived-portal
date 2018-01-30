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
// 'use strict';
// describe('Controller: WidgetDetailsModalCtrl', ()=> {
//     /**
//      * INITIALIZATION
//      */
//     beforeEach(module('testUtils'));
//     beforeEach(module('ecompApp'));
//
//     let promisesTestUtils;
//     //destroy $http default cache before starting to prevent the error 'default cache already exists'
//     //_promisesTestUtils_ comes from testUtils for promises resolve/reject
//     beforeEach(inject((_CacheFactory_, _promisesTestUtils_)=> {
//         _CacheFactory_.destroyAll();
//         promisesTestUtils = _promisesTestUtils_;
//     }));
//
//     let widgetDetails, scope, $controller, $q, $rootScope, $log, widgetsService, errorMessageByCode, ECOMP_URL_REGEX;
//     let deferredAdminApps, deferredUserProfile;
//     let applicationsServiceMock, widgetsServiceMock, userProfileServiceMock;
//     beforeEach(inject((_$controller_, _$q_, _$rootScope_, _$log_)=> {
//         [$controller, $q, $rootScope, $log] = [_$controller_, _$q_, _$rootScope_, _$log_];
//
//         deferredAdminApps = $q.defer();
//         deferredUserProfile = $q.defer();
//         /*applicationsServiceMock = {
//             getAppsForSuperAdminAndAccountAdmin: () => {
//                 var promise = () => {return deferredAdminApps.promise};
//                 var cancel = jasmine.createSpy();
//                 return {
//                     promise: promise,
//                     cancel: cancel
//                 }
//             }
//         };*/
//
//         widgetsServiceMock = {
//             updateWidget: () => {
//                 var promise = () => {return deferredAdminApps.promise};
//                 var cancel = jasmine.createSpy();
//                 return {
//                     promise: promise,
//                     cancel: cancel
//                 }
//             },
//             createWidget: () => {
//                 var promise = () => {return deferredAdminApps.promise};
//                 var cancel = jasmine.createSpy();
//                 return {
//                     promise: promise,
//                     cancel: cancel
//                 }
//             }
//         };
//
//         userProfileServiceMock = jasmine.createSpyObj('userProfileServiceMock',['getUserProfile']);
//         userProfileServiceMock.getUserProfile.and.returnValue(deferredUserProfile.promise);
//
//         applicationsServiceMock = jasmine.createSpyObj('applicationsServiceMock',['getAppsForSuperAdminAndAccountAdmin']);
//         applicationsServiceMock.getAppsForSuperAdminAndAccountAdmin.and.returnValue(deferredAdminApps.promise);
//
//     }));
//
//     beforeEach(()=> {
//         errorMessageByCode = [];
//         ECOMP_URL_REGEX = "";
//         scope = $rootScope.$new();
//         createController(scope);
//     });
//
//     let createController = scopeObj => {
//         widgetDetails = $controller('WidgetDetailsModalCtrl', {
//             $scope: scope,
//             $log: $log,
//             applicationsService: applicationsServiceMock,
//             widgetsService: widgetsServiceMock,
//             errorMessageByCode: errorMessageByCode,
//             ECOMP_URL_REGEX: ECOMP_URL_REGEX,
//             userProfileService: userProfileServiceMock
//         });
//     };
//
//     /**
//      * MOCK DATA
//      */
//     let newWidgetModel = {
//         name: null,
//         appId: null,
//         appName: null,
//         width: 360,
//         height: 300,
//         url: null
//     };
//     let exsistingWidget = {
//         name: 'some widget',
//         appId: 1,
//         appName: 'APP NAME',
//         width: 360,
//         height: 300,
//         url: 'http://a.com'
//     };
//     let adminApps = [{id: 1, name: 'a'}, {id: 2, name: 'b'}];
//
//     /**
//      * TEST CASES
//      */
//     it('should initialize controller with new widget mode when opening the modal without selected widget', ()=> {
//         expect(widgetDetails.widget).toEqual(newWidgetModel);
//     });
//
//     it('should initialize controller with exsisting widget details when opening the modal with selected widget', ()=> {
//         scope.ngDialogData = {
//             widget: exsistingWidget
//         };
//         createController(scope);
//         expect(widgetDetails.widget).toEqual(exsistingWidget);
//     });
//
//     it('should populate widget selected app name and id when initializing controller with widget', () =>{
//         deferredAdminApps.resolve(adminApps);
//         scope.ngDialogData = {
//             widget: exsistingWidget
//         };
//         createController(scope);
//         scope.$apply();
//         expect(widgetDetails.widget.appId).toEqual(adminApps[0].id);
//         expect(widgetDetails.widget.appName).toEqual(adminApps[0].name);
//     });
//
//     //TODO:
//     //save changes fail - conflict handling
//     //save changes success
//
//
//
// });
