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

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import org.onap.portal.domain.dto.transport.EcompUserAppRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EcompUserAppRolesService {

  private final static String QUERY = "select\n"
      + "  fr.role_name as roleName,\n"
      + "  fu.app_id as appId,\n"
      + "  fu.user_id as userId,\n"
      + "  fu.priority as priority,\n"
      + "  fu.role_id as roleId\n"
      + " from\n"
      + "  fn_user_role fu\n"
      + "  left outer join fn_role fr on fu.role_id = fr.role_id\n"
      + " where\n"
      + "  fu.user_id = :userId\n"
      + "  and fu.app_id = :appId";

  private final EntityManager entityManager;

  @Autowired
  public EcompUserAppRolesService(EntityManager entityManager) {
    this.entityManager = entityManager;
  }


  public List<EcompUserAppRoles> getUserAppExistingRoles(final Long appId, final Long userId){
    List<Tuple> tuples = entityManager.createQuery(QUERY, Tuple.class)
        .setParameter("appId", appId)
        .setParameter("userId", userId)
        .getResultList();
    return tuples.stream().map(this::tupleToEcompUserAppRoles).collect(Collectors.toList());
  }

  private EcompUserAppRoles tupleToEcompUserAppRoles(Tuple tuple){
    return new EcompUserAppRoles((String)tuple.get("appId"), (Long) tuple.get("userId"), (Integer) tuple.get("priority"), (Long) tuple.get("roleId"), (String) tuple.get("roleName"));
  }
}
