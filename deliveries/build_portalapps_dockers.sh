#!/bin/bash
# Builds Portal and Portal-SDK webapps; packages all into a docker.
# Prereq: all projects have been cloned from git.
# Expects to be invoked with CWD=portal/deliveries
# Caches files in local directory for docker build.

# Stop on error; show output
set -e -x

# This reuses the docker-compose environment file
echo "Set image tag name variables"
source $(dirname $0)/.env
if [ $1 ]; then
  echo "Sourcing extra parameters from $1"
  source $(dirname $0)/$1
else
  echo "Using only base parameters from .env"
fi

# Check for Jenkins build number
if [ -n "$BUILD_NUMBER" ]; then
    echo "Using Jenkins build number $BUILD_NUMBER; Docker Tag $PORTAL_TAG"
else
    # This indicates a non-Jenkins build
    export BUILD_NUMBER="999"
    echo "Using Default build number $BUILD_NUMBER; Docker Tag $PORTAL_TAG"
    
fi

# Must work when called by ONAP Jenkins AND local builds.
# Pick up Jenkins settings for this script.
# Use -B for batch operation to skip download progress output
if [ -n "$MVN" ]; then
    export MVN="${MVN} -B -gs ${GLOBAL_SETTINGS_FILE} -s ${SETTINGS_FILE} -Dbuild.number=$BUILD_NUMBER"
else
    # Force refresh of snapshots
    MVN="mvn -B -U -Dbuild.number=$BUILD_NUMBER"
fi

# This expects to start in the deliveries folder; make sure
if [ "$PORTAL_DOCKERFILE" != "skip"] && [ ! -f $PORTAL_DOCKERFILE ] ; then
    echo "Failed to find file ${PORTAL_DOCKERFILE}; must start in deliveries folder; exiting"
    exit 1
fi

# Store directory names as variables
# This is the deliveries area.
DELIVDIR="$(pwd)"
# parent directory, for finding source projects
cd ..
BASEDIR="$(pwd)"
cd $DELIVDIR

# Relative path of temp directory
BUILD_REL="build"
# Absolute path of temp directory
BUILD_ABS=$DELIVDIR/$BUILD_REL

# Build Java projects.
# (use env var toskip when debugging Docker build problems)
if [ "$SKIP_JAVA_BUILD" = "please" ]; then

	echo "SKIPPING JAVA BUILD!"

else
	echo "Starting Java build."

	# Clean out and recreate
	rm -fr $BUILD_REL
	mkdir $BUILD_REL

	echo "Build jar and war files"
	cd $BASEDIR
	${MVN} ${MVN_EXTRA_PORTAL} clean install

        if [ "$SDK_DOCKERFILE" != "skip" ] && [ "SDK_APP_DIR" != "skip" ]; then
	  echo "Build Portal-SDK app"
	  cd $BASEDIR/$SDK_APP_DIR
	  ${MVN} ${MVN_EXTRA_SDK} clean package
	fi

	echo "Java build complete."
fi

if [ "$BE_DOCKERFILE" != "skip" ] || [ "PORTAL_DOCKERFILE" != "skip" ]; then
  echo "Copy Portal app BE"
  cp $BASEDIR/$BE_WAR_DIR/$BE_WAR_FILE $BUILD_ABS
fi

if [ "$FE_DOCKERFILE" != "skip" ] || [ "PORTAL_DOCKERFILE" != "skip" ]; then
  echo "Copy Portal app FE"
  cp -r $BASEDIR/$FE_DIR $BUILD_ABS
fi

if [ "$WMS_DOCKERFILE" != "skip" ]; then
  echo "Copy Portal widget-ms"
  cp $BASEDIR/$WIDGET_MS_JAR_DIR/$WIDGET_MS_JAR_FILE $BUILD_ABS
fi

if [ "$SDK_DOCKERFILE" != "skip" ] && [ "SDK_APP_DIR" != "skip" ]; then
  echo "Copy Portal-SDK app build results"
  cp $BASEDIR/$SDK_WAR_DIR/$SDK_WAR_FILE $BUILD_ABS
fi

# Build Docker images

PROXY_ARGS=""
if [ $HTTP_PROXY ]; then
    PROXY_ARGS+="--build-arg HTTP_PROXY=${HTTP_PROXY}"
fi
if [ $HTTPS_PROXY ]; then
    PROXY_ARGS+=" --build-arg HTTPS_PROXY=${HTTPS_PROXY}"
fi

# must work in delivery directory
cd $DELIVDIR

if [ "$DB_DOCKERFILE" = "skip" ]; then 
  echo "SKIPPING DB DOCKER BUILD!"
else 
# Copy DDL/DML to required directories
# RELATIVE PATHS to local directories with database scripts
# bcos Docker looks within this build area only
  DB_SCRIPT_DIR=$BUILD_REL/db-scripts
  mkdir -p ${DELIVDIR}/${DB_SCRIPT_DIR}
# Portal
  cp $BASEDIR/ecomp-portal-DB-common/*.sql ${DB_SCRIPT_DIR}
  cp $BASEDIR/ecomp-portal-DB-os/*.sql ${DB_SCRIPT_DIR}
# SDK app
  cp $BASEDIR/sdk/ecomp-sdk/epsdk-app-common/db-scripts/*.sql ${DB_SCRIPT_DIR}
  cp $BASEDIR/sdk/ecomp-sdk/epsdk-app-os/db-scripts/*.sql ${DB_SCRIPT_DIR}

  echo "Build mariadb docker image"
  DB_DOCKER_CMD="
    docker build -t ${DB_IMG_NAME}:${PORTAL_TAG} ${PROXY_ARGS}
      --build-arg DB_SCRIPT_DIR=${DB_SCRIPT_DIR}
      -f $DB_DOCKERFILE .
  "
  $DB_DOCKER_CMD
fi

# Copy cassandra scripts to required directories
# Portal
#cp $BASEDIR/ecomp-portal-DB-common/*.cql ${DELIVDIR}
# SDK app
#cp $BASEDIR/sdk/ecomp-sdk/epsdk-app-common/db-scripts/*.cql ${DELIVDIR}

# Build Docker Images

#Combined FE/BE image
if [ "$PORTAL_DOCKERFILE" = "skip" ]; then
  echo "SKIPPING PORTAL DOCKER IMAGE BUILD!"
else
  echo "Build portal docker image"
  PORTAL_DOCKER_CMD="
    docker build -t ${EP_IMG_NAME}:${PORTAL_TAG} ${PROXY_ARGS}
      --build-arg FE_DIR=$BUILD_REL/public
      --build-arg PORTAL_WAR=$BUILD_REL/$BE_WAR_FILE
      --build-arg SERVERXML=${SERVER_XML_DIR}/server.xml
      --build-arg PORTALCONTEXT=$PORTALCONTEXT
      -f $PORTAL_DOCKERFILE .
  "
  $PORTAL_DOCKER_CMD
fi

if [ "$SDK_DOCKERFILE" = "skip" ]; then
  echo "SKIPPING SDK DOCKER IMAGE BUILD!"
else
  echo "Build sdk demo app docker image"
  SDK_DOCKER_CMD="
    docker build -t ${SDK_IMG_NAME}:${PORTAL_TAG} ${PROXY_ARGS}
      --build-arg SDK_WAR=$BUILD_REL/$SDK_WAR_FILE
      --build-arg SDKCONTEXT=$SDKCONTEXT
      -f $SDK_DOCKERFILE .
  "
  $SDK_DOCKER_CMD
fi

if [ "$BE_DOCKERFILE" = "skip" ]; then
  echo "SKIPPING BE DOCKER IMAGE BUILD!"
else 
  echo "Build portal be image"
  BE_DOCKER_CMD="
    docker build -t ${BE_IMG_NAME}:${PORTAL_TAG} ${PROXY_ARGS}
      --build-arg PORTAL_WAR=$BUILD_REL/$BE_WAR_FILE
      --build-arg SERVERXML=${SERVER_XML_DIR}/server.xml
      --build-arg PORTALCONTEXT=$PORTALCONTEXT
      --build-arg BE_BASE_IMAGE=$BE_BASE_IMAGE
      -f $BE_DOCKERFILE .
  "
  $BE_DOCKER_CMD
fi

if [ "$FE_DOCKERFILE" = "skip" ]; then
  echo "SKIPPING FE DOCKER IMAGE BUILD!"
else 
  echo "Build portal fe image"
  FE_DOCKER_CMD="
    docker build -t ${FE_IMG_NAME}:${PORTAL_TAG} ${PROXY_ARGS}
      --build-arg FE_DIR=$BUILD_REL/public
      --build-arg FECONTEXT=$FECONTEXT
      --build-arg FE_BASE_IMAGE=$FE_BASE_IMAGE
      -f $FE_DOCKERFILE .
  "
  $FE_DOCKER_CMD
fi

if [ "$WMS_DOCKERFILE" = "skip" ]; then
  echo "SKIPPING WIDGET-MS DOCKER IMAGE BUILD!"
else
  echo "Build widget-ms docker image"
  WMS_DOCKER_CMD="
    docker build -t ${WMS_IMG_NAME}:${PORTAL_TAG} ${PROXY_ARGS}
      --build-arg WMS_JAR=$BUILD_REL/$WIDGET_MS_JAR_FILE
      -f Dockerfile.widgetms .
  "
  $WMS_DOCKER_CMD
fi

# For ease of debugging, leave the build dir
# echo "Cleaning up"
# rm -fr $BUILD_REL
