#!/bin/bash
# Builds and pushes SNAPSHOT versions of Portal images

# be verbose
set -x

# Establish environment variables
source $(dirname $0)/.env

if [ -z "$PORTAL_VERSION" ]; then
    echo "PORTAL_VERSION not set"
    exit 1
fi

TIMESTAMP=$(date +%C%y%m%dT%H%M%S)
export VERSION="${PORTAL_VERSION}-${TIMESTAMP}"
export LATEST="latest"

exec ./os_docker_base.sh
