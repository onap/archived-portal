<%--
  ============LICENSE_START==========================================
  ONAP Portal
  ===================================================================
  Copyright © 2017 AT&T Intellectual Property. All rights reserved.
  ===================================================================
 
  Unless otherwise specified, all software contained herein is licensed
  under the Apache License, Version 2.0 (the “License”);
  you may not use this software except in compliance with the License.
  You may obtain a copy of the License at
 
              http://www.apache.org/licenses/LICENSE-2.0
 
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 
  Unless otherwise specified, all documentation contained herein is licensed
  under the Creative Commons License, Attribution 4.0 Intl. (the “License”);
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
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags"%>
<o:header title="Admin"/>
<o:topbar pageName="Admin"/>
<div class="container-fluid main">
	<div class="row-fluid">
		<div class="span10 offset1">

			<h1>Hello ${ userInfo.name }</h1>

			<div>
				<p>This page requires that the user be logged in with a valid account and the <code>ROLE_ADMIN</code> Spring Security authority.
				If you are reading this page, <span class="text-success">you are currently logged in as an administrator</span>.</p>

				<p>The authorization provider will assign your account a set of authorities depending on how it's configured.
				Your current login has the following Spring Security authorities:</p>
				
				<ul>
					<security:authentication property="authorities" var="authorities" />
					<c:forEach items="${authorities}" var="auth">
						<li><code>${ auth }</code></li>
					</c:forEach>
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
