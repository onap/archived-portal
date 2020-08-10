/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017-2018 AT&T Intellectual Property. All rights reserved.
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

import { TabbarComponent } from './tabbar.component';
import { NgMaterialModule } from 'src/app/ng-material-module';
import { Component, Input } from '@angular/core';
import { ElipsisPipe } from 'src/app/shared/pipes/elipsis/elipsis.pipe';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientTestingModule} from '@angular/common/http/testing';

describe('TabbarComponent', () => {
  let component: TabbarComponent;
  let fixture: ComponentFixture<TabbarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TabbarComponent, AppSideBarStubComponent, AppUserBarStubComponent,RouterOutletStubComponent,AppFooterBarStubComponent,ElipsisPipe],
      imports: [NgMaterialModule,BrowserAnimationsModule,HttpClientTestingModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TabbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

@Component({selector: 'app-sidebar', template: ''})
class AppSideBarStubComponent {
	@Input() langFromTab:string;
}

@Component({selector: 'router-outlet', template: ''})
class RouterOutletStubComponent { }

@Component({selector: 'app-userbar', template: ''})
class AppUserBarStubComponent {}

@Component({selector: 'app-footer', template: ''})
class AppFooterBarStubComponent {}
