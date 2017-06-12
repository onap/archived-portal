# ECOMP Portal Web Application Front End Common Files

## Overview

This project contains the ECOMP Portal Front End common files: CSS, JavaScript, 
HTML and other static files that are used in the internal and ONAP versions of
of the ECOMP Portal web application.  Those specific versions are built using
separate Maven projects that copy in ("overlay") the contents of this project
at package time.

## Static files

Static files should be entered into the ecomp-portal-FE-common/home directory, 
whereby home = the webserver html root directory.  The files and directories 
will be copied exactly as they are placed.
