#!/bin/bash

export spring_datasource_username=portal
export spring_datasource_password=Test123456

mvn clean install
docker build -t portal_app .
docker-compose up