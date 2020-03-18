/*
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 * Modifications Copyright (c) 2019 Samsung
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

package org.onap.portal.service.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.utils.EPCommonSystemProperties;
import org.onap.portal.utils.EPSystemProperties;
import org.onap.portalsdk.core.domain.FusionObject.Utilities;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FnUserService implements UserDetailsService {

    private final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(FnUserService.class);

    private final FnUserDao fnUserDao;

    @Autowired
    public FnUserService(FnUserDao fnUserDao) {
        this.fnUserDao = fnUserDao;
    }

    @Override
    public FnUser loadUserByUsername(final String username) throws UsernameNotFoundException {
        Optional<FnUser> fnUser = fnUserDao.findByLoginId(username);
        if (fnUser.isPresent()) {
            return fnUser.get();
        } else {
            throw new UsernameNotFoundException("User not found for username: " + username);
        }
    }

    public FnUser saveFnUser(final FnUser fnUser) {
        return fnUserDao.save(fnUser);
    }

    public Optional<FnUser> getUser(final Long id) {
        return Optional.of(fnUserDao.getOne(id));
    }

    public List<FnUser> getUserWithOrgUserId(final String orgUserIdValue) {
        return fnUserDao.getUserWithOrgUserId(orgUserIdValue).orElse(new ArrayList<>());
    }

    public List<FnUser> getUsersByOrgIds(final List<String> orgIds) {
        return fnUserDao.getUsersByOrgIds(orgIds).orElse(new ArrayList<>());
    }

    public List<FnUser> getActiveUsers() {
        return fnUserDao.getActiveUsers().orElse(new ArrayList<>());
    }

    public void deleteUser(final FnUser fnUser) {
        fnUserDao.delete(fnUser);
    }

    public boolean existById(final Long userId) {
        return fnUserDao.existsById(userId);
    }

    public List<FnUser> findAll() {
        return fnUserDao.findAll();
    }

    public List<FnUser> saveAll(final List<FnUser> fnUsers) {
        return fnUserDao.saveAll(fnUsers);
    }

    public FnUser save(final FnUser user) {
        return fnUserDao.save(user);
    }

    public void delete(final FnUser user) {
        fnUserDao.delete(user);
    }

    public List<FnUser> findByFirstNameAndLastName(final String firstName, final String lastName) {
        return fnUserDao.findByFirstNameAndLastName(firstName, lastName).orElse(new ArrayList<>());
    }

    public List<FnUser> getUserByUserId(String userId) {
        if (SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM).trim().equalsIgnoreCase("OIDC")) {
            List<FnUser> users = new ArrayList<>();
            List<FnUser> filterdUsers = new ArrayList<>();
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
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                JSONObject jObject = new JSONObject(response.toString()); // json
                JSONArray jsonUsers = jObject.getJSONArray("response"); // get data object
                for (int i = 0; i < jsonUsers.length(); i++) {
                    JSONObject eachObject = jsonUsers.getJSONObject(i);
                    FnUser eachUser = new FnUser();
                    eachUser.setOrgUserId(eachObject.get("id").toString());// getString("id"));
                    eachUser.setFirstName(eachObject.get("givenName").toString());
                    eachUser.setLastName(eachObject.get("familyName").toString());
                    eachUser.setEmail(eachObject.get("email").toString());
                    users.add(eachUser);
                }

                for (FnUser user : users) {

                    if (Utilities.nvl(userId).length() > 0) {
                        if (!userId.equalsIgnoreCase(user.getOrgUserId())) {
                            continue;
                        }
                    }
                    filterdUsers.add(user);

                }

            } catch (Exception e) {
                logger.error(EELFLoggerDelegate.errorLogger, "getUserByUserId failed", e);
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                    con.disconnect();
                } catch (IOException e) {
                    logger.error(EELFLoggerDelegate.errorLogger, "getUserByUserId 2 failed", e);
                }
            }
            return filterdUsers;
        } else {
            List<FnUser> list = this.getUserWithOrgUserId(userId);
            return (list == null || list.size() == 0) ? null : list;
        }

    }

    public List<FnUser> getUserByFirstLastName(String firstName, String lastName) {
        if (!SystemProperties.getProperty(SystemProperties.AUTHENTICATION_MECHANISM).trim().equalsIgnoreCase("OIDC")) {
            List<FnUser> list = this.findByFirstNameAndLastName(firstName, lastName);
            return (list == null || list.size() == 0) ? null : list;
        } else {
            List<FnUser> users = new ArrayList<>();
            List<FnUser> filterdUsers = new ArrayList<>();
            BufferedReader in = null;
            HttpURLConnection con = null;
            try {
                String url = EPCommonSystemProperties.getProperty(EPCommonSystemProperties.AUTH_USER_SERVER);
                URL obj = new URL(url);
                con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(3000);
                con.setReadTimeout(8000);
                StringBuffer response = new StringBuffer();
                in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                JSONObject jObject = new JSONObject(response.toString());
                JSONArray jsonUsers = jObject.getJSONArray("response");
                for (int i = 0; i < jsonUsers.length(); i++) {
                    JSONObject eachObject = jsonUsers.getJSONObject(i);
                    FnUser eachUser = new FnUser();
                    eachUser.setOrgUserId(eachObject.get("id").toString());
                    eachUser.setFirstName(eachObject.get("givenName").toString());
                    eachUser.setLastName(eachObject.get("familyName").toString());
                    eachUser.setEmail(eachObject.get("email").toString());
                    users.add(eachUser);
                }
                for (FnUser user : users) {
                    if (Utilities.nvl(firstName).length() > 0) {
                        if (!firstName.equalsIgnoreCase(user.getFirstName())) {
                            continue;
                        }
                    }
                    if (Utilities.nvl(lastName).length() > 0) {
                        if (!lastName.equalsIgnoreCase(user.getLastName())) {
                            continue;
                        }
                    }
                    filterdUsers.add(user);
                }
            } catch (Exception e) {
                logger.error(EELFLoggerDelegate.errorLogger, "getUserByFirstLastName failed", e);
            } finally {
                try {
                    if (in != null) {
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
}
