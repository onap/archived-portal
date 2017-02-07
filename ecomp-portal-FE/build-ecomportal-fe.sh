#!/bin/sh

# Script name : build_ecompportal_fe.sh
# Script purpose : To have an easy way to build the front-end part of the eComp portal
# Pre requisites :
# 1. Your home directory must reside at /home
# 2. Your server must have a 'Node' installation
#----------------------------------------------------------------------------------------


################################################
### Functions
################################################
function log_message() {
msgType=$1
message=$2

if [ ${msgType} == "I" ]; then
printf "\033[32m %s \n\033[0m" "INF - ${message}"
elif [ ${msgType} == "E" ]; then
printf "\033[31m %s \n\033[0m" "ERR - ${message}"
else
echo "${msgType} - ${message}";
fi
}



function exit_with_error() {

log_message "E" ""
log_message "E" "$1"
log_message "E" ""

exit 1
}



################################################
### Hard coded information.
################################################
NVM_DIR="/home/${USER}/.nvm"
NODE_VERSION=v0.12.4
SCRIPT_USAGE="USAGE: $0 [ dev | ci | integ | qa ]"



################################################
### Verify arguments
################################################
log_message "I" "Checking command line arguments."
if [ $# == 1 ]; then
if [ $1 == "ci" -o $1 == "integ" -o $1 == "dev" -o $1 == "qa" ]; then
BUILD_BY_ENV=$1
else
exit_with_error "The environment '$1' is invalid."
fi
else
log_message "E" ""
log_message "E" "$SCRIPT_USAGE"
log_message "E" ""
exit 1
fi
log_message "I" "OK."
log_message "I" ""



################################################
### Set the node environment.
################################################
log_message "I" "Set the node environment."
if [ -s "$NVM_DIR/nvm.sh" ]; then
# This loads nvm
. "$NVM_DIR/nvm.sh"
if [ $? != 0 ]; then
exit_with_error "Cannot load the NODE env."
fi
else
exit_with_error "The nvm.sh script does not exist."
fi
log_message "I" "OK."
log_message "I" ""



################################################
### Set the node version manager version.
################################################
log_message "I" "Set the node version manager version."
nvm use v0.12.4
TOOLS_ROOT_FOLDER=${NVM_DIR}/versions/node/${NODE_VERSION}/bin
log_message "I" "OK."
log_message "I" ""



################################################
### Set the proxy servers.
################################################
log_message "I" "Set the proxy servers."
log_message "I" "OK."
log_message "I" ""



################################################
### Install bower, if neeeded.
################################################
log_message "I" "Install bower, if neeeded."
if [ ! -e ${TOOLS_ROOT_FOLDER}/bower ]; then
npm install -g bower
if [ $? != 0 ]; then
exit_with_error "Cannot install bower."
fi
fi
log_message "I" "OK."
log_message "I" ""



################################################
### Install grunt, if neeeded.
################################################
log_message "I" "Install grunt, if neeeded."
if [ ! -e ${TOOLS_ROOT_FOLDER}/grunt ]; then
npm config set ca ""
npm install -g grunt-cli

if [ $? != 0 ]; then
exit_with_error "Cannot install grunt."
fi
fi
log_message "I" "OK."
log_message "I" ""



################################################
### Run the Node package manager (NPM).
################################################
log_message "I" "Run the Node package manager (npm install)."
npm install
if [ $? != 0 ]; then
exit_with_error "Cannot run 'npm install'."
fi
log_message "I" "OK."
log_message "I" ""



################################################
### Install the Bower components.
################################################
log_message "I" "Install the Bower components."
bower install
if [ $? != 0 ]; then
exit_with_error "Cannot run 'npm install'."
fi
log_message "I" "OK."
log_message "I" ""



################################################
### Build the application.
################################################
log_message "I" "Build the application."
grunt build --env=${BUILD_BY_ENV}
if [ $? != 0 ]; then
exit_with_error "Cannot run 'grunt build --env=${BUILD_BY_ENV}'."
fi
log_message "I" "OK."
log_message "I" ""



log_message "I" ""
log_message "I" "Done."
log_message "I" ""