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
import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ApplicationsService, UsersService } from 'src/app/shared/services';
import { MatTableDataSource, MatRadioChange } from '@angular/material';
import { UserAdminApps, UserAccessRoles } from 'src/app/shared/model';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { InformationModalComponent } from 'src/app/modals/information-modal/information-modal.component';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-new-user-modal',
  templateUrl: './new-user-modal.component.html',
  styleUrls: ['./new-user-modal.component.scss']
})
export class NewUserModalComponent implements OnInit {
  @Input() dialogState: number;
  @Input() userTitle: string;
  @Input() disableBack: boolean;
  @Input() adminModalData: any;
  @Input() userModalData: any;
  @Output() passBackNewUserPopup: EventEmitter<any> = new EventEmitter();
  changedSelectedUser: any;
  showSpinner: boolean;
  isGettingAdminApps: boolean;
  adminApps: any;
  modelSelectedRoles: any;
  appRoles: Array<UserAccessRoles>;
  numberAppsProcessed: number;
  isSystemUserCheck = false;
  extRequestValue = false;
  searchTitleText = 'Enter First Name, Last Name or Org-ID';
  placeholderText = 'Search';
  ngRepeatDemo = [
    { id: 'userButton', value: 'true', labelValue: 'User' },
    { id: 'systemUserButton', value: 'false', labelValue: 'System' }
  ]
  selectedvalueradioButtonGroup = {
    type: 'true'
  }
  userRadioSearchButton = this.ngRepeatDemo[0].labelValue;
  displayedColumns: string[] = ['applications', 'roles', 'delete'];
  userAdminAppsSource = new MatTableDataSource(this.adminApps);
  showAppSpinner: boolean;
  isError: boolean;
  errorMessage: any;
  originalSelectedRoles: any;
  numberAppsSucceeded: number;
  anyChanges: boolean;
  orgUserIdValue: string;
  constructor(public ngbModal: NgbModal, public activeModal: NgbActiveModal, private applicationsService: ApplicationsService, private usersService: UsersService) { }

  ngOnInit() {
    this.appRoles = [];
    this.adminApps = [];
    this.anyChanges = false;
    this.modelSelectedRoles = [];
    this.originalSelectedRoles = [];
    this.changedSelectedUser = null;
    if (this.dialogState === 2) {
      this.changedSelectedUser = this.userModalData;
      this.orgUserIdValue = this.changedSelectedUser.orgUserId;
      this.getUserAppsRoles();
    }
  }

  addUserFlag : boolean = false;

  addNewUser(newUserFlag : boolean){
    console.log("New user flag ", newUserFlag);
    this.addUserFlag = true;
  }

  changeSelectedUser(user: any) {
    this.changedSelectedUser = user;
    if (this.changedSelectedUser.firstName === undefined || this.changedSelectedUser.lastName === undefined) {
      this.userTitle = this.changedSelectedUser;
      this.orgUserIdValue = this.changedSelectedUser;
    }
    else {
      this.orgUserIdValue = this.changedSelectedUser.orgUserId;
      this.userTitle = `${this.changedSelectedUser.firstName}, ` + ` ${this.changedSelectedUser.lastName} ` + ` (${this.changedSelectedUser.orgUserId})`;
    }
  }

  searchUserRadioChange($event: MatRadioChange) {
    if ($event.value === 'System') {
      this.searchTitleText = 'Enter System UserID';
      this.placeholderText = 'xxxxxx or xxxxxx@org.com';
      this.isSystemUserCheck = true;
    } else {
      this.searchTitleText = 'Enter First Name, Last Name or ATTUID';
      this.placeholderText = 'Search';
      this.isSystemUserCheck = false;
    }
  }

  navigateBack() {
    this.anyChanges = false;
    this.dialogState = 1;
  }

  roleSelectChange(element: any) { // update this.adminApps list when user select roles from dropdown
    let existingSelectedRoles = this.modelSelectedRoles;
    this.modelSelectedRoles = [];
    this.anyChanges = true;
    this.adminApps.forEach(_item => {
      if (_item.id === element.id) {
        _item['isChanged'] = true;
      }
      let appRoleList = _item.appRoles;
      if (appRoleList != undefined) {
        appRoleList.forEach(_itemRole => {
          if (_itemRole.appId === element.id) {
            const index = existingSelectedRoles.indexOf(_itemRole);
            if (index != -1) {
              _itemRole.isApplied = true;
            } else {
              _itemRole.isApplied = false;
            }
          }
          if (_itemRole.isApplied)
            this.modelSelectedRoles.push(_itemRole);
        });
      }
    });
  }

  getUserAppsRoles() {
    if (this.changedSelectedUser === undefined) {
      this.dialogState = 1;
      return;
    }
    this.isGettingAdminApps = true;
    this.applicationsService.getAdminApps().subscribe((apps: Array<UserAdminApps>) => {
      this.isGettingAdminApps = false;
      if (!apps || !apps.length) {
        return null;
      }
      this.adminApps = apps;
      this.dialogState = 2;
      this.numberAppsProcessed = 0;
      this.showSpinner = true;
      apps.forEach(app => {
        //$log.debug('NewUserModalCtrl::getUserAppsRoles: app: id: ', app.id, 'name: ',app.name);
        // Keep track of which app has changed, so we know which apps to update using a BE API
        app['isChanged'] = false;
        // Each of these specifies a state, which corresponds to a different message and style that gets displayed
        app['showSpinner'] = true;
        app['isError'] = false;
        app['isDeleted'] = false;
        app['printNoChanges'] = false;
        app['isUpdating'] = false;
        app['isErrorUpdating'] = false;
        app['isDoneUpdating'] = false;
        app['errorMessage'] = "";
        this.usersService.getUserAppRoles(app.id, this.orgUserIdValue, this.extRequestValue, this.isSystemUserCheck).toPromise().then((userAppRolesResult) => {
          app['appRoles'] = userAppRolesResult;
          app['showSpinner'] = false;
          for (var i = 0; i < app['appRoles'].length; i++) {
            app['appRoles'][i]['appId'] = app.id;
            if (app['appRoles'][i].roleName.indexOf('global_') != -1) {
              app['appRoles'][i].roleName = '*' + app['appRoles'][i].roleName;
            }
            if (app['appRoles'][i].isApplied)
              this.modelSelectedRoles.push(app['appRoles'][i]);
          }
        }).catch((err: HttpErrorResponse) => {
          app['isError'] = true;
          app['showSpinner'] = false;
          if (err.status == 200 || err.message.toLowerCase().includes("rollback"))
            app['errorMessage'] = 'Error: ' + 500;
          else
            app['errorMessage'] = 'Error: ' + err.status;
        }).finally(() => {
          this.numberAppsProcessed++;
          if (this.numberAppsProcessed === this.adminApps.length) {
            this.originalSelectedRoles = this.modelSelectedRoles;
            this.showSpinner = false;
          }
        });
      })
      this.userAdminAppsSource = new MatTableDataSource(this.adminApps);
    }, (_err) => {

    })
  }

  updateUserAppsRoles() {
    this.anyChanges = false;
    if (!this.changedSelectedUser ||
        (this.changedSelectedUser.orgUserId == undefined && !this.isSystemUserCheck) ||
          !this.adminApps) {
      return;
    }
    this.showSpinner = true;
    this.numberAppsProcessed = 0;
    this.numberAppsSucceeded = 0;
    this.adminApps.forEach(app => {
      if (app.isChanged) {
        app.isUpdating = true;
        for (var i = 0; i < app.appRoles.length; i++) {
          if (app.appRoles[i].roleName.indexOf('*global_') != -1) {
            app.appRoles[i].roleName = app.appRoles[i].roleName.replace('*', '');
          }
        }
        var newUserAppRoles = {
          orgUserId: this.orgUserIdValue,
          appId: app.id,
          appRoles: app.appRoles,
          appName: app.name,
          isSystemUser: this.isSystemUserCheck
        };
        this.usersService.updateUserAppRoles(newUserAppRoles).toPromise()
          .then((res: any) => {
            if (res.status && res.status.toLowerCase() === 'error') {
              const modalErrorRef = this.ngbModal.open(ConfirmationModalComponent);
              modalErrorRef.componentInstance.message = "Error: " + res.message;
            }
            app.isUpdating = false;
            app.isDoneUpdating = true;
            this.numberAppsSucceeded++;
          }).catch(err => {
            var errorMessage = 'Failed to update the user application roles: ' + err;
            if (err.status == 504) {
              this.numberAppsSucceeded++;
              errorMessage = 'Request is being processed, please check back later!';
            } else {
              app.isErrorUpdating = true;
            }
            const modalErrorRef = this.ngbModal.open(ConfirmationModalComponent);
            modalErrorRef.componentInstance.message = errorMessage;
          }).finally(() => {
            this.numberAppsProcessed++;
            if (this.numberAppsProcessed === this.adminApps.length) {
              this.showSpinner = false; // hide the spinner
            }
            if (this.numberAppsSucceeded === this.adminApps.length) {
              this.passBackNewUserPopup.emit();
              this.activeModal.close('Close');//close and resolve dialog promise with true (to update the table)
            }
          })
      } else {
        app.noChanges = true;
        app.isError = false; //remove the error message; just show the No Changes messages
        this.numberAppsProcessed++;
        this.numberAppsSucceeded++;
        if (this.numberAppsProcessed === this.adminApps.length) {
          this.showSpinner = false; // hide the spinner
        }
        if (this.numberAppsSucceeded === this.adminApps.length) {
          this.activeModal.close('Close');
        }
      }
    });
  }

  deleteApp(app) {
    let appMessage = this.changedSelectedUser.firstName + ' ' + this.changedSelectedUser.lastName;
    const ngbModalConfirm = this.ngbModal.open(InformationModalComponent);
    ngbModalConfirm.componentInstance.title = 'Confirmation';
    ngbModalConfirm.componentInstance.message = 'Are you sure you want to delete ' + appMessage;
    ngbModalConfirm.result.then((_res) => {
      if (_res === 'Ok') {
        this.anyChanges = true;
        app.isChanged = true;
        app.isDeleted = true; // use this to hide the app in the display
        app.appRoles.forEach((role) => {
          role.isApplied = false;
        });
        // remove app roles if user app delete option is selected
        this.modelSelectedRoles.forEach((_role, index) => {
          if (_role.appId === app.id) this.modelSelectedRoles.splice(index, 1);
        })
        this.roleSelectChange(app);
      }
    }).catch(err => {
      // $log.error('NewUserModalCtrl::deleteApp error: ',err);
      const ngbModalError = this.ngbModal.open(InformationModalComponent);
      ngbModalError.componentInstance.title = 'Error';
      ngbModalError.componentInstance.message = 'There was a problem deleting the the applications. ' +
        'Please try again later. Error: ' + err.status;
    });
  }

}
