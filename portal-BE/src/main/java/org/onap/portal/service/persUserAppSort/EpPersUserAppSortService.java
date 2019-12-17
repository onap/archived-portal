package org.onap.portal.service.persUserAppSort;

import org.onap.portal.domain.db.ep.EpPersUserAppSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EpPersUserAppSortService {

    private final EpPersUserAppSortDao epPersUserAppSortDao;

    @Autowired
    public EpPersUserAppSortService(EpPersUserAppSortDao epPersUserAppSortDao) {
        this.epPersUserAppSortDao = epPersUserAppSortDao;
    }

    public EpPersUserAppSort save(EpPersUserAppSort appSort) {
        return epPersUserAppSortDao.save(appSort);
    }
}
