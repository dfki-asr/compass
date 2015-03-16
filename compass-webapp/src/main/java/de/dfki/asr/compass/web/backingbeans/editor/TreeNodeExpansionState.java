/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.backingbeans.editor;

import de.dfki.asr.compass.web.backingbeans.UITreeNode;
import de.dfki.asr.compass.model.AbstractCompassEntity;
import java.util.HashSet;
import java.util.Set;
import org.primefaces.model.TreeNode;


public class TreeNodeExpansionState<TreeNodeType extends UITreeNode<? extends AbstractCompassEntity>> {
	private Set<Long> savedExpandedNodes;

	public TreeNodeExpansionState() {
	}

	public void saveExpansionStatus(TreeNodeType root) {
		savedExpandedNodes = new HashSet<>();
		saveExansionForNode(root);
	}

	private void saveExansionForNode(TreeNodeType treeNode) {
		if (treeNode.isExpanded()) {
			AbstractCompassEntity entity = treeNode.getNode();
			// the virtual root node doesn't have an entity.
			// therefore:
			if (entity != null) {
				Long sceneNodeId = entity.getId();
				savedExpandedNodes.add(sceneNodeId);
			}
		}
		for (TreeNode child : treeNode.getChildren()) {
			saveExansionForNode((TreeNodeType) child);
		}
	}

	public void restoreExpansionStatus(TreeNodeType treeNode) {
		if (savedExpandedNodes == null) {
			return; // no expansion saved
		}
		if (shouldExpandTreeNode(treeNode)) {
			// will expand parents
			treeNode.setExpandedHere(true);
		} else {
			// will leave parents' expansion state alone
			treeNode.setExpandedHere(false);
		}
		for (TreeNode child : treeNode.getChildren()) {
			restoreExpansionStatus((TreeNodeType) child);
		}
	}

	private boolean shouldExpandTreeNode(TreeNodeType treeNode) {
		AbstractCompassEntity sceneNode = treeNode.getNode();
		if (sceneNode == null) {
			// root node, epxand
			return true;
		}
		boolean wasPreviouslyExpanded = savedExpandedNodes.contains(sceneNode.getId());
		boolean hasChildren = treeNode.getChildCount() > 0;
		// no need to retain expanded state if the node became a leaf node.
		// only leads to the "suprising" behaviour that leaves which have
		// children added to them come pre-expanded.
		return hasChildren && wasPreviouslyExpanded;
	}
}
