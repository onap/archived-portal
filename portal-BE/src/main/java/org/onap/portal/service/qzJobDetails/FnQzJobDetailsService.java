package org.onap.portal.service.qzJobDetails;

import org.onap.portal.domain.db.fn.FnQzJobDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FnQzJobDetailsService {

    private final FnQzJobDetailsDao fnQzJobDetailsDao;

    @Autowired
    public FnQzJobDetailsService(FnQzJobDetailsDao fnQzJobDetailsDao) {
        this.fnQzJobDetailsDao = fnQzJobDetailsDao;
    }

    public List<FnQzJobDetails> saveAll(List<FnQzJobDetails> jobDetails) {
        return fnQzJobDetailsDao.saveAll(jobDetails);
    }
}
