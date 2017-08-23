This file tracks patches to b2b-angular.js

14 August 2017: fix b2b-tree de-select the parent node issue - code provided by b2b team

24 April 2017: hide header menu when click on iframe in a new tab
+
$(window).blur(function() {
                	if(scope.showMenu){
               	 	 scope.showMenu = false;
                     elem.removeClass('active');
                     scope.$apply();
               	 	}
                });

20 March 2017: Page auto adjustment with left menu collapse and expand.
+
scope.toggleDrawer = function(showmenu){
	scope.idx=-1; /*hide the sunmenus*/
	if(showmenu){
		document.getElementById('page-content').style.paddingLeft = "50px";
	}
	else
		document.getElementById('page-content').style.paddingLeft = "230px";           	
};

26 May 2017 : Changed b2b sort icons
+
"<i ng-class=\"{'icon-controls-upPRIMARY active': sortPattern === 'ascending', 'icon-controls-down active down': sortPattern === 'descending'}\"></i>\n" 
