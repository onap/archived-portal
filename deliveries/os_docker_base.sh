#!/bin/bash
# Builds and pushes versions of Portal images
# Invoked by scripts that set VERSION and LATEST

# be verbose
set -x 

if [ -z "$VERSION" ]; then
    echo "VERSION not set"
    exit 1
fi
if [ -z "$LATEST" ]; then
    echo "LATEST not set"
    exit 1
fi

# ONAP docker registry for pushing; may need to move
# this into os_docker_push, os_docker_release scripts
NEXUS_REPO=nexus3.onap.org:10003

# Build the containers
./build_portalapps_dockers.sh

# Establish environment variables
source $(dirname $0)/.env

APPS_VERSION="${NEXUS_REPO}/${EP_IMG_NAME}:${VERSION}"
DB_VERSION="${NEXUS_REPO}/${DB_IMG_NAME}:${VERSION}"
WMS_VERSION="${NEXUS_REPO}/${WMS_IMG_NAME}:${VERSION}"

APPS_LATEST="${NEXUS_REPO}/${EP_IMG_NAME}:${LATEST}"
DB_LATEST="${NEXUS_REPO}/${DB_IMG_NAME}:${LATEST}"
WMS_LATEST="${NEXUS_REPO}/${WMS_IMG_NAME}:${LATEST}"

# tag versions
docker tag ${EP_IMG_NAME}:${PORTAL_TAG} ${APPS_VERSION}
docker tag ${EP_IMG_NAME}:${PORTAL_TAG} ${APPS_LATEST}

docker tag ${DB_IMG_NAME}:${PORTAL_TAG} ${DB_VERSION}
docker tag ${DB_IMG_NAME}:${PORTAL_TAG} ${DB_LATEST}

docker tag ${WMS_IMG_NAME}:${PORTAL_TAG} ${WMS_VERSION}
docker tag ${WMS_IMG_NAME}:${PORTAL_TAG} ${WMS_LATEST}

# push to registry
docker push ${APPS_VERSION}
docker push ${APPS_LATEST}

docker push ${DB_VERSION}
docker push ${DB_LATEST}

docker push ${WMS_VERSION}
docker push ${WMS_LATEST}
