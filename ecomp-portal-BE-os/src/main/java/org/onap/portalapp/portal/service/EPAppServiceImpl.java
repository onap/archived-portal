/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
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

import com.att.nsa.cambria.client.CambriaClientFactory;
import com.att.nsa.cambria.client.CambriaTopicManager;
import java.net.MalformedURLException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.logging.aop.EPMetricsLog;
import org.onap.portalsdk.core.service.DataAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("epAppService")
@Transactional
@Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
public class EPAppServiceImpl extends EPAppCommonServiceImpl implements EPAppService {
	@Autowired
	private DataAccessService dataAccessService;

	@Override
	public List<EPApp> getUserRemoteApps(String id) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM FN_APP join FN_USER_ROLE ON FN_USER_ROLE.APP_ID = FN_APP.APP_ID where ");
		query.append("FN_USER_ROLE.USER_ID = ").append(id).append(" AND FN_USER_ROLE.ROLE_ID != ")
			.append(SUPER_ADMIN_ROLE_ID);
		query.append(" AND FN_APP.ENABLED = 'Y'");
		@SuppressWarnings("unchecked")
		List<EPApp> adminApps = dataAccessService.executeSQLQuery(query.toString(), EPApp.class, null);
		return new ArrayList<>(new TreeSet<>(adminApps));
	}



	@Override
	public CambriaTopicManager getTopicManager(List<String> urlList, String key, String secret)
			throws MalformedURLException, GeneralSecurityException {
		return CambriaClientFactory.createTopicManager(null, urlList, key, secret);
	}

}