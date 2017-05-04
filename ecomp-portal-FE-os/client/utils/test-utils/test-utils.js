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
/**
 * Created by nnaffar on 1/10/16.
 */
'use strict';
angular.module('testUtils', [])
    .factory('promisesTestUtils', function utilsFactory($q) {

        var setupPromise = function(object, method, data, resolve) {
            spyOn(object, method).and.callFake(function() {
                var deferred = $q.defer();
                if (resolve) {
                    deferred.resolve(data);
                } else {
                    deferred.reject(data);
                }

                return deferred.promise;
            });
        };

        var service = {};

        service.resolvePromise = function(object, method, data) {
            return setupPromise(object, method, data, true);
        };

        service.rejectPromise = function(object, method, data) {
            return setupPromise(object, method, data, false);
        };

        return service;
    });
