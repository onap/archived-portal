package org.onap.portal.service.sharedContext;

import org.onap.portal.domain.db.fn.FnSharedContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FnSharedContextService {

    private final FnSharedContextDao fnSharedContextDao;

    @Autowired
    public FnSharedContextService(FnSharedContextDao fnSharedContextDao) {
        this.fnSharedContextDao = fnSharedContextDao;
    }

    public List<FnSharedContext> saveAll(List<FnSharedContext> sharedContexts) {
        return fnSharedContextDao.saveAll(sharedContexts);
    }
}
