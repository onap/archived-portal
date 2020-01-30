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
import { RoleFunction, Role } from 'src/app/shared/model';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { InformationModalComponent } from 'src/app/modals/information-modal/information-modal.component';

@Component({
  selector: 'app-role-function-modal',
  templateUrl: './role-function-modal.component.html',
  styleUrls: ['./role-function-modal.component.scss']
})
export class RoleFunctionModalComponent implements OnInit {

  @Input() title: string;
  @Input() appId: any;
  @Input() dialogState: number;
  @Input() currentRoleFunctions: any;
  @Input() editRoleFunction: RoleFunction;
  @Output() passBackRoleFunctionPopup: EventEmitter<any> = new EventEmitter();
  roleFunction: RoleFunction;
  otherTypeValue: string;
  typeOptions: string[] = ['menu', 'url', 'other'];
  api = environment.api;
  isEditing: any;
  editDisable: boolean;
  showSpinner: boolean;
  selectedType: string;
  createOrUpdate: string;
  constructor(public activeModal: NgbActiveModal, public ngbModal: NgbModal, public http: HttpClient) { }

  ngOnInit() {
    this.createOrUpdate = 'create';
    this.selectedType = 'menu';
    this.roleFunction = new RoleFunction(this.selectedType, '', '*', '');
    this.otherTypeValue = '';
    if (this.editRoleFunction) {
      this.createOrUpdate = 'update';
      this.editDisable = true;
      this.selectedType = this.editRoleFunction.type;
      if (this.editRoleFunction.type !== 'menu' && this.editRoleFunction.type !== 'url') {
        this.selectedType = 'other';
        this.otherTypeValue = this.editRoleFunction.type;
      }
      this.roleFunction = new RoleFunction(this.editRoleFunction.type, this.editRoleFunction.code, this.editRoleFunction.action, this.editRoleFunction.name);
    }
  }

  saveRoleFunction() {
    if (/[^a-zA-Z0-9\-\.\_]/.test(this.roleFunction.type) && this.selectedType === 'other') {
      this.openConfirmationModal('Confirmation', 'Type can only contain alphanumeric characters, dots(.) and underscores(_)');
      return;
    } else {
      this.roleFunction.type = this.selectedType;
    }
    if (this.roleFunction.action !== '*' && /[^a-zA-Z0-9\-\.\_]/.test(this.roleFunction.action)) {
      this.openConfirmationModal('Confirmation', 'Action can only contain alphanumeric characters, hyphens(-), dots(.) and underscores(_) and single asterisk character(*)');
      return;
    }
    if (/[^a-zA-Z0-9\-\:\_\./*]/.test(this.roleFunction.code)) {
      this.openConfirmationModal('Confirmation', 'Instance can only contain alphanumeric characters, hyphens(-), dots(.), colons(:), forwardSlash(/) , asterisk(*) and underscores(_)');
      return;
    }
    const modalInfoRef = this.ngbModal.open(InformationModalComponent);
    modalInfoRef.componentInstance.title = 'Confirmation';
    modalInfoRef.componentInstance.message = 'You are about to ' + this.createOrUpdate + ' the role function ' + this.roleFunction.name + '. Do you want to continue?';
    modalInfoRef.result.then((_res) => {
      if (_res === 'Ok') {
        this.showSpinner = true;
        var uuu = this.api.saveRoleFunction.replace(':appId', this.appId);
        var postData = this.roleFunction;
        var exists = false, x;
        for (x in this.currentRoleFunctions) {
          if (this.currentRoleFunctions[x].type == this.roleFunction.type
            && this.currentRoleFunctions[x].code == this.roleFunction.code
            && this.currentRoleFunctions[x].action == this.roleFunction.action
            && this.currentRoleFunctions[x].name == this.roleFunction.name) {
            this.openConfirmationModal('Confirmation', "Role Function already exist.");
            exists = true;
            this.showSpinner = false;
            break;
          }
          if (!this.editDisable) {
            if (this.currentRoleFunctions[x].type == this.roleFunction.type
              && this.currentRoleFunctions[x].code == this.roleFunction.code
              && this.currentRoleFunctions[x].action == this.roleFunction.action
            ) {
              this.openConfirmationModal('Confirmation', "Please make sure code, type and action is unique. Please create a role function with a different code or type or action to proceed.");
              exists = true;
              this.showSpinner = false;
              break;
            }
          }
        }
        if (this.selectedType === 'other')
          this.roleFunction.type = this.otherTypeValue;
        if (!exists && this.roleFunction.name.trim() != '' && this.roleFunction.code.trim() != '') {
          this.http.post(uuu, JSON.stringify(postData)).toPromise().then((res: any) => {
            if (res.status == 'OK') {
              this.showSpinner = false;
              if (this.editRoleFunction) {
                this.editRoleFunction.name = this.roleFunction.name;
                this.passBackRoleFunctionPopup.emit(this.editRoleFunction);
              } else{
                this.passBackRoleFunctionPopup.emit(this.roleFunction);
              }
              this.openConfirmationModal('Success', res.message);
            } else {
              this.showSpinner = false;
              this.openConfirmationModal('Error', res.message);
            }
          });

        }
      }
    }, (_dismiss) => {

    })
  }


  openConfirmationModal(_title: string, _message: string) {
    const modalInfoRef = this.ngbModal.open(ConfirmationModalComponent);
    modalInfoRef.componentInstance.title = _title;
    modalInfoRef.componentInstance.message = _message;
  }
}
