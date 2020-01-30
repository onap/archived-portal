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
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { MatTableDataSource } from '@angular/material';
import { MatSort, MatPaginator } from '@angular/material';
import { NotificationService } from '../../shared/services/index';
import { NewNotificationModalComponent } from './new-notification-modal/new-notification-modal.component';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { InformationModalComponent } from 'src/app/modals/information-modal/information-modal.component';

@Component({
  selector: 'app-user-notification-admin',
  templateUrl: './user-notification-admin.component.html',
  styleUrls: ['./user-notification-admin.component.scss']
})
export class UserNotificationAdminComponent implements OnInit {

  isEditMode: any;
  result: any;
  tableAdminNotifItems: any = [];
  displayedColumns: string[] = ['messageSource', 'message', 'startDateLocalTime','endDateLocalTime', 'priority', 'createdBy', 'createdTime', 'allUsersRoles', 'viewOrDelete'];
  notificationsDataSource = new MatTableDataSource(this.tableAdminNotifItems);
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(public notificationService: NotificationService, public ngbModal: NgbModal) { }

  ngOnInit() {
    this.getAdminNotifications();
  }

  getAdminNotifications(){
    this.notificationService.getAdminNotification()
    .subscribe(_data => {
        this.result = _data;
        if (this.result == null || this.result == 'undefined') {
          console.log('NotificationService::getAdminNotifications Failed:::: Result or result.data is null');
        }else {
          this.tableAdminNotifItems = this.result;
          this.populateTableData(this.tableAdminNotifItems);
        }
    }, error =>{
      console.log(error);
      this.openConfirmationModal('Error', error);
      return;
    });
  }

  removeUserNotification(selectedAdminNotification: any){ 
    let confirmationMsg = 'You are about to delete this Notification : ' + selectedAdminNotification.msgHeader+ '. Click OK to continue.';
    this.openInformationModal("Confirmation",confirmationMsg).result.then((result) => {
      if (result === 'Ok') {
        selectedAdminNotification.activeYn = 'N';
        this.notificationService.updateAdminNotification(selectedAdminNotification)
        .subscribe(_data => {
            this.result = _data;
            this.tableAdminNotifItems = [];
            this.getAdminNotifications();
        }, error =>{
          console.log(error);
          this.openConfirmationModal('Error', error);
          return;
        });
      }
    }, (resut) => {
      this.openConfirmationModal('Error', resut);
      return;
    })
  }


  openAddNewNotificationModal(rowData: any){
    const modalRef = this.ngbModal.open(NewNotificationModalComponent, { windowClass: 'add-notification-modal'});
    modalRef.componentInstance.title = 'Add a New Notification';
    if(rowData != 'undefined' && rowData){
      modalRef.componentInstance.selectedNotification = rowData;
      this.isEditMode = true;
    }else{
      modalRef.componentInstance.notification  = {};
      this.isEditMode = false;
    }
    modalRef.componentInstance.passEntry.subscribe((receivedEntry: any) => {
      if(receivedEntry){
        this.tableAdminNotifItems = [];
        this.getAdminNotifications();
      }
    });
  }

  populateTableData(notificationHistory: Array<Object>){
    this.notificationsDataSource = new MatTableDataSource(notificationHistory);
    this.notificationsDataSource.sort = this.sort;
    this.notificationsDataSource.paginator = this.paginator;
  }

  applyFilter(filterValue: string) {
    this.notificationsDataSource.filter = filterValue.trim().toLowerCase();
  }

  openConfirmationModal(_title: string, _message: string) {
    const modalInfoRef = this.ngbModal.open(ConfirmationModalComponent);
    modalInfoRef.componentInstance.title = _title;
    modalInfoRef.componentInstance.message = _message;
  }

  openInformationModal(_title: string, _message: string){
    const modalInfoRef = this.ngbModal.open(InformationModalComponent);
    modalInfoRef.componentInstance.title = _title;
    modalInfoRef.componentInstance.message = _message;
    return modalInfoRef;
 }
}
