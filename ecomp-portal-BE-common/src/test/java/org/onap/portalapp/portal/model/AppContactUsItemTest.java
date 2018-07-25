/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017-2018 AT&T Intellectual Property. All rights reserved.
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
package org.onap.portalapp.portal.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.onap.portalapp.portal.ecomp.model.AppContactUsItem;

public class AppContactUsItemTest {
	
	private static final String TEST="test";
	
	public AppContactUsItem mockAppContactUsItem(){
		AppContactUsItem appContactUsItem = new AppContactUsItem();
				
		appContactUsItem.setAppId((long)1);
		appContactUsItem.setAppName(TEST);
		appContactUsItem.setDescription(TEST);
		appContactUsItem.setContactName(TEST);
		appContactUsItem.setContactEmail(TEST);
		appContactUsItem.setUrl(TEST);
		appContactUsItem.setActiveYN(TEST);
		
		return appContactUsItem;
	}

	
	@Test
	public void appContactUsItemTest(){
		AppContactUsItem appContactUsItem1 = mockAppContactUsItem();
		
		AppContactUsItem appContactUsItem = new AppContactUsItem();
		appContactUsItem.setAppId(appContactUsItem1.getAppId());
		appContactUsItem.setAppName(appContactUsItem1.getAppName());
		appContactUsItem.setDescription(appContactUsItem1.getDescription());
		appContactUsItem.setContactName(appContactUsItem1.getContactName());
		appContactUsItem.setContactEmail(appContactUsItem1.getContactEmail());
		appContactUsItem.setUrl(appContactUsItem1.getUrl());
		appContactUsItem.setActiveYN(appContactUsItem1.getActiveYN());
		assertNotNull(appContactUsItem.toString());
		
		assertEquals(appContactUsItem.hashCode(), appContactUsItem1.hashCode());
		assertTrue(appContactUsItem.equals(appContactUsItem1));
		assertFalse(appContactUsItem.equals(null));
		appContactUsItem.setUrl(null);
		assertFalse(appContactUsItem.equals(appContactUsItem1));
		appContactUsItem.setDescription(null);
		assertFalse(appContactUsItem.equals(appContactUsItem1));
		appContactUsItem.setContactName(null);
		assertFalse(appContactUsItem.equals(appContactUsItem1));
		appContactUsItem.setContactEmail(null);
		assertFalse(appContactUsItem.equals(appContactUsItem1));
		appContactUsItem.setAppName(null);
		assertFalse(appContactUsItem.equals(appContactUsItem1));
		appContactUsItem.setAppId(null);
		assertFalse(appContactUsItem.equals(appContactUsItem1));
		appContactUsItem.setActiveYN(null);
		assertFalse(appContactUsItem.equals(appContactUsItem1));
	}
}
