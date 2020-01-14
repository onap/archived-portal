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
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { BasicAuthAccountService } from 'src/app/shared/services';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';

@Component({
  selector: 'app-account-add-details',
  templateUrl: './account-add-details.component.html',
  styleUrls: ['./account-add-details.component.scss']
})
export class AccountAddDetailsComponent implements OnInit {

  result: any;
  isEditMode: boolean = false;
  dupliateName: boolean = false;
  emptyAccountName: boolean = false;
  emptyAccountUsername: boolean = false;
  passwordMatched: boolean = false;

  @Input() accountOnboarding: any;
  @Output() passEntry: EventEmitter<any> = new EventEmitter();

  constructor(public basicAuthAccountService: BasicAuthAccountService, public activeModal: NgbActiveModal, public ngbModal: NgbModal) { }

  ngOnInit() {
    this.passwordMatched = true;
    this.dupliateName = false;
    this.emptyAccountName = false;
    this.emptyAccountUsername = false;
  
    if(this.accountOnboarding.applicationName){
      this.isEditMode = true;
    }else{
      this.isEditMode = false;
      this.accountOnboarding.isActive = true;
      this.accountOnboarding.endpointList = [];
    }
    //console.log("IsEditMode in Add account Dialog :: ",this.isEditMode)

  }

  addEndpoint(){
    const modalRef = this.ngbModal.open(ConfirmationModalComponent);
    modalRef.componentInstance.title = "";
    modalRef.componentInstance.message = 'Please add the roles to this Username/MechId through AAF Screen';
    modalRef.result.then((result) => { }, (resut) => {return;});

    /*this.accountOnboarding.endpointList.push({
      valid: true
    });*/
  }

  //Add Or Update Account.
  saveChanges(){
    var isValid = true;
    //console.log("saveChanges called Account Onboarding");

    if(this.accountOnboarding.applicationName == ''
      || this.accountOnboarding.applicationName == undefined){
      this.emptyAccountName = true;
        isValid = false;
      }
    
    if(this.accountOnboarding.username == ''
      || this.accountOnboarding.username == undefined){
      this.emptyAccountUsername = true;
          isValid = false;
    }
    
    if(this.dupliateName == true){
          isValid = false;
    }

    if(this.dupliateName == true){
      isValid = false;
    }
 
    if(this.accountOnboarding.password != this.accountOnboarding.repassword){
        this.passwordMatched =  false;
        isValid = false;
    }
    //console.log("isValid....",isValid)
    if(!isValid)
      return;
    
    var active = 'N';
    if(this.accountOnboarding.isActive == true)
      active = 'Y';

    var newAccount = {
      applicationName: this.accountOnboarding.applicationName,
      username: this.accountOnboarding.username,
      password: this.accountOnboarding.password,
      endpoints: this.accountOnboarding.endpoints,
      isActive: active
    };

    if(this.isEditMode){
      var message = "Are you sure you want to change '" + this.accountOnboarding.applicationName + "'?"
      this.basicAuthAccountService.updateAccount(this.accountOnboarding.id, newAccount)
        .subscribe( _data => {
          this.result = _data;
          //console.log("updateAccount response :: ",this.result);
          this.passEntry.emit(this.result);
          this.ngbModal.dismissAll();
        }, error => {
          console.log(error);
      });
    }else{
      this.basicAuthAccountService.createAccount(newAccount)
        .subscribe( _data => {
          this.result = _data;
          //console.log("createAccount response :: ",this.result);
          this.passEntry.emit(this.result);
          this.ngbModal.dismissAll();
        }, error => {
          console.log(error);
      });
    }
  }
}
