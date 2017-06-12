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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.domain.GetAccessResult;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;

@Service("getAccessService")
@Transactional
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
public class GetAccessServiceImpl implements GetAccessService{

	@Autowired
	private DataAccessService dataAccessService;

	/*
	 * (non-Javadoc)
	 * @see org.openecomp.portalapp.portal.service.GetAccessService#getAppAccessList()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<GetAccessResult> getAppAccessList(EPUser user) {
		final Map<String, Long> params = new HashMap<>();
		List<GetAccessResult> appAccessList = null;
		params.put("userId", user.getId());
		appAccessList = dataAccessService
				.executeNamedQuery("getAppAccessFunctionRole", params, null);
		return appAccessList;
	}

}
