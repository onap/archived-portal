#!/bin/bash

# Establish environment variables
source $(dirname $0)/os_settings.sh

echo "Stopping docker container named ${EP_CONT_NAME}"
docker stop ${EP_CONT_NAME}
echo "Removing docker container named ${EP_CONT_NAME}"
docker  rm ${EP_CONT_NAME}
