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
import { Component, OnInit, ViewChild, Input} from '@angular/core';
import { WebAnalyticsService } from '../../shared/services/index';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { MatTableDataSource } from '@angular/material';
import { MatSort, MatPaginator } from '@angular/material';
import { WebAnalyticsDetailsDialogComponent } from './web-analytics-details-dialog/web-analytics-details-dialog.component';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { InformationModalComponent } from 'src/app/modals/information-modal/information-modal.component';

@Component({
  selector: 'app-web-analytics',
  templateUrl: './web-analytics.component.html',
  styleUrls: ['./web-analytics.component.scss']
})
export class WebAnalyticsComponent implements OnInit {

  application: any;
  isAppSelectDisabled: boolean;
  selectApp: any;
  sortedApps: any = [];
  userAppReports: any = [];
  userTableAppReports: any= [];
  userJourneyAnalytics: any = [];
  userApps: any = [];
  result: any;
  isEditMode: boolean = false;

  displayedColumns: string[] = ['applicationName', 'reportName', 'reportURL','delete'];
  dataSource = new MatTableDataSource(this.userTableAppReports);
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;


  constructor(public webAnalyticsService: WebAnalyticsService, public ngbModal: NgbModal) { }

  ngOnInit() {

    this.application = null;	
    this.isAppSelectDisabled = false;
    this.selectApp = 'All Applications';
    this.sortedApps = [{
        index: 0,
        appName: 'All Applications',
        value: 'All Applications'
    }];

    this.getAllWebAnalyticsReport();
    //this.getUserApps();

  }

  getAllWebAnalyticsReport(){
    //console.log("getAllWebAnalyticsReport called");
    this.webAnalyticsService.getAllWebAnalyticsReport()
    .subscribe(_data => {
        this.result = _data;
        if (this.result == null || this.result == 'undefined') {
             console.log('WebAnalyticsService::getAllWebAnalyticsReport Failed: Result or result.data is null');
        }else {
          //console.log('WebAnalyticsService::getAllWebAnalyticsReport', this.result);
          for (let i = 0; i < this.result.length; i++) {
            var userTableAppReport = {
                reportName: this.result[i].reportName,
                reportSrc: this.result[i].reportSrc,
                appName: this.result[i].appName,
                appId : this.result[i].appId,
                resourceId : this.result[i].resourceId
            };
            this.userTableAppReports.push(userTableAppReport);
          }
          this.populateTableData(this.userTableAppReports);
        }
    }, error =>{
      console.log(error);
    });
  }

  deleteWebAnalyticsReport(deleteObj: any){
    let confirmationMsg = 'You are about to delete this Web Analytics : ' + deleteObj.reportName+ '. Click OK to continue.';
    this.openInformationModal("Confirmation",confirmationMsg).result.then((result) => {
      if (result === 'Ok') {
        this.userTableAppReports.splice(this.userTableAppReports.indexOf(deleteObj), 1);
        this.webAnalyticsService.deleteWebAnalyticsReport(deleteObj)
        .subscribe(_data => {
            this.userTableAppReports = [];
            this.getAllWebAnalyticsReport();
        }, error =>{
          console.log(error);
        });
      }
    }, (resut) => {
      return;
    })
  }

  getUserApps(){
    //console.log("getUserApps called");
    this.webAnalyticsService.getWebAnalyticsAppReports()
    .subscribe(_data => {
        this.result = _data;
        if (this.result == null || this.result == 'undefined') {
             console.log('WebAnalyticsService::getServiceList Failed: Result or result.data is null');
         }else {
          for (let i = 0; i < this.result.length; i++) {
            var userAppReport = {
                sizeX: 3,
                sizeY: 2,
                reportName: this.result[i].reportName,
                reportSrc: this.result[i].reportSrc,
                appName: this.result[i].appName,
            };
            
            if(this.result[i].reportSrc.includes("appName")){
                let appName = this.result[i].reportSrc.split("appName=").splice(-1)[0];
                this.webAnalyticsService.getUserJourneyList(appName)
                .subscribe(_data => {
                  let userJourneyReports = {
                    sizeX: 6,
                    sizeY: 3,
                      title: appName+" User journey",
                      analyticsList: this.result
                  };  
                  this.userJourneyAnalytics.push(userJourneyReports);
                }, error =>{
                  console.log(error);
                });

            }                               
            this.userAppReports.push(userAppReport);
        }

        for (var i = 0; i < this.result.length; i++) {
            var userApp = {
                appName: this.result[i].appName,
            };

            this.userApps.push(userApp);
        }
        /*angular.forEach($scope.userApps, function(value, key) {
            var index = $scope.uniqueUserApps.indexOf(value.appName);

            if (index === -1) {
                $scope.uniqueUserApps.push(value.appName);
            }
        });


        for (let i = 1; i <= $scope.uniqueUserApps.length; i++) {
            this.sortedApps.push({
                index: i,
                appName: $scope.uniqueUserApps[i - 1],
                value: $scope.uniqueUserApps[i - 1]
            });
        }*/
      }
    }, error =>{
      //console.log(error);
    });
  }

  openWebAnalyticsModal(rowData: any) {
    const modalRef = this.ngbModal.open(WebAnalyticsDetailsDialogComponent, { size: 'lg' });
    //console.log("selectedData in parent",rowData);
    if(rowData != 'undefined' && rowData){
      modalRef.componentInstance.userTableAppReport = rowData;
      this.isEditMode = true;
    }else{
      modalRef.componentInstance.userTableAppReport  = {};
      this.isEditMode = false;
    }
    modalRef.componentInstance.passEntry.subscribe((receivedEntry: any) => {
      if(receivedEntry.httpStatusCode && receivedEntry.httpStatusCode ==='200' ||
      receivedEntry.status && receivedEntry.status === 'OK'){
        this.userTableAppReports = [];
        this.getAllWebAnalyticsReport();
      } 
    });
  }


  populateTableData(userTableAppReports: Array<Object>){
    this.dataSource = new MatTableDataSource(userTableAppReports);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
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
