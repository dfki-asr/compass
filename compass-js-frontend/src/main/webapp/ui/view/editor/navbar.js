/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var AmpersandView = require("ampersand-view");
var template = require("../../templates/editor/navbar.html");
var DefaultLightButtonView = require("./navbar/defaultlight");

var EditorNavbarView = AmpersandView.extend({
	template: template,
	subviews: {
		defaultLightButton: {
			hook: "defaultLightButton",
			constructor: DefaultLightButtonView
		}
	},
	session: {
		parent: "state"
	},
	bindings: {
		"parent.scenario.name": {
			hook: "scenarioname"
		}
	}
});

module.exports = EditorNavbarView;
