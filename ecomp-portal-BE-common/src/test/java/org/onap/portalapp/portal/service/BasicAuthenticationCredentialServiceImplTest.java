

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
 */
package org.onap.portalapp.portal.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.ProjectionList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.domain.BasicAuthCredentials;
import org.onap.portalsdk.core.onboarding.exception.CipherUtilException;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.onap.portalsdk.core.service.DataAccessService;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CipherUtil.class})

public class BasicAuthenticationCredentialServiceImplTest {

	@Mock
	private DataAccessService dataAccessService;

	@InjectMocks
	private BasicAuthenticationCredentialServiceImpl serviceImpl = new BasicAuthenticationCredentialServiceImpl();

	@Before
	public void setup() throws CipherUtilException {
		MockitoAnnotations.initMocks(this);
		
	}

	@Test
	public void unt_getBasicAuthCredentialByUsernameAndPassword() throws CipherUtilException {
		PowerMockito.mockStatic(CipherUtil.class);
		PowerMockito.when(CipherUtil.decryptPKC(Matchers.anyString())).thenReturn("password1234");
		List credList=new ArrayList<>();
		BasicAuthCredentials basicAuthCredentials=new BasicAuthCredentials();
		basicAuthCredentials.setPassword("password");
		//basicAuthCredentials.setId(123L);
		credList.add(basicAuthCredentials);
		Mockito.when(dataAccessService.getList(Matchers.any(),(ProjectionList)Matchers.isNull() , Matchers.anyListOf(Criterion.class),(List)Matchers.isNull())).thenReturn(credList);
		BasicAuthCredentials credentials = serviceImpl.getBasicAuthCredentialByUsernameAndPassword("abc1234",
				"password1234");
		Assert.assertNotNull(credentials);
		
	}
	
	@Test
	public void unt_getBasicAuthCredentialByUsernameAndPassword_withException() {
		List credList=new ArrayList<>();
		BasicAuthCredentials basicAuthCredentials=new BasicAuthCredentials();
		basicAuthCredentials.setPassword("password");
		credList.add(basicAuthCredentials);
		Mockito.when(dataAccessService.getList(Matchers.any(),(ProjectionList)Matchers.isNull() , Matchers.anyListOf(Criterion.class),(List)Matchers.isNull())).thenReturn(credList);
		BasicAuthCredentials credentials = serviceImpl.getBasicAuthCredentialByUsernameAndPassword("abc1234",
				"password1234");
		Assert.assertNull(credentials);
		
	}

}


