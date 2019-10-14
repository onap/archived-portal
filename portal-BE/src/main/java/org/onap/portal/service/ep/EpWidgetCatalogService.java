package org.onap.portal.service.ep;

import org.onap.portal.dao.fn.EpWidgetCatalogDao;
import org.onap.portal.domain.db.ep.EpWidgetCatalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
