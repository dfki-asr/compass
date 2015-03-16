/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
XML3D.tools.namespace("COMPASS");

(function() {
	"use strict";
	var toDegrees = 180 / Math.PI;
	var toRadians = Math.PI / 180;

	COMPASS.EulerRotation = new XML3D.tools.Class({
		set yaw(value) {
			this.yaw = value % 360;
		},
		set pitch(value) {
			this.pitch = value % 360;
		},
		set roll(value) {
			this.roll = value % 360;
		},
		initialize: function(yaw, pitch, roll) {
			//Note: These values are in DEGREES
			this.yaw = yaw || 0;
			this.pitch = pitch || 0;
			this.roll = roll || 0;
		},
		fromQuaternion: function(quat) {
			var x = quat._data[0],
					y = quat._data[1],
					z = quat._data[2],
					w = quat._data[3];
			if (!w || !(x || y || z)) {
				//Identity quaternion can produce different results for 0 0 1 0 vs 0 0 0 1, but both should result in 0 rotation
				this.pitch = this.yaw = this.roll = 0;
				return this;
			}
			var test = x * y + z * w;
			if (test > 0.499) { // singularity at north pole
				this.yaw = 2 * Math.atan2(x, w) * toDegrees;
				this.pitch = 0;
				this.roll = Math.PI / 2 * toDegrees;
				return this;
			}
			if (test < -0.499) { // singularity at south pole
				this.yaw = -2 * Math.atan2(x, w) * toDegrees;
				this.pitch = 0;
				this.roll = -Math.PI / 2 * toDegrees;
				return this;
			}
			var sqx = x * x;
			var sqy = y * y;
			var sqz = z * z;
			this.yaw = Math.atan2(2 * y * w - 2 * x * z, 1 - 2 * sqy - 2 * sqz) * toDegrees;
			this.pitch = Math.atan2(2 * x * w - 2 * y * z, 1 - 2 * sqx - 2 * sqz) * toDegrees;
			this.roll = Math.asin(2 * test) * toDegrees;
			return this;
		},
		toQuaternion: function() {
			var c1 = Math.cos(this.yaw * toRadians / 2),
					s1 = Math.sin(this.yaw * toRadians / 2),
					c2 = Math.cos(this.roll * toRadians / 2),
					s2 = Math.sin(this.roll * toRadians / 2),
					c3 = Math.cos(this.pitch * toRadians / 2),
					s3 = Math.sin(this.pitch * toRadians / 2),
					c1c2 = c1 * c2,
					s1s2 = s1 * s2;
			var quat = new XML3DRotation();
			quat.w = c1c2 * c3 - s1s2 * s3;
			quat.x = c1c2 * s3 + s1s2 * c3;
			quat.y = s1 * c2 * c3 + c1 * s2 * s3;
			quat.z = c1 * s2 * c3 - s1 * c2 * s3;
			quat.normalize();
			return quat;
		}
	});
})();
