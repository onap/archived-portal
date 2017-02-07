/*-
 * ================================================================================
 * eCOMP Portal
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
package org.openecomp.portalapp.service.sessionmgt;

import java.util.List;

import org.openecomp.portalapp.portal.domain.EPApp;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.service.WebServiceCallServiceImpl;
import org.openecomp.portalsdk.core.util.CipherUtil;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("remoteWebServiceCallService")
@Transactional
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
public class RemoteWebServiceCallServiceImpl extends WebServiceCallServiceImpl implements RemoteWebServiceCallService {

	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(RemoteWebServiceCallServiceImpl.class);
	
	/*
	 * (non-Javadoc)
	 * @see org.openecomp.portalapp.service.sessionmgt.RemoteWebServiceCallService#verifyRESTCredential(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
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
		String decryptedPwd = CipherUtil.decrypt(encryptedPwdDB,
				secretKey == null ? SystemProperties.getProperty(SystemProperties.Decryption_Key) : secretKey);
		if (decryptedPwd.equals(requestPassword) && appUserName.equals(requestAppName))
			return true;
		else
			return false;
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

	public static void main(String args[]) throws Exception {
		String decryptedPwd = CipherUtil.decrypt("okYTaDrhzibcbGVq5mjkVQ==", "AGLDdG4D04BKm2IxIWEr8o==");
		System.out.print(decryptedPwd);
	}
	
}
