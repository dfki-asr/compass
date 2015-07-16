/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var AmpersandView = require("ampersand-view");
var template = require("../../templates/editor/navbar.html");

var EditorNavbarView = AmpersandView.extend({
    template: template,
	scenario: undefined,
	events: {
		"click [data-hook=action-toggle-light]": "toggleDefaultLight"
	},
    initialize: function () {
    },
	bindings: {
		"scenario.name": "[data-hook=scenarioname]"
	},
    render: function () {
        this.renderWithTemplate();
        return this;
    },
	setScenario: function (scenario) {
		this.scenario = scenario;
		this.render();
	},
	toggleDefaultLight: function () {
		console.log("Hey there, I\'m toggling the default light!");
	setButtonCSS: function ($button, isActive) {
		if (isActive) {
			$button.addClass("active");
		} else {
			$button.removeClass("active");
		}
		$button.blur();
	}
});

module.exports = EditorNavbarView;
