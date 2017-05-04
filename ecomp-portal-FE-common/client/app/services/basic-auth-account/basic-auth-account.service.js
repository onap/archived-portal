'use strict';

(function () {
    class BasicAuthAccountService {
        constructor($q, $log, $http, conf,uuid, utilsService) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.uuid = uuid;
            this.utilsService = utilsService;   
        }
        
        createAccount(newAccount) {
        	let deferred = this.$q.defer();
        	this.$http({
                method: "POST",
                url: this.conf.api.basicAuthAccount,
                data: newAccount,
                cache: false,
                headers:{
            		'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
            	if (res == null || res.data == null) {
        	 		this.$log.error('BasicAuthAccountService::createAccount Failed: Result or result.data is null');
                     deferred.reject("BasicAuthAccountService::createAccount Failed: Result or result.data is null");
                } else if (!this.utilsService.isValidJSON(res.data)) {
                 	this.$log.error('BasicAuthAccountService::createAccount Failed: Invalid JSON format');
                    deferred.reject("BasicAuthAccountService::createAccount Failed: Invalid JSON format");
                } else {
                    deferred.resolve(res.data);
                }
            })
            .catch(errRes => {
                deferred.reject(errRes);
            });
            return deferred.promise;
        }
        
        updateAccount(accountId, newAccount) {
        	let deferred = this.$q.defer();
        	this.$http({
                method: "PUT",
                url: this.conf.api.basicAuthAccount + "/" + accountId,
                data: newAccount,
                cache: false,
                headers:{
            		'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
            	if (res == null || res.data == null) {
        	 		this.$log.error('BasicAuthAccountService::updateAccount Failed: Result or result.data is null');
                     deferred.reject("BasicAuthAccountService::updateAccount Failed: Result or result.data is null");
                } else if (!this.utilsService.isValidJSON(res.data)) {
                 	this.$log.error('BasicAuthAccountService::updateAccount Failed: Invalid JSON format');
                    deferred.reject("BasicAuthAccountService::updateAccount Failed: Invalid JSON format");
                } else {
                    deferred.resolve(res.data);
                }
            })
            .catch(errRes => {
                deferred.reject(errRes);
            });
            return deferred.promise;
        }
        
        getAccountList() {
        	let deferred = this.$q.defer();
        	this.$http({
                method: "GET",
                url: this.conf.api.basicAuthAccount,
                cache: false,
                headers:{
            		'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
            	if (res == null || res.data == null) {
        	 		this.$log.error('BasicAuthAccountService::getAccountList Failed: Result or result.data is null');
                     deferred.reject("BasicAuthAccountService::getAccountList Failed: Result or result.data is null");
                } else if (!this.utilsService.isValidJSON(res.data)) {
                 	this.$log.error('BasicAuthAccountService::getAccountList Failed: Invalid JSON format');
                    deferred.reject("BasicAuthAccountService::getAccountList Failed: Invalid JSON format");
                } else {
                    deferred.resolve(res.data.response);
                }
            })
            .catch(errRes => {
                deferred.reject(errRes);
            });
            return deferred.promise;
        }
        
        deleteAccount(accountId) {
        	let deferred = this.$q.defer();
        	this.$http({
                method: "DELETE",
                url: this.conf.api.basicAuthAccount + "/" + accountId,
                cache: false,
                headers:{
            		'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
            	if (res == null || res.data == null) {
        	 		this.$log.error('BasicAuthAccountService::deleteAccount Failed: Result or result.data is null');
                     deferred.reject("BasicAuthAccountService::deleteAccount Failed: Result or result.data is null");
                } else if (!this.utilsService.isValidJSON(res.data)) {
                 	this.$log.error('BasicAuthAccountService::deleteAccount Failed: Invalid JSON format');
                    deferred.reject("BasicAuthAccountService::deleteAccount Failed: Invalid JSON format");
                } else {
                    deferred.resolve(res.data);
                }
            })
            .catch(errRes => {
                deferred.reject(errRes);
            });
            return deferred.promise;
        }
       
    }
    
    BasicAuthAccountService.$inject = ['$q', '$log', '$http', 'conf','uuid4', 'utilsService'];
    angular.module('ecompApp').service('basicAuthAccountService', BasicAuthAccountService)
})();
