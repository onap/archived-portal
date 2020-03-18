package org.onap.portal.service.roleFunction;

import java.util.Optional;
import org.onap.portal.domain.db.fn.FnRoleFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FnRoleFunctionService {

    private final FnRoleFunctionDao fnRoleFunctionDao;

    @Autowired
    public FnRoleFunctionService(FnRoleFunctionDao fnRoleFunctionDao) {
        this.fnRoleFunctionDao = fnRoleFunctionDao;
    }

    public List<FnRoleFunction> saveAll(List<FnRoleFunction> roleFunctions) {
        return fnRoleFunctionDao.saveAll(roleFunctions);
    }

    public List<FnRoleFunction> findAll(){
        return fnRoleFunctionDao.findAll();
    }

    public Optional<FnRoleFunction> findById(final Long id) {
        return fnRoleFunctionDao.findById(id);
    }
}
