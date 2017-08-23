#!/bin/bash
# Builds and pushes STAGING versions of Portal images

# be verbose
set -x

TIMESTAMP=$(date +%C%y%m%dT%H%M%S)
export VERSION="1.1.0-STAGING-${TIMESTAMP}"
export LATEST="1.1-STAGING-latest"

exec ./os_docker_base.sh
