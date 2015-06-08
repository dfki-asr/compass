/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var app = require('ampersand-app');
var AmpersandRestCollection = require("ampersand-rest-collection");
var Config = require("../config");
var SceneNode = require("../model/scenenode");
var SceneNodeCollection = AmpersandRestCollection.extend({
	url: Config.getRESTPath("scenenodes/"),
	model: SceneNode,
	fetchNodeTree: function(nodeId){
		console.log("getting or fetching node with id " +nodeId);
		this.getOrFetch(nodeId, function(err, model) {
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
});
module.exports = SceneNodeCollection;
