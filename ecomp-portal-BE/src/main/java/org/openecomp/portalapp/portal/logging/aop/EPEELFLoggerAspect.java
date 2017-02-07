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
package org.openecomp.portalapp.portal.logging.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.openecomp.portalapp.portal.transport.FieldsValidator;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.util.SystemProperties.SecurityEventTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;

@Aspect
@org.springframework.context.annotation.Configuration
public class EPEELFLoggerAspect {
	
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(EPEELFLoggerAspect.class);

	@Autowired
	EPEELFLoggerAdvice epAdvice;
	
	/*
	 * Point-cut expression to handle all INCOMING_REST_MESSAGES
	 */
	@Pointcut("execution(public * org.openecomp.portalapp.portal.controller.*.*(..))")
	public void incomingAuditMessages() {}
	
	/*
	 * Handles all INCOMING_REST_MESSAGES from kpiDashboard
	 */
	@Pointcut("execution(public * org.openecomp.portalapp.kpidash.controller.*.*(..))")
	public void kpiDashboardAuditMessages() {}

	/*
	 * Point-cut expression to handle all session management INCOMING_REST_MESSAGES
	 */
	@Pointcut("execution(public * org.openecomp.portalapp.controller.sessionmgt.*.*(..))")
	public void sessionMgtIncomingAuditMessages() {}
	
	/*
	 * Point-cut expression to handle UserProfileController INCOMING_REST_MESSAGES
	 */
	@Pointcut("execution(public * org.openecomp.portalapp.controller.UserProfileController.*(..))")
	public void userProfileIncomingAuditMessages() {}
	
	/*
	 * Point-cut expression to handle UserProfileController INCOMING_REST_MESSAGES
	 */
	@Pointcut("execution(public * org.openecomp.portalapp.controller.WelcomeController.*(..))")
	public void welcomeIncomingAuditMessages() {}
	
	/*
	 * Point-cut expression to handle INCOING Logout Rest Messages
	 */
	@Pointcut("execution(public * org.openecomp.portalapp.controller.ECOMPLogoutController.*(..))")
	public void logoutAuditMessages() {}

	
	/*
	 * Point-cut expression which handles all the OUTGOING_REST_MESSAGES.
	 */
	@Pointcut("execution(public * org.openecomp.portalapp.portal.service.ApplicationsRestClientServiceImpl.*(..))")
	public void outgoingAuditMessages() {}
	
	/*
	 * Point-cut expression to handle all the session management OUTGOING_REST_MESSAGES.
	 */
	@Pointcut("execution(public * org.openecomp.portalapp.service.sessionmgt.SessionCommunication.*(..))")
	public void sessionMgtOutgoingAuditMessages() {}
	
	/*
	 * Point-cut expression which handles all the LDAP_PHONEBOOK_USER_SEARCH calls.
	 */
	@Pointcut("execution(public * org.openecomp.portalapp.portal.service.EPLdapServiceImpl.*(..))")
	public void phoneBookSearchAuditMessages() {}
	
	/*
	 * Handles Audit, Metrics & Debug logging for the point-cut
	 * expression defined at class-level
	 */
	@Around("(incomingAuditMessages() || kpiDashboardAuditMessages() || sessionMgtIncomingAuditMessages() || "
			+ "userProfileIncomingAuditMessages() || welcomeIncomingAuditMessages()) && @within(epAuditLog)")
	public Object incomingAuditMessagesAroundClass(ProceedingJoinPoint joinPoint, EPAuditLog epAuditLog) throws Throwable {
		return this.logAroundMethod(joinPoint, SecurityEventTypeEnum.INCOMING_REST_MESSAGE);
	}
	
	/*
	 * Handles Audit, Metrics & Debug logging for the point-cut
	 * expression defined at class-level
	 */
	@Around("(outgoingAuditMessages() || sessionMgtOutgoingAuditMessages()) && @within(epAuditLog)")
	public Object outgoingAuditMessagesAroundClass(ProceedingJoinPoint joinPoint, EPAuditLog epAuditLog) throws Throwable {
		return this.logAroundMethod(joinPoint, SecurityEventTypeEnum.OUTGOING_REST_MESSAGE);
	}
	
	
	/*
	 * Handles Audit, Metrics & Debug logging for the point-cut
	 * expression defined at method-level
	 */
	@Around("(outgoingAuditMessages() || sessionMgtOutgoingAuditMessages()) && @annotation(epAuditLog)")
	public Object outgoingAuditMessagesAroundMethod(ProceedingJoinPoint joinPoint, EPAuditLog epAuditLog) throws Throwable {
		return this.logAroundMethod(joinPoint, SecurityEventTypeEnum.OUTGOING_REST_MESSAGE);
	}
	
	/*
	 * Handles Audit, Metrics & Debug logging for the point-cut
	 * expression defined at method-level
	 */
	@Around("(incomingAuditMessages() || kpiDashboardAuditMessages() || sessionMgtIncomingAuditMessages() ||"
			+ "userProfileIncomingAuditMessages() || welcomeIncomingAuditMessages()) && @annotation(epAuditLog)")
	public Object incomingAuditMessagesAroundMethod(ProceedingJoinPoint joinPoint, EPAuditLog epAuditLog) throws Throwable {
		return this.logAroundMethod(joinPoint, SecurityEventTypeEnum.INCOMING_REST_MESSAGE);
	}
	
	@Around("@annotation(epAuditLog)")
	public Object loginAuditMessagesAroundMethod(ProceedingJoinPoint joinPoint, EPAuditLog epAuditLog) throws Throwable {
		return this.logAroundMethod(joinPoint, SecurityEventTypeEnum.FE_LOGIN_ATTEMPT);
	}
	
	@Around("logoutAuditMessages() && @annotation(epAuditLog)")
	public Object logoutAuditMessagesAroundMethod(ProceedingJoinPoint joinPoint, EPAuditLog epAuditLog) throws Throwable {
		return this.logAroundMethod(joinPoint, SecurityEventTypeEnum.FE_LOGOUT);
	}
	
	@Around("phoneBookSearchAuditMessages() && @annotation(epAuditLog)")
	public Object phonebookSearchAuditMessagesAroundMethod(ProceedingJoinPoint joinPoint, EPAuditLog epAuditLog) throws Throwable {
		return this.logAroundMethod(joinPoint, SecurityEventTypeEnum.LDAP_PHONEBOOK_USER_SEARCH);
	}
	
	private Object logAroundMethod(ProceedingJoinPoint joinPoint, SecurityEventTypeEnum securityEventType) throws Throwable {
		//Before
		Object[] passOnArgs = new Object[] {joinPoint.getSignature().getDeclaringType().getName(), joinPoint.getSignature().getName()};
		Object[] returnArgs = epAdvice.before(securityEventType, joinPoint.getArgs(), passOnArgs);
		
		//Call the actual method
		Object result = null;
		String statusCode = "COMPLETE";
		String responseCode = "200";
		try {
			result = joinPoint.proceed();
		} catch(Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
			statusCode = "ERROR";
			responseCode = "500"; //Internal server error
		}
		
		//Check the result
		if (securityEventType!=null) {
			if (result==null) {
				statusCode = "ERROR";
				//Check if there is an internal response code
				//and use it if the caller function has configured it.
				responseCode = epAdvice.getInternalResponseCode();
				if (responseCode==null||responseCode=="") {
					responseCode = "500"; //Internal server error
				}
			} else if (result instanceof FieldsValidator) {
				FieldsValidator fieldsValidator = (FieldsValidator) result;
				if (fieldsValidator!=null && fieldsValidator.httpStatusCode!=null) {
					responseCode = fieldsValidator.httpStatusCode.toString();
				}
			}
		}
		
		//After
		epAdvice.after(securityEventType, statusCode, responseCode, joinPoint.getArgs(), returnArgs, passOnArgs);
		
		return result;
	}
	
	//Metrics Logging
	@Pointcut("execution(* *(..))")
    public void performMetricsLogging() {}
	
	@Around("performMetricsLogging() && @within(epMetricsLog)")
	public Object metricsLoggingAroundClass(ProceedingJoinPoint joinPoint, EPMetricsLog epMetricsLog) throws Throwable {
		return this.logAroundMethod(joinPoint, null);
	}
	
	@Around("performMetricsLogging() && @annotation(epMetricsLog)")
	public Object metricsLoggingAroundMethod(ProceedingJoinPoint joinPoint, EPMetricsLog epMetricsLog) throws Throwable {
		return this.logAroundMethod(joinPoint, null);
	}
}
