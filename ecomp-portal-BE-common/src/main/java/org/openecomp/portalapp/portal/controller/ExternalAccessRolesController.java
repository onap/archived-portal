package org.openecomp.portalapp.portal.controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openecomp.portalapp.portal.domain.CentralRoleFunction;
import org.openecomp.portalapp.portal.domain.EPApp;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.domain.EcompAuditLog;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.openecomp.portalapp.portal.logging.aop.EPAuditLog;
import org.openecomp.portalapp.portal.logging.aop.EPEELFLoggerAdvice;
import org.openecomp.portalapp.portal.logging.logic.EPLogUtil;
import org.openecomp.portalapp.portal.service.ExternalAccessRolesService;
import org.openecomp.portalapp.portal.transport.CentralRole;
import org.openecomp.portalapp.portal.utils.EPCommonSystemProperties;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.portal.utils.PortalConstants;
import org.openecomp.portalsdk.core.domain.AuditLog;
import org.openecomp.portalsdk.core.domain.Role;
import org.openecomp.portalsdk.core.domain.RoleFunction;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.service.AuditService;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/auxapi")
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class ExternalAccessRolesController implements BasicAuthenticationController {

	private static final String LOGIN_ID = "LoginId";

	@Autowired
	private AuditService auditService;
	
	private static final String UEBKEY = "uebkey";

	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(ExternalAccessRolesController.class);

	@Autowired
	private ExternalAccessRolesService externalAccessRolesService;

	@ApiOperation(value = "Gets user role for an application.", response = String.class, responseContainer="List")
	@RequestMapping(value = {
			"/user/{loginId}" }, method = RequestMethod.GET, produces = "application/json")
	public String getUser(HttpServletRequest request, HttpServletResponse response, @PathVariable("loginId") String loginId) throws Exception {
		
		String answer = null;
		try {
			answer = externalAccessRolesService.getUserWithRoles(loginId, request.getHeader(UEBKEY));
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getUser failed", e);
		}
	return answer;
	}
	
	@ApiOperation(value = "Gets roles for an application.", response = CentralRole.class, responseContainer="Json")
	@RequestMapping(value = {
			"/roles" }, method = RequestMethod.GET, produces = "application/json")
	public List<CentralRole> getRolesForApp(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug(EELFLoggerDelegate.debugLogger, "Request received for getRolesForApp");
		List<EPApp> applicationList=new ArrayList<>();
		applicationList = externalAccessRolesService.getApp(request.getHeader(UEBKEY));
		EPApp app = applicationList.get(0);
        externalAccessRolesService.syncApplicationRolesWithEcompDB(app);
		List<CentralRole> answer = null;
		try {
			answer = externalAccessRolesService.getRolesForApp(request.getHeader(UEBKEY));
		} catch (HttpClientErrorException e) {
			if("Application not found".equalsIgnoreCase(e.getMessage())){
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			logger.error(EELFLoggerDelegate.errorLogger, "getRolesForApp failed", e);
		}
		logger.debug(EELFLoggerDelegate.debugLogger, "Request completed for getRolesForApp");
		return answer;
	}

	@ApiOperation(value = "Gets all role functions for an application.", response = CentralRoleFunction.class, responseContainer="Json")
	@RequestMapping(value = {
			"/functions" }, method = RequestMethod.GET, produces = "application/json")
	public List<CentralRoleFunction> getRoleFunctionsList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<CentralRoleFunction> answer = null;
		logger.debug(EELFLoggerDelegate.debugLogger, "Request received for getRoleFunctionsList");
		try {
			answer = externalAccessRolesService.getRoleFuncList(request.getHeader(UEBKEY));
		} catch (HttpClientErrorException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			logger.error(EELFLoggerDelegate.errorLogger, "getRoleFunctionsList failed", e);
		}
		logger.debug(EELFLoggerDelegate.debugLogger, "Request completed for getRoleFunctionsList");
		return answer;
	}	
	
	@ApiOperation(value = "Gets role information for an application.", response = CentralRole.class, responseContainer="Json")
	@RequestMapping(value = {
			"/role/{role_id}" }, method = RequestMethod.GET, produces = "application/json")
	public CentralRole getRoleInfo(HttpServletRequest request, HttpServletResponse response, @PathVariable("role_id") Long roleId) throws Exception {
		CentralRole answer = null;
		logger.debug(EELFLoggerDelegate.debugLogger, "Request received for getRoleInfo");

		try {
			answer = externalAccessRolesService.getRoleInfo(roleId, request.getHeader(UEBKEY));
		} catch (HttpClientErrorException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			logger.error(EELFLoggerDelegate.errorLogger, "getRoleInfo failed", e);
		}
		logger.debug(EELFLoggerDelegate.debugLogger, "Request completed for getRoleInfo");
		return answer;
	}
	
	@ApiOperation(value = "Gets role information for an application provided by function code.", response = CentralRoleFunction.class, responseContainer = "Json")
	@RequestMapping(value = { "/function/{code}" }, method = RequestMethod.GET, produces = "application/json")
	public CentralRoleFunction getRoleFunction(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("code") String code) throws Exception {
		CentralRoleFunction centralRoleFunction = null;
		try {
			centralRoleFunction = externalAccessRolesService.getRoleFunction(code, request.getHeader(UEBKEY));
		} catch (HttpClientErrorException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			logger.error(EELFLoggerDelegate.errorLogger, "getRoleInfo failed", e);
		}
		return centralRoleFunction;
	}

	@ApiOperation(value = "Saves role function for an application.", response = PortalRestResponse.class, responseContainer = "Json")
	@RequestMapping(value = { "/roleFunction" }, method = RequestMethod.POST, produces = "application/json")
	public PortalRestResponse<String> saveRoleFunction(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String roleFunc) throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		List<EPApp> applicationList = externalAccessRolesService.getApp(request.getHeader(UEBKEY));
		EPApp requestedApp = applicationList.get(0);
		Long appId = requestedApp.getId();
		try {
			 String data = roleFunc;
			CentralRoleFunction availableRoleFunction = mapper.readValue(data, CentralRoleFunction.class);
			availableRoleFunction.setAppId(appId);
			boolean saveOrUpdateResponse = externalAccessRolesService.saveCentralRoleFunction(availableRoleFunction, requestedApp);
			if(saveOrUpdateResponse){
				EPUser user = externalAccessRolesService.getUser(request.getHeader(LOGIN_ID)).get(0);
				EPApp app = externalAccessRolesService.getApp(request.getHeader(UEBKEY)).get(0);
				CentralRoleFunction function = externalAccessRolesService.getRoleFunction(availableRoleFunction.getCode(), request.getHeader(UEBKEY));
				String activityCode = (function.getCode() == null) ? EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_ADD_FUNCTION: EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_UPDATE_FUNCTION;
				logger.info(EELFLoggerDelegate.applicationLogger, "saveRoleFunction: succeeded for app {}, function {}",
						app.getId(), availableRoleFunction.getCode());
				AuditLog auditLog = new AuditLog();
				auditLog.setUserId(user.getId());
				auditLog.setActivityCode(activityCode);
				auditLog.setComments(EcompPortalUtils.truncateString("saveRoleFunction role for app:"+app.getId()+" and function:'"+availableRoleFunction.getCode()+"'", PortalConstants.AUDIT_LOG_COMMENT_SIZE));
				auditLog.setAffectedRecordId(user.getOrgUserId());
				auditService.logActivity(auditLog, null);
				MDC.put(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP,
						EPEELFLoggerAdvice.getCurrentDateTimeUTC());
				MDC.put(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP,
						EPEELFLoggerAdvice.getCurrentDateTimeUTC());
				EcompPortalUtils.calculateDateTimeDifferenceForLog(
						MDC.get(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP),
						MDC.get(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP));
				logger.info(EELFLoggerDelegate.auditLogger,
						EPLogUtil.formatAuditLogMessage("ExternalAccessRolesController.saveRoleFunction",
								activityCode, String.valueOf(user.getId()),
								user.getOrgUserId(), availableRoleFunction.getCode()));
				MDC.remove(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP);
				MDC.remove(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP);
				MDC.remove(SystemProperties.MDC_TIMER);
			} else{
				logger.error(EELFLoggerDelegate.errorLogger, "saveRoleFunction failed");
				return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "Failed to saveRoleFunction for '"+availableRoleFunction.getCode()+"'", "Failed");
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "saveRoleFunction failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, e.getMessage(), "Failed");
		}
		return new PortalRestResponse<String>(PortalRestStatusEnum.OK, "Successfully Saved", "Success");
	}
	
	@ApiOperation(value = "Deletes role function for an application.", response = PortalRestResponse.class, responseContainer = "Json")
	@RequestMapping(value = { "/roleFunction/{code}" }, method = RequestMethod.DELETE, produces = "application/json")
	public  PortalRestResponse<String> deleteRoleFunction(HttpServletRequest request, HttpServletResponse response, @PathVariable("code") String code) throws Exception {
		try {
			EPUser user = externalAccessRolesService.getUser(request.getHeader(LOGIN_ID)).get(0);
			EPApp app = externalAccessRolesService.getApp(request.getHeader(UEBKEY)).get(0);
			boolean getDelFuncResponse = externalAccessRolesService.deleteCentralRoleFunction(code, app);
			if(getDelFuncResponse){
				logger.info(EELFLoggerDelegate.applicationLogger, "deleteRoleFunction: succeeded for app {}, role {}",
						app.getId(), code);
				AuditLog auditLog = new AuditLog();
				auditLog.setUserId(user.getId());
				auditLog.setActivityCode(EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_DELETE_FUNCTION);
				auditLog.setComments(EcompPortalUtils.truncateString("Deleted function for app:"+app.getId()+" and function code:'"+code+"'", PortalConstants.AUDIT_LOG_COMMENT_SIZE));
				auditLog.setAffectedRecordId(user.getOrgUserId());
				auditService.logActivity(auditLog, null);
				MDC.put(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP,
						EPEELFLoggerAdvice.getCurrentDateTimeUTC());
				MDC.put(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP,
						EPEELFLoggerAdvice.getCurrentDateTimeUTC());
				EcompPortalUtils.calculateDateTimeDifferenceForLog(
						MDC.get(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP),
						MDC.get(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP));
				logger.info(EELFLoggerDelegate.auditLogger,
						EPLogUtil.formatAuditLogMessage("ExternalAccessRolesController.deleteRoleFunction",
								EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_DELETE_FUNCTION, String.valueOf(user.getId()),
								user.getOrgUserId(), code));
				MDC.remove(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP);
				MDC.remove(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP);
				MDC.remove(SystemProperties.MDC_TIMER);
			} else{
				logger.error(EELFLoggerDelegate.errorLogger, "deleteRoleFunction failed");
				return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "Failed to deleteRoleFunction for '"+code+"'", "Failed");
			}
		} catch (HttpClientErrorException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			logger.error(EELFLoggerDelegate.errorLogger, "deleteRoleFunction failed", e);
			return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "Failed to deleteRoleFunction for '"+code+"'", "Failed");
		}
		return new PortalRestResponse<>(PortalRestStatusEnum.OK, "Successfully Deleted", "Success");

	}	
	
	@ApiOperation(value = "Saves role for an application.", response = PortalRestResponse.class, responseContainer = "Json")
	@RequestMapping(value = { "/role" }, method = RequestMethod.POST, produces = "application/json")
	public PortalRestResponse<String> saveRole(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Role role) throws Exception {
		try {
			EPUser user = externalAccessRolesService.getUser(request.getHeader(LOGIN_ID)).get(0);
			EPApp app = externalAccessRolesService.getApp(request.getHeader(UEBKEY)).get(0);
			boolean getAddResponse = externalAccessRolesService.saveRoleForApplication(role, request.getHeader(UEBKEY));
			if (getAddResponse) {
				String activityCode = (role.getId() == null) ? EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_ADD_ROLE
						: EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_UPDATE_ROLE_AND_FUNCTION;
				logger.info(EELFLoggerDelegate.applicationLogger, "saveRole: succeeded for app {}, role {}",
						app.getId(), role.getName());
				AuditLog auditLog = new AuditLog();
				auditLog.setUserId(user.getId());
				auditLog.setActivityCode(activityCode);
				auditLog.setComments(EcompPortalUtils.truncateString("saveRole role for app:" + app.getId() + " and role:'" + role.getName()+"'", PortalConstants.AUDIT_LOG_COMMENT_SIZE));
				auditLog.setAffectedRecordId(user.getOrgUserId());
				auditService.logActivity(auditLog, null);
				MDC.put(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP, EPEELFLoggerAdvice.getCurrentDateTimeUTC());
				MDC.put(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP, EPEELFLoggerAdvice.getCurrentDateTimeUTC());
				EcompPortalUtils.calculateDateTimeDifferenceForLog(
						MDC.get(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP),
						MDC.get(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP));
				logger.info(EELFLoggerDelegate.auditLogger,
						EPLogUtil.formatAuditLogMessage("ExternalAccessRolesController.saveRole", activityCode,
								String.valueOf(user.getId()), user.getOrgUserId(), role.getName()));
				MDC.remove(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP);
				MDC.remove(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP);
				MDC.remove(SystemProperties.MDC_TIMER);
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "Failed to saveRole for '"+role.getName()+"'", "Failed");

			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			logger.error(EELFLoggerDelegate.errorLogger, "saveRole failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "Failed to saveRole", "Failed");
		}
		return new PortalRestResponse<String>(PortalRestStatusEnum.OK, "Successfully Saved", "Success");
	}
	
	@ApiOperation(value = "Deletes role for an application.", response = PortalRestResponse.class, responseContainer = "Json")
	@RequestMapping(value = { "/deleteRole/{code}" }, method = RequestMethod.DELETE, produces = "application/json")
	public  PortalRestResponse<String> deleteRole(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String code) throws Exception {
		try {
			boolean deleteResponse = externalAccessRolesService.deleteRoleForApplication(code, request.getHeader(UEBKEY));
			if(deleteResponse){
				EPUser user = externalAccessRolesService.getUser(request.getHeader(LOGIN_ID)).get(0);
				EPApp app = externalAccessRolesService.getApp(request.getHeader(UEBKEY)).get(0);
				logger.info(EELFLoggerDelegate.applicationLogger, "deleteRole: succeeded for app {}, role {}",
						app.getId(), code);
				AuditLog auditLog = new AuditLog();
				auditLog.setUserId(user.getId());
				auditLog.setActivityCode(EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_DELETE_ROLE);
				auditLog.setComments(EcompPortalUtils.truncateString("Deleted role for app:"+app.getId()+" and role:'"+code+"'", PortalConstants.AUDIT_LOG_COMMENT_SIZE));
				auditLog.setAffectedRecordId(user.getOrgUserId());
				auditService.logActivity(auditLog, null);
				MDC.put(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP,
						EPEELFLoggerAdvice.getCurrentDateTimeUTC());
				MDC.put(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP,
						EPEELFLoggerAdvice.getCurrentDateTimeUTC());
				EcompPortalUtils.calculateDateTimeDifferenceForLog(
						MDC.get(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP),
						MDC.get(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP));
				logger.info(EELFLoggerDelegate.auditLogger,
						EPLogUtil.formatAuditLogMessage("ExternalAccessRolesController.deleteRole",
								EcompAuditLog.CD_ACTIVITY_EXTERNAL_AUTH_DELETE_ROLE, String.valueOf(user.getId()),
								user.getOrgUserId(), code));
				MDC.remove(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP);
				MDC.remove(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP);
				MDC.remove(SystemProperties.MDC_TIMER);
			} else{
				logger.error(EELFLoggerDelegate.errorLogger, "deleteRole failed");
				return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "Failed to deleteRole for '"+code+"'", "Failed");
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			logger.error(EELFLoggerDelegate.errorLogger, "deleteRole failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "Failed to deleteRole", "Failed");
		}
		return new PortalRestResponse<String>(PortalRestStatusEnum.OK, "Successfully Deleted", "Success");

	}
	
	@ApiOperation(value = "Gets active roles for an application.", response = CentralRole.class, responseContainer = "Json")
	@RequestMapping(value = { "/activeRoles" }, method = RequestMethod.GET, produces = "application/json")
	public  List<CentralRole> getActiveRoles(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<CentralRole> cenRole = null;
		try {
			cenRole = externalAccessRolesService.getActiveRoles(request.getHeader(UEBKEY));
		} catch (HttpClientErrorException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			logger.error(EELFLoggerDelegate.errorLogger, "getActiveRoles failed", e);
		}
		return cenRole;
		
	}
	
	@ApiOperation(value = "deletes user roles for an application.", response = PortalRestResponse.class, responseContainer = "Json")
	@RequestMapping(value = { "/deleteDependcyRoleRecord/{roleId}" }, method = RequestMethod.DELETE, produces = "application/json")
	public PortalRestResponse<String> deleteDependencyRoleRecord(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("roleId") Long roleId) throws Exception {
		try {
			boolean deleteResponse = externalAccessRolesService.deleteDependencyRoleRecord(roleId,
					request.getHeader(UEBKEY), request.getHeader(LOGIN_ID));
			if (!deleteResponse) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "Failed to deleteDependencyRoleRecord",
						"Failed");
			}
		} catch (HttpClientErrorException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			logger.error(EELFLoggerDelegate.errorLogger, "deleteDependencyRoleRecord failed", e);
			return new PortalRestResponse<>(PortalRestStatusEnum.ERROR, "Failed to deleteDependencyRoleRecord",
					"Failed");
		}
		return new PortalRestResponse<>(PortalRestStatusEnum.OK, "Successfully Deleted", "Success");
	}
	
	@ApiOperation(value = "Bulk upload functions for an application.", response = PortalRestResponse.class, responseContainer = "Json")
	@RequestMapping(value = { "/upload/portal/functions" }, method = RequestMethod.POST, produces = "application/json")
	public  PortalRestResponse<String> bulkUploadFunctions(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer result = 0;
		try {
			result = externalAccessRolesService.bulkUploadFunctions(request.getHeader(UEBKEY));
		} catch (HttpClientErrorException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadFunctions failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "Failed to bulkUploadFunctions", "Failed");
		}
		return new PortalRestResponse<String>(PortalRestStatusEnum.OK, "Successfully added: "+result, "Success");
	}
	
	@ApiOperation(value = "Bulk upload roles for an application.", response = PortalRestResponse.class, responseContainer = "Json")
	@RequestMapping(value = { "/upload/portal/roles" }, method = RequestMethod.POST, produces = "application/json")
	public  PortalRestResponse<String> bulkUploadRoles(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer result = 0;
		try {
			result = externalAccessRolesService.bulkUploadRoles(request.getHeader(UEBKEY));
		} catch (HttpClientErrorException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadRoles failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "Failed to bulkUploadRoles", "Failed");
		}
		return new PortalRestResponse<String>(PortalRestStatusEnum.OK, "Successfully added: "+result, "Success");
	}
	
	@ApiOperation(value = "Bulk upload role functions for an application.", response = PortalRestResponse.class, responseContainer = "Json")
	@RequestMapping(value = { "/upload/portal/roleFunctions" }, method = RequestMethod.POST, produces = "application/json")
	public  PortalRestResponse<String> bulkUploadRoleFunctions(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer result = 0;
		try {
			result = externalAccessRolesService.bulkUploadRolesFunctions(request.getHeader(UEBKEY));
		} catch (HttpClientErrorException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadRoleFunctions failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "Failed to bulkUploadRoleFunctions", "Failed");
		}
		return new PortalRestResponse<String>(PortalRestStatusEnum.OK, "Successfully added: "+result, "Success");
	}
	
	@ApiOperation(value = "Bulk upload user roles for an application.", response = PortalRestResponse.class, responseContainer = "Json")
	@RequestMapping(value = { "/upload/portal/userRoles" }, method = RequestMethod.POST, produces = "application/json")
	public  PortalRestResponse<String> bulkUploadUserRoles(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer result = 0;
		try {
			result = externalAccessRolesService.bulkUploadUserRoles(request.getHeader(UEBKEY));
		} catch (HttpClientErrorException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadUserRoles failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "Failed to bulkUploadUserRoles", "Failed");
		}
		return new PortalRestResponse<String>(PortalRestStatusEnum.OK, "Successfully added: "+result, "Success");
	}
	
	@ApiOperation(value = "Bulk upload functions for an partner application.", response = PortalRestResponse.class, responseContainer = "Json")
	@RequestMapping(value = { "/upload/partner/functions" }, method = RequestMethod.POST, produces = "application/json")
	public  PortalRestResponse<String> bulkUploadPartnerFunctions(HttpServletRequest request, HttpServletResponse response, @RequestBody List<RoleFunction> upload) throws Exception {
		try {
			externalAccessRolesService.bulkUploadPartnerFunctions(request.getHeader(UEBKEY), upload);
		} catch (HttpClientErrorException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadFunctions failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "Failed to bulkUploadFunctions", "Failed");
		}
		return new PortalRestResponse<String>(PortalRestStatusEnum.OK, "Successfully added", "Success");
	}
	
	@ApiOperation(value = "Bulk upload roles for an partner application.", response = PortalRestResponse.class, responseContainer = "Json")
	@RequestMapping(value = { "/upload/partner/roles" }, method = RequestMethod.POST, produces = "application/json")
	public  PortalRestResponse<String> bulkUploadPartnerRoles(HttpServletRequest request, HttpServletResponse response, @RequestBody List<Role> upload) throws Exception {
		try {
			externalAccessRolesService.bulkUploadPartnerRoles(request.getHeader(UEBKEY), upload);
		} catch (HttpClientErrorException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadRoles failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "Failed to bulkUploadRoles", "Failed");
		}
		return new PortalRestResponse<String>(PortalRestStatusEnum.OK, "Successfully added", "Success");
	}
	
	@ApiOperation(value = "Bulk upload role functions for an partner application.", response = PortalRestResponse.class, responseContainer = "Json")
	@RequestMapping(value = { "/upload/partner/roleFunctions" }, method = RequestMethod.POST, produces = "application/json")
	public  PortalRestResponse<String> bulkUploadPartnerRoleFunctions(HttpServletRequest request, HttpServletResponse response, @RequestBody List<Role> upload) throws Exception {
		try {
			externalAccessRolesService.bulkUploadPartnerRoleFunctions(request.getHeader(UEBKEY), upload);
		} catch (HttpClientErrorException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadRoles failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "Failed to bulkUploadPartnerRoleFunctions", "Failed");
		}
		return new PortalRestResponse<String>(PortalRestStatusEnum.OK, "Successfully added", "Success");
	}
	
	@ApiOperation(value = "Gets all functions along with global functions", response = List.class, responseContainer = "Json")
	@RequestMapping(value = { "/menuFunctions" }, method = RequestMethod.GET, produces = "application/json")
	public  List<String> getMenuFunctions(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<String> functionsList = null;
		try {
			functionsList = externalAccessRolesService.getMenuFunctionsList(request.getHeader(UEBKEY));
		} catch (HttpClientErrorException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			logger.error(EELFLoggerDelegate.errorLogger, "getMenuFunctions failed", e);
		}
		return functionsList;
	}
	
}
