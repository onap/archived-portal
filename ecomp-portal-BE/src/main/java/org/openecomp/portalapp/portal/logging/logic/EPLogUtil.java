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
package org.openecomp.portalapp.portal.logging.logic;

import static com.att.eelf.configuration.Configuration.MDC_ALERT_SEVERITY;

import java.text.MessageFormat;

import org.openecomp.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.openecomp.portalsdk.core.logging.format.AlarmSeverityEnum;
import org.openecomp.portalsdk.core.logging.format.ErrorSeverityEnum;
import org.openecomp.portalsdk.core.web.support.UserUtils;
import org.slf4j.MDC;

import com.att.eelf.configuration.EELFLogger;
import com.att.eelf.configuration.EELFManager;

public class EPLogUtil {
	private static EELFLogger errorLogger = EELFManager.getInstance().getErrorLogger();
	public static void logEcompError(EPAppMessagesEnum epMessageEnum, String... param) {
		try {
			AlarmSeverityEnum alarmSeverityEnum = epMessageEnum.getAlarmSeverity();
			ErrorSeverityEnum errorSeverityEnum = epMessageEnum.getErrorSeverity();
			
			MDC.put(MDC_ALERT_SEVERITY, alarmSeverityEnum.name());
			MDC.put("ErrorCode", epMessageEnum.getErrorCode());
			MDC.put("ErrorDescription", epMessageEnum.getErrorDescription());
			MDC.put("ClassName", EPLogUtil.class.getName());
			
			String resolution = EPLogUtil.formatMessage(epMessageEnum.getDetails() + " " + epMessageEnum.getResolution(), (Object[]) param);
			if (errorSeverityEnum == ErrorSeverityEnum.WARN) {
				errorLogger.warn(resolution);
			} else if(errorSeverityEnum == ErrorSeverityEnum.INFO) {
				errorLogger.info(resolution);
			} else {
				errorLogger.error(resolution);
			}
		} catch(Exception e) {
			errorLogger.error("Failed to log the error code. Details: " + UserUtils.getStackTrace(e));
		} finally {
			MDC.remove("ErrorCode");
			MDC.remove("ErrorDescription");
			MDC.remove("ClassName");
			MDC.remove(MDC_ALERT_SEVERITY);
		}
	}
	
	public static String formatMessage(String message, Object...args) {
		StringBuilder sbFormattedMessage = new StringBuilder();
		if (args!=null && args.length>0 && message!=null && message != "") {
			MessageFormat mf = new MessageFormat(message);
			sbFormattedMessage.append(mf.format(args));
		} else {
			sbFormattedMessage.append(message);
		}
		
		return sbFormattedMessage.toString();
	}
}
