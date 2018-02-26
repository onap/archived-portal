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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalsdk.core.command.support.SearchResult;
import org.onap.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ EcompPortalUtils.class, PortalConstants.class, SystemProperties.class,
		EPCommonSystemProperties.class })
public class SearchServiceImplTest {

	@InjectMocks
	SearchServiceImpl searchServiceImpl = new SearchServiceImpl();

	@Mock
	EPLdapService epLdapService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	MockEPUser mockUser = new MockEPUser();
	
	@SuppressWarnings("unchecked")
	@Test
	public void searchUsersInPhoneBookTest() throws Exception {
		PowerMockito.mockStatic(EcompPortalUtils.class);
		List<String> tokens = new ArrayList<>();
		tokens.add("ts1234");
		tokens.add("TestName");
		EPUser user = new EPUser();
		user.setOrgUserId("ts1234");
		user.setFirstName("TestName");
		Mockito.when(EcompPortalUtils.parsingByRegularExpression(Matchers.anyString(), Matchers.anyString())).thenReturn(tokens);
		SearchResult searchResult  = new SearchResult();
		searchResult.add(user);
		Mockito.when(epLdapService.searchPost(Matchers.any(EPUser.class), Matchers.anyString(), Matchers.anyString(), Matchers.anyString(), Matchers.anyInt(), Matchers.anyInt(), Matchers.anyInt())).thenReturn(searchResult);
		String actual = searchServiceImpl.searchUsersInPhoneBook("ts1234");
		assertNotEquals("[]", actual);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void searchUsersInPhoneBookFirtAndLastNameTest() throws Exception {
		PowerMockito.mockStatic(EcompPortalUtils.class);
		List<String> tokens = new ArrayList<>();
		tokens.add("ts1234");
		tokens.add("TestFirstName");
		tokens.add("TestLastName");
		EPUser user = new EPUser();
		user.setOrgUserId("ts1234");
		user.setFirstName("TestFirstName");
		user.setLastName("TestLastName");	
		Mockito.when(EcompPortalUtils.parsingByRegularExpression(Matchers.anyString(), Matchers.anyString())).thenReturn(tokens);
		SearchResult searchResult  = new SearchResult();
		searchResult.add(user);
		Mockito.when(epLdapService.searchPost(Matchers.any(EPUser.class), Matchers.anyString(), Matchers.anyString(), Matchers.anyString(), Matchers.anyInt(), Matchers.anyInt(), Matchers.anyInt())).thenReturn(searchResult);
		String actual = searchServiceImpl.searchUsersInPhoneBook("ts1234");
		assertNotEquals("[]", actual);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void searchUserByUserIdTest() throws Exception {
		EPUser user = new EPUser();
		user.setOrgUserId("ts1234");
		SearchResult searchResult  = new SearchResult();
		searchResult.add(user);
		Mockito.when(epLdapService.searchPost(Matchers.any(EPUser.class), Matchers.anyString(), Matchers.anyString(), Matchers.anyString(), Matchers.anyInt(), Matchers.anyInt(), Matchers.anyInt())).thenReturn(searchResult);
		EPUser actual = searchServiceImpl.searchUserByUserId("ts1234");
		assertEquals(user, actual);
	}
	
	@Test
	public void searchUserByUserIdExceptionTest() throws Exception {
		Mockito.doThrow(new NullPointerException()).when(epLdapService).searchPost(Matchers.any(EPUser.class), Matchers.anyString(), Matchers.anyString(), Matchers.anyString(), Matchers.anyInt(), Matchers.anyInt(), Matchers.anyInt());
		EPUser actual = searchServiceImpl.searchUserByUserId("ts1234");
		assertNull(actual);
	}
}
