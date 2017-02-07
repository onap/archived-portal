/*-
 * ================================================================================
 * eCOMP Portal
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ================================================================================
 */
package org.openecomp.portalapp.kpidash.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.openecomp.portalsdk.core.domain.support.DomainVo;

@Entity
@Table(name = "kpi_user_story_stats1")
public class KpiUserStoryStats extends DomainVo {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1193197465342848502L;

	@Id
	@Column(name = "release_key")
	private String releaseKey;

	@Column(name = "delivered")
	private Long delivered;

	@Column(name = "in_progress")
	private Long inProgress;

	@Column(name = "IST_progress_readiness")
	private String ISTProgressReadiness;

	@Column(name = "E2E_progress_readiness")
	private String E2EProgressReadiness;

	@Column(name = "Key_Highlights")
	private String KeyHighlights;

	public KpiUserStoryStats() {
	}

	public String getReleaseKey() {
		return releaseKey;
	}

	public void setReleaseKey(String releaseKey) {
		this.releaseKey = releaseKey;
	}

	public Long getDelivered() {
		return delivered;
	}

	public void setDelivered(Long delivered) {
		this.delivered = delivered;
	}

	public Long getInProgress() {
		return inProgress;
	}

	public void setInProgress(Long inProgress) {
		this.inProgress = inProgress;
	}

	public String getISTProgressReadiness() {
		return ISTProgressReadiness;
	}

	public void setISTProgressReadiness(String iSTProgressReadiness) {
		ISTProgressReadiness = iSTProgressReadiness;
	}

	public String getE2EProgressReadiness() {
		return E2EProgressReadiness;
	}

	public void setE2EProgressReadiness(String e2eProgressReadiness) {
		E2EProgressReadiness = e2eProgressReadiness;
	}

	public String getKeyHighlights() {
		return KeyHighlights;
	}

	public void setKeyHighlights(String keyHighlights) {
		KeyHighlights = keyHighlights;
	}

}
