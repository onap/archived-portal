#!/bin/bash

# Establish environment variables
source $(dirname $0)/os_settings.sh

BASEDIR=/PROJECT/OpenSource/UbuntuEP
WIDGETMSAPPPROPDIR=ECOMPWIDGETMS

echo "Running docker image ${WMS_IMG_NAME} as container ${WMS_CONT_NAME}"
docker run -d --name ${WMS_CONT_NAME} -p 8082:8082 -v ${BASEDIR}/etc/${WIDGETMSAPPPROPDIR}/application.properties:/application.properties ${WMS_IMG_NAME}
