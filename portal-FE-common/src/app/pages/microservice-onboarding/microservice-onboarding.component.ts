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

import { Component, OnInit, ViewChild, Input } from '@angular/core';
import { MicroserviceService, WidgetOnboardingService } from '../../shared/services/index'
import { IMircroservies } from 'src/app/shared/model/microservice-onboarding/microservices';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { MatTableDataSource } from '@angular/material';
import { MatSort, MatPaginator } from '@angular/material';
import { MicroserviceAddDetailsComponent } from './microservice-add-details/microservice-add-details.component';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { InformationModalComponent } from 'src/app/modals/information-modal/information-modal.component';

@Component({
  selector: 'app-microservice-onboarding',
  templateUrl: './microservice-onboarding.component.html',
  styleUrls: ['./microservice-onboarding.component.scss']
})
export class MicroserviceOnboardingComponent implements OnInit {

  showSpinner = true;
  microServiceList: Array<IMircroservies> = [];
  result: any;
  isEditMode: boolean = false;
  
  displayedColumns: string[] = ['microserviceName', 'serviceEndPointURL', 'securityType','delete'];
  dataSource = new MatTableDataSource(this.microServiceList);
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(public microservice: MicroserviceService, public ngbModal: NgbModal ) { }

  ngOnInit() {
    this.getOnboardingServices();
  }

  getOnboardingServices(){
    //console.log("getOnboardingServices called");
    this.microservice.getServiceList()
    .subscribe(_data => {
        this.result = _data;
        if (this.result == null || this.result == 'undefined') {
             console.log('MicroserviceService::getServiceList Failed: Result or result.data is null');
         }else {
          this.microServiceList = this.result;
          this.populateTableData(this.microServiceList);
         }
    }, error =>{
      console.log(error);
    });
  }

  deleteService(microserviceObj: IMircroservies, isConfirmed: boolean): void {
    let confirmationMsg = 'You are about to delete this Microservice : ' + microserviceObj.name+ '. Click OK to continue.';
    this.openInformationModal("Confirmation",confirmationMsg).result.then((result) => {
      if (result === 'Ok') {
        if(!microserviceObj || microserviceObj == null){
          console.log('MicroserviceOnboardingCtrl::deleteService: No service or ID... cannot delete');
          return;
        }
        //console.log("service id to delete", microserviceObj.id)
        this.microServiceList.splice(this.microServiceList.indexOf(microserviceObj), 1);
        this.populateTableData(this.microServiceList);
        this.microservice.deleteService(microserviceObj.id)
          .subscribe( data => {
            this.result = data;
            this.microServiceList = [];
            this.getOnboardingServices();
          }, error => {
            console.log(error);
        }); 
      }
    }, (resut) => {
      return;
    })
  }

 
  openAddNewMicroserviceModal(rowData: any){
    //console.log("openAddNewMicroserviceModal getting called...");
    const modalRef = this.ngbModal.open(MicroserviceAddDetailsComponent, { size: 'lg' });
    modalRef.componentInstance.title = 'Microservice Onboarding';
    if(rowData != 'undefined' && rowData){
      modalRef.componentInstance.ms = rowData;
      this.isEditMode = true;
    }else{
      modalRef.componentInstance.ms  = {};
      this.isEditMode = false;
    }
    modalRef.componentInstance.passEntry.subscribe((receivedEntry: any) => {
      //console.log("receivedEntry >>> ",receivedEntry);
      if(receivedEntry){
        this.microServiceList = [];
        this.getOnboardingServices();
      }
    });
  }

  populateTableData(microServiceList: Array<IMircroservies>){
    this.dataSource = new MatTableDataSource(microServiceList);
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
