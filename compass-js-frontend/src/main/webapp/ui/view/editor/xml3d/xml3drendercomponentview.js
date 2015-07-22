/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var AmpersandView = require("ampersand-view");

var RenderGeometryView = AmpersandView.extend({
    template: "<model></model>",
	initialize: function () {
    },
	bindings: {
		"model.meshSource": {
			type: "attribute",
			name: "src"
		}
	},
    render: function () {
        this.renderWithTemplate();
    }
});

module.exports = RenderGeometryView;
