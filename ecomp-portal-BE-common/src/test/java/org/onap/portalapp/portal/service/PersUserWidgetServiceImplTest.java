/*
* ============LICENSE_START=======================================================
* ONAP : PORTAL
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
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.PersUserWidgetSelection;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalsdk.core.service.DataAccessService;

public class PersUserWidgetServiceImplTest {
	
	@Mock
	DataAccessService dataAccessService;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@InjectMocks
	PersUserWidgetServiceImpl  persUserWidgetServiceImpl = new PersUserWidgetServiceImpl();
	
	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();
	
	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();
	MockEPUser mockUser = new MockEPUser();
	
		
	@Test(expected = IllegalArgumentException.class)
	public void setPersUserAppValueIfUserNull() {
		persUserWidgetServiceImpl.setPersUserAppValue(null, null, false);
	}
	
	@Test
	public void setPersUserAppValueTest() {
		EPUser user = mockUser.mockEPUser();
		List<PersUserWidgetSelection> persUserAppSelectionList = new ArrayList<>();
		PersUserWidgetSelection persUserWidgetSelection = new PersUserWidgetSelection();
		persUserWidgetSelection.setId((long) 1);
		persUserAppSelectionList.add(persUserWidgetSelection);
		Mockito.when(dataAccessService.getList(PersUserWidgetSelection.class, "test", null, null))
				.thenReturn(persUserAppSelectionList);
		persUserWidgetServiceImpl.setPersUserAppValue(user, (long)999, false);
	}
	
	public void setPersUserAppValueTestPass() {
		EPUser user = mockUser.mockEPUser();
		List<PersUserWidgetSelection> persUserAppSelectionList = new ArrayList<>();
		PersUserWidgetSelection persUserWidgetSelection = new PersUserWidgetSelection();
		persUserWidgetSelection.setId((long) 1);
		persUserAppSelectionList.add(persUserWidgetSelection);
		Mockito.when(dataAccessService.getList(PersUserWidgetSelection.class, "test", null, null))
				.thenReturn(persUserAppSelectionList);
		persUserWidgetServiceImpl.setPersUserAppValue(user, (long)999, false);
	}
	
	@Test
	public void setPersUserAppValueIfSelectTest_DeleteDomain() {
		EPUser user = mockUser.mockEPUser();
		List<PersUserWidgetSelection> persUserAppSelectionList = new ArrayList<>();
		PersUserWidgetSelection persUserWidgetSelection = new PersUserWidgetSelection();
		persUserWidgetSelection.setId((long) 1);
		persUserAppSelectionList.add(persUserWidgetSelection);
		Mockito.when(dataAccessService.getList(PersUserWidgetSelection.class, "test", null, null))
				.thenReturn(persUserAppSelectionList);
		persUserWidgetServiceImpl.setPersUserAppValue(user, (long)999, true);
		Mockito.doNothing().when(dataAccessService).deleteDomainObject(persUserWidgetSelection, null);
	}
	
	@Test
	public void setPersUserAppValueIfSelectTest_SaveDomain() {
		EPUser user = mockUser.mockEPUser();
		List<PersUserWidgetSelection> persUserAppSelectionList = new ArrayList<>();
		PersUserWidgetSelection persUserWidgetSelection = new PersUserWidgetSelection();
		persUserWidgetSelection.setId(null);
		persUserAppSelectionList.add(persUserWidgetSelection);
		Mockito.when(dataAccessService.getList(PersUserWidgetSelection.class, "test", null, null))
				.thenReturn(persUserAppSelectionList);
		Mockito.doNothing().when(dataAccessService).saveDomainObject(persUserWidgetSelection, null);
		persUserWidgetServiceImpl.setPersUserAppValue(user, (long)999, true);
		persUserWidgetSelection.setStatusCode("S");
		Mockito.doNothing().when(dataAccessService).saveDomainObject(persUserWidgetSelection, null);
	}
	
	@Test
	public void setPersUserAppValueWithoutSelectTest_DeleteDomain() {
		EPUser user = mockUser.mockEPUser();
		List<PersUserWidgetSelection> persUserAppSelectionList = new ArrayList<>();
		PersUserWidgetSelection persUserWidgetSelection = new PersUserWidgetSelection();
		persUserAppSelectionList.add(persUserWidgetSelection);
		Mockito.when(dataAccessService.getList(PersUserWidgetSelection.class, "test", null, null))
				.thenReturn(persUserAppSelectionList);
		persUserWidgetServiceImpl.setPersUserAppValue(user, (long)999, false);
		persUserWidgetSelection.setId((long) 1);
		Mockito.doNothing().when(dataAccessService).deleteDomainObject(persUserWidgetSelection, null);
	}
	
	@Test
	public void setPersUserAppValueWithoutSelectTest_SaveDomain() {
		EPUser user = mockUser.mockEPUser();
		List<PersUserWidgetSelection> persUserAppSelectionList = new ArrayList<>();
		PersUserWidgetSelection persUserWidgetSelection = new PersUserWidgetSelection();
		persUserWidgetSelection.setId(null);
		persUserAppSelectionList.add(persUserWidgetSelection);
		Mockito.when(dataAccessService.getList(PersUserWidgetSelection.class, "test", null, null))
				.thenReturn(persUserAppSelectionList);
		persUserWidgetServiceImpl.setPersUserAppValue(user, (long)999, false);
		persUserWidgetSelection.setStatusCode("H");
		Mockito.doNothing().when(dataAccessService).saveDomainObject(persUserWidgetSelection, null);
	}

	
}
