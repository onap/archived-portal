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

import java.util.List;

public class EPWidgetsSortPreference {
	
	private int SizeX;
	private int SizeY;
	private String headerText;
	private String url;
	private Long widgetid;
	private List<Object> attrb;
	private String widgetIdentifier;
	private int row;
	private int col;
	
	public String getWidgetIdentifier() {
		return widgetIdentifier;
	}
	public List<Object> getAttrb() {
		return attrb;
	}
	public void setAttrb(List<Object> attrb) {
		this.attrb = attrb;
	}
	public void setWidgetIdentifier(String widgetIdentifier) {
		this.widgetIdentifier = widgetIdentifier;
	}
	public int getSizeX() {
		return SizeX;
	}
	public void setSizeX(int sizeX) {
		SizeX = sizeX;
	}
	public int getSizeY() {
		return SizeY;
	}
	public void setSizeY(int sizeY) {
		SizeY = sizeY;
	}
	public String getHeaderText() {
		return headerText;
	}
	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Long getWidgetid() {
		return widgetid;
	}
	public void setWidgetid(Long widgetid) {
		this.widgetid = widgetid;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	
	
}
