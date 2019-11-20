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

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NamedNativeQuery(
		name = "EPUserAppCurrentRoles",
		query = "select\n"
				+ "  distinct fu.role_id,\n"
				+ "  fr.user_id,\n"
				+ "  fu.role_name,\n"
				+ "  fu.priority\n"
				+ " from\n"
				+ "  fn_role fu\n"
				+ "  left outer join fn_user_role fr ON fu.role_id = fr.role_id\n"
				+ "  and fu.app_id = fr.app_id\n"
				+ "  and fr.role_id != 999\n"
				+ " where\n"
				+ "  fu.app_id = :appId\n"
				+ "  and fr.user_id = :userId\n"
				+ "  and fu.active_yn = 'Y'\n",
		resultSetMapping = "EPUserAppCurrentRoles"
)

@SqlResultSetMapping(
		name = "EPUserAppCurrentRoles",
		classes = @ConstructorResult(
				targetClass = EPUserAppCurrentRoles.class,
				columns = {
						@ColumnResult(name = "roleName"),
						@ColumnResult(name = "userId"),
						@ColumnResult(name = "priority"),
						@ColumnResult(name = "roleId")
				}
		)
)

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EPUserAppCurrentRoles implements Serializable{

	private static final long serialVersionUID = -8145807875293949759L;

	private String roleName;
	private Long userId;
	private Integer priority ;
	private Long roleId;

}
