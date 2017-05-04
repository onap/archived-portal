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

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.openecomp.portalsdk.core.domain.AuditLog;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;
import org.openecomp.portalapp.portal.utils.EPCommonSystemProperties;

@Service("epAuditService")
@Transactional
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
public class EPAuditServiceImpl implements EPAuditService {
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(EPAuditServiceImpl.class);

	@Autowired
	private DataAccessService  dataAccessService;

	@Override
	/* get the guest last login time with orgUserId as param.
	 * If record not found in table, return null.
	 *  
	 * (non-Javadoc)
	 * @see org.openecomp.portalapp.portal.service.EPUserService#getGuestLastLogin(java.lang.String)
	 */
	public Date getGuestLastLogin(String userId) {
		Map<String, String> params = new HashMap<>();
		params.put("userId", userId);
		List<Date> list = getDataAccessService().executeNamedQuery("getGuestLastLogin", params, null);	
		Date date=null;
		if(list!=null){
			if(list.size()==1) /* if list only contains one item, meaning this is the first time user logs in or record not found in db*/
				date = list.get(0); /*the guest's current log in time*/
			else if(list.size()==2)
				date = list.get(1); /*most recent login date from db*/
		}
		return date;   	
	}

	@Override
	/* Clean all the records in fn_audit_log table that are less than defined date in system.property
	 * 
	 * (non-Javadoc)
	 * @see org.openecomp.portalapp.portal.service.EPAuditService#delAuditLogFromDay()
	 */
	public void delAuditLogFromDay(){	
		if (EPCommonSystemProperties.containsProperty(EPCommonSystemProperties.AUDITLOG_DEL_DAY_FROM)) {
			String day = EPCommonSystemProperties.getProperty(EPCommonSystemProperties.AUDITLOG_DEL_DAY_FROM);				
			LocalDate removeDateFrom = LocalDate.now().minusDays(Integer.valueOf(day));
			getDataAccessService().deleteDomainObjects(AuditLog.class, "audit_date  <'"+removeDateFrom+"'",null);
		}else{
			logger.error(EELFLoggerDelegate.errorLogger, "delAuditLogFromDay Exception = system.propertiy value is empty on" + EPCommonSystemProperties.AUDITLOG_DEL_DAY_FROM);
		}	
	}
	
	public DataAccessService getDataAccessService() {
		return dataAccessService;
	}

	public void setDataAccessService(DataAccessService dataAccessService) {
		this.dataAccessService = dataAccessService;
	}


}
