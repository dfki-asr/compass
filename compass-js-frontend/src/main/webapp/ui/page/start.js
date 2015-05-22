/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var app = require('ampersand-app');
var template = require('../templates/startpage.html');
var BasePage = require('./basepage');
var riot = require("riot");
require("../tags/list-selection.tag");

var StartPage = BasePage.extend({
	pageTitle: 'Start Page',
	template: template,
	initialize: function (options) {
		app.projects.on("sync", this.initUI.bind(this));
		app.projects.on("change:selected", this.renderScenarioList.bind(this));
		app.projects.on("change:scenarios", this.renderScenarioList.bind(this));
	},
	render: function () {
		this.renderWithTemplate();
		return this;
	},
	initUI: function () {
		this.initRiot();
	},
	initRiot: function () {
		riot.mount("#projectselection", {
			list: app.projects
		});
	},
	renderScenarioList: function(project){
		if(!project.selected){
			return;
		}
		riot.mount("#scenarioselection", {
			list: project.scenarios
		});
	}
});
module.exports = StartPage;
