package org.onap.portal.service.luMenuSet;

import org.onap.portal.domain.db.fn.FnLuMenuSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FnLuMenuSetService {

    private final FnLuMenuSetDao fnLuMenuSetDao;

    @Autowired
    public FnLuMenuSetService(FnLuMenuSetDao fnLuMenuSetDao) {
        this.fnLuMenuSetDao = fnLuMenuSetDao;
    }

    public FnLuMenuSet save(FnLuMenuSet menuSet) {
        return fnLuMenuSetDao.save(menuSet);
    }
}
