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
package org.openecomp.portalapp.portal.ecomp.model;

public class PortalRestResponse<T> {
	
	private PortalRestStatusEnum status;
	private String message;
	
	private T response;
	
	public PortalRestResponse(){};
	
	public PortalRestResponse(PortalRestStatusEnum status, String message, T response){
		this.status = status;
		this.message = message;
		this.response = response;
	}

	public PortalRestStatusEnum getStatus() {
		return status;
	}

	public void setStatus(PortalRestStatusEnum status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getResponse() {
		return response;
	}

	public void setResponse(T response) {
		this.response = response;
	};	
}
