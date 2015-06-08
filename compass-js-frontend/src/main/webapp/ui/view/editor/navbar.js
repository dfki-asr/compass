/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var $ = global.jQuery;
var app = require('ampersand-app');
var AmpersandView = require('ampersand-view');
var template = require('../../templates/editor/navbar.html');

var EditorNavbarView = AmpersandView.extend({
    template: template,
	scenario: undefined,
    initialize: function (options) {
    },
	bindings: {
		"scenario.name": "[data-hook=scenarioname]"
	},
    render: function() {
        this.renderWithTemplate();
        return this;
    },
	setScenario: function(scenario){
		this.scenario = scenario;
		this.render();
	}
});

module.exports = EditorNavbarView;