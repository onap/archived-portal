BASEDIR=/PROJECT/OpenSource/UbuntuEP
WIDGETMSAPPPROPDIR=ECOMPWIDGETMS
echo "Starting ecomp-portal-widget-ms image in a new container !!!"
docker run -d --name "ecomp-portal-widget-ms" -p 8082:8082 -v ${BASEDIR}/etc/${WIDGETMSAPPPROPDIR}/application.properties:/application.properties widget-ms

