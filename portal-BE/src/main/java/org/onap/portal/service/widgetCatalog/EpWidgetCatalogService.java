package org.onap.portal.service.widgetCatalog;

import org.onap.portal.domain.db.ep.EpWidgetCatalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EpWidgetCatalogService {

       private final EpWidgetCatalogDao epWidgetCatalogDao;

       @Autowired
       public EpWidgetCatalogService(EpWidgetCatalogDao epWidgetCatalogDao) {
              this.epWidgetCatalogDao = epWidgetCatalogDao;
       }

       public EpWidgetCatalog save(final EpWidgetCatalog epWidgetCatalog){
              return epWidgetCatalogDao.save(epWidgetCatalog);
       }

       public Optional<EpWidgetCatalog> findById(Long widgetId) {
              return epWidgetCatalogDao.findById(widgetId);
       }

       public List<EpWidgetCatalog> saveAll(List<EpWidgetCatalog> epWidgetCatalogs) {
              return epWidgetCatalogDao.saveAll(epWidgetCatalogs);
       }
}
