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
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { BasicAuthAccountService } from '../../shared/services/index';
import { IAccountOnboarding } from 'src/app/shared/model/account-onboarding/accountOnboarding';
import { AccountAddDetailsComponent } from './account-add-details/account-add-details.component';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { InformationModalComponent } from 'src/app/modals/information-modal/information-modal.component';

@Component({
  selector: 'app-account-onboarding',
  templateUrl: './account-onboarding.component.html',
  styleUrls: ['./account-onboarding.component.scss']
})
export class AccountOnboardingComponent implements OnInit {

  accountList: Array<IAccountOnboarding> = [];
  result: any;
  isEditMode: boolean = false;
  displayedColumns: string[] = ['accountName', 'userName','delete'];
  dataSource = new MatTableDataSource(this.accountList);
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(public basicAuthAccountService: BasicAuthAccountService, public ngbModal: NgbModal) { }

  ngOnInit() {
    this.getOnboardingAccounts();
  }

  populateTableData(wigetList: Array<IAccountOnboarding>){
    this.dataSource = new MatTableDataSource(wigetList);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  };

  getOnboardingAccounts(){
    //console.log("getOnboardingAccounts called");
    this.basicAuthAccountService.getAccountList()
      .subscribe(_data => {
          this.result = _data;
          //console.log("getOnboardingAccounts Data :: ", _data);
          if (this.result == null || this.result == 'undefined') {
              //console.log('BasicAuthAccountService::getOnboardingAccounts Failed: Result or result.data is null');
          }else {
            this.accountList = this.result.response;
            this.populateTableData(this.accountList);
          }
      }, error =>{
        console.log(error);
      });

  };

  openAddNewAccountModal(rowData: any){
    //console.log("openAddNewAccountModal getting called...");
    const modalRef = this.ngbModal.open(AccountAddDetailsComponent, { size: 'lg' });
    modalRef.componentInstance.title = 'Account Details';
    if(rowData != 'undefined' && rowData){
      rowData.repassword = rowData.password;
      modalRef.componentInstance.accountOnboarding = rowData;
      this.isEditMode = true;
    }else{
      modalRef.componentInstance.accountOnboarding  = {};
      this.isEditMode = false;
    }
    modalRef.componentInstance.passEntry.subscribe((receivedEntry: any) => {
      //console.log("receivedEntry >>> ",receivedEntry);
      if(receivedEntry){
        this.accountList = [];
        this.getOnboardingAccounts();
      }
    });
  }

  deleteAccount(selectedAccount : any){
    let confirmationMsg = 'You are about to delete this account : ' + selectedAccount.applicationName + '. Click OK to continue.';
    this.openInformationModal("Confirmation",confirmationMsg).result.then((result) => {
      if (result === 'Ok') {
        //console.log("deleteAccount called Account Onboarding");
        if(!selectedAccount || selectedAccount == null || selectedAccount =='undefined'){
          console.log('AccountOnboardingCtrl::deleteAccount: No Account or ID... cannot delete');
          return;
        }
        //console.log("deleteAccount>>id",selectedAccount.id)
        this.basicAuthAccountService.deleteAccount(selectedAccount.id)
          .subscribe( _data => {
            this.result = _data;
            //console.log("deleteAccount response :: ",this.result);
            this.accountList.splice(this.accountList.indexOf(selectedAccount), 1);
            this.getOnboardingAccounts();
          }, error => {
            console.log(error);
        });
      }
    }, (resut) => {
      return;
    })
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
