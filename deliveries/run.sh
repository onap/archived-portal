#!/bin/bash
# Stop on errors; show output
set -e -x
./os_build_febe.sh
./createMaria.sh
docker images
