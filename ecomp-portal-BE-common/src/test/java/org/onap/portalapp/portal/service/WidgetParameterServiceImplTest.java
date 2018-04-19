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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.WidgetCatalogParameter;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.service.DataAccessServiceImpl;

public class WidgetParameterServiceImplTest {
	@Mock
	DataAccessService dataAccessService = new DataAccessServiceImpl();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@InjectMocks 
	WidgetParameterServiceImpl widgetParameterServiceImpl = new WidgetParameterServiceImpl();
	
	MockEPUser mockUser = new MockEPUser();
	
	@SuppressWarnings("unchecked")
	@Test
	public void getUserParamByIdTest() {
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion widgetIdCrit = Restrictions.eq("widgetId", "1");
		restrictionsList.add(widgetIdCrit);
		Criterion userIdCrit = Restrictions.eq("userId", "1");
		restrictionsList.add(userIdCrit);
		Criterion paramIdCrit = Restrictions.eq("paramId", "1");
		restrictionsList.add(paramIdCrit);
		List<WidgetCatalogParameter> mockWidgetCatParamsList = new ArrayList<>();
		Mockito.when((List<WidgetCatalogParameter>)dataAccessService
				.getList(WidgetCatalogParameter.class, null, restrictionsList, null)).thenReturn(mockWidgetCatParamsList);
		WidgetCatalogParameter widgetCatalogParameter = widgetParameterServiceImpl.getUserParamById(1l, 1l, 1l);
		if(mockWidgetCatParamsList.isEmpty()) {
			mockWidgetCatParamsList = null;
		}
		assertEquals(widgetCatalogParameter, mockWidgetCatParamsList);
	}
	
	@Test
	public void saveUserParameterTest(){
		WidgetCatalogParameter mockWidgetParameter =  new WidgetCatalogParameter();
		WidgetCatalogParameter widgetParameter =  new WidgetCatalogParameter();
		widgetParameter.setId(1l);
		widgetParameter.setParamId(1l);
		widgetParameter.setUser_value("test");
		widgetParameter.setWidgetId(1l);
		widgetParameter.setUserId(1l);
		Mockito.doNothing().when(dataAccessService).saveDomainObject(mockWidgetParameter, null);
		widgetParameterServiceImpl.saveUserParameter(widgetParameter);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getUserParameterByIdTest() {
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion paramIdCrit = Restrictions.eq("paramId", 1);
		restrictionsList.add(paramIdCrit);
		List<WidgetCatalogParameter> mockWidgetCatalogParameter = new ArrayList<>();
		Mockito.when((List<WidgetCatalogParameter>) dataAccessService
				.getList(WidgetCatalogParameter.class, null, restrictionsList, null)).thenReturn(mockWidgetCatalogParameter);
		List<WidgetCatalogParameter> list = widgetParameterServiceImpl.getUserParameterById(1l);
		assertEquals(list, mockWidgetCatalogParameter);
	}
	
	@Test 
	public void deleteUserParameterById() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("paramId", Long.toString(1));
		Mockito.when(dataAccessService.executeNamedQuery("deleteWidgetCatalogParameter", params, null)).thenReturn(null);
		Mockito.when(dataAccessService.executeNamedQuery("deleteMicroserviceParameterById", params, null)).thenReturn(null);
		widgetParameterServiceImpl.deleteUserParameterById(1l);
	}
	
}
