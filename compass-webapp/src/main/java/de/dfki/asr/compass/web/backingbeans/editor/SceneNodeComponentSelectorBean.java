/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.backingbeans.editor;

import de.dfki.asr.compass.business.api.ComponentManager;
import de.dfki.asr.compass.business.api.ComponentRegistry;
import de.dfki.asr.compass.model.SceneNode;
import de.dfki.asr.compass.model.SceneNodeComponent;
import de.dfki.asr.compass.web.util.ClassNameComparator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.jboss.logging.Logger;
import org.primefaces.model.DualListModel;

@Named
@RequestScoped
public class SceneNodeComponentSelectorBean implements Serializable {

	private static final long serialVersionUID = -235962134495735679L;

	@Inject
	private Logger log;

	@Inject
	private ComponentManager componentManager;

	@Inject
	private ComponentRegistry componentRegistry;

	@Inject
	private ScenarioEditorBean editor;

	private transient List<Class<? extends SceneNodeComponent>> available;
	private transient List<Class<? extends SceneNodeComponent>> used;
	private DualListModel<Class<? extends SceneNodeComponent>> model;

	public SceneNodeComponentSelectorBean() {
	}

	@PostConstruct
	public void initialize() {
		initializeUsedClasses();
		initializeAvailbleClasses();
		createPrimeFacesModel();
	}

	public DualListModel<Class<? extends SceneNodeComponent>> getModel() {
		return model;
	}

	public void setModel(DualListModel<Class<? extends SceneNodeComponent>> _model) {
		model = _model;
	}

	public void confirmComponentSelection() {
		deleteRemovedComponents();
		createAddedComponents();
	}

	public void cancelComponentSelection() {
		initialize();
	}

	private void initializeAvailbleClasses() {
		if (componentRegistry == null) {
			log.error("No component registry available. No SceneNodeComponents to offer.");
			available = new ArrayList<>();
		} else {
			Set<Class<? extends SceneNodeComponent>> availableSet = componentRegistry.getAvailableSceneNodeComponents();
			List<Class<? extends SceneNodeComponent>> all = new ArrayList<>(availableSet);
			all.removeAll(used);
			Collections.sort(all, new ClassNameComparator());
			available = all;
			log.infov("{0} Components retrieved from registry.", all.size());
		}
	}

	private void initializeUsedClasses() {
		if (editor == null || editor.getSelectedSceneNode() == null) {
			used = new ArrayList<>();
		} else {
			used = new ArrayList<>(componentManager.getComponentClassesUsedBySceneNode(editor.getSelectedSceneNode()));
			Collections.sort(used, new ClassNameComparator());
		}
	}

	private void createPrimeFacesModel() {
		model = new DualListModel(available, used);
	}

	private void deleteRemovedComponents() {
		SceneNode node = editor.getSelectedSceneNode();
		Set<Class<? extends SceneNodeComponent>> currentlyUsed = componentManager.getComponentClassesUsedBySceneNode(editor.getSelectedSceneNode());
		List<Class<? extends SceneNodeComponent>> expected = model.getTarget();
		for (Class<? extends SceneNodeComponent> current : currentlyUsed) {
			if (!expected.contains(current)) {
				componentManager.removeComponentClassFromSceneNode(current, node);
			}
		}
	}

	private void createAddedComponents() {
		SceneNode node = editor.getSelectedSceneNode();
		Set<Class<? extends SceneNodeComponent>> currentlyUsed = componentManager.getComponentClassesUsedBySceneNode(editor.getSelectedSceneNode());
		List<Class<? extends SceneNodeComponent>> expected = model.getTarget();
		for (Class<? extends SceneNodeComponent> expectedClass : expected) {
			if (!currentlyUsed.contains(expectedClass)) {
				try {
					SceneNodeComponent created = componentManager.createComponent(expectedClass);
					componentManager.addComponentToNode(created, node);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException e) {
					log.error("Cannot instantiate SceneNodeComponent " + expectedClass, e);
				}
			}
		}
	}

}
