package org.onap.portal.service.tab;

import org.onap.portal.domain.db.fn.FnTab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FnTabService {

    private final FnTabDao fnTabDao;

    @Autowired
    public FnTabService(FnTabDao fnTabDao) {
        this.fnTabDao = fnTabDao;
    }

    public List<FnTab> saveAll(List<FnTab> fnTabs) {
        return fnTabDao.saveAll(fnTabs);
    }
}
