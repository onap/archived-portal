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

import { Component, OnInit, Input, Output, EventEmitter, Injectable} from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NotificationService, FunctionalMenuService } from 'src/app/shared/services';
import { SelectionModel } from '@angular/cdk/collections';
import { FlatTreeControl } from '@angular/cdk/tree';
import { MatTreeFlatDataSource, MatTreeFlattener } from '@angular/material/tree';
import { BehaviorSubject } from 'rxjs';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { InformationModalComponent } from 'src/app/modals/information-modal/information-modal.component';

@Component({
  selector: 'app-new-notification-modal',
  templateUrl: './new-notification-modal.component.html',
  styleUrls: ['./new-notification-modal.component.scss']
})
export class NewNotificationModalComponent implements OnInit {

  notification = {
    'notificationId': null,
    'isOnlineUsersOnly': null,
    'isForAllRoles': null,
    'priority': null,
    'isActive': null,
    'startTime': null,
    'endTime': null,
    'msgHeader': null,
    'msgDescription': null,
    'roleIds': null,
    'isFunctionalMenu': null,
    'treeTitle': null,
    'anyTreeItemSelected': false,
    'notifObj':null,
    'roleObj': {
        notificationRoleIds: null
    },
    'notificationRoleIds': null
  };
  
  /**
  * The Json object for to-do list data.
  */
  MAT_TREE_DATA = {};
  notificationId = null;
  selectedCat = null;
  selectedEcompFunc = null;
  YN_index_mapping = {
      "Y": 0,
      "N": 1
  }

  onlineAllUsersOptions = [{
          "index": 0,
          "value": "Y",
          "title": "Online Users Only"
      },
      {
          "index": 1,
          "value": "N",
          "title": "Online & Offline Users"
      }
  ];

  isForAllRoles = [{
          "index": 0,
          "value": "Y",
          "title": "Yes"
      },
      {
          "index": 1,
          "value": "N",
          "title": "No"
      }
  ];

  priorityOptions = [{
          "index": 0,
          "value": "1",
          "title": "Normal"
      },
      {
          "index": 1,
          "value": "2",
          "title": "Important"
      }
  ];

  isActiveOptions = [{
    "index": 0,
    "value": "Y",
    "title": "Yes"
    },
    {
        "index": 1,
        "value": "N",
        "title": "No"
    }
  ];

  @Input() selectedNotification: any;
  @Output() passEntry: EventEmitter<any> = new EventEmitter();
  result: any;
  isEditMode: any;
  functionalMenuRes = {};
  treedata = [];
  checkBoxObj: any;

  dataChange = new BehaviorSubject<TodoItemNode[]>([]);
  get data(): TodoItemNode[] { return this.dataChange.value; }

  constructor(public ngbModal: NgbModal, 
    public activeModal: NgbActiveModal, 
    private notificationService: NotificationService,
    public functionalMenuService: FunctionalMenuService) { }

  ngOnInit() {
    this.notification.isFunctionalMenu='Y';
    console.log("selectedNotification ::::",this.selectedNotification);
    if(this.selectedNotification && this.selectedNotification.msgSource !=''){
     
      this.isEditMode = true;
      this.notification = Object.assign({}, this.selectedNotification);
      if(this.selectedNotification && this.selectedNotification.priority ==1 ){
        this.notification.priority = this.priorityOptions[0].value;
      }else{
        this.notification.priority = this.priorityOptions[1].value;
      }
      if(this.selectedNotification && this.selectedNotification.startTime){
        this.notification.startTime = new Date(this.selectedNotification.startTime);
      }
      if(this.selectedNotification && this.selectedNotification.endTime ){
        this.notification.endTime = new Date(this.selectedNotification.endTime);
      }
      this.notification.isFunctionalMenu='Y';
    }else{
      this.isEditMode = false;
      this.notification.isActive = this.isActiveOptions[0];
      this.notification.isOnlineUsersOnly = this.onlineAllUsersOptions[1];
      this.notification.isForAllRoles = this.isForAllRoles[0].value;
      this.notification.priority = this.priorityOptions[0].value;
      this.notification.isFunctionalMenu = "Y";      
      this.notification.msgHeader = '';
      this.notification.msgDescription = '';
      this.notification.treeTitle = "Functional Menu";
      this.notification.notifObj = {
          isCategoriesFunctionalMenu: true
      }
    }
    this.getFunctionalMenu();
    this.treeFlattener = new MatTreeFlattener(this.transformer, this.getLevel, this.isExpandable, this.getChildren);
    this.treeControl = new FlatTreeControl<TodoItemFlatNode>(this.getLevel, this.isExpandable);
    this.dataSource = new MatTreeFlatDataSource(this.treeControl, this.treeFlattener);
    const data = this.buildFileTree(this.MAT_TREE_DATA, 0);
    this.dataChange.next(data);
    this.dataChange.subscribe(data => {
      this.dataSource.data = data;
    }); 
  }

  addUserNotification(){
    let notificationRoleIds = []
    let endDate = new Date(this.notification.endTime);
    let selectedTreeNode: Array<TodoItemFlatNode> = this.checklistSelection.selected;
    selectedTreeNode.forEach(element => {
      if(element && element.roleIds && element.roleIds.length >0){
        element.roleIds.forEach(id =>{
          if(id && id !='undefined'){
            notificationRoleIds.push(id);
          }
        });
      }
    });
    console.log("notificationRoleIds >>>>>>>>",notificationRoleIds);
    if(notificationRoleIds && notificationRoleIds.length >0){
      notificationRoleIds.sort();
    }
    
    let newUserNotification = {
      'notificationId': (this.notification && this.notification.notificationId ? this.notification.notificationId: null),
      'isForOnlineUsers': (this.notification && this.notification.isOnlineUsersOnly ? this.notification.isOnlineUsersOnly.value : null),
      'isForAllRoles': this.notification.isForAllRoles,
      'priority': this.notification.priority,
      'activeYn': (this.notification && this.notification.isActive ? this.notification.isActive.value :'Y'),
      'startTime': new Date(this.notification.startTime),
      'endTime': new Date(this.notification.endTime),
      'msgHeader': this.notification.msgHeader,
      'msgDescription': this.notification.msgDescription,
      'roleIds': notificationRoleIds,
      'createdDate': new Date()
    };

    if((newUserNotification.isForAllRoles) && (newUserNotification.priority) && (newUserNotification.activeYn) 
        && (newUserNotification.startTime) && (newUserNotification.endTime) 
        && (newUserNotification.msgHeader != '') && (newUserNotification.msgDescription != '')){

      console.log("newUserNotification >>> ",newUserNotification);
      // POST ajax call here;
      if (this.isEditMode) {
        this.notificationService.updateAdminNotification(newUserNotification)
        .subscribe(_data => {
            this.result = _data;
            if(this.result && this.result.status ==='ERROR'){
              this.openConfirmationModal('Error', this.result.response);
            }else{
              this.passEntry.emit(this.result);
              this.ngbModal.dismissAll(); 
            }       
        }, error =>{
          console.log(error);
          this.openConfirmationModal('Error', error);
          return;
        });
      }else{
        this.notificationService.addAdminNotification(newUserNotification)
        .subscribe(_data => {
            this.result = _data;
            if(this.result && this.result.status ==='ERROR'){
              this.openConfirmationModal('Error', this.result.response);
              return;
            }else{
              this.passEntry.emit(this.result);
              this.ngbModal.dismissAll(); 
            }
        }, error =>{
          console.log(error);
          this.openConfirmationModal('Error', error);
          return;
        });
      }
    }else{
      this.openConfirmationModal("","Please fill in all required(*) fields.");
      return;
    }
  }

  getFunctionalMenu(){
    let menu_role_dict = {};
    /**First Rest Call */
    this.functionalMenuService.getFunctionalMenuRole()
    .subscribe(role_res => {
        this.result = role_res;
        if (this.result == null || this.result == 'undefined') {
          console.log('FunctionalMenuService::getFunctionalMenu Failed: Result or result.data is null');
        }else {
          //console.log('First Rest Call Data ::',this.result);
          for (var i in role_res) {
            // if first time appear in menu_role_dict
            if (!(role_res[i].menuId in menu_role_dict)) {
                menu_role_dict[role_res[i].menuId] = [role_res[i].roleId];
            } else {
                menu_role_dict[role_res[i].menuId].push(role_res[i].roleId);
            }
          }

          /** 2nd Rest Call */
          this.functionalMenuService.getManagedFunctionalMenuForNotificationTree()
          .subscribe(res => {
              this.result = res;
              //console.log("2nd REST CALL RESPONSE :: ",this.result);
              var exclude_list = ['Favorites'];
              let actualData = [];
              //Adding children and label attribute to all objects in res
              for (let i = 0; i < this.result.length; i++) {
                res[i].child = [];
                res[i].name = res[i].text;
                res[i].id = res[i].text;
                res[i].displayCheckbox = true;
                let checkBoxObj = {
                    isAnyRoleSelected: false
                };
                res[i].roleId = menu_role_dict[res[i].menuId];
                res[i].onSelect = function() {
                  this.notification.anyTreeItemSelected = this.checkTreeSelect();
                };

                let intersectionObj: any; 
                intersectionObj = res[i].roleId & this.notification.notificationRoleIds;
                if (res[i].roleId && res[i].roleId.length == intersectionObj.length) {
                    res[i].isSelected = true;
                    res[i].selected = true;
                    res[i].indeterminate = false;
                } else {
                    /*default*/
                    res[i].isSelected = false;
                    res[i].selected = false;
                    res[i].indeterminate = false;
                }
            }

            // Adding actual child items to children array in res
                        // objects
            let parentChildDict = {};
            let parentChildRoleIdDict = {};
            for (let i = 0; i < this.result.length; i++) {
                let parentId = this.result[i].menuId;
                parentChildDict[parentId] = [];
                parentChildRoleIdDict[parentId] = [];
                for (let j = 0; j < this.result.length; j++) {
                    let childId = res[j].parentMenuId;
                    if (parentId === childId) {
                        res[i].child.push(res[j]);
                        parentChildDict[parentId].push(res[j].menuId);
                        //if res[j].roleId is defined
                        if (res[j].roleId) {
                            for (let k in res[j].roleId) {
                                parentChildRoleIdDict[parentId].push(res[j].roleId[k]);
                            }

                        }
                    }
                }
            }
            
            //check if grand children exist
            for (var key in parentChildDict) {
              var child = parentChildDict[key];
              var isGrandParent = false;
              if (child.length > 0) {
                  for (var i in child) {
                      if (parentChildDict[child[i]].length > 0) {
                          isGrandParent = true;
                          break;
                      }
                  }
              }
              if (isGrandParent) {
                  for (var i in child) {
                      // if the child has children
                      if (parentChildDict[child[i]].length > 0) {
                          for (var j in parentChildRoleIdDict[child[i]]) {
                              if (parentChildRoleIdDict[key].indexOf(parentChildRoleIdDict[child[i]][j]) === -1) {
                                  parentChildRoleIdDict[key].push(parentChildRoleIdDict[child[i]][j]);
                              }
                          }
                      }
                  }
              }

          };

          // Sort the top-level menu items in order based on the column
          this.result.sort(function(a, b) {
            return a.column - b.column;
          });

          // Sort all the child in order based on the column
          for (let i = 0; i < this.result.length; i++) {
              res[i].child.sort(function(a, b) {
                  return a.column - b.column;
              });
          }

          //Forming actual parent items
          for (let i = 0; i < this.result.length; i++) {
              let pmid = res[i].parentMenuId;
              if (pmid === null) {
                  actualData.push(res[i]);
              }
          }
          var treedata = actualData[0].child;
          
          this.treedata = [];

          /*Remove favorite from the list */
          for (var i in treedata) {
              if (!(treedata[i].name.indexOf(exclude_list) > -1)) {
                  this.treedata.push(treedata[i])
              }
          }
          //setting b2b tree parameter
          this.settingTreeParam();
          this.populateTreeDataSourceByFunctionalMenu(this.treedata);

          }, error =>{
            console.log(error);
          });
        }
    }, error =>{
      console.log(error);
    });
  }

  settingTreeParam() {
    /**************first level****************/
    for (var fi = 0; fi < this.treedata.length; fi++) {
        var fLevel = this.treedata[fi];
        var sLevel = this.treedata[fi].child;
        var sLevelSelectedCount = 0;
        var sLevelChildNumber = 0
        if (fLevel.child.length == 0 && fLevel.roleId == null) {
            delete fLevel.child;
        } else if (sLevel) {
            /**************Second level****************/
            var sLevelDelArray = [];
            for (var si = 0; si < sLevel.length; si++) {
                var deletThisSLev = false;
                if (sLevel[si].child.length == 0 && sLevel[si].roleId == null) {
                    sLevel[si].displayCheckbox = false;
                    sLevelDelArray.push(sLevel[si].name);
                    sLevel[si].name = '';
                    sLevel[si].active = false;
                    delete sLevel[si].child;
                } else if (sLevel[si].child.length == 0) {
                    delete sLevel[si].child;
                } else {
                    /**************Third level****************/
                    var tLevel = sLevel[si].child;
                    var tLevelSelectedCount = 0;
                    var tLevelChildNumber = 0;
                    if (tLevel) {
                        var tLevelDelArray = [];
                        var tLevelLen = tLevel.length;
                        var tLevelRoleIdUndefined = 0;
                        for (var ti = 0; ti < tLevel.length; ti++) {
                            delete tLevel[ti].child;
                            if (tLevel[ti].roleId == null) {
                                tLevel[ti].displayCheckbox = false;
                                tLevelDelArray.push(tLevel[ti].name);
                                tLevel[ti].name = '';
                                tLevel[ti].active = false;
                                tLevelRoleIdUndefined++
                            } else {
                                if (tLevel[ti].isSelected)
                                    tLevelSelectedCount++;

                                if (tLevel[ti].displayCheckbox)
                                    tLevelChildNumber++;
                            }
                        }
                        if (tLevelRoleIdUndefined == tLevelLen)
                            deletThisSLev = true;
                        if (tLevelSelectedCount == tLevelChildNumber) {
                            sLevel[si].isSelected = true;
                            sLevel[si].indeterminate = false;
                            sLevel[si].active = true;
                        } else if (tLevelSelectedCount > 0) {
                            sLevel[si].indeterminate = true;
                            sLevel[si].active = true;
                        }

                        /*Cleanup unused third level items*/
                        for (var i = 0; i < tLevelDelArray.length; i++) {
                            var name = tLevelDelArray[i];
                            for (var ti = 0; ti < tLevel.length; ti++) {
                                if (name == tLevel[ti].text) {
                                    tLevel.splice(ti, 1);
                                    break;
                                }
                            }
                        }
                    }
                }
                if (deletThisSLev) { //remove the whole second level item if all it's child has no roleId
                    sLevel[si].displayCheckbox = false;
                    sLevelDelArray.push(sLevel[si].name);
                    sLevel[si].name = '';
                    sLevel[si].active = false;
                } else {
                    if (sLevel[si].isSelected)
                        sLevelSelectedCount++;
                    if (sLevel[si].displayCheckbox)
                        sLevelChildNumber++;
                }
            }
            if (sLevelSelectedCount == sLevelChildNumber && sLevelChildNumber != 0) {
                fLevel.isSelected = true;
                fLevel.indeterminate = false;
                fLevel.active = true;
            } else if (sLevelSelectedCount > 0) {
                fLevel.indeterminate = true;
                fLevel.active = true;
            } else {
                //fLevel.active=false;
                fLevel.indeterminate = false;
            }
            /*Cleanup unused second level items*/
            for (var i = 0; i < sLevelDelArray.length; i++) {
                var name = sLevelDelArray[i];
                for (var si = 0; si < sLevel.length; si++) {
                    if (name == sLevel[si].text) {
                        sLevel.splice(si, 1);
                        break;
                    }
                }
            }
        }
    }
}

  
checkTreeSelect() {
    if (this.treedata) {
        for (var fi = 0; fi < this.treedata.length; fi++) {
            var fLevel = this.treedata[fi];
            if (fLevel.isSelected) {
                return true;
            }
            var sLevel = fLevel.child;
            if (sLevel) {
                for (var si = 0; si < sLevel.length; si++) {
                    if (sLevel[si].isSelected) {
                        return true;
                    }
                    var tLevel = sLevel[si].child;
                    if (tLevel) {
                        for (var ti = 0; ti < tLevel.length; ti++) {
                            if (tLevel[ti].isSelected) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
    }
    return false;
  }

  getAppRoleIds() {
    this.notification.notifObj = {
        isCategoriesFunctionalMenu: false
    };
    this.notificationService.getAppRoleIds()
      .subscribe(_data => {
        this.result = _data;
        let actualData = [];
        var app_id_name_list = {};
        this.checkBoxObj = {
            isAnyRoleSelected: false
        };
        for (let i = 0; i < this.result.length; i++) {
            if (!(this.result[i].appId in app_id_name_list)) {
              app_id_name_list[this.result[i].appId] = this.result[i].appName;
            }
            this.result[i].child = [];
            this.result[i].name = this.result[i].roleName;
            this.result[i].displayCheckbox = true;
            this.result[i].id = this.result[i].roleId;
            this.result[i].menuId = this.result[i].roleId;
            this.result[i].parentMenuId = this.result[i].appId;
            this.result[i].can_check = true;
            this.result[i].roleId = [this.result[i].roleId];
            this.result[i].onSelect = function() {
              this.notification.anyTreeItemSelected = this.checkTreeSelect();
            };
            /*assigning selected value*/
            let intersectionObj: any; 
            intersectionObj = this.result[i].roleId & this.notification.notificationRoleIds;
            if (this.result[i].roleId && this.result[i].roleId.length == intersectionObj.length) {
                this.result[i].isSelected = true;
                this.result[i].selected = true;
                this.result[i].indeterminate = false;
            } else {
                /*default*/
                this.result[i].isSelected = false;
                this.result[i].selected = false;
                this.result[i].indeterminate = false;
            }       
        }

        for (var app_id in app_id_name_list) {

          let new_res = {
            'child': null,
            'name': null,
            'id': null,
            'displayCheckbox': null,
            'menuId': null,
            'parentMenuId': '',
            'appId': '',
            'can_check': null,
            'msgDescription': null,
            'roleIds': null,
            'roleId': null,
            'onSelect': null,
          };

          new_res.child = [];
          new_res.name = app_id_name_list[app_id];
          new_res.id = app_id;
          new_res.displayCheckbox = true;
          new_res.menuId = app_id;
          new_res.parentMenuId = null;
          new_res.appId = null;
          new_res.can_check = true;
          new_res.roleId = null;
          new_res.onSelect = function() {
              this.notification.anyTreeItemSelected = this.checkTreeSelect();
          };
          this.result.push(new_res);
        }

        let parentChildRoleIdDict = {};
        //Adding actual child items to child array in res objects
        for (let i = 0; i < this.result.length; i++) {
          let parentId = this.result[i].menuId;
          parentChildRoleIdDict[parentId] = [];
          for (let j = 0; j < this.result.length; j++) {
            let childId = this.result[j].parentMenuId;
            if (parentId == childId) {
              this.result[i].child.push(this.result[j]);
              if (this.result[j].roleId) {
                for (let k in this.result[j].roleId) {
                  parentChildRoleIdDict[parentId].push(this.result[j].roleId[k]);
                }
              }
            }
          }
        }

        //Forming actual parent items
        for (let i = 0; i < this.result.length; i++) {
          let parentId = this.result[i].parentMenuId;
          if (parentId === null) {
              actualData.push(this.result[i]);
          }
        }

        this.treedata = actualData;
        //setting correct parameters for b2b tree
        this.settingTreeParam();
        this.makeFinalTreeDataByProcessingAppRoleIds(this.treedata);

      }, error =>{
        console.log(error);
      });
  }

  makeFinalTreeDataByProcessingAppRoleIds(treeData_approleids){
    let datamap: any  = new Map();
    let data = treeData_approleids;
    
    if(data && data.length > 0){
      data.forEach(element => {
        let iskeyPresent: boolean = false;
        try{
          if(datamap.get(element.id)){
            iskeyPresent = true;
          }
        }catch(e){// 
        }
        if(iskeyPresent){
          let temp = datamap.get(element.id);
          temp.push(element);
          datamap.set(element.id,temp)
        }else{
          datamap.set(element.id,element);
        }  
      });
    }

    if(datamap && datamap.size > 0){
      let treeNode = {};
      datamap.forEach((value, key) => {
        if(value.child){
          let keyname = "";
          let childArray = [];
          for(let item of value.child){
            keyname = item.appName+"~"+'undefined';
            let childItem = item.roleName+"~"+item.roleId;
            childArray.push(childItem);
          }
          treeNode[keyname] = childArray;
        }
      });
      console.log("makeFinalTreeDataByProcessingAppRoleIds >>",treeNode);

      this.treeFlattener = new MatTreeFlattener(this.transformer, this.getLevel, this.isExpandable, this.getChildren);
      this.treeControl = new FlatTreeControl<TodoItemFlatNode>(this.getLevel, this.isExpandable);
      this.dataSource = new MatTreeFlatDataSource(this.treeControl, this.treeFlattener);
      const data = this.buildFileTree(treeNode, 0);
      this.dataChange.next(data);
      this.dataChange.subscribe(data => {
        this.dataSource.data = data;
      }); 
    }
  }

  
  /**
   * Prepare Tree datasoure using functional Menu Data.
   * @param functionMenuData 
   */
  populateTreeDataSourceByFunctionalMenu(functionMenuData: any){
    console.log("populateTreeDataSourceByFunctionalMenu :: ",functionMenuData);
    if(functionMenuData){
      let treeNode = {};
      functionMenuData.forEach(element => {
        let name = element.text+"~"+element.roleId;
        treeNode[name] = { };

        if(element.child && element.child.length >0){
          let childArray = [];
          element.child.forEach(element => {
            let childItem = element.text+"~"+element.roleId;
            childArray.push(childItem); 
          });
          treeNode[name] = childArray;
        }

      });
      console.log("treeNode>>",treeNode);

      this.treeFlattener = new MatTreeFlattener(this.transformer, this.getLevel, this.isExpandable, this.getChildren);
      this.treeControl = new FlatTreeControl<TodoItemFlatNode>(this.getLevel, this.isExpandable);
      this.dataSource = new MatTreeFlatDataSource(this.treeControl, this.treeFlattener);
      const data = this.buildFileTree(treeNode, 0);
      this.dataChange.next(data);
      this.dataChange.subscribe(data => {
        this.dataSource.data = data;
      }); 
    }
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
  
  
  /*************************************** mat-tree code start here ******************************/

  /** Map from flat node to nested node. This helps us finding the nested node to be modified */
  flatNodeMap = new Map<TodoItemFlatNode, TodoItemNode>();
  /** Map from nested node to flattened node. This helps us to keep the same object for selection */
  nestedNodeMap = new Map<TodoItemNode, TodoItemFlatNode>();
  /** A selected parent node to be inserted */
  selectedParent: TodoItemFlatNode | null = null;
  /** The new item's name */
  newItemName = '';
  treeControl: FlatTreeControl<TodoItemFlatNode>;
  treeFlattener: MatTreeFlattener<TodoItemNode, TodoItemFlatNode>;
  dataSource: MatTreeFlatDataSource<TodoItemNode, TodoItemFlatNode>;
  /** The selection for checklist */
  checklistSelection = new SelectionModel<TodoItemFlatNode>(true /* multiple */);
  getLevel = (node: TodoItemFlatNode) => node.level;
  isExpandable = (node: TodoItemFlatNode) => node.expandable;
  getChildren = (node: TodoItemNode): TodoItemNode[] => node.children;
  hasChild = (_: number, _nodeData: TodoItemFlatNode) => _nodeData.expandable;
  hasNoContent = (_: number, _nodeData: TodoItemFlatNode) => _nodeData.item === '';

  /**
   * Transformer to convert nested node to flat node. Record the nodes in maps for later use.
   */
  transformer = (node: TodoItemNode, level: number) => {
    const existingNode = this.nestedNodeMap.get(node);
    const flatNode = existingNode && existingNode.item === node.item
        ? existingNode
        : new TodoItemFlatNode();
    flatNode.item = node.item;
    flatNode.level = level;
    flatNode.expandable = !!node.children;
    flatNode.roleIds = node.roleIds;
    this.flatNodeMap.set(flatNode, node);
    this.nestedNodeMap.set(node, flatNode);
    return flatNode;
  }

  /** Whether all the descendants of the node are selected. */
  descendantsAllSelected(node: TodoItemFlatNode): boolean {
    const descendants = this.treeControl.getDescendants(node);
    const descAllSelected = descendants.every(child =>
      this.checklistSelection.isSelected(child)
    );
    return descAllSelected;
  }

  /** Whether part of the descendants are selected */
  descendantsPartiallySelected(node: TodoItemFlatNode): boolean {
    const descendants = this.treeControl.getDescendants(node);
    const result = descendants.some(child => this.checklistSelection.isSelected(child));
    return result && !this.descendantsAllSelected(node);
  }

  /** Toggle the to-do item selection. Select/deselect all the descendants node */
  todoItemSelectionToggle(node: TodoItemFlatNode): void {
    this.checklistSelection.toggle(node);
    const descendants = this.treeControl.getDescendants(node);
    this.checklistSelection.isSelected(node)
      ? this.checklistSelection.select(...descendants)
      : this.checklistSelection.deselect(...descendants);

    // Force update for the parent
    descendants.every(child =>
      this.checklistSelection.isSelected(child)
    );
    this.checkAllParentsSelection(node);
  }

  /** Toggle a leaf to-do item selection. Check all the parents to see if they changed */
  todoLeafItemSelectionToggle(node: TodoItemFlatNode): void {
    this.checklistSelection.toggle(node);
    this.checkAllParentsSelection(node);
  }

  /* Checks all the parents when a leaf node is selected/unselected */
  checkAllParentsSelection(node: TodoItemFlatNode): void {
    let parent: TodoItemFlatNode | null = this.getParentNode(node);
    while (parent) {
      this.checkRootNodeSelection(parent);
      parent = this.getParentNode(parent);
    }
  }

  /** Check root node checked state and change it accordingly */
  checkRootNodeSelection(node: TodoItemFlatNode): void {
    const nodeSelected = this.checklistSelection.isSelected(node);
    const descendants = this.treeControl.getDescendants(node);
    const descAllSelected = descendants.every(child =>
      this.checklistSelection.isSelected(child)
    );
    if (nodeSelected && !descAllSelected) {
      this.checklistSelection.deselect(node);
    } else if (!nodeSelected && descAllSelected) {
      this.checklistSelection.select(node);
    }
  }

  /* Get the parent node of a node */
  getParentNode(node: TodoItemFlatNode): TodoItemFlatNode | null {
    const currentLevel = this.getLevel(node);

    if (currentLevel < 1) {
      return null;
    }

    const startIndex = this.treeControl.dataNodes.indexOf(node) - 1;

    for (let i = startIndex; i >= 0; i--) {
      const currentNode = this.treeControl.dataNodes[i];

      if (this.getLevel(currentNode) < currentLevel) {
        return currentNode;
      }
    }
    return null;
  }


  /**
   * Build the file structure tree. The `value` is the Json object, or a sub-tree of a Json object.
   * The return value is the list of `TodoItemNode`.
   */
  buildFileTree(obj: {[key: string]: any}, level: number): TodoItemNode[] {
    return Object.keys(obj).reduce<TodoItemNode[]>((accumulator, key) => {
      const value = obj[key]; 
      const node = new TodoItemNode();
      node.item = key;
            
      if(key.indexOf("~") !== -1){
        let nodeDetails  = key.split("~");
        node.item = nodeDetails[0];
        if(nodeDetails[1]){
          node.roleIds = nodeDetails[1].split(",");
        }else{
          node.roleIds = [];
        }
      }

      if (value != null) {
        if (typeof value === 'object') {
          node.children = this.buildFileTree(value, level + 1);
        } else {
          node.item = value;
          if(value.indexOf("~") !== -1){
            let nodeDetails  = value.split("~");
            node.item = nodeDetails[0];
            if(nodeDetails[1]){
              node.roleIds = nodeDetails[1].split(",");
            }else{
              node.roleIds = [];
            }
          }
        }
      }

      return accumulator.concat(node);
    }, []);
  }
  /*************************************** mat-tree code end here ******************************/
}

/**
 * Node for to-do item
 */
export class TodoItemNode {
  children: TodoItemNode[];
  item: string;
  roleIds: string[];
}

/** Flat to-do item node with expandable and level information */
export class TodoItemFlatNode {
  item: string;
  level: number;
  expandable: boolean;
  roleIds: string[];
}