/*-
 * ================================================================================
 * eCOMP Portal
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ================================================================================
 */
package org.openecomp.portalapp.portal.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openecomp.portalapp.controller.EPRestrictedRESTfulBaseController;
import org.openecomp.portalapp.portal.domain.SharedContext;
import org.openecomp.portalapp.portal.logging.aop.EPAuditLog;
import org.openecomp.portalapp.portal.service.SharedContextService;
import org.openecomp.portalapp.portal.utils.EPSystemProperties;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The shared-context feature allows onboarded applications to share data among
 * themselves easily for a given session. It basically implements a Java map:
 * put or get a key-value pair within a map identified by a session ID.
 * 
 * This REST endpoint listens on the Portal app server and answers requests made
 * by back-end application servers. Reads and writes values to the database
 * using a Hibernate service to ensure all servers in a high-availability
 * cluster see the same data.
 * 
 * TODO: This extends EPRestrictedRESTfulBaseController which is an exact copy
 * of org.openecomp.portalsdk.core.controller.RestrictedRESTfulBaseController.
 */

@RestController
@RequestMapping("/context")
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class SharedContextRestController extends EPRestrictedRESTfulBaseController {
	/**
	 * Access to the database
	 */
	@Autowired
	SharedContextService contextService;

	/**
	 * Logger for debug etc.
	 */
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SharedContextRestController.class);

	/**
	 * Reusable JSON (de)serializer
	 */
	private final ObjectMapper mapper = new ObjectMapper();

	/**
	 * Gets a value for the specified context and key (RESTful service method).
	 * 
	 * @param ctxtId
	 *            ID that identifies the context, usually the ECOMP Portal
	 *            session key.
	 * @param ckey
	 *            Key for the key-value pair to fetch
	 * @return JSON with shared context object; response=null if not found.
	 */
	@RequestMapping(value = { "/get" }, method = RequestMethod.GET, produces = "application/json")
	public String getContext(HttpServletRequest request, @RequestParam String context_id, @RequestParam String ckey) throws Exception {
		String jsonResponse		= "";
		
		logger.debug(EELFLoggerDelegate.debugLogger, "getContext for ID " + context_id + ", key " + ckey);
		if (context_id == null || ckey == null) {
			throw new Exception("Received null for context_id and/or ckey");
		}
		
		SharedContext context = contextService.getSharedContext(context_id, ckey);
		if (context == null) {
			jsonResponse = convertResponseToJSON(context);
		} else {
			jsonResponse = mapper.writeValueAsString(context);
		}
		
		return jsonResponse;
	}

	/**
	 * Gets user information for the specified context (RESTful service method).
	 * 
	 * @param ctxtId
	 *            ID that identifies the context, usually the ECOMP Portal
	 *            session key.
	 * 
	 * @return JSON with user's first name, last name and email address; null if
	 *         none found
	 */
	@RequestMapping(value = { "/get_user" }, method = RequestMethod.GET, produces = "application/json")
	public String getUserContext(HttpServletRequest request, @RequestParam String context_id) throws Exception {
		String jsonResponse		= "";
		List<SharedContext> listSharedContext = new ArrayList<SharedContext>();
		try{
			logger.debug(EELFLoggerDelegate.debugLogger, "getUserContext for ID " + context_id);
			if (context_id == null) {
				throw new Exception("Received null for contextId");
			}
			try{
				SharedContext firstNameContext = contextService.getSharedContext(context_id, EPSystemProperties.USER_FIRST_NAME);
				SharedContext lastNameContext = contextService.getSharedContext(context_id, EPSystemProperties.USER_LAST_NAME);
				SharedContext emailContext = contextService.getSharedContext(context_id, EPSystemProperties.USER_EMAIL);
				SharedContext userIdContext = contextService.getSharedContext(context_id, EPSystemProperties.USER_ORG_USERID);				
				if (firstNameContext != null)
					listSharedContext.add(firstNameContext);
				if (lastNameContext != null)
					listSharedContext.add(lastNameContext);
				if (emailContext != null)
					listSharedContext.add(emailContext);
				if (userIdContext != null)
					listSharedContext.add(userIdContext);
			}catch(Exception e){
				logger.error(EELFLoggerDelegate.errorLogger, "Error while getting SharedContext values from database ... ",e);
			}
			jsonResponse = convertResponseToJSON(listSharedContext);
		}catch(Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, "Error while running getUserContext request ... ", e);
		}	
		return jsonResponse;
	}

	/**
	 * Tests for presence of the specified key in the specified context (RESTful
	 * service method).
	 * 
	 * @param context_id
	 *            ID that identifies the context, usually the ECOMP Portal
	 *            session key.
	 * @param ckey
	 *            Key for the key-value pair to test
	 * @return JSON with result indicating whether the context and key were
	 *         found.
	 */
	@RequestMapping(value = { "/check" }, method = RequestMethod.GET, produces = "application/json")
	public String checkContext(HttpServletRequest request, @RequestParam String context_id, @RequestParam String ckey) throws Exception {
		String jsonResponse		= "";
		
		logger.debug(EELFLoggerDelegate.debugLogger, "checkContext for " + context_id + ", key " + ckey);
		if (context_id == null || ckey == null) {
			throw new Exception("Received null for contextId and/or key");
		}
		
		String response = null;
		SharedContext context = contextService.getSharedContext(context_id, ckey);
		if (context != null)
			response = "exists";
		
		jsonResponse = convertResponseToJSON(response);
		
		return jsonResponse;
	}

	/**
	 * Removes the specified key in the specified context (RESTful service
	 * method).
	 * 
	 * @param context_id
	 *            ID that identifies the context, usually the ECOMP Portal
	 *            session key.
	 * @param ckey
	 *            Key for the key-value pair to remove
	 * @return JSON with result indicating whether the context and key were
	 *         found.
	 */
	@RequestMapping(value = { "/remove" }, method = RequestMethod.GET, produces = "application/json")
	public String removeContext(HttpServletRequest request, @RequestParam String context_id, @RequestParam String ckey) throws Exception {
		String jsonResponse		= "";
		
		logger.debug(EELFLoggerDelegate.debugLogger, "removeContext for " + context_id + ", key " + ckey);
		if (context_id == null || ckey == null) {
			throw new Exception("Received null for contextId and/or key");
		}
		
		SharedContext context = contextService.getSharedContext(context_id, ckey);
		String response = null;
		if (context != null) {
			contextService.deleteSharedContext(context);
			response = "removed";
		}
		
		jsonResponse = convertResponseToJSON(response);
		
		return jsonResponse;
	}

	/**
	 * Clears all key-value pairs in the specified context (RESTful service
	 * method).
	 * 
	 * @param context_id
	 *            ID that identifies the context, usually the ECOMP Portal
	 *            session key.
	 * @return JSON with result indicating the number of key-value pairs removed.
	 */
	@RequestMapping(value = { "/clear" }, method = RequestMethod.GET, produces = "application/json")
	public String clearContext(HttpServletRequest request, @RequestParam String context_id) throws Exception {
		String jsonResponse		= "";
		
		logger.debug(EELFLoggerDelegate.debugLogger, "clearContext for " + context_id);
		if (context_id == null) {
			throw new Exception("Received null for contextId");
		}
		
		int count = contextService.deleteSharedContexts(context_id);
		jsonResponse = convertResponseToJSON(Integer.toString(count));
		
		return jsonResponse;
	}

	/**
	 * Sets a context value for the specified context and key (RESTful service
	 * method). Creates the context if no context with the specified ID-key pair
	 * exists, overwrites the value if it exists already.
	 * 
	 * @param userJson
	 *            JSON block with these tag-value pairs:
	 *            <UL>
	 *            <LI>context_id: ID that identifies the context
	 *            <LI>ckey: Key for the key-value pair to store
	 *            <LI>cvalue: Value to store
	 *            </UL>
	 * @return JSON with result indicating whether the value was added (key not
	 *         previously known) or replaced (key previously known).
	 */
	@RequestMapping(value = { "/set" }, method = RequestMethod.POST, produces = "application/json")
	public String setContext(HttpServletRequest request, @RequestBody String userJson) throws Exception {
		String jsonResponse		= "";
		
		@SuppressWarnings("unchecked")
		Map<String, Object> userData = mapper.readValue(userJson, Map.class);
		// Use column names as JSON tags
		final String contextId = (String) userData.get("context_id");
		final String key = (String) userData.get("ckey");
		final String value = (String) userData.get("cvalue");
		if (contextId == null || key == null) {
			throw new Exception("Received null for contextId and/or key");
		}
		
		logger.debug(EELFLoggerDelegate.debugLogger, "setContext for ID " + contextId + ", key " + key + "->" + value);
		String response = null;
		SharedContext existing = contextService.getSharedContext(contextId, key);
		if (existing == null) {
			contextService.addSharedContext(contextId, key, value);
		} else {
			existing.setCvalue(value);
			contextService.saveSharedContext(existing);
		}
		response = existing == null ? "added" : "replaced";
		jsonResponse = convertResponseToJSON(response);
		
		return jsonResponse;
	}

	/**
	 * Creates a two-element JSON object tagged "response".
	 * 
	 * @param responseBody
	 * @return
	 * @throws JsonProcessingException
	 */
	private String convertResponseToJSON(String responseBody) throws JsonProcessingException {
		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response", responseBody);
		String response = mapper.writeValueAsString(responseMap);
		return response;
	}

	/**
	 * Converts list of SharedContext objects to a JSON array.
	 * 
	 * @param contextList
	 * @return
	 * @throws JsonProcessingException
	 */
	private String convertResponseToJSON(List<SharedContext> contextList) throws JsonProcessingException {
		String jsonArray = mapper.writeValueAsString(contextList);
		return jsonArray;
	}

	/**
	 * Creates a JSON object with the content of the shared context; null is ok.
	 * 
	 * @param responseBody
	 * @return tag "response" with collection of context object's fields
	 * @throws JsonProcessingException
	 */
	private String convertResponseToJSON(SharedContext context) throws JsonProcessingException {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("response", context);
		String responseBody = mapper.writeValueAsString(responseMap);
		return responseBody;
	}

	@ExceptionHandler(Exception.class)
	protected void handleBadRequests(Exception e, HttpServletResponse response) throws IOException {
		logger.warn(EELFLoggerDelegate.errorLogger, "Handling bad request", e);
		response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
	}

}
