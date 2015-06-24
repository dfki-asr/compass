/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var CompassCollection = require("./compass-collection");
var Project = require("../model/project");
var Config = require("../config");

var ProjectCollection = CompassCollection.extend({
	model: Project,
	selectedProject: undefined,
	indexes: ["name"],
	url: Config.getRESTPath("projects/"),
	selectById: function(id) {
		var newSelection = this.get(id);
		if (newSelection === this.selectedProject) {
			return;
		}
		this.fetchScenarios(newSelection);
		this.trigger("change:selected");
	},
	selectByName: function(name) {
		var newSelection = this.get(name, "name");
		if (newSelection === this.selectedProject) {
			return;
		}
		this.fetchScenarios(newSelection);
		this.trigger("change:selected");
	},
	fetchScenarios: function(newSelection) {
		var project = this.selectedProject = newSelection;
		// the events do not bubble from scenarioCollection (child) to project (parent)
		// see: http://ampersandjs.com/docs#ampersand-state-collections
		project.scenarios.fetchCollectionEntries().then(function() {
			project.trigger("change:scenarios", project);
		});
	},
	isSelected: function(model) {
		return this.selectedProject && this.selectedProject === model;
	},
	getSelected: function() {
		return this.selectedProject;
	}
});

module.exports = ProjectCollection;
