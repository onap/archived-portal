package org.onap.portal.service.luActivity;

import org.onap.portal.domain.db.fn.FnLuActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FnLuActivityService {

    private final FnLuActivityDao fnLuActivityDao;

    @Autowired
    public FnLuActivityService(FnLuActivityDao fnLuActivityDao) {
        this.fnLuActivityDao = fnLuActivityDao;
    }

    public List<FnLuActivity> saveAll(List<FnLuActivity> luActivities) {
        return fnLuActivityDao.saveAll(luActivities);
    }
}
