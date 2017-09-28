Google Charts
=============

Now that we've established our database connection and can request and retrieve data from our tables, we can focus on our web application.

Installing
----------

First, we'll need to grab the Angular-ized Google Charts. Do this:

::

  cd sdk/ecomp-sdk/epsdk-app-os/src/main/webapp/app/fusion/scripts/myapp/myfirstpage  
  wget https://raw.githubusercontent.com/angular-google-chart/angular-google-chart/development/ng-google-chart.min.js

.. note:: The "min" in the file name indicates that any and all unnecessary spaces, newlines, etc. have been removed from the file to "min"imize the size.

Now, add a reference to Google Charts in your :code:`sdk/ecomp-sdk/epsdk-app-os/src/main/webapp/app/fusion/scripts/myapp/myfirstpage/myfirstpage.html` file:

::

  .
  .
  .
  <!-- Page specific items -->
  <script src="app/fusion/scripts/myapp/myfirstpage/controller.js"></script>
  <script src="app/fusion/scripts/myapp/myfirstpage/route.js"></script>
  <script src="app/fusion/scripts/myapp/myfirstpage/data-service.js"></script>
  <!-- Google Charts -->
  <script src="app/fusion/scripts/myapp/myfirstpage/ng-google-chart.min.js"></script> 
  .
  .
  .

Now, make sure we tell our app that we'll need to use this dependency by adding a reference to it in :code:`sdk/ecomp-sdk/epsdk-app-os/src/main/webapp/app/fusion/scripts/myapp/myfirstpage/app.js`:

.. code-block:: javascript

  var appDS2=angular.module("abs", ['ngRoute', 'ngMessages','modalServices', 'ngCookies', 'b2b.att','gridster','ui.bootstrap','ui.bootstrap.modal','googlechart']);

Configuring our chart
---------------------

Change the initialization of the :code:`$scope.state` variable by using the following inside the init function in your :code:`sdk/ecomp-sdk/epsdk-app-os/src/main/webapp/app/fusion/scripts/myapp/myfirstpage/controller.js` file:

.. code-block:: javascript

  $scope.state = {
    // Holds a message string for testing
    message: "Hello from myFirstPageController",
    // Holds the desired direction of the chart
    direction: "download",
    chart: {
      type: "LineChart",
      data: {
        cols: [{id: "dt", label: "Date", type: "date"},
               {id: "c1", label: "Bandwidth", type: "number"},
               {type: "string", role: "tooltip"}],
        rows:[] // These change for every chart
      },
      options: {
        title: "",
        hAxis: {title: "Date", format: "MM/dd/yyyy"},
        vAxis: {title: "Bandwidth (Mbps)", minValue: 0},
        colors: ['blue'],
        defaultColors: ['blue'],
        legend: {position: "top", maxLines: 2},
        isStacked: false,
        pointSize: 2
      }
    }
  };

The configuration options are self-explanatory. Experiment with them to get a better idea of what's possible.

Populating our chart
--------------------

Add the following function to your :code:`sdk/ecomp-sdk/epsdk-app-os/src/main/webapp/app/fusion/scripts/myapp/myfirstpage/controller.js` file:

.. code-block:: javascript

  $scope.getChartData = function(direction) {
    dataService.getChartData(encodeURI(direction)).then(function(rv) {
      var arr = JSON.parse(angular.toJson(rv));
      
      // Clear out our rows
      $scope.state.chart.data.rows = [];

      for (var i=0; i<arr.length; i++) {
        var t = arr[i].data_date.split(/[-]/);
        var d = new Date(t[0], t[1]-1, t[2], 0, 0, 0);
        var row = {};
  
        row.c = [{v: d},
                 {v: arr[i].speedmbps, f: "speed"},
                 {v: arr[i].speedmbps + " Mbps"}];
  
        $scope.state.chart.options.title = "Avg Bandwith in Mbps (" + direction + ")"
        $scope.state.chart.data.rows.push(row);
      }
    });
   }

When our call to :code:`getChartData` returns (and only then) do we populate the rows of our Google Chart. Each row in a Google Chart, as defined in our :code:`init` function, consists of a date, speed, and finally a special annotation called a "tooltip" that pops up a small window with some text whenever the user hovers over a data point. You can also use these special annotations to change the style of the point being displayed. See the `Google Charts`_ documentation for more info.

One subtle but important piece of the code above is that we wrap :code:`direction` in :code:`encodeURI`. This ensures that the arguments passed in the HTTP request are acceptable and mean what you think they mean when they are caught and decoded in your Spring controller. That framework will automatically handle the decoding.

Now, add a call to :code:`getChartData` in your :code:`init` function, just before the closing curly bracket:

.. code-block:: javascript

  $scope.getChartData("download");

Updating the template
---------------------

Finally, we'll need to add a placeholder for the chart in :code:`sdk/ecomp-sdk/epsdk-app-os/src/main/webapp/app/fusion/scripts/myapp/myfirstpage/template.html`. Here's what it looks like:

.. code-block:: html

  <div id="page-content" class="content"  style="padding: 25px;">
    <p>{{state.message}}</p>
    <div class="md-media-lg" align="center" google-chart chart="state.chart" style="height: 400px;"></div>
  </div>

Recompile, install, and try it out.

.. _Google Charts: https://developers.google.com/chart/interactive/docs/points
