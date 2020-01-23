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
import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FunctionalMenuService } from 'src/app/shared/services';
import { ConfirmationModalComponent } from 'src/app/modals/confirmation-modal/confirmation-modal.component';
import { FunctionalMenuDialogComponent } from './functional-menu-dialog/functional-menu-dialog.component';


@Component({
  selector: 'app-functional-menu',
  templateUrl: './functional-menu.component.html',
  styleUrls: ['./functional-menu.component.scss']
})
export class FunctionalMenuComponent implements OnInit {

  result: any;
  functionalMenu: any = [];
  treedata = [];
  isEditMode: boolean;
  operationName: string;
  self: any;
  constructor(public functionalMenuService : FunctionalMenuService, public ngbModal: NgbModal) { }

  ngOnInit() {
    this.self = this;
    this.functionalMenu = [];
    this.getFunctionalMenu();

  }

  /**
   * regenerateFunctionalMenuAncestors
   */
  regenerateFunctionalMenuAncestors(){
    this.functionalMenuService.regenerateFunctionalMenuAncestors()
    .subscribe(_data => {
        this.result = _data
        if(this.result){
          const modalRef = this.ngbModal.open(ConfirmationModalComponent);
          modalRef.componentInstance.title = "";
          modalRef.componentInstance.message = 'You have successfully regenerated the menu.';
          modalRef.result.then((result) => { }, (resut) => {return;});
          this.getFunctionalMenu();
        }
    }, error =>{
      console.log(error);
      const modalRef = this.ngbModal.open(ConfirmationModalComponent);
      modalRef.componentInstance.title = "";
      modalRef.componentInstance.message = 'There was an error while regenerating the menu.';
      modalRef.result.then((result) => { }, (resut) => {return;});
    });
  }

  /**
   * getFunctionalMenu
   */
  getFunctionalMenu(){
    let actualData=[];
    this.functionalMenuService.getManagedFunctionalMenu()
    .subscribe(_data => {
        this.result = _data;
        if(this.result){
          for(let i = 0; i < this.result.length; i++){
            this.result[i].children=[];
            this.result[i].label= this.result[i].text;
            this.result[i].id= this.result[i].text;
          }
          //Adding actual child items to children array in res objects
          for(let i = 0; i < this.result.length; i++){
             let parentId=this.result[i].menuId;
             for(let j = 0; j < this.result.length; j++){
                let childId=this.result[j].parentMenuId;
                if(parentId===childId){
                  this.result[i].children.push(this.result[j]);
                }
             }
          }
           // Sort the top-level menu items in order based on the column
          this.result.sort(function(a, b) {
            return a.column-b.column;
          });

          // Sort all the children in order based on the column
          for(let i = 0; i <  this.result.length; i++){
              this.result[i].children.sort(function(a, b){
                  return a.column-b.column;
              });
          }

          //Forming actual parent items
          for(let i = 0; i <  this.result.length; i++){
              let parentId= this.result[i].parentMenuId;
              if(parentId===null){
                  actualData.push( this.result[i]);
              }
          }

          this.treedata = actualData;
          //console.log("this.treedata :: ",this.treedata);
          
          if(this.treedata){
            this.buildTree(this.treedata,this.ngbModal, this.self);
          }
          
       }
    }, error =>{
      console.log(error);
    });

  }

  /**
   * buildTree
   * @param treedataarray 
   * @param ngbModal 
   */
  buildTree(treedataarray,ngbModal: NgbModal , _self){
    //console.log("treedataarray>>>>",treedataarray);
    $(function() {
        $('#jqTree').tree('loadData', treedataarray);
        $('#jqTree').tree({
            data: treedataarray,
            autoOpen: false,
            dragAndDrop: true,
            onCreateLi: function(node, $li) {
                ////console.log("node >>",node);
            }
        }).on(
          'tree.contextmenu',
          function(event:any) {
              // The clicked node is 'event.node'
              var node = event.node;
              openMenuDetailsModal(node, "view");
          }
        );

        var openMenuDetailsModal = function(node: any, actionName: string ){
          const modalRef = ngbModal.open(FunctionalMenuDialogComponent, { size: 'lg' });
          modalRef.componentInstance.title = 'Functional Menu ',actionName;
          if(node != 'undefined' && node){
            modalRef.componentInstance.nodedata = node;
            modalRef.componentInstance.operationName = actionName;
          }else{
            modalRef.componentInstance.nodedata  = {};
          }
          modalRef.componentInstance.passEntry.subscribe((receivedEntry: any) => {
            //console.log("receivedEntry>>>>",receivedEntry);
            ngbModal.dismissAll();
            if(receivedEntry.httpStatusCode===200){
              _self.getFunctionalMenu();
            }
          });
        }
     });
  }
}
