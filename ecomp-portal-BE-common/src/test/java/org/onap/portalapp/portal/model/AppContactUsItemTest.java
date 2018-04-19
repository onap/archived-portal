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
package org.onap.portalapp.portal.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.onap.portalapp.portal.ecomp.model.AppContactUsItem;

public class AppContactUsItemTest {
	
	public AppContactUsItem mockAppContactUsItem(){
		AppContactUsItem appContactUsItem = new AppContactUsItem();
				
		appContactUsItem.setAppId((long)1);
		appContactUsItem.setAppName("test");
		appContactUsItem.setDescription("test");
		appContactUsItem.setContactName("test");
		appContactUsItem.setContactEmail("test");
		appContactUsItem.setUrl("test");
		appContactUsItem.setActiveYN("test");
		
		return appContactUsItem;
	}

	@Test
	public void appContactUsItemTest(){
		AppContactUsItem appContactUsItem = mockAppContactUsItem();
		
		AppContactUsItem appContactUsItem1 = new AppContactUsItem();
		appContactUsItem1.setAppId((long)1);
		appContactUsItem1.setAppName("test");
		appContactUsItem1.setDescription("test");
		appContactUsItem1.setContactName("test");
		appContactUsItem1.setContactEmail("test");
		appContactUsItem1.setUrl("test");
		appContactUsItem1.setActiveYN("test");
		
		assertEquals(appContactUsItem.getAppId(), appContactUsItem1.getAppId());
		assertEquals(appContactUsItem.getAppName(), appContactUsItem1.getAppName());
		assertEquals(appContactUsItem.getDescription(), appContactUsItem1.getDescription());
		assertEquals(appContactUsItem.getContactName(), appContactUsItem1.getContactName());
		assertEquals(appContactUsItem.getContactEmail(), appContactUsItem1.getContactEmail());
		assertEquals(appContactUsItem.getUrl(), appContactUsItem1.getUrl());
		assertEquals(appContactUsItem.getActiveYN(), appContactUsItem1.getActiveYN());
		assertEquals(appContactUsItem.toString(), "AppContactUsItem [appId=1, appName=test, description=test, contactName=test, contactEmail=test, url=test, activeYN=test]");
		assertEquals(appContactUsItem.hashCode(), appContactUsItem1.hashCode());
		assertTrue(appContactUsItem.equals(appContactUsItem1));
	}
}
