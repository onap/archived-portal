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
 
import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { makeStateKey, TransferState } from '@angular/platform-browser';
import { isPlatformBrowser, isPlatformServer } from '@angular/common';
import { of } from 'rxjs';
import { tap } from 'rxjs/operators';

let isBrowser: boolean;
let isServer: boolean;
let transferState: TransferState;

@Injectable({
  providedIn: 'root'
})
export class TransferStateService {
  constructor(
    private state: TransferState,
    @Inject(PLATFORM_ID) private platformId: any
  ) {
    transferState = state;
    isBrowser = isPlatformBrowser(this.platformId);
    isServer = isPlatformServer(this.platformId);
  }
}

export const preserveServerState = (
  keyName: string,
  emptyResult: any = null
) => {
  const key = makeStateKey(keyName);
  return (target: any, propertyKey: string, descriptor: PropertyDescriptor) => {
    const method = descriptor.value;
    descriptor.value = function() {
      if (isBrowser && transferState.hasKey(key)) {
        const state = transferState.get(key, emptyResult);
        return of(state);
      }

      return method.apply(this, arguments).pipe(
        tap(res => {
         // if (isServer) {
            transferState.set(key, res);
        //  }
        })
      );
    };
  };
};
