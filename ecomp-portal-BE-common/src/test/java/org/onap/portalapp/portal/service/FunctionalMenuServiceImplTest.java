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
package org.onap.portalapp.portal.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.FunctionalMenuItemWithAppID;
import org.onap.portalapp.portal.transport.BusinessCardApplicationRole;
import org.onap.portalapp.portal.transport.FavoritesFunctionalMenuItem;
import org.onap.portalapp.portal.transport.FavoritesFunctionalMenuItemJson;
import org.onap.portalapp.portal.transport.FieldsValidator;
import org.onap.portalapp.portal.transport.FunctionalMenuItem;
import org.onap.portalapp.portal.transport.FunctionalMenuItemWithRoles;
import org.onap.portalapp.portal.transport.FunctionalMenuRole;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SystemProperties.class, EPCommonSystemProperties.class, StringUtils.class})
public class FunctionalMenuServiceImplTest {

	@Mock
	FunctionalMenuService functionalMenuService;
	
	@Mock
	DataAccessService dataAccessService;
	
	@Mock
	Session session;
	
	@Mock
	private SessionFactory sessionFactory;
	
	@Mock
	Criteria criteria;
		
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
    @InjectMocks
	FunctionalMenuServiceImpl functionalMenuServiceImpl= new FunctionalMenuServiceImpl();
	
    MockEPUser mockUser = new MockEPUser();
    
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
		FunctionalMenuRole functionalMenuRole = new FunctionalMenuRole();
		roleItems.add(functionalMenuRole);
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
	
	@Test
	public void getFavoriteItemsExceptionTest(){
		Long userId = (long)1;
		String sql = "SELECT DISTINCT f.user_id,f.menu_id,m.text,m.url  "
				+ "FROM fn_menu_favorites f, fn_menu_functional m, fn_menu_functional_roles mr  "
				+ "WHERE f.user_id='1' AND f.menu_id = m.menu_id  AND f.menu_id = mr.menu_id  AND mr.role_id != '900' ";
		Mockito.when(dataAccessService.executeSQLQuery(sql, FavoritesFunctionalMenuItemJson.class, null));
		List<FavoritesFunctionalMenuItemJson> expectedResult = functionalMenuServiceImpl.getFavoriteItems(userId);
	}
	
	@Test
	public void getFunctionalMenuItemsTest1(){
		EPUser epUser = new EPUser();
		List<FunctionalMenuItem> expected = new ArrayList<FunctionalMenuItem>();
		List<FunctionalMenuItem> actual = functionalMenuServiceImpl.getFunctionalMenuItems(epUser);
		assertEquals(actual, expected);
	}
	
	@Test
	public void getFunctionalMenuItemsTest2(){
		List<FunctionalMenuItem> expected = new ArrayList<FunctionalMenuItem>();
		List<FunctionalMenuItem> actual = functionalMenuServiceImpl.getFunctionalMenuItems();
		assertEquals(actual, expected);
	}
	
	@Test
	public void transformFunctionalMenuItemWithAppIDToFunctionalMenuItem(){
		FunctionalMenuItemWithAppID functionalMenuItemWithAppID = new FunctionalMenuItemWithAppID();
		List<FunctionalMenuItemWithAppID> functionalMenuItemWithAppIDList = new ArrayList<FunctionalMenuItemWithAppID>();
		FunctionalMenuItem functionalMenuItem = new FunctionalMenuItem();
		functionalMenuItemWithAppIDList.add(functionalMenuItemWithAppID);
		List<FunctionalMenuItem> expected = new ArrayList<FunctionalMenuItem>();
		expected.add(functionalMenuItem);
		List<FunctionalMenuItem> actual = functionalMenuServiceImpl.transformFunctionalMenuItemWithAppIDToFunctionalMenuItem(functionalMenuItemWithAppIDList);
		assertEquals(actual.size(), expected.size());
	}
	
	@Test
	public void addRolesTest(){
		FunctionalMenuItemWithRoles menuItemJson = new FunctionalMenuItemWithRoles();
		List<Integer> list = new ArrayList<Integer>();
		list.add(123);
		menuItemJson.setRoles(list);
		functionalMenuServiceImpl.addRoles(menuItemJson, session);
	}
	
	@Test
	public void deleteRolesTest(){
		Long id = (long)123;
		Mockito.doNothing().when(dataAccessService).deleteDomainObjects(FavoritesFunctionalMenuItem.class, "menu_id='" + id + "'", null);
		functionalMenuServiceImpl.deleteFavorites(id);
	}
	
	@Test
	public void deleteFavoritesTest(){
		Long id = (long)123;
		Mockito.doNothing().when(dataAccessService).deleteDomainObjects(FavoritesFunctionalMenuItem.class, "menu_id='" + id + "'", null);
		functionalMenuServiceImpl.deleteFavorites(id);
	}
	
	@Test
	public void removeAppInfoTest(){
		Long id = (long)123;
		FunctionalMenuItem menuItem = new FunctionalMenuItem();
		Mockito.when(session.get(FunctionalMenuItem.class, id)).thenReturn(menuItem);
		functionalMenuServiceImpl.removeAppInfo(session, id);
	}
	
	@Test
	public void editFunctionalMenuItem400Status(){
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(400l);
		FunctionalMenuItem functionalMenuItem = new FunctionalMenuItem();
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion textCriterion = Restrictions.eq("text",functionalMenuItem.text);
		restrictionsList.add(textCriterion);
		FunctionalMenuItemWithRoles  functionalMenuItemWithRoles  = new FunctionalMenuItemWithRoles();
		List<Integer> list = new ArrayList<Integer>();
		list.add(123);
		functionalMenuItemWithRoles.setRoles(list);		
		FieldsValidator actual = functionalMenuServiceImpl.editFunctionalMenuItem(functionalMenuItemWithRoles);
		assertEquals(expected.getHttpStatusCode(), actual.getHttpStatusCode());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void editFunctionalMenuItemMenuIdNullTest(){
		Session session = PowerMockito.mock(Session.class);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
		Transaction tx = PowerMockito.mock(Transaction.class);
		Mockito.when(session.beginTransaction()).thenReturn(tx);
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(400l);
		FunctionalMenuItem functionalMenuItem = new FunctionalMenuItem();
		List<Integer> list1 = new ArrayList<Integer>();
		list1.add(123);
		functionalMenuItem.setUrl("test.com");
		functionalMenuItem.setRoles(null);
		List<FunctionalMenuItem> functionalMenuItemList = new ArrayList<FunctionalMenuItem>();
		functionalMenuItemList.add(functionalMenuItem);
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion textCriterion = Restrictions.eq("text",functionalMenuItem.text);
		restrictionsList.add(textCriterion);
		FunctionalMenuItemWithRoles  functionalMenuItemWithRoles  = new FunctionalMenuItemWithRoles();
		functionalMenuItemWithRoles.text = "test";
		//functionalMenuItemWithRoles.menuId = (long)123;
		functionalMenuItemWithRoles.column = (Integer)123;
		functionalMenuItemWithRoles.parentMenuId = (Integer)123;
		List<Integer> list = new ArrayList<Integer>();
		list.add(123);
		functionalMenuItemWithRoles.setRoles(null);	
		Mockito.when((List<FunctionalMenuItem>)dataAccessService.getList(FunctionalMenuItem.class, null, restrictionsList, null)).thenReturn(functionalMenuItemList);
		Mockito.when(session.get(FunctionalMenuItem.class, functionalMenuItemWithRoles.menuId)).thenReturn(functionalMenuItem);
		FieldsValidator actual = functionalMenuServiceImpl.editFunctionalMenuItem(functionalMenuItemWithRoles);
		assertEquals(expected.getHttpStatusCode(), actual.getHttpStatusCode());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void editFunctionalMenuItemTest(){
		Session session = PowerMockito.mock(Session.class);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
		Transaction tx = PowerMockito.mock(Transaction.class);
		Mockito.when(session.beginTransaction()).thenReturn(tx);
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(200l);
		FunctionalMenuItem functionalMenuItem = new FunctionalMenuItem();
		List<Integer> list1 = new ArrayList<Integer>();
		list1.add(123);
		functionalMenuItem.setUrl("test.com");
		functionalMenuItem.setRoles(null);
		List<FunctionalMenuItem> functionalMenuItemList = new ArrayList<FunctionalMenuItem>();
		functionalMenuItemList.add(functionalMenuItem);
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion textCriterion = Restrictions.eq("text",functionalMenuItem.text);
		restrictionsList.add(textCriterion);
		FunctionalMenuItemWithRoles  functionalMenuItemWithRoles  = new FunctionalMenuItemWithRoles();
		functionalMenuItemWithRoles.text = "test";
		functionalMenuItemWithRoles.menuId = (long)123;
		functionalMenuItemWithRoles.column = (Integer)123;
		functionalMenuItemWithRoles.parentMenuId = (Integer)123;
		List<Integer> list = new ArrayList<Integer>();
		list.add(123);
		functionalMenuItemWithRoles.setRoles(null);	
		Mockito.when((List<FunctionalMenuItem>)dataAccessService.getList(FunctionalMenuItem.class, null, restrictionsList, null)).thenReturn(functionalMenuItemList);
		Mockito.when(session.get(FunctionalMenuItem.class, functionalMenuItemWithRoles.menuId)).thenReturn(functionalMenuItem);
		FieldsValidator actual = functionalMenuServiceImpl.editFunctionalMenuItem(functionalMenuItemWithRoles);
		assertEquals(expected.getHttpStatusCode(), actual.getHttpStatusCode());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void editFunctionalMenuItem1Test(){
		Session session = PowerMockito.mock(Session.class);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
		Transaction tx = PowerMockito.mock(Transaction.class);
		Mockito.when(session.beginTransaction()).thenReturn(tx);
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(200l);
		FunctionalMenuItem functionalMenuItem = new FunctionalMenuItem();
		List<Integer> list1 = new ArrayList<Integer>();
		list1.add(123);
		functionalMenuItem.setRoles(list1);
		List<FunctionalMenuItem> functionalMenuItemList = new ArrayList<FunctionalMenuItem>();
		functionalMenuItemList.add(functionalMenuItem);
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion textCriterion = Restrictions.eq("text",functionalMenuItem.text);
		restrictionsList.add(textCriterion);
		FunctionalMenuItemWithRoles  functionalMenuItemWithRoles  = new FunctionalMenuItemWithRoles();
		functionalMenuItemWithRoles.text = "test";
		functionalMenuItemWithRoles.menuId = (long)123;
		functionalMenuItemWithRoles.column = (Integer)123;
		functionalMenuItemWithRoles.parentMenuId = (Integer)123;
		functionalMenuItemWithRoles.appid =1;
		functionalMenuItemWithRoles.url = "test.com";
		List<Integer> list = new ArrayList<Integer>();
		list.add(123);
		functionalMenuItemWithRoles.setRoles(list);	
		PowerMockito.mockStatic(StringUtils.class);
		Mockito.when(StringUtils.isEmpty(Matchers.anyString())).thenReturn(false);
		Mockito.when((List<FunctionalMenuItem>)dataAccessService.getList(FunctionalMenuItem.class, null, restrictionsList, null)).thenReturn(functionalMenuItemList);
		Mockito.when(session.get(FunctionalMenuItem.class, functionalMenuItemWithRoles.menuId)).thenReturn(functionalMenuItem);
		FieldsValidator actual = functionalMenuServiceImpl.editFunctionalMenuItem(functionalMenuItemWithRoles);
		assertEquals(expected.getHttpStatusCode(), actual.getHttpStatusCode());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void editFunctionalMenuItem2Test(){
		Session session = PowerMockito.mock(Session.class);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
		Transaction tx = PowerMockito.mock(Transaction.class);
		Mockito.when(session.beginTransaction()).thenReturn(tx);
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(200l);
		FunctionalMenuItem functionalMenuItem = new FunctionalMenuItem();
		List<Integer> list1 = new ArrayList<Integer>();
		list1.add(123);
		functionalMenuItem.setRoles(list1);
		List<FunctionalMenuItem> functionalMenuItemList = new ArrayList<FunctionalMenuItem>();
		functionalMenuItemList.add(functionalMenuItem);
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion textCriterion = Restrictions.eq("text",functionalMenuItem.text);
		restrictionsList.add(textCriterion);
		FunctionalMenuItemWithRoles  functionalMenuItemWithRoles  = new FunctionalMenuItemWithRoles();
		functionalMenuItemWithRoles.text = "test";
		functionalMenuItemWithRoles.menuId = (long)123;
		functionalMenuItemWithRoles.column = (Integer)123;
		functionalMenuItemWithRoles.parentMenuId = (Integer)123;
		functionalMenuItemWithRoles.appid =1;
		functionalMenuItemWithRoles.column = 1;
		List<Integer> list = new ArrayList<Integer>();
		list.add(123);
		functionalMenuItemWithRoles.setRoles(list);	
		PowerMockito.mockStatic(StringUtils.class);
		Mockito.when(StringUtils.isEmpty(Matchers.anyString())).thenReturn(false);
		Mockito.when(session.createCriteria(FunctionalMenuItem.class)).thenReturn(criteria);
		Mockito.when((List<FunctionalMenuItem>)dataAccessService.getList(FunctionalMenuItem.class, null, restrictionsList, null)).thenReturn(functionalMenuItemList);
		Mockito.when(session.get(FunctionalMenuItem.class, functionalMenuItemWithRoles.menuId)).thenReturn(functionalMenuItem);
		FieldsValidator actual = functionalMenuServiceImpl.editFunctionalMenuItem(functionalMenuItemWithRoles);
		assertEquals(expected.getHttpStatusCode(), actual.getHttpStatusCode());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void editFunctionalMenuItem3Test(){
		Session session = PowerMockito.mock(Session.class);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
		Transaction tx = PowerMockito.mock(Transaction.class);
		Mockito.when(session.beginTransaction()).thenReturn(tx);
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(500l);
		FunctionalMenuItem functionalMenuItem = new FunctionalMenuItem();
		List<Integer> list1 = new ArrayList<Integer>();
		list1.add(123);
		functionalMenuItem.setRoles(list1);
		functionalMenuItem.parentMenuId = (Integer)123;
		List<FunctionalMenuItem> functionalMenuItemList = new ArrayList<FunctionalMenuItem>();
		functionalMenuItemList.add(functionalMenuItem);
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion textCriterion = Restrictions.eq("text",functionalMenuItem.text);
		restrictionsList.add(textCriterion);
		FunctionalMenuItemWithRoles  functionalMenuItemWithRoles  = new FunctionalMenuItemWithRoles();
		functionalMenuItemWithRoles.text = "test";
		functionalMenuItemWithRoles.menuId = (long)123;
		functionalMenuItemWithRoles.column = (Integer)123;
		functionalMenuItemWithRoles.parentMenuId = (Integer)123;
		functionalMenuItemWithRoles.appid =1;
		functionalMenuItemWithRoles.column = 1;
		List<Integer> list = new ArrayList<Integer>();
		list.add(123);
		functionalMenuItemWithRoles.setRoles(list);	
		PowerMockito.mockStatic(StringUtils.class);
		Mockito.when(StringUtils.isEmpty(Matchers.anyString())).thenReturn(false);
		Mockito.when(session.createCriteria(FunctionalMenuItem.class)).thenReturn(criteria);
		Mockito.when((List<FunctionalMenuItem>)dataAccessService.getList(FunctionalMenuItem.class, null, restrictionsList, null)).thenReturn(functionalMenuItemList);
		Mockito.when(session.get(FunctionalMenuItem.class, functionalMenuItemWithRoles.menuId)).thenReturn(functionalMenuItem);
		FieldsValidator actual = functionalMenuServiceImpl.editFunctionalMenuItem(functionalMenuItemWithRoles);
		assertEquals(expected.getHttpStatusCode(), actual.getHttpStatusCode());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void editFunctionalMenuItem4Test(){
		Session session = PowerMockito.mock(Session.class);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
		Transaction tx = PowerMockito.mock(Transaction.class);
		Mockito.when(session.beginTransaction()).thenReturn(tx);
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(200l);
		FunctionalMenuItem functionalMenuItem = new FunctionalMenuItem();
		List<Integer> list1 = new ArrayList<Integer>();
		list1.add(123);
		functionalMenuItem.setRoles(list1);
		functionalMenuItem.parentMenuId = (Integer)12;
		List<FunctionalMenuItem> functionalMenuItemList = new ArrayList<FunctionalMenuItem>();
		functionalMenuItemList.add(functionalMenuItem);
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion textCriterion = Restrictions.eq("text",functionalMenuItem.text);
		restrictionsList.add(textCriterion);
		FunctionalMenuItemWithRoles  functionalMenuItemWithRoles  = new FunctionalMenuItemWithRoles();
		functionalMenuItemWithRoles.text = "test";
		functionalMenuItemWithRoles.menuId = (long)123;
		functionalMenuItemWithRoles.column = (Integer)123;
		functionalMenuItemWithRoles.parentMenuId = (Integer)123;
		functionalMenuItemWithRoles.appid =1;
		functionalMenuItemWithRoles.column = 1;
		List<Integer> list = new ArrayList<Integer>();
		list.add(123);
		functionalMenuItemWithRoles.setRoles(list);	
		PowerMockito.mockStatic(StringUtils.class);
		Mockito.when(StringUtils.isEmpty(Matchers.anyString())).thenReturn(false);
		Mockito.when(session.createCriteria(FunctionalMenuItem.class)).thenReturn(criteria);
		Mockito.when((List<FunctionalMenuItem>)dataAccessService.getList(FunctionalMenuItem.class, null, restrictionsList, null)).thenReturn(functionalMenuItemList);
		Mockito.when(session.get(FunctionalMenuItem.class, functionalMenuItemWithRoles.menuId)).thenReturn(functionalMenuItem);
		FieldsValidator actual = functionalMenuServiceImpl.editFunctionalMenuItem(functionalMenuItemWithRoles);
		assertEquals(expected.getHttpStatusCode(), actual.getHttpStatusCode());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void editFunctionalMenuItem5Test(){
		Session session = PowerMockito.mock(Session.class);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
		Transaction tx = PowerMockito.mock(Transaction.class);
		Mockito.when(session.beginTransaction()).thenReturn(tx);
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(200l);
		FunctionalMenuItem functionalMenuItem = new FunctionalMenuItem();
		List<Integer> list1 = new ArrayList<Integer>();
		list1.add(123);
		functionalMenuItem.setRoles(list1);
		functionalMenuItem.parentMenuId = (Integer)123;
		functionalMenuItem.setUrl("test.com");
		List<FunctionalMenuItem> functionalMenuItemList = new ArrayList<FunctionalMenuItem>();
		functionalMenuItemList.add(functionalMenuItem);
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion textCriterion = Restrictions.eq("text",functionalMenuItem.text);
		restrictionsList.add(textCriterion);
		FunctionalMenuItemWithRoles  functionalMenuItemWithRoles  = new FunctionalMenuItemWithRoles();
		functionalMenuItemWithRoles.text = "test";
		functionalMenuItemWithRoles.menuId = (long)123;
		functionalMenuItemWithRoles.column = (Integer)123;
		functionalMenuItemWithRoles.parentMenuId = (Integer)12;
		functionalMenuItemWithRoles.appid =1;
		functionalMenuItemWithRoles.column = 1;
		List<Integer> list = new ArrayList<Integer>();
		list.add(123);
		functionalMenuItemWithRoles.setRoles(list);	
		PowerMockito.mockStatic(StringUtils.class);
		Mockito.when(StringUtils.isEmpty(Matchers.anyString())).thenReturn(false);
		Mockito.when((FunctionalMenuItem) session.get(FunctionalMenuItem.class, (long)12)).thenReturn(functionalMenuItem);
		Mockito.when(session.createCriteria(FunctionalMenuItem.class)).thenReturn(criteria);
		Mockito.when((List<FunctionalMenuItem>)dataAccessService.getList(FunctionalMenuItem.class, null, restrictionsList, null)).thenReturn(functionalMenuItemList);
		Mockito.when(session.get(FunctionalMenuItem.class, functionalMenuItemWithRoles.menuId)).thenReturn(functionalMenuItem);
		FieldsValidator actual = functionalMenuServiceImpl.editFunctionalMenuItem(functionalMenuItemWithRoles);
		assertEquals(expected.getHttpStatusCode(), actual.getHttpStatusCode());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void editFunctionalMenuItem6Test(){
		Session session = PowerMockito.mock(Session.class);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
		Transaction tx = PowerMockito.mock(Transaction.class);
		Mockito.when(session.beginTransaction()).thenReturn(tx);
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(200l);
		FunctionalMenuItem functionalMenuItem = new FunctionalMenuItem();
		List<Integer> list1 = new ArrayList<Integer>();
		list1.add(123);
		functionalMenuItem.setUrl("test.com");
		functionalMenuItem.setRoles(null);
		List<FunctionalMenuItem> functionalMenuItemList = new ArrayList<FunctionalMenuItem>();
		functionalMenuItemList.add(functionalMenuItem);
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion textCriterion = Restrictions.eq("text",functionalMenuItem.text);
		restrictionsList.add(textCriterion);
		FunctionalMenuItemWithRoles  functionalMenuItemWithRoles  = new FunctionalMenuItemWithRoles();
		functionalMenuItemWithRoles.text = "test";
		functionalMenuItemWithRoles.menuId = (long)123;
		functionalMenuItemWithRoles.column = (Integer)123;
		functionalMenuItemWithRoles.parentMenuId = (Integer)123;
		List<Integer> list = new ArrayList<Integer>();
		list.add(123);
		functionalMenuItemWithRoles.setRoles(null);	
		EPApp app = new EPApp();
		app.setId((long)123);
		Mockito.when(session.get(EPApp.class, (long)123)).thenReturn(app);
		Mockito.when((List<FunctionalMenuItem>)dataAccessService.getList(FunctionalMenuItem.class, null, restrictionsList, null)).thenReturn(functionalMenuItemList);
		Mockito.when(session.get(FunctionalMenuItem.class, functionalMenuItemWithRoles.menuId)).thenReturn(functionalMenuItem);
		FieldsValidator actual = functionalMenuServiceImpl.editFunctionalMenuItem(functionalMenuItemWithRoles);
		assertEquals(expected.getHttpStatusCode(), actual.getHttpStatusCode());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void editFunctionalMenuItem7Test(){
		Session session = PowerMockito.mock(Session.class);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
		Transaction tx = PowerMockito.mock(Transaction.class);
		Mockito.when(session.beginTransaction()).thenReturn(tx);
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(500l);
		FunctionalMenuItem functionalMenuItem = new FunctionalMenuItem();
		List<Integer> list1 = new ArrayList<Integer>();
		list1.add(123);
		functionalMenuItem.setRoles(list1);
		List<FunctionalMenuItem> functionalMenuItemList = new ArrayList<FunctionalMenuItem>();
		functionalMenuItemList.add(functionalMenuItem);
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion textCriterion = Restrictions.eq("text",functionalMenuItem.text);
		restrictionsList.add(textCriterion);
		FunctionalMenuItemWithRoles  functionalMenuItemWithRoles  = new FunctionalMenuItemWithRoles();
		functionalMenuItemWithRoles.text = "test";
		functionalMenuItemWithRoles.menuId = (long)123;
		functionalMenuItemWithRoles.column = (Integer)123;
		functionalMenuItemWithRoles.parentMenuId = (Integer)123;
		functionalMenuItemWithRoles.appid =1;
		functionalMenuItemWithRoles.url = "test.com";
		List<Integer> list = new ArrayList<Integer>();
		list.add(123);
		functionalMenuItemWithRoles.setRoles(list);	
		PowerMockito.mockStatic(StringUtils.class);
		Mockito.when(StringUtils.isEmpty(Matchers.anyString())).thenReturn(false);
		EPApp app = new EPApp();
		app.setEnabled(false);
		PowerMockito.when((EPApp)session.get(EPApp.class,1)).thenReturn(app);
		FieldsValidator actual = functionalMenuServiceImpl.editFunctionalMenuItem(functionalMenuItemWithRoles);
		assertEquals(expected.getHttpStatusCode(), actual.getHttpStatusCode());
	}
	
	@Test
	public void deleteFunctionalMenuItemTest(){
		String sql = "set FOREIGN_KEY_CHECKS=";
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(500l);
		Long menuId = (long)123;
		Session session = PowerMockito.mock(Session.class);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
		Transaction tx = PowerMockito.mock(Transaction.class);
	//	SQLQuery query = PowerMockito.mock(Query.class);
		Mockito.when(session.beginTransaction()).thenReturn(tx);
	//	Mockito.when(session.createSQLQuery(sql)).thenReturn(query);
		FieldsValidator actual = functionalMenuServiceImpl.deleteFunctionalMenuItem(menuId);
		assertEquals(expected.getHttpStatusCode(), actual.getHttpStatusCode());
	}
	
	@Test
	public void deleteFunctionalMenuItemExceptionTest(){
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(500l);
		Long menuId = (long)123;
		FieldsValidator actual = functionalMenuServiceImpl.deleteFunctionalMenuItem(menuId);
		assertEquals(expected.getHttpStatusCode(), actual.getHttpStatusCode());
	}
	
	@Test
	public void getFunctionalMenuRoleTest(){
		List<FunctionalMenuRole> expected = null;
		String sql = "SELECT * from fn_menu_functional_roles";
		Mockito.when(dataAccessService.executeSQLQuery(sql, FunctionalMenuRole.class,
				null)).thenReturn(expected);
		List<FunctionalMenuRole> actual = functionalMenuServiceImpl.getFunctionalMenuRole();
		assertEquals(expected, actual);
	}
	
	@Test
	public void getUserAppRolesListTest(){
		String userId = "test";
		Map<String, String> params = new HashMap<>();
		params.put("userId", "test");
		List<BusinessCardApplicationRole> userAppRoles = null;
		List<BusinessCardApplicationRole> expected = null;
		Mockito.when(dataAccessService.executeNamedQuery("getUserApproles", params, null)).thenReturn(userAppRoles);
		List<BusinessCardApplicationRole> actual = functionalMenuServiceImpl.getUserAppRolesList(userId);
		assertEquals(expected, actual);
	}
	
	@Test
	public void getUserAppRolesListExceptionTest(){
		String userId = "test";
		Map<String, String> params = new HashMap<>();
		params.put("userId", "test");
		Mockito.when(dataAccessService.executeNamedQuery("getUserApproles", params, null));
		List<BusinessCardApplicationRole> actual = functionalMenuServiceImpl.getUserAppRolesList(userId);
	}
	
	@Test
	public void createFunctionalMenuItem400StautusTest(){
		FunctionalMenuItemWithRoles functionalMenuItemWithRoles = new FunctionalMenuItemWithRoles();
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(400l);
		FieldsValidator actual = functionalMenuServiceImpl.createFunctionalMenuItem(functionalMenuItemWithRoles);
		assertEquals(expected.getHttpStatusCode(), actual.getHttpStatusCode());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void createFunctionalMenuItemTest(){
		Session session = PowerMockito.mock(Session.class);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
		Transaction tx = PowerMockito.mock(Transaction.class);
		Mockito.when(session.beginTransaction()).thenReturn(tx);
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		FunctionalMenuItem functionalMenuItem = new FunctionalMenuItem();
		functionalMenuItem.setRestrictedApp(true);
		Criterion textCriterion = Restrictions.eq("text",functionalMenuItem.text);
		restrictionsList.add(textCriterion);
		FunctionalMenuItemWithRoles functionalMenuItemWithRoles = new FunctionalMenuItemWithRoles();
		functionalMenuItemWithRoles.menuId = (long) 123;
		functionalMenuItemWithRoles.url = "test";
		List<FunctionalMenuItem> functionalMenuItemList = new ArrayList<FunctionalMenuItem>();
		functionalMenuItemList.add(functionalMenuItem);
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(400l);
		DataAccessService dao = PowerMockito.mock(DataAccessService.class);
		PowerMockito.when((List<FunctionalMenuItem>)dao.getList(FunctionalMenuItem.class, null, restrictionsList, null)).thenReturn(null);
		FieldsValidator actual = functionalMenuServiceImpl.createFunctionalMenuItem(functionalMenuItemWithRoles);
		assertEquals(expected.getHttpStatusCode(), actual.getHttpStatusCode());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void createFunctionalMenuItemExceptionTest1(){
		Session session = PowerMockito.mock(Session.class);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
		Transaction tx = PowerMockito.mock(Transaction.class);
		Mockito.when(session.beginTransaction()).thenReturn(tx);
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		FunctionalMenuItem functionalMenuItem = new FunctionalMenuItem();
		functionalMenuItem.setRestrictedApp(true);
		Criterion textCriterion = Restrictions.eq("text",functionalMenuItem.text);
		restrictionsList.add(textCriterion);
		FunctionalMenuItemWithRoles functionalMenuItemWithRoles = new FunctionalMenuItemWithRoles();
		functionalMenuItemWithRoles.menuId = (long) 123;
		List<FunctionalMenuItem> functionalMenuItemList = new ArrayList<FunctionalMenuItem>();
		functionalMenuItemList.add(functionalMenuItem);
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(500l);
		DataAccessService dao = PowerMockito.mock(DataAccessService.class);
		PowerMockito.when((List<FunctionalMenuItem>)dao.getList(FunctionalMenuItem.class, null, restrictionsList, null)).thenReturn(null);
		FieldsValidator actual = functionalMenuServiceImpl.createFunctionalMenuItem(functionalMenuItemWithRoles);
		assertEquals(expected.getHttpStatusCode(), actual.getHttpStatusCode());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void createFunctionalMenuItemTest1(){
		Session session = PowerMockito.mock(Session.class);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
		Transaction tx = PowerMockito.mock(Transaction.class);
		Mockito.when(session.beginTransaction()).thenReturn(tx);
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		FunctionalMenuItem functionalMenuItem = new FunctionalMenuItem();
		functionalMenuItem.setRestrictedApp(true);
		Criterion textCriterion = Restrictions.eq("text",functionalMenuItem.text);
		restrictionsList.add(textCriterion);
		FunctionalMenuItemWithRoles functionalMenuItemWithRoles = new FunctionalMenuItemWithRoles();
		functionalMenuItemWithRoles.menuId = (long) 123;
		List<FunctionalMenuItem> functionalMenuItemList = new ArrayList<FunctionalMenuItem>();
		functionalMenuItemList.add(functionalMenuItem);
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(200l);
		Criteria criteria =  Mockito.mock(Criteria.class);
		DataAccessService dao = PowerMockito.mock(DataAccessService.class);
		Mockito.when(session.createCriteria(FunctionalMenuItem.class)).thenReturn(criteria);
		PowerMockito.when((List<FunctionalMenuItem>)dao.getList(FunctionalMenuItem.class, null, restrictionsList, null)).thenReturn(null);
		FieldsValidator actual = functionalMenuServiceImpl.createFunctionalMenuItem(functionalMenuItemWithRoles);
		assertEquals(expected.getHttpStatusCode(), actual.getHttpStatusCode());
	}
	
	@Test
	public void regenerateAncestorTableTest(){
		FieldsValidator expected = new FieldsValidator ();
		expected.setHttpStatusCode(200l);
		Session session = PowerMockito.mock(Session.class);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
		Transaction tx = PowerMockito.mock(Transaction.class);
		Mockito.when(session.beginTransaction()).thenReturn(tx);
		SQLQuery query = Mockito.mock(SQLQuery.class);
		String sql = "DELETE FROM fn_menu_functional_ancestors";
		Mockito.when(session.createSQLQuery(Matchers.anyString())).thenReturn(query);
		FieldsValidator actual = functionalMenuServiceImpl.regenerateAncestorTable();
		assertEquals(expected, actual);		
	}
	
	@Test
	public void regenerateAncestorTableExceptionTest(){
		FieldsValidator expected = new FieldsValidator ();
		expected.setHttpStatusCode(500l);
		Session session = PowerMockito.mock(Session.class);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
		Transaction tx = PowerMockito.mock(Transaction.class);
		Mockito.when(session.beginTransaction()).thenReturn(tx);
		SQLQuery query = Mockito.mock(SQLQuery.class);
		String sql = "DELETE FROM fn_menu_functional_ancestors";
		Mockito.when(session.createSQLQuery(sql)).thenReturn(query);
		FieldsValidator actual = functionalMenuServiceImpl.regenerateAncestorTable();
		assertEquals(expected, actual);		
	}
	
	@Test
	public void setFavoriteItemTest(){
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(200l);
		Session session = PowerMockito.mock(Session.class);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
		Transaction tx = PowerMockito.mock(Transaction.class);
		Mockito.when(session.beginTransaction()).thenReturn(tx);
		FavoritesFunctionalMenuItem favoritesFunctionalMenuItem = new FavoritesFunctionalMenuItem ();
		Mockito.doNothing().when(tx).commit();
		FieldsValidator actual = functionalMenuServiceImpl.setFavoriteItem(favoritesFunctionalMenuItem);
		assertEquals(expected, actual);
	}
	
	@Test
	public void setFavoriteItemExceptionTest(){
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(500l);
		Session session = PowerMockito.mock(Session.class);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
		Transaction tx = PowerMockito.mock(Transaction.class);
		FavoritesFunctionalMenuItem favoritesFunctionalMenuItem = new FavoritesFunctionalMenuItem ();
		Mockito.doNothing().when(tx).commit();
		FieldsValidator actual = functionalMenuServiceImpl.setFavoriteItem(favoritesFunctionalMenuItem);
		assertEquals(expected, actual);
	}
	
	@Test
	public void removeFavoriteItemTest(){
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(200l);
		Session session = PowerMockito.mock(Session.class);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
		Transaction tx = PowerMockito.mock(Transaction.class);
		Mockito.when(session.beginTransaction()).thenReturn(tx);
		Mockito.doNothing().when(tx).commit();
		FieldsValidator actual = functionalMenuServiceImpl.removeFavoriteItem((long)123, (long)123);
		assertEquals(expected, actual);
	}
	
	@Test
	public void removeFavoriteItemExceptionTest(){
		FieldsValidator expected = new FieldsValidator();
		expected.setHttpStatusCode(500l);
		Session session = PowerMockito.mock(Session.class);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
		Transaction tx = PowerMockito.mock(Transaction.class);
		Mockito.doNothing().when(tx).commit();
		FieldsValidator actual = functionalMenuServiceImpl.removeFavoriteItem((long)123, (long)123);
		assertEquals(expected, actual);
	}
}
