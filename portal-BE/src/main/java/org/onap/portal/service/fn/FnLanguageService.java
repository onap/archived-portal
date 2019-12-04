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
import java.util.List;
import java.util.Optional;
import org.onap.portal.dao.fn.FnLanguageDao;
import org.onap.portal.domain.db.fn.FnLanguage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableAspectJAutoProxy
@Transactional
public class FnLanguageService {
       private final FnLanguageDao fnLanguageDao;

       @Autowired
       public FnLanguageService(final FnLanguageDao fnLanguageDao) {
              this.fnLanguageDao = fnLanguageDao;
       }

       public Optional<FnLanguage> findById(final Long id){
              return fnLanguageDao.findById(id);
       }
       public List<FnLanguage> getLanguages(Principal principal){
              return fnLanguageDao.findAll();
       }
       public FnLanguage save(final FnLanguage fnLanguage){
              return fnLanguageDao.saveAndFlush(fnLanguage);
       }
}
