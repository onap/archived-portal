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

package org.onap.portal.utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.onap.portal.domain.db.fn.FnRole;
import org.onap.portal.domain.db.fn.FnRoleComposite;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.db.fn.FnUserRole;
import org.onap.portal.domain.dto.transport.Role;
import org.onap.portal.exception.RoleFunctionException;
import org.onap.portal.service.fn.old.EPRoleFunctionService;
import org.onap.portalsdk.core.domain.RoleFunction;
import org.onap.portalsdk.core.exception.SessionExpiredException;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.menu.MenuBuilder;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.portalsdk.core.web.support.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;

@NoArgsConstructor
public class EPUserUtils {

       public static final String ALL_ROLE_FUNCTIONS = "allRoleFunctions";

       private static final String decodeValueOfForwardSlash = "2f";
       private static final String decodeValueOfHyphen = "2d";
       private static final String decodeValueOfAsterisk = "2a";
       private static final Long ACCOUNT_ADMIN_ROLE_ID = 999L;

       private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(EPUserUtils.class);
       private static DataAccessService dataAccessService;

       /**
        * Gets the EPUser object from the session.
        *
        * @param request HttpServletRequest
        * @return EPUser object that was created upon login
        * @throws SessionExpiredException if no session exists.
        */
       public static FnUser getUserSession(HttpServletRequest request) {
              HttpSession session = AppUtils.getSession(request);
              if (session == null) {
                     throw new SessionExpiredException();
              }
              return (FnUser) session.getAttribute(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME));
       }

       /**
        * Establishes the user's portal session
        *
        * @param request HttpServletRequest
        * @param user EPUser
        * @param applicationMenuData Menu data
        * @param businessDirectMenuData Menu data
        * @param ePRoleFunctionService role function service
        */
       @SuppressWarnings("rawtypes")
       public static void setUserSession(HttpServletRequest request, FnUser user, Set applicationMenuData,
               Set businessDirectMenuData, EPRoleFunctionService ePRoleFunctionService) throws RoleFunctionException {
              HttpSession session = request.getSession(true);

              // clear the current user session to avoid any conflicts
              EPUserUtils.clearUserSession(request);
              session.setAttribute(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME), user);

              setAllRoleFunctions(ePRoleFunctionService.getRoleFunctions(), session);

              ePRoleFunctionService.getRoleFunctions(request, user);

              // truncate the role (and therefore the role function) data to save
              // memory in the session
              user.setFnRoles(null);
              session.setAttribute(SystemProperties.getProperty(SystemProperties.USER_NAME), user.getFullName());

              ServletContext context = session.getServletContext();
              try {
                     context.getAttribute("licenseVerification");
              } catch (Exception e) {
                     logger.error(EELFLoggerDelegate.errorLogger,
                             "setUserSession failed to get licenseVerification attribute",
                             e);
              }
              session.setAttribute(SystemProperties.getProperty(SystemProperties.APP_DISPLAY_NAME), "My Portal");
              session.setAttribute(SystemProperties.getProperty(SystemProperties.APPLICATION_MENU_ATTRIBUTE_NAME),
                      MenuBuilder.filterMenu(applicationMenuData, request));
              session.setAttribute(SystemProperties.getProperty(SystemProperties.BUSINESS_DIRECT_MENU_ATTRIBUTE_NAME),
                      MenuBuilder.filterMenu(businessDirectMenuData, request));
       }

       /**
        * Creates a set of role function names and stores the set as a session attribute.
        *
        * @param allRoleFunctions List of role functions.
        * @param session HttpSession
        */
       private static void setAllRoleFunctions(List<RoleFunction> allRoleFunctions, HttpSession session)
               throws RoleFunctionException {
              if (allRoleFunctions == null) {
                     return;
              }
              Set<String> roleFnSet = new HashSet<>();
              for (RoleFunction roleFn : allRoleFunctions) {
                     roleFnSet.add(decodeFunctionCode(roleFn.getCode()));
              }
              session.setAttribute(ALL_ROLE_FUNCTIONS, roleFnSet);
       }


       public static String decodeFunctionCode(String str) throws RoleFunctionException {
              String decodedString = str;
              List<Pattern> decodingList = new ArrayList<>();
              decodingList.add(Pattern.compile(decodeValueOfForwardSlash));
              decodingList.add(Pattern.compile(decodeValueOfHyphen));
              decodingList.add(Pattern.compile(decodeValueOfAsterisk));
              for (Pattern xssInputPattern : decodingList) {
                     try {
                            decodedString = decodedString.replaceAll("%" + xssInputPattern,
                                    new String(Hex.decodeHex(xssInputPattern.toString().toCharArray())));
                     } catch (DecoderException e) {
                            logger.error(EELFLoggerDelegate.errorLogger, "Failed to decode the Rolefunction: " + str,
                                    e);
                            throw new RoleFunctionException("decode failed", e);
                     }
              }

              return decodedString;
       }

       /**
        * Removes all stored attributes from the user's session
        *
        * @param request HttpServletRequest
        * @throws SessionExpiredException if no session exists
        */
       private static void clearUserSession(HttpServletRequest request) {
              HttpSession session = AppUtils.getSession(request);
              if (session == null) {
                     throw new SessionExpiredException();
              }

              // removes all stored attributes from the current user's session
              session.removeAttribute(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME));
              session.removeAttribute(SystemProperties.getProperty(SystemProperties.APPLICATION_MENU_ATTRIBUTE_NAME));
              session.removeAttribute(
                      SystemProperties.getProperty(SystemProperties.BUSINESS_DIRECT_MENU_ATTRIBUTE_NAME));
              session.removeAttribute(SystemProperties.getProperty(SystemProperties.ROLES_ATTRIBUTE_NAME));
              session.removeAttribute(SystemProperties.getProperty(SystemProperties.ROLE_FUNCTIONS_ATTRIBUTE_NAME));
       }

       /**
        * Gets role information from the user session, in the cached user object. As a side effect sets a session
        * variable with the roles.
        *
        * @param request HttpServletRequest
        * @return Map of role ID to role object
        */
       @SuppressWarnings("rawtypes")
       public static Map getRoles(HttpServletRequest request) {
              HashMap roles;

              HttpSession session = AppUtils.getSession(request);
              roles = (HashMap) session
                      .getAttribute(SystemProperties.getProperty(SystemProperties.ROLES_ATTRIBUTE_NAME));

              // if roles are not already cached, let's grab them from the user
              // session
              if (roles == null) {
                     FnUser user = getUserSession(request);

                     // get all user roles (including the tree of child roles)
                     roles = getAllUserRoles(user);

                     session.setAttribute(SystemProperties.getProperty(SystemProperties.ROLES_ATTRIBUTE_NAME), roles);
              }

              return roles;
       }

       /**
        * Builds a map of role ID to role object.
        *
        * @param user EPUser
        * @return Map of role ID to role object
        */
       @SuppressWarnings({"rawtypes", "unchecked"})
       private static HashMap getAllUserRoles(FnUser user) {
              HashMap roles = new HashMap();

              for (FnRole role : user.getFnRoles()) {
                     if (role.getActiveYn()) {
                            roles.put(role.getId(), role);
                            addChildRoles(role, roles);
                     }
              }

              // Additionally; the account admin role is overloaded between onap
              // portal and partners; lets also include that
              for (FnUserRole epUserApp : user.getUserApps()) {
                     FnRole role = epUserApp.getRoleId();

                     if (role.getActiveYn() && role.getId().equals(ACCOUNT_ADMIN_ROLE_ID)) {
                            roles.put(role.getId(), role);

                            // let's take a recursive trip down the tree to add all child
                            // roles
                            addChildRoles(role, roles);
                     }
              }

              return roles;
       }

       /**
        * Adds all child roles of the specified role to the map of roles.
        *
        * @param role EPRole
        * @param roles Maps role id to role object
        */
       @SuppressWarnings({"rawtypes", "unchecked"})
       private static void addChildRoles(FnRole role, HashMap roles) {
              List<Role> childRoles = role.getChildRoles()
                  .stream()
                  .map(FnRoleComposite::getChildRoles)
                  .collect(Collectors.toList());
              if (!childRoles.isEmpty()) {
                     for (Object o : childRoles) {
                            FnRole childRole = (FnRole) o;
                            if (childRole.getActiveYn()) {
                                   roles.put(childRole.getId(), childRole);
                                   addChildRoles(childRole, roles);
                            }
                     }
              }

       }

       public static boolean hasRole(FnUser user, String roleKey) {
              return getAllUserRoles(user).keySet().contains(new Long(roleKey));
       }

       public static DataAccessService getDataAccessService() {
              return dataAccessService;
       }

       @Autowired
       public static void setDataAccessService(DataAccessService dataAccessService) {
              EPUserUtils.dataAccessService = dataAccessService;
       }

       /**
        * Gets the user's ID from the user object in the session
        *
        * @param request HttpServletRequest
        * @return Integer ID of current user
        */
       public static int getUserId(HttpServletRequest request) {
              return getUserIdAsLong(request).intValue();
       }

       /**
        * Gets the user's ID from the user object in the session
        *
        * @param request HttpServletREquest
        * @return Long ID of current user
        */
       static Long getUserIdAsLong(HttpServletRequest request) {
              Long userId = new Long(SystemProperties.getProperty(SystemProperties.APPLICATION_USER_ID));
              if (request != null && getUserSession(request) != null) {
                     userId = getUserSession(request).getId();
              }
              return userId;
       }

       /**
        * Gets the request ID from the request.
        *
        * @param request HttpServletRequest
        * @return Request ID
        */
       public static String getRequestId(HttpServletRequest request) {
              Enumeration<String> headerNames = request.getHeaderNames();

              String requestId = "";
              try {
                     while (headerNames.hasMoreElements()) {
                            String headerName = headerNames.nextElement();
                            logger.debug(EELFLoggerDelegate.debugLogger,
                                    "One header is " + headerName + " : " + request.getHeader(headerName));
                            if (headerName.equalsIgnoreCase(SystemProperties.ECOMP_REQUEST_ID)) {
                                   requestId = request.getHeader(headerName);
                                   break;
                            }
                     }
              } catch (Exception e) {
                     logger.error(EELFLoggerDelegate.errorLogger, "getRequestId failed", e);
              }

              return (requestId.isEmpty() ? UUID.randomUUID().toString() : requestId);
       }

       /**
        * Gets the full URL from the request.
        *
        * @param request HttpServletRequest
        * @return Full URL
        */
       static String getFullURL(HttpServletRequest request) {
              if (request != null) {
                     StringBuffer requestURL = request.getRequestURL();
                     String queryString = request.getQueryString();

                     if (queryString == null) {
                            return requestURL.toString();
                     } else {
                            return requestURL.append('?').append(queryString).toString();
                     }
              }
              return "";
       }

       public static Boolean matchRoleFunctions(String portalApiPath, Set<? extends String> roleFunctions) {
              String[] path = portalApiPath.split("/");
              List<String> roleFunList;
              if (path.length > 1) {
                     roleFunList = roleFunctions.stream().filter(item -> item.startsWith(path[0]))
                             .collect(Collectors.toList());
                     if (roleFunList.size() >= 1) {
                            for (String roleFunction : roleFunList) {
                                   String[] roleFunctionArray = roleFunction.split("/");
                                   boolean b = true;
                                   if (roleFunctionArray.length == path.length) {
                                          for (int i = 0; i < roleFunctionArray.length; i++) {
                                                 if (!roleFunctionArray[i].equals("*")) {
                                                        Pattern p = Pattern.compile(Pattern.quote(path[i]),
                                                                Pattern.CASE_INSENSITIVE);
                                                        Matcher m = p.matcher(roleFunctionArray[i]);
                                                        b = m.matches();
                                                 }
                                          }
                                          if (b) {
                                                 return true;
                                          }
                                   }
                            }
                     }
              } else {
                     for (String roleFunction : roleFunctions) {
                            if (roleFunction.equals(("*"))) {
                                   return true;
                            } else if (portalApiPath.matches(roleFunction)) {
                                   return true;
                            }
                     }
              }
              return false;
       }
}
