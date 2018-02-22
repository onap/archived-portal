/*
* ============LICENSE_START=======================================================
* ONAP  PORTAL
* ================================================================================
* Copyright 2018 TechMahindra
*=================================================================================
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
* ============LICENSE_END=========================================================
*/
package org.onap.portalapp.portal.service;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.service.EPProfileServiceImpl;
import org.onap.portalsdk.core.dao.ProfileDao;
import org.onap.portalsdk.core.domain.Profile;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.service.DataAccessServiceImpl;

public class EPProfileServiceImplTest {

	@Mock
	DataAccessService dataAccessService = new DataAccessServiceImpl();
	
	@Mock
	ProfileDao profileDao;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@InjectMocks
	EPProfileServiceImpl epProfileServiceImpl = new EPProfileServiceImpl();

	MockEPUser mockUser = new MockEPUser();
	
	@Test
	public void findAllTest() {
		List<Profile> profileList = new ArrayList<>();
		Mockito.when(dataAccessService.getList(Profile.class, null)).thenReturn(profileList);
		List<Profile> expectedRoleList = epProfileServiceImpl.findAll();
		assertEquals(expectedRoleList, profileList);
	}
	
	@Test(expected = java.lang.NumberFormatException.class)
	public void getUserTest() {
		EPUser epUser = new EPUser();
		Mockito.when(dataAccessService.getDomainObject(EPUser.class, Long.parseLong("test"), null)).thenReturn(epUser);
		epProfileServiceImpl.getUser("test");
	}
	
	@Test
	public void saveUserTest() {
		EPUser user = mockUser.mockEPUser();
		Mockito.doNothing().when(dataAccessService).saveDomainObject(user,  null);
		epProfileServiceImpl.saveUser(user);
	}
	
	@Test
	public void getProfileTest() {
		epProfileServiceImpl.getProfile(1);
	
	}

}
