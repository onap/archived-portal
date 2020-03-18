package org.onap.portal.service;

import java.util.List;
import javax.persistence.EntityManager;
import org.onap.portal.domain.dto.ecomp.CentralizedApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CentralizedAppService {

    private final EntityManager entityManager;

    @Autowired
    public CentralizedAppService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<CentralizedApp> getCentralizedAppsOfUser(final String userId) {
        String query = "select distinct fa.app_id, fa.app_name "
            + "from  fn_role fr, fn_user_role fur, fn_app fa, fn_user fu "
            + "Where  fu.user_id =  fur.user_id and fur.role_id = fr.role_id and fa.app_id = fur.app_id "
            + "and fu.org_user_id = :userId and (fur.role_id = 999 or fur.role_id = 1) and fr.active_yn='Y' and ((fa.enabled = 'Y' and fa.auth_central='Y') or fa.app_id =1)";

        return entityManager.createQuery(query).getResultList();
    }
}
