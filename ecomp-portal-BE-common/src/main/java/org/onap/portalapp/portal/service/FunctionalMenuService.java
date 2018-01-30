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
package org.onap.portalapp.portal.service;

import java.util.List;

import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.FunctionalMenuItemWithAppID;
import org.onap.portalapp.portal.transport.BusinessCardApplicationRole;
import org.onap.portalapp.portal.transport.FavoritesFunctionalMenuItem;
import org.onap.portalapp.portal.transport.FavoritesFunctionalMenuItemJson;
import org.onap.portalapp.portal.transport.FieldsValidator;
import org.onap.portalapp.portal.transport.FunctionalMenuItem;
import org.onap.portalapp.portal.transport.FunctionalMenuItemWithRoles;
import org.onap.portalapp.portal.transport.FunctionalMenuRole;

public interface FunctionalMenuService {
	List<FunctionalMenuItem> getFunctionalMenuItems (EPUser user);
	// return all active menu items
	List<FunctionalMenuItem> getFunctionalMenuItems ();
	// return all active menu items. If all is true, return all active and inactive menu items.
	List<FunctionalMenuItem> getFunctionalMenuItems(Boolean all);
	// return all active Functional menu items for Notification Tree in User Notification . If all is true, return all active menu items.
	List<FunctionalMenuItem> getFunctionalMenuItemsForNotificationTree(Boolean all);
	List<FunctionalMenuItem> getFunctionalMenuItemsForApp (Integer appId);
	List<FunctionalMenuItem> getFunctionalMenuItemsForUser (String orgUserId);
	FunctionalMenuItem getFunctionalMenuItemDetails (Long menuid);
	FieldsValidator createFunctionalMenuItem (FunctionalMenuItemWithRoles menuItemJson);
	FieldsValidator editFunctionalMenuItem (FunctionalMenuItemWithRoles menuItemJson);
	FieldsValidator deleteFunctionalMenuItem (Long menuId);
	FieldsValidator regenerateAncestorTable ();
	//Methods relevant to favorites
	FieldsValidator setFavoriteItem(FavoritesFunctionalMenuItem menuItemJson);
	List<FavoritesFunctionalMenuItemJson> getFavoriteItems(Long userId);
	FieldsValidator removeFavoriteItem (Long userId, Long menuId);
	List<FunctionalMenuItem> transformFunctionalMenuItemWithAppIDToFunctionalMenuItem(List<FunctionalMenuItemWithAppID> functionalMenuItemWithAppIDList);
	List<FunctionalMenuRole> getFunctionalMenuRole();
	//Assign URLs under Help Menu
	void assignHelpURLs(List<FunctionalMenuItem> menuItems);
	List<BusinessCardApplicationRole> getUserAppRolesList(String userId);
}
