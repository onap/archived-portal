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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.onap.music.eelf.logging.EELFLoggerDelegate;
import org.onap.portalapp.music.conf.MusicSession;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

public class MusicUtil {
	private static final Set<String> sessionAttrNameSet = new HashSet<>(Arrays.asList("CREATION_TIME", "LAST_ACCESS_TIME","MAX_INACTIVE_INTERVAL","EXPIRY_TIME","PRINCIPAL_NAME"));

	private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MusicUtil.class);
	private static String atomicPut = MusicProperties.getProperty(MusicProperties.MUSIC_ATOMIC_PUT);
	private static String atomicGet = MusicProperties.getProperty(MusicProperties.MUSIC_ATOMIC_GET);
	private static String cached = MusicProperties.getProperty(MusicProperties.MUSIC_CACHE);
	private static String cleanUpFreq = MusicProperties.getProperty(MusicProperties.MUSIC_CLEAN_UP_FREQUENCY);
	private static String musicSerializeCompress = MusicProperties.getProperty(MusicProperties.MUSIC_SERIALIZE_COMPRESS);
	private static String musicEnable = MusicProperties.getProperty(MusicProperties.MUSIC_ENABLE);
	private static final int MILLIS_IN_HOUR = 3600000; 

	public static boolean isSessionMetaAttr(String key){
		return sessionAttrNameSet.contains(key);
	}

	public static <T> T musicRestResponseDataParsing(ResultSet rs, String attributeName) throws Exception{
		logger.debug(EELFLoggerDelegate.debugLogger, "musicRestResponseDataParsing: start");
		Row row = rs.one();
		if(!sessionAttrNameSet.contains(attributeName)){
			if(row!=null)
				return MusicUtil.musicDeserialize(row.getBytes("attribute_bytes"));
		}else{
			return (T) row.getString(attributeName);
		}
		return null;
	}

	public static <T> T musicDeserialize (ByteBuffer byteBuf) throws Exception{
		logger.debug(EELFLoggerDelegate.debugLogger, "musicDeserialize: start");
		ByteArrayInputStream byteArr = new ByteArrayInputStream(byteBuf.array());
		ObjectInputStream ois  = null;
		if(isMusicSerializeCompress()){
			GZIPInputStream zos = new GZIPInputStream(byteArr);
			ois = new ObjectInputStream(zos);
		}else{
			ois = new ObjectInputStream(byteArr);		
		}
		return (T) ois.readObject();
	}

	public static ByteBuffer musicSerialize (Object value) throws Exception{
		logger.debug(EELFLoggerDelegate.debugLogger, "musicSerialize: start");
		ByteArrayOutputStream bo = new ByteArrayOutputStream();			
		try {	
			if(isMusicSerializeCompress()){
				GZIPOutputStream zos = new GZIPOutputStream(bo);
				ObjectOutputStream oos = new ObjectOutputStream(zos);
				oos.writeObject(value);
				oos.flush();
				zos.finish();
			}else{		
				ObjectOutputStream oos = new ObjectOutputStream(bo);
				oos.writeObject(value);
				oos.flush();
			}
		} catch (IOException e) {
			logger.error(EELFLoggerDelegate.errorLogger, "Failed to serialize ");
		}
		return ByteBuffer.wrap(bo.toByteArray());
	}

	public static MusicSession parseMetaData (Row row) throws Exception{
		logger.debug(EELFLoggerDelegate.debugLogger, "parseMetaData: start");

		if(row==null)
			return null;
		String sessionId = row.getString("primary_id");
		MusicSession musicSession = new MusicSession(sessionId);
		musicSession.setCreationTime(Instant.parse(row.getString("creation_time")));
		musicSession.setLastAccessedTime(Instant.parse(row.getString("last_access_time")));
		musicSession.setMaxInactiveInterval(Duration.parse(row.getString("max_inactive_interval")));
		return musicSession;
	}
	
	public static Set<String> getMusicExcludedAPI(){
		Set<String> excludedApiSet = new HashSet<>();
		String musicExcludedApi = MusicProperties.getProperty(MusicProperties.MUSIC_EXCLUDE_API);
		String[] musicExcludedApiArray = musicExcludedApi.split(",");
		if(musicExcludedApiArray.length>0){
			for(String str : musicExcludedApiArray){
				excludedApiSet.add(str.trim());
			}
		}
		return excludedApiSet;
	}
	
	public static boolean isExcludedApi(String api){
		Set<String> excludedApiSet = getMusicExcludedAPI();
		for(String str: excludedApiSet){
			if(api.matches(str))
				return true;
		}
		return false;
	}

	
	public static boolean isMusicSerializeCompress(){
		if(musicSerializeCompress==null){
			logger.error(EELFLoggerDelegate.errorLogger, "Failed to read property file " + MusicProperties.MUSIC_SERIALIZE_COMPRESS +" fall back to eventual put");
			return false;
		}
		return musicSerializeCompress.trim().equalsIgnoreCase("true");
	}

	public static boolean isAtomicPut(){
		if(atomicPut==null){
			logger.error(EELFLoggerDelegate.errorLogger, "Failed to read property file " + MusicProperties.MUSIC_ATOMIC_PUT +" fall back to eventual put");
			return false;
		}
		return atomicPut.trim().equalsIgnoreCase("true");
	}

	public static boolean isAtomicGet(){
		if(atomicGet==null){
			logger.error(EELFLoggerDelegate.errorLogger, "Failed to read property file " + MusicProperties.MUSIC_ATOMIC_GET +" fall back to eventual get");
			return false;
		}
		return atomicGet.trim().equalsIgnoreCase("true");
	}

	public static boolean isCached(){
		if(cached==null){
			logger.error(EELFLoggerDelegate.errorLogger, "Failed to read property file " + MusicProperties.MUSIC_CACHE +" fall back to non cache");
			return false;
		}
		return cached.trim().equalsIgnoreCase("true");
	}
	
	public static int convertHoursToMillSec(int hour){
		return hour* MILLIS_IN_HOUR;
	}
	
	public static boolean cleanUp(){
		Date lastCleanUpDate = MusicCleanUp.getInstance().getLastCleanUpTime();
		if(lastCleanUpDate==null)
			return false;
		else{
			int cleanUpDurationMili = convertHoursToMillSec(Integer.valueOf(cleanUpFreq));
			Date currentTime = new Date();
			long diffInMillies = Math.abs(currentTime.getTime() - lastCleanUpDate.getTime());
			if(diffInMillies > cleanUpDurationMili){
				MusicCleanUp.getInstance().updateLastCleanUpTimeToCurrent();
				return true;
			}
			else 
				return false;
		}
	}
	
	public static boolean isMusicEnable(){
		if(musicEnable==null)
			return false;
		if(musicEnable.equals("true"))
			return true;
		else
			return false;
	}
}
