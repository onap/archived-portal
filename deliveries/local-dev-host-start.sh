#!/bin/bash
# Starts docker containers for ONAP Portal in test environment
# For development use only; this does NOT pull from git nor build.

# be verbose
set -x

# Get variables from docker-compose environment file
source .env

# Define local subdirectory with host-specific property files
# The leading "./" is required for docker-compose
export PROPS_DIR=./properties_vm-ep-dev11
if [ ! -d $PROPS_DIR ] ; then
    echo "Failed to find directory $PROPS_DIR"
    exit 1
fi
echo "Using properties directory $PROPS_DIR"

# Constants as of Oct 2017, Amsterdam release
NEXUS_REPO=nexus3.onap.org:10001
CLI_IMG_VERSION=1.1-STAGING-latest
CDR_IMG_VERSION=latest
ZK_IMG_VERSION=3.4

# Pull and tag the CLI image, which is provided elsewhere.
# Authenticate like this; the username and password are NOT stored here.
docker login -u username -p password $NEXUS_REPO 
docker pull $NEXUS_REPO/$CLI_IMG_NAME:${CLI_IMG_VERSION}
docker tag $NEXUS_REPO/$CLI_IMG_NAME:${CLI_IMG_VERSION} $CLI_IMG_NAME:$PORTAL_TAG

# Pull and tag the Music Cassandra image.
docker pull $NEXUS_REPO/$CDR_IMG_NAME:${CDR_IMG_VERSION}
docker tag $NEXUS_REPO/$CDR_IMG_NAME:${CDR_IMG_VERSION} $CDR_IMG_NAME:$PORTAL_TAG

# Pull and tag the ZK image.
docker pull library/$ZK_IMG_NAME:${ZK_IMG_VERSION}
docker tag library/$ZK_IMG_NAME:${ZK_IMG_VERSION} $ZK_IMG_NAME:$PORTAL_TAG

# Create local logs directory
# The leading "./" is required for docker-compose
export LOGS_DIR=./logs
mkdir -p $LOGS_DIR

# Make inter-app communication work in dev3
export EXTRA_HOST_IP=localhost
export EXTRA_HOST_NAME="portal.api.simpledemo.onap.org"

# (re)start containers
docker-compose down
docker-compose up -d 