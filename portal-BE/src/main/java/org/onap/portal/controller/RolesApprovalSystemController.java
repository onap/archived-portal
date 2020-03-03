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
 *
 */

package org.onap.portal.controller;

import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.onap.portal.domain.dto.PortalRestResponse;
import org.onap.portal.domain.dto.PortalRestStatusEnum;
import org.onap.portal.domain.dto.model.ExternalSystemRoleApproval;
import org.onap.portal.domain.dto.model.ExternalSystemUser;
import org.onap.portal.domain.dto.transport.ExternalRequestFieldsValidator;
import org.onap.portal.service.AdminRolesService;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auxapi")
@Configuration
@EnableAspectJAutoProxy
public class RolesApprovalSystemController {

    private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(RolesApprovalSystemController.class);

    private final AdminRolesService userRolesService;

    @Autowired
    public RolesApprovalSystemController(AdminRolesService userRolesService) {
        this.userRolesService = userRolesService;
    }

    @ApiOperation(value = "Creates an application user with the specified roles.", response = PortalRestResponse.class)
    @RequestMapping(value = {"/userProfile"}, method = RequestMethod.POST, produces = "application/json")
    public PortalRestResponse<String> postUserProfile(HttpServletRequest request,
        @RequestBody ExternalSystemUser extSysUser, HttpServletResponse response) {
        ExternalRequestFieldsValidator reqResult = null;
        try {
            logger.info(EELFLoggerDelegate.debugLogger, "postUserProfile: request received for app {}, user {}",
                extSysUser.getApplicationName(), extSysUser.getLoginId());

            validateExtSystemUser(extSysUser, true);
            reqResult = userRolesService.setExternalRequestUserAppRole(extSysUser, "POST");
            if (!reqResult.isResult()) {
                throw new Exception(reqResult.getDetailMessage());
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "postUserProfile: failed for app {}, user {}",
                extSysUser.getApplicationName(), extSysUser.getLoginId(), e);
            if (reqResult == null || (!reqResult.isResult() && !e.getMessage().contains("404") && !e.getMessage()
                .contains("405"))) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR,
                    e.getMessage(), "save user profile failed");
            } else if (e.getMessage().contains("404")) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR,
                    e.getMessage(), "save user profile failed");
            } else if (e.getMessage().contains("500")) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR,
                    e.getMessage(), "save user profile failed");
            } else if (e.getMessage().contains("405")) {
                response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, e.getMessage(),
                    "save user profile failed");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, e.getMessage(),
                    "save user profile failed");
            }
        }
        return new PortalRestResponse<String>(PortalRestStatusEnum.OK, reqResult.getDetailMessage(), "Success");
    }

    @ApiOperation(value = "Updates an application user to have only the specified roles.", response = PortalRestResponse.class)
    @RequestMapping(value = {"/userProfile"}, method = RequestMethod.PUT, produces = "application/json")
    public PortalRestResponse<String> putUserProfile(HttpServletRequest request,
        @RequestBody ExternalSystemUser extSysUser, HttpServletResponse response) {
        ExternalRequestFieldsValidator reqResult = null;
        try {
            logger.info(EELFLoggerDelegate.debugLogger, "putUserProfile: request received for app {}, user {}",
                extSysUser.getApplicationName(), extSysUser.getLoginId());
            validateExtSystemUser(extSysUser, true);
            reqResult = userRolesService.setExternalRequestUserAppRole(extSysUser, "PUT");
            if (!reqResult.isResult()) {
                throw new Exception(reqResult.getDetailMessage());
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "putUserProfile: failed for app {}, user {}",
                extSysUser.getApplicationName(), extSysUser.getLoginId(), e);
            if (reqResult == null || (!reqResult.isResult() && !e.getMessage().contains("404") && !e.getMessage()
                .contains("405"))) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR,
                    e.getMessage(), "save user profile failed");
            } else if (e.getMessage().contains("404")) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR,
                    e.getMessage(), "save user profile failed");
            } else if (e.getMessage().contains("500")) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR,
                    e.getMessage(), "save user profile failed");
            } else if (e.getMessage().contains("405")) {
                response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, e.getMessage(),
                    "save user profile failed");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR,
                    e.getMessage(), "save user profile failed");
            }
        }
        return new PortalRestResponse<String>(PortalRestStatusEnum.OK, reqResult.getDetailMessage(), "Success");
    }

    @ApiOperation(value = "Processes a request to delete one or more application roles for one	specified user who has roles.", response = PortalRestResponse.class)
    @RequestMapping(value = {"/userProfile"}, method = RequestMethod.DELETE, produces = "application/json")
    public PortalRestResponse<String> deleteUserProfile(HttpServletRequest request,
        @RequestBody ExternalSystemUser extSysUser, HttpServletResponse response) {
        ExternalRequestFieldsValidator reqResult = null;
        try {
            logger.info(EELFLoggerDelegate.debugLogger, "deleteUserProfile: request received for app {}, user {}",
                extSysUser.getApplicationName(), extSysUser.getLoginId());
            validateExtSystemUser(extSysUser, false);
            // Ignore any roles that might be mistakenly present in the request
            extSysUser.setRoles(new ArrayList<ExternalSystemRoleApproval>());
            reqResult = userRolesService.setExternalRequestUserAppRole(extSysUser, "DELETE");
            if (!reqResult.isResult()) {
                throw new Exception(reqResult.getDetailMessage());
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "deleteUserProfile: failed for app {}, user {}",
                extSysUser.getApplicationName(), extSysUser.getLoginId(), e);
            if (reqResult == null || (!reqResult.isResult() && !e.getMessage().contains("404"))) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR,
                    e.getMessage(), "delete user profile failed");
            } else if (e.getMessage().contains("404")) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR,
                    e.getMessage(), "delete user profile failed");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR,
                    e.getMessage(), "delete user profile failed");
            }
        }
        return new PortalRestResponse<String>(PortalRestStatusEnum.OK, "Deleted Successfully", "Success");
    }

    private void validateExtSystemUser(ExternalSystemUser extSysUser, boolean rolesRequired) throws Exception {
        if (extSysUser.getLoginId() == null || extSysUser.getLoginId() == "") {
            throw new Exception("Request has no login ID");
        }
        if (extSysUser.getApplicationName() == null || extSysUser.getApplicationName() == "") {
            throw new Exception("Request has no application name");
        }
        if (extSysUser.getMyloginrequestId() == null) {
            throw new Exception("Request has no request ID");
        }
        if (rolesRequired && (extSysUser.getRoles() == null || extSysUser.getRoles().size() == 0)) {
            throw new Exception("Request has no roles");
        }
    }

}
