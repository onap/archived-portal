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

package org.onap.portal.domain.db.fn;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portal.domain.db.fn.FnRestrictedUrl.FnRestrictedUrlId;

/*
CREATE TABLE `fn_restricted_url` (
        `restricted_url` varchar(250) NOT NULL,
        `function_cd` varchar(30) NOT NULL,
        PRIMARY KEY (`restricted_url`,`function_cd`),
        KEY `fk_restricted_url_function_cd` (`function_cd`),
        CONSTRAINT `fk_restricted_url_function_cd` FOREIGN KEY (`function_cd`) REFERENCES `fn_function` (`function_cd`)
        )
*/

@Table(name = "fn_restricted_url", indexes = {
        @Index(name = "fk_restricted_url_function_cd", columnList = "function_cd")
})
@NoArgsConstructor
@AllArgsConstructor

@Getter
@Setter
@Entity
@IdClass(FnRestrictedUrlId.class)
public class FnRestrictedUrl {
       @Column(name = "restricted_url", length = 250, nullable = false)
       @Size(max = 250)
       @SafeHtml
       @Id
       private String restricted_url;
       @ManyToOne()
       @JoinColumn(name = "function_cd", nullable = false)
       @Valid
       @NotNull
       @Id
       private FnFunction functionCd;

       @Getter
       @Setter
       @NoArgsConstructor
       @EqualsAndHashCode
       @AllArgsConstructor
       public static class FnRestrictedUrlId implements Serializable {
              @Size(max = 250)
              @SafeHtml
              private String restricted_url;
              @Valid
              @NotNull
              private FnFunction functionCd;
       }
}