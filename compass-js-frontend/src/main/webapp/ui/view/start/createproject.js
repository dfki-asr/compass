/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var $ = global.jQuery;
var app = require('ampersand-app');
var AmpersandView = require('ampersand-view');
var template = require('../../templates/start/create-project.html');
var _ = require('lodash');

var CreateProjectView = AmpersandView.extend({
	template: template,
	initialize: function (options) {
		this.el = options.el;
	},
	events: {
		"click [data-action=save]": "createNewProject",
		"click [data-action=close]": "close"
	},
	render: function () {
		this.renderWithTemplate();
		//while rendering el gets replaced by the template
		$(this.el).modal("show");
		return this;
	},
	createNewProject: function (event) {
		var newName = this.el.querySelector("#new-project-name").value;
		app.projects.create({
			name: newName
		}, {
			wait: true,
			success: this.close.bind(this),
			error: function (model, response, opts) {
				$("#new-project-name-error-msg").text(response.body);
				$("#new-project-name-error").show().delay("20000").hide("slow");
			}
		});
	},
	close: function(){
		$(this.el).modal("hide");
		this.remove();
	}
});
module.exports = CreateProjectView;
