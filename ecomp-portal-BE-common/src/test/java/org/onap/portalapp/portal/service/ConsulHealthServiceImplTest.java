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
package org.onap.portalapp.portal.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.model.ConsulResponse;
import com.orbitz.consul.model.health.ServiceHealth;
import com.orbitz.consul.Consul.Builder;
import com.orbitz.consul.model.health.Node;
import com.orbitz.consul.model.health.Service;
@RunWith(PowerMockRunner.class)
@PrepareForTest({ Consul.class ,EcompPortalUtils.class})
public class ConsulHealthServiceImplTest {
	
	private static final String TEST="test";
	@InjectMocks
	ConsulHealthServiceImpl consulHealthServiceImpl;
	
	@Mock
	Builder builder;
	@Mock
	Consul consul ;
	@Mock
	HealthClient healthClient;
	@Mock
	ServiceHealth serviceHealth;
	@Mock
	ConsulResponse<List<ServiceHealth>> response;
	@Mock
	Node node;
	@Mock
	Service service;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void getServiceLocation_Error() {
		
		PowerMockito.mockStatic(Consul.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.when(Consul.builder()).thenReturn(builder);
		PowerMockito.when(EcompPortalUtils.localOrDockerHost()).thenReturn(TEST);
		when(builder.build()).thenReturn(consul);
		when(consul.healthClient()).thenReturn(healthClient);
	String location=	consulHealthServiceImpl.getServiceLocation(TEST, TEST);
	assertNotNull(location);
		
	}
	
	@Test
	public void getServiceLocation_Empty() {
		
		List<ServiceHealth> nodes=new ArrayList<>();
		nodes.add(serviceHealth);
	
		PowerMockito.mockStatic(Consul.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.when(Consul.builder()).thenReturn(builder);
		PowerMockito.when(EcompPortalUtils.localOrDockerHost()).thenReturn(TEST);
		when(builder.build()).thenReturn(consul);
		when(consul.healthClient()).thenReturn(healthClient);
		when( healthClient.getHealthyServiceInstances(TEST)).thenReturn(null);
	
		
	String location=	consulHealthServiceImpl.getServiceLocation(TEST, TEST);
	assertNotNull(location);
		
	}
	
	
	@Test
	public void getServiceLocation() {
		
		List<ServiceHealth> nodes=new ArrayList<>();
		nodes.add(serviceHealth);
	
		PowerMockito.mockStatic(Consul.class);
		PowerMockito.mockStatic(EcompPortalUtils.class);
		PowerMockito.when(Consul.builder()).thenReturn(builder);
		PowerMockito.when(EcompPortalUtils.localOrDockerHost()).thenReturn(TEST);
		when(builder.build()).thenReturn(consul);
		when(consul.healthClient()).thenReturn(healthClient);
		when( healthClient.getHealthyServiceInstances(TEST)).thenReturn(response);
		when(response.getResponse()).thenReturn(nodes);
		when(serviceHealth.getNode()).thenReturn(node);
		when(serviceHealth.getService()).thenReturn(service);
		
	String location=	consulHealthServiceImpl.getServiceLocation(TEST, TEST);
	assertNotNull(location);
		
	}
	
	@Test
	public void getAllHealthyNodes() {
		List<ServiceHealth> nodes=new ArrayList<>();
		nodes.add(serviceHealth);
	
		PowerMockito.mockStatic(Consul.class);
		PowerMockito.when(Consul.builder()).thenReturn(builder);
		
		when(builder.build()).thenReturn(consul);
		when(consul.healthClient()).thenReturn(healthClient);
		when( healthClient.getHealthyServiceInstances(TEST)).thenReturn(response);
		when(response.getResponse()).thenReturn(nodes);
		List<ServiceHealth> list=	consulHealthServiceImpl.getAllHealthyNodes(TEST);
		assertEquals(1, list.size());
		
	}
	
	@Test
	public void getAllNodes() {
		List<ServiceHealth> nodes=new ArrayList<>();
		nodes.add(serviceHealth);
	
		PowerMockito.mockStatic(Consul.class);
		PowerMockito.when(Consul.builder()).thenReturn(builder);
		
		when(builder.build()).thenReturn(consul);
		when(consul.healthClient()).thenReturn(healthClient);
		when( healthClient.getAllServiceInstances(TEST)).thenReturn(response);
		when(response.getResponse()).thenReturn(nodes);
		List<ServiceHealth> list=	consulHealthServiceImpl.getAllNodes(TEST);
		assertEquals(1, list.size());
	}

}
