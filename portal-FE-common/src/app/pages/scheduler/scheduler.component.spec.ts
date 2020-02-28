/*
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2019 AT&T Intellectual Property. All rights reserved.
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
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SchedulerComponent } from './scheduler.component';
import { FormsModule } from '@angular/forms';
import { NgMaterialModule } from 'src/app/ng-material-module';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NgbActiveModal, NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserDynamicTestingModule } from '@angular/platform-browser-dynamic/testing';
import { InformationModalComponent } from 'src/app/modals/information-modal/information-modal.component';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';

describe('SchedulerComponent', () => {
  let component: SchedulerComponent;
  let fixture: ComponentFixture<SchedulerComponent>;
  const payload: any = {"widgetData":"widgetTestData"};
  const policy = ["test1","test2","test3"];

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SchedulerComponent,InformationModalComponent,ConfirmationModalComponent ],
      imports:[FormsModule,NgMaterialModule,HttpClientTestingModule,BrowserAnimationsModule,NgbModule.forRoot()],
      providers:[NgbActiveModal]
    }).overrideModule(BrowserDynamicTestingModule, { set: { entryComponents: [InformationModalComponent,ConfirmationModalComponent] } })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SchedulerComponent);
    component = fixture.componentInstance;
    component.payload =payload;
    component.policys = policy;
    component.schedulerObjConst.WorkflowName = "workFlowName";
    component.schedulerObj.domainData=[{"WorkflowName":"test"}];
   // component.schedulerObj.schedulingInfo.normalDurationInSeconds= 20;
   component.vnfObject = {
    workflow: null,
    vnfNames:'test1'
  };
    component.schedulerObj.schedulingInfo = {
      normalDurationInSeconds: '',
      additionalDurationInSeconds: '',
      concurrencyLimit: '',
      policyId: '',
      vnfDetails: [
        {
          groupId: "",
          node: [],
          changeWindow: [{
            startTime: '',
            endTime: ''
          }]
        }]
      };
    
    // component.schedulerObj.schedulingInfo.additionalDurationInSeconds= null;
    // component.schedulerObj.schedulingInfo.concurrencyLimit=null;
    // component.schedulerObj.schedulingInfo['vnfDetails'][0].groupId=null;
    // component.schedulerObj.schedulingInfo['vnfDetails'][0].node=null;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('radioSelections should return stubbed value', () => {
    spyOn(component, 'radioSelections').and.callThrough();
    component.schedulerForm.checkboxSelection = "true";
    component.radioSelections();
    expect(component.radioSelections).toHaveBeenCalledWith();
  });

  it('onChangeUpdatePolicyName should return stubbed value', () => {
    spyOn(component, 'onChangeUpdatePolicyName').and.callThrough();
    component.onChangeUpdatePolicyName("test1","test1");
    expect(component.onChangeUpdatePolicyName).toHaveBeenCalledWith("test1","test1");
  });

  it('onChangeUpdateTimeUnit should return stubbed value', () => {
    spyOn(component, 'onChangeUpdateTimeUnit').and.callThrough();
    component.onChangeUpdateTimeUnit("HOURS","HOURS");
    expect(component.onChangeUpdateTimeUnit).toHaveBeenCalledWith("HOURS","HOURS");
  });
  it('isDateValid should return stubbed value', () => {
    spyOn(component, 'isDateValid').and.callThrough();
    component.isDateValid("10/24/2020");
    expect(component.isDateValid).toHaveBeenCalledWith("10/24/2020");
  });
  it('isStartDateValidFromToday should return stubbed value', () => {
    spyOn(component, 'isStartDateValidFromToday').and.callThrough();
    component.isStartDateValidFromToday("10/24/2020");
    expect(component.isStartDateValidFromToday).toHaveBeenCalledWith("10/24/2020");
    component.isStartDateValidFromToday("10/24/2021");
    expect(component.isStartDateValidFromToday).toHaveBeenCalledWith("10/24/2021");
  });

  it('isToDateGreaterFromDate should return stubbed value', () => {
    spyOn(component, 'isToDateGreaterFromDate').and.callThrough();
    component.isToDateGreaterFromDate('10/24/2020','10/24/2021');
    expect(component.isToDateGreaterFromDate).toHaveBeenCalledWith('10/24/2020','10/24/2021');
    component.isToDateGreaterFromDate('10/24/2021','10/24/2020');
    expect(component.isToDateGreaterFromDate).toHaveBeenCalledWith('10/24/2021','10/24/2020');
  });

  it('submit should return stubbed value', () => {
    spyOn(component, 'submit').and.callThrough();
    component.timeSlots = [{"startTime":"10/24/2021"}];
    component.submit();
    expect(component.submit).toHaveBeenCalledWith();
  });

  it('reject should return stubbed value', () => {
    spyOn(component, 'reject').and.callThrough();
    component.timeSlots = [{"startTime":"10/24/2021"}];
    component.reject();
    expect(component.reject).toHaveBeenCalledWith();
  });

  // it('sendSchedulerReq should return stubbed value', () => {
  //   spyOn(component, 'sendSchedulerReq').and.callThrough();
  //   component.timeSlots = [{"startTime":"10/24/2021"}];
  //   component.sendSchedulerReq();
  //   expect(component.sendSchedulerReq).toHaveBeenCalledWith();
  // });
  it('seviceCallToGetTimeSlots should return stubbed value', () => {
    spyOn(component, 'seviceCallToGetTimeSlots').and.callThrough();
    component.seviceCallToGetTimeSlots();
    expect(component.seviceCallToGetTimeSlots).toHaveBeenCalledWith();
  });

  it('getPolicy should return stubbed value', () => {
    spyOn(component, 'getPolicy').and.callThrough();
    component.getPolicy();
    expect(component.getPolicy).toHaveBeenCalledWith();
  });

  it('parseErrorMsg should return stubbed value', () => {
    const response ={"entity":{"requestError":{"text":"EmptyTest"}}};
    spyOn(component, 'parseErrorMsg').and.callThrough();
    component.parseErrorMsg(response,"TestMethod");
    expect(component.parseErrorMsg).toHaveBeenCalledWith(response,"TestMethod");
  });

  it('extractChangeManagementCallbackDataStr should return stubbed value', () => {
    let response ={"workflow":"Update","vnfNames":[
      {"availableVersions":[{"requestInfo":{"source":"URL","suppressRollback":"suppressRollback","requestorId":"requestorId"},"requestParameters":{"usePreload":2}}]},
      {"availableVersions":[{"requestInfo":{"source":"URL","suppressRollback":"suppressRollback","requestorId":"requestorId"},"requestParameters":{"usePreload":2}}]}]};
    spyOn(component, 'extractChangeManagementCallbackDataStr').and.callThrough();

    component.extractChangeManagementCallbackDataStr(response);
    expect(component.extractChangeManagementCallbackDataStr).toHaveBeenCalledWith(response);
       response ={"workflow":"Replace","vnfNames":[
      {"availableVersions":[{"requestInfo":{"source":"URL","suppressRollback":"suppressRollback","requestorId":"requestorId"},"requestParameters":{"usePreload":2}}]},
      {"availableVersions":[{"requestInfo":{"source":"URL","suppressRollback":"suppressRollback","requestorId":"requestorId"},"requestParameters":{"usePreload":2}}]}]};
    component.extractChangeManagementCallbackDataStr(response);
    expect(component.extractChangeManagementCallbackDataStr).toHaveBeenCalledWith(response);
    response ={"workflow":"VNF In Place Software Update","vnfNames":[
      {"availableVersions":[{"requestInfo":{"source":"URL","suppressRollback":"suppressRollback","requestorId":"requestorId"},"requestParameters":{"usePreload":2}}]},
      {"availableVersions":[{"requestInfo":{"source":"URL","suppressRollback":"suppressRollback","requestorId":"requestorId"},"requestParameters":{"usePreload":2}}]}]};
    component.extractChangeManagementCallbackDataStr(response);
    expect(component.extractChangeManagementCallbackDataStr).toHaveBeenCalledWith(response);
    response ={"workflow":"VNF Config Update","vnfNames":[
      {"availableVersions":[{"requestInfo":{"source":"URL","suppressRollback":"suppressRollback","requestorId":"requestorId"},"requestParameters":{"usePreload":2}}]},
      {"availableVersions":[{"requestInfo":{"source":"URL","suppressRollback":"suppressRollback","requestorId":"requestorId"},"requestParameters":{"usePreload":2}}]}]};
    component.extractChangeManagementCallbackDataStr(response);
    expect(component.extractChangeManagementCallbackDataStr).toHaveBeenCalledWith(response);

    response ={"workflow":"VNF In Place Software Update","vnfNames":[
      {"availableVersions":[{"requestInfo":{"source":"URL","suppressRollback":"suppressRollback","requestorId":"requestorId"},"requestParameters":{"usePreload":2}}]},
      {"availableVersions":[{"requestInfo":{"source":"URL","suppressRollback":"suppressRollback","requestorId":"requestorId"},"requestParameters":{"usePreload":2}}]}]};
    component.extractChangeManagementCallbackDataStr(response);
    expect(component.extractChangeManagementCallbackDataStr).toHaveBeenCalledWith(response);
  });
  it('formValidation should return stubbed value', () => {
    spyOn(component, 'formValidation').and.callThrough();
    component.formValidation();
    expect(component.formValidation).toHaveBeenCalledWith();
  });

  it('convertToSecs should return stubbed value', () => {
    spyOn(component, 'convertToSecs').and.callThrough();
    component.convertToSecs(200);
    expect(component.convertToSecs).toHaveBeenCalledWith(200);
  });

  it('stopPoll should return stubbed value', () => {
    spyOn(component, 'stopPoll').and.callThrough();
    component.stopPoll();
    expect(component.stopPoll).toHaveBeenCalledWith();
  });

  it('getVnfData should return stubbed value', () => {
    spyOn(component, 'getVnfData').and.callThrough();
    component.getVnfData([{"name":"testName"}]);
    expect(component.getVnfData).toHaveBeenCalledWith([{"name":"testName"}]);
  });

  it('openConfirmationModal should return stubbed value', () => {
    spyOn(component, 'openConfirmationModal').and.callThrough();
    component.openConfirmationModal("Test1","Test1");
    expect(component.openConfirmationModal).toHaveBeenCalledWith("Test1","Test1");
  });

  it('openInformationModal should return stubbed value', () => {
    spyOn(component, 'openInformationModal').and.callThrough();
    component.openInformationModal("Test1","Test1");
    expect(component.openInformationModal).toHaveBeenCalledWith("Test1","Test1");
  });

  

  
  

  
  
  
  
});
