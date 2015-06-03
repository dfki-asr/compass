/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var app = require('ampersand-app');
var Scenario = require('../../model/scenario');
var SceneNodeCollection = require('../../collection/scenenode-collection');
var template = require('../templates/editorpage.html');
var BasePage = require('./basepage');
var riot = require("riot");

var EditorPage = BasePage.extend({
	pageTitle: 'Editor',
	template: template,
	scenario: undefined,
	scenenodes: new SceneNodeCollection(),
	initialize: function (scenarioId, options) {
		//The router gives as a string, but the model wants a number...
		var idAsNumber = parseInt(scenarioId);
		this.scenario = new Scenario({id: idAsNumber});
		this.scenario.fetch({
			success: this.initUI.bind(this)
		});
	},
	render: function () {
		this.renderWithTemplate();
		return this;
	},
	initUI: function () {
		console.log("Editor fetched scenario: " + this.scenario.name);
		this.scenenodes.fetchNodeTree(this.scenario.root);
	}
});
module.exports = EditorPage;
