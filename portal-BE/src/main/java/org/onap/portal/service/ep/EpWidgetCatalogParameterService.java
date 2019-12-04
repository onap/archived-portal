/*
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 * Modifications Copyright (c) 2019 Samsung
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Unless otherwise specified, all documentation contained herein is licensed
 * under the Creative Commons License, Attribution 4.0 Intl. (the "License");
 * you may not use this documentation except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             https://creativecommons.org/licenses/by/4.0/
 *
 * Unless required by applicable law or agreed to in writing, documentation
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ============LICENSE_END============================================
 *
 *
 */

package org.onap.portal.service.ep;

import java.util.ArrayList;
import java.util.List;
import org.onap.portal.dao.ep.EpWidgetCatalogParameterDao;
import org.onap.portal.domain.db.ep.EpWidgetCatalogParameter;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EpWidgetCatalogParameterService {

       final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(EpWidgetCatalogParameterService.class);

       private final EpWidgetCatalogParameterDao epWidgetCatalogParameterDao;
       private final EpMicroserviceParameterService epMicroserviceParameterService;

       @Autowired
       public EpWidgetCatalogParameterService(
               final EpWidgetCatalogParameterDao epWidgetCatalogParameterDao,
               final EpMicroserviceParameterService epMicroserviceParameterService) {
              this.epWidgetCatalogParameterDao = epWidgetCatalogParameterDao;
              this.epMicroserviceParameterService = epMicroserviceParameterService;
       }

       public List<EpWidgetCatalogParameter> getUserParameterById(final Long paramId) {
              return epWidgetCatalogParameterDao.retrieveByParamId(paramId).orElse(new ArrayList<>());
       }

       public boolean deleteUserParameterById(final Long paramId) {
              return (deleteByParamId(paramId) &&
                      epMicroserviceParameterService.deleteMicroserviceParameterById(paramId));
       }

       public EpWidgetCatalogParameter getById(final Long id){
              return epWidgetCatalogParameterDao.getOne(id);
       }

       @Transactional
       public boolean deleteByParamId(final Long paramId) {
              try {
                     epWidgetCatalogParameterDao.deleteWidgetCatalogParameter(paramId);
                     return true;
              } catch (Exception e) {
                     logger.error(EELFLoggerDelegate.errorLogger, e.getMessage());
                     return false;
              }
       }

       public EpWidgetCatalogParameter getUserParamById(final Long widgetId, final Long userId, final Long paramId) {
              EpWidgetCatalogParameter widgetParam = null;
              List<EpWidgetCatalogParameter> list = epWidgetCatalogParameterDao
                      .getUserParamById(widgetId, userId, paramId)
                      .orElse(null);
              if (list != null && !list.isEmpty()) {
                     widgetParam = list.get(0);
              }
              logger.debug(EELFLoggerDelegate.debugLogger,
                      "getUserParamById: widget parameters: " + widgetParam);
              return widgetParam;
       }

       public void saveUserParameter(final EpWidgetCatalogParameter newParameter) {
              epWidgetCatalogParameterDao.save(newParameter);
       }
}
