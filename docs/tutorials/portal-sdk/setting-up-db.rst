Setting up a database connection
================================
 
Most applications will need access to a database. In this tutorial, we'll connect to a database in order to pull data for displaying in a Google chart.

Injecting data
--------------

First, let's generate some fake data to display. Create an sql file and populate it with the following:

.. code-block:: sql

  use ecomp_sdk;

  create table MOCK_DATA_AVG_SPEED (
    data_date DATE,
    speedmbps INT,
    direction varchar(10)
  );
  insert into MOCK_DATA_AVG_SPEED (data_date, speedmbps, direction) values ('2017-08-01', 40, 'download');
  insert into MOCK_DATA_AVG_SPEED (data_date, speedmbps, direction) values ('2017-08-02', 18, 'download');
  insert into MOCK_DATA_AVG_SPEED (data_date, speedmbps, direction) values ('2017-08-03', 25, 'download');
  insert into MOCK_DATA_AVG_SPEED (data_date, speedmbps, direction) values ('2017-08-04', 48, 'download');
  insert into MOCK_DATA_AVG_SPEED (data_date, speedmbps, direction) values ('2017-08-05', 49, 'download');
  insert into MOCK_DATA_AVG_SPEED (data_date, speedmbps, direction) values ('2017-08-06', 46, 'download');
  insert into MOCK_DATA_AVG_SPEED (data_date, speedmbps, direction) values ('2017-08-07', 35, 'download');
  insert into MOCK_DATA_AVG_SPEED (data_date, speedmbps, direction) values ('2017-08-01', 10, 'upload');
  insert into MOCK_DATA_AVG_SPEED (data_date, speedmbps, direction) values ('2017-08-02', 15, 'upload');
  insert into MOCK_DATA_AVG_SPEED (data_date, speedmbps, direction) values ('2017-08-03', 14, 'upload');
  insert into MOCK_DATA_AVG_SPEED (data_date, speedmbps, direction) values ('2017-08-04', 9, 'upload');
  insert into MOCK_DATA_AVG_SPEED (data_date, speedmbps, direction) values ('2017-08-05', 12, 'upload');
  insert into MOCK_DATA_AVG_SPEED (data_date, speedmbps, direction) values ('2017-08-06', 13, 'upload');
  insert into MOCK_DATA_AVG_SPEED (data_date, speedmbps, direction) values ('2017-08-07', 15, 'upload');

Now, run it. Something like this:

::

	mysql -p<passwd> -u<user> < mock_data.sql

.. _connectionjava:

Setting up a connection in Java
-------------------------------

We'll need a place to store some data sources. In this case, we only need one, but your application might have more. Add the following member variable to your :code:`MyAppController.java` class:

.. code-block:: java

	private HashMap<String,DataSource> m_dataSources;

Don't forget to import the HashMap object:

.. code-block:: java

	import java.util.HashMap;

Now, we'll add a new private function, :code:`_getDataSources`:

.. code-block:: java

  private HashMap<String,DataSource> _getDataSources() throws Exception {
    HashMap<String,DataSource> dataSources = new HashMap<String,DataSource>();
    ComboPooledDataSource ds = new ComboPooledDataSource();
    try {
      ds.setDriverClass(SystemProperties.getProperty("db.driver"));
      ds.setJdbcUrl(SystemProperties.getProperty("db.connectionURL"));
      ds.setUser(SystemProperties.getProperty("db.userName"));
      ds.setPassword(SystemProperties.getProperty("db.password"));
      ds.setMinPoolSize(Integer.parseInt(SystemProperties.getProperty(SystemProperties.DB_MIN_POOL_SIZE)));
      ds.setMaxPoolSize(Integer.parseInt(SystemProperties.getProperty(SystemProperties.DB_MAX_POOL_SIZE)));
      ds.setIdleConnectionTestPeriod(Integer.parseInt(SystemProperties.getProperty(SystemProperties.IDLE_CONNECTION_TEST_PERIOD)));
      dataSources.put("myappdb", ds);
    }
    catch (Exception e) {
      throw e;
    }
    
    return dataSources;
  }

Notice that because we're piggy-backing our data to the ecomp_sdk database, we're borrowing a few properties as well. You can also add your own properties to :code:`sdk/ecomp-sdk/epsdk-app-os/src/main/webapp/WEB-INF/conf/system.properties` and use them. This allows you to create any number of connections and connection methods in your app. E.g.:

.. code-block:: java

	ds.setDriverClass(SystemProperties.getProperty("db.some_other_driver"));

Now, we need to add some code to our constructor so that the connection is set up when the controller is instantiated:

.. code-block:: java

  public MyAppController() {
    super();
    try {
      this.m_dataSources = _getDataSources();
    }
    catch (Exception e) {
      // Probably a good idea to do something here ;-)
    }
  }


