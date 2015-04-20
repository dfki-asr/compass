/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.cdi.producers;

import de.dfki.asr.compass.cdi.qualifiers.EventChannel;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;
import org.jboss.logging.Logger;

/**
 * Manages the necessary entities for setting up a JMS session. It will produce the message
 * producers for certain types that can be injected using the {@link CompassJMSProducer} qualifier.
 */
@javax.inject.Singleton
public class JMSProducer {

	@Inject
	protected Logger log;

	@Resource(lookup = "java:/ConnectionFactory")
	protected static ConnectionFactory connectionFactory;

	private Connection jmsConnection;
	private Session jmsSession;

	@PostConstruct
	public void onPostConstruct() {
		try {
			jmsConnection = connectionFactory.createConnection();
			// session: 1st argument: no transaction
			jmsSession = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			jmsConnection.start();
		} catch (JMSException e) {
			log.error("Cannot setup JMS connection: " + e.getMessage());
		}
	}

	@PreDestroy
	public void onPreDestroy() {
		if (jmsConnection != null) {
			try {
				jmsConnection.close();
			} catch (JMSException e) {
				log.error("Cannot close JMS connection: " + e.getMessage());
			}
		}
	}

	@Produces
	public Session getJMSSession() {
		return jmsSession;
	}

	@Produces
	@EventChannel("") // also works for nonempty values, beacuse value is @Nonbinding
	public MessageProducer getTopicProducer(final InjectionPoint injection) {
		String requestedTopic = injection.getAnnotated().getAnnotation(EventChannel.class).value();
		try {
			Topic theTopic = jmsSession.createTopic("compass." + requestedTopic);
			MessageProducer producer = jmsSession.createProducer(theTopic);
			log.infov("Created MessageProducer for Topic: {0}", requestedTopic);
			return producer;
		} catch (JMSException jMSException) {
			log.errorv(jMSException, "Cannot create Topic '{0}'", requestedTopic);
			return null;
		}
	}

	public void closeTopicProducer(@Disposes @EventChannel("") final MessageProducer producer) {
		try {
			producer.close();
		} catch (JMSException ex) {
			log.error("JMS Exception trying to dispose of topic", ex);
		}
	}
}
