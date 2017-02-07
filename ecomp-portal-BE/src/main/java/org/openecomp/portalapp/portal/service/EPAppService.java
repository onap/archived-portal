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

import java.util.List;

import org.openecomp.portalapp.portal.domain.AdminUserApplications;
import org.openecomp.portalapp.portal.domain.AppIdAndNameTransportModel;
import org.openecomp.portalapp.portal.domain.AppsResponse;
import org.openecomp.portalapp.portal.domain.EPApp;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.domain.EcompApp;
import org.openecomp.portalapp.portal.domain.UserRoles;
import org.openecomp.portalapp.portal.transport.FieldsValidator;
import org.openecomp.portalapp.portal.transport.LocalRole;
import org.openecomp.portalapp.portal.transport.OnboardingApp;
import org.openecomp.portalapp.portal.ecomp.model.AppCatalogItem;

public interface EPAppService {

	/**
	 * Get all applications adminId is an admin
	 * 
	 * @param adminId
	 *            - the admin
	 * @return the admin's applications
	 */
	List<EPApp> getUserAsAdminApps(EPUser user);

	List<EPApp> getUserByOrgUserIdAsAdminApps(String attuid);

	/**
	 * Gets all rows and all fields from the fn_app table.
	 * 
	 * @return list of EPApp objects
	 */
	List<EPApp> getAppsFullList();

	/**
	 * Gets all rows and most fields from the fn_app table.
	 * 
	 * @return list of EcompApp objects.
	 */
	List<EcompApp> getEcompAppAppsFullList();

	/**
	 * Get apps with app app admins
	 * 
	 * @return
	 */
	List<AdminUserApplications> getAppsAdmins();

	/**
	 * Get all apps from fn_app table (index, name, title only). If all is true,
	 * return active and inactive apps; otherwise, just active apps.
	 * 
	 * @return List of AppsResponse objects.
	 */
	List<AppsResponse> getAllApps(Boolean all);

	UserRoles getUserProfile(String loginId);

	List<LocalRole> getAppRoles(Long appId);

	List<AppIdAndNameTransportModel> getAdminApps(EPUser user);

	List<AppIdAndNameTransportModel> getAppsForSuperAdminAndAccountAdmin(EPUser user);
	
	List<EPApp> getUserRemoteApps(String id);

	/**
	 * Gets the applications accessible to the specified user, which includes
	 * all enabled open applications, plus all enabled applications for which
	 * the user has a defined role for that app.
	 * 
	 * @param user
	 *            EPUser object with the user's UID 
	 * @return the user's list of applications, which may be empty.
	 */
	List<EPApp> getUserApps(EPUser user);

	/**
	 * Gets the user-personalized list of applications for the Portal (super)
	 * admin, which includes enabled open applications, enabled applications for
	 * which the user has a defined role for that app, and/or enabled
	 * applications which the user has chosen to show.
	 * 
	 * @param user
	 *            EPUser object with the user's UID
	 * @return the user's personalized list of applications, which may be empty.
	 */
	List<EPApp> getPersAdminApps(EPUser user);

	/**
	 * Gets the user-personalized list of accessible applications, which
	 * includes enabled open applications and/or enabled applications for which
	 * the user has a defined role for that app. Personalization means the user
	 * can indicate an accessible application should be excluded from this
	 * result.
	 * 
	 * @param user
	 *            EPUser object with the user's UID
	 * @return the user's personalized list of applications, which may be empty.
	 */
	List<EPApp> getPersUserApps(EPUser user);

	/**
	 * Gets the application catalog for the specified user who is a super admin.
	 * This includes all enabled applications. Each item indicates whether the
	 * user has access (open or via a role), and whether the application is
	 * selected for showing in the user's home (applications) page. Admin sees
	 * slightly different behavior - can force an app onto the home page using
	 * the personalization feature (user-app-selection table).
	 * 
	 * @param user
	 * @return list of all enabled applications, which may be empty
	 */
	List<AppCatalogItem> getAdminAppCatalog(EPUser user);

	/**
	 * Gets the application catalog for the specified user, who is a regular
	 * user. This includes all enabled applications. Each item indicates whether
	 * the user has access (open or via a role), and whether the application is
	 * selected for showing in the user's home (applications) page.
	 * 
	 * @param user
	 * @return list of all enabled applications, which may be empty
	 */
	List<AppCatalogItem> getUserAppCatalog(EPUser user);

	List<OnboardingApp> getOnboardingApps();

	List<OnboardingApp> getEnabledNonOpenOnboardingApps();

	FieldsValidator modifyOnboardingApp(OnboardingApp modifiedOnboardingApp, EPUser user);

	FieldsValidator addOnboardingApp(OnboardingApp newOnboardingApp, EPUser user);

	FieldsValidator deleteOnboardingApp(EPUser user, Long onboardingAppId);

	List<EcompApp> transformAppsToEcompApps(List<EPApp> appsList);

	EPApp getApp(Long appId);

	void writeAppsImagesToDiskCacheIfNecessary();

}