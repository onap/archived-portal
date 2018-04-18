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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
/**
 * Created by nnaffar on 12/21/15.+
 */
angular.module('ecompApp')
    .directive('multipleSelect', function ($window) {
        return {
            restrict: 'E',
            templateUrl: 'app/directives/multiple-select/multiple-select.tpl.html',
            scope: {
                onChange: '&',
                nameAttr: '@',
                valueAttr: '@',
                ngModel: '=',
                placeholder: '@',
                uniqueData: '@?',
                onDropdownClose: '&?'
            },
            link: function(scope, elm, attrs){
                scope.isExpanded = false;

                scope.isDisabled = !scope.ngModel || !scope.ngModel.length;
                scope.$watch('ngModel', function(newVal){
                    scope.isDisabled = !newVal || !newVal.length;
                });


                let startListening = () => {
                    console.log('listening on $window!');
                    angular.element($window).on('click', function () {
                        stopListening();
                    });

                    angular.element('multiple-select').on('click', function(e) {
                        if($(e.target).closest('multiple-select')[0].attributes['unique-data'].value === attrs.uniqueData){
                            console.log('ignored that..:', attrs.uniqueData);
                            e.stopPropagation();
                        }else{
                            console.log('shouldnt ignore, close expanded!:', attrs.uniqueData);
                            scope.isExpanded = false;
                            scope.$applyAsync();
                        }
                    });
                };

                let stopListening = function() {
                    if(scope.onDropdownClose){
                        scope.onDropdownClose();
                    }
                    scope.isExpanded = false;
                    scope.$applyAsync();
                    console.log('stop listening on $window and multiple-element!');
                    angular.element($window).off('click');
                    angular.element('multiple-select').off('click');
                };

                scope.showCheckboxes = function(){
                    scope.isExpanded = !scope.isExpanded;
                    if(scope.isExpanded){
                        startListening();
                    }else{
                        stopListening();
                        if(scope.onDropdownClose){
                            scope.onDropdownClose();
                        }
                    }
                };

                scope.onCheckboxClicked = function() {
                    console.log('checkbox clicked; unique data: ',attrs.uniqueData);
                    if(scope.onChange) {
                        scope.onChange();
                    }
                }

                scope.getTitle = function(){
                    var disp = '';
                    if(!scope.ngModel || !scope.ngModel.length) {
                        return disp;
                    }
                    scope.ngModel.forEach(function(item){
                        if(item[scope.valueAttr]){
                            disp+=item[scope.nameAttr] + ',';
                        }
                    });
                    if(disp!==''){
                        disp = disp.slice(0,disp.length-1);
                    }else{
                        disp = scope.placeholder;
                    }
                    return disp;
                };
                
                scope.getIdTitle = function(){
                    var disp = '';
                    if(!scope.ngModel || !scope.ngModel.length) {
                        return disp;
                    }
                    scope.ngModel.forEach(function(item){
                        if(item[scope.valueAttr]){
                            disp+=item[scope.nameAttr] + ',';
                        }
                    });
                    if(disp!==''){
                        disp = disp.slice(0,disp.length-1);
                    }else{
                        disp = scope.placeholder;
                    }
                    return disp+attrs.uniqueData;
                };
            }
        };
    });
