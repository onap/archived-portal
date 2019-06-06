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

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.springframework.stereotype.Component;

@Component
public class DataValidator {
       private volatile static ValidatorFactory VALIDATOR_FACTORY;

       public DataValidator() {
              if (VALIDATOR_FACTORY == null) {
                     synchronized (DataValidator.class) {
                            if (VALIDATOR_FACTORY == null) {
                                   VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
                            }
                     }
              }
       }

       public <E> Set<ConstraintViolation<E>> getConstraintViolations(E classToValid) {
              Validator validator = VALIDATOR_FACTORY.getValidator();
              Set<ConstraintViolation<E>> constraintViolations = validator.validate(classToValid);
              return constraintViolations;
       }

       public <E> boolean isValid(E classToValid) {
              Set<ConstraintViolation<E>> constraintViolations = getConstraintViolations(classToValid);
              return constraintViolations.isEmpty();
       }

}
