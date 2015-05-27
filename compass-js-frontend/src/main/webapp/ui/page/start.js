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
var _ = require('lodash');
require("../tags/list-selection.tag");

var StartPage = BasePage.extend({
	pageTitle: 'Start Page',
	template: template,
	projectSelectionList: undefined,
	scenarioSelectionList: undefined,
	initialize: function (options) {
		app.projects.on("sync", this.renderScenarioList.bind(this));
		app.projects.on("change:selected", this.renderScenarioList.bind(this));
		app.projects.on("change:scenarios", this.renderScenarioList.bind(this));
	},
	onProjectEntryClick: function(event) {
		// curiously, item is double-wrapped.
		app.projects.selectById(event.item.item.id);
	},
	onScenarioEntryClick: function(event) {
		// nothing yet
	},
	events:{
		"click #openscenariobutton" : "openScenario"
	},
	render: function () {
		this.renderWithTemplate();
		this.projectSelectionList = riot.mount(
			this.el.querySelector("#projectselection"), {
			collection: app.projects,
			clickHandler: this.onProjectEntryClick.bind(this)
		});
		this.scenarioSelectionList = riot.mount(
			this.el.querySelector("#scenarioselection"), {
			clickHandler: this.onScenarioEntryClick.bind(this)
		});
		return this;
	},
	renderScenarioList: function (project) {
		_.each(this.projectSelectionList, function (tag) {
			tag.update(); // just the selection
		});
		var selectedProject = app.projects.getSelected();
		if (selectedProject) {
			_.each(this.scenarioSelectionList, function(tag) {
				// why do I need opts here?
				// isn't this the equivalent of React's setState()?
				tag.update({opts:{collection: selectedProject.scenarios}});
			});
		}
	},
	openScenario: function(){
		app.router.redirectTo("/editor");
	}
});
module.exports = StartPage;
