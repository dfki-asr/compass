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
	ajaxConfig: {
		headers: {
			"Accept": "application/json"
		}
	},
	url: "http://localhost:8080/compass/resources/restv1/projects/",
	selectById: function(id){
		this.each(function(p){
			p.selected = false;
		});
		this.get(id).selected = true;
	}
});

module.exports = exports = ProjectCollection;
