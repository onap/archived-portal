/*
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2018 AT&T Intellectual Property. All rights reserved.
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
 *.
 */

package org.onap.portalapp.music.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.onap.music.eelf.logging.EELFLoggerDelegate;
import org.onap.portalapp.music.util.MusicUtil;
import org.springframework.web.filter.DelegatingFilterProxy;

public class MusicSessionRepositoryFilter extends DelegatingFilterProxy{
	private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MusicUtil.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try{
			if (request instanceof HttpServletRequest) {
				String path = ((HttpServletRequest)request).getRequestURI().substring(((HttpServletRequest) request).getContextPath().length());		 
				if(MusicUtil.isExcludedApi(path) || !MusicUtil.isMusicEnable())
					request.setAttribute("org.springframework.session.web.http.SessionRepositoryFilter.FILTERED", Boolean.TRUE);
			}	
		}catch(Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, "Failed to check the exclude apis ", e);
		}
		super.doFilter(request, response, filterChain);
	}
}