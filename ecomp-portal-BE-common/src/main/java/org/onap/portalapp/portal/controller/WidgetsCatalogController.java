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

import org.apache.commons.lang.StringUtils;
import org.onap.portalapp.controller.EPRestrictedBaseController;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.MicroserviceParameter;
import org.onap.portalapp.portal.domain.WidgetCatalog;
import org.onap.portalapp.portal.domain.WidgetCatalogParameter;
import org.onap.portalapp.portal.domain.WidgetParameterResult;
import org.onap.portalapp.portal.domain.WidgetServiceHeaders;
import org.onap.portalapp.portal.ecomp.model.PortalRestResponse;
import org.onap.portalapp.portal.ecomp.model.PortalRestStatusEnum;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.service.ConsulHealthService;
import org.onap.portalapp.portal.service.MicroserviceService;
import org.onap.portalapp.portal.service.WidgetParameterService;
import org.onap.portalapp.portal.utils.EPCommonSystemProperties;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalapp.util.EPUserUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
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

	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(WidgetsCatalogController.class);

	private static final String MS_WIDGET_LOCAL_PORT = "microservices.widget.local.port";
	
	private static final String UNAUTHORIZED_OR_FORBIDDEN_FOR_A_DISABLED_USER = "Unauthorized or  Forbidden for a disabled user";

	private RestTemplate template = new RestTemplate();

	private String whatService = "widgets-service";

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
				if ("localhost".equals(hostname))
					return true;
				return false;
			}
		});
	}

	@RequestMapping(value = { "/portalApi/microservices/widgetCatalog/{loginName}" }, method = RequestMethod.GET)
	public List<WidgetCatalog> getUserWidgetCatalog(@PathVariable("loginName") String loginName) {
		List<WidgetCatalog> widgets = new ArrayList<>();
		try {
			ResponseEntity<List> ans = template.exchange(
					EcompPortalUtils.widgetMsProtocol() + "://"
							+ consulHealthService.getServiceLocation(whatService,
									SystemProperties.getProperty(EPCommonSystemProperties.MS_WIDGET_LOCAL_PORT))
							+ "/widget/microservices/widgetCatalog/" + loginName,
					HttpMethod.GET, new HttpEntity<>(WidgetServiceHeaders.getInstance()), List.class);
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
	public List<WidgetCatalog> getWidgetCatalog() {
		List<WidgetCatalog> widgets = new ArrayList<>();
		try {
			ResponseEntity<List> ans = template.exchange(
					EcompPortalUtils.widgetMsProtocol() + "://"
							+ consulHealthService.getServiceLocation(whatService,
									SystemProperties.getProperty(EPCommonSystemProperties.MS_WIDGET_LOCAL_PORT))
							+ "/widget/microservices/widgetCatalog",
					HttpMethod.GET, new HttpEntity<>(WidgetServiceHeaders.getInstance()), List.class);
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
	public void updateWidgetCatalog(@RequestBody WidgetCatalog newWidgetCatalog, @PathVariable("widgetId") long widgetId) throws Exception {
		template.exchange(
				EcompPortalUtils.widgetMsProtocol() + "://"
						+ consulHealthService.getServiceLocation(whatService,
								SystemProperties.getProperty(MS_WIDGET_LOCAL_PORT))
						+ "/widget/microservices/widgetCatalog/" + widgetId,
				HttpMethod.PUT, new HttpEntity<>(newWidgetCatalog, WidgetServiceHeaders.getInstance()), String.class);
	}

	@RequestMapping(value = { "/portalApi/microservices/widgetCatalog/{widgetId}" }, method = RequestMethod.DELETE)
	public void deleteOnboardingWidget(@PathVariable("widgetId") long widgetId) throws Exception {
		template.exchange(
				EcompPortalUtils.widgetMsProtocol() + "://"
						+ consulHealthService.getServiceLocation(whatService,
								SystemProperties.getProperty(MS_WIDGET_LOCAL_PORT))
						+ "/widget/microservices/widgetCatalog/" + widgetId,
				HttpMethod.DELETE, new HttpEntity<>(WidgetServiceHeaders.getInstance()), String.class);
	}

	@RequestMapping(value = { "/portalApi/microservices/widgetCatalog/{widgetId}" }, method = RequestMethod.POST)
	public String updateWidgetCatalogWithFiles(HttpServletRequest request,
			@PathVariable("widgetId") long widgetId) throws RestClientException, Exception {
		MultipartHttpServletRequest mRequest;
		MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
		String fileName;
		String tmpFolderName = "/tmp/";
		String respond = null;
		FileOutputStream fo = null;
		try {
			mRequest = (MultipartHttpServletRequest) request;
			MultipartFile mFile = mRequest.getFile("file");
			fileName = mFile.getOriginalFilename();
			fo = new FileOutputStream(tmpFolderName + fileName);
			fo.write(mFile.getBytes());
			// silence sonar scan by calling close here
			fo.close();
			fo = null;

			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.MULTIPART_FORM_DATA);
			multipartRequest.add("file", new FileSystemResource(tmpFolderName + fileName));
			multipartRequest.add("widget", request.getParameter("newWidget"));
			respond = template.postForObject(
					EcompPortalUtils.widgetMsProtocol() + "://"
							+ consulHealthService.getServiceLocation(whatService,
									SystemProperties.getProperty(EPCommonSystemProperties.MS_WIDGET_LOCAL_PORT))
							+ "/widget/microservices/widgetCatalog/" + widgetId,
					new HttpEntity<>(multipartRequest, WidgetServiceHeaders.getInstance()), String.class);
			File f = new File(tmpFolderName + fileName);
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
	public String createWidgetCatalog(HttpServletRequest request)
			throws Exception {
		
		if (StringUtils.isNotBlank(SystemProperties.getProperty(EPCommonSystemProperties.MS_WIDGET_UPLOAD_FLAG))
				&& SystemProperties.getProperty(EPCommonSystemProperties.MS_WIDGET_UPLOAD_FLAG).equalsIgnoreCase("false")) {
			return UNAUTHORIZED_OR_FORBIDDEN_FOR_A_DISABLED_USER;
		}
		
		MultipartHttpServletRequest mRequest;
		MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
		String fileName;
		String tmpFolderName = "/tmp/";
		String respond = null;
		FileOutputStream fo = null;
		try {
			mRequest = (MultipartHttpServletRequest) request;
			MultipartFile mFile = mRequest.getFile("file");
			fileName = mFile.getOriginalFilename();
			fo = new FileOutputStream(tmpFolderName + fileName);
			fo.write(mFile.getBytes());
			// silence sonar scan by calling close here
			fo.close();
			fo = null;

			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.MULTIPART_FORM_DATA);
			multipartRequest.add("file", new FileSystemResource(tmpFolderName + fileName));
			multipartRequest.add("widget", request.getParameter("newWidget"));

			respond = template.postForObject(
					EcompPortalUtils.widgetMsProtocol() + "://"
							+ consulHealthService.getServiceLocation(whatService,
									SystemProperties.getProperty(EPCommonSystemProperties.MS_WIDGET_LOCAL_PORT))
							+ "/widget/microservices/widgetCatalog",
					new HttpEntity<>(multipartRequest, WidgetServiceHeaders.getInstance()), String.class);
			File f = new File(tmpFolderName + fileName);
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
	public String getWidgetFramework(@PathVariable("widgetId") long widgetId) throws Exception {
		return template.getForObject(EcompPortalUtils.widgetMsProtocol() + "://"
				+ consulHealthService.getServiceLocation(whatService,
						SystemProperties.getProperty(MS_WIDGET_LOCAL_PORT))
				+ "/widget/microservices/" + widgetId + "/framework.js", String.class,
				WidgetServiceHeaders.getInstance());
	}

	@RequestMapping(value = "/portalApi/microservices/{widgetId}/controller.js", method = RequestMethod.GET)
	public String getWidgetController(@PathVariable("widgetId") long widgetId) throws Exception {
		return template.getForObject(EcompPortalUtils.widgetMsProtocol() + "://"
				+ consulHealthService.getServiceLocation(whatService,
						SystemProperties.getProperty(MS_WIDGET_LOCAL_PORT))
				+ "/widget/microservices/" + widgetId + "/controller.js", String.class,
				WidgetServiceHeaders.getInstance());
	}

	@RequestMapping(value = "/portalApi/microservices/{widgetId}/style.css", method = RequestMethod.GET)
	public String getWidgetCSS(@PathVariable("widgetId") long widgetId) throws Exception {
		return template.getForObject(EcompPortalUtils.widgetMsProtocol() + "://"
				+ consulHealthService.getServiceLocation(whatService,
						SystemProperties.getProperty(MS_WIDGET_LOCAL_PORT))
				+ "/widget/microservices/" + widgetId + "/styles.css", String.class,
				WidgetServiceHeaders.getInstance());
	}

	@RequestMapping(value = { "/portalApi/microservices/parameters/{widgetId}" }, method = RequestMethod.GET)
	public PortalRestResponse<List<WidgetParameterResult>> getWidgetParameterResult(HttpServletRequest request,
			@PathVariable("widgetId") long widgetId) throws Exception {
		EPUser user = EPUserUtils.getUserSession(request);

		List<WidgetParameterResult> list = new ArrayList<>();
		Long serviceId = template.exchange(
				EcompPortalUtils.widgetMsProtocol() + "://"
						+ consulHealthService.getServiceLocation(whatService,
								SystemProperties.getProperty(EPCommonSystemProperties.MS_WIDGET_LOCAL_PORT))
						+ "/widget/microservices/widgetCatalog/parameters/" + widgetId,
				HttpMethod.GET, new HttpEntity<>(WidgetServiceHeaders.getInstance()), Long.class).getBody();
		if (serviceId == null) {
			// return ok/sucess and no service parameter for this widget
			return new PortalRestResponse<List<WidgetParameterResult>>(PortalRestStatusEnum.WARN,
					"No service parameters for this widget", list);
		} else {
			List<MicroserviceParameter> defaultParam = microserviceService.getParametersById(serviceId);
			for (MicroserviceParameter param : defaultParam) {
				WidgetParameterResult userResult = new WidgetParameterResult();
				userResult.setParam_id(param.getId());
				userResult.setDefault_value(param.getPara_value());
				userResult.setParam_key(param.getPara_key());
				WidgetCatalogParameter userValue = widgetParameterService.getUserParamById(widgetId, user.getId(),
						param.getId());
				if (userValue == null)
					userResult.setUser_value(param.getPara_value());
				else {
					userResult.setUser_value(userValue.getUser_value());
				}
				list.add(userResult);
			}
		}
		return new PortalRestResponse<List<WidgetParameterResult>>(PortalRestStatusEnum.OK, "SUCCESS", list);
	}

	@RequestMapping(value = { "/portalApi/microservices/services/{paramId}" }, method = RequestMethod.GET)
	public List<WidgetCatalogParameter> getUserParameterById(	@PathVariable("paramId") long paramId) {
		List<WidgetCatalogParameter> list = widgetParameterService.getUserParameterById(paramId);
		return list;
	}

	@RequestMapping(value = { "/portalApi/microservices/services/{paramId}" }, method = RequestMethod.DELETE)
	public void deleteUserParameterById(@PathVariable("paramId") long paramId) {
		widgetParameterService.deleteUserParameterById(paramId);
	}

	@RequestMapping(value = { "/portalApi/microservices/download/{widgetId}" }, method = RequestMethod.GET)
	public void doDownload(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("widgetId") long widgetId) throws Exception {

		ServletContext context = request.getServletContext();
		byte[] byteFile = template
				.exchange(
						EcompPortalUtils.widgetMsProtocol() + "://"
								+ consulHealthService.getServiceLocation(whatService,
										SystemProperties.getProperty(EPCommonSystemProperties.MS_WIDGET_LOCAL_PORT))
								+ "/widget/microservices/download/" + widgetId,
						HttpMethod.GET, new HttpEntity<>(WidgetServiceHeaders.getInstance()), byte[].class)
				.getBody();

		File downloadFile = File.createTempFile("temp", ".zip");
		try(FileOutputStream stream = new FileOutputStream(downloadFile.getPath())){
			stream.write(byteFile);
		}catch(Exception e)
		{
			logger.error(EELFLoggerDelegate.errorLogger, "doDownload failed", e);
			throw e;
		}

		try(FileInputStream inputStream = new FileInputStream(downloadFile);
			OutputStream outStream = response.getOutputStream()){
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
	
			byte[] buffer = new byte[32 * 1024];
			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}
		}catch(Exception e)
		{
			logger.error(EELFLoggerDelegate.errorLogger, "doDownload failed", e);
			throw e;
		}
	}

	@RequestMapping(value = { "/portalApi/microservices/parameters" }, method = RequestMethod.POST)
	public PortalRestResponse<String> saveWidgetParameter(HttpServletRequest request,
			@RequestBody WidgetCatalogParameter widgetParameters) {
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
			logger.error(EELFLoggerDelegate.errorLogger, "saveWidgetParameter failed", e);
			return new PortalRestResponse<String>(PortalRestStatusEnum.ERROR, "FAILURE", e.getMessage());
		}
		return new PortalRestResponse<String>(PortalRestStatusEnum.OK, "SUCCESS", "");
	}
	
	@RequestMapping(value = { "/portalApi/microservices/uploadFlag" }, method = RequestMethod.GET)
	public String getUploadFlag() {
	     String uplaodFlag="";
		try {
			uplaodFlag = SystemProperties.getProperty(EPCommonSystemProperties.MS_WIDGET_UPLOAD_FLAG);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "uploadFlag failed", e);
			return null;
		}
		return uplaodFlag;
	}
}
