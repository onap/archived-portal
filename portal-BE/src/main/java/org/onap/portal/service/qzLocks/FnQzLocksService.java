package org.onap.portal.service.qzLocks;

import org.onap.portal.domain.db.fn.FnQzLocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FnQzLocksService {

    private final FnQzLocksDao fnQzLocksDao;

    @Autowired
    public FnQzLocksService(FnQzLocksDao fnQzLocksDao) {
        this.fnQzLocksDao = fnQzLocksDao;
    }

    public List<FnQzLocks> saveAll(List<FnQzLocks> locks) {
        return fnQzLocksDao.saveAll(locks);
    }
}
