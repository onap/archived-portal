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
import { AdminsService, ApplicationsService } from 'src/app/shared/services';
import { Admins, AllApps } from 'src/app/shared/model';
import { MatTableDataSource, MatSort, MatPaginator } from '@angular/material';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NewAdminComponent } from './new-admin/new-admin.component';

@Component({
  selector: 'app-admins',
  templateUrl: './admins.component.html',
  styleUrls: ['./admins.component.scss']
})
export class AdminsComponent implements OnInit {
  availableApps: Array<{ index: number, title: string, value: string }> = [];

  constructor(private adminsService: AdminsService, private applicationService: ApplicationsService,
    public ngModal: NgbModal) { }

  showSpinner = true;
  displayedColumns: string[] = ['firstName', 'lastName', 'userId', 'appName'];
  adminsData: Admins[] = [];
  adminsDataSource = new MatTableDataSource(this.adminsData);
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  ngOnInit() {
    this.adminsData = [];
    this.getAccoutAdminsData();
    this.getAllApps();
  }

  openAddNewAdminModal() {
    const modalRef = this.ngModal.open(NewAdminComponent);
    modalRef.componentInstance.title = 'New Admin';
    modalRef.componentInstance.dialogState = 1;
    modalRef.componentInstance.disableBack = false;
    modalRef.componentInstance.passBackNewAdminPopup.subscribe((_result: any) => {
      modalRef.close();
      this.showSpinner = true;
      this.getAccoutAdminsData();
    }, (_reason: any) => {
      return;
    });
  }

  openExistingAdminModal(_adminData: Admins) {
    const modalRef = this.ngModal.open(NewAdminComponent);
    modalRef.componentInstance.userTitle = `${_adminData.firstName}, ${_adminData.lastName} `+'('+`${_adminData.orgUserId}`+')';
    modalRef.componentInstance.adminModalData = _adminData;
    modalRef.componentInstance.dialogState = 2;
    modalRef.componentInstance.disableBack = true;
    modalRef.componentInstance.passBackNewAdminPopup.subscribe((_result: any) => {
      modalRef.close();
      this.showSpinner = true;
      this.getAccoutAdminsData();
    }, (_reason: any) => {
      return;
    });
  }

  applyFilterByAppName(filterValue: string) {

  }

  applyFilter(filterValue: string) {
    this.adminsDataSource.filter = filterValue.trim().toLowerCase();
  }


  getAccoutAdminsData() {
    this.adminsService.getAccountAdmins().subscribe((_res: Admins[]) => {
      this.showSpinner = false;
      this.adminsData = _res;
      this.adminsDataSource = new MatTableDataSource(this.adminsData);
      this.adminsDataSource.sort = this.sort;
      this.adminsDataSource.paginator = this.paginator;
    });
  }

  getAllApps() {
    this.applicationService.getAvailableApps().subscribe((_res: AllApps[]) => {
      var realAppIndex = 1;
      for (let i = 1; i <= _res.length; i++) {
        if (!_res[i - 1].restrictedApp) {
          this.availableApps.push({
            index: realAppIndex,
            title: _res[i - 1].title,
            value: _res[i - 1].value
          });
          realAppIndex = realAppIndex + 1;
        } else {
        }
      }
    });
  }

}
