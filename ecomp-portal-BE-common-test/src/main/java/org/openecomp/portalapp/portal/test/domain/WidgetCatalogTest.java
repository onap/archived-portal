package org.openecomp.portalapp.portal.test.domain;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.App;
import org.openecomp.portalapp.portal.domain.RoleApp;
import org.openecomp.portalapp.portal.domain.WidgetCatalog;

public class WidgetCatalogTest {

	public WidgetCatalog mockWidgetCatalog() {

		WidgetCatalog widgetCatalog = new WidgetCatalog();

		Set<RoleApp> roleAppSet = new HashSet<RoleApp>();
		// App
		App app = new App();
		app.setAppId((long) 1);
		app.setAppName("test");

		// RoleApp
		RoleApp roleApp = new RoleApp();
		roleApp.setRoleId((long) 1);
		roleApp.setRoleName("test");
		roleApp.setApp(app);
		roleApp.setWidgets(null);

		roleAppSet.add(roleApp);

		widgetCatalog.setId((long) 1);
		widgetCatalog.setName("test");
		widgetCatalog.setDesc("test");
		widgetCatalog.setFileLocation("test");
		widgetCatalog.setAllowAllUser("test");
		widgetCatalog.setServiceId((long) 1);
		widgetCatalog.setSortOrder("test");
		widgetCatalog.setStatusCode("test");

		widgetCatalog.setWidgetRoles(roleAppSet);

		return widgetCatalog;

	}
	
	@Test
	public void widgetCatalogTest() {

		// App
		App app = new App();
		app.setAppId((long) 1);
		app.setAppName("test");

		Set<RoleApp> roleAppSet = new HashSet<RoleApp>();
		// RoleApp
		RoleApp roleApp = new RoleApp();
		roleApp.setRoleId((long) 1);
		roleApp.setRoleName("test");
		roleApp.setApp(app);
		roleApp.setWidgets(null);

		roleAppSet.add(roleApp);

		WidgetCatalog widgetCatalog = mockWidgetCatalog();

	
		assertEquals(widgetCatalog.getId(), (long)1);
		assertEquals(widgetCatalog.getName(), "test");
		assertEquals(widgetCatalog.getDesc(), "test");
		assertEquals(widgetCatalog.getFileLocation(), "test");
		assertEquals(widgetCatalog.getAllowAllUser(), "test");
		assertEquals(widgetCatalog.getServiceId(), new Long (1));
		assertEquals(widgetCatalog.getSortOrder(), "test");
		assertEquals(widgetCatalog.getStatusCode(), "test");
		assertEquals(widgetCatalog.getWidgetRoles().size(),1);
		
		assertEquals("WidgetCatalog [id=1, name=test, desc=test, fileLocation=test, allowAllUser=test]", widgetCatalog.toString());
	}
	
}
