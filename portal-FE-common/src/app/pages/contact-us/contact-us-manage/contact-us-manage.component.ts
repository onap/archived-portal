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

import { Component, OnInit, Input} from '@angular/core';
import { ContactUsService } from '../../../shared/services/index';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-contact-us-manage',
  templateUrl: './contact-us-manage.component.html',
  styleUrls: ['./contact-us-manage.component.scss']
})
export class ContactUsManageComponent implements OnInit {

  contactUsList = [];
  contactUsAllAppList = [];
  result: any;
  selectedApp: any;
  showEdit: boolean = false;
  newContactUs ={
    appId:'',
    appName:'',
    description:'',
    contactName:'',
    contactEmail:'',
    url:'' ,
    activeYN:''    			
 };
  

  constructor(public activeModal: NgbActiveModal, public ngbModal: NgbModal, public contactUsService: ContactUsService) { }

  ngOnInit() {

    this.getContactUsList();
    this.getListOfApp();
  }

  getContactUsList(){
    console.log("getContactUsList called...");
    this.contactUsService.getContactUs()
      .subscribe( _data => {
        this.result = _data;
        console.log("getContactUsList Data :: ", _data);
        if (this.result.response == null || this.result.response == 'undefined') {
            console.log('ContactUsService::getContactUsList Failed: Result or result.data is null');
        }else{
          for(var i=0; i<this.result.response.length;i++){
            if(this.result.response[i].appId!=1)
              this.contactUsList.push(this.result.response[i]);
          }
        }
      },error =>{
        console.log(error);
    });
  }

  getListOfApp(){
    console.log("getListOfApp called...");
    this.contactUsService.getListOfApp()
      .subscribe( _data => {
        this.result = _data;
        console.log("getListOfApp Data :: ", _data);
        if (this.result == null || this.result == 'undefined') {
            console.log('ContactUsService::getListOfApp Failed: Result or result.data is null');
        }else{
          let res1 = this.result;
          let realAppIndex = 0;
          this.contactUsAllAppList.length=0;
          console.log("this.contactUsList ",this.contactUsList)
          for (var i = 1; i <= res1.length; i++) {
              if (!res1[i - 1].restrictedApp) {
                var okToAdd = true;
                for(var j =0; j<this.contactUsList.length;j++){
                  if(res1[i - 1].title == this.contactUsList[j].appName){
                    okToAdd=false;
                    console.log("okToAdd=false res1[i - 1].title ",res1[i - 1].title);
                  }
                }
                // not allowed to add(duplicate) another entry if the app is already available in the table
                if(okToAdd){
                  if(res1[i - 1].title){
                    this.contactUsAllAppList.push({
                        index: realAppIndex,
                        title: res1[i - 1].title,
                        value: res1[i - 1].index
                    });
                  }       
                  realAppIndex = realAppIndex + 1;
                }         
              }
          }
        }
      },error =>{
        console.log(error);
    });
  }

  addNewContactUs(){
    console.log("Calling addNewContactUs");
    let selectedApplication = this.selectedApp;
    this.newContactUs.appId = selectedApplication.value;
    this.newContactUs.appName = selectedApplication.title;
    console.log("newContactUsObj ",this.newContactUs);
    this.contactUsService.addContactUs(this.newContactUs)
      .subscribe( _data => {
        this.result = _data;
        console.log("addContactUs response :: ", _data);
        this.contactUsList.push(this.newContactUs);
      },error =>{
        console.log(error);
    });
  }

  editContactUs(contactObj: any){

    var contactUsObj={
      appId:contactObj.appId,
      appName:contactObj.appName,
      description:contactObj.description,
      contactName:contactObj.contactName,
      contactEmail:contactObj.contactEmail,
      url:contactObj.url,       		
    };

    this.contactUsService.modifyContactUs(contactUsObj)
      .subscribe( _data => {
        this.result = _data;
        console.log("editContactUsFun response :: ", _data);
        this.showEdit=false;
      },error =>{
        console.log(error);
    });
  }

  delContactUs(appObj: any){
      this.contactUsService.removeContactUs(appObj.appId)
      .subscribe( _data => {
        this.result = _data;
        console.log("delContactUsFun response :: ", _data);
        this.contactUsList.splice(appObj, 1);
      },error =>{
        console.log(error);
      });
  }

}
