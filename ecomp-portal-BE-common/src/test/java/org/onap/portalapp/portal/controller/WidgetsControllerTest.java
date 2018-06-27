/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
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
package org.onap.portalapp.portal.controller;

import static org.junit.Assert.assertEquals;

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
import org.mockito.stubbing.Answer;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.service.AdminRolesService;
import org.onap.portalapp.portal.service.PersUserWidgetService;
import org.onap.portalapp.portal.service.WidgetService;
import org.onap.portalapp.portal.transport.FieldsValidator;
import org.onap.portalapp.portal.transport.FieldsValidator.FieldName;
import org.onap.portalapp.portal.transport.OnboardingWidget;
import org.onap.portalapp.portal.transport.WidgetCatalogPersonalization;
import org.onap.portalapp.util.EPUserUtils;
import org.springframework.web.client.RestClientException;

public class WidgetsControllerTest  extends MockitoTestSuite{

	@InjectMocks
	WidgetsController widgetsController = new WidgetsController();
	
	@Mock
	private AdminRolesService rolesService;
	
	@Mock
	private WidgetService widgetService ;
	
	@Mock
	private PersUserWidgetService persUserWidgetService;
	
	@Mock
	EPUser epuser;
	
	MockEPUser mockUser = new MockEPUser();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	
	@Test
	public void getOnboardingWidgetsTest() throws RestClientException, Exception{
		EPUser user = mockUser.mockEPUser();
		String getType="test";
		List<OnboardingWidget> actualResult = null;
		String expectedResult = null;
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<OnboardingWidget> onboardingWidgets = null;
		//Mockito.when(StringUtils.isEmpty("")).thenReturn(true);
		Mockito.when(widgetService.getOnboardingWidgets(user, getType.equals("managed"))).thenReturn(onboardingWidgets);
		actualResult = widgetsController.getOnboardingWidgets(mockedRequest, mockedResponse);
		assertEquals(expectedResult, actualResult);
	
	}
	
	@Test
	public void getOnboardingWidgetsTest1(){
		EPUser user = mockUser.mockEPUser();
		Mockito.when(mockedRequest.getHeader("X-Widgets-Type")).thenReturn("managed");
		//String getType=Matchers.any(String.class);
		List<OnboardingWidget> actualResult = null;
		String expectedResult = null;
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<OnboardingWidget> onboardingWidgets = new ArrayList<>();
		OnboardingWidget widget = new OnboardingWidget();
		onboardingWidgets.add(widget);		//Mockito.when(StringUtils.isEmpty("")).thenReturn(true);
		Mockito.when(widgetService.getOnboardingWidgets(user, true)).thenReturn(onboardingWidgets);
		actualResult = widgetsController.getOnboardingWidgets(mockedRequest, mockedResponse);
	//	assertEquals(expectedResult, actualResult);
	
	}
	
	@Test
	public void getOnboardingWidgetswithUserNullTest(){
		EPUser user = null;
		Mockito.when(mockedRequest.getHeader("X-Widgets-Type")).thenReturn("managed");
		//String getType=Matchers.any(String.class);
		List<OnboardingWidget> actualResult = null;
		String expectedResult = null;
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<OnboardingWidget> onboardingWidgets = new ArrayList<>();
		OnboardingWidget widget = new OnboardingWidget();
		onboardingWidgets.add(widget);		//Mockito.when(StringUtils.isEmpty("")).thenReturn(true);
		Mockito.when(widgetService.getOnboardingWidgets(user, true)).thenReturn(onboardingWidgets);
		actualResult = widgetsController.getOnboardingWidgets(mockedRequest, mockedResponse);
	
	}
	
	@Test
	public void putOnboardingWidgetTest() {
		FieldsValidator actualFieldsValidator = null;
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		OnboardingWidget onboardingWidget=new OnboardingWidget();
		onboardingWidget.id=12L;
		onboardingWidget.normalize();
		//Mockito.doNothing().when(onboardingWidget).normalize();	
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		List<FieldName> fields = new ArrayList<>();

		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(fields);
		expectedFieldValidator.setErrorCode(null);
		Mockito.when(widgetService.setOnboardingWidget(user, onboardingWidget)).thenReturn(expectedFieldValidator);
		actualFieldsValidator = widgetsController.putOnboardingWidget(mockedRequest, 12L, onboardingWidget, mockedResponse);
		
	}
	
	@Test
	public void putOnboardingWidgetWithUserPermissionTest() {
		FieldsValidator actualFieldsValidator = null;
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(rolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(rolesService.isAccountAdmin(user)).thenReturn(true);
		OnboardingWidget onboardingWidget=new OnboardingWidget();
		onboardingWidget.id=12L;
		onboardingWidget.normalize();
		//Mockito.doNothing().when(onboardingWidget).normalize();	
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		List<FieldName> fields = new ArrayList<>();

		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(fields);
		expectedFieldValidator.setErrorCode(null);
		Mockito.when(widgetService.setOnboardingWidget(user, onboardingWidget)).thenReturn(expectedFieldValidator);
		actualFieldsValidator = widgetsController.putOnboardingWidget(mockedRequest, 12L, onboardingWidget, mockedResponse);
		assertEquals(expectedFieldValidator.getHttpStatusCode(), actualFieldsValidator.getHttpStatusCode());
		assertEquals(expectedFieldValidator.getErrorCode(), actualFieldsValidator.getErrorCode());
		assertEquals(expectedFieldValidator.getFields(), actualFieldsValidator.getFields());
	}
	
	@Test
	public void postOnboardingWidgetTest(){
		EPUser user=mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		FieldsValidator actualFieldsValidator = null;
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.when(rolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(rolesService.isAccountAdmin(user)).thenReturn(true);
		OnboardingWidget onboardingWidget=new OnboardingWidget();
		onboardingWidget.id=12L;
		onboardingWidget.normalize();
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		List<FieldName> fields = new ArrayList<>();

		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(fields);
		expectedFieldValidator.setErrorCode(null);
		Mockito.when(widgetService.setOnboardingWidget(user, onboardingWidget)).thenReturn(expectedFieldValidator);
		actualFieldsValidator = widgetsController.postOnboardingWidget(mockedRequest, onboardingWidget, mockedResponse);
		assertEquals(expectedFieldValidator.getHttpStatusCode(), actualFieldsValidator.getHttpStatusCode());
		assertEquals(expectedFieldValidator.getErrorCode(), actualFieldsValidator.getErrorCode());
		assertEquals(expectedFieldValidator.getFields(), actualFieldsValidator.getFields());
	}
	
	@Test
	public void postOnboardingWidgetTestwiThoutUserPermission() {
		FieldsValidator actualFieldsValidator = null;
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		OnboardingWidget onboardingWidget=new OnboardingWidget();
		onboardingWidget.id=12L;
		onboardingWidget.normalize();
		//Mockito.doNothing().when(onboardingWidget).normalize();	
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		List<FieldName> fields = new ArrayList<>();

		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(fields);
		expectedFieldValidator.setErrorCode(null);
		Mockito.when(widgetService.setOnboardingWidget(user, onboardingWidget)).thenReturn(expectedFieldValidator);
		actualFieldsValidator = widgetsController.postOnboardingWidget(mockedRequest,  onboardingWidget, mockedResponse);
		
	}
	
	@Test
	public void deleteOnboardingWidgetTest(){
		FieldsValidator actualFieldsValidator = null;
		EPUser user=mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		List<FieldName> fields = new ArrayList<>();
		Mockito.when(rolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(rolesService.isAccountAdmin(user)).thenReturn(true);
		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(fields);
		expectedFieldValidator.setErrorCode(null);
		Mockito.when(widgetService.deleteOnboardingWidget(user, 12L)).thenReturn(expectedFieldValidator);
		actualFieldsValidator = widgetsController.deleteOnboardingWidget(mockedRequest,  12L, mockedResponse);
		assertEquals(expectedFieldValidator.getHttpStatusCode(), actualFieldsValidator.getHttpStatusCode());
		assertEquals(expectedFieldValidator.getErrorCode(), actualFieldsValidator.getErrorCode());
		assertEquals(expectedFieldValidator.getFields(), actualFieldsValidator.getFields());
	}
	@Test
	public void deleteOnboardingWidgetWithOutUserPermissionsTest(){
		FieldsValidator actualFieldsValidator = null;
		EPUser user=mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		List<FieldName> fields = new ArrayList<>();
		Mockito.when(rolesService.isSuperAdmin(user)).thenReturn(false);
		Mockito.when(rolesService.isAccountAdmin(user)).thenReturn(false);
		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(fields);
		expectedFieldValidator.setErrorCode(null);
		Mockito.when(widgetService.deleteOnboardingWidget(user, 12L)).thenReturn(expectedFieldValidator);
		actualFieldsValidator = widgetsController.deleteOnboardingWidget(mockedRequest,  12L, mockedResponse);
		
	}
	
	@Test
	public void putWidgetCatalogSelectionTest1() throws Exception {

		FieldsValidator actualFieldsValidator = new FieldsValidator();
		EPUser user=mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		List<FieldName> fields = new ArrayList<>();
		Mockito.when(rolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(rolesService.isAccountAdmin(user)).thenReturn(true);
		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(fields);
		expectedFieldValidator.setErrorCode(null);
		WidgetCatalogPersonalization widgetCatalogPersonalization=new WidgetCatalogPersonalization();
		widgetCatalogPersonalization.widgetId=12L;
		//Mockito.doThrow(new Exception()).doNothing().when(persUserWidgetService).setPersUserAppValue(user, 12L, true);
		//Mockito.doNothing().thenReturn();
		actualFieldsValidator = widgetsController.putWidgetCatalogSelection(mockedRequest, widgetCatalogPersonalization, mockedResponse);
		assertEquals(expectedFieldValidator.getHttpStatusCode(), actualFieldsValidator.getHttpStatusCode());
		assertEquals(expectedFieldValidator.getErrorCode(), actualFieldsValidator.getErrorCode());
		assertEquals(expectedFieldValidator.getFields(), actualFieldsValidator.getFields());
	
		
	}
	
	@Test(expected=ClassCastException.class)
	public void putWidgetCatalogSelectionTest() throws Exception {

		FieldsValidator actualFieldsValidator = new FieldsValidator();
		EPUser user=null;
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		FieldsValidator expectedFieldValidator = new FieldsValidator();
		List<FieldName> fields = new ArrayList<>();
		Mockito.when(rolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(rolesService.isAccountAdmin(user)).thenReturn(true);
		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(fields);
		expectedFieldValidator.setErrorCode(null);
		WidgetCatalogPersonalization widgetCatalogPersonalization=new WidgetCatalogPersonalization();
		Mockito.doAnswer((Answer) new Exception()).doNothing().when(persUserWidgetService).setPersUserAppValue(user, 12L, true);
		//Mockito.doNothing().thenReturn();
		actualFieldsValidator = widgetsController.putWidgetCatalogSelection(mockedRequest, widgetCatalogPersonalization, mockedResponse);
		assertEquals(expectedFieldValidator.getHttpStatusCode(), actualFieldsValidator.getHttpStatusCode());
		assertEquals(expectedFieldValidator.getErrorCode(), actualFieldsValidator.getErrorCode());
		assertEquals(expectedFieldValidator.getFields(), actualFieldsValidator.getFields());
	
		
	}

}
