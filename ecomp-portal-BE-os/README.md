# ECOMP Portal Web Application Back End for Open Source

## Overview

This is a Maven project with the ECOMP Portal web application back-end files
for public release, containing Java files specific to requirements of the
open-source version.  This project uses the Maven war plugin to copy in
("overlay") the contents of the ECOMP Portal web application back-end
common distribution at package time.

Use Apache Maven to build, package and deploy this webapp to a web container
like Apache Tomcat.  Eclipse users must install the M2E-WTP connector, see 
https://www.eclipse.org/m2e-wtp/

## Release Notes

Version 1.1.0 (Amsterdam), November 2017
- [Portal-6] Updates to License and Trademark in the PORTAL Source Code
- [Portal-7] Improvements added as part of the rebasing process
- [Portal-17] Remove jfree related items
- [PORTAL-21] FE changes to OS for AAF centralization and name space field; DB script updates for EcompPortalDDLMySql_1710_Common.sql under ecomp-portal-DB-common, EcompPortalDMLMySql_1710_OS.sql under ecomp-portal-DB-os;
- [Portal-30] Failed to communicate with the widget microservice: Fixed
- [Portal-35] Replaced the portal logo with onap logo on the login screen.
- [Portal-40] Fix to add user roles 
- [Portal-45] Fix to update an existing app on Application Onboarding
- [Portal-47] Fix to eliminate duplicate roles on Users page
- [Portal-48] Fix to save a new app on Application onboarding
- [Portal-49] image icon is missing on Widget corner
- [Portal-63] remove att_abs_tpls*.js and greensock url
- [Portal-69] unable to pick role in Functional Menu Update
- [Portal-73] unable to onboard new Application fix
- [Portal-50] Enabled the junit coverage in ONAP
- [Portal-76] Edit functional menu modal doesn't show
- [Portal-61] Fixed the routing problem, loaded data and changed the notification hyperlink 
- [Portal-77] Changes to remove preview image and update the new image automatically on App onbarding page
- [Portal-82] Unable to pick Role in Widget onboarding for assigned app
- [Portal-94] Unable to see the updates on Edit Functional Menu
- [Portal-104] Replaced mysql DB connector with mariaDB
- [Portal-72] Sonar scan - resolving severity Blocker issues 
- [Portal-103] Fix to enhanced notification ticket source for ONAP
- [Portal-50] Fix to get the Sonar coverage for ONAP
- [Portal-86] Remove internal att.com usages from tests and other files
- [Portal-102] Fixed the page refresh issue on App Account Management page
- [Portal-104] replace mysql DB connector with mariaDB connector
- [Portal-116] Empty Widgets on Home page - widget uploaded with a wrong format in db 
- [Portal-59] Fix to to display Portal users on User page
- [Portal-127] Remove GreenSock license code from b2b library
Version 1.0.0, February 2017
- Initial release
