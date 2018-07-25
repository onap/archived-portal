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
package org.onap.portalapp.music.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalapp.music.conf.MusicSession;
import org.onap.portalapp.music.service.MusicService;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

@RunWith(PowerMockRunner.class)
@PrepareForTest({  MusicProperties.class, MusicSession.class, MusicService.class, MusicCleanUp.class })
public class MusicUtilTest {

	ResultSet result = Mockito.mock(ResultSet.class);

	Row rw = Mockito.mock(Row.class);
	@Mock
	ByteBuffer buffer;
	
	@Before
	public void setUp() throws Exception {
		MusicCleanUp musCleapUp = mock(MusicCleanUp.class);
		PowerMockito.mockStatic(MusicProperties.class);
		PowerMockito.mockStatic(MusicSession.class);
		PowerMockito.mockStatic(MusicService.class);
		PowerMockito.mockStatic(MusicCleanUp.class);
		Mockito.when(MusicProperties.getProperty(MusicProperties.MUSIC_ATOMIC_PUT)).thenReturn("atomic-put");
		Mockito.when(MusicProperties.getProperty(MusicProperties.MUSIC_ATOMIC_GET)).thenReturn("atomic-get");
		Mockito.when(MusicProperties.getProperty(MusicProperties.MUSIC_CACHE)).thenReturn("cache");
		Mockito.when(MusicProperties.getProperty(MusicProperties.MUSIC_EXCLUDE_API)).thenReturn("test1,test2");
		PowerMockito.when(MusicProperties.getProperty(MusicProperties.MUSIC_SERIALIZE_COMPRESS)).thenReturn("compress");
		PowerMockito.when(MusicCleanUp.getInstance()).thenReturn(musCleapUp);
		PowerMockito.when(musCleapUp.getLastCleanUpTime()).thenReturn(null);
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void isSessionMetaAttrTest() {
		assertTrue(MusicUtil.isSessionMetaAttr("CREATION_TIME"));
	}

	@Test
	public void musicRestResponseDataParsingTest() throws Exception {
		List<Row> rows = new ArrayList<Row>();
		Mockito.doReturn("creation time").when(rw).getString("CREATION_TIME");
		rows.add(rw);
		Mockito.doReturn(rows.get(0)).when(result).one();
		assertNotNull(MusicUtil.musicRestResponseDataParsing(result, "CREATION_TIME"));
	}
	
	@Test(expected=StreamCorruptedException.class)
	public void musicRestResponseDataParsingTestBytes() throws Exception {
		List<Row> rows = new ArrayList<Row>();
		//ByteBuffer byteBuffer = ByteBuffer.allocate(6);
		ByteBuffer buff = Charset.forName("UTF-8").encode("Hello, World!");
		Mockito.when(rw.getBytes("attribute_bytes")).thenReturn(buff);
		rows.add(rw);
		Mockito.doReturn(rows.get(0)).when(result).one();
		assertNotNull(MusicUtil.musicRestResponseDataParsing(result, "TEST"));
	}
	
	@Test
	public void testMusicSerialize()throws Exception {
		String data="TEST";
		MusicUtil.musicSerialize(data);
		
		
	}
	@Test
	public void testParseMetaData()throws Exception {
		
		Mockito.when(rw.getString("primary_id")).thenReturn("TestSession");
		Mockito.when(rw.getString("creation_time")).thenReturn("2018-07-03T10:15:30.00Z");
		Mockito.when(rw.getString("last_access_time")).thenReturn("2018-07-05T10:15:30.00Z");
		Mockito.when(rw.getString("max_inactive_interval")).thenReturn("PT20.345S");
		MusicSession session=MusicUtil.parseMetaData(rw);
		assertNotNull(session);
		
	}
	
	@Test
	public void testMusicSerializeMusicCompress()throws Exception {
		PowerMockito.when(MusicProperties.getProperty(MusicProperties.MUSIC_SERIALIZE_COMPRESS)).thenReturn("true");
		String data="TEST";
		MusicUtil.musicSerialize(data);
		
		
	}
	

	@Test
	public void getMusicExcludedAPITest() {
		assertNotNull(MusicUtil.getMusicExcludedAPI());
	}

	@Test
	public void isExcludedApiTest() {
		assertTrue(MusicUtil.isExcludedApi("test1"));
	}

	@Test
	public void isExcludedApiFalseTest() {
		assertFalse(MusicUtil.isExcludedApi("test3"));
	}

	@Test
	public void isMusicSerializeCompressReturnFalseTest() {
		assertFalse(MusicUtil.isMusicSerializeCompress());
	}

	@Test
	public void isAtomicPutTest() {
		assertFalse(MusicUtil.isAtomicPut());
	}

	@Test
	public void isAtomicGetTest() {
		assertFalse(MusicUtil.isAtomicGet());
	}

	@Test
	public void isCachedTest() {
		assertFalse(MusicUtil.isCached());
	}

	@Test
	public void convertHoursToMillSecTest() {
		assertNotNull(MusicUtil.convertHoursToMillSec(1));
	}

	@Test
	public void cleanUpTest() {
		assertFalse(MusicUtil.cleanUp());
	}
}
