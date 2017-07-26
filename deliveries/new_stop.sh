#!/bin/sh

# Establish environment variables
source $(dirname $0)/os_settings.sh

echo "Stopping docker container named ${CONTNAME}"
docker stop ${CONTNAME}
echo "Removing docker container named ${CONTNAME}"
docker  rm ${CONTNAME}
