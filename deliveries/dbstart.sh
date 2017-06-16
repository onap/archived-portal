#docker create --name data_vol_portal -v /var/lib/mysql mariadb;
docker run -d --volumes-from data_vol_portal -p 3306:3306 -e MYSQL_ROOT_PASSWORD=Aa123456 --net=host --name ecompdb_portal ecompdb:portal;

