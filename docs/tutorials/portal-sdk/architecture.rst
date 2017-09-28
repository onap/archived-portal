Application architecture
============================================

When building the tutorial app with the ONAP Portal SDK, you'll be working with:

1. `Spring Framework`_: You'll build a Spring Framework controller, which will handle your database queries and provide the top-level access to your application.
2. AngularJS_: Support for AngularJS is built into the SDK. AngularJS is a popular and powerful framework that lets developers create dynamic client-side web applications. E.g. Gmail. Unlike server-side frameworks like PHP, almost all GUI interaction is handled by the client in JavaScript.
3. `Bootstrap UI`_: Bootstrap is a front-end framework that makes designing web pages easier by incorporating extensive CSS.
4. `Google Charts`_: Google charts makes including graphs in your applications easier.
5. Gridster_: Although not covered in this tutorial, the ONAP Portal SDK also comes with built-in support for the Gridster JavaScript library, which lets you produce pages with draggable and resizeable layouts from elements spanning multiple columns.

Although the learning curve may seem daunting, the most difficult aspect of creating ONAP Portal SDK web applications is understanding how all the pieces fit together. The various frameworks themselves are not hard to understand. However, how they interact with one another isn't always obvious. If you have a basic grasp of Java, JavaScript, HTML, and CSS, you'll have no trouble.

.. _Spring Framework: https://projects.spring.io/spring-framework/
.. _AngularJS: https://angularjs.org/
.. _Bootstrap UI: http://www.bootstrap-ui.com/
.. _Google Charts: https://developers.google.com/chart/
.. _Gridster: http://dsmorse.github.io/gridster.js/
