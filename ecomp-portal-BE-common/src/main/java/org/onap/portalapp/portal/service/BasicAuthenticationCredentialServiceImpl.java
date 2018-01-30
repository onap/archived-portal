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
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.onap.portalapp.portal.domain.BasicAuthCredentials;
import org.onap.portalapp.portal.domain.EPEndpoint;
import org.onap.portalapp.portal.domain.EPEndpointAccount;
import org.onap.portalapp.portal.logging.aop.EPMetricsLog;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.service.DataAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

@Service("basicAuthenticationCredentialService")
@EnableAspectJAutoProxy
@EPMetricsLog
public class BasicAuthenticationCredentialServiceImpl implements BasicAuthenticationCredentialService {

	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(BasicAuthenticationCredentialServiceImpl.class);

	@Autowired
	private DataAccessService dataAccessService;

	@Override
	public BasicAuthCredentials getBasicAuthCredentialByUsernameAndPassword(String username, String password) {

		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion contextUserNameCrit = Restrictions.eq("username", username);
		restrictionsList.add(contextUserNameCrit);
		Criterion contextPasswordCrit = Restrictions.eq("password", password);
		restrictionsList.add(contextPasswordCrit);

		@SuppressWarnings("unchecked")
		List<BasicAuthCredentials> credList = (List<BasicAuthCredentials>) dataAccessService
				.getList(BasicAuthCredentials.class, null, restrictionsList, null);
		if (credList == null || credList.size() == 0) {
			logger.error(EELFLoggerDelegate.errorLogger,
					"getBasicAuthCredentialByAppName: no credential(s) for " + username);
			return null;
		}
		logger.debug(EELFLoggerDelegate.debugLogger,
				"getBasicAuthCredentialByAppName: cred list size: " + credList.size());
		BasicAuthCredentials cred = (BasicAuthCredentials) credList.get(0);
		cred.setEndpoints(getEndpointsByAccountId(cred.getId()));
		return cred;
	}

	private List<EPEndpoint> getEndpointsByAccountId(long id) {
		List<EPEndpoint> list = new ArrayList<>();
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion contextIdCrit = Restrictions.eq("account_id", id);
		restrictionsList.add(contextIdCrit);
		@SuppressWarnings("unchecked")
		List<EPEndpointAccount> epList = (List<EPEndpointAccount>) dataAccessService.getList(EPEndpointAccount.class,
				null, restrictionsList, null);
		for (EPEndpointAccount ep : epList) {
			list.add((EPEndpoint) dataAccessService.getDomainObject(EPEndpoint.class, ep.getEp_id(), null));
		}
		return list;
	}

}
