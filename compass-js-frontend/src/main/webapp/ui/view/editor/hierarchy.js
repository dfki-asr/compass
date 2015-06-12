/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var $ = global.jQuery;
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
			lazyLoad: this.lazyloadFancyNodes.bind(this),
			selectMode: 1,
			activate: this.handleClickOnNode.bind(this)
		});
		// what we actually want to save is the internal tree object.
		this.tree = $tree.fancytree('getTree');
	},
	handleClickOnNode: function(event, data){
		var selectedNode = data.node;
		var sceneNode = this.getSceneNodeByFancyNode(selectedNode);
		this.parent.selectedNode = sceneNode;
		selectedNode.setSelected();
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
		fancyNode.key = scenenode.id;
		if(!scenenode.children.isEmpty()){
			fancyNode.folder = true;
			fancyNode.lazy = true;
			fancyNode.children = undefined;
		}
		fancyNode.sceneNode = scenenode;
		return fancyNode;
	},
	lazyloadFancyNodes: function(event, data){
		var fancyNode = data.node;
		var sceneNode = this.getSceneNodeByFancyNode(fancyNode);
		data.result = this.createFancyTreeStructure(sceneNode);
	},
	getSceneNodeByFancyNode: function(fancyNode){
		return fancyNode.data.sceneNode;
	},
	getFancyNodeBySceneNode: function(sceneNode){
		return this.tree.getNodeByKey(""+sceneNode.id);
	},
	newChild: function () {
		var sceneNode = this.parent.selectedNode;
		if (!sceneNode) {
			sceneNode = this.parent.root;
		}
		var newNode = sceneNode.children.add({name: "New Node"});
		this.insertNodeIntoTree(newNode, sceneNode);
		this.parent.selectedNode = newNode;
	},
	insertNodeIntoTree: function(sceneNode, parent) {
		var index = sceneNode.collection.indexOf(sceneNode);
		var fancyNode = this.createFancyTreeNode(sceneNode);
		if (index === 0) {
			var fancyParent = this.getFancyNodeBySceneNode(parent);
			fancyParent.addNode(fancyNode, "child");
		} else {
			var predecessor = sceneNode.collection.at(index - 1);
			var fancyPredecessor = this.getFancyNodeBySceneNode(predecessor);
			fancyPredecessor.addNode(fancyNode, "after");
		}
	},
	deleteSelected: function () {
		console.log("delete Node");
		// if a node is currently selected:
		//      let the user confirm he really wants to delete said node.
		//      delete it.
		// else:
		//      (ideally, the button would be disabled via a binding)
		//      complain.
	}
});

module.exports = HierarchyView;
