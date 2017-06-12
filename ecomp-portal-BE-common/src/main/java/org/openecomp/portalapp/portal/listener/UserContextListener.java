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
package org.openecomp.portalapp.portal.listener;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class UserContextListener implements ServletContextListener{
	@SuppressWarnings("rawtypes")
	public void contextInitialized(ServletContextEvent event){
    	ServletContext context = event.getServletContext();        
        //
        // instanciate a map to store references to all the active
        // sessions and bind it to context scope.
        //
        HashMap activeUsers = new HashMap();
        context.setAttribute("activeUsers", activeUsers);
    }

    /**
     * Needed for the ServletContextListener interface.
     */
    public void contextDestroyed(ServletContextEvent event){
        // To overcome the problem with losing the session references
        // during server restarts, put code here to serialize the
        // activeUsers HashMap.  Then put code in the contextInitialized
        // method that reads and reloads it if it exists...
    }
}
