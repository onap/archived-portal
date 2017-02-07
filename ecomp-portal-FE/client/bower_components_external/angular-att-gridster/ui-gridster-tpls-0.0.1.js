angular.module("att.gridster", ["att.gridster.tpls", "att.gridster.utilities","att.gridster.gridster"]);
angular.module("att.gridster.tpls", ["template/gridster/gridster.html"]);
angular.module('att.gridster.utilities', [])
        .factory('$extendObj', [function () {
                var _extendDeep = function (dst) {
                    angular.forEach(arguments, function (obj) {
                        if (obj !== dst) {
                            angular.forEach(obj, function (value, key) {
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
            }]);

angular.module('att.gridster.gridster', ['attGridsterLib', 'att.gridster.utilities'])
    .constant('attGridsterConfig',
            {
                columns: 3,
                maxRows: 3,
		margins: [10, 10],
		outerMargin: true,
		pushing: true,
		floating: false,
                swapping: true,
		draggable: {
			enabled: true
		}
            })
    .directive('attGridster',['attGridsterConfig', '$extendObj', function (attGridsterConfig, $extendObj) {
    return {
        restrict: 'EA',
        scope: {
            attGridsterOptions : '=?',
            attGridsterItems : '='
        },
        templateUrl: 'template/gridster/gridster.html',
        replace: false,
        controller: ['$scope', '$attrs', function ($scope, $attrs) {
                
        }],
        link: function (scope, element, attrs, ctrl) {
            if (angular.isDefined(scope.attGridsterOptions)) {
                attGridsterConfig = $extendObj.extendDeep(attGridsterConfig, scope.attGridsterOptions);
            }
            scope.attGridsterConfig = attGridsterConfig;
            
        }
    };
  }]);
angular.module("template/gridster/gridster.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/gridster/gridster.html",
    "<div>\n" +
    "    <div gridster='attGridsterConfig'>\n" +
    "        <div gridster-item=\"item\" ng-repeat=\"item in attGridsterItems\" class=\"gridster-item-container\" >\n" +
    "            <div class=\"gridster-item-header\">\n" +
    "                <img gridster-item-drag src=\"images/grips.png\" alt='||' class=\"gridster-item-handle\" />\n" +
    "                <span class=\"gridster-item-header-content\">Maintenance</span>\n" +
    "                <span class=\"gridster-item-sub-header-content\">Sub Header</span>\n" +
    "                <div class=\"gridster-item-header-buttons-container\">\n" +
    "                    <img src=\"images/att-globe-ie8.png\" alt='AT&T' />\n" +
    "                </div>\n" +
    "            </div>\n" +
    "            <div class=\"gridster-item-body\">\n" +
    "                <div style=\" width: 100%; height: 221px; font-size: 12px;\">\n" +
    "                    <br/>{{$index}}<center></center>\n" +
    "                    <br>\n" +
    "                    There are no AT&amp;T BusinessDirect tools available. To contact us for help, click the chat icon, and then click Contact Us.\n" +
    "                </div>\n" +
    "            </div>\n" +
    "            <div class=\"gridster-item-footer\">\n" +
    "                <a href='#anotherPage' class=\"gridster-item-footer-content\" >Navigate to another page</a>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>");
}]);
