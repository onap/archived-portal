/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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
 * 
 */
'use strict';

(function () {
    class CommonWidgetController {
        constructor(dashboardService, $scope, message, $q, $http, conf, $filter,confirmBoxService,$log) {
        	$scope.widgetType = message.type;
        	$scope.widgetTypeDisplay = message.type;
        	$scope.modflag = false;
        	
        	switch (message.type) {
            case 'EVENTS':
            	$scope.widgetTypeDisplay = 'Events'
                break;
            case 'NEWS':
            	$scope.widgetTypeDisplay = 'News'
                break;
            case 'IMPORTANTRESOURCES':
            	$scope.widgetTypeDisplay = 'Resources'
                break;
        	}
 
        	$scope.widgetData = [];
	        dashboardService.getCommonWidgetData(message.type)
	        .then(function (res) {
	        	 // console.log('CommonWidgetController: result is ' + res);
	        	 $scope.widgetData = res.response.items;
	         });
                 
	         
	         
	        $scope.setEdit = function(index) {
	        	
	        	//for(var i=0; i<$scope.widgetData )
	        	
	        	if($scope.modflag === false){
	        		$scope.widgetData[index].showEdit = true;
		        	$scope.modflag = true;
	        	}
	         }	         
        	
	         $scope.modify = function(index) {
        		
        		$scope.widgetObject = {};
        		$scope.widgetObject.id = $scope.widgetData[parseInt(index)].id;
        		$scope.widgetObject.category = $scope.widgetData[parseInt(index)].category;
        		$scope.widgetObject.title = $scope.widgetData[parseInt(index)].title;
        		$scope.widgetObject.href = $scope.widgetData[parseInt(index)].href;
        		$scope.widgetObject.eventDate = $scope.widgetData[parseInt(index)].eventDate;
        		$scope.widgetObject.content = $scope.widgetData[parseInt(index)].content;
        		$scope.widgetObject.sortOrder = $scope.widgetData[parseInt(index)].sortOrder;
        		
        		var validateMsg = $scope.validateWidgetObject($scope.widgetObject);
    			if (validateMsg) {
    				confirmBoxService.showInformation(validateMsg).then(isConfirmed => {return;});
    				return;
    			}
    			
    			dashboardService.saveCommonWidgetData($scope.widgetObject)
    			.then(function(res){
    				if (res.status == 'OK') {
    					dashboardService.getCommonWidgetData(message.type)
    					.then(function(res){
    						$scope.widgetData = res.response.items;
    						$scope.modflag = false;
    						$scope.cancelEdit(index);
    					});
    				}
    				else {
    					// Save failed
    					confirmBoxService.showInformation("Save failed. " +res.message).then(isConfirmed => {return;});
    					return;
    				}
    			});   

        	};	
        	
        	$scope.newWidgetObject = {};
        	
        	// Answers string error if validation fails; 
        	// answers null if all is well.
        	$scope.validateWidgetObject = function(wo) {
        		if (wo.title == null || wo.title == '')
        			return "Please enter a title.";
        		if (wo.href == null || wo.href == '' || !validateUrl(wo.href))
        			return "Please enter a valid URL that starts with http.";
        		if (wo.sortOrder == null || wo.sortOrder == '' || isNaN(parseInt(wo.sortOrder)))
        			return "Please enter a number for the sort order.";
    			if (wo.category=='EVENTS') {
    				if (wo.eventDate == null || wo.eventDate == '')
    					return "Please enter a date for the event.";
    				// Parses and normalizes the date with rollover.
    				var filteredDate = $filter('date')(wo.eventDate, "yyyy-MM-dd");
    				// $log.debug('dashboard-widget-controller: date filter yields ' + filteredDate);
    				// The date picker shows mm/dd/yy.
    				if (filteredDate == null || filteredDate.length != 10)
    					return "Please enter a valid date as YYYY-MM-DD";
    				if (wo.content==null || wo.content=='')
    					return "Please enter content for the event.";
    			}
    			return null;
    		};
        	
        	$scope.saveNew = function() {
        		$scope.newWidgetObject.category = message.type;
        		// $log.info($scope.newWidgetObject);
        		var validateMsg = $scope.validateWidgetObject($scope.newWidgetObject);
    			if (validateMsg) {
    				confirmBoxService.showInformation(validateMsg).then(isConfirmed => {return;});
    				return;
    			}
    			// Transform date into expected storage format
    			$scope.newWidgetObject.eventDate = $filter('date')($scope.newWidgetObject.eventDate, "yyyy-MM-dd");

    			dashboardService.saveCommonWidgetData($scope.newWidgetObject)
        		.then(function(res){
        			if (res.status == 'OK') {  
        				confirmBoxService.showInformation('You have added a new item').then(isConfirmed => {
        				});
        				dashboardService.getCommonWidgetData(message.type)
        				.then(function(res){
        					$scope.widgetData = res.response.items;
        					$scope.newWidgetObject = {};
        			});  
        			}
        			else {
        				confirmBoxService.showInformation("Save failed").then(isConfirmed => {return;});
        				return;
        			}        			
        		});        		
        	};
        	
        	$scope.remove = function(index) {
        		var confirmMsg = 'Are you sure you want to delete this item from the list?' + ' Press OK to delete.';
        		confirmBoxService.confirm(confirmMsg).then(function (confirmed) {
                    if (confirmed == true) { 
                    	$scope.widgetObject = {};
                		$scope.widgetObject.id = $scope.widgetData[parseInt(index)].id;
                		$scope.widgetObject.category = $scope.widgetData[parseInt(index)].category;
                		$scope.widgetObject.title = $scope.widgetData[parseInt(index)].title;
                		$scope.widgetObject.href = $scope.widgetData[parseInt(index)].href;
                		$scope.widgetObject.sortOrder = $scope.widgetData[parseInt(index)].sortOrder;
                		
                		
                		dashboardService.removeCommonWidgetData($scope.widgetObject)
                		.then(function(res){
                			dashboardService.getCommonWidgetData(message.type)
                			.then(function(res){
                				$scope.widgetData = res.response.items;
                				$scope.cancelEdit(index);
                			});        			
                		});                           	
                    }
                });
        				
        	};	
        	$scope.closeDialog = function(){
        		$scope.closeThisDialog( $scope.widgetData);
        	}
        	$scope.deleteSpeedDial = function(widget){
        		
        	 }
        	 
        	 $scope.manage = function(index) {
        		 	$scope.modflag = true;
        		 	
        		 	var thisdata = $scope.widgetData[index];
        	      	$scope.editMode=true;
        	      	var category = "#category"+index;
        	      	var categoryEdit = "#categoryEdit"+index;
        	      	
        	      	var title = "#title"+index;
        	      	var titleEdit = "#titleEdit"+index;

        	      	var url = "#href"+index;
        	      	var urlEdit = "#hrefEdit"+index;
        	      	
        	      	var order = "#order"+index;
        	      	var orderEdit = "#orderEdit"+index;
        	  
        	    	var eventDate = "#eventDate"+index;
        	      	var eventDateEdit = "#eventDateEdit"+index;
        	  
        	      	
        	      	var dsavebutton  ="#savebutton"+index;
        	      	var dremovebutton="#removebutton"+index;
        	      	var dmanagebutton="#managebutton"+index;
        	      	var deditRbutton="#editRbutton"+index;
        	      	
        	      	$(title).css('display', 'none');
        	        $(titleEdit).css('display', 'inherit'); 
        	        $(url).css('display', 'none');
        	        $(urlEdit).css('display', 'inherit'); 
        	        $(order).css('display', 'none');
        	        $(orderEdit).css('display', 'inherit');
        	        $(eventDate).css('display', 'none');
        	        $(eventDateEdit).css('display', 'inherit');
        	        
        	        $(dsavebutton).css('display', 'inherit');
        	      	$(dremovebutton).css('display', 'inherit');
        	      	$(dmanagebutton).css('display', 'none');
        	      	$(deditRbutton).css('display', 'inherit');
        	    	
        	      };
        	      
        	   $scope.cancelEdit = function(index) {
        	    	
	       	    	$scope.editMode=false;
	    	    	var category = "#category"+index;
	    	      	var categoryEdit = "#categoryEdit"+index;
	    	      	
	    	      	var title = "#title"+index;
	    	      	var titleEdit = "#titleEdit"+index;
	
	    	      	var url = "#href"+index;
	    	      	var urlEdit = "#hrefEdit"+index;
	    	      	
	    	      	var order = "#order"+index;
	    	      	var orderEdit = "#orderEdit"+index;

	    	      	var eventDate = "#eventDate"+index;
	    	      	var eventDateEdit = "#eventDateEdit"+index;

	    	    	var dsavebutton  ="#savebutton"+index;
	    	    	var dremovebutton  ="#removebutton"+index;
	    	    	var dmanagebutton="#managebutton"+index;
	    	    	var deditRbutton="#editRbutton"+index;
	    	
	    	    	$(title).css('display', 'inherit');
	    	        $(titleEdit).css('display', 'none'); 
	    	        
	    	        $(url).css('display', 'inherit');
	    	        $(urlEdit).css('display', 'none'); 
	    	        
	    	        $(order).css('display', 'inherit');
	    	        $(orderEdit).css('display', 'none');

	    	        $(eventDate).css('display', 'inherit');
	    	        $(eventDateEdit).css('display', 'none');

	    	        $(dsavebutton).css('display', 'none');
        	       	$(dremovebutton).css('display', 'none');
        	    	$(dmanagebutton).css('display', 'inherit');
        	    	$(deditRbutton).css('display', 'none');
        	      	
        	     };
        	
        	$scope.popupConfirmWin = function(title, msgBody, callback){
            	modalService.popupConfirmWin.apply(null, arguments);
            };
        }           
    }
    CommonWidgetController.$inject = ['dashboardService', '$scope', 'message', '$q', '$http', 'conf', '$filter','confirmBoxService','$log'];
    angular.module('ecompApp').controller('CommonWidgetController', CommonWidgetController); 
})();

angular.module('ecompApp').filter('cut', function () {
    return function (value, wordwise, max, tail) {
        if (!value) return '';

        max = parseInt(max, 10);
        if (!max) return value;
        if (value.length <= max) return value;

        value = value.substr(0, max);
        if (wordwise) {
            var lastspace = value.lastIndexOf(' ');
            if (lastspace != -1) {
              //Also remove . and , so its gives a cleaner result.
              if (value.charAt(lastspace-1) == '.' || value.charAt(lastspace-1) == ',') {
                lastspace = lastspace - 1;
              }
              value = value.substr(0, lastspace);
            }
        }

        return value + (tail || ' …');
    };
});

angular.module('ecompApp').controller('DatePickerController', function ($scope, uibDateParser) {
	 	  $scope.today = function() {
		    $scope.dt = new Date();
		  };
		  $scope.today();

		  $scope.clear = function() {
		    $scope.dt = null;
		  };

		  $scope.inlineOptions = {
		    customClass: getDayClass,
		    minDate: new Date(),
		    showWeeks: true
		  };

		  $scope.dateOptions = {
		    dateDisabled: disabled,
		    formatYear: 'yy',
		    minDate: new Date(),
		    startingDay: 1
		  };

		  // Disable weekend selection
		  function disabled(data) {
		    var date = data.date,
		      mode = data.mode;
		    return mode === 'day' && (date.getDay() === 0 || date.getDay() === 6);
		  }

		  $scope.toggleMin = function() {
		    $scope.inlineOptions.minDate = $scope.inlineOptions.minDate ? null : new Date();
		    $scope.dateOptions.minDate = $scope.inlineOptions.minDate;
		  };

		  $scope.toggleMin();

		  $scope.open1 = function() {
			// console.log('In open1');
		    $scope.popup1.opened = true;
		  };

		  $scope.open2 = function() {
			// console.log('In open2');
			$scope.popup2.opened = true;
		  };

		  
		  $scope.setDate = function(year, month, day) {
		    $scope.dt = new Date(year, month, day);
		  };

		  $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
		  $scope.format = $scope.formats[3];
		  $scope.altInputFormats = ['M!/d!/yyyy'];

		  $scope.popup1 = {
		    opened: false
		  };

		  $scope.popup2 = {
		    opened: false
		  };

		  var tomorrow = new Date();
		  tomorrow.setDate(tomorrow.getDate() + 1);
		  var afterTomorrow = new Date();
		  afterTomorrow.setDate(tomorrow.getDate() + 1);
		  $scope.events = [
		    {
		      date: tomorrow,
		      status: 'full'
		    },
		    {
		      date: afterTomorrow,
		      status: 'partially'
		    }
		  ];

		  function getDayClass(data) {
		    var date = data.date,
		      mode = data.mode;
		    if (mode === 'day') {
		      var dayToCheck = new Date(date).setHours(0,0,0,0);

		      for (var i = 0; i < $scope.events.length; i++) {
		        var currentDay = new Date($scope.events[i].date).setHours(0,0,0,0);

		        if (dayToCheck === currentDay) {
		          return $scope.events[i].status;
		        }
		      }
		    }

		    return '';
		  }
});



function toggleCollapsible(id, clickedIconId, subtitutingIconId){
	$("#"+id).toggle();
	$("#"+clickedIconId).toggle();
	$("#"+subtitutingIconId).toggle();
	setTimeout(function(){focusFirstEle(id);}, 1000);
}

function focusFirstEle(id){
	var focusItems = $("#"+id).find(":focusable");
	for(var i=0; i<focusItems.length; i++){
		if(!isHidden(focusItems[i])){
			var targetClassName = focusItems[i].className;
			if(targetClassName!='collapsibleArrow'){
				focusItems[i].focus();
				return;
			}
		}
	}
}     

function validateUrl(value){
    return /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i.test(value);
  }
