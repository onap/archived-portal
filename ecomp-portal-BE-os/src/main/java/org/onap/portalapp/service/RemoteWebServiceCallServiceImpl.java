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
package org.onap.portalapp.service;

import java.util.List;

import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.service.RemoteWebServiceCallService;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.onap.portalsdk.core.service.WebServiceCallServiceImpl;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("remoteWebServiceCallService")
@Transactional
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
public class RemoteWebServiceCallServiceImpl extends WebServiceCallServiceImpl implements RemoteWebServiceCallService {

	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(RemoteWebServiceCallServiceImpl.class);
	
	/*
	 * (non-Javadoc)
	 * @see org.onap.portalapp.service.sessionmgt.RemoteWebServiceCallService#verifyRESTCredential(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean verifyRESTCredential(String secretKey, String requestUebKey, String requestAppName,
			String requestPassword) throws Exception {
		EPApp appRecord = findEpApp(requestUebKey);
		if (appRecord == null) {
			logger.warn(EELFLoggerDelegate.errorLogger, "Failed to find application with UEB key " + requestUebKey);
			return false;
		}
		
		String encryptedPwdDB = appRecord.getAppPassword();
		String appUserName = appRecord.getUsername();
		String decryptedPwd = CipherUtil.decryptPKC(encryptedPwdDB,
				secretKey == null ? SystemProperties.getProperty(SystemProperties.Decryption_Key) : secretKey);
		if (decryptedPwd.equals(requestPassword) && appUserName.equals(requestAppName))
			return true;
		else
			return false;
	}
	
	/**
	 * currently this method only validates the application key to fetch the application
	 */
	public boolean verifyAppKeyCredential(String requestUebKey) throws Exception {
		String failMessage = "Failed to find application with UEB key " + requestUebKey;
		if(requestUebKey == null || requestUebKey.equals("")) {
			logger.warn(EELFLoggerDelegate.errorLogger, failMessage);
			return false;
		}
		
		EPApp appRecord = findEpApp(requestUebKey);
		if (appRecord == null) {
			logger.warn(EELFLoggerDelegate.errorLogger, failMessage);
			return false;
		}
		
		return true;
	}

	/**
	 * Searches the FN_APP table for the specified UEB key.
	 * 
	 * @return EPApp object if the key is found; else null.
	 */
	public EPApp findEpApp(String uebKey) {
		List<?> list = null;
		StringBuffer criteria = new StringBuffer();
		criteria.append(" where ueb_key = '" + uebKey + "'");
		list = getDataAccessService().getList(EPApp.class, criteria.toString(), null, null);
		return (list == null || list.size() == 0) ? null : (EPApp) list.get(0);
	}

}
