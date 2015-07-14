/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var CompassModel = require("./compass-model");
var Scenenode = require("./scenenode");
var Config = require("../config");

var ScenenodeComponent = CompassModel.extend({
	props: {
		id: "number",
		owner: Scenenode,
		type: {
			type: "string",
			required: true,
			default: "de.dfki.asr.compass.model.components.SceneNodeComponent"
		}
	},
	session: {
		hierarchyIcon: {
			type: "string",
			required: false,
			default: ""
		}
	},
	extraProperties: "allow",
	urlRoot: Config.getRESTPath("scenenodecomponents/")
});

module.exports = ScenenodeComponent;

