/*
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 * Modifications Copyright (c) 2019 Samsung
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
 *
 */

package org.onap.portal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import org.onap.portal.domain.db.ep.EpAppFunction;
import org.onap.portal.domain.db.fn.FnApp;
import org.onap.portal.domain.db.fn.FnRole;
import org.onap.portal.domain.dto.transport.CentralV2Role;
import org.onap.portal.exception.RoleFunctionException;
import org.onap.portal.logging.logic.EPLogUtil;
import org.onap.portal.service.ep.EpAppFunctionService;
import org.onap.portal.service.fn.FnRoleService;
import org.onap.portal.utils.EPCommonSystemProperties;
import org.onap.portal.utils.EPUserUtils;
import org.onap.portal.utils.EcompPortalUtils;
import org.onap.portal.utils.PortalConstants;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalAccessRolesService {

  private static final String APP_ROLE_NAME_PARAM = "appRoleName";
  private static final String GET_ROLE_TO_UPDATE_IN_EXTERNAL_AUTH_SYSTEM = "getRoletoUpdateInExternalAuthSystem";
  private static final String GET_PORTAL_APP_ROLES_QUERY = "getPortalAppRoles";
  private static final String GET_ROLE_FUNCTION_QUERY = "getRoleFunction";
  private static final String FUNCTION_CODE_PARAMS = "functionCode";
  private static final String AND_FUNCTION_CD_EQUALS = " and function_cd = '";
  private static final String OWNER = ".owner";
  private static final String ADMIN = ".admin";
  private static final String ACCOUNT_ADMINISTRATOR = ".Account_Administrator";
  private static final String FUNCTION_PIPE = "|";
  private static final String EXTERNAL_AUTH_PERMS = "perms";
  private static final String EXTERNAL_AUTH_ROLE_DESCRIPTION = "description";
  private static final String IS_EMPTY_JSON_STRING = "{}";
  private static final String CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE = "Connecting to External Auth system";
  private static final String APP_ID = "appId";
  private static final String ROLE_NAME = "name";
  private static final String APP_ID_EQUALS = " app_id = ";

  private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(ExternalAccessRolesService.class);
  private RestTemplate template = new RestTemplate();

  private final FnRoleService fnRoleService;
  private final EpAppFunctionService epAppFunctionService;

  @Autowired
  public ExternalAccessRolesService(FnRoleService fnRoleService,
      EpAppFunctionService epAppFunctionService) {
    this.fnRoleService = fnRoleService;
    this.epAppFunctionService = epAppFunctionService;
  }

  public String getFunctionCodeType(String roleFuncItem) {
    String type = null;
    if ((roleFuncItem.contains(FUNCTION_PIPE) && roleFuncItem.contains("menu"))
        || (!roleFuncItem.contains(FUNCTION_PIPE) && roleFuncItem.contains("menu"))) {
      type = "menu";
    } else if (checkIfCodeHasNoPipesAndHasTypeUrl(roleFuncItem) || checkIfCodeHasPipesAndHasTypeUrl(roleFuncItem)
        || checkIfCodeHasNoPipesAndHasNoTypeUrl(roleFuncItem)) {
      type = "url";
    } else if (roleFuncItem.contains(FUNCTION_PIPE)
        && (!roleFuncItem.contains("menu") || roleFuncItem.contains("url"))) {
      type = EcompPortalUtils.getFunctionType(roleFuncItem);
    }
    return type;
  }

  private boolean checkIfCodeHasNoPipesAndHasTypeUrl(String roleFuncItem) {
    return !roleFuncItem.contains(FUNCTION_PIPE) && roleFuncItem.contains("url");
  }

  private boolean checkIfCodeHasPipesAndHasTypeUrl(String roleFuncItem) {
    return roleFuncItem.contains(FUNCTION_PIPE) && roleFuncItem.contains("url");
  }

  private boolean checkIfCodeHasNoPipesAndHasNoTypeUrl(String roleFuncItem) {
    return !roleFuncItem.contains(FUNCTION_PIPE) && !roleFuncItem.contains("url");
  }

  public List<FnRole> getPortalAppRoleInfo(Long roleId) {
    return fnRoleService.retrieveAppRoleByRoleIdWhereAppIdIsNull(roleId);
  }

  public ResponseEntity<String> getUserRolesFromExtAuthSystem(String name, HttpEntity<String> getUserRolesEntity) {
    logger.debug(EELFLoggerDelegate.debugLogger, "Connecting to external system to get current user roles");
    ResponseEntity<String> getResponse = template
        .exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
            + "roles/user/" + name, HttpMethod.GET, getUserRolesEntity, String.class);
    if (getResponse.getStatusCode().value() == 200) {
      logger.debug(EELFLoggerDelegate.debugLogger,
          "getAllUserRoleFromExtAuthSystem: Finished GET user roles from external system and received user roles {}",
          getResponse.getBody());
    } else {
      logger.error(EELFLoggerDelegate.errorLogger,
          "getAllUserRoleFromExtAuthSystem: Failed GET user roles from external system and received user roles {}",
          getResponse.getBody());
      EPLogUtil.logExternalAuthAccessAlarm(logger, getResponse.getStatusCode());
    }
    return getResponse;
  }

  public Map<String, FnRole> getAppRoleNamesWithUnderscoreMap(FnApp app) {
    final Map<String, FnRole> currentRolesInDB = new HashMap<>();
    List<FnRole> getCurrentRoleList = null;
    final Map<String, Long> appParams = new HashMap<>();
    if (app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
      getCurrentRoleList = fnRoleService.retrieveAppRolesWhereAppIdIsNull();
    } else {
      appParams.put("appId", app.getId());
      getCurrentRoleList = fnRoleService.retrieveAppRolesByAppId(app.getId());
    }
    for (FnRole role : getCurrentRoleList) {
      currentRolesInDB.put(role.getRoleName()
          .replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"), role);
    }
    return currentRolesInDB;
  }

  public List<CentralV2Role> createCentralRoleObject(List<FnApp> app, List<FnRole> roleInfo,
      List<CentralV2Role> roleList) throws RoleFunctionException {
    for (FnRole role : roleInfo) {
      List<EpAppFunction> cenRoleFuncList = epAppFunctionService
          .getAppRoleFunctionList(role.getId(), app.get(0).getId());
      SortedSet<EpAppFunction> roleFunctionSet = new TreeSet<>();
      for (EpAppFunction roleFunc : cenRoleFuncList) {
        String functionCode = EcompPortalUtils.getFunctionCode(roleFunc.getFunctionCd());
        functionCode = EPUserUtils.decodeFunctionCode(functionCode);
        String type = getFunctionCodeType(roleFunc.getFunctionCd());
        String action = getFunctionCodeAction(roleFunc.getFunctionCd());
        EpAppFunction cenRoleFunc = new EpAppFunction(role.getId(), functionCode,
            roleFunc.getFunctionName(), null, type, action, null);
        roleFunctionSet.add(cenRoleFunc);
      }
      SortedSet<CentralV2Role> childRoles = new TreeSet<>();
      SortedSet<CentralV2Role> parentRoles = new TreeSet<>();
      CentralV2Role cenRole;
      if (role.getAppRoleId() == null) {
        cenRole = CentralV2Role.builder().id(role.getId()).created(role.getCreated())
            .modified(role.getModified()).createdId(role.getCreatedId().getUserId())
            .modifiedId(role.getModifiedId().getUserId()).rowNum(role.getRowNum()).name(role.getRoleName())
            .active(role.getActiveYn()).priority(role.getPriority()).roleFunctions(roleFunctionSet)
            .childRoles(childRoles).parentRoles(parentRoles).build();
      } else {
        cenRole = CentralV2Role.builder().id(role.getAppRoleId())
            .created(role.getCreated()).modified(role.getModified()).createdId(role.getCreatedId().getUserId())
            .modifiedId(role.getModifiedId().getUserId()).rowNum(role.getRowNum()).name(role.getRoleName())
            .active(role.getActiveYn()).priority(role.getPriority()).roleFunctions(roleFunctionSet)
            .childRoles(childRoles).parentRoles(parentRoles).build();
      }
      roleList.add(cenRole);
    }
    return roleList;
  }

  private String getFunctionCodeAction(String roleFuncItem) {
    return (!roleFuncItem.contains(FUNCTION_PIPE)) ? "*" : EcompPortalUtils.getFunctionAction(roleFuncItem);
  }

  public List<FnRole> getAppRoles(Long appId) {
    List<FnRole> applicationRoles;
    try {
      if (appId == 1) {
        applicationRoles = fnRoleService.retrieveAppRolesWhereAppIdIsNull();
      } else {
        applicationRoles = fnRoleService.retrieveAppRolesByAppId(appId);
      }
    } catch (Exception e) {
      logger.error(EELFLoggerDelegate.errorLogger, "getAppRoles: failed", e);
      throw e;
    }
    return applicationRoles;
  }
}
