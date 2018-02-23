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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.EPUserApp;
import org.onap.portalapp.portal.domain.Widget;
import org.onap.portalapp.portal.framework.MockitoTestSuite;
import org.onap.portalapp.portal.transport.FieldsValidator;
import org.onap.portalapp.portal.transport.OnboardingWidget;
import org.onap.portalsdk.core.service.DataAccessService;

public class WidgetServiceImplTest {

	@Mock
	DataAccessService dataAccessService;
	@Mock
	AdminRolesService adminRolesService;
	@Mock
	SessionFactory sessionFactory;
	@Mock
	Session session;

	@Mock
	Transaction transaction;
		
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
		Mockito.when(session.beginTransaction()).thenReturn(transaction);
	}
	
	@InjectMocks
	WidgetServiceImpl  widgetServiceImpl = new WidgetServiceImpl();
	
	
	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();
	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();
	NullPointerException nullPointerException = new NullPointerException();
	MockEPUser mockUser = new MockEPUser();
	
	Long ACCOUNT_ADMIN_ROLE_ID = 99l;
	Long LONG_ECOMP_APP_ID = 1l;
	
	
	@Test
	public void getOnboardingWidgetsTest_isSuperAdmin() {
		EPUser user = mockUser.mockEPUser();
		List<OnboardingWidget> onboardingWidgets = new ArrayList<>();
		OnboardingWidget widget = new OnboardingWidget();
		onboardingWidgets.add(widget);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		String sql = "SELECT widget.WIDGET_ID, widget.WDG_NAME, widget.APP_ID, app.APP_NAME, widget.WDG_WIDTH, widget.WDG_HEIGHT, widget.WDG_URL"
				+ "from FN_WIDGET widget join FN_APP app ON widget.APP_ID = app.APP_ID"+ " validAppsFilter ";
		
		widgetServiceImpl.getOnboardingWidgets(user, true);
	}
	
	@Test
	public void getOnboardingWidgetsTest_isAccountAdmin() {
		EPUser user = mockUser.mockEPUser();
		List<OnboardingWidget> onboardingWidgets = new ArrayList<>();
		OnboardingWidget widget = new OnboardingWidget();
		onboardingWidgets.add(widget);
		Mockito.when(adminRolesService.isSuperAdmin(null)).thenReturn(false);
		Mockito.when(adminRolesService.isAccountAdmin(user)).thenReturn(true);
		String sql = "SELECT widget.WIDGET_ID, widget.WDG_NAME, widget.APP_ID, app.APP_NAME, widget.WDG_WIDTH, widget.WDG_HEIGHT, widget.WDG_URL"
				+ " from FN_WIDGET widget join FN_APP app ON widget.APP_ID = app.APP_ID" + " join FN_USER_ROLE ON FN_USER_ROLE.APP_ID = app.APP_ID where FN_USER_ROLE.USER_ID = " + 1l
				+ " AND FN_USER_ROLE.ROLE_ID = " + 99l + "validAppsFilter";
		widgetServiceImpl.getOnboardingWidgets(user, true);
	}
	
	@Test
	public void getOnboardingWidgetsTest_isUser() {
		EPUser user = mockUser.mockEPUser();
		List<OnboardingWidget> onboardingWidgets = new ArrayList<>();
		OnboardingWidget widget = new OnboardingWidget();
		onboardingWidgets.add(widget);
		Mockito.when(adminRolesService.isUser(user)).thenReturn(true);
		Mockito.when(adminRolesService.isAccountAdmin(null)).thenReturn(false);
		String sql = "SELECT DISTINCT widget.WIDGET_ID, widget.WDG_NAME, widget.APP_ID, app.APP_NAME, widget.WDG_WIDTH, widget.WDG_HEIGHT, widget.WDG_URL\"\r\n" + 
				"			+ \" from FN_WIDGET widget join FN_APP app ON widget.APP_ID = app.APP_ID"
				+ " join FN_USER_ROLE ON FN_USER_ROLE.APP_ID = app.APP_ID where FN_USER_ROLE.USER_ID = "
				+ 99l + "validAppsFilter";
		widgetServiceImpl.getOnboardingWidgets(user, false);
	}
	
		
	@Test
	public void setOnboardingWidgetTest() {
		EPUser user = mockUser.mockEPUser();
		OnboardingWidget widget = new OnboardingWidget();
		widget.name = "test";
		widget.appId = 1l;
		widget.url = "demo";
		FieldsValidator fieldValidator = new FieldsValidator();
		fieldValidator.setHttpStatusCode(400l);
		widgetServiceImpl.setOnboardingWidget(user, widget);
	}
	
	@Test
	public void setOnboardingWidgetTest_updateOrSaveWidget() {
		EPUser user = mockUser.mockEPUser();
		OnboardingWidget widget = new OnboardingWidget();
		Mockito.when(adminRolesService.isSuperAdmin(null)).thenReturn(false);
		widget.name = "test";
		widget.appId = 9l;
		widget.url = "demo";
		widget.width = 1;
		widget.height = 1;
		FieldsValidator fieldValidator = new FieldsValidator();
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion userIdCriterion = Restrictions.eq("userId",1l);
		Criterion roleIDCriterion = Restrictions.eq("role.id",  99l);
		Criterion appIDCriterion  = Restrictions.eq("app.id",  2l);
		restrictionsList.add(Restrictions.and(userIdCriterion, roleIDCriterion,appIDCriterion));
		List<EPUserApp> userRoles = new ArrayList<>();
		EPUserApp app = new EPUserApp();
		userRoles.add(app);
		Mockito.when((List<EPUserApp>) dataAccessService.getList(EPUserApp.class, null, restrictionsList, null))
		.thenReturn(userRoles);
		fieldValidator.setHttpStatusCode(403l);
		widgetServiceImpl.setOnboardingWidget(user, widget);
	}
	
	@Test
	public void setOnboardingWidgetTest_updateOrSaveWidget_isSuperAdmin() {
		FieldsValidator fieldValidator = new FieldsValidator();
		EPUser user = mockUser.mockEPUser();
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		user.setId(1l);
		OnboardingWidget onboardingWidget = new OnboardingWidget();
		onboardingWidget.name="test";
		onboardingWidget.url="demo";
		onboardingWidget.id = 2l;
		onboardingWidget.appId = 9l;
		onboardingWidget.width = 1;
		onboardingWidget.height = 1;
		Widget widget = new Widget();
		Mockito.when(dataAccessService.getDomainObject(Widget.class, 2l, null)).thenReturn(widget);
		widget.setId(1l);
		
		fieldValidator.setHttpStatusCode(404l);
		widgetServiceImpl.setOnboardingWidget(user, onboardingWidget);
	}
	
	@Test
	public void setOnboardingWidgetTest_applyOnboardingWidget() {
		FieldsValidator fieldValidator = new FieldsValidator();
		EPUser user = mockUser.mockEPUser();
		fieldValidator.setHttpStatusCode(200l);
		OnboardingWidget onboardingWidget = new OnboardingWidget();
		onboardingWidget.name="test";
		onboardingWidget.url="demo";
		onboardingWidget.id = 2l;
		onboardingWidget.appId = 9l;
		onboardingWidget.width = 1;
		onboardingWidget.height = 1;
		Widget widget = new Widget();
		widget.setAppId(onboardingWidget.appId);
		widget.setName(onboardingWidget.name);
		widget.setWidth(onboardingWidget.width);
		widget.setHeight(onboardingWidget.height);
		widget.setUrl("demo");
		widgetServiceImpl.setOnboardingWidget(user, onboardingWidget);
	}
	
	@Test
	public void setOnboardingWidgetTest_validateOnboardingWidget() {
		EPUser user = mockUser.mockEPUser();
		user.setId(1l);
		FieldsValidator fieldsValidator = new FieldsValidator();
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		OnboardingWidget onboardingWidget = new OnboardingWidget();
		onboardingWidget.name="test";
		onboardingWidget.url="demo";
		onboardingWidget.appId = 9l;
		onboardingWidget.width = 1;
		onboardingWidget.height = 1;
		onboardingWidget.id = null;
		List<Widget> listWidget = new ArrayList<>();
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion urlCriterion = Restrictions.eq("url", onboardingWidget.url);
		Criterion nameCriterion = Restrictions.eq("name", onboardingWidget.name);
		restrictionsList.add(Restrictions.or(urlCriterion, nameCriterion));
		Mockito.when((List<Widget>) dataAccessService.getList(Widget.class, null, restrictionsList, null))
		.thenReturn(listWidget);
		Widget widget = new Widget();
		widget.setName("test");
		widget.setAppId(9l);
		boolean dublicatedName = true;
		boolean dublicatedUrl= true;
		fieldsValidator.addProblematicFieldName("demo");
		fieldsValidator.addProblematicFieldName("test");
		fieldsValidator.setHttpStatusCode(409l);
		widgetServiceImpl.setOnboardingWidget(user, onboardingWidget);
	}
	
	@Test
	public void deleteOnboardingWidgetTest() {
		EPUser user = mockUser.mockEPUser();
		OnboardingWidget onboardingWidget = new OnboardingWidget();
		FieldsValidator fieldValidator = new FieldsValidator();
		Widget widget = new Widget();
		Mockito.when(dataAccessService.getDomainObject(Widget.class, 1l, null)).thenReturn(widget);
		widget.setId(1l);
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion userIdCriterion = Restrictions.eq("userId",1l);
		Criterion roleIDCriterion = Restrictions.eq("role.id",  99l);
		Criterion appIDCriterion  = Restrictions.eq("app.id",  2l);
		restrictionsList.add(Restrictions.and(userIdCriterion, roleIDCriterion,appIDCriterion));
		List<EPUserApp> userRoles = new ArrayList<>();
		EPUserApp app = new EPUserApp();
		userRoles.add(app);
		Mockito.when((List<EPUserApp>) dataAccessService.getList(EPUserApp.class, null, restrictionsList, null))
		.thenReturn(userRoles);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		fieldValidator.setHttpStatusCode(403l);
		widgetServiceImpl.deleteOnboardingWidget(user, 1l);
	}

}
