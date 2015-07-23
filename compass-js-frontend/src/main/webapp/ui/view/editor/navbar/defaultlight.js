/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var AmpersandView = require("ampersand-view");
var template = require("../../../templates/editor/navbar/defaultlight.html");

var DefaultLightButtonView = AmpersandView.extend({
	template: template,
	events: {
		"click [data-hook=action-toggle-light]": "toggleDefaultLight"
	},
	session: {
		parent: "state"
	},
	bindings: {
		"parent.parent.defaultLight": {
			type: "booleanClass",
			hook: "action-toggle-light",
			name: "active"
		}
	},
	toggleDefaultLight: function () {
		this.parent.parent.defaultLight = !this.parent.parent.defaultLight;
		this.queryByHook("action-toggle-light").blur();
	}
});

module.exports = DefaultLightButtonView;
