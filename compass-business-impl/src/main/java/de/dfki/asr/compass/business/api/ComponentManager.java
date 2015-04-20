/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.business.api;

import de.dfki.asr.compass.model.SceneNode;
import de.dfki.asr.compass.model.SceneNodeComponent;
import java.util.Set;

/**
 *
 * @author wolfgang
 */
public interface ComponentManager extends Manager<SceneNodeComponent> {

	/**
	 * Add a component to a a scene node.
	 *
	 * @param component component to add
	 * @param node scene node to add to
	 * @return the scene node
	 */
	SceneNode addComponentToNode(SceneNodeComponent component, SceneNode node);

	/**
	 * Create a new Component of a specified type.
	 * @param <T> component type
	 * @param clazz component type
	 * @return the created component
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	<T extends Object> T createComponent(Class<T> clazz) throws InstantiationException, IllegalAccessException;

	/**
	 * Get a list of the classes of the components of a scene node.
	 *
	 * @param node node to retrieve list from
	 * @return list of the classes of the components
	 */
	Set<Class<? extends SceneNodeComponent>> getComponentClassesUsedBySceneNode(SceneNode node);

	/**
	 * Remove all components of a specific type from a scene node.
	 *
	 * @param componentClass type of the components, that will be removed
	 * @param node node to remove components from
	 * @return the scene node
	 */
	SceneNode removeComponentClassFromSceneNode(Class<? extends SceneNodeComponent> componentClass, SceneNode node);

	/**
	 * Remove a component from a scene node.
	 *
	 * @param component component to remove
	 * @param node node to remove from
	 * @return the scene node
	 */
	SceneNode removeComponentFromSceneNode(SceneNodeComponent component, SceneNode node);

}
