Setting up
==========
 
Dependencies
------------

In order to build Portal BE applications on your machine, you'll need to install the following:

1. OpenJDK 8
2. Maven
3. Docker
4. Docker Compose

Cloning the Portal repository
---------------------------------

Clone the Portal repository with git:

::

    git clone "https://gerrit.onap.org/r/portal"

Building
-----------------

::

    cd portal-BE/
    If you get this error "unable to prepare context: path ".\r" not found" please use this command dos2unix build.sh
    ./build.sh

Viewing your app
----------------


::

    http://localhost:8080/login

To log in, use user/password 'demo/demo123'.

.. _access the h2-console: http://localhost:8080/h2-console/

Change:
Saved Settings: Generic MySQL
Driver Class: com.mysql.cj.jdbc.Driver
JDBC URL: jdbc:mysql://portal-db:3306/testdb
User Name: portal
Password: Test123456