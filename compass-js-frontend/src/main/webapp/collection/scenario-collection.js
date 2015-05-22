/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var AmpersandRestCollection = require("ampersand-rest-collection");
var Scenario = require("./../model/scenario");
var app = require("ampersand-app");

var ScenarioCollection = AmpersandRestCollection.extend({
	model: Scenario,
	ajaxConfig: {
		headers: {
			"Accept": "application/json"
		}
	},
	url: "http://localhost:8080/compass/resources/restv1/scenarios/",
	selectById: function(id){
		this.each(function(p){
			p.selected = false;
		});
		this.get(id).selected = true;
	}
});

module.exports = ScenarioCollection;
