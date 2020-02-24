package org.onap.portal.service;

import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.onap.portal.domain.dto.transport.LocalRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class LocalRoleService {

    private final EntityManager entityManager;

    @Autowired
    public LocalRoleService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<LocalRole> getCurrentAppRoleFunctions(final long appId, final String functionCd){
        final String query = "select distinct fr.role_id, fr.role_name  from fn_role fr, ep_app_function ef, ep_app_role_function epr where fr.role_id = epr.role_id and epr.function_cd = ef.function_cd\n"
            + " and ef.function_cd =:functionCd and epr.app_id =:appId";
        return entityManager.createNamedQuery(query).setParameter("appId", appId).setParameter("functionCd", functionCd).getResultList();
    }
}
