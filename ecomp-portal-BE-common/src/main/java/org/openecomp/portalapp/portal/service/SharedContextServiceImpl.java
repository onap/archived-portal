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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalapp.portal.domain.SharedContext;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;

/**
 * Implementation of the shared-context service that talks to the database.
 */
@Service("sharedContextService")
@Transactional
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
public class SharedContextServiceImpl implements SharedContextService {

	@Autowired
	private DataAccessService dataAccessService;

	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SharedContextServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openecomp.portalsdk.core.service.SharedContextService#
	 * getSharedContexts()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<SharedContext> getSharedContexts(String contextId) {
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion contextIdCrit = Restrictions.eq("context_id", contextId);
		restrictionsList.add(contextIdCrit);
		List<SharedContext> contexts = (List<SharedContext>) getDataAccessService().getList(SharedContext.class, null,
				restrictionsList, null);

		return contexts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openecomp.portalsdk.core.service.SharedContextService#
	 * getSharedContext(java. lang.String, java.lang.String)
	 */
	@Override
	public SharedContext getSharedContext(String contextId, String key) {
		SharedContext context = null;
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion contextIdCrit = Restrictions.eq("context_id", contextId);
		Criterion keyCrit = Restrictions.eq("ckey", key);
		restrictionsList.add(contextIdCrit);
		restrictionsList.add(keyCrit);
		@SuppressWarnings("unchecked")
		List<SharedContext> contexts = (List<SharedContext>) getDataAccessService().getList(SharedContext.class, null,
				restrictionsList, null);
		if (contexts != null && contexts.size() == 1)
			context = contexts.get(0);

		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openecomp.portalapp.portal.service.SharedContextService#
	 * addSharedContext(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addSharedContext(String contextId, String key, String value) {
		SharedContext context = new SharedContext(contextId, key, value);
		saveSharedContext(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openecomp.portalsdk.core.service.SharedContextService#
	 * saveSharedContext(com. att.fusion.core.domain.SharedContext)
	 */
	@Override
	public void saveSharedContext(SharedContext context) {
		getDataAccessService().saveDomainObject(context, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openecomp.portalsdk.core.service.SharedContextService#
	 * deleteSharedContext(com. att.fusion.core.domain.SharedContext)
	 */
	@Override
	public void deleteSharedContext(SharedContext context) {
		getDataAccessService().deleteDomainObject(context, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openecomp.portalapp.portal.service.SharedContextService#
	 * deleteSharedContexts(java.lang.String)
	 */
	@Override
	public int deleteSharedContexts(String contextId) {
		// Uses an inefficient method to avoid a where clause
		// that could be used to mount a SQL injection attack.
		List<SharedContext> contexts = getSharedContexts(contextId);
		if (contexts == null)
			return 0;

		logger.debug(EELFLoggerDelegate.debugLogger, "deleteSharedContexts: count is " + contexts.size());
		for (SharedContext sc : contexts)
			deleteSharedContext(sc);

		return contexts.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openecomp.portalapp.portal.service.SharedContextService#
	 * expireSharedContexts(int)
	 */
	@Override
	public void expireSharedContexts(int ageInSeconds) {
		// Specific to the MySQL database.
		// final String whereClause = " where create_time < ADDDATE(NOW(),
		// INTERVAL - " + ageInSeconds + " SECOND)";
		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date expiredDateTime = new Date(System.currentTimeMillis() - ageInSeconds * 1000);
		logger.debug(EELFLoggerDelegate.debugLogger,
				"expireSharedContexts: expire time is " + expiredDateTime.toString());
		final String whereClause = " create_time < '" + dateFormat.format(expiredDateTime) + "'";
		getDataAccessService().deleteDomainObjects(SharedContext.class, whereClause, null);
	}

	public DataAccessService getDataAccessService() {
		return dataAccessService;
	}

	public void setDataAccessService(DataAccessService dataAccessService) {
		this.dataAccessService = dataAccessService;
	}

}
