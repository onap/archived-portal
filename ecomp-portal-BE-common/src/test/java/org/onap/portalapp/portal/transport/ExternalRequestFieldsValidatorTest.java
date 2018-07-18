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
package org.onap.portalapp.portal.transport;

import static org.junit.Assert.*;

import java.util.Date;

import javax.validation.constraints.AssertFalse;

import org.junit.Test;

public class ExternalRequestFieldsValidatorTest {

	
	ExternalRequestFieldsValidator externalRequestFieldsValidator= new ExternalRequestFieldsValidator(false,"test");
	
	@Test
	public void unt_isResultTest(){
		Boolean defaultValue=false;
		externalRequestFieldsValidator.setResult(false);
		assertEquals(defaultValue, externalRequestFieldsValidator.isResult());
	}
	
	@Test
	public void unt_detailMessageTest(){
		String defaultValue="test";
		externalRequestFieldsValidator.setDetailMessage("test");
		assertEquals(defaultValue, externalRequestFieldsValidator.getDetailMessage());
	}
	
	@Test
	public void unt_hashCodeTest(){
		ExternalRequestFieldsValidator externalRequestFieldsValidator= new ExternalRequestFieldsValidator(false,"test");
		ExternalRequestFieldsValidator externalRequestFieldsValidator1= new ExternalRequestFieldsValidator(false,"test");
		assertEquals(externalRequestFieldsValidator.hashCode(), externalRequestFieldsValidator1.hashCode());
		assertTrue(externalRequestFieldsValidator.equals(externalRequestFieldsValidator1));
		
	}
	
	@Test
	public void unt_hashCodeWithNullTest(){
		ExternalRequestFieldsValidator externalRequestFieldsValidator= new ExternalRequestFieldsValidator(false,null);
		ExternalRequestFieldsValidator externalRequestFieldsValidator1= new ExternalRequestFieldsValidator(false,null);
		assertEquals(externalRequestFieldsValidator.hashCode(), externalRequestFieldsValidator1.hashCode());
		assertTrue(externalRequestFieldsValidator.equals(externalRequestFieldsValidator1));
		
	}
	
	@Test
	public void unt_hashCodeWithNullTest1(){
		ExternalRequestFieldsValidator externalRequestFieldsValidator= new ExternalRequestFieldsValidator(false,null);
		ExternalRequestFieldsValidator externalRequestFieldsValidator1= new ExternalRequestFieldsValidator(false,null);
		assertEquals(externalRequestFieldsValidator.hashCode(), externalRequestFieldsValidator1.hashCode());
		assertFalse(externalRequestFieldsValidator.equals(null));
		
	}
	
}
