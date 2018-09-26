/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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
package org.onap.portalapp.portal.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;

public class PortalRestResponseTest {

    PortalRestResponse<String> portalRestResponse1 = new PortalRestResponse<String>();
    PortalRestResponse<String> portalRestResponse2 = new PortalRestResponse<String>();
        
    @Before
    public void setup(){
    	portalRestResponse1.setMessage("test");
        portalRestResponse1.setResponse(new String("testResponse"));
        portalRestResponse1.setStatus(PortalRestStatusEnum.OK);
        
        portalRestResponse2.setMessage("test");
        portalRestResponse2.setResponse(new String("testResponse"));
        portalRestResponse2.setStatus(PortalRestStatusEnum.OK);
    }
   
    @Test
    public void testHashCode(){
        assertEquals(portalRestResponse1.hashCode(), portalRestResponse2.hashCode());
    }
    
    @Test
    public void testEquals(){
    	assertTrue(portalRestResponse1.equals(portalRestResponse2));
    }
}
