/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var AmpersandRestCollection = require("ampersand-rest-collection");
var Config = require("../config");
var SceneNode = require("../model/scenenode");
var SceneNodeCollection = AmpersandRestCollection.extend({
	url: Config.getRESTPath("scenenodes/"),
	model: SceneNode,

	getById: function(id){
		var newSceneNode = this.getOrFetch(id);
		return newSceneNode;
	},
	fetchNodeTree: function(rootNode) {
		console.log("Trying to load tree for node " + rootNode);
		var fetchedNode = this.getById(rootNode);
		console.log(fetchedNode);
		this.getById(rootNode).children.each({
			success: function(n){
				SceneNodeCollection.fetchNodeTree(n.id);
			}
		});
	}
});
module.exports = SceneNodeCollection;

//SceneNodeCollection.prototype.model = SceneNode;