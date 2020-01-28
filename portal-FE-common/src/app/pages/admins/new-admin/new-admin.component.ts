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
import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { AdminsService } from 'src/app/shared/services';
import { HttpErrorResponse } from '@angular/common/http';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { PortalAdmin } from 'src/app/shared/model';
import { MatTableDataSource } from '@angular/material';
import { InformationModalComponent } from 'src/app/modals/information-modal/information-modal.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-new-admin',
  templateUrl: './new-admin.component.html',
  styleUrls: ['./new-admin.component.scss']
})
export class NewAdminComponent implements OnInit {

  @Input() dialogState: number;
  @Input() userTitle: string;
  @Input() disableBack: boolean;
  @Input() adminModalData: any;
  @Output() passBackNewAdminPopup: EventEmitter<any> = new EventEmitter();
  searchTitleText = 'Enter First Name, Last Name or Org User Id';
  placeholderText = 'Search';
  changedSelectedUser: PortalAdmin;
  adminAppsRoles: any;
  adminDropdownApps: any;
  isLoading: boolean;
  newAppSelected: boolean;
  adminAppSelectAndUnselectData: any;
  displayedColumns: string[] = ['applications'];
  adminsAppsSource = new MatTableDataSource(this.adminAppsRoles);
  constructor(public router: Router, private adminsService: AdminsService, public ngModal: NgbModal, public activeModal: NgbActiveModal) { }

  ngOnInit() {
    this.adminAppsRoles = [];
    this.changedSelectedUser = null;
    if (this.disableBack){
      this.changedSelectedUser = this.adminModalData;
      this.getAdminAppsRoles();
    }
    this.adminDropdownApps = [];
    this.adminAppSelectAndUnselectData = [];
  }

  changeSelectedUser(user: PortalAdmin) {
    this.changedSelectedUser = user;
    this.userTitle = `${this.changedSelectedUser.firstName}, ` + ` ${this.changedSelectedUser.lastName} ` + ` (${this.changedSelectedUser.orgUserId})`;
  }

  getAdminAppsRoles() {
    this.isLoading = true;
    this.adminsService.getAdminAppsRoles(this.changedSelectedUser.orgUserId).subscribe((_res: any) => {
      JSON.stringify(_res);
      if (!_res.appsRoles) {
        return;
      }
      this.adminAppsRoles = [];
      for (var i = 0; i < _res.appsRoles.length; i++) {
        if (!_res.appsRoles[i].restrictedApp && _res.appsRoles[i].isAdmin) {
          this.adminAppsRoles.push({
            id: _res.appsRoles[i].id,
            appName: _res.appsRoles[i].appName,
            isAdmin: _res.appsRoles[i].isAdmin,
            restrictedApp: _res.appsRoles[i].restrictedApp
          });
        } else if (!_res.appsRoles[i].restrictedApp) {
          this.adminDropdownApps.push({
            id: _res.appsRoles[i].id,
            appName: _res.appsRoles[i].appName,
            isAdmin: _res.appsRoles[i].isAdmin,
            restrictedApp: _res.appsRoles[i].restrictedApp
          });
        }
      }
      this.isLoading = false;
      this.newAppSelected = false;
      this.dialogState = 2;
      this.adminsAppsSource = new MatTableDataSource(this.adminAppsRoles);
    });
  }

  navigateBack() {
    this.dialogState = 1;
  }

  removeAdminApp(app: any) {
    const modalRef = this.ngModal.open(InformationModalComponent);
    modalRef.componentInstance.title = "Confirmation";
    modalRef.componentInstance.message = `Are you sure you want to delete ${app.appName}?`;
    modalRef.result.then((result) => {
      if (result === 'Ok') {
        this.adminAppsRoles.forEach((item: any, index: any) => {
          if (item === app) this.adminAppsRoles.splice(index, 1);
        });
        //call from delete admin app
        this.updateDropdown(app, false);
        this.adminsAppsSource = new MatTableDataSource(this.adminAppsRoles);
      }
    }, (resut) => {
      return;
    })
  }

  updateDropdown(_newValue: any, isDropdownCall: boolean) {
    // app is selected from dropdown
    if (isDropdownCall) {
      this.adminDropdownApps.forEach((item: any, index: any) => {
        if (item === _newValue) this.adminDropdownApps.splice(index, 1);
      });
      this.getadminAppSelectAndUnselectedData(_newValue);
      _newValue.isAdmin = true;
      this.adminAppsRoles.push(_newValue);
      this.adminsAppsSource = new MatTableDataSource(this.adminAppsRoles);
    } else {    // app is removed from the admin list
      this.getadminAppSelectAndUnselectedData(_newValue);
      _newValue.isAdmin = false;
      this.adminDropdownApps.push(_newValue);
    }

    // disable save button if nothing new in the admin list
    if (this.adminAppSelectAndUnselectData.length > 0)
      this.newAppSelected = true;
    else
      this.newAppSelected = false;

  }

  private getadminAppSelectAndUnselectedData(_newValue: any) {
    const index: number = this.adminAppSelectAndUnselectData.indexOf(_newValue);
    if (index !== -1) {
      this.adminAppSelectAndUnselectData.splice(index, 1); // if found, remove selected app from dropdown in the list
    }
    else {
      this.adminAppSelectAndUnselectData.push(_newValue);
    }
  }

  remindToAddUserIfNecessary() {
    let adminAddedToNewApp = true;
    if ((this.adminAppsRoles != null) && (this.adminAppsRoles.length > 0)) {
      for (var i = 0; i < this.adminAppSelectAndUnselectData.length; i++) {
        var foundApp = false;
        for (var j = 0; j < this.adminAppsRoles.length; j++) {
          if (this.adminAppsRoles[j] === this.adminAppSelectAndUnselectData[i]) {
            foundApp = true;
          }
        }
        if (foundApp === false) {
          adminAddedToNewApp = true;
          break;
        }
      }
    } else {
      adminAddedToNewApp = true;
    }
    if (adminAddedToNewApp === true) {
      const modalRef = this.ngModal.open(InformationModalComponent);
      modalRef.componentInstance.title = "Confirmation";
      modalRef.componentInstance.message = 'Add this person as an application user? This allows them to access the application from ONAP Portal. Press OK to go to the Add Users page.';
      modalRef.result.then((_res) => {
        if (_res === 'Ok') {
          this.router.navigate(['/users']);
        }
      });
    }
  }

  updateAdminAppsRoles() {
    const modalRef = this.ngModal.open(InformationModalComponent);
    modalRef.componentInstance.title = "Admin Update";
    modalRef.componentInstance.message = 'Are you sure you want to make these admin changes?';
    modalRef.result.then((result) => {
      if (result === 'Ok') {
        this.adminsService.updateAdminAppsRoles({ orgUserId: this.changedSelectedUser.orgUserId, appsRoles: this.adminAppsRoles }).subscribe(_data => {
          this.passBackNewAdminPopup.emit(_data);
          this.remindToAddUserIfNecessary();
        }, (_err: HttpErrorResponse) => {
          this.passBackNewAdminPopup.emit(_err);
          const modalErrorRef = this.ngModal.open(ConfirmationModalComponent);
          modalErrorRef.componentInstance.title = "Error";
          if (_err.status) {
            modalErrorRef.componentInstance.message = "There was a unknown problem while adding admin to selected application(s)." + "Please try again later. Error Status: " + _err.status;
          }
        });
      }
    }, (reason) => {
      return;
    });
  }
}
