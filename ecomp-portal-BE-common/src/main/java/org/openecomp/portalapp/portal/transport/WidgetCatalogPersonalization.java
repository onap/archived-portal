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

/**
 * Model for the object PUT to the controller when the user takes an action on
 * a widget in the catalog.
 */

public class WidgetCatalogPersonalization {
	
		public Long widgetId;
		public Boolean select;
		// not needed as only 'SHOW' and 'HIDE' status_cd is expected from the micro service now
		//public Boolean pending;

		public Long getWidgetId() {
			return widgetId;
		}

		public void setWidgetId(Long widgetId) {
			this.widgetId = widgetId;
		}

		public Boolean getSelect() {
			return select;
		}

		public void setSelect(Boolean select) {
			this.select = select;
		}

		/*public Boolean getPending() {
			return pending;
		}

		public void setPending(Boolean pending) {
			this.pending = pending;
		}*/
}
