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
 * 
 */
'use strict';

describe('Controller: DashboardCtrl ', function() {
    beforeEach(module('ecompApp'));

    let DashboardCtrl, $controller, $q, rootScope, $log, $window, $cookies, scope;
    let deferredApps, deferredUserProfile, applicationsServiceMock, userProfileServiceMock;


    beforeEach(inject( (_$controller_, _$q_, _$rootScope_, _$log_, _$window_, _$cookies_, _CacheFactory_)=>{
        rootScope = _$rootScope_;
        scope = rootScope.$new();
        $q = _$q_;
        $controller = _$controller_;
        $log = _$log_;
        $window = _$window_;
        $cookies = _$cookies_;

        _CacheFactory_.destroyAll();

        deferredApps = $q.defer();
        deferredUserProfile = $q.defer();
        applicationsServiceMock = jasmine.createSpyObj('applicationsServiceMock', ['getUserApps']);
        applicationsServiceMock.getUserApps.and.returnValue(deferredApps.promise);

        userProfileServiceMock = jasmine.createSpyObj('userProfileServiceMock',['getUserProfile']);
        userProfileServiceMock.getUserProfile.and.returnValue(deferredUserProfile.promise);

        DashboardCtrl = $controller('DashboardCtrl', {
            applicationsService: applicationsServiceMock,
            $log: $log,
            $window: $window,
            userProfileService: userProfileServiceMock,
            $scope: scope,
            $cookies: $cookies
        });
        scope.$digest();
    }));

    it('should populate this.apps with data from portals service getUserApps', inject(function ( _$q_) {
        $q = _$q_;

        let profile = {roles: 'superAdmin', orgUserId: 'userid'};

        deferredUserProfile.resolve(profile)
        deferredApps.resolve([{name: 'portal1'},{name: 'portal2'},{name: 'portal3'}]);
        scope.$digest();
        expect(scope.appsViewData.length).toBe(3);
    }));

    it('should call $log error when getAllPortals fail', inject(function ( _$q_) {
        $q = _$q_;
        spyOn($log, 'error');
        deferredUserProfile.reject('something happened!');
        scope.$digest();
        expect($log.error).toHaveBeenCalled();
    }));

});
