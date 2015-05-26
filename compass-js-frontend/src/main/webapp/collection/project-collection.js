/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var AmpersandRestCollection = require("ampersand-rest-collection");
var Project = require("./../model/project");

var ProjectCollection = AmpersandRestCollection.extend({
	model: Project,
	selectedProject: undefined,
	ajaxConfig: {
		headers: {
			"Accept": "application/json"
		}
	},
	url: "http://localhost:8080/compass/resources/restv1/projects/",
	selectById: function(id){
		var newSelection = this.get(id);
		if (newSelection === this.selectedProject) {
			return;
		}
		var project = this.selectedProject = newSelection;
		project.scenarios.each(function(s){
			s.fetch({
				success: function(s){
					//the events do not bubble from scenarioCollection (child) to project (parent)
					//see: http://ampersandjs.com/docs#ampersand-state-collections
					//thus we trigger them here...
					project.trigger("change:scenarios", project);
				}
			});
		});
		this.trigger("change:selected");
	},
	isSelected: function(model) {
		return this.selectedProject && this.selectedProject === model;
	},
	getSelected: function() {
		return this.selectedProject;
	}
});

module.exports = ProjectCollection;
