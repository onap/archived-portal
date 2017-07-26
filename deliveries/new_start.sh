#!/bin/bash

# Establish environment variables
source $(dirname $0)/os_settings.sh

BASEDIR=/PROJECT/OpenSource/UbuntuEP
PORTALDIR=/opt/apache-tomcat-8.0.37/webapps/ECOMPPORTAL
SDKAPPDIR=/opt/apache-tomcat-8.0.37/webapps/ECOMPSDKAPP
DBCAPPDIR=/opt/apache-tomcat-8.0.37/webapps/ECOMPDBCAPP
PORTALPROPDIR=ECOMPPORTALAPP
SDKAPPPROPDIR=ECOMPSDKAPP
DBCAPPPROPDIR=ECOMPDBCAPP
#docker rm ep_1610
echo "Running docker image ${EP_IMG_NAME} as name ${EP_CONT_NAME}"
docker run -d --name ${EP_CONT_NAME}  \
-p 8989:8080 -p 8010:8009 -p 8006:8005 \
-v ${BASEDIR}/etc/${PORTALPROPDIR}/system.properties:${PORTALDIR}/WEB-INF/conf/system.properties \
-v ${BASEDIR}/etc/${PORTALPROPDIR}/fusion.properties:${PORTALDIR}/WEB-INF/fusion/conf/fusion.properties \
-v ${BASEDIR}/etc/${PORTALPROPDIR}/portal.properties:${PORTALDIR}/WEB-INF/classes/portal.properties \
-v ${BASEDIR}/etc/${PORTALPROPDIR}/openid-connect.properties:${PORTALDIR}/WEB-INF/classes/openid-connect.properties \
-v ${BASEDIR}/etc/${SDKAPPPROPDIR}/fusion.properties:${SDKAPPDIR}/WEB-INF/fusion/conf/fusion.properties \
-v ${BASEDIR}/etc/${SDKAPPPROPDIR}/system.properties:${SDKAPPDIR}/WEB-INF/conf/system.properties \
-v ${BASEDIR}/etc/${SDKAPPPROPDIR}/portal.properties:${SDKAPPDIR}/WEB-INF/classes/portal.properties \
-v ${BASEDIR}/etc/${DBCAPPPROPDIR}/system.properties:${DBCAPPDIR}/WEB-INF/conf/system.properties \
-v ${BASEDIR}/etc/${DBCAPPPROPDIR}/portal.properties:${DBCAPPDIR}/WEB-INF/classes/portal.properties \
-v ${BASEDIR}/etc/${DBCAPPPROPDIR}/dbcapp.properties:${DBCAPPDIR}/WEB-INF/dbcapp/dbcapp.properties \
-v ${BASEDIR}/etc/${DBCAPPPROPDIR}/fusion.properties:${DBCAPPDIR}/WEB-INF/fusion/conf/fusion.properties \
-v ${BASEDIR}/log:/opt/apache-tomcat-8.0.37/logs  \
${EP_IMG_NAME}
