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
package org.onap.portalapp.portal.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.EpAppType;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalsdk.core.command.support.SearchResult;
import org.onap.portalsdk.core.service.support.ServiceLocator;
import org.onap.portalsdk.core.util.SystemProperties;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encoder;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SystemProperties.class, ESAPI.class})
public class EPLdapServiceImplTest {

	@Mock
	SessionFactory sessionFactory;

	@Mock
	Session session;

	@Mock
	Transaction transaction;
	
	@Mock
	ServiceLocator serviceLocator;
	
	@Mock
	DirContext dirContext;
	
	NullPointerException nullPointerException = new NullPointerException();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
		Mockito.when(session.beginTransaction()).thenReturn(transaction);
	}
	
	EPUser epUser = mockEPUser();

	@InjectMocks
	EPLdapServiceImpl epLdapServiceImpl = new EPLdapServiceImpl();

	public EPApp mockApp() {
		EPApp app = new EPApp();
		app.setName("Test");
		app.setImageUrl("test");
		app.setAppDescription("test");
		app.setAppNotes("test");
		app.setLandingPage("test");
		app.setId((long) 1);
		app.setAppRestEndpoint("test");
		app.setAlternateLandingPage("test");
		app.setName("test");
		app.setMlAppName("test");
		app.setMlAppAdminId("test");
		app.setAppBasicAuthUsername("test");
		app.setAppBasicAuthPassword("test");
		app.setOpen(false);
		app.setEnabled(false);
		app.setRolesInAAF(true);
		app.setUebKey("test");
		app.setUebSecret("test");
		app.setUebTopicName("test");
		app.setAppType(EpAppType.GUI);
		return app;
	}
	
	public EPUser mockEPUser() {

		EPUser ePUser = new EPUser();
		ePUser.setOrgId(null);
		ePUser.setManagerId(null);
		ePUser.setFirstName("test");
		ePUser.setLastName("test");
		ePUser.setMiddleInitial(null);
		ePUser.setPhone(null);
		ePUser.setFax(null);
		ePUser.setCellular(null);
		ePUser.setEmail(null);
		ePUser.setAddressId(null);
		ePUser.setAlertMethodCd(null);
		ePUser.setHrid(null);
		ePUser.setOrgUserId("guestT");
		ePUser.setOrgCode(null);
		ePUser.setAddress1(null);
		ePUser.setAddress2(null);
		ePUser.setCity(null);
		ePUser.setState(null);
		ePUser.setZipCode(null);
		ePUser.setCountry(null);
		ePUser.setOrgManagerUserId(null);
		ePUser.setLocationClli(null);
		ePUser.setBusinessCountryCode(null);
		ePUser.setBusinessCountryName(null);
		ePUser.setBusinessUnit(null);
		ePUser.setBusinessUnitName(null);
		ePUser.setDepartment(null);
		ePUser.setDepartmentName(null);
		ePUser.setCompanyCode(null);
		ePUser.setCompany(null);
		ePUser.setZipCodeSuffix(null);
		ePUser.setJobTitle(null);
		ePUser.setCommandChain(null);
		ePUser.setSiloStatus(null);
		ePUser.setCostCenter(null);
		ePUser.setFinancialLocCode(null);
		ePUser.setHrid("test");
		ePUser.setOrgUserId("test");
		ePUser.setOrgCode("test");
		
		ePUser.setEmail("test.com");
		ePUser.setLoginId(null);
		ePUser.setLoginPwd(null);
		Date date = new Date();
		ePUser.setLastLoginDate(date);
		ePUser.setActive(true);
		ePUser.setInternal(false);
		ePUser.setSelectedProfileId(null);
		ePUser.setTimeZoneId(null);
		ePUser.setOnline(true);
		ePUser.setChatId(null);
		ePUser.setUserApps(null);
		ePUser.setPseudoRoles(null);

		ePUser.setId((long) -1);
		return ePUser;
	}
	
	@Test
	public void searchPostTest() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		NamingEnumeration e = PowerMockito.mock(NamingEnumeration.class);
		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(expected, actual);
	}
	
	@Test
	public void searchPostNamingExceptionTest() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		NamingEnumeration e = PowerMockito.mock(NamingEnumeration.class);
		NamingException ne = new NamingException("test");
		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenThrow(ne);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(expected, actual);
	}
	
	@Test
	public void searchPostExceptionTest() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(expected, actual);
	}
	
	@Test
	public void searchPostWhileTest() throws Exception{
		SearchResult expected = new SearchResult();
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(expected, actual);
	}
	
	@Test
	public void searchPostwhile2Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		Mockito.when(attributes.getAll()).thenReturn(ef);
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);
		Mockito.when(attribute.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("givenName");

		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostwhile3Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		Mockito.when(attributes.getAll()).thenReturn(ef);
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);
		Mockito.when(attribute.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("initials");

		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostwhile4Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		Mockito.when(attributes.getAll()).thenReturn(ef);
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);
		Mockito.when(attribute.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("sn");

		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostwhile5Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		Mockito.when(attributes.getAll()).thenReturn(ef);
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);
		Mockito.when(attribute.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("employeeNumber");

		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostwhile6Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		Mockito.when(attributes.getAll()).thenReturn(ef);
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);
		Mockito.when(attribute.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("mail");

		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostwhile7Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		Mockito.when(attributes.getAll()).thenReturn(ef);
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);
		Mockito.when(attribute.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("telephoneNumber");
		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostwhile8Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		Mockito.when(attributes.getAll()).thenReturn(ef);
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);
		Mockito.when(attribute.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("departmentNumber");
		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostwhile9Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		Mockito.when(attributes.getAll()).thenReturn(ef);
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);
		Mockito.when(attribute.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("a1");
		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostwhile10Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		Mockito.when(attributes.getAll()).thenReturn(ef);
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);
		Mockito.when(attribute.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("street");
		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostwhile11Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		Mockito.when(attributes.getAll()).thenReturn(ef);
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);
		Mockito.when(attribute.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("roomNumber");
		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostwhile12Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		Mockito.when(attributes.getAll()).thenReturn(ef);
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);
		Mockito.when(attribute.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("l");
		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostwhile13Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		Mockito.when(attributes.getAll()).thenReturn(ef);
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);
		Mockito.when(attribute.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("st");
		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostwhile14Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		Mockito.when(attributes.getAll()).thenReturn(ef);
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);
		Mockito.when(attribute.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("postalCode");
		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostwhile15Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		Mockito.when(attributes.getAll()).thenReturn(ef);
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);
		Mockito.when(attribute.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("zip4");
		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostwhile16Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		Mockito.when(attributes.getAll()).thenReturn(ef);
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);
		Mockito.when(attribute.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("physicalDeliveryOfficeName");
		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostwhile17Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		Mockito.when(attributes.getAll()).thenReturn(ef);
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);
		Mockito.when(attribute.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("bc");
		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostwhile18Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		Mockito.when(attributes.getAll()).thenReturn(ef);
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);
		Mockito.when(attribute.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("friendlyCountryName");
		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostwhile19Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		Mockito.when(attributes.getAll()).thenReturn(ef);
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);
		Mockito.when(attribute.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("bd");
		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostwhile20Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		Mockito.when(attributes.getAll()).thenReturn(ef);
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);
		Mockito.when(attribute.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("bdname");
		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostwhile21Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		Mockito.when(attributes.getAll()).thenReturn(ef);
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);
		Mockito.when(attribute.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("jtname");
		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostwhile22Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		Mockito.when(attributes.getAll()).thenReturn(ef);
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);
		Mockito.when(attribute.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("mgrid");
		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostwhile23Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		Mockito.when(attributes.getAll()).thenReturn(ef);
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);
		Mockito.when(attribute.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("a2");
		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostwhile24Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		Mockito.when(attributes.getAll()).thenReturn(ef);
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);
		Mockito.when(attribute.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("compcode");
		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostwhile25Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		Mockito.when(attributes.getAll()).thenReturn(ef);
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);
		Mockito.when(attribute.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("compdesc");
		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostwhile26Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		Mockito.when(attributes.getAll()).thenReturn(ef);
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);
		Mockito.when(attribute.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("bu");
		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostwhile27Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		Mockito.when(attributes.getAll()).thenReturn(ef);
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);
		Mockito.when(attribute.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("buname");
		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostwhile28Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		Mockito.when(attributes.getAll()).thenReturn(ef);
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);
		Mockito.when(attribute.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("silo");
		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostwhile29Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		Mockito.when(attributes.getAll()).thenReturn(ef);
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);
		Mockito.when(attribute.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("costcenter");
		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostwhile30Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		Mockito.when(attributes.getAll()).thenReturn(ef);
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);
		Mockito.when(attribute.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("b2");
		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostwhile31Test() throws Exception{
		SearchResult expected = new SearchResult();
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		NamingException ne = new NamingException();
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);		
		Mockito.when(attribute.getAll()).thenThrow(ne);
		Mockito.when(attributes.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("b2");
		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual, expected);
	}
	
	@Test
	public void searchPostWhileCountTest() throws Exception{
		SearchResult expected = new SearchResult();
		expected.setDataSize(123);
		StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");
		PowerMockito.mockStatic(SystemProperties.class);
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_PROVIDER_URL)).thenReturn("http://todo_enter_ush_ticket_url");
		Mockito.when(SystemProperties.getProperty(EPCommonSystemProperties.POST_SECURITY_PRINCIPAL)).thenReturn("POST_SECURITY_PRINCIPAL");
		Mockito.when(SystemProperties.getProperty(SystemProperties.POST_MAX_RESULT_SIZE)).thenReturn("0");
		PowerMockito.mockStatic(ESAPI.class);
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.when(ESAPI.encoder()).thenReturn(encoder);
		Mockito.when(serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL))).thenReturn(dirContext);
		PowerMockito.when(encoder.encodeForDN(Matchers.anyString())).thenReturn("test");
		Set<javax.naming.directory.SearchResult> set = new HashSet<>();
		javax.naming.directory.SearchResult sr = Mockito.mock(javax.naming.directory.SearchResult.class);
		NamingEnumeration e = Mockito.mock(NamingEnumeration.class);
		Mockito.when(e.hasMore()).thenReturn(true);
		Mockito.when(e.next()).thenReturn(sr);
		Attributes attributes = Mockito.mock(Attributes.class);
		Mockito.when(sr.getAttributes()).thenReturn(attributes);
		NamingEnumeration ef = Mockito.mock(NamingEnumeration.class);
		NamingException ne = new NamingException();
		Mockito.when(ef.hasMore()).thenReturn(true);
		Attribute attribute = Mockito.mock(Attribute.class);
		Mockito.when(ef.next()).thenReturn(attribute);		
		Mockito.when(attribute.getAll()).thenThrow(ne);
		Mockito.when(attributes.getAll()).thenReturn(ef);
        Mockito.when(attribute.getID()).thenReturn("b2");
		Mockito.when(dirContext.search(Matchers.anyString(),Matchers.anyString(),Matchers.anyObject())).thenReturn(e);
		SearchResult actual = epLdapServiceImpl.searchPost(epUser, "test", "test", "test", 123, 123, 123);
		assertEquals(actual.getDataSize(), expected.getDataSize());
	}
}
