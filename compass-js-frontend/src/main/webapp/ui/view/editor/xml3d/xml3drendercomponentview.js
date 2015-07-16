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
    render: function () {
		console.log("render renderGeometry with geometry url: " + this.model.meshSource);
        this.renderWithTemplate();
    }
});

module.exports = RenderGeometryView;
