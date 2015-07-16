/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var AmpersandView = require("ampersand-view");
var template = require("../../../templates/editor/xml3d/default-light.html");

var XML3DDefaultLight = AmpersandView.extend({
	template: template,
	enabled: false,
	initialize: function () {
	},
	toggleVisibility: function () {
		this.enabled = !this.enabled;
		this.el.setAttribute("visible", this.enabled);
	},
    render: function () {
        this.renderWithTemplate();
        return this;
    }
});

module.exports = XML3DDefaultLight;
