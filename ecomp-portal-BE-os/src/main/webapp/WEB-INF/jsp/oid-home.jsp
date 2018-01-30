<%--
  ============LICENSE_START==========================================
  ONAP Portal
  ===================================================================
  Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
  ===================================================================
 
  Unless otherwise specified, all software contained herein is licensed
  under the Apache License, Version 2.0 (the "License");
  you may not use this software except in compliance with the License.
  You may obtain a copy of the License at
 
              http://www.apache.org/licenses/LICENSE-2.0
 
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 
  Unless otherwise specified, all documentation contained herein is licensed
  under the Creative Commons License, Attribution 4.0 Intl. (the "License");
  you may not use this documentation except in compliance with the License.
  You may obtain a copy of the License at
 
              https://creativecommons.org/licenses/by/4.0/
 
  Unless required by applicable law or agreed to in writing, documentation
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 
  ============LICENSE_END============================================
 
  ECOMP is a trademark and service mark of AT&T Intellectual Property.
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags"%>
<%@ page session="false" %>
<o:header title="Home"/>
<o:topbar pageName="Home"/>
<div class="container-fluid main">
	<div class="row-fluid">
		<div class="span10 offset1">

			<h1>
				Hello world!
			</h1>
		
			<div>
				<p class="well">
					<security:authorize access="hasRole('ROLE_USER')">
						<b><span class="text-success">You are currently logged in.</span></b>
					</security:authorize>
					<security:authorize access="!hasRole('ROLE_USER')">
						<b><span class="text-error">You are <em>NOT</em> currently logged in.</span></b>			
					</security:authorize>
				</p>
				
				<p>This example application is configured with several pages requiring different levels of access. 
				This page does not require the user to be logged in. Use the tabs in the navbar above to navigate to 
				pages with different access requirements.
				</p>
			
				<ul>
					<li><a href="user">User</a>, requires the user to be logged in with the <code>ROLE_USER</code> Spring Security authority.</li>
					<li><a href="admin">Admin</a>, requires the user to be logged in with the <code>ROLE_ADMIN</code> Spring Security authority. 
					    See below for the currently configured list of admin accounts.</li>
					<security:authorize access="hasRole('ROLE_USER')">
						<li><a href="j_spring_security_logout">Logout</a>, log out directly and return to this page.</li>
					</security:authorize>
					<security:authorize access="!hasRole('ROLE_USER')">
						<li><a href="login">Log in</a>, log in directly and return to this page.</li>
					</security:authorize>
				</ul>
			
			
			</div>
		
			<div>
				<h3>Client Filter Configuration</h3>
				
				<p>This authorization filter for this client has been configured with the following components:</p>
				
				<ul>
					<li>Issuer service: <code>${ issuerServiceClass }</code></li>
					<li>Server configuration service: <code>${ serverConfigurationServiceClass }</code></li>
					<li>Client configuration service: <code>${ clientConfigurationServiceClass }</code></li>
					<li>Auth request options service: <code>${ authRequestOptionsServiceClass }</code></li>
					<li>Auth request URI builder: <code>${ authRequestUriBuilderClass }</code></li>
				</ul>
			</div>
			
			<div>
				<h3>Administrators</h3>
				
				<p>Logged in users are assigned the <code>ROLE_USER</code> authority by default, but the following users
				 (identified by issuer/subject pairs) will also be given <code>ROLE_ADMIN</code>:</p>
				
				<table class="table table-striped table-hover span4">
					<tr>
						<th>Issuer</th>
						<th>Subject</th>
					</tr>
					<c:forEach items="${ admins }" var="admin">
						<tr>
							<td>${ admin.issuer }</td>
							<td>${ admin.subject }</td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
	</div>
</div>


<o:footer />
