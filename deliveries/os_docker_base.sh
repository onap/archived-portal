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

# Establish environment variables
source $(dirname $0)/.env

# Build the containers
./build_portalapps_dockers.sh

APPS_VERSION="${NEXUS_REPO}/openecomp/${EP_IMG_NAME}:${VERSION}"
DB_VERSION="${NEXUS_REPO}/openecomp/${DB_IMG_NAME}:${VERSION}"
WMS_VERSION="${NEXUS_REPO}/openecomp/${WMS_IMG_NAME}:${VERSION}"

APPS_LATEST="${NEXUS_REPO}/openecomp/${EP_IMG_NAME}:${LATEST}"
DB_LATEST="${NEXUS_REPO}/openecomp/${DB_IMG_NAME}:${LATEST}"
WMS_LATEST="${NEXUS_REPO}/openecomp/${WMS_IMG_NAME}:${LATEST}"

# tag versions
docker tag ${EP_IMG_NAME}:${PORTAL_TAG} ${APPS_VERSION}
docker tag ${EP_IMG_NAME}:${PORTAL_TAG} ${APPS_LATEST}

docker tag ${DB_IMG_NAME}:${PORTAL_TAG} ${DB_VERSION}
docker tag ${DB_IMG_NAME}:${PORTAL_TAG} ${DB_LATEST}

docker tag ${WMS_IMG_NAME}:${PORTAL_TAG} ${WMS_VERSION}
docker tag ${WMS_IMG_NAME}:${PORTAL_TAG} ${WMS_LATEST}

# push
docker push ${APPS_VERSION}
docker push ${APPS_LATEST}

docker push ${DB_VERSION}
docker push ${DB_LATEST}

docker push ${WMS_VERSION}
docker push ${WMS_LATEST}
