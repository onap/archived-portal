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
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>

<link rel="stylesheet" type="text/css" href="static/fusion/sample/css/slider.css">
<link rel="stylesheet" type="text/css" href="static/fusion/sample/css/scribble.css" />
<link rel="stylesheet" type="text/css" href="static/fusion/sample/css/spacegallery.css" />
<link rel="stylesheet" href="static/fusion/css/att_angular_gridster/ui-gridster.css"/>
<link rel="stylesheet" href="static/fusion/css/att_angular_gridster/sandbox-gridster.css"/>

<script src= "static/ebz/angular_js/angular.js"></script> 
<script src= "static/ebz/angular_js/angular-sanitize.js"></script>

<script src= "static/ebz/angular_js/app.js"></script>
<script src= "static/ebz/angular_js/gestures.js"></script>

<script src="static/js/jquery-1.10.2.js"></script>
<!-- <script src="static/fusion/js/jquery.resize.js"></script> -->
<script src="static/fusion/js/att_angular_gridster/ui-gridster-tpls.js"></script>
<script src="static/fusion/js/att_angular_gridster/angular-gridster.js"></script>

<script src= "static/ebz/angular_js/checklist-model.js"></script>
<script src= "static/js/modalService.js"></script>
<script src="static/js/jquery.mask.min.js" type="text/javascript"></script>
<script src="static/js/jquery-ui.js" type="text/javascript"></script>
<script src="static/ebz/sandbox/att-abs-tpls.js" type="text/javascript"></script>
<script src="static/ebz/sandbox/att-abs-tpls.min.js" type="text/javascript"></script>
<style>
.button--small, [class*=bg-] .button--small {
    font-size: 14px;
 };
</style>

<style>
.gridster-item-container .gridster-item-body{
bottom:0px;
}
.gridster-item-container{
min-height:50px;
}
.att-accordion {
    border-width: 0px;
}
</style>

<style>
  #myGallery {
	width: 100%;
	height: 400px;
  }
  
  #myGallery img {
	border: 2px solid #52697E;
  }
  
  a.loading {
	background: #fff url(../images/ajax_small.gif) no-repeat center;
  }
  
  .center {
    margin-left: auto;
    margin-right: auto;
  }

</style>

<script type="text/javascript" src="static/fusion/sample/js/FusionCharts.js"></script> <!--  Charts -->
<script type="text/javascript" src="static/fusion/sample/js/charts.js"></script> <!--  Charts -->

<script type="text/javascript" src="static/fusion/sample/js/slides.min.jquery.js"></script> <!-- Image Slider -->
<script type="text/javascript" src="static/fusion/sample/js/scribble.js"></script>          <!-- Scribble -->
<script type="text/javascript" src="static/fusion/sample/js/eye.js"></script>               <!-- Space Gallery -->
<script type="text/javascript" src="static/fusion/sample/js/utils.js"></script>             <!-- Space Gallery -->
<script type="text/javascript" src="static/fusion/sample/js/spacegallery.js"></script>      <!-- Space Gallery -->

 <!--  Carousel rendering -->
  <script>
  (function($){
		var initLayout = function() {
			$('#myGallery').spacegallery({loadingClass: 'loading'});
		};

		EYE.register(initLayout, 'init');
	})(jQuery)
  </script>

  <!--  Slider rendering -->
  <script>
  $(function(){
	  $('#slides').slides({
			preload: true,
			preloadImage: 'static/fusion/sample/images/loading.gif',
			play: 5000,
			pause: 10000,
			hoverPause: true,
			animationStart: function(current){
				$('.caption').animate({
					bottom:-35
				},100);
				if (window.console && console.log) {
					// example return of current slide number
					//console.log('animationStart on slide: ', current);
				};
			},
			animationComplete: function(current){
				$('.caption').animate({
					bottom:0
				},200);
				if (window.console && console.log) {
					// example return of current slide number
					//console.log('animationComplete on slide: ', current);
				};
			},
			slidesLoaded: function() {
				$('.caption').animate({
					bottom:0
				},200);
			}
		});
    });
  

  </script>

<div ng-controller="welcomeController">
	<fmt:message key="general.home" var="title" /> 
	<div>
	  	<span style="font-weight:bold;font-size:11pt;">Welcome ${sessionScope.user.firstName} ${sessionScope.user.lastName}</span>&nbsp;
	(Last Login:&nbsp;<fmt:formatDate value="${sessionScope.user.lastLoginDate}" type="date" pattern="dd MMM yyyy hh:mma zzz" var="lastLogin" /> ${lastLogin})
	</div>
	
	<div style="float:left; width:320px; height:320px; margin:10px 27px 10px 27px;color:#222222;text-shadow: 1px 1px 2px #A0A0A0;" >
	    <p style="font-weight:bold;font-size:16pt;color:#0046B8;">Network </p>
	    <p style="font-weight:bold;font-size:16pt;color:#0046B8;">Visualization </p>
	    
  	</div>
  	
  	<!-- Spacer required to center the Image slider (can also be used for content) -->
	  <div style="float:right; width:120px; height:320px; margin:10px 27px 10px 10px;color:#222222;text-shadow: 1px 1px 2px #A0A0A0;">
	    <p style="font-weight:bold;font-size:9pt;">
	      You can toggle between the Image Slider and Carousel controls by clicking on the respective radio button below:
	    </p>
	    <input type="radio" name="viewer" onClick="$('#example').show();$('#myGallery').hide();" value="Slider" />Slider<br/>
	    <input type="radio" name="viewer" onClick="$('#example').hide();$('#myGallery').show();" value="Carousel" />Carousel 
		
	    <br/>
	  </div>
  	
  	  <div id="container">

	  	<div style="position:relative">
	  		<div id="myGallery" class="spacegallery" style="position:static">
		      <img src="static/fusion/sample/images/carousel/slide_b_drive_test_map.png" alt="Drive test analytics" />
		      <img src="static/fusion/sample/images/carousel/slide_b_ios_throughput.png" alt="MTSA - Nationwide DL throughput for iOS devices over cellular network" />
		      <img src="static/fusion/sample/images/carousel/slide_b_eppt_county.png" alt="Location based services county level drive test" />
			  <img src="static/fusion/sample/images/carousel/slide_b_lata_map.png" alt="Network demand data by LATA (2020 forecast)" />
			  <img src="static/fusion/sample/images/carousel/slide_b_eppt_regression.png" alt="Linear regression prediction of LBS/E911 drive test accuracy" />
			  <img src="static/fusion/sample/images/carousel/slide_b_nova_sdn_map.png" alt="SDN Simulator - SNRC Traffic" />
		      <span style="float:right; color:#222222;">(Click on Image to Rotate)</span>
			</div>
		</div>
		<div id="example">
		  <div id="slides">
			<div class="slides_container">
			  <div class="slide">
				<a href="#" title="Drive test analytics"><img src="static/fusion/sample/images/carousel/slide_b_drive_test_map.png" width="570" height="270" alt="Drive test analytics"></a>
				<div class="caption">
				  <p>Drive test analytics</p>
				</div>
			  </div>
			  <div class="slide">
				<a href="doclib.htm" title="MTSA - Nationwide DL throughput for iOS devices over cellular network"><img src="static/fusion/sample/images/carousel/slide_b_ios_throughput.png" width="570" height="270" alt="MTSA - Nationwide DL throughput for iOS devices over cellular network"></a>
				<div class="caption">
				  <p>Nationwide DL throughput for iOS devices over cellular network</p>
				</div>
			  </div>
			  <div class="slide">
				<a href="#" title="Network demand data by LATA (2020 forecast)"><img src="static/fusion/sample/images/carousel/slide_b_lata_map.png" width="570" height="270" alt="Network demand data by LATA (2020 forecast)"></a>
				<div class="caption">
				  <p>Network demand data by LATA (2020 forecast)</p>
				</div>
			  </div>
			  <div class="slide">
				<a href="#" title="Location based services county level drive test"><img src="static/fusion/sample/images/carousel/slide_b_eppt_county.png" width="570" height="270" alt="Location based services county level drive test"></a>
				<div class="caption">
				  <p>Location based services county level drive test</p>
				</div>
			  </div>
			  <div class="slide">
				<a href="broadcast_list.htm" title="SDN Simulator - SNRC Traffic"><img src="static/fusion/sample/images/carousel/slide_b_nova_sdn_map.png" width="570" height="270" alt="SDN Simulator - SNRC Traffic"></a>
				<div class="caption">
				  <p>SDN Simulator - SNRC Traffic</p>
				</div>
			  </div>
			  <div class="slide">
				<a href="#" title="Linear regression prediction of LBS/E911 drive test accuracy"><img src="static/fusion/sample/images/carousel/slide_b_eppt_regression.png" width="570" height="270" alt="Linear regression prediction of LBS/E911 drive test accuracy"></a>
				<div class="caption" style="bottom:0">
				  <p>Linear regression prediction of LBS/E911 drive test accuracy</p>
				</div>
			  </div>
			</div>
			<a href="#" class="prev"><img src="static/fusion/sample/images/arrow-prev.png" width="24" height="43" alt="Arrow Prev"></a>
			<a href="#" class="next"><img src="static/fusion/sample/images/arrow-next.png" width="24" height="43" alt="Arrow Next"></a>
		  </div>
		  <img src="static/fusion/sample/images/example-frame.png" width="739" height="341" alt="Example Frame" id="frame">
		</div>
	  </div>
  		
  	<center>
        <div class="gridster-container">
            <div att-gridster att-gridster-options='gridsterOpts'>
                <div att-gridster-item='item' ng-repeat="item in standardItems">
                    <div att-gridster-item-header 
                         header-text={{item.headerText}} 
                         sub-header-text={{item.subHeaderText}}>
                            <!--ICON BUTTONS PLACEHOLDER START-->
                            <div class="tileMinMaxBtn" ng-click="toggleMinMax($index,'')">
								<span class="tileMinMaxIcon">
									<i	class="icon-chevron-up" style="color:gray"  ng-show="item.max"></i>
									<i	class="icon-chevron-down" style="color:gray"  ng-hide="item.max"></i>
								</span>
							</div>
                            <!--ICON BUTTONS PLACEHOLDER END-->
                    </div>
               	 	<div att-gridster-item-body >
		                <!--ACTUAL BODY CONTENT START-->
			                <div align="center" style="margin-top:10px;">
			               		<div align="left" ng-if="item.headerText=='Dashboard' && item.max">
			               			<label>&nbsp; Sample Charts</label><BR>
					              	<iframe scrolling="no" frameBorder="0" style="width: 430px; height: 360px;" src="static/fusion/sample/html/wordcloud.html"></iframe>
			               		</div>
			               		<div ng-if="item.headerText=='Donut Chart' && item.max">
					              	<iframe scrolling="no" frameBorder="0"  style="width: 310px; height: 210px;" src="static/fusion/sample/html/donut_d3.html"></iframe>
			               		</div>
			               		<div ng-if="item.headerText=='Area Chart' && item.max">
					              	<iframe scrolling="no" frameBorder="0"  style="width: 310px; height: 210px;" src="static/fusion/sample/html/area_chart.html"></iframe>
			               		</div>
			               		<div ng-if="item.headerText=='Pie Chart' && item.max">
					              	<iframe scrolling="no" frameBorder="0"  style="width: 310px; height: 210px;" src="static/fusion/sample/html/pie_chart.html"></iframe>
			               		</div>
			               		<div ng-if="item.headerText=='Line Chart' && item.max">
			               			<iframe scrolling="no" frameBorder="0"  style="width: 310px; height: 210px;" src="static/fusion/sample/html/line_chart.html"></iframe>
			               		</div>
			               		<div ng-if="item.headerText=='Gauges' && item.max">
					              	<iframe scrolling="no" frameBorder="0"  style="width: 310pxx; height: 210px;" src="static/fusion/sample/html/d3_gauges_demo.html"></iframe>
			               		</div>
			               		
			               		<div align="left" ng-if="item.headerText=='Traffic distribution by day of week' && item.max">
					              	<att-tabs title="gTabs" class="tabs" >
					                	<floating-tabs ng-model='activeTabId' size="small"></floating-tabs>
					    			</att-Tabs>
					    			<div>	
							        	<div id="Monday" align="left"><img src="static/fusion/sample/images/tunnels/1_mon.png" width=100% height=100% alt="Monday"></div>
										<div id="Tuesday" class="hidden" align="center"><img src="static/fusion/sample/images/tunnels/2_tue.png" width=100% height=100% alt="Tuesday"></div>
										<div id="Wednesday" class="hidden" align="center"><img src="static/fusion/sample/images/tunnels/3_wed.png" width=100% height=100% alt="Wednesday"></div>
										<div id="Thursday" class="hidden" align="center"><img src="static/fusion/sample/images/tunnels/4_thu.png" width=100% height=100% alt="Thursday"></div>
										<div id="Friday" class="hidden" align="center"><img src="static/fusion/sample/images/tunnels/5_fri.png" width=100% height=100% alt="Friday"></div>
										<div id="Saturday" class="hidden" align="center"><img src="static/fusion/sample/images/tunnels/6_sat.png" width=100% height=100% alt="Saturday"></div>
										<div id="Sunday" class="hidden" align="center"><img src="static/fusion/sample/images/tunnels/7_sun.png" width=100% height=100% alt="Sunday"></div>
							        </div>
			               		</div>
			               		<div align="left" ng-if="item.headerText=='Busy hour traffic analysis by day of week' && item.max">
					              	<att-tabs title="gTabs2">
					                	<floating-tabs ng-model='activeTabId2' size="small"></floating-tabs>
					    			</att-Tabs>
					    			<div>	
							        	<div id="Incoming" align="left"><img src="static/fusion/sample/images/tunnels/BH_DLSTX_IN.png" width=100% height=100% alt="Monday"></div>
										<div id="Outgoing" class="hidden" align="center"><img src="static/fusion/sample/images/tunnels/BH_DLSTX_OUT.png" width=100% height=100% alt="Tuesday"></div>
										<div id="Default" class="hidden" align="center"><img src="static/fusion/sample/images/tunnels/BH_Nat_Def.png" width=100% height=100% alt="Wednesday"></div>
										<div id="Priority" class="hidden" align="center"><img src="static/fusion/sample/images/tunnels/BH_Nat_Priority.png" width=100% height=100% alt="Thursday"></div>
										<div id="BHNational" class="hidden" align="center"><img src="static/fusion/sample/images/tunnels/BH_Nat.png" width=100% height=100% alt="Friday"></div>
							        </div>
			               		</div>

			               		<div align="left" ng-if="item.headerText=='Additional Samples' && item.max">
					              	<label>&nbsp;Quick Links</label>				          		 
						    		<table att-table >
							    
										  <tr>
										    <td att-table-body width="90%" ><a href="" target="_blank">Contacts</a></td>
										    <td att-table-body width="10%">
										    	<a ng-click="removeRole();" ><img src="static/fusion/sample/images/deleteicon.gif"></a>
										     </td>
										  </tr>
										  <tr>
										    <td att-table-body width="90%" ><a href="" target="_blank">Developer Program</a></td>
											<td att-table-body width="10%">
										    	<a ng-click="removeRole();" ><img src="static/fusion/sample/images/deleteicon.gif"></a>
										     </td>
										  </tr>
										  <tr>
										    <td att-table-body width="90%" ><a href="http://www.zkoss.org" target="_blank">ZK Framework</a></td>
											<td att-table-body width="10%">
										    	<a ng-click="removeRole();" ><img src="static/fusion/sample/images/deleteicon.gif"></a>
										     </td>
										  </tr>
										  <tr>
										    <td att-table-body width="90%" ><a href="http://jquery.com" target="_blank">JQuery</a></td>
											<td att-table-body width="10%">
										    	<a ng-click="removeRole();" ><img src="static/fusion/sample/images/deleteicon.gif"></a>
										     </td>
										  </tr>
										  <tr>
										    <td att-table-body width="90%" ><a href="force_cluster.html" target="_blank">RNC Visualization</a></td>
											<td att-table-body width="10%">
										    	<a ng-click="removeRole();" ><img src="static/fusion/sample/images/deleteicon.gif"></a>
										     </td>
										  </tr>
										  <tr>
										    <td att-table-body width="90%" ><a href="sample_heat_map.htm" target="">Heat Map</a></td>
											<td att-table-body width="10%">
										    	<a ng-click="removeRole();" ><img src="static/fusion/sample/images/deleteicon.gif"></a>
										     </td>
										  </tr>
										  <tr>
										  	<td att-table-body width="90%" ><a href="sample_animated_map.htm" target="">Animated Map</a></td>
											<td att-table-body width="10%">
										    	<a ng-click="removeRole();" ><img src="static/fusion/sample/images/deleteicon.gif"></a>
										     </td>
										  </tr>
										  <tr>
										  	<td att-table-body width="90%" ><a href="jbpm_drools.htm" target="_blank">Process Management</a></td>
											<td att-table-body width="10%">
										    	<a ng-click="removeRole();" ><img src="static/fusion/sample/images/deleteicon.gif"></a>
										     </td>
										  </tr>
										  <tr>
										  	<td att-table-body width="90%" ><a href="chatRoom.htm">Chat Session</a></td>
											<td att-table-body width="10%">
										    	<a ng-click="removeRole();" ><img src="static/fusion/sample/images/deleteicon.gif"></a>
										     </td>
										  </tr>
									</table>
			               		</div>
			               		<div ng-if="item.headerText=='Sticky Notes' && item.max">
					              	<div style="width:100%; height:400px" id="scribble-pad"><pre id="scribble" contenteditable="true" onkeyup="storeUserScribble(this.id);"></pre></div>
			               		</div>
			              		<div ng-if="item.headerText=='Service Configuration' && item.max">
					              	          <accordion close-others="true" css="att-accordion">
											          <accordion-group heading="Service Configuration" is-open="group11.open"> 
													        <iframe scrolling="no" frameBorder="0" align="center" width="100%" height="400px"  src="static/fusion/sample/org_chart/example.html" ></iframe> 	
											          </accordion-group>
											          <accordion-group heading="VSP Service Configuration" is-open="group12.open">             	
									              			<iframe scrolling="no" frameBorder="0" align="center" width="100%" height="400px" src="static/fusion/sample/org_chart/example_vsp.html" ></iframe>
											          </accordion-group>
									          </accordion> 	
			               		</div>
			               		
				      	  	</div>
							
		                <!--ACTUAL BODY CONTENT END-->
                	</div>
                   <!--  <div att-gridster-item-footer 
                         att-gridster-item-footer-link={{item.footerLink}}>
                        {{item.footerLinkText}}
                    </div> -->
                </div>
            </div>
        </div>
    </center>
    
</div>
	
<script>
$(document).ready(function(){
	  $( "#rightIcon" ).hide();
		$( "#leftIcon" ).show();
});
var app=angular.module("abs", ["att.abs", "modalServices","att.gridster","checklist-model"]);
app.controller('welcomeController', function ($scope, modalService, $modal) { 

	$scope.gridsterOpts = {
		    columns: 3, // the width of the grid, in columns
		    pushing: true, // whether to push other items out of the way on move or resize
		    floating: true, // whether to automatically float items up so they stack (you can temporarily disable if you are adding unsorted items with ng-repeat)
		    width: 'auto', // can be an integer or 'auto'. 'auto' scales gridster to be the full width of its containing element
		    colWidth: 'auto', // can be an integer or 'auto'.  'auto' uses the pixel width of the element divided by 'columns'
		    rowHeight: 60, // can be an integer or 'match'.  Match uses the colWidth, giving you square widgets.
		    margins: [10, 10], // the pixel distance between each widget
		    outerMargin: true, // whether margins apply to outer edges of the grid
	        swapping: true,
	        draggable: {
	            enabled: true, // whether dragging items is supported
	            stop: function(event, uiWidget, $element) {$scope.setCookie();} // optional callback fired when item is finished dragging
	         }

		};
	
	/* $scope.gridsterOpts = {
            columns: 6,
            width: 'auto',
            colWidth: '230',
			rowHeight: '120',
			margins: [10, 10],
			outerMargin: true,
			pushing: true,
			floating: true,
            swapping: true
	        }; */
		
	
	$scope.toggleMinMax = function(index, tileName){
		if(tileName==''){
			$scope.standardItems[index].max = !$scope.standardItems[index].max;
			if($scope.standardItems[index].max)
				$scope.standardItems[index].sizeY=$scope.standardItems[index].maxHeight;
			else
				$scope.standardItems[index].sizeY=0;	
		}else{
			$scope.tileTemp = $scope.$eval(tileName);
			var tileMax = $parse(tileName+'.max');
			tileMax.assign($scope, !$scope.$eval(tileName).max);
			var tileSizeY = $parse(tileName+'.sizeY');
			if($scope.tileTemp.max)
				tileSizeY.assign($scope, $scope.tileTemp.maxHeight);
			else
				tileSizeY.assign($scope, 0);
		}
	};
		// These map directly to gridsterItem options
		    // IMPORTANT: Items should be placed in the grid in the order in which 
		    // they should appear.
		    // In most cases the sorting should be by row ASC, col ASC
		    $scope.standardItems = [{
			sizeX: 1,
			sizeY: 8,
 	        maxHeight: 8,
			row: 0,
			col: 0,
		            headerText:'Dashboard',
		            max:false
		            
		            
		},
		    {
			sizeX: 1,
			sizeY: 5,
 	        maxHeight: 5,
			row: 0,
			col: 1,
		            headerText:'Donut Chart',
		            max:false
		            
		},
		    {
			sizeX: 1,
			sizeY: 5,
 	        maxHeight: 5,
			row: 0,
			col: 2,
		            headerText:'Area Chart',
		            max:true
		},
		    {
			sizeX: 1,
			sizeY: 5,
 	        maxHeight: 5,
			row: 8,
			col: 0,
		            headerText:'Pie Chart',
		            max:false
		},
		    {
			sizeX: 1,
			sizeY: 5,
 	        maxHeight: 5,
			row: 8,
			col: 1,
		            headerText:'Line Chart',
		            max:true
		},
		    {
			sizeX: 1,
			sizeY: 5,
 	        maxHeight: 5,
			row: 8,
			col: 2,
		            headerText:'Gauges',
		            max:false
		},
		    {
			sizeX: 1,
			sizeY: 8,
 	        maxHeight: 8,
			row: 16,
			col: 0,
		            headerText:'Traffic distribution by day of week',
		            max:false
		},
		    {
			sizeX: 1,
			sizeY: 8,
 	        maxHeight: 8,
			row: 16,
			col: 1,
		            headerText:'Busy hour traffic analysis by day of week',
		            max:false
		},
		    {
			sizeX: 1,
			sizeY: 6,
 	        maxHeight: 6,
			row: 24,
			col: 0,
		            headerText:'Additional Samples',
		            max:false
		},
		    {
			sizeX: 1,
			sizeY: 8,
 	        maxHeight: 8,
			row: 24,
			col: 1,
		            headerText:'Sticky Notes',
		            max:false
		},
		    {
			sizeX: 3,
			sizeY: 10,
 	        maxHeight: 10,
			row: 32,
			col: 0,
		            headerText:'Service Configuration',
		            max:false
		}];
	
	$.each($scope.standardItems, function(i, a){ 
		    $scope.toggleMinMax(i,'');
	});
	 $scope.activeTabId = 'Monday';
	//for generic tabs
    $scope.gTabs = [{
            title: 'Monday',
            id: 'Monday',
            url: '#Monday',
            selected: true
        },{
            title: 'Tuesday',
            id: 'Tuesday',
            url: '#Tuesday'
        },{
            title: 'Wednesday',
            id: 'Wednesday',
            url: '#Wednesday'
        },{
            title: 'Thursday',
            id: 'Thursday',
            url: '#Thursday'
        },{
            title: 'Friday',
            id: 'Friday',
            url: '#Friday'
        },{
            title: 'Saturday',
            id: 'Saturday',
            url: '#Saturday'
        },{
            title: 'Sunday',
            id: 'Sunday',
            url: '#Sunday'
        }
    ];

    $scope.activeTabId2 = 'Incoming';
	//for generic tabs
    $scope.gTabs2 = [{
            title: 'BH SNRC DLSTX - Incoming',
            id: 'Incoming',
            url: '#Incoming',
            selected: true
        },{
            title: 'BH SNRC DLSTX - Outgoing',
            id: 'Outgoing',
            url: '#Outgoing'
        },{
            title: 'BH National - Default',
            id: 'Default',
            url: '#Default'
        },{
            title: 'BH National - Priority',
            id: 'Priority',
            url: '#Priority'
        },{
            title: 'BH National',
            id: 'BHNational',
            url: '#BHNational'
        }
    ];
	
    $scope.activeTabId3 = 'Incoming';
	//for generic tabs
    $scope.gTabs3 = [{
            title: 'BH SNRC DLSTX - Incoming',
            id: 'Incoming',
            url: '#Incoming',
            selected: true            
        },{
            title: 'BH SNRC DLSTX - Outgoing',
            id: 'Outgoing',
            url: '#Outgoing'
        },{
            title: 'BH National - Default',
            id: 'Default',
            url: '#Default'
        },{
            title: 'BH National - Priority',
            id: 'Priority',
            url: '#Priority'
        },{
            title: 'BH National',
            id: 'BHNational',
            url: '#BHNational'
        }
    ];

	/* $scope.$watch('activeTabId', function(newVal) {
		alert(newval);
		$('#'+newval).show();
	}, true); */
	
	$scope.toggleEastToWest = function() {
		$( "#toggle" ).toggle( 'slide');	
		if ($("#leftIcon").is(":visible")) {
			$( "#rightIcon" ).show();
			$( "#leftIcon" ).hide();
	    } 
		else if ($("#rightIcon").is(":visible")) {
	    	$( "#rightIcon" ).hide();
			$( "#leftIcon" ).show();
	    }
	};
	
	$scope.group1 = {
            open: true
        };
	$scope.group2 = {
            open: true
        };
	$scope.group3 = {
            open: true
        };
	$scope.group4 = {
            open: true
        };
	$scope.group5 = {
            open: true
        };
	$scope.group6 = {
            open: true
        };	
	$scope.group7 = {
            open: true
        };
	$scope.group71 = {
            open: true
        };
	$scope.group8 = {
            open: true
        };
	$scope.group9 = {
            open: true
        };
	$scope.group10 = {
            open: true
        };
	$scope.group11 = {
            open: true
        };
	$scope.group12 = {
            open: false
        };
});
</script>

  <!-- Select the Slider control by default -->
  <script>$('input[name=viewer]:eq(1)').click();</script>
