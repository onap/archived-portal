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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.framework.MockEPUser;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.SearchServiceImpl;
import org.onap.portalapp.portal.service.UserService;
import org.onap.portalapp.portal.transport.UserWithNameSurnameTitle;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ EcompPortalUtils.class})
public class SearchServiceImplTest {
	
	@InjectMocks
	SearchServiceImpl searchServiceImpl = new SearchServiceImpl();

	@Mock
	UserService userService;
	

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();

	NullPointerException nullPointerException = new NullPointerException();
	MockEPUser mockUser = new MockEPUser();
	
	@Test
	public void searchUsersInPhoneBookTest()
	{
		PowerMockito.mockStatic(EcompPortalUtils.class);
		List<String> list = new ArrayList<>();
		String str = "Test";
		list.add(str);
		Mockito.when(EcompPortalUtils.parsingByRegularExpression("Test", " ")).thenReturn(list);
		assertEquals(searchServiceImpl.searchUsersInPhoneBook("Test"), "[]");
	}
	
	@Test
	public void searchUsersInFnTableToFindUserIdTest()
	{
	PowerMockito.mockStatic(EcompPortalUtils.class);
	List<String> list = new ArrayList<>();
	String str = "Test";
	String str2 = "Test new";
	String str1 = "Test new1";
	list.add(str);
	list.add(str1);
	list.add(str2);
	Mockito.when(EcompPortalUtils.parsingByRegularExpression("Test", " ")).thenReturn(list);
	List<EPUser> userList = new ArrayList();
	EPUser user = mockUser.mockEPUser();
	user.setLastName("Test new");
	userList.add(user);
	Mockito.when( this.userService.getUserByFirstLastName("Test","Test new")).thenReturn(userList);
	String result = searchServiceImpl.searchUsersInPhoneBook("Test");
	assertEquals("[{\"orgUserId\":\"guestT\",\"firstName\":\"test\",\"lastName\":\"Test new\",\"jobTitle\":null}]" , result);
	}
	
	
	
	@Test
	public void searchUsersInFnTableFirstNameTest()
	{
	PowerMockito.mockStatic(EcompPortalUtils.class);
	List<String> list = new ArrayList<>();
	String str = "TestTT";
	String str2 = "Test new1";
	String str1 = "Test new";
	String str3 = "Test new2";
	list.add(str);
	list.add(str1);
	list.add(str2);
	list.add(str3);
	Mockito.when(EcompPortalUtils.parsingByRegularExpression("TestTT", " ")).thenReturn(list);
	List<EPUser> userList = new ArrayList();
	EPUser user = mockUser.mockEPUser();
	user.setLastName("Test new");
	user.setFirstName(null);
	userList.add(user);
	Mockito.when( this.userService.getUserByFirstLastName("TestTT","Test new")).thenReturn(userList);
	assertEquals(searchServiceImpl.searchUsersInPhoneBook("TestTT"), "[]");	}

	
	@Test
	public void searchUsersInFnTableLastNameTest()
	{
		PowerMockito.mockStatic(EcompPortalUtils.class);
	    List<String> list = new ArrayList<>();
		String str = "Test";
		String str2 = "Test new";
		String str1 = "Test new1";
		list.add(str);
		list.add(str1);
		list.add(str2);
		Mockito.when(EcompPortalUtils.parsingByRegularExpression("Test", " ")).thenReturn(list);
		List<EPUser> userList = new ArrayList();
		EPUser user = mockUser.mockEPUser();
		user.setLastName(null);
		userList.add(user);
		Mockito.when( this.userService.getUserByFirstLastName("Test","Test new")).thenReturn(userList);
		assertEquals(searchServiceImpl.searchUsersInPhoneBook("Test"), "[]");	}
		
	

	@Test
	public void searchUserByUserIdTest()
	{
		List<EPUser> userList = new ArrayList();
		EPUser user = mockUser.mockEPUser();
		user.setLastName("Test new");
		userList.add(user);
		List<EPUser> foundUsers = new ArrayList<EPUser>();
		Mockito.when(this.userService.getUserByUserId("guestT")).thenReturn(userList);
		
		EPUser expectedUser = searchServiceImpl.searchUserByUserId("guestT");
		assertEquals(user, expectedUser);
	}
	
	@Test
	public void searchUserByUserIdExceptionTest()
	{
		Mockito.when(this.userService.getUserByUserId("guestT")).thenThrow(nullPointerException);
		assertNull(searchServiceImpl.searchUserByUserId("guestT"));
		
	}
	
	@Test
	public void searchUsersByUserIdTest()
	{
		
		List<EPUser> userList = new ArrayList();
		EPUser user = mockUser.mockEPUser();
		user.setLastName("Test new");
		userList.add(user);
		Mockito.when(this.userService.getUserByUserId("guestT")).thenReturn(userList);
		List<UserWithNameSurnameTitle> foundUsers = searchServiceImpl.searchUsersByUserId(user);
		assertEquals(foundUsers.size(), 1);
		
	}
	
	@Test
	public void searchUsersByUserIdExceptionTest()
	{
		EPUser user = mockUser.mockEPUser();
		user.setLastName("Test new");
		Mockito.when(this.userService.getUserByUserId("guestT")).thenThrow(nullPointerException);
		List<UserWithNameSurnameTitle> foundUsers =  searchServiceImpl.searchUsersByUserId(user);
		assertEquals(foundUsers.size(), 0);
		
	}
	
	@Test
	public void searchUsersByNameExceptionTest()
	{
		EPUser user = mockUser.mockEPUser();
		user.setLastName("test");
		user.setFirstName("test");

		Mockito.when(this.userService.getUserByFirstLastName("test","test")).thenThrow(nullPointerException);
		List<UserWithNameSurnameTitle> foundUsers =	searchServiceImpl.searchUsersByName(user);
		assertEquals(foundUsers.size(), 0);
		
	}
}
