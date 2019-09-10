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
import org.onap.portal.dao.ep.EpPersUserWidgetSelDao;
import org.onap.portal.dao.fn.EpWidgetCatalogDao;
import org.onap.portal.domain.db.ep.EpPersUserWidgetSel;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.dto.ecomp.PersUserWidgetSelection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersUserWidgetService {

       private final EpPersUserWidgetSelDao epPersUserWidgetSelDao;
       private final EpWidgetCatalogDao epWidgetCatalogDao;

       @Autowired
       public PersUserWidgetService(final EpPersUserWidgetSelDao epPersUserWidgetSelDao,
               final EpWidgetCatalogDao epWidgetCatalogDao) {
              this.epPersUserWidgetSelDao = epPersUserWidgetSelDao;
              this.epWidgetCatalogDao = epWidgetCatalogDao;
       }

       public void setPersUserAppValue(FnUser user, Long widgetId, Boolean select) {
              if (user == null || widgetId == null) {
                     throw new IllegalArgumentException("setPersUserAppValue: Null values");
              }

              List<PersUserWidgetSelection> persList = getUserWidgetSelction(user, widgetId);
              // Key constraint limits to 1 row
              PersUserWidgetSelection persRow = null;
              if (persList.size() == 1) {
                     persRow = persList.get(0);
              } else {
                     persRow = new PersUserWidgetSelection(null, user.getId(), widgetId, null);
              }
              if (select) {
                     if (persRow.getId() != null) {
                            epPersUserWidgetSelDao.deleteById(persRow.getId());
                     }
                     persRow.setStatusCode("S"); // show
                     EpPersUserWidgetSel epPersUserWidgetSel = new EpPersUserWidgetSel();
                     epPersUserWidgetSel.setUserId(user);
                     epPersUserWidgetSel.setWidgetId(epWidgetCatalogDao.findById(widgetId).get());
                     epPersUserWidgetSelDao.saveAndFlush(epPersUserWidgetSel);
              } else {
                     if (persRow.getId() != null) {
                            epPersUserWidgetSelDao.deleteById(persRow.getId());
                     }
                     persRow.setStatusCode("H"); // Hide
                     EpPersUserWidgetSel epPersUserWidgetSel = new EpPersUserWidgetSel();
                     epPersUserWidgetSel.setUserId(user);
                     epPersUserWidgetSel.setWidgetId(epWidgetCatalogDao.findById(widgetId).get());
                     epPersUserWidgetSelDao.saveAndFlush(epPersUserWidgetSel);
              }
       }

       private List<PersUserWidgetSelection> getUserWidgetSelction(FnUser user, Long widgetId) {
              return epPersUserWidgetSelDao.getEpPersUserWidgetSelForUserIdAndWidgetId(user.getId(), widgetId)
                      .orElse(new ArrayList<>()).stream().map(
                              this::epPersUserWidgetSelToPersUserWidgetSelection).collect(Collectors.toList());
       }

       private PersUserWidgetSelection epPersUserWidgetSelToPersUserWidgetSelection(EpPersUserWidgetSel widgetSel) {
              return new PersUserWidgetSelection(widgetSel.getId(), widgetSel.getUserId().getId(),
                      widgetSel.getWidgetId().getWidgetId(), widgetSel.getStatusCd());
       }
}
