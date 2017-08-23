package org.openecomp.portalapp.portal.service;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalsdk.core.domain.RoleFunction;

public interface EPRoleFunctionService {
	/**
	 * Builds a set of role functions and sets a session attribute with it.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return Set of role functions that was built.
	 */
	public List<RoleFunction> getRoleFunctions();
	
	
	/**
	 * Builds a set of role functions of user 
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return Set of role functions that was built.
	 */
	public Set getRoleFunctions(HttpServletRequest request, EPUser user);

}
