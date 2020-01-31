#!/bin/bash

export spring_datasource_username=portal
export spring_datasource_password=Test123456
echo "mvn clean install"
mvn clean install > file-log.log
echo "docker build"
docker build -t portal_app .
echo "docker compose"
docker-compose up
