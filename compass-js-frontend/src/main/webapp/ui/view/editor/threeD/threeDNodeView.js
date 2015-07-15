/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var AmpersandView = require("ampersand-view");

var ThreeDNodeView = AmpersandView.extend({
    template: "<group></group>",
	initialize: function (sceneNode, $rootView) {
		this.sceneNode = sceneNode;
		this.$rootView = $rootView;
    },
    render: function () {
        this.renderWithTemplate();
		var renderGeometry = this.sceneNode.components.getComponentByType("de.dfki.asr.compass.model.components.RenderGeometry");
		if (renderGeometry.length) {
			this.renderRenderGeometry(renderGeometry);
		}
		this.renderChildSceneNode();
        return this;
    },
	renderRenderGeometry: function (renderGeometries) {
		for (var index in renderGeometries) {
			var rg = renderGeometries[index];
			console.log("render renderGeometry with geometry url: " + rg.meshSource);
		}
	},
	renderChildSceneNode: function () {
		var self = this;
		this.sceneNode.children.each(function (child) {
			var sceneNodeView = new ThreeDNodeView(child, self.rootView);
			self.registerSubView(sceneNodeView);
			self.renderSubview(sceneNodeView, self.el);
		});
	}
});

module.exports = ThreeDNodeView;
