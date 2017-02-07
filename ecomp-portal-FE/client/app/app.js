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

var app = angular.module('ecompApp', [
        'ngCookies',
        'ngResource',
        'ngSanitize',
        'ui.router',
        'ecomp.conf',
        'ngMessages',
        'ui.select',
        'angular-cache',
        'ngDialog',
        'att.abs',
        'att.gridster',
        'uuid',
        'ui.bootstrap',
        'ngMaterial'
    ])
    .config(($stateProvider, $urlRouterProvider, $locationProvider, $httpProvider, ngDialogProvider) => {
        $urlRouterProvider.otherwise('/error404');
        $locationProvider.html5Mode(true);

        //initialize get if not there
        if (!$httpProvider.defaults.headers.get) {
             $httpProvider.defaults.headers.get = {};
        }

        //withCredentials flag on the XHR object - add cookie to XHR requests
        $httpProvider.defaults.withCredentials = true;
        $httpProvider.defaults.useXDoain = true;
        $httpProvider.defaults.timeout = 30000;

        //default configuration for ngDialog modal
        ngDialogProvider.setDefaults({
            className: 'ngdialog-theme-default',
            showClose: true,
            closeByDocument: false,
            closeByEscape: false
        });


        var myHostName;
        myHostName = location.host;

        if (!(myHostName.includes("localhost"))) {
            $httpProvider.defaults.headers.get['If-Modified-Since'] = 'Mon, 26 Jul 1997 05:00:00 GMT';
            $httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
            $httpProvider.defaults.headers.get['Pragma'] = 'no-cache';
        }


        //interceptor here:
        var interceptor = function ($q, $injector, $log) {
            return {
                'responseError': function (rejection) {
                    $log.error('Interceptor rejection: ' + JSON.stringify(rejection));
                    var $state = $injector.get('$state');
                    switch (rejection.status) {
                        case 401:
                            var globalLoginUrl = rejection.headers()['global-login-url'];
                            if (globalLoginUrl) {
                                window.location = globalLoginUrl +
                                    (globalLoginUrl.indexOf('?') === -1 ? '?' : '') +
                                    '&retUrl=' + encodeURI(window.location);
                                return;
                            }
                            break;
                        case 403:
                            // $state.go('root.applicationsHome');
                            //handle forbidden error
                            break;
                        // case 404:
                        //     $state.go('/login.htm');
                        //     //handle forbidden error
                        //     break;
                        default:
                        //handle internal server error
                    }
                    return $q.reject(rejection);
                }
            };
        };
        $httpProvider.interceptors.push(interceptor);

    }).run(($http, CacheFactory) => {
        //default configuration for cache factory
        $http.defaults.cache = CacheFactory('defaultCache', {
            maxAge: 15 * 60 * 1000, // Items added to this cache expire after 15 minutes
            cacheFlushInterval: 60 * 60 * 1000, // This cache will clear itself every hour
            deleteOnExpire: 'aggressive' // Items will be deleted from this cache when they expire
        });
    });


angular.module( 'ecompApp' ).config( [
'$compileProvider',
function( $compileProvider )
{
	$compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|chrome-extension):/);
}
]);
