/**
* FileName ui-gridster
* Version 0.1.0
* Build number 81850
* Date 03/14/2016
*/


(function(angular, window){
angular.module("att.gridster", ["att.gridster.tpls", "att.gridster.utilities","att.gridster.gridster"]);
angular.module("att.gridster.tpls", ["template/gridster/gridster.html","template/gridster/gridsterItem.html","template/gridster/gridsterItemBody.html","template/gridster/gridsterItemFooter.html","template/gridster/gridsterItemHeader.html"]);
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
angular.module("template/gridster/gridster.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/gridster/gridster.html",
    "<div gridster='attGridsterConfig'><div ng-transclude></div></div>");
}]);

angular.module("template/gridster/gridsterItem.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/gridster/gridsterItem.html",
    "<div gridster-item='attGridsterItem' class=\"gridster-item-container\" ng-transclude></div>");
}]);

angular.module("template/gridster/gridsterItemBody.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/gridster/gridsterItemBody.html",
    "<div class=\"gridster-item-body\" ng-transclude></div>");
}]);

angular.module("template/gridster/gridsterItemFooter.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/gridster/gridsterItemFooter.html",
    "<div class=\"gridster-item-footer\" ng-transclude></div>");
}]);

angular.module("template/gridster/gridsterItemHeader.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/gridster/gridsterItemHeader.html",
    "<div class=\"gridster-item-header\">\n" +
    "    <img gridster-item-drag ng-src={{gripsImgPath}} alt=\"||\" aria-label=\"Tap or Click to move\" class=\"gridster-item-handle\" />\n" +
  
    "    <span class=\"gridster-item-header-content\" tabindex=\"0\" role=\"presentation\" aria-label=\"{{headerText}}\" ng-bind-html=\"headerText | unsafe\"></span>\n" +
  /*  "	 <span class=\"{{headerIcon}}\" style=\"font-size: 40px; color:black; float:left; margin-left:30px; margin-right: -2	0px;    line-height: 1.2; \"></span>"+*/
    "    <span class=\"gridster-item-sub-header-content\" role=\"presentation\">{{subHeaderText}}</span>\n" +
    "    <div class=\"gridster-item-header-buttons-container\" ng-transclude></div>\n" +
    "</div>");
}]);

return {}
})(angular, window);