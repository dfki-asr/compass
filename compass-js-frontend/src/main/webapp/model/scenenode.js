/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var CompassModel = require("./compass-model");
var Config = require("../config");
var CompassError = require("../compass-error");
var SceneNodeCollection = require("../collection/scenenode-collection");
var ScenenodeComponentCollection = require("../collection/scenenode-component-collection");
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
	extraProperties: "allow",
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
		},
		components: ScenenodeComponentCollection
	},
	init: function () {
	},
	parse: function (attrs) {
		if (!attrs) {
			return attrs;
		}
		if (attrs.id !== undefined && attrs.id === 0) {
			throw new CompassError("id 0 is reserved");
		}
		if (attrs.children) {
			var children = attrs.children;
			for (var index in children) {
				var id = children[index];
				children[index] = {id: id, parentNode: this};
			}
		}
		if (attrs.components) {
			var components = attrs.components;
			for (index in components) {
				components[index].owner = this;
			}
		}
		return attrs;
	},
	url: function () {
		var basePath = Config.getRESTPath("scenenodes/");
		if (!this.id) {
			// must be a new node for POSTing
			if (!(this.parentNode instanceof SceneNode)) {
				throw new CompassError("Cannot construct URL for this node. Need either id or parentNode.");
			}
			return basePath + this.parentNode.id + "/children/";
		} else {
			// has an id, so we might as well...
			return basePath + this.id;
		}
	},
	fetchRecursively: function () {
		var outerPromises = [];
		if (this.components.isEmpty()) {
			outerPromises.push(Promise.resolve());
		} else {
			var componentPromise = this.components.fetchCollectionEntries();
			outerPromises.push(componentPromise);
		}
		if (this.children.isEmpty()) {
			outerPromises.push(Promise.resolve());
		}
		var self = this;
		var fetchPromises = this.children.fetchCollectionEntries().then(function () {
			var promises = [];
			self.children.each(function (c) {
				var promise = c.fetchRecursively().catch(function (err) {
				console.log(err);
			});
				promises.push(promise);
			});
			return Promise.all(promises);
		});
		outerPromises.push(fetchPromises);
		return Promise.all(outerPromises);
	}
});

module.exports = SceneNode;
