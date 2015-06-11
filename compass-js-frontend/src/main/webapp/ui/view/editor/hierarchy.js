/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var $ = global.jQuery;
var app = require('ampersand-app');
var AmpersandView = require('ampersand-view');
var template = require('../../templates/editor/hierarchy.html');

var HierarchyView = AmpersandView.extend({
	template: template,
	tree: undefined,
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
			}
		});
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
			fancyNode.children = this.createFancyTreeStructure(scenenode);
		}
		return fancyNode;
	}
});

module.exports = HierarchyView;