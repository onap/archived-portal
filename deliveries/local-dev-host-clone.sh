#!/bin/bash
# Sets up a new build area for Portal projects on on a dev/test host such as vm-ep-dev3

USER=attuid-goes-here
git clone https://${USER}@codecloud.web.att.com/scm/st_quantum/lf_portal.git
cd lf_portal
git clone https://${USER}@codecloud.web.att.com/scm/st_quantum/lf_portal_sdk.git sdk
