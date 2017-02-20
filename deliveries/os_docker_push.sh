#!/bin/bash

./run.sh

REPO="nexus3.openecomp.org:10003"

APPS="${REPO}/openecomp/portalapps:1.0.0"
DB="${REPO}/openecomp/portaldb:1.0.0"

# docker login nexus3.openecomp.org:10003

# tag
docker tag ep:1610-1 ${APPS}
docker tag ecompdb:portal ${DB}

# push
docker push ${APPS}
docker push ${DB}