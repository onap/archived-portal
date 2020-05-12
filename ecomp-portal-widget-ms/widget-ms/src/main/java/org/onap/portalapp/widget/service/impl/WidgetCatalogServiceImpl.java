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

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.onap.portalapp.widget.domain.MicroserviceData;
import org.onap.portalapp.widget.domain.RoleApp;
import org.onap.portalapp.widget.domain.WidgetCatalog;
import org.onap.portalapp.widget.service.WidgetCatalogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

@Service("widgetCatalogService")
@Transactional
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
public class WidgetCatalogServiceImpl implements WidgetCatalogService {

	private static final Logger logger = LoggerFactory.getLogger(WidgetCatalogServiceImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<WidgetCatalog> getWidgetCatalog(){		
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(WidgetCatalog.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<WidgetCatalog> widgets = criteria.list();
		logger.debug("WidgetCatalogServiceImpl.getWidgetCatalog: result={}", widgets);
		return widgets;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<WidgetCatalog> getUserWidgetCatalog(String loginName){
		Session session = sessionFactory.getCurrentSession();
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
				.append("  u.login_id = '" + loginName + "' and (w.all_user_flag = 'N' or w.all_user_flag is null)                                                          	   ")			
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
				.append(" 				from (select * from ep_pers_user_widget_placement where user_id = (select user_id from fn_user where login_id = '" + loginName + "')) pers ") 
				.append(" 					left outer join                                                                                                                        ") 
				.append(" 				(select * from ep_pers_user_widget_sel where user_id = (select user_id from fn_user where login_id = '" + loginName + "')) sel             ") 
				.append(" 		on (pers.user_id = sel.user_id and pers.widget_id = sel.widget_id)                                                                                 ") 
				.append(" 			                                                                                                                                               ") 
				.append(" 		union                                                                                                                                              ") 
				.append(" 			                                                                                                                                               ") 
				.append(" 		 select case when pers.user_id is null then sel.user_id else pers.user_id end as 'user_id',  case when sel.widget_id is null                       ") 
				.append(" 			then pers.widget_id else sel.widget_id end as  'widget_id', pers.x, sel.status_cd, pers.y, pers.height, pers.width                             ") 
				.append(" 				from (select * from ep_pers_user_widget_placement where user_id = (select user_id from fn_user where login_id = '" + loginName + "')) pers ") 
				.append(" 					right outer join                                                                                                                       ") 
				.append(" 				(select * from ep_pers_user_widget_sel where user_id = (select user_id from fn_user where login_id = '" + loginName + "')) sel             ") 
				.append(" 		on (pers.user_id = sel.user_id and pers.widget_id = sel.widget_id)                                                                                 ") 
				.append(" 		                                                                                                                                                   ") 
				.append(" 		 order by user_id, widget_id                                                                                                                       ") 
				.append(" )b                                                                                                                                                       ") 
				.append("  on                                                                                                                                                      ") 
				.append("  (userWidgets.widget_id = b.widget_id) order by b.x;                                                                                                     "); 	
				
				
				
		Query query = session.createSQLQuery(sql.toString());
		List<WidgetCatalog> widgets = query.list();
		logger.debug("WidgetCatalogServiceImpl.getUserWidgetCatalog: result size={}", widgets);
		return widgets;		
	}

	@Override
	public WidgetCatalog getWidgetCatalog(Long widgetCatalogId) {
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		WidgetCatalog widget = (WidgetCatalog) session.get(WidgetCatalog.class, widgetCatalogId);
		tx.commit();
		logger.debug("WidgetCatalogServiceImpl.getWidgetCatalog: getting widget={}", widget);
		return widget;
	}
	
	@Override
	public void deleteWidgetCatalog(long widgetCatalogId) {
		logger.debug("WidgetCatalogServiceImpl.deleteWidgetCatalog: deleting the widget with widgetId={}", widgetCatalogId);
		WidgetCatalog widget = getWidgetCatalog(widgetCatalogId);
		if (widget == null){
			logger.error("No widget found in database while performing WidgetCatalogServiceImpl.deleteWidgetCatalog.");
			return;
		}
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createSQLQuery("delete from ep_pers_user_widget_sel where widget_id = :widgetId ").setParameter("widgetId", widgetCatalogId);
		query.executeUpdate();
		query = session.createSQLQuery("delete from ep_pers_user_widget_placement where widget_id = :widgetId ").setParameter("widgetId", widgetCatalogId);
		query.executeUpdate();
		query = session.createSQLQuery("delete from ep_widget_catalog_files where widget_id = :widgetId ").setParameter("widgetId", widgetCatalogId);
		query.executeUpdate();
		query = session.createSQLQuery("delete from ep_widget_catalog_parameter where widget_id = :widgetId ").setParameter("widgetId", widgetCatalogId);
		query.executeUpdate();
		session.delete(widget);
		tx.commit();
	}
	
	@Override
	public long saveWidgetCatalog(WidgetCatalog newWidgetCatalog) {
		
		try{
			if(newWidgetCatalog.getAllowAllUser().equals("1"))
				newWidgetCatalog.setAllowAllUser("Y");
			else
				newWidgetCatalog.setAllowAllUser("N");
			
			logger.debug("WidgetCatalogServiceImpl.saveWidgetCatalog: widget={}", newWidgetCatalog);
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();		
			session.save(newWidgetCatalog);
			tx.commit();
			//session.flush();
			session.close();
			updateAppId(newWidgetCatalog.getId(), newWidgetCatalog.getWidgetRoles());
		}
		catch(Exception e){
			logger.error("Exception occurred while performing WidgetCatalogServiceImpl.saveWidgetCatalog in widget microservices. Details:" + e.getMessage());
		}
		return newWidgetCatalog.getId();
	}

	@Override
	public void updateWidgetCatalog(Long widgetCatalogId, WidgetCatalog newWidgetCatalog) {
		logger.debug("WidgetCatalogServiceImpl.updateWidgetCatalog: widget={}", newWidgetCatalog);
		WidgetCatalog oldWidget = getWidgetCatalog(widgetCatalogId);
		try{
			if (newWidgetCatalog.getAllowAllUser().equals("1")) 
				newWidgetCatalog.setAllowAllUser("Y");
			else 
				newWidgetCatalog.setAllowAllUser("N");
			
			newWidgetCatalog.setId(widgetCatalogId);
			newWidgetCatalog.setServiceId(oldWidget.getServiceId());
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			session.update(newWidgetCatalog);
			tx.commit();
			//session.flush();
			session.close();
			updateAppId(newWidgetCatalog.getId(), newWidgetCatalog.getWidgetRoles());
		}catch(Exception e){
			logger.error("Exception occurred while performing WidgetCatalogServiceImpl.updateWidgetCatalog in widget microservices. Details:" + e.getMessage());
		}
		
	}	
	
	@Override
	public Long getServiceIdByWidget(Long widgetCatalogId) {
		Session session = sessionFactory.getCurrentSession();
		WidgetCatalog widget = (WidgetCatalog) session.get(WidgetCatalog.class, widgetCatalogId);
		logger.debug("WidgetCatalogServiceImpl.getServiceIdByWidget: result={}", widget);
		return widget.getServiceId();
	}
	
	@Override
	public List<WidgetCatalog> getWidgetsByServiceId(Long serviceId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(WidgetCatalog.class)
				.add(Restrictions.eq("serviceId", serviceId))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<WidgetCatalog> widgets = criteria.list();
		logger.debug("WidgetCatalogServiceImpl.getWidgetCatalog: result={}", widgets);
		return widgets;
	}

	private void updateAppId(long widgetId, Set<RoleApp> roles){
		Session session = sessionFactory.openSession();
		for(RoleApp role: roles){
			String sql = "UPDATE ep_widget_catalog_role SET app_id = :appId WHERE widget_id = :widgetId AND ROLE_ID = :roleId" ;
			Query query = session.createSQLQuery(sql);
			query.setParameter("appId", role.getApp().getAppId());
			query.setParameter("widgetId", widgetId);
			query.setParameter("roleId", role.getRoleId());
			query.executeUpdate();
		}
		session.flush();
		session.close();
	}

	@Override
	public boolean getWidgetIdByName(String newWidgetName) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(WidgetCatalog.class)
				.add(Restrictions.eq("name", newWidgetName))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<MicroserviceData> widgets = criteria.list();
		logger.debug("WidgetCatalogServiceImpl.getWidgetIdByName: result={}", widgets);
//		session.flush();
		session.close();
		
		return (widgets.size() > 0) ? true : false;
	}

	
}

