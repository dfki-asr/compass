/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var $ = global.jQuery;
var AmpersandView = require("ampersand-view");
var XML3DGroupView = require("./xml3d/xml3dgroupview");
var template = require("../../templates/editor/viewport.html");

var XML3DView = AmpersandView.extend({
    template: template,
    initialize: function () {
		this.parent.on("sceneTreeLoaded", this.renderXML3DTree.bind(this));
		this.parent.on("change:selectedNode", this.updateSelectionDisplay.bind(this));
    },
    render: function () {
        this.renderWithTemplate();
        return this;
    },
	renderXML3DTree: function () {
		this.sceneNodeRoot = this.parent.root;
		var rootGroup = ($(this.el).find("#rootGroup"))[0];
		this.renderCollection(this.sceneNodeRoot.children, XML3DGroupView, rootGroup, {rootView: this});
	},
	updateSelectionDisplay: function () {
		console.log("3DView: update selected scene node ... ");
	}
});

module.exports = XML3DView;
