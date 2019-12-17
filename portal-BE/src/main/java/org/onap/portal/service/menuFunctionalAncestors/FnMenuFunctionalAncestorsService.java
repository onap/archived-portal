package org.onap.portal.service.menuFunctionalAncestors;

import org.onap.portal.domain.db.fn.FnMenuFunctionalAncestors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FnMenuFunctionalAncestorsService {

    private final FnMenuFunctionalAncestorsDao fnMenuFunctionalAncestorsDao;

    @Autowired
    public FnMenuFunctionalAncestorsService(FnMenuFunctionalAncestorsDao fnMenuFunctionalAncestorsDao) {
        this.fnMenuFunctionalAncestorsDao = fnMenuFunctionalAncestorsDao;
    }

    public List<FnMenuFunctionalAncestors> saveAll(List<FnMenuFunctionalAncestors> ancestors) {
        return fnMenuFunctionalAncestorsDao.saveAll(ancestors);
    }
}
