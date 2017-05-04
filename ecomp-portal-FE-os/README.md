# ECOMP Portal Web Application Front End for Open-Source Use

## Overview

This is a Maven project with the ECOMP Portal front-end files for public
release, containing font files, image files and other static files.
This project uses Maven to copy in ("overlay") the contents of the ECOMP
Portal FE common files at package time.

Use Apache Maven to build and package this collection of files using Bower,
Grunt and NPM.

## Release Notes

Release notes for all ECOMP Portal *Open Source specific* files, including
front-end files, are tracked in file ecomp-portal-BE-os/README.md

## Installation Notes

TODO: These notes are somewhat outdated.

### Prerequisites

1. [node.js](https://nodejs.org/en/) - ssp project is running on node.js 4.1.1 please install from the link
2. bower - install with npm: `npm install -g bower` _NOTE: mac/linux users should run the following command with `sudo`_
3. [git](https://git-scm.com/) - install git command line tool _NOTE: please select to add git command line into the PATH variable for it to be accessible from any folder_
4. grunt - install grunt with npm: `npm install -g grunt-cli` _NOTE: mac/linux users should run the following command with `sudo`_


### First run
_NOTE: some npm modules inside the project does not support yet the new node.js 4.1.1, npm install command will probably throw an error. even so, everything should work, so no need to worry

1. run `npm install` to install all the server side dependencies
2. run `bower install` to install all the client side dependencies

### Running the project

this project it supported by grunt task manager, below are the different run options
ssp project can be run with different client configurations per environment, the environment is set byt the option `--env=<environement name>`
the following evironments are available:

mock - mock will activate the mock server and will return static data examples from predefined json files.

### Dev time

1. to start the project in dev mode under mock please run: `grunt serve --env=mock`
2. for any other client configuration change the `--env` option accordingly to env options available 


### Tests

There are 3 options available for tests:

1. `grunt test` will run all re server and the client tests all together
2. `grunt test:client` will run only the client tests
3. `grunt test:server` will run only the server tests

### Build time

Execute below commands

npm cache clean
npm install
npm install -g bower
bower install
grunt build --env=integ

COPY ${FE_DIR}/dist/public into ${BE_DIR}/public

deploy BE project onto tomcat and goto login page http://portal.openecomp.org:8989/ECOMPPORTAL/login.htm
