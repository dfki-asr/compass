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
		this.tree = $(this.query(".tree")).fancytree({
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
			click: this.handleClickOnNode.bind(this)
		});
	},
	handleClickOnNode: function(event, data){
		if(data.targetType === "expander") {
			return;
		}
		var selectedNode = $.ui.fancytree.getNode(event.originalEvent);
		var sceneNode = this.getSceneNodeByFancyNode(selectedNode);
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
		return fancyNode;
	},
	lazyloadFancyNodes: function(event, data){
		var fancyNode = data.node;
		var sceneNode = this.getSceneNodeByFancyNode(fancyNode);
		data.result = this.createFancyTreeStructure(sceneNode);
	},
	getSceneNodeByFancyNode: function(node){
		var pathToNode = this.createIdPathToSceneNode(node);
		return this.getSceneNodeFromIdPath(pathToNode);
	},
	createIdPathToSceneNode: function(fancyNode){
		var path = [];
		while(fancyNode.parent){
			path.unshift(fancyNode.key);
			fancyNode = fancyNode.parent;
		};
		return path;
	},
	getSceneNodeFromIdPath: function(path){
		var node = this.parent.root;
		for(var index in path){
			var id = path[index];
			node = node.children.get(id);
		}
		return node;
	},
	newChild: function () {
		console.log("new Node");
		// if a node is currently selected:
		//      create a new child node of it
		// else:
		//      create a new child of the (invisible) root node
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
