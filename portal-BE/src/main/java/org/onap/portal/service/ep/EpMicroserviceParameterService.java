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
import java.util.stream.Collectors;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.onap.portal.dao.ep.EpMicroserviceParameterDao;
import org.onap.portal.domain.db.ep.EpMicroserviceParameter;
import org.onap.portal.domain.dto.ecomp.MicroserviceParameter;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EpMicroserviceParameterService {

       EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(EpWidgetCatalogParameterService.class);

       private final EpMicroserviceParameterDao epMicroserviceParameterDao;

       @Autowired
       public EpMicroserviceParameterService(
               final EpMicroserviceParameterDao epMicroserviceParameterDao) {
              this.epMicroserviceParameterDao = epMicroserviceParameterDao;
       }

       public List<MicroserviceParameter> getParametersById(long serviceId) {
              List<Criterion> restrictionsList = new ArrayList<>();
              Criterion contextIdCrit = Restrictions.eq("serviceId", serviceId);
              restrictionsList.add(contextIdCrit);
              List<MicroserviceParameter> list = mapToMicroserviceParameterList(epMicroserviceParameterDao.getParametersById(serviceId));
              logger.debug(EELFLoggerDelegate.debugLogger,
                      "getParametersById: microservice parameters list size: " + list.size());
              return list;
       }

       private MicroserviceParameter epWidgetCatalogParameterToMicroserviceParameter(
               final EpMicroserviceParameter microservice) {
              return new MicroserviceParameter(microservice.getId(), microservice.getServiceId().getId(),
                      microservice.getParaKey(), microservice.getParaValue());
       }

       private List<MicroserviceParameter> mapToMicroserviceParameterList(final List<EpMicroserviceParameter> list){
              return list.stream().map(this::epWidgetCatalogParameterToMicroserviceParameter).collect(Collectors.toList());
       }

       public EpMicroserviceParameter save(EpMicroserviceParameter epMicroserviceParameter){
              return epMicroserviceParameterDao.save(epMicroserviceParameter);
       }

       @Transactional
       public boolean deleteMicroserviceParameterById(final Long paramid){
              try {
                     epMicroserviceParameterDao.deleteById(paramid);
                     return true;
              }catch (Exception e){
                     logger.error(EELFLoggerDelegate.errorLogger, e.getMessage());
                     return false;
              }
       }
}
