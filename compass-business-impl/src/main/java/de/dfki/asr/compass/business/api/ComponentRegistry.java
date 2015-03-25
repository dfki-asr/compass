/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.business.api;

import de.dfki.asr.compass.model.SceneNodeComponent;
import de.dfki.asr.compass.model.components.ApplicationComponent;
import java.util.Set;

/**
 * A registry for classes.
 * A bean implementing this interface should be created as a @Singleton, and should
 * query all implementations of {@see ComponentAnnouncer} via CDI on first access.
 */
public interface ComponentRegistry {

	/**
	 * Registered ApplicationComponent classes.
	 * Shorthand for: getAvailableComponentClassesFor(ApplicationComponent.class);
	 * @return Set of all registered Classes that derive from ApplicationComponent.
	 */
	Set<Class<? extends ApplicationComponent>> getAvailableApplicationComponents();

	/**
	 * Query the registry for components of a specific type hierarchy.
	 * @param <T> the parent type of the components to query the registry for.
	 * @param clazz must be equal to T.class
	 * @return Set of all component classes that derive from T.
	 */
	<T extends Object> Set<Class<? extends T>> getAvailableComponentClassesFor(Class<T> clazz);

	/**
	 * Registered SceneNodeComponent classes.
	 * Shorthand for: getAvailableComponentClassesFor(SceneNodeComponent.class);
	 * @return Set of all registered Classes that derive from SceneNodeComponent.
	 */
	Set<Class<? extends SceneNodeComponent>> getAvailableSceneNodeComponents();

	@Deprecated
	Set<String> getAvaliableComponentNames();

	/**
	 * Register a new component class.
	 * @see ComponentAnnouncer
	 * @param clazz the new component class to register with the registry.
	 */
	void registerComponent(Class<? extends Object> clazz);

}
