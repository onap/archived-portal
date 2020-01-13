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

package org.onap.portal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.onap.portal.domain.db.ep.EpPersUserWidgetSel;
import org.onap.portal.domain.db.ep.EpWidgetCatalog;
import org.onap.portal.domain.dto.ecomp.PersUserWidgetSelection;
import org.onap.portal.domain.dto.transport.WidgetCatalogPersonalization;
import org.onap.portal.service.persUserWidgetSel.EpPersUserWidgetSelService;
import org.onap.portal.service.user.FnUserService;
import org.onap.portal.service.widgetCatalog.EpWidgetCatalogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@EnableAspectJAutoProxy
public class PersUserWidgetService {

       private static final Logger LOGGER = LoggerFactory.getLogger(PersUserWidgetService.class);
       private final EpPersUserWidgetSelService epPersUserWidgetSelService;
       private final EpWidgetCatalogService epWidgetCatalogService;
       private final FnUserService fnUserService;

       @Autowired
       public PersUserWidgetService(final EpPersUserWidgetSelService epPersUserWidgetSelService,
           final EpWidgetCatalogService epWidgetCatalogService,
           FnUserService fnUserService) {
              this.epPersUserWidgetSelService = epPersUserWidgetSelService;
              this.epWidgetCatalogService = epWidgetCatalogService;
              this.fnUserService = fnUserService;
       }

       public void setPersUserAppValue(final long userId, final WidgetCatalogPersonalization personalization) {
              List<PersUserWidgetSelection> persList = getUserWidgetSelction(userId, personalization.getWidgetId());
              LOGGER.info("Error: " + persList.size());
              // Key constraint limits to 1 row
              PersUserWidgetSelection persRow;
              if (persList.size() == 1) {
                     persRow = persList.get(0);
              } else {
                     persRow = new PersUserWidgetSelection(null, userId, personalization.getWidgetId(), null);
              }
              if (persRow.getId() != null) {
                     epPersUserWidgetSelService.deleteById(persRow.getId());
              }
              persRow.setStatusCode(personalization.getSelect() ? "S" : "H"); // Show / Hide
              EpPersUserWidgetSel epPersUserWidgetSel = new EpPersUserWidgetSel();
              epPersUserWidgetSel.setUserId(fnUserService.getUser(userId).get());
              EpWidgetCatalog catalog = epWidgetCatalogService.findById(personalization.getWidgetId()).orElse(new EpWidgetCatalog());
              epWidgetCatalogService.save(catalog);
              epPersUserWidgetSel.setWidgetId(catalog);
              epPersUserWidgetSelService.saveAndFlush(epPersUserWidgetSel);
       }

       private List<PersUserWidgetSelection> getUserWidgetSelction(final long userId, final long widgetId) {
              return epPersUserWidgetSelService
                      .getEpPersUserWidgetSelForUserIdAndWidgetId(userId, widgetId)
                      .orElse(new ArrayList<>())
                      .stream()
                      .map(this::epPersUserWidgetSelToPersUserWidgetSelection)
                      .collect(Collectors.toList());
       }

       private PersUserWidgetSelection epPersUserWidgetSelToPersUserWidgetSelection(final EpPersUserWidgetSel widgetSel) {
              return new PersUserWidgetSelection(widgetSel.getId(), widgetSel.getUserId().getId(),
                      widgetSel.getWidgetId().getWidgetId(), widgetSel.getStatusCd());
       }
}
