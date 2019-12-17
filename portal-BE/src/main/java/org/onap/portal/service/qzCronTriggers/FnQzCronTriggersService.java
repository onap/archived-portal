package org.onap.portal.service.qzCronTriggers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FnQzCronTriggersService {

    private final FnQzCronTriggersDao fnQzCronTriggersDao;

    @Autowired
    public FnQzCronTriggersService(FnQzCronTriggersDao fnQzCronTriggersDao) {
        this.fnQzCronTriggersDao = fnQzCronTriggersDao;
    }
}
