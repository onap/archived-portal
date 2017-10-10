#!/bin/sh
# Starts the Apache-Tomcat web container with the Portal, EPSDK and DMaaP BC web apps.
# If arguments "-i ip.2.3.4" AND "-n name" are present, adds an entry to /etc/hosts;
# this was added as a workaround for missing DNS in the CSIT environment.

hostip=""
hostname=""
while [ $# -gt 0 ]; do
    key="$1"
    case $key in
        -i|--ip)
        hostip="$2"
        shift # past argument
        shift # past value
        ;;
        -n|--name)
        hostname="$2"
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
if [ -z "${hostip}" -o -z "${hostname}" ]; then
    echo "$0: Arguments for IP and name not found, continuing."
else
    echo "$0: Using IP-name arguments $hostip $hostname"
    grep $hostname /etc/hosts
    ret_code=$?
    if [ $ret_code != 0 ]; then
        echo "$hostip $hostname" >> /etc/hosts
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
