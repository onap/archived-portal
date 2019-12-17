package org.onap.portal.service.tabSelected;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FnTabSelectedService {

    private final FnTabSelectedDao fnTabSelectedDao;

    @Autowired
    public FnTabSelectedService(FnTabSelectedDao fnTabSelectedDao) {
        this.fnTabSelectedDao = fnTabSelectedDao;
    }
}
