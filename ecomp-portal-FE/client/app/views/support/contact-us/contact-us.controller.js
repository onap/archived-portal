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

(function () {	
		
    class ContactUsCtrl {
    	constructor($log, contactUsService, applicationsService, $modal, ngDialog, $state) {  
        	
        	contactUsService.getContactUSPortalDetails().then(res => {
        		// $log.info('ContactUsCtrl:: contactUsService getContactUSPortalDetails res',res);
                // $log.info('getting res',res);
                var result = (typeof(res.response) != "undefined" && res.response!=null)?res.response:null;
                //	$log.info('result',result);
                //	$log.info('done');
                var source = JSON.parse(result);
                //	$log.info(source); 
                this.ush_TicketInfoUrl = source.ush_ticket_url; 
                this.portalInfo_Address = source.feedback_email_address; 
                this.feedback_Url = source.portal_info_url;                	
        	}).catch(err=> {
           	 	$log.error('ContactUsCtrl:error:: ', err);
            }).finally(() => {
           });
        	
        	let init = () => {
                // $log.info('ecomp app::contact-us-controller::initializing...');
                this.appTable=[];
                this.functionalTableData=[];
            };
            init();     
            
            let updateContactUsTable = () => {
            	contactUsService.getAppsAndContacts().then(res=> {
         			// $log.info('ContactUsCtrl:: contactUsService getAppsAndContacts res',res);
                 	var tableData=[];
                 	// $log.info('getting res',res);
                 	var result = (typeof(res.response) != "undefined" && res.response!=null)?res.response:null;
                 	// $log.info('result',result);
                 	// $log.info('done');
                 	var source = result;
                 	// $log.info(source);
                 	// Drop Portal app, empty name entries
                 	for(var i=0;i<source.length; i++) {
                 		var dataArr = source[i];
                 		if ( !dataArr.appName  || dataArr.appId == 1) {
                 			continue;
                 		}
                     	var dataTemp={
                     		app_name: dataArr.appName,
                     		contact_name: dataArr.contactName,
                     		contact_email: dataArr.contactEmail,
                     		desc: dataArr.description,
                     		url_Info: dataArr.url,
                     		app_Id: dataArr.appId,
                     	}
                     	tableData.push(dataTemp);
                 	}              	
                 	this.appTable=tableData;
                 }).catch(err=> {
          		 	 $log.error('ContactUsCtrl.updateContactUsTable:error:: ', err);
          		 })
            };
            
            contactUsService.getAppCategoryFunctions().then(res=> {
     			// $log.info('ContactUsCtrl:: contactUsService getAppCategoryFunctionsthen res',res);
             	var tablefunctionalData=[];
             	// $log.info('getting res',res);
             	var result = (typeof(res.response) != "undefined" && res.response!=null)?res.response:null;
             	// $log.info('result',result);
             	// $log.info('done');
             	var source = result;
             	// $log.info(source);                	
             	for(var i=0;i<source.length; i++) {
             		var datafunctionalArr = source[i];
                 	var datafuntionalTemp={
                 		category: datafunctionalArr.category,
                 		app_Name: datafunctionalArr.application,
                 		functions: datafunctionalArr.functions,
                 		app_Id: datafunctionalArr.appId,
                 	}
                 	tablefunctionalData.push(datafuntionalTemp);
             	}              	
             	this.functionalTableData=tablefunctionalData;
             }).catch(err=> {
      		 	 $log.error('ContactUsCtrl:error:: ', err);
      		 })
            
            updateContactUsTable();
            this.editContactUsModalPopup = () => {
                 // $log.debug('ContactUsCtrl::editContactUsModalPopup updating table data...');               
                 var modalInstance = ngDialog.open({
                     templateUrl: 'app/views/support/contact-us/contact-us-manage/contact-us-manage.html',
                     controller: 'ContactUsManageController',
                     resolve: {
                         message: function message() {
                             var message = {
                                 type: 'Contact',
                             };
                             return message;
                         }
                     }
                 }).closePromise.then(needUpdate => {	              	 
                	 updateContactUsTable();
	             });       
             };
             
             this.goToSection = (id) => {
            	 var targetDiv = document.getElementById(id);        	 
            	 var offSetHeight = 0;
            	 for(var i=0;i<this.appTable.length;i++){  
            		 if(this.appTable[i].app_Id==id)
            			 break;
            		 if(this.appTable[i].showFlag==true){
            			 offSetHeight+=document.getElementById('collapse'+i).clientHeight;         			
            		 }
            	 }
            	 this.appTable.forEach(d => d.showFlag = false);
            	// let index = this.appTable.findIndex(a => a.app_Id == id);
            	 var index =-1;
            	 for(var i=0; i<this.appTable.length;i++){
            		 if(this.appTable[i].app_Id==id){
            			 index = i;
            			 break;
            		 }
            	 }
            	 console.log(index);
            	 if (index > -1) {
            		 // setting the showFlag to true based on index comparing with the app_Id 
            		 this.appTable[index].showFlag = true;
            		 $('#contentId').animate({
          	            scrollTop: targetDiv.offsetTop-offSetHeight + 'px'
          	        }, 'fast');
            	 }            	             	 
            	 
             };
             
             // Take the user to the application on the get access page.
             this.goGetAccess = (appName) => {
            	 // $log.debug('ContactUsCtrl::goGetAccess received name ' + appName);
            	 applicationsService.goGetAccessAppName = appName;
            	 $state.go('root.getAccess');
             };
             
        }
    }
    ContactUsCtrl.$inject = ['$log','contactUsService', 'applicationsService', '$modal', 'ngDialog', '$state'];
    angular.module('ecompApp').controller('ContactUsCtrl', ContactUsCtrl);
})();
