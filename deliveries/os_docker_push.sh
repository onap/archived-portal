#!/bin/bash

#docker login
docker tag ep:1610-1 portalapps:1.0.0
docker tag ecompdb:portal portaldb:1.0.0
docker push portalapps:1.0.0
docker push portaldb:1.0.0