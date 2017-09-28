Pulling DB data
===============

In the :ref:`connectionjava` section, we set up a connection to the :code:`ecomp_sdk` database. Now, we going to use our AngularJS controller (:code:`controller.js`) and data service (:code:`data-service.js`) to make an HTTP request to our Spring controller (:code:`MyAppController.java`), wait for the results, and map them into a Google Chart.

AngularJS Promises
----------------------

"Promises" are a core feature of AngularJS, and whether you fully understand them or not, you will be using them in your applications. Promises use AJAX (Asynchronous JavaScript and XML -- you can also use JSON). When we make a call to a web server for some data, we don't want our application to become unresponsive while we wait for an answer. Therefore, :code:`$http.get` calls (more in a minute), return a Promise object rather than text from the web server. Then, we make sure that the Promise object does the right thing at the right time by assigning callback functions when the web server either returns results or fails to return a result. Something like this:

.. code-block:: javascript

  var p = $http.get("http://somedomain.com/some_request");
  
  p.success(function(result) {
    console.log("The web server returned: " + result);
  });
  
  p.error(function(response, status) {
    console.log("The web server returned:\n\tStatus: " + status + "\n\tError: " + response);
  });

Here, AJAX (via the AngularJS module :code:`$http`) makes a request to :code:`somedomain.com/some_request`. Our JavaScript engine immediately then moves to assign anonymous functions as callbacks for success and error conditions. Then, when the web server finally returns a result, the callbacks are run. 

Data service
------------

Our special function in :code:`data-service.js` uses Promises. We make sure to execute code in the correct order by using the :code:`then` Promise function.

Something like this:

.. code-block:: javascript

  $scope.myFunction = function() {
    dataService.getSomeData().then(function(rv) {
      // Do something here.
    });
  }

Here, :code:`getSomeData` returns a Promise object. The :code:`then` function tells the JavaScript engine to execute the given anonymous function only after the request has completed.

Technically, the :code:`then` function takes two functions as arguments. The first defines what to do upon success, and the second defines what to do upon failure. We omitted the failure argument above.

Here is our :code:`data-service.js` code:

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

The syntax of this function is not immediately obvious unless you are comfortable with JavaScript Promises and Deferreds. For a more complete explanation with examples, check out `this blog post <http://chariotsolutions.com/blog/post/angularjs-corner-using-promises-q-handle-asynchronous-calls/>`_.

Essentially, our service definition is a super-concise JavaScript way to allow this from within our controller:

.. code-block:: javascript

  dataService.getChartData(direction).then(function(rv) {
    // Do something here
  });

Behind the scenes, this makes an :code:`HTTP` request that looks like this:

:code:`http://localhost:8080/epsdk-app-os/get_chart_data/<direction>/`

where :code:`direction` is either "upload" or "download" and returns the result back to our controller as JSON text, which we'll convert into a JavaScript object for further processing.

Modifying our Spring controller
-------------------------------

Let's add a couple of functions to our Spring controller, :code:`MyAppController.java`:

.. code-block:: java
  
  @RequestMapping(value = {"/get_chart_data/{direction}/"}, method = RequestMethod.GET)
  public void getChartData(@PathVariable("direction") String direction, HttpServletRequest request, HttpServletResponse response){
    try {
      Object a = _getChartData(direction);
      response.getWriter().write(a.toString());
    } catch (IOException e) {
      // Probably should do something here ;-)
    }
  }

  private Object _getChartData(String direction) {
    ArrayList<JSONObject> allData = new ArrayList<JSONObject>();
    JdbcTemplate jdbcTempl = new JdbcTemplate(m_dataSources.get("myappdb"));

    // Check our parameter
    if (!direction.equals("download") && !direction.equals("upload"))
      direction = "download";
    }

    String query = "select data_date, speedmbps, direction from mock_data_avg_speed where direction='" + direction + "' order by data_date asc";

    List<Map<String,Object>> out = jdbcTempl.queryForList(query);
    for (Map<String,Object> row: out) {
      JSONObject jo = new JSONObject();
      jo.put("data_date", row.get("data_date"));
      jo.put("speedmbps", row.get("speedmbps"));
      jo.put("direction", row.get("direction"));
      allData.add(jo);
    }

    return allData;
  }

Testing our changes
-------------------

To test our database connection, first compile and install the war as in the :ref:`installingyourapp` section. Next, `login`_. Now try the `following URL`_:

::

  http://localhost:8080/epsdk-app-os/get_chart_data/download/

.. note:: Using the trailing '/' character can prevent confusion with AngularJS routing. It might not always be necessary, but it is good practice to use it in this context to prevent headaches later on.

If everything went as planned, you should see:

::

  [{"speedmbps":40,"data_date":"2017-08-01","direction":"download"}, {"speedmbps":18,"data_date":"2017-08-02","direction":"download"}, {"speedmbps":25,"data_date":"2017-08-03","direction":"download"}, {"speedmbps":48,"data_date":"2017-08-04","direction":"download"}, {"speedmbps":49,"data_date":"2017-08-05","direction":"download"}, {"speedmbps":46,"data_date":"2017-08-06","direction":"download"}, {"speedmbps":35,"data_date":"2017-08-07","direction":"download"}]

This is what makes JSON such a powerful tool. We'll take that JSON output and convert it into JavaScript objects in order to build our chart.

.. _following URL: http://localhost:8080/epsdk-app-os/get_chart_data/download/
.. _login: http://localhost:8080/epsdk-app-os/login.htm
