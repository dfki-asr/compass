/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.business.components;

import de.dfki.asr.compass.business.api.ComponentManager;
import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.business.exception.PersistenceException;
import de.dfki.asr.compass.model.SceneNode;
import de.dfki.asr.compass.model.SceneNodeComponent;
import de.dfki.asr.compass.business.services.CRUDService;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class ComponentManagerImpl implements  Serializable, ComponentManager {
	private static final long serialVersionUID = -6931525940449463067L;

	@Inject
	private CRUDService crudService;

	@Override
	public void save(final SceneNodeComponent component) throws PersistenceException {
		crudService.save(component);
	}

	@Override
	public SceneNodeComponent findById(final long entityId) throws EntityNotFoundException {
		return crudService.findById(SceneNodeComponent.class, entityId);
	}

	@Override
	public void removeById(final long entityId) throws EntityNotFoundException {
		crudService.remove( findById(entityId) );
	}

	@Override
	public void remove(final SceneNodeComponent entity) throws EntityNotFoundException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public SceneNodeComponent referenceById(final long id) throws EntityNotFoundException {
		return crudService.referenceById(SceneNodeComponent.class, id);
	}

	@Override
	public <T extends Object> T createComponent(final Class<T> clazz) throws InstantiationException, IllegalAccessException {
		return clazz.newInstance();
	}

	@Override
	public SceneNode addComponentToNode(final SceneNodeComponent component, final SceneNode node) {
		node.addComponent(component);
		component.setOwner(node);
		crudService.save(component);
		return crudService.save(node);
	}

	@Override
	public Set<Class<? extends SceneNodeComponent>> getComponentClassesUsedBySceneNode(final SceneNode node) {
		HashSet<Class<? extends SceneNodeComponent>> classes = new HashSet<>();
		for (SceneNodeComponent c : node.getComponents()) {
			classes.add(c.getClass());
		}
		return classes;
	}

	@Override
	public SceneNode removeComponentFromSceneNode(final SceneNodeComponent component, final SceneNode node) {
		if (node.removeComponent(component)) {
			crudService.remove(component);
			return crudService.save(node);
		} else {
			return node;
		}
	}

	@Override
	public SceneNode removeComponentClassFromSceneNode(final Class<? extends SceneNodeComponent> componentClass, final SceneNode node) {
		List<? extends SceneNodeComponent> components = node.getComponentsByType(componentClass);
		for (SceneNodeComponent sceneNodeComponent : components) {
			node.removeComponent(sceneNodeComponent);
			crudService.remove(sceneNodeComponent);
		}
		return crudService.save(node);
	}
}
