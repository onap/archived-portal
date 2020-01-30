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
import { Component, OnInit, ViewChild } from '@angular/core';
import { RoleService, ApplicationsService } from 'src/app/shared/services';
import { HttpErrorResponse, HttpClient } from '@angular/common/http';
import { MatTableDataSource, MatSort, MatPaginator } from '@angular/material';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { InformationModalComponent } from 'src/app/modals/information-modal/information-modal.component';
import { environment } from 'src/environments/environment';
import { BulkUploadRoleComponent } from './bulk-upload-role/bulk-upload-role.component';
import { AddRoleComponent } from './add-role/add-role.component';

@Component({
  selector: 'app-role',
  templateUrl: './role.component.html',
  styleUrls: ['./role.component.scss']
})
export class RoleComponent implements OnInit {

  selectedCentralizedApp: any;
  centralizedApps: any;
  showSpinner: boolean;
  availableRoles: any[];
  syncRolesApplied: boolean;
  displayedColumns: string[] = ['name', 'priority', 'active', 'edit', 'delete'];
  roleDataSource = new MatTableDataSource(this.availableRoles);
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  appName: any;
  api = environment.api;
  availableRoleFunctions: any;
  constructor(private roleService: RoleService, private applicationsService: ApplicationsService, public ngbModal: NgbModal, public http: HttpClient) { }

  ngOnInit() {
    this.centralizedApps = [];
    this.availableRoles = [];
    this.appName = '';
    this.selectedCentralizedApp = '';
    this.getCentralizedApps(sessionStorage.userId);
  }

  toggleRole(_element) {
    let activeOrInactive = (_element.active) ? 'activate' : 'inactivate';
    const modalInfoRef = this.ngbModal.open(InformationModalComponent);
    modalInfoRef.componentInstance.title = 'Confirmation';
    modalInfoRef.componentInstance.message = 'You are about to ' + activeOrInactive + ' the role ' + _element.name + '. Do you want to continue?';
    modalInfoRef.result.then((_res) => {
      if (_res === 'Ok') {
        var uuu = this.api.toggleRole + '/' + this.selectedCentralizedApp + '/' + _element.id;
        var postData = {
          appId: this.selectedCentralizedApp,
          role: _element
        };
        this.http.post(uuu, postData).toPromise().then((data: any) => {
          if (typeof data === 'object' && data.restcallStatus == 'Success') {
            this.availableRoles = data.availableRoles;
            this.roleDataSource = new MatTableDataSource(this.availableRoles);
            this.roleDataSource.sort = this.sort;
            this.roleDataSource.paginator = this.paginator;
            // $log.debug('role::availableRoles:'+$scope.availableRoles);
          } else {
            _element.active = !_element.active;
            const modalErrorRef = this.ngbModal.open(ConfirmationModalComponent);
            modalErrorRef.componentInstance.title = 'Error';
            modalErrorRef.componentInstance.message = 'Error while saving. ' + data.restCallStatus;
          }

        }, (response) => {
          // debug.log('response:'+response);
          _element.active = !_element.active;
          const modalErrorRef = this.ngbModal.open(ConfirmationModalComponent);
          modalErrorRef.componentInstance.title = 'Error';
          modalErrorRef.componentInstance.message = 'Error while saving. ' + response.restCallStatus;
        });
      } else {
        _element.active = !_element.active;
      }

    }, (result) => {

    })
  }

  openBulkUploadRolesAndFunctionsModal() {
    const modalBulkUploadRole = this.ngbModal.open(BulkUploadRoleComponent);
    modalBulkUploadRole.componentInstance.title = 'Bulk Upload Role-Function';
    modalBulkUploadRole.componentInstance.dialogState = 1;
    modalBulkUploadRole.componentInstance.appId = this.selectedCentralizedApp;
  }

  editRoleModalPopup(_element) {
    this.showSpinner = true;
    this.roleService.getRole(this.selectedCentralizedApp, _element.id).toPromise().then((data: any) => {
      this.showSpinner = false;
      var response = JSON.parse(data.data);
      var availableRoleFunctions = JSON.parse(response.availableRoleFunctions);
      const ngbModalCreateRole = this.ngbModal.open(AddRoleComponent);
      ngbModalCreateRole.componentInstance.title = 'Role';
      ngbModalCreateRole.componentInstance.dialogState = 2;
      ngbModalCreateRole.componentInstance.availableRole = _element;
      ngbModalCreateRole.componentInstance.appRoleFunctions = availableRoleFunctions;
      ngbModalCreateRole.componentInstance.appId = this.selectedCentralizedApp;
      ngbModalCreateRole.componentInstance.passBackAddRolePopup.subscribe((_result: any) => {
        this.showSpinner = true;
        this.getAppRoles(_result);
      }, (_reason: any) => {
        return;
      });
    }, (error) => {
      this.showSpinner = false;
    });
  }

  addRoleModalPopup() {
    const ngbModalCreateRole = this.ngbModal.open(AddRoleComponent);
    ngbModalCreateRole.componentInstance.title = 'Role';
    ngbModalCreateRole.componentInstance.appId = this.selectedCentralizedApp;
    ngbModalCreateRole.componentInstance.passBackAddRolePopup.subscribe((_result: any) => {
      this.showSpinner = true;
      this.getAppRoles(_result);
    }, (_reason: any) => {
      return;
    });

  }

  removeRole(_element) {
    if ((this.selectedCentralizedApp !== 1) && (_element.name.indexOf('global_') !== -1)) {
      const modalInfoRef = this.ngbModal.open(ConfirmationModalComponent);
      modalInfoRef.componentInstance.title = 'Confirmation';
      modalInfoRef.componentInstance.message = 'Global role cannot be deleted.';
    }
    else {
      const modalInfoRef = this.ngbModal.open(InformationModalComponent);
      modalInfoRef.componentInstance.title = 'Confirmation';
      modalInfoRef.componentInstance.message = 'You are about to delete the role ' + _element.name + ' . Do you want to continue?';
      modalInfoRef.result.then((_res) => {
        if (_res === 'Ok') {
          var uuu = this.api.removeRole + '/' + this.selectedCentralizedApp + '/' + _element.id;
          var postData = {
            appId: this.selectedCentralizedApp,
            availableRoleId: _element.id
          };
          this.http.post(uuu, postData).toPromise().then((data: any) => {
            if (typeof data === 'object' && data.restCallStatus == 'Success') {
              this.availableRoles = data.availableRoles;
              this.roleDataSource = new MatTableDataSource(this.availableRoles);
              this.roleDataSource.sort = this.sort;
              this.roleDataSource.paginator = this.paginator;
            } else {
              const modalErrorRef = this.ngbModal.open(ConfirmationModalComponent);
              modalErrorRef.componentInstance.title = 'Error';
              modalErrorRef.componentInstance.message = 'Failed to remove role. ' + data.error;
            }
          }, (_err) => {
            const modalErrorRef = this.ngbModal.open(ConfirmationModalComponent);
            modalErrorRef.componentInstance.title = 'Error';
            modalErrorRef.componentInstance.message = 'Error while deleting: ' + _err.error;
          })
        }
      }, (_dismiss) => {

      });
    }
  }

  // getCentalizedApps
  getCentralizedApps(userId) {
    this.showSpinner = true;
    this.roleService.getCentralizedApps(userId).toPromise().then((res: any) => {
      if (res.length > 0) {
        this.centralizedApps = res;
        this.selectedCentralizedApp = this.centralizedApps[0].appId;
        this.getRolesForSelectedCentralizedApp(this.centralizedApps[0].appId);
      }
    }).catch(err => {
      this.showSpinner = false;
      // $log.error('RoleListCtrl::centralizedApps retrieval error: ', err);
    })
  }

  syncRolesFromExternalAuthSystem() {
    this.applicationsService.syncRolesEcompFromExtAuthSystem(this.selectedCentralizedApp).toPromise().then((res: any) => {
      if (res.status == 'OK') {
        const modalInfoRef = this.ngbModal.open(InformationModalComponent);
        modalInfoRef.componentInstance.title = 'Success';
        modalInfoRef.componentInstance.message = 'Sync operation completed successfully!';
        modalInfoRef.result.then((_res) => {
          if (_res === 'Ok')
            this.getRolesForSelectedCentralizedApp(this.selectedCentralizedApp);
        }, (result) => {

        })
      } else {
        const modalErrorRef = this.ngbModal.open(ConfirmationModalComponent);
        modalErrorRef.componentInstance.title = 'Error';
        modalErrorRef.componentInstance.message = 'Sync operation failed for ' + this.appName + 'res.message';
      }
    }).catch(err => {
      const modalErrorRef = this.ngbModal.open(ConfirmationModalComponent);
      modalErrorRef.componentInstance.title = 'Error';
      modalErrorRef.componentInstance.message = 'Sync operation failed for ' + this.appName + 'err.message';
    });
  };

  getRolesForSelectedCentralizedApp(val) {
    this.showSpinner = true;
    this.availableRoles = [];
    this.roleDataSource = new MatTableDataSource(this.availableRoles);
    this.applicationsService.getSingleAppInfoById(val).subscribe((res: any) => {
      this.appName = res.name;
      if (res.centralAuth == true) {
        this.syncRolesApplied = true;
      }
    });
    this.getAppRoles(val);
  }

  private getAppRoles(val: any) {
    this.roleService.getRoles(val).subscribe((data: any) => {
      if (data) {
        var j = data;
        j = JSON.parse(j.data);
        this.availableRoles = j.availableRoles;
        this.roleDataSource = new MatTableDataSource(this.availableRoles);
        this.roleDataSource.sort = this.sort;
        this.roleDataSource.paginator = this.paginator;
        this.showSpinner = false;
      }
      else {
        this.showSpinner = false;
        const modalErrorRef = this.ngbModal.open(ConfirmationModalComponent);
        modalErrorRef.componentInstance.title = 'Error';
        modalErrorRef.componentInstance.message = 'Failed to get ' + this.appName + ' roles. Please try again later!';
      }
    }, (error: HttpErrorResponse) => {
      this.showSpinner = false;
      const modalErrorRef = this.ngbModal.open(ConfirmationModalComponent);
      modalErrorRef.componentInstance.title = 'Error';
      modalErrorRef.componentInstance.message = 'Failed to get ' + this.appName + ' roles. Please try again later!';
    });
  }
}
