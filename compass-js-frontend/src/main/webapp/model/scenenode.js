/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var CompassModel = require("./compass-model");
var Config = require("../config");
var SceneNodeCollection = require("../collection/scenenode-collection");
var Promise = require("promise");

var SceneNode = CompassModel.extend({
	props: {
		id: "number",
		name: {
			type: "string",
			required: true,
			default: "Scene Node"
		},
		selectable3d: "boolean",
		visible: "boolean"
	},
	session: {
		// does not take part in serialization, only for internal navigation.
		parentNode: {
			type: "object", // SceneNode
			required: true,
			allowNull: true
		}
	},
	collections: {
		children: function () {
			// needs level of indirection to avoid circular require()
			return new SceneNodeCollection([], {model: SceneNode});
		}
	},
	init: function() {
	},
	parse: function(attrs){
		if(!attrs){
			return attrs;
		}
		if(attrs.children){
			var children = attrs.children;
			for(var index in children){
				var id = children[index];
				children[index] = {id: id, parentNode: this};
			}
		}
		return attrs;
	},
	url: function() {
		var basePath = Config.getRESTPath("scenenodes/");
		if (!this.id) {
			// must be a new node for POSTing
			if (!this.parentNode) {
				throw new Error("Cannot construct URL for this node. Need either id or parentNode.");
			}
			return basePath+this.parentNode.id+"/children/";
		} else {
			// has an id, so we might as well...
			return basePath+this.id;
		}
	},
	fetchRecursively: function(){
		if(this.children.isEmpty()){
			return Promise.resolve();
		}
		var self = this;
		return this.children.fetchCollectionEntries().then(function(){
			var promises = [];
			self.children.each(function(c){
				var promise = c.fetchRecursively().catch(function(err){
				console.log(err);
			});
				promises.push(promise);
			});
			return Promise.all(promises);
		});
	}
});

module.exports = SceneNode;