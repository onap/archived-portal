package org.onap.portal.service.basicAuthAccount;

import org.onap.portal.domain.db.ep.EpBasicAuthAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EpBasicAuthAccountService {

    private final EpBasicAuthAccountDao epBasicAuthAccountDao;

    @Autowired
    public EpBasicAuthAccountService(EpBasicAuthAccountDao epBasicAuthAccountDao) {
        this.epBasicAuthAccountDao = epBasicAuthAccountDao;
    }

    public EpBasicAuthAccount save(EpBasicAuthAccount epBasicAuthAccount) {
        return epBasicAuthAccountDao.save(epBasicAuthAccount);
    }
}
