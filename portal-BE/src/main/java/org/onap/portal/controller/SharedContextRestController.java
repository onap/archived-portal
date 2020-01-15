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
package org.onap.portal.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.onap.portal.domain.db.fn.FnSharedContext;
import org.onap.portal.domain.dto.ecomp.SharedContext;
import org.onap.portal.exception.NotValidDataException;
import org.onap.portal.logging.aop.EPAuditLog;
import org.onap.portal.service.sharedContext.FnSharedContextService;
import org.onap.portal.utils.EPCommonSystemProperties;
import org.onap.portal.utils.PortalConstants;
import org.onap.portal.validation.DataValidator;
import org.onap.portal.validation.SecureString;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Configuration
@RestController
@RequestMapping(PortalConstants.REST_AUX_API + "/context")
@EnableAspectJAutoProxy
@EPAuditLog
public class SharedContextRestController {
	private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SharedContextRestController.class);
	private static final ObjectMapper mapper = new ObjectMapper();

	private final FnSharedContextService contextService;
	private final DataValidator dataValidator;

	@Autowired
	public SharedContextRestController(FnSharedContextService contextService,
		DataValidator dataValidator) {
		this.contextService = contextService;
		this.dataValidator = dataValidator;
	}

	@ApiOperation(value = "Gets a value for the specified context and key.", response = SharedContext.class)
	@RequestMapping(value = { "/get" }, method = RequestMethod.GET, produces = "application/json")
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

		FnSharedContext context = contextService.getFnSharedContext(context_id, ckey);
		String jsonResponse;
		if (context == null)
			jsonResponse = convertResponseToJSON(context);
		else
			jsonResponse = mapper.writeValueAsString(context);

		return jsonResponse;
	}

	@ApiOperation(value = "Gets user information for the specified context.", response = SharedContext.class, responseContainer = "List")
	@RequestMapping(value = { "/get_user" }, method = RequestMethod.GET, produces = "application/json")
	public String getUserContext(HttpServletRequest request, @RequestParam String context_id) throws Exception {

		logger.debug(EELFLoggerDelegate.debugLogger, "getUserContext for ID " + context_id);
		if (context_id == null)
			throw new Exception("Received null for context_id");
		SecureString secureContextId = new SecureString(context_id);
		if (!dataValidator.isValid(secureContextId))
			throw new NotValidDataException("context_id is not valid");

		List<FnSharedContext> listSharedContext = new ArrayList<>();
		FnSharedContext firstNameContext = contextService.getFnSharedContext(context_id,
				EPCommonSystemProperties.USER_FIRST_NAME);
		FnSharedContext lastNameContext = contextService.getFnSharedContext(context_id,
				EPCommonSystemProperties.USER_LAST_NAME);
		FnSharedContext emailContext = contextService.getFnSharedContext(context_id, EPCommonSystemProperties.USER_EMAIL);
		FnSharedContext orgUserIdContext = contextService.getFnSharedContext(context_id,
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

	@ApiOperation(value = "Tests for presence of the specified key in the specified context.")
	@RequestMapping(value = { "/check" }, method = RequestMethod.GET, produces = "application/json")
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
		FnSharedContext context = contextService.getFnSharedContext(context_id, ckey);
		if (context != null)
			response = "exists";

		return convertResponseToJSON(response);
	}

	@ApiOperation(value = "Removes the specified key in the specified context.")
	@RequestMapping(value = { "/remove" }, method = RequestMethod.GET, produces = "application/json")
	public String removeContext(HttpServletRequest request, @RequestParam String context_id, @RequestParam String ckey)
			throws Exception {

		logger.debug(EELFLoggerDelegate.debugLogger, "removeContext for " + context_id + ", key " + ckey);
		if (context_id == null || ckey == null)
			throw new Exception("Received null for contextId and/or key");

		SecureString secureContextId = new SecureString(context_id);
		SecureString secureCKey = new SecureString(ckey);

		if (!dataValidator.isValid(secureContextId) || !dataValidator.isValid(secureCKey))
			throw new NotValidDataException("Not valid data for contextId and/or key");

		FnSharedContext context = contextService.getFnSharedContext(context_id, ckey);
		String response = null;
		if (context != null) {
			contextService.delete(context);
			response = "removed";
		}

		return convertResponseToJSON(response);
	}

	@ApiOperation(value = "Clears all key-value pairs in the specified context.")
	@RequestMapping(value = { "/clear" }, method = RequestMethod.GET, produces = "application/json")
	public String clearContext(HttpServletRequest request, @RequestParam String contextId) throws Exception {

		logger.debug(EELFLoggerDelegate.debugLogger, "clearContext for " + contextId);
		if (contextId == null)
			throw new Exception("clearContext: Received null for contextId");

		SecureString secureContextId = new SecureString(contextId);

		if (!dataValidator.isValid(secureContextId))
			throw new NotValidDataException("Not valid data for contextId");

		int count = contextService.deleteSharedContexts(contextId);
		return convertResponseToJSON(Integer.toString(count));
	}

	@ApiOperation(value = "Sets a context value for the specified context and key. Creates the context if no context with the specified ID-key pair exists, overwrites the value if it exists already.")
	@RequestMapping(value = { "/set" }, method = RequestMethod.POST, produces = "application/json")
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
		FnSharedContext existing = contextService.getFnSharedContext(contextId, key);
		if (existing == null) {
			contextService.addFnSharedContext(contextId, key, value);
		} else {
			existing.setCvalue(value);
			contextService.save(existing);
		}
		response = existing == null ? "added" : "replaced";
		return convertResponseToJSON(response);
	}

	private String convertResponseToJSON(String responseBody) throws JsonProcessingException {
		Map<String, String> responseMap = new HashMap<>();
		responseMap.put("response", responseBody);
		return mapper.writeValueAsString(responseMap);
	}

	private String convertResponseToJSON(List<FnSharedContext> contextList) throws JsonProcessingException {
		return mapper.writeValueAsString(contextList);
	}

	private String convertResponseToJSON(FnSharedContext context) throws JsonProcessingException {
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("response", context);
		return mapper.writeValueAsString(responseMap);
	}

	@ExceptionHandler(Exception.class)
	protected void handleBadRequests(Exception e, HttpServletResponse response) throws IOException {
		logger.error(EELFLoggerDelegate.errorLogger, "handleBadRequest caught exception", e);
		response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
	}

}

