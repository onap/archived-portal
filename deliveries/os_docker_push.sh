#!/bin/bash
# Builds and pushes SNAPSHOT versions of Portal images

# be verbose
set -x

TIMESTAMP=$(date +%C%y%m%dT%H%M%S)
export VERSION="1.1.0-SNAPSHOT-${TIMESTAMP}"
export LATEST="latest"

exec ./os_docker_base.sh
