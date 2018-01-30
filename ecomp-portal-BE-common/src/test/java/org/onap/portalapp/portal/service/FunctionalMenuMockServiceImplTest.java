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
package org.onap.portalapp.portal.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.domain.FunctionalMenuItemWithAppID;
import org.onap.portalapp.portal.service.FunctionalMenuService;
import org.onap.portalapp.portal.service.FunctionalMenuServiceImpl;
import org.onap.portalapp.portal.transport.FavoritesFunctionalMenuItemJson;
import org.onap.portalapp.portal.transport.FunctionalMenuItem;
import org.onap.portalapp.portal.transport.FunctionalMenuRole;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SystemProperties.class, EPCommonSystemProperties.class})
public class FunctionalMenuMockServiceImplTest {

	@Mock
	FunctionalMenuService functionalMenuService;
	
	@Mock
	DataAccessService dataAccessService;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
    @InjectMocks
	FunctionalMenuServiceImpl functionalMenuServiceImpl= new FunctionalMenuServiceImpl();
	
	@Test
	public void assignHelpURLsTest(){
		List<FunctionalMenuItem> menuItems = new ArrayList<FunctionalMenuItem>();
		FunctionalMenuItem functionalMenuItem1 = new FunctionalMenuItem();
		functionalMenuItem1.text = "Contact Us";
		FunctionalMenuItem functionalMenuItem2 = new FunctionalMenuItem();
		functionalMenuItem2.text = "Get Access";
		FunctionalMenuItem functionalMenuItem3 = new FunctionalMenuItem();
		functionalMenuItem3.text = "User Guide";		
		menuItems.add(functionalMenuItem1);
		menuItems.add(functionalMenuItem2);
		menuItems.add(functionalMenuItem3);
		
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.USER_GUIDE_URL)).thenReturn("http://todo_enter_user_guide_link");
//		Mockito.doNothing().when(functionalMenuServiceImpl).assignHelpURLs(menuItems);
		functionalMenuServiceImpl.assignHelpURLs(menuItems);
	}
	
	@Test
	public void getFunctionalMenuItemsTest(){
		String sql = "SELECT m.menu_id, m.column_num, m.text, m.parent_menu_id, m.url, m.active_yn, r.app_id FROM fn_menu_functional m, "
				+ "fn_menu_functional_roles r WHERE m.menu_id = r.menu_id  AND UPPER(m.active_yn) = 'Y'  AND r.role_id != '900'  "
				+ "UNION  SELECT m.menu_id, m.column_num, m.text, m.parent_menu_id, m.url, m.active_yn,-1 app_id  FROM fn_menu_functional m  "
				+ "WHERE m.url=''  AND UPPER(m.active_yn) = 'Y'";
		 List<FunctionalMenuItem> actualResult = new ArrayList<FunctionalMenuItem>();
		 List<FunctionalMenuItem> expectedResult = new ArrayList<FunctionalMenuItem>();
		 
		 Mockito.when(dataAccessService.executeSQLQuery(sql, FunctionalMenuItemWithAppID.class, null)).thenReturn(expectedResult);
		 
		 actualResult = functionalMenuServiceImpl.getFunctionalMenuItems(true);
		 assertEquals(actualResult, expectedResult);
	}
	
	@Test
	public void getFunctionalMenuItemsNegativeTest(){
		String sql = "SELECT m.menu_id, m.column_num, m.text, m.parent_menu_id, m.url, m.active_yn, r.app_id FROM fn_menu_functional m, "
				+ "fn_menu_functional_roles r WHERE m.menu_id = r.menu_id  AND UPPER(m.active_yn) = 'Y'  AND r.role_id != '900'  "
				+ "UNION  SELECT m.menu_id, m.column_num, m.text, m.parent_menu_id, m.url, m.active_yn,-1 app_id  FROM fn_menu_functional m  "
				+ "WHERE m.url=''  AND UPPER(m.active_yn) = 'Y'";
		 List<FunctionalMenuItem> actualResult = new ArrayList<FunctionalMenuItem>();
		 List<FunctionalMenuItem> expectedResult = new ArrayList<FunctionalMenuItem>();
		 
		 Mockito.when(dataAccessService.executeSQLQuery(sql, FunctionalMenuItemWithAppID.class, null)).thenReturn(expectedResult);
		 
		 actualResult = functionalMenuServiceImpl.getFunctionalMenuItems(false);
		 assertEquals(actualResult, expectedResult);
	}
	
	@Test
	public void getFunctionalMenuItemsForNotificationTreeTest(){
		String sql = "SELECT m.menu_id, m.column_num, m.text, m.parent_menu_id, m.url, m.active_yn, r.app_id FROM fn_menu_functional m, fn_menu_functional_roles r "
				+ "WHERE m.menu_id = r.menu_id  AND UPPER(m.active_yn) = 'Y'  AND r.role_id != '900'  UNION  SELECT m.menu_id, m.column_num, m.text, m.parent_menu_id,"
				+ " m.url, m.active_yn,-1 app_id  FROM fn_menu_functional m  WHERE m.url=''  AND UPPER(m.active_yn) = 'Y'";
		 List<FunctionalMenuItem> actualResult = new ArrayList<FunctionalMenuItem>();
		 List<FunctionalMenuItem> expectedResult = new ArrayList<FunctionalMenuItem>();
		 
		 Mockito.when(dataAccessService.executeSQLQuery(sql, FunctionalMenuItemWithAppID.class, null)).thenReturn(expectedResult);
		 
		 actualResult = functionalMenuServiceImpl.getFunctionalMenuItemsForNotificationTree(true);
		 assertEquals(actualResult, expectedResult);
		
	}
	
	@Test
	public void getFunctionalMenuItemsForNotificationNegativeTreeTest(){
		String sql = "SELECT m.menu_id, m.column_num, m.text, m.parent_menu_id, m.url, m.active_yn, r.app_id FROM fn_menu_functional m, fn_menu_functional_roles r "
				+ "WHERE m.menu_id = r.menu_id  AND UPPER(m.active_yn) = 'Y'  AND r.role_id != '900'  UNION  SELECT m.menu_id, m.column_num, m.text, m.parent_menu_id,"
				+ " m.url, m.active_yn,-1 app_id  FROM fn_menu_functional m  WHERE m.url=''  AND UPPER(m.active_yn) = 'Y'";
		 List<FunctionalMenuItem> actualResult = new ArrayList<FunctionalMenuItem>();
		 List<FunctionalMenuItem> expectedResult = new ArrayList<FunctionalMenuItem>();
		 
		 Mockito.when(dataAccessService.executeSQLQuery(sql, FunctionalMenuItemWithAppID.class, null)).thenReturn(expectedResult);
		 
		 actualResult = functionalMenuServiceImpl.getFunctionalMenuItemsForNotificationTree(false);
		 assertEquals(actualResult, expectedResult);		
	}
	
	@Test
	public void getFunctionalMenuItemsForAppTest(){
		String sql = "SELECT DISTINCT m1.menu_id, m1.column_num, m1.text, m1.parent_menu_id, m1.url, m.active_yn  "
				+ "FROM fn_menu_functional m, fn_menu_functional m1, fn_menu_functional_ancestors a, fn_menu_functional_roles mr  "
				+ "WHERE  mr.app_id='1'  AND mr.menu_id = m.menu_id  AND UPPER(m.active_yn) = 'Y' AND UPPER(m1.active_yn) ='Y' AND a.menu_id = m.menu_id  "
				+ "AND a.ancestor_menu_id = m1.menu_id";
		Integer appId = 1;
		List<FunctionalMenuItem> actualResult = new ArrayList<FunctionalMenuItem>();
		List<FunctionalMenuItem> expectedResult = new ArrayList<FunctionalMenuItem>();
		 
		 Mockito.when(dataAccessService.executeSQLQuery(sql, FunctionalMenuItemWithAppID.class, null)).thenReturn(expectedResult);
		 
		 actualResult = functionalMenuServiceImpl.getFunctionalMenuItemsForApp(appId);
		 assertEquals(actualResult, expectedResult);
	}
	
	@Test
	public void getFunctionalMenuItemsForUserTest(){
		String orgUserId = "test";
		String sql = "SELECT DISTINCT m1.menu_id, m1.column_num, m1.text, m1.parent_menu_id, m1.url, m.active_yn  "
				+ "FROM fn_menu_functional m, fn_menu_functional m1, fn_menu_functional_ancestors a,  fn_menu_functional_roles mr, fn_user u , fn_user_role ur  "
				+ "WHERE  u.org_user_id='test'  AND u.user_id = ur.user_id  AND ur.app_id = mr.app_id  AND (ur.role_id = mr.role_id      OR ur.role_id = '999')  "
				+ "AND m.menu_id = mr.menu_id  AND UPPER(m.active_yn) = 'Y' AND UPPER(m1.active_yn) ='Y'  AND a.menu_id = m.menu_id  "
				+ "AND a.ancestor_menu_id = m1.menu_id  UNION  select m1.menu_id, m1.column_num, m1.text, m1.parent_menu_id, m1.url, m1.active_yn  "
				+ "FROM fn_menu_functional m, fn_menu_functional_roles mr, fn_menu_functional m1,  fn_menu_functional_ancestors a  where a.menu_id = m.menu_id  AND a.ancestor_menu_id = m1.menu_id  AND m.menu_id != m1.menu_id  AND m.menu_id = mr.menu_id  AND mr.role_id = '900'  AND UPPER(m.active_yn) = 'Y' AND UPPER(m1.active_yn) ='Y'  UNION  SELECT m.menu_id, m.column_num, m.text, m.parent_menu_id, m.url, m.active_yn  FROM fn_menu_functional m  WHERE m.text in ('Favorites','Get Access','Contact Us','Support','User Guide','Help')";
		List<FunctionalMenuItem> actualResult = new ArrayList<FunctionalMenuItem>();
		List<FunctionalMenuItem> expectedResult = new ArrayList<FunctionalMenuItem>();
		 
		Mockito.when(dataAccessService.executeSQLQuery(sql, FunctionalMenuItemWithAppID.class, null)).thenReturn(expectedResult);
		 
		actualResult = functionalMenuServiceImpl.getFunctionalMenuItemsForUser(orgUserId);
		assertEquals(actualResult, expectedResult);		
	}
	
	@Test
	public void getFunctionalMenuItemDetailsTest(){
		Long menuid = 1L;
		String sql = "SELECT * FROM fn_menu_functional_roles WHERE menu_id = '1'";
		FunctionalMenuItem actualResult = null;
		FunctionalMenuItem expectedResult = null;
		List<FunctionalMenuRole> roleItems = new ArrayList<FunctionalMenuRole>();
		Mockito.when(dataAccessService.executeSQLQuery(sql, FunctionalMenuItemWithAppID.class, null)).thenReturn(roleItems);
		 
		actualResult = functionalMenuServiceImpl.getFunctionalMenuItemDetails(menuid);
		assertEquals(actualResult, expectedResult);		
	}
		
	@Test
	public void getFavoriteItemsTest(){
		Long userId = (long)1;
		String sql = "SELECT DISTINCT f.user_id,f.menu_id,m.text,m.url  "
				+ "FROM fn_menu_favorites f, fn_menu_functional m, fn_menu_functional_roles mr  "
				+ "WHERE f.user_id='1' AND f.menu_id = m.menu_id  AND f.menu_id = mr.menu_id  AND mr.role_id != '900' ";
		List<FavoritesFunctionalMenuItemJson> actualResult = new ArrayList<FavoritesFunctionalMenuItemJson>();
		List<FavoritesFunctionalMenuItemJson> expectedResult = new ArrayList<FavoritesFunctionalMenuItemJson>();
		 
		Mockito.when(dataAccessService.executeSQLQuery(sql, FavoritesFunctionalMenuItemJson.class, null)).thenReturn(expectedResult);
		 
		actualResult = functionalMenuServiceImpl.getFavoriteItems(userId);
		assertEquals(actualResult, expectedResult);		
	}	
}
