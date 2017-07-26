#!/bin/bash

# Establish environment variables
source $(dirname $0)/os_settings.sh

./run.sh

REPO="nexus3.onap.org:10003"

TIMESTAMP=$(date +%C%y%m%dT%H%M%S)
VERSION="1.1.0-SNAPSHOT-${TIMESTAMP}"
LATEST="latest"

APPS_VERSION="${REPO}/openecomp/portalapps:${VERSION}"
DB_VERSION="${REPO}/openecomp/portaldb:${VERSION}"
WMS_VERSION="${REPO}/openecomp/portalwms:${VERSION}"

APPS_LATEST="${REPO}/openecomp/portalapps:${LATEST}"
DB_LATEST="${REPO}/openecomp/portaldb:${LATEST}"
WMS_LATEST="${REPO}/openecomp/portalwms:${LATEST}"

# tag versions
docker tag ${EP_IMG_NAME} ${APPS_VERSION}
docker tag ${EP_IMG_NAME} ${APPS_LATEST}

docker tag ${DB_IMG_NAME} ${DB_VERSION}
docker tag ${DB_IMG_NAME} ${DB_LATEST}

docker tag ${WMS_IMG_NAME} ${WMS_VERSION}
docker tag ${WMS_IMG_NAME} ${WMS_LATEST}

# push
docker push ${APPS_VERSION}
docker push ${APPS_LATEST}

docker push ${DB_VERSION}
docker push ${DB_LATEST}

docker push ${WMS_VERSION}
docker push ${WMS_LATEST}
