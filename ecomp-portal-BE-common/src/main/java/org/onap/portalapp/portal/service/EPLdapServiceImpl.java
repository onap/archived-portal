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
package org.onap.portalapp.portal.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;

import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.logging.aop.EPMetricsLog;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalsdk.core.command.support.SearchResult;
import org.onap.portalsdk.core.domain.support.DomainVo;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.service.support.FusionService;
import org.onap.portalsdk.core.service.support.ServiceLocator;
import org.onap.portalsdk.core.util.SystemProperties;
import org.owasp.esapi.ESAPI;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("epLdapService")
@Transactional
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
public class EPLdapServiceImpl extends FusionService implements EPLdapService {
	@Autowired
	private ServiceLocator serviceLocator;

	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(EPLdapServiceImpl.class);

	@EPAuditLog
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SearchResult searchPost(DomainVo searchCriteria, String sortBy1, String sortBy2, String sortBy3, int pageNo,
			int dataSize, int userId) throws Exception {

		String remoteHost = "";

		// initialize the directory context to access POST
		DirContext dirContext = serviceLocator.getDirContext(
				SystemProperties.getProperty(SystemProperties.POST_INITIAL_CONTEXT_FACTORY),
				SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
				SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL));

		SearchResult searchResult = new SearchResult();

		try {

			remoteHost = String.format("%s/%s", SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL),
					SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL));
			MDC.put(EPCommonSystemProperties.FULL_URL, remoteHost);

			String[] postAttributes = { "nickname", "givenName", "initials", "sn", "employeeNumber", "mail",
					"telephoneNumber", "departmentNumber", "a1", "street", "roomNumber", "l", "st", "postalCode",
					"zip4", "physicalDeliveryOfficeName", "bc", "friendlyCountryName", "bd", "bdname", "bu", "buname",
					"jtname", "mgrid", "a2", "compcode", "compdesc", "costcenter", "silo", "b2" };

			SearchControls searchControls = new SearchControls();
			searchControls.setTimeLimit(5000);
			searchControls.setReturningAttributes(postAttributes);

			StringBuffer filterClause = new StringBuffer("(&(objectClass=*)");

			EPUser user = (EPUser) searchCriteria;

			if (Utilities.nvl(user.getFirstName()).length() > 0) {
				filterClause.append("(givenName=").append(user.getFirstName()).append("*)");
			}
			if (Utilities.nvl(user.getLastName()).length() > 0) {
				filterClause.append("(sn=").append(user.getLastName()).append("*)");
			}
			if (Utilities.nvl(user.getHrid()).length() > 0) {
				filterClause.append("(employeeNumber=").append(user.getHrid()).append("*)");
			}
			if (Utilities.nvl(user.getOrgManagerUserId()).length() > 0) {
				filterClause.append("(mgrid=").append(user.getOrgManagerUserId()).append("*)");
			}
			if (Utilities.nvl(user.getOrgCode()).length() > 0) {
				filterClause.append("(departmentNumber=").append(user.getOrgCode()).append("*)");
			}
			if (Utilities.nvl(user.getEmail()).length() > 0) {
				filterClause.append("(mail=").append(user.getEmail()).append("*)");
			}
			if (Utilities.nvl(user.getOrgUserId()).length() > 0) {
				filterClause.append("(a1=").append(user.getOrgUserId()).append("*)");
			}
			filterClause.append("(c3=N)"); // this has been added to filter CP09 entries on the LDAP server that are
											// duplicates of existing individuals
			filterClause.append(")");

			List list = new ArrayList();
			if (!filterClause.toString().equals("(&(objectClass=*))")) {
				NamingEnumeration e = dirContext.search(
						SystemProperties.getProperty(SystemProperties.POST_PROVIDER_URL) + "/"
								+ SystemProperties.getProperty(SystemProperties.POST_SECURITY_PRINCIPAL),
								ESAPI.encoder().encodeForDN(filterClause.toString()), searchControls);
				list = processResults(e);
			}

			Collections.sort(list);

			searchResult = new SearchResult(list);
			searchResult.setPageNo(pageNo);
			if (dataSize >= 0) {
				searchResult.setDataSize(dataSize);
			} else {
				searchResult.setDataSize(list.size());
			}
		} catch (NamingException ne) {
			logger.error(EELFLoggerDelegate.errorLogger, "searchPost failed with naming exception", ne);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "searchPost failed", e);
		} finally {
			dirContext.close();
		}

		return searchResult;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@EPMetricsLog
	private ArrayList processResults(NamingEnumeration e) throws NamingException {
		ArrayList results = new ArrayList();
		int count = 0;

		while (e.hasMore()) {
			javax.naming.directory.SearchResult searchResult = (javax.naming.directory.SearchResult) e.next();
			results.add(processAttributes(searchResult.getAttributes()));
			count++;

			if (count > Integer.parseInt(SystemProperties.getProperty(SystemProperties.POST_MAX_RESULT_SIZE))) {
				break;
			}
		}
		return results;
	}

	@SuppressWarnings("rawtypes")
	@EPMetricsLog
	private DomainVo processAttributes(Attributes resultAttributes) throws NamingException {
		EPUser user = new EPUser();

		try {
			if (resultAttributes == null) {
				logger.debug(EELFLoggerDelegate.debugLogger, "This result has no attributes");
			} else {
				for (NamingEnumeration e = resultAttributes.getAll(); e.hasMore();) { // why the nested loop?
					Attribute attribute = (Attribute) e.next();
					for (NamingEnumeration ie = attribute.getAll(); ie.hasMore();) {
						if (attribute.getID().equalsIgnoreCase("givenName")) {
							user.setFirstName((String) ie.next());
						} else if (attribute.getID().equalsIgnoreCase("initials")) {
							user.setMiddleInitial((String) ie.next());
						} else if (attribute.getID().equalsIgnoreCase("sn")) {
							user.setLastName((String) ie.next());
						} else if (attribute.getID().equalsIgnoreCase("employeeNumber")) {
							user.setHrid((String) ie.next());
						} else if (attribute.getID().equalsIgnoreCase("mail")) {
							user.setEmail((String) ie.next());
						} else if (attribute.getID().equalsIgnoreCase("telephoneNumber")) {
							user.setPhone((String) ie.next());
						} else if (attribute.getID().equalsIgnoreCase("departmentNumber")) {
							user.setOrgCode((String) ie.next());
						} else if (attribute.getID().equalsIgnoreCase("a1")) {
							user.setOrgUserId((String) ie.next());
						} else if (attribute.getID().equalsIgnoreCase("street")) {
							user.setAddress1((String) ie.next());
						} else if (attribute.getID().equalsIgnoreCase("roomNumber")) {
							user.setAddress2((String) ie.next());
						} else if (attribute.getID().equalsIgnoreCase("l")) {
							user.setCity((String) ie.next());
						} else if (attribute.getID().equalsIgnoreCase("st")) {
							user.setState((String) ie.next());
						} else if (attribute.getID().equalsIgnoreCase("postalCode")) {
							user.setZipCode((String) ie.next());
						} else if (attribute.getID().equalsIgnoreCase("zip4")) {
							user.setZipCodeSuffix((String) ie.next());
						} else if (attribute.getID().equalsIgnoreCase("physicalDeliveryOfficeName")) {
							user.setLocationClli((String) ie.next());
						} else if (attribute.getID().equalsIgnoreCase("bc")) {
							user.setBusinessCountryCode((String) ie.next());
						} else if (attribute.getID().equalsIgnoreCase("friendlyCountryName")) {
							user.setBusinessCountryName((String) ie.next());
						} else if (attribute.getID().equalsIgnoreCase("bd")) {
							user.setDepartment((String) ie.next());
						} else if (attribute.getID().equalsIgnoreCase("bdname")) {
							user.setDepartmentName((String) ie.next());
						} else if (attribute.getID().equalsIgnoreCase("jtname")) {
							user.setJobTitle((String) ie.next());
						} else if (attribute.getID().equalsIgnoreCase("mgrid")) {
							user.setOrgManagerUserId((String) ie.next());
						} else if (attribute.getID().equalsIgnoreCase("a2")) {
							user.setCommandChain((String) ie.next());
						} else if (attribute.getID().equalsIgnoreCase("compcode")) {
							user.setCompanyCode((String) ie.next());
						} else if (attribute.getID().equalsIgnoreCase("compdesc")) {
							user.setCompany((String) ie.next());
						} else if (attribute.getID().equalsIgnoreCase("bu")) {
							user.setBusinessUnit((String) ie.next());
						} else if (attribute.getID().equalsIgnoreCase("buname")) {
							user.setBusinessUnitName((String) ie.next());
						} else if (attribute.getID().equalsIgnoreCase("silo")) {
							user.setSiloStatus((String) ie.next());
						} else if (attribute.getID().equalsIgnoreCase("costcenter")) {
							user.setCostCenter((String) ie.next());
						} else if (attribute.getID().equalsIgnoreCase("b2")) {
							user.setFinancialLocCode((String) ie.next());
						} else { // we don't care about returned attribute, let's move on
							ie.next();
						}

					}
				}
			}
		} catch (NamingException e) {
			logger.error(EELFLoggerDelegate.errorLogger, "processAttributes failed with naming exception", e);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "processAttributes failed", e);
		}

		return user;
	}
}
