<%--
  ================================================================================
  eCOMP Portal
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
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="org.openecomp.portalsdk.core.util.SystemProperties" %>
<%@ page import="org.openecomp.portalapp.portal.utils.EPSystemProperties" %>
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
        <link rel="stylesheet" type="text/css" href="static/fusion/css/jquery-ui.css">
		<script src="static/js/jquery-1.10.2.js" type="text/javascript"></script>
		<script src= "static/ebz/angular_js/angular.js"></script> 
		<script src= "static/ebz/angular_js/angular-sanitize.js"></script>
		<script src= "static/ebz/angular_js/app.js"></script>
		<script src= "static/ebz/angular_js/gestures.js"></script>
		<style>
			.terms {
				font-family: Verdana,Arial,Helvetica, sans-serif;
				font-size: 9px;
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
			<div align="center" id="errorInfo" style="display:none;float:left;font-size:12px;margin-left:5px"><span style="color:red">Invaild username or password, Please try again</span></div>
			<br/>
			<div align="center" style="margin-left:auto;margin-right:auto;width:40%;padding:6px;opacity:0.7;background-color:white">
          		<img src="static/fusion/images/ecomp-login.jpg" height="250"/>
				<br>
				<label>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label class="form-field__label">Login ID:</label>
				</label>
				<input  type="text" class="fn-ebz-text" ng-model="loginId" style="width: 140px;height:25px;border-radius:7px;font-size:18px;padding-left:5px;"
					maxlength="30" />
				<br/>
				<br/>
				<label >&nbsp;Password:</label>
				<input type="password" class="span3" ng-model="password" style="width: 140px;height:25px;border-radius:7px;font-size:18px;padding-left:5px;"
					maxlength="30" onkeydown="if (event.keyCode == 13) document.getElementById('loginBtn').click()"/> 
				<br />
				<br />
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<a class="login-btn" id="loginBtn" ng-click="loginExternal();">LOGIN</a>
				<br>
			</div>
		</div>
		<br/><br/><br/><br/><br/><br/><br/>
    </div>
    </body>
<script>
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
	/**
	$scope.loginExternal = function() {

        
		  var redirectUrl = "login_external/login";
          var form = $('<form action="' + redirectUrl + '" method="post">' +
                  '<input type="hidden" name="loginId" value='+$scope.loginId+' />' +
                  '<input type="hidden" name="password" value='+$scope.password+' />' +
                  '</form>');
                  
                  $('body').append(form);
                  $(form).submit();
         

    };
    */	
});
</script>
	
</html>
