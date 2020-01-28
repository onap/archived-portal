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
import { OnInit, Component, ViewChild, NgModuleRef } from '@angular/core';
import { MatSort, MatPaginator } from '@angular/material';
import { MatTableDataSource } from '@angular/material';
import { PortalAdminsService } from 'src/app/shared/services';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import { NewPortalAdminComponent } from './new-portal-admin/new-portal-admin.component';
import { InformationModalComponent } from 'src/app/modals/information-modal/information-modal.component';
import { PortalAdmin } from 'src/app/shared/model/PortalAdmin';
import { HttpErrorResponse } from '@angular/common/http';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';

@Component({
  selector: 'app-portal-admins',
  templateUrl: './portal-admins.component.html',
  styleUrls: ['./portal-admins.component.scss']
})
export class PortalAdminsComponent implements OnInit {
  portalAdmins: PortalAdmin[] = [];
  showSpinner = true;
  closeResult: string;
  constructor(private portalAdminsService: PortalAdminsService, private ngbModal: NgbModal) { }
  displayedColumns: string[] = ['firstName', 'lastName', 'loginId', 'delete'];
  dataSource = new MatTableDataSource(this.portalAdmins);
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  ngOnInit() {
    this.getAllPortalAdmins();
  }

  getAllPortalAdmins() {
    this.portalAdminsService.getPortalAdmins().subscribe((_data: PortalAdmin[]) => {
      this.showSpinner = false;
      // _data is the array of data that you getting from the db.
      this.portalAdmins = _data;
      this.dataSource = new MatTableDataSource(this.portalAdmins);
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
    }, (_err: HttpErrorResponse) =>{
      const modalErrorRef = this.ngbModal.open(ConfirmationModalComponent);
      modalErrorRef.componentInstance.title = "Error";
      if (_err.status) {    //Conflict
        modalErrorRef.componentInstance.message = 'Error Status: ' + _err.status + ' There was a unknown problem adding the portal admin.' + 'Please try again later.';
      }
    })
  }

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  addPortalAdminEntry() {
    const modalRef = this.ngbModal.open(NewPortalAdminComponent);
    modalRef.componentInstance.title = 'Add Portal Admin';
    modalRef.componentInstance.id = 1;
    modalRef.componentInstance.passBackNewPortalAdmin.subscribe((_result: any) => {
      modalRef.close();
      this.showSpinner = true;
      this.getAllPortalAdmins();
    })
  }

  removePortalAdmin(deletePortalAdmin: any) {
    const modalRef = this.ngbModal.open(InformationModalComponent);
    modalRef.componentInstance.title = 'Confirmation';
    modalRef.componentInstance.message = `Are you sure you want to delete ${deletePortalAdmin.firstName} ${deletePortalAdmin.lastName} ?`;
    modalRef.result.then((result) => {
      if (result === 'Ok') {
        this.portalAdminsService.removePortalAdmin(deletePortalAdmin.userId, deletePortalAdmin.loginId).subscribe(_data => {
          this.showSpinner = true;
          this.getAllPortalAdmins();
        })
      }
    }, (reason) => {
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }
}