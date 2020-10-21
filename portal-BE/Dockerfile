FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8081
ARG DEPENDENCY=target
ADD ${DEPENDENCY}/portal-0.0.1-SNAPSHOT.jar portal-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","portal-0.0.1-SNAPSHOT.jar"]
