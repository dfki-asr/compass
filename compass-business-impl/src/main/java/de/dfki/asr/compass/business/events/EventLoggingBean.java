/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.business.events;

import de.dfki.asr.compass.cdi.qualifiers.EventChannel;
import de.dfki.asr.compass.model.AbstractCompassEntity;
import de.dfki.asr.compass.model.PrefabSet;
import de.dfki.asr.compass.model.Project;
import de.dfki.asr.compass.model.Scenario;
import de.dfki.asr.compass.model.SceneNode;
import de.dfki.asr.compass.model.SceneNodeComponent;
import java.beans.Introspector;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.jboss.logging.Logger;

@javax.ejb.Singleton
@Startup
public class EventLoggingBean {

	@Inject
	protected Logger log;

	@Inject
	private Session jmsSession;

	@Inject
	@EventChannel("projects")
	private MessageProducer projectMessageProducer;

	@Inject
	@EventChannel("scenarios")
	private MessageProducer scenarioMessageProducer;

	@Inject
	@EventChannel("sceneNodes")
	private MessageProducer sceneNodeMessageProducer;

	@Inject
	@EventChannel("sceneNodeComponents")
	private MessageProducer sceneNodeComponentMessageProducer;

	@Inject
	@EventChannel("prefabSets")
	private MessageProducer prefabSetMessageProducer;

	private final Map<Class<?>,MessageProducer> channels;

	public EventLoggingBean() {
		channels = new HashMap<>();
	}

	@PostConstruct
	public void registerChannels() {
		channels.put(Project.class, projectMessageProducer);
		channels.put(Scenario.class, scenarioMessageProducer);
		channels.put(SceneNode.class, sceneNodeMessageProducer);
		channels.put(SceneNodeComponent.class, sceneNodeComponentMessageProducer);
		channels.put(PrefabSet.class, prefabSetMessageProducer);
	}

	public void handleEntityCreated(@Observes final EntityCreatedEvent event) {
		logEntityEvent(event.target, "created");
		publishEntityEvent(event.target, "created");
	}

	public void handleEntityChanged(@Observes final EntityChangedEvent event) {
		logEntityEvent(event.target, "changed");
		publishEntityEvent(event.target, "changed");
	}

	public void handleEntityDeleted(@Observes final EntityDeletedEvent event) {
		logEntityEvent(event.target, "deleted");
		publishEntityEvent(event.target, "deleted");
	}

	private void publishJMSEvent(final MessageProducer producer, final String eventText) {
		try {
			TextMessage message = jmsSession.createTextMessage(eventText);
			producer.send(message);
		} catch (JMSException e) {
			log.errorv("Cannot publish JMS message: {0}", e.getMessage());
		}
	}

	private void logEntityEvent(final Object target, final String eventName) {
		String logString = String.format(
				"%s a %s: %s",
				eventName,
				target.getClass().getSimpleName(),
				target.toString()
			);
		log.info(logString);
	}

	private void publishEntityEvent(final Object target, final String event) {
		if (!(target instanceof AbstractCompassEntity)) {
			log.errorv("Trying to process an event for something that is not a CompassEntity:{0}", target.toString());
			return;
		}
		MessageProducer channel = findChannel(target);
		if (channel == null) {
			log.errorv("No JMS topic registered for Class {0}", target.getClass().getName());
			return;
		}
		String eventText = formatEvent((AbstractCompassEntity)target, event);
		publishJMSEvent(channel, eventText);
	}

	private String formatEvent(final AbstractCompassEntity entity, final String event) {
		String decapitalizedClassName = Introspector.decapitalize(entity.getClass().getName());
		String eventText = String.format(
				"%s:%s:%d",
				decapitalizedClassName,
				event,
				entity.getId()
		);
		return eventText;
	}

	private MessageProducer findChannel(final Object target) {
		for (Class<?> clazz : channels.keySet()) {
			if (clazz.isAssignableFrom(target.getClass())) {
				return channels.get(clazz);
			}
		}
		return null;
	}
}
