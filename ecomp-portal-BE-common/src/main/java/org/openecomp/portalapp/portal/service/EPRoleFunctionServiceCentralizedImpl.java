package org.openecomp.portalapp.portal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.SessionFactory;
import org.openecomp.portalapp.portal.domain.CentralRoleFunction;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalsdk.core.domain.RoleFunction;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public class EPRoleFunctionServiceCentralizedImpl implements EPRoleFunctionService{

	
	@Autowired
	private DataAccessService dataAccessService;
	
	@Autowired
	private  SessionFactory sessionFactory;
	
	@SuppressWarnings({ "unchecked"})
	@Override
	public List<RoleFunction> getRoleFunctions() {
		List<CentralRoleFunction> getRoleFuncList = null;
		List<RoleFunction> getRoleFuncListOfPortal = new ArrayList<>();
		final Map<String, Long> params = new HashMap<>();
		params.put("appId", (long) 1);
		//Sync all functions from external system into Ecomp portal DB
		getRoleFuncList = dataAccessService.executeNamedQuery("getAllRoleFunctions", params, null);
		for(CentralRoleFunction roleFunction : getRoleFuncList)
		{
			RoleFunction roleFun = new RoleFunction();
			roleFun.setCode(roleFunction.getCode());
			roleFun.setName(roleFunction.getName());
			getRoleFuncListOfPortal.add(roleFun);
		}
		return getRoleFuncListOfPortal;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set getRoleFunctions(HttpServletRequest request, EPUser user) {
		HttpSession session = request.getSession();
		String userId = user.getId().toString();
		final Map<String, String> params = new HashMap<>();
		params.put("userId", userId);
		@SuppressWarnings("unchecked")
		List getRoleFuncListOfPortal =   dataAccessService.executeNamedQuery("getRoleFunctionsOfUser", params, null);
		Set<String> getRoleFuncListOfPortalSet = new HashSet<>(getRoleFuncListOfPortal);	
		session.setAttribute(SystemProperties.getProperty(SystemProperties.ROLE_FUNCTIONS_ATTRIBUTE_NAME),
				getRoleFuncListOfPortalSet);
		return getRoleFuncListOfPortalSet;
				
	}

	
	
}
