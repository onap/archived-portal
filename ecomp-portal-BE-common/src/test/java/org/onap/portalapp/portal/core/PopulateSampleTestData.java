package org.onap.portalapp.portal.core;
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
//package org.onap.portalapp.portal.core;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//
//import javax.sql.DataSource;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.onap.portalapp.framework.ApplicationCommonContextTestSuite;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.jdbc.datasource.DataSourceUtils;
//import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
//
//public class PopulateSampleTestData extends ApplicationCommonContextTestSuite {
//
//	@Autowired
//	DataSource dataSource;
//
//	@Before
//	public void populateTestData() throws SQLException {
//
//		String sql = "PopulateTestData.sql";
//		createConnection(sql);
//	}
//
//	public void createConnection(String sql) {
//		Connection connection = null;
//		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
//		populator.addScript(new ClassPathResource(sql));
//		try {
//			connection = DataSourceUtils.getConnection(dataSource);
//			populator.populate(connection);
//		} finally {
//			if (connection != null) {
//				DataSourceUtils.releaseConnection(connection, dataSource);
//			}
//		}
//	}
//
//	@Test
//	public void populateTest() {
//		assert (true);
//	}
//
//}
