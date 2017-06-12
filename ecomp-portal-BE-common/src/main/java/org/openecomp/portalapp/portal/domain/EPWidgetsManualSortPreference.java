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
package org.openecomp.portalapp.portal.domain;

import org.openecomp.portalsdk.core.domain.support.DomainVo;

public class EPWidgetsManualSortPreference extends DomainVo{

	private static final long serialVersionUID = 4607102334801223570L;
	private int userId;
	private Long widgetId;
	private int widgetRow;
	private int widgetCol;
	private int widgetWidth;
	private int widgetHeight;

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Long getWidgetId() {
		return widgetId;
	}
	public void setWidgetId(Long widgetId) {
		this.widgetId = widgetId;
	}
	public int getWidgetRow() {
		return widgetRow;
	}
	public void setWidgetRow(int widgetRow) {
		this.widgetRow = widgetRow;
	}
	public int getWidgetCol() {
		return widgetCol;
	}
	public void setWidgetCol(int widgetCol) {
		this.widgetCol = widgetCol;
	}
	public int getWidgetWidth() {
		return widgetWidth;
	}
	public void setWidgetWidth(int widgetWidth) {
		this.widgetWidth = widgetWidth;
	}
	public int getWidgetHeight() {
		return widgetHeight;
	}
	public void setWidgetHeight(int widgetHeight) {
		this.widgetHeight = widgetHeight;
	}
}
