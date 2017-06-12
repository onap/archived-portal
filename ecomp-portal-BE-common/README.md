# ECOMP Portal Web Application Back End Common Files

## Overview

This is a Maven overlay project with the ECOMP Portal back-end common files. 
This is not a stand-alone web application.  This project has common Java classes
that are used in the internal and ONAP versions of the ECOMP Portal web application. 
Those specific versions are built using separate Maven projects that copy in
("overlay") the contents of this project at package time and that use the jar
built by this project.