#!/bin/bash
source $(dirname $0)/os_settings.sh

echo ${SCRIPT_DIR}


docker build -t ecompdb:portal --build-arg SCRIPT_DIR="${SCRIPT_DIR}" --build-arg SDK_SCRIPT_DIR="${SDK_SCRIPT_DIR}" --build-arg DBC_SCRIPT_DIR="${DBC_SCRIPT_DIR}" -f ./Dockerfile.mariadb .
