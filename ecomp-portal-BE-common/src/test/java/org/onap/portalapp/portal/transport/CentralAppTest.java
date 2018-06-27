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
package org.onap.portalapp.portal.transport;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;
import org.onap.portalapp.portal.transport.CentralApp;

public class CentralAppTest {
	
	CentralApp centralApp=new CentralApp();

	public CentralApp mockCentralApp(){
		CentralApp centralApp = new CentralApp((long)1, null, null, (long)1, (long)1, (long)1, "test", "test", "test", "test", "test", "test", "test", "test", "test", 
				"test", "test", "test", "test", null, "test", "test", "test", "test");
				return centralApp;
	}
	
	@Test
	public void centralAppTest(){
		CentralApp centralApp = mockCentralApp();
		
		CentralApp centralApp1 =  new CentralApp((long)1, null, null, (long)1, (long)1, (long)1, "test", "test", "test", "test", "test", "test", "test", "test", "test", 
				"test", "test", "test", "test", null, "test", "test", "test", "test");
		
		assertEquals(centralApp.getId(), new Long(1));
		assertEquals(centralApp.getCreatedId(), new Long(1));
		assertEquals(centralApp.getModifiedId(), new Long(1));
		assertEquals(centralApp.getRowNum(), new Long(1));
		assertEquals(centralApp.getName(), "test");
		assertEquals(centralApp.getImageUrl(), "test");
		assertEquals(centralApp.getDescription(), "test");
		assertEquals(centralApp.getNotes(), "test");
		assertEquals(centralApp.getUrl(), "test");
		assertEquals(centralApp.getAlternateUrl(), "test");
		assertEquals(centralApp.getRestEndpoint(), "test");
		assertEquals(centralApp.getMlAppName(), "test");
		assertEquals(centralApp.getMlAppAdminId(), "test");
		assertEquals(centralApp.getMotsId(), "test");
		assertEquals(centralApp.getAppPassword(), "test");
		assertEquals(centralApp.getOpen(), "test");
		assertEquals(centralApp.getEnabled(), "test");
		assertEquals(centralApp.getUsername(), "test");
		assertEquals(centralApp.getUebKey(), "test");
		assertEquals(centralApp.getUebSecret(), "test");
		assertEquals(centralApp.getUebTopicName(), "test");
		
		assertTrue(centralApp.equals(centralApp1));
		assertEquals(centralApp.hashCode(), centralApp1.hashCode());
	}
	
	
	@Test
	public void unt_IdTest(){
		Long defaultValue=123L;
		centralApp.setId(defaultValue);
		assertEquals(defaultValue, centralApp.getId());
	}
	
	@Test
	public void unt_createdTest(){
		Date defaultValue=new Date();
		centralApp.setCreated(defaultValue);
		assertEquals(defaultValue, centralApp.getCreated());
	}
	
	@Test
	public void unt_modifiedTest(){
		Date defaultValue=new Date();
		centralApp.setCreated(defaultValue);
		assertEquals(defaultValue, centralApp.getCreated());
	}
	
	@Test
	public void unt_craetedIdTest(){
		Long defaultValue=123L;
		centralApp.setCreatedId(defaultValue);
		assertEquals(defaultValue, centralApp.getCreatedId());
	}
	
	@Test
	public void unt_modifiedIdTest(){
		Long defaultValue=123L;
		centralApp.setModifiedId(defaultValue);
		assertEquals(defaultValue, centralApp.getModifiedId());
	}
	
	@Test
	public void unt_rowNumTest(){
		Long defaultValue=123L;
		centralApp.setRowNum(defaultValue);
		assertEquals(defaultValue, centralApp.getRowNum());
	}
	

	@Test
	public void unt_nameTest(){
		String defaultValue="test";
		centralApp.setName(defaultValue);
		assertEquals(defaultValue, centralApp.getName());
	}
	
	@Test
	public void unt_ImageUrlTest(){
		String defaultValue="test";
		centralApp.setImageUrl(defaultValue);
		assertEquals(defaultValue, centralApp.getImageUrl());
	}
	
	@Test
	public void unt_descriptionTest(){
		String defaultValue="test";
		centralApp.setDescription(defaultValue);
		assertEquals(defaultValue, centralApp.getDescription());
	}
	
	@Test
	public void unt_notesTest(){
		String defaultValue="test";
		centralApp.setNotes(defaultValue);
		assertEquals(defaultValue, centralApp.getNotes());
	}
	
	@Test
	public void unt_urlTest(){
		String defaultValue="testUrl";
		centralApp.setUrl(defaultValue);
		assertEquals(defaultValue, centralApp.getUrl());
	}
	
	@Test
	public void unt_alternateUrlTest(){
		String defaultValue="testUrl";
		centralApp.setAlternateUrl(defaultValue);
		assertEquals(defaultValue, centralApp.getAlternateUrl());
	}
	
	@Test
	public void unt_restendpointTest(){
		String defaultValue="testUrl";
		centralApp.setRestEndpoint(defaultValue);
		assertEquals(defaultValue, centralApp.getRestEndpoint());
	}
	
	@Test
	public void unt_mlAppNameTest(){
		String defaultValue="testAppName";
		centralApp.setMlAppName(defaultValue);
		assertEquals(defaultValue, centralApp.getMlAppName());
	}
	
	@Test
	public void unt_mlAppAdminIdTest(){
		String defaultValue="testAppAdminId";
		centralApp.setMlAppAdminId(defaultValue);
		assertEquals(defaultValue, centralApp.getMlAppAdminId());
	}
	
	@Test
	public void unt_motsIdIdTest(){
		String defaultValue="testmotsid";
		centralApp.setMotsId(defaultValue);
		assertEquals(defaultValue, centralApp.getMotsId());
	}
	
	@Test
	public void unt_appPasswordTest(){
		String defaultValue="TestAppPassword";
		centralApp.setAppPassword(defaultValue);
		assertEquals(defaultValue, centralApp.getAppPassword());
	}
	
	@Test
	public void unt_openTest(){
		String defaultValue="Testopen";
		centralApp.setOpen(defaultValue);
		assertEquals(defaultValue, centralApp.getOpen());
	}
	
	@Test
	public void unt_enabledTest(){
		String defaultValue="Testenable";
		centralApp.setEnabled(defaultValue);
		assertEquals(defaultValue, centralApp.getEnabled());
	}
	
	@Test
	public void unt_thumbnailTest(){
		byte[] defaultValue={1,2,3};
		centralApp.setThumbnail(defaultValue);
		assertEquals(defaultValue, centralApp.getThumbnail());
	}
	
	@Test
	public void unt_userNameTest(){
		String defaultValue="Testusername";
		centralApp.setUsername(defaultValue);
		assertEquals(defaultValue, centralApp.getUsername());
	}
	
	@Test
	public void unt_uebKeyTest(){
		String defaultValue="Testuebkey";
		centralApp.setUebKey(defaultValue);
		assertEquals(defaultValue, centralApp.getUebKey());
	}
	
	@Test
	public void unt_uebSecreteTest(){
		String defaultValue="Testuebscrete";
		centralApp.setUebSecret(defaultValue);
		assertEquals(defaultValue, centralApp.getUebSecret());
	}
	
	@Test
	public void unt_uebTopicNameTest(){
		String defaultValue="Testuebtopicname";
		centralApp.setUebTopicName(defaultValue);
		assertEquals(defaultValue, centralApp.getUebTopicName());
	}
	
	@Test
	public void unt_hashCodeWithNullTest(){
		CentralApp centralApp=new CentralApp(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		
		CentralApp centralApp1=new CentralApp(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

		assertEquals(centralApp.hashCode(), centralApp1.hashCode());
		assertTrue(centralApp.equals(centralApp1));
		
	}
	
	@Test
	public void unt_hashCodeTest(){
		CentralApp centralApp=new CentralApp((long)1, null, null, (long)1, (long)1, (long)1, "test", "test", "test", "test", "test", "test", "test", "test", "test", 
				"test", "test", "test", "test", null, "test", "test", "test", "test");
		
		CentralApp centralApp1=new CentralApp((long)1, null, null, (long)1, (long)1, (long)1, "test", "test", "test", "test", "test", "test", "test", "test", "test", 
				"test", "test", "test", "test", null, "test", "test", "test", "test");

		assertEquals(centralApp.hashCode(), centralApp1.hashCode());
		assertTrue(centralApp.equals(centralApp1));
		
	}
	
	@Test
	public void unt_hashCodeTestWithNull(){
		CentralApp centralApp=new CentralApp((long)1, null, null, (long)1, (long)1, (long)1, "test", "test", "test", "test", "test", "test", "test", "test", "test", 
				"test", "test", "test", "test", null, "test", "test", "test", "test");
		CentralApp centralApp1=new CentralApp((long)1, null, null, (long)1, (long)1, (long)1, "test", "test", "test", "test", "test", "test", "test", "test", "test", 
				"test", "test", "test", "test", null, "test", "test", "test", "test");
		assertEquals(centralApp.hashCode(), centralApp1.hashCode());
		assertFalse(centralApp.equals(null));
		
	}
	
	@Test
	public void unt_hashCodeTestWithNull1(){
		CentralApp centralApp=new CentralApp(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		CentralApp centralApp1=new CentralApp((long)1, null, null, (long)1, (long)1, (long)1, "test", "test", "test", "test", "test", "test", "test", "test", "test", 
				"test", "test", "test", "test", null, "test", "test", "test", "test");
		assertFalse(centralApp.equals(centralApp1));
		
	}
	
	@Test
	public void unt_hashCodeTestWithalternateURL(){
		CentralApp centralApp=new CentralApp(null, null, null, null, null, null, null, null, null, null, null, "test1", null, null, null, null, null, null, null, null, null, null, null, null);
		CentralApp centralApp1=new CentralApp((long)1, null, null, (long)1, (long)1, (long)1, "test", "test", "test", "test", "test", "test", "test", "test", "test", 
				"test", "test", "test", "test", null, "test", "test", "test", "test");
		assertFalse(centralApp.equals(centralApp1));
		
	}
	
	@Test
	public void unt_hashCodeTestWithpassword(){
		CentralApp centralApp=new CentralApp(null, null, null, null, null, null, null, null, null, null, null, "test", null, null, null, null, "testPass", null, null, null, null, null, null, null);
		CentralApp centralApp1=new CentralApp((long)1, null, null, (long)1, (long)1, (long)1, "test", "test", "test", "test", "test", "test", "test", "test", "test", 
				"test", "test", "test", "test", null, "test", "test", "test", "test");
		assertFalse(centralApp.equals(centralApp1));
		
	}
	
	@Test
	public void unt_hashCodeTestWithcreateId(){
		CentralApp centralApp=new CentralApp(null, null, null, 123L, null, null, null, null, null, null, null, "test", null, null, null, null, "test", null, null, null, null, null, null, null);
		CentralApp centralApp1=new CentralApp((long)1, null, null, (long)1, (long)1, (long)1, "test", "test", "test", "test", "test", "test", "test", "test", "test", 
				"test", "test", "test", "test", null, "test", "test", "test", "test");
		assertFalse(centralApp.equals(centralApp1));
		
	}
	
	@Test
	public void unt_hashCodeTestWithcreateId1(){
		CentralApp centralApp=new CentralApp(12L, null, null, 123L, 123L, 123L, "test1", "test1", "test1", "test1", "test1", "test", "tests1", "test1", "test1", null, "test", null, null, null, null, null, null, null);
		CentralApp centralApp1=new CentralApp((long)1, null, null, (long)1, (long)1, (long)1, "test", "test", "test", "test", "test", "test", "test", "test", "test", 
				"test", "test", "test", "test", null, "test", "test", "test", "test");
		assertFalse(centralApp.equals(centralApp1));
		
	}
}
