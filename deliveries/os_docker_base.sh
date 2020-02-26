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

# ONAP docker registry for pushing; has been moved to 
# .env (or other .env override file from command line)
# NEXUS_REPO=nexus3.onap.org:10003

# Establish environment variables
source $(dirname $0)/.env
if [ $1 ]; then
  source $(dirname $0)/$1
fi

# Build the containers
if [ "$SKIP_BUILD_BEFORE_PUSH" = "please" ]; then
    echo "SKIPPING DOCKER IMAGE BUILD!"
else 
    ./build_portalapps_dockers.sh $1
fi

if [ "${PORTAL_DOCKERFILE}" != "skip" ] && [ "${EP_IMG_NAME}" != "skip" ]; then
  APP_VERSION="${NEXUS_REPO}/${EP_IMG_NAME}:${VERSION}"
  APP_LATEST="${NEXUS_REPO}/${EP_IMG_NAME}:${LATEST}"

  docker tag ${EP_IMG_NAME}:${PORTAL_TAG} ${APP_VERSION}
  docker tag ${EP_IMG_NAME}:${PORTAL_TAG} ${APP_LATEST}

  docker push ${APP_VERSION}
  docker push ${APP_LATEST}
fi 

if [ "${SDK_DOCKERFILE}" != "skip" ] && [ "${SDK_IMG_NAME}" != "skip" ]; then
  SDK_VERSION="${NEXUS_REPO}/${SDK_IMG_NAME}:${VERSION}"
  SDK_LATEST="${NEXUS_REPO}/${SDK_IMG_NAME}:${LATEST}"

  docker tag ${SDK_IMG_NAME}:${PORTAL_TAG} ${SDK_VERSION}
  docker tag ${SDK_IMG_NAME}:${PORTAL_TAG} ${SDK_LATEST}

  docker push ${SDK_VERSION}
  docker push ${SDK_LATEST}
fi

if [ "${FE_DOCKERFILE}" != "skip" ] && [ "${FE_IMG_NAME}" != "skip" ]; then
  FE_VERSION="${NEXUS_REPO}/${FE_IMG_NAME}:${VERSION}"
  FE_LATEST="${NEXUS_REPO}/${FE_IMG_NAME}:${LATEST}"

  docker tag ${FE_IMG_NAME}:${PORTAL_TAG} ${FE_VERSION}
  docker tag ${FE_IMG_NAME}:${PORTAL_TAG} ${FE_LATEST}

  docker push ${FE_VERSION}
  docker push ${FE_LATEST}
fi 

if [ "${BE_DOCKERFILE}" != "skip" ] && [ "${BE_IMG_NAME}" != "skip" ]; then
  BE_VERSION="${NEXUS_REPO}/${BE_IMG_NAME}:${VERSION}"
  BE_LATEST="${NEXUS_REPO}/${BE_IMG_NAME}:${LATEST}"

  docker tag ${BE_IMG_NAME}:${PORTAL_TAG} ${BE_VERSION}
  docker tag ${BE_IMG_NAME}:${PORTAL_TAG} ${BE_LATEST}

  docker push ${BE_VERSION}
  docker push ${BE_LATEST}
fi 

if [ "${DB_DOCKERFILE}" != "skip" ] && [ "${DB_IMG_NAME}" != "skip" ]; then
  DB_VERSION="${NEXUS_REPO}/${DB_IMG_NAME}:${VERSION}"
  DB_LATEST="${NEXUS_REPO}/${DB_IMG_NAME}:${LATEST}"

  docker tag ${DB_IMG_NAME}:${PORTAL_TAG} ${DB_VERSION}
  docker tag ${DB_IMG_NAME}:${PORTAL_TAG} ${DB_LATEST}

  docker push ${DB_VERSION}
  docker push ${DB_LATEST}
fi

if [ "${WMS_DOCKERFILE}" != "skip" ] && [ "${WMS_IMG_NAME}" != "skip" ]; then
  WMS_VERSION="${NEXUS_REPO}/${WMS_IMG_NAME}:${VERSION}"
  WMS_LATEST="${NEXUS_REPO}/${WMS_IMG_NAME}:${LATEST}"

  docker tag ${WMS_IMG_NAME}:${PORTAL_TAG} ${WMS_VERSION}
  docker tag ${WMS_IMG_NAME}:${PORTAL_TAG} ${WMS_LATEST}

  docker push ${WMS_VERSION}
  docker push ${WMS_LATEST}
fi

