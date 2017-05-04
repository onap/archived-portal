# ECOMP Portal Web Application Back End Common Files

## Overview

This is a Maven overlay project with the ECOMP Portal back-end common files. 
This is not a stand-alone web application.  This project has common Java classes
that are used for the ECOMP Portal web application.  Those specific versions are built using separate
Maven projects that copy in ("overlay") the contents of this project at package time.


## Release Notes

Release notes for all ECOMP Portal *COMMON* files, back-end files and front-end files,
are tracked here for convenience.

The last number in each build identifier is assigned by the Jenkins CI system to
the back-end component.



Build 1.1.0-SNAPSHOT, ?? May 2017
[ PORTAL 7] Rebase; This rebasing includes addition of common libraries and common overlays projects
and abstraction of components