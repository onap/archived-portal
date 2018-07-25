#!/bin/sh
# Starts the Apache-Tomcat web container.
# If arguments "-i ip.2.3.4" AND "-n name" are present, adds an entry to /etc/hosts;
# this was added as a workaround for missing DNS in the CSIT environment.

hostip=""
hostname=""
while [ $# -gt 0 ]; do
    key="$1"
    case $key in
        -i|--ip)
        hostip="$2"
        echo "$0: option -i value is $hostip"
        shift # past argument
        shift # past value
        ;;
        -n|--name)
        hostname="$2"
        echo "$0: option -n value is $hostname"
        shift # past argument
        shift # past value
        ;;
        *)
        echo "$0: ignoring argument $key"
	shift
        ;;
    esac
done

# Optionally add to /etc/hosts
# Docker-compose supplies arguments ""
if [ ${#hostip} -lt 3 -o ${#hostname} -lt 3 ]; then
    echo "$0: values for IP (-i) and/or name (-n) are empty or short"
else
    echo "$0: using IP-name arguments $hostip $hostname"
    grep $hostname /etc/hosts
    ret_code=$?
    if [ $ret_code != 0 ]; then
        echo "$0: extending hosts with $hostname"
        echo "$hostip $hostname" >> /etc/hosts
    else
        echo "$0: hosts already has $hostname"
    fi
fi

BASE=/opt/apache-tomcat-8.0.37
if [ ! -d $BASE ] ; then
    echo "$0: $BASE not found or not a directory"
    exit 1
fi
echo "$0: Starting server from $BASE"
LOGFILE=${BASE}/logs/catalina.out
echo "`date`:<--------------------    Starting     -------------------->" >> $LOGFILE
exec ${BASE}/bin/catalina.sh run  2>&1 | tee -a $LOGFILE