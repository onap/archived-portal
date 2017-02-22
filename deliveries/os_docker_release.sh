#!/bin/bash

./run.sh

REPO="nexus3.openecomp.org:10002"

VERSION="1.0.0"
LATEST="latest"

APPS_VERSION="${REPO}/openecomp/portalapps:${VERSION}"
DB_VERSION="${REPO}/openecomp/portaldb:${VERSION}"

APPS_LATEST="${REPO}/openecomp/portalapps:${LATEST}"
DB_LATEST="${REPO}/openecomp/portaldb:${LATEST}"

# tag version
docker tag ep:1610-1 ${APPS_VERSION}
docker tag ecompdb:portal ${DB_VERSION}
docker tag ep:1610-1 ${APPS_LATEST}
docker tag ecompdb:portal ${DB_LATEST}

# push
docker push ${APPS_VERSION}
docker push ${DB_VERSION}
docker push ${APPS_LATEST}
docker push ${DB_LATEST}