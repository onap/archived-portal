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
import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { RoleService, FunctionalMenuService } from 'src/app/shared/services';
import { MatTableDataSource } from '@angular/material';

@Component({
  selector: 'app-bulk-upload-role',
  templateUrl: './bulk-upload-role.component.html',
  styleUrls: ['./bulk-upload-role.component.scss']
})
export class BulkUploadRoleComponent implements OnInit {


  @Input() title: string;
  closeResult: string;
  @Input() appId: string;
  @Input() dialogState: number;
  ngRepeatBulkUploadOptions = [
    { id: '1', title: 'Functions', value: 'functions' },
    { id: '2', title: 'Roles', value: 'roles' },
    { id: '3', title: 'Role Functions', value: 'roleFunctions' },
    { id: '4', title: 'Global Role Functions', value: 'globalRoleFunctions' }
  ];
  selectedUploadDropdown: any;
  uploadTypeInstruction = "Function Type, Function Instance, Function Action, Function Name";
  uploadCheck: boolean;
  isProcessing: boolean;
  conformMsg: string;
  progressMsg: string;
  uploadFile: any;
  // Roles fetched from Role service
  appRoleFuncsResult = [];
  // Functions fetched from Role service
  appFunctionsResult = [];
  // Global  roles fetched from Role service
  appGlobalRolesResult = [];
  changeUploadTypeInstruction(typeInstrc) {
    switch (typeInstrc) {
      case 'functions':
        this.uploadTypeInstruction = "Function Type, Function Instance, Function Action, Function Name";
        break;
      case 'roles':
        this.uploadTypeInstruction = "Role Name, Priority (Optional)";
        break;
      case 'roleFunctions':
        this.uploadTypeInstruction = "Role Name, Function Type, Function Instance, Function Action, Function Name";
        break;
      default:
        this.uploadTypeInstruction = "Global Role Name, Function Type, Function Instance, Function Action, Function Name";
    }
  };

  fileModel: {};
  step1: boolean;
  fileSelected: boolean;
  isProcessedRecords: boolean;
  displayedFunctionColumns: string[] = ['line', 'type', 'instance', 'action', 'name', 'status'];
  uploadFunctionsDataSource = new MatTableDataSource(this.uploadFile);
  displayedRoleColumns: string[] = ['line', 'name', 'priority', 'status'];
  uploadRolesDataSource = new MatTableDataSource(this.uploadFile);
  displayedRoleFunctionColumns: string[] = ['line', 'role', 'type', 'instance', 'action', 'name', 'status'];
  uploadRoleFunctionsDataSource = new MatTableDataSource(this.uploadFile);
  displayedGlobalRoleFunctionColumns: string[] = ['line', 'role', 'type', 'instance', 'action', 'name', 'status'];
  uploadGlobalRoleFunctionsDataSource = new MatTableDataSource(this.uploadFile);

  constructor(public activeModal: NgbActiveModal, public ngbModal: NgbModal, private roleService: RoleService, private functionalMenuService: FunctionalMenuService) { }

  ngOnInit() {
    this.selectedUploadDropdown = this.ngRepeatBulkUploadOptions[0];
    this.fileModel = {};
    // Enable modal controls
    this.step1 = true;

    this.fileSelected = false;

    this.isProcessedRecords = false;
  }

  navigateUploadScreen() {
    if (this.selectedUploadDropdown.value === 'functions') {
      this.title = 'Bulk Upload Functions Confirmation';
    } else if (this.selectedUploadDropdown.value === 'roles') {
      this.title = 'Bulk Upload Roles Confirmation';
    } else if (this.selectedUploadDropdown.value === 'roleFunctions') {
      this.title = 'Bulk Upload Role-Functions Confirmation';
    } else {
      this.title = 'Bulk Upload Global-Role-Functions Confirmation';
    }
    this.dialogState = 2;
  }

  navigateSelectTypeUpload() {
    this.title = 'Bulk Upload Role-Function';
    this.dialogState = 1;
  }

  updateInDB() {
    if (this.selectedUploadDropdown.value === 'functions') {
      this.updateFunctionsInDB();
    } else if (this.selectedUploadDropdown.value === 'roles') {
      this.updateRolesInDB();
    } else if (this.selectedUploadDropdown.value === 'roleFunctions') {
      this.updateRoleFunctionsInDB();
    } else {
      this.updateGlobalRoleFunctionsInDB();
    }
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
    var result = [];
    var len, i, line, o;
    switch (this.selectedUploadDropdown.value) {
      case 'functions':
        for (len = lines.length, i = 1; i <= len; ++i) {
          line = lines[i - 1].trim();

          if (line.length == 0) {
            // console.log("Skipping blank line");
            result.push({
              line: i,
              type: '',
              instance: '',
              action: '',
              name: '',
              status: 'Blank line'
            });
            continue;
          }
          o = line.split(',');
          if (o.length !== 4) {
            // other lengths not valid for upload
            result.push({
              line: i,
              type: o[0],
              instance: o[1],
              action: o[2],
              name: '',
              status: 'Failed to find 4 comma-separated values'
            });
          }
          else {
            // console.log("Valid line: ", val);
            let entry = {
              line: i,
              type: o[0],
              instance: o[1],
              action: o[2],
              name: o[3]
              // leave status undefined, this
              // could be valid.
            };
            if (o[0].toLowerCase() === 'type') {
              // not valid for upload, so set status
              entry['status'] = 'Header';
            }
            else if (o[0].toLowerCase() === 'instance') {
              // not valid for upload, so set status
              entry['status'] = 'Header';
            }
            else if (o[0].toLowerCase() === 'action') {
              // not valid for upload, so set status
              entry['status'] = 'Header';
            }
            else if (o[0].toLowerCase() === 'name') {
              // not valid for upload, so set status
              entry['status'] = 'Header';
            }
            else if (o[0].trim() == '' || o[1].trim() == '' || o[2].trim() == '' || o[3].trim() == '') {
              // defend against line with only a
              // single comma etc.
              entry['status'] = 'Failed to find non-empty values';
            }
            result.push(entry);
          } // len 2
        }// for
        break;
      case 'roles':
        for (len = lines.length, i = 1; i <= len; ++i) {
          line = lines[i - 1].trim();
          if (line.length == 0) {
            // console.log("Skipping blank line");
            result.push({
              line: i,
              role: '',
              priority: '',
              status: 'Blank line'
            });
            continue;
          }
          o = line.split(',');
          if (o.length === 0 && line.length !== 0) {
            // other lengths not valid for upload
            result.push({
              line: i,
              role: o[0],
              priority: null
            });
          }
          else {
            // console.log("Valid line: ", val);
            let entry = {
              line: i,
              role: o[0],
              priority: o[1]
              // leave status undefined, this
              // could be valid.
            };
            if (o[0].toLowerCase() === 'role') {
              // not valid for upload, so set status
              entry['status'] = 'Header';
            }
            if (o[0].toLowerCase() === 'priority') {
              // not valid for upload, so set status
              entry['status'] = 'Header';
            }
            else if (o[0].trim() == '') {
              // defend against line with only a
              // single comma etc.
              entry['status'] = 'Failed to find non-empty values';
            }
            result.push(entry);
          } // len 2
        }
        break;
      case 'roleFunctions':
      case 'globalRoleFunctions':
        for (len = lines.length, i = 1; i <= len; ++i) {
          line = lines[i - 1].trim();
          if (line.length == 0) {
            // console.log("Skipping blank line");
            result.push({
              line: i,
              role: '',
              type: '',
              instance: '',
              action: '',
              name: '',
              status: 'Blank line'
            });
            continue;
          }
          o = line.split(',');
          if (o.length !== 5) {
            // other lengths not valid for upload
            result.push({
              line: i,
              role: o[0],
              type: o[1],
              instance: o[2],
              action: o[3],
              name: '',
              status: 'Failed to find 4 comma-separated values'
            });
          }
          else {
            // console.log("Valid line: ", val);
            let entry = {
              line: i,
              role: o[0],
              type: o[1],
              instance: o[2],
              action: o[3],
              name: o[4]
              // leave status undefined, this
              // could be valid.
            };
            if (o[0].toLowerCase() === 'role') {
              // not valid for upload, so set status
              entry['status'] = 'Header';
            } else if (o[0].toLowerCase() === 'type') {
              // not valid for upload, so set status
              entry['status'] = 'Header';
            }
            else if (o[0].toLowerCase() === 'instance') {
              // not valid for upload, so set status
              entry['status'] = 'Header';
            }
            else if (o[0].toLowerCase() === 'action') {
              // not valid for upload, so set status
              entry['status'] = 'Header';
            }
            else if (o[0].toLowerCase() === 'name') {
              // not valid for upload, so set status
              entry['status'] = 'Header';
            }
            else if (o[0].trim() == '' || o[1].trim() == '' || o[2].trim() == '' || o[3].trim() == '' || o[4].trim() == '') {
              // defend against line with only a
              // single comma etc.
              entry['status'] = 'Failed to find non-empty values';
            }
            result.push(entry);
          } // len 2
        }
        break;
      default:
        result = [];
        break;
    }
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
          if (this.selectedUploadDropdown.value === 'roles') {
            // if (debug){
            // $log.debug('BulkRoleAndFunctionsModalCtrl::readValidateFile onload: data length is ' + this.uploadFile.length);
            // }
            this.progressMsg = 'Fetching & validating application roles...';
            // fetch app roles
            this.roleService.getRoles(this.appId).toPromise().then((appRoles: any) => {
              // if (debug){
              // $log.debug("BulkRoleAndFunctionsModalCtrl::readValidateFile: getRoles returned " + JSON.stringify(appFunctions.data));
              // }
              let availableRolesList = JSON.parse(appRoles.data);
              this.appRoleFuncsResult = availableRolesList.availableRoles;
              this.evalAppRolesCheckResults();
              // Re sort by line for the confirmation dialog
              this.uploadFile.sort(this.getSortOrder('line', false));
              // We're done, confirm box may show the  table
              // if (debug)
              // $log.debug('BulkRoleAndFunctionsModalCtrl::readValidateFile inner-then ends');
              this.progressMsg = 'Done.';
              this.isProcessing = false;
              this.isProcessedRecords = false;
            }, (error) => {
              // $log.error('BulkUserModalCtrl::readValidateFile: failed retrieving app roles info');
              this.isProcessing = false;
              this.isProcessedRecords = false;
            });

            this.uploadRolesDataSource = new MatTableDataSource(this.uploadFile);
            this.dialogState = 3;
          } else if (this.selectedUploadDropdown.value === 'roleFunctions') {
            // if (debug) {
            // $log.debug('BulkRoleAndFunctionsModalCtrl::readValidateFile onload: data length is ' + this.uploadFile.length);
            // }
            this.progressMsg = 'Fetching & validating application role functions...';
            //fetch app functions
            this.roleService.getRoleFunctionList(this.appId).toPromise().then((appFunctions: any) => {
              // if (debug)
              // $log.debug("BulkRoleAndFunctionsModalCtrl::readValidateFile: getRoleFunctionList returned " + JSON.stringify(appFunctions.data));
              let availableRoleFunctionsList = JSON.parse(appFunctions.data);
              this.appFunctionsResult = availableRoleFunctionsList.availableRoleFunctions;
              // fetch app roles
              this.roleService.getRoles(this.appId).toPromise().then((appRoles: any) => {
                // if (debug) {
                // $log.debug("BulkRoleAndFunctionsModalCtrl::readValidateFile: getRoles returned " + JSON.stringify(appFunctions.data));
                // }
                let availableRolesList = JSON.parse(appRoles.data);
                this.appRoleFuncsResult = availableRolesList.availableRoles;
                this.evalAppRoleFuncsCheckResults(this.selectedUploadDropdown.value);
                // Re sort by line for the confirmation dialog
                this.uploadFile.sort(this.getSortOrder('line', false));
                // We're done, confirm box may show the  table
                // if (debug)
                // $log.debug('BulkRoleAndFunctionsModalCtrl::readValidateFile inner-then ends');
                this.progressMsg = 'Done.';
                this.isProcessing = false;
                this.isProcessedRecords = false;
              }, (error) => {
                // $log.error('BulkUserModalCtrl::readValidateFile: failed retrieving app roles info');
                this.isProcessing = false;
                this.isProcessedRecords = false;
              });
            }, (error) => {
              // $log.error('BulkUserModalCtrl::readValidateFile: failed retrieving app functions info');
              this.isProcessing = false;
            }
            );
            this.uploadRoleFunctionsDataSource = new MatTableDataSource(this.uploadFile);
            this.dialogState = 3;
          } else if (this.selectedUploadDropdown.value === 'functions') {
            // if (debug) {
            // $log.debug('BulkRoleAndFunctionsModalCtrl::readValidateFile onload: data length is ' + this.uploadFile.length);
            // }
            this.progressMsg = 'Fetching & validating the application functions...';
            // fetch app functions
            this.roleService.getRoleFunctionList(this.appId).toPromise().then((appFunctions: any) => {
              // if (debug)
              // $log.debug("BulkRoleAndFunctionsModalCtrl::readValidateFile: getRoleFunctionList returned " + JSON.stringify(appFunctions.data));
              let availableRoleFunctionsList = JSON.parse(appFunctions.data);
              this.appFunctionsResult = availableRoleFunctionsList.availableRoleFunctions;
              this.verifyFunctions();
              this.evalAppFunctionsCheckResults();
              // Re sort by line for the confirmation dialog
              this.uploadFile.sort(this.getSortOrder('line', false));
              // We're done, confirm box may show the  table
              // if (debug)
              // $log.debug('BulkRoleAndFunctionsModalCtrl::readValidateFile inner-then ends');
              this.progressMsg = 'Done.';
              this.isProcessing = false;
              this.isProcessedRecords = false;
            }, (error) => {
              // $log.error('BulkUserModalCtrl::readValidateFile: failed retrieving app functions info');
              this.isProcessing = false;
              this.isProcessedRecords = false;
            }
            );
            this.uploadFunctionsDataSource = new MatTableDataSource(this.uploadFile);
            this.dialogState = 3;
          } else if (this.selectedUploadDropdown.value === 'globalRoleFunctions') {
            // if (debug) {
            // $log.debug('BulkRoleAndFunctionsModalCtrl::readValidateFile onload: data length is ' + this.uploadFile.length);
            // }
            this.progressMsg = 'Fetching application global role functions...';
            //fetch app functions
            this.roleService.getRoleFunctionList(this.appId).toPromise().then((appFunctions: any) => {
              // if (debug)
              // $log.debug("BulkRoleAndFunctionsModalCtrl::readValidateFile: getRoleFunctionList returned " + JSON.stringify(appFunctions.data));
              let availableRoleFunctionsList = JSON.parse(appFunctions.data);
              this.appFunctionsResult = availableRoleFunctionsList.availableRoleFunctions;
              // fetch app roles
              this.roleService.getRoles(this.appId).toPromise().then((appRoles: any) => {
                // if (debug) {
                // $log.debug("BulkRoleAndFunctionsModalCtrl::readValidateFile: getRoles returned " + JSON.stringify(appFunctions.data));
                // }
                let availableRolesList = JSON.parse(appRoles.data);
                this.appRoleFuncsResult = availableRolesList.availableRoles;
                this.appRoleFuncsResult.forEach((appRole) => {
                  if (appRole.name.toLowerCase().startsWith("global_")) {
                    this.appGlobalRolesResult.push(appRole);
                  }
                });
                this.evalAppRoleFuncsCheckResults(this.selectedUploadDropdown.value);
                // Re sort by line for the confirmation dialog
                this.uploadFile.sort(this.getSortOrder('line', false));
                // We're done, confirm box may show the  table
                // if (debug)
                // $log.debug('BulkRoleAndFunctionsModalCtrl::readValidateFile inner-then ends');
                this.progressMsg = 'Done.';
                this.isProcessing = false;
                this.isProcessedRecords = false;
              }, (error) => {
                // $log.error('BulkUserModalCtrl::readValidateFile: failed retrieving app roles info');
                this.isProcessing = false;
                this.isProcessedRecords = false;
              });
              this.uploadGlobalRoleFunctionsDataSource = new MatTableDataSource(this.uploadFile);
              this.dialogState = 3;
            }, (error) => {
              // $log.error('BulkUserModalCtrl::readValidateFile: failed retrieving app functions info');
              this.isProcessing = false;
            }
            );
          }
        }
      }
    }
  }


  /**
   * Evaluates the result set returned by the role service.
   * Sets an uploadFile array element status if a functions is not
   * defined. Reads and writes scope variable uploadFile. Reads
   * closure variable appFunctionsResult.
   */
  verifyFunctions() {
    // if (debug)
    // $log.debug('BulkRoleAndFunctionsModalCtrl::verifyFunctions: appFunctions is ' + JSON.stringify(appFunctionsResult));
    // check functions in upload file against defined app functions
    this.uploadFile.forEach((uploadRow) => {
      // skip rows that already have a defined status: headers etc.
      if (uploadRow.status) {
        // if (debug)
        // $log.debug('BulkRoleAndFunctionsModalCtrl::verifyFunctions: skip row ' + uploadRow.line);
        return;
      }
      for (var i = 0; i < this.appFunctionsResult.length; i++) {
        if (uploadRow.type.toUpperCase() === this.appFunctionsResult[i].type.toUpperCase()
          && uploadRow.instance.toUpperCase() === this.appFunctionsResult[i].code.toUpperCase()
          && uploadRow.action.toUpperCase() === this.appFunctionsResult[i].action.toUpperCase()) {
          // if (debug)
          // $log.debug('BulkRoleAndFunctionsModalCtrl::verifyFunctions: match on function ' + uploadRow.type,
          // uploadRow.instance, uploadRow.type,  uploadRow.type);
          break;
        }
      }
    }); // foreach
  }; // verifyFunctions

  /**
 * Evaluates the result set of existing functions returned by 
 * the Roleservice and list of functions found in the upload file. 
 * Reads and writes scope variable uploadFile. 
 * Reads closure variable appFunctionsResult.
 */
  evalAppFunctionsCheckResults() {
    // if (debug)
    // $log.debug('BulkRoleAndFunctionsModalCtrl::evalAppFunctionsCheckResults: uploadFile length is ' + $scope.uploadFile.length);
    this.uploadFile.forEach((uploadRow) => {
      if (uploadRow.status) {
        // if (debug)
        // $log.debug('BulkRoleAndFunctionsModalCtrl::evalAppFunctionsCheckResults: skip row ' + uploadRow.line);
        return;
      }
      // Search for the match in the app-functions
      // array
      let isFunctionExist = false;
      this.appFunctionsResult.forEach((exixtingFuncObj) => {
        if (uploadRow.type.toUpperCase() === exixtingFuncObj.type.toUpperCase()
          && uploadRow.instance.toUpperCase() === exixtingFuncObj.code.toUpperCase()
          && uploadRow.action.toUpperCase() === exixtingFuncObj.action.toUpperCase()) {
          uploadRow.status = 'Function exits!';
          uploadRow.isCreate = false;
          isFunctionExist = true;
        }
      }); // for each result
      if (!isFunctionExist) {
        if (/[^a-zA-Z0-9\-\.\_]/.test(uploadRow.type)
          || (uploadRow.action !== '*'
            && /[^a-zA-Z0-9\-\.\_]/.test(uploadRow.action))
          || /[^a-zA-Z0-9\-\:\_\./*]/.test(uploadRow.instance)
          || /[^a-zA-Z0-9\-\_ \.]/.test(uploadRow.name)) {
          uploadRow.status = 'Invalid function';
          uploadRow.isCreate = false;
        } else {
          // if (debug){
          // $log.debug('BulkRoleAndFunctionsModalCtrl::evalAppFunctionsCheckResults: new function ' 
          // + uploadRow);
          // }
          // After much back-and-forth I decided a clear  indicator is better than blank in the table  status column.
          uploadRow.status = 'Create';
          uploadRow.isCreate = true;
        }
      }
    }); // for each row
  }; // evalAppFunctionsCheckResults


  /**
   * Evaluates the result set of existing roles returned by 
   * the Roleservice and list of roles found in the upload file. 
   * Reads and writes scope variable uploadFile. 
   * Reads closure variable appRolesResult.
   */
  evalAppRolesCheckResults() {
    // if (debug)
    // $log.debug('BulkRoleAndFunctionsModalCtrl::evalAppRolesCheckResults: uploadFile length is ' + this.uploadFile.length);
    this.uploadFile.forEach((uploadRow) => {
      if (uploadRow.status) {
        // if (debug)
        // $log.debug('BulkRoleAndFunctionsModalCtrl::evalAppRolesCheckResults: skip row ' + uploadRow.line);
        return;
      }
      // Search for the match in the app-roles
      // array
      let isRoleExist = false;
      this.appRoleFuncsResult.forEach((existingRoleObj) => {
        if (uploadRow.role.toUpperCase() === existingRoleObj.name.toUpperCase()) {
          uploadRow.status = 'Role exits!';
          uploadRow.isCreate = false;
          isRoleExist = true;
        }
      }); // for each result
      if (!isRoleExist) {
        if (/[^a-zA-Z0-9\-\_ \.\/]/.test(uploadRow.role) ||
          uploadRow.role.toLowerCase().startsWith("global_")) {
          uploadRow.status = 'Invalid role!';
          uploadRow.isCreate = false;
        } else {
          // if (debug){
          // $log.debug('BulkRoleAndFunctionsModalCtrl::evalAppRolesCheckResults: new function ' 
          // + uploadRow);
          // }
          // After much back-and-forth I decided a clear  indicator is better than blank in the table  status column.
          uploadRow.status = 'Create';
          uploadRow.isCreate = true;
        }
      }
    }); // for each row
  }; // evalAppRolesCheckResults

  /**
   * Evaluates the result set of existing roles returned by 
   * the Roleservice and list of roles found in the upload file. 
   * Reads and writes scope variable uploadFile. 
   * Reads closure variable appRolesResult.
   */
  evalAppRoleFuncsCheckResults(typeUpload) {
    // if (debug)
    // $log.debug('BulkRoleAndFunctionsModalCtrl::evalAppRoleFuncsCheckResults: uploadFile length is ' + this.uploadFile.length);
    this.uploadFile.forEach((uploadRow) => {
      if (uploadRow.status) {
        // if (debug)
        // $log.debug('BulkRoleAndFunctionsModalCtrl::evalAppRoleFuncsCheckResults: skip row ' + uploadRow.line);
        return;
      }
      // Search for the match in the app-functions array
      let isValidFunc = false;
      this.appFunctionsResult.forEach((existingFuncObj) => {
        if (uploadRow.type.toUpperCase() === existingFuncObj.type.toUpperCase()
          && uploadRow.instance.toUpperCase() === existingFuncObj.code.toUpperCase()
          && uploadRow.action.toUpperCase() === existingFuncObj.action.toUpperCase()
          && uploadRow.name.toUpperCase() === existingFuncObj.name.toUpperCase()) {
          isValidFunc = true;
        }
      });

      let isValidRole = false;
      let isRoleFuncExist = false;
      if (typeUpload === 'globalRoleFunctions') {
        // Search for the match in the app-role array
        this.appGlobalRolesResult.forEach((existingRoleObj) => {
          if (uploadRow.role.toUpperCase() === existingRoleObj.name.toUpperCase()) {
            isValidRole = true;
            if (isValidFunc) {
              existingRoleObj.roleFunctions.forEach((existingRoleFuncObj) => {
                if (uploadRow.type.toUpperCase() === existingRoleFuncObj.type.toUpperCase()
                  && uploadRow.instance.toUpperCase() === existingRoleFuncObj.code.toUpperCase()
                  && uploadRow.action.toUpperCase() === existingRoleFuncObj.action.toUpperCase()) {
                  isRoleFuncExist = true;
                }
              });
            }
          }
        }); // for each result
      } else {
        // Search for the match in the app-role array
        this.appRoleFuncsResult.forEach((existingRoleObj) => {
          if (uploadRow.role.toUpperCase() === existingRoleObj.name.toUpperCase()) {
            isValidRole = true;
            if (isValidFunc) {
              existingRoleObj.roleFunctions.forEach((existingRoleFuncObj) => {
                if (uploadRow.type.toUpperCase() === existingRoleFuncObj.type.toUpperCase()
                  && uploadRow.instance.toUpperCase() === existingRoleFuncObj.code.toUpperCase()
                  && uploadRow.action.toUpperCase() === existingRoleFuncObj.action.toUpperCase()) {
                  isRoleFuncExist = true;
                }
              });
            }
          }
        }); // for each result
      }

      uploadRow.isCreate = false;
      if (typeUpload === 'globalRoleFunctions' && (!isValidRole || !isValidFunc)) {
        uploadRow.status = 'Invalid global role function!';
      } else if (typeUpload !== 'globalRoleFunctions' && (!isValidRole || !isValidFunc)) {
        uploadRow.status = 'Invalid role function!';
      } else if (typeUpload === 'globalRoleFunctions' && !isRoleFuncExist) {
        uploadRow.status = 'Add global role function!';
        uploadRow.isCreate = true;
      } else if (typeUpload !== 'globalRoleFunctions' && !isRoleFuncExist) {
        uploadRow.status = 'Add role function!';
        uploadRow.isCreate = true;
      } else if (typeUpload === 'globalRoleFunctions') {
        uploadRow.status = 'Global role function exists!';
      } else {
        uploadRow.status = 'Role function exists!';
      }

    }); // for each row
  }; // evalAppRolesCheckResults


  /**
   * Sends requests to Portal BE requesting application functions assignment.
 * That endpoint handles creation of the application functions in the 
 * external auth system if necessary. Reads closure variable appFunctionsResult.
 * Invoked by the Next button on the confirmation dialog.
 */
  updateFunctionsInDB() {
    this.isProcessing = true;
    this.conformMsg = '';
    this.isProcessedRecords = true;
    this.progressMsg = 'Sending requests to application..';
    // if (debug)
    // $log.debug('BulkRoleAndFunctionsModalCtrl::updateFunctionsInDB: request length is ' + appUserRolesRequest.length);
    var numberFunctionsSucceeded = 0;
    let promises = [];
    this.uploadFile.forEach((appFuncPostData) => {
      // if (debug)
      // $log.debug('BulkRoleAndFunctionsModalCtrl::updateFunctionsInDB: appFuncPostData is ' + JSON.stringify(appFuncPostData));
      let updateFunctionsFinalPostData = {
        type: appFuncPostData.type,
        code: appFuncPostData.instance,
        action: appFuncPostData.action,
        name: appFuncPostData.name
      };
      // if (debug)
      // $log.debug('BulkRoleAndFunctionsModalCtrl::updateFunctionsInDB: updateFunctionsFinalPostData is ' + JSON.stringify(updateFunctionsFinalPostData));
      let updatePromise = {};
      if (appFuncPostData.isCreate) {
        updatePromise = this.functionalMenuService.saveBulkFunction(this.appId, updateFunctionsFinalPostData).toPromise().then(res => {
          // if (debug)
          // $log.debug('BulkRoleAndFunctionsModalCtrl::updateFunctionsInDB: updated successfully: ' + JSON.stringify(res));
          numberFunctionsSucceeded++;
        }).catch(err => {
          // What to do if one of many fails??
          // $log.error('BulkRoleAndFunctionsModalCtrl::updateFunctionsInDB failed: ', err);
          const modalErrorRef = this.ngbModal.open(ConfirmationModalComponent);
          modalErrorRef.componentInstance.title = 'Error';
          modalErrorRef.componentInstance.message = 'Failed to update the application functions.' + 'Error: ' + err.status;
        }).finally(() => {
        });
      }
      promises.push(updatePromise);
    }); // for each

    // Run all the promises
    Promise.all(promises).then(() => {
      this.conformMsg = 'Processed ' + numberFunctionsSucceeded + ' records.';
      this.isProcessing = false;
      this.isProcessedRecords = true;
      this.uploadFile = [];
      this.dialogState = 2;
    });
  }; // updateFunctionsInDB

  /**
 * Sends requests to Portal BE requesting application functions assignment.
* That endpoint handles creation of the application role in the 
* external auth system if necessary. Reads closure variable appRoleFuncResult.
* Invoked by the Next button on the confirmation dialog.
*/
  updateRolesInDB() {
    this.isProcessing = true;
    this.conformMsg = '';
    this.isProcessedRecords = true;
    this.progressMsg = 'Sending requests to application..';
    // if (debug)
    // $log.debug('BulkRoleAndFunctionsModalCtrl::updateRolesInDB: request length is ' + appUserRolesRequest.length);
    var numberRolesSucceeded = 0;
    let promises = [];
    this.uploadFile.forEach((appRolePostData) => {
      let priority = parseInt(appRolePostData.priority);
      // if (debug)
      // $log.debug('BulkRoleAndFunctionsModalCtrl::updateRolesInDB: appRolePostData is ' + JSON.stringify(appFuncPostData));
      let uplaodRolePostData = {};
      if (isNaN(priority)) {
        uplaodRolePostData = {
          name: appRolePostData.role,
          active: true,
        }
      } else {
        uplaodRolePostData = {
          name: appRolePostData.role,
          priority: appRolePostData.priority,
          active: true,
        }
      }
      var postData = {
        role: uplaodRolePostData,
        roleFunctions: [],
        childRoles: []
      }
      // if (debug)
      // $log.debug('BulkRoleAndFunctionsModalCtrl::updateRolesInDB: uplaodRoleFinalPostData is ' + JSON.stringify(uplaodRoleFinalPostData));
      let updatePromise = {};
      if (appRolePostData.isCreate) {
        updatePromise = this.functionalMenuService.saveBulkRole(this.appId, JSON.stringify(postData)).toPromise().then(res => {
          // if (debug)
          // $log.debug('BulkRoleAndFunctionsModalCtrl::updateRolesInDB: updated successfully: ' + JSON.stringify(res));
          numberRolesSucceeded++;
        }).catch(err => {
          // What to do if one of many fails??
          // $log.error('BulkRoleAndFunctionsModalCtrl::updateRolesInDB failed: ', err);
          const modalErrorRef = this.ngbModal.open(ConfirmationModalComponent);
          modalErrorRef.componentInstance.title = 'Error';
          modalErrorRef.componentInstance.message = 'Failed to update the application role. ' +
            'Error: ' + err.status;
        }).finally(() => {
        });
      }
      promises.push(updatePromise);
    }); // for each

    // Run all the promises
    Promise.all(promises).then(() => {
      if (numberRolesSucceeded == 0) {
        this.conformMsg = 'Processed ' + numberRolesSucceeded + ' records';
      } else {
        this.conformMsg = 'Processed ' + numberRolesSucceeded + ' records. Please sync roles to reflect in portal';
      } this.isProcessing = false;
      this.isProcessedRecords = true;
      this.uploadFile = [];
      this.dialogState = 2;
    });
  }; // updateRolesInDB

  /**
 * Sends requests to Portal BE requesting role function assignment.
 * That endpoint handles adding role function in the external auth system
 * if necessary.Invoked by the Next button on the confirmation dialog.
 */
  updateRoleFunctionsInDB() {
    this.isProcessing = true;
    this.conformMsg = '';
    this.isProcessedRecords = true;
    this.progressMsg = 'Sending requests to application..';
    // if (debug)
    // $log.debug('BulkRoleAndFunctionsModalCtrl::updateRoleFunctionsInDB: request length is ' + appUserRolesRequest.length);
    var numberRoleFunctionSucceeded = 0;
    let promises = [];
    this.uploadFile.forEach((appRoleFuncPostData) => {
      // if (debug)
      // $log.debug('BulkRoleAndFunctionsModalCtrl::updateRoleFunctionsInDB: appRoleFuncPostData is ' + JSON.stringify(appFuncPostData));
      let updateRoleFunctionFinalPostData = {
        roleName: appRoleFuncPostData.role,
        type: appRoleFuncPostData.type,
        instance: appRoleFuncPostData.instance,
        action: appRoleFuncPostData.action,
        name: appRoleFuncPostData.name,
        isGlobalRolePartnerFunc: false
      };
      // if (debug)
      // $log.debug('BulkRoleAndFunctionsModalCtrl::updateRoleFunctionsInDB: updateRoleFunctionFinalPostData is ' + JSON.stringify(updateFunctionsFinalPostData));
      let updatePromise = {};
      if (appRoleFuncPostData.isCreate) {
        updatePromise = this.functionalMenuService.updateBulkRoleFunction(this.appId, updateRoleFunctionFinalPostData).toPromise().then(res => {
          // if (debug)
          // $log.debug('BulkRoleAndFunctionsModalCtrl::updateRoleFunctionsInDB: updated successfully: ' + JSON.stringify(res));
          numberRoleFunctionSucceeded++;
        }).catch(err => {
          // What to do if one of many fails??
          // $log.error('BulkRoleAndFunctionsModalCtrl::updateRoleFunctionsInDB failed: ', err);
          const modalErrorRef = this.ngbModal.open(ConfirmationModalComponent);
          modalErrorRef.componentInstance.title = 'Error';
          modalErrorRef.componentInstance.message = 'Failed to update the application role function. ' +
            'Error: ' + err.status;
        }).finally(() => {
        });
      }
      promises.push(updatePromise);
    }); // for each

    // Run all the promises
    Promise.all(promises).then(() => {
      if (numberRoleFunctionSucceeded == 0) {
        this.conformMsg = 'Processed ' + numberRoleFunctionSucceeded + ' records';
      } else {
        this.conformMsg = 'Processed ' + numberRoleFunctionSucceeded + ' records. Please sync roles to reflect in portal';
      } this.isProcessing = false;
      this.isProcessedRecords = true;
      this.uploadFile = [];
      this.dialogState = 2;
    });
  }; // updateRoleFunctionsInDB

  /**
 * Sends requests to Portal requesting global role functions assignment.
 * That endpoint handles updating global role functions in the external auth system
 * if necessary. Invoked by the Next button on the confirmation dialog.
 */
  updateGlobalRoleFunctionsInDB() {
    this.isProcessing = true;
    this.conformMsg = '';
    this.isProcessedRecords = true;
    this.progressMsg = 'Sending requests to application..';
    // if (debug)
    // $log.debug('BulkRoleAndFunctionsModalCtrl::updateGlobalRoleFunctionsInDB: request length is ' + appUserRolesRequest.length);
    var numberGlobalRoleFunctionSucceeded = 0;
    let promises = [];
    this.uploadFile.forEach((appRoleFuncPostData) => {
      // if (debug)
      // $log.debug('BulkRoleAndFunctionsModalCtrl::updateGlobalRoleFunctionsInDB: appRoleFuncPostData is ' + JSON.stringify(appFuncPostData));
      let updateGlobalRoleFunctionFinalPostData = {
        roleName: appRoleFuncPostData.role,
        type: appRoleFuncPostData.type,
        instance: appRoleFuncPostData.instance,
        action: appRoleFuncPostData.action,
        name: appRoleFuncPostData.name,
        isGlobalRolePartnerFunc: true
      };
      // if (debug)
      // $log.debug('BulkRoleAndFunctionsModalCtrl::updateGlobalRoleFunctionsInDB: updateRoleFunctionFinalPostData is ' + JSON.stringify(updateFunctionsFinalPostData));
      let updatePromise = {};
      if (appRoleFuncPostData.isCreate) {
        updatePromise = this.functionalMenuService.updateBulkRoleFunction(this.appId, updateGlobalRoleFunctionFinalPostData).toPromise().then(res => {
          // if (debug)
          // $log.debug('BulkRoleAndFunctionsModalCtrl::updateGlobalRoleFunctionsInDB: updated successfully: ' + JSON.stringify(res));
          numberGlobalRoleFunctionSucceeded++;
        }).catch(err => {
          // What to do if one of many fails??
          // $log.error('BulkRoleAndFunctionsModalCtrl::updateGlobalRoleFunctionsInDB failed: ', err);
          const modalErrorRef = this.ngbModal.open(ConfirmationModalComponent);
          modalErrorRef.componentInstance.title = 'Error';
          modalErrorRef.componentInstance.message = 'Failed to update the global role partner function. ' +
            'Error: ' + err.status;
        }).finally(() => {
        });
      }
      promises.push(updatePromise);
    }); // for each

    // Run all the promises
    Promise.all(promises).then(() => {
      if (numberGlobalRoleFunctionSucceeded == 0) {
        this.conformMsg = 'Processed ' + numberGlobalRoleFunctionSucceeded + ' records';
      } else {
        this.conformMsg = 'Processed ' + numberGlobalRoleFunctionSucceeded + ' records. Please sync roles to reflect in portal';
      }
      this.isProcessing = false;
      this.isProcessedRecords = true;
      this.uploadFile = [];
      this.dialogState = 2;
    });
  }; // updateGlobalRoleFunctionsInDB

}
