Your AngularJS app
=========================

Now that we've established a database connection, it's time to work on the web side of your app. To start, we'll create some files in :code:`sdk/ecomp-sdk/epsdk-app-os/src/main/webapp/app/fusion/scripts/myapp/myfirstpage`.

myfirstpage.html
----------------

This is your landing page. Its purpose is to pull in all the JavaScript and CSS that your app might need as well as to set up your AngularJS app (:code:`app.js`) and your app's controller (:code:`controller.js`), data-services (:code:`data-service.js`), and routing information (:code:`route.js`) --- more on these in a moment. There is likely much that can be removed from this file (it is boilerplate copied from a sample Portal SDK app), but to save yourself headaches at first, just cut and paste all of it into the :code:`sdk/ecomp-sdk/epsdk-app-os/src/main/webapp/app/fusion/scripts/myapp/myfirstpage/myfirstpage.html` you created earlier in, redundancies and all:

.. code-block:: html

  <!DOCTYPE html>
  <!-- Single-page application for EPSDK-App using DS2 look and feel. -->
  <html>
    <head>
      <meta charset="ISO-8859-1">
      <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1" />
      
      <title>Welcome</title>
    
      <!-- B2b Library -->
      <link rel="stylesheet" type="text/css" href="app/fusion/external/b2b/css/b2b-angular/b2b-angular.css">
      <link rel="stylesheet" type="text/css" href="app/fusion/external/b2b/css/b2b-angular/font_icons.css">
        
      <!-- icons in open source -->
      <link rel="stylesheet" type="text/css" href="app/fusion/external/ds2/css/digital-ng-library/ionicons.css">
      <link rel="stylesheet" type="text/css" href="app/fusion/external/ds2/css/digital-ng-library/ecomp-ionicons.css">
      
      <link rel="stylesheet" type="text/css" href="app/fusion/external/angular-bootstrap/ui-bootstrap-csp.css">
      <link rel="stylesheet" type="text/css" href="app/fusion/external/angular-gridster/dist/angular-gridster.min.css">
      <link rel="stylesheet" type="text/css" href="static/fusion/sample/css/scribble.css" />
      <link rel="stylesheet" type="text/css" href="static/fusion/sample/css/welcome.css" />
                                      
      <link rel="stylesheet" type="text/css" href="app/fusion/styles/ecomp.css">
      
      <!-- Common scripts -->  
      <script src="app/fusion/external/angular-1.4.8/angular.min.js"></script>
      <script src="app/fusion/external/angular-1.4.8/angular-messages.js"></script>
      <script src="app/fusion/external/angular-1.4.8/angular-touch.js"></script>
      <script src="app/fusion/external/angular-1.4.8/angular-sanitize.js"></script>  
      <script src="app/fusion/external/angular-1.4.8/angular-route.min.js"></script>
      <script src="app/fusion/external/angular-1.4.8/angular-cookies.min.js"></script>
      <script src="app/fusion/external/jquery/dist/jquery.min.js"></script>
      <script src="app/fusion/external/javascript-detect-element-resize/jquery.resize.js"></script>
      <script src="app/fusion/external/angular-bootstrap/ui-bootstrap-tpls.min.js"></script>
      <script src="app/fusion/external/angular-gridster/dist/angular-gridster.min.js"></script>
          
      <!-- EPSDK App scripts and common services -->  
      <!-- B2b Library -->
      <script src="app/fusion/external/b2b/js/b2b-angular/b2b-library.min.js"></script>
      <script src="app/fusion/scripts/DS2-services/ds2-modal/modalService.js"></script>
      <script src="app/fusion/scripts/myapp/myfirstpage/app.js"></script>  
          
      <script src="app/fusion/scripts/DS2-services/userInfoServiceDS2.js"></script>   
      <script src="app/fusion/scripts/DS2-services/headerServiceDS2.js"></script>
      <script src="app/fusion/scripts/DS2-services/leftMenuServiceDS2.js"></script>
      <script src="app/fusion/scripts/DS2-services/manifestService.js"></script>
        
      <script src="app/fusion/scripts/DS2-directives/footer.js"></script>
      <script src="app/fusion/scripts/DS2-directives/ds2Header.js"></script>
      <script src="app/fusion/scripts/DS2-directives/ds2LeftMenu.js"></script>
      <script src="app/fusion/scripts/DS2-directives/b2b-leftnav-ext.js"></script> 
      <script src= "app/fusion/scripts/DS2-services/userInfoServiceDS2.js"></script>  
      
      <!-- Page specific items -->
      <script src="app/fusion/scripts/myapp/myfirstpage/controller.js"></script>
      <script src="app/fusion/scripts/myapp/myfirstpage/route.js"></script>  
      <script src="app/fusion/scripts/myapp/myfirstpage/data-service.js"></script>  
      
      <style>
        .controls {
          margin-bottom: 20px;
        }
        .page-header {
          margin-top: 20px;
        }
        ul {
          list-style: none;
        }
        .box {
          height: 100%;
          border: 1px solid #ccc;
          background-color: #fff;
          position: relative;
          overflow: hidden;
        }
        .box-header {
          background-color: #eee;
          padding: 0px 0px 0px 0px;
          margin-bottom: -25px;
          cursor: move;
          position: relative;
        }
        .box-header h3 {
          margin-top: 0px;
          display: inline-block;
        }
        .box-content {
          padding: 10px;
          display:block;
          height: 100%;
          position: relative;
          overflow-x:auto;
          overflow-y:auto;    
        }
        .box-header-btns {
          top: 15px;
          right: 10px;
          cursor: pointer;
          position: absolute;
        }
        .gridster {
          border: none;
          position:relative;    
        }
        .box-content .box-content-frame{
        }
        .box table{
          border:none;
          display:block;
        }
        .box table tr{
          line-height:20px;
        }
        .box table th{
          border:none;
          line-height:20px;
        }
        .menu-container{
          margin-top:0px
        }
        .handle-e {
          width:3px;
        }
      </style>
    </head>
    <body class="appBody" ng-app="abs">
      <!-- commented the header for now to avoid duplicate headers on portal -->
      <div ds2-Header class="header-container" ></div>
      <div ds2-menu id="menuContainer" class="menu-container"></div>
      <div ng-view id="rightContentProfile" class="content-container"></div>  
      <div ds2-Footer class="footer-container"></div>
   </body>
  </html>

app.js
------

:code:`sdk/ecomp-sdk/epsdk-app-os/src/main/webapp/app/fusion/scripts/myapp/myfirstpage/app.js` contains a single line:

.. code-block:: javascript

  var appDS2=angular.module("abs", ['ngRoute', 'ngMessages','modalServices', 'ngCookies', 'b2b.att','gridster','ui.bootstrap','ui.bootstrap.modal']);

Don't worry too much about the particulars here. Just know that the list of strings are dependencies. You might add or remove some later.

controller.js
-------------

The controller is where most of the action happens. The controller is complex, but there is one basic thing that needs clarifying. In AngularJS, :code:`$scope` essentially says, "This should be visible inside the associated :ref:`template`." You'll gradually come to understand other aspects of the controller as you work with it.

Copy and paste the following into :code:`sdk/ecomp-sdk/epsdk-app-os/src/main/webapp/app/fusion/scripts/myapp/myfirstpage/controller.js`:

.. code-block:: javascript

  appDS2
    .controller('myFirstPageController', function($scope, $routeParams, $location, $interval, $http, $q, $modal, $log, ManifestService, dataService) {
      /**********************************************
       * FUNCTIONS
       **********************************************/
  
      $scope.init = function () {
        // Set up and initialize a state object. This object will contain information about the state of
        // the application as the user interacts with it.
        $scope.state = {
          // Holds a message string for testing
          message: "Hello from myFirstPageController",
        }
      }
    
      /**********************************************
       * Setup and initialize the app on load
       **********************************************/

      $scope.init();

    }); // end .controller

data-service.js
---------------

:code:`data-service.js` is the bridge between the Java side of your app and the web side. The dataService makes http requests to :code:`MyAppController.java`. Once a response is received (it will not block, waiting for a response, because you want your app to continue working while waiting), it executes the :code:`then` portion of the code, which simply returns the result back to wherever it was called from.

We'll see it in action soon. For now copy and paste the following into :code:`sdk/ecomp-sdk/epsdk-app-os/src/main/webapp/app/fusion/scripts/myapp/myfirstpage/data-service.js`:

.. code-block:: javascript

  appDS2.factory('dataService', function ($http, $q, $log) {
    return {
      // Service to return chart data
      getChartData: function(direction) {
        return $http.get("get_chart_data/" + direction + "/").then(function(response) {
          if (typeof response.data === 'object' || typeof response.data === 'string') {
            return response.data;
          }
          else {
            return $q.reject(response.data);
          }
        }, function(response) {
          return $q.reject(response.data);
        })
      }
    };
  });

route.js
--------

:code:`route.js` tells AngularJS how to map specific kinds of incoming requests to specific pages and controllers. AngularJS uses the 'location' hashtag to pass parameters to the client as seen in the commented :code:`when` block example below.

Copy and paste the following into :code:`sdk/ecomp-sdk/epsdk-app-os/src/main/webapp/app/fusion/scripts/myapp/myfirstpage/route.js`:

.. code-block:: javascript

  appDS2.config(function($routeProvider) {
    $routeProvider
 
    // Example route that maps a specific URL to another page and
    // controller.
    // 
    // This would respond to:
    //    http://localhost:8080/epsdk-app-os#/date1/2017-08-01/date2/2017-08-07/
    //
    //.when('/date1/:date1/date2/:date2/', {
    //  templateUrl: 'app/fusion/scripts/myapp/myfirstpage/someotherpage.html',
    //  controller : "anotherController"
    //})
  
    .otherwise({
      templateUrl: 'app/fusion/scripts/myapp/myfirstpage/template.html',
      controller : "myFirstPageController"
    });
  });

.. _template:

template.html
-------------

The AngularJS template holds all the HTML and AngularJS directives that are presented to the user inside the ONAP Portal SDK boilerplate navigation. It is referenced in the :code:`route.js` file. Copy and paste the following into :code:`sdk/ecomp-sdk/epsdk-app-os/src/main/webapp/app/fusion/scripts/myapp/myfirstpage/template.html`:

.. code-block:: html

  <div id="page-content" class="content" style="padding: 25px;">
    <p>{{state.message}}</p>
  </div>

Now, compile, install, and login.

Adding your new page to the SDK navigation
------------------------------------------

First, click on :code:`Menus` in the :code:`Admin` navigation menu:

.. image:: img/menus.png
	:width: 320px

Now, click the :code:`Add Menu Item` button at the top of the page:

.. image:: img/addmenu.png
	:width: 200px

Finally, fill out the form in the following way.

.. note:: "myfirstpage" is a reference to the name we defined in :ref:`definitions.xml`.

.. image:: img/newmenuitem.png
	:width: 640px

To reload the navigation, click :code:`Home` in the Portal SDK navigation menu. You should see your new menu item at the top. If all went well, you should see "Hello from myFirstPageController" in the content area to the right of the navigation menu.

