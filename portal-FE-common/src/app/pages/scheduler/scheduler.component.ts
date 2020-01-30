/*
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2019 AT&T Intellectual Property. All rights reserved.
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
import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SchedulerService } from 'src/app/shared/services/scheduler/scheduler.service';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { InformationModalComponent } from 'src/app/modals/information-modal/information-modal.component';
import { UserProfileService } from 'src/app/shared/services';
import { analyzeAndValidateNgModules } from '@angular/compiler';

@Component({
  selector: 'app-scheduler',
  templateUrl: './scheduler.component.html',
  styleUrls: ['./scheduler.component.scss']
})
export class SchedulerComponent implements OnInit {

  pollpromise;
	/*Assign the data that's passed to scheduler UI*/
  hasParentData: boolean = true;
  schedulerID = '';
  orgUserId = "";
  policys = [];
  @Input() payload: any;

  ranges = [
    { id: 'now', value: 'true', labelValue: 'Now' },
    { id: 'range', value: 'false', labelValue: 'Range' }
  ];

  range = this.ranges[0].labelValue;

  selectedPolicy={
    policyName:"",
    policyConfig:""
  };

  scheduler = {};
  schedulingInfo = {};
  timeSlots = [];
  changeManagement = {};

  schedulerForm = {
    checkboxSelection : 'false',
    fromDate: null,
    toDate: null,
    duration:'',
    durationType:'',
    fallbackDuration:'',
    concurrencyLimit:'',
    policyName: ''
  };

  schedulerObjConst= {
    domain: null,
    scheduleName : '',
    WorkflowName : '',
    CallbackUrl : '',
    approvalType : '',
    approvalSubmitStatus : '',
    approvalRejectStatus : '',
    getTimeslotRate :0,
    policyName : '',
    groupId : '',
  }

  vnfNames = [];
  vnfTypes = [];
  schedulerObj = {
    domain: null,
    scheduleId: null,
    schedulingInfo: null,
    domainData: [],
    scheduleName: null,
    userId:''
  };

  vnfObject = {
    workflow: null,
    vnfNames:''
  };

  minDate: any = null;
  tomorrow: any = null

  /*form validation*/
  durationEmpty: boolean = false;
  concurrencyLimitEmpty: boolean = false;
  fallBackDurationEmpty: boolean = false;
  fromDateEmpty: boolean = false;
  toDateEmpty: boolean = false;

  /*interval values for getting time slots*/
  hasvaluereturnd: boolean  = true;
  hasthresholdreached: boolean  = false;
  thresholdvalue =10; // interval threshold value
  selectedTimeUint: any;
  showSpinner: boolean = false;
  selectedOption: any;
  fromDateGreater: any;

  timeUnit= [
    {text: 'HOURS'},
    {text: 'MINUTES'},
    {text: 'SECONDS'}
  ];

  constructor(public schedulerService : SchedulerService, public userProfileService: UserProfileService, public activeModal: NgbActiveModal,
    public ngbModal: NgbModal) { }

  ngOnInit() {
    this.tomorrow = new Date();
    this.tomorrow.setDate(this.tomorrow.getDate() + 1);
    this.minDate = this.tomorrow.toISOString().substring(0, 10);
    this.init();
  }

  /***** Functions for modal popup ******/
  radioSelections(){
    if( this.schedulerForm.checkboxSelection=="true"){
      this.schedulerForm.fromDate='';
      this.schedulerForm.toDate=''
    }
  }

  /*Dropdown update: everytime values in dropdown chagnes, update the selected value*/
  onChangeUpdatePolicyName(newVal, oldVal){
    for (var i = 0; i < this.policys.length; i++){
       if (this.policys[i].policyName == newVal) {
        this.selectedPolicy = this.policys[i];
       }
    }
  }

  onChangeUpdateTimeUnit(newVal, oldVal){
    for (var i = 0; i < this.timeUnit.length; i++){
	      if (this.timeUnit[i].text == newVal){
           this.selectedTimeUint = this.timeUnit[i];
        }
    }
  }

  /**
   * This function is to validate and check if the input is a valid date.
   * There are two checkers in this function:
   * Check 1: the input is a valid date object,return true, return false otherwise.
   * Check 2: check if the input has the format of MM/DD/YYYY or M/D/YYYY and is a valid date value.
   * @param  dateInput
   * @return true/false
   */
  isDateValid(dateInput){
    /*Check 1: see if the input is able to convert into date object*/
    if ( Object.prototype.toString.call(dateInput) === "[object Date]" )
        return true;
    /*Check 2: see if the input is the date format MM/DD/YYYY */
    var isDateStrFormat = false;
    try{
      /*check the format of MM/DD/YYYY or M/D/YYYY */
      let startDateformat = dateInput.split('/');
      if (startDateformat.length != 3)
        return false;
      let day = startDateformat[1];
      let month = parseInt(startDateformat[0])-1;
      let year = startDateformat[2];
      if (year.length != 4)
        return false;
      /*check the input value and see if it's a valid date*/
      let composedDate = new Date(year, month, day);
      if(composedDate.getDate() == day && composedDate.getMonth() == month && composedDate.getFullYear() == year)
        isDateStrFormat = true
      else
        isDateStrFormat =false;
    } catch(err){
      return false;
    }
    return isDateStrFormat;
  }

  /**
   * This function is to check whether the input date is greater than current date or not.
   * @param  date
   * @return true/false
   */
  isStartDateValidFromToday(date){
    if(!this.isDateValid(date))
      return false;
    let startDate = new Date(date);
    let currentDate = new Date();
    if(startDate<=currentDate)
      return false;

    return true;
  }

  /**
   * This function is to check whether the input to date is greater than input from date.
   * @param  fromDate , toDate
   * @return true/false
   */
  isToDateGreaterFromDate(fromDate,toDate){
    if(!this.isDateValid(fromDate) || !this.isDateValid(toDate))
    return false;
    var fromDateObj = new Date(fromDate);
    var toDateObj = new Date(toDate);
    if(toDateObj<=fromDateObj)
      return false;
    return true;
  }

  /**
   * This function is to get error message from the input json response object.
   * @param  response , method
   * @return errorMsg
   */

  parseErrorMsg(response, method){
    var errorMsg = '';
    if(response.entity){
      try{
        var entityJson = JSON.parse(response.entity);
        if(entityJson){
          errorMsg = entityJson.requestError.text;
        }
      }catch(err){
        console.log('SchedulerCtrl::' + method +'  error: ' + err);
      }
    }
		return errorMsg;
  }

  /***** Scheduler UI functions *****/

  /* This function is to send scheduler task approval to scheduler microservice.  */

  submit(){
    this.showSpinner =true;
    let approvalDateTime = new Date(this.timeSlots[0].startTime);
    this.schedulingInfo={
      scheduleId: this.schedulerID,
      approvalDateTime:approvalDateTime.toISOString(),
      approvalUserId:this.orgUserId,
      approvalStatus:this.schedulerObjConst.approvalSubmitStatus,
      approvalType: this.schedulerObjConst.approvalType
    }
    let approvalObj= JSON.stringify(this.schedulingInfo)

    this.schedulerService.postSubmitForApprovedTimeslots(approvalObj)
      .subscribe( _data => {
        let response = _data;
        if(response.status>=200 && response.status<=204){
          this.openConfirmationModal("Successfully Sent for Approval",'');
        }else{
          var errorMsg = this.parseErrorMsg(response, 'postSubmitForApprovedTimeslots');
          this.openConfirmationModal("Failed to Send for Approval ", errorMsg);
        }
        this.showSpinner = false;
      }, error => {
        console.log('SchedulerCtrl::postSubmitForApprovedTimeslots error: ' + error);
        var errorMsg = '';
        if(error.data)
          errorMsg = this.parseErrorMsg(error.data, 'postSubmitForApprovedTimeslots');
        else
          errorMsg = error;

        this.openConfirmationModal("There was a problem sending Schedule request.", errorMsg);
        this.showSpinner = false;
    });
  }

  /* This function is to send scheduler task rejection to scheduler microservice.  */

  reject(){
    this.showSpinner =true;
    let approvalDateTime = new Date(this.timeSlots[0].startTime);
    this.schedulingInfo={
      scheduleId: this.schedulerID,
      approvalDateTime:approvalDateTime.toISOString(),
      approvalUserId: this.orgUserId,
      approvalStatus: this.schedulerObjConst.approvalRejectStatus,
      approvalType: this.schedulerObjConst.approvalType
    }
    let approvalObj= JSON.stringify(this.schedulingInfo);
    this.schedulerService.postSubmitForApprovedTimeslots(approvalObj)
      .subscribe( _data => {
        let response = _data;
        if(response.status>=200 && response.status<=299){
          this.openConfirmationModal("Successfully Sent for Reject",'');
        }else{
          var errorMsg = this.parseErrorMsg(response, 'postSubmitForApprovedTimeslots');
          this.openConfirmationModal("Failed to Send for Approval ", errorMsg);
        }
        this.showSpinner = false;
      }, error => {
        console.log('SchedulerCtrl::postSubmitForApprovedTimeslots error: ' + error);
        var errorMsg = '';
        if(error.data)
          errorMsg = this.parseErrorMsg(error.data, 'postSubmitForApprovedTimeslots');
        else
          errorMsg = error;

        this.openConfirmationModal("There was a problem rejecting Schedule request. .", errorMsg);
        this.showSpinner = false;
    });
  }

  /* This function is to send policy config and receive scheduler Id.  */

  sendSchedulerReq(){
    this.timeSlots=[];
    this.timeSlots.length=0;
    this.schedulerObj.userId = this.orgUserId;
    this.schedulerObj.domainData[0].WorkflowName= this.vnfObject.workflow;
    this.schedulerObj.schedulingInfo.normalDurationInSeconds= this.convertToSecs(this.schedulerForm.duration);
    this.schedulerObj.schedulingInfo.additionalDurationInSeconds= this.convertToSecs(this.schedulerForm.fallbackDuration);
    this.schedulerObj.schedulingInfo.concurrencyLimit=parseInt(this.schedulerForm.concurrencyLimit);
    this.schedulerObj.schedulingInfo['vnfDetails'][0].groupId=this.schedulerObjConst.groupId;
    this.schedulerObj.schedulingInfo['vnfDetails'][0].node = this.getVnfData(this.vnfObject.vnfNames);

    for(var i=0;i<this.policys.length;i++){
      if(this.policys[i].policyName == this.schedulerForm.policyName){
        try{
          var config = this.policys[i].config;
          var configJson = JSON.parse(config);
          this.selectedPolicy.policyConfig = configJson.policyName;
        }catch(err){
          this.openConfirmationModal("There was a problem setting Policy config. Please try again later.", err);
          return;
        }
      }
    }


    this.schedulerObj.schedulingInfo.policyId = this.selectedPolicy.policyConfig;
    let changeWindow=[{
      startTime: new Date(this.schedulerForm.fromDate),
      endTime: new Date(this.schedulerForm.toDate)
      //startTime: $filter('date')(new Date(this.schedulerForm.fromDate), "yyyy-MM-ddTHH:mmZ", "UTC"),
      //endTime: $filter('date')(new Date(this.schedulerForm.toDate), "yyyy-MM-ddTHH:mmZ", "UTC")
    }];
    this.schedulerObj.schedulingInfo['vnfDetails'][0].changeWindow=changeWindow;
    if(this.schedulerForm.checkboxSelection=="true"){//When Scheduled now we remove the changeWindow
      delete this.schedulerObj.schedulingInfo['vnfDetails'][0].changeWindow;
    }
    let requestScheduler=  JSON.stringify(this.schedulerObj)
    this.showSpinner = true;
    this.schedulerService.getStatusSchedulerId(requestScheduler)
      .subscribe( _data => {
        let response = _data;
        let errorMsg = '';
        if(response && response.entity!=null){
          this.openConfirmationModal("There was a problem retrieving scheduler ID. Please try again later. ",'');
        }else{
          if(response && response.uuid){
            this.schedulerID = response.uuid;
            let scheduledID= JSON.stringify({scheduleId:this.schedulerID});
            this.seviceCallToGetTimeSlots();
          }else{
            this.openConfirmationModal("There was a problem retrieving scheduler ID. Please try again later ", errorMsg);
          }
        }
        this.showSpinner = false;
      }, error => {
        this.showSpinner = false;
        console.log('SchedulerCtrl::getStatusSchedulerId error: ' + error);
        let errorMsg = '';
        if(error.data)
          errorMsg = this.parseErrorMsg(error.data, 'postSubmitForApprovedTimeslots');
        else
          errorMsg = error;
        this.openConfirmationModal("There was a problem retrieving scheduler ID. Please try again later.", errorMsg);
    });
  }

  seviceCallToGetTimeSlots(){
    this.showSpinner = true;
    this.schedulerService.getTimeslotsForScheduler(this.schedulerID)
    .subscribe( _data => {
      let response = _data;
      if(this.schedulerForm.checkboxSelection=="false"){
        if(response.entity && JSON.parse(response.entity).schedule){ //received the timeslots
          let entityJson = JSON.parse(response.entity);
          let scheduleColl=JSON.parse(entityJson.schedule);
          if(scheduleColl.length>0){
            this.timeSlots =scheduleColl;
            let hasvaluereturnd = false;
            this.showSpinner = false;
            this.stopPoll();
            this.openConfirmationModal(entityJson.scheduleId +" Successfully Returned TimeSlots.",'');
          }else{
            this.openConfirmationModal("No time slot available",'');
          }
        }else{ // do polling
          if(this.timeSlots.length==0 && this.hasthresholdreached==false){
            let polltime = this.schedulerObjConst.getTimeslotRate*1000;
            let pollpromise= this.poll(polltime, function () {
            if(this.timeSlots.length==0){
                this.hasvaluereturnd = true;
                this.seviceCallToGetTimeSlots()
            }else
                this.hasvaluereturnd = false;
            });
          } else {
            if(this.showSpinner === true){
              this.showSpinner = false;
              this.hasthresholdreached = false;
              this.openConfirmationModal("Failed to get time slot - Timeout error. Please try again later",'');
            }
          }
        }
      }else{
        if(response.entity){
          this.showSpinner = false;
          if(this.schedulerForm.checkboxSelection=="false"){
             this.openConfirmationModal("Schedule ID :" + response.entity.scheduleId +" is ready to schedule.",'');
          }else{
            var entityObj = JSON.parse(response.entity);
            this.openConfirmationModal("ID :" + entityObj.scheduleId +" is successfully sent for Approval",'');
					}
        }
      }
    }, error => {
      this.showSpinner = false;
      console.log('SchedulerCtrl::seviceCallToGetTimeSlots error: ' + error);
      this.openConfirmationModal("There was a problem retrieving time slows. Please try again later.", '');
    });
  }


  getPolicy(){
    this.schedulerService.getPolicyInfo()
      .subscribe( _data => {
        let res = _data;
        if(res==null || res=='' || res.status==null || !(res.status>=200 && res.status<=299)){
          console.log('SchedulerWidgetCtrl::getPolicyInfo caught error', res);
          var errorMsg = this.parseErrorMsg(res, 'getPolicy');
          this.openConfirmationModal("There was a problem retrieving ploicy. Please try again later. ", errorMsg);
        }else{
          this.policys = res.entity;
        }
    }, error => {
        console.log('SchedulerCtrl::getStatusSchedulerId error: ' + error);
    });
  }

  removeXMLExtension(str:any){
    return str.replace(".xml","");
  }

  /* Find Button */
  schedule() {
    if(this.formValidation()){
      this.sendSchedulerReq();
    }
  }

  extractChangeManagementCallbackDataStr(changeManagement) {
    console.log(changeManagement);
    let result = {
      requestType: null,
      requestDetails: null
  };
    result.requestType = changeManagement.workflow;
    var workflowType = changeManagement.workflow;
    result.requestDetails = [];
    changeManagement.vnfNames.forEach(function (vnf: any) {

      try{
              var requestInfoData ={};
              var requestParametersData ={

              };
              if (vnf.availableVersions && vnf.availableVersions.length!=0){

                requestInfoData ={
                  source: vnf.availableVersions[0].requestInfo.source,
                                suppressRollback: vnf.availableVersions[0].requestInfo.suppressRollback,
                                requestorId: vnf.availableVersions[0].requestInfo.requestorId
                }

                if(workflowType=='Update'){
                  requestParametersData = {
                    usePreload: vnf.availableVersions[0].requestParameters.usePreload
                  }
                }else if(workflowType=="Replace"){
                  requestParametersData = {
                    rebuildVolumeGroups: vnf.availableVersions[0].requestParameters.usePreload
                  }
                }else if(workflowType=="VNF In Place Software Update"){
                  var payloadObj = {
                    'existing_software_version':changeManagement.existingSoftwareVersion,
                    'new_software_version':changeManagement.newSoftwareVersion,
                    'operations_timeout':changeManagement.operationTimeout
                  };
                  requestParametersData = {
                    payload: JSON.stringify(payloadObj)
                  }
                }else if(workflowType=="VNF Config Update"){
                  requestParametersData = {
                    payload: changeManagement.configUpdateFile
                  }
                }
              }else if(workflowType=="VNF In Place Software Update"){
                var payloadObj = {
                  'existing_software_version':changeManagement.existingSoftwareVersion,
                  'new_software_version':changeManagement.newSoftwareVersion,
                  'operations_timeout':changeManagement.operationTimeout
                };
                requestParametersData = {
                  payload: JSON.stringify(payloadObj)
                }
              }else if(workflowType=="VNF Config Update"){
                requestParametersData = {
                  payload: changeManagement.configUpdateFile
                }
              }

              if(changeManagement.testApi){
                requestParametersData['testApi'] = changeManagement.testApi;
                }



                var data = {
                    vnfName: vnf.name,
                    vnfInstanceId: vnf.id,
                    modelInfo: {
                        modelType: 'vnf',
                        modelInvariantId: vnf.properties['model-invariant-id'],
                        modelVersionId: vnf.modelVersionId,
                        modelName: vnf.properties['vnf-name'],
                        modelVersion: vnf.version,
                        modelCustomizationName: vnf.properties['model-customization-name'],
                        modelCustomizationId: vnf.properties['model-customization-id']
                    },
                    cloudConfiguration: vnf.cloudConfiguration,
                    requestInfo: requestInfoData,
                    relatedInstanceList: [],
                    requestParameters:requestParametersData
                };

                var serviceInstanceId = '';
                vnf['service-instance-node'].forEach( function (instanceNode:any) {
                    if(instanceNode['node-type'] === 'service-instance'){
                        serviceInstanceId = instanceNode.properties['service-instance-id'];
                    }
                });

      if (vnf.availableVersions && vnf.availableVersions.length!=0){
        vnf.availableVersions[0].relatedInstanceList.forEach( function (related:any) {
          var rel = related.relatedInstance;
          var relatedInstance = {
            instanceId: serviceInstanceId,
            modelInfo: {
              modelType: rel.modelInfo.modelType,
              modelInvariantId: rel.modelInfo.modelInvariantId,
              modelVersionId: rel.modelInfo.modelVersionId,
              modelName: rel.modelInfo.modelName,
              modelVersion: rel.modelInfo.modelVersion,
              modelCustomizationName: rel.modelInfo.modelCustomizationName,
              modelCustomizationId: rel.modelInfo.modelCustomizationId
            }
          };
          if (rel.vnfInstanceId)
            relatedInstance.instanceId = rel.vnfInstanceId;

          data.relatedInstanceList.push({relatedInstance: relatedInstance});
        });
      }
      }catch(err){
        console.log('SchedulerCtrl::extractChangeManagementCallbackDataStr error: ' + err);
      }

                result.requestDetails.push(data);
    });
    return JSON.stringify(result);
  }

  constructScheduleInfo(){
    let callbackData = this.extractChangeManagementCallbackDataStr(this.vnfObject);
    this.schedulerObj = {
      domain: this.schedulerObjConst.domain,
      scheduleId: '',
      scheduleName: this.schedulerObjConst.scheduleName,
      userId: '',
      domainData: [{
        'WorkflowName':  this.schedulerObjConst.WorkflowName,
        'CallbackUrl': this.schedulerObjConst.CallbackUrl,
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

  formValidation(){
    this.durationEmpty=false;
    this.concurrencyLimitEmpty = false;
    this.fallBackDurationEmpty=false;
    this.fromDateGreater=false;
    this.fromDateEmpty=false;
    this.toDateEmpty=false;
    if(this.schedulerForm.duration=='')
      this.durationEmpty=true;
    if(this.schedulerForm.fallbackDuration=='')
      this.fallBackDurationEmpty=true;
    if(this.schedulerForm.concurrencyLimit=='')
      this.concurrencyLimitEmpty = true;
    if(!(this.schedulerForm.fromDate instanceof Date))
      this.fromDateEmpty=true;
    if(!(this.schedulerForm.toDate  instanceof Date ))
      this.toDateEmpty=true;
    var fromDateObj = new Date(this.schedulerForm.fromDate);
    var toDateObj = new Date(this.schedulerForm.toDate);
    if(fromDateObj>toDateObj)
      this.fromDateGreater = true;
    if(this.durationEmpty|| this.fallBackDurationEmpty || this.concurrencyLimitEmpty || ((this.fromDateEmpty ||
      this.toDateEmpty) &&  this.schedulerForm.checkboxSelection=='false' ) ||this.fromDateGreater)
      return false;
    if(this.schedulerForm.checkboxSelection == 'false' && (!this.isDateValid(this.schedulerForm.toDate)
      || !this.isDateValid(this.schedulerForm.fromDate)))
      return false;
    if(this.selectedPolicy.policyName=='' || this.selectedPolicy.policyName=='Select Policy'){
      this.openConfirmationModal("Policy is required", '');
      return false;
    }
    return true;
  }

  /*************utility functions**************/

  convertToSecs(number){
    this.selectedTimeUint = this.schedulerForm.durationType;
    var totalSecs;
    if(this.selectedTimeUint.text === 'HOURS'){
      totalSecs=number * 3600;
    } else if(this.selectedOption === 'MINUTES') {
      totalSecs=number * 60;
    } else {
      totalSecs=number;
    }
    return totalSecs;
  }

  poll(interval, callback) {
    return setTimeout(function () {
      if (this.hasvaluereturnd)   //check flag before start new call
        callback(this.hasvaluereturnd);
        this.thresholdvalue = this.thresholdvalue - 1;  //Decrease threshold value
      if (this.thresholdvalue == 0)
        this.stopPoll(); // Stop $interval if it reaches to threshold
    }, interval)
  }

  stopPoll() {
    //this.interval.cancel(pollpromise);
    this.thresholdvalue = 0;     //reset all flags.
    this.hasvaluereturnd = false;
    this.hasthresholdreached=true;
    this.showSpinner = false;
  }

  getVnfData(arrColl){
    var vnfcolletion=[];
    for(var i=0;i<arrColl.length;i++)
      vnfcolletion.push(arrColl[i].name);
    return vnfcolletion
  }

  getScheduleConstant(){
    this.schedulerService.getSchedulerConstants()
      .subscribe( _data => {
        let res = _data;
        if(res==null || res=='' || res.status==null || res.status!="OK"){
          console.log('SchedulerWidgetCtrl::getSchedulerConstants caught error', res);
          this.openConfirmationModal('There is a problem about the Scheduler UI. Please try again later.', '');
        }else{
          let response = res.response;
          this.schedulerObjConst= {
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
          this.constructScheduleInfo();
          this.getPolicy()
        }
    }, error => {
        console.log('SchedulerCtrl::getStatusSchedulerId error: ' + error);
    });
  }

  /*This function is to get the current logged in user id*/
  getUserId(){
    this.showSpinner = true;
    this.userProfileService.getUserProfile()
      .subscribe( _data => {
        let res = _data;
        this.orgUserId = res['orgUserId'];
        this.showSpinner = false;
    }, error => {
      this.showSpinner = false;
      console.log('SchedulerCtrl::getStatusSchedulerId error: ' + error);
    });
  }

  init(){
    this.showSpinner = false;
    this.selectedTimeUint=this.timeUnit[0].text;
    this.vnfObject = this.payload['widgetData'];
    this.getUserId();
    this.getScheduleConstant();
	}

  openConfirmationModal(_title: string, _message: string) {
    const modalInfoRef = this.ngbModal.open(ConfirmationModalComponent);
    modalInfoRef.componentInstance.title = _title;
    modalInfoRef.componentInstance.message = _message;
  }

  openInformationModal(_title: string, _message: string){
    const modalInfoRef = this.ngbModal.open(InformationModalComponent);
    modalInfoRef.componentInstance.title = _title;
    modalInfoRef.componentInstance.message = _message;
    return modalInfoRef;
  }

}
