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
<o:header title="Login"/>
<div class="container-fluid main">
	<div class="row-fluid">
		<div class="span10 offset1">

			<h2>Welcome to ECOMP Portal OpenId Login</h2>
			
			<p>
				Please provide the URI for your <code>OpenId Authorization Server</code>. Make sure the OpenId Connect Server is Running on the following location 
			</p>
			
			<p>
				If you do not have one of your own, for a quick start up, you can clone from <a href='https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server'>this</a> github location - It's an open source OpenID Connect Server.
			</p>
			
			<p>
				Simply clone on your local, go to sub-project directory called 'openid-connect-server-webapp' and do 
				
				<code>mvn jetty:run</code>				
				  
			</p>
						
			<p>
				Choose a different port using the jetty plugin inside pom.xml, if the default 8080 is already taken by your Application.
			</p>
			
			
			
			<div class="well">
				<div class="row-fluid">
	
					<div class="span8">
						<form action="openid_connect_login" method="get">
							<input type="text" class="input-xxlarge" name="identifier" id="identifier" value = "http://localhost:8383/openid-connect-server-webapp/" />
							<input type="submit" value="Log In" />
						</form>
					</div>
				</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function () {
		$('#localhost').on('click', function() {
			$('#identifier').val('http://localhost:8383/openid-connect-server-webapp/');
		});
		$('#mitreidorg').on('click', function() {
			$('#identifier').val('user@mitreid.org');
		});
		
	});
</script>
