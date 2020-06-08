.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. http://creativecommons.org/licenses/by/4.0
.. Copyright 2017-2020 AT&T Intellectual Property.  All rights reserved
.. _release_notes:

Portal Platform Release Notes
=============================
Version: 3.2.0
--------------
:Release Date: 2020-05-21

.. toctree::
    :maxdepth: 1

This release contains an Angular upgrade, bug fixes and security enhancements.

**New Features**

        * Angular Upgrade from 1.X to 7.0 - Portal and SDK (Backward Compatible)
        * OParent 2.0 Migration & Self Release Jobs
        * UI/TypeScript Test Coverage - (Portal Coverage 60% and SDK Coverage 63%)
        * Spring Boot Migration (Portal only/ Partially Completed)

**Bug Fixes**

        * License scan issues addressed
        * Fixed Sonar reported critical issues.

**Known Issues**
        * User management pages do not work properly. We will be addressing this in the Guilin release. So, the work around is:

           1.  If we try to add user role by navigating directly to an application, like A&AI, we are unable to add roles.
           2.  However, With Portal admin privileges a user (in this case Demo user) can navigate to User screen and select Portal/Default from the drop down.
           3.  Then update user roles for A&AI and other applications in the popup.
           4.  We validated that this operation is correctly updating the role in AAF.

**Security Notes**

        * Closed HTTP Ports (Portal, SDK)
        * Address Security Vulnerabilities from Nexus-IQ (Jars and Javascript libraries)
        * Containers to run as Non-Root user: portal, portal-sdk, portal-widget

*Fixed Security Issues*

        * OJSI-97 - portal-app exposes plain text HTTP endpoint using port 8989 [`OJSI-97 <https://jira.onap.org/browse/OJSI-97>`_]
        * OJSI-105 - portal-sdk exposes plain text HTTP endpoint using port 30212 [`OJSI-105 <https://jira.onap.org/browse/OJSI-105>`_]
        * OJSI-106 - portal-app exposes plain text HTTP endpoint using port 30215 [`OJSI-106 <https://jira.onap.org/browse/OJSI-106>`_]

*Known Security Issues*

*Known Vulnerabilities in Used Modules*
        * Portal was granted a waiver by SECCOM for OJSI-190. Portal stores users passwords encrypted instead of hashed. This was not fixed for Frankfurt.

Quick Links:
     - `PORTAL project page <https://wiki.onap.org/display/DW/Portal+Platform+Project>`_

     - `Passing Badge information for PORTAL <https://bestpractices.coreinfrastructure.org/en/projects/1441>`_

     - `Project Vulnerability Review Table for PORTAL <https://wiki.onap.org/pages/viewpage.action?pageId=68542388>`_

**Upgrade Notes**
        * These still apply:
           1. For https Apps onboarded to portal, a certificate has to be downloaded in the browser when first trying to access the landing page of the App.
           2. For onboarded Apps using http (since Portal is using https) the browser asks the user to click to Proceed to the unsafe URL.
           3. For onboarded Apps using http the icon in the URL bar will appear red, click on it and allow unsafe scripts. Different browsers use different methods to accomplish this, Firefox has a security icon near the URL that you can click on "Unblock" and "Disable protection for now"
           4. The first time some apps are selected in the Applications panel, an error stating the webpage might be temporarily down, copy the presented URL to a new browser; once that is done, the application will open in the Portal.

**Deprecation Notes**
        * 2.6.0 portal/sdk is the last version to support the old AngularJS UI versions.
        * Expect upgrade on Angular frontend and SpringBoot backend in next releases: The components like Policy, VID, SDC, AAI, MSB, SO – if any of them use portal/sdk java libraries, then please anticipate MAJOR changes to portal/sdk with respect to technology stack upgrade which is pending for long time on Angular frontend and SpringBoot backend.
        * The tech stack upgrade helps resolve many security vulnerabilities and also provides latest rich UI and microservices features that components can take advantage of, just by upgrading to latest portal/sdk.

**Other**
        * Below are the docker images released as part of Portal Platform project:
        * onap/portal-app:3.2.3
        * onap/portal-db:3.2.3
        * onap/portal-sdk:3.2.0
        * onap/portal-wms:3.2.3
        * portal/sdk java artifacts - (Release branch: “release-3.2.0”; Jar Version: "3.0.0")
<<<<<<< HEAD   (3934e4 Updated Frankfurt Release Notes for Images)
=======

>>>>>>> CHANGE (706a90 Added Work Around in Release Notes for User Mgt.)

Version: 2.6.0
--------------
:Release Date: 2019-10-03

.. toctree::
    :maxdepth: 1

Maintenance release with bug fixes and security enhancements.

**No New Features**

**Bug Fixes**
        * Portal Setup - MariaDB issue.
        * Issue editing application url.
        * PORTAL-* charts now use nodePortPrefix variable.
        * Fixed Sonar reported critical issues.

**Known Issues**
        * AAI UI’s new role "ui_view" is not registered in AAF, Portal cannot fetch it. So, the work around is

           1. upload new role from Bulk Upload in Portal Roles page (create a csv file which has one line like:   ui_view,10 )
           2. Sync Roles on same page
           3. Assign this ui_view role to demo account in User page
           4. Then demo user can access AAI UI app from Portal

**Security Notes**

*Fixed Security Issues*

        * CVE-2019-12122 - ONAP Portal allows to retrieve password of currently active user [`OJSI-65 <https://jira.onap.org/browse/OJSI-65>`_]
        * CVE-2019-12121 - ONAP Portal is vulnerable for Padding Oracle attack [`OJSI-92 <https://jira.onap.org/browse/OJSI-92>`_]

*Known Security Issues*

*Known Vulnerabilities in Used Modules*
        * Addressed security issues reported by NexusIQ Critical and Severe issues

Quick Links:
     - `PORTAL project page <https://wiki.onap.org/display/DW/Portal+Platform+Project>`_

     - `Passing Badge information for PORTAL <https://bestpractices.coreinfrastructure.org/en/projects/1441>`_

     - `Project Vulnerability Review Table for PORTAL <https://wiki.onap.org/pages/viewpage.action?pageId=68542388>`_

**Upgrade Notes**
        * For https Apps onboarded to portal, a certificate has to be downloaded in the browser when first trying to access the landing page of the App.
        * For onboarded Apps using http (since Portal is using https) the browser asks the user to click to Proceed to the unsafe URL.
        * For onboarded Apps using http the icon in the URL bar will appear red, click on it and allow unsafe scripts.
        * The first time some apps are selected in the Applications panel, an error stating the webpage might be temporarily down, copy the presented URL to a new browser; once that is done, the application will open in the Portal.

**Deprecation Notes**
        * 2.6.0 portal/sdk is the last version to support the old AngularJS UI versions.
        * Expect upgrade on Angular frontend and SpringBoot backend in next releases: The components like Policy, VID, SDC, AAI, MSB, SO – if any of them use portal/sdk java libraries, then please anticipate MAJOR changes to portal/sdk with respect to technology stack upgrade which is pending for long time on Angular frontend and SpringBoot backend.
        * The tech stack upgrade helps resolve many security vulnerabilities and also provides latest rich UI and microservices features that components can take advantage of, just by upgrading to latest portal/sdk.

**Other**
        * Below are the docker images released as part of Portal Platform project:
        * onap/portal-app:2.6.0
        * onap/portal-db:2.6.0
        * onap/portal-sdk:2.6.0
        * onap/portal-wms:2.6.0
        * portal/sdk java artifacts - (Release branch: “release-2.6.0”)


Version: 2.5.0
--------------
:Release Date: 2019-06-13

.. toctree::
    :maxdepth: 1

We worked on SDK upgrade to integrate with AAF. We partially implemented multi-language.

**New Features**
        * SDK upgrade to integrate with AAF
            * Use of CADI
        * 68% JUnit Test Coverage
        * Addressing security issues
        * Angular 6 upgrade delivered foundation code with sample screen
            * Documentation on the Angular 6 upgrade can be found `here <https://docs.onap.org/en/latest/submodules/portal.git/docs/tutorials/portal-sdk/your-angular-app.html>`_
        * Internationalization language support - partially implemented.
        * Reporting feature enhancement in portal/sdk - design and partial code changes
        * There is more information about new features at `DEMOS - R4 Dublin Demos <https://wiki.onap.org/display/DW/DEMOS+-+R4+Dublin+Demos>`_

**Bug Fixes**
        * Fixed Sonar reported critical issues.

**Known Issues**
        * Mismatch while displaying active online user in Portal.
        * Internationalization Language component partially completed.
        * Functional Menu change requires manual refresh.
        * Modifying Onboarded App configurations from the onboarding page malfunctions but changes to the App configuration can be done through accessing the database (portal:fn_app table) directly.

**Security Notes**

*Fixed Security Issues*

*Known Security Issues*

        * CVE-2019-12317 - Number of XSS vulnerabilities in Portal [`OJSI-15 <https://jira.onap.org/browse/OJSI-15>`_]
        * CVE-2019-12122 - ONAP Portal allows to retrieve password of currently active user [`OJSI-65 <https://jira.onap.org/browse/OJSI-65>`_]
        * CVE-2019-12121 - ONAP Portal is vulnerable for Padding Oracle attack [`OJSI-92 <https://jira.onap.org/browse/OJSI-92>`_]
        * In default deployment PORTAL (portal-app) exposes HTTP port 8989 outside of cluster. [`OJSI-97 <https://jira.onap.org/browse/OJSI-97>`_]
        * In default deployment PORTAL (portal-app) exposes HTTP port 30215 outside of cluster. [`OJSI-105 <https://jira.onap.org/browse/OJSI-105>`_]
        * In default deployment PORTAL (portal-sdk) exposes HTTP port 30212 outside of cluster. [`OJSI-106 <https://jira.onap.org/browse/OJSI-106>`_]
        * CVE-2019-12318 - Number of SQL Injections in Portal [`OJSI-174 <https://jira.onap.org/browse/OJSI-174>`_]
        * Portal stores users passwords encrypted instead of hashed [`OJSI-190 <https://jira.onap.org/browse/OJSI-190>`_]

*Known Vulnerabilities in Used Modules*

PORTAL code has been formally scanned during build time using NexusIQ and all Critical vulnerabilities have been addressed, items that remain open have been assessed for risk and determined to be false positive. The PORTAL open Critical security vulnerabilities and their risk assessment have been documented as part of the `project <https://wiki.onap.org/pages/viewpage.action?pageId=51283057>`_.

Quick Links:
     - `PORTAL project page <https://wiki.onap.org/display/DW/Portal+Platform+Project>`_

     - `Passing Badge information for PORTAL <https://bestpractices.coreinfrastructure.org/en/projects/1441>`_

     - `Project Vulnerability Review Table for PORTAL <https://wiki.onap.org/pages/viewpage.action?pageId=51283057>`_

**Upgrade Notes**
        * For https Apps onboarded to portal, a certificate has to be downloaded in the browser when first trying to access the landing page of the App.
        * For onboarded Apps using http (since Portal is using https) the browser asks the user to click to Proceed to the unsafe URL.
        * For onboarded Apps using http the icon in the URL bar will appear red, click on it and allow unsafe scripts.
        * The first time some apps are selected in the Applications panel, an error stating the webpage might be temporarily down, copy the presented URL to a new browser; once that is done, the application will open in the Portal.

**Deprecation Notes**

**Other**
        * Below are the docker images released as part of Portal Platform project:
        * onap/portal-app:2.5.0
        * onap/portal-db:2.5.0
        * onap/portal-sdk:2.5.0
        * onap/portal-wms:2.5.0
        * portal/sdk java artifacts - (Release branch: “release-2.5.0”)

Version: 2.3.2
--------------
:Release Date: 2019-04-15

.. toctree::
    :maxdepth: 1

This is the official release notes for the Casablanca Maintenance Release 3.0.2.

**Known Issues**
        * The issue is an application running on HTTPS will not open in Portal if the AAF root CA is missing.
          An error message will appear in a separate tab in Portal. It will say something like:
          “The webpage at https://portal.api.simpledemo.onap.org:30200/vid/welcome.htm?cc=........ might
          be temporarily down or it may have moved permanently to a new web address.”
          Here is the work-around, copy above VID (or other app) URL and replace welcome.htm to login.htm
          in a new browser window; after login come back to Portal home page and click VID, it will now work.

        * For applications running on HTTP (for example SDC), the user needs to disable the security check in the browser to access the application.

**Other**
        * Portal updated Keystore certificate from AAF to extend its expiry date; This change was made in OOM project.

Version: 2.3.1
--------------
:Release Date: 2019-01-31

.. toctree::
    :maxdepth: 1

This is the official release notes for the Casablanca Maintenance.

**Bug Fixes**
        * During installation Maria DB can now be accessed from within the portal-db container. The fix was made in OOM scripts to handle the db issue identified in the previous release.

Version: 2.3.0
--------------
:Release Date: 2018-11-30

.. toctree::
    :maxdepth: 1

We worked on SDK upgrade to integrate with AAF. We completed Architecture review for Portal and use case UI to support multi-language.

**New Features**
        * Platform Maturity Guidelines - Integrating with OOM
        * SDK upgrade to integrate with AAF
            * Use of Semantic Versioning - V3 is the supported version
            * Integration with AAF via REST; Supports both SDK and Framework Applications
        * 65% JUnit Test Coverage
        * Addressing security issues
        * Internationalization language support - design related
        * Reporting feature enhancement in portal/sdk - design and partial code changes
        * Platform Enhancements - Improved logging, docker separation, and SDK Simplification
        * Angular 5 upgraded with sample POC in SDK to build rich UI

**Bug Fixes**
        * Improved exception handling in reporting feature and also in login feature while getting a lock from Zookeeper.
        * Improved documentation to get access to Portal through port 8989.
        * Fixed Sonar reported critical issues.
        * Improved OOM deployment 30235 external port mapping for portal-sdk.

**Known Issues**
        * Mismatch while displaying active online user in Portal.
        * UI misaligned on updating widgets in Portal.
        * On Logout redirect landing page needs to be corrected.
        * Functional Menu change requires manual refresh.

**Security Notes**

PORTAL code has been formally scanned during build time using NexusIQ and all Critical vulnerabilities have been addressed, items that remain open have been assessed for risk and determined to be false positive. The PORTAL open Critical security vulnerabilities and their risk assessment have been documented as part of the `project <https://wiki.onap.org/pages/viewpage.action?pageId=35522356>`_.

Quick Links:
     - `PORTAL project page <https://wiki.onap.org/display/DW/Portal+Platform+Project>`_

     - `Passing Badge information for PORTAL <https://bestpractices.coreinfrastructure.org/en/projects/1441>`_

     - `Project Vulnerability Review Table for PORTAL <https://wiki.onap.org/pages/viewpage.action?pageId=35522356>`_

**Upgrade Notes**
        * For https Apps onboarded to portal, a certificate has to be downloaded in the browser when first trying to access the landing page of the App.
        * For onboarded Apps using http (since Portal is using https) the browser asks the user to click to Proceed to the unsafe URL.

**Deprecation Notes**

**Other**
        * Below are the docker images released as part of Portal Platform project:
        * onap/portal-app:2.3.1
        * onap/portal-db:2.3.1
        * onap/portal-sdk:2.3.1
        * onap/portal-wms:2.3.1
        * portal/sdk java artifacts - (Release branch: “release-2.4.0”)

Version: 2.2.0
--------------

:Release Date: 2018-06-07

.. toctree::
    :maxdepth: 1

We worked on hardening the ONAP Portal platform by improving code quality and addressing security issues.

**New Features**
        * Platform Maturity Guidelines
        * Integrating with MUSIC, OOM, and AAF
        * 50% JUnit Test Coverage
        * Addressing security issues
        * Bootstrapping of VID roles and tighter integration with AAF
        * Role Centralization capability for framework based partners - design related
        * Platform Enhancements - Improved logging, Security Hardening, and SDK Simplification

**Bug Fixes**
        * Issues with roles fixed in this release.
        * Now able to deselect widget on Widget catalog page
        * Replaced the word ECOMP to ONAP
        * Terminated menu access by App Admin User that are supposed to be available to Portal Admin only
        * Upgraded software packages to resolve security issues

**Known Issues**
        * Need to upgrade to new encrypt/decrypt algorithm in coordination with Partnering apps
        * Logging needs improvement
        * Not able to delete portal admin user
        * Add support to connect with AAF Runtime
        * Portal's SDK UI documentation in ONAP wiki needs samples
        * The Portal/SDK fn_user table has encrypted passwords that need to change to using a hash algorithm
        * UI cleanup needed: on adding entries to News Widget and display on Application Onboarding page

**Security Issues**
        * https://wiki.onap.org/pages/viewpage.action?pageId=27689089

**Security Notes**

PORTAL code has been formally scanned during build time using NexusIQ and all Critical vulnerabilities have been addressed, items that remain open have been assessed for risk and determined to be false positive. The PORTAL open Critical security vulnerabilities and their risk assessment have been documented as part of the `project <https://wiki.onap.org/pages/viewpage.action?pageId=27689089>`_.

Quick Links:
     - `PORTAL project page <https://wiki.onap.org/display/DW/Portal+Platform+Project>`_

     - `Passing Badge information for PORTAL <https://bestpractices.coreinfrastructure.org/en/projects/1441>`_

     - `Project Vulnerability Review Table for PORTAL <https://wiki.onap.org/pages/viewpage.action?pageId=27689089>`_

**Upgrade Notes**
        * Upgrades to Portal platform can be performed using Heat based installation scripts available under demo repository.

**Deprecation Notes**
        * The encryption algorithm used in Portal is now changed from AES to AES/CBC/PKCS5PADDING.

**Other**
        * Below are the docker images released as part of Portal Platform project:
        * onap/portal-db:v2.2.0
        * onap/portal-apps:v2.2.0
        * onap/portal-wms:v2.2.0
        * onap//music/music-cassandra:v3.0
        * zookeeper:v3.4.0
        * portal/sdk - (Release branch: "release-2.2.0")

Version: 1.3.0
--------------

:Release Date: 2017-11-16

The ONAP Portal is a platform that provides the ability to integrate different ONAP applications into a centralized Portal Core. The platform seed code is improved with below listed enhancements in this release. This is technically the first release of ONAP Portal Platform, previous release was the seed code contribution. As such, the defects fixed in this release were raised during the course of the release and while its integration testing. Anything not closed is captured below under Known Issues. If you want to review the defects fixed in the Amsterdam release, refer to Jira (jira.onap.org).

**New Features**
        * Digital Experience Control/UI upgrade.
        * Portal Notification Enhancement and act on it w/o copy/paste, e.g. hyperlink to target function with context transfer.
        * Prepared onboarding App process where the partner is ready for centralized user authentication via AAF.
        * Source code of Portal Platform and its SDK is released under the following repositories on gerrit.onap.org
        * portal - (Release branch: "release-1.3.0")
        * portal/sdk - (Release branch: "release-1.3.2")

**Bug Fixes**
        * Onboarding script updated due to user/role integration/synchronization issues with Partner Applications.
        * Fixed search and remove bugs in Widget Onboarding.
        * Fixed issues in the Application Onboarding.
        * Fixed issues in the Microservice Onboarding.
        * Fixed deployment scripts and streamlined the reference variables.

**Known Issues**
        * `PORTAL-140 <https://jira.onap.org/browse/PORTAL-140>`_ - Portal role synch error with partner apps.

**Security Issues**
        * The issue "`PORTAL-137 <https://jira.onap.org/browse/PORTAL-137>`_ -Enhance Authentication" is fixed in Portal and in its SDK. The Portal team recommend partnering apps like Policy, VID, AAI, and SDC to upgrade to SDK's 1.3.2 or latest version to address the login vulnerability.

**Upgrade Notes**
        * This is an initial release.

**Deprecation Notes**
        * This is an initial release.

**Other**
        * Below are the docker images released as part of Portal Platform project:
        * onap/portal-db:v1.3.0
        * onap/portal-apps:v1.3.0
        * onap/portal-wms:v1.3.0

End of Release Notes
