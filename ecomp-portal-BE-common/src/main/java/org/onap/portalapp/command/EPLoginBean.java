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
package org.onap.portalapp.command;

import java.util.Set;

import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalsdk.core.domain.support.FusionCommand;

public class EPLoginBean extends FusionCommand {
    private String loginId;
    private String loginPwd;
    private String hrid;
    private String orgUserId;
    private String siteAccess;
    private String loginErrorMessage;

    private EPUser user;
    private Set<?>  menu;
    private Set<?>  businessDirectMenu;

    /**
     * getLoginId
     *
     * @return String
     */
    public String getLoginId() {
        return loginId;
    }

    /**
     * getLoginPwd
     *
     * @return String
     */
    public String getLoginPwd() {
        return loginPwd;
    }

    /**
     * getMenu
     *
     * @return Set
     */
    public Set<?> getMenu() {
        return menu;
    }

    /**
     * getUser
     *
     * @return User
     */
    public EPUser getUser() {
        return user;
    }

    /**
     * getHrid
     *
     * @return String
     */
    public String getHrid() {
        return hrid;
    }

    /**
     * getSiteAccess
     *
     * @return String
     */
    public String getSiteAccess() {
        return siteAccess;
    }

    /**
     * getBusinessDirectMenu
     *
     * @return Set
     */
    public Set<?> getBusinessDirectMenu() {
        return businessDirectMenu;
    }

    /**
     * getLoginErrorMessage
     *
     * @return String
     */
    public String getLoginErrorMessage() {
        return loginErrorMessage;
    }

    public String getOrgUserId() {
        return orgUserId;
    }

    /**
     * setLoginId
     *
     * @param loginId String
     */
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    /**
     * setLoginPwd
     *
     * @param loginPwd String
     */
    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public void setMenu(Set<?> menu) {
        this.menu = menu;
    }

    /**
     * setUser
     *
     * @param user User
     */
    public void setUser(EPUser user) {
        this.user = user;
    }

    /**
     * setHrid
     *
     * @param hrid String
     */
    public void setHrid(String hrid) {
        this.hrid = hrid;
    }

    /**
     * setSiteAccess
     *
     * @param siteAccess String
     */
    public void setSiteAccess(String siteAccess) {
        this.siteAccess = siteAccess;
    }

    /**
     * setBusinessDirectMenu
     *
     * @param businessDirectMenu Set
     */
    public void setBusinessDirectMenu(Set<?> businessDirectMenu) {
        this.businessDirectMenu = businessDirectMenu;
    }

    /**
     * setLoginErrorMessage
     *
     * @param loginErrorMessage String
     */
    public void setLoginErrorMessage(String loginErrorMessage) {
        this.loginErrorMessage = loginErrorMessage;
    }

    public void setOrgUserId(String orgUserId) {
        this.orgUserId = orgUserId;
    }
}

