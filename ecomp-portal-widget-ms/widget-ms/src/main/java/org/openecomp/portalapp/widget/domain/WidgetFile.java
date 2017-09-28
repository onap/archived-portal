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
package org.openecomp.portalapp.widget.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ep_widget_catalog_files")
public class WidgetFile {
	@Id
	@Column (name = "file_id")
	private int id;
	
	@Column(name = "widget_name")
	private String name;
	
	@Column(name = "widget_id")
	private long widgetId;
	
	@Column(name = "markup_html")
	private byte[] markup;
	
	@Column(name = "controller_js")
	private  byte[] controller;
	
	@Column(name = "framework_js")
	private  byte[] framework;

	@Column(name = "widget_css")
	private  byte[] css;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getMarkup() {
		return markup;
	}

	public void setMarkup(byte[] markup) {
		this.markup = markup;
	}

	public byte[] getController() {
		return controller;
	}

	public void setController(byte[] controller) {
		this.controller = controller;
	}

	public byte[] getFramework() {
		return framework;
	}

	public void setFramework(byte[] framework) {
		this.framework = framework;
	}

	public byte[] getCss() {
		return css;
	}

	public void setCss(byte[] css) {
		this.css = css;
	}

	public long getWidgetId() {
		return widgetId;
	}

	public void setWidgetId(long widgetId) {
		this.widgetId = widgetId;
	}

	@Override
	public String toString() {
		return "WidgetFile [name=" + name + ", widgetId=" + widgetId + "]";
	}
	
	
    
}
