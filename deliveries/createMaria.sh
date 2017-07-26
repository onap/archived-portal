#!/bin/bash
# Stop on errors; show output
set -e -x

source $(dirname $0)/os_settings.sh

echo ${SCRIPT_DIR}

docker build -t ${DB_IMG_NAME} --build-arg SCRIPT_DIR="${SCRIPT_DIR}" --build-arg SDK_SCRIPT_DIR="${SDK_SCRIPT_DIR}" --build-arg DBC_SCRIPT_DIR="${DBC_SCRIPT_DIR}" --build-arg SCRIPT_COMMON_DIR="${SCRIPT_COMMON_DIR}" --build-arg SDK_COMMON_SCRIPT_DIR="${SDK_COMMON_SCRIPT_DIR}" --build-arg DBC_COMMON_SCRIPT_DIR="${DBC_COMMON_SCRIPT_DIR}" -f ./Dockerfile.mariadb .
