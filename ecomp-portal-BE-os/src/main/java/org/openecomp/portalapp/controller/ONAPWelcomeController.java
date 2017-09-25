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
package org.openecomp.portalapp.controller;

import java.security.Principal;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mitre.openid.connect.client.SubjectIssuerGrantedAuthority;
import org.openecomp.portalapp.portal.logging.aop.EPAuditLog;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPAuditLog
public class ONAPWelcomeController extends EPRestrictedBaseController{
	String viewName;
		
	@RequestMapping(value = "/index.htm", method = RequestMethod.GET)
    public String getIndexPage(HttpServletRequest request) {
		return "/index";
    }
	
	@RequestMapping(value = {"/applicationsHome", "/dashboard", "/widgetsHome", "/kpidash*", "/admins", "/users", "/portalAdmins", "/applications", "/widgets", "/functionalMenu", "/contactUs", "/getAccess","/appCatalog", "/widgetOnboarding", "/accountOnboarding"}, method = RequestMethod.GET)
    public String getEcompSinglePage(HttpServletRequest request, HttpServletResponse response) {
		return "forward:/index.html";
    }
	
	protected String getViewName() {
		return viewName;
	}
	
	protected void setViewName(String viewName) {
		this.viewName = viewName;
	}
	
	
	@Resource(name = "namedAdmins")
	private Set<SubjectIssuerGrantedAuthority> admins;
	
	@RequestMapping("/user")
	public String user(Principal p) {
		return "oid-user";
	}

	@RequestMapping("/admin")
	public String admin(Model model, Principal p) {

		model.addAttribute("admins", admins);

		return "oid-admin";
	}
	@RequestMapping("/oid-login")
	public ModelAndView login(Principal p) {		
		return new ModelAndView("openIdLogin");
	}	
}
