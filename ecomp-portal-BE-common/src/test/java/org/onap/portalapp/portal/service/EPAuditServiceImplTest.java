/*
* ============LICENSE_START=======================================================
* ONAP : PORTAL
* ================================================================================
* Copyright (C) 2018 TechMahindra
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onap.portalapp.portal.core.MockEPUser;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalapp.portal.domain.EPUser;

public class EPAuditServiceImplTest {
	
   @Mock
   DataAccessService dataAccessService;

   @Before
   public void setup() {
      MockitoAnnotations.initMocks(this);
   }

   @InjectMocks
   EPAuditServiceImpl ePAuditServiceImpl = new EPAuditServiceImpl ();
	
   MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();
	
   HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
   HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
   MockEPUser mockUser = new MockEPUser();
	
   @Test
   public void getDataAccessServiceTest() {
	   dataAccessService=ePAuditServiceImpl.getDataAccessService();
	   assertNotNull(dataAccessService);
   }
   
   @Test
   public void getNullUserLastLoginTest() {
      String id=null;
	  Date dt=null;
	  Date date =	ePAuditServiceImpl.getGuestLastLogin(id);
      assertNull(date);
   }
   
   @Test
   public void getGuestLastLoginTest() {
	  EPUser epUser=null;
      epUser=mockUser.mockEPUser();
      String id = epUser.getOrgUserId();
	  Date date =	ePAuditServiceImpl.getGuestLastLogin(id);
      assertNull(date);
   }
	
   @Test(expected = NullPointerException.class)
   public void delAuditLogFromDayTest() {
	   dataAccessService=ePAuditServiceImpl.getDataAccessService();
	   ePAuditServiceImpl.delAuditLogFromDay();
   }
   
}

