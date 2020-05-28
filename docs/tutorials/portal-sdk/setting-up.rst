Setting up
==========
 
Dependencies
------------

In order to build Portal SDK applications on your machine, you'll need to install the following:

1. OpenJDK 8
2. Maven
3. MariaDB (v10.1)
4. Apache Tomcat (v8.5)

Cloning the Portal SDK repository
---------------------------------

Clone the Portal SDK repository with git:

::

    git clone --depth 1 http://gerrit.onap.org/r/portal/sdk

Building the base
-----------------

Now, we'll build the base with maven:

::

    cd sdk/ecomp-sdk
    mvn install

Setting up the MariaDB database
-------------------------------

Adding support for lowercase table names
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

To add support for lowercase table names, make sure the following line in your :code:`/etc/mysql/my.cnf` file is present under the :code:`[mysqld]` section:

::

    lower_case_table_names = 1 

If you made changes, you'll need to restart your MariaDB instance. Hint:

::

    sudo systemctl restart mariadb.service


Database setup
^^^^^^^^^^^^^^

Now, we set up the database and user privileges. Log into your MariaDB instance as the 'root' user and do the following:

::

    drop database if exists ecomp_sdk;
    drop user if exists 'ecomp_sdk_user'@'localhost';
    
    create database ecomp_sdk;
    create user 'ecomp_sdk_user'@'localhost' identified by 'password';
    grant all privileges on ecomp_sdk.* to 'ecomp_sdk_user'@'localhost';

Adding tables
^^^^^^^^^^^^^

Next, we need to run several SQL statements in the repository:

::

    mysql -proot -uroot < sdk/ecomp-sdk/epsdk-app-common/db-scripts/EcompSdkDDLMySql_2_4_Common.sql
    mysql -proot -uroot < sdk/ecomp-sdk/epsdk-app-common/db-scripts/EcompSdkDMLMySql_2_4_Common.sql
    mysql -proot -uroot < sdk/ecomp-sdk/epsdk-app-os/db-scripts/EcompSdkDDLMySql_2_4_OS.sql
    mysql -proot -uroot < sdk/ecomp-sdk/epsdk-app-os/db-scripts/EcompSdkDMLMySql_2_4_OS.sql


Using 'root' for both your MySQL username and password is just about the worst security policy imaginable. For anything other than temporary setups (very temporary), please choose reasonable user names and hard-to-guess passwords.

Your project directory
-------------------------------

Because you'll want your project to use the latest portal SDK code (retrieved via :code:`git pull`), you work directly in :code:`sdk/ecomp_sdk/epsdk-app-os`.

Connecting your app to the backend database
-------------------------------------------

We need to tell our app how to access the database tables we created above. Open :code:`sdk/ecomp-sdk/epsdk-app-os/src/main/webapp/WEB-INF/conf/system.properties` and change the following two lines to reflect the database user you set up above as well as your particular installation of MariaDB:

::

    db.connectionURL = jdbc:mysql://localhost:3306/ecomp_sdk
    db.userName = ecomp_sdk_user
    db.password = password

Building your app
-----------------

When you want to build your app:

::

    # First cd to the project directory
    cd sdk/ecomp_sdk/epsdk-app-os
    mvn clean package

.. note:: You don't always have to :code:`clean`. Only use it when you want to clear out intermediate objects and build your entire project from scratch.

.. _installingyourapp:

Installing your app
-------------------

To install your app, run the following commands, or better, create a script:

::

    # Shutdown tomcat 
    <tomcat install>/bin/shutdown.sh
    rm -rf <tomcat install>/webapps/epsdk-app-os*
    cp target/epsdk-app-os.war <tomcat install>/webapps/.
    # Start tomcat 
    <tomcat install>/bin/startup.sh

Viewing your app
----------------

Assuming you have installed Tomcat locally, for example in a vagrant VM with port forwarding, you can `access the app`_ in your normal browser:

::

    http://localhost:8080/epsdk-app-os/login.htm

To log in, use user/password 'demo/demo'.

.. _access the app: http://localhost:8080/epsdk-app-os/login.htm
