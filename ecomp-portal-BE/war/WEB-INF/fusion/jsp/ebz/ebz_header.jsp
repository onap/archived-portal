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
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
	<%@ page isELIgnored ="false" %>
	<%@ page import="org.openecomp.portalsdk.core.util.SystemProperties" %>
	<link rel="stylesheet" type="text/css" href="static/ebz/header_new.css"> 
	<script src= "static/ebz/js/attHeaderSnippet.js"></script>
	<script src= "static/ebz/js/attHeader_new.js"></script> 
     <c:set var="menu"    value="<%=session.getAttribute(SystemProperties.getProperty(SystemProperties.APPLICATION_MENU_ATTRIBUTE_NAME))%>"/>
	
	<style>
	li {
		list-style: none;
	}
	
	#userIcon:hover {
		color: rgb(191, 231, 239);
	}
	
	#headerChatIcon:hover {
		background: url(static/images/headerChatIcon_hover.png) no-repeat -3px
			-3px !important;
	}
	
	.headerContentContainer .primaryMenuContainer .headerIconContainer .popbox .openpopbox:hover
		{
		color: #bfe7fb;
		margin-top: -2px;
		text-decoration: none;
		outline: none
	}
	/* .headerContentContainer .primaryMenuContainer .headerIconContainer .popbox .openpopbox:focus{color:#bfe7fb;margin-top:-2px;text-decoration:none;outline:none}  */
	.headerContentContainer .primaryMenuContainer .headerIconContainer .popbox .openpopbox
		{
		color: #fff;
		display: block;
		min-width: 70px;
		max-width: 115px;
		text-decoration: none
	}
	
	a {
		-webkit-transition: all .3s ease-out;
		-moz-transition: all .3s ease-out;
		transition: all .3s ease-out
	}
	
	a:active,a:hover {
		outline: 0
	}
	
	b,strong {
		font-weight: 700
	}
	
	.thirdMenuContainer {
		width: 100%;
		overflow: auto;
		margin-top: 101px;
		position: fixed;
		z-index: 3000;
		min-height: 140px;
		max-height: 500px;
		opacity: 1;
		background-color: rgb(255, 255, 255);
	}
</style>
	<div style="position:relative; z-index:999;">
		<div class="headerContainer" id="headerContainer" >       
			<div class="headerContentContainer">
				<div class="primaryMenuContainer">
					<div style="background: url(static/ebz/images/att_logo.png) no-repeat scroll -10px -10px transparent; !important;"></div>
					<div class="attHomeContainer">
						<div class="businessCenterR">
							<a id="attBusinessCenter" href="">OpenECOMP Portal</a>
						</div>
					</div>
					<div class="primaryMenuOptionsContainer" >
					
					   <div class="primaryMenuOption">
							<a class="primaryMenuOptionLink" href="javascript:openSecondLevel('m')">Manage</a>
						</div> 
					   <div class="primaryMenuOption">
							<a  id="supportFirstLevel" class="primaryMenuOptionLink" href="javascript:openSecondLevel('s')">Support</a> 
							
					   </div> 
						<div id="indicator" class="selectedOptionIndicator" style="display:none"></div>
					</div>   
					<div class="headerIconContainer">
						<%-- <div class="loginName" style="width:107px;">                   
							<span class="popbox" >
								<a style="color:#ffffff;display: block;min-width: 70px;max-width: 115;" class="openpopbox" href="#" >
									<div id="headerLoginIcon" class="loginIcon"><span id="userIcon" style="font-size:19px;" class="icon-user"></span> </div>
									<div id="headerNameEllipsis" class="ellipsis-header-name">${ociUserName}</div> 
								</a>
								<div class="box1" style="line-height: normal; right: 167px; min-height: 200px; height: auto; width: 390px !important; display: none; top: 0px; left: -230.5px;" target="auth">
									<div class="arrow" style="left: 230px;"></div>
									<div id="reg-header-snippet">
										<div class="reg-profilePicture" style="min-height: 215px;" id="reg-profile-links">
											<div id="reg-profileImage">	
												<div style="clear: both; height: 80px; position: relative; width: 80px;" class="">
													<img id="reg-userProfilePicture-id" style="height: 80px; width: 80px; float: left;" src="" alt="">
													<span style="  background-position: -1px -1px; height: 81px;left: 0;position: absolute;top: 0;width: 81px;">&nbsp;</span>
												</div>
											</div>
											<div id="reg-myprofile-link">
												<a href="">My Profile</a>
											</div>
											<div id="reg-companyProfile-link">
												<a href="">Company Profile</a>
											</div>
											<div id="reg-logout-div">
												<a class="reg-logout-btn" href="ebiz_logout.htm">Log Out</a>	
											</div>
										</div>
										<div tabindex="0" class="reg-profileDetails" id="reg-profiledetails-id">
										<ul class="reg-Details-table">
											<li><div class="reg-userName-table"><div id="reg-userName-table-row"><div id="reg-userName-table-cell"><h3 class="att-global-fonts" id="reg-userName">${ociUserName}&nbsp;</h3><span class="visuallyhidden">.</span></div></div></div></li>
											<li><div class="reg-userEmail-label"><span class="reg-userEmail-label-spn">EMAIL<span class="visuallyhidden">:</span></span></div></li>
											<li><div class="reg-userEmail-value"><span class="reg-userEmail-value-spn">${email}<span class="visuallyhidden">.</span></span></div></li>
											<li><div class="reg-userRole-label"><span class="reg-userRole-label-spn">PROFILE ID<span class="visuallyhidden">:</span></span></div></li>
											<li><div class="reg-userRole-value"><span class="reg-userRole-value-spn">${groupId}<span class="visuallyhidden">.</span></span></div></li>
											<li><div class="reg-userCompany-label"><span class="reg-userCompany-spn"> ENTERPRISE NAME<span class="visuallyhidden">:</span></span></div></li>
											<li><div class="reg-userCompany-value"><span class="reg-userCompany-spn">${serviceProviderId}<span class="visuallyhidden">.</span></span></div></li>
										</ul>	
										</div>
									</div>
								</div>
							</span>
						</div> --%>
						<!-- Chat -->
						
						
					<!-- <div id="headerChatIcon" class='chatIcon' style="background: url(static/images/headerChatIcon.png) no-repeat -3px -3px;"></div>
						<div class="chatBox">
							<div class='arrow'></div>
							<div class="chatBox-header">
								<span class="chatBox-heading">Live Chat</span>
								<i class="icon-erase circle_close_chat"></i>
							</div>
							
						</div>  -->                   
					</div>
				  </div>
			</div>
			   <!-- HTML for the secondary menu for dashboard. -->
			<div id="secondLevel" class="secondaryMenuContainer secondaryMenuContainerForDashboard" style="display:none;">
				<div id="secondaryMenuContentContainer" class="secondaryMenuContentContainer" style="">
					<c:forEach items="${menu}" var="menuItem">
					<div class="secondaryMenuOption" style="margin-left:20px; font-size:16px !important;">
						<a id="${menuItem.id}"   href="${menuItem.action}" class="secondaryMenuOptionLink selectedSecondaryMenuOption">${menuItem.label}</a>
					</div>
					</c:forEach>
				</div>
			</div>
			<c:forEach items="${menu}" var="menuItem">
				<c:if test="${!empty menuItem.childMenus}">
					<div id="thirdLevel${menuItem.id}" class="secondaryMenuContainer secondaryMenuContainerForDashboard" style="display:none;">
						<div id="secondaryMenuContentContainer${menuItem.id}" class="secondaryMenuContentContainer" style="">
						<c:forEach items="${menuItem.childMenus}" var="subMenuItem">
							<div class="secondaryMenuOption" style="margin-left:20px; font-size:16px !important;">
								<a  id="${subMenuItem.id}"  href="${subMenuItem.action}" class="thirdMenuOptionLink selectedSecondaryMenuOption">${subMenuItem.label}</a>
							</div>
						</c:forEach>
						</div>
					</div>
				</c:if>
			</c:forEach>
			<%-- <c:forEach items="${menu}" var="menuItem">
				<c:if test="${!empty menuItem.childMenus}">
					<div id="thirdLevel${menuItem.id}" class="secondaryMenuContainer secondaryMenuContainerForDashboard" style="display:none;">
						<div id="secondaryMenuContentContainer${menuItem.id}" class="secondaryMenuContentContainer" style="">
						<c:forEach items="${menuItem.childMenus}" var="subMenuItem">
							<div class="secondaryMenuOption" style="margin-left:20px; font-size:16px !important;">
								<a  id="${subMenuItem.id}"  href="${subMenuItem.action}" class="thirdMenuOptionLink selectedSecondaryMenuOption">${subMenuItem.label}</a>
							</div>
						</c:forEach>
						</div>
					</div>
				</c:if>
			</c:forEach> --%>
		</div>
		</div>
	
 		<c:forEach items="${menu}" var="menuItem">
			<c:if test="${!empty menuItem.childMenus}">
				<c:forEach items="${menuItem.childMenus}" var="subMenuItem">
					<div id="megaMenu${subMenuItem.id}" class="megaMenuContainer" style="right:0px; margin-top:145px; display:none;">
						<div class="megaMenuContentContainer">
							<ul class="megaMenuTable"  id="megaMenuTable" style="padding:0px;">
								<li class="megaMenuFirstRow megaMenuRow" >
									<ul style=" display: flex;   flex-direction: column;   flex-wrap: wrap; height:500px;">
										<li class="categoryContainerColumn" style="margin-right:30px;">
											<div class="categoryContainer" align="left" style="margin-top:10px; margin-left:40px;">							         
												<c:forEach items="${subMenuItem.childMenus}"	var="childSubMenuItem">
													<div class="categoryOption" style="margin-bottom:13px; width:500px; ">
														<a class="categoryOptionLink" style="margin-bottom:0px; font-size:16px;" href="${childSubMenuItem.action}">${childSubMenuItem.label}</a>
													</div>
												</c:forEach>        	 				
											</div>
										</li>
									</ul>
								</li>
							</ul>
						</div>
					</div>
				</c:forEach>
			</c:if>
		</c:forEach>
		<div id="megaMenuContainerTemp" class="megaMenuContainer" style="right:0px; margin-top:89px; display:none;">
			<div class="megaMenuContentContainer">
				<ul class="megaMenuTable"  id="megaMenuTable" style="padding:0px;">
					<li class="megaMenuFirstRow megaMenuRow" >
						<ul style=" display: flex;   flex-direction: column;   flex-wrap: wrap; height:500px;">
							<li class="categoryContainerColumn" style="margin-right:30px;">
								<div class="categoryContainer" align="left" style="margin-bottom:0px; margin-left:40px;">
									<div class="categoryTitle" style="margin-top:10px; min-width:160px;"></div>         
									<div class="categoryOption" style="margin-bottom:13px;">
										<a class="categoryOptionLink" style="margin-bottom:0px;" href="#"></a>
									</div>					      	 				
								</div>
							</li>		
						</ul>
					</li>
					<li class="megaMenuSecondRow megaMenuRow" style="display:none"></li>
				</ul>
			</div>
		</div> 
		
	
	<script>

 	 	$(document).ready(function() {
		 	$(document).on('mouseleave', '#megaMenuContainer', function() {
				 $("#thirdLevel").css("display", "none"); 
				 $("#megaMenuContainerTemp").css("display", "none"); 

			});
			$(document).on('mouseleave', '#megaMenuContainerTemp', function() {
				 $("#megaMenuContainerTemp").css("display", "none"); 
			});
			
			$(document).on('mouseenter', '#secondLevel', function() {
				<c:forEach items="${menu}" var="menuItem">
					<c:choose>
						<c:when test="${!empty menuItem.childMenus}">
							$("#${menuItem.id}").hover(function() {	
								<c:forEach items="${menu}" var="menuItem2">
									<c:if test="${!empty menuItem2.childMenus}">
										$("#thirdLevel${menuItem2.id}").css("display", "none");
									</c:if>
								</c:forEach>
								$(".megaMenuContainer").css("display", "none");
								$("#thirdLevel${menuItem.id}").css("display", "inline");
							});
						</c:when>
						<c:otherwise>
						$("#${menuItem.id}").hover(function() {	
							<c:forEach items="${menu}" var="menuItem2">
								<c:if test="${!empty menuItem2.childMenus}">
									$("#thirdLevel${menuItem2.id}").css("display", "none");
								</c:if>
							</c:forEach>
							$(".megaMenuContainer").css("display", "none");
						});
						</c:otherwise>
					</c:choose>
				</c:forEach>
			});
			
			$('.thirdMenuOptionLink').hover(function() {
				$(".megaMenuContainer").css("display", "none");
				
				var id= "#megaMenu"+this.id;
				$(id).css("display", "inline");		  
			});
		});
		function openSecondLevel(item){
			if(item=='m'){
				$("#secondLevel").css("display", "inline");
			}
		}  
	
		
	</script>
