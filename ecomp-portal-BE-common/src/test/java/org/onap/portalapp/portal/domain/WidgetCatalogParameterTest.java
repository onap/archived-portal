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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.onap.portalapp.portal.domain.WidgetCatalogParameter;

public class WidgetCatalogParameterTest {

	public WidgetCatalogParameter mockWidgetCatalogParameter(){
		WidgetCatalogParameter widgetCatalogParameter = new WidgetCatalogParameter();
		
		widgetCatalogParameter.setId((long)1);
		widgetCatalogParameter.setWidgetId((long)1);
		widgetCatalogParameter.setUserId((long)1);
		widgetCatalogParameter.setParamId((long)1);
		widgetCatalogParameter.setUser_value("test");
		
		return widgetCatalogParameter;
	}
	
	@Test
	public void widgetCatalogParameterTest(){
		WidgetCatalogParameter widgetCatalogParameter = mockWidgetCatalogParameter();
		
		assertEquals(widgetCatalogParameter.getId(), new Long(1));
		assertEquals(widgetCatalogParameter.getWidgetId(), new Long(1));
		assertEquals(widgetCatalogParameter.getUserId(), new Long(1));
		assertEquals(widgetCatalogParameter.getParamId(), new Long(1));
		assertEquals(widgetCatalogParameter.getUser_value(), "test");
		assertEquals(widgetCatalogParameter.toString(), "WidgetCatalogParameter [id=1, widgetId=1, userId=1, paramId=1, user_value=test]");
	}
}
