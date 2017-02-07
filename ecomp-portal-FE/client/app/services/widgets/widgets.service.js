/**
 * Created by doritrieur on 12/3/15.
 */
'use strict';

(function () {
    class WidgetsService {
        constructor($q, $log, $http, conf,uuid) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.uuid = uuid;
        }

        getUserWidgets() {
            let deferred = this.$q.defer();
            this.$log.info('WidgetsService::getUserWidgets');
            this.$http({
                method: "GET",
                url: this.conf.api.widgets,
                cache: false,
                headers: {
                    'X-Widgets-Type': 'all',
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                    if (res == null || res.data==null) {
                        deferred.reject("WidgetsService::getUserWidgets Failed");
                    } else {
                        this.$log.info('WidgetsService::getUserWidgets Succeeded');
                        deferred.resolve(res.data);
                    }
                })
                .catch(status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }

        getManagedWidgets() {
            let deferred = this.$q.defer();
            this.$log.info('WidgetsService::getManagedWidgets');
            this.$http({
                method: "GET",
                url: this.conf.api.widgets,
                cache: false,
                headers: {
                    'X-Widgets-Type': 'managed',
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                    if (Object.keys(res.data).length == 0) {
                        deferred.reject("WidgetsService::getManagedWidgets Failed");
                    } else {
                        this.$log.info('WidgetsService::getManagedWidgets Succeeded');
                        deferred.resolve(res.data);
                    }
                })
                .catch(status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }

        createWidget(widgetData) {
            let deferred = this.$q.defer();
            this.$log.info('WidgetsService::createWidget');
            this.$http({
                method: "POST",
                url: this.conf.api.widgets,
                cache: false,
                data: widgetData,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                    if (Object.keys(res.data).length == 0) {
                        deferred.reject("WidgetsService::createWidget Failed");
                    } else {
                        this.$log.info('WidgetsService::createWidget Succeeded');
                        deferred.resolve(res.data);
                    }
                })
                .catch(errRes => {
                    deferred.reject(errRes);
                });
            return deferred.promise;
        }

        updateWidget(widgetId, widgetData) {
            let deferred = this.$q.defer();
            this.$log.info('WidgetsService::updateWidget');
            let url = this.conf.api.widgets + '/' + widgetId;
            this.$http({
                method: "PUT",
                url: url,
                cache: false,
                data: widgetData,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                    if (Object.keys(res.data).length == 0) {
                        deferred.reject("WidgetsService::updateWidget Failed");
                    } else {
                        this.$log.info('WidgetsService::updateWidget Succeeded');
                        deferred.resolve(res.data);
                    }
                })
                .catch(errRes => {
                    deferred.reject(errRes);
                });
            return deferred.promise;
        }

        deleteWidget(widgetId) {
            let deferred = this.$q.defer();
            this.$log.info('WidgetsService::deleteWidget');
            let url = this.conf.api.widgets + '/' + widgetId;
            this.$http({
                method: "DELETE",
                url: url,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                    // If response comes back as a redirected HTML page which IS NOT a success
                    if (Object.keys(res.data).length == 0) {
                        deferred.reject("WidgetsService::deleteWidget Failed");
                    } else {
                        this.$log.info('WidgetsService::deleteWidget Succeeded');
                        deferred.resolve(res.data);
                    }
                })
                .catch(status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }

        checkIfWidgetUrlExists(urlToValidate) {
            let deferred = this.$q.defer();
            this.$log.info('WidgetsService::checkIfWidgetUrlExists:' + urlToValidate);
            let reqBody = {
                url: urlToValidate
            };
            this.$http({
                method: "POST",
                url: this.conf.api.widgetsValidation,
                cache: false,
                data: reqBody,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(() => {
                //if exists return true
                    deferred.resolve(true);
                })
                .catch(response => {
                    if(response.data.status === 404){
                        deferred.resolve(false);
                    }else{
                        deferred.reject(response.data);
                    }
                });
            return deferred.promise;
        }

    }
    WidgetsService.$inject = ['$q', '$log', '$http', 'conf','uuid4'];
    angular.module('ecompApp').service('widgetsService', WidgetsService)
})();
