/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.backingbeans;

import de.dfki.asr.compass.model.Hierarchy;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.model.TreeNode;

/**
 * Base class for entities, that are to be managed in a PrimeFaces tree. It will be a proxy for the underlying entity
 * and forward e.g. reparenting calls to it.
 *
 * @param <NodeType> underlying entity type
 */
public abstract class UITreeNode<NodeType extends Hierarchy<NodeType>> implements TreeNode {

	private NodeType node;
	private List<TreeNode> children;
	private UITreeNode<NodeType> parent;
	private boolean expanded;
	private boolean selected;
	private String rowKey;

	public UITreeNode(NodeType node) {
		this.node = node;
		children = new ArrayList();
	}

	public NodeType getNode() {
		return node;
	}

	@Override
	public Object getData() {
		return node;
	}

	public void setData(NodeType node) {
		this.node = node;
	}

	@Override
	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public void setParent(TreeNode newParent) {
		if (parent != null) {
			parent.removeChild(this);
		}
		parent = (UITreeNode) newParent;
		if (parent == null) {
			node.setParent(null);
		} else {
			parent.addChild(this);
			node.setParent(parent.getNode());
		}
	}

	private void addChild(UITreeNode treeNode) {
		// Primefaces may have already inserted the node, eg. during a drag/drop event
		if (!children.contains(treeNode)) {
			children.add(treeNode);
		}
	}

	private void removeChild(UITreeNode treeNode) {
		children.remove(treeNode);
	}

	@Override
	public boolean isExpanded() {
		return expanded;
	}

	@Override
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
		if (parent != null) {
			// use case: drag&drop an expanded child on an unexpanded parent,
			// expect: parent also expanded to show previously expanded child
			parent.setExpanded(expanded);
		}
	}

	public void setExpandedHere(boolean expanded) {
		this.expanded = expanded;
	}

	@Override
	public int getChildCount() {
		return children.size();
	}

	@Override
	public boolean isLeaf() {
		if (children == null) {
			return true;
		}
		return children.isEmpty();
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public boolean isSelectable() {
		return true;
	}

	@Override
	public void setSelectable(boolean bln) {
	}

	@Override
	public boolean isPartialSelected() {
		return false;
	}

	@Override
	public void setPartialSelected(boolean bln) {
		// ignore partial selection
	}

	@Override
	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
	}

	@Override
	public String getRowKey() {
		return rowKey;
	}

	@Override
	public String toString() {
		if (node == null) {
			return "null";
		} else {
			return node.toString();
		}
	}
}
