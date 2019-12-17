package org.onap.portal.service.luAlertMethod;

import org.onap.portal.domain.db.fn.FnLuAlertMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FnLuAlertMethodService {

    private final FnLuAlertMethodDao fnLuAlertMethodDao;

    @Autowired
    public FnLuAlertMethodService(FnLuAlertMethodDao fnLuAlertMethodDao) {
        this.fnLuAlertMethodDao = fnLuAlertMethodDao;
    }

    public List<FnLuAlertMethod> saveAll(List<FnLuAlertMethod> alertMethods) {
        return fnLuAlertMethodDao.saveAll(alertMethods);
    }
}
