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
package org.openecomp.portalapp.portal.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.openecomp.portalapp.portal.domain.EPApp;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;
import org.openecomp.portalapp.portal.transport.FavoritesFunctionalMenuItem;
import org.openecomp.portalapp.portal.transport.FavoritesFunctionalMenuItemJson;
import org.openecomp.portalapp.portal.transport.FieldsValidator;
import org.openecomp.portalapp.portal.transport.FunctionalMenuItem;
import org.openecomp.portalapp.portal.transport.FunctionalMenuItemJson;
import org.openecomp.portalapp.portal.transport.FunctionalMenuRole;
import org.openecomp.portalapp.portal.utils.EPSystemProperties;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

@Service("functionalMenuService")
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
public class FunctionalMenuServiceImpl implements FunctionalMenuService {
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(FunctionalMenuServiceImpl.class);

	private Long ACCOUNT_ADMIN_ROLE_ID = 999L;
	private String RESTRICTED_APP_ROLE_ID = "900";

	@Autowired
	private DataAccessService dataAccessService;
	@Autowired
	private SessionFactory sessionFactory;

	@PostConstruct
	private void init() {
		try {
			ACCOUNT_ADMIN_ROLE_ID = Long.valueOf(SystemProperties.getProperty(EPSystemProperties.ACCOUNT_ADMIN_ROLE_ID));
			RESTRICTED_APP_ROLE_ID = SystemProperties.getProperty(EPSystemProperties.RESTRICTED_APP_ROLE_ID);
		} catch(Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
		}
	}
	
	public List<FunctionalMenuItem> getFunctionalMenuItems(EPUser user) {
		List<FunctionalMenuItem> menuItems = new ArrayList<FunctionalMenuItem>();
		return menuItems;
	}

	public List<FunctionalMenuItem> getFunctionalMenuItems() {
		return getFunctionalMenuItems(false);
	}
	
	public List<FunctionalMenuItem> getFunctionalMenuItems(Boolean all) {
		// Divide this into 2 queries: one which returns the bottom-level menu items associated with Restricted apps,
		// and one that returns all the other menu items. Then we can easily add the boolean flag
		// restrictedApp to each FunctionalMenuItem, to be used by the front end.
		String activeWhereClause = "";
		if (! all) {
			activeWhereClause = " AND UPPER(m.active_yn) = 'Y' ";
		}
		String sql = "SELECT m.menu_id, m.column_num, m.text, m.parent_menu_id, m.url, m.active_yn "
				+ "FROM fn_menu_functional m, fn_menu_functional_roles r "
				+ "WHERE m.menu_id = r.menu_id "
				+ activeWhereClause //" AND UPPER(m.active_yn) = 'Y' "
				+ " AND r.role_id != '" + RESTRICTED_APP_ROLE_ID + "' "
			+ " UNION "
			+ " SELECT m.menu_id, m.column_num, m.text, m.parent_menu_id, m.url, m.active_yn "
				+ " FROM fn_menu_functional m "
				+ " WHERE m.url='' "
				+ activeWhereClause; //" AND UPPER(m.active_yn) = 'Y' ";
		logQuery(sql);

		@SuppressWarnings("unchecked")
		List<FunctionalMenuItem> menuItems = dataAccessService.executeSQLQuery(sql, FunctionalMenuItem.class, null);
		for (FunctionalMenuItem menuItem : menuItems) {
			menuItem.restrictedApp = false;
		}
		
		sql = "SELECT m.menu_id, m.column_num, m.text, m.parent_menu_id, m.url, m.active_yn "
				+ "FROM fn_menu_functional m, fn_menu_functional_roles r "
				+ "WHERE m.menu_id = r.menu_id "
				+ activeWhereClause //" AND UPPER(m.active_yn) = 'Y' "
				+ " AND r.role_id = '" + RESTRICTED_APP_ROLE_ID + "' ";
		logQuery(sql);
		@SuppressWarnings("unchecked")
		List<FunctionalMenuItem> menuItems2 = dataAccessService.executeSQLQuery(sql, FunctionalMenuItem.class, null);
		for (FunctionalMenuItem menuItem : menuItems2) {
			menuItem.restrictedApp = true;
			menuItems.add(menuItem);
		}
		
		return menuItems;
	}

	public List<FunctionalMenuItem> getFunctionalMenuItemsForApp(Integer appId) {
		String sql = "SELECT DISTINCT m1.menu_id, m1.column_num, m1.text, m1.parent_menu_id, m1.url, m.active_yn "
				+ " FROM fn_menu_functional m, fn_menu_functional m1, fn_menu_functional_ancestors a, fn_menu_functional_roles mr "
				+ " WHERE " + " mr.app_id='" + appId + "' " + " AND mr.menu_id = m.menu_id " + " AND UPPER(m.active_yn) = 'Y'"
				+ " AND UPPER(m1.active_yn) ='Y'" + " AND a.menu_id = m.menu_id " + " AND a.ancestor_menu_id = m1.menu_id";
		logQuery(sql);
		logger.debug(EELFLoggerDelegate.debugLogger, "getFunctionalMenuItemsForApp: logged the query");

		@SuppressWarnings("unchecked")
		List<FunctionalMenuItem> menuItems = dataAccessService.executeSQLQuery(sql, FunctionalMenuItem.class, null);
		
		return menuItems;
	}

	public List<FunctionalMenuItem> getFunctionalMenuItemsForUser(String orgUserId) {
		// m represents the functional menu items that are the leaf nodes
		// m1 represents the functional menu items for all the nodes

		// Divide this into 2 queries: one which returns the bottom-level menu items associated with Restricted apps,
		// and one that returns all the other menu items. Then we can easily add the boolean flag
		// restrictedApp to each FunctionalMenuItem, to be used by the front end.
		String sql = "SELECT DISTINCT m1.menu_id, m1.column_num, m1.text, m1.parent_menu_id, m1.url, m.active_yn "
				+ " FROM fn_menu_functional m, fn_menu_functional m1, fn_menu_functional_ancestors a, "
				+ " fn_menu_functional_roles mr, fn_user u , fn_user_role ur " + " WHERE " + " u.org_user_id='" + orgUserId
				+ "' " + " AND u.user_id = ur.user_id " + " AND ur.app_id = mr.app_id " +
				// " AND ur.role_id = mr.role_id " +
				" AND (ur.role_id = mr.role_id " + "     OR ur.role_id = '" + ACCOUNT_ADMIN_ROLE_ID + "') "
				+ " AND m.menu_id = mr.menu_id " + " AND UPPER(m.active_yn) = 'Y'" + " AND UPPER(m1.active_yn) ='Y' "
				+ " AND a.menu_id = m.menu_id " + " AND a.ancestor_menu_id = m1.menu_id "
				+ " UNION "
				// the ancestors of the restricted app menu items
				+ " select m1.menu_id, m1.column_num, m1.text, m1.parent_menu_id, m1.url, m1.active_yn "
				+ " FROM fn_menu_functional m, fn_menu_functional_roles mr, fn_menu_functional m1, "
				+ " fn_menu_functional_ancestors a "
				+ " where a.menu_id = m.menu_id "
				+ " AND a.ancestor_menu_id = m1.menu_id "
				+ " AND m.menu_id != m1.menu_id "
				+ " AND m.menu_id = mr.menu_id "
				+ " AND mr.role_id = '" + RESTRICTED_APP_ROLE_ID + "' "
				+ " AND UPPER(m.active_yn) = 'Y'" + " AND UPPER(m1.active_yn) ='Y' "
				// Add the Favorites menu item
				+ " UNION "
				+ " SELECT m.menu_id, m.column_num, m.text, m.parent_menu_id, m.url, m.active_yn "
				+ " FROM fn_menu_functional m "
				+ " WHERE m.text in ('Favorites','Get Access','Contact Us','Support','User Guide','Help')";
		
		logQuery(sql);
		logger.debug(EELFLoggerDelegate.debugLogger, "getFunctionalMenuItemsForUser: logged the query");

		@SuppressWarnings("unchecked")
		List<FunctionalMenuItem> menuItems = dataAccessService.executeSQLQuery(sql, FunctionalMenuItem.class, null);
		for (FunctionalMenuItem menuItem : menuItems) {
			menuItem.restrictedApp = false;
		}
		
		sql = " SELECT m.menu_id, m.column_num, m.text, m.parent_menu_id, m.url, m.active_yn "
			+ " FROM fn_menu_functional m, fn_menu_functional_roles r "
			+ " WHERE m.menu_id = r.menu_id "
			+ " AND UPPER(m.active_yn) = 'Y' "
			+ " AND r.role_id = '" + RESTRICTED_APP_ROLE_ID + "' ";
		logQuery(sql);
		@SuppressWarnings("unchecked")
		List<FunctionalMenuItem> menuItems2 = dataAccessService.executeSQLQuery(sql, FunctionalMenuItem.class, null);
		for (FunctionalMenuItem menuItem : menuItems2) {
			menuItem.restrictedApp = true;
			menuItems.add(menuItem);
		}
		
		return menuItems;
	}

	public FunctionalMenuItem getFunctionalMenuItemDetails(Integer menuid) {
		// First, fill in the fields that apply to all menu items

		String sql = "SELECT * FROM fn_menu_functional WHERE menu_id = '" + menuid + "'";
		logQuery(sql);
		@SuppressWarnings("unchecked")
		List<FunctionalMenuItem> menuItems = dataAccessService.executeSQLQuery(sql, FunctionalMenuItem.class, null);
		FunctionalMenuItem menuItem = (menuItems == null || menuItems.isEmpty() ? null : menuItems.get(0));
		// If it is a bottom-level menu item, must fill in the appid and the
		// roles
		sql = "SELECT * FROM fn_menu_functional_roles WHERE menu_id = '" + menuid + "'";
		logQuery(sql);
		@SuppressWarnings("unchecked")
		List<FunctionalMenuRole> roleItems = dataAccessService.executeSQLQuery(sql, FunctionalMenuRole.class, null);
		if (roleItems.size() > 0) {
			Integer appid = roleItems.get(0).appId;
			menuItem.appid = appid;
			List<Integer> roles = new ArrayList<Integer>();
			for (FunctionalMenuRole roleItem : roleItems) {
				logger.debug(EELFLoggerDelegate.debugLogger, "LR: app_id: " + roleItem.appId + "; role_id: " + roleItem.roleId + "\n");
				roles.add(roleItem.roleId);
			}
			menuItem.roles = roles;
		}
		
		return menuItem;
	}

	private FieldsValidator menuItemFieldsChecker(FunctionalMenuItemJson menuItemJson) {
		FieldsValidator fieldsValidator = new FieldsValidator();
		try {
			// TODO: validate all the fields
			@SuppressWarnings("unchecked")
			List<FunctionalMenuItem> functionalMenuItems = dataAccessService.getList(FunctionalMenuItem.class,
					" where text = '" + menuItemJson.text + "'", null, null);
			
			boolean dublicatedName = false;
			for (FunctionalMenuItem fnMenuItem : functionalMenuItems) {
				if (menuItemJson.menuId != null && menuItemJson.menuId.equals(fnMenuItem.menuId)) {
					// FunctionalMenuItem should not be compared with itself
					continue;
				}

				if (!dublicatedName && fnMenuItem.text.equalsIgnoreCase(menuItemJson.text)) {
					dublicatedName = true;
					break;
				}
			}
			if (dublicatedName) {
				fieldsValidator.addProblematicFieldName("text");
				fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_CONFLICT);
				fieldsValidator.errorCode = new Long(EPSystemProperties.DUBLICATED_FIELD_VALUE_ECOMP_ERROR);
				logger.debug(EELFLoggerDelegate.debugLogger, "In menuItemFieldsChecker, Error: we have an duplicate text field");
			} else if (StringUtils.isEmpty(menuItemJson.text) && menuItemJson.menuId == null) { 
				// text must be non empty for a create. For an edit, can be empty, which means it is a move request.
				// a null menuId indicates a create.
				fieldsValidator.addProblematicFieldName("text");
				fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_BAD_REQUEST);
				logger.debug(EELFLoggerDelegate.debugLogger, "In menuItemFieldsChecker, Error: we have an empty text field");
			} else {
				// The url, appid, and roles must either be all filled or all empty.
				Boolean urlIsEmpty = StringUtils.isEmpty(menuItemJson.url);
				Boolean rolesIsEmpty = menuItemJson.roles == null || menuItemJson.roles.isEmpty();
				Boolean appidIsEmpty = menuItemJson.appid == null || menuItemJson.appid == 0;
				logger.debug(EELFLoggerDelegate.debugLogger, "LR: menuItemfieldsChecker: urlIsEmpty: " + urlIsEmpty + "; rolesIsEmpty: " + rolesIsEmpty + "; appidIsEmpty: " + appidIsEmpty +"\n");
				if (!((urlIsEmpty && rolesIsEmpty && appidIsEmpty) || (!urlIsEmpty && !rolesIsEmpty && !appidIsEmpty)))
				{
					fieldsValidator.addProblematicFieldName("url,roles,appid");
					fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_BAD_REQUEST);
					logger.debug(EELFLoggerDelegate.debugLogger, "In menuItemFieldsChecker, Error: we don't have: either all 3 fields empty or all 3 fields nonempty");
				} else {
					logger.debug(EELFLoggerDelegate.debugLogger, "In menuItemFieldsChecker, Success: either all 3 fields empty or all 3 fields nonempty");
				}
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred while validating the FunctionalMenuItems. Details: " + EcompPortalUtils.getStackTrace(e));
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
		return fieldsValidator;
	}
	
	// Turn foreign key checks on or off
	protected void setForeignKeys(Session localSession, Boolean on) {
		String keyCheck = "0";
		if (on) {
			keyCheck = "1";
		}
		String sql = "set FOREIGN_KEY_CHECKS="+keyCheck;
		logQuery(sql);
		Query query = localSession.createSQLQuery(sql);
		query.executeUpdate();
	}

	public FieldsValidator createFunctionalMenuItem(FunctionalMenuItemJson menuItemJson) {
		FieldsValidator fieldsValidator = menuItemFieldsChecker(menuItemJson);
		if (fieldsValidator.httpStatusCode.intValue() == HttpServletResponse.SC_OK) {
			logger.debug(EELFLoggerDelegate.debugLogger, "LR: createFunctionalMenuItem: test 1");
			boolean result = false;
			Session localSession = null;
			Transaction transaction = null;
			try {
				FunctionalMenuItem menuItem = new FunctionalMenuItem();
				menuItem.appid = menuItemJson.appid;
				menuItem.roles = menuItemJson.roles;
				menuItem.url = menuItemJson.url;
				menuItem.text = menuItemJson.text;
				menuItem.parentMenuId = menuItemJson.parentMenuId;
				menuItem.active_yn = "Y";
				localSession = sessionFactory.openSession();
				
				// If the app is disabled, deactivate the menu item.
				if (menuItemJson.appid != null) {
					Long appidLong = Long.valueOf(menuItemJson.appid);
					EPApp app = (EPApp) localSession.get(EPApp.class, appidLong);
					if (app != null && ! app.getEnabled()) {
						menuItem.active_yn = "N";
					}
				}

				// Set the column number to 1 higher than the highest column
				// number under this parent.
				Criteria criteria = localSession.createCriteria(FunctionalMenuItem.class);
				criteria.setProjection(Projections.max("column"));
				criteria.add(Restrictions.eq("parentMenuId", menuItem.parentMenuId));
				Integer maxColumn = (Integer) criteria.uniqueResult();
				if (maxColumn == null) {
					maxColumn = 0;
				}
				menuItem.column = maxColumn + 1;
				logger.debug(EELFLoggerDelegate.debugLogger, "about to create menu item: " + menuItem.toString());

				transaction = localSession.beginTransaction();
				// localSession.saveOrUpdate(newMenuItem);
				localSession.save(menuItem);
				Long menuid = menuItem.menuId;
				menuItemJson.menuId = menuid;
				logger.debug(EELFLoggerDelegate.debugLogger, "after saving menu object, new id: " + menuid);

				// Next, save all the roles

				addRoles(menuItemJson, localSession);
				transaction.commit();
				result = true;
			} catch (Exception e) {
				EcompPortalUtils.rollbackTransaction(transaction, 
						"createFunctionalMenuItem rollback, exception = " + e);
				logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
			} finally {
				EcompPortalUtils.closeLocalSession(localSession, "createFunctionalMenuItem");
			}
			if (result) {
			} else {
				logger.debug(EELFLoggerDelegate.debugLogger, "LR: createFunctionalMenuItem: no result. setting httpStatusCode to "
						+ HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		} else {
			logger.error(EELFLoggerDelegate.errorLogger, "FunctionalMenuServiceImpl.createFunctionalMenuItem: bad request");
		}
		return fieldsValidator;
	}

	/* Add all the roles in the menu item to the database */
	public void addRoles(FunctionalMenuItemJson menuItemJson, Session localSession) {
		logger.debug(EELFLoggerDelegate.debugLogger, "entering addRoles.");
		List<Integer> roles = menuItemJson.roles;
		if (roles != null && roles.size() > 0) {
			Integer appid = menuItemJson.appid;
			Long menuid = menuItemJson.menuId;
			for (Integer roleid : roles) {
				logger.debug(EELFLoggerDelegate.debugLogger, "about to create record for role: " + roleid);
				FunctionalMenuRole role = new FunctionalMenuRole();
				role.appId = appid;
				role.menuId = menuid;
				role.roleId = roleid;
				localSession.save(role);
				logger.debug(EELFLoggerDelegate.debugLogger, "after saving role menu object, new id: " + role.id);
			}
		}
	}

	/* Delete all the roles associated with the menu item from the database */
	public void deleteRoles(Long menuId) {
		dataAccessService.deleteDomainObjects(FunctionalMenuRole.class, "menu_id='" + menuId + "'", null);
	}

	/* Delete all favorites associated with the menu item from the database */
	public void deleteFavorites(Long menuId) {
		dataAccessService.deleteDomainObjects(FavoritesFunctionalMenuItem.class, "menu_id='" + menuId + "'", null);
	}
	
	private Boolean parentMenuIdEqual(Integer menuId1, Integer menuId2) {
		return ((menuId1 == null && menuId2 == null) || (menuId1 != null && menuId1.equals(menuId2)));
	}

	private void updateColumnForSiblings(Session localSession, Long menuId, Integer oldParentMenuId,
			Integer newParentMenuId, Integer oldColumn, Integer newColumn) {
		logger.debug(EELFLoggerDelegate.debugLogger, "entering updateColumnForSiblings");
		Criteria criteria = localSession.createCriteria(FunctionalMenuItem.class);
		criteria.add(Restrictions.ne("menuId", menuId));
		if (parentMenuIdEqual(oldParentMenuId, newParentMenuId)) {
			logger.debug(EELFLoggerDelegate.debugLogger, "moving under the same parent");
			// We are moving to a new position under the same parent
			if (newParentMenuId == null) {
				logger.debug(EELFLoggerDelegate.debugLogger, "newParentMenuId is null, so using isNull");
				criteria.add(Restrictions.isNull("parentMenuId"));
			} else {
				logger.debug(EELFLoggerDelegate.debugLogger, "newParentMenuId is NOT null, so using eq");
				criteria.add(Restrictions.eq("parentMenuId", newParentMenuId));
			}
			if (oldColumn > newColumn) {
				logger.debug(EELFLoggerDelegate.debugLogger, "moving to a lower column under the same parent");
				// We are moving to a lower column under the same parent
				criteria.add(Restrictions.ge("column", newColumn));
				criteria.add(Restrictions.lt("column", oldColumn));
				@SuppressWarnings("unchecked")
				List<FunctionalMenuItem> menuItems = criteria.list();
				for (FunctionalMenuItem menuItem : menuItems) {
					menuItem.column += 1;
					localSession.save(menuItem);
				}
			} else if (oldColumn < newColumn) {
				logger.debug(EELFLoggerDelegate.debugLogger, "moving to a higher column under the same parent");
				// We are moving to a higher column under the same parent
				criteria.add(Restrictions.gt("column", oldColumn));
				criteria.add(Restrictions.le("column", newColumn));
				@SuppressWarnings("unchecked")
				List<FunctionalMenuItem> menuItems = criteria.list();
				for (FunctionalMenuItem menuItem : menuItems) {
					menuItem.column -= 1;
					localSession.save(menuItem);
				}
			} else {
				// No info has changed
				logger.debug(EELFLoggerDelegate.debugLogger, "no info has changed, so we are not moving");
			}
		} else {
			logger.debug(EELFLoggerDelegate.debugLogger, "moving under a new parent");
			// We are moving under a new parent.

			// Adjust the children under the old parent
			logger.debug(EELFLoggerDelegate.debugLogger, "about to adjust the children under the old parent");

			// If the parentId is null, must check for its children differently
			if (oldParentMenuId == null) {
				logger.debug(EELFLoggerDelegate.debugLogger, "oldParentMenuId is null, so using isNull");
				criteria.add(Restrictions.isNull("parentMenuId"));
			} else {
				logger.debug(EELFLoggerDelegate.debugLogger, "oldParentMenuId is NOT null, so using eq");
				criteria.add(Restrictions.eq("parentMenuId", oldParentMenuId));
			}

			criteria.add(Restrictions.gt("column", oldColumn));
			@SuppressWarnings("unchecked")
			List<FunctionalMenuItem> menuItems1 = criteria.list();
			for (FunctionalMenuItem menuItem : menuItems1) {
				menuItem.column -= 1;
				localSession.save(menuItem);
			}
			// Adjust the children under the new parent.
			logger.debug(EELFLoggerDelegate.debugLogger, "about to adjust the children under the new parent");
			logger.debug(EELFLoggerDelegate.debugLogger, "get all menu items where menuId!=" + menuId + "; parentMenuId==" + newParentMenuId
					+ "; column>=" + newColumn);
			criteria = localSession.createCriteria(FunctionalMenuItem.class);
			criteria.add(Restrictions.ne("menuId", menuId));
			if (newParentMenuId == null) {
				logger.debug(EELFLoggerDelegate.debugLogger, "newParentMenuId is null, so using isNull");
				criteria.add(Restrictions.isNull("parentMenuId"));
			} else {
				logger.debug(EELFLoggerDelegate.debugLogger, "newParentMenuId is NOT null, so using eq");
				criteria.add(Restrictions.eq("parentMenuId", newParentMenuId));
			}

			criteria.add(Restrictions.ge("column", newColumn));
			@SuppressWarnings("unchecked")
			List<FunctionalMenuItem> menuItems2 = criteria.list();
			if (menuItems2 != null) {
				logger.debug(EELFLoggerDelegate.debugLogger, "found " + menuItems2.size() + " menu items");
			} else {
				logger.debug(EELFLoggerDelegate.debugLogger, "found null menu items");
			}
			for (FunctionalMenuItem menuItem : menuItems2) {
				menuItem.column += 1;
				localSession.save(menuItem);
			}
		}
		logger.debug(EELFLoggerDelegate.debugLogger, "done with updateColumnForSiblings");
	}

	public void removeAppInfo(Session localSession, Long menuId) {
		// Remove the url, role, and app info from a menu item
		FunctionalMenuItem menuItem = (FunctionalMenuItem) localSession.get(FunctionalMenuItem.class, menuId);
		menuItem.url = "";
		deleteRoles(menuId);
	}

	public FieldsValidator editFunctionalMenuItem(FunctionalMenuItemJson menuItemJson) {
		boolean result 			= false;
		Session localSession 	= null;
		Transaction transaction = null;
		Long menuId 			= menuItemJson.menuId;

		logger.debug(EELFLoggerDelegate.debugLogger, "LR: editFunctionalMenuItem: test 1");
		FieldsValidator fieldsValidator = menuItemFieldsChecker(menuItemJson);
		if (fieldsValidator.httpStatusCode.intValue() == HttpServletResponse.SC_OK) {
			// TODO: make sure menuId is here. And, it might not already exist
			// in db table.
			if (menuId == null) {
				fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_BAD_REQUEST);
				logger.error(EELFLoggerDelegate.errorLogger, "FunctionalMenuServiceImpl.editFunctionalMenuItem: bad request");
			} else {
				// To simplify the code, assume we will have a transaction
				try {
					localSession = sessionFactory.openSession();
					transaction = localSession.beginTransaction();

					// Get the existing info associated with menuItem from the DB
					FunctionalMenuItem menuItem = (FunctionalMenuItem) localSession.get(FunctionalMenuItem.class, menuId);
					Integer oldColumn = menuItem.column;
					Integer oldParentMenuId = menuItem.parentMenuId;
					Integer newColumn = menuItemJson.column;
					Integer newParentMenuId = menuItemJson.parentMenuId;

					logger.debug(EELFLoggerDelegate.debugLogger, "prev info: column: " + oldColumn + "; parentMenuId: " + oldParentMenuId);

					if (menuItemJson.appid != null && menuItemJson.roles != null && !menuItemJson.roles.isEmpty()
							&& menuItemJson.url != null && !menuItemJson.url.isEmpty() && menuItemJson.text != null
							&& !menuItemJson.text.isEmpty()) {
						// Scenario: appid, roles, url and text are all non-null.
						// This menu item is associated with an app.
						// (Note: this should only occur for a bottom-level menu
						// item with no children.)
						// 1) Remove all the records from fn_menu_functional_role
						// for this menuId.
						// 2) Add records to the fn_menu_function_role table for the
						// appId and each roleId
						// 3) Update the url and text for this menu item.

						// Because of foreign key constraints, delete the roles,
						// then update the menuItem then add the roles.
						deleteRoles(menuId);
						// Assumption: this is not a Move, so don't change the
						// parentMenuId and column.
						menuItem.appid = menuItemJson.appid;
						menuItem.roles = menuItemJson.roles;
						menuItem.url = menuItemJson.url;
						menuItem.text = menuItemJson.text;
						
						// If the app is disabled, deactivate the menu item.
						Long appidLong = Long.valueOf(menuItemJson.appid);
						EPApp app = (EPApp) localSession.get(EPApp.class, appidLong);
						if (app != null && ! app.getEnabled()) {
							menuItem.active_yn = "N";
						} else {
							menuItem.active_yn = "Y";
						}


						localSession.update(menuItem);
						addRoles(menuItemJson, localSession);

					} else if (menuItemJson.appid == null && (menuItemJson.roles == null || menuItemJson.roles.isEmpty())
							&& (menuItemJson.url == null || menuItemJson.url.isEmpty()) && menuItemJson.text != null
							&& !menuItemJson.text.isEmpty()) {
						// Scenario: appid, roles and url are all null; text is
						// non-null.
						// This menu item is NOT associated with an app.
						// 1) Remove all the records from fn_menu_functional_role
						// for this menuId
						// (in case it was previously associated with an app).
						// 2) Update the text for this menu item.
						// 3) Set the url to ""
						deleteRoles(menuId);
						// Assumption: this is not a Move, so don't change the
						// parentMenuId and column.
						menuItem.text = menuItemJson.text;
						menuItem.url = "";
						menuItem.active_yn = "Y";
						localSession.update(menuItem);

					} else if (newColumn != null) {
						// This is a "move" request.
						// Menu item has been moved to a different position under
						// the same parent, or under a new parent.
						logger.debug(EELFLoggerDelegate.debugLogger, "Doing a move operation.");
						if (parentMenuIdEqual(oldParentMenuId, newParentMenuId)) {
							// The parent is the same. We have just changed the
							// column
							logger.debug(EELFLoggerDelegate.debugLogger, "moving under the same parent");
							menuItem.column = newColumn;
							localSession.update(menuItem);
						} else {
							logger.debug(EELFLoggerDelegate.debugLogger, "moving under a different parent");
							menuItem.parentMenuId = newParentMenuId;
							menuItem.column = newColumn;
							localSession.update(menuItem);
							// If we are moving under a new parent, must delete any
							// app/role info from
							// the new parent, since it is no longer a leaf menu
							// item and cannot have app info
							// associated with it. The front end will have warned
							// the user and gotten confirmation.
							if (menuItemJson.parentMenuId != null) {
								Long parentMenuIdLong = new Long(menuItemJson.parentMenuId);
								removeAppInfo(localSession, parentMenuIdLong);
								// deleteRoles(parentMenuIdLong);
							}
						}
						// must update the column for all old and new sibling menu
						// items
						updateColumnForSiblings(localSession, menuId, oldParentMenuId, newParentMenuId, oldColumn,
								newColumn);
					}

					transaction.commit();
					logger.debug(EELFLoggerDelegate.debugLogger, "LR: editFunctionalMenuItem: finished committing transaction");
					result = true;
				} catch (Exception e) {
					EcompPortalUtils.rollbackTransaction(transaction,
							"createFunctionalMenuItem rollback, exception = " + e);
					logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
				} finally {
					EcompPortalUtils.closeLocalSession(localSession, "editFunctionalMenuItem");
				}
				
				if (result) {
				} else {
					logger.debug(EELFLoggerDelegate.debugLogger, "LR: createFunctionalMenuItem: no result. setting httpStatusCode to "
							+ HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				}
			}
		}	
		
		return fieldsValidator;
	}

	public FieldsValidator deleteFunctionalMenuItem(Long menuId) {
		FieldsValidator fieldsValidator = new FieldsValidator();
		logger.debug(EELFLoggerDelegate.debugLogger, "LR: deleteFunctionalMenuItem: test 1");
		boolean result = false;
		Session localSession = null;
		Transaction transaction = null;

		try {
			localSession = sessionFactory.openSession();
			transaction = localSession.beginTransaction();
			// We must turn off foreign keys before deleting a menu item. Otherwise there will be a 
			// constraint violation from the ancestors table.
			setForeignKeys(localSession, false);
			deleteRoles(menuId);
			logger.debug(EELFLoggerDelegate.debugLogger, "deleteFunctionalMenuItem: after deleting roles");
			deleteFavorites(menuId);
			logger.debug(EELFLoggerDelegate.debugLogger, "deleteFunctionalMenuItem: after deleting favorites");
			localSession.delete(localSession.get(FunctionalMenuItem.class, menuId));
			logger.debug(EELFLoggerDelegate.debugLogger, "deleteFunctionalMenuItem: about to commit");
			transaction.commit();
			result = true;
		} catch (Exception e) {
			EcompPortalUtils.rollbackTransaction(transaction,
					"deleteFunctionalMenuItem rollback, exception = " + e);
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
		} finally {
			EcompPortalUtils.closeLocalSession(localSession, "deleteFunctionalMenuItem");
		}
		if (result) {
		} else {
			logger.debug(EELFLoggerDelegate.debugLogger, "LR: deleteFunctionalMenuItem: no result. setting httpStatusCode to "
					+ HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return fieldsValidator;
	}
	
	// Regenerate the fn_menu_functional_ancestors table, which is used
	// by the queries that return the functional menu items.
	public FieldsValidator regenerateAncestorTable() {
		FieldsValidator fieldsValidator = new FieldsValidator();
		Session localSession = null;
		Transaction transaction = null;

		try {
			localSession = sessionFactory.openSession();
			transaction = localSession.beginTransaction();
			String sql = "DELETE FROM fn_menu_functional_ancestors";
			logQuery(sql);
			Query query = localSession.createSQLQuery(sql);
			query.executeUpdate();
			logger.debug(EELFLoggerDelegate.debugLogger, "regenerateAncestorTable: finished query 1");
			
			sql = "ALTER TABLE fn_menu_functional_ancestors AUTO_INCREMENT=1";
			logQuery(sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();
			logger.debug(EELFLoggerDelegate.debugLogger, "regenerateAncestorTable: reset AUTO_INCREMENT to 1");

			int depth = 0;
			sql = "INSERT INTO fn_menu_functional_ancestors(menu_id, ancestor_menu_id, depth) "
					+ "SELECT m.menu_id, m.menu_id, " + depth + " FROM fn_menu_functional m";
			logQuery(sql);
			query = localSession.createSQLQuery(sql);
			query.executeUpdate();
			logger.debug(EELFLoggerDelegate.debugLogger, "regenerateAncestorTable: finished query 2");
			for (depth = 0; depth < 3; depth++) {
				int depthPlusOne = depth + 1;
				sql = "INSERT INTO fn_menu_functional_ancestors(menu_id, ancestor_menu_id, depth) "
						+ " SELECT a.menu_id, m.parent_menu_id, " + depthPlusOne
						+ " FROM fn_menu_functional m, fn_menu_functional_ancestors a " + " WHERE a.depth='" + depth
						+ "' AND " + " a.ancestor_menu_id = m.menu_id AND " + " m.parent_menu_id != '-1'";
				logQuery(sql);
				query = localSession.createSQLQuery(sql);
				query.executeUpdate();
			}
			logger.debug(EELFLoggerDelegate.debugLogger, "regenerateAncestorTable: finished query 3");
			transaction.commit();
		} catch (Exception e) {
			EcompPortalUtils.rollbackTransaction(transaction,
					"regenerateAncestorTable rollback, exception = " + e);
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} finally {
			EcompPortalUtils.closeLocalSession(localSession, "regenerateAncestorTable");
		}
		return fieldsValidator;
	}

	private void logQuery(String sql) {
		logger.debug(EELFLoggerDelegate.debugLogger, "Executing query: " + sql);
	}

	public FieldsValidator setFavoriteItem(FavoritesFunctionalMenuItem menuItemJson) {
		boolean result = false;
		FieldsValidator fieldsValidator = new FieldsValidator();
		
		Session localSession = null;
		Transaction transaction = null;
		
		try {
            logger.debug(EELFLoggerDelegate.debugLogger, String.format("Before adding favorite for user id:{0} and menu id:{1} ",menuItemJson.userId,menuItemJson.menuId));
			localSession = sessionFactory.openSession();
			transaction = localSession.beginTransaction();
			localSession.save(menuItemJson);
			transaction.commit();				
			result = true;
            logger.debug(EELFLoggerDelegate.debugLogger, String.format("After adding favorite for user id:{0} and menu id:{1} ",menuItemJson.userId,menuItemJson.menuId));								
		} catch (Exception e) {
			EcompPortalUtils.rollbackTransaction(transaction,"setFavoriteItem rollback, exception = " + e);
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
		} finally {
			EcompPortalUtils.closeLocalSession(localSession, "setFavoriteItem");
		}											
		
		if(result) {
		}
		else {
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
		return fieldsValidator;
	}
	
	public List<FavoritesFunctionalMenuItemJson> getFavoriteItems(Long userId) {		
		try {
			logger.debug(EELFLoggerDelegate.debugLogger, "Before getting favorites for user id: " + userId);
			
			// Divide this into 2 queries: one which returns the favorites items associated with Restricted apps,
			// and one that returns all the other favorites items. Then we can easily add the boolean flag
			// restrictedApp to each FavoritesFunctionalMenuItemJson, to be used by the front end.

			String sql = "SELECT f.user_id,f.menu_id,m.text,m.url "
					+ " FROM fn_menu_favorites f, fn_menu_functional m, fn_menu_functional_roles mr "
					+ " WHERE f.user_id='" + userId + "' AND f.menu_id = m.menu_id "
					+ " AND f.menu_id = mr.menu_id "
					+ " AND mr.role_id = '" + RESTRICTED_APP_ROLE_ID + "' ";

			@SuppressWarnings("unchecked")
			List<FavoritesFunctionalMenuItemJson> menuItems = dataAccessService.executeSQLQuery(sql, FavoritesFunctionalMenuItemJson.class, null);
			for (FavoritesFunctionalMenuItemJson menuItem : menuItems) {
				menuItem.restrictedApp = true;
			}
			
			sql = "SELECT DISTINCT f.user_id,f.menu_id,m.text,m.url "
					+ " FROM fn_menu_favorites f, fn_menu_functional m, fn_menu_functional_roles mr "
					+ " WHERE f.user_id='" + userId + "' AND f.menu_id = m.menu_id "
					+ " AND f.menu_id = mr.menu_id "
					+ " AND mr.role_id != '" + RESTRICTED_APP_ROLE_ID + "' ";
			@SuppressWarnings("unchecked")
			List<FavoritesFunctionalMenuItemJson> menuItems2 = dataAccessService.executeSQLQuery(sql, FavoritesFunctionalMenuItemJson.class, null);
			for (FavoritesFunctionalMenuItemJson menuItem : menuItems2) {
				menuItem.restrictedApp = false;
				menuItems.add(menuItem);
			}
			
			return menuItems;
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Exception occurred in FunctionalMenuServiceImpl.getFavoriteItems. Details: " + EcompPortalUtils.getStackTrace(e));
			List<FavoritesFunctionalMenuItemJson> menuItems = new ArrayList<FavoritesFunctionalMenuItemJson>();
			return menuItems;
		}																	
	}
	
	public FieldsValidator removeFavoriteItem(Long userId, Long menuId) {
		boolean result = false;
		FieldsValidator fieldsValidator = new FieldsValidator();
		
		Session localSession = null;
		Transaction transaction = null;
		
		try {			
			
			FavoritesFunctionalMenuItem menuItemJson = new FavoritesFunctionalMenuItem();
			menuItemJson.userId = userId;
			menuItemJson.menuId = menuId;
			
			localSession = sessionFactory.openSession();
			transaction = localSession.beginTransaction();
			localSession.delete(menuItemJson);
			localSession.flush();
			transaction.commit();				
			result = true;
			logger.debug(EELFLoggerDelegate.debugLogger, String.format("After removing favorite for user id: " + userId + "; menu id: " + menuId));								
		} catch (Exception e) {
			EcompPortalUtils.rollbackTransaction(transaction,"removeFavoriteItem rollback, exception = " + e);
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
		} finally {								
			EcompPortalUtils.closeLocalSession(localSession, "removeFavoriteItem");
		}											
		
		if(result) {
		}
		else {
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
		return fieldsValidator;
	}
	
	@Override
	public void assignHelpURLs(List<FunctionalMenuItem> menuItems) {
		try {
			String user_guide_link = SystemProperties.getProperty(EPSystemProperties.USER_GUIDE_URL);
			
			for(FunctionalMenuItem menuItem: menuItems){
				if(menuItem.text.equalsIgnoreCase("Contact Us")){
					menuItem.setUrl("contactUs");
					//menuItem.setRestrictedApp(true);
				}
				if(menuItem.text.equalsIgnoreCase("Get Access")) {
					menuItem.setUrl("getAccess");
				}
				if(menuItem.text.equalsIgnoreCase("User Guide")) {
					menuItem.setUrl(user_guide_link);
					menuItem.setRestrictedApp(true);
				}
			}
		} catch (Exception e) {		
			logger.error(EELFLoggerDelegate.errorLogger, "assignHelpURLs process failed. Details: " + EcompPortalUtils.getStackTrace(e));
		}
		
	}
}
