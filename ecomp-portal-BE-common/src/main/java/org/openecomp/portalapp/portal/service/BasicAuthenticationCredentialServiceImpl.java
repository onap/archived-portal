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
package org.openecomp.portalapp.portal.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.openecomp.portalapp.portal.domain.BasicAuthCredentials;
import org.openecomp.portalapp.portal.domain.EPEndpoint;
import org.openecomp.portalapp.portal.domain.EPEndpointAccount;
import org.openecomp.portalapp.portal.domain.SharedContext;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.service.DataAccessService;
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
	public BasicAuthCredentials getBasicAuthCredentialByAppName(String appName) {
	
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion contextIdCrit = Restrictions.eq("applicationName", appName);
		restrictionsList.add(contextIdCrit);
		@SuppressWarnings("unchecked")
		List<BasicAuthCredentials> credList = (List<BasicAuthCredentials>) dataAccessService.getList(BasicAuthCredentials.class, null, restrictionsList, null);
		if (credList == null || credList.size() == 0) {
			logger.error(EELFLoggerDelegate.errorLogger,
					"getBasicAuthCredentialByAppName: no credential(s) for " + appName);
			return null;
		}
		logger.debug(EELFLoggerDelegate.debugLogger,
				"getBasicAuthCredentialByAppName: cred list size: " + credList.size());
		BasicAuthCredentials cred = (BasicAuthCredentials) credList.get(0);
		cred.setEndpoints(getEndpointsByAccountId(cred.getId()));
		return cred;
	}
	
	private List<EPEndpoint> getEndpointsByAccountId(long id){
		
		List<EPEndpoint> list = new ArrayList<>();
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion contextIdCrit = Restrictions.eq("account_id", id);
		restrictionsList.add(contextIdCrit);
		List<EPEndpointAccount> epList = (List<EPEndpointAccount>) dataAccessService.getList(EPEndpointAccount.class, null, restrictionsList, null);
		for(EPEndpointAccount ep: epList){
			list.add((EPEndpoint) dataAccessService.getDomainObject(EPEndpoint.class, ep.getEp_id(), null));
		}
		return list;
	}

}
