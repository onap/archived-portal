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
package org.onap.portalapp.portal.transport;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.onap.portalapp.portal.transport.EPAppsManualPreference;

public class EPAppsManualPreferenceTest {

	public EPAppsManualPreference mockEPAppsManualPreference(){
		EPAppsManualPreference epAppsManualPreference = new EPAppsManualPreference();
				
		epAppsManualPreference.setAppid((long)1);
		epAppsManualPreference.setCol(1);
		epAppsManualPreference.setHeaderText("test");
		epAppsManualPreference.setImageLink("test");
		epAppsManualPreference.setOrder(1);
		epAppsManualPreference.setRestrictedApp(false);
		epAppsManualPreference.setRow(1);
		epAppsManualPreference.setSizeX(1);
		epAppsManualPreference.setSizeY(1);
		epAppsManualPreference.setSubHeaderText("test");
		epAppsManualPreference.setUrl("test");
		epAppsManualPreference.setAddRemoveApps(false);
		
		return epAppsManualPreference;
	}
	
	@Test
	public void epAppsManualPreferenceTest(){
		
		EPAppsManualPreference epAppsManualPreference = mockEPAppsManualPreference();
		
		assertEquals(epAppsManualPreference.getAppid(), new Long(1));
		assertEquals(epAppsManualPreference.getCol(), 1);
		assertEquals(epAppsManualPreference.getHeaderText(), "test");
		assertEquals(epAppsManualPreference.getImageLink(), "test");
		assertEquals(epAppsManualPreference.getOrder(), 1);
		assertEquals(epAppsManualPreference.isRestrictedApp(), false);
		assertEquals(epAppsManualPreference.getRow(), 1);
		assertEquals(epAppsManualPreference.getSizeX(), 1);
		assertEquals(epAppsManualPreference.getSizeY(), 1);
		assertEquals(epAppsManualPreference.getSubHeaderText(), "test");
		assertEquals(epAppsManualPreference.getUrl(), "test");
		assertEquals(epAppsManualPreference.isAddRemoveApps(), false);
	}
}
