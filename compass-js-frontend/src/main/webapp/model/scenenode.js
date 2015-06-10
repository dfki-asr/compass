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
var Promise = require('promise');

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
		if(attrs.children){
			var children = attrs.children;
			for(var index in children){
				var id = children[index];
				children[index] = new SceneNode({id: id});
			}
		} else {
			attrs.children = new SceneNodeCollection([]);
		}
		return attrs;
	},
	urlRoot: Config.getRESTPath("scenenodes/"),
	fetchRecursively: function(){
		if(this.children.isEmpty()){
			return Promise.resolve();
		}
		var self = this;
		return this.children.fetchCollectionEntries().then(function(){
			var promises = [];
			self.children.each(function(c){
				var promise = c.fetchRecursively();
				promises.push(promise);
			});
			return Promise.all(promises);
		});
	}
});

module.exports = SceneNode;