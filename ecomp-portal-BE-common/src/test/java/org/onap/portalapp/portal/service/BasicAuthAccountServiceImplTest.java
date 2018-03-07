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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.BasicAuthCredentials;
import org.onap.portalapp.portal.domain.EPEndpoint;
import org.onap.portalapp.portal.domain.EPEndpointAccount;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.service.DataAccessServiceImpl;
import org.onap.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ CipherUtil.class , SystemProperties.class})
public class BasicAuthAccountServiceImplTest {
	@Mock
	DataAccessService dataAccessService = new DataAccessServiceImpl();
		
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@InjectMocks
	BasicAuthAccountServiceImpl  basicAuthAccountServiceImpl = new BasicAuthAccountServiceImpl();
	
	
	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();
	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();
	MockEPUser mockUser = new MockEPUser();
	
	@Test
	public void saveBasicAuthAccountTest() throws Exception {
		BasicAuthCredentials basicAuthCredentials = new BasicAuthCredentials();
		basicAuthCredentials.setPassword(null);
		Mockito.doNothing().when(dataAccessService).saveDomainObject(basicAuthCredentials, null);
		basicAuthAccountServiceImpl.saveBasicAuthAccount(basicAuthCredentials);
		
	}
	
	@Test
	public void saveBasicAuthAccountTest_password() throws Exception{
		PowerMockito.mockStatic(CipherUtil.class);
		PowerMockito.mockStatic(SystemProperties.class);
		BasicAuthCredentials credentials = new BasicAuthCredentials();
		credentials.setPassword("password");
		String result = null;
		Mockito.when(CipherUtil.encryptPKC("password", SystemProperties.getProperty(SystemProperties.Decryption_Key))).thenReturn(result);
		basicAuthAccountServiceImpl.saveBasicAuthAccount(credentials);
	}
	
	@Test
	public void saveEndpointsTest() throws Exception {
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion NameCrit = Restrictions.eq("name", "test");
		restrictionsList.add(NameCrit);
		List<EPEndpoint> tempList = new ArrayList<>();
		EPEndpoint endpoint = new EPEndpoint();
		endpoint.setId(1l);
		endpoint.setName("name");
		tempList.add(endpoint);
		Mockito.when((List<EPEndpoint>) dataAccessService.getList(EPEndpoint.class, null, restrictionsList, null))
		.thenReturn(tempList);
		EPEndpoint epEndpoint= new EPEndpoint();
		Mockito.doNothing().when(dataAccessService).saveDomainObject(epEndpoint,  null);
		basicAuthAccountServiceImpl.saveEndpoints(epEndpoint);
	}
	
	@Test(expected= NullPointerException.class)
	public void saveEndpointAccountTest() throws Exception {
		EPEndpointAccount record = new EPEndpointAccount();
		record.setAccount_id(1l);
		record.setEp_id(2l);
		Mockito.doNothing().when(dataAccessService).saveDomainObject(record,  null);
		basicAuthAccountServiceImpl.saveEndpointAccount(1l, 2l);
	}
	
	@Test
	public void updateBasicAuthAccountTest() throws Exception {
		BasicAuthCredentials basicAuthCredentials = new BasicAuthCredentials();
		Mockito.doNothing().when(dataAccessService).saveDomainObject(basicAuthCredentials, null);
		List<EPEndpoint> endpoints = new ArrayList<>();
		EPEndpoint epEndpoint = new  EPEndpoint();
		epEndpoint.setId(1l);
		epEndpoint.setName("name");
		endpoints.add(epEndpoint);
		basicAuthCredentials.setEndpoints(endpoints);
		List<EPEndpointAccount> list = null;
		Map<String, Long> params = new HashMap<>();
		params.put("account_id", 1l);
		Mockito.when(dataAccessService.executeNamedQuery("getEPEndpointAccountByAccountId", null, null)).thenReturn(list);
		EPEndpoint temp_ep = new EPEndpoint();
		temp_ep.setId(1l);
		boolean flag = false;
		Map<String, String> params1 = new HashMap<String, String>();
		params1.put("accountId", Long.toString(1l));
		params1.put("epId", Long.toString(1l));
		Mockito.when(dataAccessService.executeNamedQuery("deleteAccountEndpointRecord", params1, null)).thenReturn(null);
		basicAuthAccountServiceImpl.updateBasicAuthAccount(1l, basicAuthCredentials);
	}
	
		
	@Test
	public void getAccountDataTest() throws Exception {
		List<BasicAuthCredentials> list = new ArrayList<>();
		BasicAuthCredentials basicAuthCredentials = new BasicAuthCredentials();
		Mockito.when((List<BasicAuthCredentials>) dataAccessService.getList(BasicAuthCredentials.class, null))
		.thenReturn(list);
		basicAuthAccountServiceImpl.getAccountData();
	}
	
	@Test
	public void getAccountDataTest_password() throws Exception {
		PowerMockito.mockStatic(CipherUtil.class);
		PowerMockito.mockStatic(SystemProperties.class);
		List<BasicAuthCredentials> list = new ArrayList<>();
		BasicAuthCredentials basicAuthCredentials = new BasicAuthCredentials();
		basicAuthCredentials.setPassword("password");
		list.add(basicAuthCredentials);
		Mockito.when((List<BasicAuthCredentials>) dataAccessService.getList(BasicAuthCredentials.class, null))
		.thenReturn(list);
		String result = null;
		Mockito.when(CipherUtil.decryptPKC("password", SystemProperties.getProperty(SystemProperties.Decryption_Key))).thenReturn(result);
		
	}

	@Test
	public void deleteEndpointAccoutTest() throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("accountId", Long.toString(1l));
		Mockito.when(dataAccessService.executeNamedQuery("deleteAccountEndpoint", params, null)).thenReturn(null);
		Mockito.when(dataAccessService.executeNamedQuery("deleteBasicAuthAccount", params, null)).thenReturn(null);
		basicAuthAccountServiceImpl.deleteEndpointAccout(1l);
		
	}
}
