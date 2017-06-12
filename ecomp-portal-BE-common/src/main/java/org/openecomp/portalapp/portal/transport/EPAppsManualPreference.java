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

public class EPAppsManualPreference {
	
	private Long appid;
	private int col;
	private String headerText;
	private String imageLink;
	private int order;
	private boolean restrictedApp;
	private int row;
	private int sizeX;
	private int sizeY;
	private String subHeaderText;
	private String url;
	private boolean addRemoveApps;
	
	
	public boolean isAddRemoveApps() {
		return addRemoveApps;
	}
	public void setAddRemoveApps(boolean addRemoveApps) {
		this.addRemoveApps = addRemoveApps;
	}
	public Long getAppid() {
		return appid;
	}
	public void setAppid(Long appid) {
		this.appid = appid;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public String getHeaderText() {
		return headerText;
	}
	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}
	public String getImageLink() {
		return imageLink;
	}
	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public boolean isRestrictedApp() {
		return restrictedApp;
	}
	public void setRestrictedApp(boolean restrictedApp) {
		this.restrictedApp = restrictedApp;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getSizeX() {
		return sizeX;
	}
	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}
	public int getSizeY() {
		return sizeY;
	}
	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}
	public String getSubHeaderText() {
		return subHeaderText;
	}
	public void setSubHeaderText(String subHeaderText) {
		this.subHeaderText = subHeaderText;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public boolean isValid(){
		return appid != null;
	}
}
