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
 * 
 */
package org.onap.portalapp.portal.service;

import java.util.Set;

import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalsdk.core.domain.MenuData;

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
