/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.backingbeans.editor;

import de.dfki.asr.compass.business.api.PrefabSetManager;
import de.dfki.asr.compass.business.api.SceneTreeManager;
import de.dfki.asr.compass.web.backingbeans.CompassBean;
import de.dfki.asr.compass.web.backingbeans.PrimeFacesHelpers;
import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.model.PrefabSet;
import de.dfki.asr.compass.model.SceneNode;
import de.dfki.asr.compass.web.util.JSFParameterMap;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.jboss.logging.Logger;
import org.primefaces.model.TreeNode;

@ViewAccessScoped
@Named
public class PrefabSetsHierarchyBean extends CompassBean implements Serializable {

	private static final long serialVersionUID = -7377340414127654334L;

	@Inject
	private Logger log;

	@Inject
	private JSFParameterMap jsfParameters;

	@Inject
	private PrefabSetManager prefabSetManager;

	@Inject
	private SceneTreeManager sceneTree;

	@Inject
	private ScenarioEditorBean scenarioEditorBean;

	@Inject
	private ViewUpdaterBean viewUpdaterBean;

	private PrefabSetTreeNode rootPrefabSetTreeNode;
	private PrefabSetTreeNode selectedPrefabSetTreeNode;
	private Map<Long, PrefabSetTreeNode> prefabSetTreeNodeByIdMap;

	public TreeNode getRootPrefabSetTreeNode() {
		if (rootPrefabSetTreeNode == null) {
			generatePrefabSetTree();
			selectFirstPrefabSet();
		}
		return rootPrefabSetTreeNode;
	}

	private void generatePrefabSetTree() {
		PrefabSet rootSet = getRootPrefabSet();
		rootPrefabSetTreeNode = new PrefabSetTreeNode(null);
		prefabSetTreeNodeByIdMap = new HashMap<>();
		prefabSetTreeNodeByIdMap.put(rootSet.getId(), rootPrefabSetTreeNode);
		generatePrefabSetTreeNode(rootSet, rootPrefabSetTreeNode);
		setSelectedPrefabSetTreeNode(rootPrefabSetTreeNode);
	}

	public PrefabSet getRootPrefabSet() {
		if (scenarioEditorBean.getScenarioBeingEdited() == null) {
			throw new IllegalStateException("No scenario selected while finding root PrefabSet.");
		}
		return scenarioEditorBean.getScenarioBeingEdited().getProject().getPrefabSet();
	}

	private void generatePrefabSetTreeNode(PrefabSet prefabSet, TreeNode parent) {
		PrefabSetTreeNode newNode = new PrefabSetTreeNode(prefabSet);
		newNode.setParent(parent);
		prefabSetTreeNodeByIdMap.put(prefabSet.getId(), newNode);
		for (PrefabSet child : prefabSet.getChildren()) {
			generatePrefabSetTreeNode(child, newNode);
		}
	}

	private void selectFirstPrefabSet() {
		PrefabSetTreeNode rootSetTreeNode = getTreeNodeOfRootPrefabSet();
		if (rootSetTreeNode != null) {
			rootSetTreeNode.setSelected(true);
			selectedPrefabSetTreeNode = rootSetTreeNode;
			scenarioEditorBean.setSelectedPrefabSet(selectedPrefabSetTreeNode.getNode());
		}
	}

	private PrefabSetTreeNode getTreeNodeOfRootPrefabSet() {
		return (PrefabSetTreeNode) rootPrefabSetTreeNode.getChildren().get(0);
	}

	public void setSelectedPrefabSetTreeNode(TreeNode selectedPrefabSet) {
		if (selectedPrefabSet == null) {
			selectedPrefabSetTreeNode = rootPrefabSetTreeNode;
		} else {
			selectedPrefabSetTreeNode = (PrefabSetTreeNode) selectedPrefabSet;
		}
		// expand the hierarchy above the selected node, so it's visible.
		selectedPrefabSetTreeNode.setExpanded(true);
		selectedPrefabSetTreeNode.setSelected(true);
		scenarioEditorBean.setSelectedPrefabSet(selectedPrefabSetTreeNode.getNode());
	}

	public TreeNode getSelectedPrefabSetTreeNode() {
		return selectedPrefabSetTreeNode;
	}

	public PrefabSet getSelectedPrefabSet() {
		return scenarioEditorBean.getSelectedPrefabSet();
	}

	public void refreshPrefabSetsHierachy() {
		PrefabSet selectedPrefabSet = getSelectedPrefabSet();
		TreeNodeExpansionState<PrefabSetTreeNode> state = new TreeNodeExpansionState<>();
		state.saveExpansionStatus(rootPrefabSetTreeNode);
		generatePrefabSetTree();
		PrimeFacesHelpers.forcePrimeFacesToRefreshTree(rootPrefabSetTreeNode);
		state.restoreExpansionStatus(rootPrefabSetTreeNode);
		restoreSelection(selectedPrefabSet);
		viewUpdaterBean.prefabHierarchyChanged();
	}

	public void resetPrefabSetSelection() {
		selectFirstPrefabSet();
	}

	private void restoreSelection(PrefabSet formerSelectedPrefabSet) {
		if (formerSelectedPrefabSet != null) {
			TreeNode selectedTreeNode = prefabSetTreeNodeByIdMap.get(formerSelectedPrefabSet.getId());
			if (selectedTreeNode != null) {
				setSelectedPrefabSetTreeNode((PrefabSetTreeNode) selectedTreeNode);
			}
		}
		// if it was null, nothing was selected at the time.
	}

	public void addNewPrefabToSet() {
		try {
			long sceneNodeId = Long.valueOf(jsfParameters.get("sceneNodeId"));
			long prefabSetId = Long.valueOf(jsfParameters.get("prefabSetId"));
			prefabSetManager.addSceneNodeToPrefabSet(sceneNodeId, prefabSetId);
		} catch (NumberFormatException e) {
			log.error("Invalid id encountered when trying to create a new prefab: " + e);
		} catch (EntityNotFoundException e) {
			log.error("Entity not found while trying to create a new prefab: " + e);
		} catch (IllegalArgumentException e) {
			log.error("Exception while trying to create a new prefab: " + e);
		}
	}

	public void addNewPrefabToSelectedSet() {
		try {
			long sceneNodeId = Long.valueOf(jsfParameters.get("sceneNodeId"));
			SceneNode sceneNode = sceneTree.findById(sceneNodeId);
			addSceneNodeToCurrentlySelectedPrefabSet(sceneNode);
		} catch (NumberFormatException e) {
			log.error("Invalid id encountered when trying to create a new prefab: " + e);
		} catch (EntityNotFoundException e) {
			log.error("Entity not found while trying to create a new prefab: " + e);
		}
	}

	private void addSceneNodeToCurrentlySelectedPrefabSet(SceneNode node) {
		PrefabSet selectedPrefabSet = scenarioEditorBean.getSelectedPrefabSet();
		try {
			prefabSetManager.addSceneNodeToPrefabSet(node, selectedPrefabSet);
		} catch (IllegalArgumentException ex) {
			log.error("Could not add Prefab to PrefabSet: " + ex.getMessage());
		}
	}

	public void movePrefabToPrefabSet() {
		try {
			long sceneNodeId = Long.valueOf(jsfParameters.get("sceneNodeId"));
			long prefabSetId = Long.valueOf(jsfParameters.get("prefabSetId"));
			prefabSetManager.movePrefabToPrefabSet(sceneNodeId, prefabSetId, selectedPrefabSetTreeNode.getNode());
		} catch (NumberFormatException e) {
			log.error("Invalid id encountered when trying to create a new prefab: " + e);
		} catch (EntityNotFoundException e) {
			log.error("Entity not found while trying to create a new prefab: " + e);
		} catch (IllegalArgumentException e) {
			log.error("Exception while trying to create a new prefab: " + e);
		}
	}

	public void reparentPrefabSet() {
		try {
			long movedNodeId = Long.valueOf(jsfParameters.get("movedNodeId"));
			long newParentId = Long.valueOf(jsfParameters.get("newParentId"));
			PrefabSetTreeNode destTreeNode = prefabSetTreeNodeByIdMap.get(newParentId);
			PrefabSetTreeNode movedTreeNode = prefabSetTreeNodeByIdMap.get(movedNodeId);
			if (destTreeNode == null) {
				destTreeNode = rootPrefabSetTreeNode;
			}
			if (destTreeNode.getNode().isChildOf(movedTreeNode.getNode())) {
				showFacesMessageAtMessageTag("error.Editor.Hierarchy.parentOfOwnChild", FacesMessage.SEVERITY_WARN);
				return;
			}
			reparentPrefabSetTreeNode(movedTreeNode, destTreeNode);
			prefabSetManager.save(movedTreeNode.getNode());
		} catch (NumberFormatException e) {
			log.error("Invalid id passed to scene hierarchy reparenting: ", e);
		}
	}

	private void reparentPrefabSetTreeNode(PrefabSetTreeNode movedNode, PrefabSetTreeNode destNode) {
		movedNode.setParent(destNode);
		PrimeFacesHelpers.forcePrimeFacesToRefreshTree(rootPrefabSetTreeNode);
	}

	public void createNewChildPrefabSet() {
		PrefabSet selected = scenarioEditorBean.getSelectedPrefabSet();
		PrefabSet newSet = prefabSetManager.createNewChild(selected);
		generatePrefabSetTreeNode(newSet, selectedPrefabSetTreeNode);
		selectedPrefabSetTreeNode.setExpanded(true);
		PrimeFacesHelpers.forcePrimeFacesToRefreshTree(rootPrefabSetTreeNode);
	}

	public boolean rootIsSelected() {
		return selectedPrefabSetTreeNode.equals(getTreeNodeOfRootPrefabSet());
	}

	public void deleteSelectedPrefabSet() {
		PrefabSet selected = scenarioEditorBean.getSelectedPrefabSet();
		detachSelectedPrefabSetTreeNode();
		prefabSetManager.cleanupPrefabs(selected);
		if (!isRootPrefabSet(selected)) {
			try {
				prefabSetManager.remove(selected);
			} catch (EntityNotFoundException ex) {
				log.error("Selected PrefabSet seems to have been removed already. Possible race condition?");
			}
		}
		selectFirstPrefabSet();
		PrimeFacesHelpers.forcePrimeFacesToRefreshTree(rootPrefabSetTreeNode);
	}

	private void detachSelectedPrefabSetTreeNode() {
		selectedPrefabSetTreeNode.setSelected(false);
		if (!selectedPrefabSetTreeNode.equals(getTreeNodeOfRootPrefabSet())) {
			selectedPrefabSetTreeNode.setParent(null);
		}
	}

	private boolean isRootPrefabSet(PrefabSet candidate) {
		return candidate.equals(getRootPrefabSet());
	}

	public void createNewPrefabFromSelectedSceneNode() {
		SceneNode sceneNode = scenarioEditorBean.getSelectedSceneNode();
		addSceneNodeToCurrentlySelectedPrefabSet(sceneNode);
	}
}
