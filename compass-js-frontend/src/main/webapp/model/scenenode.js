/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var CompassModel = require("./compassmodel");
var Config = require('../config');
var ComponentCollection = require("../collection/component-collection");

var SceneNode = CompassModel.extend({
	props: {
		id: "number",
		name: {
			type: "string",
			required: true,
			default: "Scene Node"
		},
		children: {
			childNodes: SceneNode
		},
		parent: "number",
		selectable3d: "boolean",
		visible: "boolean"
	},
	collections: {
		components: ComponentCollection
	},
	init: function() {
		console.log("initializing SceneNode");
		this.childNodes = new Array();
	},
	parse: function(attrs){
		if(!this.childNodes){
			this.childNodes = new Array();
		}
		console.log("Parsing sceneNode "+this.id);
		if(!attrs){
			return attrs;
		}
		if(attrs.children){
			for(var c in attrs.children){
				var currentChild = attrs.children[c];
				this.childNodes.push(app.scenenodes.getOrFetch(currentChild));
			}
		}
		return attrs;
	},
	urlRoot: Config.getRESTPath("scenenodes/")
});

module.exports = SceneNode;