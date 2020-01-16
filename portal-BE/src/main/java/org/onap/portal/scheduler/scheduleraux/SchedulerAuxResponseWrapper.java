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

package org.onap.portal.scheduler.scheduleraux;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This wrapper encapsulates the Policy response
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	    "status",
	    "entity"
})

public class SchedulerAuxResponseWrapper {
	
	@JsonProperty("status")
	private int status;

	@JsonProperty("entity")
	private String entity;

	@JsonProperty("entity")
    public String getEntity() {
        return entity;
    }
	
	@JsonProperty("status")
    public int getStatus() {
        return status;
    }	
	
	@JsonProperty("status")
    public void setStatus(int v) {
        this.status = v;
    }

	@JsonProperty("entity")
    public void setEntity(String v) {
        this.entity = v;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public String getResponse () {
    	
    	StringBuilder b = new StringBuilder ("{ \"status\": ");
        b.append(getStatus()).append(", \"entity\": " ).append(this.getEntity()).append("}");
        return (b.toString());
    }
}
