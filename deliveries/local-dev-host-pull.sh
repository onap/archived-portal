#!/bin/bash
# Refreshes an existing build area for Portal projects on on a dev/test host such as vm-ep-dev3

set -x
git pull 
cd sdk
git pull
