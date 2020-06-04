/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017-2018 AT&T Intellectual Property. All rights reserved.
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
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.domain.MicroserviceData;
import org.onap.portalapp.portal.domain.MicroserviceParameter;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.onap.portalsdk.core.onboarding.util.KeyConstants;
import org.onap.portalsdk.core.onboarding.util.KeyProperties;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.service.DataAccessServiceImpl;
import org.onap.portalsdk.core.util.SystemProperties;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Criterion.class, Restrictions.class, CipherUtil.class, EPCommonSystemProperties.class, SystemProperties.class, KeyProperties.class, KeyConstants.class})
public class MicroserviceServiceImplTest {
	
	private static final String TEST="test";
	@Mock
	DataAccessService dataAccessService = new DataAccessServiceImpl();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@InjectMocks
	MicroserviceServiceImpl microserviceServiceImpl = new MicroserviceServiceImpl();

	@Test
	public void saveMicroserviceTest() throws Exception {
		MicroserviceData microserviceData = new MicroserviceData();
		List<MicroserviceParameter> microserviceParameters = new ArrayList<>();
		MicroserviceParameter microserviceParameter = new MicroserviceParameter();
		microserviceParameter.setId(1l);
		microserviceParameter.setPara_key("test");
		microserviceParameter.setPara_value("test");
		microserviceParameters.add(microserviceParameter);
		microserviceData.setActive("true");
		microserviceData.setAppId(1l);
		microserviceData.setId(1l);
		microserviceData.setParameterList(microserviceParameters); 
		Long actual = microserviceServiceImpl.saveMicroservice(microserviceData);
		assertEquals((Long)1l, actual);
	}
	
	@Test
	public void saveServiceParametersTest() throws Exception {
		MicroserviceData microserviceData = new MicroserviceData();
		List<MicroserviceParameter> microserviceParameters = new ArrayList<>();
		MicroserviceParameter microserviceParameter = new MicroserviceParameter();
		microserviceParameter.setId(1l);
		microserviceParameter.setPara_key("test");
		microserviceParameter.setPara_value("test");
		microserviceParameters.add(microserviceParameter);
		microserviceData.setActive("true");
		microserviceData.setAppId(1l);
		microserviceData.setId(1l);
		microserviceData.setParameterList(microserviceParameters); 
		microserviceServiceImpl.saveServiceParameters(1l, microserviceParameters);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getMicroserviceDataByIdTest() throws Exception {
		PowerMockito.mockStatic(Restrictions.class);
		PowerMockito.mockStatic(Criterion.class);
		MicroserviceData microserviceData = new MicroserviceData();
		List<MicroserviceParameter> microserviceParameters = new ArrayList<>();
		MicroserviceParameter microserviceParameter = new MicroserviceParameter();
		microserviceParameter.setId(1l);
		microserviceParameter.setPara_key("test");
		microserviceParameter.setPara_value("test");
		microserviceParameters.add(microserviceParameter);
		microserviceData.setActive("true");
		microserviceData.setAppId(1l);
		microserviceData.setId(1l);
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion idCriterion = Restrictions.eq("id", 1l);
		restrictionsList.add(idCriterion);
		List<MicroserviceData> microserviceDatas = new ArrayList<>();
		microserviceDatas.add(microserviceData);
		Mockito.when((List<MicroserviceData>) dataAccessService.getList(MicroserviceData.class, null, restrictionsList, null)).thenReturn(microserviceDatas);
		List<Criterion> restrictionsList2 = new ArrayList<Criterion>();
		Criterion serviceIdCriterion = Restrictions.eq("serviceId", 1l);
		restrictionsList2.add(serviceIdCriterion);
		Mockito.when((List<MicroserviceParameter>) dataAccessService.getList(MicroserviceParameter.class, null, restrictionsList2, null)).thenReturn(microserviceParameters);
		MicroserviceData actual = microserviceServiceImpl.getMicroserviceDataById(1l);
		assertNotNull(actual);
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Test
	public void getMicroserviceDataTest() throws Exception {
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(CipherUtil.class);
		PowerMockito.mockStatic(Restrictions.class);
		PowerMockito.mockStatic(Criterion.class);
		PowerMockito.mockStatic(KeyProperties.class);
		PowerMockito.mockStatic(KeyConstants.class);
		List<MicroserviceParameter> microserviceParameters = new ArrayList<>();
		MicroserviceData microserviceData = new MicroserviceData();
		MicroserviceParameter microserviceParameter = new MicroserviceParameter();
		microserviceParameter.setId(1l);
		microserviceParameter.setPara_key("test");
		microserviceParameter.setPara_value("test");
		microserviceParameters.add(microserviceParameter);
		microserviceData.setActive("true");
		microserviceData.setAppId(1l);
		microserviceData.setPassword("xyz");
		microserviceData.setId(1l);
		List<MicroserviceData> microserviceDatas = new ArrayList<>();
		microserviceDatas.add(microserviceData);
		Mockito.when((List<MicroserviceData>) dataAccessService.getList(MicroserviceData.class, null)).thenReturn(microserviceDatas);
		List<Criterion> restrictionsList2 = new ArrayList<Criterion>();
		Criterion serviceIdCriterion = Restrictions.eq("serviceId", 1l);
		restrictionsList2.add(serviceIdCriterion);
		Mockito.when((List<MicroserviceParameter>) dataAccessService.getList(MicroserviceParameter.class, null, restrictionsList2, null)).thenReturn(microserviceParameters);
		Mockito.when(CipherUtil.decryptPKC("xyz",KeyProperties.getProperty(KeyConstants.CIPHER_ENCRYPTION_KEY))).thenReturn("abc");
		List<MicroserviceData> actual = microserviceServiceImpl.getMicroserviceData();
		assertNotNull(actual);
	}
	
	@SuppressWarnings({ "unchecked", "deprecation", "static-access" })
	@Test
	public void getMicroserviceDataBadPaddingExceptionTest() throws Exception {
		PowerMockito.mockStatic(SystemProperties.class);
		PowerMockito.mockStatic(CipherUtil.class);
		PowerMockito.mockStatic(Restrictions.class);
		PowerMockito.mockStatic(Criterion.class);
		PowerMockito.mockStatic(KeyProperties.class);
		PowerMockito.mockStatic(KeyConstants.class);
		List<MicroserviceParameter> microserviceParameters = new ArrayList<>();
		MicroserviceData microserviceData = new MicroserviceData();
		MicroserviceParameter microserviceParameter = new MicroserviceParameter();
		microserviceParameter.setId(1l);
		microserviceParameter.setPara_key("test");
		microserviceParameter.setPara_value("test");
		microserviceParameters.add(microserviceParameter);
		microserviceData.setActive("true");
		microserviceData.setAppId(1l);
		microserviceData.setPassword("xyz");
		microserviceData.setId(1l);
		List<MicroserviceData> microserviceDatas = new ArrayList<>();
		microserviceDatas.add(microserviceData);
		Mockito.when((List<MicroserviceData>) dataAccessService.getList(MicroserviceData.class, null)).thenReturn(microserviceDatas);
		List<Criterion> restrictionsList2 = new ArrayList<Criterion>();
		Criterion serviceIdCriterion = Restrictions.eq("serviceId", 1l);
		restrictionsList2.add(serviceIdCriterion);
		Mockito.when((List<MicroserviceParameter>) dataAccessService.getList(MicroserviceParameter.class, null, restrictionsList2, null)).thenReturn(microserviceParameters);
		Mockito.when(CipherUtil.decryptPKC("xyz",KeyProperties.getProperty(KeyConstants.CIPHER_ENCRYPTION_KEY))).thenThrow(BadPaddingException.class);
		List<MicroserviceData> actual = microserviceServiceImpl.getMicroserviceData();
		assertNotNull(actual);
	}
	
	@Test
	public void updateMicroservice()throws Exception {
		
		List<MicroserviceParameter> microserviceParameters = new ArrayList<>();
		MicroserviceParameter microserviceParameter = new MicroserviceParameter();
		microserviceParameter.setId(1l);
		microserviceParameter.setPara_key(TEST);
		microserviceParameter.setPara_value(TEST);
		microserviceParameters.add(microserviceParameter);
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		
		PowerMockito.mockStatic(Restrictions.class);
		PowerMockito.mockStatic(CipherUtil.class);
		Criterion serviceIdCriterion = Restrictions.eq("serviceId", 1l);
		restrictionsList.add(serviceIdCriterion);
		PowerMockito.mockStatic(KeyProperties.class);
		PowerMockito.mockStatic(KeyConstants.class);
		Mockito.when(KeyProperties.getProperty(KeyConstants.CIPHER_ENCRYPTION_KEY)).thenReturn(TEST);
		Mockito.when(CipherUtil.encryptPKC(TEST, TEST)).thenReturn(TEST);
		Mockito.when((List<MicroserviceParameter>) dataAccessService.getList(MicroserviceParameter.class, null, restrictionsList, null)).thenReturn(microserviceParameters);
		microserviceServiceImpl.updateMicroservice(1l, buildData());
		
	}
	
	@Test
	public void getParametersById() {
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		PowerMockito.mockStatic(Restrictions.class);
		PowerMockito.mockStatic(CipherUtil.class);
		Criterion serviceIdCriterion = Restrictions.eq("serviceId", 1l);
		restrictionsList.add(serviceIdCriterion);
		Mockito.when((List<MicroserviceParameter>) dataAccessService.getList(MicroserviceParameter.class, null, restrictionsList, null)).thenReturn(buildData().getParameterList());
		
		List<MicroserviceParameter> parameters=	microserviceServiceImpl.getParametersById(1l);
		assertEquals(TEST, parameters.get(0).getPara_key());
	}
	
	@Test
	public void deleteMicroservice()throws Exception {
		microserviceServiceImpl.deleteMicroservice(1l);
		
	}
	
	
	public MicroserviceData buildData() {
		MicroserviceData microserviceData=new MicroserviceData();
		microserviceData.setId((long)1);
		microserviceData.setName(TEST);
		microserviceData.setActive(TEST);
		microserviceData.setDesc(TEST);
		microserviceData.setAppId((long)1);
		microserviceData.setUrl(TEST);
		microserviceData.setSecurityType(TEST);
		microserviceData.setUsername(TEST);
		microserviceData.setPassword(TEST);
		
		List<MicroserviceParameter> microserviceParameters = new ArrayList<>();
		MicroserviceParameter microserviceParameter = new MicroserviceParameter();
		microserviceParameter.setId(1l);
		microserviceParameter.setPara_key(TEST);
		microserviceParameter.setPara_value(TEST);
		microserviceParameters.add(microserviceParameter);
		microserviceData.setParameterList(microserviceParameters);
		
		return microserviceData;
	}
	
}
