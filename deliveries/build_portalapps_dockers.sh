#!/bin/bash
# Builds Portal, Portal-SDK and DMaaP-BC webapps;
# then packages all into a docker.
# Prereq: all three projects have been cloned from git.
# Expects to be invoked with CWD=portal/deliveries
# Caches files in local directory for docker build.

# Stop on error; show output
set -e -x

# For debugging only bcox the FE build takes a long time
SKIPFE=N
if [ $# -gt 0 -a "$1" == "skipfe" ] ; then
    echo "Skipping Portal-FE build step"
    SKIPFE=Y
fi

# Establish environment variables
echo "Set variables"
source $(dirname $0)/os_settings.sh

# Work standalone AND in the ONAP Jenkins.
# Pick up Jenkins settings for this script.
# Use -B for batch operation to skip download progress output
if [ -n "$MVN" ]; then
    export MVN="${MVN} -B -gs ${GLOBAL_SETTINGS_FILE} -s ${SETTINGS_FILE}"
else
    MVN=mvn
fi

# This expects to start in the deliveries folder; make sure
DOCKERFILE=Dockerfile.portalapps
if [ ! -f $DOCKERFILE ] ; then
    echo "Failed to find expected file; must start in deliveries folder"
    exit 1
fi

# Establish directories and variables
DELIV="$(pwd)"
# Relative path of temp directory
BUILD="build"
# Absolute path of temp directory
OUT=$DELIV/$BUILD
if [ $SKIPFE == "Y" ]; then
    echo "Skipping clean/recreate of $OUT"
else
    rm -fr $OUT
    mkdir $OUT
fi
# parent directory
cd ..
BASE="$(pwd)"

# Copy DDL/DML to required directories (old scripts use long path /PROJECT/...)
cd $DELIV
rm -fr PROJECT
# copy over DB scripts for the dockerfiles
# forgive the ugly trick with the .. at end.
mkdir -p ${SCRIPT_COMMON_DIR}     && cp -r $BASE/ecomp-portal-DB-common ${SCRIPT_COMMON_DIR}/..
mkdir -p ${SCRIPT_DIR}            && cp -r $BASE/ecomp-portal-DB-os ${SCRIPT_DIR}/..
mkdir -p ${SDK_COMMON_SCRIPT_DIR} && cp -r $BASE/sdk/ecomp-sdk/epsdk-app-common/db-scripts ${SDK_COMMON_SCRIPT_DIR}/..
mkdir -p ${SDK_SCRIPT_DIR}        && cp -r $BASE/sdk/ecomp-sdk/epsdk-app-os/db-scripts ${SDK_SCRIPT_DIR}/..
# Build complete database script for DBC
DBCA_OPEN_SD=$BASE/dmaapbc/dcae_dmaapbc_webapp/dbca-os/db-scripts
DBCA_COMM_SD=$BASE/dmaapbc/dcae_dmaapbc_webapp/dbca-common/db-scripts
# Old scripts expect this path
mkdir -p $DBC_SCRIPT_DIR
cat $DBCA_OPEN_SD/dbca-create-mysql-1707-os.sql $DBCA_COMM_SD/dbca-ddl-mysql-1707-common.sql $DBCA_OPEN_SD/dbca-dml-mysql-1707-os.sql > $DBC_SCRIPT_DIR/dbca-complete-mysql-1707-os.sql

cd $BASE/ecomp-portal-BE-common
${MVN} clean install

cd $BASE/ecomp-portal-BE-os
${MVN} clean package
cp target/ecompportal-be-os.war $OUT

cd $BASE/ecomp-portal-FE-os/
if [ $SKIPFE == "Y" ]; then
    echo "Skipping MVN in $(pwd)"
else
    ${MVN} clean package
    cp -r dist/public $OUT
fi

cd $BASE/sdk/ecomp-sdk/epsdk-app-os
${MVN} clean package
cp target/epsdk-app-os.war $OUT

cd $BASE/dmaapbc/dcae_dmaapbc_webapp
${MVN} clean package
cp dbca-os/target/dmaap-bc-app-os.war $OUT

PROXY_ARGS=""
if [ $HTTP_PROXY ]; then
    PROXY_ARGS+="--build-arg HTTP_PROXY=${HTTP_PROXY}"
fi
if [ $HTTPS_PROXY ]; then
    PROXY_ARGS+=" --build-arg HTTPS_PROXY=${HTTPS_PROXY}"
fi

# build portal docker
cd $DELIV
PORTAL_DOCKER_CMD="
	docker build -t ${EP_IMG_NAME} ${PROXY_ARGS}
	--build-arg FE_DIR=$BUILD/public
	--build-arg PORTAL_WAR=$BUILD/ecompportal-be-os.war
	--build-arg SDK_WAR=$BUILD/epsdk-app-os.war
	--build-arg DBC_WAR=$BUILD/dmaap-bc-app-os.war
	-f $DOCKERFILE .
"
echo "Invoking portal docker build"
$PORTAL_DOCKER_CMD

# Build widget-ms docker
cd $BASE/ecomp-portal-widget-ms
${MVN} package docker:build
