/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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
package org.onap.portalapp.portal.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onap.portalapp.controller.EPRestrictedRESTfulBaseController;
import org.onap.portalapp.portal.domain.SharedContext;
import org.onap.portalapp.portal.exceptions.NotValidDataException;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.service.SharedContextService;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.PortalConstants;
import org.onap.portalapp.validation.DataValidator;
import org.onap.portalapp.validation.SecureString;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;

/**
 * The shared-context feature allows onboarded applications to share data among
 * themselves easily for a given session. It basically implements a Java map:
 * put or get a key-value pair within a map identified by a session ID.
 * 
 * This REST endpoint listens on the Portal app server and answers requests made
 * by back-end application servers. Reads and writes values to the database
 * using a Hibernate service to ensure all servers in a high-availability
 * cluster see the same data.
 */
@Configuration
@RestController
@RequestMapping(PortalConstants.REST_AUX_API + "/context")
@EnableAspectJAutoProxy
@EPAuditLog
public class SharedContextRestController extends EPRestrictedRESTfulBaseController {
	private static final DataValidator dataValidator = new DataValidator();
	private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SharedContextRestController.class);
	private static final ObjectMapper mapper = new ObjectMapper();

	private SharedContextService contextService;

	@Autowired
	public SharedContextRestController(SharedContextService contextService) {
		this.contextService = contextService;
	}

	/**
	 * Gets a value for the specified context and key (RESTful service method).
	 *
	 * @param request
	 *            HTTP servlet request
	 * @param context_id
	 *            ID that identifies the context, usually the ONAP Portal
	 *            session key.
	 * @param ckey
	 *            Key for the key-value pair to fetch
	 * @return JSON with shared context object; response=null if not found.
	 * @throws Exception
	 *             on bad arguments
	 */
	@ApiOperation(value = "Gets a value for the specified context and key.", response = SharedContext.class)
	@GetMapping(value = { "/get" }, produces = "application/json")
	public String getContext(HttpServletRequest request, @RequestParam String context_id, @RequestParam String ckey)
			throws Exception {
		logger.debug(EELFLoggerDelegate.debugLogger, "getContext for ID " + context_id + ", key " + ckey);
		if (context_id == null || ckey == null)
			throw new Exception("Received null for context_id and/or ckey");
		SecureString secureContextId = new SecureString(context_id);
		SecureString secureCKey = new SecureString(ckey);

		if(!dataValidator.isValid(secureContextId) || !dataValidator.isValid(secureCKey)){
			throw new NotValidDataException("Received not valid for context_id and/or ckey");
		}

		SharedContext context = contextService.getSharedContext(context_id, ckey);
		String jsonResponse;
		if (context == null)
			jsonResponse = convertResponseToJSON(context);
		else
			jsonResponse = mapper.writeValueAsString(context);

		return jsonResponse;
	}

	/**
	 * Gets user information for the specified context (RESTful service method).
	 *
	 * @param request
	 *            HTTP servlet request
	 * @param context_id
	 *            ID that identifies the context, usually the ONAP Portal
	 *            session key.
	 * @return List of shared-context objects as JSON; should have user's first
	 *         name, last name and email address; null if none found
	 * @throws Exception
	 *             on bad arguments
	 */
	@ApiOperation(value = "Gets user information for the specified context.", response = SharedContext.class, responseContainer = "List")
	@GetMapping(value = { "/get_user" }, produces = "application/json")
	public String getUserContext(HttpServletRequest request, @RequestParam String context_id) throws Exception {

		logger.debug(EELFLoggerDelegate.debugLogger, "getUserContext for ID " + context_id);
		if (context_id == null)
			throw new Exception("Received null for context_id");
		SecureString secureContextId = new SecureString(context_id);
		if (!dataValidator.isValid(secureContextId))
			throw new NotValidDataException("context_id is not valid");

		List<SharedContext> listSharedContext = new ArrayList<>();
		SharedContext firstNameContext = contextService.getSharedContext(context_id,
				EPCommonSystemProperties.USER_FIRST_NAME);
		SharedContext lastNameContext = contextService.getSharedContext(context_id,
				EPCommonSystemProperties.USER_LAST_NAME);
		SharedContext emailContext = contextService.getSharedContext(context_id, EPCommonSystemProperties.USER_EMAIL);
		SharedContext orgUserIdContext = contextService.getSharedContext(context_id,
				EPCommonSystemProperties.USER_ORG_USERID);
		if (firstNameContext != null)
			listSharedContext.add(firstNameContext);
		if (lastNameContext != null)
			listSharedContext.add(lastNameContext);
		if (emailContext != null)
			listSharedContext.add(emailContext);
		if (orgUserIdContext != null)
			listSharedContext.add(orgUserIdContext);
		return convertResponseToJSON(listSharedContext);
	}

	/**
	 * Tests for presence of the specified key in the specified context (RESTful
	 * service method).
	 *
	 * @param request
	 *            HTTP servlet request
	 * @param context_id
	 *            ID that identifies the context, usually the ONAP Portal
	 *            session key.
	 * @param ckey
	 *            Key for the key-value pair to test
	 * @return JSON with result indicating whether the context and key were
	 *         found.
	 * @throws Exception
	 *             on bad arguments
	 */
	@ApiOperation(value = "Tests for presence of the specified key in the specified context.", response = SharedContextJsonResponse.class)
	@GetMapping(value = { "/check" }, produces = "application/json")
	public String checkContext(HttpServletRequest request, @RequestParam String context_id, @RequestParam String ckey)
			throws Exception {

		logger.debug(EELFLoggerDelegate.debugLogger, "checkContext for " + context_id + ", key " + ckey);
		if (context_id == null || ckey == null)
			throw new Exception("Received null for contextId and/or key");

		SecureString secureContextId = new SecureString(context_id);
		SecureString secureCKey = new SecureString(ckey);

		if (!dataValidator.isValid(secureContextId) || !dataValidator.isValid(secureCKey))
			throw new NotValidDataException("Not valid data for contextId and/or key");

		String response = null;
		SharedContext context = contextService.getSharedContext(context_id, ckey);
		if (context != null)
			response = "exists";

		return convertResponseToJSON(response);
	}

	/**
	 * Removes the specified key in the specified context (RESTful service
	 * method).
	 *
	 * @param request
	 *            HTTP servlet request
	 * @param context_id
	 *            ID that identifies the context, usually the ONAP Portal
	 *            session key.
	 * @param ckey
	 *            Key for the key-value pair to remove
	 * @return JSON with result indicating whether the context and key were
	 *         found.
	 * @throws Exception
	 *             on bad arguments
	 */
	@ApiOperation(value = "Removes the specified key in the specified context.", response = SharedContextJsonResponse.class)
	@GetMapping(value = { "/remove" }, produces = "application/json")
	public String removeContext(HttpServletRequest request, @RequestParam String context_id, @RequestParam String ckey)
			throws Exception {

		logger.debug(EELFLoggerDelegate.debugLogger, "removeContext for " + context_id + ", key " + ckey);
		if (context_id == null || ckey == null)
			throw new Exception("Received null for contextId and/or key");

		SecureString secureContextId = new SecureString(context_id);
		SecureString secureCKey = new SecureString(ckey);

		if (!dataValidator.isValid(secureContextId) || !dataValidator.isValid(secureCKey))
			throw new NotValidDataException("Not valid data for contextId and/or key");

		SharedContext context = contextService.getSharedContext(context_id, ckey);
		String response = null;
		if (context != null) {
			contextService.deleteSharedContext(context);
			response = "removed";
		}

		return convertResponseToJSON(response);
	}

	/**
	 * Clears all key-value pairs in the specified context (RESTful service
	 * method).
	 *
	 * @param request
	 *            HTTP servlet request
	 * @param context_id
	 *            ID that identifies the context, usually the ONAP Portal
	 *            session key.
	 * @return JSON with result indicating the number of key-value pairs
	 *         removed.
	 * @throws Exception
	 *             on bad arguments
	 */
	@ApiOperation(value = "Clears all key-value pairs in the specified context.", response = SharedContextJsonResponse.class)
	@GetMapping(value = { "/clear" }, produces = "application/json")
	public String clearContext(HttpServletRequest request, @RequestParam String context_id) throws Exception {

		logger.debug(EELFLoggerDelegate.debugLogger, "clearContext for " + context_id);
		if (context_id == null)
			throw new Exception("clearContext: Received null for contextId");

		SecureString secureContextId = new SecureString(context_id);

		if (!dataValidator.isValid(secureContextId))
			throw new NotValidDataException("Not valid data for contextId");

		int count = contextService.deleteSharedContexts(context_id);
		return convertResponseToJSON(Integer.toString(count));
	}

	/**
	 * Sets a context value for the specified context and key (RESTful service
	 * method). Creates the context if no context with the specified ID-key pair
	 * exists, overwrites the value if it exists already.
	 *
	 * @param request
	 *            HTTP servlet request
	 * @param userJson
	 *            JSON block with these tag-value pairs:
	 *            <UL>
	 *            <LI>context_id: ID that identifies the context
	 *            <LI>ckey: Key for the key-value pair to store
	 *            <LI>cvalue: Value to store
	 *            </UL>
	 * @return JSON with result indicating whether the value was added (key not
	 *         previously known) or replaced (key previously known).
	 * @throws Exception
	 *             on bad arguments
	 */
	@ApiOperation(value = "Sets a context value for the specified context and key. Creates the context if no context with the specified ID-key pair exists, overwrites the value if it exists already.", response = SharedContextJsonResponse.class)
	@PostMapping(value = { "/set" }, produces = "application/json")
	public String setContext(HttpServletRequest request, @RequestBody String userJson) throws Exception {
		if (userJson !=null){
		SecureString secureUserJson = new SecureString(userJson);
		if (!dataValidator.isValid(secureUserJson))
			throw new NotValidDataException("Not valid data for userJson");
		}

		@SuppressWarnings("unchecked")
		Map<String, Object> userData = mapper.readValue(userJson, Map.class);
		// Use column names as JSON tags
		final String contextId = (String) userData.get("context_id");
		final String key = (String) userData.get("ckey");
		final String value = (String) userData.get("cvalue");
		if (contextId == null || key == null)
			throw new Exception("setContext: received null for contextId and/or key");

		logger.debug(EELFLoggerDelegate.debugLogger, "setContext: ID " + contextId + ", key " + key + "->" + value);
		String response;
		SharedContext existing = contextService.getSharedContext(contextId, key);
		if (existing == null) {
			contextService.addSharedContext(contextId, key, value);
		} else {
			existing.setCvalue(value);
			contextService.saveSharedContext(existing);
		}
		response = existing == null ? "added" : "replaced";
		return convertResponseToJSON(response);
	}

	/**
	 * Creates a two-element JSON object tagged "response".
	 *
	 * @param responseBody
	 * @return JSON object as String
	 * @throws JsonProcessingException
	 */
	private String convertResponseToJSON(String responseBody) throws JsonProcessingException {
		Map<String, String> responseMap = new HashMap<>();
		responseMap.put("response", responseBody);
		return mapper.writeValueAsString(responseMap);
	}

	/**
	 * Converts a list of SharedContext objects to a JSON array.
	 *
	 * @param contextList
	 * @return JSON array as String
	 * @throws JsonProcessingException
	 */
	private String convertResponseToJSON(List<SharedContext> contextList) throws JsonProcessingException {
		return mapper.writeValueAsString(contextList);
	}

	/**
	 * Creates a JSON object with the content of the shared context; null is ok.
	 *
	 * @param context
	 * @return tag "response" with collection of context object's fields
	 * @throws JsonProcessingException
	 */
	private String convertResponseToJSON(SharedContext context) throws JsonProcessingException {
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("response", context);
		return mapper.writeValueAsString(responseMap);
	}

	/**
	 * Handles any exception thrown by a method in this controller.
	 *
	 * @param e
	 *            Exception
	 * @param response
	 *            HttpServletResponse
	 * @throws IOException
	 */
	@ExceptionHandler(Exception.class)
	protected void handleBadRequests(Exception e, HttpServletResponse response) throws IOException {
		logger.error(EELFLoggerDelegate.errorLogger, "handleBadRequest caught exception", e);
		response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
	}

}
class SharedContextJsonResponse {
	String response;
}

