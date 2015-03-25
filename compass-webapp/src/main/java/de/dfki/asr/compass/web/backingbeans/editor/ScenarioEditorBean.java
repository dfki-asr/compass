/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.backingbeans.editor;

import de.dfki.asr.compass.business.api.PrefabSetManager;
import de.dfki.asr.compass.business.api.SceneTreeManager;
import de.dfki.asr.compass.web.backingbeans.project.OpenScenarioBean;
import de.dfki.asr.compass.math.Vector3f;
import de.dfki.asr.compass.model.PrefabSet;
import de.dfki.asr.compass.model.Scenario;
import de.dfki.asr.compass.model.SceneNode;
import de.dfki.asr.compass.web.util.JSFParameterMap;
import de.dfki.asr.compass.web.util.XML3DUtils;
import java.io.Serializable;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.jboss.logging.Logger;
import org.primefaces.context.RequestContext;

@ViewAccessScoped
@Named
public class ScenarioEditorBean implements Serializable {
	private static final long serialVersionUID = -5665912650612144725L;

	@Inject
	private Logger log;

	@Inject
	protected OpenScenarioBean openScenarioBean;

	@Inject
	protected SceneHierarchyBean sceneHierarchyBean;

	@Inject
	protected PrefabBean prefabBean;

	protected SceneNode selectedSceneNode;
	protected PrefabSet selectedPrefabSet;

	@Inject
	protected SceneTreeManager sceneTree;

	@Inject
	protected PrefabSetManager prefabSetManager;

	@Inject
	protected ViewUpdaterBean viewUpdaterBean;

	@Inject
	private JSFParameterMap jsfParameters;

	public SceneNode getSelectedSceneNode() {
		return selectedSceneNode;
	}

	public void setSelectedSceneNode(SceneNode newSelectedSceneNode) {
		selectedSceneNode = newSelectedSceneNode;
		sceneHierarchyBean.setSelectedSceneNode(selectedSceneNode);
	}

	public void setSelectedPrefabSet(PrefabSet selected) {
		selectedPrefabSet = selected;
	}

	public PrefabSet getSelectedPrefabSet() {
		return selectedPrefabSet;
	}

	public Scenario getScenarioBeingEdited() {
		return openScenarioBean.getSelectedScenario();
	}

	public void saveSelectedSceneNodeChanges() {
		if (selectedSceneNode == null) {
			return;
		}
		sceneTree.saveNode(selectedSceneNode);
		viewUpdaterBean.updateTreeView();
		viewUpdaterBean.updatePrefabSelection();
		viewUpdaterBean.updatePropertyView();
	}

	public void updateSelectedSceneNodeTransform() {
		if (selectedSceneNode == null) {
			return;
		}
		try {
			Vector3f translation = XML3DUtils.createVectorfromXML3DDOMString(jsfParameters.get("translation"));
			double yaw = Float.valueOf(jsfParameters.get("yaw"));
			double pitch = Float.valueOf(jsfParameters.get("pitch"));
			double roll = Float.valueOf(jsfParameters.get("roll"));
			float scale = Float.valueOf(jsfParameters.get("scale"));

			selectedSceneNode.setLocalTranslation(translation);
			selectedSceneNode.setLocalYaw(yaw);
			selectedSceneNode.setLocalPitch(pitch);
			selectedSceneNode.setLocalRoll(roll);
			selectedSceneNode.setLocalScale(scale);
			saveSelectedSceneNodeChanges();
		} catch (NumberFormatException e) {
			log.error("Invalid transform value encountered during selected scene node transform update.");
		}
	}

	public void onDeselectSceneNode() {
		setSelectedSceneNode(null);
		viewUpdaterBean.sceneNodeSelectionCleared();
	}

	public void saveSelectedPrefabSetChanges() {
		if (selectedPrefabSet == null) {
			return;
		}
		prefabSetManager.save(selectedPrefabSet);
		RequestContext context = RequestContext.getCurrentInstance();
		context.update("prefabSetViewForm:prefabSetView");
	}
}
