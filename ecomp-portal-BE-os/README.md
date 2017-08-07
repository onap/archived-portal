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

Version 1.1.0, July 2017
- [Portal-6] Updates to License and Trademark in the PORTAL Source Code
- [Portal-7] Improvements added as part of the rebasing process
- [Portal-17] Remove jfree related items
- [Portal-30] Failed to communicate with the widget microservice: Fixed
- [Portal-35] Replaced the portal logo with onap logo on the login screen.
- [Portal-40] Fix to add user roles 
- [Portal-45] Fix to update an existing app on Application Onboarding
- [Portal-47] Fix to eliminate duplicate roles on Users page
- [Portal-48] Fix to save a new app on Application onboarding
- [Portal-49] image icon is missing on Widget corner
- [Portal-63] remove att_abs_tpls*.js and greensock url

Version 1.0.0, February 2017
- Initial release
