/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
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
package org.onap.portalapp.portal.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class EPUserAppRolesRequestTest {
	
	private static final String TEST="test";
	Date date=new Date();
	
	
	@Test
	public void testEpUserAppRoles() {
		EPUserAppRolesRequest request=new EPUserAppRolesRequest();
		request=buildEPUserAppRolesRequest(request);
		EPUserAppRolesRequest request1=new EPUserAppRolesRequest();
		request1=buildEPUserAppRolesRequest(request1);
		
		assertEquals(request.getRequestStatus(), "test");
		request.getAppId();
		request.getUserId();
		assertEquals(request.getCreated(),date);
		assertEquals(request.getCreatedDate(),date);
		assertEquals(request.getUpdatedDate(),date);
		assertNotNull(request.getEpRequestIdDetail());
		assertEquals(request.hashCode(), request1.hashCode());
		assertTrue(request.equals(request1));
		assertNotNull(request.toString());
		
		
	}
	private EPUserAppRolesRequest buildEPUserAppRolesRequest(EPUserAppRolesRequest request) {
		request.setUserId(1l);
		request.setAppId(1l);
		request.setCreated(date);
		request.setCreatedDate(date);
		request.setUpdatedDate(date);
		request.setRequestStatus(TEST);
		Set<EPUserAppRolesRequestDetail>  epRequestIdDetail=new HashSet<>();
		EPUserAppRolesRequestDetail detail=new EPUserAppRolesRequestDetail();
		detail.setId(1l);
		epRequestIdDetail.add(detail);
		request.setEpRequestIdDetail(epRequestIdDetail);
		
		return request;
	}

}
