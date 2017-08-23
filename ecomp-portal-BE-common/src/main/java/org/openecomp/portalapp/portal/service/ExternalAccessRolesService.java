package org.openecomp.portalapp.portal.service;

import java.util.List;

import org.hibernate.Session;
import org.openecomp.portalapp.portal.domain.CentralRoleFunction;
import org.openecomp.portalapp.portal.domain.EPApp;
import org.openecomp.portalapp.portal.domain.EPRole;
import org.openecomp.portalapp.portal.transport.CentralRole;
import org.openecomp.portalsdk.core.domain.Role;
import org.openecomp.portalsdk.core.domain.RoleFunction;

public interface ExternalAccessRolesService {
	
	/**
	 * It gets all application roles 
	 * 
	 * @param appId
	 * @param extRequestValue
	 * @return List
	 * @throws Exception 
	 */
	public List<EPRole> getAppRoles(Long appId, Boolean extRequestValue) throws Exception;
	
	/**
	 * It returns application details
	 * 
	 * @param uebkey
	 * @return List
	 * @throws Exception 
	 */		
	public List<EPApp> getApp(String uebkey) throws Exception;
	
	/**
	 * Adds role in the external access system if fails throws exception
	 * 
	 * @param addRoles
	 * @param UE
	 * @return boolean
	 * @throws Exception
	 */
	public boolean addRole(Role addRoles, String uebkey) throws Exception;
	
	/**
	 * Updates role in the external access system otherwise throws exception
	 * 
	 * @param updateRole
	 * @param uebkey
	 * @return boolean
	 * @throws Exception
	 */
	void updateRole(Role updateRole, EPApp app) throws Exception;

	/**
	 * It returns complete user information including application roles permissions
	 * 
	 * @param loginId
	 * @param uebkey
	 * @return String
	 * @throws Exception
	 */
	String getUser(String loginId, String uebkey) throws Exception;

	/**
	 * It returns list of all role functions
	 * @param string 
	 * @return List
	 * @throws Exception 
	 */
	List<CentralRoleFunction> getRoleFuncList(String string) throws Exception;
	
	/**
	 * It return list of role provided by the app uebkey and roleId
	 * 
	 * @param roleId
	 * @param uebkey
	 * @return CentralRole
	 * @throws Exception
	 */
	CentralRole getRoleInfo(Long roleId, String uebkey) throws Exception;
	
	/**
	 *  It returns the CentralRoleFunction object 
	 *   
	 * @param functionCode
	 * @param uebkey 
	 * @return CentralRoleFunction
	 * @throws Exception 
	 */
	public CentralRoleFunction getRoleFunction(String functionCode, String uebkey) throws Exception;

	/**
	 *  It saves role function in the DB
	 *  
	 * @param domainCentralRoleFunction
	 * @param requestedApp 
	 * @throws Exception 
	 */
	public void saveCentralRoleFunction(CentralRoleFunction domainCentralRoleFunction, EPApp requestedApp) throws Exception;

	/**
	 * It deletes role function in the DB
	 * 
	 * @param code
	 * @param string 
	 */
	public void deleteCentralRoleFunction(String code, String string);

	/**
	 * It gets all roles the applications
	 * 
	 * @param uebkey
	 * @return List
	 * @throws Exception 
	 */
	public List<CentralRole> getRolesForApp(String uebkey) throws Exception;
	
	/**
	 * It saves role function in the DB
	 * 
	 * @param saveRole
	 * @param uebkey
	 * @throws Exception 
	 */
	void saveRoleForApplication(Role saveRole, String uebkey) throws Exception;

	/**
	 *  It deletes role in the DB
	 *  
	 * @param code
	 * @param uebkey
	 * @throws Exception 
	 */
	void deleteRoleForApplication(String code, String uebkey) throws Exception;

	/**
	 * It gets all active roles for single application 
	 * 
	 * @param uebkey
	 * @return List
	 * @throws Exception
	 */
	List<CentralRole> getActiveRoles(String uebkey) throws Exception;
	
	/**
	 * It deletes user related roles for an application in the table
	 * @param roleId
	 * @param uebkey
	 * @param LoginId 
	 * @return
	 * @throws Exception 
	 */
	public void deleteDependcyRoleRecord(Long roleId, String uebkey, String LoginId) throws Exception;
	
	/**
	 * It sync new functions codes and names from and updates role functions from external access system
	 * 
	 * @param app
	 * @throws Exception
	 */
	public void syncRoleFunctionFromExternalAccessSystem(EPApp app) throws Exception;

	public Integer bulkUploadFunctions(String uebkey) throws Exception;

	public Integer bulkUploadRoles(String uebkey) throws Exception;

	public void bulkUploadPartnerFunctions(String header, List<RoleFunction> upload) throws Exception;

	public void bulkUploadPartnerRoles(String header, List<Role> upload) throws Exception;

	Integer bulkUploadRolesFunctions(String uebkey) throws Exception;
	
	/**
	 * SyncApplicationRolesWithEcompDB sync the roles and rolefunctions to the ecomp DB from AAF
	 * @param app
	 * @throws Exception
	 */

	void SyncApplicationRolesWithEcompDB(EPApp app) throws Exception;

	public Integer bulkUploadUserRoles(String uebkey) throws Exception;

	void bulkUploadPartnerRoleFunctions(String uebkey, List<Role> roleList) throws Exception;

	public void deleteRoleDependeciesRecord(Session localSession, Long roleId) throws Exception;

	List<String> getMenuFunctionsList(String uebkey) throws Exception;


}
