/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var $ = global.jQuery;
var browserSetTimeout = global.setTimeout;
var basicContext = global.basicContext;
var AmpersandView = require("ampersand-view");
var _notify = require("./_notify");
var template = require("../../templates/editor/hierarchy.html");

var HierarchyView = AmpersandView.extend({
	template: template,
	tree: undefined,
	$scrollArea: undefined,
	events: {
		"click [data-hook=action-add-child]": "newChild",
		"click [data-hook=action-delete-selected]": "deleteSelected"
	},
	initialize: function () {
		this.parent.on("sceneTreeLoaded", this.renderTree.bind(this));
		this.parent.on("change:selectedNode", this.updateSelectionDisplay.bind(this));
	},
	render: function () {
		this.renderWithTemplate();
		return this;
	},
	renderTree: function () {
		var fancyTree = this.createFancyTreeStructure(this.parent.root);
		var $tree = $(this.query(".tree")).fancytree({
			extensions: ["glyph"],
			source: fancyTree,
			glyph: {
				map: {
					doc: "fa fa-circle-thin", // scenenode without chidlren
					docOpen: "fa fa-file",
					checkbox: "glyphicon glyphicon-unchecked",
					checkboxSelected: "glyphicon glyphicon-check",
					checkboxUnknown: "glyphicon glyphicon-share",
					error: "glyphicon glyphicon-warning-sign",
					expanderClosed: "fa fa-caret-right",
					expanderLazy: "fa fa-caret-right",
					expanderOpen: "fa fa-caret-down",
					folder: "fa fa-compress", // scenenode with chidlren, not expanded
					folderOpen: "fa fa-expand", // expanded scenenode
					loading: "fa fa-spinner fa-pulse"
				}
			},
			selectMode: 1,
			activate: this.handleClickOnNode.bind(this)
		});
		// what we actually want to save is the internal tree object.
		this.tree = $tree.fancytree("getTree");
		this.$scrollArea = $tree;
		$tree.on("contextmenu", this.showContextMenu.bind(this));
	},
	preventMenuOnBackdrop: function () {
		var $backdrop = $(".basicContextContainer");
		$backdrop.on("contextmenu", function () {
			basicContext.close();
			return false;
		});
	},
	showContextMenu: function (event) {
		var node = $.ui.fancytree.getNode(event);
		this.selectCurrentNode(node);
		var items = [
			{type: "item", title: "Add Node", icon: "fa fa-plus-circle", fn: this.newChild.bind(this)},
			{type: "item", title: "Delete Node", icon: "fa fa-trash-o", fn: this.deleteSelected.bind(this)}
		];
		basicContext.show(items, event);
		this.preventMenuOnBackdrop();
		return false;
	},
	handleClickOnNode: function (event, data) {
		var selectedNode = data.node;
		this.selectCurrentNode(selectedNode);
	},
	selectCurrentNode: function (node) {
		var sceneNode = this.getSceneNodeByFancyNode(node);
		this.parent.selectedNode = sceneNode;
	},
	updateSelectionDisplay: function () {
		if (!!this.parent.selectedNode) {
			var node = this.tree.activateKey(this.parent.selectedNode.cid);
			this.$scrollArea.scrollTo(node.span, 150);
		} else {
			// selection cleared
			this.tree.activateKey(false);
		}
	},
	createFancyTreeStructure: function (scenenode) {
		var fancyTree = [];
		var self = this;
		scenenode.children.each(function (child) {
			fancyTree.push(self.createFancyTreeNode(child));
		});
		return fancyTree;
	},
	createFancyTreeNode: function (scenenode) {
		var fancyNode = {};
		fancyNode.title = scenenode.name;
		fancyNode.key = scenenode.cid;
		if (!scenenode.children.isEmpty()) {
			fancyNode.folder = true;
			fancyNode.children = this.createFancyTreeStructure(scenenode);
		}
		fancyNode.sceneNode = scenenode;
		return fancyNode;
	},
	getSceneNodeByFancyNode: function (fancyNode) {
		return fancyNode.data.sceneNode;
	},
	getFancyNodeBySceneNode: function (sceneNode) {
		return this.tree.getNodeByKey(sceneNode.cid);
	},
	newChild: function (event) {
		if (event) {
			this.blurButtonAfterClickEvent(event);
		}
		var sceneNode = this.parent.selectedNode;
		if (!sceneNode) {
			sceneNode = this.parent.root;
		}
		var newNode = sceneNode.children.add({name: "New Node", parentNode: sceneNode});
		newNode.save().then(function () {
			// success
			// we just expect that, so no need to tell the user anything.
		}, function () {
			// fail
			_notify("danger", "Could not save your new node to the server.");
		});
		this.insertNodeIntoTree(newNode, sceneNode);
		this.parent.selectedNode = newNode;
	},
	blurButtonAfterClickEvent: function (event) {
		if (!event || !event.target) {
			return;
		}
		var $eventTarget = $(event.target);
		var tagName = $eventTarget.prop("tagName");
		if (tagName === "I" || tagName === "i") { // target was the icon not the actual button
			$eventTarget = $eventTarget.parent();
		}
		$eventTarget.blur();
	},
	insertNodeIntoTree: function (sceneNode, parent) {
		var index = sceneNode.collection.indexOf(sceneNode);
		var fancyNode = this.createFancyTreeNode(sceneNode);
		if (index === 0) {
			var fancyParent;
			if (parent.parentNode) {
				fancyParent = this.getFancyNodeBySceneNode(parent);
			} else {
				fancyParent = this.tree.getRootNode();
			}
			fancyParent.folder = true;
			fancyParent.addNode(fancyNode, "child");
		} else {
			var predecessor = sceneNode.collection.at(index - 1);
			var fancyPredecessor = this.getFancyNodeBySceneNode(predecessor);
			fancyPredecessor.addNode(fancyNode, "after");
		}
	},
	deleteSelected: function (event) {
		if (event) {
			this.blurButtonAfterClickEvent(event);
		}
		var selectedNode = this.parent.selectedNode;
		var $node = $(this.getFancyNodeBySceneNode(selectedNode).span).find(".fancytree-title");
		this.$scrollArea.scrollTo($node, 150, {
			onAfter: this.triggerConfirmationPopover.bind(this, selectedNode, $node)
		});
	},
	triggerConfirmationPopover: function (nodeToDelete, $titleSpan) {
		$titleSpan.confirmation({
			trigger: "manual",
			placement: "right",
			container: "body",
			popout: false,
			title: "Really delete this?",
			btnOkIcon: "fa fa-trash-o",
			btnOkLabel: "Delete",
			btnCancelIcon: "",
			btnCancelLabel: "Keep",
			onConfirm: this.deleteNodeOnServer.bind(this, nodeToDelete, $titleSpan)
		}).confirmation("show");
		// due to event race condition, leave a little longer to capture scrolls.
		var $scroll = this.$scrollArea;
		browserSetTimeout(function () {
			$scroll.one("scroll", function () { $titleSpan.confirmation("destroy");});
		}, 10); // In my tests, the lagging scroll event took at most 5msec to complete.
	},
	deleteNodeOnServer: function (nodeToDelete, $titleSpan) {
		var self = this;
		nodeToDelete.destroy().then(function () {
			// successfully deleted the node, remove it from the tree
			self.removeFancyNodeBySceneNode(nodeToDelete);
			self.parent.selectedNode = undefined;
		}, function () {
			_notify("danger", "Could not delete node '" + nodeToDelete.name + "' from the server.");
		});
		$titleSpan.confirmation("destroy");
	},
	removeFancyNodeBySceneNode: function (sceneNode) {
		var fancyNode = this.getFancyNodeBySceneNode(sceneNode);
		var fancyNodeParent = fancyNode.getParent();
		fancyNode.remove();
		if (fancyNodeParent && !fancyNodeParent.hasChildren() && !fancyNodeParent.isRoot()) {
			fancyNodeParent.folder = false;
			fancyNodeParent.setExpanded(false);
		}
	}
});

module.exports = HierarchyView;
