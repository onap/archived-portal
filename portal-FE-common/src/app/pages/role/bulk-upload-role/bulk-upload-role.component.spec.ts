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

import { BulkUploadRoleComponent } from './bulk-upload-role.component';
import { FormsModule } from '@angular/forms';
import { NgMaterialModule } from 'src/app/ng-material-module';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

describe('BulkUploadRoleComponent', () => {
  let component: BulkUploadRoleComponent;
  let fixture: ComponentFixture<BulkUploadRoleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BulkUploadRoleComponent ],
      imports:[FormsModule,NgMaterialModule,HttpClientTestingModule],
      providers:[NgbActiveModal]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BulkUploadRoleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('changeUploadTypeInstruction should return stubbed value', () => {
    spyOn(component, 'changeUploadTypeInstruction').and.callThrough();
    component.changeUploadTypeInstruction("functions");
    expect(component.changeUploadTypeInstruction).toHaveBeenCalledWith("functions");
    component.changeUploadTypeInstruction("roles");
    expect(component.changeUploadTypeInstruction).toHaveBeenCalledWith("roles");
    component.changeUploadTypeInstruction("roleFunctions");
    expect(component.changeUploadTypeInstruction).toHaveBeenCalledWith("roleFunctions"); 
    component.changeUploadTypeInstruction("default");
    expect(component.changeUploadTypeInstruction).toHaveBeenCalledWith("default") ;
  });

  it('navigateUploadScreen should return stubbed value', () => {
    spyOn(component, 'navigateUploadScreen').and.callThrough();
    component.selectedUploadDropdown.value='functions';
    component.navigateUploadScreen();
    expect(component.navigateUploadScreen).toHaveBeenCalledWith();
    component.selectedUploadDropdown.value='roles';
    component.navigateUploadScreen();
    expect(component.navigateUploadScreen).toHaveBeenCalledWith();
    component.selectedUploadDropdown.value='roleFunctions';
    component.navigateUploadScreen();
    expect(component.navigateUploadScreen).toHaveBeenCalledWith();
    component.selectedUploadDropdown.value='default';
    component.navigateUploadScreen();
    expect(component.navigateUploadScreen).toHaveBeenCalledWith();
  });
  it('navigateSelectTypeUpload should return stubbed value', () => {
    spyOn(component, 'navigateSelectTypeUpload').and.callThrough();
    component.navigateSelectTypeUpload();
    expect(component.navigateSelectTypeUpload).toHaveBeenCalledWith();
  });
  
  it('getSortOrder should return stubbed value', () => {
    spyOn(component, 'getSortOrder').and.callThrough();
    component.getSortOrder(1,true);
    expect(component.getSortOrder).toHaveBeenCalledWith(1,true);
  });
});
