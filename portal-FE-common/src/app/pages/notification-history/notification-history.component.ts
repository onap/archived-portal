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
import { MatTableDataSource } from '@angular/material';
import { MatSort, MatPaginator } from '@angular/material';
import { NotificationService } from '../../shared/services/index';

@Component({
  selector: 'app-notification-history',
  templateUrl: './notification-history.component.html',
  styleUrls: ['./notification-history.component.scss']
})
export class NotificationHistoryComponent implements OnInit {

  result: any;
  notificationHistory: any= [];
  displayedColumns: string[] = ['messageSource', 'message', 'startDateLocalTime','endDateLocalTime', 'priority', 'createdBy', 'createdTime'];
  notificationsDataSource = new MatTableDataSource(this.notificationHistory);
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(public notificationService: NotificationService) { }

  ngOnInit() {
    this.getNotificationHistory();
  }

  getNotificationHistory(){
    //console.log("getNotificationHistory called");
    this.notificationService.getNotificationHistory()
    .subscribe(_data => {
        this.result = _data;
        if (this.result == null || this.result == 'undefined') {
          console.log('NotificationService::getNotificationHistory Failed: Result or result.data is null');
        }else {
          //console.log('Notification Data ::',this.result);
          this.notificationHistory = this.result;
          this.populateTableData(this.notificationHistory);
        }
    }, error =>{
      console.log(error);
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

}
