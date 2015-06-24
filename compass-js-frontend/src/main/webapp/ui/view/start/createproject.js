/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var $ = global.jQuery;
var app = require("ampersand-app");
var AmpersandView = require("ampersand-view");
var template = require("../../templates/start/create-project.html");

var CreateProjectView = AmpersandView.extend({
	template: template,
	initialize: function () {
	},
	events: {
		"click [data-action=save]": "createNewProject",
		"click [data-action=close]": "close"
	},
	render: function () {
		this.renderWithTemplate();
		$(this.el).modal("show");
		$(this.el).on("shown.bs.modal", function() {
			$("#new-project-name").focus();
		});
		return this;
	},
	createNewProject: function () {
		var newName = this.el.querySelector("#new-project-name").value;
		app.projects.create({
			name: newName
		}, {
			wait: true,
			success: this.close.bind(this),
			error: function (model, response) {
				$("#new-project-name-error-msg").text(response.body);
				$("#new-project-name-error").show().delay("20000").hide("slow");
			}
		});
	},
	close: function() {
		var self = this;
		$(this.el).modal("hide").on("hidden.bs.modal", function() {
			self.remove();
		});
	}
});

module.exports = CreateProjectView;
