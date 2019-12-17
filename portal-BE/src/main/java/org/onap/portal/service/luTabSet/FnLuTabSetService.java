package org.onap.portal.service.luTabSet;

import org.onap.portal.domain.db.fn.FnLuTabSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FnLuTabSetService {

    private final FnLuTabSetDao fnLuTabSetDao;

    @Autowired
    public FnLuTabSetService(FnLuTabSetDao fnLuTabSetDao) {
        this.fnLuTabSetDao = fnLuTabSetDao;
    }

    public FnLuTabSet save(FnLuTabSet fnLuTabSet) {
        return fnLuTabSetDao.save(fnLuTabSet);
    }
}
