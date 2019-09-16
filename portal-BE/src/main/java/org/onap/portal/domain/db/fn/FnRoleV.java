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
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

@Entity
@Table(name = "fn_role_v")
@Immutable
@Subselect("select fn_role.role_id as role_id,"
        + "         fn_role.role_name as role_name,"
        + "         fn_role.active_yn as active_yn,"
        + "         fn_role.priority as priority,"
        + "         fn_role.app_id as app_id,"
        + "         fn_role.app_role_id as app_role_id"
        + " from fn_role where isnull(fn_role.app_id)")
@Getter
@NoArgsConstructor
public class FnRoleV implements Serializable {
       @Id
       @Column(name = "role_id")
       private Integer roleId;
       @Column(name = "role_name")
       private String roleName;
       @Column(name = "active_yn")
       private String activeYn;
       @Column(name = "priority")
       private BigInteger priority;
       @Column(name = "app_Id")
       private Integer appId;
       @Column(name = "app_role_id")
       private Integer appRoleId;
}

