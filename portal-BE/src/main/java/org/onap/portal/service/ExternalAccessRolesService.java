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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import javax.management.InvalidApplicationException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import org.json.JSONArray;
import org.json.JSONObject;
import org.onap.portal.domain.db.DomainVo;
import org.onap.portal.domain.db.ep.EpAppFunction;
import org.onap.portal.domain.db.ep.EpAppRoleFunction;
import org.onap.portal.domain.db.fn.FnApp;
import org.onap.portal.domain.db.fn.FnFunction;
import org.onap.portal.domain.db.fn.FnRole;
import org.onap.portal.domain.db.fn.FnRoleFunction;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.db.fn.FnUserRole;
import org.onap.portal.domain.dto.ecomp.EPAppRoleFunction;
import org.onap.portal.domain.dto.ecomp.EPUserAppRolesRequest;
import org.onap.portal.domain.dto.ecomp.ExternalRoleDetails;
import org.onap.portal.domain.dto.model.ExternalSystemUser;
import org.onap.portal.domain.dto.transport.BulkUploadRoleFunction;
import org.onap.portal.domain.dto.transport.BulkUploadUserRoles;
import org.onap.portal.domain.dto.transport.CentralApp;
import org.onap.portal.domain.dto.transport.CentralRole;
import org.onap.portal.domain.dto.transport.CentralRoleFunction;
import org.onap.portal.domain.dto.transport.CentralUser;
import org.onap.portal.domain.dto.transport.CentralUserApp;
import org.onap.portal.domain.dto.transport.CentralV2Role;
import org.onap.portal.domain.dto.transport.CentralV2User;
import org.onap.portal.domain.dto.transport.CentralV2UserApp;
import org.onap.portal.domain.dto.transport.EPUserAppCurrentRoles;
import org.onap.portal.domain.dto.transport.EcompUserRoles;
import org.onap.portal.domain.dto.transport.ExternalAccessPerms;
import org.onap.portal.domain.dto.transport.ExternalAccessPermsDetail;
import org.onap.portal.domain.dto.transport.ExternalAccessRole;
import org.onap.portal.domain.dto.transport.ExternalAccessRolePerms;
import org.onap.portal.domain.dto.transport.ExternalAccessUser;
import org.onap.portal.domain.dto.transport.ExternalRequestFieldsValidator;
import org.onap.portal.domain.dto.transport.GlobalRoleWithApplicationRoleFunction;
import org.onap.portal.domain.dto.transport.LocalRole;
import org.onap.portal.exception.DeleteDomainObjectFailedException;
import org.onap.portal.exception.ExternalAuthSystemException;
import org.onap.portal.exception.InactiveApplicationException;
import org.onap.portal.exception.InvalidUserException;
import org.onap.portal.exception.RoleFunctionException;
import org.onap.portal.logging.logic.EPLogUtil;
import org.onap.portal.service.app.FnAppService;
import org.onap.portal.service.appFunction.EpAppFunctionService;
import org.onap.portal.service.appRoleFunction.EpAppRoleFunctionService;
import org.onap.portal.service.role.FnRoleService;
import org.onap.portal.service.roleFunction.FnRoleFunctionService;
import org.onap.portal.service.user.FnUserService;
import org.onap.portal.utils.EPCommonSystemProperties;
import org.onap.portal.utils.EPUserUtils;
import org.onap.portal.utils.EcompPortalUtils;
import org.onap.portal.utils.PortalConstants;
import org.onap.portalsdk.core.domain.Role;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.restful.domain.EcompRole;
import org.onap.portalsdk.core.restful.domain.EcompRoleFunction;
import org.onap.portalsdk.core.restful.domain.EcompUser;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@SuppressWarnings("unchecked")
@Service
public class ExternalAccessRolesService {

    private static final String APP_ROLE_NAME_PARAM = "appRoleName";
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

    private static final String GET_GLOBAL_ROLE_WITH_APPLICATION_ROLE_FUNCTIONS = "select"
        + "  distinct d.roleId as roleId,"
        + "  d.roleName as roleName,"
        + "  d.activeYn as active,"
        + "  d.priority as priority,"
        + "  c.epAppFunction.functionCd as functionCd,"
        + "  e.functionName as functionName,"
        + "  c.epAppFunction.appId as appId,"
        + "  c.roleAppId as roleAppId"
        + " from"
        + "  FnUserRole a,"
        + "  FnApp b,"
        + "  EpAppRoleFunction c,"
        + "  FnRole d,"
        + "  EpAppFunction e"
        + " where"
        + "  b.appId = c.appId.appId"
        + "  and a.appId = c.roleAppId"
        + "  and b.enabled = 'Y'"
        + "  and c.fnRole.roleId = d.roleId"
        + "  and d.activeYn = 'Y'"
        + "  and e.functionCd = c.epAppFunction.functionCd"
        + "  and c.appId.appId = :appId"
        + "  and e.appId.appId = c.appId.appId";

    private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(ExternalAccessRolesService.class);
    private final RestTemplate template = new RestTemplate();

    private final FnUserService fnUserService;
    private final FnRoleService fnRoleService;
    private final FnAppService fnAppService;
    private final EntityManager entityManager;
    private final FnRoleFunctionService fnRoleFunctionService;
    private final EpAppFunctionService epAppFunctionService;
    private final EpAppRoleFunctionService epAppRoleFunctionService;
    private final LocalRoleService localRoleService;
    private final BulkUploadUserRolesService bulkUploadUserRolesService;

    @Autowired
    public ExternalAccessRolesService(
        final FnUserService fnUserService,
        final FnRoleService fnRoleService,
        final FnAppService fnAppService, EntityManager entityManager,
        FnRoleFunctionService fnRoleFunctionService,
        final EpAppFunctionService epAppFunctionService,
        final EpAppRoleFunctionService epAppRoleFunctionService,
        final LocalRoleService localRoleService,
        BulkUploadUserRolesService bulkUploadUserRolesService) {
        this.fnUserService = fnUserService;
        this.fnRoleService = fnRoleService;
        this.fnAppService = fnAppService;
        this.entityManager = entityManager;
        this.fnRoleFunctionService = fnRoleFunctionService;
        this.epAppFunctionService = epAppFunctionService;
        this.epAppRoleFunctionService = epAppRoleFunctionService;
        this.localRoleService = localRoleService;
        this.bulkUploadUserRolesService = bulkUploadUserRolesService;
    }

    String getFunctionCodeType(String roleFuncItem) {
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

    List<FnRole> getPortalAppRoleInfo(Long roleId) {
        return fnRoleService.retrieveAppRoleByRoleIdWhereAppIdIsNull(roleId);
    }

    ResponseEntity<String> getUserRolesFromExtAuthSystem(String name, HttpEntity<String> getUserRolesEntity) {
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

    Map<String, FnRole> getAppRoleNamesWithUnderscoreMap(FnApp app) {
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

    List<CentralV2Role> createCentralRoleObject(List<FnApp> app, List<FnRole> roleInfo,
        List<CentralV2Role> roleList) throws RoleFunctionException {
        for (FnRole role : roleInfo) {
            List<EpAppFunction> cenRoleFuncList = epAppFunctionService
                .getAppRoleFunctionList(role.getId(), app.get(0).getId());
            SortedSet<DomainVo> roleFunctionSet = new TreeSet<>();
            for (EpAppFunction roleFunc : cenRoleFuncList) {
                String functionCode = EcompPortalUtils.getFunctionCode(roleFunc.getFunctionCd());
                functionCode = EPUserUtils.decodeFunctionCode(functionCode);
                String type = getFunctionCodeType(roleFunc.getFunctionCd());
                String action = getFunctionCodeAction(roleFunc.getFunctionCd());
                FnRoleFunction cenRoleFunc = new FnRoleFunction(role,
                    FnFunction.builder().code(functionCode).name(roleFunc.getFunctionName()).type(type).action(action)
                        .build());
            }
            SortedSet<CentralV2Role> childRoles = new TreeSet<>();
            SortedSet<CentralV2Role> parentRoles = new TreeSet<>();
            CentralV2Role cenRole;
            if (role.getAppRoleId() == null) {
                cenRole = CentralV2Role.builder().id(role.getId()).created(role.getCreated())
                    .modified(role.getModified())
                    .rowNum(role.getRowNum()).name(role.getRoleName())
                    .active(role.getActiveYn()).priority(role.getPriority()).roleFunctions(roleFunctionSet)
                    .childRoles(childRoles).parentRoles(parentRoles).build();
            } else {
                cenRole = CentralV2Role.builder().id(role.getAppRoleId())
                    .created(role.getCreated()).modified(role.getModified())
                    .rowNum(role.getRowNum()).name(role.getRoleName())
                    .active(role.getActiveYn()).priority(role.getPriority()).roleFunctions(roleFunctionSet)
                    .childRoles(childRoles).parentRoles(parentRoles).build();
            }
            roleList.add(cenRole);
        }
        return roleList;
    }

    String getFunctionCodeAction(String roleFuncItem) {
        return (!roleFuncItem.contains(FUNCTION_PIPE)) ? "*" : EcompPortalUtils.getFunctionAction(roleFuncItem);
    }

    public List<CentralV2Role> getRolesForApp(String uebkey) throws Exception {
        logger.debug(EELFLoggerDelegate.debugLogger, "getRolesForApp: Entering into getRolesForApp");
        List<CentralV2Role> roleList = new ArrayList<>();
        try {
            List<FnApp> app = fnAppService.getByUebKey(uebkey);
            List<FnRole> appRolesList = fnRoleService.getAppRoles(app.get(0).getId());
            roleList = createCentralRoleObject(app, appRolesList, roleList);
            if (!Objects.equals(app.get(0).getId(), PortalConstants.PORTAL_APP_ID)) {
                List<CentralV2Role> globalRoleList = getGlobalRolesOfApplication(app.get(0).getId());
                List<FnRole> globalRolesList = fnRoleService.getGlobalRolesOfPortal();
                List<CentralV2Role> portalsGlobalRolesFinlaList = new ArrayList<>();
                if (!globalRolesList.isEmpty()) {
                    for (FnRole eprole : globalRolesList) {
                        CentralV2Role cenRole = convertRoleToCentralV2Role(eprole);
                        portalsGlobalRolesFinlaList.add(cenRole);
                    }
                    roleList.addAll(globalRoleList);
                    for (CentralV2Role role : portalsGlobalRolesFinlaList) {
                        CentralV2Role result = roleList.stream().filter(x -> role.getId().equals(x.getId())).findAny()
                            .orElse(null);
                        if (result == null) {
                            roleList.add(role);
                        }
                    }
                } else {
                    for (FnRole role : globalRolesList) {
                        CentralV2Role cenRole = convertRoleToCentralV2Role(role);
                        roleList.add(cenRole);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "getRolesForApp: Failed!", e);
            throw e;
        }
        logger.debug(EELFLoggerDelegate.debugLogger, "getRolesForApp: Finished!");
        return roleList.stream().distinct().collect(Collectors.toList());
    }

    private List<CentralV2Role> getGlobalRolesOfApplication(final Long appId) {
        List<GlobalRoleWithApplicationRoleFunction> globalRoles = new ArrayList<>();
        try {
            List<Tuple> tuples = entityManager.createQuery(GET_GLOBAL_ROLE_WITH_APPLICATION_ROLE_FUNCTIONS, Tuple.class)
                .setParameter("appId", appId)
                .getResultList();
            globalRoles = tuples.stream().map(this::tupleToGlobalRoleWithApplicationRoleFunction)
                .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "getCentralizedAppsOfUser failed", e);
        }
        List<CentralV2Role> roleList = new ArrayList<>();
        if (globalRoles.size() > 0) {
            roleList = finalListOfCentralRoles(globalRoles);
        }
        return roleList;
    }

    private GlobalRoleWithApplicationRoleFunction tupleToGlobalRoleWithApplicationRoleFunction(Tuple tuple) {
        return GlobalRoleWithApplicationRoleFunction.builder().roleId((Long) tuple.get("roleId"))
            .roleName((String) tuple.get("roleName"))
            .functionCd((String) tuple.get("functionCd")).functionName((String) tuple.get("functionName"))
            .active((Boolean) tuple.get("active")).priority((Integer) tuple.get("priority"))
            .appId((Long) tuple.get("appId")).roleAppId((Long) tuple.get("roleAppId")).build();
    }

    private List<CentralV2Role> finalListOfCentralRoles(List<GlobalRoleWithApplicationRoleFunction> globalRoles) {
        List<CentralV2Role> rolesfinalList = new ArrayList<>();
        for (GlobalRoleWithApplicationRoleFunction role : globalRoles) {
            boolean found = false;
            for (CentralV2Role cenRole : rolesfinalList) {
                if (role.getRoleId().equals(cenRole.getId())) {
                    SortedSet<DomainVo> roleFunctions = new TreeSet<>();
                    for (DomainVo vo : cenRole.getRoleFunctions()) {
                        Optional<FnRoleFunction> roleFunction = fnRoleFunctionService.findById(vo.getId());
                        roleFunction.ifPresent(roleFunctions::add);
                    }
                    FnRoleFunction cenRoleFun = createCentralRoleFunctionForGlobalRole(role);
                    roleFunctions.add(cenRoleFun);
                    cenRole.setRoleFunctions(roleFunctions);
                    found = true;
                    break;
                }
            }
            if (!found) {
                CentralV2Role cenrole = new CentralV2Role();
                cenrole.setName(role.getRoleName());
                cenrole.setId(role.getRoleId());
                cenrole.setActive(role.getActive());
                cenrole.setPriority(role.getPriority());
                SortedSet<DomainVo> roleFunctions = new TreeSet<>();
                FnRoleFunction cenRoleFun = createCentralRoleFunctionForGlobalRole(role);
                roleFunctions.add(cenRoleFun);
                cenrole.setRoleFunctions(roleFunctions);
                rolesfinalList.add(cenrole);
            }
        }
        return rolesfinalList;
    }

    public String getV2UserWithRoles(String loginId, String uebkey) throws Exception {
        final Map<String, String> params = new HashMap<>();
        FnUser user = null;
        CentralV2User cenV2User = null;
        String result = null;
        try {
            params.put("orgUserIdValue", loginId);
            List<FnApp> appList = getApp(uebkey);
            if (!appList.isEmpty()) {
                user = fnUserService.loadUserByUsername(loginId);
                ObjectMapper mapper = new ObjectMapper();
                cenV2User = getV2UserAppRoles(loginId, uebkey);
                result = mapper.writeValueAsString(cenV2User);
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "getUser: failed", e);
            throw e;
        }
        return result;
    }

    public void syncApplicationRolesWithEcompDB(FnApp app) {
        try {
            logger.debug(EELFLoggerDelegate.debugLogger, "syncRoleFunctionFromExternalAccessSystem: Started");
            // Sync functions and roles assigned to it which also creates new roles if does
            // not exits in portal
            syncRoleFunctionFromExternalAccessSystem(app);
            logger.debug(EELFLoggerDelegate.debugLogger, "syncRoleFunctionFromExternalAccessSystem: Finished");
            ObjectMapper mapper = new ObjectMapper();
            logger.debug(EELFLoggerDelegate.debugLogger, "Entering to getAppRolesJSONFromExtAuthSystem");
            // Get Permissions from External Auth System
            JSONArray extRole = getAppRolesJSONFromExtAuthSystem(app.getId(), app.getAuthNamespace());
            logger.debug(EELFLoggerDelegate.debugLogger, "Entering into getExternalRoleDetailsList");
            // refactoring done
            List<ExternalRoleDetails> externalRoleDetailsList = getExternalRoleDetailsList(app, mapper, extRole);
            List<FnRole> finalRoleList = new ArrayList<>();
            for (ExternalRoleDetails externalRole : externalRoleDetailsList) {
                FnRole ecompRole = convertExternalRoleDetailsToEpRole(externalRole);
                finalRoleList.add(ecompRole);
            }
            List<FnRole> applicationRolesList;
            applicationRolesList = getAppRoles(app.getId());
            List<String> applicationRoleIdList = new ArrayList<>();
            for (FnRole applicationRole : applicationRolesList) {
                applicationRoleIdList.add(applicationRole.getRoleName());
            }
            List<FnRole> roleListToBeAddInEcompDB = new ArrayList<>();
            for (FnRole aafRole : finalRoleList) {
                if (!applicationRoleIdList.contains(aafRole.getRoleName())) {
                    roleListToBeAddInEcompDB.add(aafRole);
                }
            }
            logger.debug(EELFLoggerDelegate.debugLogger, "Entering into inactiveRolesNotInExternalAuthSystem");
            // Check if roles exits in external Access system and if not make inactive in DB
            inactiveRolesNotInExternalAuthSystem(app.getId(), finalRoleList, applicationRolesList);
            logger.debug(EELFLoggerDelegate.debugLogger, "Entering into addNewRoleInEcompDBUpdateDescInExtAuthSystem");
            // Add new roles in DB and updates role description in External Auth System
            addNewRoleInEcompDBUpdateDescInExtAuthSystem(app, roleListToBeAddInEcompDB);
            logger.debug(EELFLoggerDelegate.debugLogger, "syncApplicationRolesWithEcompDB: Finished");
        } catch (HttpClientErrorException e) {
            logger.error(EELFLoggerDelegate.errorLogger,
                "syncApplicationRolesWithEcompDB: Failed due to the External Auth System", e);
            EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "syncApplicationRolesWithEcompDB: Failed ", e);
        }
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

    private FnRole convertExternalRoleDetailsToEpRole(ExternalRoleDetails externalRoleDetails) {
        FnRole role = new FnRole();
        role.setActiveYn(true);
        role.setAppId(externalRoleDetails.getAppId());
        role.setAppRoleId(externalRoleDetails.getAppRoleId());
        role.setRoleName(externalRoleDetails.getName());
        role.setPriority(externalRoleDetails.getPriority());
        return role;
    }

    public List<ExternalRoleDetails> getExternalRoleDetailsList(FnApp app, ObjectMapper mapper, JSONArray extRole)
        throws IOException {
        List<ExternalRoleDetails> externalRoleDetailsList = new ArrayList<>();
        ExternalAccessPerms externalAccessPerms;
        List<String> functionCodelist = new ArrayList<>();
        Map<String, FnRole> curRolesMap = getAppRoleNamesMap(app.getId());
        Map<String, FnRole> curRolesUnderscoreMap = getAppRoleNamesWithUnderscoreMap(app);
        for (int i = 0; i < extRole.length(); i++) {
            ExternalRoleDetails externalRoleDetail = new ExternalRoleDetails();
            EPAppRoleFunction ePAppRoleFunction = new EPAppRoleFunction();
            JSONObject Role = (JSONObject) extRole.get(i);
            String name = extRole.getJSONObject(i).getString(ROLE_NAME);
            String actualRoleName = name.substring(app.getAuthNamespace().length() + 1);
            if (extRole.getJSONObject(i).has(EXTERNAL_AUTH_ROLE_DESCRIPTION)) {
                actualRoleName = extRole.getJSONObject(i).getString(EXTERNAL_AUTH_ROLE_DESCRIPTION);
            }
            SortedSet<ExternalAccessPerms> externalAccessPermsOfRole = new TreeSet<>();
            if (extRole.getJSONObject(i).has(EXTERNAL_AUTH_PERMS)) {
                JSONArray extPerm = (JSONArray) Role.get(EXTERNAL_AUTH_PERMS);
                for (int j = 0; j < extPerm.length(); j++) {
                    JSONObject perms = extPerm.getJSONObject(j);
                    boolean isNamespaceMatching = EcompPortalUtils.checkNameSpaceMatching(perms.getString("type"),
                        app.getAuthNamespace());
                    if (isNamespaceMatching) {
                        externalAccessPerms = new ExternalAccessPerms(perms.getString("type"),
                            perms.getString("instance"), perms.getString("action"));
                        ePAppRoleFunction.setCode(externalAccessPerms.getInstance());
                        functionCodelist.add(ePAppRoleFunction.getCode());
                        externalAccessPermsOfRole.add(externalAccessPerms);
                    }
                }
            }
            externalRoleDetail.setActive(true);
            externalRoleDetail.setName(actualRoleName);
            if (app.getId() == 1) {
                externalRoleDetail.setAppId(null);
            } else {
                externalRoleDetail.setAppId(app.getId());
            }
            FnRole currRole = null;
            currRole = (!extRole.getJSONObject(i).has(EXTERNAL_AUTH_ROLE_DESCRIPTION))
                ? curRolesUnderscoreMap.get(actualRoleName)
                : curRolesMap.get(actualRoleName);
            Long roleId = null;
            if (currRole != null) {
                roleId = currRole.getId();
            }
            final Map<String, EpAppRoleFunction> roleFunctionsMap = new HashMap<>();
            if (roleId != null) {
                List<EpAppRoleFunction> appRoleFunctions = epAppRoleFunctionService
                    .getAppRoleFunctionOnRoleIdAndAppId(app.getId(), roleId);
                if (!appRoleFunctions.isEmpty()) {
                    for (EpAppRoleFunction roleFunc : appRoleFunctions) {
                        roleFunctionsMap.put(roleFunc.getEpAppFunction().getFunctionCd(), roleFunc);
                    }
                }
            }
            if (!externalAccessPermsOfRole.isEmpty()) {
                // Adding functions to role
                for (ExternalAccessPerms externalpermission : externalAccessPermsOfRole) {
                    EpAppRoleFunction checkRoleFunctionExits = roleFunctionsMap.get(externalpermission.getInstance());
                    if (checkRoleFunctionExits == null) {
                        String funcCode = externalpermission.getType().substring(app.getAuthNamespace().length() + 1)
                            + FUNCTION_PIPE + externalpermission.getInstance() + FUNCTION_PIPE
                            + externalpermission.getAction();
                        EpAppRoleFunction checkRoleFunctionPipeExits = roleFunctionsMap.get(funcCode);
                        if (checkRoleFunctionPipeExits == null) {
                            try {
                                logger.debug(EELFLoggerDelegate.debugLogger,
                                    "SyncApplicationRolesWithEcompDB: Adding function to the role: {}",
                                    externalpermission.getInstance());
                                List<EpAppFunction> roleFunction = epAppFunctionService
                                    .getAppFunctionOnCodeAndAppId(app.getId(), externalpermission.getInstance());
                                if (roleFunction.isEmpty()) {
                                    roleFunction = epAppFunctionService
                                        .getAppFunctionOnCodeAndAppId(app.getId(), funcCode);
                                }
                                if (!roleFunction.isEmpty()) {
                                    EpAppRoleFunction apRoleFunction = new EpAppRoleFunction();
                                    apRoleFunction.setAppId(app);
                                    apRoleFunction.setFnRole(currRole);
                                    apRoleFunction.setEpAppFunction(roleFunction.get(0));
                                    epAppRoleFunctionService.save(apRoleFunction);
                                }
                            } catch (Exception e) {
                                logger.error(EELFLoggerDelegate.errorLogger,
                                    "SyncApplicationRolesWithEcompDB: Failed to add role function", e);
                            }
                        }
                    }
                }
            }
            externalRoleDetailsList.add(externalRoleDetail);
        }
        return externalRoleDetailsList;
    }

    private Map<String, FnRole> getAppRoleNamesMap(final Long appId) {
        final Map<String, FnRole> currentRolesInDB = new HashMap<>();
        List<FnRole> getCurrentRoleList = null;
        final Map<String, Long> appParams = new HashMap<>();
        if (appId.equals(PortalConstants.PORTAL_APP_ID)) {
            getCurrentRoleList = fnRoleService.retrieveAppRolesWhereAppIdIsNull();
        } else {
            getCurrentRoleList = fnRoleService.retrieveAppRolesByAppId(appId);
        }
        for (FnRole role : getCurrentRoleList) {
            currentRolesInDB.put(role.getRoleName(), role);
        }
        return currentRolesInDB;
    }

    public JSONArray getAppRolesJSONFromExtAuthSystem(final long appId, final String authNamespace) throws Exception {
        ResponseEntity<String> response = null;
        HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        logger.debug(EELFLoggerDelegate.debugLogger, "syncApplicationRolesWithEcompDB: {} ",
            CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE);
        response = template.exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
            + "roles/ns/" + authNamespace, HttpMethod.GET, entity, String.class);
        String res = response.getBody();
        logger.debug(EELFLoggerDelegate.debugLogger,
            "syncApplicationRolesWithEcompDB: Finished GET roles from External Auth system and the result is :",
            res);
        JSONObject jsonObj = new JSONObject(res);
        JSONArray extRole = jsonObj.getJSONArray("role");
        for (int i = 0; i < extRole.length(); i++) {
            if (extRole.getJSONObject(i).getString(ROLE_NAME).equals(authNamespace + ADMIN)
                || extRole.getJSONObject(i).getString(ROLE_NAME).equals(authNamespace + OWNER)
                || (extRole.getJSONObject(i).getString(ROLE_NAME).equals(authNamespace + ACCOUNT_ADMINISTRATOR)
                && !(appId == PortalConstants.PORTAL_APP_ID))) {
                extRole.remove(i);
                i--;
            }
        }
        return extRole;
    }

    private void addNewRoleInEcompDBUpdateDescInExtAuthSystem(FnApp app, List<FnRole> roleListToBeAddInEcompDB) {
        FnRole roleToBeAddedInEcompDB;
        for (FnRole fnRole : roleListToBeAddInEcompDB) {
            try {
                roleToBeAddedInEcompDB = fnRole;
                if (app.getId() == 1) {
                    roleToBeAddedInEcompDB.setAppRoleId(null);
                }
                fnRoleService.saveOne(roleToBeAddedInEcompDB);
                List<FnRole> getRoleCreatedInSync = null;
                if (!app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
                    getRoleCreatedInSync = fnRoleService
                        .retrieveAppRolesByRoleNameAndByAppId(roleToBeAddedInEcompDB.getRoleName(), app.getId());
                    FnRole epUpdateRole = getRoleCreatedInSync.get(0);
                    epUpdateRole.setAppRoleId(epUpdateRole.getId());
                    fnRoleService.saveOne(epUpdateRole);
                }
                List<FnRole> roleList;
                final Map<String, String> params = new HashMap<>();
                params.put(APP_ROLE_NAME_PARAM, roleToBeAddedInEcompDB.getRoleName());
                boolean isPortalRole;
                if (app.getId() == 1) {
                    isPortalRole = true;
                    roleList = fnRoleService
                        .retrieveAppRolesByRoleNameAndWhereAppIdIsNull(roleToBeAddedInEcompDB.getRoleName());
                } else {
                    isPortalRole = false;
                    roleList = fnRoleService
                        .retrieveAppRolesByRoleNameAndByAppId(roleToBeAddedInEcompDB.getRoleName(), app.getId());
                }
                FnRole role = roleList.get(0);
                Role aaFrole = new Role();
                aaFrole.setId(role.getId());
                aaFrole.setActive(role.getActiveYn());
                aaFrole.setPriority(role.getPriority());
                aaFrole.setName(role.getRoleName());
                updateRoleInExternalSystem(aaFrole, app, isPortalRole);
            } catch (Exception e) {
                logger.error(EELFLoggerDelegate.errorLogger,
                    "SyncApplicationRolesWithEcompDB: Failed to add or update role in external auth system", e);
            }
        }
    }

    private void updateRoleInExternalSystem(Role updateExtRole, FnApp app, boolean isGlobalRole) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<String> deleteResponse = null;
        List<FnRole> epRoleList = null;
        if (app.getId().equals(PortalConstants.PORTAL_APP_ID)
            || (isGlobalRole && !app.getId().equals(PortalConstants.PORTAL_APP_ID))) {
            epRoleList = getPortalAppRoleInfo(updateExtRole.getId());
        } else {
            epRoleList = getPartnerAppRoleInfo(updateExtRole.getId(), app.getId());
        }
        // Assigning functions to global role
        if ((isGlobalRole && !app.getId().equals(PortalConstants.PORTAL_APP_ID))) {
            List<FnFunction> functions = new ArrayList<>();
            for (FnRoleFunction roleFunction : convertSetToListOfRoleFunctions(updateExtRole)) {
                functions.add(roleFunction.getFunctionCd());
            }
            // TODO HARDCODED ID
            FnApp portalAppInfo = fnAppService.getById(PortalConstants.PORTAL_APP_ID);
            addFunctionsTOGlobalRole(epRoleList, updateExtRole, functions, mapper, app, portalAppInfo);
        } else {
            String appRole = getSingleAppRole(epRoleList.get(0).getRoleName(), app);
            List<FnRoleFunction> roleFunctionListNew = convertSetToListOfRoleFunctions(updateExtRole);
            if (!appRole.equals(IS_EMPTY_JSON_STRING)) {
                JSONObject jsonObj = new JSONObject(appRole);
                JSONArray extRole = jsonObj.getJSONArray("role");
                if (!extRole.getJSONObject(0).has(EXTERNAL_AUTH_ROLE_DESCRIPTION)) {
                    String roleName = extRole.getJSONObject(0).getString(ROLE_NAME);
                    Map<String, String> delRoleKeyMapper = new HashMap<>();
                    delRoleKeyMapper.put(ROLE_NAME, roleName);
                    String delRoleKeyValue = mapper.writeValueAsString(delRoleKeyMapper);
                    deleteResponse = deleteRoleInExternalSystem(delRoleKeyValue);
                    if (deleteResponse.getStatusCode().value() != 200) {
                        throw new ExternalAuthSystemException(deleteResponse.getBody());
                    }
                    addRole(updateExtRole, app.getUebKey());
                } else {
                    String desc = extRole.getJSONObject(0).getString(EXTERNAL_AUTH_ROLE_DESCRIPTION);
                    String name = extRole.getJSONObject(0).getString(ROLE_NAME);
                    List<ExternalAccessPerms> list = new ArrayList<>();
                    if (extRole.getJSONObject(0).has(EXTERNAL_AUTH_PERMS)) {
                        JSONArray perms = extRole.getJSONObject(0).getJSONArray(EXTERNAL_AUTH_PERMS);
                        list = mapper.readValue(perms.toString(), TypeFactory.defaultInstance()
                            .constructCollectionType(List.class, ExternalAccessPerms.class));
                    }
                    // If role name or role functions are updated then delete
                    // record in External System and add new record to avoid
                    // conflicts
                    boolean isRoleNameChanged = false;
                    if (!desc.equals(updateExtRole.getName())) {
                        isRoleNameChanged = true;
                        deleteRoleInExtSystem(mapper, name);
                        addRole(updateExtRole, app.getUebKey());
                        // add partner functions to the global role in External
                        // Auth System
                        if (!list.isEmpty() && isGlobalRole) {
                            addPartnerHasRoleFunctionsToGlobalRole(list, mapper, app, updateExtRole);
                        }
                        list.removeIf(
                            perm -> EcompPortalUtils.checkNameSpaceMatching(perm.getType(), app.getAuthNamespace()));
                        // if role name is changes please ignore the previous
                        // functions in External Auth
                        // and update with user requested functions
                        addRemoveFunctionsToRole(updateExtRole, app, mapper, roleFunctionListNew, name, list);
                    }
                    // Delete role in External System if role is inactive
                    if (!updateExtRole.getActive()) {
                        deleteRoleInExtSystem(mapper, name);
                    }
                    if (!isRoleNameChanged) {
                        addRemoveFunctionsToRole(updateExtRole, app, mapper, roleFunctionListNew, name,
                            list);
                    }
                }
            } else {
                // It seems like role exists in local DB but not in External
                // Access system
                if (updateExtRole.getActive()) {
                    addRole(updateExtRole, app.getUebKey());
                    ExternalAccessRolePerms extAddRolePerms = null;
                    ExternalAccessPerms extAddPerms = null;
                    List<FnRoleFunction> roleFunctionListAdd = convertSetToListOfRoleFunctions(updateExtRole);
                    HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
                    for (FnRoleFunction roleFunc : roleFunctionListAdd) {
                        extAddPerms = new ExternalAccessPerms(
                            app.getAuthNamespace() + "." + roleFunc.getFunctionCd().getType(),
                            roleFunc.getFunctionCd().getCode(), roleFunc.getFunctionCd().getAction());
                        extAddRolePerms = new ExternalAccessRolePerms(extAddPerms,
                            app.getAuthNamespace() + "." + updateExtRole.getName().replaceAll(
                                EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"));
                        addRoleFuncExtSysRestAPI(mapper, extAddRolePerms, headers);
                    }
                }
            }
        }
    }

    private void addRemoveFunctionsToRole(Role updateExtRole, FnApp app, ObjectMapper mapper,
        List<FnRoleFunction> fnRoleFunctions, String name, List<ExternalAccessPerms> list) throws Exception {
        boolean response;
        List<FnFunction> roleFunctionListNew = new ArrayList<>();
        for (FnRoleFunction roleFunction : fnRoleFunctions) {
            roleFunctionListNew.add(roleFunction.getFunctionCd());
        }
        Map<String, FnFunction> updateRoleFunc = new HashMap<>();
        for (FnFunction addPerm : roleFunctionListNew) {
            updateRoleFunc.put(addPerm.getCode(), addPerm);
        }
        final Map<String, ExternalAccessPerms> extRolePermMap = new HashMap<>();
        final Map<String, ExternalAccessPerms> extRolePermMapPipes = new HashMap<>();
        list.removeIf(perm -> !EcompPortalUtils.checkNameSpaceMatching(perm.getType(), app.getAuthNamespace()));
        // Update permissions in the ExternalAccess System
        HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
        if (!list.isEmpty()) {
            for (ExternalAccessPerms perm : list) {
                FnFunction roleFunc = updateRoleFunc.get(perm.getType().substring(app.getAuthNamespace().length() + 1)
                    + FUNCTION_PIPE + perm.getInstance() + FUNCTION_PIPE + perm.getAction());
                if (roleFunc == null) {
                    FnFunction roleFuncPipeFilter = updateRoleFunc.get(perm.getInstance());
                    if (roleFuncPipeFilter == null) {
                        removePermForRole(perm, mapper, name, headers);
                    }
                }
                extRolePermMap.put(perm.getInstance(), perm);
                extRolePermMapPipes.put(perm.getType().substring(app.getAuthNamespace().length() + 1) + FUNCTION_PIPE
                    + perm.getInstance() + FUNCTION_PIPE + perm.getAction(), perm);
            }
        }
        response = true;
        if (!roleFunctionListNew.isEmpty()) {
            for (FnFunction roleFunc : roleFunctionListNew) {
                if (roleFunc.getCode().contains(FUNCTION_PIPE)) {
                    ExternalAccessPerms perm = extRolePermMapPipes.get(roleFunc.getCode());
                    if (perm == null) {
                        response = addFunctionsToRoleInExternalAuthSystem(updateExtRole, app, mapper, headers,
                            roleFunc);
                    }
                } else {
                    if (!extRolePermMap.containsKey(EcompPortalUtils.getFunctionCode(roleFunc.getCode()))) {
                        response = addFunctionsToRoleInExternalAuthSystem(updateExtRole, app, mapper, headers,
                            roleFunc);
                    }
                }
            }
        }
    }

    private boolean addFunctionsToRoleInExternalAuthSystem(Role updateExtRole, FnApp app, ObjectMapper mapper,
        HttpHeaders headers, FnFunction roleFunc) throws JsonProcessingException {
        boolean response;
        ExternalAccessRolePerms extRolePerms;
        ExternalAccessPerms extPerms;
        String code;
        String type;
        String action;
        if (roleFunc.getCode().contains(FUNCTION_PIPE)) {
            code = EcompPortalUtils.getFunctionCode(roleFunc.getCode());
            type = EcompPortalUtils.getFunctionType(roleFunc.getCode());
            action = getFunctionCodeAction(roleFunc.getCode());
        } else {
            code = roleFunc.getCode();
            type = roleFunc.getCode().contains("menu") ? "menu" : "url";
            action = "*";
        }
        extPerms = new ExternalAccessPerms(app.getAuthNamespace() + "." + type, code, action);
        extRolePerms = new ExternalAccessRolePerms(extPerms, app.getAuthNamespace() + "." + updateExtRole.getName()
            .replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"));
        String updateRolePerms = mapper.writeValueAsString(extRolePerms);
        HttpEntity<String> entity = new HttpEntity<>(updateRolePerms, headers);
        logger.debug(EELFLoggerDelegate.debugLogger, "updateRoleInExternalSystem: {} for POST: {}",
            CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE, updateRolePerms);
        ResponseEntity<String> addResponse = template.exchange(
            SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "role/perm",
            HttpMethod.POST, entity, String.class);
        if (addResponse.getStatusCode().value() != 201 && addResponse.getStatusCode().value() != 409) {
            response = false;
            logger.debug(EELFLoggerDelegate.debugLogger,
                "updateRoleInExternalSystem: Connected to External Auth system but something went wrong! due to {} and statuscode: {}",
                addResponse.getStatusCode().getReasonPhrase(), addResponse.getStatusCode().value());
        } else {
            response = true;
            logger.debug(EELFLoggerDelegate.debugLogger,
                "updateRoleInExternalSystem: Finished adding permissions to roles in External Auth system {} and status code: {} ",
                updateRolePerms, addResponse.getStatusCode().value());
        }
        return response;
    }

    private void addRoleFuncExtSysRestAPI(ObjectMapper addPermsMapper, ExternalAccessRolePerms extAddRolePerms,
        HttpHeaders headers) throws JsonProcessingException {
        boolean response;
        String updateRolePerms = addPermsMapper.writeValueAsString(extAddRolePerms);
        HttpEntity<String> entity = new HttpEntity<>(updateRolePerms, headers);
        logger.debug(EELFLoggerDelegate.debugLogger, "addRoleFunctionsInExternalSystem: {} for POST: {} ",
            CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE, updateRolePerms);
        ResponseEntity<String> addResponse = template.exchange(
            SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "role/perm",
            HttpMethod.POST, entity, String.class);
        if (addResponse.getStatusCode().value() != 201 && addResponse.getStatusCode().value() != 409) {
            response = false;
            logger.debug(EELFLoggerDelegate.debugLogger,
                "addRoleFunctionsInExternalSystem: While adding permission to the role in  External Auth system something went wrong! due to {} and statuscode: {}",
                addResponse.getStatusCode().getReasonPhrase(), addResponse.getStatusCode().value());
        } else {
            response = true;
            logger.debug(EELFLoggerDelegate.debugLogger,
                "addRoleFunctionsInExternalSystem: Finished adding permissions to roles in External Auth system {} and status code: {} ",
                updateRolePerms, addResponse.getStatusCode().value());
        }
    }

    private void addPartnerHasRoleFunctionsToGlobalRole(List<ExternalAccessPerms> permslist, ObjectMapper mapper,
        FnApp app, Role updateExtRole) throws Exception {
        for (ExternalAccessPerms perm : permslist) {
            if (!EcompPortalUtils.checkNameSpaceMatching(perm.getType(), app.getAuthNamespace())) {
                ExternalAccessRolePerms extAddGlobalRolePerms = null;
                ExternalAccessPerms extAddPerms = null;
                extAddPerms = new ExternalAccessPerms(perm.getType(), perm.getInstance(), perm.getAction());
                extAddGlobalRolePerms = new ExternalAccessRolePerms(extAddPerms,
                    app.getAuthNamespace() + "." + updateExtRole.getName().replaceAll(
                        EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"));
                String addPerms = mapper.writeValueAsString(extAddGlobalRolePerms);
                HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
                HttpEntity<String> entity = new HttpEntity<>(addPerms, headers);
                logger.debug(EELFLoggerDelegate.debugLogger, "addPartnerHasRoleFunctionsToGlobalRole: {} ",
                    CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE);
                try {
                    ResponseEntity<String> addResponse = template
                        .exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
                            + "role/perm", HttpMethod.POST, entity, String.class);
                    if (addResponse.getStatusCode().value() != 201) {
                        logger.debug(EELFLoggerDelegate.debugLogger,
                            "addPartnerHasRoleFunctionsToGlobalRole: While adding permission to the role in  External Auth system something went wrong! due to {} and statuscode: {}",
                            addResponse.getStatusCode().getReasonPhrase(), addResponse.getStatusCode().value());
                    } else {
                        logger.debug(EELFLoggerDelegate.debugLogger,
                            "addPartnerHasRoleFunctionsToGlobalRole: Finished adding permissions to roles in External Auth system and status code: {} ",
                            addResponse.getStatusCode().value());
                    }
                } catch (Exception e) {
                    logger.error(EELFLoggerDelegate.errorLogger,
                        "addPartnerHasRoleFunctionsToGlobalRole: Failed for POST request: {} due to ", addPerms, e);
                }
            }
        }
    }

    private void deleteRoleInExtSystem(ObjectMapper mapper, String name)
        throws JsonProcessingException, Exception, ExternalAuthSystemException {
        ResponseEntity<String> deleteResponse;
        Map<String, String> delRoleKeyMapper = new HashMap<>();
        delRoleKeyMapper.put(ROLE_NAME, name);
        String delRoleKeyValue = mapper.writeValueAsString(delRoleKeyMapper);
        deleteResponse = deleteRoleInExternalSystem(delRoleKeyValue);
        if (deleteResponse.getStatusCode().value() != 200) {
            logger.error(EELFLoggerDelegate.errorLogger,
                "updateRoleInExternalSystem:  Failed to delete role in external system due to {} ",
                deleteResponse.getBody());
            throw new ExternalAuthSystemException(deleteResponse.getBody());
        }
    }

    public void addRole(Role addRole, String uebkey) throws Exception {
        boolean response = false;
        ResponseEntity<String> addResponse = null;
        HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
        FnApp app = getApp(uebkey).get(0);
        String newRole = updateExistingRoleInExternalSystem(addRole.getName(), app.getAuthNamespace());
        HttpEntity<String> entity = new HttpEntity<>(newRole, headers);
        logger.debug(EELFLoggerDelegate.debugLogger, "addRole: Connecting to External Auth system");
        addResponse = template.exchange(
            SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "role",
            HttpMethod.POST, entity, String.class);
        if (addResponse.getStatusCode().value() == 201) {
            response = true;
            logger.debug(EELFLoggerDelegate.debugLogger,
                "addRole: Finished adding role in the External Auth system  and response code: {} ",
                addResponse.getStatusCode().value());
        }
        if (addResponse.getStatusCode().value() == 406) {
            logger.error(EELFLoggerDelegate.errorLogger,
                "addRole: Failed to add in the External Auth system due to {} and status code: {}",
                addResponse.getBody(), addResponse.getStatusCode().value());
        }
    }

    private ResponseEntity<String> deleteRoleInExternalSystem(String delRole) throws Exception {
        ResponseEntity<String> delResponse = null;
        HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
        HttpEntity<String> entity = new HttpEntity<>(delRole, headers);
        logger.debug(EELFLoggerDelegate.debugLogger, "deleteRoleInExternalSystem: {} for DELETE: {}",
            CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE, delRole);
        delResponse = template.exchange(
            SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "role?force=true",
            HttpMethod.DELETE, entity, String.class);
        logger.debug(EELFLoggerDelegate.debugLogger,
            "deleteRoleInExternalSystem: Finished DELETE operation in the External Auth system {} and status code: {} ",
            delRole, delResponse.getStatusCode().value());
        return delResponse;
    }

    private String getSingleAppRole(String addRole, FnApp app) throws Exception {
        HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = null;
        logger.debug(EELFLoggerDelegate.debugLogger, "getSingleAppRole: Connecting to External Auth system");
        response = template.exchange(
            SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "roles/"
                + app.getAuthNamespace() + "." + addRole
                .replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"),
            HttpMethod.GET, entity, String.class);
        logger.debug(EELFLoggerDelegate.debugLogger,
            "getSingleAppRole: Finished GET app role from External Auth system and status code: {} ",
            response.getStatusCode().value());
        return response.getBody();
    }

    private void addFunctionsTOGlobalRole(List<FnRole> epRoleList, Role updateExtRole,
        List<FnFunction> roleFunctionListNew, ObjectMapper mapper, FnApp app, FnApp portalAppInfo)
        throws Exception {
        try {
            logger.debug(EELFLoggerDelegate.debugLogger, "Entering into addFunctionsTOGlobalRole");
            // GET Permissions from External Auth System
            JSONArray extPerms = getExtAuthPermissions(app.getAuthNamespace());
            List<ExternalAccessPermsDetail> permsDetailList = getExtAuthPerrmissonList(app, extPerms);
            final Map<String, ExternalAccessPermsDetail> existingPermsWithRoles = new HashMap<>();
            final Map<String, ExternalAccessPermsDetail> existingPermsWithRolesWithPipes = new HashMap<>();
            final Map<String, FnFunction> userRquestedFunctionsMap = new HashMap<>();
            final Map<String, FnFunction> userRquestedFunctionsMapPipesFilter = new HashMap<>();
            for (ExternalAccessPermsDetail permDetail : permsDetailList) {
                existingPermsWithRoles.put(EcompPortalUtils.getFunctionCode(permDetail.getInstance()), permDetail);
                existingPermsWithRolesWithPipes.put(permDetail.getInstance(), permDetail);
            }
            // Add If function does not exists for role in External Auth System
            for (FnFunction roleFunc : roleFunctionListNew) {
                String roleFuncCode = "";
                ExternalAccessPermsDetail permsDetail;
                if (roleFunc.getCode().contains(FUNCTION_PIPE)) {
                    roleFuncCode = roleFunc.getCode();
                    permsDetail = existingPermsWithRolesWithPipes.get(roleFunc.getCode());
                } else {
                    roleFuncCode = EcompPortalUtils.getFunctionCode(roleFunc.getCode());
                    permsDetail = existingPermsWithRoles.get(roleFuncCode);
                }
                if (null == permsDetail.getRoles()
                    || !permsDetail.getRoles()
                    .contains(portalAppInfo.getAuthNamespace() + FUNCTION_PIPE
                        + epRoleList.get(0).getRoleName().replaceAll(
                        EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS,
                        "_"))) {
                    addRoleFunctionsToGlobalRoleInExternalSystem(roleFunc, updateExtRole, mapper, app, portalAppInfo);
                }
                userRquestedFunctionsMap.put(roleFuncCode, roleFunc);
                userRquestedFunctionsMapPipesFilter.put(EcompPortalUtils.getFunctionCode(roleFuncCode), roleFunc);
            }
            List<GlobalRoleWithApplicationRoleFunction> globalRoleFunctionList = entityManager
                .createNamedQuery("getGlobalRoleForRequestedApp")
                .setParameter("requestedAppId", app.getId())
                .setParameter("roleId", updateExtRole.getId())
                .getResultList();
            for (GlobalRoleWithApplicationRoleFunction globalRoleFunc : globalRoleFunctionList) {
                String globalRoleFuncWithoutPipes = "";
                FnFunction roleFunc = null;
                if (globalRoleFunc.getFunctionCd().contains(FUNCTION_PIPE)) {
                    globalRoleFuncWithoutPipes = globalRoleFunc.getFunctionCd();
                    roleFunc = userRquestedFunctionsMap.get(globalRoleFuncWithoutPipes);
                } else {
                    globalRoleFuncWithoutPipes = EcompPortalUtils.getFunctionCode(globalRoleFunc.getFunctionCd());
                    roleFunc = userRquestedFunctionsMapPipesFilter.get(globalRoleFuncWithoutPipes);
                }
                if (roleFunc == null) {
                    ExternalAccessPermsDetail permDetailFromMap = globalRoleFunc.getFunctionCd().contains(FUNCTION_PIPE)
                        ? existingPermsWithRolesWithPipes.get(globalRoleFuncWithoutPipes)
                        : existingPermsWithRoles.get(globalRoleFuncWithoutPipes);
                    ExternalAccessPerms perm = new ExternalAccessPerms(permDetailFromMap.getType(),
                        EcompPortalUtils.getFunctionCode(permDetailFromMap.getInstance()),
                        permDetailFromMap.getAction());
                    String roleName = portalAppInfo.getAuthNamespace() + "." + globalRoleFunc.getRoleName()
                        .replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_");
                    HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
                    removePermForRole(perm, mapper, roleName, headers);
                }
            }
            logger.debug(EELFLoggerDelegate.debugLogger, "Finished addFunctionsTOGlobalRole");
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "addFunctionsTOGlobalRole: Failed", e);
            throw e;
        }
    }

    private void removePermForRole(ExternalAccessPerms perm, ObjectMapper permMapper, String name, HttpHeaders headers)
        throws ExternalAuthSystemException, JsonProcessingException {
        ExternalAccessRolePerms extAccessRolePerms = new ExternalAccessRolePerms(perm, name);
        String permDetails = permMapper.writeValueAsString(extAccessRolePerms);
        try {
            HttpEntity<String> deleteEntity = new HttpEntity<>(permDetails, headers);
            logger.debug(EELFLoggerDelegate.debugLogger, "removePermForRole: {} for DELETE: {} ",
                CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE, permDetails);
            ResponseEntity<String> deletePermResponse = template
                .exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
                    + "role/" + name + "/perm", HttpMethod.DELETE, deleteEntity, String.class);
            if (deletePermResponse.getStatusCode().value() != 200) {
                throw new ExternalAuthSystemException(deletePermResponse.getBody());
            }
            logger.debug(EELFLoggerDelegate.debugLogger,
                "removePermForRole: Finished deleting permission to role in External Auth system: {} and status code: {}",
                permDetails, deletePermResponse.getStatusCode().value());
        } catch (Exception e) {
            if (e.getMessage().contains("404")) {
                logger.error(EELFLoggerDelegate.errorLogger, "Failed to add role for DELETE request: {} due to {}",
                    permDetails, e.getMessage());
            } else {
                throw e;
            }
        }
    }

    private void addRoleFunctionsToGlobalRoleInExternalSystem(FnFunction addFunction, Role globalRole,
        ObjectMapper mapper, FnApp app, FnApp portalAppInfo) throws Exception {
        try {
            logger.debug(EELFLoggerDelegate.debugLogger, "Entering into addRoleFunctionsToGlobalRoleInExternalSystem");
            ExternalAccessRolePerms extAddRolePerms = null;
            ExternalAccessPerms extAddPerms = null;
            HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
            String code = "";
            String type = "";
            String action = "";
            if (addFunction.getFunctionCd().contains(FUNCTION_PIPE)) {
                code = EcompPortalUtils.getFunctionCode(addFunction.getFunctionCd());
                type = getFunctionCodeType(addFunction.getFunctionCd());
                action = getFunctionCodeAction(addFunction.getFunctionCd());
            } else {
                code = addFunction.getFunctionCd();
                type = addFunction.getFunctionCd().contains("menu") ? "menu" : "url";
                action = "*";
            }
            extAddPerms = new ExternalAccessPerms(app.getAuthNamespace() + "." + type, code, action);
            extAddRolePerms = new ExternalAccessRolePerms(extAddPerms,
                portalAppInfo.getAuthNamespace() + "." + globalRole
                    .getName().replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"));
            String updateRolePerms = mapper.writeValueAsString(extAddRolePerms);
            HttpEntity<String> entity = new HttpEntity<>(updateRolePerms, headers);
            logger.debug(EELFLoggerDelegate.debugLogger, "addRoleFunctionsInExternalSystem: {} ",
                CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE);
            ResponseEntity<String> addResponse = template.exchange(
                SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "role/perm",
                HttpMethod.POST, entity, String.class);
            if (addResponse.getStatusCode().value() != 201) {
                logger.debug(EELFLoggerDelegate.debugLogger,
                    "addRoleFunctionsInExternalSystem: While adding permission to the role in  External Auth system something went wrong! due to {} and statuscode: {}",
                    addResponse.getStatusCode().getReasonPhrase(), addResponse.getStatusCode().value());
            } else {
                logger.debug(EELFLoggerDelegate.debugLogger,
                    "addRoleFunctionsInExternalSystem: Finished adding permissions to roles in External Auth system and status code: {} ",
                    addResponse.getStatusCode().value());
            }
            logger.debug(EELFLoggerDelegate.debugLogger, "Finished addRoleFunctionsToGlobalRoleInExternalSystem");
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "addRoleFunctionsToGlobalRoleInExternalSystem: Failed", e);
            throw e;
        }
    }

    private List<FnRoleFunction> convertSetToListOfRoleFunctions(Role updateExtRole) {
        Set<FnRoleFunction> roleFunctionSetList = updateExtRole.getRoleFunctions();
        List<FnRoleFunction> roleFunctionList = new ArrayList<>();
        ObjectMapper roleFuncMapper = new ObjectMapper();
        for (Object nextValue : roleFunctionSetList) {
            FnRoleFunction roleFunction = roleFuncMapper.convertValue(nextValue, FnRoleFunction.class);
            roleFunctionList.add(roleFunction);
        }
        return roleFunctionList.stream().distinct().collect(Collectors.toList());
    }

    private List<FnRole> getPartnerAppRoleInfo(Long roleId, Long appId) {
        List<FnRole> roleInfo = fnRoleService.retrieveAppRoleByAppRoleIdAndByAppId(roleId, appId);
        if (roleInfo.isEmpty()) {
            roleInfo = fnRoleService.retrieveAppRoleByAppRoleIdAndByAppId(appId, roleId);
        }
        return roleInfo;
    }

    private void inactiveRolesNotInExternalAuthSystem(final Long appId, List<FnRole> finalRoleList,
        List<FnRole> applicationRolesList) {
        final Map<String, FnRole> checkRolesInactive = new HashMap<>();
        for (FnRole extrole : finalRoleList) {
            checkRolesInactive.put(extrole.getRoleName(), extrole);
        }
        for (FnRole role : applicationRolesList) {
            try {
                List<FnRole> roleList;
                if (!checkRolesInactive.containsKey(role.getRoleName())) {
                    if (appId == 1) {
                        roleList = fnRoleService.retrieveAppRolesByRoleNameAndWhereAppIdIsNull(role.getRoleName());
                    } else {
                        roleList = fnRoleService.retrieveAppRolesByRoleNameAndByAppId(role.getRoleName(), appId);
                    }
                    if (!roleList.isEmpty()) {
                        FnRole updateRoleInactive = roleList.get(0);
                        updateRoleInactive.setActiveYn(false);
                        fnRoleService.saveOne(updateRoleInactive);
                    }
                }
            } catch (Exception e) {
                logger.error(EELFLoggerDelegate.errorLogger,
                    "syncApplicationRolesWithEcompDB: Failed to de-activate role ", e);
            }
        }
    }

    private JSONArray getExtAuthPermissions(String authNamespace) throws Exception {
        ResponseEntity<String> response = null;
        HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        logger.debug(EELFLoggerDelegate.debugLogger, "syncRoleFunctionFromExternalAccessSystem: {} ",
            CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE);
        response = template.exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
            + "perms/ns/" + authNamespace, HttpMethod.GET, entity, String.class);
        String res = response.getBody();
        logger.debug(EELFLoggerDelegate.debugLogger,
            "syncRoleFunctionFromExternalAccessSystem: Finished GET permissions from External Auth system and response: {} ",
            response.getBody());
        JSONObject jsonObj = new JSONObject(res);
        JSONArray extPerms = jsonObj.getJSONArray("perm");
        for (int i = 0; i < extPerms.length(); i++) {
            if (extPerms.getJSONObject(i).getString("type").equals(authNamespace + ".access")) {
                extPerms.remove(i);
                i--;
            }
        }
        return extPerms;
    }

    public void syncRoleFunctionFromExternalAccessSystem(FnApp app) {
        try {
            // get Permissions from External Auth System
            JSONArray extPerms = getExtAuthPermissions(app.getAuthNamespace());
            List<ExternalAccessPermsDetail> permsDetailList = getExtAuthPerrmissonList(app, extPerms);
            final Map<String, EpAppFunction> roleFuncMap = new HashMap<>();
            List<EpAppFunction> appFunctions = epAppFunctionService.getAllRoleFunctions(app.getId());
            if (!appFunctions.isEmpty()) {
                for (EpAppFunction roleFunc : appFunctions) {
                    roleFuncMap.put(roleFunc.getFunctionCd(), roleFunc);
                }
            }
            // get Roles for portal in DB
            List<FnRole> portalRoleList = getGlobalRolesOfPortal();
            final Map<String, FnRole> existingPortalRolesMap = new HashMap<>();
            for (FnRole epRole : portalRoleList) {
                existingPortalRolesMap.put(epRole.getRoleName().replaceAll(
                    EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"), epRole);
            }
            // get Roles in DB
            final Map<String, FnRole> currentRolesInDB = getAppRoleNamesWithUnderscoreMap(app);
            // store External Permissions with Pipe and without Pipe (just
            // instance)
            final Map<String, ExternalAccessPermsDetail> extAccessPermsContainsPipeMap = new HashMap<>();
            final Map<String, ExternalAccessPermsDetail> extAccessPermsMap = new HashMap<>();
            for (ExternalAccessPermsDetail permsDetailInfoWithPipe : permsDetailList) {
                extAccessPermsContainsPipeMap.put(permsDetailInfoWithPipe.getInstance(), permsDetailInfoWithPipe);
                String finalFunctionCodeVal = EcompPortalUtils.getFunctionCode(permsDetailInfoWithPipe.getInstance());
                extAccessPermsMap.put(finalFunctionCodeVal, permsDetailInfoWithPipe);
            }
            // Add if new functions and app role functions were added in
            // external auth system
            for (ExternalAccessPermsDetail permsDetail : permsDetailList) {
                String code = permsDetail.getInstance();
                EpAppFunction getFunctionCodeKey = roleFuncMap.get(permsDetail.getInstance());
                List<EpAppFunction> roleFunctionList = addGetLocalFunction(app, roleFuncMap, permsDetail, code,
                    getFunctionCodeKey);
                List<String> roles = permsDetail.getRoles();
                if (roles != null) {
                    addRemoveIfFunctionsRolesIsSyncWithExternalAuth(app, currentRolesInDB, roleFunctionList, roles,
                        existingPortalRolesMap);
                }
            }
            // Check if function does exits in External Auth System but exits in
            // local then delete function and its dependencies
            for (EpAppFunction roleFunc : appFunctions) {
                try {
                    ExternalAccessPermsDetail getFunctionCodeContainsPipeKey = extAccessPermsContainsPipeMap
                        .get(roleFunc.getFunctionCd());
                    if (null == getFunctionCodeContainsPipeKey) {
                        ExternalAccessPermsDetail getFunctionCodeKey = extAccessPermsMap.get(roleFunc.getFunctionCd());
                        if (null == getFunctionCodeKey) {
                            deleteAppRoleFuncDoesNotExitsInExtSystem(app.getId(), roleFunc.getFunctionCd());
                        }
                    }
                } catch (Exception e) {
                    logger.error(EELFLoggerDelegate.errorLogger,
                        "syncRoleFunctionFromExternalAccessSystem: Failed to delete function", e);
                }
            }
            logger.debug(EELFLoggerDelegate.debugLogger,
                "syncRoleFunctionFromExternalAccessSystem: Finished syncRoleFunctionFromExternalAccessSystem");
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger,
                "syncRoleFunctionFromExternalAccessSystem: Failed syncRoleFunctionFromExternalAccessSystem", e);
        }
    }

    private List<EpAppFunction> addGetLocalFunction(FnApp app,
        final Map<String, EpAppFunction> roleFuncMap, ExternalAccessPermsDetail permsDetail, String code,
        EpAppFunction getFunctionCodeKey) {
        String finalFunctionCodeVal = addToLocalIfFunctionNotExists(app, roleFuncMap, permsDetail, code,
            getFunctionCodeKey);
        List<EpAppFunction> roleFunctionList = epAppFunctionService
            .getAppFunctionOnCodeAndAppId(app.getId(), finalFunctionCodeVal);
        if (roleFunctionList.isEmpty()) {
            roleFunctionList = epAppFunctionService.getAppFunctionOnCodeAndAppId(app.getId(), code);
        }
        return roleFunctionList;
    }

    private String addToLocalIfFunctionNotExists(FnApp app, final Map<String, EpAppFunction> roleFuncMap,
        ExternalAccessPermsDetail permsDetail, String code, EpAppFunction getFunctionCodeKey) {
        String finalFunctionCodeVal = "";
        if (null == getFunctionCodeKey) {
            finalFunctionCodeVal = EcompPortalUtils.getFunctionCode(permsDetail.getInstance());
            EpAppFunction checkIfCodeStillExits = roleFuncMap.get(finalFunctionCodeVal);
            // If function does not exist in local then add!
            if (null == checkIfCodeStillExits) {
                logger.debug(EELFLoggerDelegate.debugLogger,
                    "syncRoleFunctionFromExternalAccessSystem: Adding function: {} ", code);
                addFunctionInEcompDB(app, permsDetail, code);
                logger.debug(EELFLoggerDelegate.debugLogger,
                    "syncRoleFunctionFromExternalAccessSystem: Finished adding function: {} ", code);
            }
        }
        return finalFunctionCodeVal;
    }

    private void addFunctionInEcompDB(FnApp app, ExternalAccessPermsDetail permsDetail, String code) {
        try {
            EpAppFunction addFunction = new EpAppFunction();
            addFunction.setAppId(app);
            addFunction.setFunctionCd(code);
            addFunction.setFunctionName(permsDetail.getDescription());
            epAppFunctionService.save(addFunction);
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "addFunctionInEcompDB: Failed to add function", e);
        }
    }

    private List<ExternalAccessPermsDetail> getExtAuthPerrmissonList(FnApp app, JSONArray extPerms) throws IOException {
        ExternalAccessPermsDetail permDetails = null;
        List<ExternalAccessPermsDetail> permsDetailList = new ArrayList<>();
        for (int i = 0; i < extPerms.length(); i++) {
            String description = null;
            if (extPerms.getJSONObject(i).has("description")) {
                description = extPerms.getJSONObject(i).getString(EXTERNAL_AUTH_ROLE_DESCRIPTION);
            } else {
                description =
                    extPerms.getJSONObject(i).getString("type").substring(app.getAuthNamespace().length() + 1) + "|"
                        + extPerms.getJSONObject(i).getString("instance") + "|"
                        + extPerms.getJSONObject(i).getString("action");
            }
            if (extPerms.getJSONObject(i).has("roles")) {
                ObjectMapper rolesListMapper = new ObjectMapper();
                JSONArray resRoles = extPerms.getJSONObject(i).getJSONArray("roles");
                List<String> list = rolesListMapper.readValue(resRoles.toString(),
                    TypeFactory.defaultInstance().constructCollectionType(List.class, String.class));
                permDetails = new ExternalAccessPermsDetail(extPerms.getJSONObject(i).getString("type"),
                    extPerms.getJSONObject(i).getString("type").substring(app.getAuthNamespace().length() + 1)
                        + FUNCTION_PIPE + extPerms.getJSONObject(i).getString("instance") + FUNCTION_PIPE
                        + extPerms.getJSONObject(i).getString("action"),
                    extPerms.getJSONObject(i).getString("action"), list, description);
                permsDetailList.add(permDetails);
            } else {
                permDetails = new ExternalAccessPermsDetail(extPerms.getJSONObject(i).getString("type"),
                    extPerms.getJSONObject(i).getString("type").substring(app.getAuthNamespace().length() + 1)
                        + FUNCTION_PIPE + extPerms.getJSONObject(i).getString("instance") + FUNCTION_PIPE
                        + extPerms.getJSONObject(i).getString("action"),
                    extPerms.getJSONObject(i).getString("action"), description);
                permsDetailList.add(permDetails);
            }
        }
        return permsDetailList;
    }

    public List<FnRole> getGlobalRolesOfPortal() {
        List<FnRole> globalRoles = new ArrayList<>();
        try {
            globalRoles = fnRoleService.getGlobalRolesOfPortal();
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "getGlobalRolesOfPortal failed", e);
        }
        return globalRoles;
    }

    private void deleteAppRoleFuncDoesNotExitsInExtSystem(final Long appId, final String roleFunc) {
        logger.debug(EELFLoggerDelegate.debugLogger,
            "syncRoleFunctionFromExternalAccessSystem: Deleting app role function {}", roleFunc);
        epAppRoleFunctionService.deleteByAppIdAndFunctionCd(appId, roleFunc);
        logger.debug(EELFLoggerDelegate.debugLogger,
            "syncRoleFunctionFromExternalAccessSystem: Deleted app role function {}", roleFunc);
        logger.debug(EELFLoggerDelegate.debugLogger,
            "syncRoleFunctionFromExternalAccessSystem: Deleting app function {}", roleFunc);
        epAppFunctionService.deleteByAppIdAndFunctionCd(appId, roleFunc);
        logger.debug(EELFLoggerDelegate.debugLogger,
            "syncRoleFunctionFromExternalAccessSystem: Deleted app function {}", roleFunc);
    }

    private CentralV2Role convertRoleToCentralV2Role(FnRole role) {
        return CentralV2Role.builder().id(role.getId()).created(role.getCreated())
            .modified(role.getModified()).createdId(role.getCreatedId().getId())
            .modifiedId(role.getModifiedId().getId())
            .rowNum(role.getRowNum()).name(role.getRoleName()).active(role.getActiveYn())
            .priority(role.getPriority()).roleFunctions(new TreeSet<>()).childRoles(new TreeSet<>())
            .parentRoles(new TreeSet<>()).build();
    }

    private void addRemoveIfFunctionsRolesIsSyncWithExternalAuth(FnApp app, final Map<String, FnRole> currentRolesInDB,
        List<EpAppFunction> roleFunctionList, List<String> roles,
        Map<String, FnRole> existingPortalRolesMap) throws Exception {
        if (!roleFunctionList.isEmpty()) {
            final Map<String, LocalRole> currentAppRoleFunctionsMap = new HashMap<>();
            final Map<String, String> currentRolesInExtSystem = new HashMap<>();
            List<LocalRole> localRoleList = localRoleService
                .getCurrentAppRoleFunctions(app.getId(), roleFunctionList.get(0).getFunctionCd());
            for (LocalRole localRole : localRoleList) {
                currentAppRoleFunctionsMap.put(localRole.getRolename().replaceAll(
                    EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"), localRole);
            }
            for (String addRole : roles) {
                currentRolesInExtSystem.put(addRole.substring(addRole.indexOf(FUNCTION_PIPE) + 1), addRole);
            }
            for (String extAuthrole : roles) {
                String roleNameSpace = extAuthrole.substring(0, extAuthrole.indexOf(FUNCTION_PIPE));
                boolean isNameSpaceMatching = EcompPortalUtils.checkNameSpaceMatching(roleNameSpace,
                    app.getAuthNamespace());
                if (isNameSpaceMatching) {
                    if (!currentAppRoleFunctionsMap
                        .containsKey(extAuthrole.substring(app.getAuthNamespace().length() + 1))) {
                        FnRole localAddFuntionRole = currentRolesInDB
                            .get(extAuthrole.substring(app.getAuthNamespace().length() + 1));
                        if (localAddFuntionRole == null) {
                            checkAndAddRoleInDB(app, currentRolesInDB, roleFunctionList, extAuthrole);
                        } else {
                            EpAppRoleFunction addAppRoleFunc = new EpAppRoleFunction();
                            addAppRoleFunc.setAppId(app);
                            addAppRoleFunc.setEpAppFunction(roleFunctionList.get(0));
                            addAppRoleFunc.setFnRole(localAddFuntionRole);
                            epAppRoleFunctionService.save(addAppRoleFunc);
                        }
                    }
                    // This block is to save global role function if exists
                } else {
                    String extAuthAppRoleName = extAuthrole.substring(extAuthrole.indexOf(FUNCTION_PIPE) + 1);
                    boolean checkIfGlobalRoleExists = existingPortalRolesMap.containsKey(extAuthAppRoleName);
                    if (checkIfGlobalRoleExists) {
                        FnRole role = existingPortalRolesMap.get(extAuthAppRoleName);
                        EpAppRoleFunction addGlobalRoleFunctions = new EpAppRoleFunction();
                        List<EpAppRoleFunction> currentGlobalRoleFunctionsList = epAppRoleFunctionService
                            .getAppRoleFunctionOnRoleIdAndAppId(app.getId(), role.getId());
                        boolean checkIfRoleFunctionExists = currentGlobalRoleFunctionsList.stream()
                            .anyMatch(currentGlobalRoleFunction -> currentGlobalRoleFunction.getEpAppFunction()
                                .getFunctionCd()
                                .equals(roleFunctionList.get(0).getFunctionCd()));
                        if (!checkIfRoleFunctionExists) {
                            addGlobalRoleFunctions.setAppId(app);
                            addGlobalRoleFunctions.setFnRole(role);
                            if (!app.getId().equals(role.getAppRoleId())) {
                                addGlobalRoleFunctions.setRoleAppId((PortalConstants.PORTAL_APP_ID).toString());
                            } else {
                                addGlobalRoleFunctions.setRoleAppId(null);
                            }
                            addGlobalRoleFunctions.setEpAppFunction(roleFunctionList.get(0));
                            epAppRoleFunctionService.save(addGlobalRoleFunctions);
                        }
                    }
                }
            }
            for (LocalRole localRoleDelete : localRoleList) {
                if (!currentRolesInExtSystem.containsKey(localRoleDelete.getRolename()
                    .replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"))) {
                    epAppRoleFunctionService
                        .deleteByAppIdAndFunctionCdAndRoleId(app.getId(), roleFunctionList.get(0).getFunctionCd(),
                            localRoleDelete.getRoleId());
                }
            }
        }
    }

    private void checkAndAddRoleInDB(FnApp app, final Map<String, FnRole> currentRolesInDB,
        List<EpAppFunction> roleFunctionList, String roleList) throws Exception {
        if (!currentRolesInDB.containsKey(roleList.substring(app.getAuthNamespace().length() + 1))) {
            FnRole role = addRoleInDBIfDoesNotExists(app.getId(),
                roleList.substring(app.getAuthNamespace().length() + 1));
            addRoleDescriptionInExtSystem(role.getRoleName(), app.getAuthNamespace());
            if (!roleFunctionList.isEmpty()) {
                try {
                    EpAppRoleFunction addAppRoleFunc = new EpAppRoleFunction();
                    addAppRoleFunc.setAppId(app);
                    addAppRoleFunc.setEpAppFunction(roleFunctionList.get(0));
                    addAppRoleFunc.setFnRole(role);
                    epAppRoleFunctionService.save(addAppRoleFunc);
                } catch (Exception e) {
                    logger.error(EELFLoggerDelegate.errorLogger,
                        "syncRoleFunctionFromExternalAccessSystem: Failed to save app role function ", e);
                }
            }
        }
    }

    private FnRole addRoleInDBIfDoesNotExists(final Long appId, final String role) {
        FnRole setNewRole = new FnRole();
        try {
            boolean isCreated = checkIfRoleExitsElseCreateInSyncFunctions(role, appId);
            List<FnRole> getRoleCreated = null;
            if (!appId.equals(PortalConstants.PORTAL_APP_ID)) {
                List<FnRole> roleCreated = fnRoleService.retrieveAppRolesByRoleNameAndByAppId(role, appId);
                if (!isCreated) {
                    FnRole epUpdateRole = roleCreated.get(0);
                    epUpdateRole.setAppRoleId(epUpdateRole.getId());
                    fnRoleService.saveOne(epUpdateRole);
                    getRoleCreated = fnRoleService.retrieveAppRolesByRoleNameAndByAppId(role, appId);
                } else {
                    getRoleCreated = roleCreated;
                }
            } else {
                getRoleCreated = fnRoleService.retrieveAppRolesByRoleNameAndWhereAppIdIsNull(role);
            }
            if (getRoleCreated != null && !getRoleCreated.isEmpty()) {
                FnRole roleObject = getRoleCreated.get(0);
                setNewRole.setId(roleObject.getId());
                setNewRole.setRoleName(roleObject.getRoleName());
                setNewRole.setActiveYn(roleObject.getActiveYn());
                setNewRole.setPriority(roleObject.getPriority());
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "addRoleInDBIfDoesNotExists: Failed", e);
        }
        return setNewRole;
    }

    private boolean checkIfRoleExitsElseCreateInSyncFunctions(final String role, final long appId) {
        boolean isCreated;
        List<FnRole> roleCreated = null;
        if (appId == PortalConstants.PORTAL_APP_ID) {
            roleCreated = fnRoleService.retrieveAppRolesByRoleNameAndWhereAppIdIsNull(role);
        } else {
            roleCreated = fnRoleService.retrieveAppRolesByRoleNameAndByAppId(role, appId);
        }
        if (roleCreated == null || roleCreated.isEmpty()) {
            FnRole epRoleNew = new FnRole();
            epRoleNew.setActiveYn(true);
            epRoleNew.setRoleName(role);
            if (appId == PortalConstants.PORTAL_APP_ID) {
                epRoleNew.setAppId(null);
            } else {
                epRoleNew.setAppId(appId);
            }
            fnRoleService.saveOne(epRoleNew);
            isCreated = false;
        } else {
            isCreated = true;
        }
        return isCreated;
    }

    private String updateExistingRoleInExternalSystem(final String roleName, final String authNamespace)
        throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String addNewRole = "";
        ExternalAccessRole extRole = new ExternalAccessRole();
        extRole.setName(authNamespace + "." + roleName
            .replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"));
        extRole.setDescription(String.valueOf(roleName));
        addNewRole = mapper.writeValueAsString(extRole);
        return addNewRole;
    }

    private boolean addRoleDescriptionInExtSystem(final String roleName, final String authNamespace) throws Exception {
        boolean status = false;
        try {
            String addRoleNew = updateExistingRoleInExternalSystem(roleName, authNamespace);
            HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
            HttpEntity<String> entity = new HttpEntity<>(addRoleNew, headers);
            template.exchange(
                SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "role",
                HttpMethod.PUT, entity, String.class);
            status = true;
        } catch (HttpClientErrorException e) {
            logger.error(EELFLoggerDelegate.errorLogger,
                "HttpClientErrorException - Failed to addRoleDescriptionInExtSystem", e);
            EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "addRoleDescriptionInExtSystem: Failed", e);
        }
        return status;
    }

    public List<CentralRole> convertV2CentralRoleListToOldVerisonCentralRoleList(List<CentralV2Role> v2CenRoleList) {
        List<CentralRole> cenRoleList = new ArrayList<>();
        for (CentralV2Role v2CenRole : v2CenRoleList) {
            SortedSet<EpAppFunction> cenRoleFuncList = new TreeSet<>();
            for (DomainVo vo : v2CenRole.getRoleFunctions()) {
                Optional<FnRoleFunction> v2CenRoleFunc = fnRoleFunctionService.findById(vo.getId());
                if (v2CenRoleFunc.isPresent()) {
                    EpAppFunction roleFunc = EpAppFunction.builder()
                        .functionCd(v2CenRoleFunc.get().getFunctionCd().getCode())
                        .functionName(v2CenRoleFunc.get().getRole().getRoleName())
                        .build();
                    cenRoleFuncList.add(roleFunc);
                }
            }
            CentralRole role = new CentralRole(v2CenRole.getId(), v2CenRole.getName(), v2CenRole.isActive(),
                v2CenRole.getPriority(), cenRoleFuncList);
            cenRoleList.add(role);
        }
        return cenRoleList;
    }

    public ExternalRequestFieldsValidator saveRoleForApplication(Role saveRole, String uebkey) throws Exception {
        boolean response = false;
        String message = "";
        try {
            FnApp app = getApp(uebkey).get(0);
            addRoleInEcompDB(saveRole, app);
            response = true;
        } catch (Exception e) {
            message = e.getMessage();
            logger.error(EELFLoggerDelegate.errorLogger, "saveRoleForApplication failed", e);
        }
        return new ExternalRequestFieldsValidator(response, message);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addRoleInEcompDB(Role addRoleInDB, FnApp app) throws Exception {
        boolean result;
        FnRole epRole;
        Set<FnFunction> roleFunctionList = addRoleInDB.getRoleFunctions();
        List<FnFunction> roleFunctionListNew = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for (Object nextValue : roleFunctionList) {
            FnFunction roleFunction = mapper.convertValue(nextValue, FnFunction.class);
            roleFunctionListNew.add(roleFunction);
        }
        List<FnFunction> listWithoutDuplicates = roleFunctionListNew.stream().distinct().collect(Collectors.toList());
        try {
            if (addRoleInDB.getId() == null) { // check if it is new role
                if (EcompPortalUtils.checkIfRemoteCentralAccessAllowed()) {
                    checkIfRoleExitsInExternalSystem(addRoleInDB, app);
                }
                FnRole epRoleNew = new FnRole();
                epRoleNew.setActiveYn(addRoleInDB.getActive());
                epRoleNew.setRoleName(addRoleInDB.getName());
                epRoleNew.setPriority(addRoleInDB.getPriority());
                if (app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
                    epRoleNew.setAppId(null);
                } else {
                    epRoleNew.setAppId(app.getId());
                }
                fnRoleService.saveOne(epRoleNew);
                List<FnRole> getRoleCreated = null;
                if (!app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
                    List<FnRole> roleCreated = fnRoleService
                        .retrieveAppRolesByRoleNameAndByAppId(addRoleInDB.getName(), app.getId());
                    FnRole epUpdateRole = roleCreated.get(0);
                    epUpdateRole.setAppRoleId(epUpdateRole.getId());
                    fnRoleService.saveOne(epUpdateRole);
                    getRoleCreated = fnRoleService
                        .retrieveAppRolesByRoleNameAndByAppId(addRoleInDB.getName(), app.getId());
                } else {
                    getRoleCreated = fnRoleService.retrieveAppRolesByRoleNameAndWhereAppIdIsNull(addRoleInDB.getName());
                }
                // Add role in External Auth system
                if (EcompPortalUtils.checkIfRemoteCentralAccessAllowed()) {
                    addNewRoleInExternalSystem(getRoleCreated, app);
                }
                result = true;
            } else { // if role already exists then update it
                FnRole globalRole = null;
                List<FnRole> applicationRoles;
                List<FnRole> globalRoleList = getGlobalRolesOfPortal();
                boolean isGlobalRole = false;
                if (!globalRoleList.isEmpty()) {
                    FnRole role = globalRoleList.stream().filter(x -> addRoleInDB.getId().equals(x.getId())).findAny()
                        .orElse(null);
                    if (role != null) {
                        globalRole = role;
                        isGlobalRole = true;
                    }
                }
                if (app.getId().equals(PortalConstants.PORTAL_APP_ID)
                    || (globalRole != null && app.getId() != globalRole.getAppId())) {
                    applicationRoles = getPortalAppRoleInfo(addRoleInDB.getId());
                } else {
                    applicationRoles = getPartnerAppRoleInfo(addRoleInDB.getId(), app.getId());
                }
                if (EcompPortalUtils.checkIfRemoteCentralAccessAllowed()) {
                    updateRoleInExternalSystem(addRoleInDB, app, isGlobalRole);
                    // Add all user to the re-named role in external auth system
                    if (!applicationRoles.isEmpty()
                        && !addRoleInDB.getName().equals(applicationRoles.get(0).getRoleName())) {
                        bulkUploadUsersSingleRole(app.getUebKey(), applicationRoles.get(0).getId(),
                            addRoleInDB.getName());
                    }
                }
                deleteRoleFunction(app, applicationRoles);
                if (!applicationRoles.isEmpty()) {
                    epRole = applicationRoles.get(0);
                    epRole.setRoleName(addRoleInDB.getName());
                    epRole.setPriority(addRoleInDB.getPriority());
                    epRole.setActiveYn(addRoleInDB.getActive());
                    if (app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
                        epRole.setAppId(null);
                        epRole.setAppRoleId(null);
                    } else if (!app.getId().equals(PortalConstants.PORTAL_APP_ID)
                        && applicationRoles.get(0).getAppRoleId() == null) {
                        epRole.setAppRoleId(epRole.getId());
                    }
                    fnRoleService.saveOne(epRole);
                }
                Long roleAppId = null;
                if (globalRole != null && !app.getId().equals(globalRole.getAppId())) {
                    roleAppId = PortalConstants.PORTAL_APP_ID;
                }
                saveRoleFunction(listWithoutDuplicates, app, applicationRoles, roleAppId);
                result = true;
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "addRoleInEcompDB is failed", e);
            throw e;
        }
    }

    private void saveRoleFunction(List<FnFunction> roleFunctionListNew, FnApp app, List<FnRole> applicationRoles,
        Long roleAppId) {
        for (FnFunction roleFunc : roleFunctionListNew) {
            String code = EcompPortalUtils.getFunctionCode(roleFunc.getCode());
            EpAppRoleFunction appRoleFunc = new EpAppRoleFunction();
            appRoleFunc.setAppId(app);
            appRoleFunc.setFnRole(applicationRoles.get(0));
            appRoleFunc.setRoleAppId(String.valueOf(roleAppId));
            List<EpAppFunction> roleFunction = epAppFunctionService.getRoleFunction(roleFunc.getCode(), app.getId());
            if (roleFunction.isEmpty()) {
                roleFunction = epAppFunctionService.getRoleFunction(code, app.getId());
            }
            if (roleFunction.size() > 1) {
                EpAppFunction getExactFunctionCode = appFunctionListFilter(code, roleFunction);
                appRoleFunc.setEpAppFunction(getExactFunctionCode);
            } else {
                appRoleFunc.setEpAppFunction(roleFunction.get(0));
            }
            epAppRoleFunctionService.save(appRoleFunc);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteRoleForApplication(String deleteRole, String uebkey) throws Exception {
        boolean result;
        try {
            List<FnRole> epRoleList;
            FnApp app = getApp(uebkey).get(0);
            if (app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
                epRoleList = fnRoleService.retrieveAppRolesByRoleNameAndWhereAppIdIsNull(deleteRole);
            } else {
                epRoleList = fnRoleService.retrieveAppRolesByRoleNameAndByAppId(deleteRole, app.getId());
            }
            if (!epRoleList.isEmpty()) {
                // Delete app role functions before deleting role
                deleteRoleFunction(app, epRoleList);
                if (app.getId() == 1) {
                    // Delete fn_user_ role
                    String query =
                        "DELETE FROM FN_USER_ROLE WHERE " + APP_ID_EQUALS + app.getId() + " and role_id = " + epRoleList
                            .get(0).getId();
                    entityManager.createQuery(query).executeUpdate();
                    boolean isPortalRequest = false;
                    deleteRoleDependencyRecords(epRoleList.get(0).getId(), app.getId(), isPortalRequest);
                }
                deleteRoleInExternalAuthSystem(epRoleList, app);
                logger.debug(EELFLoggerDelegate.debugLogger, "deleteRoleForApplication: committed the transaction");
                fnRoleService.delete(epRoleList.get(0));
            }
            result = true;
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "deleteRoleForApplication: failed", e);
            result = false;
        }
        return result;
    }

    private void deleteRoleInExternalAuthSystem(List<FnRole> epRoleList, FnApp app) throws Exception {
        ResponseEntity<String> deleteResponse;
        ResponseEntity<String> res = getNameSpaceIfExists(app);
        if (res.getStatusCode() == HttpStatus.OK) {
            // Delete Role in External System
            String deleteRoleKey = "{\"name\":\"" + app.getAuthNamespace() + "." + epRoleList.get(0).getRoleName()
                .replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_") + "\"}";
            deleteResponse = deleteRoleInExternalSystem(deleteRoleKey);
            if (deleteResponse.getStatusCode().value() != 200 && deleteResponse.getStatusCode().value() != 404) {
                EPLogUtil.logExternalAuthAccessAlarm(logger, deleteResponse.getStatusCode());
                logger.error(EELFLoggerDelegate.errorLogger,
                    "deleteRoleForApplication: Failed to delete role in external auth system! due to {} ",
                    deleteResponse.getBody());
            }
            logger.debug(EELFLoggerDelegate.debugLogger, "deleteRoleForApplication: about to commit the transaction");
        }
    }

    private void deleteRoleFunction(FnApp app, List<FnRole> role) {
        List<EpAppRoleFunction> appRoleFunctionList = epAppRoleFunctionService
            .getAppRoleFunctionOnRoleIdAndAppId(app.getId(), role.get(0).getId());
        epAppRoleFunctionService.deleteInBatch(appRoleFunctionList);
    }

    public List<CentralV2Role> getActiveRoles(String uebkey) throws Exception {
        List<CentralV2Role> roleList = new ArrayList<>();
        try {
            List<FnApp> app = getApp(uebkey);
            Long appId = null;
            if (!app.get(0).getId().equals(PortalConstants.PORTAL_APP_ID)) {
                appId = app.get(0).getId();
            }
            List<FnRole> epRole;
            if (appId == null) {
                epRole = fnRoleService.retrieveActiveRolesWhereAppIdIsNull();
            } else {
                epRole = fnRoleService.retrieveActiveRolesOfApplication(appId);
            }
            roleList = createCentralRoleObject(app, epRole, roleList);
            List<CentralV2Role> globalRoleList = getGlobalRolesOfApplication(app.get(0).getId());
            if (globalRoleList.size() > 0) {
                roleList.addAll(globalRoleList);
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "getActiveRoles: failed", e);
            throw e;
        }
        return roleList;
    }

    public Integer bulkUploadRoles(String uebkey) throws Exception {
        List<FnApp> app = getApp(uebkey);
        List<FnRole> roles = getAppRoles(app.get(0).getId());
        List<CentralV2Role> cenRoleList = new ArrayList<>();
        final Map<String, Long> params = new HashMap<>();
        Integer rolesListAdded = 0;
        try {
            cenRoleList = createCentralRoleObject(app, roles, cenRoleList);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
            String roleList = mapper.writeValueAsString(cenRoleList);
            List<Role> roleObjectList = mapper.readValue(roleList,
                TypeFactory.defaultInstance().constructCollectionType(List.class, Role.class));
            for (Role role : roleObjectList) {
                addRoleInExternalSystem(role, app.get(0));
                rolesListAdded++;
            }
            if (!app.get(0).getId().equals(PortalConstants.PORTAL_APP_ID)) {
                // Add Account Admin role in External AUTH System
                try {
                    String addAccountAdminRole = "";
                    ExternalAccessRole extRole = new ExternalAccessRole();
                    extRole.setName(app.get(0).getAuthNamespace() + "." + PortalConstants.ADMIN_ROLE
                        .replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"));
                    addAccountAdminRole = mapper.writeValueAsString(extRole);
                    HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
                    HttpEntity<String> entity = new HttpEntity<>(addAccountAdminRole, headers);
                    template.exchange(
                        SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "role",
                        HttpMethod.POST, entity, String.class);
                    rolesListAdded++;
                } catch (HttpClientErrorException e) {
                    logger.error(EELFLoggerDelegate.errorLogger,
                        "HttpClientErrorException - Failed to create Account Admin role", e);
                    EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
                } catch (Exception e) {
                    if (e.getMessage().equalsIgnoreCase("409 Conflict")) {
                        logger.error(EELFLoggerDelegate.errorLogger,
                            "bulkUploadRoles: Account Admin Role already exits but does not break functionality",
                            e);
                    } else {
                        logger.error(EELFLoggerDelegate.errorLogger,
                            "bulkUploadRoles: Failed to create Account Admin role", e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadRoles: failed", e);
            throw e;
        }
        return rolesListAdded;
    }

    private void addRoleInExternalSystem(Role role, FnApp app) throws Exception {
        String addRoleNew = updateExistingRoleInExternalSystem(role.getName(), app.getAuthNamespace());
        HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
        try {
            HttpEntity<String> entity = new HttpEntity<>(addRoleNew, headers);
            template.exchange(
                SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "role",
                HttpMethod.POST, entity, String.class);
        } catch (HttpClientErrorException e) {
            logger.error(EELFLoggerDelegate.errorLogger, "HttpClientErrorException - Failed to addRoleInExternalSystem",
                e);
            EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
        } catch (Exception e) {
            if (e.getMessage().equalsIgnoreCase("409 Conflict")) {
                logger.error(EELFLoggerDelegate.errorLogger,
                    "addRoleInExternalSystem: Role already exits but does not break functionality", e);
            } else {
                logger.error(EELFLoggerDelegate.errorLogger,
                    "addRoleInExternalSystem: Failed to addRoleInExternalSystem", e.getMessage());
            }
        }
    }

    public Integer bulkUploadFunctions(String uebkey) throws Exception {
        FnApp app = getApp(uebkey).get(0);
        List<FnRoleFunction> roleFuncList = fnRoleFunctionService.findAll();
        EpAppFunction cenRoleFunc;
        Integer functionsAdded = 0;
        try {
            for (FnRoleFunction roleFunc : roleFuncList) {
                cenRoleFunc = EpAppFunction.builder()
                    .functionCd(roleFunc.getFunctionCd().getName())
                    .roleId(roleFunc.getRole().getId())
                    .build();
                addRoleFunctionInExternalSystem(cenRoleFunc, app);
                functionsAdded++;
            }
        } catch (HttpClientErrorException e) {
            logger.error(EELFLoggerDelegate.errorLogger, "HttpClientErrorException - bulkUploadFunctions failed", e);
            EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadFunctions: failed", e.getMessage(), e);
        }
        return functionsAdded;
    }

    public Integer bulkUploadRolesFunctions(String uebkey) throws Exception {
        FnApp app = getApp(uebkey).get(0);
        List<FnRole> roles = getAppRoles(app.getId());
        Integer roleFunctions = 0;
        try {
            for (FnRole role : roles) {
                List<BulkUploadRoleFunction> appRoleFunc = bulkUploadUserRolesService
                    .uploadAllRoleFunctions(role.getId());
                if (!appRoleFunc.isEmpty()) {
                    for (BulkUploadRoleFunction addRoleFunc : appRoleFunc) {
                        addRoleFunctionsInExternalSystem(addRoleFunc, role, app);
                        roleFunctions++;
                    }
                }
            }
        } catch (HttpClientErrorException e) {
            logger.error(EELFLoggerDelegate.errorLogger,
                "HttpClientErrorException - Failed to bulkUploadRolesFunctions", e);
            EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadRolesFunctions: failed", e);
        }
        return roleFunctions;
    }

    private void addRoleFunctionsInExternalSystem(BulkUploadRoleFunction addRoleFunc, FnRole role, FnApp app) {
        String type;
        String instance = "";
        String action = "";
        if (addRoleFunc.getFunctionCd().contains(FUNCTION_PIPE)) {
            type = EcompPortalUtils.getFunctionType(addRoleFunc.getFunctionCd());
            instance = EcompPortalUtils.getFunctionCode(addRoleFunc.getFunctionCd());
            action = EcompPortalUtils.getFunctionAction(addRoleFunc.getFunctionCd());
        } else {
            type = addRoleFunc.getFunctionCd().contains("menu") ? "menu" : "url";
            instance = addRoleFunc.getFunctionCd();
            action = "*";
        }
        ExternalAccessRolePerms extRolePerms = null;
        ExternalAccessPerms extPerms = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
            extPerms = new ExternalAccessPerms(app.getAuthNamespace() + "." + type, instance, action,
                addRoleFunc.getFunctionName());
            extRolePerms = new ExternalAccessRolePerms(extPerms, app.getAuthNamespace() + "." + role.getRoleName()
                .replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"));
            String updateRolePerms = mapper.writeValueAsString(extRolePerms);
            HttpEntity<String> entity = new HttpEntity<>(updateRolePerms, headers);
            template.exchange(
                SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "role/perm",
                HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            if (e.getMessage().equalsIgnoreCase("409 Conflict")) {
                logger.error(EELFLoggerDelegate.errorLogger,
                    "addRoleFunctionsInExternalSystem: RoleFunction already exits but does not break functionality",
                    e);
            } else {
                logger.error(EELFLoggerDelegate.errorLogger,
                    "addRoleFunctionsInExternalSystem: Failed to addRoleFunctionsInExternalSystem", e.getMessage());
            }
        }
    }


    public Integer bulkUploadUserRoles(String uebkey) throws Exception {
        FnApp app = getApp(uebkey).get(0);
        List<BulkUploadUserRoles> userRolesList;
        Integer userRolesAdded = 0;
        if (app.getAuthCentral()) {
            userRolesList = bulkUploadUserRolesService.getBulkUserRoles(app.getUebKey());
            for (BulkUploadUserRoles userRolesUpload : userRolesList) {
                if (!userRolesUpload.getOrgUserId().equals("su1234")) {
                    addUserRoleInExternalSystem(userRolesUpload);
                    userRolesAdded++;
                }
            }
        }
        return userRolesAdded;
    }

    public Integer bulkUploadPartnerFunctions(String uebkey) throws Exception {
        FnApp app = getApp(uebkey).get(0);
        List<EpAppFunction> roleFuncList = epAppFunctionService.getAllRoleFunctions(app.getId());
        Integer functionsAdded = 0;
        try {
            for (EpAppFunction roleFunc : roleFuncList) {
                addFunctionInExternalSystem(roleFunc, app);
                functionsAdded++;
            }
        } catch (HttpClientErrorException e) {
            logger.error(EELFLoggerDelegate.errorLogger, "HttpClientErrorException - bulkUploadPartnerFunctions failed",
                e);
            EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadPartnerFunctions: failed", e.getMessage(), e);
        }
        return functionsAdded;
    }

    public void bulkUploadPartnerRoles(String uebkey, List<Role> roleList) throws Exception {
        FnApp app = getApp(uebkey).get(0);
        for (Role role : roleList) {
            addRoleInExternalSystem(role, app);
        }
    }

    private void addFunctionInExternalSystem(EpAppFunction roleFunc, FnApp app) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ExternalAccessPerms extPerms = new ExternalAccessPerms();
        HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
        String type = "";
        String instance = "";
        String action = "";
        if ((roleFunc.getFunctionCd().contains(FUNCTION_PIPE))
            || (roleFunc.getType() != null && roleFunc.getAction() != null)) {
            type = EcompPortalUtils.getFunctionType(roleFunc.getFunctionCd());
            instance = EcompPortalUtils.getFunctionCode(roleFunc.getFunctionCd());
            action = EcompPortalUtils.getFunctionAction(roleFunc.getFunctionCd());
        } else {
            type = roleFunc.getFunctionCd().contains("menu") ? "menu" : "url";
            instance = roleFunc.getFunctionCd();
            action = "*";
        }
        try {
            extPerms.setAction(action);
            extPerms.setInstance(instance);
            extPerms.setType(app.getAuthNamespace() + "." + type);
            extPerms.setDescription(roleFunc.getFunctionName());
            String addFunction = mapper.writeValueAsString(extPerms);
            HttpEntity<String> entity = new HttpEntity<>(addFunction, headers);
            logger.debug(EELFLoggerDelegate.debugLogger, "addFunctionInExternalSystem: {} for POST: {}",
                CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE, addFunction);
            ResponseEntity<String> addPermResponse = template.exchange(
                SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "perm",
                HttpMethod.POST, entity, String.class);
            logger.debug(EELFLoggerDelegate.debugLogger,
                "addFunctionInExternalSystem: Finished adding permission for POST: {} and status code: {} ",
                addPermResponse.getStatusCode().value(), addFunction);
        } catch (HttpClientErrorException e) {
            logger.error(EELFLoggerDelegate.errorLogger,
                "HttpClientErrorException - Failed to add function in external central auth system", e);
            EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
            throw e;
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger,
                "addFunctionInExternalSystem: Failed to add fucntion in external central auth system", e);
            throw e;
        }
    }

    public Integer bulkUploadPartnerRoleFunctions(String uebkey) throws Exception {
        FnApp app = getApp(uebkey).get(0);
        List<FnRole> roles = getAppRoles(app.getId());
        Integer roleFunctions = 0;
        try {
            for (FnRole role : roles) {
                List<BulkUploadRoleFunction> appRoleFunc = bulkUploadUserRolesService
                    .uploadPartnerRoleFunctions(role.getId());
                if (!appRoleFunc.isEmpty()) {
                    for (BulkUploadRoleFunction addRoleFunc : appRoleFunc) {
                        addRoleFunctionsInExternalSystem(addRoleFunc, role, app);
                        roleFunctions++;
                    }
                }
            }
            // upload global role functions to ext auth system
            if (!app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
                roleFunctions = bulkUploadGlobalRoleFunctions(app, roleFunctions);
            }
        } catch (HttpClientErrorException e) {
            logger.error(EELFLoggerDelegate.errorLogger,
                "HttpClientErrorException - Failed to bulkUploadRolesFunctions", e);
            EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "bulkUploadRolesFunctions: failed", e);
        }
        return roleFunctions;
    }

    private Integer bulkUploadGlobalRoleFunctions(FnApp app, Integer roleFunctions) throws Exception {
        try {
            //TODO HARDCODED ID!!!!!
            FnApp portalApp = fnAppService.getById(1L);
            String getBulkUploadPartnerGlobalRoleFunctions =
                "select distinct fr.role_id, fr.role_name, fr.active_yn, fr.priority, epr.function_cd, ep.function_name, ep.app_id, epr.role_app_id"
                    + " from fn_role fr, ep_app_function ep, ep_app_role_function epr"
                    + " where fr.role_id = epr.role_id and ep.function_cd = epr.function_cd and ep.app_id = epr.app_id and  epr.app_id = :appId and epr.role_app_id = 1";
            List<GlobalRoleWithApplicationRoleFunction> globalRoleFuncs = entityManager
                .createQuery(getBulkUploadPartnerGlobalRoleFunctions)
                .setParameter("appId", app.getId())
                .getResultList();
            ObjectMapper mapper = new ObjectMapper();
            HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
            for (GlobalRoleWithApplicationRoleFunction globalRoleFunc : globalRoleFuncs) {
                ExternalAccessRolePerms extRolePerms;
                ExternalAccessPerms extPerms;
                String type = "";
                String instance = "";
                String action = "";
                if (globalRoleFunc.getFunctionCd().contains(FUNCTION_PIPE)) {
                    type = EcompPortalUtils.getFunctionType(globalRoleFunc.getFunctionCd());
                    instance = EcompPortalUtils.getFunctionCode(globalRoleFunc.getFunctionCd());
                    action = EcompPortalUtils.getFunctionAction(globalRoleFunc.getFunctionCd());
                } else {
                    type = globalRoleFunc.getFunctionCd().contains("menu") ? "menu" : "url";
                    instance = globalRoleFunc.getFunctionCd();
                    action = "*";
                }
                extPerms = new ExternalAccessPerms(app.getAuthNamespace() + "." + type, instance, action);
                extRolePerms = new ExternalAccessRolePerms(extPerms,
                    portalApp.getAuthNamespace() + "." + globalRoleFunc.getRoleName().replaceAll(
                        EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"));
                String updateRolePerms = mapper.writeValueAsString(extRolePerms);
                HttpEntity<String> entity = new HttpEntity<>(updateRolePerms, headers);
                updateRoleFunctionInExternalSystem(updateRolePerms, entity);
                roleFunctions++;
            }
        } catch (HttpClientErrorException e) {
            logger.error(EELFLoggerDelegate.errorLogger,
                "HttpClientErrorException - Failed to add role function in external central auth system", e);
            EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
            throw e;
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger,
                "bulkUploadGlobalRoleFunctions: Failed to add role fucntion in external central auth system", e);
            throw e;
        }
        return roleFunctions;
    }

    private void updateRoleFunctionInExternalSystem(String updateRolePerms, HttpEntity<String> entity) {
        logger.debug(EELFLoggerDelegate.debugLogger, "bulkUploadRoleFunc: {} for POST: {}",
            CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE, updateRolePerms);
        ResponseEntity<String> addPermResponse = template.exchange(
            SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "role/perm",
            HttpMethod.POST, entity, String.class);
        logger.debug(EELFLoggerDelegate.debugLogger,
            "bulkUploadRoleFunc: Finished adding permission for POST: {} and status code: {} ",
            addPermResponse.getStatusCode().value(), updateRolePerms);
    }

    public List<String> getMenuFunctionsList(String uebkey) throws Exception {
        List<String> appMenuFunctionsList = null;
        List<String> appMenuFunctionsFinalList = new ArrayList<>();
        try {
            FnApp app = getApp(uebkey).get(0);
            String getMenuFunctions = "select f.function_cd from ep_app_function f"
                + " where f.app_id =:appId"
                + " UNION"
                + " select epa.function_cd from fn_role fnr, ep_app_role_function epr, ep_app_function epa where epr.role_id = fnr.role_id"
                + " and epa.function_cd = epr.function_cd and fnr.role_name like 'global%' and fnr.app_id is null and epr.app_id = 1";
            appMenuFunctionsList = entityManager.createQuery(getMenuFunctions).setParameter(APP_ID, app.getId())
                .getResultList();
            for (String appMenuFunction : appMenuFunctionsList) {
                if (appMenuFunction.contains(FUNCTION_PIPE)) {
                    appMenuFunctionsFinalList.add(EcompPortalUtils.getFunctionCode(appMenuFunction));
                } else {
                    appMenuFunctionsFinalList.add(appMenuFunction);
                }
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "getMenuFunctionsList: Failed", e);
            return appMenuFunctionsFinalList;
        }
        return appMenuFunctionsFinalList;
    }

    public List<EcompUser> getAllAppUsers(String uebkey) throws Exception {
        List<String> usersList = new ArrayList<>();
        List<EcompUser> usersfinalList = new ArrayList<>();
        try {
            FnApp app = getApp(uebkey).get(0);
            String ApplicationUserRoles =
                "select distinct fu.org_id, fu.manager_id, fu.first_name, fu.middle_name, fu.last_name, fu.phone, fu.email, fu.hrid, fu.org_user_id, fu.org_code, fu.org_manager_userid, fu.job_title, fu.login_id, \n"
                    + " fu.active_yn , fr.app_role_id, fr.role_name, epr.function_cd , epf.function_name\n"
                    + " from fn_user fu, fn_role fr, fn_user_role fur, ep_app_role_function epr , ep_app_function epf\n"
                    + " where fu.user_id = fur.user_id and fu.active_yn='Y' and fur.role_id = fr.role_id and fr.app_id =:appId and fr.active_yn='Y' and epr.function_cd= epf.function_cd and epf.app_id=epr.app_id and fur.role_id=epr.role_id\n"
                    + " union\n"
                    + " select distinct fu.org_id, fu.manager_id, fu.first_name, fu.middle_name, fu.last_name, fu.phone, fu.email, fu.hrid, fu.org_user_id, fu.org_code, fu.org_manager_userid, fu.job_title, \n"
                    + " fu.login_id, fu.active_yn , fr.role_id, fr.role_name, earf.function_cd , eaf.function_name\n"
                    + " from fn_user_role a, fn_role fr, fn_user fu , ep_app_role_function earf, ep_app_function eaf\n"
                    + " where a.role_id in (select b.role_id from ep_app_role_function b where b.role_app_id = 1 and b.app_id =:appId) and a.user_id =fu.user_id and a.role_id = fr.role_id and fr.active_yn='Y' and fu.active_yn='Y'\n"
                    + " and earf.role_id = a.role_id and earf.function_cd = eaf.function_cd and earf.app_id = eaf.app_id  and earf.role_app_id = 1 and fr.active_yn='Y' and fu.active_yn='Y'";

            List<EcompUserRoles> userList = entityManager.createQuery(ApplicationUserRoles)
                .setParameter("appId", app.getId()).getResultList();
            for (EcompUserRoles ecompUserRole : userList) {
                boolean found = false;
                Set<EcompRole> roles = null;
                for (EcompUser user : usersfinalList) {
                    if (user.getOrgUserId().equals(ecompUserRole.getOrgUserId())) {
                        EcompRole ecompRole = new EcompRole();
                        ecompRole.setId(ecompUserRole.getRoleId());
                        ecompRole.setName(ecompUserRole.getRoleName());
                        roles = user.getRoles();
                        EcompRole role = roles.stream().filter(x -> x.getName().equals(ecompUserRole.getRoleName()))
                            .findAny().orElse(null);
                        SortedSet<EcompRoleFunction> roleFunctionSet = new TreeSet<>();
                        if (role != null) {
                            roleFunctionSet = (SortedSet<EcompRoleFunction>) role.getRoleFunctions();
                        }
                        String functionCode = EcompPortalUtils.getFunctionCode(ecompUserRole.getFunctionCode());
                        functionCode = EPUserUtils.decodeFunctionCode(functionCode);
                        EcompRoleFunction epRoleFunction = new EcompRoleFunction();
                        epRoleFunction.setName(ecompUserRole.getFunctionName());
                        epRoleFunction.setCode(EPUserUtils.decodeFunctionCode(functionCode));
                        epRoleFunction.setType(getFunctionCodeType(ecompUserRole.getFunctionCode()));
                        epRoleFunction.setAction(getFunctionCodeAction(ecompUserRole.getFunctionCode()));
                        roleFunctionSet.add(epRoleFunction);
                        ecompRole.setRoleFunctions(roleFunctionSet);
                        roles.add(ecompRole);
                        user.setRoles(roles);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    EcompUser epUser = new EcompUser();
                    epUser.setOrgId(ecompUserRole.getOrgId());
                    epUser.setManagerId(ecompUserRole.getManagerId());
                    epUser.setFirstName(ecompUserRole.getFirstName());
                    epUser.setLastName(ecompUserRole.getLastName());
                    epUser.setPhone(ecompUserRole.getPhone());
                    epUser.setEmail(ecompUserRole.getEmail());
                    epUser.setOrgUserId(ecompUserRole.getOrgUserId());
                    epUser.setOrgCode(ecompUserRole.getOrgCode());
                    epUser.setOrgManagerUserId(ecompUserRole.getOrgManagerUserId());
                    epUser.setJobTitle(ecompUserRole.getJobTitle());
                    epUser.setLoginId(ecompUserRole.getLoginId());
                    epUser.setActive(true);
                    roles = new HashSet<>();
                    EcompRole ecompRole = new EcompRole();
                    ecompRole.setId(ecompUserRole.getRoleId());
                    ecompRole.setName(ecompUserRole.getRoleName());
                    SortedSet<EcompRoleFunction> roleFunctionSet = new TreeSet<>();
                    String functionCode = EcompPortalUtils.getFunctionCode(ecompUserRole.getFunctionCode());
                    functionCode = EPUserUtils.decodeFunctionCode(functionCode);
                    EcompRoleFunction epRoleFunction = new EcompRoleFunction();
                    epRoleFunction.setName(ecompUserRole.getFunctionName());
                    epRoleFunction.setCode(EPUserUtils.decodeFunctionCode(functionCode));
                    epRoleFunction.setType(getFunctionCodeType(ecompUserRole.getFunctionCode()));
                    epRoleFunction.setAction(getFunctionCodeAction(ecompUserRole.getFunctionCode()));
                    roleFunctionSet.add(epRoleFunction);
                    ecompRole.setRoleFunctions(roleFunctionSet);
                    roles.add(ecompRole);
                    epUser.setRoles(roles);
                    usersfinalList.add(epUser);
                }
            }
            ObjectMapper mapper = new ObjectMapper();
            for (EcompUser u1 : usersfinalList) {
                String str = mapper.writeValueAsString(u1);
                usersList.add(str);
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "getAllUsers failed", e);
            throw e;
        }
        return usersfinalList;
    }

    public List<EcompRole> missingUserApplicationRoles(String uebkey, String loginId, Set<EcompRole> CurrentUserRoles)
        throws Exception {
        List<FnApp> appList = getApp(uebkey);
        FnApp app = appList.get(0);
        List<FnUser> epUserList;
        epUserList = getUser(loginId);
        List<EcompRole> missingUserAppRoles = new ArrayList<>();
        List<String> roleNamesList = CurrentUserRoles.stream().map(EcompRole::getName).collect(Collectors.toList());
        logger.debug(EELFLoggerDelegate.debugLogger, "Roles of User from hibernate :" + roleNamesList);
        List<EcompRole> userApplicationsRolesfromDB = getUserAppRoles(app, epUserList.get(0));
        if (userApplicationsRolesfromDB.size() > 0) {
            missingUserAppRoles = userApplicationsRolesfromDB.stream().filter(x -> !roleNamesList.contains(x.getName()))
                .collect(Collectors.toList());
        }
        List<String> missingroleNamesList = missingUserAppRoles.stream().map(EcompRole::getName)
            .collect(Collectors.toList());
        logger.debug(EELFLoggerDelegate.debugLogger, "MissingUserAppRoles():" + missingroleNamesList);

        List<EcompRole> finalMissingRoleList = new ArrayList<>();
        if (missingUserAppRoles.size() > 0) {
            final Map<String, Long> params = new HashMap<>();
            for (EcompRole role : missingUserAppRoles) {
                EcompRole epRole = new EcompRole();
                epRole.setId(role.getId());
                epRole.setName(role.getName());
                String getAppRoleFunctionList =
                    "SELECT DISTINCT f.app_id , f.function_cd, f.function_name from ep_app_role_function rf, ep_app_function f"
                        + " where rf.role_id =:roleId and rf.app_id =:appId and rf.app_id = f.app_id and rf.function_cd = f.function_cd";
                List<EpAppFunction> appRoleFunctionList = entityManager.createQuery(getAppRoleFunctionList)
                    .setParameter("roleId", role.getId()).setParameter(APP_ID, app.getId()).getResultList();
                SortedSet<EcompRoleFunction> roleFunctionSet = new TreeSet<>();
                for (EpAppFunction roleFunc : appRoleFunctionList) {
                    String functionCode = EcompPortalUtils.getFunctionCode(roleFunc.getFunctionCd());
                    String type = getFunctionCodeType(roleFunc.getFunctionCd());
                    String action = getFunctionCodeAction(roleFunc.getFunctionCd());
                    EcompRoleFunction fun = new EcompRoleFunction();
                    fun.setAction(action);
                    fun.setCode(functionCode);
                    fun.setType(type);
                    fun.setName(roleFunc.getFunctionName());
                    roleFunctionSet.add(fun);

                }
                epRole.setRoleFunctions(roleFunctionSet);
                finalMissingRoleList.add(epRole);
            }
        }

        return finalMissingRoleList;
    }

    private List<EcompRole> getUserAppRoles(FnApp app, FnUser user) {
        String getUserAppCurrentRoles = "select distinct fu.role_id, fr.user_id, fu.role_name, fu.priority from fn_role fu left outer join fn_user_role fr ON fu.role_id = fr.role_id and fu.app_id = fr.app_id and fr.role_id != 999 where fu.app_id =:appId and fr.user_id =:userId and fu.active_yn='Y' \n";
        List<EPUserAppCurrentRoles> userAppsRolesList = entityManager.createQuery(getUserAppCurrentRoles)
            .setParameter("appId", app.getId())
            .setParameter("userId", user.getId())
            .getResultList();
        List<EcompRole> setUserRoles = new ArrayList<>();
        for (EPUserAppCurrentRoles role : userAppsRolesList) {
            logger.debug(EELFLoggerDelegate.debugLogger, "In getUserAppRoles()- get userRolename = {}",
                role.getRoleName());
            EcompRole ecompRole = new EcompRole();
            ecompRole.setId(role.getRoleId());
            ecompRole.setName(role.getRoleName());
            setUserRoles.add(ecompRole);
        }
        logger.debug(EELFLoggerDelegate.debugLogger, "In getUserAppRoles()- get userrole list size = {}",
            setUserRoles.size());
        return setUserRoles;
    }

    private List<FnUser> getUser(String loginId) throws InvalidUserException {
        List<FnUser> userList = fnUserService.getUserWithOrgUserId(loginId);
        if (userList.isEmpty()) {
            throw new InvalidUserException("User not found");
        }
        return userList;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ExternalRequestFieldsValidator deleteDependencyRoleRecord(Long roleId, String uebkey, String LoginId)
        throws Exception {
        String message = "";
        boolean response = false;
        FnApp app = null;
        try {
            List<FnRole> epRoleList = null;
            app = getApp(uebkey).get(0);
            if (app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
                epRoleList = getPortalAppRoleInfo(roleId);
            } else {
                epRoleList = getPartnerAppRoleInfo(roleId, app.getId());
            }
            if (EcompPortalUtils.checkIfRemoteCentralAccessAllowed()) {
                // Delete User Role in External System before deleting role
                deleteUserRoleInExternalSystem(epRoleList.get(0), app, LoginId);
            }
            // Delete user app roles
            fnRoleService.delete(epRoleList.get(0));
            boolean isPortalRequest = false;
            deleteRoleDependencyRecords(epRoleList.get(0).getId(), app.getId(), isPortalRequest);
            if (EcompPortalUtils.checkIfRemoteCentralAccessAllowed()) {
                // Final call to delete role once all dependencies has been
                // deleted
                deleteRoleInExternalAuthSystem(epRoleList, app);
            }
            fnRoleService.delete(epRoleList.get(0));
            logger.debug(EELFLoggerDelegate.debugLogger, "deleteDependencyRoleRecord: committed the transaction");
            response = true;
        } catch (HttpClientErrorException e) {
            logger.error(EELFLoggerDelegate.errorLogger, "deleteDependencyRoleRecord: HttpClientErrorException", e);
            EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
            message = e.getMessage();
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "deleteDependencyRoleRecord failed", e);
            message = e.getMessage();
        }
        return new ExternalRequestFieldsValidator(response, message);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRoleDependencyRecords(Long roleId, Long appId, boolean isPortalRequest)
        throws Exception {
        try {
            String sql = "";
            Query query = null;
            // It should delete only when it portal's roleId
            if (appId.equals(PortalConstants.PORTAL_APP_ID)) {
                // Delete from fn_role_function
                sql = "DELETE FROM fn_role_function WHERE role_id=" + roleId;
                logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
                query = entityManager.createQuery(sql);
                query.executeUpdate();
                // Delete from fn_role_composite
                sql = "DELETE FROM fn_role_composite WHERE parent_role_id=" + roleId + " OR child_role_id=" + roleId;
                logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
                query = entityManager.createQuery(sql);
                query.executeUpdate();
            }
            // Delete from ep_app_role_function
            sql = "DELETE FROM ep_app_role_function WHERE role_id=" + roleId;
            logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
            query = entityManager.createQuery(sql);
            query.executeUpdate();
            // Delete from ep_role_notification
            sql = "DELETE FROM ep_role_notification WHERE role_id=" + roleId;
            logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
            query = entityManager.createQuery(sql);
            query.executeUpdate();
            // Delete from fn_user_pseudo_role
            sql = "DELETE FROM fn_user_pseudo_role WHERE pseudo_role_id=" + roleId;
            logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
            query = entityManager.createQuery(sql);
            query.executeUpdate();
            // Delete form EP_WIDGET_CATALOG_ROLE
            sql = "DELETE FROM EP_WIDGET_CATALOG_ROLE WHERE role_id=" + roleId;
            logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
            query = entityManager.createQuery(sql);
            query.executeUpdate();
            // Delete form EP_WIDGET_CATALOG_ROLE
            sql = "DELETE FROM ep_user_roles_request_det WHERE requested_role_id=" + roleId;
            logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
            query = entityManager.createQuery(sql);
            query.executeUpdate();
            if (!isPortalRequest) {
                // Delete form fn_menu_functional_roles
                sql = "DELETE FROM fn_menu_functional_roles WHERE role_id=" + roleId;
                logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
                query = entityManager.createQuery(sql);
                query.executeUpdate();
            }
        } catch (Exception e) {
            logger.debug(EELFLoggerDelegate.debugLogger, "deleteRoleDependeciesRecord: failed ", e);
            throw new DeleteDomainObjectFailedException("delete Failed" + e.getMessage());
        }
    }

    private void deleteUserRoleInExternalSystem(FnRole role, FnApp app, String LoginId) throws Exception {
        HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        getNameSpaceIfExists(app);
        logger.debug(EELFLoggerDelegate.debugLogger, "deleteUserRoleInExternalSystem: {} ",
            CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE);
        ResponseEntity<String> getResponse = template.exchange(
            SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "userRole/"
                + LoginId
                + SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN)
                + "/" + app.getAuthNamespace() + "."
                + role.getRoleName()
                .replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"),
            HttpMethod.GET, entity, String.class);
        logger.debug(EELFLoggerDelegate.debugLogger,
            "deleteUserRoleInExternalSystem: Finished GET user roles from External Auth system and response: {} ",
            getResponse.getBody());
        if (getResponse.getStatusCode().value() != 200) {
            throw new ExternalAuthSystemException(getResponse.getBody());
        }
        String res = getResponse.getBody();
        if (!res.equals(IS_EMPTY_JSON_STRING)) {
            HttpEntity<String> userRoleentity = new HttpEntity<>(headers);
            logger.debug(EELFLoggerDelegate.debugLogger, "deleteUserRoleInExternalSystem: {} ",
                CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE);
            ResponseEntity<String> deleteResponse = template.exchange(
                SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "userRole/"
                    + LoginId
                    + SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN)
                    + "/" + app.getAuthNamespace() + "."
                    + role.getRoleName().replaceAll(
                    EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"),
                HttpMethod.DELETE, userRoleentity, String.class);
            if (deleteResponse.getStatusCode().value() != 200) {
                throw new ExternalAuthSystemException("Failed to delete user role");
            }
            logger.debug(EELFLoggerDelegate.debugLogger,
                "deleteUserRoleInExternalSystem: Finished deleting user role in External Auth system and status code: {} ",
                deleteResponse.getStatusCode().value());
        }
    }

    public Integer bulkUploadUsersSingleRole(String uebkey, Long roleId, String modifiedRoleName) throws Exception {
        FnApp app = getApp(uebkey).get(0);
        List<BulkUploadUserRoles> userRolesList;
        Integer userRolesAdded = 0;
        if (app.getAuthCentral()) {
            userRolesList = bulkUploadUserRolesService.getBulkUsersForSingleRole(app.getUebKey(), roleId);
            for (BulkUploadUserRoles userRolesUpload : userRolesList) {
                userRolesUpload.setRoleName(modifiedRoleName);
                if (!userRolesUpload.getOrgUserId().equals("su1234")) {
                    addUserRoleInExternalSystem(userRolesUpload);
                    userRolesAdded++;
                }
            }
        }
        return userRolesAdded;
    }

    private void addUserRoleInExternalSystem(BulkUploadUserRoles userRolesUpload) {
        try {
            String name = "";
            ObjectMapper mapper = new ObjectMapper();
            if (EPCommonSystemProperties
                .containsProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN)) {
                name = userRolesUpload.getOrgUserId()
                    + SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN);
            }
            ExternalAccessUser extUser = new ExternalAccessUser(name,
                userRolesUpload.getAppNameSpace() + "." + userRolesUpload.getRoleName()
                    .replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"));
            String userRole = mapper.writeValueAsString(extUser);
            HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
            HttpEntity<String> entity = new HttpEntity<>(userRole, headers);
            template.exchange(
                SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "userRole",
                HttpMethod.POST, entity, String.class);
        } catch (HttpClientErrorException e) {
            logger.error(EELFLoggerDelegate.errorLogger,
                "HttpClientErrorException - Failed to addUserRoleInExternalSystem", e);
            EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
        } catch (Exception e) {
            if (e.getMessage().equalsIgnoreCase("409 Conflict")) {
                logger.error(EELFLoggerDelegate.errorLogger,
                    "addUserRoleInExternalSystem: UserRole already exits but does not break functionality");
            } else {
                logger.error(EELFLoggerDelegate.errorLogger,
                    "addUserRoleInExternalSystem: Failed to addUserRoleInExternalSystem", e);
            }
        }
    }

    private void addNewRoleInExternalSystem(List<FnRole> newRole, FnApp app)
        throws Exception {
        try {
            HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
            ObjectMapper mapper = new ObjectMapper();
            String addNewRole;
            ExternalAccessRole extRole = new ExternalAccessRole();
            extRole.setName(app.getAuthNamespace() + "." + newRole.get(0).getRoleName()
                .replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_"));
            extRole.setDescription(String.valueOf(newRole.get(0).getRoleName()));
            addNewRole = mapper.writeValueAsString(extRole);
            HttpEntity<String> postEntity = new HttpEntity<>(addNewRole, headers);
            logger.debug(EELFLoggerDelegate.debugLogger, "addNewRoleInExternalSystem: {} for POST: {} ",
                CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE, addNewRole);
            ResponseEntity<String> addNewRoleInExternalSystem = template.exchange(
                SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "role",
                HttpMethod.POST, postEntity, String.class);
            if (addNewRoleInExternalSystem.getStatusCode().value() == 201) {
                logger.debug(EELFLoggerDelegate.debugLogger,
                    "addNewRoleInExternalSystem: Finished adding into External Auth system for POST: {} and status code: {}",
                    addNewRole, addNewRoleInExternalSystem.getStatusCode().value());
            }
        } catch (HttpClientErrorException ht) {
            fnRoleService.delete(newRole.get(0));
            logger.error(EELFLoggerDelegate.debugLogger,
                "addNewRoleInExternalSystem: Failed to add in External Auth system and status code: {}", ht);
            throw new HttpClientErrorException(ht.getStatusCode());
        }
    }

    private void checkIfRoleExitsInExternalSystem(Role checkRole, FnApp app) throws Exception {
        getNameSpaceIfExists(app);
        HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
        String roleName = app.getAuthNamespace() + "." + checkRole.getName()
            .replaceAll(EcompPortalUtils.EXTERNAL_CENTRAL_AUTH_ROLE_HANDLE_SPECIAL_CHARACTERS, "_");
        HttpEntity<String> checkRoleEntity = new HttpEntity<>(headers);
        logger.debug(EELFLoggerDelegate.debugLogger, "checkIfRoleExitsInExternalSystem: {} ",
            CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE);
        ResponseEntity<String> checkRoleInExternalSystem = template
            .exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "roles/"
                + roleName, HttpMethod.GET, checkRoleEntity, String.class);
        if (!checkRoleInExternalSystem.getBody().equals(IS_EMPTY_JSON_STRING)) {
            logger.debug(
                "checkIfRoleExitsInExternalSystem: Role already exists in external system {} and status code: {} ",
                checkRoleInExternalSystem.getBody(), checkRoleInExternalSystem.getStatusCode().value());
            throw new ExternalAuthSystemException(" Role already exists in external system");
        }
    }

    public ResponseEntity<String> getNameSpaceIfExists(FnApp app) throws Exception {
        HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        logger.debug(EELFLoggerDelegate.debugLogger, "checkIfNameSpaceExists: Connecting to External Auth system");
        ResponseEntity<String> response = null;
        try {
            response = template
                .exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
                    + "nss/" + app.getAuthNamespace(), HttpMethod.GET, entity, String.class);
            logger.debug(EELFLoggerDelegate.debugLogger, "checkIfNameSpaceExists: Finished ",
                response.getStatusCode().value());
        } catch (HttpClientErrorException e) {
            logger.error(EELFLoggerDelegate.errorLogger, "checkIfNameSpaceExists failed", e);
            EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new InvalidApplicationException("Invalid NameSpace");
            } else {
                throw e;
            }
        }
        return response;
    }

    private FnRoleFunction createCentralRoleFunctionForGlobalRole(GlobalRoleWithApplicationRoleFunction role) {
        String instance;
        String type;
        String action;
        FnRoleFunction cenRoleFun = null;
        if (role.getFunctionCd().contains(FUNCTION_PIPE)) {
            instance = EcompPortalUtils.getFunctionCode(role.getFunctionCd());
            type = EcompPortalUtils.getFunctionType(role.getFunctionCd());
            action = EcompPortalUtils.getFunctionAction(role.getFunctionCd());
            cenRoleFun = FnRoleFunction.builder().build();
            FnRole fnRole = new FnRole();
            FnFunction fnFunction = FnFunction.builder().functionCd(instance).name(role.getFunctionName()).type(type)
                .action(action).build();
            cenRoleFun.setRole(fnRole);
            cenRoleFun.setFunctionCd(fnFunction);
        } else {
            type = getFunctionCodeType(role.getFunctionCd());
            action = getFunctionCodeAction(role.getFunctionCd());
            FnFunction fnFunction = FnFunction.builder().functionCd(role.getFunctionCd()).name(role.getFunctionName())
                .type(type).action(action).build();
            cenRoleFun.setRole(new FnRole());
            cenRoleFun.setFunctionCd(fnFunction);
        }
        return cenRoleFun;
    }

    public CentralUser getUserRoles(String loginId, String uebkey) throws Exception {
        CentralUser sendUserRoles = null;
        try {
            CentralV2User cenV2User = getV2UserAppRoles(loginId, uebkey);
            sendUserRoles = convertV2UserRolesToOlderVersion(cenV2User);
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "getUserRoles: failed", e);
            throw e;
        }
        return sendUserRoles;
    }

    private CentralV2User getV2UserAppRoles(String loginId, String uebkey) throws Exception {
        FnApp app;
        List<FnApp> appList = getApp(uebkey);
        app = appList.get(0);
        FnUser user = fnUserService.loadUserByUsername(loginId);
        Set<FnUserRole> userAppSet = user.getUserApps();
        return createEPUser(user, userAppSet, app);
    }

    public List<FnApp> getApp(String uebkey) throws Exception {
        List<FnApp> app = null;
        try {
            app = fnAppService.getByUebKey(uebkey);
            if (!app.isEmpty() && !app.get(0).getEnabled()
                && !app.get(0).getId().equals(PortalConstants.PORTAL_APP_ID)) {
                throw new InactiveApplicationException("Application:" + app.get(0).getAppName() + " is Unavailable");
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "getApp: failed", e);
            throw e;
        }
        return app;
    }

    private CentralV2User createEPUser(FnUser userInfo, Set<FnUserRole> userAppSet, FnApp app) {
        CentralV2User userAppList = CentralV2User.builder().build();
        CentralV2User user1;
        List<FnRole> globalRoleList = new ArrayList<>();
        try {
            if (!app.getId().equals(PortalConstants.PORTAL_APP_ID)) {
                globalRoleList = fnRoleService.userAppGlobalRoles(userInfo.getId(), app.getId());
            }
            userAppList.setUserApps(new TreeSet<>());
            for (FnUserRole userApp : userAppSet) {
                if (userApp.getRoleId().getActiveYn()) {
                    FnApp epApp = userApp.getFnAppId();
                    String globalRole = userApp.getRoleId().getRoleName().toLowerCase();
                    if (((epApp.getId().equals(app.getId()))
                        && (!userApp.getRoleId().getId().equals(PortalConstants.ACCOUNT_ADMIN_ROLE_ID)))
                        || ((epApp.getId().equals(PortalConstants.PORTAL_APP_ID))
                        && (globalRole.toLowerCase().startsWith("global_")))) {
                        CentralV2UserApp cua = new CentralV2UserApp();
                        cua.setUserId(null);
                        CentralApp cenApp = CentralApp.builder().id(1L).created(epApp.getCreated())
                            .modified(epApp.getModified()).createdId(epApp.getId())
                            .modifiedId(epApp.getModifiedId().getId()).rowNum(epApp.getRowNum())
                            .name(epApp.getAppName()).imageUrl(epApp.getAppImageUrl())
                            .description(epApp.getAppDescription()).notes(epApp.getAppNotes())
                            .url(epApp.getAppUrl()).alternateUrl(epApp.getAppAlternateUrl())
                            .restEndpoint(epApp.getAppRestEndpoint()).mlAppName(epApp.getMlAppName())
                            .mlAppAdminId(epApp.getMlAppAdminId()).motsId(String.valueOf(epApp.getMotsId()))
                            .appPassword(epApp.getAppPassword()).open(String.valueOf(epApp.getOpen()))
                            .enabled(String.valueOf(epApp.getEnabled())).thumbnail(epApp.getThumbnail())
                            .username(epApp.getAppUsername()).uebKey(epApp.getUebKey())
                            .uebSecret(epApp.getUebSecret()).uebTopicName(epApp.getUebTopicName())
                            .build();
                        cenApp.setAppPassword(EPCommonSystemProperties.APP_DISPLAY_PASSWORD);
                        cua.setApp(cenApp);
                        Long appId = null;
                        if (globalRole.toLowerCase().startsWith("global_")
                            && epApp.getId().equals(PortalConstants.PORTAL_APP_ID)
                            && !epApp.getId().equals(app.getId())) {
                            appId = app.getId();
                            FnRole result = null;
                            if (globalRoleList.size() > 0) {
                                result = globalRoleList.stream()
                                    .filter(x -> userApp.getRoleId().getId().equals(x.getId())).findAny()
                                    .orElse(null);
                            }
                            if (result == null) {
                                continue;
                            }
                        } else {
                            appId = userApp.getFnAppId().getId();
                        }
                        List<EpAppFunction> appRoleFunctionList = epAppFunctionService
                            .getAppRoleFunctionList(userApp.getRoleId().getId(), appId);
                        SortedSet<EpAppFunction> roleFunctionSet = new TreeSet<>();
                        for (EpAppFunction roleFunc : appRoleFunctionList) {
                            String functionCode = EcompPortalUtils.getFunctionCode(roleFunc.getFunctionCd());
                            String type = getFunctionCodeType(roleFunc.getFunctionCd());
                            String action = getFunctionCodeAction(roleFunc.getFunctionCd());
                            EpAppFunction cenRoleFunc = new EpAppFunction(roleFunc.getId(),
                                functionCode, roleFunc.getFunctionName(), null, type, action, null);
                            roleFunctionSet.add(cenRoleFunc);
                        }
                        Long userRoleId;
                        if (globalRole.toLowerCase().startsWith("global_")
                            || epApp.getId().equals(PortalConstants.PORTAL_APP_ID)) {
                            userRoleId = userApp.getRoleId().getId();
                        } else {
                            userRoleId = userApp.getRoleId().getAppRoleId();
                        }
                        CentralV2Role cenRole = CentralV2Role.builder().id(userRoleId)
                            .created(userApp.getRoleId().getCreated()).modified(userApp.getRoleId().getModified())
                            .createdId(userApp.getRoleId().getCreatedId().getId())
                            .modifiedId(userApp.getRoleId().getModifiedId().getId())
                            .rowNum(userApp.getRoleId().getRowNum()).name(userApp.getRoleId().getRoleName())
                            .active(userApp.getRoleId().getActiveYn()).priority(userApp.getRoleId().getPriority())
                            //.roleFunctions(roleFunctionSet).setChildRoles(null).setParentRoles(null)
                            .build();
                        cua.setRole(cenRole);
                        userAppList.getUserApps().add(cua);
                    }
                }
            }
            user1 = CentralV2User.builder().id(null).created(userInfo.getCreated())
                .modified(userInfo.getModified()).createdId(userInfo.getCreatedId().getId())
                .modifiedId(userInfo.getModifiedId().getId()).rowNum(userInfo.getRowNum())
                .orgId(userInfo.getOrgId().getOrgId()).managerId(userInfo.getOrgManagerUserId())
                .firstName(userInfo.getFirstName()).middleInitial(userInfo.getMiddleName())
                .lastName(userInfo.getLastName()).phone(userInfo.getPhone()).fax(userInfo.getFax())
                .cellular(userInfo.getCellular()).email(userInfo.getEmail())
                .addressId(userInfo.getAddressId()).alertMethodCd(userInfo.getAlertMethodCd().getAlertMethodCd())
                .hrid(userInfo.getHrid()).orgUserId(userInfo.getOrgUserId()).orgCode(userInfo.getOrgCode())
                .address1(userInfo.getAddressLine1()).address2(userInfo.getAddressLine2()).city(userInfo.getCity())
                .state(userInfo.getStateCd()).zipCode(userInfo.getZipCode()).country(userInfo.getCountryCd())
                .orgManagerUserId(userInfo.getOrgManagerUserId()).locationClli(userInfo.getLocationClli())
                .businessCountryCode(userInfo.getBusinessUnit())
                .businessCountryName(userInfo.getBusinessUnitName())
                .businessUnit(userInfo.getBusinessUnit()).businessUnitName(userInfo.getBusinessUnitName())
                .department(userInfo.getDepartment()).departmentName(userInfo.getDepartmentName())
                .companyCode(userInfo.getOrgCode()).company(userInfo.getCompany())
                .zipCodeSuffix(userInfo.getZipCode()).jobTitle(userInfo.getJobTitle())
                //.commandChain(userInfo.getCommandChain()).siloStatus(userInfo.getSiloStatus())
                .costCenter(userInfo.getCostCenter()).financialLocCode(userInfo.getFinLocCode())
                .loginId(userInfo.getLoginId()).loginPwd(userInfo.getLoginPwd())
                .lastLoginDate(userInfo.getLastLoginDate()).active(userInfo.getActiveYn())
                //.internal(userInfo.getIsInternalYn()).selectedProfileId(userInfo.getSelectedProfileId())
                //.timeZoneId(userInfo.getTimezone().getTimezoneId()).online(userInfo.isOnline())
                //.chatId(userInfo.getChatId()).setUserApps(userAppList.getUserApps()).setPseudoRoles(null)
                .build();
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "createEPUser: createEPUser failed", e);
            throw e;
        }
        return user1;
    }

    private CentralUser convertV2UserRolesToOlderVersion(CentralV2User cenV2User) {
        Set<CentralV2UserApp> userV2Apps = cenV2User.getUserApps();
        Set<CentralUserApp> userApps = new TreeSet<>();
        for (CentralV2UserApp userApp : userV2Apps) {
            CentralApp app = userApp.getApp();
            CentralUserApp cua = new CentralUserApp();
            cua.setUserId(null);
            cua.setApp(app);
            SortedSet<EpAppFunction> cenRoleFunction = new TreeSet<>();
            for (DomainVo vo : userApp.getRole().getRoleFunctions()) {
                Optional<EpAppFunction> epApp = epAppFunctionService.getForId(vo.getId());
                if (epApp.isPresent()) {
                    EpAppFunction cenRoleFunc = EpAppFunction.builder().functionCd(epApp.get().getFunctionCd())
                        .functionName(
                            epApp.get().getFunctionName()).build();
                    cenRoleFunction.add(cenRoleFunc);
                }
            }
            CentralRole role = new CentralRole(userApp.getRole().getId(), userApp.getRole().getName(),
                userApp.getRole().isActive(), userApp.getRole().getPriority(), cenRoleFunction);
            cua.setRole(role);
            userApps.add(cua);
        }
        return CentralUser.builder().id(cenV2User.getId()).created(cenV2User.getCreated())
            .modified(cenV2User.getModified()).createdId(cenV2User.getCreatedId())
            .modifiedId(cenV2User.getModifiedId()).rowNum(cenV2User.getRowNum())
            .orgId(cenV2User.getOrgId()).managerId(cenV2User.getManagerId())
            .firstName(cenV2User.getFirstName()).middleInitial(cenV2User.getMiddleInitial())
            .lastName(cenV2User.getLastName()).phone(cenV2User.getPhone()).fax(cenV2User.getFax())
            .cellular(cenV2User.getCellular()).email(cenV2User.getEmail())
            .addressId(cenV2User.getAddressId()).alertMethodCd(cenV2User.getAlertMethodCd())
            .hrid(cenV2User.getHrid()).orgUserId(cenV2User.getOrgUserId()).orgCode(cenV2User.getOrgCode())
            .address1(cenV2User.getAddress1()).address2(cenV2User.getAddress2()).city(cenV2User.getCity())
            .state(cenV2User.getState()).zipCode(cenV2User.getZipCode()).country(cenV2User.getCountry())
            .orgManagerUserId(cenV2User.getOrgManagerUserId()).locationClli(cenV2User.getLocationClli())
            .businessCountryCode(cenV2User.getBusinessCountryCode())
            .businessCountryName(cenV2User.getBusinessCountryName()).businessUnit(cenV2User.getBusinessUnit())
            .businessUnitName(cenV2User.getBusinessUnitName()).department(cenV2User.getDepartment())
            .departmentName(cenV2User.getDepartmentName()).companyCode(cenV2User.getCompanyCode())
            .company(cenV2User.getCompany()).zipCodeSuffix(cenV2User.getZipCodeSuffix())
            .jobTitle(cenV2User.getJobTitle()).commandChain(cenV2User.getCommandChain())
            .siloStatus(cenV2User.getSiloStatus()).costCenter(cenV2User.getCostCenter())
            .financialLocCode(cenV2User.getFinancialLocCode()).loginId(cenV2User.getLoginId())
            .loginPwd(cenV2User.getLoginPwd()).lastLoginDate(cenV2User.getLastLoginDate())
            .active(cenV2User.isActive()).internal(cenV2User.isInternal())
            .selectedProfileId(cenV2User.getSelectedProfileId()).timeZoneId(cenV2User.getTimeZoneId())
            .online(cenV2User.isOnline()).chatId(cenV2User.getChatId()).userApps(userApps).build();
    }

    public CentralV2Role getRoleInfo(final Long roleId, final String uebkey) throws Exception {
        List<CentralV2Role> roleList = new ArrayList<>();
        CentralV2Role cenRole = CentralV2Role.builder().build();
        List<FnRole> roleInfo;
        List<FnApp> app;
        try {
            app = getApp(uebkey);
            if (app.isEmpty()) {
                throw new InactiveApplicationException("Application not found");
            }
            if (!app.get(0).getId().equals(PortalConstants.PORTAL_APP_ID)) {
                List<FnRole> globalRoleList = getGlobalRolesOfPortal();
                if (globalRoleList.size() > 0) {
                    FnRole result = globalRoleList.stream().filter(x -> roleId.equals(x.getId())).findAny()
                        .orElse(null);
                    if (result != null) {
                        return getGlobalRoleForRequestedApp(app.get(0).getId(), roleId);
                    }
                }
            }
            if (app.get(0).getId().equals(PortalConstants.PORTAL_APP_ID)) {
                roleInfo = getPortalAppRoleInfo(roleId);
            } else {
                roleInfo = getPartnerAppRoleInfo(roleId, app.get(0).getId());
            }
            roleList = createCentralRoleObject(app, roleInfo, roleList);
            if (roleList.isEmpty()) {
                return cenRole;
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "getRoleInfo: failed", e);
            throw e;
        }
        return roleList.get(0);
    }

    private CentralV2Role getGlobalRoleForRequestedApp(long requestedAppId, long roleId) {
        CentralV2Role finalGlobalrole;
        List<GlobalRoleWithApplicationRoleFunction> roleWithApplicationRoleFucntions = new ArrayList<>();
        try {
            roleWithApplicationRoleFucntions = entityManager
                .createNamedQuery("getGlobalRoleForRequestedApp")
                .setParameter("roleId", roleId)
                .setParameter("requestedAppId", requestedAppId)
                .getResultList();
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "getGlobalRoleForRequestedApp failed", e);
        }
        if (roleWithApplicationRoleFucntions.size() > 0) {
            List<CentralV2Role> rolesfinalList = finalListOfCentralRoles(roleWithApplicationRoleFucntions);
            finalGlobalrole = rolesfinalList.get(0);
        } else {
            List<FnRole> roleList = getPortalAppRoleInfo(roleId);
            finalGlobalrole = convertRoleToCentralV2Role(roleList.get(0));
        }
        return finalGlobalrole;
    }

    public EpAppFunction getRoleFunction(String functionCode, String uebkey) throws Exception {
        String code = EcompPortalUtils.getFunctionCode(functionCode);
        String encodedCode = EcompPortalUtils.encodeFunctionCode(code);
        EpAppFunction roleFunc = null;
        FnApp app = getApp(uebkey).get(0);
        List<EpAppFunction> getRoleFuncList = null;
        try {
            getRoleFuncList = epAppFunctionService.getRoleFunction(functionCode, app.getId());
            if (getRoleFuncList.isEmpty()) {
                getRoleFuncList = epAppFunctionService.getRoleFunction(encodedCode, app.getId());
                if (getRoleFuncList.isEmpty()) {
                    return roleFunc;
                }
            }
            if (getRoleFuncList.size() > 1) {
                EpAppFunction cenV2RoleFunction = appFunctionListFilter(encodedCode, getRoleFuncList);
                if (cenV2RoleFunction == null) {
                    return roleFunc;
                }
                roleFunc = checkIfPipesExitsInFunctionCode(cenV2RoleFunction);
            } else {
                // Check even if single record have pipes
                if (!getRoleFuncList.isEmpty() && getRoleFuncList.get(0).getFunctionCd().contains(FUNCTION_PIPE)) {
                    roleFunc = checkIfPipesExitsInFunctionCode(getRoleFuncList.get(0));
                } else {
                    roleFunc = getRoleFuncList.get(0);
                }
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "getRoleFunction: failed", e);
            throw e;
        }
        return roleFunc;
    }

    private EpAppFunction appFunctionListFilter(String roleFuncCode, List<EpAppFunction> roleFunction) {
        final Map<String, EpAppFunction> appFunctionsFilter = new HashMap<>();
        final Map<String, EpAppFunction> appFunctionsFilterPipes = new HashMap<>();
        EpAppFunction getExactFunctionCode;
        for (EpAppFunction cenRoleFunction : roleFunction) {
            appFunctionsFilter.put(cenRoleFunction.getFunctionCd(), cenRoleFunction);
            appFunctionsFilterPipes
                .put(EcompPortalUtils.getFunctionCode(cenRoleFunction.getFunctionCd()), cenRoleFunction);
        }
        getExactFunctionCode = appFunctionsFilter.get(roleFuncCode);
        if (getExactFunctionCode == null) {
            getExactFunctionCode = appFunctionsFilterPipes.get(roleFuncCode);
        }
        return getExactFunctionCode;
    }

    private EpAppFunction checkIfPipesExitsInFunctionCode(EpAppFunction getRoleFuncList) {
        EpAppFunction roleFunc;
        String functionCodeFormat = getRoleFuncList.getFunctionCd();
        if (functionCodeFormat.contains(FUNCTION_PIPE)) {
            String newfunctionCodeFormat = EcompPortalUtils.getFunctionCode(functionCodeFormat);
            String newfunctionTypeFormat = EcompPortalUtils.getFunctionType(functionCodeFormat);
            String newfunctionActionFormat = EcompPortalUtils.getFunctionAction(functionCodeFormat);
            roleFunc = new EpAppFunction(getRoleFuncList.getId(), newfunctionCodeFormat,
                getRoleFuncList.getFunctionName(), getRoleFuncList.getAppId(), newfunctionTypeFormat,
                newfunctionActionFormat, getRoleFuncList.getEditUrl());
        } else {
            roleFunc = EpAppFunction.builder()
                .id(getRoleFuncList.getId())
                .functionCd(functionCodeFormat)
                .functionName(getRoleFuncList.getFunctionName())
                .appId(getRoleFuncList.getAppId())
                .editUrl(getRoleFuncList.getEditUrl())
                .build();
        }
        return roleFunc;
    }

    public boolean saveCentralRoleFunction(EpAppFunction domainCentralRoleFunction, FnApp app)
        throws Exception {
        boolean saveOrUpdateFunction = false;
        try {
            if (EcompPortalUtils.checkFunctionCodeHasEncodePattern(domainCentralRoleFunction.getFunctionCd())) {
                domainCentralRoleFunction
                    .setFunctionCd(EcompPortalUtils.encodeFunctionCode(domainCentralRoleFunction.getFunctionCd()));
            }
            final Map<String, String> functionParams = new HashMap<>();
            functionParams.put("appId", String.valueOf(app.getId()));
            if (EcompPortalUtils.checkIfRemoteCentralAccessAllowed()) {
                addRoleFunctionInExternalSystem(domainCentralRoleFunction, app);
            }
            if (domainCentralRoleFunction.getType() != null && domainCentralRoleFunction.getAction() != null) {
                domainCentralRoleFunction.setFunctionCd(domainCentralRoleFunction.getType() + FUNCTION_PIPE
                    + domainCentralRoleFunction.getFunctionCd() + FUNCTION_PIPE + domainCentralRoleFunction
                    .getAction());
            }
            domainCentralRoleFunction.setAppId(app);
            epAppFunctionService.save(domainCentralRoleFunction);
            saveOrUpdateFunction = true;
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "saveCentralRoleFunction: failed", e);
            throw e;
        }
        return saveOrUpdateFunction;
    }

    private void addRoleFunctionInExternalSystem(EpAppFunction domainCentralRoleFunction, FnApp app)
        throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ExternalAccessPerms extPerms = new ExternalAccessPerms();
        HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
        String type = "";
        String instance = "";
        String action = "";
        if ((domainCentralRoleFunction.getType() != null && domainCentralRoleFunction.getAction() != null)
            || domainCentralRoleFunction.getFunctionCd().contains(FUNCTION_PIPE)) {
            type = domainCentralRoleFunction.getFunctionCd().contains(FUNCTION_PIPE)
                ? EcompPortalUtils.getFunctionType(domainCentralRoleFunction.getFunctionCd())
                : domainCentralRoleFunction.getType();
            instance = domainCentralRoleFunction.getFunctionCd().contains(FUNCTION_PIPE)
                ? EcompPortalUtils.getFunctionCode(domainCentralRoleFunction.getFunctionCd())
                : domainCentralRoleFunction.getFunctionCd();
            action = domainCentralRoleFunction.getFunctionCd().contains(FUNCTION_PIPE)
                ? EcompPortalUtils.getFunctionAction(domainCentralRoleFunction.getFunctionCd())
                : domainCentralRoleFunction.getAction();
        } else {
            type = domainCentralRoleFunction.getFunctionCd().contains("menu") ? "menu" : "url";
            instance = domainCentralRoleFunction.getFunctionCd();
            action = "*";
        }
        // get Permissions from External Auth System
        JSONArray extPermsList = getExtAuthPermissions(app.getAuthNamespace());
        List<ExternalAccessPermsDetail> permsDetailList = getExtAuthPerrmissonList(app, extPermsList);
        String requestedPerm = type + FUNCTION_PIPE + instance + FUNCTION_PIPE + action;
        boolean checkIfFunctionsExits = permsDetailList.stream()
            .anyMatch(permsDetail -> permsDetail.getInstance().equals(requestedPerm));
        if (!checkIfFunctionsExits) {
            try {
                extPerms.setAction(action);
                extPerms.setInstance(instance);
                extPerms.setType(app.getAuthNamespace() + "." + type);
                extPerms.setDescription(domainCentralRoleFunction.getFunctionName());
                String addFunction = mapper.writeValueAsString(extPerms);
                HttpEntity<String> entity = new HttpEntity<>(addFunction, headers);
                logger.debug(EELFLoggerDelegate.debugLogger, "addRoleFunctionInExternalSystem: {} for POST: {}",
                    CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE, addFunction);
                ResponseEntity<String> addPermResponse = template.exchange(
                    SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "perm",
                    HttpMethod.POST, entity, String.class);
                logger.debug(EELFLoggerDelegate.debugLogger,
                    "addRoleFunctionInExternalSystem: Finished adding permission for POST: {} and status code: {} ",
                    addPermResponse.getStatusCode().value(), addFunction);
            } catch (HttpClientErrorException e) {
                logger.error(EELFLoggerDelegate.errorLogger,
                    "HttpClientErrorException - Failed to add function in external central auth system", e);
                EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
                throw e;
            } catch (Exception e) {
                logger.error(EELFLoggerDelegate.errorLogger,
                    "addRoleFunctionInExternalSystem: Failed to add fucntion in external central auth system", e);
                throw e;
            }
        } else {
            try {
                extPerms.setAction(action);
                extPerms.setInstance(instance);
                extPerms.setType(app.getAuthNamespace() + "." + type);
                extPerms.setDescription(domainCentralRoleFunction.getFunctionName());
                String updateRoleFunction = mapper.writeValueAsString(extPerms);
                HttpEntity<String> entity = new HttpEntity<>(updateRoleFunction, headers);
                logger.debug(EELFLoggerDelegate.debugLogger, "addRoleFunctionInExternalSystem: {} for PUT: {}",
                    CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE, updateRoleFunction);
                ResponseEntity<String> updatePermResponse = template.exchange(
                    SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL) + "perm",
                    HttpMethod.PUT, entity, String.class);
                logger.debug(EELFLoggerDelegate.debugLogger,
                    "addRoleFunctionInExternalSystem: Finished updating permission in External Auth system {} and response: {} ",
                    updateRoleFunction, updatePermResponse.getStatusCode().value());
            } catch (HttpClientErrorException e) {
                logger.error(EELFLoggerDelegate.errorLogger,
                    "HttpClientErrorException - Failed to add function in external central auth system", e);
                EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
                throw e;
            } catch (Exception e) {
                logger.error(EELFLoggerDelegate.errorLogger,
                    "addRoleFunctionInExternalSystem: Failed to update function in external central auth system",
                    e);
                throw e;
            }
        }
    }

    public CentralRole convertV2CentralRoleToOldVerisonCentralRole(CentralV2Role v2CenRole) {
        SortedSet<EpAppFunction> cenRoleFuncList = new TreeSet<>();
        for (DomainVo vo : v2CenRole.getRoleFunctions()) {
            Optional<EpAppFunction> v2CenRoleFunc = epAppFunctionService.getForId(vo.getId());
            if (v2CenRoleFunc.isPresent()) {
                EpAppFunction roleFunc = EpAppFunction.builder()
                    .functionCd(v2CenRoleFunc.get().getFunctionCd())
                    .functionName(v2CenRoleFunc.get().getFunctionName())
                    .build();
                cenRoleFuncList.add(roleFunc);
            }

        }
        return new CentralRole(v2CenRole.getId(), v2CenRole.getName(), v2CenRole.isActive(), v2CenRole.getPriority(),
            cenRoleFuncList);
    }

    public List<EpAppFunction> getRoleFuncList(String uebkey) throws Exception {
        FnApp app = getApp(uebkey).get(0);
        List<EpAppFunction> finalRoleList = new ArrayList<>();
        List<EpAppFunction> getRoleFuncList = epAppFunctionService.getAllRoleFunctions(app.getId());
        for (EpAppFunction roleFuncItem : getRoleFuncList) {
            String code = EcompPortalUtils.getFunctionCode(roleFuncItem.getFunctionCd());
            String type = "";
            if (roleFuncItem.getFunctionCd().contains("|")) {
                type = EcompPortalUtils.getFunctionType(roleFuncItem.getFunctionCd());
            } else {
                type = getFunctionCodeType(roleFuncItem.getFunctionCd());
            }
            String action = getFunctionCodeAction(roleFuncItem.getFunctionCd());
            roleFuncItem.setFunctionCd(EPUserUtils.decodeFunctionCode(code));
            roleFuncItem.setType(type);
            roleFuncItem.setAction(action);
            finalRoleList.add(roleFuncItem);
        }
        return finalRoleList;
    }

    public List<CentralRoleFunction> convertCentralRoleFunctionToRoleFunctionObject(
        List<EpAppFunction> answer) {
        List<CentralRoleFunction> addRoleFuncList = new ArrayList<>();
        for (EpAppFunction cenRoleFunc : answer) {
            CentralRoleFunction setRoleFunc = new CentralRoleFunction();
            setRoleFunc.setCode(cenRoleFunc.getFunctionCd());
            setRoleFunc.setName(cenRoleFunc.getFunctionName());
            addRoleFuncList.add(setRoleFunc);
        }
        return addRoleFuncList;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCentralRoleFunction(String code, FnApp app) {
        boolean deleteFunctionResponse = false;
        try {
            List<EpAppFunction> domainCentralRoleFunction = epAppFunctionService.getRoleFunction(code, app.getId());
            EpAppFunction appFunctionCode = appFunctionListFilter(code, domainCentralRoleFunction);
            if (EcompPortalUtils.checkIfRemoteCentralAccessAllowed()) {
                deleteRoleFunctionInExternalSystem(appFunctionCode, app);
                // Delete role function dependency records
                deleteAppRoleFunctions(appFunctionCode.getFunctionCd(), app.getId());
            }
            epAppFunctionService.deleteOne(appFunctionCode);
            deleteFunctionResponse = true;
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "deleteCentralRoleFunction: failed", e);
        }
        return deleteFunctionResponse;
    }

    private void deleteRoleFunctionInExternalSystem(EpAppFunction domainCentralRoleFunction, FnApp app) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ExternalAccessPerms extPerms = new ExternalAccessPerms();
            String instanceValue = EcompPortalUtils.getFunctionCode(domainCentralRoleFunction.getFunctionCd());
            String checkType = getFunctionCodeType(domainCentralRoleFunction.getFunctionCd());
            String actionValue = getFunctionCodeAction(domainCentralRoleFunction.getFunctionCd());
            HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
            extPerms.setAction(actionValue);
            extPerms.setInstance(instanceValue);
            extPerms.setType(app.getAuthNamespace() + "." + checkType);
            extPerms.setDescription(domainCentralRoleFunction.getFunctionName());
            String deleteRoleFunction = mapper.writeValueAsString(extPerms);
            HttpEntity<String> entity = new HttpEntity<>(deleteRoleFunction, headers);
            logger.debug(EELFLoggerDelegate.debugLogger, "deleteRoleFunctionInExternalSystem: {} for DELETE: {} ",
                CONNECTING_TO_EXTERNAL_AUTH_SYSTEM_LOG_MESSAGE, deleteRoleFunction);
            ResponseEntity<String> delPermResponse = template
                .exchange(SystemProperties.getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_URL)
                    + "perm?force=true", HttpMethod.DELETE, entity, String.class);
            logger.debug(EELFLoggerDelegate.debugLogger,
                "deleteRoleFunctionInExternalSystem: Finished deleting permission in External Auth system {} and status code: {} ",
                deleteRoleFunction, delPermResponse.getStatusCode().value());
        } catch (HttpClientErrorException e) {
            logger.error(EELFLoggerDelegate.errorLogger,
                "HttpClientErrorException - Failed to delete functions in External System", e);
            EPLogUtil.logExternalAuthAccessAlarm(logger, e.getStatusCode());
        } catch (Exception e) {
            if (e.getMessage().equalsIgnoreCase("404 Not Found")) {
                logger.debug(EELFLoggerDelegate.debugLogger,
                    " deleteRoleFunctionInExternalSystem: It seems like function is already deleted in external central auth system  but exists in local DB",
                    e.getMessage());
            } else {
                logger.error(EELFLoggerDelegate.errorLogger,
                    "deleteRoleFunctionInExternalSystem: Failed to delete functions in External System", e);
            }
        }
    }

    private void deleteAppRoleFunctions(final String code, final Long appId) {
        epAppFunctionService.deleteByAppIdAndFunctionCd(appId, code);
    }

    public Integer updateAppRoleDescription(String uebkey) {
        Integer roleDescUpdated = 0;
        FnApp app;
        try {
            app = getApp(uebkey).get(0);
            List<FnRole> roles = getAppRoles(app.getId());
            for (FnRole epRole : roles) {
                Role role = new Role();
                role.setName(epRole.getRoleName());
                boolean status = addRoleDescriptionInExtSystem(role.getName(), app.getAuthNamespace());
                if (status)
                    roleDescUpdated++;
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "updateAppRoleDescription: Failed! ", e);
        }
        return roleDescUpdated;
    }
}
