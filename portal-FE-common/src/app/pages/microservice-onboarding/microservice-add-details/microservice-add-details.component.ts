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
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { IMircroservies } from 'src/app/shared/model/microservice-onboarding/microservices';
import { MicroserviceService, WidgetOnboardingService } from '../../../shared/services/index';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-microservice-add-details',
  templateUrl: './microservice-add-details.component.html',
  styleUrls: ['./microservice-add-details.component.scss']
})
export class MicroserviceAddDetailsComponent implements OnInit {

  @Input() public ms: IMircroservies;
  @Output() passEntry: EventEmitter<any> = new EventEmitter();
  result: any;
  selected: any;
  isEditMode: any;
  originalName: string;
  dupliateName = false;
  emptyServiceName = false;
  emptyServiceDesc = false;
  emptyServiceURL = false;
  emptyServiceApp = false;
  availableSecurityTypes = [];
  availableWidgets = [];
  applicationList: Array<Object> = [];

  constructor(public microservice: MicroserviceService, public widgetOnboardingService: WidgetOnboardingService, 
    public activeModal: NgbActiveModal, public ngbModal: NgbModal) { }

  ngOnInit() {
    if(this.ms.name){
      this.isEditMode = true;
    }else{
      this.isEditMode = false;
    }
    this.ms.parameterList = [];
    this.ms.active = true;
    this.populateAvailableApps();
    this.getAvailableSecurityTypes();
  }

  getAvailableWidgets(serviceId){
    console.log("getAvailableWidgets called");
    this.microservice.getWidgetListByService(serviceId)
    .subscribe(data => {
        this.result = data;
        if (this.result == null || this.result) {
              console.log('MicroserviceService::getServiceList Failed: Result or result.data is null');
          }else {
            this.availableWidgets = [];
            for(var i = 0; i < data.length; i++){
              this.availableWidgets.push({
                name: data[i]
              })
            }
          }
    }, error =>{
      console.log(error);
    });
  };
  
  getAvailableSecurityTypes(): Array<String> {
    this.availableSecurityTypes = [];
    this.availableSecurityTypes.push({
      id: 0,
      name: 'No Authentication'
    });
    this.availableSecurityTypes.push({
      id: 1,
      name: 'Basic Authentication'
    });
    this.availableSecurityTypes.push({
      id: 2,
      name: 'Cookie based Authentication'
    });

    return this.availableSecurityTypes;
  };
  
  populateAvailableApps(){
    this.widgetOnboardingService.populateAvailableApps()
      .subscribe( _data => {
        let allPortalsFilterObject = {index: 0, title: 'Select Application', value: ''};
        this.applicationList = [allPortalsFilterObject];
        var realAppIndex = 1;
        for (let i = 1; i <= _data.length; i++) {
            if (!_data[i-1].restrictedApp) {
                this.applicationList.push({
                    index: realAppIndex,
                    title: _data[i - 1].name,
                    value: _data[i - 1].id
                })
                realAppIndex = realAppIndex + 1;
            }
        }
      }, error => {
        console.log(error);
    }); 
  };

  addParameter() {
    this.ms.parameterList.push({}); 
  }

  testServiceURL(){
    console.log("testServiceURL called  id is :: ",this.ms.id)
    this.microservice.getServiceJSON(this.ms.id)
      .subscribe( _data => {
        this.result = _data;
        console.log("testServiceURL response :: ",this.result);
        document.getElementById("microservice-details-input-json").innerHTML = (JSON.stringify(this.result));
      }, error => {
        document.getElementById("microservice-details-input-json").innerHTML = "Something went wrong. Please go back to the previous page or try again later.";
        console.log(error);
    });
  }

  removeParamItem(parameter: any){
    console.log("removeParamItem called", parameter);
    this.ms.parameterList.splice(parameter.para_key, 1);
    this.microservice.getUserParameterById(parameter)
    .subscribe(data => {
        this.result = data;
        if (this.result == null || this.result) {
              console.log('MicroserviceService::removeParamItem Failed: Result or result.data is null');
        }else if(this.result && this.result.length > 0) {
          this.microservice.deleteUserParameterById(parameter.id)
          .subscribe(__data => {
            for(var i = 0; i < this.ms.parameterList.length; i++){
              if(this.ms.parameterList[i].para_key == parameter.para_key
              && this.ms.parameterList[i].para_value == parameter.para_value){
                this.ms.parameterList.splice(i, 1);
                return;
              }
            }
              
          }, error =>{
            console.log(error);
          });
        }else{
          for(var i = 0; i < this.ms.parameterList.length; i++){
            if(this.ms.parameterList[i].para_key == parameter.para_key
            && this.ms.parameterList[i].para_value == parameter.para_value){
              this.ms.parameterList.splice(i, 1);
              return;
            }
          }
        }
    }, error =>{
      console.log(error);
    });
  }

  //Add Or Update Microservices.
  saveChanges(){
    console.log("saveChanges..",this.ms);
    if(this.ms && this.ms.id && this.ms.id !='undefined'){
      this.isEditMode = true;
    }
    var isValid = true;
    
    if(this.ms.name == ''
      || this.ms.name == undefined){
      this.emptyServiceName = true;
      isValid = false;
    }
    console.log("a >",isValid);
    if(this.dupliateName == true){
      isValid = false;
    }
    console.log("b> ",isValid);  
    if(this.ms.desc == ''
    || this.ms.desc == undefined){
      this.emptyServiceDesc = true;
      isValid = false;
    }
    console.log("c> ",isValid); 
    
    if(this.ms.url == ''
      || this.ms.url == undefined){
        this.emptyServiceURL = true;
        isValid = false;
    }
    console.log("d> ",isValid); 
      
    if(this.ms.appId == undefined
    || this.ms.appId == null){
      this.emptyServiceApp = true;
      isValid = false;
    }
    
    console.log("IsValid flag add/update microservices ",isValid ) 

    if(!isValid)
      return;
    /*
    * Check the parameter list, delete those parameters that don't
    * have key
    */
    if(this.ms && this.ms.parameterList){
      for(var i = 0; i < this.ms.parameterList.length; i++){
        if(this.ms.parameterList[i].para_key == undefined
        || this.ms.parameterList[i].para_key == null
        || this.ms.parameterList[i].para_key == ""){
          this.ms.parameterList.splice(i, 1);
          i--;
        }
      }
    }
    if(this.ms.securityType == undefined ||
    this.ms.securityType == null)
      this.ms.securityType = "No Authentication";
    else{
      this.ms.securityType = this.ms.securityType;
      this.ms.username = this.ms.username;
      this.ms.password = this.ms.password;
    }
    
    var active = 'N';
    if(this.ms.active == true)
      active = 'Y';
    
    let paramList = [];
    if(this.ms.parameterList && this.ms.parameterList.length >0){
      paramList = this.ms.parameterList;
    }  
    var newService = {
      name: this.ms.name,
      desc: this.ms.desc,
      appId: this.ms.appId,
      url: this.ms.url,
      securityType: this.ms.securityType,
      username: this.ms.username,
      password: this.ms.password,
      active: active,
      parameterList: paramList
    };

    if(this.isEditMode){
      console.log("Edit microservice mode called");
      this.microservice.updateService(this.ms.id , newService)
        .subscribe( data => {
          this.result = data;
          console.log("update microservice response :: ",this.result);
          this.passEntry.emit(this.result);
          this.ngbModal.dismissAll();
        }, error => {
          console.log(error);
          this.ngbModal.dismissAll();
       }); 
    }else{  
      console.log("Add microservice mode called")
      this.microservice.createService(newService)
        .subscribe( data => {
          this.result = data;
          console.log("add microservice response :: ",this.result);
          this.passEntry.emit(this.result);
          this.ngbModal.dismissAll();
        }, error => {
          this.ngbModal.dismissAll();
          console.log(error);
      }); 
    }
  }
}
