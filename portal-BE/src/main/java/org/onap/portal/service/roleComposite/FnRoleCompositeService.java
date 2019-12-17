package org.onap.portal.service.roleComposite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FnRoleCompositeService {

    private final FnRoleCompositeDao fnRoleCompositeDao;

    @Autowired
    public FnRoleCompositeService(FnRoleCompositeDao fnRoleCompositeDao) {
        this.fnRoleCompositeDao = fnRoleCompositeDao;
    }
}
