# Dockerfile for image with ONAP Portal
ARG BE_BASE_IMAGE=tomcat:8.5.35-jre8-alpine
FROM ${BE_BASE_IMAGE}

RUN apk add sudo && echo "portal ALL=(ALL) NOPASSWD:ALL" >> /etc/sudoers
# Arguments are supplied by build.sh script
# the defaults below only support testing
ARG PORTAL_WAR=build/ecompportal-be-os.war
ARG HTTP_PROXY
ARG HTTPS_PROXY
# ARG PORTAL_CERT=truststoreONAPall.jks

# Just variables, never passed in
ARG PORTALCONTEXT=ONAPPORTAL

ARG TOMCAT=/usr/local/tomcat
ARG TOMCATHOME=${TOMCAT}
ARG SERVERXML=${SERVERXML}

ENV http_proxy $HTTP_PROXY
ENV https_proxy $HTTPS_PROXY

RUN if [ -d /etc/apt ] && [ ! -z ${HTTP_PROXY} ]; then echo "Acquire::http::proxy  \"${HTTP_PROXY}\";" >> /etc/apt/apt.conf; fi && \
    if [ -d /etc/apt ] && [ ! -z ${HTTPS_PROXY} ]; then echo "Acquire::https::proxy \"${HTTPS_PROXY}\";" >> /etc/apt/apt.conf; fi

# Remove manager and sample apps
RUN rm -rf ${TOMCAT}/webapps/[a-z]*
RUN mkdir -p /opt
COPY ${SERVERXML} ${TOMCAT}/conf
# TODO: ????
#RUN mv ${TOMCAT} /opt

WORKDIR ${TOMCATHOME}/webapps
RUN mkdir ${PORTALCONTEXT}

# Portal has many parts
COPY $PORTAL_WAR ${PORTALCONTEXT}
RUN cd ${PORTALCONTEXT} && unzip -q *.war && rm *.war

VOLUME ${TOMCATHOME}/logs

RUN addgroup -g 1000 -S portal && adduser -u 1000 -S portal -G portal && chown -R portal:portal . && chmod -R 777 /etc/ssl/certs/java /var/

# Switch back to root
WORKDIR /

# Define commonly used ENV variables
ENV PATH $PATH:$JAVA_HOME/bin:${TOMCATHOME}/bin
# Install the launch script
COPY start-apache-tomcat.sh /

# Define default command
ENV TOMCATHOME=$TOMCATHOME
CMD /start-apache-tomcat.sh -b $TOMCATHOME
