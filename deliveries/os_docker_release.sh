#!/bin/bash

# Establish environment variables
source $(dirname $0)/os_settings.sh

./run.sh

REPO="nexus3.onap.org:10003"

TIMESTAMP=$(date +%C%y%m%dT%H%M%S)
VERSION="1.1.0-STAGING-${TIMESTAMP}"
LATEST="1.1-STAGING-latest"

APPS_VERSION="${REPO}/openecomp/${EP_TAG_NAME}:${VERSION}"
DB_VERSION="${REPO}/openecomp/${DB_TAG_NAME}:${VERSION}"
WMS_VERSION="${REPO}/openecomp/${WMS_TAG_NAME}:${VERSION}"

APPS_LATEST="${REPO}/openecomp/${EP_TAG_NAME}:${LATEST}"
DB_LATEST="${REPO}/openecomp/${DB_TAG_NAME}:${LATEST}"
WMS_LATEST="${REPO}/openecomp/${WMS_TAG_NAME}:${LATEST}"

# tag version
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
