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
 
import { Component, OnInit, Input, Output, EventEmitter  } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FunctionalMenuService } from 'src/app/shared/services';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { InformationModalComponent } from 'src/app/modals/information-modal/information-modal.component';

@Component({
  selector: 'app-functional-menu-dialog',
  templateUrl: './functional-menu-dialog.component.html',
  styleUrls: ['./functional-menu-dialog.component.scss']
})
export class FunctionalMenuDialogComponent implements OnInit {

  @Input() nodedata: any;
  @Input() operationName: string;
  @Output() passEntry: EventEmitter<any> = new EventEmitter();
  isEditMode: boolean = false;
  isViewMode: boolean = false;
  isAddItemMode: boolean = false;
  selectedItem: any;
  result: any;
  availableRoles: any;
  preSelectedRoles: any;
  isAllApplications: boolean  = false;
  availableApps: any
  selectedRole: any = [];
  selectedApp={
    index:null,
    isDisabled: null
  };
  selectedAppIndex: any;
  menutitle:string;
  menuLocation:string;
  nodedetails: any;
  isParentDisable:boolean =true;
  hideRoleField:boolean = true;
  conflictMessages = {};
  functionalMenuForm = {};
  
  constructor(public functionalMenuService : FunctionalMenuService, public ngbModal: NgbModal, public activeModal: NgbActiveModal) { }

  ngOnInit() {
    //console.log("nodedata in dialog ",this.nodedata);
    this.nodedetails = Object.assign({}, this.nodedata);
    this.isViewMode = true;
    this.selectedItem = this.nodedata;
    this.selectedRole = [];
    this.availableRoles = [];
    if(this.nodedata && (this.isViewMode || this.isEditMode) && this.isLeafMenuItem(this.nodedetails)){
        this.selectedApp.index = this.nodedetails.appid;
        this.selectedAppIndex=this.nodedetails.appid;
        this.getAvailableRoles(this.selectedAppIndex);
    }
    
    if(this.isViewMode || this.isEditMode){
         this.nodedetails.menutitle = this.nodedetails.name;
         this.nodedetails.menuLocation = this.isParentMenuItem(this.nodedata) ? this.nodedata.name : this.nodedata.parent.name;
    }else{
        this.nodedetails.menutitle = '';
        this.nodedetails.menuLocation = this.nodedata.name;
    }
    this.nodedetails.selectedAppIndex = (this.selectedItem.appid) ? this.selectedItem.appid : 0;
    this.getAvailableApps();
    if(this.selectedItem.appid && this.selectedItem.appid >0){
      this.getAvailableRoles(this.selectedItem.appid);
    }
  }

  switchToEditMode(){
    //console.log("switchToEditMode :: ",this.nodedata);
    this.isViewMode = false;
    this.isEditMode = true;
    this.isParentDisable =true;
    this.nodedetails.name = this.selectedItem.name;
    this.nodedetails.url = this.selectedItem.url;
    this.nodedetails.selectedAppIndex = (this.selectedItem.appid) ? this.selectedItem.appid : 0;
    this.nodedetails.selectedRole = (this.selectedItem.roles) ? this.selectedItem.roles : 0;
  }

  switchToAddMode(){
    //console.log("switchToAddMode :: ",this.nodedata);
    if(this.selectedItem != null && this.selectedItem.getLevel() >= 4){
      this.openConfirmationModal("","You are not allowed to have a menu item at a level greater than 4.");
      return ;
    }
    //this.isViewMode = false;
    this.isViewMode = false;
    this.isEditMode = true;
    this.isAddItemMode = true;
    this.nodedetails.name = "";
    this.nodedetails.url = "";
    this.nodedetails.selectedAppIndex = 0;
    this.nodedetails.selectedRole = 0;
  }

  /**
   * deleteMenuItem
   * @param selectedItem 
   */
  deleteMenuItem(){
    if(this.selectedItem.children!=null && this.selectedItem.children.length>0){
      const modalRef = this.ngbModal.open(ConfirmationModalComponent);
      modalRef.componentInstance.title = "";
      modalRef.componentInstance.message = 'You are not allowed to delete a menu item that has children. You can only delete leaf menu items.';
      modalRef.result.then((result) => { }, (resut) => {return;});
    }else{
      const modalRef = this.ngbModal.open(InformationModalComponent);
      modalRef.componentInstance.title = "Confirmation";
      modalRef.componentInstance.message = 'Are you sure you want to delete '+ this.selectedItem.name+' ?';
      modalRef.result.then((result) => {
        if (result === 'Ok') {
          this.functionalMenuService.deleteMenuItem(this.selectedItem.menuId)
          .subscribe(_data => {
              this.result = _data
              this.passEntry.emit(this.result);
              let successMsg = "Item Deleted Successfully";
              this.openConfirmationModal("Success",successMsg);
              this.ngbModal.dismissAll();
          }, error =>{
            console.log(error);
            let deleteErrorMsg  = 'There was an error while deleting the item.'+error.message;
            this.openConfirmationModal("Error",deleteErrorMsg);
            return;
          });
        }
      }, (resut) => {
        
      })
    }
  }

  updateSelectedApp(appid){
    //console.log("updateSelectedApp called with appId :: ",appid);
    if (!appid) {
      return;
    }
    this.getAvailableRoles(appid);
  }

  getAvailableRoles(appid){
    //console.log("getAvailableRoles called with appId :: ",appid);
    if (appid != null && appid >0) {
      this.functionalMenuService.getManagedRolesMenu(appid)
      .subscribe(rolesObj => {
        this.availableRoles = rolesObj;
        if(this.availableRoles && this.availableRoles.length >0){
          this.hideRoleField = false;
        }
        this.preSelectedRoles = {roles:[]};
        this.preSelectedRoles = {roles:[]};

        if((this.isEditMode) && this.isMidLevelMenuItem(this.nodedata)){
            // in Edit flow , for Midlevel menu item no need to preSelect.
            this.preSelectedRoles = {roles:[]};
        }else if(this.nodedata && this.isEditMode && this.isLeafMenuItem(this.nodedata) && this.nodedata.appid!=appid) {
            // in Edit flow , for LeafMenuItem, if appid changed then no need to preSelect.
            this.preSelectedRoles = {roles:[]};
        }else{
            if(this.nodedata && this.nodedata.roles){
                for(var i=0; i< this.nodedata.roles.length; i++){
                    var role = {"roleId": this.nodedata.roles[i]};
                    this.preSelectedRoles.roles.push(role);
                }
            }
        }
        
        if(this.nodedata.rolesObj){
          for(var i=0; i< this.nodedata.rolesObj.length;i++){
            //this.availableRoles[i].isApplied = false;
            for(var j=0;j<this.preSelectedRoles.roles.length;j++){
              if(this.preSelectedRoles.roles[j].roleId==this.availableRoles[i].roleId){
                this.availableRoles[i].isApplied=true;
                this.nodedetails.selectedRole = (this.preSelectedRoles.roles[j]) ? this.preSelectedRoles.roles[j] : 0;
                break;
              }
          }
          }
        }
      }, error =>{
        console.log(error);
        let errorMsg = 'There was an error while gettting available roles. ' + error.message;
        this.openConfirmationModal("",errorMsg);
        return;
      });
      //console.log("this.availableRoles >>>>>",this.availableRoles);
    }else{
      console.log("FunctionalMenuDialogComponent::getAvailableRoles: appid was null or -1");
    }
  }

  getAvailableApps(){
    this.isAllApplications = true;
    this.functionalMenuService.getAvailableApplications()
      .subscribe(apps => {
        this.availableApps = apps;
        if (this.nodedetails && this.nodedetails.index) {
          for (var i = 0; i < this.availableApps.length; i++) {
              if (apps[i].index === this.nodedetails.index) {
                  //console.log("MenuDetailsModalCtrl::getAvailableApps: found app with index: " + this.nodedetails.index);
                  //console.log("MenuDetailsModalCtrl::getAvailableApps: setting isDisabled to: " + !apps[i].enabled);
                  this.nodedetails.isDisabled = !apps[i].enabled;
                  break;
              }
          }
          //console.log("didn't find index: " + this.nodedetails.index);
        }
      }, error =>{
        console.log(error);
        this.isAllApplications = false;
        let errorMsg = 'There was a problem retrieving the Applications. '+error.message;
        this.openConfirmationModal("Error",errorMsg);
      });
  }

  isLeafMenuItem(menu: any){
      return menu.children.length>0 ? false : true;
  }

  isMidLevelMenuItem(menu: any){
      return menu.parentMenuId!=null && menu.children.length>0 ? true : false;
  }

  isParentMenuItem(menu: any){
      return menu.parentMenuId!=null ? false : true;
  };

 isRoleSelected(){
   var selectedRoleIds=[];
   for(var i=0;i<this.availableRoles.length;i++){
       if(this.availableRoles[i].isApplied){
          selectedRoleIds.push(this.availableRoles[i].roleId);
          return true;
       }
     }
   return false;
  }  

  getDialogTitle = (source) => {
    switch (source) {
      case 'edit':
          return "Functional Menu - Edit";
      case 'view':
          return "Functional Menu - View";
      case 'add':
          return "Functional Menu - Add";
      default:
          return "Functional Menu";
    };
  }

  saveChanges(){
    if(!this.nodedetails.menuLocation || !this.nodedetails.name 
        || !this.nodedetails.url || !this.nodedetails.selectedAppIndex 
        || !this.nodedetails.selectedAppIndex || !this.nodedetails.selectedRole){
      this.openConfirmationModal("","All fields are mandatory, please provide inputs for all the fields.");
      return;
    }
    /*
    if(!!this.nodedetails.url && (!this.selectedApp || this.selectedAppIndex <=0)) {
      this.openConfirmationModal("","Please select the appropriate app, or remove the url");
      return;
    }else if(!this.nodedetails.url && (this.selectedApp) && this.selectedApp.index>0){
        this.openConfirmationModal("","Please enter url, or select No Application");
        return;
    }else if(!this.nodedetails.menutitle){
        this.openConfirmationModal("","Please enter the Menu title");
        return;
    }
    */
    
    if(this.isAddItemMode){
      if(this.selectedItem != null && this.selectedItem.getLevel() >= 4){
        this.openConfirmationModal("","You are not allowed to have a menu item at a level greater than 4.");
        return ;
      }else{
        let data = null;
        let selectedMenuDetails = null;
        this.functionalMenuService.getFunctionalMenu(this.selectedItem.menuId)
          .subscribe(_data => {
              selectedMenuDetails = _data
              if((this.selectedItem.children===null || this.selectedItem.children.length == 0) && (!!selectedMenuDetails.url
                 || !!selectedMenuDetails.appid || !!selectedMenuDetails.roles)){
                let warning_message = 'Warning: the child menu item "' + 
                              this.selectedItem.name + '" is already configured with an application. You can create a new mid-level menu item.';
                this.openConfirmationModal("",warning_message);
                return;
              }else{
                if(this.selectedItem){
                    var selectedRoleIds=[];
                    //console.log("Selected Role ID ; ",this.nodedetails.selectedRole);
                    if(this.nodedetails.selectedRole){
                      selectedRoleIds.push(this.nodedetails.selectedRole);
                    }
                    
                    let applicationid: any = null;
                    if(!this.nodedata){
                      applicationid = null;
                    }else{
                      applicationid = this.nodedata.appid;
                    }
                    var newMenuItem = {
                        menuId:null, // this is a new menu item
                        column:this.nodedata.column,
                        text:this.nodedetails.name,
                        // We are creating this new menu item under the menu item that was clicked on.
                        parentMenuId:this.nodedata.menuId,
                        url:(this.nodedetails.url) ? this.nodedetails.url : null,
                        appid:(this.nodedetails.selectedAppIndex) ? this.nodedetails.selectedAppIndex: null,
                        roles:selectedRoleIds
                    };
                    //console.log("Add menu Item newMenuItem :: ",newMenuItem)
                    this.functionalMenuService.saveMenuItem(newMenuItem)
                    .subscribe(_data => {
                        this.result = _data
                        //console.log("add menu item response :: ",_data);
                        this.passEntry.emit(this.result);
                        let successMsg = "Item added successfully";
                        this.openConfirmationModal("Success",successMsg);
                    }, error =>{
                      console.log(error);
                      if(error.status === 409){//Conflict
                        this.handleConflictErrors(error);
                      } else {
                        let errorMsg = "There was a problem saving your menu. Please try again later. Error Status: "+error.status
                        this.openConfirmationModal("",errorMsg);
                        return;
                      }
                   });
                }
              }
          }, error =>{
            //console.log(error);
            let errorMsg = "There was a problem saving your menu. Please try again later. Error Status: "+error.status
            this.openConfirmationModal("",errorMsg );
            return;
        });
      }
    }else{
      //edit mode..
      //console.log('MenuDetailsModalCtrl::saveChanges: Will be saving an edit menu item');
      var selectedRoleIds=[];
      //console.log("Selected Role ID ---->>>> ",this.nodedetails.selectedRole);
      if(this.nodedetails.selectedRole){
        selectedRoleIds.push(this.nodedetails.selectedRole);
      }
      let activeMenuItem = {
          menuId:this.nodedata.menuId,
          column:this.nodedata.column,
          text:this.nodedetails.name,
          parentMenuId:this.nodedata.parentMenuId,
          url:(this.nodedetails.url) ? this.nodedetails.url : null,
          appid: (this.nodedetails.selectedAppIndex) ? this.nodedetails.selectedAppIndex: null,          
          roles:selectedRoleIds
      };
      if ((activeMenuItem.appid==null && activeMenuItem.url=="") || (activeMenuItem.appid==null && activeMenuItem.url=="undefined")) {
        activeMenuItem.roles = null;
      }
      //console.log("Update menu Item activeMenuItem :: ",activeMenuItem);
      this.functionalMenuService.saveEditedMenuItem(activeMenuItem)
      .subscribe(_data => {
          this.result = _data
          //console.log("Edit menu item response :: ",_data);
          this.passEntry.emit(this.result);
          let successMsg = "Item updated successfully";
          this.openConfirmationModal("Success",successMsg);
          //this.ngbModal.dismissAll();
      }, error =>{
        //console.log(error);
        let errorMsg = "There was a problem updating your menu. Please try again later. Error Status: "+error.status;
        this.openConfirmationModal("",errorMsg);
        return;
      });
    } 
  }

  //This part handles conflict errors (409)
  handleConflictErrors(error){
    if(!error.data){
      return;
    }
    if(!error.data.length){ //support objects
      error.data = [error.data]
    }
    //console.log('MenuDetailsModalCtrl::handleConflictErrors: err.data = ' + JSON.stringify(error.data));
    if(error.data){
      error.data.forEach(item => {
         //set conflict message
         this.conflictMessages[item.field.name] = item.errorCode;
         //set field as invalid
         //console.log('MenuDetailsModalCtrl::handleConflictErrors: fieldName = ' + item.field.name);
         this.functionalMenuForm[item.field.name].$setValidity('conflict', false);
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
}
