package org.openecomp.portalapp.portal.controller;

import javax.servlet.http.HttpServletRequest;

import org.openecomp.portalapp.controller.EPRestrictedBaseController;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.openecomp.portalapp.portal.logging.aop.EPAuditLog;
import org.openecomp.portalapp.portal.service.UserService;
import org.openecomp.portalapp.portal.transport.ProfileDetail;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.onboarding.util.CipherUtil;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class UserController extends EPRestrictedBaseController {

	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	/**
	 * RESTful service method to get ECOMP Logged in User details.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * 
	 * @return PortalRestResponse of EPUser
	 */
	@RequestMapping(value = { "/portalApi/loggedinUser" }, method = RequestMethod.GET, produces = "application/json")
	public PortalRestResponse<ProfileDetail> getLoggedinUser(HttpServletRequest request) {
		PortalRestResponse<ProfileDetail> portalRestResponse = null;
		try {
			EPUser user = EPUserUtils.getUserSession(request);
			ProfileDetail profileDetail = new ProfileDetail(user.getFirstName(), user.getLastName(),
					user.getMiddleInitial(), user.getEmail(), user.getLoginId(),  CipherUtil.decrypt(user.getLoginPwd()));
			portalRestResponse = new PortalRestResponse<ProfileDetail>(PortalRestStatusEnum.OK, "success",
					profileDetail);
			EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/loggedinUser", "result =", profileDetail);
		} catch (Exception e) {
			portalRestResponse = new PortalRestResponse<ProfileDetail>(PortalRestStatusEnum.ERROR, e.getMessage(),
					null);
			logger.error(EELFLoggerDelegate.errorLogger, "getLoggedinUser failed", e);
		}
		return portalRestResponse;
	}

	/**
	 * RESTful service method to update ECOMP Logged in User in DB.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param profileDetail
	 *            Body with user information
	 * @return PortalRestResponse of String
	 */
	@RequestMapping(value = {
			"/portalApi/modifyLoggedinUser" }, method = RequestMethod.PUT, produces = "application/json")
	public PortalRestResponse<String> modifyLoggedinUser(HttpServletRequest request,
			@RequestBody ProfileDetail profileDetail) {
		PortalRestResponse<String> portalRestResponse = null;
		try {
			String errorMsg = "";
			if (profileDetail.getFirstName().equals("") || profileDetail.getLastName().equals("")
					|| profileDetail.getEmail().equals("") || profileDetail.getLoginId().equals("")
					|| profileDetail.getLoginPassword().equals("")) {
				errorMsg = "Required field(s) is missing";
				portalRestResponse = new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, errorMsg, null);
				logger.error(EELFLoggerDelegate.errorLogger, "modifyLoggedinUser failed", errorMsg);
			} else {
				EPUser user = EPUserUtils.getUserSession(request);
				user.setFirstName(profileDetail.getFirstName());
				user.setLastName(profileDetail.getLastName());
				user.setEmail(profileDetail.getEmail());
				user.setMiddleInitial(profileDetail.getMiddleName());
				user.setLoginId(profileDetail.getLoginId());
				user.setLoginPwd(CipherUtil.encrypt(profileDetail.getLoginPassword()));
				userService.saveUser(user);
				// Update user info in the session
				request.getSession().setAttribute(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME),
						user);
				portalRestResponse = new PortalRestResponse<String>(PortalRestStatusEnum.OK, "success", null);
				EcompPortalUtils.logAndSerializeObject(logger, "/portalApi/modifyLoggedinUser", "result =", user);
			}
		} catch (Exception e) {
			portalRestResponse = new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, e.toString(), null);
			logger.error(EELFLoggerDelegate.errorLogger, "modifyLoggedinUser failed", e);
		}
		return portalRestResponse;
	}
}
