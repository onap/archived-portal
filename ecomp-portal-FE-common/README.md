# ECOMP Portal Web Application Front End Common Files

## Overview

This project contains the ECOMP Portal Front End common files: CSS, JavaScript, 
HTML and other static files that are used in the AT&T internal version and the
open-source versions of the ECOMP Portal web application.  Those specific versions
are built using separate Maven projects that copy in ("overlay") the contents of
this project at package time.

## Release Notes

Release notes for all ECOMP Portal *COMMON* files, including front-end files,
are tracked in file ecomp-portal-BE-common/README.md

## Static files

Static files should be entered into the ecomp-portal-FE-common/home directory, 
whereby home = the webserver html root directory.  The files and directories 
will be copied exactly as they are placed.
