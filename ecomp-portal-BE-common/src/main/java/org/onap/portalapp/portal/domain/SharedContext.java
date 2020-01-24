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
package org.onap.portalapp.portal.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portalsdk.core.domain.support.DomainVo;

/**
 * A shared context is a key-value pair in a session. All shared-context objects
 * should be dropped when a session is destroyed. Because there's always a
 * chance of missing that event, this object notes its creation time so that it
 * can be expired after a suitable time interval.
 */
@Entity
@Table(name = "fn_shared_context")
@NoArgsConstructor
@Getter
@Setter
public class SharedContext extends DomainVo {
	private static final long serialVersionUID = 7287469622586677888L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Digits(integer = 11, fraction = 0)
	private Long id;

	@NotNull
	private Date create_time;

	@NotNull
	@SafeHtml
	@Size(max = 64)
	private String context_id;

	@NotNull
	@SafeHtml
	@Size(max = 128)
	private String ckey;

	@NotNull
	@SafeHtml
	@Size(max = 1024)
	private String cvalue;

	public SharedContext(final String contextId, final String key, final String value) {
		this.context_id = contextId;
		this.ckey = key;
		this.cvalue = value;
	}

}
