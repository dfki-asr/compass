/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var CompassModel = require("./compass-model");
var ScenarioCollection = require("../collection/scenario-collection");
var Config = require('../config');

var Project = CompassModel.extend({
	props: {
		id: "number",
		name: {
			type: "string",
			required: true,
			default: "Default Name"
		}
	},
	collections:{
		scenarios: ScenarioCollection
	},
	parse: function(attrs){
		if(!attrs){
			return attrs;
		}
		//if the ids are just a plain array instead of being wrapped up as an array of {id: id} objects
		//ampersand will not recognize them as the scenario ids
		//as a result we need to wrap them properly before ampersand parses the attributes
		if(attrs.scenarios){
			var scenarios = attrs.scenarios;
			for(var index in scenarios){
				var id = scenarios[index];
				scenarios[index] = {id: id};
			}
		} else {
			attrs.scenarios = new ScenarioCollection([]);
		}
		return attrs;
	},
	urlRoot: Config.getRESTPath("projects/")
});

module.exports = Project;
