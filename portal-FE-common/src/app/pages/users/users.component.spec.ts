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

import { UsersComponent } from './users.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';
import { NgMaterialModule } from 'src/app/ng-material-module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NewUserModalComponent } from './new-user-modal/new-user-modal.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { BrowserDynamicTestingModule } from '@angular/platform-browser-dynamic/testing';
import { BulkUserComponent } from './bulk-user/bulk-user.component';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { Component, Input } from '@angular/core';

describe('UsersComponent', () => {
  let component: UsersComponent;
  let fixture: ComponentFixture<UsersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UsersComponent, NewUserModalComponent,BulkUserComponent,ConfirmationModalComponent,AppSearchUsersStubComponent,AppUsersdetailsFormStubComponent],
      imports:[HttpClientTestingModule,FormsModule,NgMaterialModule,BrowserAnimationsModule,NgbModule.forRoot()]
    }).overrideModule(BrowserDynamicTestingModule, { set: { entryComponents: [NewUserModalComponent,BulkUserComponent,ConfirmationModalComponent] } })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UsersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('openAddNewUserModal should return stubbed value', () => {
    spyOn(component, 'openAddNewUserModal').and.callThrough();
    component.openAddNewUserModal();
    expect(component.openAddNewUserModal).toHaveBeenCalledWith();
  });
  it('openExistingUserModal should return stubbed value', () => {
    spyOn(component, 'openExistingUserModal').and.callThrough();
    let user ={"firstName":"FirstTestName","lastName":"LastTestName","orgUserId":""};
    component.openExistingUserModal(user);
    expect(component.openExistingUserModal).toHaveBeenCalledWith(user);
  });

  it('openBulkUserUploadModal should return stubbed value', () => {
    spyOn(component, 'openBulkUserUploadModal').and.callThrough();
    component.openBulkUserUploadModal();
    expect(component.openBulkUserUploadModal).toHaveBeenCalledWith();
  });
  it('applyDropdownFilter should return stubbed value', () => {
    spyOn(component, 'applyDropdownFilter').and.callThrough();
    let _appValue= {"value":"select-application"};
    component.applyDropdownFilter(_appValue);
    expect(component.applyDropdownFilter).toHaveBeenCalledWith(_appValue);
    _appValue= {"value":"Test"};
    component.applyDropdownFilter(_appValue);
    expect(component.applyDropdownFilter).toHaveBeenCalledWith(_appValue);
  });
  it('applyFilter should return stubbed value', () => {
    spyOn(component, 'applyFilter').and.callThrough();
    component.applyFilter("Test");
    expect(component.applyFilter).toHaveBeenCalledWith("Test");
  });
  it('getAdminApps should return stubbed value', () => {
    spyOn(component, 'getAdminApps').and.callThrough();
    component.getAdminApps();
    expect(component.getAdminApps).toHaveBeenCalledWith();
  });
});

@Component({selector: 'app-search-users', template: ''})
class AppSearchUsersStubComponent {
@Input() searchTitle:any;
@Input() isSystemUser:boolean;
@Input() placeHolder:any;


}
@Component({selector: 'app-user-details-form', template: ''})
class AppUsersdetailsFormStubComponent {

}

