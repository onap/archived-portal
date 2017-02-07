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
package org.openecomp.portalapp.portal.service;


import java.util.HashMap;

import org.openecomp.portalapp.command.EPLoginBean;
import org.openecomp.portalapp.portal.domain.EPUser;


public interface EPLoginService {

    // validate user exists in the system
    @SuppressWarnings("rawtypes")
	EPLoginBean findUser(EPLoginBean bean, String menuPropertiesFilename, HashMap additionalParams) throws Exception;
    
    @SuppressWarnings("rawtypes")
	EPLoginBean findUser(EPLoginBean bean, String menuPropertiesFilename, HashMap additionalParams, boolean matchPassword) throws Exception;
    
    public EPUser findUserWithoutPwd(String loginId);
    
    @SuppressWarnings("rawtypes")
	public EPLoginBean findUserWithoutPassword(EPLoginBean bean, String menuPropertiesFilename, HashMap additionalParams ) throws Exception;
}
