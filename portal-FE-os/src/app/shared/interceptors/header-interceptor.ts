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
    HttpEvent,
    HttpInterceptor,
    HttpHandler,
    HttpRequest,
    HttpHeaders,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { v4 as uuid } from 'uuid';
import { Injectable } from '@angular/core';
declare const getWebJunctionXSRFToken: any;

@Injectable()
export class HeaderInterceptor implements HttpInterceptor {
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        // Clone the request to add the new header
        // HttpHeader object immutable - copy values
        const headerSettings: { [name: string]: string | string[]; } = {};
        headerSettings['X-ECOMP-RequestID'] = uuid();
        const requestType = req.params.get('requestType');
       
        if(requestType!=null && requestType==='fileUpload'){
          //headerSettings['Content-Type'] = 'multipart/form-data';
        }else if(requestType!=null && requestType==='downloadWidgetFile'){
           headerSettings['X-Widgets-Type'] = 'all';
           headerSettings['Content-Type'] = 'application/octet-stream';
        }else{
            headerSettings['Content-Type'] = 'application/json';
        }
        const newHeader = new HttpHeaders(headerSettings);
        const clonedRequest = req.clone({ headers: newHeader, withCredentials: true });
        // Pass the cloned request instead of the original request to the next handle
        return next.handle(clonedRequest);
    }
}