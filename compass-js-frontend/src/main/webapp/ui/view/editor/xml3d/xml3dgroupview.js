/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var AmpersandView = require("ampersand-view");
var template = require("../../../templates/editor/xml3d/xml3dgroup.html");
// var SceneNode = require("../../../../model/scenenode");
// var RenderGeometry = require("../../../../model/rendergeometry");

var XML3DGroupView = AmpersandView.extend({
    template: template,
	initialize: function () {
    },
    render: function () {
		console.log("rendering scenenode with id: " + this.model.id + " and name: " + this.model.name);
        this.renderWithTemplate();
		var renderGeometry = this.model.components.getComponentByType("de.dfki.asr.compass.model.components.RenderGeometry");
		if (renderGeometry.length) {
			this.renderRenderGeometry(renderGeometry);
		}
		this.renderCollection(this.model.children, XML3DGroupView, ".childGroups");
        return this;
    },
	renderRenderGeometry: function (renderGeometries) {
		for (var index in renderGeometries) {
			var rg = renderGeometries[index];
			console.log("render renderGeometry with geometry url: " + rg.meshSource);
		}
	}
});

module.exports = XML3DGroupView;
