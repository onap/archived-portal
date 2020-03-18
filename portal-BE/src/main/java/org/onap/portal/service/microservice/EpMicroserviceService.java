package org.onap.portal.service.microservice;

import java.util.Optional;
import org.onap.portal.domain.db.ep.EpMicroservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EpMicroserviceService {

    private final EpMicroserviceDao epMicroserviceDao;

    @Autowired
    public EpMicroserviceService(EpMicroserviceDao epMicroserviceDao) {
        this.epMicroserviceDao = epMicroserviceDao;
    }

    public List<EpMicroservice> saveAll(List<EpMicroservice> epMicroservices) {
        return epMicroserviceDao.saveAll(epMicroservices);
    }

    public Optional<EpMicroservice> getById(long serviceId) {
        return epMicroserviceDao.findById(serviceId);
    }
}
