package org.onap.portal.service.luPriority;

import org.onap.portal.domain.db.fn.FnLuPriority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FnLuPriorityService {

    private final FnLuPriorityDao fnLuPriorityDao;

    @Autowired
    public FnLuPriorityService(FnLuPriorityDao fnLuPriorityDao) {
        this.fnLuPriorityDao = fnLuPriorityDao;
    }

    public List<FnLuPriority> saveAll(List<FnLuPriority> priorities) {
        return fnLuPriorityDao.saveAll(priorities);
    }
}
