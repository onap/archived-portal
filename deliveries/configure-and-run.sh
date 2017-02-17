#!/bin/bash

LOGFILE=/opt/apache-tomcat-8.0.37/logs/catalina.out
echo "`date`:<--------------------    Starting     -------------------->" >> $LOGFILE
exec /opt/apache-tomcat-8.0.37/bin/catalina.sh run  2>&1 | tee -a $LOGFILE