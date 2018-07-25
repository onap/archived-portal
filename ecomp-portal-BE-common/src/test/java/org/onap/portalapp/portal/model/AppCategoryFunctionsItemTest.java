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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.onap.portalapp.portal.ecomp.model.AppCategoryFunctionsItem;

public class AppCategoryFunctionsItemTest {
	
	private static final String TEST="test";

	private  AppCategoryFunctionsItem mockAppCategoryFunctionsItem(){
		AppCategoryFunctionsItem appCategoryFunctionsItem = new AppCategoryFunctionsItem();
		
		appCategoryFunctionsItem.setRowId(TEST);
		appCategoryFunctionsItem.setAppId(TEST);
		appCategoryFunctionsItem.setApplication(TEST);
		appCategoryFunctionsItem.setFunctions(TEST);
		appCategoryFunctionsItem.setCategory(TEST);
		
		return appCategoryFunctionsItem;
	}
	
	@Test
	public void appCategoryFunctionsItemTest(){
		AppCategoryFunctionsItem appCategoryFunctionsItem = mockAppCategoryFunctionsItem();
		
		AppCategoryFunctionsItem appCategoryFunctionsItem1 = new AppCategoryFunctionsItem();
		
		appCategoryFunctionsItem1.setRowId(appCategoryFunctionsItem.getRowId());
		appCategoryFunctionsItem1.setAppId(appCategoryFunctionsItem.getAppId());
		appCategoryFunctionsItem1.setApplication(appCategoryFunctionsItem.getApplication());
		appCategoryFunctionsItem1.setFunctions(appCategoryFunctionsItem.getFunctions());
		appCategoryFunctionsItem1.setCategory(appCategoryFunctionsItem.getCategory());
		assertNotNull(appCategoryFunctionsItem.toString());
		assertEquals(appCategoryFunctionsItem.hashCode(), appCategoryFunctionsItem1.hashCode());
		assertTrue(appCategoryFunctionsItem1.equals(appCategoryFunctionsItem));
		
		assertFalse(appCategoryFunctionsItem1.equals(null));
		appCategoryFunctionsItem1.setRowId(null);
		assertFalse(appCategoryFunctionsItem1.equals(appCategoryFunctionsItem));
		appCategoryFunctionsItem1.setFunctions(null);
		assertFalse(appCategoryFunctionsItem1.equals(appCategoryFunctionsItem));
		appCategoryFunctionsItem1.setCategory(null);
		assertFalse(appCategoryFunctionsItem1.equals(appCategoryFunctionsItem));
		appCategoryFunctionsItem1.setApplication(null);
		
		assertFalse(appCategoryFunctionsItem1.equals(appCategoryFunctionsItem));
appCategoryFunctionsItem1.setAppId(null);
		
		assertFalse(appCategoryFunctionsItem1.equals(appCategoryFunctionsItem));

	}
}
