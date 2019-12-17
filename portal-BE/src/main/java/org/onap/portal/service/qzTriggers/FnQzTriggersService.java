package org.onap.portal.service.qzTriggers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FnQzTriggersService {

    private final FnQzTriggersDao fnQzTriggersDao;

    @Autowired
    public FnQzTriggersService(FnQzTriggersDao fnQzTriggersDao) {
        this.fnQzTriggersDao = fnQzTriggersDao;
    }
}
