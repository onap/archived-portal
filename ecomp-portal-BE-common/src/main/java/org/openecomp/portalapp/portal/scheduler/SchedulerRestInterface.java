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
package org.openecomp.portalapp.portal.scheduler;

import java.util.Collections;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;
import org.openecomp.portalapp.portal.scheduler.client.HttpBasicClient;
import org.openecomp.portalapp.portal.scheduler.client.HttpsBasicClient;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.springframework.stereotype.Service;
import org.openecomp.portalapp.portal.scheduler.restobjects.RestObject;


@Service
public class SchedulerRestInterface implements SchedulerRestInterfaceIfc {

	private static Client client = null;
		
	private MultivaluedHashMap<String, Object> commonHeaders;
	
	public SchedulerRestInterface() {
		super();
	}
	
	public void initRestClient()
	{
		final String methodname = "initRestClient()";
		
		final String username = "";//SystemProperties.getProperty(SchedulerProperties.SCHEDULER_USER_NAME_VAL);
		//final String password = "";//SystemProperties.getProperty(SchedulerProperties.SCHEDULER_PASSWORD_VAL);
		final String scheduler_url = "";//SystemProperties.getProperty(SchedulerProperties.SCHEDULER_SERVER_URL_VAL);
		final String decrypted_password = "";//Password.deobfuscate(password);
		
		String authString = username + ":" + decrypted_password;
		
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		String authStringEnc = new String(authEncBytes);

		commonHeaders = new MultivaluedHashMap<String, Object> ();
		commonHeaders.put("Authorization",  Collections.singletonList((Object) ("Basic " + authStringEnc)));
		
		boolean use_ssl = true;
		if ( (scheduler_url != null) && ( !(scheduler_url.isEmpty()) ) ) {
			if ( scheduler_url.startsWith("https")) {
				use_ssl = true;
			}
			else {
				use_ssl = false;
			}
		}
		if (client == null) {
			
			try {
				if ( use_ssl ) { 
					
					client = HttpsBasicClient.getClient();
				}
				else {
					
					client = HttpBasicClient.getClient();
				}
			} catch (Exception e) {
				System.out.println(  methodname + " Unable to get the SSL client");
			}
		}
	}
		
	@SuppressWarnings("unchecked")
	public <T> void Get (T t, String sourceId, String path, org.openecomp.portalapp.portal.scheduler.restobjects.RestObject<T> restObject ) throws Exception {
		
		String methodName = "Get";
		String url = SystemProperties.getProperty(SchedulerProperties.SCHEDULER_SERVER_URL_VAL) + path;
		
		
		System.out.println( "<== URL FOR GET : " + url + "\n");

        initRestClient();
		
		final Response cres = client.target(url)
			 .request()
	         .accept("application/json")
	         .headers(commonHeaders)
	         .get();
				
		int status = cres.getStatus();
		restObject.setStatusCode (status);
		
		if (status == 200) {
			 t = (T) cres.readEntity(t.getClass());
			 restObject.set(t);
			
		 } else {
		     throw new Exception(methodName + " with status="+ status + ", url= " + url );
		 }

		return;
	}
		
	@SuppressWarnings("unchecked")
	public <T> void Post(T t, JSONObject requestDetails, String path, RestObject<T> restObject) throws Exception {
		
        String methodName = "Post";
        String url = SystemProperties.getProperty(SchedulerProperties.SCHEDULER_SERVER_URL_VAL) + path;
		        
        System.out.println( "<== URL FOR POST : " + url + "\n");
     
        try {
            
            initRestClient();    
    
            // Change the content length
            final Response cres = client.target(url)
            	 .request()
                 .accept("application/json")
	        	 .headers(commonHeaders)
                 //.header("content-length", 201)
                 //.header("X-FromAppId",  sourceID)
                 .post(Entity.entity(requestDetails, MediaType.APPLICATION_JSON));
            
            try {
	   			t = (T) cres.readEntity(t.getClass());
	   			restObject.set(t);
            }
            catch ( Exception e ) {
            	
            	System.out.println("<== " + methodName + " : No response entity, this is probably ok, e=" + e.getMessage());
            }

            int status = cres.getStatus();
    		restObject.setStatusCode (status);    		
    		    		
    		if ( status >= 200 && status <= 299 ) {
    			    			
    			System.out.println( "<== " + methodName + " : REST api POST was successful!" + "\n");
    		
             } else {
            	 System.out.println( "<== " + methodName + " : FAILED with http status : "+status+", url = " + url + "\n");
             }    
   
        } catch (Exception e)
        {
        	System.out.println( "<== " + methodName + " : with url="+url+ ", Exception: " + e.toString() + "\n");
        	throw e;        
        }
    }
	
	@SuppressWarnings("unchecked")
   	public <T> void Delete(T t, JSONObject requestDetails, String sourceID, String path, RestObject<T> restObject) {
	 
		String url="";
		Response cres = null;
		
		try {
			initRestClient();
			
			url = SystemProperties.getProperty(SchedulerProperties.SCHEDULER_SERVER_URL_VAL) + path;
		
			cres = client.target(url)
					 .request()
			         .accept("application/json")
	        		 .headers(commonHeaders)
			         //.entity(r)
			         .build("DELETE", Entity.entity(requestDetails, MediaType.APPLICATION_JSON)).invoke();
			       //  .method("DELETE", Entity.entity(r, MediaType.APPLICATION_JSON));
			         //.delete(Entity.entity(r, MediaType.APPLICATION_JSON));
			
			int status = cres.getStatus();
	    	restObject.setStatusCode (status);
	    			
			try {
	   			t = (T) cres.readEntity(t.getClass());
	   			restObject.set(t);
            }
            catch ( Exception e ) {
            }
   
        } 
		catch (Exception e)
        {     	
        	 throw e;        
        }
	}
	
	public <T> T getInstance(Class<T> clazz) throws IllegalAccessException, InstantiationException
	{
		return clazz.newInstance();
	}

	@Override
	public void logRequest(JSONObject requestDetails) {
		// TODO Auto-generated method stub
		
	}	
}
