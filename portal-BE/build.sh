#!/bin/bash

mvn clean install
docker build -t portal_app .
docker-compose up
