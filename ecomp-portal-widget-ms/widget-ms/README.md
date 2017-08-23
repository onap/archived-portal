# Portal Widget Microservice

For security, the server listens only on localhost (127.0.0.1) and requires HTTP Basic 
Authentication. No outside network traffic is accepted or processed (packets never leave 
the host). Currently, the server uses a self signed certificate - details below.

## Build Prerequisites

1. Java version 1.8
2. Maven version 3
3. Connectivity to AT&T Maven Central

## Run Prerequisites

1. Java version 1.8
2. A Mysql database using the same database as the Portal

## Build and Package

Use maven to build and package the microservice into a jar using this command:

	mvn clean package

## Configuring

All configuration parameters are entered in a file called application.properties.  A version with default values can be found in the top level of this project. 

Details about the database are configured in this file. The default entries for the database configuration are shown here:

	spring.datasource.url=jdbc:mariadb:failover://{db_host:db_port/{portal_db}
	spring.datasource.username={username}
	spring.datasource.password=ENC({encrypted_password})

The HTTP server's username and password are configured in this file.  Only one username/password is used to secure the REST endpoint. The default entries for the server are shown here:

	security.user.name={basic_auth_username}
	security.user.password=ENC({encrypted_basic_auth_password})

When you package the application into a jar file and launch the microservice using that jar, the configuration file must be in the current working directory where you launch the service.

## Generating Encrypted Passwords

Use the following command to generate an encrypted password for the database and the
micro service.  The entire command must be entered on one line; the command shown 
below uses backslashes to break lines for readability:

	java -cp ~/.m2/repository/org/jasypt/jasypt/1.9.2/jasypt-1.9.2.jar \
		org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI \
		algorithm=PBEWithMD5AndDES \
		input='YourPasswordHere' \
		password='EncryptionKey'

Note, 'YourPasswordHere' is the actual database password, as supplied in the 'password' 
parameter.  The value 'EncryptionKey' is used to encrypt the input, and must be supplied
at run time in one of two ways:

* On the command line with a JVM argument "-Djasypt.encryptor.password".  Here's an example
of using maven with the Spring-Boot goal:

	mvn -Djasypt.encryptor.password=EncryptionKey spring-boot:run
	
Here's an example of using java and the jar file:

	java -jar dbc-microservice.jar -Djasypt.encryptor.password=EncryptionKey

* In the application.properties file using the key jasypt.encryptor.password.  For example:

	jasypt.encryptor.password=EncryptionKey

## Development Launch

Check the configuration properties in file src/main/resources/application.properties.  Then launch the microservice for development and testing like this:

	mvn clean spring-boot:run

## Production Launch

Ensure a valid configuration is present in file application.properties in the current working directory.  Then launch the microservice for production like this:

	widget-service.sh start

## Release Notes

### Release 1702, February 2017

#### Consul Release Notes 

Build 1702.3.48, 5 Feb 2017
- DE264319 - Corrected spelling for output file from 'consule.out' to 'consul.out' - Please do not miss re-deploying consul build. Widget-ms build is not enough (or required) for this bug fix to be validated.

#### Widget-ms Release Notes

Build 1.2.175
- Def 120258 Fixed the widget displaying issues in Internet Explorer 11

Build 1.2.145
- Def 143148 - fixed the defect, common widgets work on Firefox version 45
- Def 142432 - Widget-ms: InputStream is not getting closed in file StorageServiceImpl.java 

Build 1.2.140 
- St 164888 allow partner applications to trigger a new app tab through portal
- St 164847 CCD Widgets
- Def 141951 Fixed the issue for inappropriate location of framework-template.js file
- Def 141352 Widget Onboarding Page, download icon is not clickable in FF 45 version,  Revise widget upload service temporary storage to /tmp
- St 164902 One copy of framework.js hidden in the Portal instead of being part of the widgets
- St 164905 Download the widget 
- St 164711 Widget Personalization: Allow user to define his/her own parameters on widgets
- St 164863 Widgets to use service onboarding feature

Build 1702.3.86 
- MariaDB connector / failover fix 

Build 1702.3.79, 10 Feb 2017
- Updated application.properties to correct database schema name from dbca to portal
- US799260 appended complete hostname to widget certificate for irvine
- US799260 appended complete hostname to consul config.json file in prod1 and prod2
- US799260 fixed the bar chart issue on devn1, the bar chart shows up on devn1 Home Page

Build 1702.3.78, 9 Feb 2017
- US799260 fixed the firefox compatibility issue; and y-scales issue in bar chart
- US799260 Added the three new widgets in dashboard-widgets folder; removed all testing logs in all widgets; fixed a bug in widget-test

Build 1702.3.75, 07 Feb 2017
DE267061 - Removed a hardcoded intance of loginId used in the query. 

Build 1702.3.73, 06 Feb 2017
DE267061 - Fixed - user should only see widgets that were uploaded against app/roles that they have OR if they were uploaded by checking All Users checkbox. 
Build 1702.3.71, 5 Feb 2017
- Important Note: Copied all these build notes from Portal WebApp to here (widget-ms) - in order not to confuse folks, we have removed Portal's build numbers for these notes. Going forward, will add widget-ms's own build number. Also note that we've changed the version series in POM.xml from 1702.0 to 1702.3 in pom.xml - Although Jenkins/SWM overrides and doesn't care about maven's versioning, but did this to synch with what Portal Web App POM.
- DE261560 Portal App: Widgets MS logs are not getting rolled over
- DE261655 Unable to delete standard widgets
- DE262487 EP-Portal App: News widget not adapting when tile size is changed by user
- DE257516 Common widgets reload in a single tab
- DE262505 Upload Duplicate Widget Issue
- DE262610 Widget Microservice logging is not following EELF guidelines
- DE262800 Issues with widget name this requires database modifications
- DE263090 Issues with widget roles 
- US799260 BE now 'discovers' widget-service using Consul. If consul is not running, it fallbacks to the current implemenation which is https://localhost and the port of your choosing in system.properties - Ex: localhost:8082
- US799260 Fix delete feature for the three special widgets news, events, resources	widget can have spaces in its name
- US799260 Widget feature:
  + Add the editing feature in widgetOnboarding page
  + Move widget catalog and onboarding to new directories
- US811188 Log / Audit widgets logging in DB
- US818934 Allow basic authentication
- US814730 Make news / Events and Resources as widgets
- US827836 Portal Widget Framework
 
## Contact

Ikram Ikramullah

## Certificate

### Create self signed certificate

1. Create a self signed certificate for the microservice
keytool -genkey -alias widget-microservice -storetype PKCS12 -keyalg RSA -keystore widget-keystore.p12

### Import into client's trust store

1. Export certificate from microservice's keystore - default password is 'microservice'
keytool -exportcert -keystore widget-keystore.p12 -alias widget-microservice -file widget-cert.cer

2. Import the exported certificate of microservice from step 1 and import it into Portal JRE's cacerts file. The location of this file is %JRE%/lib/secuirty/cacerts.

keytool -import -trustcacerts -keystore "C:\Program Files\Java\jre1.8.0_91\lib\security\cacerts" -noprompt -alias widget-microservice -file widget-cert.cer

OR 
;
keytool -import -trustcacerts -keystore /usr/local/add-on/jdk1.8.0_60/jre/lib/security/ -noprompt -alias widget-microservice -file widget-cert.cer

### Runing on http (not https)

If the intent is to run this microservice without https i.e run on plain http, comment out (put a leading #) infront of all properties in 'Certificate Properties' section of application.properties - these properties are

server.ssl.key-store=classpath:widget-keystore.p12
server.ssl.key-store-password=ENC(DiIYnAMab4u7rEW2yKhF9zBL00uU55q8)
server.ssl.keyStoreType=PKCS12
server.ssl.keyAlias=widget-microservice

## Running service in docker

a). Build docker image: mvn docker:build
b). Run the service in docker: docker run -p <port>:<port-in> -t <image-name> 
c). Check running docker containers: docker ps

## Migration Instruction  
1. Due to changes in the existing tables, the user will have to remove all the existing widgets in 1702 release 
2. Re-upload the latest version of widgets under dashboard-widgets folder
