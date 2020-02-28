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

import { NewUserModalComponent } from './new-user-modal.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NgMaterialModule } from 'src/app/ng-material-module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Component, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { MatRadioChange } from '@angular/material';

describe('NewUserModalComponent', () => {
  let component: NewUserModalComponent;
  let fixture: ComponentFixture<NewUserModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewUserModalComponent,AppSearchUsersStubComponent,AppUserdetailsFormStubComponent ],
      imports:[HttpClientTestingModule,FormsModule,NgMaterialModule,BrowserAnimationsModule],
      providers:[NgbActiveModal]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewUserModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('addNewUser should return stubbed value', () => {
    spyOn(component, 'addNewUser').and.callThrough();
    component.addNewUser(true);
    expect(component.addNewUser).toHaveBeenCalledWith(true);
  });

  it('changeSelectedUser should return stubbed value', () => {
    spyOn(component, 'changeSelectedUser').and.callThrough();
    let user = {};
    component.changeSelectedUser(user);
    expect(component.changeSelectedUser).toHaveBeenCalledWith(user);
    user = {"firstName":"FirstTestName","lastName":"LastTestName","orgUserId":"TestOrgId"};
    component.changeSelectedUser(user);
    expect(component.changeSelectedUser).toHaveBeenCalledWith(user);

  });

  // it('searchUserRadioChange should return stubbed value', () => {
  //   spyOn(component, 'searchUserRadioChange').and.callThrough();
  //   MatRadio = {"value":""};
  //   component.searchUserRadioChange(eventCheck);
  //   expect(component.searchUserRadioChange).toHaveBeenCalledWith(event);
  //   event = {"value":"System"};
  //   component.searchUserRadioChange(event);
  //   expect(component.searchUserRadioChange).toHaveBeenCalledWith(event);

  // });

  it('navigateBack should return stubbed value', () => {
    spyOn(component, 'navigateBack').and.callThrough();
    component.navigateBack();
    expect(component.navigateBack).toHaveBeenCalledWith();
  });

  it('roleSelectChange should return stubbed value', () => {
    spyOn(component, 'roleSelectChange').and.callThrough();
    let element = {"id":2};
    component.roleSelectChange(element);
    expect(component.roleSelectChange).toHaveBeenCalledWith(element);
  });

  it('getUserAppsRoles should return stubbed value', () => {
    spyOn(component, 'getUserAppsRoles').and.callThrough();
    component.changedSelectedUser = 'Test';
    component.getUserAppsRoles();
    expect(component.getUserAppsRoles).toHaveBeenCalledWith();
  });
  

  
});

@Component({selector: 'app-search-users', template: ''})
class AppSearchUsersStubComponent { 
  @Input() searchTitle:any;
  @Input() isSystemUser:any;
  @Input() placeHolder:any;
}

@Component({selector: 'app-user-details-form', template: ''})
class AppUserdetailsFormStubComponent { 
  
}
