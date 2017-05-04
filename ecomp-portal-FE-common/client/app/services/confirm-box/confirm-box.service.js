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
 * Created by nnaffar on 1/18/16.
 */
'use strict';

(function () {
    class ConfirmBoxService {
        constructor($q, $log, ngDialog) {
            this.$q = $q;
            this.$log = $log;
            this.ngDialog = ngDialog;
        }

        showInformation(message) {
            let deferred = this.$q.defer();
            this.ngDialog.open({
                templateUrl: 'app/views/confirmation-box/information-box.tpl.html',
                controller: 'ConfirmationBoxCtrl',
                controllerAs: 'confirmBox',
                className: 'confirm-box ngdialog-theme-default',
                showClose: false,
                data: {
                    message: message
                }
            }).closePromise.then(confirmed => {
                deferred.resolve(confirmed.value);
            }).catch(err => {
                deferred.reject(err);
            });
            return deferred.promise;
        };
        
        editItem(message) {
            let deferred = this.$q.defer();
            this.ngDialog.open({
                templateUrl: 'app/views/confirmation-box/confirmation-box.tpl.html',
                controller: 'ConfirmationBoxCtrl',
                controllerAs: 'confirmBox',
                className: 'confirm-box ngdialog-theme-default',
                showClose: false,
                data: {
                    message: message
                }
            }).closePromise.then(confirmed => {
                deferred.resolve(confirmed.value);
            }).catch(err => {
                deferred.reject(err);
            });
            return deferred.promise;
        };
       
        
        showDynamicInformation(message, templatePath, controller) {
            let deferred = this.$q.defer();
            this.ngDialog.open({
            	templateUrl: templatePath,
                controller: controller,
                controllerAs: 'confirmBox',
                className: 'confirm-box ngdialog-theme-default',
                showClose: false,
                data: {
                    message: message
                }
            }).closePromise.then(confirmed => {
                deferred.resolve(confirmed.value);
            }).catch(err => {
                deferred.reject(err);
            });
            return deferred.promise;
        };
        
        confirm(message) {
            let deferred = this.$q.defer();
            this.ngDialog.open({
                templateUrl: 'app/views/confirmation-box/confirmation-box.tpl.html',
                controller: 'ConfirmationBoxCtrl',
                controllerAs: 'confirmBox',
                className: 'confirm-box ngdialog-theme-default',
                showClose: false,
                data: {
                    message: message
                }
            }).closePromise.then(confirmed => {
                deferred.resolve(confirmed.value);
            }).catch(err => {
                deferred.reject(err);
            });
            return deferred.promise;
        };

        deleteItem(item) {
            let deferred = this.$q.defer();
            this.ngDialog.open({
                templateUrl: 'app/views/confirmation-box/confirmation-box.tpl.html',
                controller: 'ConfirmationBoxCtrl',
                controllerAs: 'confirmBox',
                className: 'confirm-box ngdialog-theme-default',
                showClose: false,
                data: {
                    item: item,
                    title: 'Functional Menu - Delete'
                }
            }).closePromise.then(confirmed => {
                deferred.resolve(confirmed.value);
            }).catch(err => {
                deferred.reject(err);
            });
            return deferred.promise;
        };

        moveMenuItem(message) {
            let deferred = this.$q.defer();
            this.ngDialog.open({
                templateUrl: 'app/views/confirmation-box/dragdrop-confirmation-box.tpl.html',
                controller: 'ConfirmationBoxCtrl',
                controllerAs: 'confirmBox',
                className: 'confirm-box ngdialog-theme-default',
                showClose: false,
                data: {
                    message: message,
                    title:'Functional Menu - Move'
                }
            }).closePromise.then(confirmed => {
                deferred.resolve(confirmed.value);
            }).catch(err => {
                deferred.reject(err);
            });
            return deferred.promise;
        };

        makeAdminChanges(message) {
            let deferred = this.$q.defer();
            this.ngDialog.open({
                templateUrl: 'app/views/confirmation-box/admin-confirmation-box.tpl.html',
                controller: 'ConfirmationBoxCtrl',
                controllerAs: 'confirmBox',
                className: 'confirm-box ngdialog-theme-default',
                showClose: false,
                data: {
                    message: message,
                    title: 'Admin Update'
                }
            }).closePromise.then(confirmed => {
                deferred.resolve(confirmed.value);
            }).catch(err => {
                deferred.reject(err);
            });
            return deferred.promise;
        };
        
        
        makeUserAppRoleCatalogChanges(message) {
            let deferred = this.$q.defer();
            this.ngDialog.open({
                templateUrl: 'app/views/confirmation-box/admin-confirmation-box.tpl.html',
                controller: 'ConfirmationBoxCtrl',
                controllerAs: 'confirmBox',
                className: 'confirm-box ngdialog-theme-default',
                showClose: false,
                data: {
                    message: message,
                    title: 'UserRoles Update'
                }
            }).closePromise.then(confirmed => {
                deferred.resolve(confirmed.value);
            }).catch(err => {
                deferred.reject(err);
            });
            return deferred.promise;
        };
     
        
        webAnalyticsChanges(message) {
            let deferred = this.$q.defer();
            this.ngDialog.open({
                templateUrl: 'app/views/confirmation-box/admin-confirmation-box.tpl.html',
                controller: 'ConfirmationBoxCtrl',
                controllerAs: 'confirmBox',
                className: 'confirm-box ngdialog-theme-default',
                showClose: false,
                data: {
                    message: message,
                    title: 'Add WebAnalytics Source'
                }
            }).closePromise.then(confirmed => {
                deferred.resolve(confirmed.value);
            }).catch(err => {
                deferred.reject(err);
            });
            return deferred.promise;
        };

        
        updateWebAnalyticsReport(message) {
            let deferred = this.$q.defer();
            this.ngDialog.open({
                templateUrl: 'app/views/confirmation-box/admin-confirmation-box.tpl.html',
                controller: 'ConfirmationBoxCtrl',
                controllerAs: 'confirmBox',
                className: 'confirm-box ngdialog-theme-default',
                showClose: false,
                data: {
                    message: message,
                    title: 'Update WebAnalytics Source'
                }
            }).closePromise.then(confirmed => {
                deferred.resolve(confirmed.value);
            }).catch(err => {
                deferred.reject(err);
            });
            return deferred.promise;
        };

    }
    ConfirmBoxService.$inject = ['$q', '$log', 'ngDialog'];
    angular.module('ecompApp').service('confirmBoxService', ConfirmBoxService)
})();
