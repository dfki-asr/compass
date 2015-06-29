/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var CompassCollection = require("./compass-collection");
var Scenario = require("../model/scenario");
var Config = require("../config");

var ScenarioCollection = CompassCollection.extend({
	model: Scenario,
	selectedScenario: undefined,
	url: Config.getRESTPath("scenarios/"),
	selectById: function (id) {
		var newSelection = this.get(id);
		if (newSelection === this.selectedScenario) {
			return;
		}
		this.selectedScenario = newSelection;
	},
	isSelected: function (model) {
		return this.selectedScenario && this.selectedScenario === model;
	},
	getSelected: function () {
		return this.selectedScenario;
	}
});

module.exports = ScenarioCollection;
