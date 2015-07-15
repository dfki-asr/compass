/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var ScenenodeComponent = require("./scenenode-component");

var RenderGeometry = ScenenodeComponent.extend({
	props: {
		meshSource: {
			type: "string",
			required: true,
			default: ""
		},
		type: {
			type: "string",
			required: true,
			default: "de.dfki.asr.compass.model.components.RenderGeometry"
		}
	},
	initialize: function () {
		this.hierarchyIcon = "fa-cube";
	}
});

module.exports = RenderGeometry;

