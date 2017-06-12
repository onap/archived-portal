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

import java.util.Set;

import org.openecomp.portalsdk.core.domain.MenuData;
import org.openecomp.portalapp.portal.domain.EPUser;

public interface EPLeftMenuService {
	
	/**
	 * Builds a JSON suitable for populating the front-end left menu from the supplied set of menu items.
	 * @param user 
	 * 
	 * @param fullMenuSet
	 * @param roleFunctionSet 
	 * @return JSON String of this form:
	 * <PRE>
	 * {"navItems":[
	 * 		{"name":"Home","imageSrc":"icon-location-pin","state":"root.applicationsHome"},
	 * 	...
	 * ] }
	 * </PRE>
	 */
	String getLeftMenuItems(EPUser user, Set<MenuData> fullMenuSet, Set<String> roleFunctionSet);
}
