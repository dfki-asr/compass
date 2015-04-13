/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.backingbeans.editor;

import de.dfki.asr.compass.business.api.SceneTreeManager;
import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.math.Vector3f;
import de.dfki.asr.compass.model.SceneNode;
import de.dfki.asr.compass.web.util.JSFParameterMap;
import java.io.Serializable;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.jboss.logging.Logger;

@ViewAccessScoped
@Named("xml3dViewBean")
public class XML3DViewBean implements Serializable {
	private static final long serialVersionUID = 1707640682040592237L;

	@Inject
	private Logger log;

	@Inject
	private JSFParameterMap jsfParameters;

	@Inject
	private SceneTreeManager sceneTree;

	@Inject
	private SceneHierarchyBean sceneHierarchy;

	@Inject
	private ScenarioEditorBean scenarioEditorBean;

	@Inject
	private ViewUpdaterBean viewUpdaterBean;

	public void handleXML3DPrefabDrop() {
		try {
			long prefabId = Long.valueOf(jsfParameters.get("prefabId"));
			Vector3f position = Vector3f.fromDOMString(jsfParameters.get("hitPoint"));
			SceneNode droppedPrefab = sceneTree.findById(prefabId);
			sceneHierarchy.addNewSceneNodeFromPrefab(droppedPrefab, sceneHierarchy.getRootSceneNode(), position);
			viewUpdaterBean.sceneNodeHierarchyChanged();
		} catch (NumberFormatException e) {
			log.error("Invalid scene node id passed as argument to XML3D click handler: " + jsfParameters.get("prefabId"));
		} catch (EntityNotFoundException e) {
			log.error("Prefab not found: " + e);
		}
	}

	public void onSelectSceneNodeInXml3dView() {
		String sceneNodeIdString = jsfParameters.get("sceneNodeId");
		try {
			long sceneNodeId = Long.valueOf(sceneNodeIdString);
			SceneNode newSelectedSceneNode = sceneTree.findById(sceneNodeId);
			scenarioEditorBean.setSelectedSceneNode(newSelectedSceneNode);
			viewUpdaterBean.visibleSceneNodeSelected();
		} catch (NumberFormatException e) {
			log.error("Failed to select scene node remotely. The ID cannot be parsed: " + jsfParameters.get("sceneNodeId"));
		} catch (EntityNotFoundException ex) {
			log.error("Failed to select scene node remotely with id: " + sceneNodeIdString + ". Scene node not found.");
		}
	}
}
