#!/bin/bash

source $(dirname $0)/os_settings.sh

export MVN="${MVN} -gs ${GLOBAL_SETTINGS_FILE} -s ${SETTINGS_FILE}"

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

cp -r ecomp-portal-FE $PROJECTDIR/ecomp-portal-FE
cp -r ecomp-portal-BE $PROJECTDIR/ecomp-portal-BE
cp -r ecompsdkos/ecomp-sdk $PROJECTDIR/ecomp-sdk

#!/bin/bash
shopt -s expand_aliases
source ~/.bashrc

cd $PROJECTDIR/ecomp-portal-FE/

${MVN} install

cd $PROJECTDIR/ecomp-portal-BE

${MVN} install

# now install sdk app
cd $PROJECTDIR/ecomp-sdk/sdk-app

${MVN} install

mv target/ep-sdk-app-1.1.0-SNAPSHOT target/ep-sdk-app

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

cd target
mv dmaap-bc-app.1.1.0-SNAPSHOT.0 ep-dbc-app


# install into docker
cd $CURRENTDIR

echo "running: docker build -t ${IMGNAME}  --build-arg VERSION=${VERSION} --build-arg PORTAL_SDK_DIR=${PORTAL_SDK_DIR}  --build-arg SDK_DIR=${SDK_DIR} --build-arg FE_DIR=${FE_DIR}  --build-arg PORTAL_DBC_DIR=${PORTAL_DBC_DIR} -f ./os_Dockerfile ."
docker build -t ${IMGNAME}  --build-arg VERSION=${VERSION} --build-arg PORTAL_SDK_DIR=${PORTAL_SDK_DIR}  --build-arg SDK_DIR=${SDK_DIR} --build-arg FE_DIR=${FE_DIR}  --build-arg PORTAL_DBC_DIR=${PORTAL_DBC_DIR} -f ./os_Dockerfile .
