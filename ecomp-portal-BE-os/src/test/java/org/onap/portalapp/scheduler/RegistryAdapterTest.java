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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.onap.portalsdk.core.scheduler.Registerable;
import org.onap.portalsdk.workflow.services.WorkflowScheduleService;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

public class RegistryAdapterTest {
	
	RegistryAdapter ra=new RegistryAdapter();
	@Mock
	Registerable registry;
	@Mock
	WorkflowScheduleService workflowScheduleService;
	SchedulerFactoryBean _schedulerBean=new SchedulerFactoryBean();
	@Before
	public void before(){
		ra.setRegistry(registry);
		ra.setSchedulerBean(_schedulerBean);
		ra.setWorkflowScheduleService(workflowScheduleService);
	}
	@Test
	public void testSchedulerBean(){
		//negative test
		assertNotEquals(ra.getSchedulerBean(), registry);
		//positive test
		assertEquals(ra.getSchedulerBean(), _schedulerBean);
	}
	@Test
	public void testRegistry(){
		//negative test
		assertNotEquals(ra.getRegistry(), _schedulerBean);
		//positive test
		assertEquals(ra.getRegistry(), registry);
	}
	@Test
	public void testWorkflowScheduleService(){
		//negative test
		assertNotEquals(ra.getWorkflowScheduleService(), _schedulerBean);
		//positive test
		assertEquals(ra.getWorkflowScheduleService(),workflowScheduleService);
	}
}
