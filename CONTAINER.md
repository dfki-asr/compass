Setting up WildFly for COMPASS
==============================

Currently, we support [WildFly](http://www.wildfly.org) as container only.
JavaEE portability is a difficult terrain, and we do not have the resources to manoeuvre around this.
We're sorry for the inconvenience.


Download and extract WildFly
----------------------------

Download the most recent version of [WildFly](http://www.wildfly.org) and extract the archive to a folder on your disk. But before we can run COMPASS there are a few setup steps to go through ...

Using the testing WildFly configuration
---------------------------------------

The `compass-integration-test` module in this repository contains a suitable configuration file to just drop in place.
It includes the database and messaging configuration detailed in the rest of this document.

Copy the `standalone.xml` file found in `compass-integration-test/src/test/resources-wildfly/standalone/configuration/`
to your WildFly `standalone/configuration/` directory.
You may need to overwrite the existing file there.

If you need to run something else apart from COMPASS on your WildFly, you probably need to configure some other things.
The detailed changes necessary can be found in the rest of this document.

Adding users to WildFly
-----------------------

In order to manage the server via the web interface, add a new user via the WildFly CLI:

First we have to open a console and start the server from the `bin` folder by typing `standalone`. Afterwards we can add the new users:

### Set up a management user

In order to manage the server via the web interface, add a new user via the WildFly CLI, as follows.
Execute the `add-user` script from the `bin` folder, and feed it the following answers:

* __Type:__ Management user
* __Name:__ (the username you want to use for the web admin panel)
* __Password:__ (some safe password)
* __Groups:__ (leave empty)
* __Used to connect to another process:__ no

### Set up a user for Messaging

COMPASS' web interface connects to the container through HornetQ's STOMP interface.
Since this requires authentication, you need to add another user using the `add-user` script:

* __Type:__ Application user
* __Name:__ stomp-user
* __Password:__ stomp-user-313
* __Groups:__ guest
* __Used to connect to another process:__ no

You __must__ use this user and password combination since those values are hardcoded in COMPASS' web module. If you want another user or password you need to change them in the `stomp-client.js` file.
We are sorry for this limitation, and have several ideas to get rid of it, none of which we got around to implement yet.


Adding a datasource for compass
-------------------------------

Datasources can be added via the configuration interface of the WildFly server. By default this is available at `localhost:9990`.

Choose *Create Datasource* and *Add*

* __Name:__ Compass
* __JNDI Name:__ java:/compass-remote
	
Press next.

* __Driver:__ h2

Next.

* __connection-url:__ jdbc:h2:mem:compass
* __Username:__ sa
* __Password:__ sa
* __Security domain:__ leave empty
	
Attention: After finishing the registration of the datasource it has to get enabled. To do so use the `enable` button in the datasource overview.

### Storing the database on disk

Note that the above configuration and compass by itself uses a temporary in-memory database. In order to keep your data after a server shutdown, apply the following changes:
In the compass datasource, change the connection-url from `jdbc:h2:mem:compass` to `jdbc:h2:<YOUR-DATABASE-FOLDER>/compass-test`.
Open the persistence unit's persistence.xml and change the entry `<property name="hibernate.hbm2ddl.auto" value="create-drop" /> to `<property name="hibernate.hbm2ddl.auto" value="update" />`.
Otherwise, the database will be reinitialized on each deploy.


Messaging configuration
-----------------------

For messaging configuration, open the server's standalone.xml (server-root/standalone/configuration/standalone.xml) and add the following lines:

Add the module for the messaging service to the list of extension modules:

`<extension module="org.jboss.as.messaging"/>`

 Add the mdb resource adapter to the ejb subsystem:

	<mdb>
		<resource-adapter-ref resource-adapter-name="${ejb.resource-adapter-name:hornetq-ra.rar}"/>
		<bean-instance-pool-ref pool-name="mdb-strict-max-pool"/>
	</mdb>

Finally, add a new subsystem for hornetq using the following settings:

	<subsystem xmlns="urn:jboss:domain:messaging:2.0">
		<hornetq-server>
			<journal-file-size>102400</journal-file-size>
			<connectors>
				<http-connector name="http-connector" socket-binding="http">
					<param key="http-upgrade-endpoint" value="http-acceptor"/>
				</http-connector>
				<http-connector name="http-connector-throughput" socket-binding="http">
					<param key="http-upgrade-endpoint" value="http-acceptor-throughput"/>
					<param key="batch-delay" value="50"/>
				</http-connector>
				<in-vm-connector name="in-vm" server-id="0"/>
				<connector name="netty-connector">
					<factory-class>org.hornetq.core.remoting.impl.netty.NettyConnectorFactory</factory-class>
				</connector>
			</connectors>
			<acceptors>
				<http-acceptor http-listener="default" name="http-acceptor"/>
				<http-acceptor http-listener="default" name="http-acceptor-throughput">
					<param key="batch-delay" value="50"/>
					<param key="direct-deliver" value="false"/>
				</http-acceptor>
				<in-vm-acceptor name="in-vm" server-id="0"/>
				<acceptor name="stomp-websocket">
					<factory-class>org.hornetq.core.remoting.impl.netty.NettyAcceptorFactory</factory-class>
					<param key="host" value="0.0.0.0"/>
					<param key="port" value="61614"/>
				</acceptor>
				<acceptor name="stomp">
					<factory-class>org.hornetq.core.remoting.impl.netty.NettyAcceptorFactory</factory-class>
					<param key="host" value="0.0.0.0"/>
					<param key="port" value="61613"/>
				</acceptor>
			</acceptors>
			<security-settings>
				<security-setting match="#">
					<permission type="send" roles="guest"/>
					<permission type="consume" roles="guest"/>
					<permission type="createNonDurableQueue" roles="guest"/>
					<permission type="deleteNonDurableQueue" roles="guest"/>
				</security-setting>
			</security-settings>
			<address-settings>
				<address-setting match="#">
					<dead-letter-address>jms.queue.DLQ</dead-letter-address>
					<expiry-address>jms.queue.ExpiryQueue</expiry-address>
					<max-size-bytes>10485760</max-size-bytes>
					<page-size-bytes>2097152</page-size-bytes>
					<message-counter-history-day-limit>10</message-counter-history-day-limit>
				</address-setting>
			</address-settings>
			<jms-connection-factories>
				<connection-factory name="InVmConnectionFactory">
					<connectors>
						<connector-ref connector-name="in-vm"/>
					</connectors>
					<entries>
						<entry name="java:/ConnectionFactory"/>
					</entries>
				</connection-factory>
				<connection-factory name="RemoteConnectionFactory">
					<connectors>
						<connector-ref connector-name="http-connector"/>
					</connectors>
					<entries>
						<entry name="java:jboss/exported/jms/RemoteConnectionFactory"/>
					</entries>
				</connection-factory>
				<pooled-connection-factory name="hornetq-ra">
					<transaction mode="xa"/>
					<connectors>
						<connector-ref connector-name="in-vm"/>
					</connectors>
					<entries>
						<entry name="java:/JmsXA"/>
						<entry name="java:jboss/DefaultJMSConnectionFactory"/>
					</entries>
				</pooled-connection-factory>
			</jms-connection-factories>
			<jms-destinations>
				<jms-queue name="ExpiryQueue">
					<entry name="java:/jms/queue/ExpiryQueue"/>
				</jms-queue>
				<jms-queue name="DLQ">
					<entry name="java:/jms/queue/DLQ"/>
				</jms-queue>
				<jms-topic name="compass.projects">
					<entry name="java:/jms/topic/compass/projects"/>
				</jms-topic>
				<jms-topic name="compass.scenarios">
					<entry name="java:/jms/topic/compass/scenarios"/>
				</jms-topic>
				<jms-topic name="compass.sceneNodes">
					<entry name="java:/jms/topic/compass/sceneNodes"/>
				</jms-topic>
				<jms-topic name="compass.prefabSets">
					<entry name="java:/jms/topic/compass/prefabSets"/>
				</jms-topic>
				<jms-topic name="compass.sceneNodeComponents">
					<entry name="java:/jms/topic/compass/sceneNodeComponents"/>
				</jms-topic>
			</jms-destinations>
		</hornetq-server>
	</subsystem>

Deploying the application
-------------------------

Once the container is configured, you can deploy the COMPASS EAR.
After compilation (see the [README file](README.md)), you can find this file in the `compass-deployment/target/` directory in your source folder.

To deploy the application, WildFly needs to be running.
Unless it already is, start the `standalone` script in WildFly's `bin` folder.
Once the container is running, it should suffice to copy the EAR archive to the `standalone/deployments/` folder of WildFly.

After allowing the container some time to bring up all the classes and services needed for COMPASS,
it should be available from your container's `compass/` context.
With the configuration supplied, you can reach this via `http://localhost:8080/compass/`.
