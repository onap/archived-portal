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
package org.onap.portalapp.music.conf;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.music.datastore.PreparedQueryObject;
import org.onap.music.main.MusicCore;
import org.onap.portalapp.music.service.MusicService;
import org.onap.portalapp.music.util.MusicUtil;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.session.Session;

import com.datastax.driver.core.ResultSet;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MusicUtil.class, MusicCore.class})
public class MusicSessionRepositoryHandlerTest {
	
	@Mock
	private MusicService musicService;
	
	@Mock
	ResultSet resultSet;
	
	@Before
	public void setUp() {
		PowerMockito.mockStatic(MusicUtil.class);
		PowerMockito.mockStatic(MusicCore.class);
		Mockito.when(MusicUtil.isCached()).thenReturn(true);
		MockitoAnnotations.initMocks(this);
	}
	
	@InjectMocks
	MusicSessionRepositoryHandler musicSessionRepositoryHandler =  new MusicSessionRepositoryHandler();
	
	MusicSession ms = new MusicSession();

	
	@SuppressWarnings("static-access")
	@Test
	public void getTest() throws Exception {
		Mockito.when(MusicCore.get(Matchers.any(PreparedQueryObject.class))).thenReturn(resultSet);
		Mockito.when(musicService.getMetaAttribute("test_id")).thenReturn(ms);
		Session session = musicSessionRepositoryHandler.get("test_id");
		assertNotNull(session);
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void getFailWithIdTest() throws Exception {
		Mockito.when(MusicCore.get(Matchers.any(PreparedQueryObject.class))).thenReturn(resultSet);
		Mockito.when((musicService).getMetaAttribute("test_id")).thenThrow(new NullPointerException());
		Session session = musicSessionRepositoryHandler.get("test_id");
		assertNull(session);
	}
}
