Dynamic content
===============
 
Now, we'll make our new application dynamic by allowing user interaction. To do this, we'll let the user select between "download" and "upload" charts with an HTML select/option menu.

Creating the Angularized select/option menu
-------------------------------------------

Add the following just above our Google Chart placeholder in :code:`sdk/ecomp-sdk/epsdk-app-os/src/main/webapp/app/fusion/scripts/myapp/myfirstpage/template.html`:

.. code-block:: html

  <select ng-model="state.direction" ng-change="getChartData(state.direction)" ng-options="direction for direction in ['download', 'upload']"></select>

The :code:`state.direction` value for the :code:`ng-model` attribute tells AngularJS that the :code:`state.direction` should hold the current value of the selected option. We saw it earlier in our controller:

.. code-block:: javascript

  $scope.state = {
    .
    .
    // Holds the selected direction from the select/options control    
    direction: "download"
    .
    .
  };

How this works is simple. Using the options defined in :code:`ng-options`, AngularJS creates a select/option button. When the user selects an option, the value is stored in :code:`ng-model`. If the option changes, AngularJS calls our :code:`getChartData` function with the selected option as an argument.

Compile, install, and try it out.
