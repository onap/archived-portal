#!/bin/bash

# This docker image name is hardcoded at the ONAP demo repository,
# so changing it is nontrivial.
EP_IMG_NAME=ep:1610-1
EP_CONT_NAME=onap_portal
DB_IMG_NAME=ecompdb:portal
DB_CONT_NAME=ecompdb_portal
WMS_IMG_NAME=widget-ms
WMS_CONT_NAME=ecomp-portal-widget-ms
VERSION=1.1.0
ETCDIR=etc
WORKINGDIR=PROJECT
SDK_DIR=PROJECT/Source/ecomp-portal-core/ecomp-portal-BE-os/target/ecompportal-be-os
FE_DIR=PROJECT/Source/ecomp-portal-core/ecomp-portal-FE-os
SCRIPT_COMMON_DIR="PROJECT/Source/ecomp-portal-core/ecomp-portal-DB-common/"
SCRIPT_DIR="PROJECT/Source/ecomp-portal-core/ecomp-portal-DB-os/"
SDK_COMMON_SCRIPT_DIR=PROJECT/Source/ecomp-portal-core/ecomp-sdk/epsdk-app-common/db-scripts
SDK_SCRIPT_DIR=PROJECT/Source/ecomp-portal-core/ecomp-sdk/epsdk-app-os/db-scripts
DBC_COMMON_SCRIPT_DIR=PROJECT/Source/ST_DBPA/dcae_dmaapbc_webapp/dbca-common/db-scripts
DBC_SCRIPT_DIR=PROJECT/Source/ST_DBPA/dcae_dmaapbc_webapp/dbca-os/db-scripts
PORTAL_SDK_DIR=PROJECT/Source/ecomp-portal-core/ecomp-sdk/epsdk-app-os/target/epsdk-app-os
PORTAL_DBC_DIR=PROJECT/Source/ST_DBPA/dcae_dmaapbc_webapp/dbca-os/target/ep-dbc-app
