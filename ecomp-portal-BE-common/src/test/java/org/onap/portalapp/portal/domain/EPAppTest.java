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
package org.onap.portalapp.portal.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.onap.portalapp.portal.domain.EPApp;

public class EPAppTest {
	
	private static final String TEST="test";

	public EPApp mockEPApp(EPApp epApp){
		epApp.setId(1l);			
		epApp.setName(TEST);
		epApp.setImageUrl(TEST);
		epApp.setDescription(TEST);
		epApp.setNotes(TEST);
		epApp.setUrl(TEST);
		epApp.setAlternateUrl(TEST);
		epApp.setAppRestEndpoint(TEST);
		epApp.setMlAppName(TEST);
		epApp.setMlAppAdminId(TEST);
		epApp.setMotsId((long)1);
		epApp.setUsername(TEST);
		epApp.setAppPassword(TEST);
		epApp.setOpen(false);
		epApp.setEnabled(false);
		epApp.setUebTopicName(TEST);
		epApp.setUebSecret(TEST);
		epApp.setAppType(1);
		epApp.setCentralAuth(false);
		epApp.setNameSpace(TEST);
		epApp.setRestrictedApp(true);
		epApp.setRestrictedApp(false);
		epApp.setAppType(null);
		epApp.setOpen(null);
		epApp.setThumbnail(TEST.getBytes());
		epApp.setUebKey(TEST);
		
		return epApp;
	}
	
	@Test
	public void epAppTest(){
		 EPApp epApp1 = new EPApp();
		  epApp1 = mockEPApp(epApp1);
		EPApp epApp=new EPApp();
		 
		epApp.setId(epApp1.getId());			
		epApp.setName(epApp1.getName());
		epApp.setImageUrl(epApp1.getImageUrl());
		epApp.setDescription(epApp1.getDescription());
		epApp.setNotes(epApp1.getNotes());
		epApp.setUrl(epApp1.getUrl());
		epApp.setAlternateUrl(epApp1.getAlternateUrl());
		epApp.setAppRestEndpoint(epApp1.getAppRestEndpoint());
		epApp.setMlAppName(epApp1.getMlAppName());
		epApp.setMlAppAdminId(epApp1.getMlAppAdminId());
		epApp.setMotsId(epApp1.getMotsId());
		epApp.setUsername(epApp1.getUsername());
		epApp.setAppPassword(epApp1.getAppPassword());
		epApp.setOpen(epApp1.getOpen());
		epApp.setEnabled(epApp1.getEnabled());
		epApp.setUebTopicName(epApp1.getUebTopicName());
		epApp.setUebSecret(epApp1.getUebSecret());
		epApp.setAppType(epApp1.getAppType());
		epApp.setCentralAuth(epApp1.getCentralAuth());
		epApp.setNameSpace(epApp1.getNameSpace());
		epApp.setRestrictedApp(epApp1.isRestrictedApp());
	
		epApp.setAppType(epApp1.getAppType());
		
		epApp.setThumbnail(epApp1.getThumbnail());
		epApp.setUebKey(epApp1.getUebKey());
		epApp.compareTo(epApp1);
		assertEquals(epApp.hashCode(), epApp1.hashCode());
		assertTrue(epApp.equals(epApp1));
		assertFalse(epApp.equals(null));
	
		
		assertEquals(epApp.getName(), TEST);
		assertEquals(epApp.getId(), Long.valueOf(1l));
		assertEquals(epApp.getImageUrl(), TEST);
		assertEquals(epApp.getDescription(), TEST);
		assertEquals(epApp.getNotes(), TEST);
		assertEquals(epApp.getUrl(), TEST);
		assertEquals(epApp.getAlternateUrl(), TEST);
		assertEquals(epApp.getAppRestEndpoint(), TEST);
		assertEquals(epApp.getMlAppName(), TEST);
		assertEquals(epApp.getMlAppAdminId(), TEST);
		assertEquals(epApp.getMotsId(), new Long(1));
		assertEquals(epApp.getUsername(), TEST);
		assertEquals(epApp.getAppPassword(), TEST);
		assertEquals(epApp.getOpen(), false);
		assertEquals(epApp.getEnabled(), false);
		assertEquals(epApp.getUebTopicName(), TEST);
		assertEquals(epApp.getUebSecret(), TEST);
		assertEquals(epApp.getAppType(), Integer.valueOf(1));
		assertEquals(epApp.getCentralAuth(), false);
		assertEquals(epApp.getNameSpace(), TEST);
		assertEquals(epApp.getUebKey(), TEST);
		
		assertEquals(epApp.getOpen(), false);
		assertEquals(epApp.isRestrictedApp(), false);
		assertEquals(epApp.hashCode(), epApp1.hashCode());
		assertTrue(epApp.equals(epApp1));
		//epApp.compareTo(epApp1);
		assertNotNull(epApp.toString());
		epApp.setContactUs(new AppContactUs());
		assertNotNull(epApp.getContactUs());
		
		
		epApp.setUsername(null);
		assertFalse(epApp.equals(epApp1));
		epApp.setUrl(null);
		assertFalse(epApp.equals(epApp1));
		epApp.setUebTopicName(null);
		assertFalse(epApp.equals(epApp1));
		
		epApp.setUebSecret(null);
		assertFalse(epApp.equals(epApp1));
		epApp.setUebKey(null);
		assertFalse(epApp.equals(epApp1));
		epApp.setOpen(null);
		assertFalse(epApp.equals(epApp1));
		epApp.setNotes(null);
		assertFalse(epApp.equals(epApp1));
		epApp.setNameSpace(null);
		assertFalse(epApp.equals(epApp1));
		
		epApp.setName(null);
		assertFalse(epApp.equals(epApp1));
		epApp.setMotsId(null);
		assertFalse(epApp.equals(epApp1));
		epApp.setMlAppName(null);
		assertFalse(epApp.equals(epApp1));
		epApp.setMlAppAdminId(null);
		assertFalse(epApp.equals(epApp1));
		epApp.setImageUrl(null);
		assertFalse(epApp.equals(epApp1));
		epApp.setEnabled(null);
		assertFalse(epApp.equals(epApp1));
		epApp.setDescription(null);
		assertFalse(epApp.equals(epApp1));
		epApp.setContactUs(null);
		assertFalse(epApp.equals(epApp1));
		
		epApp.setCentralAuth(null);
		assertFalse(epApp.equals(epApp1));
		
		epApp.setAppType(null);
		assertFalse(epApp.equals(epApp1));
		epApp.setAppRestEndpoint(null);
		assertFalse(epApp.equals(epApp1));
		epApp.setAppPassword(null);
		assertFalse(epApp.equals(epApp1));
		
		epApp.setAlternateUrl(null);
		assertFalse(epApp.equals(epApp1));
	}	
}
