.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. http://creativecommons.org/licenses/by/4.0
.. Copyright 2017 AT&T Intellectual Property.  All rights reserved.


Portal Platform Release Notes
=============================

Version: 2.1.0
--------------

:Release Date: 24 May 2018
 
.. toctree::
    :maxdepth: 1		   

We worked on hardening the ONAP Portal platform by improving code quality and addressing security issues.
	
**New Features**
	* Platform Maturity Guidelines (Highest Priority)
        * Integrating with MUSIC, OOM, and AAF
        * 50% JUnit Test Coverage 
        * Addressing security issues
    * Bootstrapping of VID roles and tighter integration with AAF (Medium Priority)
    * Role Centralization capability for framework based partners - design related (Low Priority)
    * Platform Enhancements - Improved logging, Security Hardening, and SDK Simplification (Low Priority)
	
**Bug Fixes**
    * 
    *
    *

**Known Issues**
	* 

**Security Issues**
	* 

**Upgrade Notes**
	* This is the second release.
	
**Deprecation Notes**
	* This is the second release.
	
**Other**
	* Below are the docker images released as part of Portal Platform project:
	* onap/portal-db:
	* onap/portal-apps:
	* onap/portal-wms: 

	
Version: 1.3.0
--------------

:Release Date: 16 November 2017
 
.. toctree::
    :maxdepth: 1		   

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
	* Fixed deplyoment scripts and streamlined the reference variables.

**Known Issues**
	* `PORTAL-140 <https://jira.onap.org/browse/PORTAL-140>`_ - Portal role synch error with partner apps.

**Security Issues**
	* The issue "`PORTAL-137 <https://jira.onap.org/browse/PORTAL-137>`_ -Enhance Authentication" is fixed in Portal and in its SDK. The Portal team recommend partnering apps like Policy, VID, AAI, SDC to upgrade to SDK's 1.3.2 or latest version to address the login vulnerability.

**Upgrade Notes**
	* This is an initial release.
	
**Deprecation Notes**
	* This is an initial release.
	
**Other**
	* Below are the docker images released as part of Portal Platform project:
	* onap/portal-db:v1.3.0 
	* onap/portal-apps:v1.3.0 
	* onap/portal-wms:v1.3.0 

===========

End of Release Notes
