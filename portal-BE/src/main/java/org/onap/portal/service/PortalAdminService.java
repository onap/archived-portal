package org.onap.portal.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import org.onap.portal.domain.db.fn.FnApp;
import org.onap.portal.domain.db.fn.FnRole;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.db.fn.FnUserRole;
import org.onap.portal.domain.dto.transport.ExternalAccessUser;
import org.onap.portal.domain.dto.transport.FieldsValidator;
import org.onap.portal.domain.dto.transport.PortalAdmin;
import org.onap.portal.restTemplates.AAFTemplate;
import org.onap.portal.service.app.FnAppService;
import org.onap.portal.service.role.FnRoleService;
import org.onap.portal.service.user.FnUserService;
import org.onap.portal.service.userRole.FnUserRoleService;
import org.onap.portal.utils.EPCommonSystemProperties;
import org.onap.portal.utils.EcompPortalUtils;
import org.onap.portal.utils.PortalConstants;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class PortalAdminService {

    private final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(PortalAdminService.class);

    private String SYS_ADMIN_ROLE_ID = "1";
    private String ECOMP_APP_ID = "1";

    private final ExternalAccessRolesService externalAccessRolesService;
    private final FnAppService fnAppService;
    private final FnRoleService fnRoleService;
    private final FnUserRoleService fnUserRoleService;
    private final FnUserService fnUserService;
    private final EntityManager entityManager;
    private final AAFTemplate aafTemplate;

    @Autowired
    public PortalAdminService(ExternalAccessRolesService externalAccessRolesService,
        FnAppService fnAppService, FnRoleService fnRoleService,
        FnUserRoleService fnUserRoleService, FnUserService fnUserService,
        EntityManager entityManager, AAFTemplate aafTemplate) {
        this.externalAccessRolesService = externalAccessRolesService;
        this.fnAppService = fnAppService;
        this.fnRoleService = fnRoleService;
        this.fnUserRoleService = fnUserRoleService;
        this.fnUserService = fnUserService;
        this.entityManager = entityManager;
        this.aafTemplate = aafTemplate;
    }

    @PostConstruct
    public void init() {
        SYS_ADMIN_ROLE_ID = SystemProperties.getProperty(SystemProperties.SYS_ADMIN_ROLE_ID);
        ECOMP_APP_ID = SystemProperties.getProperty(EPCommonSystemProperties.ECOMP_APP_ID);
    }


    @SuppressWarnings("unchecked")
    public List<PortalAdmin> getPortalAdmins() {
        try {
            List<PortalAdmin> portalAdmins = entityManager.createNamedQuery("PortalAdminDTO")
                .setParameter("adminRoleId", SYS_ADMIN_ROLE_ID).getResultList();
            logger.debug(EELFLoggerDelegate.debugLogger, "getPortalAdmins was successful");
            return portalAdmins;
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "getPortalAdmins failed", e);
            return null;
        }
    }

    public FieldsValidator createPortalAdmin(String orgUserId) {
        FieldsValidator fieldsValidator = new FieldsValidator();
        logger.debug(EELFLoggerDelegate.debugLogger, "LR: createPortalAdmin: orgUserId is {}", orgUserId);
        FnUser user = null;
        boolean createNewUser = false;
        List<FnUser> localUserList = fnUserService.getUserWithOrgUserId(orgUserId);
        if (!localUserList.isEmpty()) {
            user = localUserList.get(0);
        } else {
            createNewUser = true;
        }

        if (user != null && isLoggedInUserPortalAdmin(user.getId())) {
            fieldsValidator.setHttpStatusCode((long) HttpServletResponse.SC_CONFLICT);
            logger.error(EELFLoggerDelegate.errorLogger,
                "User '" + user.getOrgUserId() + "' already has PortalAdmin role assigned.");
        } else if (user != null || createNewUser) {
            try {
                if (createNewUser) {
                    user = fnUserService.getUserWithOrgUserId(orgUserId).get(0);
                    if (user != null) {
                        user.setActiveYn(true);
                        fnUserService.save(user);
                    }
                }
                if (user != null) {
                    FnUserRole userRole = new FnUserRole();
                    userRole.setUserId(user);
                    userRole.setRoleId(fnRoleService.getById(Long.valueOf(SYS_ADMIN_ROLE_ID)));
                    userRole.setFnAppId(fnAppService.getById(Long.valueOf(ECOMP_APP_ID)));
                    fnUserRoleService.saveOne(userRole);
                }
                if (user != null && EcompPortalUtils.checkIfRemoteCentralAccessAllowed()) {
                    List<FnRole> roleList = externalAccessRolesService
                        .getPortalAppRoleInfo(PortalConstants.SYS_ADMIN_ROLE_ID);
                    FnRole role = new FnRole();
                    if (roleList.size() > 0) {
                        role = roleList.get(0);
                    }
                    logger.debug(EELFLoggerDelegate.debugLogger, "Requested RoleName is  " + role.getRoleName());
                    addPortalAdminInExternalCentralAuth(user.getOrgUserId(), role.getRoleName());
                }
            } catch (Exception e) {
                logger.error(EELFLoggerDelegate.errorLogger, "createPortalAdmin failed", e);
                fieldsValidator.setHttpStatusCode((long) HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
        return fieldsValidator;
    }

    private void addPortalAdminInExternalCentralAuth(String loginId, String portalAdminRole) throws Exception {
        try {
            String name = "";
            if (EPCommonSystemProperties.containsProperty(
                EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN)) {
                name = loginId + SystemProperties
                    .getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN);
            }
            //TODO HARDCODED ID
            FnApp app = fnAppService.getById(PortalConstants.PORTAL_APP_ID);
            String extRole = app.getAuthNamespace() + "." + portalAdminRole.replaceAll(" ", "_");
            ObjectMapper addUserRoleMapper = new ObjectMapper();
            ExternalAccessUser extUser = new ExternalAccessUser(name, extRole);
            String userRole = addUserRoleMapper.writeValueAsString(extUser);
            HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
            aafTemplate.addPortalAdminInAAF(new HttpEntity<>(userRole, headers));
        } catch (Exception e) {
            if (e.getMessage().equalsIgnoreCase("409 Conflict")) {
                logger.debug(EELFLoggerDelegate.debugLogger, "Portal Admin role already exists", e.getMessage());
            } else {
                logger.error(EELFLoggerDelegate.errorLogger, "Failed to add Portal Admin role ", e);
                throw e;
            }
        }
    }

    public FieldsValidator deletePortalAdmin(Long userId) {
        FieldsValidator fieldsValidator = new FieldsValidator();
        logger.debug(EELFLoggerDelegate.debugLogger, "deletePortalAdmin: test 1");
        try {
            //TODO HARDCODED ID
            fnUserRoleService.deleteByUserIdAndRoleId(userId, SYS_ADMIN_ROLE_ID);
            if (EcompPortalUtils.checkIfRemoteCentralAccessAllowed()) {

                List<FnRole> roleList = externalAccessRolesService
                    .getPortalAppRoleInfo(PortalConstants.SYS_ADMIN_ROLE_ID);
                FnRole role = new FnRole();
                if (roleList.size() > 0) {
                    role = roleList.get(0);
                }
                logger.debug(EELFLoggerDelegate.debugLogger, "Requested RoleName is  " + role.getRoleName());
                deletePortalAdminInExternalCentralAuth(userId, role.getRoleName());
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "deletePortalAdmin failed", e);
            fieldsValidator.setHttpStatusCode((long) HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return fieldsValidator;
    }


    private void deletePortalAdminInExternalCentralAuth(Long userId, String portalAdminRole) throws Exception {
        try {
            String name = "";
            FnUser localUserList = fnUserService.getUser(userId)
                .orElseThrow(() -> new EntityExistsException("User with id:" + userId + "do not exists."));
            if (EPCommonSystemProperties.containsProperty(
                EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN)) {
                name = localUserList.getOrgUserId() + SystemProperties
                    .getProperty(EPCommonSystemProperties.EXTERNAL_CENTRAL_ACCESS_USER_DOMAIN);
            }
            //TODO HARDCODED ID
            FnApp app = fnAppService.getById(PortalConstants.PORTAL_APP_ID);
            String extRole = app.getAuthNamespace() + "." + portalAdminRole.replaceAll(" ", "_");
            HttpHeaders headers = EcompPortalUtils.base64encodeKeyForAAFBasicAuth();
            aafTemplate.deletePortalAdminFromAAF(name, extRole, new HttpEntity<>(headers));
        } catch (Exception e) {
            if (e.getMessage().equalsIgnoreCase("404 Not Found")) {
                logger.debug(EELFLoggerDelegate.debugLogger, "Portal Admin role already deleted or may not be found",
                    e.getMessage());
            } else {
                logger.error(EELFLoggerDelegate.errorLogger, "Failed to add Portal Admin role ", e);
                throw e;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private boolean isLoggedInUserPortalAdmin(Long userId) {
        try {
            List<PortalAdmin> portalAdmins = entityManager.createNamedQuery("ActivePortalAdminDTO")
                .setParameter("userId", userId)
                .setParameter("adminRoleId", SYS_ADMIN_ROLE_ID)
                .getResultList();
            logger.debug(EELFLoggerDelegate.debugLogger, portalAdmins.toString());
            return portalAdmins.size() > 0;
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "isLoggedInUserPortalAdmin failed", e);
            return false;
        }
    }
}
