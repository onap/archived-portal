#!/bin/bash

# Establish environment variables
source $(dirname $0)/os_settings.sh

#docker create --name ${DB_VOL_NAME} -v /var/lib/mysql mariadb;

echo "Running docker image ${DB_IMG_NAME} as name ${DB_CONT_NAME} with volume ${DB_VOL_NAME}"
docker run -d --volumes-from ${DB_VOL_NAME} -p 3306:3306 -e MYSQL_ROOT_PASSWORD=Aa123456 --net=host --name ${DB_CONT_NAME} ${DB_IMG_NAME};
