package org.onap.portal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.onap.portal.domain.dto.transport.BulkUploadRoleFunction;
import org.onap.portal.domain.dto.transport.BulkUploadUserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
class BulkUploadUserRolesService {

    private static final String getBulkUsersForSingleRole = "select fr.role_name as roleName, fu.org_user_id as orgUserId, fa.auth_namespace as appNameSpace from fn_user_role fur "
        + "left outer join fn_role fr on fr.role_id = fur.role_id "
        + "left outer join fn_app fa on fa.app_id = fur.app_id "
        + "left outer join fn_user fu on fu.user_id = fur.user_id "
        + "where fa.ueb_key =:uebKey and fr.role_id =:roleId";

    private static final String uploadAllRoleFunctions = "select fr.function_cd, fn.function_name from fn_role_function fr "
        + "left outer join fn_function fn on fr.function_cd = fn.function_cd "
        + "where role_id =:roleId";
    
    private static final String getBulkUserRoles = "select fr.role_name, fu.org_user_id, fa.auth_namespace from fn_user_role fur "
        + "left outer join fn_role fr on fr.role_id = fur.role_id "
        + "left outer join fn_app fa on fa.app_id = fur.app_id "
        + "left outer join fn_user fu on fu.user_id = fur.user_id where fa.ueb_key =:uebKey";

    private static final String uploadPartnerRoleFunctions = "select distinct eprf.function_cd, epfn.function_name "
        + "from ep_app_role_function eprf "
        + "left outer join ep_app_function epfn on eprf.function_cd = epfn.function_cd "
        + "where eprf.role_id =:roleId";

    private final EntityManager entityManager;

    @Autowired
    public BulkUploadUserRolesService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    List<BulkUploadUserRoles>  getBulkUsersForSingleRole(final String uebKey, final Long roleId){
        return Optional.of(entityManager
            .createQuery(getBulkUsersForSingleRole)
            .setParameter("uebKey", uebKey)
            .setParameter("roleId", roleId)
            .getResultList()).orElse(new ArrayList());
    }

    List<BulkUploadRoleFunction> uploadAllRoleFunctions(final Long roleId){
        return Optional.of(entityManager
            .createQuery(uploadAllRoleFunctions)
            .setParameter("roleId", roleId)
            .getResultList()).orElse(new ArrayList());
    }

    public List<BulkUploadUserRoles> getBulkUserRoles(String uebKey) {
        return Optional.of(entityManager
            .createQuery(getBulkUserRoles)
            .setParameter("uebKey", uebKey)
            .getResultList()).orElse(new ArrayList());
    }

    public List<BulkUploadRoleFunction> uploadPartnerRoleFunctions(Long roleId) {
        return Optional.of(entityManager
            .createQuery(uploadPartnerRoleFunctions)
            .setParameter("roleId", roleId)
            .getResultList()).orElse(new ArrayList());
    }
}
