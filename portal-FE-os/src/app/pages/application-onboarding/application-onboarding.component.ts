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
import { MatTableDataSource } from '@angular/material';
import { MatSort, MatPaginator } from '@angular/material';
import { ApplicationsService } from '../../shared/services/index';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IApplications } from 'src/app/shared/model/applications-onboarding/applications';
import { environment } from '../../../environments/environment';
import { ApplicationDetailsDialogComponent } from './application-details-dialog/application-details-dialog.component';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { InformationModalComponent } from 'src/app/modals/information-modal/information-modal.component';

@Component({
  selector: 'app-application-onboarding',
  templateUrl: './application-onboarding.component.html',
  styleUrls: ['./application-onboarding.component.scss']
})
export class ApplicationOnboardingComponent implements OnInit {

  api = environment.api;
  appsList: Array<IApplications> = [];
  result: any;
  isEditMode: boolean = false;
  emptyImgForPreview: string;
  isUserSuperAdmin: boolean = false;
  displayedColumns: string[] = ['thumbnail', 'applicationName','active', 
  'integrationType', 'guestAccess', 'url','restURL',
  'communicationKey', 'applicationNamespace', 'centralAuthAccess'];
  dataSource = new MatTableDataSource(this.appsList);
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(public applicationsService: ApplicationsService, public ngbModal: NgbModal) { }

  ngOnInit() {
    this.emptyImgForPreview = '../../../assets/images/default_app_image.gif';
    this.checkIfUserIsSuperAdmin();
    this.getOnboardingApps();
  }

  getOnboardingApps(){
    //console.log("getOnboardingApps called");
    this.applicationsService.getOnboardingApps()
      .subscribe(_data => {
          this.result = _data;
          if (this.result == null || this.result == 'undefined') {
              console.log('WidgetOnboardingService::getOnboardingWidgets Failed: Result or result.data is null');
          }else {
            this.appsList = _data;
            for (var i = 0; i < this.appsList.length; i++) {
              this.appsList[i].imageLink = '';
              if (this.appsList[i].imageUrl){
                this.appsList[i].imageLink = this.api.appThumbnail.replace(':appId', this.appsList[i].id);
                this.appsList[i].imageLink = this.appsList[i].imageLink+'?' + new Date().getTime();
              }else{
                this.appsList[i].imageLink = this.emptyImgForPreview;
              }                    			
            }
            this.populateTableData(this.appsList);
          }
      }, error =>{
        console.log(error);
        this.openConfirmationModal('Error', error.message);
    });
  }

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  };

  
  populateTableData(appsList: Array<IApplications>){
    this.dataSource = new MatTableDataSource(appsList);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  };

  openAddApplicationModal(rowData: any) {
    const modalRef = this.ngbModal.open(ApplicationDetailsDialogComponent, { size: 'lg' });
    modalRef.componentInstance.title = 'Application Details';
    //console.log("selectedData in parent",rowData);
    if(rowData != 'undefined' && rowData){
      modalRef.componentInstance.applicationObj = rowData;
      this.isEditMode = true;
    }else{
      modalRef.componentInstance.applicationObj  = {};
      this.isEditMode = false;
    }
    modalRef.componentInstance.passEntry.subscribe((receivedEntry: any) => {
      //console.log("receivedEntry >>> ",receivedEntry);
      if(receivedEntry){
        this.appsList.push(receivedEntry);
        //this.populateTableData(this.appsList);
        this.getOnboardingApps();
      }
    });
  }

  deleteApplication(application: IApplications){
    let confirmationMsg = 'You are about to delete this App : ' + application.name+ '. Click OK to continue.';
    this.openInformationModal("Confirmation",confirmationMsg).result.then((result) => {
      if (result === 'Ok') {
        if(!application || application == null){
          console.log('ApplicationOnboardingCtrl::deleteApplication: No apllication or ID... cannot delete');
          return;
        }
        this.appsList.splice(this.appsList.indexOf(application), 1);
        this.applicationsService.deleteOnboardingApp(application.id)
          .subscribe( data => {
            this.result = data;
            this.getOnboardingApps();
          }, error => {
            console.log(error);
            this.openConfirmationModal('Error', error);
        });
      }
    }, (resut) => {
      return;
    })
  }

  checkIfUserIsSuperAdmin(){
    this.applicationsService.checkIfUserIsSuperAdmin()
      .subscribe(res => {
        if(res) {
          this.isUserSuperAdmin = true;
          this.displayedColumns = ['thumbnail', 'applicationName','active', 
          'integrationType', 'guestAccess', 'url','restURL',
          'communicationKey', 'applicationNamespace', 'centralAuthAccess', 'delete'];
        }  
        //console.log("isUserSuperAdmin :: ",this.isUserSuperAdmin);
      }, error =>{
        console.log(error);
        this.openConfirmationModal('Error', 'ApplicationsCtrl.checkIfUserIsSuperAdmin:: Failed '+error.message);
    });
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
