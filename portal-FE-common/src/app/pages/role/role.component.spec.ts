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

import { RoleComponent } from './role.component';
import { FormsModule } from '@angular/forms';
import { NgMaterialModule } from 'src/app/ng-material-module';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterLinkDirectiveStub } from 'src/testing/router-link-directive-stub';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { InformationModalComponent } from 'src/app/modals/information-modal/information-modal.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { BrowserDynamicTestingModule } from '@angular/platform-browser-dynamic/testing';
import { BulkUploadRoleComponent } from './bulk-upload-role/bulk-upload-role.component';
import { AddRoleComponent } from './add-role/add-role.component';

describe('RoleComponent', () => {
  let component: RoleComponent;
  let fixture: ComponentFixture<RoleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RoleComponent,RouterLinkDirectiveStub,InformationModalComponent,ConfirmationModalComponent,BulkUploadRoleComponent,AddRoleComponent ],
      imports:[FormsModule,NgMaterialModule,HttpClientTestingModule,BrowserAnimationsModule,NgbModule.forRoot()],
    }).overrideModule(BrowserDynamicTestingModule, { set: { entryComponents: [InformationModalComponent,ConfirmationModalComponent,BulkUploadRoleComponent,AddRoleComponent] } })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RoleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('toggleRole should return stubbed value', () => {
    spyOn(component, 'toggleRole').and.callThrough();
    let element={"id":1,"active":true};
    component.toggleRole(element);
    expect(component.toggleRole).toHaveBeenCalledWith(element);
  });

  it('openBulkUploadRolesAndFunctionsModal should return stubbed value', () => {
    spyOn(component, 'openBulkUploadRolesAndFunctionsModal').and.callThrough();
    component.openBulkUploadRolesAndFunctionsModal();
    expect(component.openBulkUploadRolesAndFunctionsModal).toHaveBeenCalledWith();
  });
  it('editRoleModalPopup should return stubbed value', () => {
    spyOn(component, 'editRoleModalPopup').and.callThrough();
    let element={"id":1,"active":true};
    component.editRoleModalPopup(element);
    expect(component.editRoleModalPopup).toHaveBeenCalledWith(element);
  });

  it('addRoleModalPopup should return stubbed value', () => {
    spyOn(component, 'addRoleModalPopup').and.callThrough();
    component.addRoleModalPopup();
    expect(component.addRoleModalPopup).toHaveBeenCalledWith();
  });

  it('removeRole should return stubbed value', () => {
    spyOn(component, 'removeRole').and.callThrough();
    let element={"id":1,"active":true,"name":"Test_global_"};
    component.selectedCentralizedApp = 2;
    component.removeRole(element);
    expect(component.removeRole).toHaveBeenCalledWith(element);
  });
  it('getCentralizedApps should return stubbed value', () => {
    spyOn(component, 'getCentralizedApps').and.callThrough();
    component.getCentralizedApps('admin');
    expect(component.getCentralizedApps).toHaveBeenCalledWith('admin');
  });
  it('syncRolesFromExternalAuthSystem should return stubbed value', () => {
    spyOn(component, 'syncRolesFromExternalAuthSystem').and.callThrough();
    component.selectedCentralizedApp = 'Test';
    component.syncRolesFromExternalAuthSystem();
    expect(component.syncRolesFromExternalAuthSystem).toHaveBeenCalledWith();
  });
  it('getRolesForSelectedCentralizedApp should return stubbed value', () => {
    spyOn(component, 'getRolesForSelectedCentralizedApp').and.callThrough();
    component.getRolesForSelectedCentralizedApp('Test');
    expect(component.getRolesForSelectedCentralizedApp).toHaveBeenCalledWith('Test');
  });

  

  

  
});
