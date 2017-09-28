/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the “License”);
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
 * under the Creative Commons License, Attribution 4.0 Intl. (the “License”);
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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.openecomp.portalapp.portal.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.openecomp.portalsdk.core.domain.support.DomainVo;

/**
 * A shared context is a key-value pair in a session. All shared-context objects
 * should be dropped when a session is destroyed. Because there's always a
 * chance of missing that event, this object notes its creation time so that it
 * can be expired after a suitable time interval.
 */
@Entity
@Table(name = "fn_shared_context")
public class SharedContext extends DomainVo {

	// generated
	private static final long serialVersionUID = 7287469622586677888L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Date create_time;
	private String context_id;
	private String ckey;
	private String cvalue;

	/**
	 * Mandatory no-argument constructor
	 */
	public SharedContext() {
	}

	/**
	 * Convenience constructor. The database ID and creation timestamp are
	 * populated when the object is added to the database.
	 * 
	 * @param contextId
	 *            context ID
	 * @param key
	 *            context key
	 * @param value
	 *            context value
	 */
	public SharedContext(final String contextId, final String key, final String value) {
		this.context_id = contextId;
		this.ckey = key;
		this.cvalue = value;
	}

	/**
	 * Gets the database row ID.
	 * 
	 * @return Database row ID
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the database row ID.
	 * 
	 * @param id
	 *            database row ID
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * Gets the creation time
	 * 
	 * @return Creation time as a Date
	 */
	public Date getCreate_time() {
		return create_time;
	}

	/**
	 * Sets the creation time
	 * 
	 * @param create_time
	 *            Date
	 */
	public void setCreate_time(final Date create_time) {
		this.create_time = create_time;
	}

	/**
	 * Gets the context ID
	 * 
	 * @return Context ID
	 */
	public String getContext_id() {
		return context_id;
	}

	/**
	 * Sets the context ID
	 * 
	 * @param context_id
	 *            String
	 */
	public void setContext_id(final String context_id) {
		this.context_id = context_id;
	}

	/**
	 * Gets the key of the key-value pair. Called ckey because "key" is a
	 * reserved word in Mysql.
	 * 
	 * @return The key
	 */
	public String getCkey() {
		return ckey;
	}

	/**
	 * Sets the key of the key-value pair.
	 * 
	 * @param ckey
	 *            String
	 */
	public void setCkey(final String ckey) {
		this.ckey = ckey;
	}

	/**
	 * Gets the value of the key-value pair. Called cvalue because "value" is a
	 * reserved word in Mysql.
	 * 
	 * @return value
	 */
	public String getCvalue() {
		return cvalue;
	}

	/**
	 * Sets the value of the key-value pair.
	 * 
	 * @param cvalue
	 *            value
	 */
	public void setCvalue(final String cvalue) {
		this.cvalue = cvalue;
	}

}
