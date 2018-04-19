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

describe('Controller: TabsCtrl ',() => {
    beforeEach(module('ecompApp'));

    //destroy $http default cache before starting to prevent the error 'default cache already exists'
    beforeEach(inject((_CacheFactory_)=>{
        _CacheFactory_.destroyAll();
    }));

    let TabsCtrl, $controller, $q, $rootScope, $log, $window, $cookies,$scope;

    beforeEach(inject((_$controller_, _$q_, _$rootScope_, _$log_, _$window_, _$cookies_)=>{
        [$controller, $q, $rootScope, $log, $window, $cookies] = [_$controller_, _$q_, _$rootScope_, _$log_, _$window_, _$cookies_];
    }));

    var deferredApps, deferredUserProfile;
    beforeEach(()=>{
        deferredApps = $q.defer();
        deferredUserProfile = $q.defer();
        let applicationsServiceMock = jasmine.createSpyObj('applicationsServiceMock', ['getUserApps']);
        applicationsServiceMock.getUserApps.and.returnValue(deferredApps.promise);

        let userProfileServiceMock = jasmine.createSpyObj('userProfileServiceMock',['getUserProfile']);
        userProfileServiceMock.getUserProfile.and.returnValue(deferredUserProfile.promise);

        $scope = $rootScope.$new();
        TabsCtrl = $controller('TabsCtrl', {
            applicationsService: applicationsServiceMock,
            $log: $log,
            $window: $window,
            userProfileService: userProfileServiceMock,
            $scope: $scope,
            $cookies: $cookies,
            $rootScope: $rootScope
        });
    });

    it('should populate this.apps with data from portals service getUserApps', ()=>{
        var profile = {roles:'superAdmin',userId:'userid'};
       deferredUserProfile.resolve(profile);
       deferredApps.resolve([{name: 'portal1'},{name: 'portal2'},{name: 'portal3'}]);
        $rootScope.$apply();
        expect($scope.appsViewData.length).toBe(3);
    });

    it('should call $log error when getAllPortals fail', ()=>{
        spyOn($log, 'error');
        deferredUserProfile.reject('something happened!');
        $rootScope.$apply();
        expect($log.error).toHaveBeenCalled();
    });

    it('should open link in a new window when clicking app thumbnail', () => {
        spyOn($window, 'open');
        let someUrl = 'http://some/url/';
        TabsCtrl.goToPortal(someUrl);
        expect($window.open).toHaveBeenCalledWith(someUrl, '_self');
    });
    
 
});