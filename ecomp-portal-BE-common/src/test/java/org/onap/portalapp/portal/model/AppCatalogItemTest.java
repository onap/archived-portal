/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
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

import static org.junit.Assert.*;

import org.junit.Test;
import org.onap.portalapp.portal.ecomp.model.AppCatalogItem;

public class AppCatalogItemTest {

	public AppCatalogItem mockAppCatalogItem(){
		AppCatalogItem appCatalogItem = new AppCatalogItem();
		
		appCatalogItem.setAlternateUrl("test");
		appCatalogItem.setMlAppName("test");
		
		return appCatalogItem;
	}
	
	@Test
	public void appCatalogItemTest(){
		AppCatalogItem appCatalogItem = mockAppCatalogItem();
		
		AppCatalogItem appCatalogItem1 = new AppCatalogItem();
		appCatalogItem1.setAlternateUrl("test");
		appCatalogItem1.setMlAppName("test");
		
		assertEquals(appCatalogItem.getAlternateUrl(), appCatalogItem1.getAlternateUrl());
		assertEquals(appCatalogItem.getMlAppName(), appCatalogItem1.getMlAppName());

		assertEquals(appCatalogItem.toString(), "AppCatalogItem [id=null, name=null, access=null, select=null, pending=null]");
		assertEquals(appCatalogItem.hashCode(), appCatalogItem1.hashCode());
		assertTrue(appCatalogItem.equals(appCatalogItem1));		
	}
	
	@Test
	public void hashCodeTest(){
		AppCatalogItem appCatalogItem = new AppCatalogItem();
		appCatalogItem.setAccess(true);
		appCatalogItem.setAlternateUrl("test");
		appCatalogItem.setDescription("test");
		appCatalogItem.setId(123L);
		appCatalogItem.setImageUrl("test");
		appCatalogItem.setName("test");
		appCatalogItem.setNotes("test");
		appCatalogItem.setOpen(false);
		appCatalogItem.setPending(false);
		appCatalogItem.setRestricted(false);
		appCatalogItem.setSelect(false);
		appCatalogItem.setUrl("test");
		appCatalogItem.hashCode();
	}
	
	@Test
	public void equalsTest(){
		AppCatalogItem appCatalogItem = new AppCatalogItem();
		AppCatalogItem appCatalogItem1 = new AppCatalogItem();		
		appCatalogItem.equals(appCatalogItem1);
	}
	
	@Test
	public void equalsTest1(){
		AppCatalogItem appCatalogItem = new AppCatalogItem();
		AppCatalogItem appCatalogItem1 = new AppCatalogItem();
		appCatalogItem.setAccess(true);
		appCatalogItem.setAlternateUrl("test");
		appCatalogItem.setDescription("test");
		appCatalogItem.setId(123L);
		appCatalogItem.setImageUrl("test");
		appCatalogItem.setName("test");
		appCatalogItem.setNotes("test");
		appCatalogItem.setOpen(false);
		appCatalogItem.setPending(false);
		appCatalogItem.setRestricted(false);
		appCatalogItem.setSelect(false);
		appCatalogItem.setUrl("test");
		appCatalogItem.equals(appCatalogItem1);
	}
	
	@Test
	public void equalsTest2(){
		AppCatalogItem appCatalogItem = new AppCatalogItem();
		AppCatalogItem appCatalogItem1 = new AppCatalogItem();
		appCatalogItem.setAlternateUrl("test");
		appCatalogItem.setDescription("test");
		appCatalogItem.setId(123L);
		appCatalogItem.setImageUrl("test");
		appCatalogItem.setName("test");
		appCatalogItem.setNotes("test");
		appCatalogItem.setOpen(false);
		appCatalogItem.setPending(false);
		appCatalogItem.setRestricted(false);
		appCatalogItem.setSelect(false);
		appCatalogItem.setUrl("test");
		appCatalogItem.equals(appCatalogItem1);
	}
	
	@Test
	public void equalsTest3(){
		AppCatalogItem appCatalogItem = new AppCatalogItem();
		AppCatalogItem appCatalogItem1 = new AppCatalogItem();
		appCatalogItem.setDescription("test");
		appCatalogItem.setId(123L);
		appCatalogItem.setImageUrl("test");
		appCatalogItem.setName("test");
		appCatalogItem.setNotes("test");
		appCatalogItem.setOpen(false);
		appCatalogItem.setPending(false);
		appCatalogItem.setRestricted(false);
		appCatalogItem.setSelect(false);
		appCatalogItem.setUrl("test");
		appCatalogItem.equals(appCatalogItem1);
	}
	
	@Test
	public void equalsTest4(){
		AppCatalogItem appCatalogItem = new AppCatalogItem();
		AppCatalogItem appCatalogItem1 = new AppCatalogItem();
		appCatalogItem.setId(123L);
		appCatalogItem.setImageUrl("test");
		appCatalogItem.setName("test");
		appCatalogItem.setNotes("test");
		appCatalogItem.setOpen(false);
		appCatalogItem.setPending(false);
		appCatalogItem.setRestricted(false);
		appCatalogItem.setSelect(false);
		appCatalogItem.setUrl("test");
		appCatalogItem.equals(appCatalogItem1);
	}
	
	@Test
	public void equalsTest5(){
		AppCatalogItem appCatalogItem = new AppCatalogItem();
		AppCatalogItem appCatalogItem1 = new AppCatalogItem();
		appCatalogItem.setImageUrl("test");
		appCatalogItem.setName("test");
		appCatalogItem.setNotes("test");
		appCatalogItem.setOpen(false);
		appCatalogItem.setPending(false);
		appCatalogItem.setRestricted(false);
		appCatalogItem.setSelect(false);
		appCatalogItem.setUrl("test");
		appCatalogItem.equals(appCatalogItem1);
	}
	
	@Test
	public void equalsTest6(){
		AppCatalogItem appCatalogItem = new AppCatalogItem();
		AppCatalogItem appCatalogItem1 = new AppCatalogItem();
		appCatalogItem.setName("test");
		appCatalogItem.setNotes("test");
		appCatalogItem.setOpen(false);
		appCatalogItem.setPending(false);
		appCatalogItem.setRestricted(false);
		appCatalogItem.setSelect(false);
		appCatalogItem.setUrl("test");
		appCatalogItem.equals(appCatalogItem1);
	}
	
	@Test
	public void equalsTest7(){
		AppCatalogItem appCatalogItem = new AppCatalogItem();
		AppCatalogItem appCatalogItem1 = new AppCatalogItem();
		appCatalogItem.setNotes("test");
		appCatalogItem.setOpen(false);
		appCatalogItem.setPending(false);
		appCatalogItem.setRestricted(false);
		appCatalogItem.setSelect(false);
		appCatalogItem.setUrl("test");
		appCatalogItem.equals(appCatalogItem1);
	}
	
	@Test
	public void equalsTest8(){
		AppCatalogItem appCatalogItem = new AppCatalogItem();
		AppCatalogItem appCatalogItem1 = new AppCatalogItem();
		appCatalogItem.setOpen(false);
		appCatalogItem.setPending(false);
		appCatalogItem.setRestricted(false);
		appCatalogItem.setSelect(false);
		appCatalogItem.setUrl("test");
		appCatalogItem.equals(appCatalogItem1);
	}
	
	@Test
	public void equalsTest9(){
		AppCatalogItem appCatalogItem = new AppCatalogItem();
		AppCatalogItem appCatalogItem1 = new AppCatalogItem();
		appCatalogItem.setPending(false);
		appCatalogItem.setRestricted(false);
		appCatalogItem.setSelect(false);
		appCatalogItem.setUrl("test");
		appCatalogItem.equals(appCatalogItem1);
	}
	
	@Test
	public void equalsTest10(){
		AppCatalogItem appCatalogItem = new AppCatalogItem();
		AppCatalogItem appCatalogItem1 = new AppCatalogItem();
		appCatalogItem.setRestricted(false);
		appCatalogItem.setSelect(false);
		appCatalogItem.setUrl("test");
		appCatalogItem.equals(appCatalogItem1);
	}
	
	@Test
	public void equalsTest11(){
		AppCatalogItem appCatalogItem = new AppCatalogItem();
		AppCatalogItem appCatalogItem1 = new AppCatalogItem();
		appCatalogItem.setSelect(false);
		appCatalogItem.setUrl("test");
		appCatalogItem.equals(appCatalogItem1);
	}
	
	@Test
	public void equalsTest12(){
		AppCatalogItem appCatalogItem = new AppCatalogItem();
		AppCatalogItem appCatalogItem1 = new AppCatalogItem();
		appCatalogItem.setUrl("test");
		appCatalogItem.equals(appCatalogItem1);
	}
	
	@Test
	public void equalsTest13(){
		AppCatalogItem appCatalogItem = new AppCatalogItem();
		AppCatalogItem appCatalogItem1 = new AppCatalogItem();
		appCatalogItem.setUrl("test");
		appCatalogItem.equals(appCatalogItem1);
	}
	
	@Test
	public void equalsTest14(){
		AppCatalogItem appCatalogItem = new AppCatalogItem();
		AppCatalogItem appCatalogItem1 = new AppCatalogItem();
		appCatalogItem.equals(appCatalogItem1);
	}
}
