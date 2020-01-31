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

import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.onap.portal.domain.dto.ecomp.EcompAppRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EcompAppRoleService {

    private final String notificationAppRoles =
    "select  a.app_id, a.app_name, b.role_id, b.role_name from\n"
        + "(select * from fn_app where app_id = 1) a,\n"
        + "(select * from fn_role where app_id is null and active_yn = 'Y' and role_id <> 1) b\n"
        + "union\n"
        + "select fn_role.app_id,fn_app.app_name, fn_role.role_id ,fn_role.role_name\n"
        + "from fn_app, fn_role\n"
        + "where fn_role.app_id = fn_app.app_id and fn_app.enabled='Y' and fn_role.active_yn='Y' order by app_name";

    private final EntityManager entityManager;

    @Autowired
    public EcompAppRoleService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<EcompAppRole> getAppRoleList() {
        return entityManager.createQuery(notificationAppRoles, EcompAppRole.class).getResultList();
    }
}
