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

package org.onap.portal.domain.dto.ecomp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.SafeHtml;
import org.onap.portalsdk.core.domain.User;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EPUser extends User {

       private Long orgId;
       private Long managerId;
       @SafeHtml
       private String firstName;
       @SafeHtml
       private String middleInitial;
       @SafeHtml
       private String lastName;
       @SafeHtml
       private String phone;
       @SafeHtml
       private String fax;
       @SafeHtml
       private String cellular;
       @SafeHtml
       private String email;
       private Long addressId;
       @SafeHtml
       private String alertMethodCd;
       @SafeHtml
       private String hrid;
       @SafeHtml
       private String orgUserId;
       @SafeHtml
       private String orgCode;
       @SafeHtml
       private String address1;
       @SafeHtml
       private String address2;
       @SafeHtml
       private String city;
       @SafeHtml
       private String state;
       @SafeHtml
       private String zipCode;
       @SafeHtml
       private String country;
       @SafeHtml
       private String orgManagerUserId;
       @SafeHtml
       private String locationClli;
       @SafeHtml
       private String businessCountryCode;
       @SafeHtml
       private String businessCountryName;
       @SafeHtml
       private String businessUnit;
       @SafeHtml
       private String businessUnitName;
       @SafeHtml
       private String department;
       @SafeHtml
       private String departmentName;
       @SafeHtml
       private String companyCode;
       @SafeHtml
       private String company;
       @SafeHtml
       private String zipCodeSuffix;
       @SafeHtml
       private String jobTitle;
       @SafeHtml
       private String commandChain;
       @SafeHtml
       private String siloStatus;
       @SafeHtml
       private String costCenter;
       @SafeHtml
       private String financialLocCode;
       @SafeHtml
       private String loginId;
       @SafeHtml
       private String loginPwd;
       private Date lastLoginDate;
       private boolean active;
       private boolean internal;
       private Long selectedProfileId;
       private Long timeZoneId;
       private boolean online;
       @SafeHtml
       private String chatId;
       private boolean systemUser;
       private Integer languageId;
       private static final long serialVersionUID = 1L;
       private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(EPUser.class);
       private static final String ECOMP_PORTAL_NAME = "ECOMP";
       private boolean isGuest = false;
       @Valid
       private SortedSet<EPUserApp> userApps = new TreeSet<>();
       @Valid
       private SortedSet<EPRole> pseudoRoles = new TreeSet<>();

       @JsonIgnore
       public String getFullName() {
              return getFirstName() + " " + getLastName();
       }

       public int compareTo(Object obj) {
              EPUser user = (EPUser) obj;

              String c1 = getLastName() + getFirstName() + getMiddleInitial();
              String c2 = user.getLastName() + user.getFirstName() + user.getMiddleInitial();

              return c1.compareTo(c2);
       }


       public void addAppRoles(EPApp app, SortedSet<EPRole> roles) {
              if (roles != null) {
                     // add all
                     SortedSet<EPUserApp> userApps = new TreeSet<>();
                     // this.userApps.removeAll(this.userApps);
                     for (EPRole role : roles) {
                            EPUserApp userApp = new EPUserApp();
                            userApp.setUserId(this.id);
                            userApp.setApp(app);
                            userApp.setRole(role);
                            userApps.add(userApp);
                     }
                     setUserApps(userApps);
              } else {
                     setUserApps(null);
              }

       }

       public SortedSet<EPRole> getAppEPRoles(EPApp app) {

              logger.debug(EELFLoggerDelegate.debugLogger, "In EPUser.getAppEPRoles() - app = {}", app.getName());

              SortedSet<EPRole> roles = new TreeSet<>();
              SortedSet<EPUserApp> userAppRoles = getUserApps();

              logger.debug(EELFLoggerDelegate.debugLogger, "In EPUser.getAppEPRoles() - userApps = {} ",
                      userAppRoles.size());

              Iterator<EPUserApp> userAppRolesIterator = userAppRoles.iterator();

              EPUserApp userAppRole;
              // getting default app
              while (userAppRolesIterator.hasNext()) {
                     EPUserApp tempUserApp = userAppRolesIterator.next();
                     if (tempUserApp.getApp().getId().equals(app.getId())) {

                            logger.debug(EELFLoggerDelegate.debugLogger,
                                    "In EPUser.getAppEPRoles() - for user {}, found application {}", this.getFullName(),
                                    app.getName());

                            userAppRole = tempUserApp;

                            EPRole role = userAppRole.getRole();
                            if (role.isActive()) {
                                   logger.debug(EELFLoggerDelegate.debugLogger,
                                           "In EPUser.getAppEPRoles() - Role {} is active - adding for user {} and app {}",
                                           role.getName(), this.getFullName(), app.getName());
                                   roles.add(role);
                            } else {
                                   logger.debug(EELFLoggerDelegate.debugLogger,
                                           "In EPUser.getAppEPRoles() - Role {} is NOT active - NOT adding for user {} and app {}",
                                           role.getName(), this.getFullName(), app.getName());
                            }
                     }
              }
              logger.debug(EELFLoggerDelegate.debugLogger, "In EPUser.getAppEPRoles() - roles = {}", roles.size());

              return roles;
       }

}
