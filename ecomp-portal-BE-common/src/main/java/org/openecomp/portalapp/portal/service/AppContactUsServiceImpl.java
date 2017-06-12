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
package org.openecomp.portalapp.portal.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalapp.portal.domain.AppContactUs;
import org.openecomp.portalapp.portal.domain.EPApp;
import org.openecomp.portalapp.portal.ecomp.model.AppCategoryFunctionsItem;
import org.openecomp.portalapp.portal.ecomp.model.AppContactUsItem;

/**
 * Provides database access for the contact-us page controllers.
 */
@Transactional
@org.springframework.context.annotation.Configuration
public class AppContactUsServiceImpl implements AppContactUsService {

	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AppContactUsServiceImpl.class);

	@Autowired
	private DataAccessService dataAccessService;

	public DataAccessService getDataAccessService() {
		return dataAccessService;
	}

	public void setDataAccessService(DataAccessService dataAccessService) {
		this.dataAccessService = dataAccessService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openecomp.portalapp.portal.service.AppContactUsService#
	 * getAppContactUs()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<AppContactUsItem> getAppContactUs() throws Exception {
		List<AppContactUsItem> contactUsItemList = (List<AppContactUsItem>) getDataAccessService()
				.executeNamedQuery("getAppContactUsItems", null, null);
		Collections.sort(contactUsItemList, new AppContactUsItemCompare());
		return contactUsItemList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openecomp.portalapp.portal.service.AppContactUsService#
	 * getAllAppsAndContacts()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<AppContactUsItem> getAppsAndContacts() throws Exception {
		List<AppContactUsItem> contactUsItemList = (List<AppContactUsItem>) getDataAccessService()
				.executeNamedQuery("getAppsAndContacts", null, null);
		Collections.sort(contactUsItemList, new AppContactUsItemCompare());
		return contactUsItemList;
	}

	/**
	 * Assists in sorting app-contact-us items by application name.
	 */
	class AppContactUsItemCompare implements Comparator<AppContactUsItem> {
		@Override
		public int compare(AppContactUsItem o1, AppContactUsItem o2) {
			return o1.getAppName().compareTo(o2.getAppName());
		}
	}

	/**
	 * Gets a table of category and function details for apps that participate
	 * in the functional menu.
	 */
	@Override
	public List<AppCategoryFunctionsItem> getAppCategoryFunctions() throws Exception {
		@SuppressWarnings("unchecked")
		// This named query requires no parameters.
		List<AppCategoryFunctionsItem> list = (List<AppCategoryFunctionsItem>) dataAccessService
				.executeNamedQuery("getAppCategoryFunctions", null, null);
		logger.debug(EELFLoggerDelegate.debugLogger, "getAppCategoryFunctions: result list size is " + list.size());
		return list;
	}

	/**
	 * Saves the list of contact-us objects to the database.
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String saveAppContactUs(List<AppContactUsItem> contactUsModelList) throws Exception {
		try {
			for (AppContactUsItem contactUs : contactUsModelList) {
				String status = saveAppContactUs(contactUs);
				if (!status.equals("success"))
					throw new Exception("saveAppContaatUsFailed: service returned " + status);
			}
			return "success";

		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "", e);
			throw new Exception(e);
		}

	}

	/**
	 * Saves a single contact-us object to the database, either creating a new
	 * row or updating if the argument has the ID of an existing row.
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String saveAppContactUs(AppContactUsItem contactUsModel) throws Exception {
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			AppContactUs contactUs = null;
			try {
				contactUs = (AppContactUs) getDataAccessService().getDomainObject(AppContactUs.class,
						contactUsModel.getAppId(), map);
			} catch (Exception e) {
				logger.debug(EELFLoggerDelegate.debugLogger, "saveAppContactUs: not found for App {}, adding new entry",
						contactUsModel.getAppName());
				contactUs = new AppContactUs();
			}

			// Populate the AppContactUs model for the database.
			EPApp app = (EPApp) getDataAccessService().getDomainObject(EPApp.class, contactUsModel.getAppId(), map);
			if (app == null || app.getId() == null)
				throw new Exception("saveAppContactus: App not found for Id " + contactUsModel.getAppId());
			contactUs.setApp(app);
			contactUs.setDescription(contactUsModel.getDescription());
			contactUs.setContactName(contactUsModel.getContactName());
			contactUs.setContactEmail(contactUsModel.getContactEmail());
			contactUs.setActiveYN(contactUsModel.getActiveYN());
			contactUs.setUrl(contactUsModel.getUrl());
			getDataAccessService().saveDomainObject(contactUs, map);
			return "success";
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "saveAppContactUs failed", e);
			throw e;
			// return "failure";
		}
	}

	/**
	 * Deletes the row from the app contact us table with the specified ID.
	 */
	@Override
	public String deleteContactUs(Long id) throws Exception {
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			AppContactUs contactUs = (AppContactUs) getDataAccessService().getDomainObject(AppContactUs.class, id, map);
			if (contactUs.getApp() == null)
				throw new Exception("Delete unsuccessful for Id " + id);
			getDataAccessService().deleteDomainObject(contactUs, map);
			return "success";
		} catch (Exception e) {

			logger.info(EELFLoggerDelegate.errorLogger, "", e);
			throw e;
		}
	}

}
