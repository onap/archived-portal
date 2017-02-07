/**
* FileName ui-gridster
* Version 0.1.0
* Build number 81850
* Date 03/14/2016
*/


(function(angular, window){
angular.module("att.gridster", ["att.gridster.utilities","att.gridster.gridster"]);
angular.module('att.gridster.utilities', [])
    .factory('$extendObj', [function() {
        var _extendDeep = function(dst) {
            angular.forEach(arguments, function(obj) {
                if (obj !== dst) {
                    angular.forEach(obj, function(value, key) {
                        if (dst[key] && dst[key].constructor && dst[key].constructor === Object) {
                            _extendDeep(dst[key], value);
                        } else {
                            dst[key] = value;
                        }
                    });
                }
            });
            return dst;
        };
        return {
            extendDeep: _extendDeep
        };
    }])
    .filter('unsafe', ['$sce', function($sce) {
        return function(val) {
            return $sce.trustAsHtml(val);
        };
    }]);

angular.module('att.gridster.gridster', ['attGridsterLib', 'att.gridster.utilities', 'ngSanitize'])
        .config(['$compileProvider', function($compileProvider) {
                $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|javascript):/);
            }])
        .constant('attGridsterConfig',
                {
                    columns: 3,
                    margins: [10, 10],
                    outerMargin: true,
                    pushing: true,
                    floating: true,
                    swapping: true,
                    draggable: {
                        enabled: true
                    }
                })
        .directive('attGridster', ['attGridsterConfig', '$extendObj', function(attGridsterConfig, $extendObj) {
                return {
                    restrict: 'EA',
                    scope: {
                        attGridsterOptions: '=?'
                    },
                    templateUrl: 'template/gridster/gridster.html',
                    replace: false,
                    transclude: true,
                    controller: [function() {}],
                    link: function(scope) {
                        if (angular.isDefined(scope.attGridsterOptions)) {
                            attGridsterConfig = $extendObj.extendDeep(attGridsterConfig, scope.attGridsterOptions);
                        }
                        scope.attGridsterConfig = attGridsterConfig;
                    }
                };
            }])
        .directive('attGridsterItem', ['$timeout', function($timeout) {
                return {
                    restrict: 'EA',
                    require: ['^attGridster'],
                    scope: {
                        attGridsterItem: '='
                    },
                    templateUrl: 'template/gridster/gridsterItem.html',
                    replace: false,
                    transclude: true,
                    controller: [function() {}]
                };
            }])
        .directive('attGridsterItemHeader', [function() {
                return {
                    restrict: 'EA',
                    require: ['^attGridsterItem'],
                    scope: {
                        headerText: '@',
                        headerIcon: '@',
                        subHeaderText: '@?'
                    },
                    templateUrl: 'template/gridster/gridsterItemHeader.html',
                    replace: true,
                    transclude: true,
                    link: function(scope, element, attr) {
                        if (attr.gripsImgPath) {
                            scope.gripsImgPath = attr.gripsImgPath;
                        } else {
                            scope.gripsImgPath = 'images/grips.png';
                        }

                        if (angular.isDefined(scope.subHeaderText) && scope.subHeaderText) {
                            angular.element(element[0].querySelector('span.gridster-item-sub-header-content')).attr("tabindex", "0");
                            angular.element(element[0].querySelector('span.gridster-item-sub-header-content')).attr("aria-label", scope.subHeaderText);
                        }
                    }
                };
            }])
        .directive('attGridsterItemBody', [function() {
                return {
                    restrict: 'EA',
                    require: ['^attGridsterItem'],
                    scope: {},
                    templateUrl: 'template/gridster/gridsterItemBody.html',
                    replace: true,
                    transclude: true
                };
            }])
        .directive('attGridsterItemFooter', [function() {
                return {
                    restrict: 'EA',
                    require: ['^attGridsterItem'],
                    scope: {},
                    templateUrl: 'template/gridster/gridsterItemFooter.html',
                    replace: true,
                    transclude: true
                };
            }]);
return {}
})(angular, window);