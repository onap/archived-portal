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

import { Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { WebAnalyticsService } from 'src/app/shared/services';

@Component({
  selector: 'app-web-analytics-details-dialog',
  templateUrl: './web-analytics-details-dialog.component.html',
  styleUrls: ['./web-analytics-details-dialog.component.scss']
})
export class WebAnalyticsDetailsDialogComponent implements OnInit {

  result: any;
  isEditMode: boolean = false;
  isAllApplications: boolean = true;
  emptyImg = null;
  allApplications: any = [];
  allApps: any = [];
            
  newAppModel = {
    'appId': null,
    'appName':null,
    'reportName': null,
    'reportSrc': null,
    'resourceId': null
  };

  newApp={
    'appId': '',
    'appName':''	     		
  }

  constructor(public activeModal: NgbActiveModal, public ngbModal: NgbModal, 
    public webAnalyticsService : WebAnalyticsService) { }

  @Input() userTableAppReport: any;
  @Output() passEntry: EventEmitter<any> = new EventEmitter();

  ngOnInit() {
    this.isAllApplications = true;
    if(this.userTableAppReport.appName){
      this.isEditMode = true;
    }else{
      this.isEditMode = false;
    }
    //console.log("IsEditMode in Web analytycs Dialog :: ",this.isEditMode)
    this.getAllApplications();
  }

  getAllApplications(){
    this.isAllApplications = true;
    this.webAnalyticsService.getAllApplications()
    .subscribe(_data => {
        this.result = _data;
        if (this.result == null || this.result == 'undefined') {
             //console.log('WebAnalyticsService::getAllApplications Failed: Result or result.data is null');
        }else {
          for (let i = 0; i < this.result.length; i++) {
            var application = {
                appId : this.result[i].id,
                appName: this.result[i].name,
                enabled : this.result[i].enabled,
                restrictedApp :this.result[i].restrictedApp,
            };
            this.allApps.push(application);  
         }
         for (let i = 0; i < this.allApps.length; i++) {
          if((this.allApps[i].enabled == true && this.allApps[i].restrictedApp == false) || (this.allApps[i].appId == 1) ) {
            var validApplication = {
              appId : this.allApps[i].appId,
              appName: this.allApps[i].appName,
            };
            this.allApplications.push(validApplication);
          }
        }
      }
    }, error =>{
      this.isAllApplications = false;
      console.log(error);
    });
  }

  saveChanges(){
    //console.log("Save Changes Called.");
    let selectedApplication = this.userTableAppReport.appName;
    this.newAppModel.appId = selectedApplication.appId;
    this.newAppModel.appName = selectedApplication.appName;
    this.newAppModel.reportName = this.userTableAppReport.reportName;
    this.newAppModel.reportSrc =  this.userTableAppReport.reportSrc;
    this.newAppModel.resourceId =  this.userTableAppReport.resourceId;

    if (this.isEditMode) {
      this.newAppModel.appId = this.userTableAppReport.appId;
      //console.log("Update Analytics..newAppModel :: ",this.userTableAppReport);
      this.webAnalyticsService.updateWebAnalyticsReport(this.newAppModel)
      .subscribe(_data => {
          this.result = _data;
          //console.log("Update Analytics Response:: ",this.result);
          this.passEntry.emit(this.result);
          this.ngbModal.dismissAll();
      }, error =>{
        this.isAllApplications = false;
        console.log(error);
      });
    }else{
      //console.log("Save Analytics.newAppModel :: ",this.userTableAppReport);
      this.webAnalyticsService.save(this.newAppModel)
      .subscribe(_data => {
          this.result = _data;
          //console.log("Save Analytics Response:: ",this.result);
          this.passEntry.emit(this.result);
          this.ngbModal.dismissAll();
      }, error =>{
        this.isAllApplications = false;
        console.log(error);
      });
    }
  }
}
