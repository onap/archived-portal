#!/bin/bash

# Establish environment variables
source $(dirname $0)/os_settings.sh

#docker create --name data_vol_portal -v /var/lib/mysql mariadb;

echo "Running docker image ${DB_IMG_NAME} as name ${DB_CONT_NAME}"
docker run -d --volumes-from data_vol_portal -p 3306:3306 -e MYSQL_ROOT_PASSWORD=Aa123456 --net=host --name ${DB_CONT_NAME} ${DB_IMG_NAME};
