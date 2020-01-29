/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
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

import { Component, OnInit, ViewChild, Input } from '@angular/core';
import { MatTableDataSource } from '@angular/material';
import { MatSort, MatPaginator } from '@angular/material';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ContactUsService } from '../../shared/services/index';
import { ContactUsManageComponent } from './contact-us-manage/contact-us-manage.component';

@Component({
  selector: 'app-contact-us',
  templateUrl: './contact-us.component.html',
  styleUrls: ['./contact-us.component.scss']
})
export class ContactUsComponent implements OnInit {

  contactUsList: Array<Object> = [];
  appTable: Array<Object> = [];
  functionalTableData: Array<Object> = [];

  ush_TicketInfoUrl: string;
  portalInfo_Address: string;
  feedback_Url: string;
  showUp: boolean = true;
  showDown: boolean = false;

  result: any;
  isEditMode: boolean = false;
  displayedColumns: string[] = ['category', 'onapFunctions','onapApplications'];
  dataSource = new MatTableDataSource(this.functionalTableData);
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(public contactUsService: ContactUsService, public ngbModal: NgbModal) { }

  ngOnInit() {
    this.appTable=[];
    this.functionalTableData=[];

    this.getContactUSPortalDetails();
    this.updateContactUsTable();
    this.getAppCategoryFunctions()
  }

  getContactUSPortalDetails(){
    console.log("getContactUSPortalDetails called...");
    this.contactUsService.getContactUSPortalDetails()
      .subscribe( _data => {
        this.result = _data;
        console.log("getContactUSPortalDetails Data :: ", _data);
        if (this.result.response == null || this.result.response == 'undefined') {
            console.log('ContactUsService::getContactUSPortalDetails Failed: Result or result.data is null');
        }else{
          var source = JSON.parse(this.result.response);
          this.ush_TicketInfoUrl = source.ush_ticket_url; 
          this.portalInfo_Address = source.feedback_email_address; 
          this.feedback_Url = source.portal_info_url;
        }
      },error =>{
        console.log(error);
    });
  }

  updateContactUsTable(){
    console.log("updateContactUsTable called...");
    this.contactUsService.getAppsAndContacts()
      .subscribe( _data => {
        this.result = _data;
        console.log("getAppsAndContacts Data :: ", _data);
        if (this.result.response == null || this.result.response == 'undefined') {
            console.log('ContactUsService::getAppsAndContacts Failed: Result or result.data is null');
        }else{
          var tableData=[];
          var source = this.result.response;
          for(var i=0; i<source.length; i++) {
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
        }
      },error =>{
        console.log(error);
    });
  }

  getAppCategoryFunctions(){
    console.log("getAppCategoryFunctions called");
    this.contactUsService.getAppCategoryFunctions()
      .subscribe( _data => {
        this.result = _data;
        console.log("getAppCategoryFunctions Data :: ", _data);
        if (this.result.response == null || this.result.response == 'undefined') {
            console.log('ContactUsService::getAppCategoryFunctions Failed: Result or result.data is null');
        }else{
            var tablefunctionalData=[];
            var source = this.result.response;
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
            this.functionalTableData = tablefunctionalData;
            this.populateTableData(this.functionalTableData);
        }
      },error =>{
        console.log(error);
    });
  }

  populateTableData(functionalTableData: Array<Object>){
    this.dataSource = new MatTableDataSource(functionalTableData);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }

  editContactUsModal() {
    const modalRef = this.ngbModal.open(ContactUsManageComponent, { size: 'lg' });
  }

  showApplicationInfo(appId: any){
    console.log("AppId Contact US...",appId);
    let appInfoDiv = document.getElementById('collapse'+appId);
    let uparrowDiv = document.getElementById('arrowup'+appId);
    let downarrowDiv = document.getElementById('arrowdown'+appId);

    if(!appInfoDiv.getAttribute('hidden')){
      appInfoDiv.setAttribute("hidden","true");
      uparrowDiv.setAttribute("hidden", "true");
      downarrowDiv.removeAttribute("hidden");
    }else if(appInfoDiv.getAttribute('hidden')){
      appInfoDiv.removeAttribute("hidden");
      uparrowDiv.removeAttribute("hidden");
      downarrowDiv.setAttribute("hidden","true");
    }
  }

  goGetAccess(app_name: any){
    console.log("Get Access :: goGetAccess method implemetation is pending... appName : ",app_name);
  }

}
