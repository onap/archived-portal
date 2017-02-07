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
package org.openecomp.portalapp.portal.transport;

import java.util.ArrayList;
import java.util.List;

/**
 * User description which we receive in response from request to remote
 * application: applicationsRestClientService.get(RemoteUserWithRoles[].class,
 * appId, "/users"). It contains the most important info about remote
 * application user including his roles in this application.
 */
public class RemoteUserWithRoles {

	public Long orgId;

	public Long managerId;

	public String firstName;

	public String middleInitial;

	public String lastName;

	public String phone;

	public String email;

	public String hrid;

	public String orgUserid;

	public String orgCode;

	public String orgManagerUserId;

	public String jobTitle;

	public String loginId;

	public Boolean active;

	public List<RemoteRole> roles = new ArrayList<RemoteRole>();

}
