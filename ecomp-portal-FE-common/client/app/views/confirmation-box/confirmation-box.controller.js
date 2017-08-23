/*-
 * ================================================================================
 * ECOMP Portal
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
/**
 * Created by nnaffar on 1/18/16.
 */
'use strict';
(function () {
    class ConfirmationBoxCtrl {
        constructor($scope,$state,message,$modalInstance) {
        	$scope.message = message;
        	/*let init = () => {
                let item = ($scope.message && $scope.message.item) || 'this';
                message = $scope.message.content ? $scope.message.content : `Are you sure you want to delete "${message.item}"?`;
                this.title = $scope.message.title ? $scope.message.title : '';
            };*/

            this.closeBox = isConfirmed => {
                $scope.closeThisDialog(isConfirmed);
            };
            
            this.goTo = (state, params) => {
            	$scope.closeThisDialog(false);
            	$state.go(state,params);
            	
            };
            
            $scope.ok =function(confirm){
            	$modalInstance.close(confirm);
            }

          //  init();
        }
    }
    ConfirmationBoxCtrl.$inject = ['$scope','$state','message','$modalInstance'];
    angular.module('ecompApp').controller('ConfirmationBoxCtrl', ConfirmationBoxCtrl);
})();
