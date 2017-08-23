package org.openecomp.portalapp.widget.test.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.openecomp.portalapp.widget.domain.RoleApp;
import org.openecomp.portalapp.widget.domain.WidgetCatalog;
import org.openecomp.portalapp.widget.service.impl.WidgetCatalogServiceImpl;


@RunWith(MockitoJUnitRunner.class)
public class WidgetCatalogServiceTest {
	
	@Mock
	private SessionFactory mockedSessionFactory;

	@InjectMocks
	private WidgetCatalogServiceImpl widgetCatalogService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void getWidgetCatalog_NoError() throws Exception{
		Session mockedSession = Mockito.mock(Session.class);
		Criteria mockedCriteria = Mockito.mock(Criteria.class);
		
		List<WidgetCatalog> list = new ArrayList<WidgetCatalog>();
		WidgetCatalog widget = new WidgetCatalog();
		widget.setId(1);
		widget.setName("junit");
		list.add(widget);
		
		Mockito.when(mockedSessionFactory.getCurrentSession()).thenReturn(mockedSession);
		Mockito.when(mockedSession.createCriteria(WidgetCatalog.class)).thenReturn(mockedCriteria);
		Mockito.when(mockedCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)).thenReturn(mockedCriteria);
		Mockito.when(mockedCriteria.list()).thenReturn(list);
		
		List<WidgetCatalog> result = widgetCatalogService.getWidgetCatalog();
		assertNotNull(result);
		assertEquals(result, list);
	}
	
	
	@Test
	public void saveWidgetCatalog_NoError() throws Exception{
		Set<RoleApp> set = new HashSet<RoleApp>();
		WidgetCatalog widget = new WidgetCatalog();
		widget.setId(1);
		widget.setName("junit");
		widget.setAllowAllUser("1");
		widget.setWidgetRoles(set);
		
		Session mockedSession = Mockito.mock(Session.class);
		Transaction mockedTransaction = Mockito.mock(Transaction.class);
		Mockito.when(mockedSessionFactory.openSession()).thenReturn(mockedSession);
		Mockito.when(mockedSession.beginTransaction()).thenReturn(mockedTransaction);
		long widgetId = widgetCatalogService.saveWidgetCatalog(widget);
		assertNotNull(widgetId);
		assertEquals(widgetId, 1);
	}
	
	@Rule public ExpectedException thrown = ExpectedException.none();
	
	
	@Test
	public void deleteWidgetCatalog_NoError() throws Exception{
		long widgetId =1 ;
		WidgetCatalog widget = new WidgetCatalog();
		widget.setId(1);
		widget.setName("junit");

		Session mockedSession = Mockito.mock(Session.class, RETURNS_DEEP_STUBS);
		Transaction mockedTransaction = Mockito.mock(Transaction.class);
		
		Mockito.when(mockedSessionFactory.getCurrentSession()).thenReturn(mockedSession);
		Mockito.when(mockedSession.beginTransaction()).thenReturn(mockedTransaction);
		Mockito.when(mockedSession.get(WidgetCatalog.class, widgetId)).thenReturn(widget);
		
		widgetCatalogService.deleteWidgetCatalog(widgetId);
	}
	
	@Test
	public void updateWidgetCatalog_NoError() throws Exception{
		long widgetId =1 ;
		WidgetCatalog widget = new WidgetCatalog();
		widget.setId(1);
		widget.setName("junit");

		Session mockedSession = Mockito.mock(Session.class, RETURNS_DEEP_STUBS);
		Transaction mockedTransaction = Mockito.mock(Transaction.class);
		
		Mockito.when(mockedSessionFactory.getCurrentSession()).thenReturn(mockedSession);
		Mockito.when(mockedSession.beginTransaction()).thenReturn(mockedTransaction);
		Mockito.when(mockedSession.get(WidgetCatalog.class, widgetId)).thenReturn(widget);
		
		widgetCatalogService.deleteWidgetCatalog(widgetId);
	}
	
	
}
