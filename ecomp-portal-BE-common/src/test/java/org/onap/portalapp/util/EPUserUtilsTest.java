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
package org.onap.portalapp.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Hex;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPRole;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.EPUserApp;
import org.onap.portalapp.portal.exceptions.RoleFunctionException;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.EPRoleFunctionService;
import org.onap.portalsdk.core.domain.RoleFunction;
import org.onap.portalsdk.core.exception.SessionExpiredException;
import org.onap.portalsdk.core.menu.MenuBuilder;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.portalsdk.core.web.support.AppUtils;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.apache.commons.codec.DecoderException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SystemProperties.class,AppUtils.class,Hex.class,MenuBuilder.class})
public class EPUserUtilsTest {

/*	@Before
	public void setup() {
		 //DataAccessService dataAccessService=Mockito.mock(DataAccessService.class);
	}*/
	
	@Mock
	DataAccessService dataAccessService;
	
	@Mock
	EPRoleFunctionService epRoleFunctionService;
	
	/*@Mock
	DataAccessService dataAccessService;*/
	
	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	
	MockEPUser mockUser = new MockEPUser();

	
	@Test
	public void getUserIdAsLongTest() {
		Long expectedUserId=1L;
        PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.when(SystemProperties.getProperty(SystemProperties.APPLICATION_USER_ID)).thenReturn("1");
		Long userId=EPUserUtils.getUserIdAsLong(mockedRequest);
		assertEquals(userId,expectedUserId);
	}
	
	@Test
	public void getUserIdAsLongTestWithRequestNull() {
		Long expectedUserId=-1L;
		EPUser user = mockUser.mockEPUser();
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.when(SystemProperties.getProperty(SystemProperties.APPLICATION_USER_ID)).thenReturn("1");
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Long userId=EPUserUtils.getUserIdAsLong(mockedRequest);
		assertEquals(userId,expectedUserId);

	}
	
	@Test
	public void setDataAccessServiceTest() throws RoleFunctionException{
		
	}
	
	@Test
	public void getUserIdAsLongWithExceptionTest() {
		Long expectedUserId=1L;
        PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.when(SystemProperties.getProperty(SystemProperties.APPLICATION_USER_ID)).thenReturn("1");
		Long userId=EPUserUtils.getUserIdAsLong(mockedRequest);
		assertEquals(userId,expectedUserId);
	}
	
	@Test
	public void getRequestIdTestWithException() {
		String expectedRequestId="c6f9542c-7378-4995-9ee3-cb498710e1ea";
        PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.when(SystemProperties.getProperty(SystemProperties.APPLICATION_USER_ID)).thenReturn("1");
		String requestId=EPUserUtils.getRequestId(mockedRequest);
		assertNotNull(requestId);
	}
	
	
	
	@Test
	public void getRequestIdTest() {
		String expectedRequestId=UUID.randomUUID().toString();
		Enumeration<String> enums;
		Vector<String> attrs = new Vector<String>();
		attrs.add("ff_test");
		enums = attrs.elements();
		Mockito.when(mockedRequest.getHeaderNames()).thenReturn(enums);
        PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.when(SystemProperties.getProperty(SystemProperties.APPLICATION_USER_ID)).thenReturn("1");
		String requestId=EPUserUtils.getRequestId(mockedRequest);
		assertNotNull(requestId);
	}
	
	
	@Test
	public void getRequestIdTestWithSess() {
		Enumeration<String> enums;
		Vector<String> attrs = new Vector<String>();
		attrs.add("X-ECOMP-RequestID");
		enums = attrs.elements();
		Mockito.when(mockedRequest.getHeaderNames()).thenReturn(enums);
        PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.when(SystemProperties.getProperty(SystemProperties.APPLICATION_USER_ID)).thenReturn("1");
		Mockito.when(mockedRequest.getHeader("X-ECOMP-RequestID")).thenReturn("test");
		String requestId=EPUserUtils.getRequestId(mockedRequest);
		assertNotNull(requestId);
		
	}
	
	@Test
	public void getFullURLTest(){
		String expected="test?testString";
		StringBuffer stringBuffer = new StringBuffer("test");
		Mockito.when(mockedRequest.getRequestURL()).thenReturn(stringBuffer);
		Mockito.when(mockedRequest.getQueryString()).thenReturn("testString");
		String actual=EPUserUtils.getFullURL(mockedRequest);
		assertEquals(actual,expected);
	}
	
	@Test
	public void getFullURLTestWithQueryStringNull(){
		String expected="test";
		StringBuffer stringBuffer = new StringBuffer("test");
		Mockito.when(mockedRequest.getRequestURL()).thenReturn(stringBuffer);
		Mockito.when(mockedRequest.getQueryString()).thenReturn(null);
		String actual=EPUserUtils.getFullURL(mockedRequest);
		assertEquals(actual,expected);

	}
	
	
	
	@Test
	public void getFullURLTestWithRequestNull(){
		String expected="";
		StringBuffer stringBuffer = new StringBuffer("test");
		Mockito.when(mockedRequest.getRequestURL()).thenReturn(stringBuffer);
		Mockito.when(mockedRequest.getQueryString()).thenReturn(null);
		String actual=EPUserUtils.getFullURL(null);
		assertEquals(actual,expected);

	}
	
	@Test
	public void getUserIdTest(){
		int expected=1;
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.when(SystemProperties.getProperty(SystemProperties.APPLICATION_USER_ID)).thenReturn("1");
		int actual=EPUserUtils.getUserId(mockedRequest);
		assertEquals(actual,expected);

	}
	
	@Test
	public void hasRoleTest(){
		boolean expected=false;
		EPUser user = mockUser.mockEPUser();
		boolean actual=EPUserUtils.hasRole(user, "12");
		assertEquals(actual,expected);

	}
	
	@Test
	public void getRolesTest(){
		HashMap roles=new HashMap<>();
		roles.put((long) 1, "test");
		HashMap expected=new HashMap<>();
		expected.put((long) 1, "test");
		Set menuSet = new HashSet<>();
		menuSet.add(1);
		HttpSession session = mock(HttpSession.class);
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(AppUtils.class);
		PowerMockito.when(AppUtils.getSession(mockedRequest)).thenReturn(session);
		PowerMockito.when(SystemProperties.getProperty(Matchers.anyString())).thenReturn("12");
		Mockito.when(session.getAttribute(Matchers.anyString())).thenReturn(roles);
		roles= (HashMap) EPUserUtils.getRoles(mockedRequest);
		assertEquals(roles,expected);


	}
	
	@Test
	public void getRolesTestWithNoRoles(){
		EPUser user=mock(EPUser.class);
		HashMap roles=new HashMap<>();
		HashMap expected=new HashMap<>();
		SortedSet<EPRole> role= new TreeSet<>();
		role.add(new EPRole());
		//user.setEPRoles(role);
		HttpSession session = mock(HttpSession.class);
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(AppUtils.class);
		PowerMockito.when(AppUtils.getSession(mockedRequest)).thenReturn(session);
		PowerMockito.when(SystemProperties.getProperty(SystemProperties.ROLES_ATTRIBUTE_NAME)).thenReturn("12");
		PowerMockito.when(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME)).thenReturn("attr_name");
		Mockito.when(session.getAttribute("attr_name")).thenReturn(user);
		Mockito.when(user.getEPRoles()).thenReturn(role);
		Mockito.when(session.getAttribute("12")).thenReturn(null);
		roles= (HashMap) EPUserUtils.getRoles(mockedRequest);
		assertEquals(roles,expected);

	}
	
	@Test
	public void getRolesTestWithNoRolesException(){
		EPUser user=mock(EPUser.class);
		 EPRole epRole=mock(EPRole.class);
		 HashMap roles=new HashMap<>();
			
		SortedSet<EPRole> role= new TreeSet<>();
		 role.add(epRole);

		SortedSet<EPRole> childRoles=new TreeSet<>();
		 EPRole epRole2=new EPRole();
		 epRole2.setActive(true);
		 epRole2.setId(3L);
			childRoles.add(epRole2);
			
			SortedSet<EPUserApp> epUserApps= new TreeSet<>();
			EPUserApp epUserApp=new EPUserApp();
			EPRole epRole3=new EPRole();
			epRole3.setActive(true);
			 epRole3.setId(999L);
			epUserApp.setRole(epRole3);
			epUserApps.add(epUserApp);
			
		HttpSession session = mock(HttpSession.class);
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(AppUtils.class);
		PowerMockito.when(AppUtils.getSession(mockedRequest)).thenReturn(session);
		PowerMockito.when(SystemProperties.getProperty(SystemProperties.ROLES_ATTRIBUTE_NAME)).thenReturn("12");
		PowerMockito.when(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME)).thenReturn("attr_name");
		Mockito.when(session.getAttribute("attr_name")).thenReturn(user);
		Mockito.when(user.getEPRoles()).thenReturn(role);
		Mockito.when(epRole.getActive()).thenReturn(true);
		Mockito.when(epRole.getId()).thenReturn(2L);
		Mockito.when(epRole.getChildRoles()).thenReturn(childRoles);
		Mockito.when(user.getEPUserApps()).thenReturn(epUserApps);
		Mockito.when(session.getAttribute("12")).thenReturn(null);
		roles= (HashMap) EPUserUtils.getRoles(mockedRequest);
		assertNotNull(roles);
	}
	
	@Test
	public void decodeFunctionCodeTest() throws Exception{
		String expected="test_instance";
       String actual= EPUserUtils.decodeFunctionCode("test_instance");
       assertEquals(expected, actual);
	}
	
	@Test(expected=RoleFunctionException.class)
	public void decodeFunctionCodeTestWithException() throws DecoderException, RoleFunctionException {
		PowerMockito.mockStatic(Hex.class);
		PowerMockito.when(Hex.decodeHex(Matchers.any())).thenThrow(DecoderException.class);
		EPUserUtils.decodeFunctionCode("n+");
	}
	
	@Test
	public void setUserSessionTest() throws Exception{
		EPUser user = mockUser.mockEPUser();
        Set<String> applicationMenuData = new HashSet<String>();
        applicationMenuData.add("test_application_menuData");
        Set<String> businessDirectMenuData = new HashSet<String>();
        businessDirectMenuData.add("test_businessDirect_menuData");
        HttpSession session = mockedRequest.getSession();
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPRoleFunctionService.class);
		PowerMockito.mockStatic(MenuBuilder.class);
		List<RoleFunction> roleFunctions=new ArrayList<RoleFunction>();
		RoleFunction roleFunction=new RoleFunction();
		roleFunction.setId(1L);
		roleFunction.setCode("test_code");
		roleFunctions.add(roleFunction);
		
		Mockito.when(epRoleFunctionService.getRoleFunctions()).thenReturn(roleFunctions);
		Mockito.when(MenuBuilder.filterMenu(applicationMenuData, mockedRequest)).thenReturn(applicationMenuData);
		PowerMockito.when(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME)).thenReturn("12");
        EPUserUtils.setUserSession(mockedRequest, user, applicationMenuData, businessDirectMenuData,  epRoleFunctionService);
        assertNotNull(session);


	}
	
	@Test
	public void setUserSessionTestWithException() throws Exception{
		EPUser user = mockUser.mockEPUser();
        Set<String> applicationMenuData = new HashSet<String>();
        applicationMenuData.add("test_application_menuData");
        Set<String> businessDirectMenuData = new HashSet<String>();
        businessDirectMenuData.add("test_businessDirect_menuData");
        HttpSession session = mockedRequest.getSession();
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPRoleFunctionService.class);
		PowerMockito.mockStatic(MenuBuilder.class);
		List<RoleFunction> roleFunctions=new ArrayList<RoleFunction>();
		RoleFunction roleFunction=new RoleFunction();
		roleFunction.setId(1L);
		roleFunction.setCode("test_code");
		roleFunctions.add(roleFunction);
		Mockito.when(epRoleFunctionService.getRoleFunctions()).thenReturn(null);
		Mockito.when(MenuBuilder.filterMenu(applicationMenuData, mockedRequest)).thenReturn(applicationMenuData);
		PowerMockito.when(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME)).thenReturn("12");
        EPUserUtils.setUserSession(mockedRequest, user, applicationMenuData, businessDirectMenuData,  epRoleFunctionService);
        assertNotNull(session);
	}
	
	@Test(expected=SessionExpiredException.class)
	public void setUserSessionTestWithsessionException() throws Exception{
		EPUser user = mockUser.mockEPUser();
        Set<String> applicationMenuData = new HashSet<String>();
        applicationMenuData.add("test_application_menuData");
        Set<String> businessDirectMenuData = new HashSet<String>();
        businessDirectMenuData.add("test_businessDirect_menuData");
        HttpSession session = mockedRequest.getSession();
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(EPRoleFunctionService.class);
		PowerMockito.mockStatic(AppUtils.class);
		PowerMockito.mockStatic(MenuBuilder.class);
		List<RoleFunction> roleFunctions=new ArrayList<RoleFunction>();
		RoleFunction roleFunction=new RoleFunction();
		roleFunction.setId(1L);
		roleFunction.setCode("test_code");
		roleFunctions.add(roleFunction);
        Mockito.when(AppUtils.getSession(mockedRequest)).thenReturn(null);
		Mockito.when(epRoleFunctionService.getRoleFunctions()).thenReturn(null);
		Mockito.when(MenuBuilder.filterMenu(applicationMenuData, mockedRequest)).thenReturn(applicationMenuData);
		PowerMockito.when(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME)).thenReturn("12");
        EPUserUtils.setUserSession(mockedRequest, user, applicationMenuData, businessDirectMenuData, epRoleFunctionService);
        assertNotNull(session);


	}
	
	
	@Test(expected=SessionExpiredException.class)
	public void getUserSessionTest(){
		PowerMockito.mockStatic(AppUtils.class);
		PowerMockito.when(AppUtils.getSession(mockedRequest)).thenReturn(null);
		EPUserUtils.getUserSession(mockedRequest);
	}
	
}
