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
	selectNodeById: function(nodeId){
		console.log("getting or fetching node with id " +nodeId);
		this.fetchById(nodeId, function(err, model) {
			if(err){
				console.log("Could not fetch tree for id "+ nodeId +":"+ err);
				return;
			}
			else {
				console.log("Successfully fetched" + model);
				var children = model.children;
				for(c in children){
						app.scenenodes.fetchNodeTree(children[c].id);
					}
				}
			});
	},
	fetchNodeTree: function(rootNode) {
		console.log("Trying to load tree for node " + rootNode);
		var fetchedNode = this.selectNodeById(parseInt(rootNode));
		console.log(fetchedNode);
		if(!fetchedNode) {
			console.log("Something went wrong while fetching the node");
			return;
		}
		var childNodes = fetchedNode.childNodes;
		console.log("Child nodes: " + childNodes);
		if(!childNodes){
			return;
		}
		console.log("iterating over child nodes...");
		childNodes.each({
			success: function(n){
				SceneNodeCollection.fetchNodeTree(n);
			}
		});
	}
});
module.exports = SceneNodeCollection;

//SceneNodeCollection.prototype.model = SceneNode;