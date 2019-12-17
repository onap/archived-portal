package org.onap.portal.service.function;

import org.onap.portal.domain.db.fn.FnFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FnFunctionService {

    private final FnFunctionDao fnFunctionDao;

    @Autowired
    public FnFunctionService(FnFunctionDao fnFunctionDao) {
        this.fnFunctionDao = fnFunctionDao;
    }

    public List<FnFunction> saveAll(List<FnFunction> fnFunctions) {
        return fnFunctionDao.saveAll(fnFunctions);
    }
}
