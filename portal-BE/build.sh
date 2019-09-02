#!/bin/bash

mvn clean install
docker build -t portal_mariadb -f dockerfile.mariadb .
docker build -t portal_app .
docker-compose up
