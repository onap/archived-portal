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
 * 
 */
package org.onap.portal.service.sharedContext;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.onap.portal.domain.db.fn.FnSharedContext;
import org.onap.portal.logging.aop.EPMetricsLog;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@EnableAspectJAutoProxy
@EPMetricsLog
public class FnSharedContextService {

	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(FnSharedContextService.class);

	 private final FnSharedContextDao fnSharedContextDao;

	 @Autowired
	public FnSharedContextService(final FnSharedContextDao fnSharedContextDao) {
		this.fnSharedContextDao = fnSharedContextDao;
	}

	public List<FnSharedContext> getSharedContexts(String contextId) {
		return fnSharedContextDao.getByContextId(contextId).orElse(new ArrayList<>());
	}

	public FnSharedContext getFnSharedContext(String contextId, String ckey) {
		FnSharedContext context = null;
		Optional<List<FnSharedContext>> contexts = fnSharedContextDao.getByContextIdAndCkey(contextId, ckey);
		if (contexts.isPresent() && contexts.get().size() == 1) {
			context = contexts.get().get(0);
		}
		return context;
	}

	public FnSharedContext addFnSharedContext(String contextId, String key, String value) {
		return this.save(new FnSharedContext(contextId, key, value));
	}


	public FnSharedContext save(FnSharedContext context) {
		return this.fnSharedContextDao.saveAndFlush(context);
	}

	public List<FnSharedContext> saveAll(List<FnSharedContext> fnSharedContexts){
	 	return fnSharedContextDao.saveAll(fnSharedContexts);
	}

	public void delete(FnSharedContext context) {
		this.fnSharedContextDao.delete(context);
	}

	public int deleteSharedContexts(String contextId) {
		// Uses an inefficient method to avoid a where clause
		// that could be used to mount a SQL injection attack.
		List<FnSharedContext> contexts = getSharedContexts(contextId);
		if (contexts == null)
			return 0;

		logger.debug(EELFLoggerDelegate.debugLogger, "deleteFnSharedContexts: count is " + contexts.size());
		for (FnSharedContext sc : contexts)
			this.delete(sc);

		return contexts.size();
	}

	public void expireFnSharedContexts(int ageInSeconds) {
		Date expiredDateTime = new Date(System.currentTimeMillis() - ageInSeconds * 1000);
		logger.debug(EELFLoggerDelegate.debugLogger,
				"expireFnSharedContexts: expire time is " + expiredDateTime.toString());
		this.fnSharedContextDao.deleteByCreated(convertToLocalDateTimeViaInstant(expiredDateTime));
	}

	private LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
		return dateToConvert.toInstant()
			.atZone(ZoneId.systemDefault())
			.toLocalDateTime();
	}
}
