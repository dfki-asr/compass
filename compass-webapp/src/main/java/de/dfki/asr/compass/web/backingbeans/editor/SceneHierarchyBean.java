/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.backingbeans.editor;

import de.dfki.asr.compass.business.api.ComponentManager;
import de.dfki.asr.compass.business.api.SceneTreeManager;
import de.dfki.asr.compass.web.backingbeans.CompassBean;
import de.dfki.asr.compass.web.backingbeans.PrimeFacesHelpers;
import de.dfki.asr.compass.web.backingbeans.project.OpenScenarioBean;
import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.math.Vector3f;
import de.dfki.asr.compass.model.SceneNode;
import de.dfki.asr.compass.web.util.JSFParameterMap;
import de.dfki.asr.compass.model.SceneNodeComponent;
import de.dfki.asr.compass.model.components.annotations.CompassComponent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.jboss.logging.Logger;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.model.TreeNode;

@ViewAccessScoped
@Named
public class SceneHierarchyBean extends CompassBean implements Serializable {

	private static final long serialVersionUID = -244275266632609086L;

	@Inject
	private Logger log;

	@Inject
	private SceneTreeManager sceneTree;

	@Inject
	private ComponentManager componentManager;

	@Inject
	private OpenScenarioBean openScenarioBean;

	@Inject
	private ScenarioEditorBean scenarioEditorBean;

	@Inject
	private JSFParameterMap jsfParameters;

	@Inject
	private ViewUpdaterBean viewUpdaterBean;

	/**
	 * To be on the safe side always use setSelectedSceneNodeTreeNode() to write to selectedSceneNodeTreeNode.
	 */
	private SceneNodeTreeNode selectedSceneNodeTreeNode;
	private SceneNodeTreeNode rootSceneNodeTreeNode;
	private Map<Long, TreeNode> treeNodeByIdMap;

	public TreeNode getRootSceneNodeTreeNode() {
		if (rootSceneNodeTreeNode == null) {
			generateSceneNodeTree();
		}
		return rootSceneNodeTreeNode;
	}

	public boolean isRootSceneNodeSelected() {
		return getRootSceneNode().equals(getSelectedSceneNode());
	}

	public TreeNode getSelectedSceneNodeTreeNode() {
		return selectedSceneNodeTreeNode;
	}

	public void setSelectedSceneNodeTreeNode(TreeNode selectedNode) {
		if (selectedSceneNodeTreeNode != null) {
			// Primefaces doesn't deselect automatically if the selection is changed on the bean side
			selectedSceneNodeTreeNode.setSelected(false);
		}
		if (selectedNode == null) {
			selectedSceneNodeTreeNode = rootSceneNodeTreeNode;
		} else {
			selectedSceneNodeTreeNode = (SceneNodeTreeNode) selectedNode;
		}
		// according to stackoverflow we have to manually select it, too
		// see http://stackoverflow.com/questions/10597134/primefaces-tree-component-setting-selected-node-from-managed-bean
		selectedSceneNodeTreeNode.setSelected(true);
		// and expand the scene tree, so selection is visible;
		if (selectedSceneNodeTreeNode.getParent() != null) {
			selectedSceneNodeTreeNode.getParent().setExpanded(true);
		}
	}

	public SceneNode getSelectedSceneNode() {
		return selectedSceneNodeTreeNode.getNode();
	}

	public SceneNode getRootSceneNode() {
		return (SceneNode) getRootSceneNodeTreeNode().getData();
	}

	private void generateSceneNodeTree() {
		if (openScenarioBean.getSelectedScenario() == null) {
			throw new IllegalStateException("No scenario to be edited.");
		}
		SceneNode rootSceneNode = openScenarioBean.getSelectedScenario().getRoot();
		if (rootSceneNode == null) {
			throw new IllegalStateException("Scenario to be edited has no root node.");
		}
		treeNodeByIdMap = new HashMap<>();
		rootSceneNodeTreeNode = new SceneNodeTreeNode(rootSceneNode);
		treeNodeByIdMap.put(rootSceneNode.getId(), rootSceneNodeTreeNode);
		generateSceneNodeTreeNodeChildren(rootSceneNodeTreeNode);
		selectedSceneNodeTreeNode = rootSceneNodeTreeNode;
		log.info("generateSceneNodeTree");
	}

	private void generateSceneNodeTreeNode(SceneNode sceneNode, TreeNode parent) {
		if (sceneNode == null) {
			return;
		}
		SceneNodeTreeNode newNode = new SceneNodeTreeNode(sceneNode);
		newNode.setParent(parent);
		treeNodeByIdMap.put(sceneNode.getId(), newNode);
		generateSceneNodeTreeNodeChildren(newNode);
	}

	private void generateSceneNodeTreeNodeChildren(SceneNodeTreeNode sceneTreeNode) {
		for (SceneNode child : sceneTreeNode.getNode().getChildren()) {
			generateSceneNodeTreeNode(child, sceneTreeNode);
		}
	}

	public void deleteSelectedSceneNode() {
		/* Send this event before any modifications to the to-be-deleted node are done.
		 * All attributes such as components, or the parent node in particular, should
		 * be still accessible.
		 */
		SceneNode selected = (SceneNode) selectedSceneNodeTreeNode.getData();
		treeNodeByIdMap.remove(selected.getId());
		selectedSceneNodeTreeNode.setParent(null);
		try {
			sceneTree.remove(selected);
		} catch (EntityNotFoundException ex) {
			log.error("Selected Scene node already deleted. Possible Race condition?");
		}
		scenarioEditorBean.setSelectedSceneNode(null);
		PrimeFacesHelpers.forcePrimeFacesToRefreshTree(rootSceneNodeTreeNode);
		viewUpdaterBean.sceneNodeHierarchyChanged();
	}

	public void refreshSceneTreeView() {
		TreeNodeExpansionState<SceneNodeTreeNode> state = new TreeNodeExpansionState<>();
		state.saveExpansionStatus(rootSceneNodeTreeNode);
		generateSceneNodeTree();
		PrimeFacesHelpers.forcePrimeFacesToRefreshTree(rootSceneNodeTreeNode);
		state.restoreExpansionStatus(rootSceneNodeTreeNode);
		restoreSelection();
		viewUpdaterBean.sceneNodeHierarchyChanged();
	}

	public void nodeCollapsed(NodeCollapseEvent event) {
		// update server state with client state.
		// it's horrible enough that one actually has to do this.
		((SceneNodeTreeNode) event.getTreeNode()).setExpandedHere(false);
	}

	public void duplicateSelectedSceneNode() {
		SceneNode node = selectedSceneNodeTreeNode.getNode();
		SceneNode copy = sceneTree.duplicateNode(node);
		generateSceneNodeTreeNode(copy, selectedSceneNodeTreeNode.getParent());
		PrimeFacesHelpers.forcePrimeFacesToRefreshTree(rootSceneNodeTreeNode);
	}

	public void createNewChildSceneNode() {
		createNewSceneNode(selectedSceneNodeTreeNode);
	}

	public void createNewSiblingSceneNode() {
		createNewSceneNode(selectedSceneNodeTreeNode.getParent());
	}

	private void createNewSceneNode(TreeNode parent) {
		if (parent == null) {
			parent = rootSceneNodeTreeNode;
		}
		SceneNode parentSceneNode = (SceneNode) parent.getData();
		SceneNode newSceneNode = sceneTree.createNewChild(parentSceneNode);
		generateSceneNodeTreeNode(newSceneNode, parent);
		PrimeFacesHelpers.forcePrimeFacesToRefreshTree(rootSceneNodeTreeNode);
	}

	public void addNewSceneNodeFromPrefab(SceneNode prefab, SceneNode parent, Vector3f translationOffset) {
		SceneNode prefabInstance = sceneTree.addPrefabInstance(prefab, parent);
		addTransformOffset(prefabInstance, translationOffset);
		sceneTree.save(prefabInstance);
		generateSceneNodeTreeNode(prefabInstance, treeNodeByIdMap.get(parent.getId()));
		PrimeFacesHelpers.forcePrimeFacesToRefreshTree(rootSceneNodeTreeNode);
	}

	private void addTransformOffset(SceneNode node, Vector3f translationOffset) {
		translationOffset.add(node.getLocalTranslation());
		node.setLocalTranslation(translationOffset);
	}

	public void handlePrefabDragDrop() {
		try {
			long prefabId = Long.valueOf(jsfParameters.get("prefabId"));
			SceneNode prefab = sceneTree.findById(prefabId);
			long sceneNodeId = Long.valueOf(jsfParameters.get("sceneNodeId"));
			SceneNode parent;
			if (sceneNodeId < 0) {
				parent = (SceneNode) rootSceneNodeTreeNode.getData();
			} else {
				parent = sceneTree.findById(sceneNodeId);
			}
			addNewSceneNodeFromPrefab(prefab, parent, new Vector3f());
			viewUpdaterBean.sceneNodeHierarchyChanged();
		} catch (NumberFormatException e) {
			log.error("Invalid id passed to scene hierarchy prefab drag&drop handler: " + e);
		} catch (EntityNotFoundException e) {
			log.error("Prefab not found: " + e);
		}
	}

	public void reparentSceneNode() {
		try {
			long movedSceneNodeId = Long.valueOf(jsfParameters.get("movedSceneNodeId"));
			long newParentId = Long.valueOf(jsfParameters.get("newParentId"));
			SceneNodeTreeNode destTreeNode = (SceneNodeTreeNode) treeNodeByIdMap.get(newParentId);
			SceneNodeTreeNode movedTreeNode = (SceneNodeTreeNode) treeNodeByIdMap.get(movedSceneNodeId);
			if (destTreeNode == null) {
				destTreeNode = rootSceneNodeTreeNode;
			}
			if (destTreeNode.getNode().isChildOf(movedTreeNode.getNode())) {
				showFacesMessageAtMessageTag("error.Editor.Hierarchy.parentOfOwnChild", FacesMessage.SEVERITY_WARN);
				return;
			}
			SceneNodeTreeNode sourceTreeNode = (SceneNodeTreeNode) movedTreeNode.getParent();
			reparentSceneNodeTreeNode(movedTreeNode, sourceTreeNode, destTreeNode);
			viewUpdaterBean.sceneNodeHierarchyChanged();
		} catch (NumberFormatException e) {
			log.error("Invalid id passed to scene hierarchy reparenting: " + e);
		}
	}

	private void reparentSceneNodeTreeNode(SceneNodeTreeNode movedNode, SceneNodeTreeNode sourceNode, SceneNodeTreeNode destNode) {
		sceneTree.reparentNode(movedNode.getNode(), destNode.getNode());
		movedNode.setParent(destNode);
		PrimeFacesHelpers.forcePrimeFacesToRefreshTree(rootSceneNodeTreeNode);
	}

	public void moveSelectedSceneNodeUp() {
		List<TreeNode> list = selectedSceneNodeTreeNode.getParent().getChildren();
		if (list.size() < 2) {
			return;
		}
		int index = list.indexOf(selectedSceneNodeTreeNode);
		if (index == 0) {
			return;
		}
		swapTreeNodes(list, index, index - 1);
		swapSceneNodes(index, index - 1);
	}

	private void swapTreeNodes(List<TreeNode> list, int indexA, int indexB) {
		Collections.swap(list, indexA, indexB);
		PrimeFacesHelpers.forcePrimeFacesToRefreshTree(rootSceneNodeTreeNode);
	}

	private void swapSceneNodes(int indexA, int indexB) {
		SceneNode selected = scenarioEditorBean.getSelectedSceneNode();
		sceneTree.swapChildren(selected.getParent(), indexA, indexB);
	}

	public void moveSelectedSceneNodeDown() {
		List<TreeNode> list = selectedSceneNodeTreeNode.getParent().getChildren();
		if (list.size() < 2) {
			return;
		}
		int index = list.indexOf(selectedSceneNodeTreeNode);
		if (index == list.size() - 1) {
			return;
		}
		swapTreeNodes(list, index, index + 1);
		swapSceneNodes(index, index + 1);
	}

	public void onSelectSceneNodeFromHierarchy() {
		String sceneNodeIdString = jsfParameters.get("sceneNodeId");
		try {
			long sceneNodeId = Long.valueOf(sceneNodeIdString);
			SceneNode newSelectedSceneNode = sceneTree.findById(sceneNodeId);
			scenarioEditorBean.setSelectedSceneNode(newSelectedSceneNode);
			viewUpdaterBean.visibleSceneNodeSelected();
		} catch (NumberFormatException e) {
			log.error("Failed to select scene node remotely. The ID cannot be parsed: " + jsfParameters.get("sceneNodeId"), e);
		} catch (EntityNotFoundException ex) {
			log.error("Failed to select scene node remotely with id: " + sceneNodeIdString + ". Scene node not found.", ex);
		}
	}

	public void setSelectedSceneNode(SceneNode selected) {
		if (selected == null) {
			// selection cleared.
			setSelectedSceneNodeTreeNode(rootSceneNodeTreeNode);
			return;
		}
		TreeNode selectedNode = treeNodeByIdMap.get(selected.getId());
		if (selectedNode == null) {
			// a node that is not inside our tree. probably a prefab.
			// there's still a chance the tree is out of date, which would be bad.
			log.warnv("Could not find a tree node for scene node with id {0}. If that's not a prefab, the tree has gone stale.", selected.getId());
			// be that as it may, clear the selection.
			setSelectedSceneNodeTreeNode(rootSceneNodeTreeNode);
			return;
		}
		setSelectedSceneNodeTreeNode(selectedNode);
	}

	public List<String> iconsForNode(SceneNode node) {
		List<String> icons = new ArrayList<>();
		if (node == null) {
			return icons;
		}
		Set<Class<? extends SceneNodeComponent>> components = componentManager.getComponentClassesUsedBySceneNode(node);
		for (Class<? extends SceneNodeComponent> clazz : components) {
			CompassComponent anno = clazz.getAnnotation(CompassComponent.class);
			if (anno != null && !"".equals(anno.icon())) {
				icons.add(anno.icon());
			}
		}
		return icons;
	}

	private void restoreSelection() {
		SceneNode selectedSceneNode = scenarioEditorBean.getSelectedSceneNode();
		if (selectedSceneNode != null) {
			TreeNode selectedTreeNode = treeNodeByIdMap.get(selectedSceneNode.getId());
			if (selectedTreeNode != null) {
				setSelectedSceneNodeTreeNode((SceneNodeTreeNode) selectedTreeNode);
			}
			// if it was null, then a non-hierarchy sceneNode (e.g. a prefab) was selected.
		}
		// if it was null, nothing was selected at the time.
	}
}
