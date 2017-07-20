/*-
 * ================================================================================
 * ECOMP Portal
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openecomp.portalapp.controller.EPRestrictedBaseController;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.domain.MicroserviceParameter;
import org.openecomp.portalapp.portal.domain.WidgetCatalog;
import org.openecomp.portalapp.portal.domain.WidgetCatalogParameter;
import org.openecomp.portalapp.portal.domain.WidgetParameterResult;
import org.openecomp.portalapp.portal.domain.WidgetServiceHeaders;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.openecomp.portalapp.portal.logging.aop.EPAuditLog;
import org.openecomp.portalapp.portal.service.ConsulHealthService;
import org.openecomp.portalapp.portal.service.MicroserviceService;
import org.openecomp.portalapp.portal.service.WidgetParameterService;
import org.openecomp.portalapp.portal.utils.CustomLoggingFilter;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@SuppressWarnings("unchecked")
@RestController
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class WidgetsCatalogController extends EPRestrictedBaseController {

	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(WidgetsCatalogController.class);
	RestTemplate template = new RestTemplate();
	String whatService = "widgets-service";

	@Autowired
	private ConsulHealthService consulHealthService;

	@Autowired
	private MicroserviceService microserviceService;

	@Autowired
	private WidgetParameterService widgetParameterService;

	@Bean
	public CommonsMultipartResolver multipartResolver() {
		return new CommonsMultipartResolver();
	}

	static {
		// for localhost testing only
		javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier() {

			public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
				if (hostname.equals("localhost")) {
					return true;
				}
				return false;
			}
		});		
	}

	@RequestMapping(value = { "/portalApi/microservices/widgetCatalog/{loginName}" }, method = RequestMethod.GET)
	public List<WidgetCatalog> getUserWidgetCatalog(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("loginName") String loginName) throws RestClientException, Exception {
		List<WidgetCatalog> widgets = new ArrayList<>();
		try {CustomLoggingFilter d;
			ResponseEntity<ArrayList> ans = template.exchange(
					EcompPortalUtils.widgetMsProtocol() + "://" + consulHealthService.getServiceLocation(whatService,
							SystemProperties.getProperty("microservices.widget.local.port"))
							+ "/widget/microservices/widgetCatalog/" + loginName,
					HttpMethod.GET, new HttpEntity(WidgetServiceHeaders.getInstance()), ArrayList.class);
			widgets = ans.getBody();
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getUserWidgetCatalog failed", e);
			// returning null because null help check on the UI if there was a
			// communication problem with Microservice.
			return null;
		}
		return widgets;
	}

	@RequestMapping(value = { "/portalApi/microservices/widgetCatalog" }, method = RequestMethod.GET)
	public List<WidgetCatalog> getWidgetCatalog(HttpServletRequest request, HttpServletResponse response)
			throws RestClientException, Exception {
		List<WidgetCatalog> widgets = new ArrayList<>();
		
		String p = EcompPortalUtils.widgetMsProtocol();
		try {
			ResponseEntity<ArrayList> ans = template.exchange(
					EcompPortalUtils.widgetMsProtocol() + "://" + consulHealthService.getServiceLocation(whatService,
							SystemProperties.getProperty("microservices.widget.local.port"))
							+ "/widget/microservices/widgetCatalog",
					HttpMethod.GET, new HttpEntity(WidgetServiceHeaders.getInstance()), ArrayList.class);
			widgets = ans.getBody();
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getWidgetCatalog failed", e);
			// returning null because null help check on the UI if there was a
			// communication problem with Microservice.
			return null;
		}
		return widgets;
	}

	@RequestMapping(value = {
			"/portalApi/microservices/widgetCatalog/{widgetId}" }, method = RequestMethod.PUT, produces = "application/json")
	public void updateWidgetCatalog(HttpServletRequest request, HttpServletResponse response,
			@RequestBody WidgetCatalog newWidgetCatalog, @PathVariable("widgetId") long widgetId)
			throws RestClientException, Exception {
		template.exchange(
				EcompPortalUtils.widgetMsProtocol() + "://" + consulHealthService.getServiceLocation(whatService,
						SystemProperties.getProperty("microservices.widget.local.port"))
						+ "/widget/microservices/widgetCatalog/" + widgetId,
				HttpMethod.PUT, new HttpEntity(newWidgetCatalog, WidgetServiceHeaders.getInstance()), String.class);
	}

	@RequestMapping(value = { "/portalApi/microservices/widgetCatalog/{widgetId}" }, method = RequestMethod.DELETE)
	public void deleteOnboardingWidget(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("widgetId") long widgetId) throws RestClientException, Exception {
		template.exchange(
				EcompPortalUtils.widgetMsProtocol() + "://" + consulHealthService.getServiceLocation(whatService,
						SystemProperties.getProperty("microservices.widget.local.port"))
						+ "/widget/microservices/widgetCatalog/" + widgetId,
				HttpMethod.DELETE, new HttpEntity(WidgetServiceHeaders.getInstance()), String.class);
	}

	@RequestMapping(value = { "/portalApi/microservices/widgetCatalog/{widgetId}" }, method = RequestMethod.POST)
	public String updateWidgetCatalogWithFiles(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("widgetId") long widgetId) throws RestClientException, Exception {
		MultipartHttpServletRequest mRequest;
		MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
		String fileName;
		String tmp_folder = "/tmp/";
		String respond = null;
		FileOutputStream fo = null;
		try {
			mRequest = (MultipartHttpServletRequest) request;
			MultipartFile mFile = mRequest.getFile("file");
			fileName = mFile.getOriginalFilename();
			fo = new FileOutputStream(tmp_folder + fileName);
			fo.write(mFile.getBytes());

			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.MULTIPART_FORM_DATA);
			multipartRequest.add("file", new FileSystemResource(tmp_folder + fileName));
			multipartRequest.add("widget", request.getParameter("newWidget"));
			respond = template.postForObject(
					EcompPortalUtils.widgetMsProtocol() + "://" + consulHealthService.getServiceLocation(whatService,
							SystemProperties.getProperty("microservices.widget.local.port"))
							+ "/widget/microservices/widgetCatalog/" + widgetId,
					new HttpEntity<>(multipartRequest, WidgetServiceHeaders.getInstance()), String.class);
			File f = new File(tmp_folder + fileName);
			f.delete();
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "updateWidgetCatalogWithFiles failed", e);
		} finally {
			try {
				if (fo != null)
					fo.close();
			} catch (IOException e) {
				logger.error(EELFLoggerDelegate.errorLogger, "updateWidgetCatalogWithFiles failed 2", e);
			}
		}
		return respond;
	}

	@RequestMapping(value = { "/portalApi/microservices/widgetCatalog" }, method = RequestMethod.POST)
	public String createWidgetCatalog(HttpServletRequest request, HttpServletResponse response)
			throws RestClientException, Exception {
		MultipartHttpServletRequest mRequest;
		MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
		String fileName;
		String tmp_folder = "/tmp/";
		String respond = null;
		FileOutputStream fo = null;
		try {
			mRequest = (MultipartHttpServletRequest) request;
			MultipartFile mFile = mRequest.getFile("file");
			fileName = mFile.getOriginalFilename();
			fo = new FileOutputStream(tmp_folder + fileName);
			fo.write(mFile.getBytes());

			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.MULTIPART_FORM_DATA);
			multipartRequest.add("file", new FileSystemResource(tmp_folder + fileName));
			multipartRequest.add("widget", request.getParameter("newWidget"));

			respond = template.postForObject(
					EcompPortalUtils.widgetMsProtocol() + "://" + consulHealthService.getServiceLocation(whatService,
							SystemProperties.getProperty("microservices.widget.local.port"))
							+ "/widget/microservices/widgetCatalog",
					new HttpEntity<>(multipartRequest, WidgetServiceHeaders.getInstance()), String.class);
			File f = new File(tmp_folder + fileName);
			f.delete();
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "createWidgetCatalog failed", e);
		} finally {
			try {
				if (fo != null)
					fo.close();
			} catch (IOException e) {
				logger.error(EELFLoggerDelegate.errorLogger, "createWidgetCatalog failed 2", e);
			}
		}
		return respond;
	}

	@RequestMapping(value = "/portalApi/microservices/{widgetId}/framework.js", method = RequestMethod.GET)
	public String getWidgetFramework(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("widgetId") long widgetId) throws RestClientException, Exception {
		return template.getForObject(EcompPortalUtils.widgetMsProtocol() + "://"
				+ consulHealthService.getServiceLocation(whatService,
						SystemProperties.getProperty("microservices.widget.local.port"))
				+ "/widget/microservices/" + widgetId + "/framework.js", String.class,
				WidgetServiceHeaders.getInstance());
	}

	@RequestMapping(value = "/portalApi/microservices/{widgetId}/controller.js", method = RequestMethod.GET)
	public String getWidgetController(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("widgetId") long widgetId) throws RestClientException, Exception {
		return template.getForObject(EcompPortalUtils.widgetMsProtocol() + "://"
				+ consulHealthService.getServiceLocation(whatService,
						SystemProperties.getProperty("microservices.widget.local.port"))
				+ "/widget/microservices/" + widgetId + "/controller.js", String.class,
				WidgetServiceHeaders.getInstance());
	}

	@RequestMapping(value = "/portalApi/microservices/{widgetId}/style.css", method = RequestMethod.GET)
	public String getWidgetCSS(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("widgetId") long widgetId) throws RestClientException, Exception {
		return template.getForObject(EcompPortalUtils.widgetMsProtocol() + "://"
				+ consulHealthService.getServiceLocation(whatService,
						SystemProperties.getProperty("microservices.widget.local.port"))
				+ "/widget/microservices/" + widgetId + "/styles.css", String.class,
				WidgetServiceHeaders.getInstance());
	}

	@RequestMapping(value = { "/portalApi/microservices/parameters/{widgetId}" }, method = RequestMethod.GET)
	public PortalRestResponse<List<WidgetParameterResult>> getWidgetParameterResult(HttpServletRequest request,
			HttpServletResponse response, @PathVariable("widgetId") long widgetId) throws Exception {
		EPUser user = EPUserUtils.getUserSession(request);

		List<WidgetParameterResult> list = new ArrayList<>();
		Long serviceId = template.exchange(
				EcompPortalUtils.widgetMsProtocol() + "://" + consulHealthService.getServiceLocation(whatService,
						SystemProperties.getProperty("microservices.widget.local.port"))
						+ "/widget/microservices/widgetCatalog/parameters/" + widgetId,
				HttpMethod.GET, new HttpEntity(WidgetServiceHeaders.getInstance()), Long.class).getBody();
		if (serviceId == null) {
			// return ok/sucess and no service parameter for this widget
			return new PortalRestResponse<List<WidgetParameterResult>>(PortalRestStatusEnum.WARN,
					"No service parameters for this widget", list);
		} else {
			List<MicroserviceParameter> defaultParam = microserviceService.getParametersById(serviceId);
			for (MicroserviceParameter param : defaultParam) {
				WidgetParameterResult user_result = new WidgetParameterResult();
				user_result.setParam_id(param.getId());
				user_result.setDefault_value(param.getPara_value());
				user_result.setParam_key(param.getPara_key());
				WidgetCatalogParameter userValue = widgetParameterService.getUserParamById(widgetId, user.getId(),
						param.getId());
				if (userValue == null)
					user_result.setUser_value(param.getPara_value());
				else {
					user_result.setUser_value(userValue.getUser_value());
				}
				list.add(user_result);
			}
		}
		return new PortalRestResponse<List<WidgetParameterResult>>(PortalRestStatusEnum.OK, "SUCCESS", list);
	}

	@RequestMapping(value = { "/portalApi/microservices/services/{paramId}" }, method = RequestMethod.GET)
	public List<WidgetCatalogParameter> getUserParameterById(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("paramId") long paramId) throws Exception {
		List<WidgetCatalogParameter> list = widgetParameterService.getUserParameterById(paramId);
		return list;
	}

	@RequestMapping(value = { "/portalApi/microservices/services/{paramId}" }, method = RequestMethod.DELETE)
	public void deleteUserParameterById(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("paramId") long paramId) throws Exception {
		widgetParameterService.deleteUserParameterById(paramId);
	}

	@RequestMapping(value = { "/portalApi/microservices/download/{widgetId}" }, method = RequestMethod.GET)
	public void doDownload(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("widgetId") long widgetId) throws RestClientException, Exception {

		ServletContext context = request.getServletContext();
		byte[] byteFile = template.exchange(
				EcompPortalUtils.widgetMsProtocol() + "://" + consulHealthService.getServiceLocation(whatService,
						SystemProperties.getProperty("microservices.widget.local.port"))
						+ "/widget/microservices/download/" + widgetId,
				HttpMethod.GET, new HttpEntity(WidgetServiceHeaders.getInstance()), byte[].class).getBody();

		File downloadFile = File.createTempFile("temp", ".zip");
		FileOutputStream stream = new FileOutputStream(downloadFile.getPath());
		stream.write(byteFile);
		stream.close();

		FileInputStream inputStream = new FileInputStream(downloadFile);
		String mimeType = context.getMimeType(downloadFile.getPath());
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}

		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
		downloadFile.delete();
		response.setHeader(headerKey, headerValue);

		OutputStream outStream = response.getOutputStream();
		byte[] buffer = new byte[32 * 1024];
		int bytesRead = -1;
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}

		inputStream.close();
		outStream.close();
	}

	@RequestMapping(value = { "/portalApi/microservices/parameters" }, method = RequestMethod.POST)
	public PortalRestResponse<String> saveWidgetParameter(HttpServletRequest request, HttpServletResponse response,
			@RequestBody WidgetCatalogParameter widgetParameters) throws Exception {
		EPUser user = EPUserUtils.getUserSession(request);
		widgetParameters.setUserId(user.getId());
		try {
			WidgetCatalogParameter oldParam = widgetParameterService.getUserParamById(widgetParameters.getWidgetId(),
					widgetParameters.getUserId(), widgetParameters.getParamId());
			if (oldParam != null) {
				widgetParameters.setId(oldParam.getId());
			}
			widgetParameterService.saveUserParameter(widgetParameters);

		} catch (Exception e) {
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "FAILURE", e.getMessage());
		}
		return new PortalRestResponse<String>(PortalRestStatusEnum.OK, "SUCCESS", "");
	}
}
