Creating a new application
==========================
 
Our tutorial application will consist of two parts:

1. Java code: We'll create a controller (in reality you might need to create many, depending on how complex your application is) that will handle many of our web requests. Most importantly, it will handle the top-level access request.
2. Web code: The web code consists of all the files that Tomcat serves when requests are made for our application. Files like HTML, CSS, JavaScript, images, and so on.

If you're working with code command-line only (e.g. vim), you'll want to create symlinks to both parts of the code, :code:`java` and :code:`web`. While debugging, you'll frequently move between the two halves.

Java code location
------------------

Our Java code will reside here:

::

    sdk/ecomp-sdk/epsdk-app-os/src/main/java/org/openecomp/myapp


Create the :code:`myapp` directory.

Web code location
-----------------

Our web code will reside here:

::

    sdk/ecomp-sdk/epsdk-app-os/src/main/webapp/app/fusion/scripts/myapp

Create the :code:`myapp` directory.

Inside this directory, we'll add our first subpage:

::

    sdk/ecomp-sdk/epsdk-app-os/src/main/webapp/app/fusion/scripts/myapp/myfirstpage

Create the :code:`myfirstpage` directory.

Spring controllers
------------------

We need a controller to handle the top-level access requests (e.g. :code:`localhost:8080/epsdk-app-os/somepage`). For every :code:`somepage` our app needs, we'll need to either designate an existing controller to handle the request or create a new one.

Why would you need to create a new one?

The controller is where you'll set up your database access, read from database tables, update database tables, and so on. It may make sense to use different controllers for different :code:`somepage` functions to increase readability and make things easier when others need to maintain the code.

A basic controller
~~~~~~~~~~~~~~~~~~

Copy the following basic controller code into your Java code location as :code:`MyAppController.java`

.. code-block:: java

    package org.openecomp.myapp;

    import java.util.HashMap;
    import java.util.ArrayList;
    import java.util.Map;
    import java.util.List;
    import java.io.IOException;

    import java.beans.PropertyVetoException;
    
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestMethod;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.stereotype.Repository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.servlet.ModelAndView;
    import org.springframework.jdbc.core.JdbcTemplate;
    
    import javax.servlet.http.HttpSession;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import javax.sql.DataSource;
    import javax.annotation.PostConstruct;
    
    import com.mchange.v2.c3p0.ComboPooledDataSource;
    
    import org.json.JSONObject;
    import org.json.JSONArray;
    
    import org.openecomp.portalsdk.core.controller.RestrictedBaseController;
    import org.openecomp.portalsdk.core.util.SystemProperties;
    import org.openecomp.portalsdk.core.domain.User;
    
    @Controller
    @Repository
    @RequestMapping("/")
    public class MyAppController extends RestrictedBaseController {
      ///////////////////////////////////////////////////////////////////////////////
      // Constructor
      //
      public MyAppController() {
        super();
      }
      
      ///////////////////////////////////////////////////////////////////////////////
      // Handle 'myfirstpage' requests
      //
      @RequestMapping(value = { "/myfirstpage" }, method = RequestMethod.GET)
      public ModelAndView myfirstpage(HttpServletRequest request) {
        final String defaultViewName = null;
        return new ModelAndView(defaultViewName);
      }
    }

.. _definitions.xml:

Request routing via definitions.xml
-----------------------------------

In order for the framework to route requests for :code:`myfirstpage` correctly, we'll need to create an entry in :code:`sdk/ecomp-sdk/epsdk-app-os/src/main/webapp/WEB-INF/defs/definitions.xml` that looks like this:

::

    <definition name="myfirstpage" template="/app/fusion/scripts/myapp/myfirstpage/myfirstpage.html" />

Then, add the following to :code:`sdk/ecomp-sdk/epsdk-app-os/src/main/webapp/app/fusion/scripts/myapp/myfirstpage/myfirstpage.html`:

.. code-block:: html

    <html>
      <body>
        <p>It worked!</p>
      </body>
    </html>

Now, build and install your application as before. If everything worked, you should see `It worked!` in your browser window when you visit myfirstpage_ after logging in. 

When the request from the browser comes in, the framework creates a mapping from :code:`myfirstpage` to the MyAppController, which in turn maps your definition name to a particular template. Soon, we'll fill in that template to do more interesting things.

.. _myfirstpage: http://localhost:8080/epsdk-app-os/myfirstpage
