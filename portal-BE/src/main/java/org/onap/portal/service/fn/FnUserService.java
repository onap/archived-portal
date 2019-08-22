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

package org.onap.portal.service.fn;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.onap.portal.dao.fn.FnUserDao;
import org.onap.portal.domain.db.fn.FnUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableAspectJAutoProxy
@Transactional
public class FnUserService implements UserDetailsService {

       private final FnUserDao fnUserDao;

       @Autowired
       public FnUserService(FnUserDao fnUserDao) {
              this.fnUserDao = fnUserDao;
       }

       public FnUser saveFnUser(final Principal principal, final FnUser fnUser) {
              return fnUserDao.save(fnUser);
       }

       @Override
       public FnUser loadUserByUsername(String username) throws UsernameNotFoundException {
              Optional<FnUser> fnUser = fnUserDao.findByLoginId(username);
              if (fnUser.isPresent()) {
                     return fnUser.get();
              } else {
                     throw new UsernameNotFoundException("User not found for username: " + username);
              }
       }

       public Optional<FnUser> getUser(Long id) {
              return Optional.of(fnUserDao.getOne(id));
       }

       List<FnUser> getUserWithOrgUserId(final String orgUserIdValue){
              Optional<List<FnUser>> fnUsers =  Optional.of(fnUserDao.findAll()
                      .stream()
                      .filter(fnUser -> orgUserIdValue.equals(fnUser.getOrgUserId()))
                      .collect(Collectors.toList()));
              return fnUsers.orElse(new ArrayList<>());
       }

       List<FnUser> getUsersByOrgIdsNotificationsResult(List<String> orgIds){
              String ids = "(" + orgIds.stream().map(s -> "'" + s + "'").collect(Collectors.joining()) + ")";
              return fnUserDao.getUsersByOrgUserId(ids).orElse(new ArrayList<>());
       }

       List<FnUser> getUsersByOrgIdsNotificationsResult(String orgIds){
              return fnUserDao.getUsersByOrgUserId(orgIds).orElse(new ArrayList<>());
       }

       List<FnUser> etActiveUsers(){
              return fnUserDao.findAll().stream().filter(fnUser -> "Y".equals(fnUser.getActiveYn())).collect(Collectors.toList());
       }

       public void deleteUser(FnUser fnUser){
              fnUserDao.delete(fnUser);
       }
}
