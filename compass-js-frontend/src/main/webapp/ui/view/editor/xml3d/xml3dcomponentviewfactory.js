/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var RenderGeometry = require("../../../../model/rendergeometry");
var RenderGeometryView = require("./xml3drendercomponentview");

var XML3DComponentViewFactory = function (options) {
	var component = options.model;
	if (component instanceof RenderGeometry) {
		return new RenderGeometryView(options);
	}
};

module.exports = XML3DComponentViewFactory;
