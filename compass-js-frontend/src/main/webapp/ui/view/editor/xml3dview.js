/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var AmpersandView = require("ampersand-view");
var XML3DGroupView = require("./xml3d/xml3dgroupview");
var XML3DDefaultLight = require("./xml3d/xml3ddefaultlight");
var template = require("../../templates/editor/viewport.html");

var XML3DView = AmpersandView.extend({
    template: template,
	defaultLight: undefined,
    initialize: function () {
		this.parent.on("sceneTreeLoaded", this.renderXML3DTree.bind(this));
		this.parent.on("change:selectedNode", this.updateSelectionDisplay.bind(this));
    },
    render: function () {
        this.renderWithTemplate();
		this.defaultLight = new XML3DDefaultLight();
		this.renderSubview(this.defaultLight, "#defaultView");
        return this;
    },
	renderXML3DTree: function () {
		this.renderCollection(this.parent.root.children, XML3DGroupView, ".rootChildren");
	},
	updateSelectionDisplay: function () {
		console.log("3DView: update selected scene node ... ");
	}
});

module.exports = XML3DView;
