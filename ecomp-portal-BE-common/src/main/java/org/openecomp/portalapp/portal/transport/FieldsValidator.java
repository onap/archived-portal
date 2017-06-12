/*-
 * ================================================================================
 * ECOMP Portal
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
package org.openecomp.portalapp.portal.transport;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

public class FieldsValidator {

	public Long httpStatusCode = new Long(HttpServletResponse.SC_OK);

	public Long errorCode;

	public class FieldName {

		public String name;

		public FieldName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}
	

	public List<FieldName> fields = new ArrayList<FieldName>();

	public void addProblematicFieldName(String name) {
		fields.add(new FieldName(name));
	}

	public Long getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(Long httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	public Long getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Long errorCode) {
		this.errorCode = errorCode;
	}

	public List<FieldName> getFields() {
		return fields;
	}

	public void setFields(List<FieldName> fields) {
		this.fields = fields;
	}

	@Override
	public String toString() {
		return "FieldsValidator [httpStatusCode=" + httpStatusCode + ", errorCode=" + errorCode + ", fields=" + fields
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((errorCode == null) ? 0 : errorCode.hashCode());
		result = prime * result + ((fields == null) ? 0 : fields.hashCode());
		result = prime * result + ((httpStatusCode == null) ? 0 : httpStatusCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FieldsValidator other = (FieldsValidator) obj;
		if (errorCode == null) {
			if (other.errorCode != null)
				return false;
		} else if (!errorCode.equals(other.errorCode))
			return false;
		if (fields == null) {
			if (other.fields != null)
				return false;
		} else if (!fields.equals(other.fields))
			return false;
		if (httpStatusCode == null) {
			if (other.httpStatusCode != null)
				return false;
		} else if (!httpStatusCode.equals(other.httpStatusCode))
			return false;
		return true;
	}

}
