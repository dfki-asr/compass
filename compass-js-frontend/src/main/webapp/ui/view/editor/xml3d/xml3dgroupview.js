/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var AmpersandView = require("ampersand-view");
var template = require("../../../templates/editor/xml3d/xml3dgroup.html");
var XML3DComponentViewFactory = require("./xml3dcomponentviewfactory");

var XML3DGroupView = AmpersandView.extend({
    template: template,
	initialize: function () {
    },
    render: function () {
        this.renderWithTemplate();
		this.renderCollection(this.model.components, XML3DComponentViewFactory, ".componentGroups");
		this.renderCollection(this.model.children, XML3DGroupView, ".childGroups");
        return this;
    }
});

module.exports = XML3DGroupView;
