/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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
package org.onap.portalapp.command;

import static org.junit.Assert.*;

import org.apache.bcel.generic.AASTORE;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.onap.portalapp.command.PostSearchBean;
import org.onap.portalapp.portal.core.MockEPUser;
import org.onap.portalapp.portal.domain.EPUser;

public class PostSearchBeanTest {

	@InjectMocks
	PostSearchBean mockPostSearchBean = new PostSearchBean();
	
     MockEPUser mockUser = new MockEPUser();
    public PostSearchBean mockPostSearchBean(){
	
	PostSearchBean postSearchBean = new PostSearchBean();
	EPUser user = mockUser.mockEPUser();
	EPUser userOrig=mockUser.mockEPUser();
	postSearchBean.setUserOrig(userOrig);
	
	postSearchBean.setUser(user);
	//postSearchBean.setUserOrig(null);
	postSearchBean.setSelected(null);
	postSearchBean.setHrid(null);
	postSearchBean.setPostUserId(null);
	postSearchBean.setPostFirstName(null);
	postSearchBean.setPostLastName(null);
	postSearchBean.setPostOrgCode(null);
	postSearchBean.setPostPhone(null);
	postSearchBean.setPostEmail(null);
	postSearchBean.setPostAddress1(null);
	postSearchBean.setPostAddress2(null);
	postSearchBean.setPostCity(null);
	postSearchBean.setPostState(null);
	postSearchBean.setPostZipCode(null);
	postSearchBean.setPostLocationClli(null);
	postSearchBean.setPostBusinessCountryCode(null);
	postSearchBean.setPostBusinessCountryName(null);
	postSearchBean.setPostDepartment(null);
	postSearchBean.setPostDepartmentName(null);
	postSearchBean.setPostBusinessUnit(null);
	postSearchBean.setPostBusinessUnitName(null);
	postSearchBean.setPostJobTitle(null);
	postSearchBean.setOrgManagerUserId(null);
	postSearchBean.setPostCommandChain(null);
	postSearchBean.setPostCompanyCode(null);
	postSearchBean.setPostCostCenter(null);
	postSearchBean.setPostSiloStatus(null);
	postSearchBean.setPostFinancialLocCode(null);
	postSearchBean.setPostManagerUserId(null);
	postSearchBean.setPostHrid(null);
	postSearchBean.setPostCompany(null);
	postSearchBean.setFirstName(null);
	postSearchBean.setLastName(null);
	postSearchBean.setOrgUserId(null);
	postSearchBean.setEmail(null);
	postSearchBean.setFirstNameOrig(null);
	postSearchBean.setLastNameOrig(null);
	postSearchBean.setHridOrig(null);
	postSearchBean.setOrgUserIdOrig(null);
	postSearchBean.setOrgCodeOrig(null);
	postSearchBean.setEmailOrig(null);
	postSearchBean.setOrgManagerUserIdOrig(null);
	return postSearchBean;
	}
    
	 @Test
	 public void postSearchBeanTest()
	 {
	 PostSearchBean postSearchBean = mockPostSearchBean();
	 EPUser user = mockUser.mockEPUser();
	 assertEquals(postSearchBean.getUser().getActive(), user.getActive());
	 assertNotNull(postSearchBean.getUserOrig());
	 assertNull(postSearchBean.getSelected());
	 assertNull(postSearchBean.getHrid());
	 assertNull(postSearchBean.getPostOrgUserId());
	 assertNull(postSearchBean.getPostFirstName());
	 assertNull(postSearchBean.getPostLastName());
	 assertNull(postSearchBean.getPostOrgCode());
	 assertNull(postSearchBean.getPostPhone());
	 assertNull(postSearchBean.getPostEmail());
	 assertNull(postSearchBean.getPostAddress1());
	 assertNull(postSearchBean.getPostAddress2());
	 assertNull(postSearchBean.getPostCity());
	 assertNull(postSearchBean.getPostState());
	 assertNull(postSearchBean.getPostZipCode());
	 assertNull(postSearchBean.getPostLocationClli());
	 assertNull(postSearchBean.getPostBusinessCountryCode());
	 assertNull(postSearchBean.getPostBusinessCountryName());
	 assertNull(postSearchBean.getPostDepartment());
	 assertNull(postSearchBean.getPostDepartmentName());
	 assertNull(postSearchBean.getPostBusinessUnit());
	 assertNull(postSearchBean.getPostBusinessUnitName());
	 assertNull(postSearchBean.getPostJobTitle());
	 assertNull(postSearchBean.getOrgManagerUserId());
	 assertNull(postSearchBean.getPostCommandChain());
	 assertNull(postSearchBean.getPostCompanyCode());
	 assertNull(postSearchBean.getPostCostCenter());
	 assertNull(postSearchBean.getPostSiloStatus());
	 assertNull(postSearchBean.getPostFinancialLocCode());
	 assertNull(postSearchBean.getPostManagerUserId());
	 assertNull(postSearchBean.getPostHrid());
	 assertNull(postSearchBean.getPostCompany());
	 assertNull(postSearchBean.getFirstName());
	 assertNull(postSearchBean.getLastName());

	 assertNull(postSearchBean.getOrgUserId());
	 assertNull(postSearchBean.getEmail());
	 assertNull(postSearchBean.getFirstNameOrig());
	 assertNull(postSearchBean.getLastNameOrig());
	 assertNull(postSearchBean.getHridOrig());
	 assertNull(postSearchBean.getOrgUserIdOrig());
	 assertNull(postSearchBean.getOrgCodeOrig());
	 assertNull(postSearchBean.getEmailOrig());
	 assertNull(postSearchBean.getOrgManagerUserIdOrig());
 }
	 
	 @Test
	 public void isCriteriaUpdatedIfUserNotNullTest()
	 {
		 assertFalse(mockPostSearchBean.isCriteriaUpdated());
	 }
	 
	 @Test
	 public void isCriteriaUpdatedIfUserNullTest()
	 {
		 mockPostSearchBean.setUser(null);
		 assertTrue(mockPostSearchBean.isCriteriaUpdated());
	 }
	 
	 
	 @Test
	 public void isCriteriaUpdatedIfUserTest()
	 {
		 mockPostSearchBean.setUser(null);
		 mockPostSearchBean.setUserOrig(null);
		 assertFalse(mockPostSearchBean.isCriteriaUpdated());
	 }
	 
	 @Test
	 public void isCriteriaUpdatedIfUserOrgigTest()
	 {
		// mockPostSearchBean.setUser(null);
		 mockPostSearchBean.setUserOrig(null);
		 assertTrue(mockPostSearchBean.isCriteriaUpdated());
	 }
	 
	 @Test
	 public void isCriteriaUpdatedIfUserTest1()
	 {
		 assertFalse(mockPostSearchBean.isCriteriaUpdated());
	 }
 
}
