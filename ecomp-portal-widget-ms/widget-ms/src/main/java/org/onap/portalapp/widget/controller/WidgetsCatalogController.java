package org.onap.portalapp.widget.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onap.portalapp.widget.domain.ValidationRespond;
import org.onap.portalapp.widget.domain.WidgetCatalog;
import org.onap.portalapp.widget.service.StorageService;
import org.onap.portalapp.widget.service.WidgetCatalogService;
import org.onap.portalapp.widget.utils.AuthorizationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
public class WidgetsCatalogController {

	@Value("${server.port}")
	String port;
	@Value("${server.contextPath}")
	String context;
	
	@Value("${security.user.name}")
	String security_user;
	@Value("${security.user.password}")
	String security_pass;
	
	@Autowired
	WidgetCatalogService widgetCatalogService;

	@Autowired
	StorageService storageService;
	
	@Autowired
	RestTemplate restTemplate;

	AuthorizationUtil util = new AuthorizationUtil();
	
	private static final Logger logger = LoggerFactory.getLogger(WidgetsCatalogController.class);
	
	@ResponseBody
	@RequestMapping(value = { "/microservices/widgetCatalog" }, method = RequestMethod.GET, produces = "application/json")
	public List<WidgetCatalog> getWidgetCatalog(HttpServletRequest request, HttpServletResponse response
			,@RequestHeader(value="Authorization") String auth) throws IOException{
		
		List<WidgetCatalog> widgetCatalog = null;
		if(!util.authorization(auth, security_user, security_pass)){ 
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			logger.error("Basic Authentication Error while performing WidgetsCatalogController.getWidgetCatalog in widget microserivce. Please check your username and password.");
			return widgetCatalog;
		}
		try {
			widgetCatalog = widgetCatalogService.getWidgetCatalog();
			logger.debug("WidgetsCatalogController.getWidgetCatalog: getting widget list {}", widgetCatalog);
		} catch (Exception e) {
			logger.error("Exception occurred while performing WidgetsCatalogController.getWidgetCatalog in widget microservices. Details:" + e.getMessage());
		}
		return widgetCatalog;
	}
	
	@ResponseBody
	@RequestMapping(value = { "/microservices/widgetCatalog/{loginName}" }, method = RequestMethod.GET, produces = "application/json")
	public List<WidgetCatalog> getUserWidgetCatalog(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable("loginName") String loginName, @RequestHeader(value="Authorization") String auth) throws IOException {
		List<WidgetCatalog> widgetCatalog = null;
		if(!util.authorization(auth, security_user, security_pass)){
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			logger.error("Basic Authentication Error while performing WidgetsCatalogController.getUserWidgetCatalog in widget microserivce. Please check your username and password.");
			return widgetCatalog;
		}
		try {
			widgetCatalog = widgetCatalogService.getUserWidgetCatalog(loginName);
			logger.debug("WidgetsCatalogController.getUserWidgetCatalog: getting widget list {}", widgetCatalog);
		} catch (Exception e) {
			logger.error("Exception occurred while performing WidgetsCatalogController.getUserWidgetCatalog in widget microservices. Details:" + e.getMessage());
		}
		return widgetCatalog;
	}

	@ResponseBody
	@RequestMapping(value = { "/microservices/widgetCatalog/{widgetId}" }, method = RequestMethod.PUT, produces = "application/json")
	public void updateWidgetCatalog(HttpServletRequest request, HttpServletResponse response,
			@RequestBody WidgetCatalog newWidgetCatalog, @PathVariable("widgetId") long widgetId,
			@RequestHeader(value="Authorization") String auth) throws IOException {

		if(!util.authorization(auth, security_user, security_pass)){
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			logger.error("Basic Authentication Error while performing WidgetsCatalogController.updateWidgetCatalog in widget microserivce. Please check your username and password.");
			return;
		}
		try {
			widgetCatalogService.updateWidgetCatalog(widgetId, newWidgetCatalog);
			logger.debug("WidgetsCatalogController.updateWidgetCatalog: updating widget {}", newWidgetCatalog);
		} catch (Exception e) {
			logger.error("Exception occurred while performing WidgetsCatalogController.updateWidgetCatalog in widget microservices. Details:" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	@ResponseBody
	@RequestMapping(value = { "/microservices/widgetCatalog" }, method = RequestMethod.POST, produces = "application/json")
	public ValidationRespond saveWidgetCatalog(HttpServletRequest request, HttpServletResponse response, @RequestHeader(value="Authorization") String auth,
			@RequestParam("file") MultipartFile file, @RequestParam("widget") String widget) throws IOException {	
	
		ValidationRespond respond = null;
		if(!util.authorization(auth, security_user, security_pass)){
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			logger.error("Basic Authentication Error while performing WidgetsCatalogController.saveWidgetCatalog in widget microserivce. Please check your username and password.");
			return new ValidationRespond(false, "Basic Authentication Error, please check your username and password.");
		}	
		try {
			//check the zip file structure first
			respond = storageService.checkZipFile(file);
			
			if(respond.isValid()){ 
				//save the widget catalog
				WidgetCatalog newWidget = new ObjectMapper().readValue(widget, WidgetCatalog.class);
				
				long widgetId = widgetCatalogService.saveWidgetCatalog(newWidget);
				logger.debug("WidgetsCatalogController.saveWidgetCatalog: saving widget={}", newWidget);
				//save the widget zip file ;
				storageService.save(file, newWidget, widgetId);
			}
			
		} catch (Exception e) {
			logger.error("Exception occurred while performing WidgetsCatalogController.saveWidgetCatalog in widget microservices. Details:", e);
		}
		return respond;
	}

	@ResponseBody
	@RequestMapping(value = { "/microservices/widgetCatalog/{widgetId}" }, method = RequestMethod.POST, produces = "application/json")
	public ValidationRespond updateWidgetCatalogwithFiles(HttpServletRequest request, HttpServletResponse response, @RequestHeader(value="Authorization") String auth,
			@RequestParam("file") MultipartFile file, @RequestParam("widget") String widget, @PathVariable("widgetId") long widgetId) throws IOException {	
		System.out.println("microserivces updating with files" + widgetId);
		ValidationRespond respond = null;
		if(!util.authorization(auth, security_user, security_pass)){
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			logger.error("Basic Authentication Error while performing WidgetsCatalogController.saveWidgetCatalog in widget microserivce. Please check your username and password.");
			return new ValidationRespond(false, "Basic Authentication Error, please check your username and password.");
		}	
		try {
			//check the zip file structure first
			respond = storageService.checkZipFile(file);
			if(respond.isValid()){
				//update the widget catalog
				WidgetCatalog newWidget = new ObjectMapper().readValue(widget, WidgetCatalog.class);
				widgetCatalogService.updateWidgetCatalog(widgetId, newWidget);
				logger.debug("WidgetsCatalogController.saveWidgetCatalog: updating widget with widgetId={}", widgetId);
				//update the widget zip file
				storageService.update(file, newWidget, widgetId);
			}
		} catch (Exception e) {
			logger.error("Exception occurred while performing WidgetsCatalogController.saveWidgetCatalog in widget microservices. Details:" + e.getMessage());
			e.printStackTrace();
		}
		return respond;
	}
	
	@ResponseBody
	@RequestMapping(value = { "/microservices/widgetCatalog/{widgetId}" }, method = {
			RequestMethod.DELETE }, produces = "application/json")
	public void deleteOnboardingWidget(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("widgetId") long widgetId, @RequestHeader(value="Authorization") String auth)  throws IOException{		
		if(!util.authorization(auth, security_user, security_pass)){
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			logger.error("Basic Authentication Error while performing WidgetsCatalogController.deleteOnboardingWidget in widget microserivce. Please check your username and password.");
			return;
		}
		try {
			logger.debug("WidgetsCatalogController.deleteOnboardingWidget: deleting widget {}", widgetId);
			//WidgetCatalog widget = widgetCatalogService.getWidgetCatalog(widgetId);
			widgetCatalogService.deleteWidgetCatalog(widgetId);
			storageService.deleteWidgetFile(widgetId);
		} catch (Exception e) {
			logger.error("Exception occurred while performing WidgetsCatalogController.deleteOnboardingWidget in widget microservices. Details:" + e.getMessage());
		}
	}
	
	@ResponseBody
	@RequestMapping(value = { "/microservices/widgetCatalog/parameters/{widgetId}" }, method = RequestMethod.GET, produces = "application/json")
	public Long getServiceIdByWidget(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable("widgetId") Long widgetId, @RequestHeader(value="Authorization") String auth) throws IOException {
		
		Long serviceId = null;
		if(!util.authorization(auth, security_user, security_pass)){
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			logger.error("Basic Authentication Error while performing WidgetsCatalogController.getServiceIdByWidget in widget microserivce. Please check your username and password.");
			return serviceId;
		}
		try{
			logger.debug("WidgetsCatalogController.getServiceIdByWidget: getting service Id for widget {}", widgetId);
			serviceId = widgetCatalogService.getServiceIdByWidget(widgetId);
		}catch(Exception e){
			logger.error("Exception occurred while performing WidgetsCatalogController.getServiceIdByWidget in widget microservices. Details:" + e.getMessage());
		}
		return serviceId;
	}

	
	@ResponseBody
	@RequestMapping(value = { "/microservices/widgetCatalog/service/{serviceId}" }, method = RequestMethod.GET, produces = "application/json")
	public List<WidgetCatalog> getWidgetByServiceId(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable("serviceId") Long serviceId, @RequestHeader(value="Authorization") String auth) throws IOException {
		List<WidgetCatalog> list = new ArrayList<WidgetCatalog>();
		if(!util.authorization(auth, security_user, security_pass)){
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			logger.error("Basic Authentication Error while performing WidgetsCatalogController.getWidgetByServiceId in widget microserivce. Please check your username and password.");
			return null;
		}	
		try{
			logger.debug("WidgetsCatalogController.getWidgetByServiceId: getting service Id for widget {}", serviceId);
			list = widgetCatalogService.getWidgetsByServiceId(serviceId);
		}catch(Exception e){
			logger.error("Exception occurred while performing WidgetsCatalogController.getWidgetByServiceId in widget microservices. Details:" + e.getMessage());
		}
		return list;
	}
	
	
	@ResponseBody
	@RequestMapping(value = { "/microservices/download/{widgetId}" }, method = RequestMethod.GET, produces = "application/json")
	public byte[] getWidgetZipFile(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable("widgetId") long widgetId, @RequestHeader(value="Authorization") String auth) throws Exception {
		byte[] byteFile = null;
		if(!util.authorization(auth, security_user, security_pass)){
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			logger.error("Basic Authentication Error while performing WidgetsCatalogController.getWidgetZipFile in widget microserivce. Please check your username and password.");
			return byteFile;
		}
		try {
			byteFile = storageService.getWidgetCatalogContent(widgetId);
			logger.debug("WidgetsCatalogController.getWidgetZipFile: getting widget zip file for widget with id {}", widgetId);
		} catch (Exception e) {
			logger.error("Exception occurred while performing WidgetsCatalogController.getWidgetZipFile in widget microservices. Details:" + e.getMessage());
		}
		return byteFile;
	}

	
}