<%--
  ================================================================================
  ECOMP Portal
  ================================================================================
  Copyright (C) 2017 AT&T Intellectual Property
  ================================================================================
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  ================================================================================
  --%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="org.openecomp.portalsdk.core.util.SystemProperties" %>
<!DOCTYPE html>
<html ng-app="abs">
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1"> 
	<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
		<link rel="stylesheet" type="text/css" href="static/ebz/fn-ebz.css" >
		<link rel="stylesheet" type="text/css" href="static/ebz/sandbox/styles/style.css" >
		<script src="static/js/jquery-1.10.2.js" type="text/javascript"></script>
		<script src= "static/ebz/angular_js/angular.js"></script> 
		<script src= "static/ebz/angular_js/angular-sanitize.js"></script>
		<script src= "static/ebz/angular_js/att_abs_tpls.js"></script>
		<script src= "static/ebz/angular_js/app.js"></script>
		<script src= "static/ebz/angular_js/gestures.js"></script>
		<script src="static/js/modalService.js"></script>
		<style>
			.terms {
				font-family: Verdana,Arial,Helvetica, sans-serif;
				font-size: 12px;
			}
		</style>
	</head>
	<body style="padding-top: 15px;display: none;">
	<% 
       String returnUrl = request.getParameter("returnUrl");
	   String redirectUrl = request.getParameter("redirectUrl");
       returnUrl = ((returnUrl == null) ? (request.isSecure() ?"https://":"http://") + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/process_csp" + "?redirectUrl=" + redirectUrl: URLEncoder.encode(returnUrl));
     %>
     <c:set var="returnUrl"       value="<%=returnUrl%>"/>
     <c:set var="cspLoginUrl"     value="<%=SystemProperties.getProperty(SystemProperties.CSP_LOGIN_URL)%>"/>
	<div ng-controller="loginController">
		<!-- AT&T Logo -->
		<div style="position: fixed; left:15px; top:15px; ">
         	<img src="static/fusion/images/logo_att_header.jpg" alt="AT&T" />
        </div>
		<div class="centered style="-webkit-transform: translateZ(0);background:white, z-index:0;">
			<div class="centered" style="height:15px">
				<div class="centered">
				<div align="center" style="display: block;margin-left: auto;margin-right: auto;width: 100%;">
 					<img src="static/fusion/images/ecomp-login.jpg" height="250"/>
					<br>
					<font class="headerText">
		            	<a id="goUrl" href="${cspLoginUrl}"><b>Click here to login</b></a>
		            </font>		
				</div>
				<br />
				<br />
			</div>
		</div>
				<br/><br/><br/><br/><br/><br/><br/><br/>
		<div id="footer" style="margin-top:300px">
			<div style="margin-left:auto;margin-right:auto;text-align: center;">
				<p><font class="terms">
					<b>Warning:</b> This system is restricted to AT&T authorized users for business purposes. 
					Unauthorized access is a violation of the law. 
					<br>
					This service may be monitored for administrative and security reasons. 
					By proceeding, you consent to this monitoring.
				</font></p>
			</div>	
			<div class="terms" style="text-align: center;">
				<a target="_top" href="http://www.att.com/terms/">Terms and Conditions</a> | 
				<a target="_top" href="http://www.att.com/privacy/">Privacy Policy</a>.
				<br><span>&#169; 2017 AT&amp;T.  All rights reserved.</span>
		    </div>
		</div>
    </div>
    </body>
<script>
		function getParameterByName(name, url) {
		    if (!url) url = window.location.href;
		    name = name.replace(/[\[\]]/g, "\\$&");
		    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
		        results = regex.exec(url);
		    if (!results) return '';
		    if (!results[2]) return '';
		    return (results[2].replace(/\+/g, " "));
		}
		
		var winHref = window.location.href;
		var appPathIndex = winHref.substring(0,winHref.lastIndexOf(".htm")).lastIndexOf("/");
		var goUrl =winHref.substring(0,appPathIndex+1) + "process_csp" + "?redirectUrl=" + getParameterByName('redirectUrl') ;
		document.getElementById("goUrl").href = document.getElementById("goUrl").href + goUrl; 
		if(getParameterByName('skipClick')=="Yes"){
			document.getElementById("goUrl").click();
		}else{
			$("body").show();
		}
		
</script>
<script>
app.controller("loginController", function ($scope) { 
	
});
</script>
</html>
