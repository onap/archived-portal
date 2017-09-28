/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the “License”);
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
 * under the Creative Commons License, Attribution 4.0 Intl. (the “License”);
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
package org.openecomp.portalapp.portal.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.utils.EPSystemProperties;
import org.openecomp.portalsdk.core.FusionObject.Utilities;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.onboarding.util.CipherUtil;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(UserServiceImpl.class);

	@Autowired
	private DataAccessService dataAccessService;

	public DataAccessService getDataAccessService() {
		return dataAccessService;
	}

	public void setDataAccessService(DataAccessService dataAccessService) {
		this.dataAccessService = dataAccessService;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getUserByUserId(String userId) {

		if (SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM).trim().equalsIgnoreCase("OIDC")) {
			List<EPUser> users = new ArrayList<EPUser>();
			List<EPUser> filterdUsers = new ArrayList<EPUser>();
			BufferedReader in = null;
			HttpURLConnection con = null;
			try {
				String url = EPSystemProperties.getProperty(EPSystemProperties.AUTH_USER_SERVER);
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
				JSONArray jsonUsers = jObject.getJSONArray("response"); // get data object
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
					in.close();
					con.disconnect();
				} catch (IOException e) {
					logger.error(EELFLoggerDelegate.errorLogger, "getUserByUserId 2 failed", e);
				}
			}

			return filterdUsers;

		} else {

			List list = null;
			StringBuffer criteria = new StringBuffer();
			criteria.append(" where org_user_id = '").append(userId).append("'");
			list = getDataAccessService().getList(EPUser.class, criteria.toString(), null, null);
			return (list == null || list.size() == 0) ? null : list;

		}

	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getUserByFirstLastName(String firstName, String lastName) {

		if (!SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM).trim().equalsIgnoreCase("OIDC")) {

			List list = null;
			StringBuffer criteria = new StringBuffer();
			if (firstName != null)
				criteria.append(" where first_name = '").append(firstName).append("'");
			if (lastName != null)
				criteria.append(" where last_name = '").append(lastName).append("'");
			list = getDataAccessService().getList(EPUser.class, criteria.toString(), null, null);
			return (list == null || list.size() == 0) ? null : list;

		} else {

			List<EPUser> users = new ArrayList<EPUser>();
			List<EPUser> filterdUsers = new ArrayList<EPUser>();
			BufferedReader in = null;
			HttpURLConnection con = null;
			try {
				String url = EPSystemProperties.getProperty(EPSystemProperties.AUTH_USER_SERVER);
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
				JSONArray jsonUsers = jObject.getJSONArray("response"); // get data object
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
					in.close();
					con.disconnect();
				} catch (IOException e) {
					logger.error(EELFLoggerDelegate.errorLogger, "getUserByFirstLastName 2 failed", e);
				}
			}

			return filterdUsers;
		}

	}

	public String saveNewUser(EPUser newUser, String checkDuplicate) throws Exception {
		try {
			List list = null;
			StringBuffer criteria = new StringBuffer();
			criteria.append(" where org_user_id = '").append(newUser.getLoginId()).append("'");
			list = getDataAccessService().getList(EPUser.class, criteria.toString(), null, null);
			if (list == null || list.size() == 0) {
				newUser.setActive(true);
				newUser.setOrgUserId(newUser.getLoginId());
				newUser.setLoginPwd(CipherUtil.encrypt(newUser.getLoginPwd()));
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
						oldUser.setLoginPwd(CipherUtil.encrypt(newUser.getLoginPwd()));
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

}
