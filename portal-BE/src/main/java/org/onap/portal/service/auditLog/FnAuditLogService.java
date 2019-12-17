package org.onap.portal.service.auditLog;

import org.onap.portal.domain.db.fn.FnAuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FnAuditLogService {

    private final FnAuditLogDao fnAuditLogDao;

    @Autowired
    public FnAuditLogService(FnAuditLogDao fnAuditLogDao) {
        this.fnAuditLogDao = fnAuditLogDao;
    }

    public List<FnAuditLog> saveAll(List<FnAuditLog> auditLogs) {
        return fnAuditLogDao.saveAll(auditLogs);
    }
}
