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

package org.onap.portal.aop.service;

import java.security.Principal;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.onap.portal.domain.db.fn.FnLanguage;
import org.onap.portal.validation.DataValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class FnLanguageServiceAOP {
       private static final Logger LOGGER = LoggerFactory.getLogger(FnLanguageServiceAOP.class);

       @Autowired
       private DataValidator dataValidator;

       @Before("execution(* org.onap.portal.service.fn.FnLanguageService.save(..)) && args(principal, fnLanguage)")
       public void save(final Principal principal, final FnLanguage fnLanguage) {
              if (fnLanguage == null) {
                     LOGGER.error("User " + principal.getName() + " try to save NULL fnLanguage");
                     throw new NullPointerException("FnLanguage cannot be null or empty");
              }
              if (!dataValidator.isValid(fnLanguage)) {
                     String violations = dataValidator.getConstraintViolations(fnLanguage).stream()
                             .map(ConstraintViolation::getMessage)
                             .collect(Collectors.joining(", "));
                     LOGGER.error("User " + principal.getName() + " try to save not valid fnLanguage: " + violations);
                     throw new IllegalArgumentException("FnLanguage is not valid, " + violations);
              }
       }
}
