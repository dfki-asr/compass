/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
"use strict";

window.$ = window.jQuery = require("jquery");
var ProjectCollection = require("./collection/project-collection");

$(window).load(function (event) {
	var pc = new ProjectCollection();
	pc.fetch({
		success: function (model, response, options) {
			model.forEach(function (project) {
				console.log(project.name);
			});
		},
		error: function (model, response, options) {
			console.log("Niederlage ");
		}
	});
});