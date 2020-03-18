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

package org.onap.portal.service.appRoleFunction;

import javax.persistence.EntityManager;
import org.onap.portal.domain.db.ep.EpAppRoleFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EpAppRoleFunctionService {

    private final EpAppRoleFunctionDao epAppRoleFunctionDao;
    private final EntityManager entityManager;


    @Autowired
    public EpAppRoleFunctionService(final EpAppRoleFunctionDao epAppRoleFunctionDao,
        final EntityManager entityManager) {
        this.epAppRoleFunctionDao = epAppRoleFunctionDao;
        this.entityManager = entityManager;
    }

    public List<EpAppRoleFunction> saveAll(List<EpAppRoleFunction> epAppRoleFunctions) {
        return epAppRoleFunctionDao.saveAll(epAppRoleFunctions);
    }

    public EpAppRoleFunction save(EpAppRoleFunction apRoleFunction) {
        return epAppRoleFunctionDao.save(apRoleFunction);
    }

    public List<EpAppRoleFunction> getAppRoleFunctionOnRoleIdAndAppId(final long appId, final long roleId){
        return getAppRoleFunctionOnRoleIdAndAppId(appId, roleId);
    }

    public void deleteByAppIdAndFunctionCd(final Long appId, final String functionCd) {
        entityManager.createQuery("DELETE FROM ep_app_role_function WHERE app_id = :appId and function_cd = :functionCd")
            .setParameter("appId", appId)
            .setParameter("functionCd", functionCd)
            .executeUpdate();
    }

    public void deleteByAppIdAndFunctionCdAndRoleId(final Long appId, final String functionCd, final Integer roleId) {
        entityManager.createQuery("DELETE FROM ep_app_role_function WHERE app_id = :appId and function_cd = :functionCd and role_id = :roleId")
            .setParameter("appId", appId)
            .setParameter("functionCd", functionCd)
            .setParameter("roleId", roleId)
            .executeUpdate();
    }

    public void delete(EpAppRoleFunction approleFunction) {
        epAppRoleFunctionDao.delete(approleFunction);
    }

    public void deleteInBatch(List<EpAppRoleFunction> appRoleFunctionList) {
        epAppRoleFunctionDao.deleteInBatch(appRoleFunctionList);
    }
}
