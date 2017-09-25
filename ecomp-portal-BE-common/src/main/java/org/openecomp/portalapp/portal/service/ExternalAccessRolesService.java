package org.openecomp.portalapp.portal.service;

import java.util.List;

import org.hibernate.Session;
import org.openecomp.portalapp.portal.domain.CentralRoleFunction;
import org.openecomp.portalapp.portal.domain.EPApp;
import org.openecomp.portalapp.portal.domain.EPRole;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.transport.CentralRole;
import org.openecomp.portalsdk.core.domain.Role;
import org.openecomp.portalsdk.core.domain.RoleFunction;
import org.openecomp.portalsdk.core.restful.domain.EcompUser;

public interface ExternalAccessRolesService {
	
	/**
	 * It gets list of application roles 
	 * 
	 * @param appId
	 * @return List
	 * @throws Exception 
	 */
	public List<EPRole> getAppRoles(Long appId) throws Exception;
	
	/**
	 * It returns single app record
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
	 * It returns complete user information
	 * 
	 * @param loginId
	 * @return EPUser object
	 * @throws Exception
	 */
	List<EPUser> getUser(String loginId) throws Exception;
	
	/**
	 * It returns complete user information including application roles permissions
	 * 
	 * @param loginId
	 * @param uebkey
	 * @return String
	 * @throws Exception
	 */
	String getUserWithRoles(String loginId, String uebkey) throws Exception;

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
	 * @return true else false
	 * @throws Exception 
	 */
	public boolean saveCentralRoleFunction(CentralRoleFunction domainCentralRoleFunction, EPApp requestedApp) throws Exception;

	/**
	 * It deletes role function in the DB
	 * 
	 * @param code
	 * @param app 
	 */
	public boolean deleteCentralRoleFunction(String code, EPApp app);

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
	boolean saveRoleForApplication(Role saveRole, String uebkey) throws Exception;

	/**
	 *  It deletes role in the DB
	 *  
	 * @param code
	 * @param uebkey
	 * @return true else false
	 * @throws Exception 
	 */
	boolean deleteRoleForApplication(String code, String uebkey) throws Exception;

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
	 * @return true else false
	 * @throws Exception 
	 */
	public boolean deleteDependencyRoleRecord(Long roleId, String uebkey, String LoginId) throws Exception;
	
	/**
	 * It sync new functions codes and names from and updates role functions from external access system
	 * 
	 * @param app
	 * @throws Exception
	 */
	public void syncRoleFunctionFromExternalAccessSystem(EPApp app) throws Exception;

	/**
	 * It uploads portal functions into external auth system
	 * @param uebkey
	 * @return
	 * @throws Exception
	 */
	public Integer bulkUploadFunctions(String uebkey) throws Exception;

	/**
	 * It uploads portal roles into external auth system
	 * @param uebkey
	 * @return
	 * @throws Exception
	 */
	public Integer bulkUploadRoles(String uebkey) throws Exception;

	/**
	 * It uploads partner application role functions into external auth system
	 * 
	 * @param uebkey
	 * @param upload
	 * @throws Exception
	 */
	public void bulkUploadPartnerFunctions(String uebkey, List<RoleFunction> upload) throws Exception;

	/** 
	 * It uploads partner application role functions into external auth system
	 * 
	 * @param uebkey
	 * @param upload
	 * @throws Exception
	 */
	public void bulkUploadPartnerRoles(String uebkey, List<Role> upload) throws Exception;

	/**
	 * It returns total no. of portal application role functions records added in external auth system 
	 * @param uebkey
	 * @return
	 * @throws Exception
	 */
	Integer bulkUploadRolesFunctions(String uebkey) throws Exception;
	
	/**
	 *  It syncs the roles and rolefunctions to the ecomp DB from AAF
	 * @param app
	 * @throws Exception
	 */
	void syncApplicationRolesWithEcompDB(EPApp app) throws Exception;

	/**
	 * It uploads list of user roles of the application into external auth system 
	 * 
	 * @param uebkey
	 * @return
	 * @throws Exception
	 */
	public Integer bulkUploadUserRoles(String uebkey) throws Exception;

	/**
	 * It Uploads partner application role functions into external auth system
	 * 
	 * @param uebkey
	 * @param roleList
	 * @throws Exception
	 */
	void bulkUploadPartnerRoleFunctions(String uebkey, List<Role> roleList) throws Exception;

	/**
	 * it deletes all dependency role records 
	 * 
	 * @param localSession
	 * @param roleId
	 * @param appId
	 * @throws Exception
	 */
	public void deleteRoleDependencyRecords(Session localSession, Long roleId, Long appId) throws Exception;

	/**
	 * It returns list of applications functions along with functions associated with global role
	 * 
	 * @param uebkey
	 * @return
	 * @throws Exception
	 */
	List<String> getMenuFunctionsList(String uebkey) throws Exception;
	
	/**
	 * 
	 * @param uebkey applications UebKey
	 * @return
	 * @throws Exception
	 * Method getAllUsers returns all the active users of application
	 */
	List<EcompUser> getAllAppUsers(String uebkey) throws Exception;

}
