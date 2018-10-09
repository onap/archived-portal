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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BEPropertyTest {

    
    private static final String TEST="test";
    
    @Test
    public  void test() {
        BEProperty property=new BEProperty(TEST, TEST);
        BEProperty beProperty=new BEProperty(TEST, TEST);
        BEProperty be=property;
        
        beProperty.setKey(property.getKey());
        beProperty.setValue(property.getValue());
        
        assertEquals(property.hashCode(), beProperty.hashCode());
        assertTrue(beProperty.equals(property));
        assertTrue(be.equals(property));
        assertFalse(be.equals(null));
        be.setValue(null);
        assertTrue(be.equals(property));
        be.setKey(null);
        assertTrue(be.equals(property));
        
        assertFalse(property.equals(this));
        
        property.setKey(null);
        beProperty.setKey("notnull");
        assertFalse(property.equals(beProperty));
        assertFalse(beProperty.equals(property));
        
        property.setKey("notnull");
        beProperty.setKey("notnull");
        property.setValue(null);
        beProperty.setValue("notnull");
        assertFalse(property.equals(beProperty));
        assertFalse(beProperty.equals(property));
        
        
    }
}
