/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var app = require('ampersand-app');
var template = require('../templates/basepage.html');
var AmpersandView = require('ampersand-view');

//this is the BasePage - all other pages extend from it
var BasePage = AmpersandView.extend({
	pageTitle: 'Base Page',
	template: template,
	initialize: function (options) {
	},
	render: function () {
		this.renderWithTemplate();
		return this;
	},
	init: function () {
	}
});

module.exports = BasePage;
