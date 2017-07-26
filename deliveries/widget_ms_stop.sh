#!/bin/bash

# Establish environment variables
source $(dirname $0)/os_settings.sh

echo "Stopping docker container ${WMS_CONT_NAME}"
docker stop ${WMS_CONT_NAME}
echo "Removing docker image ${WMS_CONT_NAME}"
docker rm ${WMS_CONT_NAME}
