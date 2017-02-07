/*-
 * ================================================================================
 * eCOMP Portal
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ================================================================================
 */
'use strict';
(function () {
    class FunctionalMenuCtrl {
        constructor($log, functionalMenuService, $scope,ngDialog, confirmBoxService) {
            $log.info('FunctionalMenuCtrl init');

            $scope.invokeDialog = () => {
                alert("click dialog");
            };

            this.regenerateFunctionalMenuAncestors = () => {
                functionalMenuService.regenerateFunctionalMenuAncestors().then(res => {
                    $log.debug("FunctionalMenuCtrl:regenerateFunctionalMenuAncestors::returned from regenerateFunctionalMenuAncestors API call");
                    confirmBoxService.showInformation('You have successfully regenerated the menu.').then(isConfirmed => {
                    });
                })['catch'](function (err) {
                    $log.error("FunctionalMenuCtrl:regenerateFunctionalMenuAncestors:: error: " + err);
                    confirmBoxService.showInformation('There was an error while regenerating the menu.').then(isConfirmed => {
                    });
                });
            };

            let getFunctionalMenu = () => {
                this.isLoadingTable = true;
                functionalMenuService.getManagedFunctionalMenu().then(res => {

                    let actualData=[];

                    for(let i = 0; i < res.length; i++){
                        res[i].children=[];
                        res[i].label=res[i].text;
                        res[i].id=res[i].text;

                    }

                    for(let i = 0; i < res.length; i++){

                        let parentId=res[i].menuId;
                        for(let j = 0; j < res.length; j++){
                            let childId=res[j].parentMenuId;
                            if(parentId==childId){
                                res[i].children.push(res[j]);

                            }
                        }
                    }

                    res.sort(function(a, b) {
                        return a.column-b.column;
                    })

                    for(let i = 0; i < res.length; i++){
                        res[i].children.sort(function(a, b){
                            return a.column-b.column;
                        })
                    }

                    for(let i = 0; i < res.length; i++){
                        let parentId=res[i].parentMenuId;
                        if(parentId==null){
                            actualData.push(res[i]);
                        }
                    }

                    $scope.treedata = actualData;

                }).catch(err => {
                    $log.error('FunctionalMenuCtrl:getFunctionalMenu:: error ',err);
                }).finally(()=> {
                    this.isLoadingTable = false;
                });

            };


            let init = () => {
                this.isLoadingTable = false;
                this.functionalMenu = [];
                getFunctionalMenu();
                this.searchString = '';
            };

            this.filterByDropdownValue = item => {
                if(this.filterByApp.value === ''){
                    return true;
                }
                return item.appName === this.filterByApp.value;
            };

            let getDialogTitle = (source) => {
                switch (source) {
                    case 'edit':
                        return "Functional Menu - Edit";
                    case 'view':
                        return "Functional Menu - View";
                    case 'add':
                        return "Functional Menu - Add";
                    default:
                        return "Functional Menu";
                }
            }

            $scope.reloadTreeStructure = (selectedItem,source) => {
                getFunctionalMenu();
            };
            $scope.openMenuDetailsModal = (selectedItem,source) => {
                let data = null;
                let selectedMenuDetails = null;
                console.log('selectedItem: ', selectedItem);

                functionalMenuService.getMenuDetails(selectedItem.menuId)
                    .then(function( resp ){
                        selectedMenuDetails = resp;
                        $log.info('FunctionalMenuCtrl::openMenuDetailsModal: getMenuDetails: ', resp );

                        if(selectedItem){
                            data = {
                                menuItem: {menu: _.clone(selectedItem),menuDetails:_.clone(selectedMenuDetails)},
                                source: source,
                                title: getDialogTitle(source)
                            }
                        }
                        ngDialog.open({
                            templateUrl: 'app/views/functionalMenu/functionalMenu-dialog/menu-details.modal.html',
                            controller: 'MenuDetailsModalCtrl',
                            controllerAs: 'functionalMenuDetails',
                            data: data
                        }).closePromise.then(needUpdate => {
                            if(needUpdate.value === true){
                                $log.debug('FunctionalMenuCtrl::openMenuDetailsModal: updating table data...');
                                if(source=="edit")
                                    init();
                            }
                        });
                    });
            };


            $scope.createNewMenuItem = (selectedItem,source) => {

                if(selectedItem != null && selectedItem.getLevel() >= 4){
                    confirmBoxService.showInformation('You are not allowed to have a menu item at a level greater than 4.').then(isConfirmed => {

                    });
                    return ;
                }

                let data = null;
                let selectedMenuDetails = null;
                functionalMenuService.getMenuDetails(selectedItem.menuId)
                    .then(function( resp ){
                        selectedMenuDetails = resp;

                        if((selectedItem.children==null || !selectedItem.children.length>0) &&
                            (!!selectedMenuDetails.url || !!selectedMenuDetails.appid || !!selectedMenuDetails.roles)){
                            confirmBoxService.showInformation('Warning: the child menu item "' + selectedItem.name + '" is already configured with an application. You can create a new mid-level menu item, and move this item under it.').then(isConfirmed => {
                                return;
                            });
                        }else{
                            if(selectedItem){
                                data = {
                                    menuItem: {menu: _.clone(selectedItem)},
                                    source:source,
                                    title: getDialogTitle(source)
                                }
                            }

                            ngDialog.open({
                                templateUrl: 'app/views/functionalMenu/functionalMenu-dialog/menu-details.modal.html',
                                controller: 'MenuDetailsModalCtrl',
                                controllerAs: 'functionalMenuDetails',
                                data: data
                            }).closePromise.then(needUpdate => {
                                if(needUpdate.value === true){
                                    $log.debug('FunctionalMenuCtrl::getMenuDetails: updating table data...');
                                    init();
                                }
                            });
                        }
                    });
            };

            $scope.deleteMenuItem = (selectedItem,source) => {
                $log.info('FunctionalMenuCtrl:deleteMenuItem:: delete selectedItem: ', selectedItem);

                if(selectedItem.children!=null && selectedItem.children.length>0){
                    confirmBoxService.showInformation('You are not allowed to delete a menu item that has children. You can only delete leaf menu items.').then(isConfirmed => {

                    });
                }else{
                    confirmBoxService.deleteItem(selectedItem.name).then(isConfirmed => {
                        if(isConfirmed){
                            $log.info('FunctionalMenuCtrl:deleteMenuItem:: Deleting Menu Item :: name: '+selectedItem.name+'; menuId: '+selectedItem.menuId);
                            $log.info('FunctionalMenuCtrl:deleteMenuItem:: selectedItem: ', selectedItem);

                            functionalMenuService.deleteMenuItem(selectedItem.menuId).then(() => {
                                $log.info('FunctionalMenuCtrl:deleteMenuItem:: Removed Menu Item :: '+selectedItem.name);
                                init();
                            }).catch(err => {
                                $log.error(err);
                            });
                        }
                    }).catch(err => {
                        $log.error(err);
                    });
                }
            };

            init();
        }
    }
    FunctionalMenuCtrl.$inject = ['$log', 'functionalMenuService','$scope', 'ngDialog', 'confirmBoxService'];
    angular.module('ecompApp').controller('FunctionalMenuCtrl', FunctionalMenuCtrl);

    angular.module('ecompApp').directive('jqTree', ['functionalMenuService','$log','confirmBoxService',function(functionalMenuService,$log,confirmBoxService){
        return {
            templateUrl: 'jqtree-tmpl.html',
            link: function(scope, el, attrs){

                var $jqTree =  el.find('#jqTree').tree({
                    data: scope.treedata,
                    autoOpen: false,
                    dragAndDrop: true,
                    onCreateLi: function(node, $li) {
                        $li.attr('id', node.id.replace(/\s+/g,"_"));
                    }
                });

                el.find('#jqTree').bind('tree.move',  function(event){
                    event.preventDefault();
                    console.log('moved_node', event.move_info.moved_node);
                    console.log('target_node', event.move_info.target_node);
                    console.log('position', event.move_info.position);
                    console.log('previous_parent', event.move_info.previous_parent);



                    if(event.move_info.target_node != null &&
                        ((event.move_info.position == 'after' && event.move_info.target_node.getLevel() > 4) ||
                        (event.move_info.position == 'inside' && event.move_info.target_node.getLevel() > 3))){
                        confirmBoxService.showInformation('You are not allowed to have a menu item at a level greater than 4.').then(isConfirmed => {

                        });
                        return ;
                    }

                    var confMsg = 'Are you sure you want to move "'+event.move_info.moved_node.name+'" ?';
                    if ((event.move_info.position == "inside") && (event.move_info.target_node.url != "")) {
                        confMsg = 'Warning: You are moving "'+event.move_info.moved_node.name+'" under "'+event.move_info.target_node.name+'", which has application information associated with it. This will cause all the application information from "'+event.move_info.target_node.name+'" to be deleted.';
                    }
                    confirmBoxService.moveMenuItem(confMsg).then(isConfirmed => {
                        if(isConfirmed){
                            var new_column = event.move_info.target_node.column;
                            var old_column = event.move_info.moved_node.column;
                            if ((event.move_info.moved_node.parentMenuId !=
                                event.move_info.target_node.parentMenuId) ||
                                (new_column < old_column)
                            ) {
                                new_column += 1;
                            }
                            var activeMenuItem = {
                                menuId:event.move_info.moved_node.menuId,
                                column:new_column,
                                text:"",
                                parentMenuId:event.move_info.target_node.parentMenuId,
                                url:"",
                                appid: null,
                                roles:null
                            };

                            if (event.move_info.position == "inside") {
                                console.log("special case: target_node is parent");
                                activeMenuItem.column = 1;
                                activeMenuItem.parentMenuId = event.move_info.target_node.menuId;
                            }

                            functionalMenuService.saveEditedMenuItem(activeMenuItem)
                                .then(() => {
                                    $log.debug(' Menu Item moved');
                                    scope.reloadTreeStructure();
                                }).catch(err => {
                                $log.error(err);
                            }).finally(()=>{
                            });
                        }
                    }).catch(err => {
                        $log.error(err);
                    });
                });


                $jqTree.jqTreeContextMenu(el.find('ul.dropdown-menu'), {
                    "view": function (node) {scope.openMenuDetailsModal(node,'view'); },
                    "edit": function (node) {scope.openMenuDetailsModal(node,'edit'); },
                    "delete": function (node) { scope.deleteMenuItem(node,'delete') },
                    "add": function (node) {  scope.createNewMenuItem(node,'add') }
                });

                scope.$watch('treedata', function(oldValue, newValue){
                    if(oldValue !== newValue){
                        console.log('FunctionalMenuCtrl:: Tree value has changed in some way');
                        $jqTree.tree('loadData',  scope.treedata);
                        $jqTree.tree('reload', function() {
                            console.log('FunctionalMenuCtrl:: Tree is reloaded');
                        });
                    }
                })
            }
        }
    }]);

})();
