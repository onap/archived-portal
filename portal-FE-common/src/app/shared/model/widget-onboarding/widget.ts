/*
 * ============LICENSE_START==========================================
 * ONAP Portal SDK
 * ===================================================================
 * Copyright © 2019 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified?: any; all software contained herein is licensed
 * under the Apache License?: any; Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing?: any; software
 * distributed under the License is distributed on an "AS IS" BASIS?: any;
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND?: any; either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Unless otherwise specified?: any; all documentation contained herein is licensed
 * under the Creative Commons License?: any; Attribution 4.0 Intl. (the "License");
 * you may not use this documentation except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             https://creativecommons.org/licenses/by/4.0/
 *
 * Unless required by applicable law or agreed to in writing?: any; documentation
 * distributed under the License is distributed on an "AS IS" BASIS?: any;
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND?: any; either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ============LICENSE_END============================================
 *
 * 
 */
export interface IWidget {
    id ?: any;
    name ?: any;
    desc ?: any;
    fileLocation ?: any;
    allowAllUser ?: any;
    serviceId ?: any;
    serviceURL ?: any;
    sortOrder ?: any;
    statusCode ?: any;
    widgetRoles ?: any;
    appContent ?: any;
    appName ?: any
    file  ?: any;
    allUser ?: boolean;
    saving ?: any
}