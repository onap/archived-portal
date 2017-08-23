package org.openecomp.portalapp.portal.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.openecomp.portalapp.portal.domain.EPRole;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalsdk.core.domain.RoleFunction;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class EPRoleFunctionServiceImpl implements EPRoleFunctionService {
	@Autowired
	private DataAccessService dataAccessService;
	
	

	public DataAccessService getDataAccessService() {
		return dataAccessService;
	}

	public void setDataAccessService(DataAccessService dataAccessService) {
		this.dataAccessService = dataAccessService;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RoleFunction> getRoleFunctions() {
		return getDataAccessService().getList(RoleFunction.class, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set getRoleFunctions(HttpServletRequest request, EPUser user) {
		HashSet roleFunctions = null;

		HttpSession session = request.getSession();
		roleFunctions = (HashSet) session
				.getAttribute(SystemProperties.getProperty(SystemProperties.ROLE_FUNCTIONS_ATTRIBUTE_NAME));

		if (roleFunctions == null) {
			HashMap roles = EPUserUtils.getRoles(request);
			roleFunctions = new HashSet();

			Iterator i = roles.keySet().iterator();

			while (i.hasNext()) {
				Long roleKey = (Long) i.next();
				EPRole role = (EPRole) roles.get(roleKey);

				Iterator j = role.getRoleFunctions().iterator();

				while (j.hasNext()) {
					RoleFunction function = (RoleFunction) j.next();
					roleFunctions.add(function.getCode());
				}
			}

			session.setAttribute(SystemProperties.getProperty(SystemProperties.ROLE_FUNCTIONS_ATTRIBUTE_NAME),
					roleFunctions);
		}

		return roleFunctions;
	}
	
	
	

}
