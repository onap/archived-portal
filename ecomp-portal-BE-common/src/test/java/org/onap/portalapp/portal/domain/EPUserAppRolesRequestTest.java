/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *  Modifications Copyright © 2018 IBM.
 * ================================================================================
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
        
        request.setAppId(null);
        request1.setAppId((long) 12345);
        assertFalse(request.equals(request1));
        
        request.setAppId((long) 54321);
        request1.setAppId((long) 12345);
        assertFalse(request.equals(request1));
        
        request.setAppId((long) 12345);
        request1.setAppId((long) 12345);
        request.setCreatedDate(null);
        request1.setCreatedDate(new Date());
        assertFalse(request.equals(request1));
        
        request.setAppId((long) 12345);
        request1.setAppId((long) 12345);
        Date requstDate = new Date();
        requstDate.setYear(2000);
        request.setCreatedDate(requstDate);
        request1.setCreatedDate(new Date());
        assertFalse(request.equals(request1));
        
        request.setAppId((long) 12345);
        request1.setAppId((long) 12345);
        request.setCreatedDate(new Date());
        request1.setCreatedDate(new Date());
        request.setEpRequestIdDetail(null);
        request1.setEpRequestIdDetail(new HashSet<EPUserAppRolesRequestDetail>());
        assertFalse(request.equals(request1));
        
        request.setAppId((long) 12345);
        request1.setAppId((long) 12345);
        request.setCreatedDate(new Date());
        request1.setCreatedDate(new Date());
        HashSet<EPUserAppRolesRequestDetail> details = new HashSet<EPUserAppRolesRequestDetail>();
        details.add(new EPUserAppRolesRequestDetail());
        request.setEpRequestIdDetail(details);
        request1.setEpRequestIdDetail(new HashSet<EPUserAppRolesRequestDetail>());
        assertFalse(request.equals(request1));
        
        
        request.setAppId((long) 12345);
        request1.setAppId((long) 12345);
        request.setCreatedDate(new Date());
        request1.setCreatedDate(new Date());
        request.setEpRequestIdDetail(new HashSet<EPUserAppRolesRequestDetail>());
        request1.setEpRequestIdDetail(new HashSet<EPUserAppRolesRequestDetail>());
        request.setRequestStatus(null);
        request1.setRequestStatus("notnull");
        assertFalse(request.equals(request1));
        
        request.setAppId((long) 12345);
        request1.setAppId((long) 12345);
        request.setCreatedDate(new Date());
        request1.setCreatedDate(new Date());
        request.setEpRequestIdDetail(new HashSet<EPUserAppRolesRequestDetail>());
        request1.setEpRequestIdDetail(new HashSet<EPUserAppRolesRequestDetail>());
        request.setRequestStatus("status");
        request1.setRequestStatus("notnull");
        assertFalse(request.equals(request1));
        
        request.setAppId((long) 12345);
        request1.setAppId((long) 12345);
        request.setCreatedDate(new Date());
        request1.setCreatedDate(new Date());
        request.setEpRequestIdDetail(new HashSet<EPUserAppRolesRequestDetail>());
        request1.setEpRequestIdDetail(new HashSet<EPUserAppRolesRequestDetail>());
        request.setRequestStatus("notnull");
        request1.setRequestStatus("notnull");
        request.setUpdatedDate(null);
        request1.setUpdatedDate(new Date());
        assertFalse(request.equals(request1));
        
        request.setAppId((long) 12345);
        request1.setAppId((long) 12345);
        request.setCreatedDate(new Date());
        request1.setCreatedDate(new Date());
        request.setEpRequestIdDetail(new HashSet<EPUserAppRolesRequestDetail>());
        request1.setEpRequestIdDetail(new HashSet<EPUserAppRolesRequestDetail>());
        request.setRequestStatus("notnull");
        request1.setRequestStatus("notnull");
        Date date = new Date();
        date.setYear(2000);
        request.setUpdatedDate(date);
        request1.setUpdatedDate(new Date());
        assertFalse(request.equals(request1));
        
        
        request.setAppId((long) 12345);
        request1.setAppId((long) 12345);
        request.setCreatedDate(new Date());
        request1.setCreatedDate(new Date());
        request.setEpRequestIdDetail(new HashSet<EPUserAppRolesRequestDetail>());
        request1.setEpRequestIdDetail(new HashSet<EPUserAppRolesRequestDetail>());
        request.setRequestStatus("notnull");
        request1.setRequestStatus("notnull");
        request.setUpdatedDate(new Date());
        request1.setUpdatedDate(new Date());
        request.setUserId(null);
        request1.setUserId((long) 12345);
        assertFalse(request.equals(request1));
        
        request.setAppId((long) 12345);
        request1.setAppId((long) 12345);
        request.setCreatedDate(new Date());
        request1.setCreatedDate(new Date());
        request.setEpRequestIdDetail(new HashSet<EPUserAppRolesRequestDetail>());
        request1.setEpRequestIdDetail(new HashSet<EPUserAppRolesRequestDetail>());
        request.setRequestStatus("notnull");
        request1.setRequestStatus("notnull");
        request.setUpdatedDate(new Date());
        request1.setUpdatedDate(new Date());
        request.setUserId((long) 54321);
        request1.setUserId((long) 12345);
        assertFalse(request.equals(request1));
        
        assertFalse(request.equals(this));
        assertFalse(request.equals(null));
        
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
