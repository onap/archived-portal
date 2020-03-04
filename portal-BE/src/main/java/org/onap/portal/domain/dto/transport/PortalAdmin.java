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

package org.onap.portal.domain.dto.transport;

import java.io.Serializable;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;


@NamedNativeQuery(
    name = "PortalAdmin.PortalAdminDTO",
    query = "SELECT " +
        "u.id AS userId, " +
        "u.loginId AS loginId " +
        "u.firstName AS firstName " +
        "u.lastName AS lastName " +
        "FROM " +
        "FnUser u, " +
        "FnUserRole ur " +
        "WHERE u.activeYn = 'true' AND u.user_id = ur.user_id AND ur.role_id= :adminRoleId",
    resultSetMapping = "PortalAdminDTO")
@NamedNativeQuery(
    name = "PortalAdmin.ActivePortalAdminDTO",
    query = "SELECT " +
        "u.id AS userId, " +
        "u.loginId AS loginId " +
        "u.firstName AS firstName " +
        "u.lastName AS lastName " +
        "FROM fn_user u, fn_user_role ur " +
        "WHERE u.user_id = ur.user_id " +
        "AND ur.user_id= :userId " +
        "AND ur.role_id=:SYS_ADMIN_ROLE_ID",
    resultSetMapping = "PortalAdminDTO")

@SqlResultSetMapping(
    name = "PortalAdminDTO",
    classes = @ConstructorResult(
        targetClass = PortalAdmin.class,
        columns = {
            @ColumnResult(name = "userId"),
            @ColumnResult(name = "loginId"),
            @ColumnResult(name = "firstName"),
            @ColumnResult(name = "lastName")
        }
    )
)

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PortalAdmin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Digits(integer = 11, fraction = 0)
    private Long userId;
    @Size(max = 25)
    @SafeHtml
    private String loginId;
    @Size(max = 50)
    @SafeHtml
    private String firstName;
    @Size(max = 50)
    @SafeHtml
    private String lastName;

}
