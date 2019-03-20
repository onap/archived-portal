/*
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright © 2018 IBM.
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
package org.onap.portalapp.portal.utils;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;

public class MusicCookieCsrfTokenRepositoryTest {
    
    MusicCookieCsrfTokenRepository repo;
    
    CookieCsrfTokenRepository cookieRepo;
    
    @Before
    public void setup() {
        cookieRepo = new CookieCsrfTokenRepository();
        repo = new MusicCookieCsrfTokenRepository(cookieRepo);
    }

    @Test
    public void  generateTokenTest(){
        HttpServletRequest requestMock = Mockito.mock(HttpServletRequest.class);
        CsrfToken expected = cookieRepo.generateToken(requestMock);
        
        CsrfToken actual = repo.generateToken(requestMock);
        
        Assert.assertEquals(expected.getHeaderName(), actual.getHeaderName());
        Assert.assertEquals(expected.getParameterName(), actual.getParameterName());
    }
}
