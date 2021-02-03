/*
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright Â© 2017 AT&T Intellectual Property. All rights reserved.
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
 *.
 */

package org.onap.portalapp.music.service;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.onap.music.datastore.PreparedQueryObject;
import org.onap.music.eelf.logging.EELFLoggerDelegate;
import org.onap.music.exceptions.MusicLockingException;
import org.onap.music.exceptions.MusicQueryException;
import org.onap.music.exceptions.MusicServiceException;
import org.onap.music.main.CipherUtil;
import org.onap.music.main.MusicCore;
import org.onap.music.main.ResultType;
import org.onap.music.main.ReturnType;
import org.onap.portalapp.music.conf.MusicSession;
import org.onap.portalapp.music.model.RestResponse;
import org.onap.portalapp.music.model.RestStatusEnum;
import org.onap.portalapp.music.util.MusicProperties;
import org.onap.portalapp.music.util.MusicUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.session.Session;
import org.springframework.web.client.RestTemplate;
import org.onap.music.datastore.MusicDataStore;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MusicService {
	static RestTemplate template = new RestTemplate();
	private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MusicService.class);
	
    
    public static void init() {
    	try {
        	Properties prop = new Properties();
        	// We load encryption key from key.properties on the classpath. This key is used for decrypting cassandra password
            try(InputStream input = MusicUtil.class.getClassLoader().getResourceAsStream("key.properties")) {
                prop.load(input);
            } catch (Exception var11) {
                logger.error(EELFLoggerDelegate.errorLogger, "Unable to find properties file.");
                //throw new Exception();
            }
        	try {
        		// Load music.properties from classpath
                org.onap.music.main.MusicUtil.loadProperties();
                // decrypt encrypted password using the key we loaded before.
                String decryptedPassword = CipherUtil.decryptPKC(org.onap.music.main.MusicUtil.getCassPwd(), prop.getProperty("cipher.enc.key"));
                // set decrypted password 
                org.onap.music.main.MusicUtil.setCassPwd(decryptedPassword);
                // Here we are creating cassandra connections pool and sessions by calling MusicDataStore and passing the cassandrra hostname to that. 
                MusicCore.mDstoreHandle = new MusicDataStore(org.onap.music.main.MusicUtil.getMyCassaHost());
				// Since mDstoreHandle is already initialized in init mthod, calling this method again will have no impact on mDstoreHandle.
                MusicCore.getDSHandle();
            } catch (Exception e) {
            	logger.error(EELFLoggerDelegate.errorLogger, e.getMessage(), e);
            	logger.debug(EELFLoggerDelegate.debugLogger, e.getMessage(), e);
            }
        	
        } catch(Exception e) {
        	logger.error(EELFLoggerDelegate.errorLogger, e.getMessage(), e);
        	logger.debug(EELFLoggerDelegate.debugLogger, e.getMessage(), e);
        }
    }


	private static boolean isAtomicPut = MusicUtil.isAtomicPut();
	private static boolean isAtomicGet = MusicUtil.isAtomicGet();
	private static String musicKeySpace = MusicProperties.getProperty(MusicProperties.MUSIC_SESSION_KEYSPACE);
	private static String musicMetaTable = MusicProperties.getProperty(MusicProperties.MUSIC_SESSION_META_TABLES);
	private static String musicAttrTable = MusicProperties.getProperty(MusicProperties.MUSIC_SESSION_ATTR_TABLES);

  private static final String WITH_SESSION_ID = " with session id: ";
  private static final String RESULT = "result:";
  private static final String WHERE = " WHERE ";
  private static final String FROM = " FROM ";
  private static final String DELETE = "DELETE ";
  private static final String REMOVE_SESSION = "removeSession: ";
  private static final String SUCCESS = "success";
  public static final String ATOMIC = "atomic";
  public static final String EVENTUAL = "eventual";
  public static final String CRITICAL = "critical";

	/**
	 * Store session attribute name and values into Cassandra via Music
	 *
	 * @param attributeName
	 * @param value
	 * @param sessionId
	 * @return ReturnType that includes required body information for Music api
	 * @throws Exception
	 */
	public static ReturnType setAttribute(String attributeName, Object value, String sessionId) throws MusicLockingException {
		logger.debug(EELFLoggerDelegate.debugLogger, "setAttribute: start with id " + sessionId);
		String tableName = null;
		ReturnType result = null;
		boolean isMeta = MusicUtil.isSessionMetaAttr(attributeName);
		PreparedQueryObject queryObject = new PreparedQueryObject();
		StringBuilder querySB = new StringBuilder();
		querySB.append("INSERT INTO ").append(musicKeySpace).append(".").append(getTableName(isMeta))
				.append(getInsertQuery(isMeta, attributeName));

		queryObject.appendQueryString(querySB.toString());
		if (isMeta) {
			queryObject.addValue(sessionId);
			queryObject.addValue(String.valueOf(value));
			tableName = musicMetaTable;
		} else {
			queryObject.addValue(sessionId);
			queryObject.addValue(attributeName);
			queryObject.addValue(MusicUtil.musicSerialize(value));
			tableName = musicAttrTable;

		}
		if (isAtomicPut)
			result = MusicCore.atomicPut(musicKeySpace, tableName, sessionId, queryObject, null);
		else
			result = modEventualPut(queryObject);
		logger.debug(EELFLoggerDelegate.debugLogger, "setAttribute: attributeName: " + attributeName
				+ WITH_SESSION_ID + sessionId + RESULT + result.getMessage());
		return result;
	}

	/**
	 * Store session meta data values into Cassandra via Music
	 *
	 * @param session
	 * @return ReturnType that includes required body information for Music api
	 * @throws Exception
	 */
	public static ReturnType setMetaAttribute(Session session) throws MusicLockingException {
		logger.debug(EELFLoggerDelegate.debugLogger, "setMetaAttribute: start with session id: " + session.getId());
		ReturnType result = null;
		PreparedQueryObject queryObject = new PreparedQueryObject();
		StringBuilder querySB = new StringBuilder();
		querySB.append("INSERT INTO ").append(musicKeySpace).append(".").append(musicMetaTable).append("(")
				.append(MusicProperties.PRIMARY_ID).append(",").append(MusicProperties.CREATION_TIME).append(",")
				.append(MusicProperties.LAST_ACCESS_TIME).append(",").append(MusicProperties.MAX_INACTIVE_INTERVAL)
				.append(",").append(MusicProperties.SESSION_ID).append(") VALUES (?,?,?,?,?);");

		queryObject.appendQueryString(querySB.toString());
		queryObject.addValue(session.getId());
		queryObject.addValue(String.valueOf(session.getCreationTime()));
		queryObject.addValue(String.valueOf(session.getLastAccessedTime()));
		queryObject.addValue(String.valueOf(session.getMaxInactiveInterval()));
		queryObject.addValue(session.getId());
		if (isAtomicPut)
			result = MusicCore.atomicPut(musicKeySpace, musicMetaTable, session.getId(), queryObject, null);
		else
			result = modEventualPut(queryObject);
		logger.debug(EELFLoggerDelegate.debugLogger,
				"setMetaAttribute: with session id: " + session + RESULT + result.getMessage());

		return result;
	}

	/**
	 * Retrieve session meta data from Cassandra via Music
	 *
	 * @param sessionId
	 * @return MusicSession
	 * @throws Exception
	 */
	public static MusicSession getMetaAttribute(String sessionId) throws MusicLockingException,MusicServiceException {
		logger.debug(EELFLoggerDelegate.debugLogger, "getMetaAttribute: start with session Id: "+ sessionId);
		ResultSet result = null;
		PreparedQueryObject queryObject = new PreparedQueryObject();
		StringBuilder querySB = new StringBuilder();
		querySB.append("SELECT * FROM ").append(musicKeySpace).append(".").append(musicMetaTable).append(WHERE)
				.append(MusicProperties.PRIMARY_ID).append("=?;");
		queryObject.appendQueryString(querySB.toString());
		queryObject.addValue(sessionId);
		if (isAtomicGet)
			result = MusicCore.atomicGet(musicKeySpace, musicMetaTable, sessionId, queryObject);
		else
			result = modEventualGet(queryObject);
		logger.debug(EELFLoggerDelegate.debugLogger, "getMetaAttribute: with session id: " + sessionId);
		return MusicUtil.parseMetaData(result.one());
	}

	
	/**
	 * Get proper column names (from meta or attribute table) base on isMeta
	 *
	 * @param isMeta
	 * @param attributeName
	 * @return String
	 */
	private static String getInsertQuery(boolean isMeta, String attributeName) {
		logger.debug(EELFLoggerDelegate.debugLogger, "getInsertQuery: start inserting : " + attributeName);
		StringBuilder querySB = new StringBuilder();
		if (isMeta) {
			querySB.append(" (").append(MusicProperties.PRIMARY_ID).append(",").append(attributeName)
					.append(") VALUES (?,?);");
		} else {
			querySB.append(" (").append(MusicProperties.PRIMARY_ID).append(",").append(MusicProperties.ATTRIBUTE_NAME)
					.append(",").append(MusicProperties.ATTRIBUTE_BYTES).append(") VALUES (?,?,?);");
		}
		return querySB.toString();
	}

	/**
	 * Retrieve session attribute data from Cassandra via Music
	 *
	 * @param attributeName
	 * @param sessionId
	 * @return attribute value with T type
	 * @throws Exception
	 */
	public static <T> T getAttribute(String attributeName, String sessionId) throws Exception {
		logger.debug(EELFLoggerDelegate.debugLogger, "getAttribute: start with session id: " + sessionId);
		ResultSet result = null;
		String tableName = null;
		boolean isMeta = MusicUtil.isSessionMetaAttr(attributeName);
		PreparedQueryObject queryObject = new PreparedQueryObject();
		StringBuilder querySB = new StringBuilder();
		querySB.append("SELECT ").append(getColumn(attributeName, isMeta)).append(FROM).append(musicKeySpace)
				.append(".").append(getTableName(isMeta)).append(WHERE).append(MusicProperties.PRIMARY_ID)
				.append("= ?");

		queryObject.addValue(sessionId);
		if (!isMeta) {
			querySB.append(" and ").append(MusicProperties.ATTRIBUTE_NAME).append("= ?");
			queryObject.addValue(attributeName);
			tableName = musicAttrTable;
		} else
			tableName = musicMetaTable;

		queryObject.appendQueryString(querySB.toString());
		if (isAtomicGet)
			result = MusicCore.atomicGet(musicKeySpace, tableName, sessionId, queryObject);
		else
			result = modEventualGet(queryObject);
		return MusicUtil.musicRestResponseDataParsing(result, attributeName);

	}

	/**
	 * Remove session attribute data from Cassandra via Music
	 *
	 * @param attributeName
	 * @param sessionId
	 * @return ReturnType
	 * @throws MusicServiceException
	 * @throws MusicLockingException
	 */
	public static ReturnType removeAttribute(String attributeName, String sessionId) throws MusicLockingException {
		logger.debug(EELFLoggerDelegate.debugLogger, "removeAttribute: start with session id: " + sessionId);
		boolean isMeta = MusicUtil.isSessionMetaAttr(attributeName);
		ReturnType result = null;
		String tableName = null;
		PreparedQueryObject queryObject = new PreparedQueryObject();
		StringBuilder querySB = new StringBuilder();
		querySB.append(DELETE).append(getDelColumn(isMeta, attributeName)).append(FROM).append(musicKeySpace)
				.append(".").append(getTableName(isMeta)).append(WHERE).append(MusicProperties.PRIMARY_ID)
				.append("= ? ");
		queryObject.addValue(sessionId);

		if (!isMeta) {
			querySB.append(" and ").append(MusicProperties.ATTRIBUTE_NAME).append("= ?");
			queryObject.addValue(attributeName);
			tableName = musicAttrTable;
		} else
			tableName = musicMetaTable;
		queryObject.appendQueryString(querySB.toString());
		if (isAtomicPut)
			result = MusicCore.atomicPut(musicKeySpace, tableName, sessionId, queryObject, null);
		else
			result = modEventualPut(queryObject);
		logger.debug(EELFLoggerDelegate.debugLogger,
        REMOVE_SESSION + attributeName + WITH_SESSION_ID + sessionId + RESULT + result.getMessage());

		return result;
	}

	/**
	 * Remove entire session from Cassandra via Music
	 *
	 * @param sessionId
	 * @return ReturnType
	 * @throws MusicServiceException
	 * @throws MusicLockingException
	 */
	public static ReturnType removeSession(String sessionId) throws MusicLockingException {
		ReturnType result = null;
		boolean isAtomic = isAtomicPut;
		logger.debug(EELFLoggerDelegate.debugLogger, "removeSession: start with session id: " + sessionId);
		PreparedQueryObject queryObject = new PreparedQueryObject();
		StringBuilder querySB = new StringBuilder();
		querySB.append(DELETE).append(FROM).append(musicKeySpace).append(".").append(musicMetaTable)
				.append(WHERE).append(MusicProperties.PRIMARY_ID).append("= ? ");
		queryObject.appendQueryString(querySB.toString());
		queryObject.addValue(sessionId);
		if (isAtomic)
			result = MusicCore.atomicPut(musicKeySpace, musicMetaTable, sessionId, queryObject, null);
		else
			result = modEventualPut(queryObject);
		logger.debug(EELFLoggerDelegate.debugLogger, REMOVE_SESSION + musicMetaTable + WITH_SESSION_ID
				+ sessionId + RESULT + result.getMessage());

		queryObject = new PreparedQueryObject();
		querySB = new StringBuilder();
		querySB.append(DELETE).append(FROM).append(musicKeySpace).append(".").append(musicAttrTable)
				.append(WHERE).append(MusicProperties.PRIMARY_ID).append("= ? ");
		queryObject.appendQueryString(querySB.toString());
		queryObject.addValue(sessionId);
		if (isAtomic)
			result = MusicCore.atomicPut(musicKeySpace, musicAttrTable, sessionId, queryObject, null);
		else
			result = modEventualPut(queryObject);

		logger.debug(EELFLoggerDelegate.debugLogger, REMOVE_SESSION + musicAttrTable + WITH_SESSION_ID
				+ sessionId + RESULT + result.getMessage());

		return result;
	}

	/**
	 * Get proper table name (Meta or Attribute) base on isMeta.
	 *
	 * @param isMeta
	 * @return String
	 */
	private static String getTableName(boolean isMeta) {
		StringBuilder querySB = new StringBuilder();
		if (isMeta)
			querySB.append(musicMetaTable);
		else
			querySB.append(musicAttrTable);
		return querySB.toString();
	}

	/**
	 * Get proper column name (Meta or Attribute) base on isMeta.
	 *
	 * @param attributeName
	 * @param isMeta
	 * @return String
	 */
	private static String getColumn(String attributeName, boolean isMeta) {
		StringBuilder querySB = new StringBuilder();
		if (isMeta)
			querySB.append(attributeName);
		else
			querySB.append("attribute_bytes");
		return querySB.toString();
	}

	/**
	 * Get proper column name (Meta or Attribute) base on isMeta for removing.
	 *
	 * @param attributeName
	 * @param isMeta
	 * @return String
	 */
	private static String getDelColumn(boolean isMeta, String attributeName) {
		StringBuilder querySB = new StringBuilder();
		if (isMeta)
			querySB.append(attributeName);
		return querySB.toString();
	}

	/**
	 * To set session attributes in Music
	 *
	 * @param attributeName
	 * @param value
	 * @param session
	 * @param sessionId
	 * @param isMeta
	 * @return RestResponse<String>
	 * @throws JsonProcessingException
	 */

	public static RestResponse<String> setAttributeAPI(String attributeName, Object value, Session session,
			String sessionId, String className, boolean isMeta) throws JsonProcessingException {
		logger.debug(EELFLoggerDelegate.debugLogger, "setAttribute: " + attributeName);
		RestResponse<String> portalRestResponse = null;
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(
				getMusicRestBody(attributeName, value, sessionId, session, className, isMeta), getMusicHeader());
		String url = getInsertUrl(isMeta);
		ResponseEntity<String> response = null;
		try {
			response = template.exchange(url, HttpMethod.POST, entity, String.class);
			portalRestResponse = new RestResponse<>(RestStatusEnum.OK, SUCCESS, response.getBody());
		} catch (Exception e) {
			logger.debug(EELFLoggerDelegate.debugLogger, e.getLocalizedMessage());
			portalRestResponse = new RestResponse<>(RestStatusEnum.ERROR, e.getMessage(), null);
		}
		return portalRestResponse;
	}

	/**
	 * To get session attribute in Music
	 *
	 * @param attributeName
	 * @param value
	 * @param sessionId
	 * @param isMeta
	 * @return RestResponse<String>
	 */

	public static RestResponse<String> getAttributeAPI(String attributeName, Object value, String sessionId,
			boolean isMeta) {
		logger.debug(EELFLoggerDelegate.debugLogger, "setAttribute: " + attributeName);
		RestResponse<String> portalRestResponse = null;
		HttpEntity<String> entity = new HttpEntity<>(null, getMusicHeader());
		ResponseEntity<String> response = null;
		String url = getSelectSessionIdUrl(attributeName, sessionId, isMeta);
		try {
			response = template.exchange(url, HttpMethod.GET, entity, String.class);
			portalRestResponse = new RestResponse<>(RestStatusEnum.OK, SUCCESS, response.getBody());
		} catch (Exception e) {
			logger.debug(EELFLoggerDelegate.debugLogger, e.getLocalizedMessage());
			portalRestResponse = new RestResponse<>(RestStatusEnum.ERROR, e.getMessage(), null);
		}
		return portalRestResponse;
	}

	/**
	 * To remove session attribute or session meta in Music
	 *
	 * @param attributeName
	 * @param sessionId
	 * @param isMeta
	 * @return RestResponse<String>
	 */
	public static RestResponse<String> removeAttributeAPI(String attributeName, String sessionId, boolean isMeta) {
		RestResponse<String> portalRestResponse = null;
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(getMusicDelRestBody(null), getMusicHeader());
		ResponseEntity<String> response = null;
		String url = getSelectSessionIdUrl(attributeName, sessionId, true);
		try {
			url = getSelectSessionIdUrl(attributeName, sessionId, false);
			response = template.exchange(url, HttpMethod.DELETE, entity, String.class);
			portalRestResponse = new RestResponse<>(RestStatusEnum.OK, SUCCESS, response.getBody());
		} catch (Exception e) {
			logger.debug(EELFLoggerDelegate.debugLogger, e.getLocalizedMessage());
			portalRestResponse = new RestResponse<>(RestStatusEnum.ERROR, e.getMessage(), null);
		}
		return portalRestResponse;
	}

	/**
	 * Generate body for Music api calls
	 *
	 * @return String that includes required body information for Music api
	 *         calls
	 * @throws JsonProcessingException
	 */
	public static Map<String, Object> getMusicRestBody(String attributeName, Object value, String sessionId,
			Session session, String className, boolean isMeta) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<>();
		/* Set up column values */
		Map<String, Object> valueMap = new HashMap<>();
		if (isMeta) {
			valueMap.put(MusicProperties.PRIMARY_ID, session.getId());
			valueMap.put(MusicProperties.SESSION_ID, session.getId());
			valueMap.put(MusicProperties.CREATION_TIME, session.getCreationTime().toString());
			valueMap.put(MusicProperties.LAST_ACCESS_TIME, session.getLastAccessedTime().toString());
			valueMap.put(MusicProperties.MAX_INACTIVE_INTERVAL, session.getMaxInactiveInterval().toString());
		} else {
			ObjectMapper mapper = new ObjectMapper();
			valueMap.put(MusicProperties.PRIMARY_ID, sessionId);
			valueMap.put(MusicProperties.ATTRIBUTE_NAME, attributeName);
			valueMap.put(MusicProperties.ATTRIBUTE_BYTES, mapper.writeValueAsString(value));
			valueMap.put(MusicProperties.ATTRIBUTE_CLASS, className);
		}
		map.put("values", valueMap);
		/* Set up consistency setting */
		Map<String, String> consistencyInfoMap = new HashMap<>();
		consistencyInfoMap.put(MusicProperties.getProperty(MusicProperties.MUSIC_CONSISTENCYINFO),
				MusicProperties.getProperty(MusicProperties.MUSIC_CONSISTENCYINFO_VALUE));
		map.put("consistencyInfo", consistencyInfoMap);
		return map;
	}

	/**
	 * Generate body for Music delete api calls
	 *
	 * @return String that includes required body information for Music api
	 *         calls
	 * @throws JsonProcessingException
	 */
	public static Map<String, Object> getMusicDelRestBody(String attributeName) {
		Map<String, Object> map = new HashMap<>();
		Map<String, String> consistencyInfoMap = new HashMap<>();
		consistencyInfoMap.put(MusicProperties.getProperty(MusicProperties.MUSIC_CONSISTENCYINFO),
				MusicProperties.getProperty(MusicProperties.MUSIC_CONSISTENCYINFO_VALUE));
		if (attributeName != null && !attributeName.isEmpty()) {
			Map<String, String> conditionsMap = new HashMap<>();
			conditionsMap.put("attribute_name", attributeName);
			map.put("conditions", conditionsMap);
		}
		map.put("consistencyInfo", consistencyInfoMap);
		return map;
	}

	private static String getSelectSessionIdUrl(String attributeName, String sessionId, boolean isMeta) {
		String path = constructPath(isMeta);
		StringBuilder attriPath = new StringBuilder();
		attriPath.append(path).append(MusicProperties.getProperty(MusicProperties.MUSIC_ROWS)).append("?")
				.append(MusicProperties.PRIMARY_ID).append("=").append(sessionId);
		return attriPath.toString();
	}

	private static String getInsertUrl(boolean isMeta) {
		String path = constructPath(isMeta);
		StringBuilder attriPath = new StringBuilder();
		attriPath.append(path);
		attriPath.append(MusicProperties.getProperty(MusicProperties.MUSIC_ROWS));
		return attriPath.toString();
	}

	/**
	 * Generate header for Music api calls
	 *
	 * @return header that contains required header information for Music api
	 *         calls
	 */
	private static HttpHeaders getMusicHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("X-minorVersion", MusicProperties.getProperty(MusicProperties.MUSIC_X_MINOR_VERSION));
		headers.add("X-patchVersion", MusicProperties.getProperty(MusicProperties.MUSIC_X_PATCH_VERSION));
		headers.add("ns", MusicProperties.getProperty(MusicProperties.MUSIC_NS));
		headers.add("userId", MusicProperties.getProperty(MusicProperties.MUSIC_USER_ID));
		headers.add("password", MusicProperties.getProperty(MusicProperties.MUSIC_PASSWORD));
		return headers;
	}

	/**
	 * Construct URL for Music api calls
	 *
	 * @return path
	 */
	private static String constructPath(boolean isMeta) {
		StringBuilder path = new StringBuilder();
		path.append(MusicProperties.getProperty(MusicProperties.MUSIC_ENDPOINT))
				.append(MusicProperties.getProperty(MusicProperties.MUSIC_VERSION)).append("/")
				.append(MusicProperties.getProperty(MusicProperties.MUSIC_KEYSPACE)).append("/").append(musicKeySpace)
				.append("/").append(MusicProperties.getProperty(MusicProperties.MUSIC_TABLES)).append("/");
		if (isMeta)
			path.append(musicMetaTable);
		else
			path.append(musicAttrTable);
		path.append("/");
		return path.toString();
	}

	/**
	 * Get a list of sessions that need to be cleaned up
	 *
	 * @return List<String>
	 */
	private static List<String> getSessionToBeDeleted(){
		logger.debug(EELFLoggerDelegate.debugLogger, "initial getSessionToBeDeleted ...");

		PreparedQueryObject queryObject = new PreparedQueryObject();
		ResultSet result = null;
		List<String> sessionIDList = new ArrayList<>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		StringBuilder querySB = new StringBuilder();
		String cleanUpThreshold = MusicProperties.getProperty(MusicProperties.MUSIC_CLEAN_UP_THRESHOLD); //Clean up sessions that's cleanUpThreshold hours ago
		Date dateForCleanup = new Date(System.currentTimeMillis() - 3600 * 1000 * Integer.valueOf(cleanUpThreshold)); // Get the threshold date that needs to be clean up
		String dateForCleanupCondition = dateFormat.format(dateForCleanup);
		querySB.append("SELECT ").append(MusicProperties.PRIMARY_ID).append(FROM).append(musicKeySpace)
		.append(".").append(getTableName(true)).append(WHERE).append(MusicProperties.LAST_ACCESS_TIME)
		.append("< ? ").append(" ALLOW FILTERING");
		queryObject.appendQueryString(querySB.toString());
		queryObject.addValue(dateForCleanupCondition);

		try{
			if (isAtomicGet)
				result = MusicCore.atomicGet(musicKeySpace, musicMetaTable, null, queryObject);
			else
				result = modEventualGet(queryObject);
			Row row = result.one();
			while(row!=null){
				sessionIDList.add(row.get(MusicProperties.PRIMARY_ID, String.class));
				row = result.one();
			}
		}catch(Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, "Error while cleaning up music tables" , e);
		}
		return sessionIDList;
	}

	/**
	 * Remove session data in music base on the defined frequency
	 *
	 * @return List<String>
	 */
	public static void cleanUpMusic() {
		boolean timeToCleanUp = MusicUtil.cleanUp(); // Decide whether to clean up or not

		if(timeToCleanUp){
			/**Getting a list of sessions that need to be cleaned up*/
			List<String> sessionIDList = getSessionToBeDeleted();
			if(sessionIDList!=null || !sessionIDList.isEmpty()){
				StringBuilder sessionIDListCondition = new StringBuilder();
				sessionIDListCondition.append("('");
				for(String s : sessionIDList){
					sessionIDListCondition.append(s);
					sessionIDListCondition.append("','");
				}
				sessionIDListCondition.deleteCharAt(sessionIDListCondition.length()-1);
				sessionIDListCondition.deleteCharAt(sessionIDListCondition.length()-1);
				sessionIDListCondition.append(")");
				StringBuilder querySB = new StringBuilder();
				PreparedQueryObject queryObject = new PreparedQueryObject();
				/**Deleting attributes table**/
				querySB = new StringBuilder();
				queryObject = new PreparedQueryObject();
				querySB.append("DELETE FROM ").append(musicKeySpace)
				.append(".").append(getTableName(false)).append(WHERE).append(MusicProperties.PRIMARY_ID)
				.append(" in ").append(sessionIDListCondition);
				queryObject.appendQueryString(querySB.toString());
				try{
					if (isAtomicPut)
						MusicCore.atomicPut(musicKeySpace, null, null, queryObject, null);
					else
						modEventualPut(queryObject);
				}catch(Exception e){
					logger.error(EELFLoggerDelegate.errorLogger, "Error while cleaning up music attributes tables" , e);
				}
				logger.debug(EELFLoggerDelegate.debugLogger, "Music sessions have been cleaned up !");

				/**Deleting meta table**/
				logger.debug(EELFLoggerDelegate.debugLogger, "Cleaning up meta table ...");
				querySB = new StringBuilder();
				queryObject = new PreparedQueryObject();
				querySB.append("DELETE FROM ").append(musicKeySpace)
				.append(".").append(getTableName(true)).append(WHERE).append(MusicProperties.PRIMARY_ID)
				.append(" in ").append(sessionIDListCondition);
				queryObject.appendQueryString(querySB.toString());
				try{
					if (isAtomicPut)
						MusicCore.atomicPut(musicKeySpace, null, null, queryObject, null);
					else
						modEventualPut(queryObject);
				}catch(Exception e){
					logger.error(EELFLoggerDelegate.errorLogger, "Error while cleaning up music meta tables" , e);
				}

				logger.debug(EELFLoggerDelegate.debugLogger, "Cleaned up attributes table ... ");
			}else{
				logger.debug(EELFLoggerDelegate.debugLogger, "No Session needs to be cleaned up");
			}

		}
	}

	public static ReturnType modEventualPut(PreparedQueryObject queryObject) {
		 boolean result = false;
	        try {
	            result = MusicCore.getDSHandle().executePut(queryObject,CRITICAL);
	        } catch (MusicServiceException | MusicQueryException ex) {
	        	 logger.error(EELFLoggerDelegate.errorLogger,ex.getMessage() + "  " + ex.getCause() + " " + ex);
	            return new ReturnType(ResultType.FAILURE, ex.getMessage());
	        }
	        if (result) {
	            return new ReturnType(ResultType.SUCCESS, "Success");
	        } else {
	            return new ReturnType(ResultType.FAILURE, "Failure");
	        }
	}
	
	public static ResultSet modEventualGet(PreparedQueryObject queryObject) throws MusicServiceException {
		ResultSet result;
		result = MusicCore.quorumGet(queryObject);
		return result;
	}


}
