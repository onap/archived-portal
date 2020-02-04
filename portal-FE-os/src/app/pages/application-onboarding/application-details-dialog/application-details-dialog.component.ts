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
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IApplications } from 'src/app/shared/model/applications-onboarding/applications';
import { ApplicationsService } from 'src/app/shared/services';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { InformationModalComponent } from 'src/app/modals/information-modal/information-modal.component';

@Component({
  selector: 'app-application-details-dialog',
  templateUrl: './application-details-dialog.component.html',
  styleUrls: ['./application-details-dialog.component.scss'],
})
export class ApplicationDetailsDialogComponent implements OnInit {

  emptyImg = null;
  emptyImgForPreview:string;
  conflictMessages = {};
  result: any;
  isEditMode: boolean = false;
  appImageTypeError: boolean = false;
  isSaving: boolean = false;
  originalImage: any;
  ECOMP_URL_REGEX = "/^((?:https?\:\/\/|ftp?\:\/\/)?(w{3}.)?(?:[-a-z0-9]+\.)*[-a-z0-9]+.*)[^-_.]$/i";

  constructor(public activeModal: NgbActiveModal, public ngbModal: NgbModal,
    public applicationsService : ApplicationsService) { }

  @Input() applicationObj: IApplications;
  @Output() passEntry: EventEmitter<any> = new EventEmitter();

  newAppModel = {
    'id': null,
    'name': null,
    'imageUrl': null,
    'description': null,
    'notes': null,
    'url': null,
    'alternateUrl': null,
    'restUrl': null,
    'isOpen': false,
    'username': null,
    'appPassword': null,
    'thumbnail': this.emptyImg,
    'isEnabled': false,
    'restrictedApp': false,
    'nameSpace':null,
    'isCentralAuth': false,
    'uebTopicName':null,
    'uebKey': null,
    'uebSecret': null,
    'imageLink': null
  };


  ngOnInit() {
    if(this.applicationObj.id){
      this.isEditMode = true;
    }else{
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
    if(this.applicationObj.isCentralAuth){
        //if valid.
        if(!this.applicationObj.isEnabled){
          if(((this.applicationObj.name == 'undefined' || !this.applicationObj.name)||(this.applicationObj.nameSpace == 'undefined'
          || !this.applicationObj.nameSpace) ||(this.applicationObj.username == 'undefined' || !this.applicationObj.username))) {
            this.openConfirmationModal('','Please fill in all required fields(*) for centralized application');
            return;
          }
        }
        if(this.applicationObj.isEnabled){
          if(((this.applicationObj.name == 'undefined' || !this.applicationObj.name)
          ||(this.applicationObj.url == 'undefined'|| !this.applicationObj.url)
          ||(this.applicationObj.username == 'undefined' || !this.applicationObj.username)||(this.applicationObj.nameSpace == 'undefined'
          || !this.applicationObj.nameSpace))) {

            this.openConfirmationModal('','Please fill in all required fields(*) for centralized active application');
            return;
          }
        }
    }else{
        if(!this.applicationObj.isEnabled) {
            if((this.applicationObj.name == 'undefined' || !this.applicationObj.name)){
                this.openConfirmationModal('','Please fill in all required field(*) ApplicationName to Save the applictaion');
                return;
            }
        }else if(this.applicationObj.isEnabled && !this.applicationObj.restrictedApp){
          if((this.applicationObj.name == 'undefined' || !this.applicationObj.name)
            ||(this.applicationObj.url == 'undefined'|| !this.applicationObj.url)
            ||(this.applicationObj.username == 'undefined' || !this.applicationObj.username)||
            (this.applicationObj.appPassword== 'undefined' || !this.applicationObj.appPassword)) {

              this.openConfirmationModal('','Please fill in all required fields(*) along with password as the app is not centralized');
              return;
          }
        }else if(this.applicationObj.isEnabled && this.applicationObj.restrictedApp){
            if((this.applicationObj.name == 'undefined' || !this.applicationObj.name) ||(this.applicationObj.url == 'undefined'
            || !this.applicationObj.url)){
                this.openConfirmationModal('','Please fill in all required fields(*)');
                return;
            }
        }
    }

    //URL Validation
    if(this.applicationObj.isEnabled){
      if(this.applicationObj.url && this.applicationObj.url !='undefined' && this.applicationObj.url != ''){
        let isValidURL = this.isUrlValid(this.applicationObj.url);
        if(!isValidURL){
          this.openConfirmationModal('Error','Application URL must be a valid URL.');
          return;
        }
      }else{
        this.openConfirmationModal('Error','Application URL is required.');
        return;
      }
    }


    this.isSaving = true;
    // For a restricted app, null out all irrelevant fields
    if(this.applicationObj.restrictedApp) {
      this.newAppModel.restUrl = null;
      this.newAppModel.isOpen = true;
      this.newAppModel.username = null;
      this.newAppModel.appPassword = null;
      this.newAppModel.uebTopicName = null;
      this.newAppModel.uebKey = null;
      this.newAppModel.uebSecret = null;

      /**Need to set below fields values based on input provided in the dialog */
      this.newAppModel.restrictedApp = this.applicationObj.restrictedApp;
      this.newAppModel.name = this.applicationObj.name;
      this.newAppModel.url = this.applicationObj.url;
      if(this.applicationObj.isEnabled){
        this.newAppModel.isEnabled = this.applicationObj.isEnabled;
      }else{
        this.newAppModel.isEnabled = false;
      }

    }else{

       /**Need to set below fields values based on input provided in the dialog */
       this.newAppModel.restrictedApp = false;
       this.newAppModel.name = this.applicationObj.name;
       this.newAppModel.url = this.applicationObj.url;
       this.newAppModel.restUrl = this.applicationObj.restUrl;
       this.newAppModel.username = this.applicationObj.username;
       this.newAppModel.appPassword = this.applicationObj.appPassword;

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

       if(this.applicationObj.isCentralAuth){
        this.newAppModel.isCentralAuth = this.applicationObj.isCentralAuth;
       }else{
        this.newAppModel.isCentralAuth = false;
       }

    }

    if (this.applicationObj.nameSpace=="") {
      this.newAppModel.nameSpace = null;
    }

    if(this.isEditMode){
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
            'The Application Name and URL should  be unique.  Error: ' +
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
