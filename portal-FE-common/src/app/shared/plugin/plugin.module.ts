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
 
import {
  BrowserModule,
  BrowserTransferStateModule, TransferState
} from '@angular/platform-browser';
import { APP_INITIALIZER, NgModule, APP_BOOTSTRAP_LISTENER } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { DynamicWidgetModule } from './dynamic-widget/dynamic-widget.module';



import { ClientPluginLoaderService } from './plugin-loader/client-plugin-loader.service';
import { PluginsConfigProvider } from './plugins-config.provider';
import { TransferStateService } from './transfer-state.service';
import { PluginLoaderService } from './plugin-loader/plugin-loader.service';
import { PluginComponent } from './plugin.component';
import { ListWidgetComponent } from './dynamic-widget/list-widget/list-widget.component';

import { config } from 'rxjs';

@NgModule({
  declarations: [PluginComponent],
  imports: [
    HttpClientModule,
    //BrowserModule.withServerTransition({ appId: 'serverApp' }),
    BrowserTransferStateModule
  ],
  providers: [
    { provide: PluginLoaderService, useClass: ClientPluginLoaderService },
    //PluginsConfigProvider,
    TransferStateService,
    {
      provide: APP_BOOTSTRAP_LISTENER,
      useFactory: (provider: PluginsConfigProvider) => () =>
        provider
          .loadConfig()
          .toPromise()
          .then(config => {
            provider.config = config;
            console.log(config);
          }
          ),
      multi: true,
      deps: [PluginsConfigProvider]
    }
  ],
  bootstrap: [PluginComponent],
  exports: [PluginComponent]
})
export class PluginModule {
  constructor(transferStateService: TransferStateService) {}
}
