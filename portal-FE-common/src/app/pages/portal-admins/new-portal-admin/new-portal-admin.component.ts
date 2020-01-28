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
import { NgbActiveModal, NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import { PortalAdmin } from 'src/app/shared/model/PortalAdmin';
import { PortalAdminsService } from 'src/app/shared/services';
import { HttpErrorResponse } from '@angular/common/http';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { InformationModalComponent } from 'src/app/modals/information-modal/information-modal.component';

@Component({
  selector: 'app-new-portal-admin',
  templateUrl: './new-portal-admin.component.html',
  styleUrls: ['./new-portal-admin.component.scss']
})
export class NewPortalAdminComponent implements OnInit {

  constructor(public activeModal: NgbActiveModal, private portalAdminsService: PortalAdminsService,
    public ngbModal: NgbModal) { }
  @Input() title: string;
  @Input() id: number;
  changedSelectedUser: PortalAdmin;
  closeResult: string;
  searchTitleText = 'Enter First Name, Last Name or Org User Id';
  placeholderText = 'Search';
  @Output() passBackNewPortalAdmin: EventEmitter<any> = new EventEmitter();

  ngOnInit() {
  }

  changeSelectedUser(user: PortalAdmin) {
    this.changedSelectedUser = user;
  }

  addNewPortalAdmin(changedSelectedUser: PortalAdmin) {
    const modalRef = this.ngbModal.open(InformationModalComponent);
    modalRef.componentInstance.title = "Admin Update";
    modalRef.componentInstance.message = `Are you sure you want to add ${changedSelectedUser.firstName} ${changedSelectedUser.lastName} as a Portal Admin?`;
    modalRef.result.then((result) => {
      if (result === 'Ok') {
        this.portalAdminsService.addPortalAdmin(this.changedSelectedUser.orgUserId).subscribe(_data => {
          this.passBackNewPortalAdmin.emit(_data);
        }, (_err: HttpErrorResponse) => {
          this.passBackNewPortalAdmin.emit(_err);
          const modalErrorRef = this.ngbModal.open(ConfirmationModalComponent);
          modalErrorRef.componentInstance.title = "Error";
          if (_err.status === 409) {    //Conflict
            modalErrorRef.componentInstance.message = "This user already exists as a portal admin!";
          } else {
            modalErrorRef.componentInstance.message = "There was a unknown problem adding the portal admin." + "Please try again later. Error Status: " + _err.status;
          }
        });
      }
    }, (reason) => {
       return;
    });

  }
}
