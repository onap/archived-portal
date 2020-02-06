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
 * ============LICENSE_END===========================================
 *
 * 
 */

import { Component, OnInit, ViewChild } from '@angular/core';
import { GetAccessService } from 'src/app/shared/services/get-access/get-access.service';
import { MatTableDataSource, MatPaginator } from '@angular/material';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-get-access',
  templateUrl: './get-access.component.html',
  styleUrls: ['./get-access.component.scss']
})
export class GetAccessComponent implements OnInit {
  api = environment.api;
  isLoadingTable: boolean;
  appTable: any[];
  displayedColumns: string[] = ['function', 'applicationName', 'roleName', 'currentRole', 'requestStatus'];
  getAccessDataSource = new MatTableDataSource(this.appTable);
  @ViewChild(MatPaginator) paginator: MatPaginator;
  showSpinner: boolean;
  getAccessUrl = this.api.getAccessUrl;
  getAccessName = this.api.getAccessName;
  getAccessInfo = this.api.getAccessInfo;

  constructor(private getAccessService: GetAccessService) { }

  ngOnInit() {
    this.appTable = [];
    this.getAccessAppsList();
  }

  applyFilter(filterValue: string) {
    this.getAccessDataSource.filter = filterValue.trim().toLowerCase();
  }
  
  // Convert this code to typescript after mylogins feature is back again
  // var resultAccessValue = null;
       	
  // $scope.openAppRoleModal = (itemData) => {    	
  //   if(resultAccessValue){
  //   let data = null;
  //           data = {
  //               dialogState: 2,
  //               selectedUser:{
  //                   orgUserId: $scope.orgUserId,
  //                   firstName: $scope.firstName,
  //                   lastName: $scope.lastName,
  //                   headerText: itemData.app_name,
  //               }
  //           }
  //       ngDialog.open({
  //           templateUrl: 'app/views/catalog/request-access-catalog-dialogs/request-access-catalog.modal.html',
  //           controller: 'ExternalRequestAccessCtrl',
  //           controllerAs: 'userInfo',
  //           data: data
  //       });
  //   }
  //   }
  
  //   userProfileService.getUserProfile().then(
  //     function(profile) {
  //       $scope.orgUserId = profile.orgUserId;
  //       $scope.firstName = profile.firstName;
  //       $scope.lastName = profile.lastName;
  //   });

  getAccessAppsList() {
    this.showSpinner = true;
    this.getAccessService.getListOfApp().subscribe((_res: any) => {
      var tableData = [];
      // $log.info('GetAccessCtrl::updateAppsList: getting res');
      var result = (typeof (_res) != "undefined" && _res != null) ? _res : null;
      this.showSpinner = false;
      // $log.info('GetAccessCtrl::updateAppsList: result',result);
      // $log.info('GetAccessCtrl::updateAppsList: done');
      var source = result;
      // $log.info('GetAccessCtrl::updateAppsList source: ', source);
      for (var i = 0; i < source.length; i++) {
        var dataArr = source[i];
        var checkEcompFuncAvail = 'Function Not Available';
        var reqStatus = 'Pending';
        dataArr.ecompFunction = (dataArr.ecompFunction === null) ? checkEcompFuncAvail : dataArr.ecompFunction;
        dataArr.reqType = (dataArr.reqType === 'P') ? reqStatus : dataArr.reqType;
        var dataTemp = {
          ecomp_function: dataArr.ecompFunction,
          app_name: dataArr.appName,
          role_name: dataArr.roleName,
          current_role: dataArr.roleActive,
          request_type: dataArr.reqType
        }
        tableData.push(dataTemp);
      }
      this.appTable = tableData;
      this.getAccessDataSource = new MatTableDataSource(this.appTable);
      this.getAccessDataSource.paginator = this.paginator;
    }, (_err) => {
      this.isLoadingTable = false;
    })
  }
}
