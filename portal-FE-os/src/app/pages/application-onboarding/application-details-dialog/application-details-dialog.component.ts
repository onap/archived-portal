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

import { Component, OnInit, Input, Output, EventEmitter, ViewChild, ElementRef, Inject, PLATFORM_ID, Injector } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IApplications } from 'src/app/shared/model/applications-onboarding/applications';
import { ApplicationsService } from 'src/app/shared/services';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { InformationModalComponent } from 'src/app/modals/information-modal/information-modal.component';
import { isPlatformBrowser } from '@angular/common';

@Component({
  selector: 'app-application-details-dialog',
  templateUrl: './application-details-dialog.component.html',
  styleUrls: ['./application-details-dialog.component.scss'],
})
export class ApplicationDetailsDialogComponent implements OnInit {

  addEditAction: any;
  emptyImg = null;
  emptyImgForPreview:string;
  conflictMessages = {};
  result: any;
  isEditMode: boolean = false;
  appImageTypeError: boolean = false;
  isSaving: boolean = false;
  originalImage: any;
  ECOMP_URL_REGEX = "/^((?:https?\:\/\/|ftp?\:\/\/)?(w{3}.)?(?:[-a-z0-9]+\.)*[-a-z0-9]+.*)[^-_.]$/i";
  private ngbModal:NgbModal;

  constructor(public activeModal: NgbActiveModal,
    public applicationsService : ApplicationsService,@Inject(PLATFORM_ID)private platformId:Object,private injector:Injector) {
      if(isPlatformBrowser(this.platformId))
      {
        this.ngbModal = this.injector.get(NgbModal);
      }
     }

  @Input() applicationObj: IApplications;
  @Input() action: any;
  @Output() passEntry: EventEmitter<any> = new EventEmitter();
  @ViewChild('applicationName') applicationNameElement: ElementRef;

  newAppModel = {
    'id': null,
    'appName': null,
    'imageUrl': null,
    'appDescription': null,
    'appNotes': null,
    'landingPage': null,
    'alternateUrl': null,
    'restUrl': null,
    'isOpen': false,
    'appBasicAuthUsername': null,
    'appBasicAuthPassword': null,
    'thumbnail': this.emptyImg,
    'isEnabled': false,
    'applicationType': null,
    'rolesInAAF': false,
    'nameSpace': null,
    'uebTopicName': null,
    'uebKey': null,
    'uebSecret': null,
    'imageLink': null,
    'usesCadi': true,
    'modeOfIntegration': null,
    'appAck': false,
    'restrictedApp': false
  };

  applicationTypeArray: any[] = [
    { name: 'GUI Application', value: "1" },
    { name: 'HyperLink Application', value: "2" },
    { name: 'Non-GUI Application', value: "3" }

  ];

  rolesManagementType: any[] = [
    { name: 'Roles in Application (Non-Centralized)', value: false },
    { name: 'Roles in AAF (Centralized)', value: true }
  ];

  modeOfIntegration: any[] = [
    { name: 'Portal SDK Based', value: 'sdk' },
    { name: 'Framework Based', value: 'fw' }
  ]

  ngOnInit() {

    this.addEditAction = this.action;


    if (this.action === 'add') {
      this.applicationObj.applicationType = "1";
      if (this.applicationObj.applicationType == '1') {
        this.applicationObj.modeOfIntegration = this.modeOfIntegration[0].value;
      }
      this.applicationObj.rolesInAAF = true;
      this.applicationObj.usesCadi = true;
      this.applicationObj.appAck = false;
      console.log("Action : ", this.action);
      console.log("Focus : ", this.applicationNameElement.nativeElement);
      setTimeout(() => { // this will make the execution after the above boolean has changed
        this.applicationNameElement.nativeElement.focus();
      }, 0);
    }

    if (this.applicationObj.id) {
      this.isEditMode = true;
    } else {
      this.isEditMode = false;
    }
    //console.log("isEditMode :: ",this.isEditMode);
    this.originalImage = null
    this.emptyImgForPreview = '../../../assets/images/default_app_image.gif';
  }

  appImageHandler(event: any){
    var reader = new FileReader();
    if(event.target.files && event.target.files[0]){
      reader.readAsDataURL(event.target.files[0]); // read file as data url
      var fileName = event.target.files[0].name;
      var validFormats = ['jpg', 'jpeg', 'bmp', 'gif', 'png'];
      //Get file extension
      var ext = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
      //console.log("fileName::>>",fileName ,ext)
      //console.log("fileExtetion::>>",ext)
      //Check for valid format
      if(validFormats.indexOf(ext) == -1){
          this.newAppModel.thumbnail = this.emptyImg;
          this.originalImage = null;
          this.applicationObj.imageUrl = null;
          this.applicationObj.imageLink = null;
          this.applicationObj.thumbnail = null;
          if(!this.isEditMode){
            this.newAppModel.imageUrl = null;
            this.newAppModel.imageLink = null;
            this.newAppModel.thumbnail = null;
          }
          this.appImageTypeError=true;
      }else{
        reader.onload = (event: any) => { // called once readAsDataURL is completed
          this.applicationObj.imageLink = event.target.result;
          this.applicationObj.imageUrl = event.target.result;
          this.applicationObj.thumbnail = event.target.result;
          this.originalImage =  event.target.result;
          if(!this.isEditMode){
            this.newAppModel.imageLink = event.target.result;
            this.newAppModel.imageUrl = event.target.result;
            this.newAppModel.thumbnail = event.target.result;
            this.originalImage =  event.target.result;
          }
        }
      }
    }
  }

  removeImage(){
    let confirmationMsg = 'Are you sure you want to remove the image?';
    this.openInformationModal("Confirmation",confirmationMsg).result.then((result) => {
      if (result === 'Ok') {
        //this.imageApi.clearFile();
        this.applicationObj.thumbnail = this.emptyImg;
        this.originalImage = null;
        this.applicationObj.imageUrl = null;
        this.applicationObj.imageLink = null;
        this.emptyImgForPreview = '../../../assets/images/default_app_image.gif';
      }
    }, (resut) => {
      return;
    })
  }

  /** Add/Edit Application Method*/
  saveChanges() {
    //console.log("addNewApplication getting called..");
    if (this.applicationObj.rolesInAAF) {
      //if valid.
      if (this.applicationObj.applicationType == "1") {
        console.log("Gui Application valodations");
        //if valid.
        if (!this.applicationObj.isEnabled) {
          if (((this.applicationObj.appName == 'undefined' || !this.applicationObj.appName) || (this.applicationObj.nameSpace == 'undefined'
            || !this.applicationObj.nameSpace) || (this.applicationObj.appBasicAuthUsername == 'undefined' || !this.applicationObj.appBasicAuthUsername))) {
            this.openConfirmationModal('', 'Please fill in all required fields(*) for centralized application');
            return;
          }
        }
        if (this.applicationObj.isEnabled) {
          if (((this.applicationObj.appName == 'undefined' || !this.applicationObj.appName)
            || (this.applicationObj.landingPage == 'undefined' || !this.applicationObj.landingPage)
            || (this.applicationObj.appBasicAuthUsername == 'undefined' || !this.applicationObj.appBasicAuthUsername) || (this.applicationObj.nameSpace == 'undefined'
              || !this.applicationObj.nameSpace))) {

            this.openConfirmationModal('', 'Please fill in all required fields(*) for centralized active application');
            return;
          }
        }
      } else if (this.applicationObj.applicationType == "3") {
        console.log("Non-Gui Application valodations");
        if (!this.applicationObj.isEnabled) {
          if (((this.applicationObj.appName == 'undefined' || !this.applicationObj.appName) || (this.applicationObj.nameSpace == 'undefined'
            || !this.applicationObj.nameSpace) || (this.applicationObj.appBasicAuthUsername == 'undefined' || !this.applicationObj.appBasicAuthUsername))) {
            this.openConfirmationModal('', 'Please fill in all required fields(*) for centralized application');
            return;
          }
        }

        if (this.applicationObj.isEnabled) {
          if (((this.applicationObj.appName == 'undefined' || !this.applicationObj.appName)
            || (this.applicationObj.appBasicAuthUsername == 'undefined' || !this.applicationObj.appBasicAuthUsername) || (this.applicationObj.nameSpace == 'undefined'
              || !this.applicationObj.nameSpace))) {

            this.openConfirmationModal('', 'Please fill in all required fields(*) for centralized active application');
            return;
          }
        }
      }
    } else {
      console.log("Non-centralized applications validation");
      this.applicationObj.appAck = null;
      if (!this.applicationObj.isEnabled) {
        if ((this.applicationObj.appName == 'undefined' || !this.applicationObj.appName)) {
          this.openConfirmationModal('', 'Please fill in all required field(*) ApplicationName to Save the applictaion');
          return;
        }
      } else if (this.applicationObj.isEnabled && (this.applicationObj.applicationType == "1")) {
        if ((this.applicationObj.appName == 'undefined' || !this.applicationObj.appName)
          || (this.applicationObj.landingPage == 'undefined' || !this.applicationObj.landingPage)
          || (this.applicationObj.appBasicAuthUsername == 'undefined' || !this.applicationObj.appBasicAuthUsername) ||
          (this.applicationObj.appBasicAuthPassword == 'undefined' || !this.applicationObj.appBasicAuthPassword)) {

          this.openConfirmationModal('', 'Please fill in all required fields(*) along with password as the app is not centralized');
          return;
        }
      } else if (this.applicationObj.isEnabled && (this.applicationObj.applicationType == "3")) {
        console.log("Non gui validation");
        if ((this.applicationObj.appName == 'undefined' || !this.applicationObj.appName)
          || (this.applicationObj.appBasicAuthUsername == 'undefined' || !this.applicationObj.appBasicAuthUsername)) {
          this.openConfirmationModal('', 'Please fill in all required fields(*)');
          return;
        }
      } else if (this.applicationObj.isEnabled && (this.applicationObj.applicationType == "2")) {
        if ((this.applicationObj.appName == 'undefined' || !this.applicationObj.appName) || (this.applicationObj.landingPage == 'undefined'
          || !this.applicationObj.landingPage)) {
          this.openConfirmationModal('', 'Please fill in all required fields(*)');
          return;
        }
      }
    }

    //URL Validation
    if (this.applicationObj.isEnabled && this.applicationObj.applicationType == "1") {
      if (this.applicationObj.landingPage && this.applicationObj.landingPage != 'undefined' && this.applicationObj.landingPage != '') {
        let isValidURL = this.isUrlValid(this.applicationObj.landingPage);
        if (!isValidURL) {
          this.openConfirmationModal('Error', 'Application URL must be a valid URL.');
          return;
        }
      } else {
        this.openConfirmationModal('Error', 'Application URL is required.');
        return;
      }
    }


    this.isSaving = true;
    // For a restricted app, null out all irrelevant fields
    if (this.applicationObj.applicationType == "2") {
      console.log("Hyperlinka pplication validation");
      this.newAppModel.restUrl = null;
      this.newAppModel.isOpen = true;
      this.newAppModel.appBasicAuthUsername = null;
      this.newAppModel.appBasicAuthPassword = null;
      this.newAppModel.uebTopicName = null;
      this.newAppModel.uebKey = null;
      this.newAppModel.uebSecret = null;
      this.newAppModel.restrictedApp = true;

      /**Need to set below fields values based on input provided in the dialog */
      this.newAppModel.applicationType = this.applicationObj.applicationType;
      this.newAppModel.appName = this.applicationObj.appName;
      this.newAppModel.landingPage = this.applicationObj.landingPage;
      this.newAppModel.usesCadi = this.applicationObj.usesCadi;
      if (this.applicationObj.isEnabled) {
        this.newAppModel.isEnabled = this.applicationObj.isEnabled;
      }else{
        this.newAppModel.isEnabled = false;
      }
      console.log("New Model : ", this.newAppModel);
    } else {

      /**Need to set below fields values based on input provided in the dialog */
      this.newAppModel.applicationType = this.applicationObj.applicationType;
      this.newAppModel.appName = this.applicationObj.appName;
      this.newAppModel.landingPage = this.applicationObj.landingPage;
      this.newAppModel.restUrl = this.applicationObj.restUrl;
      this.newAppModel.appBasicAuthUsername = this.applicationObj.appBasicAuthUsername;
      this.newAppModel.appBasicAuthPassword = this.applicationObj.appBasicAuthPassword;
      this.newAppModel.modeOfIntegration = this.applicationObj.modeOfIntegration;
      this.newAppModel.usesCadi = this.applicationObj.usesCadi;
      this.newAppModel.appAck = this.applicationObj.appAck;

       if(this.applicationObj.isEnabled){
        this.newAppModel.isEnabled = this.applicationObj.isEnabled;
       }else{
        this.newAppModel.isEnabled = false;
       }

       if(this.applicationObj.isOpen){
        this.newAppModel.isOpen = this.applicationObj.isOpen;
       }else{
        this.newAppModel.isOpen = false;
       }
       //console.log("this.applicationObj.isOpen",this.applicationObj.isOpen);

      if (this.applicationObj.rolesInAAF) {
        this.newAppModel.rolesInAAF = this.applicationObj.rolesInAAF;
      } else {
        this.newAppModel.rolesInAAF = false;
        this.newAppModel.usesCadi = false;
      }

    }

    if (this.applicationObj.nameSpace=="") {
      this.newAppModel.nameSpace = null;
    }else{
      this.newAppModel.nameSpace = this.applicationObj.nameSpace;
    }

    if (this.applicationObj.applicationType == "2" || this.applicationObj.applicationType == "3") {
      this.applicationObj.modeOfIntegration = null;
    }

    if (this.newAppModel.applicationType == "2" || this.newAppModel.applicationType == "3") {
      this.newAppModel.modeOfIntegration = null;
    }

    if (this.isEditMode) {
      console.log("Edit application Object : ", JSON.stringify(this.applicationObj));
      console.log("Mode Of iNtegration : ", this.applicationObj.modeOfIntegration);
      this.applicationsService.updateOnboardingApp(this.applicationObj)
        .subscribe( _data => {
          this.result = _data;
          //console.log("update application response :: ",this.result);
          if(this.result !=null && this.result.httpStatusCode ==200){
            this.passEntry.emit(this.result);
            this.ngbModal.dismissAll();
          }else{
            this.openConfirmationModal('Error','There was a problem updating the application changes. Please try again. If the problem persists, then try again later. Error: '+this.result);
            return
          }
        }, error => {
          console.log(error);
          if(error.status == 409){
            this.openConfirmationModal('Error', 'There was a problem updating the application changes. ' +
            'The Application Name and Namespace should  be unique.  Error: ' +
            error.status);
            return;
          }else if(error.status == 500){
            this.openConfirmationModal('Error', 'There was a problem updating the application information. ' +
            'Please try again later. Error: ' +error.status);
            return;
          }else if(error.status == 404 || error.status == 403){
            this.openConfirmationModal('Error', 'There was a problem updating the application information. ' +
            'Invalid namespace. Error: ' +error.status);
            return;
          }else if(error.status == 401){
            this.openConfirmationModal('Error', 'There was a problem updating the application information. ' +
            'Portal Mechid is unauthorized to access the given namespace. Error: ' +error.status);
            return;
          }else if(error.status == 400){
            this.openConfirmationModal('Error','Bad Request Error: ' + error.status);
            return;
          } else{
            this.openConfirmationModal('Error', 'There was a problem updating the application changes. ' +
            'Please try again. If the problem persists, then try again later. Error: ' +
            error.status);
            return;
          }
      });

    }else{
      //console.log("Coming inside add application",this.newAppModel);

      this.applicationsService.addOnboardingApp(this.newAppModel)
        .subscribe( _data => {
          this.result = _data;
          //console.log("Add application response :: ",this.result);
          if(this.result !=null && this.result.httpStatusCode ==200){
            this.passEntry.emit(this.result);
            this.ngbModal.dismissAll();
          }else{
            this.openConfirmationModal('Error','There was a problem adding the application changes. Please try again. If the problem persists, then try again later. Error: '+this.result);
            return
          }
        }, error => {
          console.log(error);
          if(error.status == 409){
            this.openConfirmationModal('Error', 'There was a problem adding the application changes. ' +
            'The Application Name and URL should  be unique.  Error: ' +
            error.status);
            return;
          } else if(error.status == 500){
            this.openConfirmationModal('Error', 'There was a problem adding the application information. ' +
            'Please try again later. Error: ' +error.status);
            return;
          }else if(error.status == 400){
            this.openConfirmationModal('Error','Bad Request Error: ' + error.status);
            return;
          } else{
            this.openConfirmationModal('Error', 'There was a problem adding the application changes. ' +
            'Please try again. If the problem persists, then try again later. Error: ' +
            error.status);
            return;
          }
      });
    }
  }

  isUrlValid(userInput){
    //let  regexQuery = "/^((?:https?\:\/\/|ftp?\:\/\/)?(w{3}.)?(?:[-a-z0-9]+\.)*[-a-z0-9]+.*)[^-_.]$/i";
    let  regexQuery = "https?://.+";
    let res = userInput.match(regexQuery);
    if(res == null){
      return false;
    }else{
      return true;
    }
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
