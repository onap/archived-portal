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

	}

	public List<FieldName> fields = new ArrayList<FieldName>();

	public void addProblematicFieldName(String name) {
		fields.add(new FieldName(name));
	}

}
