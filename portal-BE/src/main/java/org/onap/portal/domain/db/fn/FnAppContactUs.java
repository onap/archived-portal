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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;
import org.onap.portal.domain.dto.DomainVo;

/*
CREATE TABLE `fn_app_contact_us` (
        `app_id` int(11) NOT NULL,
        `contact_name` varchar(128) DEFAULT NULL,
        `contact_email` varchar(128) DEFAULT NULL,
        `url` varchar(256) DEFAULT NULL,
        `active_yn` varchar(2) DEFAULT NULL,
        `description` varchar(1024) DEFAULT NULL,
        PRIMARY KEY (`app_id`),
        CONSTRAINT `fk_fn_a_con__ref_202_fn_app` FOREIGN KEY (`app_id`) REFERENCES `fn_app` (`app_id`)
        )
*/

@SqlResultSetMapping(
        name = "fnAppContactUsMapping",
        entities = {
                @EntityResult(
                        entityClass = FnAppContactUs.class,
                        fields = {
                                @FieldResult(name = "appId", column = "app_Id"),
                                @FieldResult(name = "contactName", column = "contactName"),
                                @FieldResult(name = "contact_email", column = "contactEmail"),
                                @FieldResult(name = "description", column = "description"),
                                @FieldResult(name = "activeYN", column = "activeYN")
                        }
                )
        })

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "FnAppContactUs.getAppsAndContacts",
                query = "select "
                        + "a.app_id as app_Id, a.app_name as appName,"
                        + "c.contact_name as contactName, "
                        + "c.contact_email as contactEmail, c.url, c.description, "
                        + "c.active_yn as activeYN"
                        + "from "
                        + "fn_app a"
                        + "left join "
                        + "fn_app_contact_us c"
                        + "on a.app_id = c.app_id"
                        + "where "
                        + "a.enabled = 'Y' and a.app_name is not null and a.app_name != '';",
                resultClass = FnAppContactUs.class,
                resultSetMapping = "fnAppContactUsMapping"),
        @NamedNativeQuery(
                name = "FnAppContactUs.getAppContactUsItems",
                query = "select\n"
                        + "  c.app_id as app_Id,\n"
                        + "  c.contact_name as contact_name,\n"
                        + "  c.contact_email as contact_email,\n"
                        + "  c.url,\n"
                        + "  c.description,\n"
                        + "  c.active_yn as active_yn,\n"
                        + "  a.app_name as appName\n"
                        + "from\n"
                        + "  fn_app_contact_us c\n"
                        + "  left join fn_app a on a.app_id = c.app_id\n"
                        + "where\n"
                        + "  a.enabled = 'Y'\n"
                        + "  and a.app_name is not null\n"
                        + "  and a.app_name != ''",
                resultClass = FnAppContactUs.class,
                resultSetMapping = "fnAppContactUsMapping")
})

@Table(name = "fn_app_contact_us")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Entity
public class FnAppContactUs extends DomainVo implements Serializable {
       @Id
       @Column(name = "app_id")
       private Long appId;

       @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
       @JoinColumn(name = "app_id")
       @MapsId
       @Valid
       private FnApp fnApp;
       @Column(name = "contact_name", length = 128, columnDefinition = "varchar(128) default null")
       @Size(max = 128)
       @SafeHtml
       private String contactName;
       @Column(name = "contact_email", length = 128, columnDefinition = "varchar(128) default null")
       @Size(max = 128)
       @SafeHtml
       private String contactEmail;
       @Column(name = "url", length = 256, columnDefinition = "varchar(128) default null")
       @Size(max = 256)
       @SafeHtml
       //TODO URL
       @URL
       private String url;
       @Column(name = "active_yn", length = 1, columnDefinition = "varchar(1) default null")
       @Pattern(regexp = "[YNyn]")
       @Size(max = 1)
       @SafeHtml
       private String activeYn;
       @Column(name = "description", length = 1024, columnDefinition = "varchar(1024) default null")
       @Size(max = 1024)
       @SafeHtml
       private String description;

}
