package org.onap.portal.service.displayText;

import org.onap.portal.domain.db.fn.FnDisplayText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FnDisplayTextService {

    private final FnDisplayTextDao fnDisplayTextDao;

    @Autowired
    public FnDisplayTextService(FnDisplayTextDao fnDisplayTextDao) {
        this.fnDisplayTextDao = fnDisplayTextDao;
    }

    public List<FnDisplayText> saveAll(List<FnDisplayText> fnDisplayTexts) {
        return fnDisplayTextDao.saveAll(fnDisplayTexts);
    }
}
