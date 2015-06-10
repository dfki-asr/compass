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
var SceneNodeCollection = require("../collection/scenenode-collection");

var SceneNode = CompassModel.extend({
	props: {
		id: "number",
		name: {
			type: "string",
			required: true,
			default: "Scene Node"
		},
		parent: "number",
		selectable3d: "boolean",
		visible: "boolean"
	},
	collections: {
		components: ComponentCollection,
		children: SceneNodeCollection
	},
	init: function() {
	},
	parse: function(attrs){
		if(!attrs){
			return attrs;
		}
		console.log("Parsing sceneNode "+attrs.id);
		if(attrs.children){
			}
		}
		return attrs;
	},
	urlRoot: Config.getRESTPath("scenenodes/")
});

module.exports = SceneNode;