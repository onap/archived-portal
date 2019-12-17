package org.onap.portal.service.restrictedUrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FnRestrictedUrlService {

    private final FnRestrictedUrlDao fnRestrictedUrlDao;

    @Autowired
    public FnRestrictedUrlService(FnRestrictedUrlDao fnRestrictedUrlDao) {
        this.fnRestrictedUrlDao = fnRestrictedUrlDao;
    }
}
