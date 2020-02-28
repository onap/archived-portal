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

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationDetailsDialogComponent } from './application-details-dialog.component';
import { NgMaterialModule } from 'src/app/ng-material-module';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal, NgbModal, NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { IApplications } from 'src/app/shared/model/applications-onboarding/applications';
import { InformationModalComponent } from 'src/app/modals/information-modal/information-modal.component';
import { BrowserDynamicTestingModule } from '@angular/platform-browser-dynamic/testing';
import { NgbModalBackdrop } from '@ng-bootstrap/ng-bootstrap/modal/modal-backdrop';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';


describe('ApplicationDetailsDialogComponent', () => {
  let component: ApplicationDetailsDialogComponent;
  let fixture: ComponentFixture<ApplicationDetailsDialogComponent>;
  const applicationObj: IApplications = {"id":"testID"};

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ApplicationDetailsDialogComponent,InformationModalComponent,ConfirmationModalComponent],
      imports: [NgMaterialModule,FormsModule,HttpClientTestingModule,NgbModule.forRoot()],
      providers: [NgbActiveModal]  
    }).overrideModule(BrowserDynamicTestingModule, { set: { entryComponents: [InformationModalComponent,ConfirmationModalComponent] } })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApplicationDetailsDialogComponent);
    component = fixture.componentInstance;
    component.applicationObj = applicationObj;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('removeImage should return stubbed value', () => {
    spyOn(component, 'removeImage').and.callThrough();
    component.removeImage();
    expect(component.removeImage).toHaveBeenCalledWith();
  });
  it('saveChanges should return stubbed value', () => {
    component.applicationObj.isCentralAuth = true;
    component.applicationObj.isEnabled = false;
    spyOn(component, 'saveChanges').and.callThrough();
    component.saveChanges();
    expect(component.saveChanges).toHaveBeenCalledWith();
    component.applicationObj.isEnabled = true;
    component.applicationObj.url = 'www.test.com'
    component.applicationObj.restrictedApp =true;
    
    //spyOn(component, 'saveChanges').and.callThrough();
    component.saveChanges();
    expect(component.saveChanges).toHaveBeenCalledWith();
    component.applicationObj.isCentralAuth = false;
    component.applicationObj.url = 'test'
    component.applicationObj.restrictedApp =false;
    component.applicationObj.isOpen = true;
    component.isEditMode =true;
    //spyOn(component, 'saveChanges').and.callThrough();
    component.saveChanges();
    expect(component.saveChanges).toHaveBeenCalledWith();
  });
  it('saveChanges Central Auth is disabled', () => {
    component.applicationObj.isCentralAuth = false;
    component.applicationObj.isEnabled = false;
    spyOn(component, 'saveChanges').and.callThrough();
    component.saveChanges();
    expect(component.saveChanges).toHaveBeenCalledWith();
    component.applicationObj.isEnabled = true;
    component.applicationObj.restrictedApp = true;
    component.saveChanges();
    expect(component.saveChanges).toHaveBeenCalledWith();
    
  });

  it('saveChanges URL validation changes', () => {
    component.applicationObj.isCentralAuth = true;
    component.applicationObj.isEnabled = true;
    component.applicationObj.name ='test';
    component.applicationObj.url = 'https://www.test.com'
    component.applicationObj.username ='test'
    component.applicationObj.nameSpace ='ONAP'
    spyOn(component, 'saveChanges').and.callThrough();
    component.saveChanges();
    expect(component.saveChanges).toHaveBeenCalledWith();
    component.applicationObj.restrictedApp = false;
    component.isEditMode = true;
    component.saveChanges();
    expect(component.saveChanges).toHaveBeenCalledWith();
  });
});
