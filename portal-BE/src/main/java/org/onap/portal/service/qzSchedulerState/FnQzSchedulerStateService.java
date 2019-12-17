package org.onap.portal.service.qzSchedulerState;

import org.onap.portal.domain.db.fn.FnQzSchedulerState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FnQzSchedulerStateService {

    private final FnQzSchedulerStateDao fnQzSchedulerStateDao;

    @Autowired
    public FnQzSchedulerStateService(FnQzSchedulerStateDao fnQzSchedulerStateDao) {
        this.fnQzSchedulerStateDao = fnQzSchedulerStateDao;
    }

    public FnQzSchedulerState save(FnQzSchedulerState schedulerState) {
        return fnQzSchedulerStateDao.save(schedulerState);
    }
}
