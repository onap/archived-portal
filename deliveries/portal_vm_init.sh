#!/bin/bash
# Starts docker containers for ONAP Portal
# This version for Amsterdam/R1 of Portal, uses docker-compose.
# Temporarily maintained in portal/deliveries area;
# replicated from the ONAP demo/boot area due to release concerns.

# be verbose
set -x

# Establish environment variables
NEXUS_USERNAME=$(cat /opt/config/nexus_username.txt)
NEXUS_PASSWD=$(cat /opt/config/nexus_password.txt)
NEXUS_DOCKER_REPO=$(cat /opt/config/nexus_docker_repo.txt)
DOCKER_IMAGE_VERSION=$(cat /opt/config/docker_version.txt)
# Can use this version instead to use snapshot versions:
# DOCKER_IMAGE_VERSION=latest
# CLI has a different version than Portal
CLI_IMAGE_VERSION=1.1-STAGING-latest

# Refresh configuration and scripts
cd /opt/portal
git pull
cd deliveries

# Get variables from docker-compose environment file
source .env

# Copy property files to new directory
mkdir -p $PROPS_DIR
cp -r properties_rackspace/* $PROPS_DIR
# Also create logs directory
mkdir -p $LOGS_DIR

# Refresh images
docker login -u $NEXUS_USERNAME -p $NEXUS_PASSWD $NEXUS_DOCKER_REPO
docker pull $NEXUS_DOCKER_REPO/openecomp/${DB_IMG_NAME}:$DOCKER_IMAGE_VERSION
docker pull $NEXUS_DOCKER_REPO/openecomp/${EP_IMG_NAME}:$DOCKER_IMAGE_VERSION
docker pull $NEXUS_DOCKER_REPO/openecomp/${WMS_IMG_NAME}:$DOCKER_IMAGE_VERSION

# CLI is not built locally
docker pull $NEXUS_DOCKER_REPO/onap/cli:${CLI_IMAGE_VERSION}

# Tag them as expected by docker-compose file
docker tag $NEXUS_DOCKER_REPO/openecomp/${DB_IMG_NAME}:$DOCKER_IMAGE_VERSION $DB_IMG_NAME:$PORTAL_TAG
docker tag $NEXUS_DOCKER_REPO/openecomp/${EP_IMG_NAME}:$DOCKER_IMAGE_VERSION $EP_IMG_NAME:$PORTAL_TAG
docker tag $NEXUS_DOCKER_REPO/openecomp/${WMS_IMG_NAME}:$DOCKER_IMAGE_VERSION $WMS_IMG_NAME:$PORTAL_TAG

# Tag CLI also
docker tag $NEXUS_DOCKER_REPO/onap/cli:${CLI_IMAGE_VERSION} onap/cli:$PORTAL_TAG

# compose is not in /usr/bin
/opt/docker/docker-compose down
/opt/docker/docker-compose up -d
