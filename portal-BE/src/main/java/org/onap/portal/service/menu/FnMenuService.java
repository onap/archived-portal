package org.onap.portal.service.menu;

import org.onap.portal.domain.db.fn.FnMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FnMenuService {

    private final FnMenuDao fnMenuDao;

    @Autowired
    public FnMenuService(FnMenuDao fnMenuDao) {
        this.fnMenuDao = fnMenuDao;
    }

    public List<FnMenu> saveAll(List<FnMenu> fnMenus) {
        return fnMenuDao.saveAll(fnMenus);
    }
}
