/*
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2018 AT&T Intellectual Property. All rights reserved.
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

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.FilterReply;

public class CustomLoggingFilterTest {

	@Test
	public void  decideTest(){
		ILoggingEvent event = Mockito.mock(ILoggingEvent.class);
		Mockito.when(event.getLevel()).thenReturn(Level.ERROR);
		Mockito.when(event.getThreadName()).thenReturn("UEBConsumerThread");
		Mockito.when(event.getLoggerName()).thenReturn("org.onap.nsa");
		CustomLoggingFilter customLoggingFilter =  new CustomLoggingFilter();
		 FilterReply reply = customLoggingFilter.decide(event);
		 Assert.assertEquals( FilterReply.DENY, reply);
	}
	
	@Test
	public void  decideNEUTRALTest(){
		ILoggingEvent event = Mockito.mock(ILoggingEvent.class);
		Mockito.when(event.getLevel()).thenReturn(Level.ERROR);
		Mockito.when(event.getThreadName()).thenReturn("UEBConsumerThread");
		Mockito.when(event.getLoggerName()).thenReturn("test");
		CustomLoggingFilter customLoggingFilter =  new CustomLoggingFilter();
		 FilterReply reply = customLoggingFilter.decide(event);
		 Assert.assertEquals( FilterReply.NEUTRAL, reply);
	}
	
	@Test
	public void  decideExceptionTest(){
		ILoggingEvent event = Mockito.mock(ILoggingEvent.class);
		Mockito.when(event.getLevel()).thenReturn(Level.ERROR);
		Mockito.when(event.getThreadName()).thenReturn("UEBConsumerThread");
		CustomLoggingFilter customLoggingFilter =  new CustomLoggingFilter();
		 FilterReply reply = customLoggingFilter.decide(event);
		 Assert.assertEquals( FilterReply.NEUTRAL, reply);
	}
}
