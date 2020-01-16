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

import { Component, OnInit, ViewChild, Input, ChangeDetectionStrategy } from '@angular/core';
import { MatTableDataSource } from '@angular/material';
import { MatSort, MatPaginator } from '@angular/material';
import { WidgetOnboardingService, MicroserviceService } from '../../shared/services/index';
import { IMircroservies } from 'src/app/shared/model/microservice-onboarding/microservices';
import { IWidget } from 'src/app/shared/model/widget-onboarding/widget';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpClient } from '@angular/common/http';
import { WidgetDetailsDialogComponent } from './widget-details-dialog/widget-details-dialog.component';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { InformationModalComponent } from 'src/app/modals/information-modal/information-modal.component';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'app-widget-onboarding',
  templateUrl: './widget-onboarding.component.html',
  styleUrls: ['./widget-onboarding.component.scss']
})
export class WidgetOnboardingComponent implements OnInit {

  widgetsList: Array<IWidget> = [];
  applicationList: Array<Object> = [];
  availableMicroServices: Array<IMircroservies> = [];
  displayedColumns: string[] = ['widgetName', 'application', 'download','delete'];
  isCommError: boolean = false;
  dataSource = new MatTableDataSource(this.widgetsList);
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;
 
  isEditMode: boolean = false;
  result: any;
  

  constructor( public widgetOnboardingService: WidgetOnboardingService, 
    public microservice: MicroserviceService,public ngbModal: NgbModal) { }

  ngOnInit() {
    this.prepareApplicationRoleName();
    this.getOnboardingWidgets();
    this.populateAvailableApps();
    this.getAvailableMicroServices();  
  }

  getOnboardingWidgets(){
    this.isCommError = false;
    this.widgetOnboardingService.getManagedWidgets()
      .subscribe(_data => {
          this.result = _data
          if(!(_data instanceof Array)){
            this.isCommError = true;
            return;
          }
          //console.log("getOnboardingWidgets Data :: ", _data);
          if (this.result == null || this.result == 'undefined') {
              console.log('WidgetOnboardingService::getOnboardingWidgets Failed: Result or result.data is null');
          }else {
            let reSortedWidget = _data.sort(this.getSortOrder("name"));
            this.widgetsList = reSortedWidget;
            this.prepareApplicationRoleName();
            this.populateTableData(this.widgetsList);
        }
      }, error =>{
        console.log(error);
    });
  }

  //Refactor this into a directive
  getSortOrder(prop){
    return function(a, b) {
        if (a[prop].toLowerCase() > b[prop].toLowerCase()) {
            return 1;
        } else if (a[prop].toLowerCase() < b[prop].toLowerCase()) {
            return -1;
        }
        return 0;
    }
  }

  removeWidget(widget: IWidget) {
    let confirmationMsg = 'You are about to delete this Widget : ' + widget.name+ '. Click OK to continue.';
    this.openInformationModal("Confirmation",confirmationMsg).result.then((result) => {
      if (result === 'Ok') {
        if(!widget || widget == null){
          console.log('WidgetOnboardingCtrl::deleteService: No widget or ID... cannot delete');
          return;
        }
        
        this.widgetsList.splice(this.widgetsList.indexOf(widget), 1);
        
        this.widgetOnboardingService.deleteWidget(widget.id)
          .subscribe( _data => {
            this.result = _data;
            this.openConfirmationModal("Success",'Widget deleted successfully');
          }, error => {
            console.log(error);
        }); 
    
        this.populateTableData(this.widgetsList);
      }
    }, (resut) => {
      this.openConfirmationModal('Error', resut);
      return;
    })
  }

  
  openAddWigetModal(rowData:any){
      //console.log("openAddWigetModal getting called...");
      const modalRef = this.ngbModal.open(WidgetDetailsDialogComponent, { size: 'lg' });
      modalRef.componentInstance.widget = rowData;
      modalRef.componentInstance.availableMicroServices = this.availableMicroServices;
      modalRef.componentInstance.applicationList = this.applicationList;
      modalRef.componentInstance.widgetsList = this.widgetsList;
      if(rowData != 'undefined' && rowData){
        this.isEditMode = true;
      }else{
        modalRef.componentInstance.widget = {};
        this.isEditMode = false;
      }
      modalRef.componentInstance.passEntry.subscribe((receivedEntry: any) => {
        //console.log("receivedEntry >>> ",receivedEntry);
        if(receivedEntry){
          this.widgetsList = [];
          this.getOnboardingWidgets();
        }
      });
  }

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  applyAppFilter(event) {
    let filterByApp = event.title;
    if(filterByApp == 'All Applications'){
      this.getOnboardingWidgets();
    }else{
      this.dataSource.filter = filterByApp.trim().toLowerCase();
    }
  }

  downloadWidget(widget){
    this.widgetOnboardingService.downloadWidgetFile(widget.id)
      .subscribe(res => {
        var data = res;
        //console.log("downloadWidgetFile response :: ",data);
        var filename = widget.name + ".zip";	
        if (data == undefined || data == null){
          this.openConfirmationModal("Could not download. Please retry.", '');
          return;         	
        }
        var a = document.createElement('a');
        var blob = new Blob([data], {type: 'application/octet-stream'});
        var url = window.URL.createObjectURL(blob);
        a.href = url;
        a.download = filename;
        document.body.appendChild(a);
        a.click();
        
        setTimeout(function(){
          document.body.removeChild(a);
          window.URL.revokeObjectURL(url);  
        }, 100);  
    
    }, error =>{
      console.log(error);
      this.openConfirmationModal("Could not download. Please retry.", error.message);
    });
  }

  
  populateTableData(wigetList: Array<IWidget>){
    this.dataSource = new MatTableDataSource(wigetList);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  };

  prepareApplicationRoleName(){
    if(this.widgetsList && this.widgetsList.length > 0){
      for(var i = 0; i < this.widgetsList.length; i++){
        let set = new Set();
        var info = "";
        var appContent = [];
        var appName = [];	
        if(this.widgetsList[i].widgetRoles && this.widgetsList[i].widgetRoles.length >0){
          for(var n = 0; n < this.widgetsList[i].widgetRoles.length; n++){
            if(this.widgetsList[i].widgetRoles[n].app)
            set.add(this.widgetsList[i].widgetRoles[n].app.appName);
          }
          set.forEach(function (item) {
            info = item.toString() + " - ";
            for(var n = 0; n < this.widgetsList[i].widgetRoles.length; n++){
              if(this.widgetsList[i].widgetRoles[n].app && item.toString() == this.widgetsList[i].widgetRoles[n].app.appName){
                  info += this.widgetsList[i].widgetRoles[n].roleName + "; ";
              }
            }
            appContent.push(info);
            appName.push(item.toString());
          }.bind(this));
        }
        if(this.widgetsList[i].allowAllUser == "Y"){
          info = "All Applications";
          appContent.push("All Applications");
          appName.push("All Applications");
        }
        this.widgetsList[i].appContent = appContent;
        this.widgetsList[i].appName = appName;
      }
    }
  }

  populateAvailableApps(){
    this.widgetOnboardingService.populateAvailableApps()
      .subscribe( _data => {
        this.applicationList.push({
          index: 0,
          title: 'All Applications',
          value: ''
        })
        var reSortedApp = _data.sort(this.getSortOrder("name"));
        var realAppIndex = 1;
        for (let i = 1; i <= reSortedApp.length; i++) {
            if (!reSortedApp[i-1].restrictedApp) {
                if(_data[i - 1].name && _data[i - 1].name!=""){
                  this.applicationList.push({
                      index: realAppIndex,
                      title: _data[i - 1].name,
                      value: _data[i - 1].id
                  })
                }
                realAppIndex = realAppIndex + 1;
            }
        }
      }, error => {
        console.log(error);
    }); 
  }

  getAvailableMicroServices = () =>{
    this.availableMicroServices = [];
    this.microservice.getServiceList()
    .subscribe(_data => {
        this.result = _data;
        if (this.result == null || this.result == 'undefined') {
             console.log('MicroserviceService::getAvailableMicroServices Failed: Result or result.data is null');
        }else {
          for(var i = 0; i < _data.length; i++){
            this.availableMicroServices.push({
              id: _data[i].id,
              name: _data[i].name,
              option: _data[i].name + ": " + _data[i].url
            });
          }
        }
    }, error =>{
      console.log(error);
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
