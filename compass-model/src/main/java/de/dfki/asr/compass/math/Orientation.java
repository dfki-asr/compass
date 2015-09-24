/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.math;

import java.io.Serializable;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class Orientation implements Serializable {

	private static final long serialVersionUID = -4088414392115280605L;

	private static final double TO_RADIANS = Math.PI / 180;
	private static final double TO_DEGREES = 180 / Math.PI;
	private static final double NORTHPOLE_SINGULARITY = 0.499;
	private static final double SOUTHPOLE_SINGULARITY = -1 * NORTHPOLE_SINGULARITY;

	//private static final double POLE_DELTA = 0.00001f;
	//Angles are stored in DEGREES
	private transient double localYaw;

	private transient double localPitch;

	private transient double localRoll;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "w", column = @Column(name = "rotate_w")),
		@AttributeOverride(name = "x", column = @Column(name = "rotate_x")),
		@AttributeOverride(name = "y", column = @Column(name = "rotate_y")),
		@AttributeOverride(name = "z", column = @Column(name = "rotate_z"))
	})
	private Quat4f localRotation;

	public Orientation() {
		localYaw = 0;
		localPitch = 0;
		localRoll = 0;
		localRotation = new Quat4f();
	}

	public double getLocalYaw() {
		updateEulerAnglesFromRotation();
		return localYaw;
	}

	public void setLocalYaw(final double localYaw) {
		this.localYaw = localYaw % 360;
		updateRotationFromEulerAngles();
	}

	public double getLocalPitch() {
		updateEulerAnglesFromRotation();
		return localPitch;
	}

	public void setLocalPitch(final double localPitch) {
		this.localPitch = localPitch % 360;
		updateRotationFromEulerAngles();
	}

	public double getLocalRoll() {
		updateEulerAnglesFromRotation();
		return localRoll;
	}

	public void setLocalRoll(final double localRoll) {
		this.localRoll = localRoll % 360;
		updateRotationFromEulerAngles();
	}

	public Quat4f getLocalRotation() {
		updateEulerAnglesFromRotation();
		return localRotation;
	}

	public void setLocalRotation(final Quat4f rotation) {
		this.localRotation = rotation;
		updateEulerAnglesFromRotation();
	}

	private void updateRotationFromEulerAngles() {
		double c1 = Math.cos(localYaw * TO_RADIANS / 2);
		double s1 = Math.sin(localYaw * TO_RADIANS / 2);
		double c2 = Math.cos(localPitch * TO_RADIANS / 2);
		double s2 = Math.sin(localPitch * TO_RADIANS / 2);
		double c3 = Math.cos(localRoll * TO_RADIANS / 2);
		double s3 = Math.sin(localRoll * TO_RADIANS / 2);
		double c1c2 = c1 * c2;
		double s1s2 = s1 * s2;
		localRotation.w = (float) (c1c2 * c3 - s1s2 * s3);
		localRotation.x = (float) (c1c2 * s3 + s1s2 * c3);
		localRotation.y = (float) (s1 * c2 * c3 + c1 * s2 * s3);
		localRotation.z = (float) (c1 * s2 * c3 - s1 * c2 * s3);
		localRotation.normalize();

	}

	private void updateEulerAnglesFromRotation() {
		double x = localRotation.x;
		double y = localRotation.y;
		double z = localRotation.z;
		double w = localRotation.w;
		double test = x * y + z * w;
		if (test > NORTHPOLE_SINGULARITY) {
			updateEulerAnglesAtNorthPoleSingularity();
			return;
		}
		if (test < SOUTHPOLE_SINGULARITY) {
			updateEulerAnglesAtSouthPoleSingularity();
			return;
		}
		double sqx = x * x;
		double sqy = y * y;
		double sqz = z * z;
		localYaw = Math.atan2(2 * y * w - 2 * x * z, 1 - 2 * sqy - 2 * sqz) * TO_DEGREES;
		localPitch = Math.asin(2 * test) * TO_DEGREES;
		localRoll = Math.atan2(2 * x * w - 2 * y * z, 1 - 2 * sqx - 2 * sqz) * TO_DEGREES;
	}

	private void updateEulerAnglesAtNorthPoleSingularity() {
		double x = localRotation.x;
		double w = localRotation.w;
		localYaw = 2 * Math.atan2(x, w) * TO_DEGREES;
		localRoll = 0;
		localPitch = Math.PI / 2 * TO_DEGREES;
	}

	private void updateEulerAnglesAtSouthPoleSingularity() {
		double x = localRotation.x;
		double w = localRotation.w;
		localYaw = -2 * Math.atan2(x, w) * TO_DEGREES;
		localRoll = 0;
		localPitch = -Math.PI / 2 * TO_DEGREES;
	}
}
