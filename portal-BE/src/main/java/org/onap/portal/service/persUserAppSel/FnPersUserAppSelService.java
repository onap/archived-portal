package org.onap.portal.service.persUserAppSel;

import org.onap.portal.domain.db.fn.FnPersUserAppSel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FnPersUserAppSelService {

    private final FnPersUserAppSelDao fnPersUserAppSelDao;

    @Autowired
    public FnPersUserAppSelService(FnPersUserAppSelDao fnPersUserAppSelDao) {
        this.fnPersUserAppSelDao = fnPersUserAppSelDao;
    }

    public List<FnPersUserAppSel> saveAll(List<FnPersUserAppSel> appSels) {
        return fnPersUserAppSelDao.saveAll(appSels);
    }
}
