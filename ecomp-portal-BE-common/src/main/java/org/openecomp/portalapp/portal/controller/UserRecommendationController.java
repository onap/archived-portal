package org.openecomp.portalapp.portal.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openecomp.portalapp.controller.EPRestrictedBaseController;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.logging.aop.EPAuditLog;
import org.openecomp.portalapp.portal.service.ConsulHealthService;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class UserRecommendationController extends EPRestrictedBaseController {

	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(UserRecommendationController.class);

	@Autowired
	private ConsulHealthService consulHealthService;

	private static final String MACHINE_LEARNING_SERVICE_CTX = "/ml_api";
	private static final String GET_RECOMMENDATION =  MACHINE_LEARNING_SERVICE_CTX + "/" + "getRecommendation";
	private static final String GET_RECOMM_COUNT = MACHINE_LEARNING_SERVICE_CTX + "/" + "getRecommCount";
	private static final String CONSUL_ML_SERVICE_ID = "mlearning-service";	
	private static final String SERVICE_PROTOCOL = "http";

	@RequestMapping(value = {
			"/portalApi/getRecommendationsCount" }, method = RequestMethod.GET, produces = "application/json")
	public String getRecommendationsCount(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		Map<String, String> requestMapping = new HashMap<String, String>();
		requestMapping.put("id", user.getOrgUserId());
		requestMapping.put("action", "reports");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// set your entity to send
		HttpEntity<Map<String,String>> entity = new HttpEntity<>(requestMapping, headers);
		String endpoint = SERVICE_PROTOCOL + "://"+ 	consulHealthService.getServiceLocation(CONSUL_ML_SERVICE_ID,
				SystemProperties.getProperty("microservices.m-learn.local.port")) + GET_RECOMM_COUNT;
		logger.debug(EELFLoggerDelegate.debugLogger, "Going to hit mlearning endpoint on: {1}", endpoint);
		ResponseEntity<String> out = new RestTemplate().exchange(endpoint, HttpMethod.POST, entity, String.class);
		return out.getBody();
	}

	@RequestMapping(value = {
			"/portalApi/getRecommendations" }, method = RequestMethod.GET, produces = "application/json")
	public String getRecommendations(HttpServletRequest request, HttpServletResponse response) {
		EPUser user = EPUserUtils.getUserSession(request);
		Map<String, String> requestMapping = new HashMap<String, String>();
		requestMapping.put("id", user.getOrgUserId());
		requestMapping.put("action", "reports");
		requestMapping.put("recommendations", "1");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// set your entity to send
		HttpEntity<Map<String,String>> entity = new HttpEntity<>(requestMapping, headers);
		String endpoint = SERVICE_PROTOCOL + "://"+ 
				consulHealthService.getServiceLocation(CONSUL_ML_SERVICE_ID,
						SystemProperties.getProperty("microservices.m-learn.local.port")) + GET_RECOMMENDATION;
		logger.debug(EELFLoggerDelegate.debugLogger, "Going to hit mlearning endpoint on: {1}", endpoint);
		ResponseEntity<String> out = new RestTemplate().exchange(endpoint, HttpMethod.POST, entity, String.class);
		return out.getBody();
	}

}