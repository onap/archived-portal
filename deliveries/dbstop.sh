#!/bin/bash

# Establish environment variables
source $(dirname $0)/os_settings.sh

echo "Stopping docker container named ${DB_CONT_NAME}"
docker stop ${DB_CONT_NAME}
echo "Removing docker container named ${DB_CONT_NAME}"
docker rm ${DB_CONT_NAME}
