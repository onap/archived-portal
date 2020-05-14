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
import { Component, OnInit, Input, ViewChild, Output, EventEmitter } from '@angular/core';
import { UsersService } from 'src/app/shared/services';
import { MatTableDataSource, MatPaginator, MatSort } from '@angular/material';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { PortalAdmin } from 'src/app/shared/model/PortalAdmin';

@Component({
  selector: 'app-search-users',
  templateUrl: './search-users.component.html',
  styleUrls: ['./search-users.component.scss']
})
export class SearchUsersComponent implements OnInit {

  constructor(private userService: UsersService, private ngModal: NgbModal) { }
  @Input() searchTitle: string;
  @Input() placeHolder: string;
  @Input() isSystemUser: boolean;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @Output() passBackSelectedUser: EventEmitter<any> = new EventEmitter();
  @Output() userNotFoundFlag = new EventEmitter<boolean>();
  searchString: string;
  txtResults = 'result';
  searchUsersResults: any;
  selected: any;
  isLoading: boolean;
  showUserTable: boolean;
  selectedUser: any;
  displayedColumns: string[] = ['firstName'];
  dataSourceMap = new MatTableDataSource(this.searchUsersResults);
  submitted = false;
  message = " No users found with your query. Please change your search and try again.";

  ngOnInit() {
    this.searchString = '';
    this.showUserTable = false;
    this.isSystemUser = false;
  }

  passSystemUserInfo(systemUser: string) {
    if (this.isSystemUser)
      this.passBackSelectedUser.emit(systemUser);
  }

  noUserFlag: boolean = false;
  searchUsers() {
    if (!this.isSystemUser) {
      this.isLoading = true;
      this.showUserTable = false;
      this.passBackSelectedUser.emit(this.selectedUser = '');
      this.userService.searchUsers(this.searchString).subscribe((_data: PortalAdmin) => {
        this.searchUsersResults = _data;
        if (this.searchUsersResults == null || this.searchUsersResults.length == 0) {
          this.noUserFlag = true;
          this.isLoading = false;
        } else {
          this.noUserFlag = false;
          this.showUserTable = true;
          this.isLoading = false;
          this.dataSourceMap = new MatTableDataSource(this.searchUsersResults);
          this.txtResults = (this.searchUsersResults && this.searchUsersResults.length > 1) ? 'results' : 'result';
        }
      });
    }
  }

  setSelectedUser(user: PortalAdmin) {
    this.selectedUser = user;
    this.passBackSelectedUser.emit(this.selectedUser);
  }

  addNewUser() {
    console.log("Emit the value to parent");
    this.userNotFoundFlag.emit(true);
  }

}
