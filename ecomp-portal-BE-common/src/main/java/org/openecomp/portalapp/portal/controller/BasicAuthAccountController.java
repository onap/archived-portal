package org.openecomp.portalapp.portal.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openecomp.portalapp.controller.EPRestrictedBaseController;
import org.openecomp.portalapp.portal.domain.BasicAuthCredentials;
import org.openecomp.portalapp.portal.domain.EPEndpoint;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.openecomp.portalapp.portal.logging.aop.EPAuditLog;
import org.openecomp.portalapp.portal.service.AdminRolesService;
import org.openecomp.portalapp.portal.service.BasicAuthAccountService;
import org.openecomp.portalapp.util.EPUserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("unchecked")
@RestController
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class BasicAuthAccountController extends EPRestrictedBaseController{
	
	@Autowired
	private BasicAuthAccountService basicAuthAccountService;

	@Autowired
	private AdminRolesService adminRolesService;

	/**
	 * Saves Basic Authentication account for external systems 
	 * @param BasicAuthCredentials
	 * @return Id of the newly created account
	*/
	
	@RequestMapping(value = { "/portalApi/basicAuthAccount" }, method = RequestMethod.POST)
	public PortalRestResponse<String> createBasicAuthAccount(HttpServletRequest request, HttpServletResponse response,
			@RequestBody BasicAuthCredentials newBasicAuthAccount) throws Exception {
		
		EPUser user = EPUserUtils.getUserSession(request);
		if (!adminRolesService.isSuperAdmin(user)){
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "Authorization Required", "Admin Only Operation! ");
		}
		
		if(newBasicAuthAccount == null){
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "FAILURE",
					"newBasicAuthAccount cannot be null or empty");
		}
		long accountId = basicAuthAccountService.saveBasicAuthAccount(newBasicAuthAccount);
		
		List<Long> endpointIdList = new ArrayList<>();
		try {
			for(EPEndpoint ep: newBasicAuthAccount.getEndpoints()){
				endpointIdList.add(basicAuthAccountService.saveEndpoints(ep));
			}
			for(Long endpointId: endpointIdList){
				basicAuthAccountService.saveEndpointAccount(accountId, endpointId);
			}
		} catch (Exception e) {
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "FAILURE", e.getMessage());
		}
		
		return new PortalRestResponse<String>(PortalRestStatusEnum.OK, "SUCCESS", "");
	}
	
	/**
	 * Returns list of all  BasicAuthCredentials in the system
	 * @return List<BasicAuthCredentials>
	 */
	
	@RequestMapping(value = { "/portalApi/basicAuthAccount" }, method = RequestMethod.GET)
	public PortalRestResponse<List<BasicAuthCredentials>> getBasicAuthAccount(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		EPUser user = EPUserUtils.getUserSession(request);
		if (!adminRolesService.isSuperAdmin(user)){
			return new PortalRestResponse<List<BasicAuthCredentials>>(PortalRestStatusEnum.ERROR, "UnAuthorized! Admin Only Operation", new ArrayList<>());
		}

		return new PortalRestResponse<List<BasicAuthCredentials>>(PortalRestStatusEnum.OK, "Success", basicAuthAccountService.getAccountData());
	}
	
	/**
	 * Updates an existing BasicAuthCredentials account
	 */
	
	@RequestMapping(value = { "/portalApi/basicAuthAccount/{accountId}" }, method = RequestMethod.PUT)
	public PortalRestResponse<String> updateAccount(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("accountId") long accountId, @RequestBody BasicAuthCredentials newBasicAuthAccount) throws Exception {
		
		EPUser user = EPUserUtils.getUserSession(request);
		if (!adminRolesService.isSuperAdmin(user)){
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "Authorization Required", "Admin Only Operation! ");
		}

		if (newBasicAuthAccount == null) {
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "FAILURE",
					"BasicAuthCredentials cannot be null or empty");
		}
		try {
			basicAuthAccountService.updateBasicAuthAccount(accountId, newBasicAuthAccount);
		} catch (Exception e) {
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "FAILURE", e.getMessage());
		}
		return new PortalRestResponse<String>(PortalRestStatusEnum.OK, "SUCCESS", "");
	}
	
	/**
	 * deletes an existing BasicAuthCredentials account
	 */

	@RequestMapping(value = { "/portalApi/basicAuthAccount/{accountId}" }, method = RequestMethod.DELETE)
	public PortalRestResponse<String> deleteAccount(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("accountId") long accountId) throws Exception {
		
		EPUser user = EPUserUtils.getUserSession(request);
		if (!adminRolesService.isSuperAdmin(user)){
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "Authorization Required", "Admin Only Operation! ");
		}

		
		try {
			basicAuthAccountService.deleteEndpointAccout(accountId);
		} catch (Exception e) {
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "FAILURE", e.getMessage());
		}
		return new PortalRestResponse<String>(PortalRestStatusEnum.OK, "SUCCESS", "");
	}
	
	
}
