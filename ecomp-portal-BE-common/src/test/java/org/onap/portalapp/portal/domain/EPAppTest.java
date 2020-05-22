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
		epApp.setAppDescription(TEST);
		epApp.setAppNotes(TEST);
		epApp.setLandingPage(TEST);
		epApp.setAlternateLandingPage(TEST);
		epApp.setAppRestEndpoint(TEST);
		epApp.setMlAppName(TEST);
		epApp.setMlAppAdminId(TEST);
		epApp.setMotsId((long)1);
		epApp.setAppBasicAuthUsername(TEST);
		epApp.setAppBasicAuthPassword(TEST);
		epApp.setOpen(false);
		epApp.setEnabled(false);
		epApp.setUebTopicName(TEST);
		epApp.setUebSecret(TEST);
		epApp.setAppType(EpAppType.GUI);
		epApp.setRolesInAAF(false);
		epApp.setNameSpace(TEST);
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
		epApp.setAppDescription(epApp1.getAppDescription());
		epApp.setAppNotes(epApp1.getAppNotes());
		epApp.setLandingPage(epApp1.getLandingPage());
		epApp.setAlternateLandingPage(epApp1.getAlternateLandingPage());
		epApp.setAppRestEndpoint(epApp1.getAppRestEndpoint());
		epApp.setMlAppName(epApp1.getMlAppName());
		epApp.setMlAppAdminId(epApp1.getMlAppAdminId());
		epApp.setMotsId(epApp1.getMotsId());
		epApp.setAppBasicAuthUsername(epApp1.getAppBasicAuthUsername());
		epApp.setAppBasicAuthPassword(epApp1.getAppBasicAuthPassword());
		epApp.setOpen(epApp1.getOpen());
		epApp.setEnabled(epApp1.getEnabled());
		epApp.setUebTopicName(epApp1.getUebTopicName());
		epApp.setUebSecret(epApp1.getUebSecret());
		epApp.setAppType(epApp1.getAppType());
		epApp.setRolesInAAF(epApp1.getRolesInAAF());
		epApp.setNameSpace(epApp1.getNameSpace());
	
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
		assertEquals(epApp.getAppDescription(), TEST);
		assertEquals(epApp.getAppNotes(), TEST);
		assertEquals(epApp.getLandingPage(), TEST);
		assertEquals(epApp.getAlternateLandingPage(), TEST);
		assertEquals(epApp.getAppRestEndpoint(), TEST);
		assertEquals(epApp.getMlAppName(), TEST);
		assertEquals(epApp.getMlAppAdminId(), TEST);
		assertEquals(epApp.getMotsId(), new Long(1));
		assertEquals(epApp.getAppBasicAuthUsername(), TEST);
		assertEquals(epApp.getAppBasicAuthPassword(), TEST);
		assertEquals(epApp.getOpen(), false);
		assertEquals(epApp.getEnabled(), false);
		assertEquals(epApp.getUebTopicName(), TEST);
		assertEquals(epApp.getUebSecret(), TEST);
		assertEquals(epApp.getAppType(), Integer.valueOf(1));
		assertEquals(epApp.getRolesInAAF(), false);
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
		
		
		epApp.setAppBasicAuthUsername(null);
		assertFalse(epApp.equals(epApp1));
		epApp.setLandingPage(null);
		assertFalse(epApp.equals(epApp1));
		epApp.setUebTopicName(null);
		assertFalse(epApp.equals(epApp1));
		
		epApp.setUebSecret(null);
		assertFalse(epApp.equals(epApp1));
		epApp.setUebKey(null);
		assertFalse(epApp.equals(epApp1));
		epApp.setOpen(null);
		assertFalse(epApp.equals(epApp1));
		epApp.setAppNotes(null);
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
		epApp.setAppDescription(null);
		assertFalse(epApp.equals(epApp1));
		epApp.setContactUs(null);
		assertFalse(epApp.equals(epApp1));
		
		epApp.setRolesInAAF(null);
		assertFalse(epApp.equals(epApp1));
		
		epApp.setAppType(null);
		assertFalse(epApp.equals(epApp1));
		epApp.setAppRestEndpoint(null);
		assertFalse(epApp.equals(epApp1));
		epApp.setAppBasicAuthPassword(null);
		assertFalse(epApp.equals(epApp1));
		
		epApp.setAlternateLandingPage(null);
		assertFalse(epApp.equals(epApp1));
	}	
}
