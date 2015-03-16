/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.business.components;

import de.dfki.asr.compass.business.api.ComponentRegistry;
import de.dfki.asr.compass.business.api.ComponentAnnouncer;
import de.dfki.asr.compass.business.exception.CompassRuntimeException;
import de.dfki.asr.compass.model.SceneNodeComponent;
import de.dfki.asr.compass.model.components.ApplicationComponent;
import de.dfki.asr.compass.model.components.annotations.CompassComponent;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import org.jboss.logging.Logger;

@Named
@Singleton
public class ComponentRegistryImpl implements ComponentRegistry {

	@Inject
	private Logger log;

	@Inject
	private Instance<ComponentAnnouncer> announcers;

	private final Set<Class<? extends Object>> availableComponents = new HashSet<>();

	@PostConstruct
	protected void collectAnnouncedComponents() {
		for (ComponentAnnouncer announcer : announcers) {
			log.infov("Asking {0} to announce its components.", announcer.getClass().getPackage().getName());
			announcer.announceComponents(this);
		}
	}

	@Override
	public void registerComponent(final Class<? extends Object> clazz) {
		if (!clazz.isAnnotationPresent(CompassComponent.class)) {
			throw new CompassRuntimeException("Tried to register a COMPASS Component without a @CompassComponent annotation! This component will be ignored.");
		}
		log.infov("Registered new component: {0}", clazz.getName());
		availableComponents.add(clazz);
	}

	@Override
	public Set<Class<? extends ApplicationComponent>> getAvailableApplicationComponents() {
		return getAvailableComponentClassesFor(ApplicationComponent.class);
	}

	@Override
	public Set<Class<? extends SceneNodeComponent>> getAvailableSceneNodeComponents() {
		return getAvailableComponentClassesFor(SceneNodeComponent.class);
	}

	@Override
	public <T extends Object> Set<Class<? extends T>> getAvailableComponentClassesFor(final Class<T> clazz) {
		if (clazz == null) {
			return new HashSet<>();
		} else {
			Set<Class<? extends T>> filteredSet = new HashSet<>();
			for (Class<? extends Object> c : availableComponents) {
				boolean internalUseOnly = c.getAnnotation(CompassComponent.class).internalUseOnly();
				if (!internalUseOnly && clazz.isAssignableFrom(c)) {
					filteredSet.add((Class<? extends T>) c);
				}
			}
			return Collections.unmodifiableSet(filteredSet);
		}
	}

	@Override
	public Set<String> getAvaliableComponentNames() {
		HashSet<String> names = new HashSet<>();
		for (Class<? extends Object> clazz : availableComponents) {
			names.add(clazz.getSimpleName());
		}
		return names;
	}
}
