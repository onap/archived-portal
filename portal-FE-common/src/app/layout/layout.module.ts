/*
 * ============LICENSE_START==========================================
 * ONAP Portal SDK
 * ===================================================================
 * Copyright � 2019 AT&T Intellectual Property. All rights reserved.
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
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { NgMaterialModule } from '../ng-material-module';
import { LayoutRoutingModule } from './layout-routing.module';
import { LayoutComponent } from './layout.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { HeaderComponent } from './components/header/header.component';
import { GlobalSearchComponent } from './components/global-search/global-search.component';
import { ClickOutsideModule } from 'ng-click-outside';
import { TabbarComponent } from './components/tabbar/tabbar.component';
import { HeaderMenuComponent } from './components/header-menu/header-menu.component';
import { UserbarComponent } from './components/userbar/userbar.component';
import { FooterComponent } from './components/footer/footer.component';
import { ApplicationPipesModule } from '../shared/pipes/application-pipes.module';

@NgModule({
    imports: [
        CommonModule,
        NgMaterialModule,
        LayoutRoutingModule,
        ApplicationPipesModule,
        NgbDropdownModule,
        ClickOutsideModule
    ],
    declarations: [LayoutComponent, SidebarComponent, HeaderComponent,GlobalSearchComponent, TabbarComponent, HeaderMenuComponent, UserbarComponent, FooterComponent]
})
export class LayoutModule {}
