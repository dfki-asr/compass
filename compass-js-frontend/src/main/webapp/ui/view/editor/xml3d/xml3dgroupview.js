/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var XML3D = global.XML3D;
var AmpersandView = require("ampersand-view");
var template = require("../../../templates/editor/xml3d/xml3dgroup.html");
var XML3DComponentViewFactory = require("./xml3dcomponentviewfactory");

var XML3DGroupView = AmpersandView.extend({
    template: template,
	derived: {
		xml3dCSSTransformString: {
			deps: ["model.localTranslation", "model.localScale", "model.localRotation"],
			fn: function () {
				var translate3dString = this.generateTranslate3dString();
				var rotate3dString = this.generateRotate3dString();
				var scale3dString = this.generateScale3dString();
				return "transform: " + translate3dString + " " + rotate3dString + " " + scale3dString;
			}
		}
	},
	bindings: {
		xml3dCSSTransformString: {
			type: "attribute",
			name: "style"
		}
	},
	initialize: function () {
    },
    render: function () {
        this.renderWithTemplate();
		this.renderCollection(this.model.components, XML3DComponentViewFactory, ".componentGroups");
		this.renderCollection(this.model.children, XML3DGroupView, ".childGroups");
        return this;
    },
	generateTranslate3dString: function () {
		var translation = this.model.localTranslation;
		return "translate3d(" + translation.x + "px, " + translation.y + "px, " + translation.z + "px)";
	},
	generateRotate3dString: function () {
		var rotation = this.model.localRotation;
		var quatarnion = [rotation.x, rotation.y, rotation.z, rotation.w];
		var axisAngle = XML3D.AxisAngle.fromQuat(quatarnion);
		var axis = axisAngle.axis;
		return "rotate3d(" + axis.x + ", " + axis.y + ", " + axis.z + ", " + axisAngle.angle  + "rad)";
	},
	generateScale3dString: function () {
		var scale = this.model.localScale;
		return "scale3d(" + scale + ", " + scale + ", " + scale + ")";
	}
});

module.exports = XML3DGroupView;
