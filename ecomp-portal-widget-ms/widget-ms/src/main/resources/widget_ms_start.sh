APP_PROPERTY_LOCATION=/tmp/application.properties
echo "Starting ecomp-portal-widget-ms image in a new container !!!"
docker run -d --name "ecomp-portal-widget-ms" -p 8083:8082 -v $APP_PROPERTY_LOCATION:/application.properties ecompportal-widget-ms

