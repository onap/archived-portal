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
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.dto.ecomp.PersUserWidgetSelection;
import org.onap.portal.domain.dto.transport.WidgetCatalogPersonalization;
import org.onap.portal.service.persUserWidgetSel.EpPersUserWidgetSelService;
import org.onap.portal.service.widgetCatalog.EpWidgetCatalogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PersUserWidgetService {

       private static final Logger LOGGER = LoggerFactory.getLogger(PersUserWidgetService.class);
       private final EpPersUserWidgetSelService epPersUserWidgetSelService;
       private final EpWidgetCatalogService epWidgetCatalogService;

       @Autowired
       public PersUserWidgetService(final EpPersUserWidgetSelService epPersUserWidgetSelService,
               final EpWidgetCatalogService epWidgetCatalogService) {
              this.epPersUserWidgetSelService = epPersUserWidgetSelService;
              this.epWidgetCatalogService = epWidgetCatalogService;
       }

       public void setPersUserAppValue(FnUser user, WidgetCatalogPersonalization personalization) {
              List<PersUserWidgetSelection> persList = getUserWidgetSelction(user, personalization.getWidgetId());
              LOGGER.info("Error: " + persList.size());
              // Key constraint limits to 1 row
              PersUserWidgetSelection persRow;
              if (persList.size() == 1) {
                     persRow = persList.get(0);
              } else {
                     persRow = new PersUserWidgetSelection(null, user.getId(), personalization.getWidgetId(), null);
              }

              if (persRow.getId() != null) {
                     epPersUserWidgetSelService.deleteById(persRow.getId());
              }

              persRow.setStatusCode(personalization.getSelect() ? "S" : "H"); // Show / Hide
              EpPersUserWidgetSel epPersUserWidgetSel = new EpPersUserWidgetSel();
              epPersUserWidgetSel.setUserId(user);
              epPersUserWidgetSel.setWidgetId(
                      epWidgetCatalogService.findById(personalization.getWidgetId()).orElse(new EpWidgetCatalog()));
              epPersUserWidgetSelService.saveAndFlush(epPersUserWidgetSel);
       }

       private List<PersUserWidgetSelection> getUserWidgetSelction(FnUser user, Long widgetId) {
              return epPersUserWidgetSelService
                      .getEpPersUserWidgetSelForUserIdAndWidgetId(user.getId(), widgetId)
                      .orElse(new ArrayList<>())
                      .stream()
                      .map(this::epPersUserWidgetSelToPersUserWidgetSelection)
                      .collect(Collectors.toList());
       }

       private PersUserWidgetSelection epPersUserWidgetSelToPersUserWidgetSelection(EpPersUserWidgetSel widgetSel) {
              return new PersUserWidgetSelection(widgetSel.getId(), widgetSel.getUserId().getId(),
                      widgetSel.getWidgetId().getWidgetId(), widgetSel.getStatusCd());
       }
}
