/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Unless otherwise specified, all documentation contained herein is licensed
 * under the Creative Commons License, Attribution 4.0 Intl. (the "License");
 * you may not use this documentation except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             https://creativecommons.org/licenses/by/4.0/
 *
 * Unless required by applicable law or agreed to in writing, documentation
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ============LICENSE_END============================================
 *
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.portal.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.json.JSONArray;
import org.onap.portalapp.portal.domain.CentralV2RoleFunction;
import org.onap.portalapp.portal.domain.CentralizedApp;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPRole;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.ExternalRoleDetails;
import org.onap.portalapp.portal.exceptions.InvalidUserException;
import org.onap.portalapp.portal.transport.CentralRole;
import org.onap.portalapp.portal.transport.CentralUser;
import org.onap.portalapp.portal.transport.CentralV2Role;
import org.onap.portalapp.portal.transport.ExternalRequestFieldsValidator;
import org.onap.portalsdk.core.domain.Role;
import org.onap.portalsdk.core.domain.RoleFunction;
import org.onap.portalsdk.core.restful.domain.EcompUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	 * @param uebkey
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
	List<EPUser> getUser(String loginId) throws InvalidUserException;
	
	/**
	 * It returns complete user information including application roles permissions
	 * 
	 * @param loginId
	 * @param uebkey
	 * @return String
	 * @throws Exception
	 */
	String getV2UserWithRoles(String loginId, String uebkey) throws Exception;

	/**
	 * It returns list of all role functions
	 * @param string 
	 * @return List
	 * @throws Exception 
	 */
	List<CentralV2RoleFunction> getRoleFuncList(String string) throws Exception;
	
	/**
	 * It return list of role provided by the app uebkey and roleId
	 * 
	 * @param roleId
	 * @param uebkey
	 * @return CentralRole
	 * @throws Exception
	 */
	CentralV2Role getRoleInfo(Long roleId, String uebkey) throws Exception;
	
	/**
	 *  It returns the CentralRoleFunction object 
	 *   
	 * @param functionCode
	 * @param uebkey 
	 * @return CentralRoleFunction
	 * @throws Exception 
	 */
	public CentralV2RoleFunction getRoleFunction(String functionCode, String uebkey) throws Exception;

	/**
	 *  It saves role function in the DB
	 *  
	 * @param domainCentralRoleFunction
	 * @param requestedApp 
	 * @return true else false
	 * @throws Exception 
	 */
	public boolean saveCentralRoleFunction(CentralV2RoleFunction domainCentralRoleFunction, EPApp requestedApp) throws Exception;

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
	public List<CentralV2Role> getRolesForApp(String uebkey) throws Exception;
	
	/**
	 * 
	 * It saves role function in the DB
	 * 
	 * @param saveRole
	 * @param uebkey
	 * @return message and true or false  
	 * @throws Exception
	 */
	ExternalRequestFieldsValidator saveRoleForApplication(Role saveRole, String uebkey) throws Exception;

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
	List<CentralV2Role> getActiveRoles(String uebkey) throws Exception;
	
	/**
	 * It deletes user related roles for an application in the table
	 * @param roleId
	 * @param uebkey
	 * @param LoginId 
	 * @return true else false
	 * @throws Exception 
	 */
	public ExternalRequestFieldsValidator deleteDependencyRoleRecord(Long roleId, String uebkey, String LoginId) throws Exception;
	
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
	 * @param isPortalRequest 
	 * @throws Exception
	 */
	public void deleteRoleDependencyRecords(Session localSession, Long roleId, Long appId, boolean isPortalRequest) throws Exception;

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
	
	/**
	 * 
	 * @param result
	 * @return returns Role Object
	 * Method ConvertCentralRoleToRole converts the CentralRole String to Role Object
	 */
	public Role ConvertCentralRoleToRole(String result);
	
	/**
	 * It returns the list of centralized applications
	 * 
	 * @param userId
	 * @return List
	 */
	public List<CentralizedApp> getCentralizedAppsOfUser(String userId);
	
	/**
	 * It returns the list of globalRoles of Portal
	 * @return
	 */
	public  List<EPRole> getGlobalRolesOfPortal();
	
	/**
	 * It converts list of CentralRoleFunction objects to RoleFunction objects
	 * @param answer contains list of CentralRoleFunction objects
	 * @return List of RoleFunction objects
	 */
	public List<RoleFunction> convertCentralRoleFunctionToRoleFunctionObject(List<CentralV2RoleFunction> answer);
	
	/**
	 * 
	 * It returns user roles for older version
	 * 
	 * @param loginId
	 * @param uebkey
	 * @return EPUser
	 * @throws Exception 
	 */
	public CentralUser getUserRoles(String loginId, String uebkey) throws Exception;
	
	/**
	 * It converts list of V2 CentralRole objects to old version CentralRole objects
	 * 
	 * @param v2CenRole
	 * @return List of CentralRole objects
	 */
	public List<CentralRole> convertV2CentralRoleListToOldVerisonCentralRoleList(List<CentralV2Role> v2CenRole);
	
	/**
	 * 
	 * It finds namespace in external auth system if found returns namespace information
	 * 
	 * @param epApp
	 * @return Http response
	 */
	public ResponseEntity<String> getNameSpaceIfExists(EPApp epApp) throws Exception, HttpClientErrorException;
	
	/**
	 * 
	 * It converts V2 CentralRole objects to old version CentralRole objects
	 * 
	 * @param answer
	 * @return
	 */
	public CentralRole convertV2CentralRoleToOldVerisonCentralRole(CentralV2Role answer);

	/**
	 * 
	 * Returns list of EPRole Objects if exists
	 * 
	 * @param app
	 * @return List of EPRole objects
	 */
	Map<String, EPRole> getCurrentRolesInDB(EPApp app);
    
	
	/**
	 * 
	 * It uploads list of users for single role when role name is re-named
	 * 
	 * @param header
	 * @param roleId
	 * @param roleName 
	 * @return number of user roles added in External Auth System
	 * @throws Exception 
	 */
	public Integer bulkUploadUsersSingleRole(String uebkey, Long roleId, String roleName) throws Exception;
	
	/**
	 * 
	 * It returns JSON array of external auth roles and its corresponding functions
	 * 
	 * @param app
	 * @return JSON Array
	 * @throws Exception
	 */
	public JSONArray getAppRolesJSONFromExtAuthSystem(EPApp app) throws Exception;
	
	/**
	 * It encodes the function code  based on Hex encoding
	 * @param funCode
	 * 
	 */
	public String encodeFunctionCode(String funCode);
	
	/**
	 * 
	 * It returns list of ExternalRoleDetails which is converted from JSON array of roles
	 * 
	 * @param app 
	 * @param mapper
	 * @param extRole contains external auth application roles JSON array
	 * @return List of ExternalRoleDetails objects
	 * @throws IOException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 */
	public List<ExternalRoleDetails> getExternalRoleDetailsList(EPApp app,
			ObjectMapper mapper, JSONArray extRole)	throws IOException, JsonParseException, JsonMappingException;
	
	public JSONArray getAllUsersByRole(String roleName) throws Exception;

}
