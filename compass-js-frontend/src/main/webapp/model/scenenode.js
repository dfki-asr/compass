/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var AmpersandModel = require("ampersand-model");
var SceneNodeCollection = require("../collection/scenenode-collection");
var Config = require('../config');

var SceneNode = AmpersandModel.extend({
	props: {
		id: "number",
		name: {
			type: "string",
			required: true,
			default: "Scene Node"
		},
		parent: "number",
		selectable3d: "boolean",
		visible: "boolean"
	},
	collections:{
		children: SceneNodeCollection,
		components: "number"
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