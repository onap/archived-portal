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
package org.onap.portalapp.portal.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.EPUserApp;
import org.onap.portalapp.portal.domain.PersUserAppSelection;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.AdminRolesService;
import org.onap.portalapp.portal.service.PersUserAppServiceImpl;
import org.onap.portalapp.portal.service.UserRolesService;
import org.onap.portalsdk.core.service.DataAccessService;

public class PersUserAppServiceImplTest {

	@Mock
	DataAccessService dataAccessService;
	@Mock
	AdminRolesService adminRolesService;
	@Mock
	UserRolesService userRolesService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@InjectMocks
	PersUserAppServiceImpl persUserAppServiceImpl = new PersUserAppServiceImpl();

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();
	MockEPUser mockUser = new MockEPUser();

	public EPApp getApp() {
		EPApp app = new EPApp();
		app.setName("Test");
		app.setImageUrl("test");
		app.setDescription("test");
		app.setNotes("test");
		app.setUrl("test");
		app.setId((long) 1);
		app.setAppRestEndpoint("test");
		app.setAlternateUrl("test");
		app.setName("test");
		app.setMlAppName("test");
		app.setMlAppAdminId("test");
		app.setUsername("test");
		app.setAppPassword("test");
		app.setOpen(true);
		app.setEnabled(false);
		app.setUebKey("test");
		app.setUebSecret("test");
		app.setUebTopicName("test");
		app.setAppType(1);
		return app;
	}

	@Test(expected = IllegalArgumentException.class)
	public void setPersUserAppValueIfUserNull() {
		persUserAppServiceImpl.setPersUserAppValue(null, null, false, false);
	}

	@Test
	public void setPersUserAppValueTest() {
		EPApp app = getApp();
		EPUser user = mockUser.mockEPUser();
		List<PersUserAppSelection> persUserAppSelectionList = new ArrayList<>();
		PersUserAppSelection persUserAppSelection = new PersUserAppSelection();
		persUserAppSelection.setId((long) 1);
		persUserAppSelectionList.add(persUserAppSelection);
		Mockito.when(dataAccessService.getList(PersUserAppSelection.class, "test", null, null))
				.thenReturn(persUserAppSelectionList);
		Mockito.doNothing().when(dataAccessService).deleteDomainObject(persUserAppSelection, null);
		persUserAppServiceImpl.setPersUserAppValue(user, app, false, true);
	}

	@Test
	public void setPersUserAppValueIfSelectTest() {
		EPApp app = getApp();
		EPUser user = mockUser.mockEPUser();
		List<PersUserAppSelection> persUserAppSelectionList = new ArrayList<>();
		PersUserAppSelection persUserAppSelection = new PersUserAppSelection();
		persUserAppSelection.setId((long) 1);
		persUserAppSelectionList.add(persUserAppSelection);
		Mockito.when(dataAccessService.getList(PersUserAppSelection.class, "test", null, null))
				.thenReturn(persUserAppSelectionList);
		Mockito.doNothing().when(dataAccessService).saveDomainObject(persUserAppSelection, null);
		persUserAppServiceImpl.setPersUserAppValue(user, app, true, true);
	}

	@Test
	public void setPersUserAppValueIfOpenTest() {
		EPApp app = getApp();
		app.setOpen(false);
		EPUser user = mockUser.mockEPUser();
		List<PersUserAppSelection> persUserAppSelectionList = new ArrayList<>();
		PersUserAppSelection persUserAppSelection = new PersUserAppSelection();
		persUserAppSelection.setId((long) 1);
		persUserAppSelectionList.add(persUserAppSelection);
		Mockito.when(dataAccessService.getList(PersUserAppSelection.class, "test", null, null))
				.thenReturn(persUserAppSelectionList);
		Mockito.doNothing().when(dataAccessService).saveDomainObject(persUserAppSelection, null);
		persUserAppServiceImpl.setPersUserAppValue(user, app, true, true);
	}

	@Test
	public void setPersUserAppValueIfAppNotOpenTest() {
		EPApp app = getApp();
		app.setOpen(false);
		EPUser user = mockUser.mockEPUser();
		List<PersUserAppSelection> persUserAppSelectionList = new ArrayList<>();
		PersUserAppSelection persUserAppSelection = new PersUserAppSelection();
		persUserAppSelection.setId((long) 1);
		persUserAppSelectionList.add(persUserAppSelection);
		Mockito.when(dataAccessService.getList(PersUserAppSelection.class, "test", null, null))
				.thenReturn(persUserAppSelectionList);
		Mockito.doNothing().when(dataAccessService).saveDomainObject(persUserAppSelection, null);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		List<EPUserApp> roles = new ArrayList<>();
		EPUserApp epUserApp = new EPUserApp();
		roles.add(epUserApp);
		Mockito.when(userRolesService.getCachedAppRolesForUser(app.getId(), user.getId())).thenReturn(roles);
		persUserAppServiceImpl.setPersUserAppValue(user, app, true, false);
	}

	@Test
	public void setPersUserAppValueIfNotPortalAdminTest() {
		EPApp app = getApp();
		app.setOpen(false);
		EPUser user = mockUser.mockEPUser();
		List<PersUserAppSelection> persUserAppSelectionList = new ArrayList<>();
		PersUserAppSelection persUserAppSelection = new PersUserAppSelection();
		persUserAppSelection.setId((long) 1);
		persUserAppSelectionList.add(persUserAppSelection);
		Mockito.when(dataAccessService.getList(PersUserAppSelection.class, "test", null, null))
				.thenReturn(persUserAppSelectionList);
		Mockito.doNothing().when(dataAccessService).deleteDomainObject(persUserAppSelection, null);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		List<EPUserApp> roles = new ArrayList<>();
		EPUserApp epUserApp = new EPUserApp();
		roles.add(epUserApp);
		Mockito.when(userRolesService.getCachedAppRolesForUser(app.getId(), user.getId())).thenReturn(roles);
		persUserAppServiceImpl.setPersUserAppValue(user, app, true, false);
	}

	@Test
	public void setPersUserAppValueNewTest() {
		EPApp app = getApp();
		app.setOpen(false);
		EPUser user = mockUser.mockEPUser();
		List<PersUserAppSelection> persUserAppSelectionList = new ArrayList<>();
		PersUserAppSelection persUserAppSelection = new PersUserAppSelection();
		persUserAppSelection.setId((long) 1);
		persUserAppSelectionList.add(persUserAppSelection);
		Mockito.when(dataAccessService.getList(PersUserAppSelection.class, "test", null, null))
				.thenReturn(persUserAppSelectionList);
		Mockito.doNothing().when(dataAccessService).saveDomainObject(persUserAppSelection, null);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		List<EPUserApp> roles = new ArrayList<>();
		EPUserApp epUserApp = new EPUserApp();
		roles.add(epUserApp);
		Mockito.when(userRolesService.getCachedAppRolesForUser(app.getId(), user.getId())).thenReturn(roles);
		persUserAppServiceImpl.setPersUserAppValue(user, app, false, false);
	}
}
