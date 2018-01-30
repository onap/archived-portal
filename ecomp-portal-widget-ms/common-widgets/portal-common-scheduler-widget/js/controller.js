function SchedulerCtrl($rootScope , $scope,$state,widgetsCatalogService,$log,schedulerService,$filter,confirmBoxService,userProfileService,conf,$interval,$compile) {
			/****define fields****/
			var pollpromise;
			/*Assign the data that's passed to scheduler UI*/
			$scope.hasParentData = true;
			$rootScope.schedulerID = '';
			$scope.orgUserId="";
			$scope.policys = [];
			$scope.selectedPolicy={policyName:"",policyConfig:""};
			$scope.scheduler = {};
			$scope.schedulingInfo = {};
			$scope.timeSlots = [];
			$scope.changeManagement = {};
            $rootScope.schedulerForm = {
            	checkboxSelection : 'false',
        		fromDate:'',
        		toDate:'',
        		duration:'',
        		fallbackDuration:'',
        		concurrencyLimit:''
            };
            
            $scope.vnfNames = [];
            $scope.vnfTypes = [];
            $scope.schedulerObj = {};
			
			var tomorrow = new Date();
			tomorrow.setDate(tomorrow.getDate() + 1);
            $scope.minDate = tomorrow.toISOString().substring(0, 10);

    		
			/*form validation*/
			$scope.durationEmpty=false;
			$scope.concurrencyLimitEmpty = false;
			$scope.fallBackDurationEmpty=false;
			$scope.fromDateEmpty = false;
			$scope.toDateEmpty=false;

			/*interval values for getting time slots*/
			var hasvaluereturnd = true; 
			var hasthresholdreached = false; 
			var thresholdvalue =10; // interval threshold value				
			
			$scope.timeUnit= [
				{text: 'HOURS'},
				{text: 'MINUTES'},
				{text: 'SECONDS'}
			];
		

			/***** Functions for modal popup ******/ 
			$scope.radioSelections=function (){
				if( $rootScope.schedulerForm.checkboxSelection=="true"){
					$rootScope.schedulerForm.fromDate='';
					$rootScope.schedulerForm.toDate=''
				}
			}
			
			/*Dropdown update: everytime values in dropdown chagnes, update the selected value*/
			$scope.$watch('selectedPolicy.policyName', (newVal, oldVal) => {
	            for (var i = 0; i < $scope.policys.length; i++) 
	                if ($scope.policys[i].policyName == newVal) 
	                    $scope.selectedPolicy = angular.copy($scope.policys[i]);;            
	        });
			
			$scope.$watch('selectedTimeUint.text', (newVal, oldVal) => {
	            for (var i = 0; i < $scope.timeUnit.length; i++) 
	                if ($scope.timeUnit[i].text == newVal) 
	                	$scope.selectedTimeUint = angular.copy($scope.timeUnit[i]);;
	        });
			
			/**
			 * This function is to validate and check if the input is a valid date. 
			 * There are two checkers in this function:
			 * Check 1: the input is a valid date object,return true, return false otherwise.
			 * Check 2: check if the input has the format of MM/DD/YYYY or M/D/YYYY and is a valid date value. 
			 * @param  dateInput
			 * @return true/false
			 */	
			$scope.isDateValid = function(dateInput) {
				/*Check 1: see if the input is able to convert into date object*/
				if ( Object.prototype.toString.call(dateInput) === "[object Date]" ) 
					return true;
				/*Check 2: see if the input is the date format MM/DD/YYYY */
				var isDateStrFormat = false;
				try{
					/*check the format of MM/DD/YYYY or M/D/YYYY */
					var startDateformat = dateInput.split('/');
					if (startDateformat.length != 3) 
						return false; 
					var day = startDateformat[1];
					var month = parseInt(startDateformat[0])-1;
					var year = startDateformat[2];
					if (year.length != 4) 
						return false;
					/*check the input value and see if it's a valid date*/
					var composedDate = new Date(year, month, day);
					if(composedDate.getDate() == day && composedDate.getMonth() == month && composedDate.getFullYear() == year)
						isDateStrFormat = true
					else
						isDateStrFormat =false;
				}catch(err){
					return false;
				}
				return isDateStrFormat;
			};

			/**
			 * This function is to check whether the input date is greater than current date or not.  
			 * @param  date
			 * @return true/false
			 */			
			$scope.isStartDateValidFromToday = function (date) {
				if(!$scope.isDateValid(date))
					return false;
				var startDate = new Date(date);
                var currentDate = new Date();
                if(startDate<=currentDate)
					return false;
				return true;
            };
			
			/**
			 * This function is to check whether the input to date is greater than input from date.  
			 * @param  fromDate , toDate
			 * @return true/false
			 */	
			$scope.isToDateGreaterFromDate = function (fromDate,toDate) {
				if(!$scope.isDateValid(fromDate) || !$scope.isDateValid(toDate))
					return false; 	
                var fromDateObj = new Date(fromDate);
                var toDateObj = new Date(toDate);
                if(toDateObj<=fromDateObj)
                	return false;        
                return true;
            };
			
			/**
			 * This function is to get error message from the input json response object.  
			 * @param  response , method
			 * @return errorMsg
			 */				
			$scope.parseErrorMsg = function(response, method){
				var errorMsg = '';
				if(response.entity){
					try{
						var entityJson = JSON.parse(response.entity);
						if(entityJson){
							errorMsg = entityJson.requestError.text;
						}	
					}catch(err){
						$log.error('SchedulerCtrl::' + method +'  error: ' + err);
					}
				}
				return errorMsg;
			}
			/***** Scheduler UI functions *****/

			/* This function is to send scheduler task approval to scheduler microservice.  */	
			$scope.submit = function () {
				$rootScope.showSpinner =true;
				
				var approvalDateTime = new Date($scope.timeSlots[0].startTime);
				$scope.schedulingInfo={
					scheduleId: $rootScope.schedulerID,
					approvalDateTime:approvalDateTime.toISOString(),
					approvalUserId:$scope.orgUserId,
					approvalStatus:$scope.schedulerObjConst.approvalSubmitStatus,
					approvalType: $scope.schedulerObjConst.approvalType
				}
				var approvalObj= JSON.stringify($scope.schedulingInfo)
				schedulerService.postSubmitForApprovedTimeslots(approvalObj).then(response => {
					if(response.status>=200 && response.status<=204){
						confirmBoxService.showInformation("Successfully Sent for Approval").then(isConfirmed => {});
					}else{
						var errorMsg = $scope.parseErrorMsg(response, 'postSubmitForApprovedTimeslots');		
						confirmBoxService.showInformation("Failed to Send for Approval "+ errorMsg).then(isConfirmed => {
							$scope.closeModal();
						});
					}
				}).catch(err => {
					$log.error('SchedulerCtrl::postSubmitForApprovedTimeslots error: ' + err);
					var errorMsg = '';
					if(err.data)
						errorMsg = $scope.parseErrorMsg(err.data, 'postSubmitForApprovedTimeslots');	
					else
						errorMsg = err;
					confirmBoxService.showInformation("There was a problem sending Schedule request. " + errorMsg).then(isConfirmed => {
						$scope.closeModal();
					});
				}).finally(() => {
					$rootScope.showSpinner = false;
				});
			};

			/* This function is to send scheduler task rejection to scheduler microservice.  */	
			$scope.reject = function () {
				$rootScope.showSpinner =true;
				var approvalDateTime = new Date($scope.timeSlots[0].startTime);
				$scope.schedulingInfo={
					scheduleId: $rootScope.schedulerID,
					approvalDateTime:approvalDateTime.toISOString(),
					approvalUserId:$scope.orgUserId,
					approvalStatus: $scope.schedulerObjConst.approvalRejectStatus,
					approvalType: $scope.schedulerObjConst.approvalType
				}
				var approvalObj= JSON.stringify($scope.schedulingInfo)
				schedulerService.postSubmitForApprovedTimeslots(approvalObj).then(response => {
					if(response.status>=200 && response.status<=299){
						confirmBoxService.showInformation("Successfully Sent for Reject").then(isConfirmed => {});
					}else{
						var errorMsg = $scope.parseErrorMsg(response, 'postSubmitForApprovedTimeslots');		
						confirmBoxService.showInformation("Failed to Send for Reject "+ errorMsg).then(isConfirmed => {});
					}
				}).catch(err => {
					$log.error('SchedulerCtrl::postSubmitForApprovedTimeslots error: ' + err);
					var errorMsg = '';
					if(err.data)
						errorMsg = $scope.parseErrorMsg(err.data, 'postSubmitForApprovedTimeslots');	
					else
						errorMsg = err;
					confirmBoxService.showInformation("There was a problem rejecting Schedule request. " + errorMsg).then(isConfirmed => {
						$scope.closeModal();
					});
				}).finally(() => {
					$rootScope.showSpinner = false;
				});
			};

			/* This function is to send policy config and receive scheduler Id.  */	
			function sendSchedulerReq(){
				$scope.timeSlots=[];
				$scope.timeSlots.length=0;
				$scope.schedulerObj.userId=$scope.orgUserId;   
				$scope.schedulerObj.domainData[0].WorkflowName=$scope.vnfObject.workflow;
				$scope.schedulerObj.schedulingInfo.normalDurationInSeconds=convertToSecs($rootScope.schedulerForm.duration)
				$scope.schedulerObj.schedulingInfo.additionalDurationInSeconds=convertToSecs($rootScope.schedulerForm.fallbackDuration)
				$scope.schedulerObj.schedulingInfo.concurrencyLimit=parseInt($rootScope.schedulerForm.concurrencyLimit)
				
				$scope.schedulerObj.schedulingInfo['vnfDetails'][0].groupId=$scope.schedulerObjConst.groupId;				
				$scope.schedulerObj.schedulingInfo['vnfDetails'][0].node=getVnfData($scope.vnfObject.vnfNames);	
				for(var i=0;i<$scope.policys.length;i++){
					if($scope.policys[i].policyName == $scope.selectedPolicy.policyName){
						try{						
							var config = $scope.policys[i].config;
							var configJson = JSON.parse(config);
							$scope.selectedPolicy.policyConfig = configJson.policyName;
						}catch(err){
							confirmBoxService.showInformation("There was a problem setting Policy config. Please try again later. " + err).then(isConfirmed => {
								$scope.closeModal();
							});
							return;
						}								
					}							
				}
				$scope.schedulerObj.schedulingInfo.policyId=$scope.selectedPolicy.policyConfig; 								
				var changeWindow=[{
					startTime:$filter('date')(new Date($rootScope.schedulerForm.fromDate), "yyyy-MM-ddTHH:mmZ", "UTC"),
					endTime:$filter('date')(new Date($rootScope.schedulerForm.toDate), "yyyy-MM-ddTHH:mmZ", "UTC")
				}];
				$scope.schedulerObj.schedulingInfo['vnfDetails'][0].changeWindow=changeWindow;
				
				if($rootScope.schedulerForm.checkboxSelection=="true"){               //When Scheduled now we remove the changeWindow
					delete $scope.schedulerObj.schedulingInfo['vnfDetails'][0].changeWindow;
				}
				var requestScheduler=  JSON.stringify($scope.schedulerObj)
				$rootScope.showSpinner = true;
				schedulerService.getStatusSchedulerId(requestScheduler).then(response => {
					
					var errorMsg = '';
					if(response && response.entity!=null){
						var errorMsg = $scope.parseErrorMsg(response, 'getStatusSchedulerId');		
						confirmBoxService.showInformation("There was a problem retrieving scheduler ID. Please try again later. " + errorMsg).then(isConfirmed => {
							$scope.closeModal();
						});

					}else{
						if(response && response.uuid){
							$rootScope.schedulerID = response.uuid;
							var scheduledID= JSON.stringify({scheduleId:$rootScope.schedulerID});              	 
							$scope.seviceCallToGetTimeSlots();
						}else{
							confirmBoxService.showInformation("There was a problem retrieving scheduler ID. Please try again later. " + response).then(isConfirmed => {

							});
						}
						
						
					}
				}).catch(err => {
					$rootScope.showSpinner = false; 
                    $log.error('SchedulerCtrl::getStatusSchedulerId error: ' + err);
					var errorMsg = '';
					if(err.data)
						errorMsg = $scope.parseErrorMsg(err.data, 'getStatusSchedulerId');	
					else
						errorMsg = err;
					confirmBoxService.showInformation("There was a problem retrieving scheduler ID. Please try again later." + errorMsg).then(isConfirmed => {
						$scope.closeModal();
					});
                }).finally(() => {
					$rootScope.showSpinner = false;
				});
			}

			/* This function is to get time slots from SNIRO  */	
			$scope.seviceCallToGetTimeSlots = function(){
				$rootScope.showTimeslotSpinner = true;
				schedulerService.getTimeslotsForScheduler($rootScope.schedulerID).then(response => {	
					if($rootScope.schedulerForm.checkboxSelection=="false"){
						if(response.entity && JSON.parse(response.entity).schedule){ //received the timeslots
							var entityJson = JSON.parse(response.entity);
							var scheduleColl=JSON.parse(entityJson.schedule);
							if(scheduleColl.length>0){
								$scope.timeSlots =scheduleColl;
								hasvaluereturnd = false;
								$rootScope.showTimeslotSpinner = false; 
								$scope.stopPoll();
								confirmBoxService.showInformation(entityJson.scheduleId +" Successfully Returned TimeSlots.").then(isConfirmed => {});
							}else
								confirmBoxService.showInformation("No time slot available").then(isConfirmed => {
									$scope.closeModal();
								});
						}else{ // do polling 
							if($scope.timeSlots.length==0 && hasthresholdreached==false){
								var polltime=$scope.schedulerObjConst.getTimeslotRate*1000;
								pollpromise= poll(polltime, function () {
									if($scope.timeSlots.length==0){
										hasvaluereturnd = true;                          
										$scope.seviceCallToGetTimeSlots()
									}else
										hasvaluereturnd = false;		
								});
							} else {
								if($rootScope.showTimeslotSpinner === true){
									$rootScope.showTimeslotSpinner = false;
									hasthresholdreached = false;
									confirmBoxService.showInformation("Failed to get time slot - Timeout error. Please try again later").then(isConfirmed => { 
										$scope.closeModal();
									});
								}
							}
						}
					}else{
						if(response.entity){
							$rootScope.showTimeslotSpinner = false; 
							if($rootScope.schedulerForm.checkboxSelection=="false")
								confirmBoxService.showInformation("Schedule ID :" + response.entity.scheduleId +" is ready to schedule.").then(isConfirmed => {});	
							else{
								var entityObj = JSON.parse(response.entity);
								confirmBoxService.showInformation("ID :" + entityObj.scheduleId +" is successfully sent for Approval").then(isConfirmed => {
									$scope.closeModal();
								});	
							}		
						}
					}
				}).catch(err => {
					$log.error('SchedulerCtrl::seviceCallToGetTimeSlots error: ' + err);
					$rootScope.showTimeslotSpinner = false; 
					confirmBoxService.showInformation("There was a problem retrieving time slows. Please try again later.").then(isConfirmed => {
						$scope.closeModal();
					});
				})
			}
			
			
			$scope.closeModal = function(){		
				setTimeout(function(){ $rootScope.closeModal(); }, 500);
			}
			
			/* This function is to get policy list from policy microservice */	
			$scope.getPolicy = function(){
			     schedulerService.getPolicyInfo().then(res =>{
			    	 if(res==null || res=='' || res.status==null || !(res.status>=200 && res.status<=299)){
	                    $log.error('SchedulerWidgetCtrl::getPolicyInfo caught error', res);
						var errorMsg = $scope.parseErrorMsg(res, 'getPolicy');		
						confirmBoxService.showInformation('There was a problem retrieving ploicy. Please try again later. ' + errorMsg).then(isConfirmed => {
							$scope.closeModal();
						});
			    	 }else
			    		$scope.policys = res.entity;
			     });
			}
			
			$scope.removeXMLExtension = function(str){
				return str.replace(".xml","");
			};
			/* Find Button */
			$scope.schedule = function () {		
				if($scope.formValidation())
					sendSchedulerReq();
			};
			
			/*************utility functions**************/
			
			function convertToSecs(number){
				var totalSecs;
				if($scope.selectedTimeUint.text === 'HOURS'){
					totalSecs=number * 3600;
				} else if($scope.selectedOption === 'MINUTES') {
					totalSecs=number * 60;
				} else {
					totalSecs=number;
				}
				return totalSecs;
			}

			function poll(interval, callback) {
				return $interval(function () {
					if (hasvaluereturnd)   //check flag before start new call
						callback(hasvaluereturnd);				
					thresholdvalue = thresholdvalue - 1;  //Decrease threshold value 
					if (thresholdvalue == 0) 
						$scope.stopPoll(); // Stop $interval if it reaches to threshold				
				}, interval)
			}

			// stop interval.
			$scope.stopPoll = function () {
				$interval.cancel(pollpromise);
				thresholdvalue = 0;     //reset all flags. 
				hasvaluereturnd = false;
				hasthresholdreached=true;
				$rootScope.showSpinner = false;
			}

			function getVnfData(arrColl){
				var vnfcolletion=[];
				for(var i=0;i<arrColl.length;i++)
					vnfcolletion.push(arrColl[i].name);				
				return vnfcolletion
			}

			function extractChangeManagementCallbackDataStr(changeManagement) {
				var result = {};
				result.requestType = changeManagement.workflow;
				result.requestDetails = [];
				_.forEach(changeManagement.vnfNames, function (vnfName) {
					if (vnfName && vnfName.version) {
						if (vnfName.selectedFile) {
							vnfName.version.requestParameters.userParams = vnfName.selectedFile;
						}
						result.requestDetails.push(vnfName.version)
					}
				});
				return JSON.stringify(result);
			}

			
			$scope.constructScheduleInfo = function(){
	            var callbackData = extractChangeManagementCallbackDataStr($scope.vnfObject);
				$scope.schedulerObj = {
	            		domain: $scope.schedulerObjConst.domain,
	            		scheduleId: '',
	            		scheduleName: $scope.schedulerObjConst.scheduleName,
	            		userId: '',
	            		domainData: [{
	            			'WorkflowName':  $scope.schedulerObjConst.WorkflowName,
	            			'CallbackUrl': $scope.schedulerObjConst.CallbackUrl,
	            			'CallbackData': callbackData
	            		}],
	            		schedulingInfo: {
	            			normalDurationInSeconds: '',
	            			additionalDurationInSeconds: '',
	            			concurrencyLimit: '',
	            			policyId: '',
	            			vnfDetails: [
	            				{
	            					groupId: "",
	            					node: [],
	            					changeWindow: [{
	            						startTime: '',
	            						endTime: ''
	            					}]
	            				}
	            			]
	            		},
		            }
			}
			
			$scope.formValidation = function(){
				$scope.durationEmpty=false;
				$scope.concurrencyLimitEmpty = false;
				$scope.fallBackDurationEmpty=false;
				$scope.fromDateGreater=false;
				$scope.fromDateEmpty=false;
				$scope.toDateEmpty=false;
				if($rootScope.schedulerForm.duration=='')
					$scope.durationEmpty=true;
				if($rootScope.schedulerForm.fallbackDuration=='')
					$scope.fallBackDurationEmpty=true;	
				if($rootScope.schedulerForm.concurrencyLimit=='')
					$scope.concurrencyLimitEmpty = true;
				if(!($rootScope.schedulerForm.fromDate instanceof Date))
					$scope.fromDateEmpty=true;
				if(!($rootScope.schedulerForm.toDate  instanceof Date ))
					$scope.toDateEmpty=true;
				var fromDateObj = new Date($rootScope.schedulerForm.fromDate);
				var toDateObj = new Date($rootScope.schedulerForm.toDate);				
				if(fromDateObj>toDateObj)
					$scope.fromDateGreater = true;					
				if($scope.durationEmpty||$scope.fallBackDurationEmpty ||$scope.concurrencyLimitEmpty || (($scope.fromDateEmpty || $scope.toDateEmpty) &&  $rootScope.schedulerForm.checkboxSelection=='false' ) ||$scope.fromDateGreater)
					return false;
				if($rootScope.schedulerForm.checkboxSelection == false && (!isDateValid($rootScope.schedulerForm.toDate) || !isDateValid($rootScope.schedulerForm.fromDate)))
					return false;
				if($scope.selectedPolicy.policyName=='' || $scope.selectedPolicy.policyName=='Select Policy'){
					confirmBoxService.showInformation("Policy is required").then(isConfirmed => {});
					return false;
				}
				return true;		
			}
			
			$scope.getScheduleConstant =function(){
				schedulerService.getSchedulerConstants().then(res =>{
					if(res==null || res=='' || res.status==null || res.status!="OK"){
						$log.error('SchedulerWidgetCtrl::getSchedulerConstants caught error', res);		
						confirmBoxService.showInformation('There is a problem about the Scheduler UI. Please try again later.').then(isConfirmed => {
							$scope.closeModal();
						});
					}else{
						var response = res.response;
						$scope.schedulerObjConst= {
							domain: response.domainName,
							scheduleName : response.scheduleName,
							WorkflowName : response.workflowName,
							CallbackUrl : response.callbackUrl,
							approvalType : response.approvalType,
							approvalSubmitStatus : response.approvalSubmitStatus,
							approvalRejectStatus : response.approvalRejectStatus,
							getTimeslotRate : response.intervalRate,
							policyName : response.policyName,
							groupId : response.groupId
						}
						$scope.constructScheduleInfo();
						$scope.getPolicy() // get policy items for the dropdown in the scheduler UI	
					}
				});	
			}
			
			/*This function is to get the current logged in user id*/
			$scope.getUserId = function(){
				$rootScope.showSpinner = true;
				userProfileService.getUserProfile()
				.then(profile=> {
					$scope.orgUserId = profile.orgUserId;
				}).finally(() => {
					$rootScope.showSpinner = false;
				});
			}
			
			$scope.activateThis = function(ele){
   			 	$compile(ele.contents())($scope);
   			 	$scope.$apply();
            };
			
			/** listening calls from parents **/
			$scope.$on("submit", function(events,data){
				$scope.submit();
			});

			$scope.$on("reject", function(events,data){
				$scope.reject();
			});
			
			$scope.$on("schedule", function(events,data){
				$scope.schedule();
			});

			/** init **/
            
            var init = function () {				
				$rootScope.showSpinner = false;
				$scope.selectedTimeUint=$scope.timeUnit[0];

				if($scope.$parent.parentData){	
					$scope.hasParentData = true;
					$scope.message = $scope.$parent.parentData;				
					$scope.vnfObject = $scope.message.data;
					$scope.schedulerObj = $scope.message.data;
					$scope.getUserId();
					$scope.getScheduleConstant(); //get Scheduler constants from properties file     		
				}else{
					//second approach to get data
					var isModal = $( "#scheduler-body" ).hasClass( "b2b-modal-body" );
					if(isModal){
						$scope.message = schedulerService.getWidgetData();
						if($scope.message){
							$scope.hasParentData = true;
							$scope.vnfObject = $scope.message.data;
							$scope.schedulerObj = $scope.message.data;
							$scope.getUserId();
							$scope.getScheduleConstant(); //get Scheduler constants from properties file
						}								
					}else{
						$scope.hasParentData = false;
					}
				}
			};
			
			init();
			
			
}

