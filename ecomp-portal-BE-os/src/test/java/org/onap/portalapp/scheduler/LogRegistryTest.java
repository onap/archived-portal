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

import static org.junit.Assert.assertNotNull;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

public class LogRegistryTest {

	@Mock
	JobDetailFactoryBean job;
	@Mock
	LogJob lj;
	@InjectMocks
	LogRegistry lr;
	
	String groupName = "AppGroup";
	String jobName = "LogJob";
	@Before
	public void initialize(){
		MockitoAnnotations.initMocks(this);
	}
	@Test
	public void jobDetailFactoryBeantest() throws ParseException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("units", "bytes"); 
	    job.setJobClass(LogJob.class);
		job.setJobDataAsMap(map);
		job.setGroup(groupName);
		job.setName(jobName);
		assertNotNull(lr.jobDetailFactoryBean());
	}
}
