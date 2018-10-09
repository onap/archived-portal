#!/bin/bash
# Sets up a new build area for Portal projects on on a dev/test host such as vm-ep-dev3

USER=org.UserId-goes-here
git clone https://${USER}@gerrit.onap.org/r/a/portal
cd lf_portal
git clone https://${USER}@gerrit.onap.org/r/a/portal/sdk
