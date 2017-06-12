/*-
 * ================================================================================
 * ECOMP Portal
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ================================================================================
 */
package org.openecomp.portalapp.portal.test.framework;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openecomp.portalsdk.core.conf.AppConfig;
//import org.openecomp.portalapp.conf.ExternalAppConfig;
//import org.openecomp.portalapp.conf.HibernateMappingLocations;
import org.openecomp.portalsdk.core.conf.HibernateConfiguration;
import org.openecomp.portalsdk.core.conf.HibernateMappingLocatable;
import org.openecomp.portalsdk.core.objectcache.AbstractCacheManager;
import org.openecomp.portalsdk.core.util.CacheManager;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

/**
 * 
 * In order to write a unit test, 1. inherit this class 2. place the "war"
 * folder on your test class's classpath 3. run the test with the following VM
 * argument; This is important because when starting the application from
 * Container, the System Properties file (SystemProperties.java) can have the
 * direct path but, when running from the Mock Junit container, the path should
 * be prefixed with "classpath" to enable the mock container to search for the
 * file in the classpath -Dcontainer.classpath="classpath:"
 */

@SuppressWarnings("deprecation")
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class, classes = { MockAppConfig.class })
@ActiveProfiles(value = "test")
public class ApplicationCommonContextTestSuite {

	@Autowired
	public WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		if (mockMvc == null) {
			this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

		}
	}

	public Object getBean(String name) {
		return this.wac.getBean(name);
	}

	public MockMvc getMockMvc() {
		return mockMvc;
	}

	public void setMockMvc(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

	public WebApplicationContext getWebApplicationContext() {
		return wac;
	}

}

@Configuration
@ComponentScan(basePackages = "org.openecomp", excludeFilters = {
		// the following source configurations should not be scanned; instead of
		// using Exclusion filter, we can use the @Profile annotation to exclude
		// them
		@ComponentScan.Filter(type = FilterType.REGEX, pattern = "org.openecomp.portalsdk.core.controller.LogoutController*"),
		@ComponentScan.Filter(type = FilterType.REGEX, pattern = "org.openecomp.portalsdk.core.controller.SDKLoginController*"),
		@ComponentScan.Filter(type = FilterType.REGEX, pattern = "org.openecomp.portalapp.conf.ExternalAppConfig*"),
		@ComponentScan.Filter(type = FilterType.REGEX, pattern = "org.openecomp.*.*InitUebHandler*") })
@Profile("test")
class MockAppConfig extends AppConfig {

	@Bean
	HibernateMappingLocatable locatable() {
		return new MockHibernateMappingLocations();
	}

	@Bean
	HibernateConfiguration hibConfiguration() {
		return new HibernateConfiguration();
	}

	@Bean
	public SystemProperties systemProperties() {
		return new MockSystemProperties();
	}

	@Bean
	public AbstractCacheManager cacheManager() {
		return new CacheManager() {

			public void configure() throws IOException {

			}
		};
	}

	public String[] tileDefinitions() {
		return new String[] { "classpath:/WEB-INF/fusion/defs/definitions.xml",
				"classpath:/WEB-INF/defs/definitions.xml" };
	}

	public void addInterceptors(InterceptorRegistry registry) {
		// registry.addInterceptor(new
		// SessionTimeoutInterceptor()).excludePathPatterns(getExcludeUrlPathsForSessionTimeout());
		// registry.addInterceptor(resourceInterceptor());
	}

	public static class MockSystemProperties extends SystemProperties {

		public MockSystemProperties() {
		}

	}

}

@Profile("test")
class MockHibernateMappingLocations implements HibernateMappingLocatable {

	public Resource[] getMappingLocations() {
		return new Resource[] { new ClassPathResource("WEB-INF/fusion/orm/Fusion.hbm.xml"),
				new ClassPathResource("WEB-INF/fusion/orm/EP.hbm.xml"),
				new ClassPathResource("WEB-INF/fusion/orm/Workflow.hbm.xml") };

	}

	public String[] getPackagesToScan() {
		return new String[] { "org.openecomp", "src" };
	}

}
