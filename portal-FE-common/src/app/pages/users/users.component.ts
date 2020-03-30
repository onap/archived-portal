/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2020 AT&T Intellectual Property. All rights reserved.
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
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatPaginator } from '@angular/material';
import { ApplicationsService, UsersService } from 'src/app/shared/services';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { UserAdminApps } from 'src/app/shared/model';
import { HttpErrorResponse } from '@angular/common/http';
import { NewUserModalComponent } from './new-user-modal/new-user-modal.component';
import { BulkUserComponent } from './bulk-user/bulk-user.component';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent implements OnInit {
  multiAppAdmin: boolean;
  adminApps: any;
  selectApp = 'select-application';
  selectedApp: any;
  appsIsDown: boolean;
  noUsersInApp: boolean;
  searchString: string;
  isAppSelectDisabled: boolean;
  accountUsers: any;
  noAppSelected: boolean;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  displayedColumns: string[] = ['firstName', 'lastName', 'userId', 'roles'];
  adminsDataSource = new MatTableDataSource(this.accountUsers);
  adminsData: [];
  showSpinner: boolean;
  adminAppsIsNull: any;

  constructor(private applicationsService: ApplicationsService, public ngbModal: NgbModal,
    private usersService: UsersService) { }

  ngOnInit() {
    this.adminApps = [];
    this.accountUsers = [];
    this.getAdminApps();
  }

  openAddNewUserModal() {
    const modalRef = this.ngbModal.open(NewUserModalComponent);
    modalRef.componentInstance.title = 'New User';
    modalRef.componentInstance.dialogState = 1;
    modalRef.componentInstance.disableBack = false;
    modalRef.componentInstance.passBackNewUserPopup.subscribe((_result: any) => {
      this.showSpinner = true;
      this.updateUsersList();
    }, (_reason: any) => {
      return;
    });
  }

  openExistingUserModal(userData: any) {
    const modalRef = this.ngbModal.open(NewUserModalComponent);
    let firstName = '';
    let lastName = '';
    let orgUserId = '';
    if(userData && userData.firstName && userData.firstName!=null){
      firstName = userData.firstName;
    }
    if(userData && userData.lastName && userData.lastName!=null){
      lastName = ',' + userData.lastName;
    }
    if(userData && userData.orgUserId && userData.orgUserId!=null){
      orgUserId = ' (' +userData.orgUserId + ')';
    }
    modalRef.componentInstance.userTitle = `${firstName} ${lastName} ${orgUserId}` ;
    modalRef.componentInstance.dialogState = 2;
    modalRef.componentInstance.userModalData = userData;
    modalRef.componentInstance.disableBack = true;
    modalRef.componentInstance.passBackNewUserPopup.subscribe((_result: any) => {
      this.showSpinner = true;
      this.updateUsersList();
    }, (_reason: any) => {
      return;
    });
  }

  openBulkUserUploadModal() {
    const modalRef = this.ngbModal.open(BulkUserComponent);
    modalRef.componentInstance.title = 'Bulk User Upload';
    modalRef.componentInstance.adminsAppsData = this.adminApps;
    modalRef.componentInstance.passBackBulkUserPopup.subscribe((_result: any) => {
      this.showSpinner = true;
      this.updateUsersList();
    }, (_reason: any) => {
      return;
    });
  }

  applyDropdownFilter(_appValue: any) {
    if (_appValue !== 'select-application') {
      this.selectedApp = _appValue;
      this.selectApp = this.selectedApp.value;
      this.updateUsersList();
    } else {
      this.showSpinner = false;
      this.noAppSelected = true;
      this.accountUsers = [];
      this.adminsDataSource = new MatTableDataSource(this.accountUsers);
    }
  }

  applyFilter(filterValue: string) {
    this.adminsDataSource.filter = filterValue.trim().toLowerCase();
  }

  getAdminApps() {
    this.showSpinner = true;
    this.applicationsService.getAdminApps().subscribe((apps: Array<UserAdminApps>) => {
      this.showSpinner = false;
      if (!apps) {
        return null;
      }

      if (apps.length >= 2) {
        this.multiAppAdmin = true;
      } else {
        this.adminApps = [];
      }

      let sortedApps = apps.sort(this.getSortOrder("name"));
      let realAppIndex = 1;
      for (let i = 1; i <= sortedApps.length; i++) {
        this.adminApps.push({
          index: realAppIndex,
          id: sortedApps[i - 1].id,
          value: sortedApps[i - 1].name,
          title: sortedApps[i - 1].name
        });
        realAppIndex = realAppIndex + 1;
      }
      this.selectApp = this.adminApps[0];
      this.adminAppsIsNull = false;
      if (this.selectApp != 'select-application') {
        this.isAppSelectDisabled = false;
        this.noUsersInApp = false;
        this.noAppSelected = true;
      }
    }, (_err: HttpErrorResponse) => {
      this.showSpinner = false;
      if (_err.status === 403) {
        this.adminAppsIsNull = true;
      } else {
        const modalErrorRef = this.ngbModal.open(ConfirmationModalComponent);
        modalErrorRef.componentInstance.title = "Error";
        if (_err.status) {    //Conflict
          modalErrorRef.componentInstance.message = 'Error Status: ' + _err.status + ' There was a unknown problem adding the portal admin.' + 'Please try again later.';
        }
      }
    });
  }

  updateUsersList() {
    this.appsIsDown = false;
    this.noUsersInApp = false;
    // $log.debug('UsersCtrl::updateUsersList: Starting updateUsersList');
    //reset search string
    this.searchString = '';
    //should i disable this too in case of moving between tabs?
    this.isAppSelectDisabled = true;
    //activate spinner
    this.showSpinner = true;
    this.accountUsers = [];
    this.adminsDataSource = new MatTableDataSource(this.accountUsers);
    if (this.selectApp != 'select-application' && this.selectedApp) { // 'Select Application'
      this.noAppSelected = false;
      this.usersService.getAccountUsers(this.selectedApp.id)
        .subscribe((accountUsers: []) => {
          this.isAppSelectDisabled = false;
          this.accountUsers = accountUsers;
          if (!accountUsers || accountUsers.length === 0) {
            this.noUsersInApp = true;
          }
          this.showSpinner = false;
          this.adminsDataSource = new MatTableDataSource(this.accountUsers);
          this.adminsDataSource.paginator = this.paginator;
          this.adminsDataSource.sort = this.sort;
        }, (_err: HttpErrorResponse) => {
          this.isAppSelectDisabled = false;
          const modalErrorRef = this.ngbModal.open(ConfirmationModalComponent);
          modalErrorRef.componentInstance.title = "Error";
          modalErrorRef.componentInstance.message = 'Error Status: ' + _err.status + ' There was a problem updating the users List.' + 'Please try again later.';
          this.appsIsDown = true;
          this.showSpinner = false;
        })
    } else {
      this.isAppSelectDisabled = false;
      this.showSpinner = false;
      this.noUsersInApp = false;
      this.noAppSelected = true;
    }
  };

  getSortOrder = (prop) => {
    return function (a, b) {
      if (a[prop] > b[prop]) {
        return 1;
      } else if (a[prop] < b[prop]) {
        return -1;
      }
      return 0;
    }
  }
}
