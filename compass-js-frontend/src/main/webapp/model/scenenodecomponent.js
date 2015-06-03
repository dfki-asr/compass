/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var AmpersandModel = require("ampersand-model");
var SceneNode = require("scenenode");
var Config = require('../config');

var SceneNodeComponent = AmpersandModel.extend({
	props: {
		id: "number",
		owner: "number"
	},
	ajaxConfig: {
		headers: {
			"Accept": "application/json"
		}
	},
	extraProperties: 'ignore',
	urlRoot: Config.getRESTPath("scenenodecomponents/")
});

module.exports = SceneNodeComponent;
