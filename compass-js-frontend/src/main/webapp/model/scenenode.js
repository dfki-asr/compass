/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var AmpersandModel = require("ampersand-model");
var ScenarioCollection = require("../collection/scenario-collection");
var Config = require('../config');

var SceneNode = AmpersandModel.extend({
	props: {
		id: "number",
		name: {
			type: "string",
			required: true,
			default: "Default Name"
		},
		parent: "number"
	},
	collections:{
		children: SceneNodeCollection
	},
	ajaxConfig: {
		headers: {
			"Accept": "application/json"
		}
	},
	extraProperties: 'ignore',
	urlRoot: Config.getRESTPath("scenenodes/")
});

module.exports = SceneNode;