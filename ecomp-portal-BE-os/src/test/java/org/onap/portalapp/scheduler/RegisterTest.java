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
package org.onap.portalapp.scheduler;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.quartz.Trigger;

public class RegisterTest {

	Register reg=new Register();
	List<Trigger> scheduleTriggers = new ArrayList<Trigger>();
	
	@Test
	public void test() throws Exception {
		reg.setScheduleTriggers(scheduleTriggers);
		//positive test
		assertEquals(reg.getScheduleTriggers(), scheduleTriggers);
		//negative test
		assertNotEquals(reg.getScheduleTriggers(), reg);
	}
}
