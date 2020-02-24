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

package org.onap.portal.service.appFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.persistence.EntityManager;
import org.onap.portal.domain.db.ep.EpAppFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EpAppFunctionService {

  private final EpAppFunctionDao epAppFunctionDao;
  private final EntityManager entityManager;

  @Autowired
  public EpAppFunctionService(final EpAppFunctionDao epAppFunctionDao,final EntityManager entityManager) {
    this.epAppFunctionDao = epAppFunctionDao;
    this.entityManager = entityManager;
  }

  public List<EpAppFunction> getAppRoleFunctionList(final Long roleId, final Long appId) {
    return Optional.of(epAppFunctionDao.getAppRoleFunctionList(roleId, appId))
        .orElse(new ArrayList<>());
  }

  public Optional<EpAppFunction> getForId(Long id){
    return epAppFunctionDao.findById(id);
  }

  private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
    List<Object> seen = new ArrayList<>();
    return t -> seen.add(keyExtractor.apply(t));
  }

  public EpAppFunction save(final EpAppFunction epAppFunction){
    return epAppFunctionDao.save(epAppFunction);
  }

  public List<EpAppFunction> saveAll(List<EpAppFunction> epAppFunctions) {
    return epAppFunctionDao.saveAll(epAppFunctions);
  }

  public List<EpAppFunction> getAllRoleFunctions(final Long appId){
    return epAppFunctionDao.getAllRoleFunctions(appId).orElse(new ArrayList<>());
  }

  public List<EpAppFunction> getAppFunctionOnCodeAndAppId(final long appId, final String functionCd) {
      return epAppFunctionDao.getAppFunctionOnCodeAndAppId(appId, functionCd);
  }

  public List<EpAppFunction> getRoleFunction(final String functionCd, final long appId) {
    return epAppFunctionDao.getRoleFunction(functionCd, appId);
  }

  public void deleteOne(EpAppFunction function){
    epAppFunctionDao.delete(function);
  }

  public void deleteByAppIdAndFunctionCd(Long appId, String functionCd) {
    entityManager.createQuery("DELETE FROM ep_app_function WHERE app_id = :appId and function_cd = :functionCd")
        .setParameter("appId", appId)
        .setParameter("functionCd", functionCd)
        .executeUpdate();
  }
}
