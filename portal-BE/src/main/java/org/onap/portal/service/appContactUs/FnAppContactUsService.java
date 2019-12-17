package org.onap.portal.service.appContactUs;

import org.onap.portal.domain.db.fn.FnAppContactUs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FnAppContactUsService {

    private final FnAppContactUsDao fnAppContactUsDao;

    @Autowired
    public FnAppContactUsService(FnAppContactUsDao fnAppContactUsDao) {
        this.fnAppContactUsDao = fnAppContactUsDao;
    }

    public List<FnAppContactUs> saveAll(List<FnAppContactUs> fnAppContactUses) {
        return fnAppContactUsDao.saveAll(fnAppContactUses);
    }
}
