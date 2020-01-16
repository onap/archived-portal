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

import { Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import { IWidget } from 'src/app/shared/model/widget-onboarding/widget';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { WidgetOnboardingService, MicroserviceService, AdminsService, ApplicationsService } from 'src/app/shared/services';
import { FormBuilder, FormGroup, FormControl } from '@angular/forms';
import { IMircroservies } from 'src/app/shared/model/microservice-onboarding/microservices';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { InformationModalComponent } from 'src/app/modals/information-modal/information-modal.component';

@Component({
  selector: 'app-widget-details-dialog',
  templateUrl: './widget-details-dialog.component.html',
  styleUrls: ['./widget-details-dialog.component.scss']
})
export class WidgetDetailsDialogComponent implements OnInit {
  @Input() public widget: IWidget;
  @Input() public availableMicroServices: Array<IMircroservies>;
  @Input() public applicationList: Array<Object>;

  @Output() passEntry: EventEmitter<any> = new EventEmitter();

  widgetsList: any;
  uploadForm: FormGroup;
  result: any;
  selected: any;
  isEditMode: boolean = false;
  hasSelectedApp: boolean = false;
  availableApps = [];
  availableRoles = [];
  allRoleSelected: boolean = false;
  appCounter = 0;
  duplicatedName:boolean = true;
  widgetFileTypeError:boolean = false;
  isFileNotSelected:boolean = true;

  constructor(public widgetOnboardingService: WidgetOnboardingService, 
    public microservice: MicroserviceService, public applicationsService: ApplicationsService,
    public formBuilder: FormBuilder, public activeModal: NgbActiveModal, 
    public ngbModal: NgbModal, public adminsService: AdminsService) { }

  ngOnInit() {
    this.widget.allUser = true;
    this.getOnboardingWidgets();
    this.getAvailableApps();
    this.duplicatedName = true;
    this.allRoleSelected = false;
    this.appCounter = 0;
    this.uploadForm = this.formBuilder.group({
      profile: [''],
      widgetName:[''],
      description:[''],
      serviceEndPoint:[''],
      allowAllUser:[''],
      applicationName:[''],
      applicationRole:['']
    });
    
    if(this.widget && this.widget.name){
      this.isEditMode = true;
    }
    if(this.isEditMode && this.widget && this.widget.allowAllUser == "Y"){
      this.widget.allUser = true;
    }else if(this.isEditMode && this.widget && this.widget.allowAllUser == "N"){
      this.widget.allUser = false;
    }
    if(this.widget && this.widget.serviceId != null){
      this.widget.serviceURL = this.widget.serviceId;
    }
  }

  //Add Or Update Widget.
  saveChanges(){
    if(this.widget.name == '' || this.widget.name == undefined){
      this.openConfirmationModal("Error",'Widget Name is required.');
      return;
    }

    if(!this.isEditMode){
      this.updateWidgetName();
    }

    if(this.duplicatedName == false){
      this.openConfirmationModal("Error",'Name not available - please choose different name.');
      return;
    }

    if(this.widgetFileTypeError == true){
      this.openConfirmationModal("Error",'File must be .zip');
      return;
    }

    let widgetName = this.widget.name;
    let description = this.widget.desc
    let file_loc = widgetName + ".zip";
    const formData = new FormData();
    formData.append('file', this.uploadForm.get('profile').value);
    //console.log("FormData >>>>::> ",formData.get('file'));

    /*if((formData == undefined && !this.isEditMode) ||
      (!this.widget.allUser && this.appCounter == 0) ||
      this.widget.name == null ||
      (!this.widget.allUser && !this.allRoleSelected)){
        console.log("return from 2nd check");
        return;	
    }*/
    
    let selectedRoles = [];

    if(!this.widget.allUser){
      for(var i = 0; i < this.availableApps.length; i++){
        if(this.availableApps[i].isSelected){
          for(var n = 0; n < this.availableApps[i].roles.length; n++) {
            if(this.availableApps[i].roles[n].isSelected){
              var role = {
                    app: {appId: this.availableApps[i].id},
                    roleId: this.availableApps[i].roles[n].id,
                    roleName: this.availableApps[i].roles[n].name,
                };
              selectedRoles.push(role);
            }
          }
        }
      }
    }

    let allowAllUser = 0;
    if(this.widget.allUser){
      allowAllUser = 1;
    }

    let serviceId = null;
    if(this.widget.serviceURL != null &&   this.widget.serviceURL != undefined){
        serviceId = parseInt(this.widget.serviceURL);
    }
    var newWidget = {
        name: widgetName,
        desc: description,
        widgetRoles: selectedRoles,
        fileLocation: file_loc,
        allowAllUser: allowAllUser,
        serviceId: serviceId
    };

    if(this.isEditMode){
      //console.log("widget in updateWidget :::: >>> ",newWidget);
      if(formData && formData.get('file')){
        this.widgetOnboardingService.updateWidgetWithFile(formData, this.widget.id, newWidget)
          .subscribe( _data => {
            this.result = 'updated';
            this.passEntry.emit(this.result);
          }, error => {
            console.log(error);
            this.openConfirmationModal("Error",'Could not update. Please retry.');
        });
      }else{
        this.widgetOnboardingService.updateWidget(this.widget.id, newWidget)
          .subscribe( _data => {
            this.result = 'updated';
            this.passEntry.emit(this.result);
          }, error => {
            this.openConfirmationModal("Error",'Could not update. Please retry.');
            console.log(error);
        });
      }
    }else{
      //console.log("newWidget in createWidget :::: >>> ",newWidget);
      this.widgetOnboardingService.createWidget(newWidget, formData)
        .subscribe( _data => {
          this.result = 'created';
          this.passEntry.emit(this.result);
        }, error => {
          this.openConfirmationModal("Error",'Could not save. Please retry.');
          console.log(error);
      });
    }
    this.ngbModal.dismissAll();
  }

  onFileSelect(event) {
    this.widgetFileTypeError = false;
    this.isFileNotSelected = false;
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      //console.log("onFileSelect called.. ",file);
      var fileName = file.name;
      var validFormats = ['zip'];
      var ext = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
      if(validFormats.indexOf(ext) == -1){
        this.widgetFileTypeError = true;
      }
      this.uploadForm.get('profile').setValue(file);
    }
  }

  appUpdate(){
    this.hasSelectedApp = false;
    this.appCounter = 0;
    for(var i = 0; i < this.availableApps.length; i++){
      if(this.availableApps[i].isSelected){
        this.appCounter++;
        if(!this.hasSelectedApp)
          this.hasSelectedApp = true;
      }
      if(this.availableApps[i].isSelected
      && this.availableApps[i].roles.length == 0){
        var index = i;
        this.availableRoles = [];    
        this.adminsService.getRolesByApp(this.availableApps[i].id)
        .subscribe( roles => {
          if(roles && roles.length >0){
            for(var i = 0; i < roles.length; i++){
              this.availableRoles.push({
                id: roles[i].id,
                      name: roles[i].name,
                      isSelected: false,
              }); 
            }
          }
          this.availableApps[index].roles = this.availableRoles;
        }, error => {
          console.log(error);
        });
      }
    }
    this.allRoleSelected = true;
    this.checkRoleSelected();
  }

  roleUpdate(app){
    this.allRoleSelected = true;
      for(var i = 0; i < app.roles.length; i++){
        if(app.roles[i].isSelected){
          app.roleSelected = true;
          this.checkRoleSelected();
          return;
        }
      }
    app.roleSelected = false;
    this.checkRoleSelected();
  }
    
  checkRoleSelected(){
    for(var i = 0; i < this.availableApps.length; i++){
      if(this.availableApps[i].isSelected
      && !this.availableApps[i].roleSelected){
        this.allRoleSelected = false;
        return;
      }
    }
  }

  getAppName = function(appId){
    for(var i = 0; i < this.availableApps.length; i++){
      if(this.availableApps[i].id == appId){
        return this.availableApps[i].name;
      }
    }
  }

  updateWidgetName(){
    for(var i = 0; i < this.widgetsList.length; i++){
      if(this.widget.name && this.widget.name.toUpperCase() == this.widgetsList[i].name.toUpperCase()){
        this.duplicatedName = false;
        return;
      }
    }
    this.duplicatedName = true;
  }

  getOnboardingWidgets(){
    this.widgetOnboardingService.getManagedWidgets()
      .subscribe(_data => {
          this.result = _data
          if(!(_data instanceof Array)){
           return;
          }
          if (this.result == null || this.result == 'undefined') {
              console.log('WidgetOnboardingService::getOnboardingWidgets Failed: Result or result.data is null');
          }else {
            this.widgetsList = _data;
        }
      }, error =>{
        console.log(error);
    });
  }

  getAvailableApps(){
    if(this.isEditMode == false){	
      this.availableApps=[];
      this.applicationsService.getAppsForSuperAdminAndAccountAdmin()
        .subscribe(apps => {
          this.availableApps=[];
          for(var i=0;i<apps.length;i++) {
            if (!apps[i].restrictedApp) {
              this.availableApps.push({
                  id: apps[i].id,
                  name: apps[i].name,
                  roles: [],
                  roleSelected: false,
                  isSelected: false,
              });
          }
        }
        }, error =>{
          console.log(error);
      });
    }else if(this.isEditMode == true){
      if(this.widget.allowAllUser == "Y"){
        this.widget.allUser = true;
      }
      this.applicationsService.getAppsForSuperAdminAndAccountAdmin()
      .subscribe(apps => {
        this.availableApps=[];
        let selectedApps = {};
        var availableApps = this.availableApps;  
        this.allRoleSelected = true;
        for(var i=0; i < this.widget.widgetRoles.length; i++){
          if(selectedApps[this.widget.widgetRoles[i].app.appId] != undefined)
            selectedApps[this.widget.widgetRoles[i].app.appId] += this.widget.widgetRoles[i].roleId + ";" + this.widget.widgetRoles[i].roleName + ";"; 
          else{
            selectedApps[this.widget.widgetRoles[i].app.appId] = this.widget.widgetRoles[i].roleId + ";" + this.widget.widgetRoles[i].roleName + ";";        		
            this.appCounter++;
          }
        }         		
        apps.forEach(function(app, index){
            availableApps.push({
              id: app.id,
              name: app.name,
              roles: [],
              roleSelected: false,
              isSelected: false,
            });
            if(selectedApps[app.id] != undefined){
              this.adminsService.getRolesByApp(app.id)
              .subscribe(roles => {
                var role = selectedApps[app.id].split(';');
                var selectedRoles = [];
                var n = 0;
                while((n+1) < role.length){
                    selectedRoles.push({
                        id: role[n++],
                        name: role[n++],
                        isSelected: true,
                    });
                }					
                for(var m = 0; m < roles.length; m++){
                  var hasSelected = true;
                  for(var n = 0; n < selectedRoles.length; n++){
                    if(selectedRoles[n].id == roles[m].id){
                      hasSelected = false;
                      break;
                    }
                  }
                  if(hasSelected){
                    selectedRoles.push({
                      id: roles[m].id,
                      name: roles[m].name,
                      isSelected: false,
                    }); 
                  }	   
                }  
                availableApps[index].roleSelected = true;
                availableApps[index].isSelected = true;
                availableApps[index].roles = selectedRoles;
              }, error =>{
                  console.log(error);
              });
            }
        })
      
      }, error =>{
        console.log(error);
      });
    }
    //console.log("this.availableApps :: ",this.availableApps);
  }

  openConfirmationModal(_title: string, _message: string) {
    const modalInfoRef = this.ngbModal.open(ConfirmationModalComponent);
    modalInfoRef.componentInstance.title = _title;
    modalInfoRef.componentInstance.message = _message;
  }

  openInformationModal(_title: string, _message: string){
    const modalInfoRef = this.ngbModal.open(InformationModalComponent);
    modalInfoRef.componentInstance.title = _title;
    modalInfoRef.componentInstance.message = _message;
    return modalInfoRef;
  }
  
}
