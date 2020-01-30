/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
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
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { UsersService, ApplicationsService, FunctionalMenuService } from 'src/app/shared/services';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { MatTableDataSource } from '@angular/material';

@Component({
  selector: 'app-bulk-user',
  templateUrl: './bulk-user.component.html',
  styleUrls: ['./bulk-user.component.scss']
})
export class BulkUserComponent implements OnInit {

  @Input() title: string;
  @Input() adminsAppsData: any;
  @Output() passBackBulkUserPopup: EventEmitter<any> = new EventEmitter();
  adminApps: any;
  // Roles fetched from app service
  appRolesResult: any;
  // Users fetched from user service
  userCheckResult: any;
  // Requests for user-role assignment built by validator
  appUserRolesRequest: any;
  fileSelected: boolean;
  isProcessing: boolean;
  isProcessedRecords: boolean;
  dialogState: number;
  selectedFile: any;
  fileModel: any;
  selectApp: boolean;
  fileToRead: any;
  selectedAppValue: any;
  progressMsg: string;
  conformMsg: string;
  uploadFile: any;
  uploadCheck: boolean;
  displayedColumns: string[] = ['line', 'orgUserId', 'appRole', 'status'];
  uploadFileDataSource = new MatTableDataSource(this.uploadFile);
  constructor(public ngbModal: NgbModal, public activeModal: NgbActiveModal, private applicationsService: ApplicationsService, private usersService: UsersService, private functionalMenuService: FunctionalMenuService) { }

  ngOnInit() {
    this.selectApp = true;
    this.fileSelected = false;
    this.uploadCheck = false;
    // Flag that indicates background work is proceeding
    this.isProcessing = true;
    this.isProcessedRecords = false;
    this.dialogState = 1;
  }

  changeSelectApp(val: any) {
    if (val === 'select-application')
      this.selectApp = true;
    else
      this.selectApp = false;
    this.selectedAppValue = val;
  }

  // Answers a function that compares properties with the specified name.
  getSortOrder = (prop, foldCase) => {
    return function (a, b) {
      let aProp = foldCase ? a[prop].toLowerCase() : a[prop];
      let bProp = foldCase ? b[prop].toLowerCase() : b[prop];
      if (aProp > bProp)
        return 1;
      else if (aProp < bProp)
        return -1;
      else
        return 0;
    }
  }

  onFileLoad(fileLoadedEvent) {
    const textFromFileLoaded = fileLoadedEvent.target.result;
    let lines = textFromFileLoaded.split('\n');
    // this.uploadFile = lines;
    let result = [];
    var len, i, line, o;

    // Need 1-based index below
    for (len = lines.length, i = 1; i <= len; ++i) {
      // Use 0-based index for array
      line = lines[i - 1].trim();
      if (line.length == 0) {
        result.push({
          line: i,
          orgUserId: '',
          role: '',
          status: 'Blank line'
        });
        continue;
      }
      o = line.split(',');
      if (o.length !== 2) {
        // other lengths not valid for upload
        result.push({
          line: i,
          orgUserId: line,
          role: '',
          status: 'Failed to find 2 comma-separated values'
        });
      }
      else {
        let entry = {
          line: i,
          orgUserId: o[0],
          role: o[1]
          // leave status undefined, this could be valid.
        };
        if (o[0].toLowerCase() === 'orgUserId') {
          // not valid for upload, so set status
          entry['status'] = 'Header';
        }
        else if (o[0].trim() == '' || o[1].trim() == '') {
          // defend against line with only a single comma etc.
          entry['status'] = 'Failed to find 2 non-empty values';
        }
        result.push(entry);
      } // len 2
    } // for
    return result;
  }

  onFileSelect(input: HTMLInputElement) {
    var validExts = new Array(".csv", ".txt");
    var fileExt = input.value;
    fileExt = fileExt.substring(fileExt.lastIndexOf('.'));
    if (validExts.indexOf(fileExt) < 0) {
      const modalFileErrorRef = this.ngbModal.open(ConfirmationModalComponent);
      modalFileErrorRef.componentInstance.title = 'Confirmation';
      modalFileErrorRef.componentInstance.message = 'Invalid file selected, valid files are of ' +
        validExts.toString() + ' types.'
      this.uploadCheck = false;
      return false;
    }
    else {
      const files = input.files;
      this.isProcessing = true;
      this.conformMsg = '';
      this.isProcessedRecords = true;
      this.progressMsg = 'Reading upload file..';
      if (files && files.length) {
        this.uploadCheck = true;
        const fileToRead = files[0];
        const fileReader = new FileReader();
        fileReader.readAsText(fileToRead, "UTF-8");
        fileReader.onloadend = (e) => {
          this.uploadFile = this.onFileLoad(e);
          this.uploadFile.sort(this.getSortOrder('orgUserId', true));
          let appId = this.selectedAppValue.id;
          this.progressMsg = 'Fetching application roles..';
          this.functionalMenuService.getManagedRolesMenu(appId).toPromise().then((rolesObj) => {
            this.appRolesResult = rolesObj;
            this.progressMsg = 'Validating application roles..';
            this.verifyAppRoles(this.appRolesResult);
            this.progressMsg = 'Validating Org Users..';
            let userPromises = this.buildUserChecks();
            Promise.all(userPromises).then(userPromise => {
              this.evalUserCheckResults();
              let appPromises = this.buildAppRoleChecks();
              this.progressMsg = 'Querying application for user roles..';
              Promise.all(appPromises).then(() => {
                this.evalAppRoleCheckResults();
                // Re sort by line for the confirmation dialog
                this.uploadFile.sort(this.getSortOrder('line', false));
                // We're done, confirm box may show the table
                this.progressMsg = 'Done.';
                this.isProcessing = false;
                this.isProcessedRecords = false;
              },
                function (error) {
                  this.isProcessing = false;
                  this.isProcessedRecords = false;
                }
              ); // then of app promises
            },
              function (_error) {
                this.isProcessing = false;
                this.isProcessedRecords = false;
              }
            ); // then of user promises
          },
            function (error) {
              this.isProcessing = false;
              this.isProcessedRecords = false;
            }
          );
          this.uploadFileDataSource = new MatTableDataSource(this.uploadFile);
          this.dialogState = 3;
        };
      }
    }
  }

  /**
   * Evaluates the result set returned by the app role service.
   * Sets an uploadFile array element status if a role is not defined.
   * Reads and writes scope variable uploadFile.
   * Reads closure variable appRolesResult.
   */
  verifyAppRoles(appRolesResult: any) {
    // check roles in upload file against defined app roles
    this.uploadFile.forEach(function (uploadRow) {
      // skip rows that already have a defined status: headers etc.
      if (uploadRow.status) {
        return;
      }
      uploadRow.role = uploadRow.role.trim();
      var foundRole = false;
      for (var i = 0; i < appRolesResult.length; i++) {
        if (uploadRow.role.toUpperCase() === appRolesResult[i].rolename.trim().toUpperCase()) {
          foundRole = true;
          break;
        }
      };
      if (!foundRole) {
        uploadRow.status = 'Invalid role';
      };
    }); // foreach
  }; // verifyRoles

  /**
  * Builds and returns an array of promises to invoke the 
  * searchUsers service for each unique Org User UID in the input.
  * Reads and writes scope variable uploadFile, which must be sorted by Org User UID.
  * The promise function writes to closure variable userCheckResult
  */
  buildUserChecks() {
    // if (debug)
    // $log.debug('BulkUserModalCtrl::buildUserChecks: uploadFile length is ' + $scope.uploadFile.length);
    this.userCheckResult = [];
    let promises = [];
    let prevRow = null;
    this.uploadFile.forEach((uploadRow) => {
      if (uploadRow.status) {
        // if (debug)
        // $log.debug('BulkUserModalCtrl::buildUserChecks: skip row ' + uploadRow.line);
        return;
      };
      // detect repeated UIDs
      if (prevRow == null || prevRow.orgUserId.toLowerCase() !== uploadRow.orgUserId.toLowerCase()) {
        // if (debug)
        // $log.debug('BulkUserModalCtrl::buildUserChecks: create request for orgUserId ' + uploadRow.orgUserId);
        let userPromise = this.usersService.searchUsers(uploadRow.orgUserId).toPromise().then((usersList) => {
          if (typeof usersList[0] !== "undefined") {
            this.userCheckResult.push({
              orgUserId: usersList[0].orgUserId,
              firstName: usersList[0].firstName,
              lastName: usersList[0].lastName,
              jobTitle: usersList[0].jobTitle
            });
          }
          else {
            // User not found.
            // if (debug)
            // $log.debug('BulkUserModalCtrl::buildUserChecks: searchUsers returned null');
          }
        }, function (error) {
          // $log.error('BulkUserModalCtrl::buildUserChecks: searchUsers failed ' + JSON.stringify(error));
        });
        promises.push(userPromise);
      }
      else {
        // if (debug)
        // $log.debug('BulkUserModalCtrl::buildUserChecks: skip repeated orgUserId ' + uploadRow.orgUserId);    					
      }
      prevRow = uploadRow;
    }); // foreach
    return promises;
  }; // buildUserChecks

  /**
   * Evaluates the result set returned by the user service to set
   * the uploadFile array element status if the user was not found.
   * Reads and writes scope variable uploadFile.
   * Reads closure variable userCheckResult.
   */
  evalUserCheckResults = () => {
    // if (debug)
    // $log.debug('BulkUserModalCtrl::evalUserCheckResult: uploadFile length is ' + $scope.uploadFile.length);
    this.uploadFile.forEach((uploadRow) => {
      if (uploadRow.status) {
        // if (debug)
        // $log.debug('BulkUserModalCtrl::evalUserCheckResults: skip row ' + uploadRow.line);
        return;
      };
      let foundorgUserId = false;
      this.userCheckResult.forEach(function (userItem) {
        if (uploadRow.orgUserId.toLowerCase() === userItem.orgUserId.toLowerCase()) {
          // if (debug)
          // $log.debug('BulkUserModalCtrl::evalUserCheckResults: found orgUserId ' + uploadRow.orgUserId);
          foundorgUserId = true;
        };
      });
      if (!foundorgUserId) {
        // if (debug)
        // $log.debug('BulkUserModalCtrl::evalUserCheckResults: NO match on orgUserId ' + uploadRow.orgUserId);
        uploadRow.status = 'Invalid orgUserId';
      }
    }); // foreach
  }; // evalUserCheckResults

  /**
  * Builds and returns an array of promises to invoke the getUserAppRoles
  * service for each unique Org User in the input file.
  * Each promise creates an update to be sent to the remote application
  * with all role names.
  * Reads scope variable uploadFile, which must be sorted by Org User.
  * The promise function writes to closure variable appUserRolesRequest
  */
  buildAppRoleChecks() {
    this.appUserRolesRequest = [];
    let appId = this.selectedAppValue.id;
    let promises = [];
    let prevRow = null;
    this.uploadFile.forEach((uploadRow) => {
      if (uploadRow.status) {
        return;
      }
      // Because the input is sorted, generate only one request for each Org User
      if (prevRow == null || prevRow.orgUserId.toLowerCase() !== uploadRow.orgUserId.toLowerCase()) {
        let appPromise = this.usersService.getUserAppRoles(appId, uploadRow.orgUserId, true, false).toPromise().then((userAppRolesResult) => {
          // Reply for unknown user has all defined roles with isApplied=false on each.  
          if (typeof userAppRolesResult[0] !== "undefined") {
            this.appUserRolesRequest.push({
              orgUserId: uploadRow.orgUserId,
              userAppRoles: userAppRolesResult
            });
          } else {
            //  $log.error('BulkUserModalCtrl::buildAppRoleChecks: getUserAppRoles returned ' + JSON.stringify(userAppRolesResult));
          };
        }, function (error) {
          //  $log.error('BulkUserModalCtrl::buildAppRoleChecks: getUserAppRoles failed ', error);
        });
        promises.push(appPromise);
      } else {
        //  if (debug)
        //  $log.debug('BulkUserModalCtrl::buildAppRoleChecks: duplicate orgUserId, skip: '+ uploadRow.orgUserId);
      }
      prevRow = uploadRow;
    }); // foreach
    return promises;
  }; // buildAppRoleChecks

  /**
   * Evaluates the result set returned by the app service and adjusts 
   * the list of updates to be sent to the remote application by setting
   * isApplied=true for each role name found in the upload file.
   * Reads and writes scope variable uploadFile.
   * Reads closure variable appUserRolesRequest.
   */
  evalAppRoleCheckResults() {
    this.uploadFile.forEach((uploadRow) => {
      if (uploadRow.status) {
        return;
      }
      // Search for the match in the app-user-roles array
      this.appUserRolesRequest.forEach((appUserRoleObj) => {
        if (uploadRow.orgUserId.toLowerCase() === appUserRoleObj.orgUserId.toLowerCase()) {
          let roles = appUserRoleObj.userAppRoles;
          roles.forEach(function (appRoleItem) {
            //if (debug)
            //	$log.debug('BulkUserModalCtrl::evalAppRoleCheckResults: checking uploadRow.role='
            //			+ uploadRow.role + ', appRoleItem.roleName= ' + appRoleItem.roleName);
            if (uploadRow.role === appRoleItem.roleName) {
              if (appRoleItem.isApplied) {
                uploadRow.status = 'Role exists';
              }
              else {
                // After much back-and-forth I decided a clear indicator
                // is better than blank in the table status column.
                uploadRow.status = 'OK';
                appRoleItem.isApplied = true;
              }
              // This count is not especially interesting.
              // numberUserRolesSucceeded++;
            }
          }); // for each role
        }
      }); // for each result		
    }); // for each row
  }; // evalAppRoleCheckResults

  // Sets the variable that hides/reveals the user controls
  uploadFileDialog() {
    this.fileSelected = false;
    this.selectedFile = null;
    this.fileModel = null;
    this.dialogState = 2;
  }

  // Navigate between dialog screens using number: 1,2,3
  navigateBack() {
    this.selectApp = true;
    this.dialogState = 1;
    this.fileSelected = false;
  };

  // Navigate between dialog screens using number: 1,2,3
  navigateDialog2() {
    this.dialogState = 2;
  };

  /**
  * Sends requests to Portal requesting user role assignment.
  * That endpoint handles creation of the user at the remote app if necessary.
  * Reads closure variable appUserRolesRequest.
  * Invoked by the Next button on the confirmation dialog.
  */
  updateDB() {
    this.isProcessing = true;
    this.conformMsg = '';
    this.isProcessedRecords = true;
    this.progressMsg = 'Sending requests to application..';
    // if (debug)
    // $log.debug('BulkUserModalCtrl::updateDB: request length is ' + appUserRolesRequest.length);
    var numberUsersSucceeded = 0;
    let promises = [];
    this.appUserRolesRequest.forEach(appUserRoleObj => {
      // if (debug) 
      // $log.debug('BulkUserModalCtrl::updateDB: appUserRoleObj is ' + JSON.stringify(appUserRoleObj));
      let updateRequest = {
        orgUserId: appUserRoleObj.orgUserId,
        appId: this.selectedAppValue.id,
        appRoles: appUserRoleObj.userAppRoles
      };
      //  if (debug)
      //  $log.debug('BulkUserModalCtrl::updateDB: updateRequest is ' + JSON.stringify(updateRequest));
      let updatePromise = this.usersService.updateUserAppRoles(updateRequest).toPromise().then(res => {
        //  if (debug)
        //  $log.debug('BulkUserModalCtrl::updateDB: updated successfully: ' + JSON.stringify(res));
        numberUsersSucceeded++;
      }).catch(err => {
        // What to do if one of many fails??
        //  $log.error('BulkUserModalCtrl::updateDB failed: ', err);
        const modelErrorRef = this.ngbModal.open(ConfirmationModalComponent);
        modelErrorRef.componentInstance.title = 'Error';
        modelErrorRef.componentInstance.message = 'Failed to update the user application roles. ' +
          'Error: ' + err.status;
      }).finally(() => {
        // $log.debug('BulkUserModalCtrl::updateDB: finally()');
      });
      promises.push(updatePromise);
    }); // for each

    // Run all the promises
    Promise.all(promises).then(() => {

      this.conformMsg = 'Processed ' + numberUsersSucceeded + ' users.';
      const modelRef = this.ngbModal.open(ConfirmationModalComponent);
      modelRef.componentInstance.title = 'Confirmation';
      modelRef.componentInstance.message = this.conformMsg
      this.isProcessing = false;
      this.isProcessedRecords = true;
      this.uploadFile = [];
      this.dialogState = 2;
    });
  }; // updateDb

}