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
    class ConfirmationBoxCtrl {
        constructor($scope) {

            let init = () => {
                let item = ($scope.ngDialogData && $scope.ngDialogData.item) || 'this';
                this.message = $scope.ngDialogData.message ? $scope.ngDialogData.message : `Are you sure you want to delete "${item}"?`;
                this.title = $scope.ngDialogData.title ? $scope.ngDialogData.title : '';
            };

            this.closeBox = isConfirmed => {
                $scope.closeThisDialog(isConfirmed);
            };

            init();
        }
    }
    ConfirmationBoxCtrl.$inject = ['$scope'];
    angular.module('ecompApp').controller('ConfirmationBoxCtrl', ConfirmationBoxCtrl);
})();
