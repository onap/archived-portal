/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.portal.service;

import java.util.List;

import org.onap.portalapp.portal.domain.WidgetCatalogParameter;

public interface WidgetParameterService {
	
	/**
	 * Saves the specified user-defined widget parameters to the table ep_widget_parameters
	 * 
	 * @param newParameter
	 */
	void saveUserParameter(WidgetCatalogParameter newParameter);
	
	/**
	 * Gets the specified user-defined widget parameter where paramId is used from all users 
	 * 
	 * @param paramId
	 * @return
	 * List of widget parameters
	 */
	List<WidgetCatalogParameter> getUserParameterById(Long paramId);
	
	
	/**
	 * Deletes the specified user-defined widget parameters from ep_widget_parameters table 
	 * where paramId is used
	 * 
	 * @param paramId
	 */
	void deleteUserParameterById(Long paramId);

	
	/**
	 * Gets the specified user-defined widget parameter where paramId is used from the specified
	 * user with userId 
	 * @param widgetId
	 * @param userId
	 * @param paramId
	 * @return WidgetCatalogParameter
	 */
	WidgetCatalogParameter getUserParamById(Long widgetId, Long userId, Long paramId);
	
}
