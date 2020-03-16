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

import { WidgetOnboardingComponent } from './widget-onboarding.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';
import { NgMaterialModule } from 'src/app/ng-material-module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { WidgetOnboardingService } from 'src/app/shared/services/widget-onboarding/widget-onboarding.service';
import { Observable } from 'rxjs';
import { HttpClientModule } from '@angular/common/http';

describe('WidgetOnboardingComponent', () => {
  let component: WidgetOnboardingComponent;
  let fixture: ComponentFixture<WidgetOnboardingComponent>;
  let widgetList = [{"id" :"1",
    "name":"ONAP-A",
    "desc" :"desc",
    "fileLocation" : "fileLocation",
    "allowAllUser" : "allowAllUser",
    "serviceId" : "serviceId",
    "serviceURL" : "serviceURL",
    "sortOrder" : "sortOrder",
    "statusCode" : "statusCode",
    "widgetRoles": "widgetRoles",
    "appContent" : "appContent",
    "appName" : "appName",
    "file"  : "file",
    "allUser": false,
    "saving": "saving"},{"id" :"1",
    "name":"ONAP-B",
    "desc" :"desc",
    "fileLocation" : "fileLocation",
    "allowAllUser" : "allowAllUser",
    "serviceId" : "serviceId",
    "serviceURL" : "serviceURL",
    "sortOrder" : "sortOrder",
    "statusCode" : "statusCode",
    "widgetRoles": "widgetRoles",
    "appContent" : "appContent",
    "appName" : "appName",
    "file"  : "file",
    "allUser": false,
    "saving": "saving"}]


  beforeEach(async(() => {
    let widgetOnboardingService: WidgetOnboardingService;
    
   // widgetOnboardingService = jasmine.createSpyObj('WidgetOnboardingService', ['getManagedWidgets']);
    //widgetOnboardingService.getManagedWidgets.and.returnValue(Observable.of(widgetList));
    TestBed.configureTestingModule({
      declarations: [ WidgetOnboardingComponent ],
      imports:[HttpClientModule,FormsModule,NgMaterialModule,BrowserAnimationsModule],
      providers:[WidgetOnboardingService]
    })
    .compileComponents();


    widgetOnboardingService = TestBed.get(WidgetOnboardingService);
    spyOn(widgetOnboardingService, 'getManagedWidgets').and.returnValue(Observable.of(widgetList));
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WidgetOnboardingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('getOnboardingWidgets should return stubbed value', () => {
    spyOn(component, 'getOnboardingWidgets').and.callThrough();
    component.getOnboardingWidgets();
    expect(component.getOnboardingWidgets).toHaveBeenCalledWith();
  });
});
