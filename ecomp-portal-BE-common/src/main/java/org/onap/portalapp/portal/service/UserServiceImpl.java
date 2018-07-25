/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017-2018 AT&T Intellectual Property. All rights reserved.
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
 * 
 */
package org.onap.portalapp.portal.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalsdk.core.domain.FusionObject.Utilities;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(UserServiceImpl.class);

	@Autowired
	private DataAccessService dataAccessService;

	public DataAccessService getDataAccessService() {
		return dataAccessService;
	}

	public void setDataAccessService(DataAccessService dataAccessService) {
		this.dataAccessService = dataAccessService;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EPUser> getUserByUserId(String userId) {

		if (SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM).trim().equalsIgnoreCase("OIDC")) {
			List<EPUser> users = new ArrayList<EPUser>();
			List<EPUser> filterdUsers = new ArrayList<EPUser>();
			BufferedReader in = null;
			HttpURLConnection con = null;
			try {
				String url = EPCommonSystemProperties.getProperty(EPCommonSystemProperties.AUTH_USER_SERVER);
				URL obj = new URL(url);

				con = (HttpURLConnection) obj.openConnection();

				// optional default is GET
				con.setRequestMethod("GET");
				con.setConnectTimeout(3000);
				con.setReadTimeout(8000);

				StringBuffer response = new StringBuffer();

				in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
				String inputLine;
				while ((inputLine = in.readLine()) != null)
					response.append(inputLine);
				JSONObject jObject = new JSONObject(response.toString()); // json
				JSONArray jsonUsers = jObject.getJSONArray("response"); // get
																		// data
																		// object
				for (int i = 0; i < jsonUsers.length(); i++) {
					JSONObject eachObject = jsonUsers.getJSONObject(i);
					EPUser eachUser = new EPUser();
					eachUser.setOrgUserId(eachObject.get("id").toString());
					eachUser.setFirstName(eachObject.get("givenName").toString());
					eachUser.setLastName(eachObject.get("familyName").toString());
					eachUser.setEmail(eachObject.get("email").toString());
					users.add(eachUser);
				}

				for (int i = 0; i < users.size(); i++) {
					if (Utilities.nvl(userId).length() > 0) {
						if (!userId.equalsIgnoreCase(users.get(i).getOrgUserId())) {
							continue;
						}
					}
					filterdUsers.add(users.get(i));
				}

			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "getUserByUserId failed", e);
			} finally {
				try {
					if(in!=null) {
					in.close();
					}
					con.disconnect();
				} catch (IOException e) {
					logger.error(EELFLoggerDelegate.errorLogger, "getUserByUserId failed to close", e);
				}
			}

			return filterdUsers;

		} else {

			List<Criterion> restrictionsList = new ArrayList<Criterion>();
			Criterion orgUserIdCriterion = Restrictions.eq("orgUserId",userId);
			restrictionsList.add(orgUserIdCriterion);
			
			List<EPUser> list = (List<EPUser>) dataAccessService.getList(EPUser.class, null, restrictionsList, null);

			return (list == null || list.size() == 0) ? null : list;

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EPUser> getUserByFirstLastName(String firstName, String lastName) {

		if (!SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM).trim().equalsIgnoreCase("OIDC")) {
			List<Criterion> restrictionsList = new ArrayList<Criterion>();
			Criterion firstNameCriterion = Restrictions.eq("firstName", firstName);
			Criterion lastNameCriterion = Restrictions.eq("lastName", lastName);
			restrictionsList.add(Restrictions.or(firstNameCriterion, lastNameCriterion));

			List<EPUser> list = (List<EPUser>) dataAccessService.getList(EPUser.class, null, restrictionsList, null);
			return (list == null || list.size() == 0) ? null : list;

		} else {

			List<EPUser> users = new ArrayList<EPUser>();
			List<EPUser> filterdUsers = new ArrayList<EPUser>();
			BufferedReader in = null;
			HttpURLConnection con = null;
			try {
				String url = EPCommonSystemProperties.getProperty(EPCommonSystemProperties.AUTH_USER_SERVER);
				URL obj = new URL(url);

				con = (HttpURLConnection) obj.openConnection();

				// optional default is GET
				con.setRequestMethod("GET");
				con.setConnectTimeout(3000);
				con.setReadTimeout(8000);

				StringBuffer response = new StringBuffer();

				in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				JSONObject jObject = new JSONObject(response.toString()); // json
				JSONArray jsonUsers = jObject.getJSONArray("response"); // get
																		// data
																		// object
				for (int i = 0; i < jsonUsers.length(); i++) {
					JSONObject eachObject = jsonUsers.getJSONObject(i);
					EPUser eachUser = new EPUser();
					eachUser.setOrgUserId(eachObject.get("id").toString());// getString("id"));
					eachUser.setFirstName(eachObject.get("givenName").toString());
					eachUser.setLastName(eachObject.get("familyName").toString());
					eachUser.setEmail(eachObject.get("email").toString());
					users.add(eachUser);
				}

				for (int i = 0; i < users.size(); i++) {

					if (Utilities.nvl(firstName).length() > 0) {
						if (!firstName.equalsIgnoreCase(users.get(i).getFirstName())) {
							continue;
						}
					}
					if (Utilities.nvl(lastName).length() > 0) {
						if (!lastName.equalsIgnoreCase(users.get(i).getLastName())) {
							continue;
						}
					}

					filterdUsers.add(users.get(i));

				}

			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "getUserByFirstLastName failed", e);
			} finally {
				try {
					if(in!=null) {
					in.close();
					con.disconnect();
					}
				} catch (IOException e) {
					logger.error(EELFLoggerDelegate.errorLogger, "getUserByFirstLastName failed to close", e);
				}
			}

			return filterdUsers;
		}

	}

	@SuppressWarnings("unchecked")
	public String saveNewUser(EPUser newUser, String checkDuplicate) throws Exception {

		try {

			List<Criterion> restrictionsList = new ArrayList<Criterion>();
			Criterion orgUserIdCriterion = Restrictions.eq("orgUserId",newUser.getLoginId());
			restrictionsList.add(orgUserIdCriterion);
			List<EPUser> list = (List<EPUser>) dataAccessService.getList(EPUser.class, null, restrictionsList, null);
			
			if (list == null || list.size() == 0) {
				newUser.setActive(true);
				newUser.setOrgUserId(newUser.getLoginId());
				newUser.setLoginPwd(CipherUtil.encryptPKC(newUser.getLoginPwd()));
				getDataAccessService().saveDomainObject(newUser, null);
			} else {
				if (checkDuplicate.equals("Yes")) {
					// userId already exist in database
					return "Record already exist";
				} else {

					EPUser oldUser = (EPUser) list.get(0);
					oldUser.setFirstName(newUser.getFirstName());
					oldUser.setLastName(newUser.getLastName());
					oldUser.setMiddleInitial(newUser.getMiddleInitial());
					if (!oldUser.getLoginPwd().equals(newUser.getLoginPwd()))
						oldUser.setLoginPwd(CipherUtil.encryptPKC(newUser.getLoginPwd()));
					else
						oldUser.setLoginPwd(newUser.getLoginPwd());
					getDataAccessService().saveDomainObject(oldUser, null);

				}

			}

		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "saveNewUser failed", e);
			throw new Exception(e);
		}
		return "success";
	};

	@Override
	public void saveUser(EPUser user) throws Exception {
		getDataAccessService().saveDomainObject(user, null);
	}

}
