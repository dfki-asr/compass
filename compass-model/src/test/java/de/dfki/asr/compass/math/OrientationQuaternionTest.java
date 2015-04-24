/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.math;

import static de.dfki.asr.compass.test.matcher.Quat4fSimilarity.similarTo;
import static org.hamcrest.MatcherAssert.assertThat;
import org.testng.annotations.Test;

@SuppressWarnings("PMD.ExcessivePublicCount")
public class OrientationQuaternionTest {

	private static final float EQUALS_DELTA = 0.0001f;

	// Example Quaternions taken from:
	// http://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToQuaternion/steps/index.htm
	// Basically, all cases of SO(3), i.e. of a Cube unto itself.

	@Test
	public void yaw0Pitch0Roll0() {
		sameQuaternion(new Quat4f(0, 0, 0, 1), yawPitchRoll(0, 0, 0));
	}

	@Test
	public void yaw90Pitch0Roll0() {
		sameQuaternion(new Quat4f(0, 0.7071f, 0, 0.7071f), yawPitchRoll(90, 0, 0));
	}

	@Test
	public void yaw180Pitch0Roll0() {
		sameQuaternion(new Quat4f(0, 1, 0, 0), yawPitchRoll(180, 0, 0));
	}

	@Test
	public void yawMinus90Pitch0Roll0() {
		sameQuaternion(new Quat4f(0, -0.7071f, 0, 0.7071f), yawPitchRoll(-90, 0, 0));
	}

	@Test
	public void yaw0Pitch90Roll0() {
		sameQuaternion(new Quat4f(0, 0, 0.7071f, 0.7071f), yawPitchRoll(0, 90, 0));
	}

	@Test
	public void yaw90Pitch90Roll0() {
		sameQuaternion(new Quat4f(0.5f, 0.5f, 0.5f, 0.5f), yawPitchRoll(90, 90, 0));
	}

	@Test
	public void yaw180Pitch90Roll0() {
		sameQuaternion(new Quat4f(0.7071f, 0.7071f, 0, 0), yawPitchRoll(180, 90, 0));
	}

	@Test
	public void yawMinus90Pitch90Roll0() {
		sameQuaternion(new Quat4f(-0.5f, -0.5f, 0.5f, 0.5f), yawPitchRoll(-90, 90, 0));
	}


	@Test
	public void yaw0PitchMinus90Roll0() {
		sameQuaternion(new Quat4f(0, 0, -0.7071f, 0.7071f), yawPitchRoll(0, -90, 0));
	}

	@Test
	public void yaw90PitchMinus90Roll0() {
		sameQuaternion(new Quat4f(-0.5f, 0.5f, -0.5f, 0.5f), yawPitchRoll(90, -90, 0));
	}

	@Test
	public void yaw180PitchMinus90Roll0() {
		sameQuaternion(new Quat4f(-0.7071f, 0.7071f, 0, 0), yawPitchRoll(180, -90, 0));
	}

	@Test
	public void yawMinus90PitchMinus90Roll0() {
		sameQuaternion(new Quat4f(0.5f, -0.5f, -0.5f, 0.5f), yawPitchRoll(-90, -90, 0));
	}

	@Test
	public void yaw0Pitch0Roll90() {
		sameQuaternion(new Quat4f(0.7071f, 0, 0, 0.7071f), yawPitchRoll(0, 0, 90));
	}

	@Test
	public void yaw90Pitch0Roll90() {
		sameQuaternion(new Quat4f(0.5f, 0.5f, -0.5f, 0.5f), yawPitchRoll(90, 0, 90));
	}

	@Test
	public void yaw180Pitch0Roll90() {
		sameQuaternion(new Quat4f(0, 0.7071f, -0.7071f, 0), yawPitchRoll(180, 0, 90));
	}

	@Test
	public void yawMinus90Pitch0Roll90() {
		sameQuaternion(new Quat4f(0.5f, -0.5f, 0.5f, 0.5f), yawPitchRoll(-90, 0, 90));
	}

	@Test
	public void yaw0Pitch0Roll180() {
		sameQuaternion(new Quat4f(1, 0, 0, 0), yawPitchRoll(0, 0, 180));
	}

	@Test
	public void yaw90Pitch0Roll180() {
		sameQuaternion(new Quat4f(0.7071f, 0, -0.7071f, 0), yawPitchRoll(90, 0, 180));
	}

	@Test
	public void yaw180Pitch0Roll180() {
		// And this example contains a typo on the source page.
		sameQuaternion(new Quat4f(0, 0, -1, 0), yawPitchRoll(180, 0, 180));
	}

	@Test
	public void yawMinus90Pitch0Roll180() {
		sameQuaternion(new Quat4f(0.7071f, 0, 0.7071f, 0), yawPitchRoll(-90, 0, 180));
	}

	@Test
	public void yaw0Pitch0RollMinus90() {
		sameQuaternion(new Quat4f(-0.7071f, 0, 0, 0.7071f), yawPitchRoll(0, 0, -90));
	}

	@Test
	public void yaw90Pitch0RollMinus90() {
		sameQuaternion(new Quat4f(-0.5f, 0.5f, 0.5f, 0.5f), yawPitchRoll(90, 0, -90));
	}

	@Test
	public void yaw180Pitch0RollMinus90() {
		sameQuaternion(new Quat4f(0, 0.7071f, 0.7071f, 0), yawPitchRoll(180, 0, -90));
	}

	@Test
	public void yawMinus90Pitch0RollMinus90() {
		sameQuaternion(new Quat4f(-0.5f, -0.5f, -0.5f, 0.5f), yawPitchRoll(-90, 0, -90));
	}

	// not part of SO(3)
	@Test(enabled = false)
	public void yaw0Pitch90Roll90() {
		// by wolfram alpha
		sameQuaternion(new Quat4f(0.5f, 0.5f, 0.5f, -0.5f), yawPitchRoll(0, 90, 90));
	}

	@Test(enabled = false)
	public void yaw0Pitch180Roll0() {
		// pitch 180 = half-turn around the X-axis
		// by sheer brain power (and a lttle bit of help from alpha)
		// (http://www.wolframalpha.com/input/?i=draw+0%2B1i%2B0j%2B0k+as+a+rotation+operator)
		sameQuaternion(new Quat4f(0f, 1f, 0f, 0f), yawPitchRoll(0, 180, 0));
	}

	private Orientation yawPitchRoll(final double yaw, final double pitch, final double roll) {
		Orientation o = new Orientation();
		o.setLocalYaw(yaw);
		o.setLocalPitch(pitch);
		o.setLocalRoll(roll);
		return o;
	}

	private void sameQuaternion(final Quat4f quaternion, final Orientation orientation) {
		assertThat("Quaternion", orientation.getLocalRotation(), similarTo(quaternion, EQUALS_DELTA));
	}
}
