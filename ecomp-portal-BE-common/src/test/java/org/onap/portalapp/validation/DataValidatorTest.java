/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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

package org.onap.portalapp.validation;

import static org.junit.Assert.*;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.drools.core.command.assertion.AssertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.onap.portalapp.portal.domain.EPUser;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;

@RunWith(PowerMockRunner.class)
public class DataValidatorTest {
       private static final ValidatorFactory VALIDATOR_FACTORY  = Validation.buildDefaultValidatorFactory();
       @InjectMocks
       DataValidator dataValidator;

       @Test
       public void getConstraintViolationsSecureString() {
              SecureString secureString = new SecureString("<script>alert(“XSS”);</script>");
              Validator validator = VALIDATOR_FACTORY.getValidator();
              Set<ConstraintViolation<SecureString>> expectedConstraintViolations = validator.validate(secureString);
              Set<ConstraintViolation<SecureString>> actualConstraintViolations = dataValidator.getConstraintViolations(secureString);
              assertEquals(expectedConstraintViolations, actualConstraintViolations);
       }

       @Test
       public void isValidSecureString() {
              SecureString secureString = new SecureString("<script>alert(“XSS”);</script>");
              assertFalse(dataValidator.isValid(secureString));
       }

       @Test
       public void getConstraintViolationsEPUser() {
              EPUser user = new EPUser();
              user.setEmail("“><script>alert(“XSS”)</script>");
              user.setLoginId("<IMG SRC=”javascript:alert(‘XSS’);”>");
              user.setFinancialLocCode("<IMG SRC=javascript:alert(‘XSS’)> ");
              Validator validator = VALIDATOR_FACTORY.getValidator();
              Set<ConstraintViolation<EPUser>> expectedConstraintViolations = validator.validate(user);
              Set<ConstraintViolation<EPUser>> actualConstraintViolations = dataValidator.getConstraintViolations(user);
              assertEquals(expectedConstraintViolations, actualConstraintViolations);
       }

       @Test
       public void isValidEPUser() {
              EPUser user = new EPUser();
              user.setEmail("“><script>alert(“XSS”)</script>");
              user.setLoginId("<IMG SRC=”javascript:alert(‘XSS’);”>");
              user.setFinancialLocCode("<IMG SRC=javascript:alert(‘XSS’)> ");
              assertFalse(dataValidator.isValid(user));
       }

}
