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

import java.util.List;

import org.openecomp.portalapp.portal.domain.WidgetCatalogParameter;

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
