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
import { RoleService } from 'src/app/shared/services';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { Role } from 'src/app/shared/model';
import { MatTableDataSource } from '@angular/material';
import { InformationModalComponent } from 'src/app/modals/information-modal/information-modal.component';

@Component({
  selector: 'app-add-role',
  templateUrl: './add-role.component.html',
  styleUrls: ['./add-role.component.scss']
})
export class AddRoleComponent implements OnInit {

  @Input() title: string;
  @Input() appId: string;
  @Input() dialogState: number;
  @Input() availableRole: any;
  @Input() appRoleFunctions: any;
  @Output() passBackAddRolePopup: EventEmitter<any> = new EventEmitter();
  availableRoleFunctions: any;
  isGlobalRoleChecked = {
    isChecked: false
  }
  role: Role;
  roleFunctions: any;
  showGlobalRole: boolean;
  api = environment.api;
  showSpinner: boolean;
  displayedColumns: string[] = ['active', 'name'];
  roleFunctionsDataSource = new MatTableDataSource(this.roleFunctions);
  finalSelectedRoleFunctions: any;
  constructor(public activeModal: NgbActiveModal, public ngbModal: NgbModal, private roleService: RoleService, public http: HttpClient) { }

  ngOnInit() {
    this.role = new Role;
    this.finalSelectedRoleFunctions = [];
    if (this.appId == '1')
      this.showGlobalRole = true;
    if (this.dialogState === 2) {
      this.isGlobalRoleChecked.isChecked = (this.availableRole.name.includes('global_')) ? true : false;
      this.availableRoleFunctions = [];
      this.role = this.availableRole;
      this.roleFunctionsDataSource = new MatTableDataSource(this.setSelectedRoleFucntions());
    }
  }

  setSelectedRoleFucntions() {
    for (var i = 0; i < this.appRoleFunctions.length; i++) {
      var availableRoleFunction = this.appRoleFunctions[i];
      availableRoleFunction['selected'] = false;
      for (var j = 0; j < this.availableRole.roleFunctions.length; j++) {
        if (availableRoleFunction.code === this.availableRole.roleFunctions[j].code
          && availableRoleFunction.type === this.availableRole.roleFunctions[j].type
          && availableRoleFunction.action === this.availableRole.roleFunctions[j].action) {
          availableRoleFunction.selected = true;
          console.log(availableRoleFunction.selected);
        }
      }
      this.availableRoleFunctions.push(availableRoleFunction);
    }
    return this.availableRoleFunctions;
  }

  toggleRoleFunction(_element) {
    if (this.appRoleFunctions) {
      for (var i = 0; i < this.appRoleFunctions.length; i++) {
        var availableRoleFunction = this.appRoleFunctions[i];
        if (availableRoleFunction.selected && !this.finalSelectedRoleFunctions.includes(availableRoleFunction)) {
          this.finalSelectedRoleFunctions.push(availableRoleFunction);
        }
      }
    }
    if (!_element.selected) {
      for (var i = 0; i < this.finalSelectedRoleFunctions.length; i++) {
        var availableRoleFunction = this.finalSelectedRoleFunctions[i];
        if (availableRoleFunction.code == _element.code
          && availableRoleFunction.type == _element.type
          && availableRoleFunction.action == _element.action) {
          this.finalSelectedRoleFunctions.splice(i, 1);
        }
      }
    }
  }



  saveRole() {
    var uuu = this.api.saveRole.replace(':appId', this.appId);
    if (this.isGlobalRoleChecked.isChecked) {
      this.role.name = (this.role.name.indexOf('global_') == -1) ? ('global_' + this.role.name) : (this.role.name);
      this.saveOrUpdateRole(uuu);
    } else {
      var roleName = this.role.name.toLowerCase();
      if (roleName.includes('global_')) {
        const modalInfoRef = this.ngbModal.open(ConfirmationModalComponent);
        modalInfoRef.componentInstance.title = 'Confirmation';
        modalInfoRef.componentInstance.message = 'Global prefix:"global_" can only be used when the global flag is checked for the role name:' + this.role.name + '. Please try again!';
      } else {
        this.role.childRoles = [];
        this.role.roleFunctions = [];
        this.saveOrUpdateRole(uuu);
      }
    }
  }

  saveOrUpdateRole(uuu) {
    var confirmMessage = (this.dialogState === 2) ? 'You are about to update the role/role functions. Do you want to continue?' : 'You are about to create the role `' + this.role.name + '` . Do you want to continue?';
    const modalInfoRef = this.ngbModal.open(InformationModalComponent);
    modalInfoRef.componentInstance.title = 'Confirmation';
    modalInfoRef.componentInstance.message = confirmMessage;
    modalInfoRef.result.then((_res) => {
      if (_res === 'Ok') {
        //overriding the final list of rolefunctions to role
        if (this.finalSelectedRoleFunctions.length > 0)
          this.role.roleFunctions = this.finalSelectedRoleFunctions;
        var postData = {
          role: this.role,
          childRoles: this.role.childRoles,
          roleFunctions: this.role.roleFunctions
        };
        this.showSpinner = true
        this.http.post(uuu, postData).toPromise().then((res: any) => {
          this.showSpinner = false;
          if (res && res.role) {
            const modalInfoRef = this.ngbModal.open(ConfirmationModalComponent);
            modalInfoRef.componentInstance.title = 'Success';
            modalInfoRef.componentInstance.message = 'Update Successful.';
            this.passBackAddRolePopup.emit(this.appId);

          }
          else {
            const modalInfoRef = this.ngbModal.open(ConfirmationModalComponent);
            modalInfoRef.componentInstance.title = 'Error';
            modalInfoRef.componentInstance.message = res.error;
          }
        }, (res: HttpErrorResponse) => {
          this.showSpinner = false;
          const modalInfoRef = this.ngbModal.open(ConfirmationModalComponent);
          modalInfoRef.componentInstance.title = 'Error';
          modalInfoRef.componentInstance.message = 'Error while saving.' + res.status;
        }
        );
      }
    }, (_dismiss) => {

    })
  }

}
