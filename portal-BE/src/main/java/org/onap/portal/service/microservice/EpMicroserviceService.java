package org.onap.portal.service.microservice;

import java.util.Optional;
import java.util.Set;
import org.onap.portal.domain.db.ep.EpMicroservice;
import org.onap.portal.domain.db.ep.EpMicroserviceParameter;
import org.onap.portal.service.microserviceParameter.EpMicroserviceParameterService;
import org.onap.portal.utils.EPCommonSystemProperties;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EpMicroserviceService {

    EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(EpMicroserviceService.class);

    private final EpMicroserviceDao epMicroserviceDao;
    private final EpMicroserviceParameterService epMicroserviceParameterService;

    @Autowired
    public EpMicroserviceService(EpMicroserviceDao epMicroserviceDao,
        EpMicroserviceParameterService epMicroserviceParameterService) {
        this.epMicroserviceDao = epMicroserviceDao;
        this.epMicroserviceParameterService = epMicroserviceParameterService;
    }

    public List<EpMicroservice> saveAll(List<EpMicroservice> epMicroservices) {
        return epMicroserviceDao.saveAll(epMicroservices);
    }

    public Optional<EpMicroservice> getById(long serviceId) {
        return epMicroserviceDao.findById(serviceId);
    }

    public EpMicroservice saveOne(EpMicroservice newServiceData) {
        return epMicroserviceDao.save(newServiceData);
    }

    public List<EpMicroservice> getAll() {
        return epMicroserviceDao.findAll();
    }

    public void deleteById(long serviceId) {
        epMicroserviceDao.deleteById(serviceId);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void updateMicroservice(long serviceId, EpMicroservice newServiceData) throws Exception {
        EpMicroservice newService = getById(serviceId).get();
        try {
            newService.setId(serviceId);
            if (newService.getPassword() != null) {
                if (newService.getPassword().equals(EPCommonSystemProperties.APP_DISPLAY_PASSWORD)) {
                    EpMicroservice oldMS = getById(serviceId).get();
                    newService.setPassword(oldMS.getPassword()); // keep the old password
                } else {
                    newService.setPassword(encryptedPassword(newService.getPassword())); //new password
                }
            }
            saveOne(newService);
            List<EpMicroserviceParameter> oldService = epMicroserviceParameterService.getByServiceId(serviceId);
            boolean foundParam;
            for (EpMicroserviceParameter microserviceParameter : oldService) {
                foundParam = false;
                for (EpMicroserviceParameter service : newService.getEpMicroserviceParameters()) {
                    if (service.getId().equals(microserviceParameter.getId())) {
                        foundParam = true;
                        break;
                    }
                }
                if (!foundParam) {
                    epMicroserviceParameterService.deleteOne(microserviceParameter);
                }
            }
            for (EpMicroserviceParameter param : newService.getEpMicroserviceParameters()) {
                param.setServiceId(newService);
                epMicroserviceParameterService.save(param);
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "updateMicroservice failed", e);
            throw e;
        }
        saveServiceParameters(newService.getId(), newService.getEpMicroserviceParameters());
    }

    public void saveServiceParameters(Long newServiceId, Set<EpMicroserviceParameter> list) {
        EpMicroservice newService = getById(newServiceId).get();
        for (EpMicroserviceParameter para : list) {
            para.setServiceId(newService);
            epMicroserviceParameterService.save(para);
        }
    }

    private String encryptedPassword(String decryptedPwd) throws Exception {
        String result = "";
        if (decryptedPwd != null && !decryptedPwd.isEmpty()) {
            try {
                result = CipherUtil.encryptPKC(decryptedPwd,
                    SystemProperties.getProperty(SystemProperties.Decryption_Key));
            } catch (Exception e) {
                logger.error(EELFLoggerDelegate.errorLogger, "encryptedPassword failed", e);
                throw e;
            }
        }
        return result;
    }
}
