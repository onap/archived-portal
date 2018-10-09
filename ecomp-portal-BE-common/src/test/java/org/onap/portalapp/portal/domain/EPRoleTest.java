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

package org.onap.portalapp.portal.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;
import org.onap.portalapp.portal.domain.EPRole;
import org.onap.portalsdk.core.domain.RoleFunction;
import org.onap.portalsdk.core.restful.domain.EcompRoleFunction;

public class EPRoleTest {

    
    @Test
    public void testEpRole() {
        EPRole role=new EPRole();
        role.setActive(true);
        role.setAppId(1l);
        role.setAppRoleId(2l);
        role.setId(3l);
        role.setName("TEST_ADMIN");
        SortedSet<EPRole> childRoles = new TreeSet<EPRole>();
        EPRole child=new EPRole();
        child.setActive(true);
        child.setAppId(1l);
        child.setAppRoleId(3l);
        child.setId(6l);
        child.setName("TEST_USER");
        childRoles.add(child);
        role.setChildRoles(childRoles);
        SortedSet<EPRole> parentRoles = new TreeSet<EPRole>();
        EPRole parent=new EPRole();
        parent.setActive(true);
        parent.setAppId(1l);
        parent.setAppRoleId(3l);
        parent.setId(6l);
        parent.setName("TEST_USER");
        parentRoles.add(parent);
        role.setParentRoles(parentRoles);
        
        SortedSet<RoleFunction> rolefunction = new TreeSet<RoleFunction>();
        RoleFunction function=new RoleFunction();
        function.setAction("Test");;
        function.setCode("code");
        rolefunction.add(function);
        role.setRoleFunctions(rolefunction);
        role.setPriority(5);
        role.setAppRoleId(3l);
        assertEquals(3l, role.getAppRoleId().longValue());
        assertNotNull(role.getChildRoles());
        assertNotNull(role.getParentRoles());
        assertNotNull(role.getRoleFunctions());
        role.compareTo(role);
        assertEquals(1l, role.getAppId().longValue());
        assertEquals("TEST_ADMIN",role.getName());
        role.removeChildRole(6l);
        role.removeParentRole(6l);
        assertEquals(role.toString(), "[Id = 3, name = TEST_ADMIN]");
        role.removeRoleFunction("code");
        role.addChildRole(child);
        role.addParentRole(parent);
        role.addRoleFunction(function);
        
        parent.setAppId(null);
        child.setAppId((long) 1234);
        assertEquals(parent.compareTo(child), -1);
        
        child.setAppId(null);
        parent.setAppId((long) 1234);
        assertEquals(parent.compareTo(child), 1);
        
        
        
    }
    
}

