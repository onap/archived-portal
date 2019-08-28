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
package org.onap.portal.domain.dto;


import java.util.Objects;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProfileDetail {
       @NotBlank(message = "firstName must not be blank")
       private String firstName;
       @NotBlank(message = "lastName must not be blank")
       private String lastName;
       private String middleName;
       @Email
       @NotBlank(message = "email must not be blank")
       private String email;
       @NotBlank(message = "loginId must not be blank")
       private String loginId;
       @NotBlank(message = "loginPassword must not be blank")
       private String loginPassword;

       @Override
       public boolean equals(Object o) {
              if (this == o) {
                     return true;
              }
              if (!(o instanceof ProfileDetail)) {
                     return false;
              }
              ProfileDetail that = (ProfileDetail) o;
              return Objects.equals(firstName, that.firstName) &&
                      Objects.equals(lastName, that.lastName) &&
                      Objects.equals(middleName, that.middleName) &&
                      Objects.equals(email, that.email) &&
                      Objects.equals(loginId, that.loginId) &&
                      Objects.equals(loginPassword, that.loginPassword);
       }

       @Override
       public int hashCode() {
              return Objects.hash(firstName, lastName, middleName, email, loginId, loginPassword);
       }
}
