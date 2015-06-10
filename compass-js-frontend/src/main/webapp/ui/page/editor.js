/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var app = require('ampersand-app');
var Scenario = require('../../model/scenario');
var SceneNode = require("../../model/scenenode");
var template = require('../templates/editorpage.html');
var BasePage = require('./basepage');
var riot = require("riot");

var EditorPage = BasePage.extend({
	pageTitle: 'Editor',
	template: template,
	scenario: undefined,
	root: undefined,
	initialize: function (scenarioId, options) {
		//The router gives as a string, but the model wants a number...
		var idAsNumber = parseInt(scenarioId);
		this.scenario = new Scenario({id: idAsNumber});
		this.fetchData();
	},
	render: function () {
		this.renderWithTemplate();
		return this;
	},
	initUI: function (rootNode) {
		this.root = rootNode;
		console.log("Editor fetched scenario: " + this.scenario.name);
		console.log("Editor fetched root node: " + this.root.name);
	},
	fetchData: function(){
		this.scenario.fetch()
				.then(this.fetchSceneNodeTree.bind(this))
				.then(this.initUI.bind(this));
	},
	fetchSceneNodeTree: function(){
		var rootId = this.scenario.root;
		var promise = new Promise(function(resolve, reject) {
			var rootNode = new SceneNode({id: rootId});
			rootNode.fetch()
					.then(rootNode.fetchRecursively.bind(rootNode))
					.then(function() {
							resolve(rootNode);
						},
						function() { reject(); }
					);
		});
		return promise;
	}
});
module.exports = EditorPage;
