#!/bin/bash

# Stop on errors; show output
set -e -x
# 1610 builder
# ./os_build_febe.sh
# 1707 builder
./build_portalapps_dockers.sh
./createMaria.sh
docker images
