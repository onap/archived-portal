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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.portal.domain;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.onap.portalapp.portal.domain.App;
import org.onap.portalapp.portal.domain.RoleApp;
import org.onap.portalapp.portal.domain.WidgetCatalog;

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
