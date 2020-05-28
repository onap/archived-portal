#!/bin/bash
# Sets up a new build area for Portal projects on on a dev/test host such as vm-ep-dev3

USER=org.UserId-goes-here
git clone --depth 1 https://${USER}@gerrit.onap.org/r/a/portal
cd lf_portal
git clone --depth 1 https://${USER}@gerrit.onap.org/r/a/portal/sdk
