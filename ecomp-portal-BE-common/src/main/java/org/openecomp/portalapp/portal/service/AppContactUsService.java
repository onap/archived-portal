/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the “License”);
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
 * under the Creative Commons License, Attribution 4.0 Intl. (the “License”);
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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.openecomp.portalapp.portal.service;

import java.util.List;

import org.openecomp.portalapp.portal.ecomp.model.AppCategoryFunctionsItem;
import org.openecomp.portalapp.portal.ecomp.model.AppContactUsItem;

public interface AppContactUsService {
	
	/**
	 * Gets a list of contact-us information for all entries in
	 * the fn_app_contact_us table, sorted by app name.  If an application is active but has no fn_app_contact_us entry, it will have no entry in this result.
 	* 
	 * @return List of AppContactUsItem, one for each item in fn_app_contact_us table.
	 * @throws Exception
	 */
	public List<AppContactUsItem> getAppContactUs() throws Exception;

	/**
	 * Gets a list of contact-us information for all applications
	 * in the fn_app table, extended with any information in the fn_app_contact_us table.
	 * 
	 * @return List of AppContactUsItem, one for each item in fn_app table.
	 * @throws Exception
	 */
	public List<AppContactUsItem> getAppsAndContacts() throws Exception;
	
	public List<AppCategoryFunctionsItem> getAppCategoryFunctions() throws Exception;
	
	public String saveAppContactUs(List<AppContactUsItem> contactUs) throws Exception;
	
	public String saveAppContactUs(AppContactUsItem contactUs) throws Exception;

	public String deleteContactUs(Long id) throws Exception;
	
}
