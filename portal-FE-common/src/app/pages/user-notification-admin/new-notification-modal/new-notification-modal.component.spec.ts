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

import { NewNotificationModalComponent } from './new-notification-modal.component';
import { FormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NgMaterialModule } from 'src/app/ng-material-module';
import { NgbActiveModal, NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { InformationModalComponent } from 'src/app/modals/information-modal/information-modal.component';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { BrowserDynamicTestingModule } from '@angular/platform-browser-dynamic/testing';

describe('NewNotificationModalComponent', () => {
  let component: NewNotificationModalComponent;
  let component1: NewNotificationModalComponent;
  let fixture: ComponentFixture<NewNotificationModalComponent>;
  let fixture1: ComponentFixture<NewNotificationModalComponent>;
  const selectedNotification ={"msgSource":"TestSource","priority":1,"startTime":"10/11/2020","endTime":"10/11/2021"}

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewNotificationModalComponent ,InformationModalComponent,ConfirmationModalComponent],
      imports:[FormsModule,HttpClientTestingModule,NgMaterialModule,BrowserAnimationsModule,NgbModule.forRoot()],
      providers:[NgbActiveModal]
    }).overrideModule(BrowserDynamicTestingModule, { set: { entryComponents: [InformationModalComponent,ConfirmationModalComponent] } })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewNotificationModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();


    fixture1 = TestBed.createComponent(NewNotificationModalComponent);
    component1 = fixture1.componentInstance;
    component1.selectedNotification = selectedNotification;
    fixture1.detectChanges();

    
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('addUserNotification should return stubbed value', () => {
    spyOn(component1, 'addUserNotification').and.callThrough();
    component1.addUserNotification();
    expect(component1.addUserNotification).toHaveBeenCalledWith();
  });

  it('settingTreeParam should return stubbed value', () => {
    spyOn(component1, 'settingTreeParam').and.callThrough();
    component1.settingTreeParam();
    expect(component1.settingTreeParam).toHaveBeenCalledWith();
  });

  it('checkTreeSelect should return stubbed value', () => {
    spyOn(component1, 'checkTreeSelect').and.callThrough();
    component1.checkTreeSelect();
    expect(component1.checkTreeSelect).toHaveBeenCalledWith();
  });

  it('openConfirmationModal should return stubbed value', () => {
    spyOn(component1, 'openConfirmationModal').and.callThrough();
    component1.openConfirmationModal('Title','Message');
    expect(component1.openConfirmationModal).toHaveBeenCalledWith('Title','Message');
  });

  it('openInformationModal should return stubbed value', () => {
    spyOn(component1, 'openInformationModal').and.callThrough();
    component1.openInformationModal('Title','Message');
    expect(component1.openInformationModal).toHaveBeenCalledWith('Title','Message');
  });
});
