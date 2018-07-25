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
package org.onap.portalapp.portal.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.Date;

import org.junit.Test;

public class CentralRoleFunctionTest {
	
	
	private static final String TEST="test";
	private static final Long ID=1l;
	private static final Date DATE=new Date();
	
	@Test
	public void testCentralFunction() {
		CentralRoleFunction centralRoleFunction=buildCentralRoleFunction();
		CentralRoleFunction crf=new CentralRoleFunction(TEST,TEST);
		crf.setId(centralRoleFunction.getId());
		crf.setCreated(centralRoleFunction.getCreated());
		crf.setModified(centralRoleFunction.getModified());
		crf.setCreatedId(centralRoleFunction.getCreatedId());
		crf.setModifiedId(centralRoleFunction.getModifiedId());
		crf.setAuditTrail(centralRoleFunction.getAuditTrail());
		crf.setAuditUserId(centralRoleFunction.getAuditUserId());
		crf.setRowNum(centralRoleFunction.getRowNum());
		crf.setCode(centralRoleFunction.getCode());
		crf.setName(centralRoleFunction.getName());
		crf.setEditUrl(centralRoleFunction.getEditUrl());
		
		assertEquals(centralRoleFunction.hashCode(), crf.hashCode());
		assertTrue(centralRoleFunction.equals(crf));
		centralRoleFunction.compareTo(crf);
		
		
	}
	
	
	private CentralRoleFunction buildCentralRoleFunction() {
		
		CentralRoleFunction crf=new CentralRoleFunction();
		crf.setId(ID);
		crf.setCreated(DATE);
		crf.setModified(DATE);
		crf.setCreatedId(ID);
		crf.setModifiedId(ID);
		crf.setAuditTrail(null);
		crf.setAuditUserId(TEST);
		crf.setRowNum(ID);
		crf.setCode(TEST);
		crf.setName(TEST);
		crf.setEditUrl(TEST);
	return crf;
	}

}
