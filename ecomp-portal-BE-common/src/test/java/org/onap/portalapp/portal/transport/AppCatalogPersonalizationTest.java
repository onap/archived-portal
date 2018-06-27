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
import org.onap.portalapp.portal.transport.AppCatalogPersonalization;

public class AppCatalogPersonalizationTest {
	
	public AppCatalogPersonalization mockAppCatalogPersonalization(){
		AppCatalogPersonalization appCatalogPersonalization = new AppCatalogPersonalization();
		
		return appCatalogPersonalization;
	}
	
	@Test
	public void appCatalogPersonalizationTest(){
		AppCatalogPersonalization appCatalogPersonalization = mockAppCatalogPersonalization();
		
		AppCatalogPersonalization appCatalogPersonalization1 = new AppCatalogPersonalization();
		
		assertEquals(appCatalogPersonalization.hashCode(), appCatalogPersonalization1.hashCode());
		assertTrue(appCatalogPersonalization.equals(appCatalogPersonalization1));
	}
	
	@Test
	public void unt_hashCodeTest(){
		AppCatalogPersonalization appCatalogPersonalization=new AppCatalogPersonalization();
		appCatalogPersonalization.setAppId(123L);
		appCatalogPersonalization.setPending(true);
		appCatalogPersonalization.setSelect(true);
		AppCatalogPersonalization appCatalogPersonalization1 = new AppCatalogPersonalization();
		appCatalogPersonalization1.setAppId(123L);
		appCatalogPersonalization1.setPending(true);
		appCatalogPersonalization1.setSelect(true);
		assertEquals(appCatalogPersonalization.hashCode(), appCatalogPersonalization1.hashCode());
		assertTrue(appCatalogPersonalization.equals(appCatalogPersonalization1));
		
	}
	
	@Test
	public void appCatalogPersonalizationTestwithAppId(){
		AppCatalogPersonalization appCatalogPersonalization = mockAppCatalogPersonalization();
		AppCatalogPersonalization appCatalogPersonalization1 = new AppCatalogPersonalization();
		appCatalogPersonalization1.setAppId(123L);
		assertFalse(appCatalogPersonalization.equals(appCatalogPersonalization1));
	}
	
	@Test
	public void appCatalogPersonalizationTestwithpending(){
		AppCatalogPersonalization appCatalogPersonalization = mockAppCatalogPersonalization();
		AppCatalogPersonalization appCatalogPersonalization1 = new AppCatalogPersonalization();
		appCatalogPersonalization1.setPending(true);
		assertFalse(appCatalogPersonalization.equals(appCatalogPersonalization1));
	}
	
	@Test
	public void appCatalogPersonalizationTestwithSelect(){
		AppCatalogPersonalization appCatalogPersonalization = mockAppCatalogPersonalization();
		AppCatalogPersonalization appCatalogPersonalization1 = new AppCatalogPersonalization();
		appCatalogPersonalization1.setSelect(true);
		assertFalse(appCatalogPersonalization.equals(appCatalogPersonalization1));
	}

}
