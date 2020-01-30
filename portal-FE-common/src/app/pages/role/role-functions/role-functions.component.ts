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
import { MatTableDataSource, MatPaginator, MatSort } from '@angular/material';
import { RoleFunctionModalComponent } from './role-function-modal/role-function-modal.component';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import { InformationModalComponent } from 'src/app/modals/information-modal/information-modal.component';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-role-functions',
  templateUrl: './role-functions.component.html',
  styleUrls: ['./role-functions.component.scss']
})
export class RoleFunctionsComponent implements OnInit {
  api = environment.api
  centralizedApps: any;
  selectedCentralizedApp: any;
  availableRoleFunctions: any;
  displayedColumns: string[] = ['type', 'instance', 'action', 'name', 'edit', 'delete'];
  roleFunctionsDataSource = new MatTableDataSource(this.availableRoleFunctions);
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  showSpinner: boolean;

  constructor(public ngbModal: NgbModal,private roleService: RoleService, private applicationsService: ApplicationsService, public http: HttpClient) {}

  ngOnInit() {
    this.availableRoleFunctions = [];
    this.centralizedApps = [];
    this.getCentralizedApps(sessionStorage.userId);
  }

  syncRolesFromExternalAuthSystem() {
    this.applicationsService.syncRolesEcompFromExtAuthSystem(this.selectedCentralizedApp).toPromise().then((res: any) => {
      if (res.status == 'OK') {
        const modalInfoRef = this.ngbModal.open(InformationModalComponent);
        modalInfoRef.componentInstance.title = 'Success';
        modalInfoRef.componentInstance.message = 'Sync role functions completed successfully!';
        modalInfoRef.result.then((_res) => {
          if (_res === 'Ok')
            this.getRoleFunctions(this.selectedCentralizedApp);
        }, (result) => {

        })
      } else {
        this.openConfirmationModal('Error', 'Sync failed ' + res.message);
      }
    }).catch(err => {
      this.openConfirmationModal('Error', 'Sync failed' + err);
    });
  };


  // getCentalizedApps
  getCentralizedApps(userId) {
    this.roleService.getCentralizedApps(userId).toPromise().then((res: any) => {
      if (res.length > 0) {
        this.centralizedApps = res;
        this.selectedCentralizedApp = this.centralizedApps[0].appId;
        this.getRoleFunctions(this.centralizedApps[0].appId);
      }
    }).catch(err => {
      // $log.error('RoleListCtrl::centralizedApps retrieval error: ', err);
    }).finally(() => {
      // this.isLoadingTable = false;
    });
  }

  getRoleFunctions(id) {
    this.showSpinner = true;
    this.roleService.getRoleFunctionList(id).subscribe((data: any) => {
      this.showSpinner = false;
      var j = data;
      var roleFunctions = JSON.parse(j.data);
      this.availableRoleFunctions = roleFunctions.availableRoleFunctions;
      this.roleFunctionsDataSource = new MatTableDataSource(this.availableRoleFunctions);
      this.roleFunctionsDataSource.sort = this.sort;
      this.roleFunctionsDataSource.paginator = this.paginator;
    }, (error) => {
      this.showSpinner = false;
      this.openConfirmationModal('Error', 'Failed to get role functions. Please try again!' + error.message);
    })
  };

  addRoleFunctionModalPopup(){
    const modalInfoRef = this.ngbModal.open(RoleFunctionModalComponent);
    modalInfoRef.componentInstance.title = 'Add Role Function';
    modalInfoRef.componentInstance.appId = this.selectedCentralizedApp;
    modalInfoRef.componentInstance.currentRoleFunctions = this.availableRoleFunctions;
    modalInfoRef.componentInstance.passBackRoleFunctionPopup.subscribe((_result: any) => {
      if(_result){
        modalInfoRef.close();
        this.availableRoleFunctions.push(_result);
        this.roleFunctionsDataSource = new MatTableDataSource(this.availableRoleFunctions);
        this.roleFunctionsDataSource.sort = this.sort;
        this.roleFunctionsDataSource.paginator = this.paginator;
      }
    }, (_reason: any) => {
      return;
    });

  }

  editRoleFunctionModalPopup(_element){
    const modalInfoRef = this.ngbModal.open(RoleFunctionModalComponent);
    modalInfoRef.componentInstance.title = 'Edit Role Function';
    modalInfoRef.componentInstance.appId = this.selectedCentralizedApp;
    modalInfoRef.componentInstance.editRoleFunction = _element;
    modalInfoRef.componentInstance.currentRoleFunctions = this.availableRoleFunctions;
    modalInfoRef.componentInstance.passBackRoleFunctionPopup.subscribe((_result: any) => {
      if(_result){
        modalInfoRef.close();
        this.availableRoleFunctions.splice(this.availableRoleFunctions.indexOf(_element), 1);
        this.availableRoleFunctions.push(_result);
        this.roleFunctionsDataSource = new MatTableDataSource(this.availableRoleFunctions);
        this.roleFunctionsDataSource.sort = this.sort;
        this.roleFunctionsDataSource.paginator = this.paginator;
      }
    }, (_reason: any) => {
      return;
    });
  }

  removeRoleFunction(_element: any){
    const ngbInfoModal = this.ngbModal.open(InformationModalComponent);
    ngbInfoModal.componentInstance.title = 'Confirmation';
    ngbInfoModal.componentInstance.message = 'You are about to delete the role function ' + _element.name + '. Do you want to continue?';
    ngbInfoModal.result.then(_res =>{
      if(_res === 'Ok'){
        this.showSpinner = true;
				var uuu = this.api.removeRoleFunction.replace(':appId', this.selectedCentralizedApp);
				var postData = _element;
				this.http.post(uuu, postData).subscribe((response: any) => {
          this.showSpinner = false;
					if(response.status == 'OK'){
            this.openConfirmationModal('Success', response.message);
            this.availableRoleFunctions.splice(this.availableRoleFunctions.indexOf(_element), 1);
            this.roleFunctionsDataSource = new MatTableDataSource(this.availableRoleFunctions);
            this.roleFunctionsDataSource.sort = this.sort;
            this.roleFunctionsDataSource.paginator = this.paginator;
					} else{
            this.showSpinner = false;
						this.openConfirmationModal('Error', "Error while deleting: " + response.message);
					}
				}, (err) => {
          this.showSpinner = false;
          this.openConfirmationModal('Error', err.message);
				});
      }
    }, (_reason: any) => {
      return;
    });
  }

  openConfirmationModal(_title: string, _message: string) {
    const modalInfoRef = this.ngbModal.open(ConfirmationModalComponent);
    modalInfoRef.componentInstance.title = _title;
    modalInfoRef.componentInstance.message = _message;
  }

}
