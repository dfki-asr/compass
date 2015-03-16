/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
XML3D.tools.namespace("COMPASS.util");

(function(){

	"use strict";

	COMPASS.util.sceneNodeRotationToString = function(rotation) {
		var vector = new XML3DVec3(rotation.x, rotation.y, rotation.z);
		var xml3dRotation = new XML3DRotation();
		xml3dRotation.setQuaternion(vector, rotation.w);
		return xml3dRotation.str();
	};

	COMPASS.util.sceneNodeVectorToString = function(vector) {
		var xml3dVector = new XML3DVec3(vector.x, vector.y, vector.z);
		return xml3dVector.str();
	};

	COMPASS.util.sceneNodeScalarToVectorString = function(scale) {
		var xml3dVector = new XML3DVec3(scale, scale, scale);
		return xml3dVector.str();
	};

	COMPASS.util.focusElement = function(elementId) {
		$(document.getElementById(elementId)).focus();
	};
}());
