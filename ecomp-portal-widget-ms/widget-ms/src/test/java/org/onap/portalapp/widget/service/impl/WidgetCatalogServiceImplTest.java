/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
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
package org.onap.portalapp.widget.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.widget.domain.App;
import org.onap.portalapp.widget.domain.RoleApp;
import org.onap.portalapp.widget.domain.WidgetCatalog;

public class WidgetCatalogServiceImplTest {

	@InjectMocks
	WidgetCatalogServiceImpl widgetCatalogServiceImpl;

	@Mock
	Session session;
	@Mock
	SessionFactory sessionFactory;
	@Mock
	Transaction transaction;
	@Mock
	Criteria criteria;

	@Mock
	SQLQuery query;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testSaveMicroserivce() {
		List<WidgetCatalog> list = buildWidgetCatalog();
		when(sessionFactory.getCurrentSession()).thenReturn(session);
		// when(session.beginTransaction()).thenReturn(transaction);
		when(session.createCriteria(WidgetCatalog.class)).thenReturn(criteria);
		when(criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)).thenReturn(criteria);
		when(criteria.list()).thenReturn(list);

		List<WidgetCatalog> catalogList = widgetCatalogServiceImpl.getWidgetCatalog();
		assertEquals("junit", catalogList.get(0).getName());

	}

	@Test
	public void tesGetUserWidgetCatalog() {
		when(sessionFactory.getCurrentSession()).thenReturn(session);
		StringBuilder sb = widgetUserCatalog("test");
		when(session.createSQLQuery(sb.toString())).thenReturn(query);
		when(query.list()).thenReturn(buildWidgetCatalog());
		List<WidgetCatalog> catalogList = widgetCatalogServiceImpl.getUserWidgetCatalog("test");
		assertEquals("junit", catalogList.get(0).getName());

	}

	@Test
	public void getWidgetCatalog() {

		when(sessionFactory.getCurrentSession()).thenReturn(session);
		when(session.beginTransaction()).thenReturn(transaction);
		when(session.get(WidgetCatalog.class, 1l)).thenReturn(buildWidgetCatalog().get(0));
		WidgetCatalog catalog = widgetCatalogServiceImpl.getWidgetCatalog(1l);
		assertEquals("junit", catalog.getName());
	}

	@Test
	public void deleteWidgetCatalog() {
		Long widgetCatalogId = 1l;
		when(sessionFactory.getCurrentSession()).thenReturn(session, session);
		when(session.beginTransaction()).thenReturn(transaction, transaction);
		when(session.get(WidgetCatalog.class, widgetCatalogId)).thenReturn(buildWidgetCatalog().get(0));
		String queryString = "delete from ep_pers_user_widget_sel where widget_id = :widgetId ";
		when(session.createSQLQuery(queryString)).thenReturn(query);

		String deleteEpUserWidget = "delete from ep_pers_user_widget_placement where widget_id = :widgetId ";
		String deleteEpUserWidgetCatalog = "delete from ep_widget_catalog_files where widget_id = :widgetId ";
		String deleteUserWidgetCatalog = "delete from ep_widget_catalog_parameter where widget_id = :widgetId ";
		when(session.createSQLQuery(deleteEpUserWidget)).thenReturn(query);
		when(session.createSQLQuery(deleteEpUserWidgetCatalog)).thenReturn(query);
		when(session.createSQLQuery(deleteUserWidgetCatalog)).thenReturn(query);
		when(query.setParameter("widgetId", widgetCatalogId)).thenReturn(query, query, query, query);
		widgetCatalogServiceImpl.deleteWidgetCatalog(widgetCatalogId);

	}

	@Test
	public void deleteWidgetCatalogEmpty() {
		Long widgetCatalogId = 1l;
		when(sessionFactory.getCurrentSession()).thenReturn(session, session);
		when(session.beginTransaction()).thenReturn(transaction, transaction);
		when(session.get(WidgetCatalog.class, widgetCatalogId)).thenReturn(null);
		widgetCatalogServiceImpl.deleteWidgetCatalog(widgetCatalogId);
	}

	@Test
	public void saveWidgetCatalog() {
		WidgetCatalog widgetCatalog = buildWidgetCatalog().get(0);
		widgetCatalog.setAllowAllUser("1");
		App app = new App();
		app.setAppId(1l);
		RoleApp roleApp = new RoleApp();
		roleApp.setRoleId(1l);
		roleApp.setApp(app);
		Set<RoleApp> roles = new HashSet<>();
		roles.add(roleApp);
		widgetCatalog.setWidgetRoles(roles);
		String sql = "UPDATE ep_widget_catalog_role SET app_id = " + roleApp.getApp().getAppId() + " WHERE widget_id = "
				+ widgetCatalog.getId() + " AND ROLE_ID = " + roleApp.getRoleId();
		when(sessionFactory.openSession()).thenReturn(session, session);
		when(session.beginTransaction()).thenReturn(transaction);
		when(session.createSQLQuery(sql)).thenReturn(query);
		widgetCatalogServiceImpl.saveWidgetCatalog(widgetCatalog);

	}

	@Test
	public void updateWidgetCatalog() {
		Long widgetCatalogId = 1l;
		WidgetCatalog widgetCatalog = buildWidgetCatalog().get(0);
		widgetCatalog.setServiceId(widgetCatalogId);
		widgetCatalog.setAllowAllUser("1");
		App app = new App();
		app.setAppId(1l);
		RoleApp roleApp = new RoleApp();
		roleApp.setRoleId(1l);
		roleApp.setApp(app);
		Set<RoleApp> roles = new HashSet<>();
		roles.add(roleApp);
		widgetCatalog.setWidgetRoles(roles);
		String sql = "UPDATE ep_widget_catalog_role SET app_id = " + roleApp.getApp().getAppId() + " WHERE widget_id = "
				+ widgetCatalog.getId() + " AND ROLE_ID = " + roleApp.getRoleId();
		when(sessionFactory.getCurrentSession()).thenReturn(session);
		when(session.beginTransaction()).thenReturn(transaction, transaction);
		when(session.get(WidgetCatalog.class, widgetCatalogId)).thenReturn(buildWidgetCatalog().get(0));
		when(sessionFactory.openSession()).thenReturn(session, session);
		when(session.createSQLQuery(sql)).thenReturn(query);

		widgetCatalogServiceImpl.updateWidgetCatalog(widgetCatalogId, widgetCatalog);

	}

	@Test
	public void getServiceIdByWidget() {
		Long widgetCatalogId = 1l;
		when(sessionFactory.getCurrentSession()).thenReturn(session, session);
		when(session.beginTransaction()).thenReturn(transaction, transaction);
		when(session.get(WidgetCatalog.class, widgetCatalogId)).thenReturn(buildWidgetCatalog().get(0));
		Long serviceId = widgetCatalogServiceImpl.getServiceIdByWidget(widgetCatalogId);
		assertEquals(widgetCatalogId, serviceId);

	}
	
	@Test(expected=NullPointerException.class)
	public void getWidgetsByServiceId() {
		Long serviceId=1l;
		List<WidgetCatalog> list = buildWidgetCatalog();
		when(sessionFactory.getCurrentSession()).thenReturn(session,session);
		// when(session.beginTransaction()).thenReturn(transaction);
		when(session.createCriteria(WidgetCatalog.class)).thenReturn(criteria);
		when(criteria.add(Restrictions.eq("serviceId", serviceId))).thenReturn(criteria);
		when(criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)).thenReturn(criteria);
		when(criteria.list()).thenReturn(list);

		List<WidgetCatalog> catalogList = widgetCatalogServiceImpl.getWidgetsByServiceId(serviceId);
		assertEquals("junit", catalogList.get(0).getName());
	}

	private List<WidgetCatalog> buildWidgetCatalog() {
		List<WidgetCatalog> list = new ArrayList<WidgetCatalog>();
		WidgetCatalog widget = new WidgetCatalog();
		widget.setId(1);
		widget.setName("junit");
		widget.setServiceId(1l);
		list.add(widget);
		return list;
	}

	private StringBuilder widgetUserCatalog(String loginName) {
		StringBuilder sql = new StringBuilder()

				.append("  select userWidgets.widget_id, userWidgets.wdg_name, userWidgets.wdg_desc, b.x, b.status_cd, b.y, b.height, b.width  from      		                   ")
				.append("  (  			                                                                                                                                           ")
				.append("  select distinct w.widget_id, w.wdg_name, w.wdg_desc from                                 														  	   ")
				.append("  ep_widget_catalog w,                                                                                                                            		   ")
				.append("  ep_widget_catalog_role wr,                                                                                                             				   ")
				.append("  fn_user_role ur,																																		   ")
				.append("  fn_app app,																																		   ")
				.append("  fn_user u                                                                                                                          					   ")
				.append("  where                                                                                                                                            	   ")
				.append("  w.widget_id = wr.WIDGET_ID and 																														   ")
				.append("  ur.app_id = app.app_id and 																														   ")
				.append("  app.enabled = 'Y' and																														   ")
				.append("  wr.role_id = ur.role_id and      																													   ")
				.append("  ur.user_id = u.user_id and         																													   ")
				.append("  u.login_id = '" + loginName
						+ "' and (w.all_user_flag = 'N' or w.all_user_flag is null)                                                          	   ")
				.append(" 	                                                                                                                                                       ")
				.append(" 	union all                                                                                                                                              ")
				.append(" 	                                                                                                                                                       ")
				.append(" 	                                                                                                                                                       ")
				.append("  select distinct w.widget_id, w.wdg_name, w.wdg_desc from                                                                  							   ")
				.append(" 	ep_widget_catalog w                                                                                                                                    ")
				.append(" 	where w.all_user_flag = 'Y'                                                                                                                            ")
				.append(" 	                                                                                                                                                       ")
				.append(" 	 ) userWidgets                                                                                                                                         ")
				.append(" 	                                                                                                                                                       ")
				.append("  left join                                                                                                                                               ")
				.append(" 	                                                                                                                                                       ")
				.append("  (                                                                                                                                                       ")
				.append(" 		select case when pers.user_id is null then sel.user_id else pers.user_id end as 'user_id', case when sel.widget_id is null then                    ")
				.append(" 			pers.widget_id else sel.widget_id end as  'widget_id', pers.x, sel.status_cd, pers.y, pers.height, pers.width                                  ")
				.append(" 				from (select * from ep_pers_user_widget_placement where user_id = (select user_id from fn_user where login_id = '"
						+ loginName + "')) pers ")
				.append(" 					left outer join                                                                                                                        ")
				.append(" 				(select * from ep_pers_user_widget_sel where user_id = (select user_id from fn_user where login_id = '"
						+ loginName + "')) sel             ")
				.append(" 		on (pers.user_id = sel.user_id and pers.widget_id = sel.widget_id)                                                                                 ")
				.append(" 			                                                                                                                                               ")
				.append(" 		union                                                                                                                                              ")
				.append(" 			                                                                                                                                               ")
				.append(" 		 select case when pers.user_id is null then sel.user_id else pers.user_id end as 'user_id',  case when sel.widget_id is null                       ")
				.append(" 			then pers.widget_id else sel.widget_id end as  'widget_id', pers.x, sel.status_cd, pers.y, pers.height, pers.width                             ")
				.append(" 				from (select * from ep_pers_user_widget_placement where user_id = (select user_id from fn_user where login_id = '"
						+ loginName + "')) pers ")
				.append(" 					right outer join                                                                                                                       ")
				.append(" 				(select * from ep_pers_user_widget_sel where user_id = (select user_id from fn_user where login_id = '"
						+ loginName + "')) sel             ")
				.append(" 		on (pers.user_id = sel.user_id and pers.widget_id = sel.widget_id)                                                                                 ")
				.append(" 		                                                                                                                                                   ")
				.append(" 		 order by user_id, widget_id                                                                                                                       ")
				.append(" )b                                                                                                                                                       ")
				.append("  on                                                                                                                                                      ")
				.append("  (userWidgets.widget_id = b.widget_id) order by b.x;                                                                                                     ");

		return sql;

	}

}
