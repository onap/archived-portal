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
package org.openecomp.portalapp.controller;

public class EPRestrictedRESTfulBaseController extends EPFusionBaseController{
	
	protected String viewName;
	private String exceptionView;
	
	@Override
	public boolean isAccessible() {
		return false;
	}
	
	@Override
	public boolean isRESTfulCall(){
		return true;
	}
	
	protected String getViewName() {
		return viewName;
	}
	
	protected void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public String getExceptionView() {
        return (exceptionView == null) ? "runtime_error_handler" : exceptionView;
	}

	public void setExceptionView(String exceptionView) {
		this.exceptionView = exceptionView;
	}
	

}
