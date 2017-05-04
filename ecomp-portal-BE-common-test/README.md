# ECOMP Portal Web Application Back End for AT&T Internal Use

## Overview

This is a Maven project with the ECOMP Portal web application back-end test files 
for internal use, containing test cases for  controllers, interceptors and other Java classes
for the AT&T version. This project uses the Maven war plugin to copy in ("overlay")
the contents of the ECOMP Portal web application back-end common distribution at
package time. 

Use Apache Maven to build.

For more information please visit:

	https://wiki.web.att.com/display/EcompPortal/ECOMP+Portal+Home

## Release Notes

This file tracks all changes to AT&T-specific versions, including back-end *and*
All of the release notes in the ecomp-portal-BE-common-test area apply.


-US874768 Created a new project to have common junit test cases which can be run from ecomp-portal-BE-att
  -Created PopulateTestData.sql and RemoveTestData.sql to populate and rollback the test data
  -Created Coomon configuration files ApplicationCommonContextTestSuite and MockitoTestSuite
    which can be extended from respective test classes.
  -Added Test cases for AppCatalogController  

