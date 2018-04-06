Enabling Single-Signon
======================

This tutorial explains the Single-Signon of ONAP Portal, and how to enable it in our sample application.

When a user logs in using the ONAP Portal Login, a cookie is created to record the logged-in session,
and it includes the username and other parameters.

In our tutorial app so far we used "http://app-host:port/epsdk-app-os/login.htm" to login into the application.

This is called the external login, which is used a backdoor for developers to test the applications
in a local environment, without having the entire Portal.

Now, when the application is ready and onboarded on to the Portal, we can now use
"http://app_host:port/epsdk-app-os/welcome.htm" as the application onboarding URL in ONAP Portal.

Onboarding steps can be found here
https://wiki.onap.org/display/DW/Application+Onboarding#ApplicationOnboarding-addapp.

The welcome.htm page code will try to read the session cookie from the browser to detect if there is
a user that's already logged-in into the ONAP Portal.

If a cookie is found, then this means a user is already logged-in to the  ONAP Portal and
the application welcome.htm page will display showing the application frontend in a tab inside ONAP Portal.

However, if a cookie is not found, the code will look for the redirect URL listed in the
WEB-INFO/classes/portal.properties file.

Assuming the ecomp_redirect_url = http://portal.api.simpledemo.onap.org:8989/ONAPPORTAL/login.htm
for Amsterdam release, then, the user will be redirected to the ONAP Portal login page to log in.

If the user login succeeds, the Portal login page will redirect the user back to the Application
welcome page.

This should enable the application to  use the Portal’s Single Signon.

Note that the ONAP Portal and its on-boarded application must be on the same domain
(e.g. *.simpledemo.onap.org) to take advantage of the Single Signon.

Otherwise, if the application is hosted in a different domain, its code will not be able to read ONAP
Portal session cookies.

Due to security reasons the ONAP Portal restricts to one domain (defined in the properties) across
the applications on-boarded to it.

However, if the application is not interested in single signon feature, then the new application can
be always be on-boarded as a “Hyperlink only application” which will be opened into a new browser
rather than the internal Portal’s tab.

Changing the cookie domain
--------------------------

Changing the single-sign on cookie domain in portal requires both development and config activity:


development - change this file and rebuild the front-end
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

::

    ./ecomp-portal-FE-os/client/configurations/integ.json:102:      "cookieDomain": "onap.org"


configuration - change this deployment entry
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

::

    ./deliveries/properties_simpledemo/ONAPPORTAL/system.properties:104:ext_central_access_user_domain = @csp.onap.org


Apps that are built on the EP-SDK have an entry in a config file that must be changed
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

::

    ./deliveries/properties_simpledemo/ONAPPORTALSDK/system.properties:67:cookie_domain = onap.org