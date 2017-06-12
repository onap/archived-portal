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
package org.openecomp.portalapp.portal.domain;

import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;

import org.openecomp.portalsdk.core.onboarding.util.CipherUtil;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;

public class WidgetServiceHeaders {
	
	private static HttpHeaders widgetHeaders;
	
	private WidgetServiceHeaders(){}
	
	public static HttpHeaders getInstance() throws Exception{
		if(null == widgetHeaders){
		   return new HttpHeaders(){{
			   	
				    String username = EcompPortalUtils.getPropertyOrDefault("microservices.widget.username", "widget_user");
				    String password = CipherUtil.decrypt(EcompPortalUtils.getPropertyOrDefault("microservices.widget.password", "widget_password"));
			        String auth = username + ":" + password;
			        byte[] encodedAuth = Base64.encodeBase64( 
			           auth.getBytes(Charset.forName("US-ASCII")) );
			        String authHeader = "Basic " + new String( encodedAuth );
			        set( "Authorization", authHeader );
			      }
			   };
		}
		else 
			return widgetHeaders;
		
	}
}
