/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.portal.domain;

import org.onap.portalsdk.core.domain.support.DomainVo;

public class EPWidgetsManualSortPreference extends DomainVo{

	private static final long serialVersionUID = 4607102334801223570L;
	private Long userId;
	private Long widgetId;
	private int widgetRow;
	private int widgetCol;
	private int widgetWidth;
	private int widgetHeight;

	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
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
