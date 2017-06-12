/*-
 * ================================================================================
 * ECOMP Portal
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ================================================================================
 */
package org.openecomp.portalapp.portal.service;

import org.openecomp.portalsdk.core.command.support.SearchResult;
import org.openecomp.portalsdk.core.domain.support.DomainVo;


public interface EPLdapService {

    // search POST for users based on the criteria selected in the Request
    SearchResult searchPost(DomainVo searchCriteria, String sortBy1, String sortBy2, String sortBy3, int pageNo, int dataSize, int userId) throws Exception;

}
