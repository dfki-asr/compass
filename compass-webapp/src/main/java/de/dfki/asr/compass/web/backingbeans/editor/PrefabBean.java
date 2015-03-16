/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.backingbeans.editor;

import de.dfki.asr.compass.business.PrefabImporter;
import de.dfki.asr.compass.business.api.Manager;
import de.dfki.asr.compass.business.api.PrefabManager;
import de.dfki.asr.compass.business.api.PrefabSetManager;
import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.web.backingbeans.CompassBean;
import de.dfki.asr.compass.web.util.JSFParameterMap;
import de.dfki.asr.compass.model.PrefabSet;
import de.dfki.asr.compass.model.SceneNode;
import de.dfki.asr.compass.model.resource.Image;
import de.dfki.asr.compass.model.components.PreviewImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.jboss.logging.Logger;

@ViewAccessScoped
@Named
public class PrefabBean extends CompassBean implements Serializable {

	private static final long serialVersionUID = 8915154371549586236L;

	@Inject
	private Logger log;

	@Inject
	private PrefabManager prefabManager;

	@Inject
	private PrefabSetManager prefabSetManager;

	@Inject
	private Manager<SceneNode> sceneNodeManager;

	@Inject
	private JSFParameterMap jsfParameters;

	@Inject
	private ScenarioEditorBean scenarioEditorBean;

	@Inject
	private ViewUpdaterBean viewUpdaterBean;

	@Inject
	private PrefabImporter prefabImporter;

	private boolean importCompleteAssetHierarchy;

	public void setImportCompleteAssetHierarchy(boolean val) {
		importCompleteAssetHierarchy = val;
	}

	public boolean getImportCompleteAssetHierarchy() {
		return importCompleteAssetHierarchy;
	}

	public List<SceneNode> getPrefabListOfCurrentlySelectedPrefabSet() {
		PrefabSet selectedPrefabSet = scenarioEditorBean.getSelectedPrefabSet();
		return selectedPrefabSet.getPrefabs();
	}

	public boolean prefabHasPreview(SceneNode prefab) {
		List<PreviewImage> previews = prefab.getComponentsByType(PreviewImage.class);
		return !previews.isEmpty();
	}

	public Image getPreviewImageOfPrefab(SceneNode prefab) {
		List<PreviewImage> components = prefab.getComponentsByType(PreviewImage.class);
		if (components.isEmpty()) {
			return null;
		}
		return components.get(0).getImage();
	}

	public void replacePrefabWithSceneNode() {
		try {
			long sceneNodeId = Long.valueOf(jsfParameters.get("sceneNodeId"));
			long prefabId = Long.valueOf(jsfParameters.get("prefabId"));
			prefabManager.replacePrefabWithSceneNode(prefabId, sceneNodeId);
		} catch (NumberFormatException e) {
			log.error("Invalid id encountered when trying to replace prefab.", e);
		} catch (EntityNotFoundException e) {
			log.error("Entity not found while trying to create a new prefab.", e);
		}
	}

	public void onSelectSceneNodeFromPrefab() {
		Long id = Long.valueOf(jsfParameters.get("sceneNodeId"));
		try {
			SceneNode selectedPrefab = sceneNodeManager.findById(id);
			scenarioEditorBean.setSelectedSceneNode(selectedPrefab);
			viewUpdaterBean.prefabSceneNodeSelected();
		} catch (EntityNotFoundException e) {
			log.error("Could not find Prefab with id: " + id, e);
		}
	}

	public void deleteSelectedPrefab() {
		SceneNode selectedPrefab = scenarioEditorBean.getSelectedSceneNode();
		PrefabSet prefabSet = scenarioEditorBean.getSelectedPrefabSet();
		prefabSetManager.deletePrefabFromPrefabSet(selectedPrefab, prefabSet);
		scenarioEditorBean.setSelectedSceneNode(null);
	}

	public void importAssetAsPrefab() {
		String assetURL = jsfParameters.get("assetURL");
		String assetName = assetURL.substring(assetURL.lastIndexOf('#') + 1, assetURL.length());
		try {
			SceneNode prefab;
			if (importCompleteAssetHierarchy) {
				prefab = prefabImporter.createSceneNodeHierachyForAsset(assetURL, assetName);
			} else {
				prefab = prefabImporter.createSceneNodeForAsset(assetURL, assetName);
			}
			if (prefab != null) {
				PrefabSet activePrefabSet = scenarioEditorBean.getSelectedPrefabSet();
				prefabSetManager.addPrefabToSet(prefab, activePrefabSet);
			}
		} catch (IOException | IllegalArgumentException e) {
			log.error("Error creating RenderGeometry for external prefab ", e);
		}
	}

	public void refreshPrefabGrid() {
		viewUpdaterBean.updatePrefabSelection();
	}
}
