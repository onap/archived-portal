#!/bin/bash

source $(dirname $0)/os_settings.sh

export MVN="${MVN} -gs ${GLOBAL_SETTINGS_FILE} -s ${SETTINGS_FILE}"
#export MVN="mvn"

CURRENTDIR="$(pwd)"


# install ecomp portal
rm -rf $CURRENTDIR/$WORKINGDIR
mkdir $CURRENTDIR/$WORKINGDIR 
cd $CURRENTDIR/$WORKINGDIR
SOURCEDIR=$CURRENTDIR/$WORKINGDIR/Source
mkdir $SOURCEDIR
cd $SOURCEDIR
PROJECTDIR=$SOURCEDIR/ecomp-portal-core 
mkdir $PROJECTDIR

#create project dir
#copy FE and BE
cd $CURRENTDIR
cd ..

cp -r ecomp-portal-FE-common $PROJECTDIR/ecomp-portal-FE-common
cp -r ecomp-portal-FE-os $PROJECTDIR/ecomp-portal-FE-os
cp -r ecomp-portal-BE-common $PROJECTDIR/ecomp-portal-BE-common
cp -r ecomp-portal-BE-os $PROJECTDIR/ecomp-portal-BE-os
cp -r ecomp-portal-DB-common $PROJECTDIR/ecomp-portal-DB-common
cp -r ecomp-portal-DB-os $PROJECTDIR/ecomp-portal-DB-os

cp -r ecompsdkos/ecomp-sdk $PROJECTDIR/ecomp-sdk

#!/bin/bash
shopt -s expand_aliases
source ~/.bashrc

cd $PROJECTDIR/ecomp-portal-FE-os/

${MVN} clean install

cd $PROJECTDIR/ecomp-portal-BE-common

${MVN} install


cd $PROJECTDIR/ecomp-portal-BE-os

${MVN} install


# now install sdk app
cd $PROJECTDIR/ecomp-sdk/epsdk-app-os

${MVN} install

mv target/epsdk-app-os-1.1.0-SNAPSHOT target/ep-sdk-app

# now install DBC app
cd $SOURCEDIR
DBCDIR=$SOURCEDIR/ST_DBPA 
mkdir $DBCDIR

#copy DBC project
cd $CURRENTDIR
cd ..

cp -r dmaapbc/dcae_dmaapbc_webapp $DBCDIR/dcae_dmaapbc_webapp

cd $DBCDIR/dcae_dmaapbc_webapp

${MVN} install

cd dbca-os/target

mv  dmaap-bc-app-os-1.1.0-SNAPSHOT ep-dbc-app

# Build complete database script in the "OS" script area
cd ../db-scripts
cat dbca-create-mysql-1707-os.sql ../../dbca-common/db-scripts/dbca-ddl-mysql-1707-common.sql dbca-dml-mysql-1707-os.sql > dbca-complete-mysql-1707-os.sql


# install into docker
cd $CURRENTDIR

PROXY_ARGS=""
if [ $HTTP_PROXY ]; then
    PROXY_ARGS+="--build-arg HTTP_PROXY=${HTTP_PROXY}"
fi
if [ $HTTPS_PROXY ]; then
    PROXY_ARGS+=" --build-arg HTTPS_PROXY=${HTTPS_PROXY}"
fi

EXEC_CMD="docker build -t ${IMGNAME} ${PROXY_ARGS} --build-arg VERSION=${VERSION} --build-arg PORTAL_SDK_DIR=${PORTAL_SDK_DIR}  --build-arg SDK_DIR=${SDK_DIR} --build-arg FE_DIR=${FE_DIR}  --build-arg PORTAL_DBC_DIR=${PORTAL_DBC_DIR} -f ./os_Dockerfile ."
echo $EXEC_CMD
$EXEC_CMD