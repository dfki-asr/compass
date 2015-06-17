/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var $ = global.jQuery;
var basicContext = global.basicContext;
var AmpersandView = require('ampersand-view');
var template = require('../../templates/editor/hierarchy.html');

var HierarchyView = AmpersandView.extend({
	template: template,
	tree: undefined,
	events: {
		"click [data-hook=action-add-child]": "newChild",
		"click [data-hook=action-delete-selected]": "deleteSelected"
	},
	initialize: function (options) {
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
					doc: "fa fa-circle-thin", //scenenode without chidlren
					docOpen: "fa fa-file",
					checkbox: "glyphicon glyphicon-unchecked",
					checkboxSelected: "glyphicon glyphicon-check",
					checkboxUnknown: "glyphicon glyphicon-share",
					error: "glyphicon glyphicon-warning-sign",
					expanderClosed: "fa fa-caret-right",
					expanderLazy: "fa fa-caret-right",
					expanderOpen: "fa fa-caret-down",
					folder: "fa fa-compress", // scenenode with chidlren, not expanded
					folderOpen: "fa fa-expand", //expanded scenenode
					loading: "fa fa-spinner fa-pulse"
				}
			},
			selectMode: 1,
			activate: this.handleClickOnNode.bind(this),
			removeNode: this.handleNodeRemove.bind(this)
		});
		// what we actually want to save is the internal tree object.
		this.tree = $tree.fancytree('getTree');
		$tree.on('contextmenu', this.showContextMenu.bind(this));
	},
	registerContextOnMenu: function() {
		var $menu = $('.basicContextContainer');
		$menu.on('contextmenu', function() {
			basicContext.close();
			return false;
		});
	},
	showContextMenu: function(event, data){
		var node = $.ui.fancytree.getNode(event);
		this.selectCurrentNode(node);
		var items = [
			{ type: 'item', title: 'Add Node', icon: 'fa fa-plus-circle', fn: this.newChild.bind(this) },
			{ type: 'item', title: 'Delete Node', icon: 'fa fa-trash-o', fn: this.deleteSelected.bind(this) }
		]
		basicContext.show(items,event);
		this.registerContextOnMenu();
		return false;
	},
	handleClickOnNode: function(event, data){
		var selectedNode = data.node;
		this.selectCurrentNode(selectedNode);
	},
	selectCurrentNode: function(node) {
		var sceneNode = this.getSceneNodeByFancyNode(node);
		this.parent.selectedNode = sceneNode;
	},
	updateSelectionDisplay: function() {
		if (!!this.parent.selectedNode) {
			this.tree.activateKey(this.parent.selectedNode.cid);
		}
	},
	createFancyTreeStructure: function(scenenode){
		var fancyTree = [];
		var self = this;
		scenenode.children.each(function(child){
			fancyTree.push(self.createFancyTreeNode(child));
		});
		return fancyTree;
	},
	createFancyTreeNode: function(scenenode){
		var fancyNode = {};
		fancyNode.title = scenenode.name;
		fancyNode.key = scenenode.cid;
		if(!scenenode.children.isEmpty()){
			fancyNode.folder = true;
			fancyNode.children = this.createFancyTreeStructure(scenenode);
		}
		fancyNode.sceneNode = scenenode;
		return fancyNode;
	},
	getSceneNodeByFancyNode: function(fancyNode){
		return fancyNode.data.sceneNode;
	},
	getFancyNodeBySceneNode: function(sceneNode){
		return this.tree.getNodeByKey(sceneNode.cid);
	},
	newChild: function () {
		var sceneNode = this.parent.selectedNode;
		if (!sceneNode) {
			sceneNode = this.parent.root;
		}
		var newNode = sceneNode.children.add({name: "New Node", parentNode: sceneNode});
		newNode.save();
		this.insertNodeIntoTree(newNode, sceneNode);
		this.parent.selectedNode = newNode;
	},
	insertNodeIntoTree: function(sceneNode, parent) {
		var index = sceneNode.collection.indexOf(sceneNode);
		var fancyNode = this.createFancyTreeNode(sceneNode);
		if (index === 0) {
			var fancyParent = this.getFancyNodeBySceneNode(parent);
			fancyParent.folder = true;
			fancyParent.addNode(fancyNode, "child");
		} else {
			var predecessor = sceneNode.collection.at(index - 1);
			var fancyPredecessor = this.getFancyNodeBySceneNode(predecessor);
			fancyPredecessor.addNode(fancyNode, "after");
		}
	},
	deleteSelected: function () {
		console.log("delete Node");
		var selectedNode = this.parent.selectedNode;
		var node = this.getFancyNodeBySceneNode(selectedNode);
		var nodeParent = node.getParent();
		//destroy node in the collection first, as node.remove triggers tree rendering
		selectedNode.destroy();
		//do this after destroy, as otherwise the non-destroyed node will be refetched from the collection
		node.remove();
		if(nodeParent) {
			this.checkFolderStatus(nodeParent);
		}
		this.parent.selectedNode = this.parent.root;
	},
	handleNodeRemove: function(event, data) {
		console.log('Removed node '+data.node);
	},
	checkFolderStatus: function(node) {
		var asSceneNode = this.getSceneNodeByFancyNode(node);
		if(asSceneNode === this.parent.root) {
			return;
		}
		if(!node.hasChildren()) {
			node.folder = false;
			node.setExpanded(false);
		}
	}
});

module.exports = HierarchyView;
