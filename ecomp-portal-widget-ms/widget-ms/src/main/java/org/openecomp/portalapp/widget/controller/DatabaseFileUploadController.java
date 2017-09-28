/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the “License”);
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
 * under the Creative Commons License, Attribution 4.0 Intl. (the “License”);
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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.openecomp.portalapp.widget.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openecomp.portalapp.widget.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DatabaseFileUploadController {
	
	@Autowired
	private StorageService storageService;
	
	private static final Logger logger = LoggerFactory.getLogger(DatabaseFileUploadController.class); 

	@ResponseBody
	@RequestMapping(value = "/microservices/markup/{widgetId}", method = RequestMethod.GET)
	public String getWidgetMarkup(HttpServletRequest request, HttpServletResponse response, @PathVariable("widgetId") long widgetId){
		String markup = null;
		try{
			logger.debug("DatabaseFileUploadController.getWidgetMarkup: getting markup.html for widget with widgetId = {}" , widgetId);
			markup = storageService.getWidgetMarkup(widgetId);
		}catch(Exception e){
			logger.error("Exception occurred while performing DatabaseFileUploadController.getWidgetMarkup in widget microservices. Details:" + e.getMessage());
		}
		return markup;
	}

	@ResponseBody
	@RequestMapping(value = "/microservices/{widgetId}/controller.js", method = RequestMethod.GET)
	public String getWidgetController(HttpServletRequest request, HttpServletResponse response, @PathVariable("widgetId") long widgetId){
		String controller = null;
		try{
			logger.debug("DatabaseFileUploadController.getWidgetController: getting controller.js for widget with widgetId = {}" , widgetId);
			controller = storageService.getWidgetController(widgetId); 
		}catch(Exception e){
			logger.error("Exception occurred while performing DatabaseFileUploadController.getWidgetController in widget microservices. Details:" + e.getMessage());
		}
		return controller;
	}

	@ResponseBody
	@RequestMapping(value = "/microservices/{widgetId}/framework.js", method = RequestMethod.GET)
	public String getWidgetFramework(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("widgetId") long widgetId){
		String framework = null;
		try{
			logger.debug("DatabaseFileUploadController.getWidgetFramework: getting framework.js for widget with widgetId = {}" , widgetId);
			framework = storageService.getWidgetFramework(widgetId);
		}catch(Exception e){
			logger.error("Exception occurred while performing DatabaseFileUploadController.getWidgetFramework in widget microservices. Details:" + e.getMessage());
		}
		return framework;
	}
	
	@ResponseBody
	@RequestMapping(value = "/microservices/{widgetId}/styles.css", method = RequestMethod.GET)
	public String getWidgetCSS(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("widgetId") long widgetId){
		String css = null;
		try {
			logger.debug("DatabaseFileUploadController.getWidgetCSS: getting styles.css for widget with widgetId = {}" , widgetId);
			css = storageService.getWidgetCSS(widgetId);
		} catch (UnsupportedEncodingException e) {
			logger.error("Exception occurred while performing DatabaseFileUploadController.getWidgetCSS in widget microservices. Details:" + e.getMessage());
		} 
		return css;	
	}
}
