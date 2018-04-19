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
 
  
  --%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="org.onap.portalsdk.core.util.SystemProperties" %>
<%@ page import="org.onap.portalapp.portal.utils.EPSystemProperties" %>
<c:set var="title" value="Login" />
<c:set var="isMobileEnabled"
	value="<%=(SystemProperties.getProperty(SystemProperties.MOBILE_ENABLE)!= null && SystemProperties.getProperty(SystemProperties.MOBILE_ENABLE).trim().equals(\"true\"))%>" />

<!DOCTYPE html>
<html ng-app="abs">
	<head>
		<link rel="shortcut icon" href="assets/images/1cc621d2.ecomp_logo.png">
	    <title>
	      Login
        </title>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1"> 
	<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
		<script src="static/js/jquery-1.10.2.js" type="text/javascript"></script>
		<script src= "static/ebz/angular_js/angular.js"></script> 
		<script src= "static/ebz/angular_js/angular-sanitize.js"></script>
		<script src= "static/ebz/angular_js/gestures.js"></script>
		<style>
			.terms {
				font-family: Verdana,Arial,Helvetica, sans-serif;
				font-size: 9px;
			}
			.login-tbl {
				border: 0px;
			}
			.login-txt {
			    font-family: Arial;
			    font-size: 14px;
			    text-decoration: none;
			}
			.login-input-text {
				width: 140px;
				height:25px;
				border-radius:7px;
				padding-left:5px;			
			    font-family: Arial;
				font-size: 14px;
			}
			.login-btn {
			    cursor: pointer;
			    background: #d97b34;
			    background-image: -webkit-linear-gradient(top, #d97b34, #b8632b);
			    background-image: -moz-linear-gradient(top, #d97b34, #b8632b);
			    background-image: -ms-linear-gradient(top, #d97b34, #b8632b);
			    background-image: -o-linear-gradient(top, #d97b34, #b8632b);
			    background-image: linear-gradient(to bottom, #d97b34, #b8632b);
			    -webkit-border-radius: 7;
			    -moz-border-radius: 7;
			    border-radius: 7px;
			    font-family: Arial;
			    color: #ffffff;
			    font-size: 13px;
			    padding: 4px 10px 4px 10px;
			    text-decoration: none;
			}
		</style>
	</head>
	<body style="padding-top: 15px;">
	<% 
       String frontUrl = SystemProperties.getProperty(EPSystemProperties.FE_URL);
    %>
	<div ng-controller="externalLoginController">
		<div class="centered style="-webkit-transform: translateZ(0);background:white, z-index:0;">
			<div align="center" id="errorInfo" style="display:none; float:left; font-family: Arial; font-size:12px; margin-left:5px">
				<span style="color:red">Invalid username or password. Please try again.</span>
			</div>
			<br/>
			<div align="center" style="margin-left:auto;margin-right:auto;width:40%;padding:6px;background-color:white">
          		<img src="static/fusion/images/onap-portal-logo.png" height="250"/>
				<br>
				<div style="opacity: 0.7;">
					<table class="login-tbl">
						<tr>
							<td>	
								<label class="login-txt">Login ID:</label>
							</td>
							<td>
								<input type="text" class="login-input-text" ng-model="loginId" maxlength="30" />
							</td>
						</tr>
						<tr>
							<td>
								<label class="login-txt">Password:</label>
							</td>
							<td>
								<input type="password" class="login-input-text" ng-model="password" maxlength="30" 
									onkeydown="if (event.keyCode == 13) document.getElementById('loginBtn').click()"/>
							</td>
						</tr>
					</table> 
					<br />
					<a class="login-btn" id="loginBtn" ng-click="loginExternal();">LOGIN</a>
				</div>
				<br>
			</div>
		</div>
		<br/><br/><br/><br/><br/><br/><br/>
    </div>
    </body>
<script>
var app=angular.module("abs", []);
app.controller("externalLoginController", function ($scope) { 
	// Table Data
	
	$scope.viewPerPage = 200;
	$scope.currentPage = 2;
	$scope.totalPage;
	$scope.searchCategory = "";
	$scope.searchString = "";
	$scope.loginId="";
	$scope.password="";
	$scope.loginError=true;
	$scope.viewPerPage = 200;
	$scope.currentPage = 2;
	$scope.totalPage;
	$scope.searchCategory = "";
	$scope.searchString = "";
	$scope.loginId="";
	$scope.password="";
	$scope.loginUrl = "";
	
	$scope.loginExternal = function() {
		var postData={loginId:$scope.loginId,password:$scope.password};
		$.ajax({
	            url: "open_source/login?",
                type : "POST",
		  		 dataType: 'json',
		  		 contentType: 'application/json',
		  		 data: JSON.stringify(postData),                
                success:function (response){
                  if(response.success=="success"){
                    //window.location.href = 'applicationsHome';
                    window.location.href= "<%=frontUrl%>"
                  }else{
                	$("#errorInfo span").text(response);
                	//$("#errorInfo").text = response;
                	$("#errorInfo").show();
                  }
                },
                error:function( jqXHR, status,error ){
                	$("#errorInfo").show();
                }
                
        });

    };
});
</script>
	
</html>
