/*
* ============LICENSE_START=======================================================
* ONAP : Portal
* ================================================================================
* Copyright 2018 TechMahindra
*=================================================================================
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
* ============LICENSE_END=========================================================
*/

package org.onap.portalapp.portal.transport;

import static org.junit.Assert.*;

import org.junit.Test;

public class OnboardingAppTest {
	
	OnboardingApp oba = new OnboardingApp();
	OnboardingApp oba2 = new OnboardingApp();
	@Test
	public void test() {
		oba.setUebKey("key");
		oba.setUebSecret("secret");
		oba.setUebTopicName("topicName");
		oba.setRestrictedApp(false);
		oba2.normalize();
		oba.name="test";
		oba.appPassword="pass";
		oba.username="username";
		oba.normalize();
		assertEquals("test", oba.name);
		assertEquals("key", oba.uebKey);
		assertEquals("secret", oba.uebSecret);
		assertEquals(false, oba.restrictedApp);
		
	}

}
