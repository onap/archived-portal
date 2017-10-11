#!/bin/bash
# Builds Portal and Portal-SDK webapps; packages all into a docker.
# Prereq: all projects have been cloned from git.
# Expects to be invoked with CWD=portal/deliveries
# Caches files in local directory for docker build.

# Stop on error; show output
set -e -x

# This reuses the docker-compose file
echo "Set image tag name variables"
source $(dirname $0)/.env

# Work standalone AND in the ONAP Jenkins.
# Pick up Jenkins settings for this script.
# Use -B for batch operation to skip download progress output
if [ -n "$MVN" ]; then
    export MVN="${MVN} -B -gs ${GLOBAL_SETTINGS_FILE} -s ${SETTINGS_FILE}"
else
    # Force refresh of snapshots
    MVN="mvn -B -U"
fi

# This expects to start in the deliveries folder; make sure
PORTAL_DOCKERFILE=Dockerfile.portalapps
if [ ! -f $PORTAL_DOCKERFILE ] ; then
    echo "Failed to find file ${PORTAL_DOCKERFILE}; must start in deliveries folder; exiting"
    exit 1
fi

# Store directory names as variables
# This is the Docker Project area.
DELIV="$(pwd)"
# parent directory, for finding source projects
cd ..
BASE="$(pwd)"
cd $DELIV

# Relative path of temp directory
BUILD_REL="build"
# Absolute path of temp directory
BUILD_ABS=$DELIV/$BUILD_REL
rm -fr $BUILD_REL
mkdir $BUILD_REL

# Copy DDL/DML to required directories

# RELATIVE PATHS to local directories with database scripts
# bcos Docker looks within this build area only
SCR_BASE=$BUILD_REL/scripts
PORTAL_SCRIPT_DIR=$SCR_BASE/ecomp-portal-DB-os
SDK_SCRIPT_DIR=$SCR_BASE/epsdk-app-os
mkdir -p ${PORTAL_SCRIPT_DIR} ${SDK_SCRIPT_DIR}

# copy over DB scripts for the dockerfiles
# Portal
cp $BASE/ecomp-portal-DB-common/*.sql ${PORTAL_SCRIPT_DIR}
cp $BASE/ecomp-portal-DB-os/*.sql ${PORTAL_SCRIPT_DIR}
# SDK app
cp $BASE/sdk/ecomp-sdk/epsdk-app-common/db-scripts/*.sql ${SDK_SCRIPT_DIR}
cp $BASE/sdk/ecomp-sdk/epsdk-app-os/db-scripts/*.sql ${SDK_SCRIPT_DIR}

# build database docker
DB_DOCKER_CMD="
  docker build -t ${DB_IMG_NAME}:${PORTAL_TAG} ${PROXY_ARGS}
    --build-arg PORTAL_SCRIPT_DIR=${PORTAL_SCRIPT_DIR}
    --build-arg SDK_SCRIPT_DIR=${SDK_SCRIPT_DIR}
    -f Dockerfile.mariadb .
"
echo "Build mariadb docker image"
$DB_DOCKER_CMD

echo "Build all jar and war files in Portal"
cd $BASE
${MVN} clean install

echo "Copy Portal app BE"
cd $BASE/ecomp-portal-BE-os
cp target/ecompportal-be-os.war $BUILD_ABS

echo "Copy Portal app FE"
cd $BASE/ecomp-portal-FE-os/
cp -r dist/public $BUILD_ABS

echo "Copy Portal widget-ms"
cd $BASE/ecomp-portal-widget-ms
cp widget-ms/target/widget-ms.jar $BUILD_ABS

echo "Build and copy Portal-SDK app"
cd $BASE/sdk/ecomp-sdk/epsdk-app-os
${MVN} clean package
cp target/epsdk-app-os.war $BUILD_ABS

PROXY_ARGS=""
if [ $HTTP_PROXY ]; then
    PROXY_ARGS+="--build-arg HTTP_PROXY=${HTTP_PROXY}"
fi
if [ $HTTPS_PROXY ]; then
    PROXY_ARGS+=" --build-arg HTTPS_PROXY=${HTTPS_PROXY}"
fi

echo "Build portal docker image"
cd $DELIV
PORTAL_DOCKER_CMD="
  docker build -t ${EP_IMG_NAME}:${PORTAL_TAG} ${PROXY_ARGS}
    --build-arg FE_DIR=$BUILD_REL/public
    --build-arg PORTAL_WAR=$BUILD_REL/ecompportal-be-os.war
    --build-arg SDK_WAR=$BUILD_REL/epsdk-app-os.war
    -f $PORTAL_DOCKERFILE .
"
$PORTAL_DOCKER_CMD

echo "Bbuild widget-ms docker image"
WMS_DOCKER_CMD="
  docker build -t ${WMS_IMG_NAME}:${PORTAL_TAG} ${PROXY_ARGS}
    --build-arg WMS_JAR=$BUILD_REL/widget-ms.jar
    -f Dockerfile.widgetms .
"
$WMS_DOCKER_CMD

# For ease of debugging, leave the build dir
# echo "Cleaning up"
# rm -fr $BUILD_REL
